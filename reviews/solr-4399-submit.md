# solr-4399-submit

- Branch: origin/solr-4399-submit
- Head: de6cc6b27edd (matches the listed head; fork tip unchanged after fetch 2026-10-08)
- Base: upstream/main at 9d7cc2884e8a (merge-base 97d973814336, 19 commits behind, 2 commits ahead)
- Scope: 2 commits, 3 files. `FileBasedSpellChecker.java` (+10, a `sourceLocation == null` check in `init`, `:63`), `FileBasedSpellCheckerTest.java` (+14, `testMissingSourceLocationIsRejected`), changelog `SOLR-4399-filebased-spellchecker-location.yml` (`type: fixed`). No `SOLR-4399-TESTING.md` on the tip.
- Verdict: Needs work (the guard fails startup for a configuration that base starts fine and that never builds)
- Reviewer: claude-haiku-5-5 (Claude Code), round 36 Group B review, 2026-10-08

Nothing here was compiled, formatted, or run. No Gradle, no `drain`, no test runs. Every claim rests on reading the diff and the code at the listed head.

## Delta check against the bulk review

Bulk review `research/branch-reviews/round-28/SOLR-4399-review.md` (verdict Needs work) was written at the same head (de6cc6b27edd). No delta.

- Bulk F1 (the guard rejects configs that never request a build): **confirmed**, with the code path below (finding 1).

## Findings (ranked)

1. **MEDIUM, verified by reading. The new `init` guard breaks an index-only configuration that base starts.**
   - Base `FileBasedSpellChecker.init` never reads `sourceLocation`. `AbstractLuceneSpellChecker.init` assigns it (`:89`, `config.get(LOCATION)`). The field is used only on the external-dictionary path: `FileBasedSpellChecker.java` `:115` (`getLines`), `:131` (`PlainTextDictionary`), and `:136` (`openResource`), all inside build.
   - `AbstractLuceneSpellChecker.initIndex` opens the configured `spellcheckIndexDir` and documents that it "does not actually create the spelling index" (`:222-240`). So a prebuilt index can be queried with no source file at all.
   - The branch adds `if (sourceLocation == null) throw ...` to `init` (`FileBasedSpellChecker.java`, the hunk at `@@ -59,6 +60,15 @@`). A config with a prebuilt index and no `sourceLocation` now fails at core load. Base starts it and fails only on `spellcheck.build`. That is a startup regression for a config the failure report did not describe.
   - Shipped configs are fine: every `FileBasedSpellChecker` in `solr/core/src/test-files/.../solrconfig*.xml` and in `FileBasedSpellCheckerTest` sets `sourceLocation`. No in-tree break was found. The regression applies to deployments outside the tree.
   - Proposed fix (not applied): remove the `init` guard and throw the same `SolrException` at the top of the build path (before `:115`), naming the `sourceLocation` parameter. Add two tests: init with no `sourceLocation` succeeds, and `build` with none fails with the named message. Then the changelog sentence "now fails at startup" becomes "now fails at build with a message naming the parameter".

2. **LOW, verified. The changelog describes the startup failure as the fix.** `changelog/unreleased/SOLR-4399-filebased-spellchecker-location.yml:8` says the config "now fails at startup". That matches the head code, but not the ticket's reported failure (an NPE on `spellcheck.build`). If finding 1 is adopted, the changelog changes with it.

## Owner calls (not decided here)

1. **Is an index-only `FileBasedSpellChecker` (prebuilt index, no `sourceLocation`) a supported configuration?** If yes, the guard moves to the build path (finding 1). If no, the startup failure is intended, and the change is a compatibility break that must be stated in the changelog. The bulk review's "demonstrate that every supported file-based configuration must have a source location" is the check the owner has to answer.

## Checked and not a finding

- Shipped and test configs all set `sourceLocation` (grep over `solr/core/src/test-files` and `FileBasedSpellCheckerTest`), so the in-tree suite is not broken by the guard.
- The build path that the JIRA failure describes (`spellcheck.build` NPE) is real at base; the guard does fix the message, just at a different point.

## Proposed fixes (not applied; the owner decides)

- Finding 1: move the guard into the build path, add the two tests, and update the changelog (see finding 1 and finding 2).
- Owner call 1 decides whether any guard at all should remain at `init`.

## Interactions with other branches

- Pairs with SOLR-4367 in the bulk round's notes (both are spellchecker config diagnostics). The two branches touch different files (`FileBasedSpellChecker.java` here, `SpellCheckComponent.java` there). No shared code found.

## Not checked

- Not compiled, formatted, or run. No Gradle, no `drain`, no test runs.
- The JIRA ticket text was not re-read here; the failure description comes from the bulk review.
- Whether any deployment outside this tree uses an index-only config (no way to check from the repo).
- The `FileBasedSpellCheckerTest.test()` path with a prebuilt index was not run.
- No GitHub or JIRA writes were made.
