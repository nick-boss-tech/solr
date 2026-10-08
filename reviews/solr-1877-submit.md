# solr-1877-submit

- Branch: origin/solr-1877-submit
- Head: 0d5916186797 (matches the listed head; fork tip unchanged after fetch 2026-10-08)
- Base: upstream/main at 9d7cc2884e8a (merge-base c3cdf7b46e8c, 26 commits behind, 4 commits ahead)
- Scope: 4 commits, 6 files (+95/-2). `IndexBasedSpellChecker.java` (`initSourceReader` closes the previous reader and directory; new `close()`), `AbstractLuceneSpellChecker.java` (`close()`), `SolrSpellChecker.java` (`implements Closeable`, no-op `close()`), `SpellCheckComponent.java` (core close hook, added once at the end of `inform`), `IndexBasedSpellCheckerTest.java` (`testSourceReaderClosedOnReloadAndClose`, +38), changelog `SOLR-1877-close-spellcheckers.yml` (`type: fixed`). No `SOLR-1877-TESTING.md` on the tip; a hypothetical-reproduction handoff doc was added in `15f5e04baab` and removed in `0d591618679`.
- Verdict: Needs work (one High race verified by reading the call sites; the fix is a design choice for the author)
- Reviewer: claude-haiku-5-5 (Claude Code), round 36 Group B review, 2026-10-08

Nothing here was compiled, formatted, or run. No Gradle, no `drain`, no test runs. Every claim rests on reading the diff and the code at the listed head.

## Delta check against the bulk review

Bulk review `research/branch-reviews/round-28/SOLR-1877-review.md` (verdict Needs work) was written at the same head (0d5916186797). No delta.

- Bulk F1 (High, reader replaced while a build uses it): **confirmed**, with the call-site evidence and failure mode below (finding 1).

## Findings (ranked)

1. **HIGH, verified by reading the call sites. The source reader can be closed while `build()` is indexing from it.**
   - `initSourceReader()` installs the new reader and then closes the previous one immediately (`IndexBasedSpellChecker.java`, `initSourceReader`, the two `closeQuietly(previous...)` calls).
   - `build()` reads `reader = this.reader` with no reference count, and passes it to `HighFrequencyDictionary` (`IndexBasedSpellChecker.java`, `build`). Neither `reader` nor the methods are `synchronized` or `volatile`; grep of the three spelling files finds no lock.
   - Two real threads reach these paths. Per-request `spellcheck.reload=true` calls `reload` on the request thread (`SpellCheckComponent.java:124`). Commit-driven indexing calls `buildSpellIndex` → `build` from the searcher listener (`SpellCheckComponent.java:843`, with `buildOnCommit`/`buildOnOptimize`). The firstSearcher `reload` (`SpellCheckComponent.java:813`) is a third path.
   - Failure: request A (`spellcheck.reload=true`) installs R2 and closes R1 while the newSearcher build is iterating R1. The build throws (AlreadyClosed-type). `build()` has already called `spellChecker.clearIndex()` before `indexDictionary(...)`, so the spell index is left cleared until the next successful build. The exception is logged by `buildSpellIndex`, so the failure is quiet.
   - Base code never closed the previous reader, so this race is introduced by the branch, not inherited.
   - The test `testSourceReaderClosedOnReloadAndClose` checks refcounts across a sequential reload and close. It does not overlap a reload with a build, as the bulk review says.
   - Proposed fix (not applied; the author or owner picks one): (a) ref-count the source reader and close the old one when the last user releases it, or (b) serialize `reload`, `build`, and `close` on the same lock. Either way, add a test that holds a build open while a reload runs. Mark `reader` volatile if the handoff stays unlocked.

2. **LOW, hypothesis. `SolrSpellChecker` now implements `Closeable` with a no-op `close()`.** Source-compatible for existing subclasses that do not define `close()`. A plugin subclass that already declares `close()` with an incompatible signature would stop compiling. The bulk review's "confirm custom implementations remain compatible" is the right check; not checked against any third-party plugin.

## Checked and not a finding

- The core close hook (`SpellCheckComponent.java`, end of `inform`) closes the map entries once per core. It is not inside the per-dictionary loop.
- `ConjunctionSolrSpellChecker` has no `close()` override. It is built per request in `getSpellChecker` (`SpellCheckComponent.java:594`) and wraps checkers that already live in `spellCheckers`. Those entries are closed by the hook, so no conjunction leak was found.

## Proposed fixes (not applied; the owner decides)

- Finding 1: ref-count or lock, plus an overlap test (see finding 1).
- Finding 2: no change unless a plugin compatibility check shows a break.

## Owner calls (not decided here)

None on this branch. The choice between ref-counting and locking in finding 1 is an implementation choice the author can make; flagged here, not decided.

## Not checked

- Not compiled, formatted, or run. No Gradle, no `drain`, no test runs.
- The local test-queue report (no result, per the bulk review) was not re-read.
- The behavior of Lucene `SpellChecker.close()` on a closed index writer, and whether the failure in finding 1 is an `AlreadyClosedException` in practice, were not run.
- `determineReader` callers (used for the source reader outside `build`) were not traced.
- No GitHub or JIRA writes were made.
