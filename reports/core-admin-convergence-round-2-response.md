# Core admin convergence, round 2: main-side response

Round 2 returned NOT CONVERGED: 11 of the 18 re-checked drafts converge and 7 do not (SOLR-4502, 5011, 8576, 9750, 12916, 17708, 17731). Its new findings are 4 should-fix (CA-R2-01, CA-R2-02, CA-R2-41, CA-R2-42) and 2 notes (CA-R2-03, CA-R2-04), and it carries CA-R1-01, CA-R1-24 and CA-R1-34 still open, with CA-R1-87 still owed. This lane verified every round 2 finding against the receipts and the branch code before applying anything. All four should-fix findings hold, and all four are applied to the drafts in this lane, in the register's exact replacement text. No finding is rebutted this round. CA-R2-04 (a note with a factual line range) holds and is applied; CA-R2-03 is a record item and is recorded below. The three carried should-fix items are branch-side or gate-side and stay open, each with its state re-checked below. Application method: exact-string replacements, each asserted to occur exactly once before applying; the full diff was then reviewed file by file.

## Counts

| Grade | Round 2 new items | Applied in this lane | Record item | Still open |
|---|---|---|---|---|
| Should-fix | 4 (CA-R2-01, 02, 41, 42) | 4 | 0 | 0 |
| Note | 2 (CA-R2-03, 04) | 1 (CA-R2-04) | 1 (CA-R2-03) | 0 |
| Carried should-fix | 3 (CA-R1-01, 24, 34) | 0 | 0 | 3 |
| Carried note owed | 1 (CA-R1-87) | 0 | 0 | 1 |

After application, 15 of the 18 re-checked drafts are CONVERGED on their text (the 11 round 2 confirms plus SOLR-9750, 5011, 17708 and 17731). SOLR-4502, 8576 and 12916 are not; each waits on a branch commit or a gate job, named below. SOLR-5011's text is converged and its hold stands for the narrowing job; SOLR-17731's text is converged at the current head, with CA-R1-87 still owed.

## Fact checks before applying

- **SOLR-9750 (CA-R2-01):** the draft's line 9 names `/admin/info` with the parenthetical "(linked at the merge-base)" and carries no link on it. `ImplicitPlugins.json` at the merge-base `14c7aac0d151` line 78 reads `"useParams":"_ADMIN_CORE"`, the entry the replacement links.
- **SOLR-5011 (CA-R2-02):** `reports/solr-5011-shared-schema-analysis.md` names the reload case at lines 58 and 75 (the reloaded core takes the cached schema, and the old core's close inside `registerCore` then closes that schema's loader; the case applies only when the schema file is unchanged), and `gates/SOLR-5011-narrowing.md` carries the same case in its proving test (line 10 and the test spec). The draft's Limits named only the unload case.
- **SOLR-17731 (CA-R2-41):** receipts/SOLR-17731.md records the counts at the head (`f2b4ba164f56`) and no run date; its only date is the takeover-log reference (round 35, 2026-10-07). The draft's "(verified 2026-10-07)" presented that as a run date, the defect CA-R1-65 graded for SOLR-17377.
- **SOLR-17708 (CA-R2-42):** at the gated head `2eceb5473037`, `V2HttpCall.shouldAuthorize()` (lines 228 to 232) returns false only when `api == null` and the action is ADMIN or PROCESS. A non-JAX-RS v2 API has an Api object and still runs the first check, so the draft's sentence ("a v2 path that matches no JAX-RS resource no longer goes through the first check") covered requests the code does not skip, and contradicted the same paragraph's last sentence.
- **SOLR-12007 (CA-R2-04):** in `SolrCore.java` at `bdeba582fd63`, the private `cleanupOldIndexDirectories(boolean, boolean)` overload starts at line 3501 and its closing brace is line 3528. The draft cited 3501 to 3525.
- **SOLR-4502 (CA-R1-01, CA-R2-03):** `git ls-remote` shows `solr-4502-submit` still at `0ee4c644ee63`, and the fragment there still ends its title "...instead of creating a core that later hits a NullPointerException on search". The retitle has not landed.
- **SOLR-8576 (CA-R1-24):** `solr-8576-submit` still at `4c46f95c7851`; `gates/SOLR-8576-alias-fix.md` is OPEN and unclaimed. The draft updates ride that job.
- **SOLR-12916 (CA-R1-34):** `solr-12916-submit` still at `50b1f4bcab03`, and the fragment there is still titled "...accepts warming queries given to the Config API as flat name/value lists...", naming only the Config API. The retitle has not landed. (Round 2's verdict line says a fragment commit `1a4eb16c567` "has landed"; that commit is the original fragment commit in the branch history, not the retitle. The round's carried-items statement, that the title commit has not landed, is the accurate one.)
- **SOLR-17731 (CA-R1-87):** `gates/SOLR-17731.md` is OPEN and unclaimed, and the branch tip is unchanged, so the fixes and the `ListAliasesAPITest` run it names are not on the branch yet.

## Per-finding dispositions

### SOLR-9750
- CA-R2-01 (should-fix): applied. `/admin/info` now links `_ADMIN_CORE` at the merge-base line 78, in the register's exact replacement. The draft converges.

### SOLR-5011
- CA-R2-02 (should-fix): applied, both parts. The Limits summary now reads "the shared-schema cases that reach a closed loader", and bullet 1 gains the reload case in the register's exact appended text. The draft's text now matches the analysis record. The hold stands: it waits on `gates/SOLR-5011-narrowing.md`, and the hold is not a finding.

### SOLR-17731
- CA-R2-41 (should-fix): applied. The Proof line now reads "2 of 2 at head `f2b4ba164f56`. Recorded 2026-10-07 at this head."
- CA-R1-87 (note): still owed. It rides `gates/SOLR-17731.md` (OPEN, unclaimed); when that job lands, the head, counts and links update in its pass. With CA-R2-41 applied, the draft is CONVERGED on its text at the current head, the same footing the round 1 response recorded.

### SOLR-17708
- CA-R2-42 (should-fix): applied. The behavior-change sentence now scopes the skip to a v2 request with no v2 API object that is an admin or core request, in the register's exact replacement. The settled behavior is unchanged; only the scope words moved. The draft converges.

### SOLR-12007
- CA-R2-04 (note): applied. The citation now reads lines 3501 to 3528, in the text and in the link anchor. The draft was CONVERGED in round 2 and stays so.

### SOLR-4502
- CA-R1-01 (should-fix, branch-side): still open. The fragment replacement is recorded in the round 1 response for the packaging lane. NOT CONVERGED until it lands.
- CA-R2-03 (note): recorded here, as the round asks. When the CA-R1-01 fragment commit lands, `0ee4c644ee63` stops being the head: the draft's references at lines 15, 21, 33 and 37 move to the new head in that pass, and because receipts/SOLR-4502.md covers only `0ee4c644ee63`, the title change needs a changelog parse and a top-up gate at the new head.

### SOLR-8576
- CA-R1-24 (should-fix): still open, unchanged in state. The alias fix and its run ride `gates/SOLR-8576-alias-fix.md`; the marker, head and counts update in that job's pass. NOT CONVERGED until then.

### SOLR-12916
- CA-R1-34 (should-fix, branch-side): still open. The fragment replacement is recorded in the round 1 response for the branch lane; the draft's changelog link moves to the new head in the same pass. NOT CONVERGED until it lands.

### SOLR-18010
- CA-R1-91 (note, branch-side): stands, as round 2 records. It is a note, not a should-fix, and does not hold the draft.

## Record notes from the round, dispositioned

- Round 2 disputes the round 1 response's CONVERGED labels for SOLR-12916, 17708 and 17731. For 17708 and 17731 the dispute rested on the new should-fix findings, both now applied; their verdicts below follow the round's own rule (no open blocking or should-fix item). For 12916 the label is corrected here: with CA-R1-34 open, the draft is NOT CONVERGED, and round 3 carries it.
- The answers' gate states for SOLR-12916 and SOLR-16499 ("No gate recorded") and for SOLR-4502 and SOLR-5011 ("NO GATE") are superseded by the receipts, which record green gates at the live tips. The receipts govern; no application turns on the stale lines.
- SOLR-16725's owed branch edits (a raw-request test comment and a ClusterStatus javadoc) have not landed; they would move the head and need a top-up run. Branch-side state, recorded; the draft is CONVERGED.
- CA-R1-69 (SOLR-17297): the observed base failure line is still owed on the main side. Record item, unchanged.

## Verdicts after application

CONVERGED: SOLR-6438, 9750, 12007, 13246, 15024, 15805, 16499, 16725, 16849, 17297, 17377, 17708, 18010, and on text SOLR-5011 (hold stands for the narrowing job) and SOLR-17731 (CA-R1-87 owed on its gate job).
NOT CONVERGED: SOLR-4502 (CA-R1-01), SOLR-8576 (CA-R1-24), SOLR-12916 (CA-R1-34).

## What round 3 needs to settle

Round 3 re-checks only the three drafts still open (SOLR-4502, 8576, 12916): whether the CA-R1-01 fragment retitle has landed (and the draft's head references and top-up state with it), whether the SOLR-8576 alias fix has landed under its gate job (and the marker, head and counts with it), and whether the CA-R1-34 fragment retitle has landed (and the draft's changelog link with it). No item was rebutted this round, so round 3 has no rebuttal to judge. The assignment is `assignments/pool-core-admin-convergence-round-3.md`.
