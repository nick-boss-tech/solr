# Claim: skip-SOLR-13471-vm5

- Claimant host: vm5, matching a file in `hosts/`.
- Status: DONE 2026-10-19T23:40:00Z. Remediation redo of batch 06, item 15, under `assignments/pool-aaa-skip-verdict-remediation-06.md`.
- Job or assignment: `assignments/pool-aaa-skip-verdict-remediation-06.md` (item 15, SOLR-13471).
- Slice: the whole ticket.
- Capability tags used: review, draft.
- Started: 2026-10-19T23:25:00Z (UTC).

Heartbeat: 2026-10-19T23:25:00Z

Result: 2026-10-19T23:40:00Z: keep-open on solr-13471-draft-open at 498a09aea97aac86c2cc7ac114113b946d249942 (head c7836dfc50, unchanged verdict; audit-derived record replaced). Jira read this pass: Open, no resolution, updated 2019-06-12. Code read: JavaBinResponseWriter.java:52-62 (marshal straight to the output stream); JavaBinCodec.java:1483-1491 (close, no second flush after failure), :283 END_OBJ (map sentinel, no error or restart marker); HttpSolrCall.java:513 (writeResponse), :518-520 (catch Throwable, sendError). Whether a partial body commits the response before sendError, and the client's read, not run.

DONE: 2026-10-19T23:40:00Z. Deliverables: drafts/SOLR-13471/record.md and jira-comment.md on solr-13471-draft-open (c7836dfc50); queue-current set; reports/skip-verdict-remediation-06.md line.
