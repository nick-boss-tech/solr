# Gate job: SOLR-16750 review pass 1 (grouping on EnumFieldType returns labels)

Status: HANDED BACK (pass 1, step 4: `EnumFieldTest.testGroupingOnEnumFieldReturnsLabels` fails at the head under a numeric-DocValues seed; the unit stops here)

- Branch: solr-16750-enum-group-label. Head: `783c1323f0735b07ee84435feed3595da34aa520` (ls-remote from the claim, unchanged through the run). Prior gated head: none on this unit.
- Intended host: vm4. Capability tags: gate.
- Module: `:solr:core`. Focused class: `org.apache.solr.schema.EnumFieldTest`.
- Diff scope against `c8ab71430f`: 4 files, 47 insertions, 1 deletion (`Grouping.java`, `EnumFieldType.java`, `EnumFieldTest.java`, changelog).

## Checks

- [x] head confirmed by ls-remote
- [x] diff scope confirmed
- [x] step 0 changelog YAML parse, rc=0
- [x] step 1 tidy, rc=0
- [x] step 2 Error Prone compile, rc=0 (15:24:19)
- [ ] step 3 proof leg: NOT VALID. At the gate seed `16750C0FFEE16750` the new test is skipped by its own `assumeFalse` on `solr.tests.numeric.dv` (the test framework picks that flag from the seed), so the base run (rc=0, 16 tests, 15 skipped) shows nothing about the new test. The base run is `/workspace/gates/logs/vm4/16750/pass1-step3-proofleg.log`.
- [ ] step 4 focused head run at the gate seed: rc=0, but the new test is SKIPPED (same assumption). Recorded as a skipped test per the gate rules. Log `/workspace/gates/logs/vm4/16750/pass1-step4-focused.log`, XML 16 tests, 15 skipped, 0 failures.
- [ ] step 4b (runner attempt outside the gate script, seed search): seed `16750C0FFEE16751` runs the new test, and it FAILS on the head: `java.lang.RuntimeException: mismatch: 'High'!='Not Available' @ grouped/severity/groups/[0]/groupValue` at `EnumFieldTest.java:726`. Log `/workspace/gates/logs/vm4/16750/seed-16750C0FFEE16751.log`: 16 tests, 1 failed, 1 skipped. The rule for a run that executed tests and failed is to record it and not relaunch it, so no further seeds were run (the seed search loop was stopped after this attempt).
- [ ] step 5 module check `:solr:core:check -x test`: rc=0 (15:39:11), but this does not change the result above.

## Hand-back

- Pass 1 is HANDED BACK. The head fix does not give the labels the test expects under the numeric DocValues path that the seed 16751 run exercises. Not diagnosed in this review (no repair attempted; the Gate repairs scope is for tidy, changelog, compile and lint, and up to two failing tests in test code, not a production change of this kind).
- Passes 2 and 3 do not run. The claim's HANDED BACK line names the pass and the step.

Deliverable: `reports/newdev-critblock-review-round-1/solr-16750-enum-group-label.md` (pass 1 section), `receipts/SOLR-16750.md` (pass 1 section, HANDED BACK).
