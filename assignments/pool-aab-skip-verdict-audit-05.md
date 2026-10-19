# Pool assignment: skip verdict cross-audit, batch 5 of 21

Status: DONE 2026-10-19T16:14:54Z (vm2: pass 0, suspect 5, deferred 20). Deliverable: reports/skip-verdict-audit-batch-05.md

- Capability tags: review, draft.
- Intended host: any pool Linux host. Claim under `claims/` before starting (WORKFLOW.md).
- Posted by: main agent (vm1), 2026-10-19, at the direction of Nick.

Audit each recorded closeout verdict in this batch against the floor in `material/skip-audit/verdict-rubric.md`, the same checks the vm1 honesty watch runs. A verdict passes only if all of these hold: a claim in `claims/` naming the ticket carries a DONE line; the named draft branch exists on the fork and holds `drafts/<KEY>/record.md` and `drafts/<KEY>/jira-comment.md`; the record cites the pinned head it re-checked at and that head matches `evidence_head` in `material/skip-audit/queue-current.json`; the record names the Jira status read in that pass; the record gives a code path with file and line or a reproduction attempt and its result; the record is substantive (about 40 words or more, and not identical to another ticket's record); the verdict is one the rubric allows and the draft branch suffix matches it.

Cross-audit rule: never audit a verdict whose closeout claim or record names your own host as the worker. Mark it `deferred-own-host` in the report and leave it; another host or the vm1 watch takes it. When in doubt, the verdict is suspect and the report names what is missing; a thin or unverifiable pass is worse than a suspect.

## Items

1. **SOLR-9970**, recorded verdict `keep-open` on `solr-9970-draft-open`, evidence head `498a09aea97a`. Output: one row in the batch report.
2. **SOLR-9965**, recorded verdict `keep-open` on `solr-9965-draft-open`, evidence head `498a09aea97a`. Output: one row in the batch report.
3. **SOLR-9942**, recorded verdict `keep-open` on `solr-9942-draft-open`, evidence head `498a09aea97a`. Output: one row in the batch report.
4. **SOLR-9925**, recorded verdict `keep-open` on `solr-9925-draft-open`, evidence head `498a09aea97a`. Output: one row in the batch report.
5. **SOLR-9913**, recorded verdict `close-obsolete` on `solr-9913-draft-close`, evidence head `498a09aea97a`. Output: one row in the batch report.
6. **SOLR-9872**, recorded verdict `close-obsolete` on `solr-9872-draft-close`, evidence head `498a09aea97a`. Output: one row in the batch report.
7. **SOLR-9871**, recorded verdict `keep-open` on `solr-9871-draft-open`, evidence head `498a09aea97a`. Output: one row in the batch report.
8. **SOLR-9866**, recorded verdict `keep-open` on `solr-9866-draft-open`, evidence head `498a09aea97a`. Output: one row in the batch report.
9. **SOLR-9841**, recorded verdict `keep-open` on `solr-9841-draft-open`, evidence head `498a09aea97a`. Output: one row in the batch report.
10. **SOLR-9830**, recorded verdict `close-fixed` on `solr-9830-draft-close`, evidence head `498a09aea97a`. Output: one row in the batch report.
11. **SOLR-9826**, recorded verdict `close-fixed` on `solr-9826-draft-close`, evidence head `498a09aea97a`. Output: one row in the batch report.
12. **SOLR-9825**, recorded verdict `keep-open` on `solr-9825-draft-open`, evidence head `498a09aea97a`. Output: one row in the batch report.
13. **SOLR-9809**, recorded verdict `keep-open` on `solr-9809-draft-open`, evidence head `498a09aea97a`. Output: one row in the batch report.
14. **SOLR-9806**, recorded verdict `keep-open` on `solr-9806-draft-open`, evidence head `498a09aea97a`. Output: one row in the batch report.
15. **SOLR-9791**, recorded verdict `keep-open` on `solr-9791-draft-open`, evidence head `498a09aea97a`. Output: one row in the batch report.
16. **SOLR-9769**, recorded verdict `keep-open` on `solr-9769-draft-open`, evidence head `498a09aea97a`. Output: one row in the batch report.
17. **SOLR-9765**, recorded verdict `keep-open` on `solr-9765-draft-open`, evidence head `498a09aea97a`. Output: one row in the batch report.
18. **SOLR-9754**, recorded verdict `close-obsolete` on `solr-9754-draft-close`, evidence head `498a09aea97a`. Output: one row in the batch report.
19. **SOLR-9749**, recorded verdict `close-obsolete` on `solr-9749-draft-close`, evidence head `498a09aea97a`. Output: one row in the batch report.
20. **SOLR-9741**, recorded verdict `close-obsolete` on `solr-9741-draft-close`, evidence head `498a09aea97a`. Output: one row in the batch report.
21. **SOLR-9723**, recorded verdict `keep-open` on `solr-9723-draft-open`, evidence head `498a09aea97a`. Output: one row in the batch report.
22. **SOLR-9722**, recorded verdict `keep-open` on `solr-9722-draft-open`, evidence head `498a09aea97a`. Output: one row in the batch report.
23. **SOLR-9713**, recorded verdict `keep-open` on `solr-9713-draft-open`, evidence head `498a09aea97a`. Output: one row in the batch report.
24. **SOLR-9677**, recorded verdict `close-fixed` on `solr-9677-draft-close`, evidence head `498a09aea97a`. Output: one row in the batch report.
25. **SOLR-9664**, recorded verdict `keep-open` on `solr-9664-draft-open`, evidence head `498a09aea97a`. Output: one row in the batch report.

## Decisions

| Decision | Answer or default | Condition and action | Owner |
| --- | --- | --- | --- |
| Standard | decided: the checks in this assignment, applied ticket by ticket | no sampling, no bulk pass | Nick |
| Own-host items | decided: deferred-own-host, never a pass from the host that did the closeout | the vm1 watch or another host audits it later | Nick |
| Acceptance | decided: the vm1 watch spot-checks at least 5 tickets per batch and imports the results; suspects enter `material/skip-audit/remediation-current.json` through the watch | auditors do not edit queue-current.json or remediation-current.json | Nick |

## Deliverable

`reports/skip-verdict-audit-batch-05.md`: a table with one row per item: ticket, result (pass, suspect, or deferred-own-host), the claim file found or none, the draft branch head, and for suspects exactly what is missing. End with a one-line count of pass, suspect, and deferred.

## Limits

- Audit only: do not edit the audited tickets' claims, drafts, queue records, or verdicts. No PR, comment, JIRA post or email. No force push.
- Commit as Nick Shanin with no trailers. No em dashes.
