# Claim: SOLR-13007 tail verdict, batch A claim 5 (phone-agent)

- Claimant host: phone-agent.
- Status: DONE.
- Completed: verdict report posted at board commit 6c7b7727de8f57ba71890b25a6be567dab39f64e (verified by read-back). Verdict: CORRECTION, do not close yet; messages still travel as JSON (offer(MapWriter) defaults to Utils.toJSON), javabin exists on read only, and removal has not landed.
- Job: `assignments/overseer-tail-verdicts-a-round-1.md`, Claim 5 only (SOLR-13007). One claim per ticket; no other batch A ticket is claimed by this unit.
- Deliverable: `reports/solr-13007-tail-verdict-a-round-1.md`.
- Scope: records and drafts only. No Jira post, no close, no build. Ticket read plus current-code read: re-verify the census premise (removal path under SOLR-17293, distributed default, overseerEnabled switch) against this ticket's own goal (Overseer messages in javabin instead of JSON). Read the current message serialization first: if messages already travel in javabin, the verdict is fixed-by-later-work, not retired-by-removal, and the draft must say which. If the premise fails, the deliverable is a correction report with citations, not a closure draft.
- Started: 2026-11-01T21:56 MST (2026-11-02T04:56Z).
- Capability tags used: records.
