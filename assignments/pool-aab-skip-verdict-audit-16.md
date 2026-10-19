# Pool assignment: skip verdict cross-audit, batch 16 of 21

Status: DONE 2026-10-19T16:46:24Z (vm2: pass 0, suspect 17, deferred 8). Deliverable: reports/skip-verdict-audit-batch-16.md

- Capability tags: review, draft.
- Intended host: any pool Linux host. Claim under `claims/` before starting (WORKFLOW.md).
- Posted by: main agent (vm1), 2026-10-19, at the direction of Nick.

Audit each recorded closeout verdict in this batch against the floor in `material/skip-audit/verdict-rubric.md`, the same checks the vm1 honesty watch runs. A verdict passes only if all of these hold: a claim in `claims/` naming the ticket carries a DONE line; the named draft branch exists on the fork and holds `drafts/<KEY>/record.md` and `drafts/<KEY>/jira-comment.md`; the record cites the pinned head it re-checked at and that head matches `evidence_head` in `material/skip-audit/queue-current.json`; the record names the Jira status read in that pass; the record gives a code path with file and line or a reproduction attempt and its result; the record is substantive (about 40 words or more, and not identical to another ticket's record); the verdict is one the rubric allows and the draft branch suffix matches it.

Cross-audit rule: never audit a verdict whose closeout claim or record names your own host as the worker. Mark it `deferred-own-host` in the report and leave it; another host or the vm1 watch takes it. When in doubt, the verdict is suspect and the report names what is missing; a thin or unverifiable pass is worse than a suspect.

## Items

1. **SOLR-5462**, recorded verdict `keep-open` on `solr-5462-draft-open`, evidence head `498a09aea97a`. Output: one row in the batch report.
2. **SOLR-5435**, recorded verdict `keep-open` on `solr-5435-draft-open`, evidence head `498a09aea97a`. Output: one row in the batch report.
3. **SOLR-5383**, recorded verdict `keep-open` on `solr-5383-draft-open`, evidence head `498a09aea97a`. Output: one row in the batch report.
4. **SOLR-5376**, recorded verdict `keep-open` on `solr-5376-draft-open`, evidence head `498a09aea97a`. Output: one row in the batch report.
5. **SOLR-5252**, recorded verdict `keep-open` on `solr-5252-draft-open`, evidence head `498a09aea97a`. Output: one row in the batch report.
6. **SOLR-5235**, recorded verdict `keep-open` on `solr-5235-draft-open`, evidence head `498a09aea97a`. Output: one row in the batch report.
7. **SOLR-5224**, recorded verdict `keep-open` on `solr-5224-draft-open`, evidence head `498a09aea97a`. Output: one row in the batch report.
8. **SOLR-5205**, recorded verdict `keep-open` on `solr-5205-draft-open`, evidence head `498a09aea97a`. Output: one row in the batch report.
9. **SOLR-5161**, recorded verdict `keep-open` on `solr-5161-draft-open`, evidence head `498a09aea97a`. Output: one row in the batch report.
10. **SOLR-5145**, recorded verdict `close-fixed` on `solr-5145-draft-close`, evidence head `498a09aea9`. Output: one row in the batch report.
11. **SOLR-5115**, recorded verdict `keep-open` on `solr-5115-draft-open`, evidence head `498a09aea97a`. Output: one row in the batch report.
12. **SOLR-5095**, recorded verdict `keep-open` on `solr-5095-draft-open`, evidence head `498a09aea97a`. Output: one row in the batch report.
13. **SOLR-5089**, recorded verdict `keep-open` on `solr-5089-draft-open`, evidence head `498a09aea97a`. Output: one row in the batch report.
14. **SOLR-5067**, recorded verdict `keep-open` on `solr-5067-draft-open`, evidence head `498a09aea97a`. Output: one row in the batch report.
15. **SOLR-5046**, recorded verdict `keep-open` on `solr-5046-draft-open`, evidence head `498a09aea97a`. Output: one row in the batch report.
16. **SOLR-4996**, recorded verdict `keep-open` on `solr-4996-draft-open`, evidence head `498a09aea97a`. Output: one row in the batch report.
17. **SOLR-4983**, recorded verdict `keep-open` on `solr-4983-draft-open`, evidence head `498a09aea97a`. Output: one row in the batch report.
18. **SOLR-4880**, recorded verdict `keep-open` on `solr-4880-draft-open`, evidence head `498a09aea97a`. Output: one row in the batch report.
19. **SOLR-4878**, recorded verdict `keep-open` on `solr-4878-draft-open`, evidence head `498a09aea97a`. Output: one row in the batch report.
20. **SOLR-4859**, recorded verdict `keep-open` on `solr-4859-draft-open`, evidence head `498a09aea97a`. Output: one row in the batch report.
21. **SOLR-4851**, recorded verdict `keep-open` on `solr-4851-draft-open`, evidence head `498a09aea97a`. Output: one row in the batch report.
22. **SOLR-4753**, recorded verdict `keep-open` on `solr-4753-draft-open`, evidence head `498a09aea97a`. Output: one row in the batch report.
23. **SOLR-4739**, recorded verdict `close-fixed` on `solr-4739-draft-close`, evidence head `498a09aea9`. Output: one row in the batch report.
24. **SOLR-4733**, recorded verdict `keep-open` on `solr-4733-draft-open`, evidence head `498a09aea97a`. Output: one row in the batch report.
25. **SOLR-4686**, recorded verdict `keep-open` on `solr-4686-draft-open`, evidence head `498a09aea97a`. Output: one row in the batch report.

## Decisions

| Decision | Answer or default | Condition and action | Owner |
| --- | --- | --- | --- |
| Standard | decided: the checks in this assignment, applied ticket by ticket | no sampling, no bulk pass | Nick |
| Own-host items | decided: deferred-own-host, never a pass from the host that did the closeout | the vm1 watch or another host audits it later | Nick |
| Acceptance | decided: the vm1 watch spot-checks at least 5 tickets per batch and imports the results; suspects enter `material/skip-audit/remediation-current.json` through the watch | auditors do not edit queue-current.json or remediation-current.json | Nick |

## Deliverable

`reports/skip-verdict-audit-batch-16.md`: a table with one row per item: ticket, result (pass, suspect, or deferred-own-host), the claim file found or none, the draft branch head, and for suspects exactly what is missing. End with a one-line count of pass, suspect, and deferred.

## Limits

- Audit only: do not edit the audited tickets' claims, drafts, queue records, or verdicts. No PR, comment, JIRA post or email. No force push.
- Commit as Nick Shanin with no trailers. No em dashes.
