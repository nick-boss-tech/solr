# Review round 2, group 3: SOLR-6045, SOLR-7504, SOLR-12703, SOLR-12705

Read-only check at `origin/pr-prepare` `0870cb8f092`. The assignment, claim, round 1 roll-up and group 3 report, the corrections report, the answers file, the SOLR-7504 null-fix report, `pr-formula.md`, the four final drafts, and the four receipts were read with `git show`. Branch files were read at each group head with `git show`. Live PR fields were read with `gh.ps1 pr view --json "body,title,headRefOid,isDraft"` and compared byte for byte with the final drafts. No draft, receipt, branch, or PR was changed. No builds, Gradle, or tests were run.

Checks common to all four drafts: no em dash, no internal vocabulary (gate, receipt, ledger, JUnit XML, rc=, takeover, live tip, dispositioned, planted, negative control), no first-person plural. Part 2: the six dispositions name none of these tickets, so each item is not applicable here.

## Summary

| PR | Ticket | Verdict | Live head = receipt head | Live body = final draft |
|---|---|---|---|---|
| 5077 | SOLR-6045 | NOT READY: branch changelog still unscoped | Yes, `bcae04d77bdb` | Byte-equal (7,169 bytes) |
| 5081 | SOLR-7504 | READY TO FLIP (the flip is the owner's call) | Yes, `2fe06bfd917f` | Byte-equal (5,120 bytes) |
| 5085 | SOLR-12703 | NOT READY: branch changelog gives an unsupported symptom | Yes, `63c84919c80b` | Byte-equal (3,167 bytes) |
| 5096 | SOLR-12705 | NOT READY on one line: follow-up sentence conflicts with the ratification | Yes, `aa56b7b1be4c` | Byte-equal (6,609 bytes) |

## SOLR-6045 (PR 5077)

**Verdict: NOT READY. One remaining item: the changelog title on the branch still states the mixed-field rule without the merger-path scope.**

Part 1
- O4, summary scoped to the merger path: applied as specified.
- O4, Limits names the factory path with a planned follow-up sentence: applied as specified.
- O4, changelog scoped to the merger path: missing. `changelog/unreleased/SOLR-6045-atomic-update-repeated-field.yml` at `bcae04d77bdb` still reads "A field that mixes a plain value with operation maps is rejected ... whichever value comes first" with no path. Round 1 listed the changelog, and the corrections did not change the branch.
- Round 1 A, two factory tests missing from Proof: applied differently. The tests are named in a Limits bullet ("Factory tests") instead of a Proof line. The content matches.
- Round 1 B, follow-up for JSON-array and XML-writer tests: applied as ratified, as the last Limits bullet.
- Round 1 C, title: stands under the owner's O1 ruling. Not changed.
- Round 1 non-blocking test comment (`AtomicUpdatesTest.java` L1373-L1374): not a draft item and not in the corrections. Optional on the branch.
- Bold one-line summary under Limits: present.

Part 2: not applicable.

Body: byte-equal to the final draft. Live head `bcae04d77bdbb33c33e077c3945a5deca0ce7dc1` matches the receipt. Live title: "SOLR-6045: atomic updates w/ solrj + BinaryRequestWriter aren't working when adding multiple fields w/ same name in a single SolrInputDocument" (142 characters). `isDraft` true.

Proposed replacement (branch changelog title block; the head moves, so re-gate, and owner ratification):
- Replace "A field that mixes a plain value with operation maps is rejected" with "On the merger path, a field that mixes a plain value with operation maps is rejected".

## SOLR-7504 (PR 5081)

**Verdict: READY TO FLIP. The flip itself needs the owner's go-ahead.**

Part 1
- Round 1 A, null-count NullPointerException: applied, on the branch. At `2fe06bfd917f` the scan reads `values` once and guards null (`CountFieldValuesUpdateProcessorFactory.java` L78-L85). The receipt records 30 of 30, 42 of 42, and a pre-fix result of one failure, `testCountValuesNullValue`, at `22b77196e662`.
- Round 1 B, "operation map with no operation": applied differently. The draft says a field with no `set` in any operation map is rejected. That matches the code (L127-L133). Round 1's wording, "no operation at all", would be wrong for a map with only `add`.
- Round 1 C, delete "one behavior change": applied as specified.
- Round 1 D, cross-PR conflict: applied. The draft names one import block in `FieldMutatingUpdateProcessorTest.java` and says SOLR-12705 merges it by hand.
- Round 1 E, pre-fix head `e3fdc8eb58f2` and the observed failure: superseded by the null-fix round. The Proof cites `22b77196e662` and the null test only, which is the only failure the receipt records there. The lead should confirm this is intended.
- Round 1 F, follow-up: applied as ratified, as the last Limits bullet.
- Round 1 G, title: stands under O1.
- Null-fix head references: applied. Cited lines were checked at `2fe06bfd917f` (L78-L85, L91-L136, L127-L133, L138-L148, test L787-L794). The base link L70-L74 was checked at `97d973814336`.
- Combined run with SOLR-12705: stated as before the null fix, at `22b77196e662`. Applied.

Optional, not required for the verdict
- After "only this test fails: 1 failure in 30." add: "The base code also stores 0 for a null value, so this test passes there." This rests on round 1's reading of base, not a re-run.
- The 7504 receipt's History line says the `22b77196e662` gate ran "25 and 43 tests". Round 1 read 29 and 42 for that head. This is a receipt note, not a draft change, and the lead should reconcile it.

Body: byte-equal to the final draft. Live head `2fe06bfd917f193b3591dfa4c088ef0994279ed6` matches the receipt. Live title: "SOLR-7504: Atomic Update causes solr.CountFieldValuesUpdateProcessorFactory to be wrong" (87 characters). `isDraft` true.

## SOLR-12703 (PR 5085)

**Verdict: NOT READY. One remaining item: the branch changelog still gives a RunUpdateProcessor symptom that round 1 found unsupported.**

Part 1
- Round 1 A, bold summary under Limits: applied as specified.
- Round 1 B, stale SOLR-6045 link: applied as specified (`bcae04d77bdb`, L202).
- Round 1 C, verification date: applied as specified.
- Round 1 D, changelog RunUpdateProcessor wording: missing. The changelog at `63c84919c80b` still says "instead of surfacing as a confusing RunUpdateProcessor failure". No owner ruling is recorded.
- Round 1 E, follow-up sentence: applied as ratified.
- Round 1 F, title: stands under O1.
- Cross-PR sequencing bullet: holds, and it agrees with the SOLR-6045 draft.

Part 2: not applicable.

Body: byte-equal to the final draft. Live head `63c84919c80b15c20d5b93bbacaf3f21835648e5` matches the receipt. Live title: "SOLR-12703: Better validation of bad atomic updates". `isDraft` true.

Proposed replacement (branch changelog; the head moves, so re-gate, and then the "Verified 2026-10-09 at this head" line needs the new head):
- Replace "instead of surfacing as a confusing RunUpdateProcessor failure." with "instead of being stored as a field value."

## SOLR-12705 (PR 5096)

**Verdict: NOT READY on one item. The planned follow-up line in Limits conflicts with the owner's ratification. Once that line is resolved, the draft is READY TO FLIP.**

Part 1
- Round 1 A, no fail-before result: applied, with different wording. The Proof says no run without the fix is on record, and that the opt-out test is inconclusive by construction. Same content.
- Round 1 B, second conflict with SOLR-7504: applied as specified, with "once" added.
- Round 1 C, SOLR-16655 description: applied differently. The draft adds "and the imports". The 16655 diff at `5e2317443f41` adds four import lines, so the addition is supported.
- Round 1 D, follow-up: applied, but it conflicts with ratification (2). Ratification (2) keeps SOLR-12705 among the five gaps that "stay as stated limitations", and the assignment says it "deliberately carr[ies] none". The corrections report lists 12705 in both places. The owner must confirm. Recommendation: delete the line below.
- Round 1 E, default scope (O5): applied as ruled. The default and the per-class opt-out are stated, and the overload is at L358-L374.
- Round 1 F, title: stands under O1.
- Combined run and head statements: applied. The draft says the run predates the null fix and does not cover `2fe06bfd917f`.
- Landing order (SOLR-7504 and SOLR-16655 before this change): applied.

Part 2: not applicable.

Body: byte-equal to the final draft. Live head `aa56b7b1be4c5491723c58f296e20c906d7b7334` matches the receipt. Live title: "SOLR-12705: ParseDateFieldUpdateProcessorFactory does not work for atomic update values". `isDraft` true.

Proposed change, for the owner to confirm: delete this Limits line.
- `- A follow-up submission is planned to add the add-distinct, remove and dropped-operand cases.`

## Cross-cutting

The same conflict applies to any other draft named in ratification (2) that carries a follow-up sentence: SOLR-5065, SOLR-14718, SOLR-14262 and SOLR-7022. Groups 1, 4 and 6 should check. The corrections report's list of 22 drafts with a sentence includes all five names, so the owner's record is internally inconsistent on this point.
