# Pool assignment: skip verdict cross-audit, batch 20 of 21

Status: DONE (reports/skip-verdict-audit-batch-20.md, 2026-10-19T16:51:28Z)

- Capability tags: review, draft.
- Intended host: any pool Linux host. Claim under `claims/` before starting (WORKFLOW.md).
- Posted by: main agent (vm1), 2026-10-19, at the direction of Nick.

Audit each recorded closeout verdict in this batch against the floor in `material/skip-audit/verdict-rubric.md`, the same checks the vm1 honesty watch runs. A verdict passes only if all of these hold: a claim in `claims/` naming the ticket carries a DONE line; the named draft branch exists on the fork and holds `drafts/<KEY>/record.md` and `drafts/<KEY>/jira-comment.md`; the record cites the pinned head it re-checked at and that head matches `evidence_head` in `material/skip-audit/queue-current.json`; the record names the Jira status read in that pass; the record gives a code path with file and line or a reproduction attempt and its result; the record is substantive (about 40 words or more, and not identical to another ticket's record); the verdict is one the rubric allows and the draft branch suffix matches it.

Cross-audit rule: never audit a verdict whose closeout claim or record names your own host as the worker. Mark it `deferred-own-host` in the report and leave it; another host or the vm1 watch takes it. When in doubt, the verdict is suspect and the report names what is missing; a thin or unverifiable pass is worse than a suspect.

## Items

1. **SOLR-3201**, recorded verdict `close-obsolete` on `solr-3201-draft-close`, evidence head `498a09aea97a`. Output: one row in the batch report.
2. **SOLR-3128**, recorded verdict `keep-open` on `solr-3128-draft-open`, evidence head `498a09aea97a`. Output: one row in the batch report.
3. **SOLR-3124**, recorded verdict `keep-open` on `solr-3124-draft-open`, evidence head `498a09aea97a`. Output: one row in the batch report.
4. **SOLR-3064**, recorded verdict `close-obsolete` on `solr-3064-draft-close`, evidence head `498a09aea97a`. Output: one row in the batch report.
5. **SOLR-3050**, recorded verdict `keep-open` on `solr-3050-draft-open`, evidence head `498a09aea97a`. Output: one row in the batch report.
6. **SOLR-3034**, recorded verdict `keep-open` on `solr-3034-draft-open`, evidence head `498a09aea97a`. Output: one row in the batch report.
7. **SOLR-3027**, recorded verdict `keep-open` on `solr-3027-draft-open`, evidence head `498a09aea97a`. Output: one row in the batch report.
8. **SOLR-3023**, recorded verdict `keep-open` on `solr-3023-draft-open`, evidence head `498a09aea97a`. Output: one row in the batch report.
9. **SOLR-3008**, recorded verdict `keep-open` on `solr-3008-draft-open`, evidence head `498a09aea97a`. Output: one row in the batch report.
10. **SOLR-2976**, recorded verdict `close-fixed` on `solr-2976-draft-close`, evidence head `498a09aea97a`. Output: one row in the batch report.
11. **SOLR-2936**, recorded verdict `close-fixed` on `solr-2936-draft-close`, evidence head `498a09aea9`. Output: one row in the batch report.
12. **SOLR-2928**, recorded verdict `keep-open` on `solr-2928-draft-open`, evidence head `498a09aea97a`. Output: one row in the batch report.
13. **SOLR-2913**, recorded verdict `close-fixed` on `solr-2913-draft-close`, evidence head `498a09aea9`. Output: one row in the batch report.
14. **SOLR-2900**, recorded verdict `keep-open` on `solr-2900-draft-open`, evidence head `498a09aea97a`. Output: one row in the batch report.
15. **SOLR-2865**, recorded verdict `keep-open` on `solr-2865-draft-open`, evidence head `498a09aea97a`. Output: one row in the batch report.
16. **SOLR-2855**, recorded verdict `keep-open` on `solr-2855-draft-open`, evidence head `498a09aea97a`. Output: one row in the batch report.
17. **SOLR-2845**, recorded verdict `keep-open` on `solr-2845-draft-open`, evidence head `498a09aea97a`. Output: one row in the batch report.
18. **SOLR-2751**, recorded verdict `keep-open` on `solr-2751-draft-open`, evidence head `498a09aea97a`. Output: one row in the batch report.
19. **SOLR-2720**, recorded verdict `keep-open` on `solr-2720-draft-open`, evidence head `498a09aea97a`. Output: one row in the batch report.
20. **SOLR-2715**, recorded verdict `keep-open` on `solr-2715-draft-open`, evidence head `498a09aea97a`. Output: one row in the batch report.
21. **SOLR-2516**, recorded verdict `keep-open` on `solr-2516-draft-open`, evidence head `498a09aea97a`. Output: one row in the batch report.
22. **SOLR-2507**, recorded verdict `keep-open` on `solr-2507-draft-open`, evidence head `498a09aea97a`. Output: one row in the batch report.
23. **SOLR-2435**, recorded verdict `close-fixed` on `solr-2435-draft-close`, evidence head `498a09aea9`. Output: one row in the batch report.
24. **SOLR-2326**, recorded verdict `keep-open` on `solr-2326-draft-open`, evidence head `498a09aea97a`. Output: one row in the batch report.
25. **SOLR-2274**, recorded verdict `close-fixed` on `solr-2274-draft-close`, evidence head `498a09aea9`. Output: one row in the batch report.

## Decisions

| Decision | Answer or default | Condition and action | Owner |
| --- | --- | --- | --- |
| Standard | decided: the checks in this assignment, applied ticket by ticket | no sampling, no bulk pass | Nick |
| Own-host items | decided: deferred-own-host, never a pass from the host that did the closeout | the vm1 watch or another host audits it later | Nick |
| Acceptance | decided: the vm1 watch spot-checks at least 5 tickets per batch and imports the results; suspects enter `material/skip-audit/remediation-current.json` through the watch | auditors do not edit queue-current.json or remediation-current.json | Nick |

## Deliverable

`reports/skip-verdict-audit-batch-20.md`: a table with one row per item: ticket, result (pass, suspect, or deferred-own-host), the claim file found or none, the draft branch head, and for suspects exactly what is missing. End with a one-line count of pass, suspect, and deferred.

## Limits

- Audit only: do not edit the audited tickets' claims, drafts, queue records, or verdicts. No PR, comment, JIRA post or email. No force push.
- Commit as Nick Shanin with no trailers. No em dashes.
