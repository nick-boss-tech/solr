# Pool assignment: skip verdict cross-audit, batch 13 of 21

Status: CLAIMED (claims/pool-aab-skip-verdict-audit-13.md)

- Capability tags: review, draft.
- Intended host: any pool Linux host. Claim under `claims/` before starting (WORKFLOW.md).
- Posted by: main agent (vm1), 2026-10-19, at the direction of Nick.

Audit each recorded closeout verdict in this batch against the floor in `material/skip-audit/verdict-rubric.md`, the same checks the vm1 honesty watch runs. A verdict passes only if all of these hold: a claim in `claims/` naming the ticket carries a DONE line; the named draft branch exists on the fork and holds `drafts/<KEY>/record.md` and `drafts/<KEY>/jira-comment.md`; the record cites the pinned head it re-checked at and that head matches `evidence_head` in `material/skip-audit/queue-current.json`; the record names the Jira status read in that pass; the record gives a code path with file and line or a reproduction attempt and its result; the record is substantive (about 40 words or more, and not identical to another ticket's record); the verdict is one the rubric allows and the draft branch suffix matches it.

Cross-audit rule: never audit a verdict whose closeout claim or record names your own host as the worker. Mark it `deferred-own-host` in the report and leave it; another host or the vm1 watch takes it. When in doubt, the verdict is suspect and the report names what is missing; a thin or unverifiable pass is worse than a suspect.

## Items

1. **SOLR-6875**, recorded verdict `keep-open` on `solr-6875-draft-open`, evidence head `498a09aea97a`. Output: one row in the batch report.
2. **SOLR-6846**, recorded verdict `keep-open` on `solr-6846-draft-open`, evidence head `498a09aea97a`. Output: one row in the batch report.
3. **SOLR-6837**, recorded verdict `keep-open` on `solr-6837-draft-open`, evidence head `498a09aea97a`. Output: one row in the batch report.
4. **SOLR-6836**, recorded verdict `keep-open` on `solr-6836-draft-open`, evidence head `498a09aea97a`. Output: one row in the batch report.
5. **SOLR-6828**, recorded verdict `keep-open` on `solr-6828-draft-open`, evidence head `498a09aea97a`. Output: one row in the batch report.
6. **SOLR-6822**, recorded verdict `close-fixed` on `solr-6822-draft-close`, evidence head `498a09aea97a`. Output: one row in the batch report.
7. **SOLR-6813**, recorded verdict `keep-open` on `solr-6813-draft-open`, evidence head `498a09aea97a`. Output: one row in the batch report.
8. **SOLR-6811**, recorded verdict `close-obsolete` on `solr-6811-draft-close`, evidence head `498a09aea97a`. Output: one row in the batch report.
9. **SOLR-6785**, recorded verdict `keep-open` on `solr-6785-draft-open`, evidence head `498a09aea97a`. Output: one row in the batch report.
10. **SOLR-6782**, recorded verdict `close-obsolete` on `solr-6782-draft-close`, evidence head `498a09aea97a`. Output: one row in the batch report.
11. **SOLR-6756**, recorded verdict `close-obsolete` on `solr-6756-draft-close`, evidence head `498a09aea97a`. Output: one row in the batch report.
12. **SOLR-6719**, recorded verdict `keep-open` on `solr-6719-draft-open`, evidence head `498a09aea97a`. Output: one row in the batch report.
13. **SOLR-6717**, recorded verdict `keep-open` on `solr-6717-draft-open`, evidence head `498a09aea97a`. Output: one row in the batch report.
14. **SOLR-6714**, recorded verdict `keep-open` on `solr-6714-draft-open`, evidence head `498a09aea97a`. Output: one row in the batch report.
15. **SOLR-6712**, recorded verdict `keep-open` on `solr-6712-draft-open`, evidence head `498a09aea97a`. Output: one row in the batch report.
16. **SOLR-6678**, recorded verdict `keep-open` on `solr-6678-draft-open`, evidence head `498a09aea97a`. Output: one row in the batch report.
17. **SOLR-6649**, recorded verdict `keep-open` on `solr-6649-draft-open`, evidence head `498a09aea97a`. Output: one row in the batch report.
18. **SOLR-6621**, recorded verdict `keep-open` on `solr-6621-draft-open`, evidence head `498a09aea97a`. Output: one row in the batch report.
19. **SOLR-6593**, recorded verdict `keep-open` on `solr-6593-draft-open`, evidence head `498a09aea97a`. Output: one row in the batch report.
20. **SOLR-6588**, recorded verdict `close-answered` on `solr-6588-draft-close`, evidence head `498a09aea97a`. Output: one row in the batch report.
21. **SOLR-6582**, recorded verdict `close-obsolete` on `solr-6582-draft-close`, evidence head `498a09aea97a`. Output: one row in the batch report.
22. **SOLR-6576**, recorded verdict `keep-open` on `solr-6576-draft-open`, evidence head `498a09aea97a`. Output: one row in the batch report.
23. **SOLR-6561**, recorded verdict `close-obsolete` on `solr-6561-draft-close`, evidence head `498a09aea97a`. Output: one row in the batch report.
24. **SOLR-6537**, recorded verdict `keep-open` on `solr-6537-draft-open`, evidence head `498a09aea97a`. Output: one row in the batch report.
25. **SOLR-6534**, recorded verdict `close-fixed` on `solr-6534-draft-close`, evidence head `498a09aea97a`. Output: one row in the batch report.

## Decisions

| Decision | Answer or default | Condition and action | Owner |
| --- | --- | --- | --- |
| Standard | decided: the checks in this assignment, applied ticket by ticket | no sampling, no bulk pass | Nick |
| Own-host items | decided: deferred-own-host, never a pass from the host that did the closeout | the vm1 watch or another host audits it later | Nick |
| Acceptance | decided: the vm1 watch spot-checks at least 5 tickets per batch and imports the results; suspects enter `material/skip-audit/remediation-current.json` through the watch | auditors do not edit queue-current.json or remediation-current.json | Nick |

## Deliverable

`reports/skip-verdict-audit-batch-13.md`: a table with one row per item: ticket, result (pass, suspect, or deferred-own-host), the claim file found or none, the draft branch head, and for suspects exactly what is missing. End with a one-line count of pass, suspect, and deferred.

## Limits

- Audit only: do not edit the audited tickets' claims, drafts, queue records, or verdicts. No PR, comment, JIRA post or email. No force push.
- Commit as Nick Shanin with no trailers. No em dashes.
