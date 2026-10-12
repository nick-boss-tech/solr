# Claim: SOLR-16267 def() behavior check against Lucene 9.12.3 and 10.4.0 (vm1)

- Claimed: 2026-10-12T14:23Z by vm1 (main agent). Job: gates/SOLR-16267-def-check.md.
- Slice: the whole job (record only: read DefFunction in Lucene 9.12.3 and 10.4.0, verdict into receipts/SOLR-16267.md).
- Capability tags: investigation, record. Staffing: 1.
- Heartbeat: 2026-10-12T14:23Z vm1 claimed, starting the artifact reads.
- Heartbeat: 2026-10-12T14:25Z vm1 finished the reads; verdict written.
- Status: DONE by vm1, 2026-10-12. Deliverable: receipts/SOLR-16267.md (def() behavior check section, 2026-10-12). Verdict: DefFunction's value path consults the wrapped source's exists() in Lucene 9.12.3 and in 10.4.0 alike; the draft's def() claim stands for both versions and needs no narrowing for 9.x. Job file gates/SOLR-16267-def-check.md marked DONE.
