# Replication and backup round 1: main-side answers (2026-10-10)

Report: `reports/replication-backup-round-1.md`, with parts `reports/replication-backup-round-1-g1.md` through `-g4.md`. Assignment: `assignments/replication-backup-round-1.md`. Under the owner's standing decision practice, recorded recommendations are adopted below unless an item is marked DISCUSS; DISCUSS items carry the recommendation and the call is not taken. Gate state is settled by the receipt in `receipts/` at the exact live tip; where a report or draft claim and a receipt differ, the receipt wins and the entry says so. The four fresh arrivals (SOLR-5589, 6711, 9091, 9382) are NO GATE and audit only: no draft-readiness claim for any of them stands, and a first gate for each is main-side work owed once the audit's fixes land. SOLR-18249 and SOLR-18280 are live PRs, consistency only.

## Gate states settled by the receipts

- Gated green at the live tip, per the receipts: SOLR-8430 (`49af21be589`), SOLR-9598 (`c8407773f76`, proof timing-dependent, stated in its entry), SOLR-9865 (`4937608bb18`), SOLR-11650 (`e4f5e941cd8`), SOLR-12085 (`c8dba502339`), SOLR-12246 (`3ffc2539ec7`), SOLR-17287 (`6957daf8261`).
- Live PRs, consistency only: SOLR-18249 (receipt says gated green at the live tip `deffea4c51be` with a Javadoc-only tip over the gated tree; part g4 shows the gated head itself is ambiguous on the record, see its entry), SOLR-18280 (NO GATE on the main side; test-only; its state rests on the live PR's own checks, with the correction in its entry).
- No gate recorded: SOLR-5589 (`c707aa95e2ae`), SOLR-6711 (`5a120cd69d64`), SOLR-9091 (`e31bdaa4d279`), SOLR-9382 (`c0b5fec1be21`). All four still carry their TESTING.md handoff note at the tip.

## Per-ticket answers

### SOLR-9865 (draftable, held for two confirmations)

- Gate state: green at the live tip per the receipt (11 of 11 across TestRestoreCore, TestReplicationHandlerBackup, TestSnapshotCoreBackup; proof discriminates after the test gained a core reload step, and the draft's Proof carries that explanation, as the receipt requires).
- ADOPTED: the draft's Choice stands as written (write the previous directory name back, as implemented; the ticket's auto-discovery route is posed to the maintainer).
- Held, main-side confirmation owed: the TestRestoreCore count. The receipt says 4 (and the total of 11 depends on it); the head file declares 3 `@Test` methods. The draft's `[CONFIRM: count]` placeholder stays until the main side confirms the count from the gate's JUnit XML (below). The draft does not post before that.
- Held, branch correction owed: the test comment at TestRestoreCore.java lines 237-238 states a mechanism the audit's code reading does not show (part g1, finding 1). The replacement in the report is adopted ("reload the core so the check below reads `index.properties` on a fresh open"). The main side also checks the receipt's Proof wording, which uses the same mechanism language, against the premise logs named in the receipt (g9865-premise.log, g9865-premise2.log) and corrects the receipt if they do not support it.
- ADOPTED: landing order for the RestoreCore cluster: 9865, then 17287, then 9091. The three diffs share no lines and pairwise trial merges are clean.

### SOLR-17287 (draftable, held for one confirmation)

- Gate state: green at the live tip per the receipt (TestRestoreCore 4 of 4, UpdateLogTest 6 of 6; proof discriminates). The draft states the Update processing cross-area effect plainly, as the assignment requires: the UpdateLog change is additions only.
- Held, main-side confirmation owed: the same TestRestoreCore count question as 9865 (receipt 4 of 4, head file 3 `@Test` methods; the draft's `[CONFIRM: count]` placeholder). One confirmation settles both.
- ADOPTED: the clear stays after the cleanup of old index files, as implemented and as drafted, with the Limits line that a cleanup failure after the switch skips the clear (part g1, finding 8). The clear is not moved above the cleanup.
- ADOPTED: the draft's Choice stands as written (standalone only; SolrCloud restores unchanged, posed to the maintainer).
- Receipt correction owed: the receipt's line "UpdateLog.discardLog no longer force-closes cleared logs" describes an intermediate commit. Against the branch base the diff only adds lines; `discardLog` is a new private helper and no existing UpdateLog method changes (part g1, finding 9). Replacement wording is in the report. The draft already describes the net change correctly.

### SOLR-9091 (audit only; not drafted)

- Gate state: NO GATE per the receipt, and the receipt wins over any readiness reading of the audit. The audit checked the premise by reading: it holds against the ticket and base RestoreCore (backup-only files are copied with no check, and the backup-side checksum read error is swallowed). Nothing has run. A first gate with a focused fail-before proof is main-side work owed (below).
- ADOPTED: before any opening, the branch gets the focused proof and `SOLR-9091-TESTING.md` comes off the branch, as the report's owner list states.
- ADOPTED, branch corrections owed: the branch note's claim that `doRestore` rolls back on a corrupt file is wrong (the failure happens in the download step, before the switch, so the live index is not touched); the same correction applies to the test comment at TestRestoreCore.java line 295 (part g1, finding 3; replacement wording in the report). Any future draft words the new check as the report's finding 4 states: it checks each copied file against the checksum in its own footer; it does not compare against a checksum recorded in the backup, and the branch does not change the swallowed backup-side read.
- ADOPTED: lands last in the RestoreCore cluster, after 9865 and 17287.

### SOLR-12246 (draftable)

- Gate state: green at the live tip per the receipt (IndexFetcherCompareFileTest 1 of 1 counted; TestReplicationHandler is `@Nightly` and was skipped, and the draft's Proof says so).
- ADOPTED, the recorded Choice is answered: lower the whole "did not match" statement to INFO, as implemented. The route matches the no-checksum branch directly above it on the same method, which is already INFO; the comparison result is unchanged, so the file is fetched again exactly as on base. The draft's Choice section stands, so the maintainer still confirms on the PR, and its Limits keeps the differing `.liv` checksum cause as an open question with the follow-up offer. The branch is not changed to keep WARN for length differences, so no new run follows from this call.
- Record note: the shipped test covers the equal-length case only; the draft's Limits says so (part g2, finding 17). No code change owed.

### SOLR-12085 (draftable)

- Gate state: green at the live tip per the receipt (IndexFetcherUnusedFilesTest 1 of 1 counted; TestReplicationHandler `@Nightly` skipped, and the draft's Proof claims no end-to-end replication coverage, as the receipt requires).
- ADOPTED: no Choice section is added. The ticket's three suggested routes are not the implemented one; the branch fixes the used set the existing check reads, which is the direct correction, and the draft's Limits covers what is untested (part g2, finding 15). A Choice posing "remove the check" would ask the maintainer to ratify dropping a safety check this change does not need to drop.
- ADOPTED, branch correction owed: the changelog title says "every replication"; the wait runs only on the path the draft names. The replacement title is in part g2, finding 14. Changelog-only commit; it moves the head and needs no new run (see the moved-head adoption under SOLR-11650).
- ADOPTED: landing order for the IndexFetcher cluster: 12246, then 12085, then 11650, with 6711 last. No two branches change the same line and all pairs merge cleanly.

### SOLR-11650 (draftable after the title and comment fixes)

- Gate state: green at the live tip per the receipt (URLUtilTest 18 of 18, IndexFetcherLeaderUrlRedactionTest 2 of 2 with the password visible on base in both messages, TestUserManagedReplicationWithAuth 3 of 3). The draft states the SolrJ cross-area effect plainly, as the assignment requires.
- ADOPTED: narrow the changelog title to the three messages the receipt covers (part g2, finding 3; replacement in the report). The current title claims "IndexFetcher log and error messages" generally, and two "Leader at ... is not available" warnings plus the client's connection error still print the URL with the password; the draft's Limits already states that, and no public text may claim the wider coverage.
- ADOPTED: keep the narrowed claim and the Limits line; redaction is not extended to the two warnings or the client error text in this branch (part g2, decision 2, recommended route). ADOPTED: raw `/`, `?` and no-scheme values stay a Limits line, as drafted (decision 3, recommended route); `redactUserInfo` is not widened.
- ADOPTED, branch corrections owed: the `URLUtil` javadoc replacement (part g2, finding 5) and the test comment replacement (finding 6; the assertion stays). Both ride with the changelog title commit.
- ADOPTED, moved head: the title, javadoc and test-comment commits are comment and changelog changes only, so no new focused run is owed for them. The draft's Proof is re-pointed to the new head and states that only comments and the changelog title changed after the verified run. The same rule is adopted for SOLR-12085's changelog commit and SOLR-9598's javadoc fix.
- Owed before the Proof may say more: a base run for the details case (`testFollowerDetailsRedactLeaderUrlPassword`) is main-side work owed (part g2, finding 7). Until it lands, the draft says only that the case passes, which it already does.

### SOLR-6711 (held; audit only)

- Gate state: NO GATE per the receipt; audit only, not drafted. The premise is real by reading (the ticket reports `disablepoll` and `disablereplication` are lost after a restart; the branch makes only `disablepoll` persist, and only with `persist=true`). Nothing has run; a first gate is main-side work owed after the calls and fixes below.
- DISCUSS: opt-in persistence or default persistence. The branch keeps the reported behavior by default and persists only when the request passes `persist=true`; its test pins that a plain `disablepoll` is not persistent. Recommendation: keep opt-in, as implemented. Making persistence the default changes restart behavior for every existing `disablepoll` caller, which is a wider behavior change than the ticket's narrowest reading and one a maintainer might plausibly reverse. The call is not taken; the ticket stays held.
- ADOPTED: `disablereplication` stays out of this branch. Any future draft names it in Limits with the follow-up offer (part g2, finding 9), per the standing follow-up rule.
- ADOPTED, branch fixes owed before the first gate: remove `SOLR-6711-TESTING.md` (finding 8); fix the lost-update race between the two writers of `replication.properties` with one shared lock (finding 11; shipping it as a Limits line is not accepted, since both writers are in this branch's own change); fix the write order so the persisted file is written before the in-memory flag changes (finding 12; replacement code in the report). The two over-length lines (finding 13) are handled by the tidy step in packaging.

### SOLR-5589 (held; audit only)

- Gate state: NO GATE per the receipt; audit only, not drafted. The audit checked the premise by reading: it holds on current main for leader-only and both-disabled configurations (the handler falls back to a default leader when no section is enabled). A first gate is main-side work owed after the fixes below.
- ADOPTED: apply the disable-rule fix from part g3, finding 10 (the report's recommended route): disable only when a leader section is present and disabled (`if (leader != null)` in place of `leader != null || follower != null`). The shipped form also disables for a follower-only disabled section, which is untested and goes against the ticket thread's argument that a disabled follower should not stop a leader from serving. The alternative (disable only when both sections are present and disabled) would fail the branch's own leader test and is not taken.
- ADOPTED, branch corrections owed: the changelog title replacement that matches the fixed rule (finding 11), and removal of `SOLR-5589-TESTING.md` before any gate or PR (finding 12).
- ADOPTED: any future draft carries the Limits sentence from finding 13 (a disabled leader still takes commit-time snapshots when `backupAfter` is set, as the `disablereplication` command does).

### SOLR-9382 (held; audit only)

- Gate state: NO GATE per the receipt; audit only, not drafted. The audit checked the premise by reading: it holds for the glob only. A wildcard in `confFiles` is silently dropped on current main; the ticket's main report (a standalone follower does not see managed resource changes until a reload) is wider than the branch, and the branch does not change reload behavior.
- ADOPTED: no draft this round. Drafting follows the first gate in the normal flow, and any draft states that the change answers the wildcard suggestion only (part g3, finding 14).
- ADOPTED, branch corrections owed before the first gate: the changelog title replacement with the correct example path (finding 15; managed resource files sit in the config directory, not under a `managed-resources` folder, for default standalone storage), removal of `SOLR-9382-TESTING.md` (finding 12), and the two over-length lines (finding 16; replacements in the report, also handled by tidy in packaging).

### SOLR-8430 (draft written, held for one fix and one call)

- Gate state: green at the live tip per the receipt (ReplicationRateLimiterTest 3 of 3, plus a probe 1 of 1). The receipt's proof is two legs: the shipped test fails compilation on base (it calls the new helper), and a standalone probe fails on base and passes at the head. The draft states both honestly, including that the probe is not part of the change.
- ADOPTED: apply the capped replacement for the static limiter map (part g3, finding 1; replacement code in the report) before opening. The map is keyed by a client-supplied rate and never shrinks, so a client can grow it without limit; that defect is in this branch's own new code and is fixed here, not shipped as a Limits line. The draft's first Limits bullet is replaced once the cap lands, and a focused re-run at the new head is main-side work owed (the moved-head adoption under SOLR-11650 does not cover this one, since the cap is a code change).
- ADOPTED: the changelog retitle from finding 2 (the throttle is shared by requests that use the same rate, not by all requests on the node).
- DISCUSS: one limiter per rate value (this branch), or one limiter for the whole node (the ticket's wording). Recommendation: keep one per rate value, as implemented, with the draft's Choice posing the per-node route to the maintainer. A per-node limiter takes its rate from the lowest request and so overrides rates other replicas explicitly asked for; the per-rate form never does. The call is not taken; the draft stays held.
- Record note: the branch changes no endpoint, parameter or response; the draft says so (finding 18).

### SOLR-9598 (draft written, held for the default call)

- Gate state: green at the live tip per the receipt (TestLocalFSCloudBackupRestore 2 of 2, RestoreCollectionAPITest 7 of 7, BackupRestoreApiErrorConditionsTest 4 of 4). The proof is timing-dependent, and the draft states it the same way the receipt does: on base the new check failed in one of five runs and passed four, so the draft claims no deterministic base failure. That honesty stands.
- DISCUSS: the RESTORE default. Options as drafted: (1) wait by default with `waitForFinalState=false` as an opt-out (this branch); (2) always wait, no opt-out (the direction SOLR-17712 is taking other commands); (3) no default wait, as CREATE does today. Recommendation: option 1, as implemented. The ticket asks RESTORE to do this check so clients do not have to; option 3 leaves the gap for every caller that does not opt in, and option 2 removes the escape existing callers may need while the default changes under them. The call is not taken; the draft stays held.
- ADOPTED: the `RestoreCmd` javadoc fix (part g3, finding 6; replacement in the report). The current text says the wait follows SOLR-17712's direction, which is backwards. Comment-only; it rides with the changelog spelling fix below, moves the head, and needs no new run under the moved-head adoption (the draft's Proof states only a comment changed).
- ADOPTED: the changelog wording fix (finding 5, "behavior" spelling), riding the same commit.
- ADOPTED: landing order, 9598 before SOLR-12651. The two branches both change RestoreCmd.java and trial-merge cleanly; 12651 is gated at an older head in SolrCloud round 1 and rebases over this change.
- Record correction: the assignment's note that SOLR-15863 shares TestLocalFSCloudBackupRestore with 9598 is wrong (finding 9). At its head, 15863 changes no file 9598 changes; there is no overlap and no landing order between them.

### SOLR-18249 (live PR; consistency: hold the receipt)

- Consistency result: the live head, file list and code are consistent with the branch; the drift is in the PR text and the receipt, not the code (part g4). No new gate is owed for the code as it stands.
- Receipt correction owed (the receipt's gated head is ambiguous, and the report's recommended reading is adopted): the round 29 gate ran against `c4cd38c` (the receipt's own wording points at it, its recorded finish date of 2026-10-06 predates the tip commit of 2026-10-07, and the round 29 review of 2026-10-07 reviewed `c4cd38c`). Tip `deffea4c51be` adds a four-line Javadoc note to LocalFileSystemRepository.java and is not separately gated; the live PR's checks at that tip are green. The replacement line for the receipt is in part g4, item 2. No verify run at `deffea4` is owed for a Javadoc-only delta; if the owner wants one anyway, it is a small focused run, main-side.
- Receipt correction owed: the counts. Only ShardBackupMetadataTest 4 of 4 has an on-disk run record, dated 2026-09-28 with no head recorded, which predates the earliest PR commit; the 1 of 1 and 5 of 5 counts have no on-disk record (part g4, item 3). The receipt is corrected to say which counts rest on the gate record and which have no record on disk.
- ADOPTED, live PR text corrections (edits to our own PR description carry standing authorization; applied by the main side, below): the Proof parenthetical replacement in part g4, item 1 (it carries process words and a re-run claim no receipt records), the `ShardBackupMetadataTest` bullet wording in the same item (those cases cannot run on base, since they call the method this change adds), the DeleteBackupCmdTest sentence in item 4 (the code-reading wording, since no run record is added), and the pointed Choice question in item 6.

### SOLR-18280 (live PR; consistency: the PR is ready as code)

- Consistency result: the live head, file list and code are consistent; test-only. The wording and the receipt need the fixes below (part g4). Gate state: NO GATE on the main side per the receipt, and none is owed for a consistency pass.
- ADOPTED: the PR title takes the Jira wording (the failures reproduce reliably from nightly seeds; "flaky" does not match), and the first sentence is corrected the same way. Replacements in part g4, item 7.
- ADOPTED: the changelog line at the end of the PR body comes out (item 8, recommended route). The branch dropped that file in 2026-10-02 as not changelog-worthy for a test-only change, and the PR carries the no-changelog label; the body line names a file that is not in the PR.
- ADOPTED: the unsupported Proof counts come out of the PR body (item 9). The body says the `@Nightly` class has 25 tests and passes 24 of 25 twice; the head class has 24 `@Test` methods, and no run record at this head supports either number. The counts are cut rather than re-derived, since no JUnit XML for them exists; recording a run at `0da92abd9e96` stays available if the owner wants the numbers back.
- Receipt corrections owed: the counts line (item 10; an older-head queue result from 2026-10-01 exists and is recorded as such, with no record at the live tip), the test-state basis line (item 11; the live PR's checks are green at the head, but they run only the new class, since the changed test sits in the `@Nightly` class the checks do not run), and the round 31 review date (item 12; 2026-10-07, not 2026-10-06, in both places).

## Cross-cutting adoptions

- Landing orders: RestoreCore cluster 9865, 17287, 9091; IndexFetcher cluster 12246, 12085, 11650, with 6711 last; SOLR-9598 before SOLR-12651. The audit-only tickets' trial merges are clean in every pair checked, and no order causes a textual conflict.
- ADOPTED: moved-head rule for this round. Comment-only and changelog-only commits after a verified run need no new run; the draft's Proof is re-pointed to the new head and states that only comments or the changelog changed after the run. Code changes after a verified run (the SOLR-8430 cap) do need a focused re-run at the new head.
- ADOPTED: the four arrivals' TESTING.md handoff notes come off their branches as part of each branch's pre-gate packaging, together with the audit's branch fixes listed per ticket. No arrival is drafted before its first gate.
- ADOPTED: no rebase of the gated branches. They sit behind current main with clean trial merges; the gates stand at the recorded heads.

## Draft corrections owed (draft-fix pass; not done in this pass)

1. SOLR-9865 and SOLR-17287: the `[CONFIRM: count]` placeholder in each Proof resolves when the main side confirms the TestRestoreCore count (see main-side work owed). Neither draft posts before that.
2. SOLR-9865 and SOLR-17287: the Choice sections open with body text, not a bold one-line summary. Add the bold opener to each.
3. SOLR-9865 and SOLR-17287: the Limits sections open with bullets, not a bold one-line summary. Add the bold opener to each.
4. SOLR-9865 and SOLR-17287: file citations are bare text (`RestoreCore.java` line 210 form), not links. Link each citation to the blob at the head SHA, and link the changelog line the same way, as the other five drafts do.
5. SOLR-8430 and SOLR-9598: the OWNER NOTE headers come off before posting (both drafts are held, so the notes stay until their holds clear). SOLR-8430's first Limits bullet is also replaced once the cap lands.
6. SOLR-11650: after the title and comment commits land, re-point the draft's citations and Proof head to the new head and add the moved-head sentence from its entry.
7. Length: SOLR-9598 (about 4,570 characters with its links) and SOLR-11650 (about 4,500) run over the 3,500-character guide. For 9598 the report already accepts the length (the Choice and the timing proof need the room); for 11650 the overage is citation links on a two-module change. Both stand as complex tickets; trim at the draft-fix pass only if the openings slate wants it. SOLR-12246 (about 3,610) is marginally over, links included, and stands.
8. Checked and clean: titles. No draft carries a separate PR title line; the one-line summaries and the changelog titles they point at were checked, and the only inaccurate titles are the branch changelog titles listed under branch corrections owed. Every section of every draft opens with a bold one-line summary except the four sections in items 2 and 3. No internal process vocabulary appears in any draft's PR text (the only hits are in the OWNER NOTE headers that come off). Proof numbers match the receipts, with the two honest qualifications stated in the drafts themselves: SOLR-9598's proof is timing-dependent and says so, and SOLR-12085 and SOLR-12246 both state that the `@Nightly` replication suite was skipped and claim no end-to-end coverage. No draft names a Lucene version, so the cross-version rule is not triggered.

## Branch corrections owed (not drafts; for the lanes that touch the branches)

1. SOLR-11650: changelog title narrowing; `URLUtil` javadoc replacement; test comment replacement (part g2, findings 3, 5, 6).
2. SOLR-12085: changelog title replacement (part g2, finding 14).
3. SOLR-8430: capped limiter map replacement; changelog retitle (part g3, findings 1, 2).
4. SOLR-9598: `RestoreCmd` javadoc replacement; changelog spelling (part g3, findings 6, 5).
5. SOLR-9865: test comment replacement (part g1, finding 1).
6. SOLR-9091: branch note and test comment corrections (part g1, finding 3); TESTING.md removal.
7. SOLR-5589: disable-rule fix; changelog title replacement; TESTING.md removal (part g3, findings 10, 11, 12).
8. SOLR-9382: changelog title replacement; TESTING.md removal; two over-length lines (part g3, findings 15, 12, 16).
9. SOLR-6711: TESTING.md removal; shared lock for the properties writers; write-order fix (part g2, findings 8, 11, 12).

## Main-side work owed (gates first)

1. Confirm the TestRestoreCore count from the gate's JUnit XML for SOLR-9865 and SOLR-17287 (one confirmation settles both receipts and both drafts). If the XML cannot be recovered, say so and correct both receipts to the count a confirmation run gives.
2. SOLR-8430: focused re-run at the new head after the cap fix lands.
3. SOLR-11650: base run for `testFollowerDetailsRedactLeaderUrlPassword`, owed before the draft's Proof may claim that case fails without the change.
4. First gates, each after its branch corrections and packaging: SOLR-9091 (with the focused fail-before proof), SOLR-5589, SOLR-9382, SOLR-6711 (6711 also waits on the DISCUSS call in its entry).
5. Receipt corrections: SOLR-17287 (discardLog wording), SOLR-18249 (gated head line and counts basis), SOLR-18280 (counts line, test-state basis, review date), and SOLR-9865's Proof wording if the premise logs do not support its mechanism language.
6. Live PR text edits adopted above: SOLR-18249 (Proof parenthetical, two bullets, Choice question) and SOLR-18280 (title, first sentence, changelog line, Proof counts).
7. No gate owed: SOLR-18249 and SOLR-18280 (consistency only, code consistent at their live tips).

## DISCUSS list (recommendations recorded; calls not taken)

1. SOLR-9598: the RESTORE default. Options: wait by default with an opt-out (this branch); always wait, no opt-out; no default wait, as CREATE does. Recommendation: wait by default with an opt-out, as implemented.
2. SOLR-8430: one limiter per rate value (this branch), or one limiter for the whole node (the ticket's wording). Recommendation: keep per rate value; the draft's Choice poses the per-node route to the maintainer.
3. SOLR-6711: opt-in persistence (`persist=true`, this branch), or persistence by default. Recommendation: keep opt-in.
