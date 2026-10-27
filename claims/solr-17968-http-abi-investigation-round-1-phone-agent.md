# Claim: SOLR-17968 HTTP binary compatibility investigation, round 1

- Host: phone-agent. Tags: review, records, draft, premise-run.
- Started: 2026-10-27 (America/Denver).
- Authority: Nick Shanin's direct direction in chat, 2026-10-27: "Claim the assignment and decide what is needed."
- Scope: the full investigation in assignments/solr-17968-http-abi-investigation-round-1.md, questions 1 to 5, run on this VM.
- Board snapshot: 04a2f33f094a057ad395a090b45ef1fcf3ebe4c6.
- Starting evidence: receipts/SOLR-18325-binary-attribution-round-1.md (released 10.0.0 consumer bytes pass on released 10.0.0, fail with identical NoSuchMethodError on the pre-Jackson base and the Jackson candidate, before any HTTP operation; the defect is inherited from the SOLR-17968 rename refactor, not from Jackson). SOLR-17968 itself is Resolved/Fixed for 10.1; its Jira comments discuss the abstraction, not the binary consequence.
- Deliverables: reports/solr-17968-http-abi-investigation-linux-round-1.md, receipts/SOLR-17968-http-abi-investigation-round-1.md, and jira/solr-17968-http-abi-followup-ticket-draft-round-1.md only if the evidence warrants a new ticket.
- Boundaries: investigation and probes only. No production change, no restoration or shim implementation, no public Jira or GitHub write. Any ticket filing returns to Nick for his go with the concrete draft.
