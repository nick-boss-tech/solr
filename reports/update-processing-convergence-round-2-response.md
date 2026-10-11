# Update processing convergence, round 2: main-side response

Round 2 returned NOT CONVERGED: 12 of 28 drafts converged, 16 not, with one blocking item (UPC-R2-501) and 17 new should-fix items, plus three carried round 1 items. This response applies the blocking item under the round 2 report's own conditional branch, applies every new should-fix replacement and the carried items, and records one conflict where a prescribed sentence contradicts the workspace record. Application method: exact-string replacements, each asserted to occur exactly once before applying; the full diff was then reviewed file by file.

## Counts

| Grade | Items | Applied | Applied under a conditional branch | Not applied (conflict) |
|---|---|---|---|---|
| Blocking | 1 (UPC-R2-501) | 0 | 1 (line 24) | 1 part (the line 26 qualifier sentence) |
| Should-fix (new) | 17 | 17 | 0 | 0 |
| Carried from round 1 | 3 (UPC-R1-14, UPC-R1-227(c), UPC-R1-231) | 3 | 0 | 0 |

UPC-R1-232 is UPC-R2-501 and is counted there.

## The blocking item: UPC-R2-501 (SOLR-7022)

The round 2 report offers two branches: if the takeover-log entry for the gate at `6a233ab2fdb` is produced in the workspace and accepted, keep the counts, drop the log name, and keep line 26 with the "checked by reading" qualifier; otherwise replace line 24 with the `a2ed957063b` citation and line 26's last sentence with the qualifier sentence.

The entry is produced on this host. The takeover log's Review1 entry (2026-10-06) records the gate green at `6a233ab2fdb` with `DirectUpdateHandler2CommitWaitTest` 1 of 1 from fresh JUnit XML, and the files are on disk here: `g7022r1-gate.log` with its per-class XML (CommitWaitTest tests=1, failures=0), and `g7022r1-premise.log` with `g7022r1-premise-DirectUpdateHandler2CommitWaitTest.xml`. This is the same record the round 1 response's UPC-R1-232 reconciliation relied on, and the later closeout (`material/update-processing-round-3-closeout.md`, owner decision 1) prescribes citing that gate over `material/review-opened-28-answers.md` item (3). So:

- **Line 24: applied under the conditional branch.** The counts at `6a233ab2fdb` (2026-10-06) stay; the log file name is dropped as internal process vocabulary. The fallback replacement text (the `a2ed957063b` citation) was not used, because its premise, that no workspace record gives the CommitWaitTest counts, does not hold on this host.
- **Line 26 qualifier sentence: not applied; conflict recorded.** The prescribed sentence says the commit-level test's base failure "was checked by reading the base code, not by a run." The workspace record contradicts that: the premise run on untouched base `97d973814336` with only the test file added exists on this host (`g7022r1-premise.log`, 2026-10-06; its XML shows tests=1, failures=1), and the takeover log's Review1 entry records that run and its failure at the interrupt-restored assertion. The draft's existing last sentence ("The commit-level test, DirectUpdateHandler2CommitWaitTest, is the one that fails on the base at the interrupt-restored assertion") is supported by that run and stands unchanged. Round 3 is asked to judge line 26 against this recorded evidence.

## Fact checks before applying

- **UPC-R2-301 (SOLR-11483):** `g11483-premise.log` opens "premise tree: base production + branch test"; its XML shows 21 tests, 1 failure; the failure text is `expected:<1000> but was:<10>`. The replacement's setup statement and values match.
- **UPC-R2-305 (SOLR-13265):** receipts/SOLR-13265.md records the pre-fix proof on base production (3 tests, exactly 1 failure, the branch's test class in place), and `g13265-failbefore-base.log` is the 2026-10-09 run with seed `13265C0FFEE13265`. The replacement's run date and seed match.
- **UPC-R2-402 (SOLR-14718):** the mapping is confirmed against the audit record of the JIRA packet: `audits/update-processing/SOLR-14718.md` finding 2 names the packet's second flaw as the streaming-client association that can report the first document of a batch, which is the wrong-document case the draft's line 34 states. The replacement makes that identity explicit.
- **UPC-R2-403 (SOLR-16655):** the test name is confirmed in the pre-fix gate log: `g16655-gating-prefix.log` shows `FieldMutatingUpdateProcessorTest > testTrimFieldsRecurseIntoNestedDocumentsOnlyWhenParentSelected FAILED` at the pre-fix head, and receipts/SOLR-16655.md records the proof at `b201a57fb3e5`.
- **UPC-R2-404 (SOLR-16673):** receipts/SOLR-16673.md records the gate at `d5c19e64ba1b` (2026-10-04), `ParsingFieldUpdateProcessorsTest` 43 of 43, and the 2026-10-09 top-up at `d7170b12f312` changing only the changelog fragment title. The replacement matches, including the dates.
- **UPC-R2-405 (SOLR-3657):** receipts/SOLR-3657.md records the pre-fix proof: on base production, `DocumentBuilderTest` runs 17 tests with exactly 2 failures, both at the destination-prefix assertions; the gate date is 2026-10-07. The replacement matches.
- **UPC-R2-306 (SOLR-13943):** receipts/SOLR-13943.md records the counts at `cc155cf68e1d` on 2026-10-09. The replacement only removes the process word and matches the receipt.
- **UPC-R2-505 (SOLR-7504):** the replacement names SOLR-12705's head as `aa56b7b1be4`, the head the SOLR-12705 draft itself cites as verified 2026-10-09. Recorded here, as the round 2 report asks: the SOLR-12705 head moved after round 3; the round 3-era combined run used the earlier head `22b77196e662`.

No other replacement was held back for contradicting a receipt.

## Per-finding dispositions

### SOLR-7022
- UPC-R2-501 (blocking): line 24 applied under the conditional branch; line 26 qualifier not applied, conflict recorded above.

### SOLR-11483
- UPC-R2-301 applied (verified above).

### SOLR-12705
- UPC-R2-302 applied (Limits summary now says "some behavior is missing from the changelog"; the dropped-field bullet now ends "...or the output change to IgnoreFieldUpdateProcessorFactory described above").
- UPC-R2-303 applied (the counted-field sentence is deleted from Proof line 25; the Limits bullet "- The test does not assert what a counted field does with an atomic update. Counted fields follow the rule in SOLR-7504, which lands first." is added after the dropped-field bullet).
- UPC-R1-14 (carried): applied as a move. The pre-fix head bullet (b053944b127) now sits directly after the `ParsingFieldUpdateProcessorsTest` bullet, above the combined-run bullet, as the round 1 register specified.

### SOLR-12864
- UPC-R2-304 applied (the repeated "cannot show a failure" sentence is removed from the Proof bullet; the bold summary still carries the point).

### SOLR-13265
- UPC-R2-305 applied (both Proof lines now name the merge-base `9b3a84b1c460` and the branch test file applied; verified above).

### SOLR-13943
- UPC-R2-306 applied ("from a test run at cc155cf68e1d"; the fragment sentence now says "which that run did not include").

### SOLR-14718
- UPC-R2-401 applied (Limits summary now reads "a failed merged stream that reports its members names each request against its own document").
- UPC-R2-402 applied (verified above; the second-flaw bullet now names the flaw and its identity with the wrong-document case).

### SOLR-16655
- UPC-R2-403 applied (the failing test is named; verified above).

### SOLR-16673
- UPC-R2-404 applied (both Proof bullets replaced; verified above).

### SOLR-3657
- UPC-R2-405 applied (verified above).

### SOLR-5065
- UPC-R2-406 applied (Proof now names head `ab894a996c8a`).

### SOLR-5939
- UPC-R2-502(a) applied (the Limits bullet now reads "is reported against the request the client was built with", matching the UPC-R1-102 wording for SOLR-14718).
- UPC-R2-502(b) applied (the base-run sentence now states the branch's test class was added on the merge-base production code).

### SOLR-5941
- UPC-R2-503 applied ("round 30 focused list" is now "the focused run at `62516cc338e`"; "not re-run here" is now "not re-run at this head").

### SOLR-6065
- UPC-R1-227(c) (carried) applied (the base-run sentence now states the branch's test file was added on the merge-base production code).

### SOLR-6973
- UPC-R1-231 (carried) applied (same merge-base and test-file wording).
- UPC-R2-504 applied (the "review fix" narration is removed; the sentence now reads "The same 7 tests and the same base failure were re-verified at this head.").

### SOLR-7504
- UPC-R2-505 applied (the conflict sentence now names both heads, `2fe06bfd917f` and `aa56b7b1be4`).

## Round 2 notes: dispositions

The round 2 notes require no draft change in this lane and are recorded as read, with three exceptions that belong in this record:

- **UPC-R1-21 (SOLR-13265, carried note in round 2):** round 2 corrects the round 1 response's rationale. The correction stands: the cited lines do exist at the merge-base `9b3a84b1c460`; the earlier statement that they have no base counterpart was wrong. The draft's "does not touch that code" already meets the note, so no draft change follows.
- **UPC-R2-505 record note:** recorded above with UPC-R2-505 (the SOLR-12705 head moved after round 3).
- **UPC-R2-517 (SOLR-7022):** the premise entry in receipts/SOLR-7022.md carries no date or head. Receipt-side, outside this lane's draft scope; flagged here for the next receipt refresh. The premise record itself is the 2026-10-05 gate at `a2ed957063b` (log g7022-premise.log) per that receipt.

The remaining notes (owner calls, optional sentences, cross-cutting record items UPC-R2-415 to 417, and the per-draft observations) stay with the main side and are not round 3 scope except where they touch the text of the 16 drafts below.

## What round 3 needs to settle

Round 3 re-checks the 16 drafts that were not converged in round 2 (SOLR-11483, 12705, 12864, 13265, 13943, 14718, 16655, 16673, 3657, 5065, 5939, 5941, 6065, 6973, 7022, 7504), verifies each disposition above against the live draft text, and gives a verdict of CONVERGED or NOT CONVERGED per draft. For SOLR-7022 it judges line 26 against the evidence recorded in this response. The assignment is `assignments/pool-update-processing-convergence-round-3.md`.
