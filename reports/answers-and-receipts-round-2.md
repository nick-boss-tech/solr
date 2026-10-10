# Answers and receipts round 2: round roll-up

Claim: `claims/answers-and-receipts-round-2.md` (commit `87bb08655c9`). Parts: `reports/answers-and-receipts-round-2-p1.md` (replication draft pass), `-r2.md` (SOLR-12849, live PR 5011, consistency), and `-r3.md` (SOLR-15003 and SOLR-18010).

Done 2026-10-10 by Claude Code (AI agent), working for Nick Shanin. Three subagents did the read-only audit and the draft edits, one round, within the cap of six. No build, Gradle run, or test was run. No branch, receipt, live PR, JIRA item, or comment was touched. Nothing was posted. Nothing was pushed to a submit branch.

## Changes made

Drafts only, all unposted:

- `pr-drafts/replication-backup/SOLR-9865.md`: bold openers on the Choice and Limits sections (claim item 2 and 3). File citations are linked at head `4937608bb181` (item 4). The Choice body and the first Limits bullet no longer restate their openers (lead edit, see below).
- `pr-drafts/replication-backup/SOLR-17287.md`: the same openers and citation links at head `6957daf82610` (items 2 to 4). Lead edits, see below.
- `pr-drafts/core-admin/SOLR-18010.md`: new draft (r3). Head `c3685bb37d9d`. Bold openers on all three sections it has (What this change does, Proof, Limits). Its Limits opener was added by the lead, because the formula requires one on every section.

## Lead edits beyond the claim's item list

The claim said to apply items 2 to 4 only. The lead also made these edits, each checked against the source. Each is small and easy to revert.

1. SOLR-17287, "It adds one method" changed to "It adds two methods, the public `clearAndActivate` and the private helper `discardLog`". Checked: `git diff e2cdb2d7e8ae 6957daf82610 -- solr/core/src/java/org/apache/solr/update/UpdateLog.java` adds two method declarations (public and private) and deletes no lines. The one-caller claim now reads "The public method has one caller", which p1 verified at `RestoreCore.java` line 253.
2. SOLR-9865 and SOLR-17287: the body sentences that repeated their Choice or Limits openers were cut back, following the presentation rule in `pr-formula.md` (a claim stated in the summary is not restated in the body). Changes: SOLR-9865 Choice "The first writes the previous directory name back ... (this change)" became "The first route is this change."; SOLR-9865 Limits "The SolrCloud restore calls the same method, so it gets the same rollback. This change adds no SolrCloud test." became "The SolrCloud restore calls the same method."; SOLR-17287 Choice became "The first route is this change."; SOLR-17287 Limits lost its first bullet, "SolrCloud restores are not changed or tested here.", which the opener already states.
3. SOLR-18010: bold Limits opener "Standalone mode only, and the lock covers one Solr process." added.

## Checks after the edits

- No em or en dashes in any of the three drafts or the four round reports.
- Process words: no hits for owner, receipt, gate, round, audit, handoff, TESTING, premise, takeover, settle, claim, harden, fail-before, OWED or CONFIRM in public text, except the bracketed `[CONFIRM: count]` placeholders in SOLR-9865 and SOLR-17287 (open, see below). The one "owed" hit in SOLR-18010 is inside "followed".
- Heads named in each draft exist as commits: `4937608bb181`, `6957daf82610`, `c3685bb37d9d`.
- Blob links: every link in SOLR-9865 and SOLR-17287 points at the head named in its draft. In SOLR-18010, the four links in "What happens today" (`security.js`, `SecurityConfHandler.java` lines 124 and 151, and `SecurityConfHandlerLocal.java` lines 86 and 87) point at the base commit `14c7aac0d151`, because they describe the old behavior. The other links point at head `c3685bb37d9d`. The formula says citations link at the PR head SHA; owner decision 7 covers this.
- Length, counted with `wc -m` including the links: SOLR-9865 4,144, SOLR-17287 4,090, SOLR-18010 5,237. The formula guide is about 3,500 "unless the ticket is unusually complex". None was trimmed. See owner decisions.
- `git status`: only the files named in this round are changed or added.

## Verdicts

| Ticket | Verdict | Draft | Live state | Blocks what |
|---|---|---|---|---|
| SOLR-9865 | Draftable, held | `pr-drafts/replication-backup/SOLR-9865.md` | head `4937608bb181` | The `[CONFIRM: count]` placeholder for `TestRestoreCore`. The gate JUnit XML is not on disk. |
| SOLR-17287 | Draftable, held | `pr-drafts/replication-backup/SOLR-17287.md` | head `6957daf82610` | The same `[CONFIRM: count]` placeholder. |
| SOLR-12849 | Consistency only, live PR 5011 | none | head `6b92223bc24f` matches; PR OPEN, mergeStateStatus CLEAN | One PR body sentence about the base run's venue (owner decision). Receipt gaps (main side). |
| SOLR-15003 | Held, no draft | none | head `1004abee39ab` matches | The receipt's count and its base proof (main side). |
| SOLR-18010 | Draftable, not postable yet | `pr-drafts/core-admin/SOLR-18010.md` | head `c3685bb37d9d` matches | The settle-log claim in its receipt, and the changelog title (owner decision; a fix moves the head). |

## SOLR-12849 (live PR 5011): details

Head, state, Proof counts (2 of 2 and 4 of 4), title and changelog all match the receipt and the branch. Drift, by r2:

- FIX: the PR body says the base comparison "ran on the fork's GitHub Actions test runner". The receipt names no venue. The premise logs are not on disk, so the venue cannot be confirmed here. Main side: record the run URL and ID in receipt line 7, or say "local". The PR sentence stays as it is until then. Editing the live PR is the owner's call.
- NOTE: dates. The commit is recorded at 2026-10-07 04:20 UTC, while the receipt and PR say 2026-10-06. They agree only in a zone west of UTC. Main side: state the zone.
- NOTE: the receipt does not name the tidy task or the module check task, which the PR body names as `:solr:core:check -x test`.
- NOTE: the second test method (`HttpSolrCallCollectionParamTest` lines 57 to 68) also fails on base, by reading. The receipt cites only the first message. Add that only after the premise log confirms it.
- NOTE: no verify-fail-before verdict file exists under `research/test-queue/results/`. Main side: record a verdict, or say the premise logs are the only base evidence.

## SOLR-15003: details

Held. The count and the baseline in the receipt do not match the branch as recorded:

- FIX 1, receipt line 6: the class `TestReplicationHandler` has 28 `@Test` methods at `1004abee39ab`, 27 at `8f5b6f8360a`, and 24 at the merge-base `b5c71bc5573`. The four new tests are `testFullCopyRemovesOldIndexDirWhenNoSnapshot`, `testFullCopyKeepsSnapshotFilesInOldIndexDir`, `testFullCopyWithReloadRemovesOldIndexDir`, and `testDeleteNamedSnapshotWithMissingIndexDir`. The receipt's "5 tests" matches neither count, and the receipt names neither the filter nor the tests run. Main side: fill the counts from `g15003r36fix-gate.log`, which is not on disk here.
- FIX 2, receipt line 7: the only recorded failure is at `8f5b6f8360a` with the head's test file overlaid. That is not the fail-before baseline. The baseline is the merge-base `b5c71bc5573` with the head's `src/test` files overlaid. Main side: record that run, or record NOT_PROVEN. By reading, base `IndexFetcher.java` lines 744 to 750 remove the old index directory unconditionally after a full copy, so `testFullCopyKeepsSnapshotFilesInOldIndexDir` (line 278) is the likeliest base failure. This is a reading, not a run.
- NOTE: the labels 3a, 3b, 3c and 4 appear only in the receipt and the log, not in the code. The reload test does not exist at `8f5b6f8360a`. Its step 3a must have run the head's test file, and the receipt does not say so.
- NOTE: the receipt gives no time of day for the gate. The commit is dated 2026-10-08 10:33:48 UTC. Optional: add the time to the gate line.

## SOLR-18010: details

Draftable. The draft names head `c3685bb37d9d` and the counts match the code: `SecurityConfHandlerTest` 3 of 3, `BasicAuthStandaloneTest` 1 of 1, `V2SecurityAPIMappingTest` 5 of 5 (not changed by the branch). Both concurrent-edit tests read as failing on base, because base `SecurityConfHandlerLocal.java` lines 86 and 87 empty the file before writing, and the base edit path takes no lock. The draft states the base failures as the receipt's record, not as a run here.

Before posting:

- FIX 7, receipt line 8: the receipt says the settling run is "on disk: g18010-settle.log". That file is not on disk. Its figures (forced interleave 5 of 5, unforced barrier 4 of 50) were measured at base `14c7aac0d15`, and forced interleave 5 of 5 at `fadc5ee31e3`. Neither measures the tip. Main side: correct line 8 to say where the record is. The draft does not use the settle figures.
- FIX 8, changelog title (branch): "can no longer corrupt the file or overwrite each other" overclaims. The lock covers one process, and cloud mode is outside the change. The narrower title is in r3 FIX 8. A change to the title is a new commit, which moves the head, so the draft must be re-headed before posting. This is the owner's call.
- NOTE: `g18010-premise.log` is not on disk. The base failures are the receipt's record and by reading only.

## Replication answers (claim items 1 to 7)

- Item 1 (`[CONFIRM: count]`): still open in both SOLR-9865 and SOLR-17287. It needs the gate JUnit XML.
- Item 2 and 3 (openers): applied in both drafts, and SOLR-18010 has its openers.
- Item 4 (citation links at the head SHA): applied in both drafts. Every link resolves at its head (`git cat-file -e`).
- Item 5 (OWNER NOTE headers): not applicable to these two drafts. It belongs to SOLR-8430 and SOLR-9598 in the replication round roll-up.
- Item 6 (SOLR-11650 re-pointing): not applicable here. Not edited.
- Item 7 (length): SOLR-9865 and SOLR-17287 are over the guide. Not trimmed. See owner decisions.

## Owner decisions

1. SOLR-9865 and SOLR-17287 `[CONFIRM: count]`: confirm the `TestRestoreCore` counts from the gate JUnit XML, which is not on disk. The replication round roll-up found the receipt's count (4) does not match the head file's `@Test` methods (3), and asks for the JUnit XML to settle it.
2. Length: SOLR-9865 (4,144), SOLR-17287 (4,090) and SOLR-18010 (5,237) characters, with links. Trim, or accept the length for complex tickets.
3. SOLR-18010 changelog title: narrow it as r3 FIX 8 suggests. That is a branch change and it moves the head, so the draft is re-headed.
4. SOLR-18010 Limits: the bullets "Standalone mode only", "The lock covers one Solr process" and "Cloud mode and coordination between processes are not covered here" restate the new opener. The last bullet also offers "A follow-up ticket and PR can be opened on request", which is not in the evidence. Keep or cut.
5. SOLR-18010 "Verified 2026-10-05 at this head." The date is taken from the receipt's gate line. Confirm it is the date the gate ran.
6. SOLR-12849 live PR 5011, body venue sentence: your call on whether to edit it, after the main side records the venue. The PR is public, so nothing here touches it.
7. SOLR-18010 citation links: the formula says file citations link at the PR head SHA, but the four "What happens today" links point at base `14c7aac0d151`. Keep them at base, since they show the old code, or move them to head. Moving them needs each line range checked at head first, because the lines may have moved.

## Main-side work owed (not ours to do here)

- SOLR-12849: receipt line 7 (venue, the second test's base failure once confirmed, the verify-fail-before verdict or a note that there is none), receipt line 5 (the tidy and check task names), receipt line 8 (the time zone).
- SOLR-15003: receipt lines 6 and 7 (the counts and tests from the log; a fail-before at `b5c71bc5573` with the head's test files overlaid, or NOT_PROVEN); the gate log is not on disk.
- SOLR-18010: receipt line 8 (where the settle record is); the premise log is not on disk.
- SOLR-9865 and SOLR-17287: the `TestRestoreCore` counts.

## Not done

- No build, Gradle run, test, or `gh` write call. Read-only `gh pr view` was used for 5011.
- No commit to any submit branch, no fetch of new refs, no live PR or JIRA edit, no receipt edit.
- The logs named in the receipts were searched for and not found on disk. Counts and fail-before claims rest on the receipts, checked by reading.
- The changelog YAML was not parsed (no YAML parser and no Python on this machine).
