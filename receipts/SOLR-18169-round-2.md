# Receipt: SOLR-18169 gate round 2 (rebase onto apache main) and PR opening

Status: GATE GREEN at head `f5a5480383a2e8d25d1411ec1d813da593305952`. PR #5289 opened as a draft (see below).

Supersedes the head named in `receipts/SOLR-18169-round-1.md`. The round 1 branch was based on the fork's main (`498a09aea97a`), which would have put three fork-only commits (the fork test runner workflow) into the PR diff against apache/solr. At the owner's direction to open the PR, the gating host rebased the two SOLR-18169 commits onto apache origin/main `ad26fdf2c6bee8168eae043b94833f05643d1995` and force-pushed with lease: implementation `f9349ab39b`, tidy `f5a5480383`. The diff against apache main is exactly the four intended files (+96/-5), byte-equivalent in substance to the round 1 diff.

## Round 2 legs (logs gate-18169-r2.log and gate-18169-r2-tidy.log on the phone agent host)

1. Packaging installs: `:solr:packaging:installSlimDist :solr:packaging:installFullDist` rc 0. Slim tree has no `example/`; full tree has `example/`.
2. Focused tests at head: TestSolrCLIRunExample 9 tests, 0 failures, 0 errors, 0 skipped (fresh XML).
3. Proof without the fix: apache main `ad26fdf2c6` plus only the new test file: 9 tests, exactly 1 failure, `testMissingDefaultExampleDirectoryExplainsSlimDistribution` on the old invalid-directory message; the other 8 pass.
4. `:solr:core:check -x test -Pvalidation.errorprone=true` rc 0. The round 1 check-leg failure (Error Prone DirectInvocationOnMock warnings in CoreReplicationAPITest) does not exist on apache main: PR #5272 (merged 2026-11-03) restructured that test, and a standalone Error Prone compile of `:solr:core:compileTestJava` at apache main was verified green separately. The warnings survive only on the fork's main line, which lacks that merge. No new Jira ticket was filed for them, at the evidence above.
5. Tidy: the first round 2 tidy attempt was interrupted by a runtime restart after legs 1 to 4 had completed and been logged; the tidy leg was rerun on its own, rc 0 with a 0-byte diff.

## PR

PR #5289, "SOLR-18169: Slim distribution no longer includes examples", base main, head `f5a5480383`, 4 files. Creation as a non-draft returned 404 (the account ready-state restriction recorded since 2026-11-03 is still in effect), so it was created as a draft in the same attempt sequence, per the owner's go for the PR to go out. Body readback verified byte-identical to the prepared text.

## Limits

Same as round 1: no live `bin/solr start -e` run (root host) and the Docker slim image was not built; both are stated in the PR body.
