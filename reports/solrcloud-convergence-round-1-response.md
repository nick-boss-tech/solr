# SolrCloud convergence, round 1: main-side response

Round 1 returned NOT CONVERGED: 12 of the 18 drafts not converged, with one blocking finding (SC-R1-21), 17 should-fix findings and 26 notes. This response verifies every finding against the receipts and the branch code before applying, applies each holding finding, records two findings that do not hold or are not applied (SC-R1-28 rebutted on live Jira evidence; SC-R1-51, an optional note, declined on the ticket's own text), and corrects two places where the round found the answers record itself wrong (SC-R1-50, SC-R1-52). Application method: exact-string replacements, each asserted to occur exactly once before applying; the full diff was then reviewed file by file.

## Counts

| Grade | Items | Applied | Not applied |
|---|---|---|---|
| Blocking | 1 (SC-R1-21) | 1 | 0 |
| Should-fix | 17 | 16 | 1 (SC-R1-28, rebutted) |
| Note | 26 | 9 as draft edits, 2 as answers corrections | 15 dispositioned without an edit (branch-side records, length acceptances, already-satisfied or explained items, one declined rewording) |

## The rebuttal: SC-R1-28 (SOLR-15035, should-fix, not applied)

The finding prescribes deleting the sentence "The ticket summary describes this difference." from the draft's What section, because no Jira packet for this ticket is on disk. The packet's absence is real (the round 1 audit, part p2 finding 8, records the same gap, and the answers owed a summary confirmation before opening). The main side made that confirmation against live Jira on 2026-10-11 instead of deleting a true sentence: the summary of SOLR-15035 reads "core.properties different when using ADDREPLICA .vs. when the replica created with CREATE". That is the difference the draft's first paragraph states (a replica added with ADDREPLICA has no numShards entry in its core.properties; a replica created with CREATE has one). The sentence stands. The confirmation the answers owed is now done, and round 2 is asked to judge the sentence against the quoted summary.

## The date-form pick: SC-R1-30

The note asks the main side to pick one form for the set. The pick follows the answers record, which prescribes the date form per draft: dates that come from a receipts-ledger or takeover-log record are stated as "recorded", and the answers' draft corrections list (item 1) names SOLR-9155, 13186, 15106 and 15386 for the recorded form, with per-ticket forms in the cross-cutting list (15035 recorded 2026-10-03, 15863 recorded 2026-10-08, 17292 verified 2026-10-04 for the pre-remedy head, 15674 verified 2026-10-03). Applied: SOLR-13186, 15106 and 15386 (the note's named drafts) and SOLR-9155 (same answers item, same provenance), plus the should-fix instances SC-R1-29 (15035) and SC-R1-41 (15863). One disclosed deviation: the SC-R1-21 replacement bullet in the round 1 register carries "verified on 2026-10-07"; it is applied with "recorded 2026-10-07" under this pick, and the rest of the replacement is verbatim. Drafts whose dates are run dates or whose prescribed form is "verified" (5813, 11479, 12651, 15674, 16437, 17292) are unchanged.

## Fact checks before applying

- **SC-R1-21 (SOLR-15106, blocking): holds.** `OverseerProcessorExitTest` calls `getCollectionProcessorThread()` at lines 36 and 46 of the test at head `40b7e5d0efa`. The accessor is added by the branch (Overseer.java L827-L829 at the head, the register's citation) and is absent at the merge-base `9b3a84b1c460`. The base code cannot compile the test, so the draft's base-failure narrative was unsupported. The receipt's proof line ("the premise step fails as designed") does not record a base assertion run and does not contradict this.
- **SC-R1-22: holds.** `OverseerTaskProcessor.java` is not in the branch diff (the diff is Overseer.java, the new test, and the changelog). At the merge-base, L185-L186 are the `IllegalStateException` catch and its `return`.
- **SC-R1-23: holds.** The branch's Overseer.java hunks sit at head lines 654 to 847; L451-L492 is byte-identical between the head and the merge-base.
- **SC-R1-24: holds.** The ZkController block at head L3053-L3060 is byte-identical to the merge-base (`e2cdb2d7e8ae`) block at L3043-L3050.
- **SC-R1-25: holds.** The code's own condition and comment say the path runs when distributed cluster state updates are enabled (`isDistributedStateUpdate()`; "when distributed cluster state updates are enabled, we mark the node down synchronously from this thread"). The Overseer still runs in that mode; "used when the Overseer is disabled" was wrong.
- **SC-R1-26: holds.** `DistributedZkUpdateProcessor.java` is not in the SOLR-14919 branch diff. At the merge-base `b5c71bc5573c`, L215-L237 carry the commit_end_point handling (the `leaders` forward and the leader's own commit with `replicas`) that the RecoveryStrategy marker re-activates.
- **SC-R1-27: holds.** At head `12d7491e82c`, `paramToProp` (CoreAdminHandler.java L239-L251, which this branch extends with numShards) is consumed only by `org.apache.solr.handler.admin.api.CreateCore` (L133-L134, "Copy standard core create parameters from paramToProp allowlist"), the v2 create path. The receipt says the same ("the v2 CoreAdmin create path copies only its allowlist"). The second replacement is applied to the sentence's first clause; the tail (", and the count was not on that list, so the test still failed.") is kept, and "that list" now reads against the allowlist.
- **SC-R1-29: holds.** receipts/SOLR-15035.md records the gate in the receipts ledger table dated 2026-10-03; the date is a record date.
- **SC-R1-41: holds.** receipts/SOLR-15863.md records the gate in the takeover log (round 36, 2026-10-08); the draft's "verified 2026-10-08" took the record date for a run date.
- **SC-R1-42: holds.** At the merge-base `e2cdb2d7e8ae`, CollApiCmds.java L364-L365 are DeleteReplicaPropCmd's `checkRequired` call, the only validation that command performs; at the head the same lines sit at L388-L389, past this branch's own addition, which is why the draft's head link pointed at pre-change code without saying so.
- **SC-R1-43: holds.** At the merge-base, `PerReplicaStatesOps.persist` logs nothing: stale failures (NodeExists, NoNode) are retried with a refresh at INFO, any other KeeperException propagates at once, and an all-stale run returns silently. At head `f614a42fbc8` the ERROR line sits after the loop, immediately before the throw of the last stale exception. At the node-down call site, the head wraps persist in a try that logs a warning (ZkController.java L3058-L3068) and the DOWNNODE offer follows at L3074-L3079; at the merge-base the unwrapped persist call let a KeeperException skip that offer.
- **SC-R1-44: holds.** CreateAliasCmd at the merge-base builds the message as "A routed alias requires these params: " plus `RoutedAlias.MINIMAL_REQUIRED_PARAMS` plus " plus some create-collection prefixed ones."; the set is `router.name` and `router.field`, rendered in the ticket's order as [router.field, router.name]. TimeRoutedAlias.REQUIRED_ROUTER_PARAMS at the head has exactly five entries (name, router.name, router.field, router.start, router.interval). Both replacement texts are applied verbatim.
- **SC-R1-45: holds, on the draft's own Proof.** The Proof states the test also checks that a local-only delete still returns 400 (TestDistribFileStore L210-L213 at the head), so "the test covers only the normal path" understated the coverage; the replacement scopes the sentence to the failed-delete case, which the test does not cover.
- **SC-R1-46: holds.** DistribFileStore.java at the merge-base `56ec140e3636`, L495-L510, is the same async remote-delete loop the draft linked at head L496-L511, and L162-L168 (the local delete logging its error without raising it) is identical at both commits.
- **SC-R1-47: holds.** At the merge-base, the version comparison (`result.version == res.second()`) is in `IndexSchemaFactory.getFromCache`; SolrConfig.java L199-L209 calls that method and reads the returned version. The wording now credits the check to IndexSchemaFactory and the call to SolrConfig.
- **SC-R1-54: the receipt supplies the counts.** receipts/SOLR-17680.md records the base leg (both production files at base, final tests in place): CreateRoutedAliasTest 15 tests, exactly 1 failure; CreateAliasAPITest 13, exactly 1 failure. The draft now states them, and "pins" becomes "checks" in both places.
- **Merge-bases.** Computed against upstream main for every touched branch, they match the bases the drafts and the register cite: 15106 `9b3a84b1c460`, 15386 and 16437 `e2cdb2d7e8ae`, 14919, 15035 and 15674 `b5c71bc5573c`, 17292 `14c7aac0d151`, 17680 and 17733 `56ec140e3636`, 11479 `cabedd1d968` (also stated in its receipt). All eighteen live heads match the drafts; none moved during this application.

## Per-finding dispositions

### SOLR-15106
- SC-R1-21 (blocking): applied. The Proof bold line and bullet are the register's replacements (bullet with the date phrase in the recorded form, per the pick above); the draft no longer claims a base assertion failure, and the proof reads inconclusive by construction.
- SC-R1-22, SC-R1-23 (should-fix): applied. Both links now sit at the merge-base with "unchanged by this branch, linked at the merge-base".
- SC-R1-30 (note): applied under the pick ("recorded 2026-10-07").
- SC-R1-37, SC-R1-38 (notes): see Record items.

### SOLR-9155
- SC-R1-03 (should-fix): applied verbatim. Each clause of the replacement matches the Limits bullet under it: the conflicting-leader check still throws inside the try; the generic message also covers that case; the test interrupts before the call.
- SC-R1-30 pick applied to its Proof date as well (same answers item): "recorded 2026-10-06".
- SC-R1-08 (note, length about 4,950 characters): accepted, recorded here. The length is the interrupt behavior disclosure (the restored flag is visible to later blocking calls on the thread, with the two callers named) plus a genuine signature Choice; the formula's guide allows over-length for the unusually complex case.

### SOLR-11479
- SC-R1-01 (should-fix): applied. The Limits bullet on the unrebased branch now ends "This branch is meant to land after SOLR-15035.", the order the answers adopt.
- SC-R1-10 (note): applied. The citation now reads "(merge-base, line 352)"; the receipt confirms `cabedd1d968` is the branch's merge-base, so the stricter word is the accurate one.

### SOLR-12651
- SC-R1-02 (should-fix): applied. The HOLD comment line is deleted. Its substance is not lost: the owed changelog title commit is in the answers' branch corrections list and in SC-R1-05 below.
- SC-R1-06 (note): applied. The Limits now carry "This branch is meant to land after SOLR-9598.", the order the Replication and backup answers adopt (both branches change RestoreCmd.java and trial-merge cleanly).
- SC-R1-05 (note): recorded as a branch-side item; no draft edit. See Record items.

### SOLR-14919
- SC-R1-26 (should-fix): applied (merge-base link form, as verified above).
- SC-R1-34 (note): applied. The Choice now states "The reason for that 2018 change has not been read." The commit `75b183196798` is dated 2018-11-29 in the local history, and the answers' DISCUSS entry records the same unread status.
- SC-R1-33, SC-R1-35 (notes): recorded as branch-side items; no draft edit. See Record items.
- SC-R1-38 (note, length about 4,800 characters): accepted, recorded here. The draft carries two changes (the processor parsing and the recovery marker) and the marker Choice; if the owner takes the answers' DISCUSS recommendation to ship the processor alone, the draft shortens with the split.

### SOLR-15035
- SC-R1-27 (should-fix): applied (both replacements; construction note in the fact checks).
- SC-R1-28 (should-fix): NOT applied; rebutted above on the live Jira summary.
- SC-R1-29 (should-fix): applied ("recorded 2026-10-03").
- SC-R1-31 (note): no edit; satisfied in substance. The answers owed the observed base failure line pasted into the Proof, or the sentence reworded to the receipt's recorded outcome if the line could not be recovered. The line was searched for in the gate logs on this host (g15035-harden.log, g15035-harden2.log, g15035-harden3.log, g15035-coreadmin.log) and is not in them. The draft's Proof already states the receipt's outcome for both legs: on base the new core has no numShards entry, and with the ADDREPLICA change alone the count is dropped by the create path's allowlist (the second bullet, as amended by SC-R1-27).

### SOLR-15386
- SC-R1-24, SC-R1-25 (should-fix): applied.
- SC-R1-30 (note): applied under the pick ("recorded 2026-10-07").
- SC-R1-37, SC-R1-38 (notes): see Record items. Length (about 4,400 characters) accepted: the Choice prices both alternatives from the ticket comments and the draft carries three Limits; SC-R1-38's own text notes the answers accept length only for SOLR-15674, and this acceptance is the main side's recorded exception for the same reason as SOLR-15106 (about 4,000 characters; the early-return disclosure and its Limits carry it).

### SOLR-13186
- SC-R1-30 (note): applied under the pick ("recorded 2026-10-06"). SC-R1-37: see Record items. The draft was CONVERGED in round 1 and stays so; round 2 re-checks only this date form on this draft.

### SOLR-13369
- SC-R1-32 (note): applied. The receipt's wording is "a seed hunt over six further seeds"; the Proof now says "seed" in both places the register named.
- SC-R1-36 (note): recorded as a packaging item; no draft edit. See Record items.

### SOLR-15863
- SC-R1-41 (should-fix): applied ("recorded 2026-10-08").
- SC-R1-49 (note): recorded as a branch-side item; verified at the head, the fragment title begins "incremental backup properties now record...", lower case as the finding states. See Record items.

### SOLR-16437
- SC-R1-42 (should-fix): applied verbatim.
- SC-R1-51 (note): NOT applied. The offered rewording replaces "the other APIs the ticket mentions" in the Limits summary. The ticket's own text, read live from Jira on 2026-10-11, closes with "Maybe we should update this (and other APIs) to do a best-effort sanity-check of input parameters before shipping them off to the asynchronous work queue." The ticket does mention other APIs, in those words, without naming them; the draft's phrase matches it. The summary of SOLR-16437 is "ADDREPLICAPROP does not sanity-check inputs". Superseded, 2026-10-12 (SC-R2-21): the ticket names no other API; its closing sentence says "(and other APIs)".
- SC-R1-50, SC-R1-52 (notes): applied as corrections to the answers record; see Record items.

### SOLR-17292
- SC-R1-43 (should-fix): applied verbatim.
- SC-R1-53 (note): no edit; the note explains itself. The answers' "verified 2026-10-04" belongs to the pre-remedy head `e43200b0fb6`; the receipt records the remedy re-gate at `f614a42fbc8` finished 2026-10-10, which is the draft's date. Both records stand, each for its head.

### SOLR-17680
- SC-R1-44 (should-fix): applied verbatim (both replacements).
- SC-R1-54 (note): applied (base counts from the receipt; "checks" for "pins").
- SC-R1-55 (note): no edit. RoutedAlias.java and TimeRoutedAlias.java are not in the branch diff (verified), so the head links are accurate; the note itself allows either SHA.

### SOLR-17733
- SC-R1-45, SC-R1-46 (should-fix): applied.
- SC-R1-56 (note): no edit, same basis as SC-R1-55: the ClusterFileStore.java lines are unchanged by the branch and exist at the merge-base; either SHA is accurate.

### SOLR-5813
- SC-R1-07 (note): applied. "That path is not tested." is deleted from the What section; the Limits bullet ("Existing core.properties files with an empty `collection` value now load under the core name. This is not tested.") carries the point once.

### SOLR-11288
- SC-R1-04 (note): recorded as a branch-side item; no draft edit. Verified at head `cd094c3c623`: the fragment title names the repeat and blank-around-names behavior and does not name the blank-only behavior; the replacement title is already recorded in the answers (part p4, finding 1). When that commit lands, the draft's head references (Proof and changelog link) are re-read, as the finding directs.
- SC-R1-09 (note, length about 4,260 characters): accepted, recorded here. Three APIs each carry a stated behavior change (repeat, padded, and blank-only handling differ per API), and the Choice turns on the blank-only case.

### SOLR-15674
- SC-R1-47 (note): applied (wording in the fact checks above).
- SC-R1-48 (note): applied. The first Limits bullet now ends "This branch is meant to land before SOLR-15478.", the order the answers adopt. The paired version-reset wording was checked after the edit, as the answers require when either draft is edited: "A new znode starts that version over at 0." stands identical in this draft and in `pr-drafts/configsets/SOLR-15478.md`, and neither edit touched it.

## Record items

- **Answers corrections applied (SC-R1-50, SC-R1-52):** in `material/solrcloud-round-1-answers.md`, the SOLR-16437 entry's wrong-shard sentence now states the old behavior as the draft has it, with a dated correction note giving the merge-base evidence (lookup in any shard, DocCollection.java L382-L391; write only to the named shard, ReplicaMutator.java L184-L195). Its gate-state paragraph now records GATE GREEN at `aa2a6b8afb6f` (first gate finished 2026-10-10, per receipts/SOLR-16437.md), the summary gate list moves SOLR-16437 from "No gate recorded" to the gated-green list, and the main-side work list marks the first gate DONE. The stale summary list still names other tickets whose gates landed after 2026-10-09; only the SOLR-16437 line was in this round's scope, and the rest is left for a record-currency check.
- **Branch-side items recorded, no draft edit:** SOLR-11288 changelog title replacement (SC-R1-04) and SOLR-15863 title capitalization (SC-R1-49) and SOLR-12651 changelog title replacement (SC-R1-05) are the answers' branch corrections 3, 2 and 1 respectively; each moves its head when it lands, and the drafts' head references are re-read then. SOLR-14919: the changelog title check (SC-R1-33; the title at `84e7bcaeb57` claims the recovery outcome the untested recovery hunk carries) and the test comment correction (SC-R1-35; the comment at IgnoreCommitOptimizeUpdateProcessorFactoryTest L63-L66, replacement text in part p4, finding 5) ride the next branch touch, per the answers; the draft's Proof and links take the new head after that fix. SOLR-13369 (SC-R1-36): the changelog fragment kept for a test-only change is a packaging call, recorded here for consistency with the answers' SOLR-10234 recommendation (drop the fragment for a test-only change); the branch is test and changelog only per its receipt, and the draft describes the branch as it stands until packaging.
- **Titles (SC-R1-37): answered.** The four drafts the note names carried their suggested titles in the owner-notes headers the draft-fix pass stripped; no draft in this set carries a Title line now, and the titles are not recoverable from the round 1 parts on this branch. The main side's answer: drafts in this set do not carry title lines; the PR title is set at opening and checked for accuracy then, as the title rule requires. Round 2 should not re-raise this. Correction, 2026-10-12 (SC-R2-01, SC-R2-25): the titles are recoverable with `git show 0e287a38159:pr-drafts/solrcloud/<ID>.md` for SOLR-9155, 13186, 15106 and 15386; SC-R1-37 names three of them (13186, 15106, 15386).
- **Lengths accepted in this response:** SOLR-9155 (SC-R1-08), SOLR-11288 (SC-R1-09), SOLR-14919, SOLR-15106 and SOLR-15386 (SC-R1-38), each with its reason in its disposition above. SOLR-15674's acceptance already stands in the answers.

## What round 2 needs to settle

Round 2 re-checks the 12 drafts round 1 marked NOT CONVERGED (SOLR-9155, 11479, 12651, 14919, 15035, 15106, 15386, 15863, 16437, 17292, 17680, 17733), verifies each disposition above against the live draft text, and gives a verdict of CONVERGED or NOT CONVERGED per draft. It also judges the two points this response decided against the register: the SC-R1-28 sentence against the live Jira summary quoted above, and the SC-R1-51 phrase against the ticket's closing sentence, also quoted above. Two limited re-checks on drafts round 1 found converged: SOLR-13186 (only the SC-R1-30 date form) and SOLR-15674 (only SC-R1-47 and SC-R1-48, plus the paired version-reset wording against SOLR-15478). The assignment is `assignments/pool-solrcloud-convergence-round-2.md`.
