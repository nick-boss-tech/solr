# Update processing convergence, round 1: main-side response

Round 1 verdict was NOT CONVERGED: 105 findings (7 blocking, 71 should-fix, 27 notes) across the 28 drafts in `pr-drafts/update-processing/`. This file records what the main side did with each finding. Every blocking and should-fix replacement is applied to the drafts, with the exceptions and reconciliations recorded below. Round 2 is posted as `assignments/pool-update-processing-convergence-round-2.md`.

## Counts

| Grade | Total | Applied | Applied in substance | Conflict held back | Noted as read |
|---|---|---|---|---|---|
| Blocking | 7 | 7 | 0 | 0 | 0 |
| Should-fix | 71 | 70 | 1 (UPC-R1-232, see below) | 0 | 0 |
| Notes | 27 | 4 | 0 | 0 | 23 |

No replacement was held back for contradicting a receipt. The verifications and the three decision reconciliations are recorded next; then the per-draft disposition list.

## Decision reconciliations and fact checks

- **UPC-R1-103 (SOLR-14718, retry counting).** Applied as the report's draft wording: "Retries are counted per request, and for adds each request holds one document for one node." Reconciliation with the recorded round 2 decision ("retries are not per document"): the code counts retries per request (`SolrCmdDistributor` Req retry count), and for adds each request carries exactly one document to one node, so the decision's point stands in effect; the applied wording states the mechanism instead of the shorter phrase, which read as if adds were counted some other way. The decision record and the draft now agree.
- **UPC-R1-104 (SOLR-14718, counts in Proof).** The recorded "green wording without counts" decision stands. The receipt at `29c09959791a` would permit counts, but a receipt does not withdraw a recorded decision, so the conditional replacement is applied: "- With this change, the focused test class passes at this head (checked 2026-10-07)."
- **UPC-R1-232 (SOLR-7022, which gate the Proof cites).** Applied. The takeover log confirms the Review1 gate at `6a233ab2fdb` (log g7022r1-gate.log, 2026-10-06: DirectUpdateHandler2AwaitSearcherTest 2 of 2, DirectUpdateHandler2CommitWaitTest 1 of 1, DirectUpdateHandlerTest 7 of 7, module check rc=0), and `material/update-processing-round-3-closeout.md` (owner decision 1) prescribes citing that gate. The Proof bullet now cites it. Two parts of the finding are applied in substance rather than word for word: the "Later commits change wording only" line is not added, because the draft already carries it ("The two last commits change wording only. `5b822b7e8e9` changes the changelog title, and `db357868610b` changes one Javadoc comment in `DirectUpdateHandler2.java`."); and the Limits change the finding requires is made (the clause "No run of this class is counted in the proof above" is dropped from the CommitWaitTest bullet, because the Proof now counts that class). One record tension is noted for the record: `material/review-opened-28-answers.md` item (3) says the SOLR-7022 Proof line about DirectUpdateHandler2CommitWaitTest is dropped; the later round 3 closeout governs the draft and prescribes citing it, so the closeout wins.
- **UPC-R1-214 (SOLR-5941, reference guide check).** Applied as the "not run" bullet. No receipt, ledger entry or log records `:solr:solr-ref-guide:check` running for this branch (the receipts record `:solr:core:check` only), so the alternative form (record that it ran) has no evidence and the bullet stands.
- **UPC-R1-02 (SOLR-11475).** Verified against receipts/SOLR-11475.md: the gate ran at `0de48e492fd4` on 2026-10-08 and head `229947201fd7` has the same tree, so the replacement is applied as specified.
- **UPC-R1-24 (SOLR-13943, blocking).** Verified against receipts/SOLR-13943.md: the counts were gated at `cc155cf68e1d` on 2026-10-09; applied as specified.
- **UPC-R1-23 (SOLR-13696, note).** Confirmed on the record: the r8 GREEN gate at `da4fa6df1178` (2026-10-09) governs; the assignment's r6 status predates it. No draft defect, no draft change.

## Per-draft dispositions

Applied means the report's replacement text is in the draft. Changelog-link findings (the bare code span replaced by a link at the head SHA) are listed as "changelog link" without repeating the URL.

### SOLR-11475
- UPC-R1-01 applied. UPC-R1-02 applied (verified above). UPC-R1-03 applied (changelog link).

### SOLR-11483
- UPC-R1-04 applied. UPC-R1-05 applied (changelog link).
- UPC-R1-06 (note): noted as read. The two open low audit items stay for the audit record; no draft change.

### SOLR-12245
- UPC-R1-07 applied (multi-error Limits bullet added). UPC-R1-08 applied (changelog link).

### SOLR-12703
- UPC-R1-09 applied. UPC-R1-10 applied (changelog link).
- UPC-R1-11 (note): applied; the suggested plain form replaces "should be sequenced, not merged cold".

### SOLR-12705
- UPC-R1-12 applied. UPC-R1-13 applied (two-processors bullet added). UPC-R1-14 applied (pre-fix head bullet added). UPC-R1-15 applied (SOLR-5065 Limits bullet added). UPC-R1-16 applied (blocking: one hunk, not two). UPC-R1-17 applied (changelog link).

### SOLR-12864
- UPC-R1-18 applied.

### SOLR-13265
- UPC-R1-19 applied. UPC-R1-20 applied (fail-before sentences added after the Proof bullet). UPC-R1-22 applied (changelog link).
- UPC-R1-21 (note): noted as read, not applied. The suggested merge-base form does not check out: at the merge-base `9b3a84b1c460` the file `DistributedUpdateProcessor.java` has 1380 lines, so the head line numbers the draft cites have no counterpart there to link to. Left for round 2 to re-check against the branch's own tree.

### SOLR-13696
- UPC-R1-23 (note): confirmed on the record (see above). No draft change.

### SOLR-13943
- UPC-R1-24 applied (blocking; verified above). UPC-R1-25 applied. UPC-R1-26 applied. UPC-R1-27 applied (changelog link).
- UPC-R1-28 (note): noted as read; the optional addition is not made.
- UPC-R1-32 (note, shared with SOLR-12705): noted as read. The length acceptances stay a main-side confirmation with the owner; no draft change in this round.

### SOLR-14262
- UPC-R1-29 applied. UPC-R1-30 applied (changelog link).
- UPC-R1-31 (note): noted as read; the low audit item is not a readiness item.

### SOLR-14718
- UPC-R1-101 applied (both symptom links moved to the merge-base `9b3a84b1c460`, with the merge-base parenthetical at the end of the paragraph). UPC-R1-102 applied (blocking). UPC-R1-103 applied (reconciled above). UPC-R1-104 applied (decision form, above). UPC-R1-105 applied (settling-check bullet added). UPC-R1-106 applied (wrong-document Limits bullet added). UPC-R1-107 applied (changelog link).

### SOLR-16356
- UPC-R1-108 applied (blocking: catch-block link moved to the merge-base `14c7aac0d151`, lines 949 to 951, with the merge-base parenthetical). UPC-R1-109 applied (first link's SHA moved to the merge-base, "(at the merge-base)" added). UPC-R1-110 applied (changelog link).
- UPC-R1-111 (note): noted as read; the report gives no individual text for this note, so there is nothing to apply.

### SOLR-16655
- UPC-R1-112 applied (line 16 replaced; the repeated landing-order sentence deleted from the Limits bullet). UPC-R1-115 applied (changelog link).
- UPC-R1-113, UPC-R1-114, UPC-R1-116 (notes): noted as read; the report gives no individual text for these notes, so there is nothing to apply.

### SOLR-16673
- UPC-R1-117 applied. UPC-R1-118 applied (changelog link).

### SOLR-16910
- UPC-R1-119 applied (changelog link).

### SOLR-3657
- UPC-R1-120 applied. UPC-R1-121 applied (changelog link).
- UPC-R1-122 (note): noted as read; the optional test-comment cleanup stays with the branch.

### SOLR-4841
- UPC-R1-123 applied (the three pre-change links moved to the merge-base `b6b2b8f10e98`, "(at the merge-base)" added; the DetectedLanguage link already used the merge-base and is unchanged). UPC-R1-124 applied. UPC-R1-125 applied (changelog link).
- UPC-R1-126 (note): noted as read; clarity only, and the body already covers the new head.

### SOLR-5065
- UPC-R1-127 applied. UPC-R1-128 applied (blocking). UPC-R1-129 applied (combined-change Limits bullet added). UPC-R1-130 applied (changelog link).
- UPC-R1-131 (note): noted as read; the shorthand stays, with the claim scoped as the draft already states it.

### SOLR-5505
- UPC-R1-132 applied (both links moved to the merge-base `cabedd1d968`, the old log line cited at `LoggingInfoStream.java#L32`, "(at the merge-base)" added). UPC-R1-133 applied (changelog link).
- UPC-R1-134 (note): noted as read; the fragment header check belongs to the opening checklist for this PR, not to the draft.

### SOLR-5754
- UPC-R1-201 applied (blocking: the headline now states the hypothetical as a hypothetical). UPC-R1-202 applied (Proof headline states why a run without the fix is inconclusive). UPC-R1-203 applied (the tested-head bullet replaces the process narration). UPC-R1-204 applied (changelog link).
- UPC-R1-239 (note): noted as read; not a finding.

### SOLR-5887
- UPC-R1-205 applied (both symptom links moved to the merge-base `b6b2b8f10e98`). UPC-R1-206 applied (the base leg is now stated as the revert setup the receipt records). UPC-R1-207 applied (changelog link).

### SOLR-5939
- UPC-R1-208 applied (changelog link).
- UPC-R1-209 (note): noted as read. The correction belongs to the audit record; the draft's Limits already state that the proof run used the merged tree.
- UPC-R1-210 (note): noted as read. The base run's log is not in the workspace, so the conditional wording change is not made on a guess.

### SOLR-5941
- UPC-R1-211 applied (blocking: the pre-change forwarding sentence). UPC-R1-212 applied (the pre-fix head basis is stated). UPC-R1-213 applied (the five un-rerun classes are named). UPC-R1-214 applied (see above). UPC-R1-215 applied (changelog link).
- UPC-R1-216 (note): applied as a plain correction. The `IgnoreCommitOptimizeUpdateProcessorFactory.java` link now uses the merge-base `cabedd1d968`; verified at that SHA, line 125 is the `getBool` call the draft cites.
- UPC-R1-217 (note): noted as read; the SOLR-13696 overlap line is added when that branch resumes.

### SOLR-6045
- UPC-R1-218 applied (factory and array-parser links moved to the merge-base `97d973814336`). UPC-R1-219 applied (changelog link). UPC-R1-220 applied (SOLR-7504 overlap bullet added).
- UPC-R1-221 (note): noted as read. Whether the factory test is gated before opening stays a main-side call; the draft already discloses the gap.
- UPC-R1-222 (note): applied; the suggested wording "The changelog text does not mention it." replaces the reviewer-note phrasing.

### SOLR-6065
- UPC-R1-223 applied (generic-mapping link moved to the merge-base `97d973814336`, lines 418 to 429). UPC-R1-224 applied (the internal decision citation is removed). UPC-R1-225 applied (the Choice now closes with the pointed question). UPC-R1-226 applied (the JVM-wide setter effect is stated). UPC-R1-228 applied (changelog link).
- UPC-R1-227 (note): (a) noted as read; the audit re-check record belongs to the audit file. (b) Noted as read; naming the supporting report in the receipt is a record matter. (c) Noted as read; cross-reference only. (d) Applied as a plain correction: the `addIndexes` Limits link now uses the merge-base `97d973814336` at line 765; verified at that SHA, line 765 is the `addIndexes` call in the merge path.

### SOLR-6973
- UPC-R1-229 applied (both links moved to the merge-base `cabedd1d968`, with the corrected line range for the signature factory). UPC-R1-230 applied (changelog link).
- UPC-R1-231 (note): noted as read; the premise question is the same one UPC-R1-206 settled by stating the setup, and round 2 can re-check this draft's wording the same way.

### SOLR-7022
- UPC-R1-232 applied in substance (see the reconciliation above: the Proof cites the gate at `6a233ab2fdb` with its counts; the wording-only line was already in the draft; the Limits clause is dropped). UPC-R1-233 applied (the merge-base compile point and the commit-level fail-before are appended to the Proof bullet). UPC-R1-234 applied (changelog link).
- UPC-R1-235 (note): noted as read; nothing to add.

### SOLR-7504
- UPC-R1-236 applied (changelog link).
- UPC-R1-237 (note): noted as read; not a finding.
- UPC-R1-238 (note): noted as read. Owner acceptance of the null and collection semantics stays a main-side confirmation before this PR opens.

## What round 2 needs to settle

Round 2 re-checks the changed drafts first (per the round 1 report's own plan: SOLR-5754, 5939, 5941, 6045, 6065 and 7504 changed after round 3; SOLR-12705 and 13943 changed on 2026-10-10), then the drafts this application changed, and gives a verdict of CONVERGED or NOT CONVERGED per draft. The assignment is `assignments/pool-update-processing-convergence-round-2.md`.
