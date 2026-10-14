# vm2 open items handoff, round 1

- Assignment: `assignments/pool-vm2-open-items-handoff-round-1.md` (commit `eb39b9b75c2`). Claim: `claims/vm2-open-items-handoff-round-1.md` (pushed at `38fe9eece52`).
- Trigger: the two-hourly scheduled check. Recorded tip `63a4d1e4d2a`; live tip at the check `bb6b287d339`.
- Written by the parent from the subagent replies. The subagents could not write report files.
- Gate: no build, test or Gradle ran on this host. Every branch change below needs a gate job for vm2 at the new head.
- The assignment's item 5 names starting heads (`8f1f4ef91e` and `636196b7954a`) that are stale. The live tips were checked by `git ls-remote`.

## Item 1: SOLR-17055 sort-path fix (`solr-17055-submit`), pushed

- Old head `c2396f425fa2a67a40fea7afee341840c3896358`, new head `2d154d1a402ed9c1705d187773e7a3009ed0645d`, one plain fast-forward through `push-fork-ff.ps1`.
- Change: for a knn request with a sort, the shard request now asks for scores (`createMainQuery`). `mergeIds` first keeps the global topK by score, in a score-ordered queue of size topK. Only that set goes into the sort queue, so start and rows apply to the topK set. The score-only path (sort null) is unchanged. In the sorted knn path, `maxScore` is dropped when the request neither asks for scores nor sorts on them, so the response shape does not gain a key. The changelog is unchanged.
- Test: one case added after the paging case in `DistributedKnnTopKTest`. The query has `fl=id,score`, `rows=20`, `sort=id asc`. It asserts numFound 3, three documents, and ids "1", "2", "3" at positions 0 to 2. On the premise shape the distributed and control comparison should give `.response.size():9!=3`.
- Gate job: `gates/SOLR-17055-fix-gate.md` (OPEN, not run). Head `2d154d1a402`, base `c2396f425fa2`, module `:solr:core`, class `DistributedKnnTopKTest`, seed `17055C0FFEE17055`. The steps include the fail-before leg at the parent with the test overlaid, and the fix leg.
- **Owner question 1 (choose one).** vm2 holds fix commit `2fe0986158` on the same parent `c2396f4`. It is not on the fork, and its proof rerun is in `gates/SOLR-17055-sort-path-proof-rerun.md`. This commit is an independent implementation, and it is now the fork tip. Both cannot land as fast-forwards from `c2396f4`. Someone must choose one, and the proof-rerun job's target must follow.
- **Owner question 2.** With `sort=id asc`, the topK ids are also in score order in this fixture, so the sort case does not prove that the order follows the sort. An `id desc` case would. It was not added, since the job names `id asc`.
- **Owner question 3.** The `maxScore` suppression is untested by the focused test. Keep or drop is the owner's call.

## Item 2: SOLR-15906 sort-paren fix (`solr-15906-submit`), not done

- The subagent for items 2 and 3 was refused by the auto-mode classifier before it started. The refusal reason was "Blind Apply". No fix was written, no patch was applied and nothing was pushed.
- The fork tip is `50139a8e979cd33319a30317d9371ac0b9198e73`, unchanged. The job file is `gates/SOLR-15906-sort-paren-fix.md`.
- **Owner question.** How should a patch-apply on a fork branch be reviewed before it is pushed? The refusal means the review side should not push a patch it has not reviewed with a human present.

## Item 3: SOLR-11356 SJ-R1-49 header copy (`solr-11356-submit`), not done

- Same refusal as item 2. The fork tip is `8b2e826154afb883cfbbe32b3247b53811991939`, unchanged.
- The patch `gates/SOLR-11356-sj-r1-49-header-copy.patch` was not applied, and the header test was not written.

## Item 4: SOLR-16725 branch edits (`solr-16725-submit`), pushed

- One commit `fc5c54770cb8c22c968c0ab6234ff0e1286190cb`, Nick Shanin, no trailers, a plain fast-forward from `be1838ef8ccf` through `push-fork-ff.ps1`. The ls-remote check confirmed the new head.
- Test comment, `LocalFSCloudIncrementalBackupTest.java`, above the raw `GenericSolrRequest` (line 124). Before: none. After: "// Raw request so replicationFactor is sent; the SolrJ create helper sends nrtReplicas." Basis: `Create.getParams` writes `nrtReplicas`, never `replicationFactor`.
- ClusterStatus javadoc. This rewords the existing helper javadoc from `06fd3991c77`, not a new one. Before: "Reports the replica count properties as strings. Collections created through CREATE store them as strings and restored collections as numbers, so without this a client sees both types." After: "Reports the replica count properties as strings. Some collections, such as restored ones, store them as numbers, so without this a client can see both types." Basis: `RestoreCmd` stores `replicationFactor` as an Integer. The helper and its call lines (341 to 353, 377) are unchanged.
- Gate job: `gates/SOLR-16725-branch-edits-gate.md` (new, OPEN, not run). The top-up job file has the wording and the moved head. It also has a correction: its focused class is in package `org.apache.solr.cloud.api.collections`, not `handler.admin`.
- **Not done:** tidy was not run, because Gradle is barred here. The pushed commit is untidied. vm2 runs tidy first. If tidy changes a file, it should land as a follow-up fast-forward. The draft `pr-drafts/core-admin/SOLR-16725.md` was not edited.

## Item 5: landed heads for SOLR-14919 and SOLR-17733, verified, nothing pushed

- `solr-14919-submit` at `483b3885f6439391a3128e6c61be34fc47122514`, parent `8f1f4ef91e`, one commit, two files. Its +/- lines match `gates/SOLR-14919-precision-round-1.patch` exactly.
- `solr-17733-submit` at `cf1b283272641e8e4944cf5a80773cb336be8e0c`, parent `636196b7954a`, one commit, one file. Its +/- lines match the patch exactly.
- Both gate files record the landed head and the evidence, with steps 0 and 1 ticked. The starting heads in the assignment are stale, and the patches are already on the live tips.

## Item 6: SOLR-8628 claim, read only, stale

- The claim's last heartbeat is 2026-10-11T14:06:34Z. Its header says RELEASED at 14:10:34Z by owner direction. There is no heartbeat since.
- The claim covers the premise job `gates/SOLR-8628-premise.md`, which is DONE. The vm1 branch-head leg is in `claims/pool-vm1-premise-8628-part3.md` (DONE). The receipt is GATE GREEN at `5b606dc779d0`, with a corrected test.
- Still needed, in `gates/SOLR-8628-packaging.md` (OPEN, unclaimed): a packaging commit removing `SOLR-8628-TESTING.md` as a fast-forward from `5b606dc779d0`; changelog parse; tidy; Error Prone; the proof leg at `cabedd1d968` (26 tests, 3 failures); the focused `TestCoreContainer` at the packaged head (26 tests, 3 failures, 0 errors); the module check; and a receipt refresh.
- Not taken over and not edited. The vm1 claim says vm3 went down. vm2 decides after a stopped-log check of `/workspace/gates/logs/vm3`.

## Uncommitted gate files on this worktree

- `gates/SOLR-16725-branch-edits-gate.md`, `gates/SOLR-16725-branch-edits-topup-gate.md`, `gates/SOLR-14919-precision-round-1-gate.md` and `gates/SOLR-17733-title-narrowing-round-1-gate.md` (the last two were updated by item 5).
- `gates/SOLR-17055-fix-gate.md` (from item 1).
