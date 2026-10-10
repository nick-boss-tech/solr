# SolrCloud round 1: main-side answers (2026-10-09)

Report: `reports/solrcloud-round-1.md`, with parts `reports/solrcloud-round-1-p1.md` through `-p6.md`. Assignment: `assignments/solrcloud-round-1.md`. Under the owner's standing decision practice, recorded recommendations are adopted below unless an item is marked DISCUSS; DISCUSS items carry the recommendation and the call is not taken. Gate state is settled by the receipt in `receipts/` at the exact live tip; where a report claim and a receipt differ, the receipt wins and the entry says so. Count note: the assignment header says thirty-one tickets and lists thirty; the thirty listed are answered here. SOLR-18391 has two branches and two entries.

## Gate states settled by the receipts

- Gated green at the live tip, per the receipts: SOLR-5813 (`90b8baa08ae`), SOLR-7394 (`9857d9ee802`, live PR, consistency only), SOLR-9155 (`9f08d033023`), SOLR-11288 (`cd094c3c623`), SOLR-12991 (`1a86966179e`), SOLR-13186 (`b436d90d2a8`), SOLR-13239 (`699a1fce368`, submission-held), SOLR-13369 (`dfa0db5bdf9`, proof by construction), SOLR-14919 (`84e7bcaeb57`), SOLR-15035 (`12d7491e82c`), SOLR-15106 (`40b7e5d0efa`), SOLR-15386 (`ca8cb61ee95`, proof inconclusive by construction), SOLR-15674 (`ba01c83d4c5`, proof inconclusive by construction), SOLR-15863 (`f381fd8d4dd`), SOLR-17292 (`e43200b0fb6`), SOLR-17680 (`f4b8ce83365`), SOLR-17733 (`636196b7954`), SOLR-18277 (`17c0e644812`, merged).
- Gated at an older head; the live tip is not gated: SOLR-12651 (gated `90032e274b7`, tip `f3131d1ee84`), SOLR-12998 (gated `25520806c26`, tip `62a17a116b5`), SOLR-13136 (gated `2ad44409ffe`, tip `486b3877556`), SOLR-16013 (gated `e1bd21fd11a`, tip `ba26b702891`), SOLR-18391 graceful-create (gated `3a0ff1262bf`, tip `adcda10b501`).
- No gate recorded: SOLR-3865, SOLR-4754, SOLR-10234, SOLR-10641, SOLR-11479, SOLR-16437, SOLR-17281 (parked), and SOLR-18391 submit (tip `3000eeede7a`). The GitHub corroboration run recorded for SOLR-16437 does not settle gate state; the receipt and the report agree on that.

## Per-ticket answers

### SOLR-15674 (draftable; pair with SOLR-15478)

- ADOPTED: land SOLR-15674 first in the mzxid pair. It covers the default SolrCloud path; SOLR-15478 (gated green at `0478bdf0ac5`, Configsets round 1) covers only the configset cache that `shareSchema` enables. The two branches share no file and trial merges are clean.
- ADOPTED: the draft's Choice stands as written (mzxid alone against the earlier creation-ID-plus-data-version route).
- ADOPTED: the managed-schema fallback (part p1, finding 7) and the SolrCore config watcher (finding 6) stay as Limits lines in the draft, with the follow-up offer. Neither is fixed in this branch.
- The version-reset wording in the two drafts stays identical (part p1, finding 5): if either draft is edited, both are checked.
- Gate state: green at the live tip per the receipt; proof inconclusive by construction, and the draft says so.

### SOLR-5813 (draftable, PR-ready)

- ADOPTED: default the empty collection name to the core name, as drafted. The reporter's on-ticket direction supports the default; the draft's Choice poses the reject route to the maintainer, since the ticket title asks for it.
- ADOPTED: the existing core.properties effect (cores with an empty `collection` value now load under the core name) stays a Limits line; no further treatment before opening.
- Gate state: green at the live tip per the receipt (CloudDescriptorTest 3 of 3; the new test fails on base). No disagreement.

### SOLR-17292 (drafted, held for one in-branch fix)

- ADOPTED: apply the node-down remedy from part p1, finding 1 in this branch before opening. When the per-replica persist throws inside `publishNodeAsDown`, the DOWNNODE offer for the whole node is currently skipped; that skip is a side effect of this branch's own change, so it is fixed here rather than shipped as a Choice. The remedy wraps the persist call, logs a warning, and lets the offer run. The draft's Choice section then comes out and its node-down bullet is updated, as part p1 describes. The branch re-gates after the fix; the re-gate is main-side work owed (below).
- ADOPTED: the duplicate ERROR log line (part p1, finding 3) stays as recorded in the draft's Limits; no change.
- Gate state: green at the live tip per the receipt (TestPerReplicaStates 4 of 4, proof passed) for the branch as it stands; the remedy above moves the head, so the green does not carry to the fixed head.

### SOLR-12991 (draftable)

- ADOPTED: WARN, as drafted. The draft's Choice states the position and the alternative honestly: the ticket asks for WARN or ERROR, ERROR is already the base level, and a 2018 ticket comment leans informational. The maintainer confirms on the PR.
- Record note: the receipt's shorthand ("the ticket accepts either") is looser than the ticket packet the audit read (part p1, finding 14). The draft states the ticket's position accurately, so no draft change follows; gate state is unaffected.
- Gate state: green at the live tip per the receipt (focused class 1 of 1; the base run fails on the WARN assertion).

### SOLR-15035 (draftable)

- ADOPTED: ship the split case as the Limits bullet already in the draft (the count includes an inactive split parent, so a replica added after a split can record a higher numShards than the create-time count). No count-source change before opening.
- ADOPTED: landing order for the AddReplicaCmd pair: SOLR-15035 first, then SOLR-11479 after its first gate (part p2, finding 5).
- Owed before opening: paste the observed base failure line into the draft's Proof placeholder. The main-side record does not carry the exact line and the gate logs are not on disk; if the line cannot be recovered from the run record, the Proof sentence is reworded to the receipt's recorded outcome (the test failed with the banked fix alone, because the CoreAdmin allowlist dropped numShards) instead of quoting a line. Also confirm the ticket summary before opening; no Jira packet for this ticket is on disk (part p2, finding 8).
- Gate state: green (hardened) at the live tip per the receipt (AddReplicaTest 4 of 4, proof passed).

### SOLR-15863 (draftable)

- ADOPTED: a shard that reports no version is skipped, as implemented and as stated in the draft's Limits (part p2, finding 15). The backup never fails over a missing version; if no shard reports one, the running version is recorded, as before.
- ADOPTED: the S3 and GCS incremental tests that inherit the shared test base were not run for this change; the draft's Limits line stands (part p2, finding 16). No extra run is owed unless the owner directs one.
- Correction owed (branch): the changelog fragment's title starts in lower case; the replacement line is in part p2, finding 17. One branch commit before opening.
- Gate state: green at the live tip per the receipt (BackupCmdTest 4 of 4, BackupCoreAPITest 6 of 6, LocalFSCloudIncrementalBackupTest 7 of 7). The premise was grounded at the earlier head `8f5e94cf314` and is inconclusive by construction at this head; the draft says so.

### SOLR-12651 (draftable, held for a run at the live tip)

- Gate state, receipt wins: gated at `90032e274b7` (TestLocalFSCloudBackupRestore 2 tests, proof discriminates). The live tip `f3131d1ee84` is two commits past the gate; the property-upload leg and its move into the cleanup try are in those commits and have no run. A gate at the live tip is main-side work owed before opening.
- ADOPTED: delete-on-failure stays the implemented position, and the ticket thread's retry-and-resume question (whether cleanup should be optional) stays in the draft's Choice, as drafted.
- ADOPTED: the async gap ships as the draft's Limits bullet, with the corrected changelog wording below; the shard-failure check sketched in part p2, finding 10 is named there as the follow-up, not added in this branch.
- Corrections owed (branch): the changelog title overstates the cleanup; the replacement text is in part p2, finding 9 (cleanup covers failures before the shards become active, and not async restores). Corrections owed (draft): the bracketed Proof line is replaced with the live-tip run result once the gate lands, and the Choice section gets a bold one-line summary opener (see the draft corrections list).
- Interaction: SOLR-12651 and SOLR-15863 share no file; the assignment's shared-test-class note was wrong (part p2, finding 19). No landing order between them.

### SOLR-13186 (draftable, PR-ready)

- ADOPTED: the draft stands; Limits lines only, as the receipt requires.
- Draft corrections owed: strip the owner-notes header (it carries an internal line about the gate record) and set the Proof date from the main-side record: the gate is recorded in the receipts ledger at 2026-10-06 (round 27). The four drafts with a "[date to confirm]" Proof line (9155, 13186, 15106, 15386) use the recorded date in the same "recorded" form the other drafts already use; see the draft corrections list.
- Gate state: green at the live tip per the receipt (OverseerElectionContextTest 1 of 1; the base failure text is in the receipt).

### SOLR-15106 (draftable, PR-ready)

- ADOPTED: the processor-exit versus session-expiry race stays a Limits line with the follow-up offer, as drafted and as the receipt records it.
- Record note: the receipt's Proof line ("the premise step fails as designed") carries no base failure text, and the gate log is not on disk, so the base failure cannot be confirmed from the main-side record. The draft's Proof wording, which names the assertion the test makes rather than a logged message, stands (part p3, finding 6). Gate state stands per the receipt: green at the live tip (OverseerProcessorExitTest 1 of 1; shipped tree byte-identical to the gated tree).
- Draft corrections owed: strip the owner-notes header and set the Proof date from the record (takeover log, round 35, 2026-10-07).

### SOLR-9155 (draftable)

- ADOPTED: the interrupt surfaces as a SolrException with the flag restored, as drafted; the draft's Choice poses the declared-InterruptedException route to the maintainer.
- ADOPTED: the conflict-path remedy stays as the branch has it (part p3, finding 10, option b declined as well). The generic catch is not narrowed and the remedy text is not reworded in this branch: the cause chain keeps the specific conflict message, the draft's Limits disclose that the generic message also covers the conflict case, and the rethrow option's effect on other SolrExceptions inside the try is unchecked (getLeaderProps was not read). The follow-up offer in Limits covers this path if a maintainer asks.
- Draft corrections owed: strip the owner-notes header, set the Proof date from the record (receipts ledger, round 27, 2026-10-06), and move the Proof section to the formula order (after "What this change does", before the Choice and Limits).
- Gate state: green at the live tip per the receipt (ZkControllerGetLeaderTest 1 of 1, proof discriminates).

### SOLR-15386 (draftable)

- ADOPTED: the single check against the Overseer's cached live node list is the position, as drafted. The draft's Choice puts the question to the maintainer with both alternatives from the ticket comments (a second check after the update is computed; a conditional ZooKeeper update) and their costs.
- Draft corrections owed: strip the owner-notes header and set the Proof date from the record (takeover log, round 35, 2026-10-07); move the Proof section to the formula order.
- Gate state: green at the live tip per the receipt (NodeMutatorTest 3 of 3). Proof inconclusive by construction; the draft says so, in the formula's required phrasing.

### SOLR-11288 (draftable, partial fix): DISCUSS on one call

- DISCUSS: blank-only input. A blank-only value now behaves like an omitted one in all three APIs, so a blank `nodes` value starts a rebalance across every live node where it used to fail. Recommendation: keep blank-only equals omitted, as implemented; it follows each API's existing omitted-value rule, and the draft's Choice poses it to the maintainer. The call is not taken here because the report leaves it open with no recommendation and the behavior change is one a maintainer might plausibly reverse.
- Record disagreement, receipt wins on gate state: the receipt attributes the two base runs to different failure points (one the padded repeat, one the exact repeat). The audit shows the shipped test asserts the exact repeat first (TestCollectionAPI line 321), so a base run cannot reach the padded case (part p4, finding 2). The gate result and the counts (TestCollectionAPI 4 of 4 at the live tip) stand per the receipt; the per-run attribution is unverified, the gate log is not on disk, and the draft does not use the attribution. No draft change follows.
- Correction owed (branch): the changelog title understates the change; the replacement title is in part p4, finding 1 (it names the blank-only behavior). One branch commit before opening, whichever way the DISCUSS call goes.
- ADOPTED: the escaping gap stays in Limits with the follow-up offer, as the receipt requires.

### SOLR-13369 (draftable)

- ADOPTED: the test-side rule, as drafted. The draft's Choice poses the router-change route to the maintainer; the position here is the one the round 35 record stands behind (premise by construction on the ticket's Jenkins record and the router reading).
- Gate state: green at the live tip per the receipt (TriLevelCompositeIdRoutingTest 1 of 1). The proof was not obtained by run, and the draft says exactly that; it does not claim a failing base run.

### SOLR-14919 (drafted, opening held): DISCUSS

- DISCUSS: the RecoveryStrategy marker. The processor half is gated and proven; the recovery half sets `commit_end_point=true` on every replica recovery commit in every cluster, has no test, and re-enables a marker upstream commented out in `75b183196798` (the SOLR-12801 batch, which also lists SOLR-12933). The reason for the 2018 change has not been read. Recommendation: ship the processor change alone in this PR, and move the recovery marker to a follow-up with its own test, named in Limits with a stated plan to submit it, after the 2018 reason is checked. The call is not taken; the branch stays whole and the opening stays held until the owner rules.
- Gate state: green at the live tip per the receipt for what the gate ran (IgnoreCommitOptimizeUpdateProcessorFactoryTest 1 of 1, proof discriminates for the processor). The receipt's proof does not cover the recovery hunk.
- Correction owed (branch, whenever the branch is next touched): the test comment at IgnoreCommitOptimizeUpdateProcessorFactoryTest lines 63-66 misstates which hop carries which marker; the replacement text is in part p4, finding 5.
- Interaction: the RecoveryStrategy hunks of SOLR-14919 and SOLR-7394 share no lines and trial merges are clean in both orders (parts p4 and p6). No landing dependency either way.

### SOLR-17680 (draftable)

- ADOPTED: the moved field check, as drafted; the draft's Choice poses the up-front-check route. The draft names the v2 aliases endpoint starting to work, as the receipt requires.
- ADOPTED: the branch changelog title is left as it is. The audit marks the v2-naming title optional (part p4, finding 9); the PR text carries the v2 fact.
- Gate state: green at the live tip per the receipt (CreateRoutedAliasTest 15 of 15, CreateAliasAPITest 13 of 13, proof grounded).

### SOLR-17733 (draftable)

- ADOPTED: the public DELETE owns the shared cleanup, as drafted; the draft's Choice poses the 400 route. The draft states the contract change (an API delete now removes the ZooKeeper entry) and that the ticket's sync complaint was not reproduced, as the receipt requires.
- Gate state: green at the live tip per the receipt (TestDistribFileStore 1 test; the base run fails with expected 200, was 500).

### SOLR-13239 (submission-held; record verified)

- No draft, correctly. The audit verifies the record: gate green as-is at the live tip per the receipt (ZkStateReaderTest 17 tests, 1 skipped, 0 failures; TestCloudCollectionsListeners 2 of 2). The submission hold is the owner's standing decision and is unchanged.
- Correction owed before any future submission, if the hold is ever lifted: `SOLR-13239-TESTING.md` is still committed on the branch (commit `09725b66e6b`) and comes out first (part p4, finding 13). The branch is not touched while held.

### SOLR-18277 (retire confirmed): DISCUSS on deletion

- The audit confirms the merged state: its PR merged on apache/solr on 2026-10-05, the head `17c0e644812` matches the live fork head, and the merged files match the branch. Nothing to open; no draft, correctly. Gate state per the receipt: green (hardened) at the tip, proof inconclusive, recorded as such.
- DISCUSS: delete the retired fork branch `solr-18277-submit`, and the two CI branches `ci/solr-18277` and `ci/solr-18277-proof`. Recommendation: delete all three; the work is merged and nothing else references them on the record. Not executed; branch deletion is the owner's call.

### SOLR-16013 (held; no draft): DISCUSS on the history rewrite

- Gate state, receipt wins: gated at `e1bd21fd11a` (OverseerCloseOrderingTest 1 of 1, proof passed with the rewritten pinning test). The live tip `ba26b702891` adds an interrupt-safe wait (`002d0d08a45`, which fixes the round 28 P1 finding) and a second test; neither has a run. No draft can state an honest Proof at the live tip.
- DISCUSS: approve a history rewrite of the fork branch that drops the `Co-Authored-By` trailer from commit `3059f9be884`. Recommendation: approve the rewrite, without squashing the five commits. Trailers of that kind are barred by the owner's standing rule, and the branch is unopened. Not executed here; rewriting the fork branch is the owner's explicit call.
- Owed after the call: a re-gate at the rewritten head (main-side work, below), which must also watch the two timing points in part p3, finding 19. The changelog title replacement in part p3, finding 3 goes in with the rewrite.
- ADOPTED: landing order for the overseer cluster (part p3, finding 16): 13186, 15106, 9155, 15386, then 16013 last, since it needs the rewrite and re-gate first.

### SOLR-11479 (audit only, held)

- Gate state: NO GATE per the receipt; the live tip `7e538e844c4` is the code commit. A first gate is main-side work owed.
- Corrections owed before the first gate (branch): delete `SOLR-11479-TESTING.md` from the branch diff (part p2, finding 1), and correct the changelog title, which claims the alias behavior without the one-replica limit (replacement in finding 2). The 400 message naming only `coreNodeName` (finding 3) is a wording fix that can ride the same commit.
- ADOPTED: add a test for the several-replica rejection before the first gate (part p2, finding 4 and decision 7). The rejection is half the change; a gate that proves only the alias half is not enough. This directs the main-side lane that packages the branch; it is not done here.
- ADOPTED: lands after SOLR-15035 (see its entry).

### SOLR-3865 (audit only, not ready): DISCUSS on the ticket link

- Gate state: NO GATE per the receipt. The audit's central finding stands: the leak path the ticket names (a ZkStateReader leaked when connect fails) is already closed on main; the branch fixes a different, real gap (close before first use) in the same class.
- DISCUSS: keep the SOLR-3865 link on the changelog and PR, with the text stating plainly that the reported path is already closed on main and this change covers close-before-first-use; or drop the link. Recommendation: keep the link with the disclosure. The gap is in the class the ticket is about, and dropping the link would orphan the change. The call is not taken; it is a premise call of the kind the owner has decided himself in earlier rounds.
- Owed before any first gate (main side): the premise run on the audit's spec (base with the branch tests: both tests fail with a connect failure, not AlreadyClosedException), the missing short connect timeout in the second test (part p5, finding 7, branch fix), and the handoff-note cleanup in the cross-cutting items. The behavior change to state in any draft: after close, every later call throws AlreadyClosedException, including a call that used to connect again (finding 8).

### SOLR-4754 (audit only, not ready): DISCUSS on keeping the guard

- Gate state: NO GATE per the receipt. The audit's findings stand: the guard fires only for a scheme-only or blank host value, not for the ticket's empty value, which already falls back to the advertised address on main; the error text names `-Dhost`, which the shipped solr.xml does not read (it reads `solr.host.advertise`); and the proof cannot fail on base by construction, because the test calls a helper that is private and non-static on base (part p5, findings 2 to 4).
- DISCUSS: keep the scheme-only guard as the whole change, with the error text and the changelog title corrected and the PR text disclosing that the ticket's empty-value case is already handled on main; or drop the guard. Recommendation: keep it with those fixes. A scheme-only host would otherwise register an empty host name silently, which is a real failure worth a clear error. The call is not taken.
- Owed before any first gate (main side): the error-text replacement and changelog title replacement from part p5, findings 2 and 3 (branch fixes), a premise run showing a node with `solr.host.advertise=http://` registering an empty host on base, and the handoff-note cleanup. No text may claim a failing base run for the current test; its fail-before is inconclusive by construction.

### SOLR-10234 (audit only, plausible): DISCUSS on the route

- Gate state: NO GATE per the receipt. The change pins `@SuppressFileSystems({"ExtrasFS", "HandleLimitFS"})` on the two cloud test base classes; by reading, the pin test fails on base (base inherits only ExtrasFS) and the change compiles.
- DISCUSS: keep the suppression on the base classes as implemented, which every cloud test inherits, or pursue the per-node limit, which needs a Lucene-side change. Recommendation: keep the suppression; it is the ticket's own first option, and the per-node limit is a separate Lucene-side project. Second call in the same item: drop the changelog fragment for this test-only change (its type is `other`, and the dev-docs guidance says most such changes are too minor for an entry). Recommendation: drop it. Neither call is taken.
- Owed after the call (main side): the premise run (the pin fails on base) and a first gate. Any Proof text states that the change pins the annotation only and does not reproduce the 2048 handle limit (part p5, finding 15).

### SOLR-10641 (audit only, no draft): DISCUSS on shipping the pin

- Gate state: NO GATE per the receipt, and the receipt's open owner call is the same one the audit reaches: whether a passing pin ships as its own PR. The audit confirms both new tests pass on base too (the NOT_PROVEN expectation stands), and that the production change (the response write and request delete as one multi) handles both failure shapes by falling back to the old sequential code (part p5, findings 9 and 10).
- DISCUSS: ship this branch as its own small PR, or fold it into a later change. Recommendation: ship it as its own PR once the tidy fold-in and a first gate land; the text states plainly that the tests pin behavior and do not measure round trips, and the proof is inconclusive by construction. The call is not taken.
- Correction owed (branch): the tidy fold-in the receipt names is still owed; the replacement lines are in part p5, finding 6. A first gate follows it (main-side work, below).

### SOLR-16437 (audit only; draftable after a first gate)

- Gate state: NO GATE per the receipt; the 2026-10-07 GitHub corroboration run at the head does not settle gate state. The audit finds the premise holds in both modes by reading, and the new test fails on base at its first assertion (part p5, finding 11). A first gate is main-side work owed (below); the branch is draftable once it lands.
- ADOPTED: scope stays ADDREPLICAPROP only, as implemented. DELETEREPLICAPROP and the other APIs the ticket names go in the draft's Limits with the follow-up offer (part p5, finding 12), per the standing follow-up rule.
- ADOPTED: the wrong-shard behavior change is stated in the draft: a valid replica name with the wrong shard is now a 400, where before it changed the replica in the other shard (part p5, finding 13).

### SOLR-17281 (parked, not ready): DISCUSS on revert or replace

- Gate state: NO GATE per the receipt; the branch is parked under review on the owner's side. The branch's own round 6 review and its DECISION file agree the one-line change points the wrong way and name the same choice; the ticket packet has no logs.
- DISCUSS: revert the one-line change (the branch becomes empty) and ask the reporter for the election logs and terms.json; or replace the change once the root cause is known. Recommendation: revert, and ask for the logs. The branch stays parked either way, and no gate is owed until the call is taken. Not executed here.

### SOLR-7394 (live PR, consistency pass)

- Consistency result: consistent. Head, counts, and proof in the live text match the receipt exactly (gate green at the live tip `9857d9ee802`). No gate owed.
- DISCUSS (small): the live PR title and the branch changelog omit the restore half of the change (the failed recovery writes back the term saved at recovery start). Suggested wording is in part p6, finding 12. Recommendation: apply it; the title rule is accuracy, and the restore is half the change. A title edit on a live PR is the owner's call, so it is not taken here.

### SOLR-12998 (live PR, consistency pass: drift)

- Receipt wins: the gate covers head `25520806c26`. That head was rewritten (the same trees re-parented) and one code commit, `62a17a116b5`, followed it (CoreAdminOperation and its test; the audit read the hunk and found the branch structure unchanged). The live tip is not gated. A gate at `62a17a116b5` is main-side work owed before any further proof claim.
- Corrections owed (live PR text): the Proof sentence "The local gate passes" uses a banned process word in public text; the replacement is in part p6, finding 4. The changelog parenthetical "(fixed)" in the text comes out. The sentence claiming a runner verification of the previous head `ee342298ba4` on 2026-10-04 has no receipt line; the receipt names the tidy commit `0f4eead1f99`, which has the same tree, so the claim is confirmable as same-tree only. The main side confirms or the sentence comes out (owed, below).
- ADOPTED: cut the CI sentence about `TestUserManagedReplicationWithAuth` being pre-existing (part p6, finding 5). The claim is unchecked, the fork run IDs it rests on cannot be re-read while Actions is suspended, and the PR's current checks all pass.

### SOLR-13136 (live PR, consistency pass: drift): DISCUSS on the behavior

- Receipt wins: the gate covers head `2ad44409ffe` and the route-B behavior it proved (the slice is deleted when the replica wait or the buffered-updates apply fails). The live tip `486b3877556` reverses that behavior: the failure propagates and the shard stays in CONSTRUCTION, because replicas can hold acknowledged writes. The PR's Choice states the live position correctly; the gap is proof, since the receipt's proof covers the deleting behavior. The PR Proof's claim of a gate record at the live tip, and its counts, are not in the receipt and must not stand unverified (part p6, finding 1).
- DISCUSS: keep the live behavior (shard stays in CONSTRUCTION after a failure once replicas exist), or restore the gated delete. Recommendation: keep the live behavior; deleting a shard whose replicas may hold acknowledged writes is the riskier default. The call is not taken. A gate at `486b3877556` follows the call (main-side work, below).
- Corrections owed (live PR text): the Proof sentence and counts come out or are replaced after the gate, and the process wording ("this round") comes out either way; replacement wording is in part p6, finding 1. The title and changelog, which state only the CONSTRUCTION change, are revisited after the behavior call (finding 13).

### SOLR-18391, submit branch (live draft PR): DISCUSS on closing it

- Head matches the branch tip `3000eeede7a`; the receipt records no gate at the tip. The PR body's verification sentence dated 2026-10-04 is not in the receipt; the main side confirms it or the sentence comes out (owed, below).
- DISCUSS: close the draft PR as superseded by the graceful-create PR, or keep it as a draft. The graceful-create PR's own text already says this route "stays as a draft and is superseded"; this PR's body does not say so. Recommendation: close it as superseded. Keeping a second, broader draft invites split review of the same ticket. Not executed; PR state changes are the owner's.
- The alias-failure deletion question is not reopened; see the graceful-create entry.

### SOLR-18391, graceful-create branch (live PR)

- Gate state, receipt wins: gated green at `3a0ff1262bf` (CreateCollectionCleanupTest 4, PlacementPluginIntegrationTest 8, OverseerCollectionConfigSetProcessorTest 26, DeleteCoreRemnantsOnCreateTest 5, all 0 failures; proof discriminates). The live tip `adcda10b501` is eight commits past the gate. A gate at the live tip is main-side work owed.
- The alias-failure deletion question is not reopened: the recommendation on record (keep the delete) stands and is already posed in the PR's Choices.
- Corrections owed (live PR text and one branch file; part p6, findings 6 to 10): the Proof says the head differs from the verified head by "one later commit"; four commits follow it. "Round 29 follow-up" and "internal handoff document" are process wording. Two count claims in the body (CreateCollectionCmdRetryTest 4 of 4; CreateCollectionCleanupTest 5 of 5 at the tip) are not in the receipt; the main side confirms which runs exist, and the base-run setup sentence is corrected to the receipt's setup (the two production files at base). The Limits first bullet omits failover; the replacement is in finding 8. Every file link points at an old SHA on the upstream repo instead of the fork blob at the live head, and anchors at line 446 and up shift by five; the mapping is in finding 10. The branch changelog title overstates the alias delete; the replacement phrase is in finding 9, and fixing it needs a branch commit.
- Presentation hold: this PR carries an approving maintainer review, so under the formula its description is not edited while a maintainer is active on it without the owner's check. The corrections above are recorded as owed and flagged for the owner, not silently applied.

## Cross-cutting adoptions

- Landing orders: overseer cluster 13186, 15106, 9155, 15386, 16013 (part p3); AddReplicaCmd pair 15035 before 11479 (part p2); mzxid pair 15674 before 15478 (part p1). The backup pair (12651, 15863) needs no order, and 14919 against 7394 has no dependency.
- ADOPTED: no rebase of the gated branches. They sit 37 to 67 commits behind current main with clean trial merges; a rebase would change every head and every Proof line, and the gates stand at the recorded heads. The audit-only branches are rebased only as part of their pre-gate packaging, when that work is directed.
- ADOPTED: the six audit-only branches (3865, 4754, 10234, 10641, 16437, 17281) get their handoff-note removal and the "Hypothetical, unrun" commit-body cleanup as part of each branch's pre-gate packaging (part p5, finding 1), not as a separate rewrite now. The SOLR-16013 trailer rewrite is the exception and is a DISCUSS item above, because that branch is gated and its head is on record.
- ADOPTED: process words in the live PR branches' commit subjects (part p6, finding 14) are left as they are. No history rewrite of live PR branches; squash on merge is the maintainer's choice at merge time.
- ADOPTED: the live PR bodies are not restyled to the 2026-10-08 presentation rule (part p6, finding 15). They predate it; only the factual and process-word corrections listed per ticket are owed.
- ADOPTED: no single rule for the optional changelog author field (part p5, finding 7). Each branch stays as it is.
- ADOPTED: the verification dates in the drafts come from the main-side record (receipts ledger and takeover log dates), in the "recorded" form several drafts already use: 15035 recorded 2026-10-03, 12651 gated head verified 2026-10-04, 15863 recorded 2026-10-08, 17680 and 17733 recorded 2026-10-04, 17292 verified 2026-10-04, 15674 verified 2026-10-03. Part p2, finding 21 is answered by those record dates.

## Draft corrections owed (draft-fix pass; not done in this pass)

1. SOLR-9155, SOLR-13186, SOLR-15106, SOLR-15386: remove the owner-notes header at the top of each draft, including the internal "Open: the gate record gives no run date" line, and replace the "[date to confirm]" in each Proof with the recorded date (9155 and 13186: 2026-10-06; 15106 and 15386: 2026-10-07).
2. SOLR-12651: replace the bracketed Proof instruction line with the live-tip run result once the gate at `f3131d1ee84` lands; give the Choice section a bold one-line summary opener.
3. SOLR-15863: give the Choice section a bold one-line summary opener.
4. SOLR-15035: fill the Proof placeholder with the observed base failure line, or reword it to the receipt's recorded outcome if the line cannot be recovered (see the ticket entry).
5. SOLR-9155 and SOLR-15386: move the Proof section to the formula order (after "What this change does", before the Choice and Limits).
6. Limits sections that open with bullets instead of a bold one-line summary (SOLR-5813, 9155, 11288, 12991, 13186, 13369, 14919, 15106, 15386, 15674, 17292, 17680, 17733): add the bold opener at the draft-fix pass.
7. SOLR-15674: the draft runs about 4.4 KB, over the 3,500-character guide, because of the citation links and the pair and signature-change content. Accepted as a complex ticket; trim at the draft-fix pass only if the openings slate wants it.
8. Checked and clean: no internal process vocabulary in any draft's PR text (the only hits are in the owner-notes headers that come off), Proof numbers match the receipts, titles in the four drafts that carry a suggested PR title (9155, 13186, 15106, 15386) are accurate, and no draft names a Lucene version, so the cross-version rule is not triggered.

## Branch corrections owed (not drafts; for the lanes that touch the branches)

1. SOLR-12651: changelog title replacement (part p2, finding 9).
2. SOLR-15863: changelog title capitalization (part p2, finding 17).
3. SOLR-11288: changelog title replacement naming the blank-only behavior (part p4, finding 1).
4. SOLR-11479: delete `SOLR-11479-TESTING.md`; changelog title correction; the 400 message wording (part p2, findings 1 to 3).
5. SOLR-16013: changelog title replacement, with the history rewrite (part p3, finding 3).
6. SOLR-14919: test comment correction (part p4, finding 5).
7. SOLR-18391 graceful-create: changelog alias wording (part p6, finding 9).
8. SOLR-13239: `SOLR-13239-TESTING.md` removal, only if the submission hold is lifted.
9. SOLR-17292: the node-down remedy (adopted in its entry above), then the draft updates that follow it.

## Main-side work owed (gates first)

1. SOLR-12651: gate at the live tip `f3131d1ee84` before opening.
2. SOLR-17292: re-gate after the adopted node-down remedy lands in-branch.
3. SOLR-16437: first gate (the new test passes with the change and fails without it). The branch is draftable once it lands.
4. SOLR-11479: first gate, after the branch corrections above and with the adopted several-replica test added during packaging.
5. SOLR-12998: gate at the live tip `62a17a116b5`.
6. SOLR-13136: gate at the live tip `486b3877556`, after the owner's behavior call.
7. SOLR-16013: re-gate at the rewritten head, after the owner's rewrite call.
8. SOLR-18391 graceful-create: gate at the live tip `adcda10b501`.
9. SOLR-3865, SOLR-4754, SOLR-10234, SOLR-10641: premise run and first gate each, after the owner's call on each ticket and the pre-gate cleanup in the cross-cutting items.
10. Confirm-or-drop checks: the SOLR-18391 submit PR's 2026-10-04 verification sentence; the SOLR-12998 PR's previous-head runner sentence (same-tree confirmation only, per its entry); the SOLR-18391 graceful-create PR's two unrecorded tip count claims and its base-run setup wording.
11. No gate owed: SOLR-13239 (held), SOLR-17281 (parked, pending the owner's call), SOLR-18277 (merged), SOLR-7394 (consistent at the gated tip).

## DISCUSS list (recommendations recorded; calls not taken)

1. SOLR-14919: ship the RecoveryStrategy marker in this PR, or the processor alone. Recommendation: processor alone; the marker becomes a named follow-up with its own test.
2. SOLR-13136: keep the live CONSTRUCTION behavior, or restore the gated delete-on-failure. Recommendation: keep the live behavior.
3. SOLR-11288: blank-only input means "all", or keeps failing. Recommendation: keep blank-only equals omitted.
4. SOLR-16013: approve the history rewrite dropping the co-author trailer from `3059f9be884`. Recommendation: approve, no squash.
5. SOLR-18391 submit PR: close as superseded by the graceful-create PR, or keep as a draft. Recommendation: close.
6. SOLR-18277: delete `solr-18277-submit` and the two CI branches. Recommendation: delete all three.
7. SOLR-17281: revert the one-line change and ask the reporter for logs, or replace it later. Recommendation: revert; parked either way.
8. SOLR-3865: keep the ticket link with the disclosure that the reported path is already closed on main, or drop the link. Recommendation: keep with disclosure.
9. SOLR-4754: keep the scheme-only guard with its text fixes, or drop it. Recommendation: keep with fixes and disclosure.
10. SOLR-10234: suppression on the base classes as implemented, or a Lucene-side per-node limit; and drop the test-only changelog fragment. Recommendation: suppression as implemented; drop the fragment.
11. SOLR-10641: ship the passing pin as its own PR, or fold it into a later change. Recommendation: ship it, after the tidy fold-in and a first gate.
12. SOLR-7394: add the restore half to the live PR title and the branch changelog. Recommendation: apply the suggested wording.
