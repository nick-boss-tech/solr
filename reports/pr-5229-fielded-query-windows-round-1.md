# PR 5229 fielded-query test and Linux proof handoff, Windows round 1

Status: IMPLEMENTED, PROOF PENDING

- Implementation claim: `claims/pr-5229-fielded-query-windows-round-1.md`.
- Old head: `f2001e640a464e8e861ee3c202821b6dcc4bd497`.
- New pushed fork and PR head: `5275653c66edc2610ab90046927d1c30fbe6f0f4`, branch `solr-10928-submit`, PR https://github.com/apache/solr/pull/5229.
- Commit: `SOLR-10928: cover elevation override for fielded queries`, Nick Shanin, plain fork push. Both the GitHub branch ref and the PR head were read back and matched. The worktree was clean after commit/push.
- Source change: 12 added lines in `solr/core/src/test/org/apache/solr/handler/component/QueryElevationComponentTest.java`; new method `testElevateQOverridesFieldedSearchQuery`.
- The test first verifies `q=text:MMMM` returns IDs 3 and 6, then adds `elevate.q=YYYY` and asserts elevated IDs 1 and 2 ahead of the same search matches. Existing helper assertions check `numFound`, IDs/order and `[elevated]` flags. No production or changelog change.

## Runnable proof handoff

New job: `gates/SOLR-10928-pr5229-fielded-query-round-2.md`, published and verified at board commit `3a119c586e1f78ffa8b56986a330628b7e869188` (also the observed live tip at verification).

It embeds a standalone Bash runner for Linux with JDK 21. The runner takes a Solr clone path, verifies the pinned fork head, uses a host build lock and fresh detached worktrees, unsets NODE_ENV, supplies absolute test work directories, and preserves logs/XML/worktrees. It performs tidy with a clean-tree guard, Error Prone compilation, core check without tests, and the three focused classes at two seeds. Expected counts at the new head are 24/4/1 per seed, not results already obtained.

For each matching seed, fail-before uses fixed merge base `b73f03976fa17b857bc0a81bc8af2c6b1c31e0bb` with only the head's endpoint test file transplanted. The new helper unit suite is not copied, so absent helper APIs cannot create a false compile-failure proof. Base acceptance is exactly one assertion failure in the new method, reaching the four-result override assertion and showing the two original matches. Missing XML, compilation/setup failures, skips, and unexpected base passes are rejected.

The old vm6 receipt remains historical proof only for `f2001e640a464e8e861ee3c202821b6dcc4bd497`. The new gate requests an additive phase receipt, preserving the previous writer's record. Windows did not run Gradle or Java tests. Whitespace review was clean; the runner passed `bash -n`, and its embedded Python verifier parsed successfully. Linux execution remains pending.

## PR body

Under Nick's existing authorization, the PR body was updated at 2026-10-26T00:50:46Z (October 25, 20:50:46 EDT) to identify the new head and fielded-query test, state current-head validation and fail-before proof pending, and retain the previous-head results explicitly as historical evidence. Source/changelog links point at the new head. The AI header/footer remain, with no em or en dashes.

A separate live read-back at 2026-10-26T00:51:22Z exactly matched the working draft. The PR remained draft at the new head; no conversation comments, inline comments, or reviews were present. No public comment or draft-status change was made.

Implementation and handoff deliverables are complete for this Windows claim. This is not a Linux proof receipt and does not mark the new head validated.
