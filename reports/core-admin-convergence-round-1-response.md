# Core admin convergence, round 1: main-side response

Round 1 returned NOT CONVERGED: 18 of the 21 drafts were NOT CONVERGED, with 7 blocking, 28 should-fix and 29 note findings. This lane verified every finding against the receipts and the branch code before applying anything, applied each holding finding's exact replacement text to its draft, and dispositions the rest below. Two should-fix findings (CA-R1-01, CA-R1-34) hold but their replacement text edits a branch changelog fragment rather than the draft; they are recorded here verbatim for the branch lanes. One should-fix (CA-R1-24) is not applied: its precondition has not happened, and the reason is written out. Application method: exact-string replacements, each asserted to occur exactly once (or a stated count for link swaps) before applying; the full diff was then reviewed file by file.

## Counts

| Grade | Items | Applied in this lane | Branch-side, recorded below | Not applied |
|---|---|---|---|---|
| Blocking | 7 | 7 | 0 | 0 |
| Should-fix | 28 | 25 | 2 (CA-R1-01, CA-R1-34) | 1 (CA-R1-24) |
| Note | 29 | 3 (CA-R1-03, CA-R1-25, CA-R1-88) | 0 | 26 standing or record items |

After application, 19 of the 21 drafts are CONVERGED on their text. SOLR-8576 is not (CA-R1-24 open). SOLR-5011's text is converged and its main-side hold stands; the hold was never a finding.

## Fact checks before applying

- **SOLR-5011 (CA-R1-04, 05, 06):** `reports/solr-5011-shared-schema-analysis.md` returns UNSAFE. With shareSchema on, the surviving core's cached schema keeps the origin core's loader; after the origin unloads and its loader closes, a schema add-field-type on the survivor naming a lib class not yet loaded fails through that closed loader (analysis sections 3 and 4). The draft's "so the result is unknown" contradicted the record, and its "should not be used for later class loading" was wrong in the same place.
- **SOLR-8576 (CA-R1-21):** the test at head `4c46f95c7851` (CollectionsAPISolrJTest.java lines 1184 to 1188) asserts the original collection still exists and that `hasCollection(aliasName)` is false. No assertion checks that the alias still resolves, so "the alias are unchanged" overstated the test.
- **SOLR-8576 (CA-R1-22):** at the merge-base `b6b2b8f10e98`, no test asserts either create message; `ReindexCollectionTest` checks "already exists" for a reindex target (lines 348, 354) and `CollectionsAPISolrJTest` mentions "already exists" for a rename (line 1147). The narrower claim holds.
- **SOLR-9750 (CA-R1-26):** `ImplicitPlugins.json` at the merge-base `14c7aac0d151`: `/admin/info` uses `_ADMIN_CORE` (line 78), `/tasks/cancel` uses `_TASK_CANCELLATION`, `/tasks/list` uses `_LIST_TASKS`, `/graph` uses `_ADMIN_GRAPH`. "Each implicit handler names its paramset after its endpoint" is not true of the base.
- **SOLR-17708 (CA-R1-81, 82, 83, 84):** receipts/SOLR-17708.md gates the live tip `2eceb5473037` on 2026-10-11 with JaxRsSingleAuthorizationTest 2 of 2, BasicAuthIntegrationTest 1 of 1, AuditLoggerIntegrationTest 9 of 9; `git ls-remote` confirms that tip. CA-R1-83 is the one exception to the head swap: the Limits link to `V2HttpCall.java` L166 is pre-change code (the admin route block sits at lines 164 to 168 at `9b3a84b1c460`), so it points at the base commit instead of the new head.
- **SOLR-17731 (CA-R1-85):** the branch diff against `e2cdb2d7e8ae` touches nine files, including `ListAliasesApi.java`, `CollectionsHandler.java` and the new handler class `GetAliasByName.java`. "Local to two API declarations" was wrong.
- **SOLR-18010 (CA-R1-90):** `SecurityConfHandlerLocal.java` at head `c3685bb37d9d` moves with `ATOMIC_MOVE` (lines 127 to 131) and falls back to a plain move (line 133).
- **SOLR-15024 (CA-R1-44, 46):** the test at head `f95b5010b3fe` asserts the class name only for the first entry (`lst[1]`) and the pattern for each entry; the token filter block sits at lines 1028 to 1036 at the merge-base `b5c71bc5573c`, and the `schema.js` loop at lines 648 to 670 there.
- **SOLR-16499 (CA-R1-50):** the branch diff against `e2cdb2d7e8ae` touches only the changelog fragment, `ReplaceNodeRequestBody.java`, `ReplaceNode.java`, `CollectionsHandler.java` and the test. The guide, `ReplaceNodeCmd`, `AddReplicaCmd` and `ReplicaMigrationUtils` are unchanged; at the merge-base, `ReplaceNodeCmd` lines 55 to 56 read the timeout (default 600) and parallel (default false).
- **SOLR-16849 (CA-R1-62):** the parent of `f2aaf8769fd` is `b2e1f8ed4a30`, the commit the replacement's links use.
- **SOLR-6438 (CA-R1-09):** receipts/SOLR-6438.md records the base run failing with "expected 400 but was 500", so "The request does not fail" conflicted with the draft's own evidence.
- **Merge-bases (CA-R1-23, 31, 42, 45, 48):** verified with git: SOLR-8576 `b6b2b8f10e98` (ancestor of the head), SOLR-12007 `14c7aac0d151`, SOLR-13246 `97d973814336` (line 48 there is the debug line printing the searcher), SOLR-15024 and SOLR-15805 `b5c71bc5573c`.
- **Record dates (CA-R1-27, CA-R1-65):** the receipts record the runs through the takeover log (SOLR-8576 round 28, 2026-10-07; SOLR-9750 reconciliation gate, 2026-10-05; SOLR-17377 recorded 2026-10-05), not as run dates.

## Per-finding dispositions

### SOLR-4502
- CA-R1-02 (should-fix): applied.
- CA-R1-01 (should-fix): holds, branch-side. The fragment at `0ee4c644ee63` is titled "...instead of creating a core that later hits a NullPointerException on search", and receipts/SOLR-4502.md records the premise run failing at create; that search-time state is not reached on base. The draft carries no title text (its changelog line is a link), so there is nothing to edit in the draft. Branch replacement, recorded for the packaging lane: "CoreContainer.create now fails with a message that names load() when the container has not been loaded, instead of a NullPointerException message."

### SOLR-4989
- CA-R1-03 (note): applied. At `97d973814336` lines 235 to 236 are the SCHEMA branch and lines 237 to 238 are the default branch the draft describes; the citation now reads L237-L238.

### SOLR-5011
- CA-R1-04, CA-R1-05 (blocking), CA-R1-06, CA-R1-07 (should-fix): applied (fact checks above). The draft now states the shared-schema verdict the record carries.
- CA-R1-08 (note): stands. The changelog title is rechecked when the close is narrowed; the narrowing job is `gates/SOLR-5011-narrowing.md`. The draft stays HELD until that job lands; the hold is the main side's, not a finding.

### SOLR-6438
- CA-R1-09 (should-fix): applied.

### SOLR-8275
- CA-R1-10, CA-R1-11 (notes): stand. The draft already attributes the quoted message to the ticket, and its Limits already cover the untested message variants on this path. The draft was CONVERGED in round 1 and is unchanged.

### SOLR-8576
- CA-R1-21 (blocking), CA-R1-22, CA-R1-23 (should-fix): applied in the current form. CA-R1-21's second form ("...that the original collection is unchanged and that the alias still resolves to it") applies only after the alias fix lands and is recorded here for that pass.
- CA-R1-25 (note): applied ("It pins error behavior" to "It checks error behavior").
- CA-R1-27 (should-fix): applied.
- CA-R1-24 (should-fix): NOT APPLIED. The register prescribes deleting the [OWED BEFORE POSTING] marker together with the head and counts update "after the fix run". The alias fix has not landed: `git ls-remote` shows `solr-8576-submit` still at `4c46f95c7851`, the head the draft names, so the head, counts and links still describe the pre-fix test. Deleting the marker now would drop the only in-draft record of the owed fix. The marker stays until the fix lands and `CollectionsAPISolrJTest` runs at the new head; round 2 re-checks the state.

### SOLR-9750
- CA-R1-26 (blocking), CA-R1-27 (should-fix): applied.
- CA-R1-28 (note): stands. The Limits already state that the new test reads `ImplicitPlugins.json` and checks the value.

### SOLR-11939
- CA-R1-29, CA-R1-30 (notes): stand. CA-R1-30 is a packaging item (the root testing file comes off the branch before opening; the gate job `gates/SOLR-11939.md` covers packaging). The draft was CONVERGED in round 1 and is unchanged.

### SOLR-12007
- CA-R1-31 (should-fix): applied.
- CA-R1-32 (note): stands. The draft's summary and Limits already state that reload and the other callers of the public cleanup method use the background thread; core creation is among those callers.
- CA-R1-33 (note): stands. The length (about 3,900 characters) is accepted: the draft carries a real two-route Choice on close timing.

### SOLR-12916
- CA-R1-35 (should-fix): applied at the current head `50b1f4bcab03` (lines 108 to 110 and 118 to 119 verified in `QuerySenderListener.java` at that head).
- CA-R1-34 (should-fix): holds, branch-side. The fragment at `50b1f4bcab03` is titled "...accepts warming queries given to the Config API as flat name/value lists...", naming only the Config API. Branch replacement, recorded for the branch lane: "QuerySenderListener accepts warming queries given as flat name and value lists, from the Config API and from XML, instead of ignoring them." The draft's changelog link moves to the new head in the same pass that lands that commit; until then it correctly points at `50b1f4bcab03`.
- CA-R1-36 (note): stands as a record item. The handoff commit in the branch history is decided before the PR commit list is shown; it is not a draft-text item.

### SOLR-13246
- CA-R1-42 (should-fix): applied.
- CA-R1-41 (note): record correction, not a draft item. The premise record's `SolrCore.java` line 2823 is inside a comment block at head and prints nothing; the draft's generic Limits line is right. The correction belongs to the record that carries the premise.
- CA-R1-43 (note): stands. The draft's "tens of kilobytes on a large index" matches its own cited ticket report of a message above 20 KB.

### SOLR-15024
- CA-R1-44, CA-R1-45, CA-R1-46 (should-fix): applied.
- CA-R1-47 (note): stands. receipts/SOLR-15024.md records the suite failing against base production, so the base failure is a run result; the "because" clause states the shape the new test asserts. Confirming the ticket text is a posting-time item.

### SOLR-15805
- CA-R1-48 (should-fix): applied.
- CA-R1-49 (note): stands, with the evidence narrowed. The gate log `g15805-harden.log` on this host records the pre-fix proof as PASS (the focused test fails on unpatched code) but names no failing assertion, so there is no failure text to quote; the draft's code-reading statement stands as written.

### SOLR-16499
- CA-R1-50 (should-fix): applied. Eight links to the four unchanged files now point at the merge-base `e2cdb2d7e8ae`, and the draft says so once, in What happens today.
- CA-R1-53 (note): stands. The length (about 6,000 characters with URLs) is accepted for a two-API, two-parameter change; rechecked after CA-R1-50 as the note asks, and no further trim is taken.

### SOLR-16725
- CA-R1-51 (blocking): applied. No record supports "most collections use" CREATE or how clients read the output; the replacement rests on the ticket's own example, which the draft already cites.
- CA-R1-52 (note): stands.

### SOLR-16849
- CA-R1-61, CA-R1-62 (should-fix): applied.
- CA-R1-66, CA-R1-67 (notes): stand. The receipt confirms the pass on unpatched main is the designed confirmation, which the draft now states in its summary.

### SOLR-17297
- CA-R1-63 (should-fix): applied, in the register's exact text. The record names the reverse order as a follow-up (receipts/SOLR-17297.md and the Group O settlement), and the Limits now offer it on request instead of promising a separate pull request.
- CA-R1-68 (note): stands. The draft already states the ticket reproduces the case with a module enabled in solr.xml.
- CA-R1-69 (note): record item. receipts/SOLR-17297.md records the proof as PASS without the observed failure line; the observed line stays owed on the main side.

### SOLR-17377
- CA-R1-64, CA-R1-65 (should-fix): applied.
- CA-R1-70, CA-R1-71 (notes): stand, per the notes themselves (no record requires a landing-order Limit; the Limits line on module classes is supported by the code).

### SOLR-17708
- CA-R1-81 (blocking), CA-R1-82, CA-R1-83, CA-R1-84 (should-fix): applied. The head hold is cleared: every head link now uses `2eceb5473037` except the Limits route link, which CA-R1-83 points at the base commit. Verdict after application: CONVERGED, at the gated live tip.

### SOLR-17731
- CA-R1-85 (blocking), CA-R1-86 (should-fix): applied.
- CA-R1-88 (note): applied ("changes routing for every v2 API" to "changes how the router orders endpoints"); no source checked gives the wider claim.
- CA-R1-87 (note): record item. The head and links update when the owed fixes land; the job is `gates/SOLR-17731.md` (its FIX 2 is the owed `ListAliasesAPITest` run).
- CA-R1-89 (note): stands. The length (about 3,800 characters) is accepted for a two-endpoint routing change with a router Choice.

### SOLR-18010
- CA-R1-90 (should-fix): applied.
- CA-R1-91 (note): branch-side, recorded for packaging. Changelog title replacement: "Standalone security.json edits are serialized and written atomically."
- CA-R1-92, CA-R1-93 (notes): stand. The length (about 5,400 characters) is accepted: the change carries two mechanisms (edit lock and atomic write) plus a marker removal.

## Record notes from the round, dispositioned

- The SOLR-15805 branch history includes a commit carrying a Claude co-author trailer. That blocks opening that branch, not the draft text; it stays a main-side decision.
- The answers' gate states for SOLR-17708 and SOLR-18010 are superseded by the receipts; the receipts govern, and the applications above follow them.
- The answers' line that packaging uses the origin tip for SOLR-16499 is superseded: the packaging commit is the live head, and CA-R1-50 was checked against it.
- SOLR-17708's round-35 report is absent from the record. Record gap, noted; the receipt carries the gate facts the draft needs.

## Verdicts after application

CONVERGED: SOLR-4502 (branch-side CA-R1-01 owed before opening), 4989, 5011 (text converged; hold stands), 6438, 8275, 9750, 11939, 12007, 12916 (branch-side CA-R1-34 owed), 13246, 15024, 15805, 16499, 16725, 16849, 17297, 17377, 17708, 17731 (at the current head), 18010.
NOT CONVERGED: SOLR-8576 (CA-R1-24 open, pending the branch alias fix and its re-run).

## What round 2 needs to settle

Round 2 re-checks the 18 drafts round 1 marked NOT CONVERGED (SOLR-4502, 5011, 6438, 8576, 9750, 12007, 12916, 13246, 15024, 15805, 16499, 16725, 16849, 17297, 17377, 17708, 17731, 18010), verifies each disposition above against the live draft text, and gives a verdict of CONVERGED or NOT CONVERGED per draft. It re-checks the CA-R1-24 rebuttal state (whether the SOLR-8576 alias fix has landed and the class has run at a new head), the branch-side items CA-R1-01 and CA-R1-34 (whether the fragment commits have landed), SOLR-5011's text against the analysis record with the hold still not a finding, and SOLR-17731's head state if `gates/SOLR-17731.md` has landed. The assignment is `assignments/pool-core-admin-convergence-round-2.md`.

## Addendum (2026-10-11): voice correction after application

Two sentences the applications took verbatim from the round 1 registers used the first-person "we", which authored text does not use. Both are corrected in the drafts, with the content and the offer unchanged: SOLR-17297's Limits now reads "A follow-up ticket and pull request can be opened for it on request, with its own test for a module SPI when a shared lib is present." (CA-R1-63), and SOLR-9091's Limits in the Replication and backup application now reads "A follow-up ticket and pull request can be opened for that on request." The sweep covered every line the two convergence applications added across the 28 edited drafts; these were the only two first-person "we" sentences introduced. Pre-existing text was not touched, and no finding disposition changes.
