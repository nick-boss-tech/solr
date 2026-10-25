# SOLR-3479 PR 5126 highlight-query fix gate, round 1

Result: PASSED at final head `8a685825b6202f45eb981d3b8e7f05b01a5c1829`. The first fix head `23c159d06bd86180e0bbd4b474517b60b693f926` failed the Error Prone compile leg and is superseded; see the repair report `reports/pr-5126-highlight-query-fix-repair-round-1.md`.

Gate job: `gates/SOLR-3479-pr5126-highlight-query-23c159d06bd8.md` (fix head 23c159d06b; superseded for the final head by `gates/SOLR-3479-pr5126-highlight-query-8a685825b6.md`).
Claim: `claims/pr-5126-highlight-query-gate-vm4-round-1.md` (done record `claims/pr-5126-highlight-query-gate-vm4-round-1-done.md`).
Receipt: `receipts/SOLR-3479-pr5126-highlight-query-round-1.md`.
Comment draft (not posted, awaiting Nick's review): `reports/pr-5126-production-fix-reply-round-1.md`.

## Heads and diff

- Original PR 5126 head (pinned): `74b09fb443f060caac3f8ec9701909cec276cfb4`, by David Smiley. Base `bd2b2d5a02584893c5b5fe16c1a7eee89c2bb6c9`. Open, not draft when checked on 2026-10-25.
- Fork branch `nick-boss-tech/solr:solr-3479-submit`, final head `8a685825b6202f45eb981d3b8e7f05b01a5c1829`, read back by ls-remote. Author of both commits: Nick Shanin, no trailers.
- Commits on top of the PR head:
  - `23c159d06b`, "SOLR-3479: use the search parser highlight query for inline snippets" (production fix and tests).
  - `8a685825b6`, "SOLR-3479: suppress ReferenceEquality on the intentional identity check" (one annotation and one comment line in `HighlightAugmenterFactory.java`).
- Files changed in the tested delta: `solr/core/src/java/org/apache/solr/response/transform/HighlightAugmenterFactory.java` (+22 -1) and `solr/core/src/test/org/apache/solr/response/transform/TestHighlightAugmenterFactory.java` (+83 -0). No other file. `git diff 74b09fb..8a685825` shows no other path.
- Compare links:
  - Primary reviewer link (moving branch): `https://github.com/dsmiley/solr/compare/SOLR-3479-highlightDocTransformer...nick-boss-tech:solr-3479-submit`. API checked: base and merge base `74b09fb443f0`, ahead 2, behind 0, head `8a685825b6`, two files. It moves if the branch moves.
  - Immutable tested delta: `https://github.com/nick-boss-tech/solr/compare/74b09fb443f060caac3f8ec9701909cec276cfb4...8a685825b6202f45eb981d3b8e7f05b01a5c1829`. API checked: merge base `74b09fb443f0`, two commits, same two files.
- Patch of the tested delta: `evidence/incremental-74b09fb-to-8a685825b6.patch`.
- Reproduction branch `solr-3479-pr5126-query-examples-r1` at `022eb6d781` is unchanged.

## Environment

- Host `vm4`, Linux aarch64, JAVA_HOME `/workspace/tools/jdk21` (JDK 21.0.12.1). Gradle 9.7.0 via the repo wrapper. Gradle wrapper jar bootstrapped by `gradlew`, checksum matches `gradle-wrapper.jar.sha256`.
- Runner `pool/bin/sweep-run.sh` from `coordination-ui`, one build at a time under `/workspace/tools/.gradle-vm4.lock`, inode guard in place. Seed `34795126` on every test leg.
- Worktrees: `/workspace/worktrees/gate-5126-fail` (original production `74b09fb443f0` with only the fix's test file overlaid), `/workspace/worktrees/gate-5126-fix` (final head). Production hashes: `HighlightAugmenterFactory.java` is `ce20e120...` in the fail tree (original) and `3df19427...` at the fix commit `23c159d06b`; the repair commit changes it again.
- Free space on `/`: 4.6G and about 479,570 inodes free before and after each leg.
- Raw logs: `/workspace/gates/logs/vm4/pr-5126-fix/` on the VM. Durable copies: `reports/pr-5126-highlight-query-fix-gate-round-1-evidence/` (XML, run records, patch, compile excerpt).

## Preflight

- Detached `gate-preflight.sh` (tidy) at `23c159d06b`: tidy clean, log `/workspace/gates/logs/vm4/pr-5126-fix/preflight/preflight.log`.

## Legs

| Leg | Head | Command (task form) | UTC | Exit | Fresh XML |
| --- | --- | --- | --- | --- | --- |
| 1 fail before | original `74b09fb443f0` plus fix test file | `:solr:core:cleanTest :solr:core:test --tests ...TestHighlightAugmenterFactory.testImplicitQueryUsesHighlightQuery -Ptests.seed=34795126` | 17:56:49 to 18:02:51 | 1 (expected) | 1 test, 1 failure, 0 errors, 0 skipped |
| 2 pass after (final) | `8a685825b6` | `:solr:core:cleanTest :solr:core:test --tests ...TestHighlightAugmenterFactory` | 18:22:52 to 18:25:44 | 0 | 11 tests, 0 failures, 0 errors, 0 skipped |
| 3 neighbor (final) | `8a685825b6` | `:solr:core:cleanTest :solr:core:test --tests org.apache.solr.highlight.TestUnifiedSolrHighlighter` | 18:25:50 to 18:27:11 | 0 | 33 tests, 0 failures, 0 errors, 0 skipped |
| 4a tidy (final) | `8a685825b6` | `tidy` | 18:27:15 to 18:28:13 | 0 | tidy changed no files (tree clean after) |
| 4b compile (final) | `8a685825b6` | `:solr:core:compileJava :solr:core:compileTestJava -Pvalidation.errorprone=true` | 18:28:18 to 18:33:38 | 0 | BUILD SUCCESSFUL |
| 4c module check (final) | `8a685825b6` | `:solr:core:check -x test` | 18:33:45 to 18:39:25 | 0 | BUILD SUCCESSFUL |

Leg 1 failure, by content: `testImplicitQueryUsesHighlightQuery` failed at `TestHighlightAugmenterFactory.java:200`, the first implicit assertion in the `weightMatches=false`, `component=false` pass (the `weightMatches=true` passes complete first). Message: `Boost-only text must not be highlighted: SolrDocument{id=2, body_hl=A story about <em>cats</em>. <em>Cats</em> sleep a lot.}`. This is the expected sensitivity failure: the original production highlights the boost-only term in the inline `body_hl`. Full text: `evidence/fail-before-failure-message.txt`.

Leg 1 is not invalidated by the repair commit: the fail tree uses the original production file, and the test file is identical in both heads.

Leg 2 class contents: `testImplicitQueryUsesMainParser`, `testExplicitHighlightQueryOverridesMainQuery`, `testImplicitQueryUsesHighlightQuery` (the three new tests), plus the eight original methods. All 11 passed.

## Superseded runs at `23c159d06b` (pre-repair)

These are not proof for the final head. They are kept as record.

- Leg 2 at `23c159d06b`: 11 tests, 0 failures. Superseded by final leg 2.
- Leg 3 at `23c159d06b`: 33 tests, 0 failures. Superseded by final leg 3.
- Leg 4a tidy at `23c159d06b`: rc 0, clean.
- Leg 4b compile at `23c159d06b`: rc 1. Error Prone `ReferenceEquality` warnings at `HighlightAugmenterFactory.java:195` (`rb.req != context.getRequest()`) and `:196` (`rb.getQuery() != context.getQuery()`), with `-Werror`. Excerpt: `evidence/superseded-pre-repair-23c159-compile-errorprone.txt`.
- Leg 4c module check at `23c159d06b`: stopped by me after the compile failure. It was not run to completion and records no result.

## Repair

Details and reasoning: `reports/pr-5126-highlight-query-fix-repair-round-1.md`. In short, the two identity comparisons are intended. The helper must only use a parser whose request and query objects are the same instances as the result context. Solr uses `@SuppressWarnings("ReferenceEquality")` with a one-line reason for this pattern elsewhere. The repair adds that annotation to `getMainHighlightQuery` only, with one comment line. No logic changed.

## Observations versus source

- Observation: with the fix, the inline `body_hl` for document 2 no longer contains cats for boost-only text, with `hl.weightMatches` false or true, with or without component, and with `hl.q` or local `q` overrides. Explicit and local `q` overrides and `q.alt`/local-parser cases pass.
- Source: the transformer's implicit path previously used `context.getQuery()`, which includes the eDisMax boost clause, as recorded in the Q report. The patch uses the search request's parser, `getHighlightQuery()`, which excludes boost clauses (`ExtendedDismaxQParser.java:703-705`) and returns `altUserQuery` when set.

## Notes and limits

- The fix does not implement or claim real-time get, `/export` alias support, or uncommitted RTG.
- Default `hl.weightMatches=true` did not reproduce the original cats highlight in the Q matrix. The regression tests in this patch cover both settings, but the Q reproduction itself applies only at `false`.
- Module check is `:solr:core:check -x test`. It is not a full repository check.
- The first head's leg 4c check was not run to completion; the final head ran it fully.
