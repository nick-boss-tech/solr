# Pool assignment: skip verdict cross-audit, batch 8 of 21

Status: OPEN

- Capability tags: review, draft.
- Intended host: any pool Linux host. Claim under `claims/` before starting (WORKFLOW.md).
- Posted by: main agent (vm1), 2026-10-19, at the direction of Nick.

Audit each recorded closeout verdict in this batch against the floor in `material/skip-audit/verdict-rubric.md`, the same checks the vm1 honesty watch runs. A verdict passes only if all of these hold: a claim in `claims/` naming the ticket carries a DONE line; the named draft branch exists on the fork and holds `drafts/<KEY>/record.md` and `drafts/<KEY>/jira-comment.md`; the record cites the pinned head it re-checked at and that head matches `evidence_head` in `material/skip-audit/queue-current.json`; the record names the Jira status read in that pass; the record gives a code path with file and line or a reproduction attempt and its result; the record is substantive (about 40 words or more, and not identical to another ticket's record); the verdict is one the rubric allows and the draft branch suffix matches it.

Cross-audit rule: never audit a verdict whose closeout claim or record names your own host as the worker. Mark it `deferred-own-host` in the report and leave it; another host or the vm1 watch takes it. When in doubt, the verdict is suspect and the report names what is missing; a thin or unverifiable pass is worse than a suspect.

## Items

1. **SOLR-9062**, recorded verdict `keep-open` on `solr-9062-draft-open`, evidence head `498a09aea97a`. Output: one row in the batch report.
2. **SOLR-9061**, recorded verdict `keep-open` on `solr-9061-draft-open`, evidence head `498a09aea97a`. Output: one row in the batch report.
3. **SOLR-9042**, recorded verdict `keep-open` on `solr-9042-draft-open`, evidence head `498a09aea97a`. Output: one row in the batch report.
4. **SOLR-8991**, recorded verdict `close-fixed` on `solr-8991-draft-close`, evidence head `498a09aea97a`. Output: one row in the batch report.
5. **SOLR-8966**, recorded verdict `keep-open` on `solr-8966-draft-open`, evidence head `498a09aea97a`. Output: one row in the batch report.
6. **SOLR-8941**, recorded verdict `keep-open` on `solr-8941-draft-open`, evidence head `498a09aea97a`. Output: one row in the batch report.
7. **SOLR-8928**, recorded verdict `keep-open` on `solr-8928-draft-open`, evidence head `498a09aea97a`. Output: one row in the batch report.
8. **SOLR-8909**, recorded verdict `keep-open` on `solr-8909-draft-open`, evidence head `498a09aea97a`. Output: one row in the batch report.
9. **SOLR-8899**, recorded verdict `keep-open` on `solr-8899-draft-open`, evidence head `498a09aea97a`. Output: one row in the batch report.
10. **SOLR-8893**, recorded verdict `keep-open` on `solr-8893-draft-open`, evidence head `498a09aea97a`. Output: one row in the batch report.
11. **SOLR-8879**, recorded verdict `keep-open` on `solr-8879-draft-open`, evidence head `498a09aea97a`. Output: one row in the batch report.
12. **SOLR-8862**, recorded verdict `keep-open` on `solr-8862-draft-open`, evidence head `498a09aea97a`. Output: one row in the batch report.
13. **SOLR-8843**, recorded verdict `close-obsolete` on `solr-8843-draft-close`, evidence head `498a09aea97a`. Output: one row in the batch report.
14. **SOLR-8841**, recorded verdict `keep-open` on `solr-8841-draft-open`, evidence head `498a09aea97a`. Output: one row in the batch report.
15. **SOLR-9202**, recorded verdict `keep-open` on `solr-9202-draft-open`, evidence head `498a09aea97a`. Output: one row in the batch report.
16. **SOLR-8806**, recorded verdict `keep-open` on `solr-8806-draft-open`, evidence head `498a09aea97a`. Output: one row in the batch report.
17. **SOLR-8770**, recorded verdict `keep-open` on `solr-8770-draft-open`, evidence head `498a09aea97a`. Output: one row in the batch report.
18. **SOLR-8768**, recorded verdict `keep-open` on `solr-8768-draft-open`, evidence head `498a09aea97a`. Output: one row in the batch report.
19. **SOLR-8757**, recorded verdict `keep-open` on `solr-8757-draft-open`, evidence head `498a09aea97a`. Output: one row in the batch report.
20. **SOLR-8756**, recorded verdict `keep-open` on `solr-8756-draft-open`, evidence head `498a09aea97a`. Output: one row in the batch report.
21. **SOLR-8741**, recorded verdict `keep-open` on `solr-8741-draft-open`, evidence head `498a09aea97a`. Output: one row in the batch report.
22. **SOLR-8733**, recorded verdict `keep-open` on `solr-8733-draft-open`, evidence head `498a09aea97a`. Output: one row in the batch report.
23. **SOLR-8732**, recorded verdict `keep-open` on `solr-8732-draft-open`, evidence head `498a09aea97a`. Output: one row in the batch report.
24. **SOLR-8729**, recorded verdict `keep-open` on `solr-8729-draft-open`, evidence head `498a09aea97a`. Output: one row in the batch report.
25. **SOLR-8709**, recorded verdict `keep-open` on `solr-8709-draft-open`, evidence head `498a09aea97a`. Output: one row in the batch report.

## Decisions

| Decision | Answer or default | Condition and action | Owner |
| --- | --- | --- | --- |
| Standard | decided: the checks in this assignment, applied ticket by ticket | no sampling, no bulk pass | Nick |
| Own-host items | decided: deferred-own-host, never a pass from the host that did the closeout | the vm1 watch or another host audits it later | Nick |
| Acceptance | decided: the vm1 watch spot-checks at least 5 tickets per batch and imports the results; suspects enter `material/skip-audit/remediation-current.json` through the watch | auditors do not edit queue-current.json or remediation-current.json | Nick |

## Deliverable

`reports/skip-verdict-audit-batch-08.md`: a table with one row per item: ticket, result (pass, suspect, or deferred-own-host), the claim file found or none, the draft branch head, and for suspects exactly what is missing. End with a one-line count of pass, suspect, and deferred.

## Limits

- Audit only: do not edit the audited tickets' claims, drafts, queue records, or verdicts. No PR, comment, JIRA post or email. No force push.
- Commit as Nick Shanin with no trailers. No em dashes.
