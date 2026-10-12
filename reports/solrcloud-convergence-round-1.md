# SolrCloud convergence, round 1

Verdict: **NOT CONVERGED.** Twelve of the eighteen drafts are NOT CONVERGED. Six are CONVERGED: SOLR-5813, 11288, 12991, 13186, 13369 and 15674. SOLR-12991 has no finding at all.

Counts: drafts checked 18. Drafts with no finding 1 (SOLR-12991). Drafts with a blocking finding 1 (SOLR-15106). Drafts with should-fix findings and no blocking finding 11. Drafts with notes only 5. Findings 44: 1 blocking, 17 should-fix, 26 note.

Scope: `assignments/pool-solrcloud-convergence-round-1.md`, with the answers in `material/solrcloud-round-1-answers.md`. Settled decisions were not reopened. Three read-only subagents covered the eighteen drafts in six-draft slices. Every head was checked with `git ls-remote origin refs/heads/solr-<n>-submit`, and all eighteen match the heads the drafts name. Merge-bases were computed against the local `upstream/main`. The drafts were not edited in this round.

## Verdict per draft

- **SOLR-5813: CONVERGED.** SC-R1-07 (note).
- **SOLR-9155: NOT CONVERGED.** SC-R1-03 (should-fix); SC-R1-08 (note).
- **SOLR-11288: CONVERGED.** SC-R1-04, SC-R1-09 (note).
- **SOLR-11479: NOT CONVERGED.** SC-R1-01 (should-fix); SC-R1-10 (note).
- **SOLR-12651: NOT CONVERGED.** SC-R1-02 (should-fix); SC-R1-05, SC-R1-06 (note).
- **SOLR-12991: CONVERGED.** No finding.
- **SOLR-13186: CONVERGED.** SC-R1-30, SC-R1-37 (note).
- **SOLR-13369: CONVERGED.** SC-R1-32, SC-R1-36 (note).
- **SOLR-14919: NOT CONVERGED.** SC-R1-26 (should-fix); SC-R1-33, 34, 35, 38 (note).
- **SOLR-15035: NOT CONVERGED.** SC-R1-27, 28, 29 (should-fix); SC-R1-31 (note).
- **SOLR-15106: NOT CONVERGED.** SC-R1-21 (blocking); SC-R1-22, 23 (should-fix); SC-R1-30, 37, 38 (note).
- **SOLR-15386: NOT CONVERGED.** SC-R1-24, 25 (should-fix); SC-R1-30, 37, 38 (note).
- **SOLR-15674: CONVERGED.** SC-R1-47, 48 (note).
- **SOLR-15863: NOT CONVERGED.** SC-R1-41 (should-fix); SC-R1-49 (note).
- **SOLR-16437: NOT CONVERGED.** SC-R1-42 (should-fix); SC-R1-50, 51, 52 (note).
- **SOLR-17292: NOT CONVERGED.** SC-R1-43 (should-fix); SC-R1-53 (note).
- **SOLR-17680: NOT CONVERGED.** SC-R1-44 (should-fix); SC-R1-54, 55 (note).
- **SOLR-17733: NOT CONVERGED.** SC-R1-45, 46 (should-fix); SC-R1-56 (note).

## Register

Blocking and should-fix rows carry the replacement text from the subagent reports. Note rows are summarized.

### Slice A: SOLR-5813, 9155, 11288, 11479, 12651, 12991

**SC-R1-01 | SOLR-11479 | should-fix.** Line 39 omits the adopted landing order. Replacement: line 39 adds "This branch is meant to land after SOLR-15035." The answers record 15035 first, then 11479.

**SC-R1-02 | SOLR-12651 | should-fix.** Line 3 is an internal comment ("HOLD (not PR text)") that uses process vocabulary. Delete line 3.

**SC-R1-03 | SOLR-9155 | should-fix.** Line 35 "**Three limits remain: the flow-control point, the shared message on the conflict path, and when the test interrupts.**" is compressed. Replacement: "**Three limits remain: the conflict check still uses an exception, one message also covers the conflict case, and the test interrupts before the call.**"

**SC-R1-04 | SOLR-11288 | note.** The branch changelog title lacks the blank-only wording. The owed title commit moves the head, so re-read lines 25 and 40 after it lands.

**SC-R1-05 | SOLR-12651 | note.** The branch changelog title overstates the cleanup. The draft links move after the owed commit.

**SC-R1-06 | SOLR-12651 | note.** The SOLR-9598 landing order is not stated. The record says 9598 lands before 12651 and does not contradict it. Optional: the main side decides whether the PR names the stacking.

**SC-R1-07 | SOLR-5813 | note.** Line 15 repeats the Limits line about the path not being tested. Optional: delete it from line 15.

**SC-R1-08 | SOLR-9155 | note.** About 4,950 characters, above the guide. Main side accepts or trims.

**SC-R1-09 | SOLR-11288 | note.** About 4,260 characters. Same as SC-R1-08.

**SC-R1-10 | SOLR-11479 | note.** Line 9 says "base commit, line 352". Optional: "(merge-base, line 352)".

### Slice 2: SOLR-13186, 13369, 14919, 15035, 15106, 15386

**SC-R1-21 | SOLR-15106 | blocking.** The bold line "One new test fails on the base code and passes with this change." is not supported. `OverseerProcessorExitTest` calls `getCollectionProcessorThread()`, which this change adds, so the base code cannot compile the test and never reaches its assertion. Replacement for the bold line: "**One new test passes with this change. The base code cannot compile it, so no base failure is shown.**" Replacement for the bullet: "- `OverseerProcessorExitTest` 1 of 1 at head `40b7e5d0efa`, verified on 2026-10-07 ([test](https://github.com/nick-boss-tech/solr/blob/40b7e5d0efa70d61b74b9fa9ac04beb1ed397637/solr/core/src/test/org/apache/solr/cloud/OverseerProcessorExitTest.java#L34-L59)). The test calls `getCollectionProcessorThread()`, which this change adds ([accessor](https://github.com/nick-boss-tech/solr/blob/40b7e5d0efa70d61b74b9fa9ac04beb1ed397637/solr/core/src/java/org/apache/solr/cloud/Overseer.java#L827-L829)), so the base code cannot compile it. The proof is inconclusive by construction." A compile failure on the old code is inconclusive under the workspace rules.

**SC-R1-22 | SOLR-15106 | should-fix.** The early-return link points at the head, but `OverseerTaskProcessor.java` is not in the branch diff. Replacement: link `OverseerTaskProcessor.java#L185-L186` at `9b3a84b1c460981eab09d8ffaef776acc4a184f8`, and say it is unchanged by this branch, linked at the merge-base.

**SC-R1-23 | SOLR-15106 | should-fix.** The exit link to `Overseer.java` L451 to 492 points at the head, but that range is outside every branch hunk. Replacement: link at `9b3a84b1c460981eab09d8ffaef776acc4a184f8`, unchanged by this branch, linked at the merge-base.

**SC-R1-24 | SOLR-15386 | should-fix.** The distributed-path link to `ZkController.java` L3053 to 3060 points at head lines outside the branch hunks. The block sits at the merge-base lines 3043 to 3050. Replacement: link `#L3043-L3050` at `e2cdb2d7e8ae0be4cf6cf0606206c67d89e7278f`, unchanged by this branch, linked at the merge-base.

**SC-R1-25 | SOLR-15386 | should-fix.** "used when the Overseer is disabled" is wrong. The mode is distributed cluster state updates, and the Overseer still runs. Replacement: "used when distributed cluster state updates are on".

**SC-R1-26 | SOLR-14919 | should-fix.** The "now commits itself" link points at the head, but `DistributedZkUpdateProcessor.java` is unchanged. Replacement: "...([DistributedZkUpdateProcessor.java](https://github.com/nick-boss-tech/solr/blob/b5c71bc5573c4e31b4cee5a7965d73587fc0ae58/solr/core/src/java/org/apache/solr/update/processor/DistributedZkUpdateProcessor.java#L215-L237), unchanged by this branch, linked at the merge-base)."

**SC-R1-27 | SOLR-15035 | should-fix.** "and the CoreAdmin create path keeps it" and "The CoreAdmin create path copies only the parameters on its list" describe a path the proof does not test. The allowlist is read only by the v2 create path. Replacements: "and the v2 create path keeps it"; "The v2 create path (`CreateCore`) copies only the parameters on its allowlist ([paramToProp](https://github.com/nick-boss-tech/solr/blob/12d7491e82cd660ad55063cb85633b7e05e1918c/solr/core/src/java/org/apache/solr/handler/admin/CoreAdminHandler.java#L239-L251))."

**SC-R1-28 | SOLR-15035 | should-fix.** "The ticket summary describes this difference." No Jira packet for this ticket is on disk. Delete the sentence until the summary is confirmed.

**SC-R1-29 | SOLR-15035 | should-fix.** "AddReplicaTest 4 of 4 at 12d7491e82c, verified 2026-10-03." The date is recorded in the ledger, not a run date. Replacement: "AddReplicaTest 4 of 4 at 12d7491e82c, recorded 2026-10-03."

**SC-R1-30 | SOLR-13186, 15106, 15386 | note.** The Proof says "verified on <date>", and the answers prefer "recorded". The main side should pick one form for the set.

**SC-R1-31 | SOLR-15035 | note.** The base-failure sentence does not carry the owed observed line or the receipt's outcome. Optional: name the receipt outcome.

**SC-R1-32 | SOLR-13369 | note.** "Six more inputs were tried" and "because the input it ran": the receipt says six further seeds. Optional: "seeds".

**SC-R1-33 | SOLR-14919 | note.** The changelog title "no longer breaks replica recovery" overstates a recovery hunk that has no test. The branch owner should check the title when it is next touched.

**SC-R1-34 | SOLR-14919 | note.** The Choice does not say the 2018 reason has not been read. Consider one sentence stating that.

**SC-R1-35 | SOLR-14919 | note.** The test link at lines 63 to 76 includes a comment the answers flag as misstating the hop. The Proof and links need the new head after that fix.

**SC-R1-36 | SOLR-13369 | note.** The changelog fragment is kept for a test-only change. The answers drop that kind of fragment for another ticket. Keep the two consistent.

**SC-R1-37 | SOLR-13186, 15106, 15386 | note.** The answers say these drafts carry a suggested PR title. None does. Confirm whether a title belongs in the draft.

**SC-R1-38 | SOLR-14919, 15106, 15386 | note.** About 4,800, 4,000 and 4,400 characters against the 3,500 guide. The answers accept length only for SOLR-15674.

### Slice 3: SOLR-15674, 15863, 16437, 17292, 17680, 17733

**SC-R1-41 | SOLR-15863 | should-fix.** Line 23 "verified 2026-10-08." The date is a takeover-log record date, not a run date. Replacement: "recorded 2026-10-08".

**SC-R1-42 | SOLR-16437 | should-fix.** Line 35 "DELETEREPLICAPROP checks only its required fields" links to pre-change code at the head without saying so. Replacement: "- DELETEREPLICAPROP checks only its required fields ([CollApiCmds.java L364-L365](https://github.com/nick-boss-tech/solr/blob/e2cdb2d7e8ae0be4cf6cf0606206c67d89e7278f/solr/core/src/java/org/apache/solr/cloud/api/collections/CollApiCmds.java#L364-L365), base commit). I can open a follow-up ticket and PR for it and for the other APIs on request."

**SC-R1-43 | SOLR-17292 | should-fix.** Line 20 says persist logs an ERROR first at the call site, and that the node-down message is sent after any persist exception. Persist logs ERROR only after stale retries are exhausted, and at base a lost connection logs nothing in persist. Replacement for the sentences after "moves on to the next collection.": "When every retry is stale, persist logs an ERROR before it throws. This call site then logs a warning. The node-down message is now sent even when persist throws ([L3074-L3079](https://github.com/nick-boss-tech/solr/blob/f614a42fbc8046e47cb7ed216f6b52b8a01f594c/solr/core/src/java/org/apache/solr/cloud/ZkController.java#L3074-L3079)). Before this change, a KeeperException from persist, for example a lost ZooKeeper connection, skipped that message."

**SC-R1-44 | SOLR-17680 | should-fix.** Lines 9 and 19 quote the error text cut short. The code appends "plus some create-collection prefixed ones." Replacement for line 9: "The ticket reports the request is rejected with "A routed alias requires these params: [router.field, router.name] plus some create-collection prefixed ones."" Replacement for line 19: "- A time routed alias without `router.field` now fails with a message that starts "A time routed alias requires these params:" and lists its five required parameters. Before, the command gave "A routed alias requires these params: [router.field, router.name] plus some create-collection prefixed ones.""

**SC-R1-45 | SOLR-17733 | should-fix.** The bold summary says the test "covers only the normal path", but the Proof says it also checks a local-only delete returns 400. Replacement: "**A failed remote or local delete still returns success, and the test does not cover that case.**"

**SC-R1-46 | SOLR-17733 | should-fix.** Bullets 1 and 2 (lines 33 and 34) link `DistribFileStore.java` at the head for delete behavior the branch leaves unchanged. Replacement: bullet 1 links `DistribFileStore.java` L495 to 510 at `56ec140e3636d5f4150fa87fbf7103529536ac99`, base commit. Bullet 2 links L162 to 168 at the same commit, base commit.

**SC-R1-47 | SOLR-15674 | note.** "The cache entry is checked against the data version of the znode, in IndexSchemaFactory.java and SolrConfig.java." The check is in `IndexSchemaFactory.java`; `SolrConfig.java` only calls into it. Optional wording change.

**SC-R1-48 | SOLR-15674 | note.** The pair with SOLR-15478 has no landing order in the draft. Answers adopt 15674 first. The main side decides whether the PR text states the order.

**SC-R1-49 | SOLR-15863 | note.** The changelog title starts lowercase at head. One branch commit is owed before opening, and it moves the head, so re-record the head, counts and changelog link then.

**SC-R1-50 | SOLR-16437 | note.** The answers' wording "before it changed the replica in the other shard" is wrong. Base writes only to the named slice's replicas. The draft's "set the property on no replica" is correct. Correct the answers wording.

**SC-R1-51 | SOLR-16437 | note.** "the other APIs the ticket mentions" is more than the ticket says. Optional: "and other APIs that take the same inputs".

**SC-R1-52 | SOLR-16437 | note.** The answers say "No gate recorded." The receipt records a green gate at this head, and the draft follows the receipt.

**SC-R1-53 | SOLR-17292 | note.** The answers' "verified 2026-10-04" is for the pre-remedy head. The draft's date for the re-gate at `f614a42` is right.

**SC-R1-54 | SOLR-17680 | note.** The Proof gives head counts and the base outcome in words, not counts. Optional: add the base counts from the receipt, and say "checks" rather than "pins".

**SC-R1-55 | SOLR-17680 | note.** Links at head to `RoutedAlias.java` and `TimeRoutedAlias.java` are unchanged by the branch. Either SHA is accurate; the merge-base is the stricter form.

**SC-R1-56 | SOLR-17733 | note.** The link to `ClusterFileStore.java` at head is unchanged by the branch, and the same lines exist at the merge-base. Either SHA is accurate.

## Not checked

- Gate logs for SOLR-15863 and SOLR-17680 are not on disk. Their counts come from the receipts.
- Live JIRA was not queried. Ticket quotes come from `research/jira-context/` packets. SOLR-14919 and SOLR-15035 have no packet.
- Trial merges were not run.
- The master receipts ledger, the takeover log and security material.

## Convergence line

NOT CONVERGED. One blocking and seventeen should-fix findings are open.
