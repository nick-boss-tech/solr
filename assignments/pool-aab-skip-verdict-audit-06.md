# Pool assignment: skip verdict cross-audit, batch 6 of 21

Status: DONE (vm5, claims/pool-aab-skip-verdict-audit-06.md). Deliverable: reports/skip-verdict-audit-batch-06.md, 0 pass, 25 suspect, 0 deferred-own-host.

- Capability tags: review, draft.
- Intended host: any pool Linux host. Claim under `claims/` before starting (WORKFLOW.md).
- Posted by: main agent (vm1), 2026-10-19, at the direction of Nick.

Audit each recorded closeout verdict in this batch against the floor in `material/skip-audit/verdict-rubric.md`, the same checks the vm1 honesty watch runs. A verdict passes only if all of these hold: a claim in `claims/` naming the ticket carries a DONE line; the named draft branch exists on the fork and holds `drafts/<KEY>/record.md` and `drafts/<KEY>/jira-comment.md`; the record cites the pinned head it re-checked at and that head matches `evidence_head` in `material/skip-audit/queue-current.json`; the record names the Jira status read in that pass; the record gives a code path with file and line or a reproduction attempt and its result; the record is substantive (about 40 words or more, and not identical to another ticket's record); the verdict is one the rubric allows and the draft branch suffix matches it.

Cross-audit rule: never audit a verdict whose closeout claim or record names your own host as the worker. Mark it `deferred-own-host` in the report and leave it; another host or the vm1 watch takes it. When in doubt, the verdict is suspect and the report names what is missing; a thin or unverifiable pass is worse than a suspect.

## Items

1. **SOLR-9648**, recorded verdict `keep-open` on `solr-9648-draft-open`, evidence head `498a09aea97a`. Output: one row in the batch report.
2. **SOLR-9645**, recorded verdict `keep-open` on `solr-9645-draft-open`, evidence head `498a09aea97a`. Output: one row in the batch report.
3. **SOLR-9622**, recorded verdict `keep-open` on `solr-9622-draft-open`, evidence head `498a09aea97a`. Output: one row in the batch report.
4. **SOLR-9599**, recorded verdict `keep-open` on `solr-9599-draft-open`, evidence head `498a09aea97a`. Output: one row in the batch report.
5. **SOLR-9594**, recorded verdict `keep-open` on `solr-9594-draft-open`, evidence head `498a09aea97a`. Output: one row in the batch report.
6. **SOLR-9593**, recorded verdict `keep-open` on `solr-9593-draft-open`, evidence head `498a09aea97a`. Output: one row in the batch report.
7. **SOLR-9578**, recorded verdict `keep-open` on `solr-9578-draft-open`, evidence head `498a09aea97a`. Output: one row in the batch report.
8. **SOLR-9573**, recorded verdict `keep-open` on `solr-9573-draft-open`, evidence head `498a09aea97a`. Output: one row in the batch report.
9. **SOLR-9571**, recorded verdict `keep-open` on `solr-9571-draft-open`, evidence head `498a09aea97a`. Output: one row in the batch report.
10. **SOLR-9550**, recorded verdict `keep-open` on `solr-9550-draft-open`, evidence head `498a09aea97a`. Output: one row in the batch report.
11. **SOLR-9514**, recorded verdict `keep-open` on `solr-9514-draft-open`, evidence head `498a09aea97a`. Output: one row in the batch report.
12. **SOLR-9492**, recorded verdict `keep-open` on `solr-9492-draft-open`, evidence head `498a09aea97a`. Output: one row in the batch report.
13. **SOLR-9477**, recorded verdict `keep-open` on `solr-9477-draft-open`, evidence head `498a09aea97a`. Output: one row in the batch report.
14. **SOLR-9468**, recorded verdict `close-fixed` on `solr-9468-draft-close`, evidence head `498a09aea97a`. Output: one row in the batch report.
15. **SOLR-9428**, recorded verdict `keep-open` on `solr-9428-draft-open`, evidence head `498a09aea97a`. Output: one row in the batch report.
16. **SOLR-9427**, recorded verdict `keep-open` on `solr-9427-draft-open`, evidence head `498a09aea97a`. Output: one row in the batch report.
17. **SOLR-9426**, recorded verdict `keep-open` on `solr-9426-draft-open`, evidence head `498a09aea97a`. Output: one row in the batch report.
18. **SOLR-9400**, recorded verdict `close-obsolete` on `solr-9400-draft-close`, evidence head `498a09aea97a`. Output: one row in the batch report.
19. **SOLR-9398**, recorded verdict `keep-open` on `solr-9398-draft-open`, evidence head `498a09aea97a`. Output: one row in the batch report.
20. **SOLR-9390**, recorded verdict `keep-open` on `solr-9390-draft-open`, evidence head `498a09aea97a`. Output: one row in the batch report.
21. **SOLR-9369**, recorded verdict `keep-open` on `solr-9369-draft-open`, evidence head `498a09aea97a`. Output: one row in the batch report.
22. **SOLR-9366**, recorded verdict `close-obsolete` on `solr-9366-draft-close`, evidence head `498a09aea97a`. Output: one row in the batch report.
23. **SOLR-9361**, recorded verdict `keep-open` on `solr-9361-draft-open`, evidence head `498a09aea97a`. Output: one row in the batch report.
24. **SOLR-9352**, recorded verdict `keep-open` on `solr-9352-draft-open`, evidence head `498a09aea97a`. Output: one row in the batch report.
25. **SOLR-9329**, recorded verdict `keep-open` on `solr-9329-draft-open`, evidence head `498a09aea97a`. Output: one row in the batch report.

## Decisions

| Decision | Answer or default | Condition and action | Owner |
| --- | --- | --- | --- |
| Standard | decided: the checks in this assignment, applied ticket by ticket | no sampling, no bulk pass | Nick |
| Own-host items | decided: deferred-own-host, never a pass from the host that did the closeout | the vm1 watch or another host audits it later | Nick |
| Acceptance | decided: the vm1 watch spot-checks at least 5 tickets per batch and imports the results; suspects enter `material/skip-audit/remediation-current.json` through the watch | auditors do not edit queue-current.json or remediation-current.json | Nick |

## Deliverable

`reports/skip-verdict-audit-batch-06.md`: a table with one row per item: ticket, result (pass, suspect, or deferred-own-host), the claim file found or none, the draft branch head, and for suspects exactly what is missing. End with a one-line count of pass, suspect, and deferred.

## Limits

- Audit only: do not edit the audited tickets' claims, drafts, queue records, or verdicts. No PR, comment, JIRA post or email. No force push.
- Commit as Nick Shanin with no trailers. No em dashes.
