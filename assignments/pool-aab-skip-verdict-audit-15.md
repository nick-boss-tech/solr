# Pool assignment: skip verdict cross-audit, batch 15 of 21

Status: OPEN

- Capability tags: review, draft.
- Intended host: any pool Linux host. Claim under `claims/` before starting (WORKFLOW.md).
- Posted by: main agent (vm1), 2026-10-19, at the direction of Nick.

Audit each recorded closeout verdict in this batch against the floor in `material/skip-audit/verdict-rubric.md`, the same checks the vm1 honesty watch runs. A verdict passes only if all of these hold: a claim in `claims/` naming the ticket carries a DONE line; the named draft branch exists on the fork and holds `drafts/<KEY>/record.md` and `drafts/<KEY>/jira-comment.md`; the record cites the pinned head it re-checked at and that head matches `evidence_head` in `material/skip-audit/queue-current.json`; the record names the Jira status read in that pass; the record gives a code path with file and line or a reproduction attempt and its result; the record is substantive (about 40 words or more, and not identical to another ticket's record); the verdict is one the rubric allows and the draft branch suffix matches it.

Cross-audit rule: never audit a verdict whose closeout claim or record names your own host as the worker. Mark it `deferred-own-host` in the report and leave it; another host or the vm1 watch takes it. When in doubt, the verdict is suspect and the report names what is missing; a thin or unverifiable pass is worse than a suspect.

## Items

1. **SOLR-5850**, recorded verdict `close-obsolete` on `solr-5850-draft-close`, evidence head `498a09aea97a`. Output: one row in the batch report.
2. **SOLR-5845**, recorded verdict `keep-open` on `solr-5845-draft-open`, evidence head `498a09aea97a`. Output: one row in the batch report.
3. **SOLR-5840**, recorded verdict `keep-open` on `solr-5840-draft-open`, evidence head `498a09aea97a`. Output: one row in the batch report.
4. **SOLR-5833**, recorded verdict `close-obsolete` on `solr-5833-draft-close`, evidence head `498a09aea97a`. Output: one row in the batch report.
5. **SOLR-5822**, recorded verdict `keep-open` on `solr-5822-draft-open`, evidence head `498a09aea97a`. Output: one row in the batch report.
6. **SOLR-5812**, recorded verdict `keep-open` on `solr-5812-draft-open`, evidence head `498a09aea97a`. Output: one row in the batch report.
7. **SOLR-5793**, recorded verdict `keep-open` on `solr-5793-draft-open`, evidence head `498a09aea97a`. Output: one row in the batch report.
8. **SOLR-5792**, recorded verdict `keep-open` on `solr-5792-draft-open`, evidence head `498a09aea97a`. Output: one row in the batch report.
9. **SOLR-5791**, recorded verdict `keep-open` on `solr-5791-draft-open`, evidence head `498a09aea97a`. Output: one row in the batch report.
10. **SOLR-5788**, recorded verdict `keep-open` on `solr-5788-draft-open`, evidence head `498a09aea97a`. Output: one row in the batch report.
11. **SOLR-5772**, recorded verdict `keep-open` on `solr-5772-draft-open`, evidence head `498a09aea97a`. Output: one row in the batch report.
12. **SOLR-5759**, recorded verdict `keep-open` on `solr-5759-draft-open`, evidence head `498a09aea97a`. Output: one row in the batch report.
13. **SOLR-5736**, recorded verdict `keep-open` on `solr-5736-draft-open`, evidence head `498a09aea97a`. Output: one row in the batch report.
14. **SOLR-5719**, recorded verdict `keep-open` on `solr-5719-draft-open`, evidence head `498a09aea97a`. Output: one row in the batch report.
15. **SOLR-5717**, recorded verdict `close-fixed` on `solr-5717-draft-close`, evidence head `498a09aea97a`. Output: one row in the batch report.
16. **SOLR-5712**, recorded verdict `close-fixed` on `solr-5712-draft-close`, evidence head `498a09aea97a`. Output: one row in the batch report.
17. **SOLR-5628**, recorded verdict `keep-open` on `solr-5628-draft-open`, evidence head `498a09aea97a`. Output: one row in the batch report.
18. **SOLR-5592**, recorded verdict `keep-open` on `solr-5592-draft-open`, evidence head `498a09aea97a`. Output: one row in the batch report.
19. **SOLR-5579**, recorded verdict `keep-open` on `solr-5579-draft-open`, evidence head `498a09aea97a`. Output: one row in the batch report.
20. **SOLR-5566**, recorded verdict `keep-open` on `solr-5566-draft-open`, evidence head `498a09aea97a`. Output: one row in the batch report.
21. **SOLR-5560**, recorded verdict `keep-open` on `solr-5560-draft-open`, evidence head `498a09aea97a`. Output: one row in the batch report.
22. **SOLR-5559**, recorded verdict `close-fixed` on `solr-5559-draft-close`, evidence head `498a09aea97a`. Output: one row in the batch report.
23. **SOLR-5516**, recorded verdict `keep-open` on `solr-5516-draft-open`, evidence head `498a09aea9`. Output: one row in the batch report.
24. **SOLR-5508**, recorded verdict `keep-open` on `solr-5508-draft-open`, evidence head `498a09aea97a`. Output: one row in the batch report.
25. **SOLR-5485**, recorded verdict `keep-open` on `solr-5485-draft-open`, evidence head `498a09aea97a`. Output: one row in the batch report.

## Decisions

| Decision | Answer or default | Condition and action | Owner |
| --- | --- | --- | --- |
| Standard | decided: the checks in this assignment, applied ticket by ticket | no sampling, no bulk pass | Nick |
| Own-host items | decided: deferred-own-host, never a pass from the host that did the closeout | the vm1 watch or another host audits it later | Nick |
| Acceptance | decided: the vm1 watch spot-checks at least 5 tickets per batch and imports the results; suspects enter `material/skip-audit/remediation-current.json` through the watch | auditors do not edit queue-current.json or remediation-current.json | Nick |

## Deliverable

`reports/skip-verdict-audit-batch-15.md`: a table with one row per item: ticket, result (pass, suspect, or deferred-own-host), the claim file found or none, the draft branch head, and for suspects exactly what is missing. End with a one-line count of pass, suspect, and deferred.

## Limits

- Audit only: do not edit the audited tickets' claims, drafts, queue records, or verdicts. No PR, comment, JIRA post or email. No force push.
- Commit as Nick Shanin with no trailers. No em dashes.
