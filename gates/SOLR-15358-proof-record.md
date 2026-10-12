# Proof-record job: SOLR-15358

- Branch: solr-15358-submit. Head: cbb345f2e7df452e20036d357c82cc63db96ffd3 (live tip, verified by ls-remote at posting, 2026-10-11; matches receipts/SOLR-15358.md, GATE GREEN at this head, log g15358-harden.log on vm1). Merge-base: b5c71bc5573c4e31b4cee5a7965d73587fc0ae58.
- Intended host: any capable Linux host, under the WORKFLOW.md claim rules. Windows never claims build work.
- Status: OPEN, unclaimed.
- Capability tags: gate.
- Job type: record completion only. No branch edit, no new gate at the head; the head does not move.
- Why: receipts/SOLR-15358.md and the receipts ledger both record the gate's pre-fix proof only as "the gate's pre-fix proof step passed at this head", without naming which tests failed on the base run or how they failed. Every other receipt names its proof. This job re-runs the proof leg once and puts the names on record.
- Production delta on the branch: one file, `solr/core/src/java/org/apache/solr/schema/CurrencyFieldType.java` (commit f5fc8953e7, "honor docValues on CurrencyFieldType sub-fields"). The proof tree is the head with only that file reverted to the merge-base; the branch's tests stay.
- Run, in :solr:core, from fresh JUnit XML: org.apache.solr.schema.CurrencyFieldTypeDocValuesTest and org.apache.solr.schema.CurrencyFieldTypeTest. Seed: the seed named in g15358-harden.log if it records one; otherwise 15358C0FFEE15358. Head counts for comparison, from the receipt: CurrencyFieldTypeDocValuesTest 2 of 2; CurrencyFieldTypeTest 42 tests with 21 skipped (the suite's currency-locale assumption skips).
- Deliverable: receipts/SOLR-15358.md gains a proof section naming every test that fails on the proof tree (class, method, and the failure text, checked by content, not count alone), the seed, and the log path. If the proof tree does not compile, record that plainly as INCONCLUSIVE BY COMPILE instead (not expected: the production delta is two lines). Mark this job DONE and the claim DONE in the same push.

## Step checklist

- [ ] proof tree built: head with CurrencyFieldType.java reverted to b5c71bc557
- [ ] both classes run on the proof tree; outcomes read from fresh JUnit XML
- [ ] failing tests named (class, method, failure text) in receipts/SOLR-15358.md
- [ ] job and claim DONE
