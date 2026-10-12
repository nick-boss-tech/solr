# Claim: SOLR-14199 prescribed fix and gate (pr-prepare)

- Claimed by: vm1 (main agent, first Linux VM)
- Capability tags: implementation, gate
- Trigger: the record job gates/SOLR-14199-record.md (DONE 2026-10-12) ran SchemaVersionSpecificBehaviorTest at the gated head e743c90da79a and recorded the failure reports/schema-analysis-round-1-s1.md finding 1 predicted: testVersionBehavior, "int field's type has wrong omitNorm for ver=1.0 expected:<false> but was:<true>". The s1 report prescribes the fix (an exact replacement for the test's assertion at lines 60-64, recording that point fields always omit norms under this change) plus a gate run of that class. This lane is that prescribed fix under the main side's separate approval.
- Scope: branch solr-14199-submit at e743c90da79a. One fix commit applying the s1 finding 1 replacement to SchemaVersionSpecificBehaviorTest.java, then the full hardening gate at the new head. On green: push the branch, refresh receipts/SOLR-14199.md, and update pr-drafts/schema-analysis/SOLR-14199.md (the two owed Limits lines as the receipt now records them, head citations and Proof counts refreshed, only the hold items this work discharges removed).
- Not in scope: any production change; any other test edit; the draft's standing owner items beyond the Limits lines the receipt records.
- Heartbeat: 2026-10-12T14:35Z (claim taken).
- Status: IN PROGRESS.
