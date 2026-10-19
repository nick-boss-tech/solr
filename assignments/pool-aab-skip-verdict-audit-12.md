# Pool assignment: skip verdict cross-audit, batch 12 of 21

Status: OPEN

- Capability tags: review, draft.
- Intended host: any pool Linux host. Claim under `claims/` before starting (WORKFLOW.md).
- Posted by: main agent (vm1), 2026-10-19, at the direction of Nick.

Audit each recorded closeout verdict in this batch against the floor in `material/skip-audit/verdict-rubric.md`, the same checks the vm1 honesty watch runs. A verdict passes only if all of these hold: a claim in `claims/` naming the ticket carries a DONE line; the named draft branch exists on the fork and holds `drafts/<KEY>/record.md` and `drafts/<KEY>/jira-comment.md`; the record cites the pinned head it re-checked at and that head matches `evidence_head` in `material/skip-audit/queue-current.json`; the record names the Jira status read in that pass; the record gives a code path with file and line or a reproduction attempt and its result; the record is substantive (about 40 words or more, and not identical to another ticket's record); the verdict is one the rubric allows and the draft branch suffix matches it.

Cross-audit rule: never audit a verdict whose closeout claim or record names your own host as the worker. Mark it `deferred-own-host` in the report and leave it; another host or the vm1 watch takes it. When in doubt, the verdict is suspect and the report names what is missing; a thin or unverifiable pass is worse than a suspect.

## Items

1. **SOLR-7257**, recorded verdict `keep-open` on `solr-7257-draft-open`, evidence head `498a09aea97a`. Output: one row in the batch report.
2. **SOLR-7249**, recorded verdict `keep-open` on `solr-7249-draft-open`, evidence head `498a09aea97a`. Output: one row in the batch report.
3. **SOLR-7247**, recorded verdict `keep-open` on `solr-7247-draft-open`, evidence head `498a09aea97a`. Output: one row in the batch report.
4. **SOLR-7235**, recorded verdict `keep-open` on `solr-7235-draft-open`, evidence head `498a09aea97a`. Output: one row in the batch report.
5. **SOLR-7221**, recorded verdict `close-obsolete` on `solr-7221-draft-close`, evidence head `498a09aea97a`. Output: one row in the batch report.
6. **SOLR-7198**, recorded verdict `keep-open` on `solr-7198-draft-open`, evidence head `498a09aea97a`. Output: one row in the batch report.
7. **SOLR-7192**, recorded verdict `keep-open` on `solr-7192-draft-open`, evidence head `498a09aea97a`. Output: one row in the batch report.
8. **SOLR-7153**, recorded verdict `close-fixed` on `solr-7153-draft-close`, evidence head `498a09aea97a`. Output: one row in the batch report.
9. **SOLR-7149**, recorded verdict `keep-open` on `solr-7149-draft-open`, evidence head `498a09aea97a`. Output: one row in the batch report.
10. **SOLR-7088**, recorded verdict `close-fixed` on `solr-7088-draft-close`, evidence head `498a09aea97a`. Output: one row in the batch report.
11. **SOLR-7075**, recorded verdict `keep-open` on `solr-7075-draft-open`, evidence head `498a09aea97a`. Output: one row in the batch report.
12. **SOLR-7071**, recorded verdict `keep-open` on `solr-7071-draft-open`, evidence head `498a09aea97a`. Output: one row in the batch report.
13. **SOLR-7070**, recorded verdict `keep-open` on `solr-7070-draft-open`, evidence head `498a09aea97a`. Output: one row in the batch report.
14. **SOLR-7069**, recorded verdict `keep-open` on `solr-7069-draft-open`, evidence head `498a09aea97a`. Output: one row in the batch report.
15. **SOLR-7035**, recorded verdict `keep-open` on `solr-7035-draft-open`, evidence head `498a09aea97a`. Output: one row in the batch report.
16. **SOLR-7009**, recorded verdict `close-fixed` on `solr-7009-draft-close`, evidence head `498a09aea97a`. Output: one row in the batch report.
17. **SOLR-7000**, recorded verdict `keep-open` on `solr-7000-draft-open`, evidence head `498a09aea97a`. Output: one row in the batch report.
18. **SOLR-6977**, recorded verdict `keep-open` on `solr-6977-draft-open`, evidence head `498a09aea97a`. Output: one row in the batch report.
19. **SOLR-6934**, recorded verdict `keep-open` on `solr-6934-draft-open`, evidence head `498a09aea97a`. Output: one row in the batch report.
20. **SOLR-6929**, recorded verdict `keep-open` on `solr-6929-draft-open`, evidence head `498a09aea97a`. Output: one row in the batch report.
21. **SOLR-6922**, recorded verdict `keep-open` on `solr-6922-draft-open`, evidence head `498a09aea97a`. Output: one row in the batch report.
22. **SOLR-6914**, recorded verdict `keep-open` on `solr-6914-draft-open`, evidence head `498a09aea97a`. Output: one row in the batch report.
23. **SOLR-6911**, recorded verdict `keep-open` on `solr-6911-draft-open`, evidence head `498a09aea97a`. Output: one row in the batch report.
24. **SOLR-6903**, recorded verdict `close-fixed` on `solr-6903-draft-close`, evidence head `498a09aea97a`. Output: one row in the batch report.
25. **SOLR-6890**, recorded verdict `close-obsolete` on `solr-6890-draft-close`, evidence head `498a09aea97a`. Output: one row in the batch report.

## Decisions

| Decision | Answer or default | Condition and action | Owner |
| --- | --- | --- | --- |
| Standard | decided: the checks in this assignment, applied ticket by ticket | no sampling, no bulk pass | Nick |
| Own-host items | decided: deferred-own-host, never a pass from the host that did the closeout | the vm1 watch or another host audits it later | Nick |
| Acceptance | decided: the vm1 watch spot-checks at least 5 tickets per batch and imports the results; suspects enter `material/skip-audit/remediation-current.json` through the watch | auditors do not edit queue-current.json or remediation-current.json | Nick |

## Deliverable

`reports/skip-verdict-audit-batch-12.md`: a table with one row per item: ticket, result (pass, suspect, or deferred-own-host), the claim file found or none, the draft branch head, and for suspects exactly what is missing. End with a one-line count of pass, suspect, and deferred.

## Limits

- Audit only: do not edit the audited tickets' claims, drafts, queue records, or verdicts. No PR, comment, JIRA post or email. No force push.
- Commit as Nick Shanin with no trailers. No em dashes.
