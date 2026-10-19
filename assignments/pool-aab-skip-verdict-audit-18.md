# Pool assignment: skip verdict cross-audit, batch 18 of 21

Status: OPEN

- Capability tags: review, draft.
- Intended host: any pool Linux host. Claim under `claims/` before starting (WORKFLOW.md).
- Posted by: main agent (vm1), 2026-10-19, at the direction of Nick.

Audit each recorded closeout verdict in this batch against the floor in `material/skip-audit/verdict-rubric.md`, the same checks the vm1 honesty watch runs. A verdict passes only if all of these hold: a claim in `claims/` naming the ticket carries a DONE line; the named draft branch exists on the fork and holds `drafts/<KEY>/record.md` and `drafts/<KEY>/jira-comment.md`; the record cites the pinned head it re-checked at and that head matches `evidence_head` in `material/skip-audit/queue-current.json`; the record names the Jira status read in that pass; the record gives a code path with file and line or a reproduction attempt and its result; the record is substantive (about 40 words or more, and not identical to another ticket's record); the verdict is one the rubric allows and the draft branch suffix matches it.

Cross-audit rule: never audit a verdict whose closeout claim or record names your own host as the worker. Mark it `deferred-own-host` in the report and leave it; another host or the vm1 watch takes it. When in doubt, the verdict is suspect and the report names what is missing; a thin or unverifiable pass is worse than a suspect.

## Items

1. **SOLR-4139**, recorded verdict `keep-open` on `solr-4139-draft-open`, evidence head `498a09aea97a`. Output: one row in the batch report.
2. **SOLR-4130**, recorded verdict `keep-open` on `solr-4130-draft-open`, evidence head `498a09aea97a`. Output: one row in the batch report.
3. **SOLR-4122**, recorded verdict `keep-open` on `solr-4122-draft-open`, evidence head `498a09aea97a`. Output: one row in the batch report.
4. **SOLR-4113**, recorded verdict `close-obsolete` on `solr-4113-draft-close`, evidence head `498a09aea97a`. Output: one row in the batch report.
5. **SOLR-4100**, recorded verdict `keep-open` on `solr-4100-draft-open`, evidence head `498a09aea97a`. Output: one row in the batch report.
6. **SOLR-4089**, recorded verdict `keep-open` on `solr-4089-draft-open`, evidence head `498a09aea97a`. Output: one row in the batch report.
7. **SOLR-4069**, recorded verdict `keep-open` on `solr-4069-draft-open`, evidence head `498a09aea97a`. Output: one row in the batch report.
8. **SOLR-4040**, recorded verdict `keep-open` on `solr-4040-draft-open`, evidence head `498a09aea97a`. Output: one row in the batch report.
9. **SOLR-4039**, recorded verdict `keep-open` on `solr-4039-draft-open`, evidence head `498a09aea97a`. Output: one row in the batch report.
10. **SOLR-4037**, recorded verdict `keep-open` on `solr-4037-draft-open`, evidence head `498a09aea97a`. Output: one row in the batch report.
11. **SOLR-4023**, recorded verdict `keep-open` on `solr-4023-draft-open`, evidence head `498a09aea97a`. Output: one row in the batch report.
12. **SOLR-4021**, recorded verdict `keep-open` on `solr-4021-draft-open`, evidence head `498a09aea97a`. Output: one row in the batch report.
13. **SOLR-4006**, recorded verdict `keep-open` on `solr-4006-draft-open`, evidence head `498a09aea97a`. Output: one row in the batch report.
14. **SOLR-3991**, recorded verdict `close-obsolete` on `solr-3991-draft-close`, evidence head `498a09aea97a`. Output: one row in the batch report.
15. **SOLR-3950**, recorded verdict `keep-open` on `solr-3950-draft-open`, evidence head `498a09aea97a`. Output: one row in the batch report.
16. **SOLR-3945**, recorded verdict `keep-open` on `solr-3945-draft-open`, evidence head `498a09aea97a`. Output: one row in the batch report.
17. **SOLR-3944**, recorded verdict `keep-open` on `solr-3944-draft-open`, evidence head `498a09aea97a`. Output: one row in the batch report.
18. **SOLR-3931**, recorded verdict `keep-open` on `solr-3931-draft-open`, evidence head `498a09aea97a`. Output: one row in the batch report.
19. **SOLR-3919**, recorded verdict `keep-open` on `solr-3919-draft-open`, evidence head `498a09aea97a`. Output: one row in the batch report.
20. **SOLR-3905**, recorded verdict `keep-open` on `solr-3905-draft-open`, evidence head `498a09aea97a`. Output: one row in the batch report.
21. **SOLR-3888**, recorded verdict `keep-open` on `solr-3888-draft-open`, evidence head `498a09aea97a`. Output: one row in the batch report.
22. **SOLR-3859**, recorded verdict `keep-open` on `solr-3859-draft-open`, evidence head `498a09aea97a`. Output: one row in the batch report.
23. **SOLR-3806**, recorded verdict `close-fixed` on `solr-3806-draft-close`, evidence head `498a09aea9`. Output: one row in the batch report.
24. **SOLR-3798**, recorded verdict `close-fixed` on `solr-3798-draft-close`, evidence head `498a09aea9`. Output: one row in the batch report.
25. **SOLR-3785**, recorded verdict `close-fixed` on `solr-3785-draft-close`, evidence head `498a09aea9`. Output: one row in the batch report.

## Decisions

| Decision | Answer or default | Condition and action | Owner |
| --- | --- | --- | --- |
| Standard | decided: the checks in this assignment, applied ticket by ticket | no sampling, no bulk pass | Nick |
| Own-host items | decided: deferred-own-host, never a pass from the host that did the closeout | the vm1 watch or another host audits it later | Nick |
| Acceptance | decided: the vm1 watch spot-checks at least 5 tickets per batch and imports the results; suspects enter `material/skip-audit/remediation-current.json` through the watch | auditors do not edit queue-current.json or remediation-current.json | Nick |

## Deliverable

`reports/skip-verdict-audit-batch-18.md`: a table with one row per item: ticket, result (pass, suspect, or deferred-own-host), the claim file found or none, the draft branch head, and for suspects exactly what is missing. End with a one-line count of pass, suspect, and deferred.

## Limits

- Audit only: do not edit the audited tickets' claims, drafts, queue records, or verdicts. No PR, comment, JIRA post or email. No force push.
- Commit as Nick Shanin with no trailers. No em dashes.
