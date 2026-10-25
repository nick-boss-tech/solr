# Assignment: warm-ticket evidence units, round 2 (each ending in a comment draft)

Status: OPEN
Tags: premise-run, draft, records

Four separately claimable units, one claim per unit, in the shape of round 1 (`assignments/warm-ticket-evidence-units-round-1.md`): evidence first, then a comment draft in `jira/` on this branch (Jira wiki markup, first person, footer disclosure) that says only what the evidence supports. If the evidence does not support a comment, the unit's report says so and no draft is written. Nothing posts; every text needs the owner's explicit go.

## Unit 1: SOLR-18553 plurality and consistency audit (Eric Pugh's ticket)

"Review params and path segments for plurality and consistency." Audit the v2 API params and path segments against the ticket's ask; list each inconsistency found with the API and file cited. Deliverables: `reports/solr-18553-plurality-audit-round-1.md` and, if the findings are concrete, `jira/jira-comment-SOLR-18553-draft.md` carrying the list and offering the cleanup.

## Unit 2: SOLR-18449 test system property audit (Eric Pugh's ticket)

"Ensure test oriented system properties start with test." Enumerate the system properties read by test code that do not start with `test.` (or the project's current prefix convention, read from the code), each cited to its read site. Deliverables: `reports/solr-18449-test-sysprops-audit-round-1.md` and, if the list is real, `jira/jira-comment-SOLR-18449-draft.md` carrying the count and the list, offering the rename.

## Unit 3: SOLR-18500 curl quoting audit (Eric Pugh's ticket)

"Get our arms around single, double, no quote on curl command parameters." Audit the Ref Guide's curl examples for the quoting inconsistency the ticket describes; count the forms in use with examples cited per form. Deliverables: `reports/solr-18500-curl-quoting-audit-round-1.md` and, if the split is real, `jira/jira-comment-SOLR-18500-draft.md` with the counts and a proposed single convention for Eric to pick or reject.

## Unit 4: SOLR-18439 v2 error status trace (Eric Pugh's ticket, he is assignee)

"Some v2 command APIs report failures via HTTP 200 instead of an error status." Trace two or three concrete v2 command APIs end to end and establish, with citations, which failures return 200 and what the error body carries. Deliverables: `reports/solr-18439-v2-error-status-trace-round-1.md` and, if the trace confirms the ticket, `jira/jira-comment-SOLR-18439-draft.md` carrying the traced cases and asking which behavior he wants.
