# SolrCloud HOLD drafts: first rung, round 1

Assignment: `assignments/pool-solrcloud-hold-first-rung-round-1.md`. Claim: `claims/pool-solrcloud-hold-first-rung-round-1.md`. Run on the Windows review lane on 2026-10-15. No builds, tests, gate runs, pushes, public writes or PR changes on this host.

Basis: pool tip `4cd51f44ff67` (origin/pr-prepare). Fork heads read with `git ls-remote origin refs/heads/solr-NNNN-submit` on 2026-10-15. Each receipt was read at the live head it names. Upstream code was read with `git grep` and `git show` at the heads and merge-bases named in each section.

## Summary

| Ticket | Live fork head (2026-10-15) | Record that settles or holds it | Draft edits this round | Closing line |
|---|---|---|---|---|
| SOLR-11288 | cbf0c678f277 | base run landed at the live head | Proof base sentence now names the head | SETTLED TO SATISFIED |
| SOLR-12651 | de3f3604f0ee | scrub and gate at de3f3604f0ee landed; base run in the receipt | Head and dates re-pointed; base-run wording | OWNER ITEM (title commit cannot be pushed here) |
| SOLR-13369 | dfa0db5bdf96 | dated head run and dated base run landed; test-side rule adopted | Seed sentence corrected; Limits line added | SETTLED TO SATISFIED |
| SOLR-15035 | 12d7491e82cd | base run and head-production run landed; slice-count route adopted | none | SETTLED TO SATISFIED |
| SOLR-15106 | 40b7e5d0efa7 | premise compile text and dated head run landed | none | SETTLED TO SATISFIED |
| SOLR-15386 | ca8cb61ee957 | dated focused run landed | none | SETTLED TO SATISFIED |
| SOLR-15674 | 75ddf9ba518a | scrub and gate at 75ddf9ba518a landed | none | SETTLED TO SATISFIED |
| SOLR-16437 | a23f770e4b9 | changelog re-gate and nightly run landed | nightly Limits line rewritten as the record | SETTLED TO SATISFIED |

Files written by this assignment:

- `C:\Users\shaninna\dev\Solr-issues\wt\pr-prepare-windows\reports\solrcloud-hold-first-rung-round-1.md` (this report)
- `C:\Users\shaninna\dev\Solr-issues\wt\pr-prepare-windows\pr-drafts\solrcloud\SOLR-11288.md` (edited)
- `C:\Users\shaninna\dev\Solr-issues\wt\pr-prepare-windows\pr-drafts\solrcloud\SOLR-12651.md` (edited)
- `C:\Users\shaninna\dev\Solr-issues\wt\pr-prepare-windows\pr-drafts\solrcloud\SOLR-13369.md` (edited)
- `C:\Users\shaninna\dev\Solr-issues\wt\pr-prepare-windows\pr-drafts\solrcloud\SOLR-16437.md` (edited)
- Not in the pool worktree, not committed, not posted (scratch drafts): `C:\Users\shaninna\AppData\Local\Temp\2\claude\C--Users-shaninna-dev-Solr-issues\39075c50-d965-4305-9aac-4943a4515305\scratchpad\SOLR-12651-title-regate-DRAFT.md` and `C:\Users\shaninna\AppData\Local\Temp\2\claude\C--Users-shaninna-dev-Solr-issues\39075c50-d965-4305-9aac-4943a4515305\scratchpad\SOLR-13369-seed-premise-DRAFT.md`

No other files were touched. The claim file is the coordinator's and still has no `DONE:` line; the coordinator should add it in the same push as this report.

---

## SOLR-11288

Draft: `pr-drafts/solrcloud/SOLR-11288.md`. Head: `cbf0c678f277d6f50d75408d4889d3b664469905`, same as the claim.

Gate job: `gates/SOLR-11288-base-run.md`, status DONE in its header. Question it carried: a base-code run of TestCollectionAPI at the live head, to show the padded-repeat premise. The answer is in `receipts/SOLR-11288.md` under "Base-code run at the live head (2026-10-13)": merge-base `97d973814336`, run 07:45:13Z to 07:52:09Z, TestCollectionAPI tests=4, failures=1, seed `515D62C7D388C84F`, failing at line 321 with "duplicate element: shard1". The claim `claims/pool-vm2-gate-backlog-11288-baserun.md` is marked DONE at 08:02Z.

Records checked at the live head: receipt gated head `cbf0c678f277` (changelog-only carry over from `cd094c3c623`), 4 of 4 from fresh XML. Changelog title already names the blank-only behavior.

Code checks (heads and bases): `ClusterStatus.java` Set.of at merge-base line 171; trim and empty filter at head 177 to 178; empty requested set at head 260. `BalanceReplicasCmd.java` String branch at head 52 to 58; "Cannot balance across a single node" at base line 65 (the draft's L62-L65 range covers it). `MigrateReplicasCmd.java` split at head 144; "sourceNodes is a required param" at head 57; blank targetNodes fallback at head 76 to 82. `TestCollectionAPI.java` new method at head 318; the exact repeat at line 321 (matches the receipt).

Decision taken: the blank-only rule stays as implemented, which is the recorded recommendation (`material/solrcloud-round-1-answers.md`, SOLR-11288 entry, DISCUSS line, and line 257 of the same file). The draft's Choice stays as a question to the maintainer, as the record says.

Draft edit: the base sentence now reads "with the branch's test file at head `cbf0c678f277` in place", because the base run was made against that head's test file. Nothing else changed.

Closing line: **SETTLED TO SATISFIED**. Record: `receipts/SOLR-11288.md`, base-run section, and the DONE claim above.

---

## SOLR-12651

Draft: `pr-drafts/solrcloud/SOLR-12651.md`. Live head: `de3f3604f0ee12935080c8f3d418b6bd1be3013e` (ls-remote 2026-10-15, same as the scrub job and receipt).

Gate jobs:

- `gates/SOLR-12651-history-scrub.md`: DONE. The three Co-Authored-By trailers were removed (`13e6789badd` to `8413cd30fb71`, `37f5989b9d` to `92aea8b9c388`, `db8ba836d8` to `2d83452c533b`). The tree is unchanged (`3b4b0e9bc873`). Full gate green at `de3f3604f0ee`, 07:09:06Z to 07:28:43Z. `git log` over `14c7aac0d151..de3f3604f0ee` finds no "claude" or co-author text (checked this round).
- `gates/SOLR-12651-base-run.md`: the job file still shows its checklist open and says the receipt section is left for the main agent. The base run itself is recorded in `receipts/SOLR-12651.md` (Proof paragraph): base `14c7aac0d151` with the branch tests overlaid, 1 of 2 failing with "Failed restore left collection backuprestore_testokboo2 behind", seed `C039DD4A67379E23`, run 2026-10-13 04:55Z, log `logs/vm2/queue-g12651-base-gate-20261013T044827Z.out`. The run was made at `404f13b501c9`, which has the same tree as the live head, and the receipt says so.
- `gates/SOLR-12651-title-regate.md`: DONE at `404f13b501c9`. It re-gated the title change that is on the branch now, which carries the overbroad wording (see below).

Draft edits made:

- All three head links (RestoreCmd.java lines 265 to 316, the errorRestore test lines 131 to 198, and the changelog) now point at `de3f3604f0ee`. The tree is the same, so the line ranges hold (checked: RestoreCmd try block at lines 266 to 316, markAllShardsAsActive at 302, addReplicasToShards at 319, restoringAlias at 324).
- The Proof's head sentence now reads "passes 2 of 2 at head `de3f3604f0ee`, verified 2026-10-13 07:24 UTC" (receipt XML timestamp 2026-10-13T07:24:25Z).
- The base-run bullet now says the run was made at `404f13b501c9`, the same tree as the live head.
- The Choice, the Limits, and the landing-order line after SOLR-9598 are unchanged, as the assignment asks.

Title defect (effort, not information). The changelog title on the branch reads: "A RESTORE that fails before its shards become active now deletes the new collection instead of leaving a partly restored collection behind, unless the restore runs asynchronously." The code does not support "unless the restore runs asynchronously." Property upload and replica setup run inside the same try block for async restores, so their failures are cleaned up in both modes. Only a shard copy that fails in an async restore is not cleaned up, because that failure is recorded in the async status (`CollectionHandlingUtils.java` waitForAsyncCallsToComplete, lines 794 to 816).

Decision taken: the corrected title is "A RESTORE that fails before its shards become active now deletes the new collection instead of leaving a partly restored collection behind, except when a shard copy fails in an async restore." This supersedes the replacement in `reports/solrcloud-round-1-p2.md` finding 9, which carried the overbroad wording.

Why it is not done: the title lives in the branch, so the fix is a new commit on `solr-12651-submit` followed by a gate at the new head. This lane may not push (hard rule 3), so the commit cannot be made here. The scrub job already ran at `de3f3604f0ee`, so the title cannot ride that scrub, and I did not edit the DONE scrub job file. The corrected title is in the scratch draft `C:\Users\shaninna\AppData\Local\Temp\2\claude\C--Users-shaninna-dev-Solr-issues\39075c50-d965-4305-9aac-4943a4515305\scratchpad\SOLR-12651-title-regate-DRAFT.md` with the steps for the commit and its gate. That draft is not posted.

Closing line: **OWNER ITEM**: Who pushes the title-only commit on `solr-12651-submit` (text in the scratch draft named above) and runs its gate before the PR opens, given that this lane may not push and the title overstates the async carve-out until then?

---

## SOLR-13369

Draft: `pr-drafts/solrcloud/SOLR-13369.md`. Live head: `dfa0db5bdf96d119d985a8b895ff90c4d783ad47`. Merge-base: `c3cdf7b46e8cfff3673f76d881f32cf8e7b00622` (checked with `git merge-base upstream/main`).

Gate jobs:

- `gates/SOLR-13369-dated-run.md`: DONE. Head run GREEN at `dfa0db5bdf96`, XML 2026-10-13T04:47:49Z, TriLevelCompositeIdRoutingTest tests=1, failures=0, errors=0 (`receipts/SOLR-13369.md`, "Dated focused run (2026-10-13, vm2)"). The job's step 4 (base run) is covered by the next job.
- `gates/SOLR-13369-base-premise.md`: DONE 2026-10-14T11:47Z. Base run at `c3cdf7b46e8c` with the head test overlaid, seed `13369C0FFEE13369`, 1 test, 0 failures, 0 errors, run 11:16:48Z to 11:26:38Z, XML 11:26:05Z (`receipts/SOLR-13369.md`, "Base-code dated run (vm2, 2026-10-14)"). The draft's base sentence matches.

Records checked: the receipt's proof line says no run has shown the old failure, and the record says the Proof is inconclusive by construction. The draft's Proof says the same.

Code checks: the `@AwaitsFix` marker is at base `TriLevelCompositeIdRoutingTest.java` line 66 and is absent at head, so "the marker is removed" holds. The head test draws a random bit mask (line 87).

JIRA packet: `research/jira-context/SOLR-13369.json` exists on this host. It names the ticket's failing seed `A6B6F0104FE6018F` (reproduce line with multiplier 3, locale sr-Latn, timezone Pacific/Tongatapu) and the failure "routePrefix app9/2!user32 found in multiple shards" with the two documents on shard2 and shard3. The draft's "What happens today" matches.

Decision taken: the test-side rule stays, as the record adopted it (`material/solrcloud-round-1-answers.md`, SOLR-13369, ADOPTED; `reports/solrcloud-readiness-round-1-application.md`, Part 3). The draft's Choice stays as a question to the maintainer, as the record says. No run has yet shown a route prefix in two shards on this branch.

Draft edits:

- The Proof sentence "the test data had no route prefix on two shards" is replaced with "the seed used put no route prefix in two shards", and the six-seed hunt is named. The receipt does not record the head run's seed, so the old phrasing claimed more than the record shows.
- A Limits line names the ticket's failing seed as not yet run on this branch, says a run can be added on request, and says it may not reproduce on the current test code. This turns the optional run into a named follow-up with a plan.

Premise question for the ticket seed (not posted): `C:\Users\shaninna\AppData\Local\Temp\2\claude\C--Users-shaninna-dev-Solr-issues\39075c50-d965-4305-9aac-4943a4515305\scratchpad\SOLR-13369-seed-premise-DRAFT.md`. Question: with seed `A6B6F0104FE6018F`, does the one-shard rule fail on base, and which shards hold the app9/2!user32 documents? If the answer is yes, the Proof can show the old failure. The draft is not posted.

Closing line: **SETTLED TO SATISFIED**. Records: `receipts/SOLR-13369.md` (dated head run 2026-10-13 and dated base run 2026-10-14), `gates/SOLR-13369-base-premise.md` DONE, and the ADOPTED test-side rule in `material/solrcloud-round-1-answers.md`. The Choice stays a maintainer question. The seed run above is an optional follow-up named in Limits.

---

## SOLR-15035

Draft: `pr-drafts/solrcloud/SOLR-15035.md`. Live head: `12d7491e82cd660ad55063cb85633b7e05e1918c`.

Gate job: `gates/SOLR-15035-base-run.md`, DONE 2026-10-13T08:05Z. Its runs are recorded in `receipts/SOLR-15035.md` ("Base-code and head-production runs (2026-10-13)"):

- Base run at merge-base `b5c71bc5573c` with the branch test file: `AddReplicaTest.testAddReplicaPersistsNumShardsInCoreProperties` fails with `java.lang.AssertionError: expected:<2> but was:<null>`. Tests 1, failures 1. XML 2026-10-13T06:13:52Z, seed `850D344DC97DDBB9`.
- Head-production run on base tests (full production change at head, merge-base test file): AddReplicaTest tests 3, failures 0, errors 0. XML 2026-10-13T06:18:58Z, seed `1D8248B792379CAA`.

The draft's Proof quotes the same failure line, so the placeholder noted in the readiness round is filled.

Records checked: the receipt is green at the tip (AddReplicaTest 4 of 4, adjacent suites 8 of 8 and 44 of 44). The ledger date 2026-10-03 is the basis for the banked-only claim ("ADDREPLICA alone is not enough"). The readiness round confirmed the ticket summary, per `reports/solrcloud-application-round-2.md`.

Code checks: `AddReplicaCmd.java` line 305 sets the slice count at head. `CoreAdminHandler.java` line 250 maps `ZkStateReader.NUM_SHARDS_PROP` at head, and the base `paramToProp` map starts at line 238.

Decision: the slice-count route (record every slice, split case as a Limits bullet) is ADOPTED (`material/solrcloud-round-1-answers.md`, SOLR-15035). No change.

Note on the dated banked-only claim: no 2026-10-13 run used the ADDREPLICA change alone. The "not enough" claim rests on the 2026-10-03 ledger record, which the other drafts already accept as a dated record. The 2026-10-13 head-production run used the full change. I did not add a run for the banked-only case.

Landing order: SOLR-15035 is the first opening in its AddReplicaCmd group. With these records landed, its settlement clears the SOLR-11479 dependency (the 11479 draft opens after SOLR-15035).

Closing line: **SETTLED TO SATISFIED**. Records: `receipts/SOLR-15035.md` base and head-production runs (2026-10-13) and `gates/SOLR-15035-base-run.md` DONE. SOLR-11479's dependency on this draft clears.

---

## SOLR-15106

Draft: `pr-drafts/solrcloud/SOLR-15106.md`. Live head: `40b7e5d0efa70d61b74b9fa9ac04beb1ed397637`. Merge-base: `9b3a84b1c460981eab09d8ffaef776acc4a184f8`.

Gate job: `gates/SOLR-15106-premise-base.md`, DONE 2026-10-13T04:45Z. Its record in `receipts/SOLR-15106.md` ("Base failure text and dated head run (2026-10-13, vm2)"):

- Base compile failure, verbatim: `OverseerProcessorExitTest.java:36: error: cannot find symbol` and `symbol: method getCollectionProcessorThread()`, repeated at line 46. No test executed. Log `logs/vm2/15106-premise-base.log`.
- Head run: OverseerProcessorExitTest tests 1, skipped 0, failures 0, errors 0, XML 2026-10-13T04:41:12Z, rc 0.

Draft check: the draft's Proof quotes the same two compile lines (lines 36 and 46) and gives the head run as 2026-10-13. The draft already matches the record, so no text change is needed. The phrase "inconclusive by construction" is the formula's own wording (`pr-formula.md`, lines 27 to 30 and 149), so it stays.

Code checks at head: `Overseer.java` run method at line 223 and the processor-thread stop path near 774 to 839; the stop flag `closeRequested` at 654; the accessor `getCollectionProcessorThread` at 827. The base exit path is at `Overseer.java` lines 453 and 486 to 492, and the base early return at `OverseerTaskProcessor.java` line 186.

Still open, not part of this item: `gates/SOLR-15106-claim-verification.md` (the 1200 queued messages figure) is OPEN and unclaimed. My reading of the head test: it issues one `createCollection` request (`OverseerProcessorExitTest.java` line 56), and it contains no queue-count logic. The number therefore cannot come from the branch. The draft attributes the figure to the ticket ("The ticket reports more than 1200 queued messages..."), which is the Unverified outcome that job requires. The claimant should record that outcome in the receipt. I did not edit the receipt.

Closing line: **SETTLED TO SATISFIED**. Records: `receipts/SOLR-15106.md` base compile text and dated head run (2026-10-13), `gates/SOLR-15106-premise-base.md` DONE. The separate claim-verification job remains open (see above).

---

## SOLR-15386

Draft: `pr-drafts/solrcloud/SOLR-15386.md`. Live head: `ca8cb61ee957ed728034a2e6c90532e6082ef3ff`.

Gate job: `gates/SOLR-15386-dated-run.md`, DONE 2026-10-13T05:01Z. Record in `receipts/SOLR-15386.md` ("Dated focused run, 2026-10-13"): NodeMutatorTest tests 3, skipped 0, failures 0, errors 0, XML 2026-10-13T05:01:08Z, run 04:55:14Z to 05:01:11Z, at `ca8cb61ee957`. The runner does not pin a seed.

Draft check: the Proof says "verified 2026-10-13 in a run timestamped 05:01 UTC" and "3 of 3, with 0 skipped". That matches the record. The "recorded 2026-10-07" takeover-log date is gone. The base is inconclusive by construction (the test uses `NodeMutator.ONLY_IF_NODE_NOT_LIVE`, which is absent at merge-base), which the draft says.

Code checks at head: the flag constant at `NodeMutator.java` line 51, the guard at lines 58 to 61. The flag is set on the shutdown path (`ZkController.java` line 848 calls `publishNodeAsDown(getNodeName(), true)`, and the queued message carries the flag at line 3086).

The receipt's owner question (whether the cluster-state snapshot at queue consumption is enough) is not in the draft. The draft's Choice already asks the maintainer the adjacent question, so I did not add a second one.

Closing line: **SETTLED TO SATISFIED**. Record: `receipts/SOLR-15386.md` dated run (2026-10-13 05:01:08Z) and `gates/SOLR-15386-dated-run.md` DONE.

---

## SOLR-15674

Draft: `pr-drafts/solrcloud/SOLR-15674.md`. Live head: `75ddf9ba518a370278c8171aecce3e80d055a78c`.

Sequence, stated plainly: the history scrub ran first. One commit (`9b51865402`, "compare mzxid instead of version+czxid...") carried only a Co-Authored-By trailer. The scrub removed that trailer and rebuilt the branch onto the merge-base, with each commit's tree, author and date kept. The tip moved from `ba01c83d4c5` to `75ddf9ba518a`. The gate then ran at the scrubbed head.

Gate job: `gates/SOLR-15674-history-scrub.md`, DONE. Its spec was complete: the contaminated commit is named, the tree-identity checks are listed, and the gate at the scrubbed head is specified. The job file's checklist is all ticked.

Receipt: `receipts/SOLR-15674.md`, "Gate at 75ddf9ba518a": gate green 06:19:20Z to 06:37:15Z. IndexSchemaFactoryCacheTest 1 test, 0 skipped, 0 failures, XML written 06:33:28Z. Tree checks (tip tree `e1a6fafc1377`), and no "claude" or "co-authored" text in the range. The proof stays inconclusive by construction.

Draft check: the links and the changelog point at `75ddf9ba518a`, and the "What happens today" links point at the merge-base (correct for the pre-change symptom). The Proof says "passes 1 of 1 at head 75ddf9ba518a. Verified 2026-10-13." The code lines I checked (`IndexSchemaFactory.java` 149, 150, 187; `SolrConfig.java` 170) match the head.

Length: accepted for a complex ticket, per `reports/solrcloud-readiness-round-1-application.md` (SOLR-15674 paragraph). No trim.

Choice: `material/solrcloud-round-1-answers.md` (SOLR-15674) says "the draft's Choice stands as written". The draft still carries the question, which matches that record. The readiness application said the question had been removed; the draft does not show that, and I followed the record.

Closing line: **SETTLED TO SATISFIED**. Records: `receipts/SOLR-15674.md` (gate at `75ddf9ba518a`, scrub record) and `gates/SOLR-15674-history-scrub.md` DONE. The length is accepted for this complex ticket.

---

## SOLR-16437

Draft: `pr-drafts/solrcloud/SOLR-16437.md`. Live head: `a23f770e4b9587acbd14fcdbaae2047bd5c727c4`. Merge-base: `e2cdb2d7e8ae0be4cf6cf0606206c67d89e7278f`.

Gate jobs:

- `gates/SOLR-16437-changelog-regate.md`: DONE. Full gate green at `a23f770e4b9`, GATE END 06:58:31Z. The focused head count was re-run from fresh XML (`receipts/SOLR-16437.md`: CollectionsAPISolrJTest tests 25, skipped 1, failures 0, XML 2026-10-13T07:35:26Z, seed `16437C0FFEE16437`). The proof leg with `CollApiCmds.java` reverted to the merge-base gives 25 tests, 1 skipped, exactly 1 failure, `testAddReplicaPropRejectsUnknownReplica` ("Expected exception RemoteSolrException but no exception was thrown", `CollectionsAPISolrJTest.java:1301`). The receipt and the draft name the head `a23f770e4b9`. The earlier owed recount is closed.
- `gates/SOLR-16437-nightly-run.md`: DONE. The nightly-group run at `f72a9edd0a8b` (the earlier head, same code tree apart from the changelog title) gave TestPullReplica 39 tests, 1 skipped, 0 failures; TestTlogReplica 14 tests, 1 skipped, 0 failures; XML written 07:02:28Z. Recorded in `receipts/SOLR-16437.md` ("Nightly-group run at f72a9edd0a8b").

Code checks: `CollApiCmds.java` lookup and the HTTP 400 at head lines 345 to 362 (the 400 message at 354 to 357). The test starts at `CollectionsAPISolrJTest.java` line 1293 (annotation 1292). The base `ReplicaMutator.java` throw at 166, and the base `DocCollection` lookup named in the draft.

Draft edit: the Limits bullet that discloses the nightly run now reads as the record: "The two nightly-group classes ran with the nightly group enabled at f72a9edd0a8b, which differs from the live head only in the changelog title." The counts stay in the bullet, with the one skipped test per class. The rest of the draft already carried the re-pointed head and the 2026-10-13 date.

Closing line: **SETTLED TO SATISFIED**. Records: `receipts/SOLR-16437.md` changelog re-gate (`a23f770e4b9`, fresh XML 2026-10-13T07:35:26Z) and nightly run (`f72a9edd0a8b`, fresh XML 2026-10-13T07:02:28Z); `gates/SOLR-16437-changelog-regate.md` and `gates/SOLR-16437-nightly-run.md` both DONE. The skip disclosure is now the record.

---

## Decisions taken under the decision default

1. SOLR-11288 blank-only input: keep blank equal to omitted (recorded recommendation).
2. SOLR-12651 changelog title: corrected to the async-shard-copy-only carve-out, superseding the overbroad wording from `reports/solrcloud-round-1-p2.md` finding 9. The branch change is blocked here (no push); see the owner item.
3. SOLR-13369: test-side rule kept (ADOPTED in the record); ticket seed named as a Limits follow-up.
4. SOLR-15035: slice-count route kept (ADOPTED in the record).
5. SOLR-15674: the Choice stays as the record adopted it.
6. SOLR-16437: nightly record stated as results, not as a skip disclosure.

## Pending approval (not done here)

- Push the title-only commit on `solr-12651-submit` and gate it. Text in the scratch draft named above. Needs a host that may push.
- Post the scratch premise job for SOLR-13369 (ticket seed `A6B6F0104FE6018F`, base and head). It is a premise question for a Linux lane. Not posted here.
- No PR, comment, JIRA, or mail action was taken or prepared for posting.

## Could not verify or not settled

- SOLR-12651: the base-run job file's checklist is still open. The base run is in the receipt, so I treated it as recorded. I did not edit the job file.
- SOLR-15035: no 2026-10-13 run of the ADDREPLICA-only change exists. The banked-only claim rests on the 2026-10-03 ledger date.
- SOLR-15106: the claim-verification job for the 1200 figure is still OPEN. That is outside this assignment, and its Unverified outcome already matches the draft's attribution.
- SOLR-15674: the readiness report says the Choice was removed; the draft and the record both keep it. I followed the record.
- SOLR-15386: the receipt's owner question is not in the draft. I did not add it, because it is not a hold.
- Code citations: I checked the load-bearing lines for each draft, not every link in every draft.
