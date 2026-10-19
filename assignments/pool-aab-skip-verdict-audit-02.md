# Pool assignment: skip verdict cross-audit, batch 2 of 21

Status: OPEN

- Capability tags: review, draft.
- Intended host: any pool Linux host. Claim under `claims/` before starting (WORKFLOW.md).
- Posted by: main agent (vm1), 2026-10-19, at the direction of Nick.

Audit each recorded closeout verdict in this batch against the floor in `material/skip-audit/verdict-rubric.md`, the same checks the vm1 honesty watch runs. A verdict passes only if all of these hold: a claim in `claims/` naming the ticket carries a DONE line; the named draft branch exists on the fork and holds `drafts/<KEY>/record.md` and `drafts/<KEY>/jira-comment.md`; the record cites the pinned head it re-checked at and that head matches `evidence_head` in `material/skip-audit/queue-current.json`; the record names the Jira status read in that pass; the record gives a code path with file and line or a reproduction attempt and its result; the record is substantive (about 40 words or more, and not identical to another ticket's record); the verdict is one the rubric allows and the draft branch suffix matches it.

Cross-audit rule: never audit a verdict whose closeout claim or record names your own host as the worker. Mark it `deferred-own-host` in the report and leave it; another host or the vm1 watch takes it. When in doubt, the verdict is suspect and the report names what is missing; a thin or unverifiable pass is worse than a suspect.

## Items

1. **SOLR-11378**, recorded verdict `keep-open` on `solr-11378-draft-open`, evidence head `498a09aea97a`. Output: one row in the batch report.
2. **SOLR-11353**, recorded verdict `keep-open` on `solr-11353-draft-open`, evidence head `498a09aea97a`. Output: one row in the batch report.
3. **SOLR-11301**, recorded verdict `keep-open` on `solr-11301-draft-open`, evidence head `498a09aea97a`. Output: one row in the batch report.
4. **SOLR-11292**, recorded verdict `keep-open` on `solr-11292-draft-open`, evidence head `498a09aea97a`. Output: one row in the batch report.
5. **SOLR-11290**, recorded verdict `keep-open` on `solr-11290-draft-open`, evidence head `498a09aea97a`. Output: one row in the batch report.
6. **SOLR-11274**, recorded verdict `keep-open` on `solr-11274-draft-open`, evidence head `498a09aea97a`. Output: one row in the batch report.
7. **SOLR-11269**, recorded verdict `keep-open` on `solr-11269-draft-open`, evidence head `498a09aea97a`. Output: one row in the batch report.
8. **SOLR-11259**, recorded verdict `close-obsolete` on `solr-11259-draft-close`, evidence head `498a09aea97a`. Output: one row in the batch report.
9. **SOLR-11247**, recorded verdict `keep-open` on `solr-11247-draft-open`, evidence head `498a09aea97a`. Output: one row in the batch report.
10. **SOLR-11246**, recorded verdict `close-fixed` on `solr-11246-draft-close`, evidence head `498a09aea97a`. Output: one row in the batch report.
11. **SOLR-11214**, recorded verdict `keep-open` on `solr-11214-draft-open`, evidence head `498a09aea97a`. Output: one row in the batch report.
12. **SOLR-11208**, recorded verdict `keep-open` on `solr-11208-draft-open`, evidence head `498a09aea97a`. Output: one row in the batch report.
13. **SOLR-11193**, recorded verdict `keep-open` on `solr-11193-draft-open`, evidence head `498a09aea97a`. Output: one row in the batch report.
14. **SOLR-11175**, recorded verdict `close-answered` on `solr-11175-draft-close`, evidence head `498a09aea97a`. Output: one row in the batch report.
15. **SOLR-11168**, recorded verdict `keep-open` on `solr-11168-draft-open`, evidence head `498a09aea97a`. Output: one row in the batch report.
16. **SOLR-11150**, recorded verdict `keep-open` on `solr-11150-draft-open`, evidence head `498a09aea97a`. Output: one row in the batch report.
17. **SOLR-11139**, recorded verdict `keep-open` on `solr-11139-draft-open`, evidence head `498a09aea97a`. Output: one row in the batch report.
18. **SOLR-11083**, recorded verdict `keep-open` on `solr-11083-draft-open`, evidence head `498a09aea97a`. Output: one row in the batch report.
19. **SOLR-11058**, recorded verdict `keep-open` on `solr-11058-draft-open`, evidence head `498a09aea97a`. Output: one row in the batch report.
20. **SOLR-11034**, recorded verdict `keep-open` on `solr-11034-draft-open`, evidence head `498a09aea97a`. Output: one row in the batch report.
21. **SOLR-11009**, recorded verdict `close-fixed` on `solr-11009-draft-close`, evidence head `498a09aea97a`. Output: one row in the batch report.
22. **SOLR-11008**, recorded verdict `close-fixed` on `solr-11008-draft-close`, evidence head `498a09aea9`. Output: one row in the batch report.
23. **SOLR-10982**, recorded verdict `keep-open` on `solr-10982-draft-open`, evidence head `498a09aea97a`. Output: one row in the batch report.
24. **SOLR-11022**, recorded verdict `keep-open` on `solr-11022-draft-open`, evidence head `498a09aea97a`. Output: one row in the batch report.
25. **SOLR-10976**, recorded verdict `keep-open` on `solr-10976-draft-open`, evidence head `498a09aea97a`. Output: one row in the batch report.

## Decisions

| Decision | Answer or default | Condition and action | Owner |
| --- | --- | --- | --- |
| Standard | decided: the checks in this assignment, applied ticket by ticket | no sampling, no bulk pass | Nick |
| Own-host items | decided: deferred-own-host, never a pass from the host that did the closeout | the vm1 watch or another host audits it later | Nick |
| Acceptance | decided: the vm1 watch spot-checks at least 5 tickets per batch and imports the results; suspects enter `material/skip-audit/remediation-current.json` through the watch | auditors do not edit queue-current.json or remediation-current.json | Nick |

## Deliverable

`reports/skip-verdict-audit-batch-02.md`: a table with one row per item: ticket, result (pass, suspect, or deferred-own-host), the claim file found or none, the draft branch head, and for suspects exactly what is missing. End with a one-line count of pass, suspect, and deferred.

## Limits

- Audit only: do not edit the audited tickets' claims, drafts, queue records, or verdicts. No PR, comment, JIRA post or email. No force push.
- Commit as Nick Shanin with no trailers. No em dashes.
