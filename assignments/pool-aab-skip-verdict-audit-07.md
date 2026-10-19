# Pool assignment: skip verdict cross-audit, batch 7 of 21

Status: OPEN

- Capability tags: review, draft.
- Intended host: any pool Linux host. Claim under `claims/` before starting (WORKFLOW.md).
- Posted by: main agent (vm1), 2026-10-19, at the direction of Nick.

Audit each recorded closeout verdict in this batch against the floor in `material/skip-audit/verdict-rubric.md`, the same checks the vm1 honesty watch runs. A verdict passes only if all of these hold: a claim in `claims/` naming the ticket carries a DONE line; the named draft branch exists on the fork and holds `drafts/<KEY>/record.md` and `drafts/<KEY>/jira-comment.md`; the record cites the pinned head it re-checked at and that head matches `evidence_head` in `material/skip-audit/queue-current.json`; the record names the Jira status read in that pass; the record gives a code path with file and line or a reproduction attempt and its result; the record is substantive (about 40 words or more, and not identical to another ticket's record); the verdict is one the rubric allows and the draft branch suffix matches it.

Cross-audit rule: never audit a verdict whose closeout claim or record names your own host as the worker. Mark it `deferred-own-host` in the report and leave it; another host or the vm1 watch takes it. When in doubt, the verdict is suspect and the report names what is missing; a thin or unverifiable pass is worse than a suspect.

## Items

1. **SOLR-9328**, recorded verdict `keep-open` on `solr-9328-draft-open`, evidence head `498a09aea97a`. Output: one row in the batch report.
2. **SOLR-9312**, recorded verdict `keep-open` on `solr-9312-draft-open`, evidence head `498a09aea97a`. Output: one row in the batch report.
3. **SOLR-9267**, recorded verdict `close-fixed` on `solr-9267-draft-close`, evidence head `498a09aea97a`. Output: one row in the batch report.
4. **SOLR-9260**, recorded verdict `keep-open` on `solr-9260-draft-open`, evidence head `498a09aea97a`. Output: one row in the batch report.
5. **SOLR-9250**, recorded verdict `keep-open` on `solr-9250-draft-open`, evidence head `498a09aea97a`. Output: one row in the batch report.
6. **SOLR-9248**, recorded verdict `close-obsolete` on `solr-9248-draft-close`, evidence head `498a09aea97a`. Output: one row in the batch report.
7. **SOLR-9230**, recorded verdict `keep-open` on `solr-9230-draft-open`, evidence head `498a09aea97a`. Output: one row in the batch report.
8. **SOLR-9226**, recorded verdict `keep-open` on `solr-9226-draft-open`, evidence head `498a09aea97a`. Output: one row in the batch report.
9. **SOLR-9223**, recorded verdict `keep-open` on `solr-9223-draft-open`, evidence head `498a09aea97a`. Output: one row in the batch report.
10. **SOLR-9214**, recorded verdict `keep-open` on `solr-9214-draft-open`, evidence head `498a09aea97a`. Output: one row in the batch report.
11. **SOLR-9213**, recorded verdict `keep-open` on `solr-9213-draft-open`, evidence head `498a09aea97a`. Output: one row in the batch report.
12. **SOLR-9208**, recorded verdict `keep-open` on `solr-9208-draft-open`, evidence head `498a09aea97a`. Output: one row in the batch report.
13. **SOLR-9206**, recorded verdict `keep-open` on `solr-9206-draft-open`, evidence head `498a09aea97a`. Output: one row in the batch report.
14. **SOLR-9197**, recorded verdict `keep-open` on `solr-9197-draft-open`, evidence head `498a09aea97a`. Output: one row in the batch report.
15. **SOLR-9183**, recorded verdict `close-obsolete` on `solr-9183-draft-close`, evidence head `498a09aea97a`. Output: one row in the batch report.
16. **SOLR-9182**, recorded verdict `keep-open` on `solr-9182-draft-open`, evidence head `498a09aea97a`. Output: one row in the batch report.
17. **SOLR-9174**, recorded verdict `keep-open` on `solr-9174-draft-open`, evidence head `498a09aea97a`. Output: one row in the batch report.
18. **SOLR-9173**, recorded verdict `keep-open` on `solr-9173-draft-open`, evidence head `498a09aea97a`. Output: one row in the batch report.
19. **SOLR-9144**, recorded verdict `keep-open` on `solr-9144-draft-open`, evidence head `498a09aea97a`. Output: one row in the batch report.
20. **SOLR-9129**, recorded verdict `keep-open` on `solr-9129-draft-open`, evidence head `498a09aea97a`. Output: one row in the batch report.
21. **SOLR-9126**, recorded verdict `close-fixed` on `solr-9126-draft-close`, evidence head `498a09aea97a`. Output: one row in the batch report.
22. **SOLR-9123**, recorded verdict `keep-open` on `solr-9123-draft-open`, evidence head `498a09aea97a`. Output: one row in the batch report.
23. **SOLR-9122**, recorded verdict `keep-open` on `solr-9122-draft-open`, evidence head `498a09aea97a`. Output: one row in the batch report.
24. **SOLR-9094**, recorded verdict `close-fixed` on `solr-9094-draft-close`, evidence head `498a09aea97a`. Output: one row in the batch report.
25. **SOLR-9089**, recorded verdict `keep-open` on `solr-9089-draft-open`, evidence head `498a09aea97a`. Output: one row in the batch report.

## Decisions

| Decision | Answer or default | Condition and action | Owner |
| --- | --- | --- | --- |
| Standard | decided: the checks in this assignment, applied ticket by ticket | no sampling, no bulk pass | Nick |
| Own-host items | decided: deferred-own-host, never a pass from the host that did the closeout | the vm1 watch or another host audits it later | Nick |
| Acceptance | decided: the vm1 watch spot-checks at least 5 tickets per batch and imports the results; suspects enter `material/skip-audit/remediation-current.json` through the watch | auditors do not edit queue-current.json or remediation-current.json | Nick |

## Deliverable

`reports/skip-verdict-audit-batch-07.md`: a table with one row per item: ticket, result (pass, suspect, or deferred-own-host), the claim file found or none, the draft branch head, and for suspects exactly what is missing. End with a one-line count of pass, suspect, and deferred.

## Limits

- Audit only: do not edit the audited tickets' claims, drafts, queue records, or verdicts. No PR, comment, JIRA post or email. No force push.
- Commit as Nick Shanin with no trailers. No em dashes.
