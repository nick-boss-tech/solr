# PR description drift, round 1

Verdict. Fifteen open PR descriptions were corrected where the live text contradicted its receipt, the code at the PR head, or the head SHA. Each edit was applied as a targeted replacement of the named sentence, with a read-back that matched the corrected body. Every other claim was left alone. The owner decisions listed below were not made here and need your answer. Titles were not changed.

Inputs: `assignments/pool-pr-drift-round-1.md` (authorisation: PR descriptions only), claim `claims/pool-pr-drift-round-1.md`. Six read-only reviewers compared the live bodies (saved under the scratchpad) with the receipts, the code at each head and the reports. The edits went through the REST endpoint for the PR body, because `gh pr edit` needs the `read:org` scope that the token lacks. Nothing else on GitHub was changed.

Method notes. A draft is not treated as the source of a PR body. Many live bodies have content their drafts do not, and drafts are older in places, so no text was copied from a draft. Of 59 open PRs, 35 have a draft in `pr-drafts/` and 24 have none. For the 24, the check used receipts and reports only.

## Applied

| PR | Ticket | Correction | Basis | Read-back |
|---|---|---|---|---|
| 4996 | SOLR-18280 | Jira wording: "failed reproducibly in nightly builds, with seeds that reproduce reliably" replaces "is flaky and failed reproducibly"; changelog line becomes "Changelog: none (test-only change)." | Jira export row for SOLR-18280; `changelog/unreleased/SOLR-18280.yml` does not exist at head | matches |
| 5002 | SOLR-11431 | CloudSolrClient sentence: a 503 is treated as a communication error (marks state maybe stale, may refresh, retries), not as a 404 | `CloudSolrClient.java` lines 727 to 777 at the head | matches |
| 5003 | SOLR-7394 | "exactly the three recoveryFailed tests" becomes "exactly three of the six tests" | `receipts/SOLR-7394.md` (3 of 6 fail) | matches |
| 5009 | SOLR-18482 | "full, unsorted result" becomes "full result" | the PR's own ref guide and `FacetRangeProcessor.java` lines 546 to 553 (ranges are built in order) | matches |
| 5010 | SOLR-12998 | Error Prone step replaced by `:solr:core:compileTestJava` | `receipts/SOLR-12998.md` gate steps (no Error Prone run) | matches |
| 5013 | SOLR-13524 | Proof head and date: `f08e7c12a8e` on 2026-10-06 replaces `2b6f17e2622` on 2026-10-04 | `receipts/SOLR-13524` gated head and log date | matches |
| 5016 | SOLR-13097 | "the allow case" becomes "the deny case"; the unrecorded "4 failing test executions" is dropped; the cloud-mode sentence is replaced by the probe-observed outcome | `receipts/SOLR-13097.md` lines 16, 23, 24 and 32 | matches |
| 5017 | SOLR-13136 | Gate date 2026-10-12 replaces 2026-10-06 (re-gate at `486b3877556`); the unrecorded `:solr:api:check -x test` is removed | `receipts/SOLR-13136.md` re-gate section | matches |
| 5030 | SOLR-15823 | "the one commit since then adds tests only" becomes "the two commits since then add tests and one reference-guide sentence" | `git log 100ad2e0df69..1f60cd39baa` (two commits) and `receipts/SOLR-15823.md` line 4 | matches |
| 5089 | SOLR-13943 | awaitsfix counts gain their head: "At the earlier head `cc155cf68e1d` (2026-10-09)" | `receipts/SOLR-13943.md` lines 21 and 26 | matches |
| 5091 | SOLR-14718 | "names only the first request in that stream" becomes "names only the request the client was built with" | `StreamingSolrClients.java` lines 149 to 164 at `f8d4bdbea518` | matches |
| 5092 | SOLR-16356 | "What happens today" link repointed from head `39c0585` line 953 to the merge-base `14c7aac0` lines 949 to 951, where the ERROR log happens | base code | matches |
| 5093 | SOLR-16655 | "at head `b201a57fb3e5`" now says "with this branch's test file applied" | `receipts/SOLR-16655.md` line 8 | matches |
| 5094 | SOLR-16673 | "What happens today" link repointed to the merge-base `14c7aac0` line 101, where the null call happens | base code (the head has a null guard at line 93) | matches |
| 5096 | SOLR-12705 | "in two hunks, both in one import block" becomes "in one hunk, in the import block" | `git merge-file` gives one conflict hunk, in the import block | matches |

Partial correction, 4996. The count change "(25 tests)" becomes "(24 tests)" was not applied. The body's "24 of 25" run counts conflict with that change, and nothing in the record explains them. It stays as drift until the owner decides.

## Owner decisions (not made here)

Claims with no record, or a choice only you can make:
- **4968.** Confirm the gate command for "`:solr:core:check -x test` green". The receipt says "module check passes". Keep the command or reword.
- **4969.** Confirm that `deffea4` is the gated head (c4cd38c plus a Javadoc note). Accept it, or re-gate. The DeleteBackupCmdTest sentence has no run record: record a base run or reword it to the code reading.
- **4996.** Count question above. No record exists at `0da92abd9e96` for the 2026-10-04 date, the "2 of 2", the "24 of 25, twice" run or the base comparison. Record a run, or cut those sentences. Also decide the title: "SOLR-18280: Fix TestReplicationHandler.testUrlAllowList failures" is proposed in `reports/replication-backup-round-1-g4.md` item 7.
- **4999.** The three bullets of the 2026-10-04 controlled run have no receipt record. Record the output or cut them. The base gradle script hardcodes `python3`.
- **5002.** Confirm the module-check command. Confirm the total of 25 tests in TestCoreContainer, since the source has 24 `@Test` methods.
- **5003.** Confirm the module-check command. The fail-before claims for ZkShardTermsRecoveryFailureTest and RecoveryStrategyTest have no record: record the runs or cut the sentences. Decide whether to apply the proposed title "SOLR-7394: Clear the recovering shard-terms entry and restore the pre-recovery term when recovery is abandoned" (`reports/solrcloud-round-1-p6.md` item 12).
- **5004 and 4968 (module-check wording).** Same question as 5002 for the other module-check sentences.
- **5010.** D1: confirm the 2026-10-07 gate date for `62a17a116b5`, or drop it. D2: keep or cut the fork run IDs and the "dispatched" sentence. D3: confirm "ee342298ba4 verified on the runner on 2026-10-04", or drop it. D4: keep or cut the CoreAdminOperationTest base sentence (no base run recorded). D5: keep or cut the "pre-existing" CI sentence.
- **5013.** D1: confirm the venue of fork run `37567533617` (the report says its head is `1869191c4c`). D2: keep the base-code counts only with a recorded base run.
- **5016.** D1: re-point the Proof and upgrade-note links from `f0e7395f58f` to `1cf59fe6a9f` (the rebased head). This needs your approval, and the object is not in this worktree.
- **5017.** D1: the previous-head run on `e6f3f18929f` and the "On base it fails" claims are not in the receipt; keep with a record or cut. D2: the title and changelog describe only the CONSTRUCTION change, not the failure-behaviour change; widen them or not.
- **5027.** The changelog says a failed alias write "removes the completed collection", but `DeleteCollectionCmd.java` lines 237 to 244 refuse that delete when an alias was applied. The wording needs a branch commit. Your decision on the wording.
- **5028.** D1: keep or cut "Later the same day on this VM the merge lost the race". D2: name the tidy and Error Prone runs at `8ca33200e37` and `e28739b4069` in the receipt, or drop the names.
- **5029.** Squash message: use the PR title and body, or the commit title `Caffeine eviction notification race`. Also read failing Crave run `37384974639` at head `77c019e1ff0` (GCSInstallShardTest, not this test) before a merge decision.
- **5030.** Verify on Jira the Eric Pugh 2026-10-05 remark and the "verb-split question". They are not in any receipt.
- **5031.** Restack onto the live head `1f60cd39baa` now (a fork force push) or wait. This sets the landing order.
- **5061.** Verify the Jira-sourced quotes and dates against Jira. Decide whether PR 4999 is closed or held.
- **5062.** Confirm the Lucene PR statements (apache/lucene#14905 and #15350) against those PRs.
- **5089.** Keep or remove the new follow-up sentence in the first Limits bullet (SOLR-13943 is not among the 22 ratified drafts). Keep, soften or caveat "The new comment gives the reason ...".
- **5090.** Keep or remove the planned cloud follow-up (the owner ruling says SOLR-14262 carries none). Ratify the title replacement "SOLR-14262: A commit that is skipped because the update log is not ACTIVE now reports commitIgnored in the response header instead of looking successful." (`reports/review-opened-28-g6.md` item 1).
- **5091.** Keep or remove "A follow-up submission for that flaw is planned."
- **5093.** Ratify the Limits commitment "A follow-up submission is planned to check the combined tree after SOLR-12705 rebases, and to add a test that sends an update request." No ratified wording exists.
- **5094.** Ratify the Schema Designer commitment "A follow-up submission is planned to test that link." Verify the Houston Putman sentence against the Jira comment, or drop it. It is not in any receipt or report.
- **5086.** The live title changed without a recorded ruling. Confirm and record it.
- **5087, 5088, 5091.** The live titles equal the proposed replacements, but ratification is not recorded.
- **5100.** "Once this change lands, I will follow up on that branch by dropping its rollback edit" (SOLR-9865). No record shows you confirmed this public commitment. Keep or strike.
- **5103.** The changelog line 1 still says "(or a Date.toString())", while the live title no longer does. Decide whether to edit the changelog and re-gate.
- **5106.** "This is a reopened ticket, not a new one." Confirm the JIRA status or strike it.
- **5015 (optional).** The title could name the wider serialization change (`reports/review-confidence-round-3.md` line 39). Owner call.

## Not verified here

- Jira content was not fetched (no network access in this review). Every Jira-sourced quote is listed above.
- The gate logs are not on disk. Test runs, seeds and CI run IDs were checked only against the receipts.
- Commit 1ddbf36202d (the SOLR-5939 merge commit) is absent from this clone. Its claim was left alone.
