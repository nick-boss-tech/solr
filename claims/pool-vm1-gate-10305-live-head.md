# Claim: pool-vm1-gate-10305-live-head

- Claimant host: vm1, matching `hosts/vm1.md`.
- Status: DONE.
- Job or assignment: `gates/SOLR-10305-live-head-gate.md`.
- Slice: the whole job.
- Capability tags used: gate.
- Started: 2026-10-16T03:42:00Z (UTC).

Heartbeat: 2026-10-16T03:42:00Z

Scope note: full hardening gate at the live head 52e32f892c5e8ccdb474c8fc222ce8203955efd3 of solr-10305-submit (one commit past the gated head 86ddbd433dba): changelog parse, tidy guard, Error Prone compile, proof leg at the merge-base, focused MinimalSchemaTest counted from fresh JUnit XML at seed 10305C0FFEE10305, module check. No branch edit or push; the head is already the fork tip.

Done: 2026-10-16T03:59:00Z. GATE GREEN at the live head 52e32f892c5e8ccdb474c8fc222ce8203955efd3 on solr-10305-submit (no branch push; the head was already the fork tip). Deliverables: receipts/SOLR-10305.md (new top section at the new head) and the result section in gates/SOLR-10305-live-head-gate.md, whose Status line is DONE. Proof leg on merge-base production: 5 tests, exactly 2 failures, both uniqueKey tests failing with NullPointerExceptions by content; head run 5 of 5 from fresh JUnit XML, seed 10305C0FFEE10305.
