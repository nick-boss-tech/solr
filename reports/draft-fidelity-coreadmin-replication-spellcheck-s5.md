# Replication-backup and spellcheck draft fidelity, slice 5

Assignment: `assignments/pool-draft-fidelity-coreadmin-replication-spellcheck.md`. Claim: `claims/pool-draft-fidelity-coreadmin-replication-spellcheck.md`, slice B5, taken 2026-10-11T04:12Z. Worktree `wt/pr-prepare-suggester` at HEAD `d627304e96b`. Read-only: no builds, tests, gates, posts, commits or pushes.

Slice drafts: `pr-drafts/replication-backup/SOLR-17287.md`, `SOLR-8430.md`, `SOLR-9598.md`, `SOLR-9865.md`; `pr-drafts/spellcheck/SOLR-3701.md`, `SOLR-4367.md`.

Sources for the Limits and Choice check: `reports/replication-backup-round-1.md` and `-g1.md` to `-g4.md`; `material/replication-backup-round-1-answers.md` (2026-10-10 default); `reports/spellcheck-round-5.md`, `reports/spellcheck-draft-round.md`, `material/spellcheck-round-5-answers.md`. Proof sources: `receipts/<TICKET>.md`, `gates/SOLR-9865-17287-recount.md`. Ticket text: `research/jira-context/` in the main workspace. Process rule: `pr-formula.md`.

## Head check per draft

`git ls-remote origin refs/heads/solr-<n>-submit`, compared with the head each draft names. All six match.

| Ticket | Live tip (ls-remote) | Draft names | Match |
|---|---|---|---|
| SOLR-17287 | `6957daf82610d6f09c533297cceec173a46e82a7` | `6957daf8261` | yes |
| SOLR-8430 | `49af21be58927a62b50bd7ba1df1688110fba63e` | `49af21be5892` | yes |
| SOLR-9598 | `c8407773f76ca49711b5777ff9f69736d87b1e90` | `c8407773f76` | yes |
| SOLR-9865 | `4937608bb181efae104c0d6f0257f445af50bf52` | `4937608bb181` | yes |
| SOLR-3701 | `aabd678dec7330c55dbc044ce6b2f46dd00a0c29` | `aabd678dec7` | yes |
| SOLR-4367 | `0af6087f43fa2b03a9cedb259c1b0dd62039963c` | `0af6087f43fa` | yes |

Merge-bases with upstream main `8e62c2686882` (used for pre-change symptom links): SOLR-8430 `cabedd1d968059215188f4e7563fb303241899ed`; SOLR-9598 `22a8cfebbbdb026437d10b0aa47bb66ffdb23a9a`; SOLR-3701 `cabedd1d968059215188f4e7563fb303241899ed`. SOLR-17287 and SOLR-9865 have no pre-change symptom link.

## Verdicts

| Draft | Head checked | Verdict |
|---|---|---|
| SOLR-17287 | `6957daf82610` (live) | DRIFT (2 items) |
| SOLR-8430 | `49af21be5892` (live) | DRIFT (3 items) |
| SOLR-9598 | `c8407773f76c` (live) | DRIFT (1 item) |
| SOLR-9865 | `4937608bb181` (live) | DRIFT (1 item) |
| SOLR-3701 | `aabd678dec7` (live) | DRIFT (1 item) |
| SOLR-4367 | `0af6087f43fa` (live) | DRIFT (2 items) |

Checked and clean in all six: heads; cited line ranges at the named SHAs; changelog fragments exist at the named head; no em dash (U+2014) or en dash in any draft; a verification date is present in every Proof; no internal words (gate, receipt, ledger, seed, claim, pool, assignment, subagent) in the public text. The Choice sections in SOLR-8430, SOLR-9598, SOLR-9865 and SOLR-17287 each name a live alternative that matches the recorded answers. Proof numbers match the receipts. The SOLR-9865 and SOLR-17287 TestRestoreCore count is now confirmed at 4 by the recount note.

---

## SOLR-17287

Verdict: DRIFT (2 items).

1. Draft says: "Counts, run 2026-10-08 at head `6957daf8261`: TestRestoreCore [CONFIRM: count], UpdateLogTest 6 of 6."
   - Evidence: `receipts/SOLR-17287.md` line 6 (TestRestoreCore 4 of 4, UpdateLogTest 6 of 6, gate run) and line 7 (count confirmed at 4 by a focused run at the head, 2026-10-11, 4 tests, 0 failures). `gates/SOLR-9865-17287-recount.md` line 5 (6957daf8261: 4 tests, 0 failures, DONE). Head file `TestRestoreCore.java` has 3 `@Test` methods (lines 90, 173, 269) and `testBackupFailsMissingAllowPaths` (line 255), which runs by its test-name prefix. UpdateLogTest has 6 `@Test` at this head.
   - Replacement: "Counts at head `6957daf8261`: TestRestoreCore 4 of 4 (run 2026-10-08, and run again 2026-10-11 at this head), UpdateLogTest 6 of 6 (run 2026-10-08)."

2. Draft says: "The public method has one caller, this restore path."
   - Evidence: `git grep` at `6957daf8261`: `clearAndActivate` is called from `RestoreCore.java` line 253 (production) and from `UpdateLogTest.java` line 264 (the new unit test). The production claim holds; the sentence as written does not.
   - Replacement: "The public method has one production caller, this restore path. The unit test also calls it."

Optional notes, not blocking:
- Diff against the merge-base `e2cdb2d7e8a` has 0 deleted lines in every file, so "changes no existing method" holds.
- "Cross-filed under Update processing and atomic updates" is Jira component wording. A plainer line is optional.
- The Choice (standalone only; SolrCloud unchanged) matches the answers. The SolrCloud claim holds: `RestoreCore.java` lines 247-248 say the SolrCloud log is BUFFERING and the caller applies buffered updates.

## SOLR-8430

Verdict: DRIFT (3 items).

1. Draft says: "The base code then builds a new rate limiter for every file stream at that rate ([ReplicationAPIBase.java](https://github.com/apache/solr/blob/8e62c2686882aa704480ab13b6a60ee8f7b5c8af/solr/core/src/java/org/apache/solr/handler/admin/api/ReplicationAPIBase.java#L308-L313))."
   - Evidence: `pr-formula.md`, the presentation rule and section 3 of the template: a pre-change symptom links the merge-base and the text says so. The draft links upstream main `8e62c2686882` on apache/solr and does not say so. Lines 308-313 are the same at the merge-base `cabedd1d968`, so only the link target is wrong.
   - Replacement (the whole sentence): "The base code then builds a new rate limiter for every file stream at that rate ([ReplicationAPIBase.java at the base commit](https://github.com/nick-boss-tech/solr/blob/cabedd1d968059215188f4e7563fb303241899ed/solr/core/src/java/org/apache/solr/handler/admin/api/ReplicationAPIBase.java#L308-L313))."

2. Draft says: "File streams that ask for the same rate now share one limiter on the node." with the changelog link to `changelog/unreleased/SOLR-8430-shared-replication-throttle.yml`.
   - Evidence: the changelog title at `49af21be5892` line 1 reads "...is shared by all concurrent file requests on a node instead of being applied separately to each request". The code shares only among equal rate values (`ReplicationAPIBase.java` lines 107-109 at head). The draft's Choice and Limits say streams at different rates are not capped together. The answers (2026-10-10, SOLR-8430 section, and report g3 finding 2) adopt a retitle. The changelog title overclaims.
   - Replacement (line 1 of the changelog YAML, a changelog-only commit that moves the head): `title: "Replication maxWriteMBPerSec throttling is shared by concurrent file requests on a node that use the same rate, instead of each request getting its own limiter"`

3. Draft says: "- Limiters stay in memory for the life of the node, one for each rate value that a request has used."
   - Evidence: `replication-backup-round-1-answers.md`, SOLR-8430 section: the cap is ADOPTED, "the draft's first Limits bullet is replaced once the cap lands", and the defect "is fixed here, not shipped as a Limits line". Report g3 finding 1: the static map is keyed by the client-supplied `maxWriteMBPerSec` and never shrinks. The current bullet describes the uncapped head.
   - Replacement, to apply in the same commit that lands the cap (the 64 is the value in report g3 finding 1; use the value the code actually carries): "- About 64 shared limiters at most are kept for the life of the node, one for each rate value. A stream that asks for a rate past that cap gets its own limiter and is not shared."
   - The cap is a code change. A focused re-run at the new head is owed before the Proof may name that head (answers, main-side item 2).

Optional notes, not blocking:
- The OWNER NOTE block (lines 1-4) must come out before posting. It names internal items (report findings, a Jira packet).
- The Choice (one limiter per rate value, with the per-node route posed) matches the recorded DISCUSS recommendation. The per-node route is a live alternative.
- The Proof matches `receipts/SOLR-8430.md` lines 6-7 (ReplicationRateLimiterTest 3 of 3; the base-code compile failure on `rateLimiterFor`; the probe 1 of 1 failing on base and passing at head). Verified 2026-10-07 matches the receipt's gate date. The 3 test methods match the file at head.
- The Limits bullets "No test measures transfer speed" and "The follower side and the index fetch path are not changed" match the diff (only `ReplicationAPIBase.java`, its test and the changelog change).

## SOLR-9598

Verdict: DRIFT (1 item).

1. Draft says: "RESTORE creates the new collection, requests its replicas, and sets the alias, without waiting for those replicas to become active ([RestoreCmd.java](https://github.com/apache/solr/blob/8e62c2686882aa704480ab13b6a60ee8f7b5c8af/solr/core/src/java/org/apache/solr/cloud/api/collections/RestoreCmd.java#L301-L306))."
   - Evidence: as for SOLR-8430 item 1. The pre-change symptom links the merge-base `22a8cfebbbdb026437d10b0aa47bb66ffdb23a9a`, and the draft links upstream main on apache/solr without saying so. Lines 301-306 (`addReplicasToShards`, then `restoringAlias`) are identical at the merge-base.
   - Replacement (the whole sentence): "RESTORE creates the new collection, requests its replicas, and sets the alias, without waiting for those replicas to become active ([RestoreCmd.java at the base commit](https://github.com/nick-boss-tech/solr/blob/22a8cfebbbdb026437d10b0aa47bb66ffdb23a9a/solr/core/src/java/org/apache/solr/cloud/api/collections/RestoreCmd.java#L301-L306))."

Checked and clean for 9598 (no action):
- Head code: wait at `RestoreCmd.java` lines 308-348 (call at 308, method at 320-348, `restoringAlias` at 309). Timeout is `10 * 60` with the comment "as in ADDREPLICA" (`AddReplicaCmd.java` line 129 uses the same default). The timeout raises a `SERVER_ERROR` naming the collection. A timed-out RESTORE skips the alias, and no try or catch in `process()` cleans up (`RestoreCmd.java` lines 245-312), so the Limits sentence holds.
- Option `waitForFinalState` at `RestoreCollectionRequestBody.java` lines 41-45. `waitForFinalState=false` returns early at `RestoreCmd.java` line 321.
- The deprecation text on CREATE (`CreateCollectionRequestBody.java` lines 42-44 at upstream main) matches the Choice's SOLR-17712 route.
- Test citation `AbstractCloudBackupRestoreTestCase.java` lines 378-390 holds the active-replica check.
- Proof counts match `receipts/SOLR-9598.md` lines 6-7, including the timing-dependent base result (one failure in five runs, seed `A1B2C3D4E5F60718`).
- The Choice matches the recorded DISCUSS recommendation (option 1 as implemented, with options 2 and 3 posed). The Limits match the answers.

Optional notes, not blocking:
- The OWNER NOTE block must come out before posting. Its finding numbers (4, 5, 6) do not match report g3 (javadoc is finding 6, changelog wording is finding 5, the landing order is finding 8).
- Changelog title (head): "old behaviour". Use "old behavior" to match the repo (answers, adopted; report g3 finding 5). The title content matches the draft.
- The Javadoc at `RestoreCmd.java` lines 317-318 says the wait "Follows the direction of SOLR-17712". That is backwards. The draft does not repeat it. The head fix is owed (answers, branch correction 4).
- The draft runs over the 3,500-character guide. The answers accept this (item 7).

## SOLR-9865

Verdict: DRIFT (1 item).

1. Draft says: "Counts, run 2026-10-05 at head `4937608bb181`: TestRestoreCore [CONFIRM: count], TestReplicationHandlerBackup 2 of 2, TestSnapshotCoreBackup 5 of 5."
   - Evidence: `receipts/SOLR-9865.md` line 6 (11 of 11: TestRestoreCore 4, TestReplicationHandlerBackup 2, TestSnapshotCoreBackup 5) and line 7 (TestRestoreCore 4 confirmed by a focused run at the head, 2026-10-11, 4 tests, 0 failures). `gates/SOLR-9865-17287-recount.md` line 5 (4937608bb181: 4 tests, 0 failures, DONE). The head file has `@Test` at lines 90, 182 and 245, plus `testBackupFailsMissingAllowPaths` (line 168), which has no annotation and runs by its test-name prefix. Gate date 2026-10-05 is the receipt's takeover-log date.
   - Replacement: "Counts at head `4937608bb181`: TestRestoreCore 4 of 4 (run 2026-10-05, and run again 2026-10-11 at this head), TestReplicationHandlerBackup 2 of 2 (run 2026-10-05), TestSnapshotCoreBackup 5 of 5 (run 2026-10-05)."

Checked and clean for 9865 (no action):
- Head lines: `RestoreCore.java` line 210 (records `previousIndexDirName`), lines 232-233 (deletes only when the previous name is `index`), line 235 (writes the previous name back). These match the draft.
- Base lines (merge-base `14c7aac0d151`) 227 deletes `index.properties` unconditionally, which the "What happens today" describes.
- Test `testFailedRestoreAfterSuccessfulRestoreKeepsCurrentIndex` at lines 182-243 (cited L182-L243).
- The ticket quote "would roll you back to index instead of index.timestamp1" matches `research/jira-context/SOLR-9865.json`.
- The SolrCloud claim holds: `InstallShardDataCmd.java` line 89 sends RESTORECORE to each replica, and `admin/api/RestoreCore.java` `doRestore` reaches the same handler `RestoreCore`.
- Changelog `changelog/unreleased/SOLR-9865-restore-rollback-previous-index.yml` exists at this head, and its title matches the draft's summary.
- The Choice (write back the previous name, with auto-discovery posed) matches the answers' ADOPTED decision and the ticket's suggestion.

Optional notes, not blocking:
- The Proof's sentence "A first version of the test had no reload step, and it passed on the base code too, so it did not show the bug." narrates an internal catch. `pr-formula.md` section 3 keeps such catches out unless they change shipped behavior. Optional replacement for the sentence pair: "The test reloads the core before the final check, so that check reads index.properties on a fresh open." (The reload wording is the one the answers adopt.)
- The head comment at `TestRestoreCore.java` lines 237-238 still gives the unverified mechanism. The replacement is owed on the branch (answers, branch correction 5; report g1 finding 1): `// reload the core so the check below reads index.properties on a fresh open`. It is comment-only, so per the moved-head rule the Proof then names the new head and says only a comment changed.
- The receipt's mechanism wording (premise logs `g9865-premise.log`, `g9865-premise2.log`) is not on disk. The draft's Proof does not repeat the mechanism, so it does not depend on those logs.

## SOLR-3701

Verdict: DRIFT (1 item).

1. Draft says (Changelog link): "Changelog: [`changelog/unreleased/SOLR-3701-spellcheck-apostrophe.yml`](https://github.com/nick-boss-tech/solr/blob/aabd678dec7330c55dbc044ce6b2f46dd00a0c29/changelog/unreleased/SOLR-3701-spellcheck-apostrophe.yml)". The Limits say: "The test does not check the final collation, only the converter's tokens."
   - Evidence: the fragment title at `aabd678dec7` reads "SpellingQueryConverter keeps a word with an apostrophe (pandora's) as one token, so collations no longer come out as pandora's's." The title states a collation result, but the test checks tokens only (`SpellingQueryConverterTest.java` lines 74-87), and the draft's own Limits say so. Owner item 3 in `reports/spellcheck-draft-round.md` is still open.
   - Correction of record: `reports/spellcheck-draft-round.md` says the "pandora's's" symptom has no source. `research/jira-context/SOLR-3701.json` shows the collation `spell:pandora's's star`, so the symptom is sourced. The problem is the untested outcome claim in the title.
   - Replacement (YAML title line, a changelog-only commit that moves the head): `title: "SpellingQueryConverter keeps a word with an apostrophe, such as pandora's, as one token instead of splitting it into pandora and 's."`

Checked and clean for 3701 (no action):
- Base pattern at `cabedd1d968` line 86 (the link the draft uses, "base code"). Head pattern at `aabd678dec7` lines 86-88, with the U+2019 alternative at line 88.
- Test `testApostropheStaysInWord` at lines 74-87 asserts two tokens, `pandora's` at offsets 0 to 9, then `star`. This matches the Proof.
- Token results: on base, the word `pandora` then `'s` (base pattern has no apostrophe suffix). With the new suffix, `pandora's`, `don't` and `qu'il` stay whole. `l'homme` and `d'accord` give `'homme` and `'accord`, because the word part needs two characters. These match the Limits, by reading the pattern (no run).
- Proof matches `receipts/SOLR-3701.md` (exactly 1 failure on base production; checked 2026-10-06 at head `aabd678dec7`).

Optional notes, not blocking:
- "The rule ... one optional suffix" (What this change does): the group is `(?:['’]\p{L}+)*`, so it is zero or more apostrophe parts. "zero or more apostrophe parts" is exact.
- Breadth (answers, decision 2): stated in What this change and named in Limits. No Choice section is required by the answers. A possessive-only rule is a live alternative (the draft-round report says it can be written but would also match it's and let's), so a Choice section is optional.
- Plain language: "contractions", "elisions" and "collation" are compressed terms. Optional: "words such as don't and qu'il".

## SOLR-4367

Verdict: DRIFT (2 items).

1. Draft says (Proof): "[Owner to supply before posting: the run counts, and the failure on base code. The answers file names the receipt but gives neither.]"
   - Evidence: `receipts/SOLR-4367.md` gives a date, the head and a Pairs round, but no run counts and no base-code result. `reports/spellcheck-draft-round.md` owner item 1 keeps this line open. `pr-formula.md` section 3 requires the base-code failure and the counts.
   - Replacement: none can be written from the worktree or the receipt. Replace the bracket with this sentence once the takeover log supplies both values: "On the base code the test fails with `<observed message>`. With this change, SpellCheckComponentTest `<n>` of `<n>`." The `<...>` values come from the owner. The draft is not postable until they are in.

2. Draft says (Proof): "Checked 2026-10-06 at head `0af6087f43fa`. A second check at the same head reviewed the proof and the description wording; the code did not change."
   - Evidence: `pr-formula.md` section 3: "Proof reports outcomes only." The second sentence narrates an internal review step.
   - Replacement (both sentences): "Checked 2026-10-06 at head `0af6087f43fa`."

Checked and clean for 4367 (no action):
- `SpellCheckComponent.java` lines 698-706 hold the top-level `classname` check and the `SolrException` that names the `spellchecker` list. Line 707 closes the `if`, so the range stops one line short of the close, which is fine.
- `SolrCore.java` line 1141 (`resourceLoader.inform(this)`) and the constructor catch at lines 1167-1186 match the text.
- The check runs in `inform`; `queryAnalyzerFieldType` is read at line 737. Other top-level keys (name, sourceLocation, field, class) are not read, so the "still ignored" Limit holds.
- Test `testSpellcheckerOutsideSpellcheckerListIsRejected` at lines 284-294 sets name, classname and sourceLocation at the top level and expects a `SolrException` containing "spellchecker". This matches the draft.
- The changelog `changelog/unreleased/SOLR-4367-spellcheck-misplaced-config.yml` exists at this head, and its title matches the draft's summary.
- Limits match `material/spellcheck-round-5-answers.md` decision 5: classname-only scope with a follow-up offer, and the single-trigger test named as a Limit.

Optional notes, not blocking:
- Throw at core load versus warn-and-load is the one behavior choice the answers adopted without a Choice section. A maintainer could plausibly reject a load failure, so a Choice section ("throw at load, or log a warning and keep loading") would meet the `pr-formula.md` section 4 bar. Optional.
- Plain language: "inform" is a Solr method name. "The check runs while the core is built" is plainer.

---

## Not done

- Core-admin drafts and the replication drafts outside this slice (SOLR-11650, 12085, 12246) are not in this slice.
- No builds, tests or gates. The 9865 and 17287 TestRestoreCore count rests on the recount note and the receipts. The recount's JUnit XML (`/workspace/gates/logs/vm2/recount-*.log`) is not on disk, so I did not re-read it.
- Not on disk: gate logs, the 9865 premise logs, and the spellcheck takeover log. So the SOLR-4367 base-code result and counts, and the SOLR-9865 mechanism, are unverified.
- Token results for SOLR-3701 were checked by reading the pattern, not by running it.
- Fork links for the merge-base SHAs (`cabedd1d968`, `22a8cfebbbdb`) were checked against the local object store only, not against GitHub.
- Live JIRA was not queried. Ticket text was checked against the `research/jira-context` packets.
- Not committed, not pushed. The claim is not marked DONE here; the lead does that with the deliverable.
