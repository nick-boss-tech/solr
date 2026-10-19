# Pool assignment: skip verdict cross-audit, batch 9 of 21

Status: DONE 2026-10-19T16:25:41Z (vm2: pass 0, suspect 17, deferred 8). Deliverable: reports/skip-verdict-audit-batch-09.md

- Capability tags: review, draft.
- Intended host: any pool Linux host. Claim under `claims/` before starting (WORKFLOW.md).
- Posted by: main agent (vm1), 2026-10-19, at the direction of Nick.

Audit each recorded closeout verdict in this batch against the floor in `material/skip-audit/verdict-rubric.md`, the same checks the vm1 honesty watch runs. A verdict passes only if all of these hold: a claim in `claims/` naming the ticket carries a DONE line; the named draft branch exists on the fork and holds `drafts/<KEY>/record.md` and `drafts/<KEY>/jira-comment.md`; the record cites the pinned head it re-checked at and that head matches `evidence_head` in `material/skip-audit/queue-current.json`; the record names the Jira status read in that pass; the record gives a code path with file and line or a reproduction attempt and its result; the record is substantive (about 40 words or more, and not identical to another ticket's record); the verdict is one the rubric allows and the draft branch suffix matches it.

Cross-audit rule: never audit a verdict whose closeout claim or record names your own host as the worker. Mark it `deferred-own-host` in the report and leave it; another host or the vm1 watch takes it. When in doubt, the verdict is suspect and the report names what is missing; a thin or unverifiable pass is worse than a suspect.

## Items

1. **SOLR-8685**, recorded verdict `keep-open` on `solr-8685-draft-open`, evidence head `498a09aea97a`. Output: one row in the batch report.
2. **SOLR-8681**, recorded verdict `keep-open` on `solr-8681-draft-open`, evidence head `498a09aea97a`. Output: one row in the batch report.
3. **SOLR-8641**, recorded verdict `keep-open` on `solr-8641-draft-open`, evidence head `498a09aea97a`. Output: one row in the batch report.
4. **SOLR-8635**, recorded verdict `keep-open` on `solr-8635-draft-open`, evidence head `498a09aea97a`. Output: one row in the batch report.
5. **SOLR-8634**, recorded verdict `keep-open` on `solr-8634-draft-open`, evidence head `498a09aea97a`. Output: one row in the batch report.
6. **SOLR-8632**, recorded verdict `keep-open` on `solr-8632-draft-open`, evidence head `498a09aea97a`. Output: one row in the batch report.
7. **SOLR-8629**, recorded verdict `keep-open` on `solr-8629-draft-open`, evidence head `498a09aea97a`. Output: one row in the batch report.
8. **SOLR-8623**, recorded verdict `close-fixed` on `solr-8623-draft-close`, evidence head `498a09aea97a`. Output: one row in the batch report.
9. **SOLR-8619**, recorded verdict `keep-open` on `solr-8619-draft-open`, evidence head `498a09aea97a`. Output: one row in the batch report.
10. **SOLR-8609**, recorded verdict `keep-open` on `solr-8609-draft-open`, evidence head `498a09aea97a`. Output: one row in the batch report.
11. **SOLR-8572**, recorded verdict `keep-open` on `solr-8572-draft-open`, evidence head `498a09aea97a`. Output: one row in the batch report.
12. **SOLR-8568**, recorded verdict `keep-open` on `solr-8568-draft-open`, evidence head `498a09aea97a`. Output: one row in the batch report.
13. **SOLR-8553**, recorded verdict `keep-open` on `solr-8553-draft-open`, evidence head `498a09aea97a`. Output: one row in the batch report.
14. **SOLR-8543**, recorded verdict `keep-open` on `solr-8543-draft-open`, evidence head `498a09aea97a`. Output: one row in the batch report.
15. **SOLR-8539**, recorded verdict `keep-open` on `solr-8539-draft-open`, evidence head `498a09aea97a`. Output: one row in the batch report.
16. **SOLR-8478**, recorded verdict `keep-open` on `solr-8478-draft-open`, evidence head `498a09aea97a`. Output: one row in the batch report.
17. **SOLR-8469**, recorded verdict `keep-open` on `solr-8469-draft-open`, evidence head `498a09aea97a`. Output: one row in the batch report.
18. **SOLR-8463**, recorded verdict `keep-open` on `solr-8463-draft-open`, evidence head `498a09aea97a`. Output: one row in the batch report.
19. **SOLR-8431**, recorded verdict `keep-open` on `solr-8431-draft-open`, evidence head `498a09aea97a`. Output: one row in the batch report.
20. **SOLR-8380**, recorded verdict `keep-open` on `solr-8380-draft-open`, evidence head `498a09aea97a`. Output: one row in the batch report.
21. **SOLR-8368**, recorded verdict `keep-open` on `solr-8368-draft-open`, evidence head `498a09aea97a`. Output: one row in the batch report.
22. **SOLR-8365**, recorded verdict `keep-open` on `solr-8365-draft-open`, evidence head `498a09aea97a`. Output: one row in the batch report.
23. **SOLR-8311**, recorded verdict `keep-open` on `solr-8311-draft-open`, evidence head `498a09aea97a`. Output: one row in the batch report.
24. **SOLR-8310**, recorded verdict `keep-open` on `solr-8310-draft-open`, evidence head `498a09aea97a`. Output: one row in the batch report.
25. **SOLR-8304**, recorded verdict `keep-open` on `solr-8304-draft-open`, evidence head `498a09aea97a`. Output: one row in the batch report.

## Decisions

| Decision | Answer or default | Condition and action | Owner |
| --- | --- | --- | --- |
| Standard | decided: the checks in this assignment, applied ticket by ticket | no sampling, no bulk pass | Nick |
| Own-host items | decided: deferred-own-host, never a pass from the host that did the closeout | the vm1 watch or another host audits it later | Nick |
| Acceptance | decided: the vm1 watch spot-checks at least 5 tickets per batch and imports the results; suspects enter `material/skip-audit/remediation-current.json` through the watch | auditors do not edit queue-current.json or remediation-current.json | Nick |

## Deliverable

`reports/skip-verdict-audit-batch-09.md`: a table with one row per item: ticket, result (pass, suspect, or deferred-own-host), the claim file found or none, the draft branch head, and for suspects exactly what is missing. End with a one-line count of pass, suspect, and deferred.

## Limits

- Audit only: do not edit the audited tickets' claims, drafts, queue records, or verdicts. No PR, comment, JIRA post or email. No force push.
- Commit as Nick Shanin with no trailers. No em dashes.
