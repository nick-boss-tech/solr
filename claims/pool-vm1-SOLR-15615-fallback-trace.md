# Claim: SOLR-15615 fallback path trace for the receipt (vm1)

- Claimed: 2026-10-12T14:23Z by vm1 (main agent). Job: gates/SOLR-15615-fallback-trace.md.
- Slice: the whole job (code investigation at head be77267bf554, trace and verdict into receipts/SOLR-15615.md).
- Capability tags: investigation, record. Staffing: 1.
- Heartbeat: 2026-10-12T14:23Z vm1 claimed, starting the trace.
- Heartbeat: 2026-10-12T14:31Z vm1 finished the trace; verdict written.
- Status: DONE by vm1, 2026-10-12. Deliverable: receipts/SOLR-15615.md (Fallback path trace, 2026-10-12). Verdict: the fallback's per-collection requests are node-to-node requests whose PKI-signed header asserts the original requester's identity, so the receiving collection's authorization check runs against the original user; the receipt quotes the exact Limits sentence the trace supports. Job file gates/SOLR-15615-fallback-trace.md marked DONE.
