# Skip record: SOLR-13837

- Ticket: SOLR-13837, AuditLogger must handle POST and V2 requests better. JIRA read 2026-10-19 (this pass, evidence tool): status Open, resolution None, created 2019-10-11, updated 2021-06-21, linked as required by SOLR-13741 (Closed); no comments.
- Verdict: keep-open.
- Draft branch: solr-13837-draft-open (the existing keep-open branch; this record replaces its draft files).
- Pinned head: fork origin/main 498a09aea97aac86c2cc7ac114113b946d249942. Code read and grep run at this head (detached worktree).
- Source: remediation redo of batch 05, item 18, under assignments/pool-aaa-skip-verdict-remediation-05.md. The earlier record (round audit-1, 2026-10-04, confirmed-skip) cited the V2 path lines from audit evidence; they are confirmed here.

## Ticket reading

The audit logger does not log the body of V2 admin requests (where the action is), does not record which collections a V2 request targets, and reports V2 resource paths in an internal form. The ticket lists these as the gaps.

## Code reading and grep at the pinned head

- solr/core/src/java/org/apache/solr/security/AuditEvent.java:546: `return resourcePath.replaceFirst("^/____v2", "/api");`. The internal V2 prefix is converted to `/api`. This part of the ticket is in code.
- AuditEvent.java:552-555: regular expressions for V2 collection paths (`^/api/(c|collections)$`, `^/api/(c|collections)/[^/]+/config$`, `^/api/(c|collections)/[^/]+/schema$`, `^/api/(c|collections)/[^/]+/shards.*`). The collection list for V2 requests is derived from those paths. This part is also in code.
- `git show 498a09aea97aac86c2cc7ac114113b946d249942:solr/core/src/java/org/apache/solr/security/AuditEvent.java | grep -i -c body` gives 0. The audit event has no request body field, so the body logging the ticket asks for is not implemented.
- No commit on the pin names SOLR-13837.

## History

- The path conversion and collection regexes were not traced to their commits in this pass; the record relies on the code at the pin.

## Reproduction attempts

Not run. A run would send a V2 POST with a body to a node with an audit logger and inspect the audit event; this pass did not start a node.

## Gap analysis

Two of the three gaps are addressed in code (V2 path conversion and collection mapping). The remaining gap, logging the request body, is not in the audit event. Logging a body can capture credentials or other sensitive data, so what to record is a design decision, and the ticket does not settle it.

## Verdict justification

keep-open: the attempts made are the code reads and the grep above at the pinned head, the Jira read, and the body check. The missing piece is the design decision on which body content to log and how to avoid recording secrets, and then the implementation and a run that shows the body in the audit event. close-fixed is not supported: one of the three asks is open. pr-needed is not supported without that decision.

Missing for a closing or PR verdict: the body-logging decision and the run output.

## Limits

This record does not run an audit event. It does not post anything to JIRA or GitHub. The draft comment is a draft until someone posts it by hand.
