# Pool assignment: skip verdict cross-audit, batch 19 of 21

Status: CLAIMED (claims/pool-aab-skip-verdict-audit-19.md)

- Capability tags: review, draft.
- Intended host: any pool Linux host. Claim under `claims/` before starting (WORKFLOW.md).
- Posted by: main agent (vm1), 2026-10-19, at the direction of Nick.

Audit each recorded closeout verdict in this batch against the floor in `material/skip-audit/verdict-rubric.md`, the same checks the vm1 honesty watch runs. A verdict passes only if all of these hold: a claim in `claims/` naming the ticket carries a DONE line; the named draft branch exists on the fork and holds `drafts/<KEY>/record.md` and `drafts/<KEY>/jira-comment.md`; the record cites the pinned head it re-checked at and that head matches `evidence_head` in `material/skip-audit/queue-current.json`; the record names the Jira status read in that pass; the record gives a code path with file and line or a reproduction attempt and its result; the record is substantive (about 40 words or more, and not identical to another ticket's record); the verdict is one the rubric allows and the draft branch suffix matches it.

Cross-audit rule: never audit a verdict whose closeout claim or record names your own host as the worker. Mark it `deferred-own-host` in the report and leave it; another host or the vm1 watch takes it. When in doubt, the verdict is suspect and the report names what is missing; a thin or unverifiable pass is worse than a suspect.

## Items

1. **SOLR-3765**, recorded verdict `keep-open` on `solr-3765-draft-open`, evidence head `498a09aea97a`. Output: one row in the batch report.
2. **SOLR-3744**, recorded verdict `keep-open` on `solr-3744-draft-open`, evidence head `498a09aea97a`. Output: one row in the batch report.
3. **SOLR-3742**, recorded verdict `keep-open` on `solr-3742-draft-open`, evidence head `498a09aea97a`. Output: one row in the batch report.
4. **SOLR-3741**, recorded verdict `close-fixed` on `solr-3741-draft-close`, evidence head `498a09aea97a`. Output: one row in the batch report.
5. **SOLR-3740**, recorded verdict `close-fixed` on `solr-3740-draft-close`, evidence head `498a09aea97a`. Output: one row in the batch report.
6. **SOLR-3739**, recorded verdict `close-fixed` on `solr-3739-draft-close`, evidence head `498a09aea97a`. Output: one row in the batch report.
7. **SOLR-3721**, recorded verdict `keep-open` on `solr-3721-draft-open`, evidence head `498a09aea97a`. Output: one row in the batch report.
8. **SOLR-3716**, recorded verdict `keep-open` on `solr-3716-draft-open`, evidence head `498a09aea97a`. Output: one row in the batch report.
9. **SOLR-3697**, recorded verdict `close-fixed` on `solr-3697-draft-close`, evidence head `498a09aea9`. Output: one row in the batch report.
10. **SOLR-3694**, recorded verdict `close-fixed` on `solr-3694-draft-close`, evidence head `498a09aea9`. Output: one row in the batch report.
11. **SOLR-3693**, recorded verdict `close-fixed` on `solr-3693-draft-close`, evidence head `498a09aea9`. Output: one row in the batch report.
12. **SOLR-3692**, recorded verdict `keep-open` on `solr-3692-draft-open`, evidence head `498a09aea97a`. Output: one row in the batch report.
13. **SOLR-3676**, recorded verdict `keep-open` on `solr-3676-draft-open`, evidence head `498a09aea97a`. Output: one row in the batch report.
14. **SOLR-3605**, recorded verdict `keep-open` on `solr-3605-draft-open`, evidence head `498a09aea97a`. Output: one row in the batch report.
15. **SOLR-3576**, recorded verdict `keep-open` on `solr-3576-draft-open`, evidence head `498a09aea97a`. Output: one row in the batch report.
16. **SOLR-3539**, recorded verdict `keep-open` on `solr-3539-draft-open`, evidence head `498a09aea97a`. Output: one row in the batch report.
17. **SOLR-3523**, recorded verdict `keep-open` on `solr-3523-draft-open`, evidence head `498a09aea97a`. Output: one row in the batch report.
18. **SOLR-3473**, recorded verdict `keep-open` on `solr-3473-draft-open`, evidence head `498a09aea97a`. Output: one row in the batch report.
19. **SOLR-3461**, recorded verdict `keep-open` on `solr-3461-draft-open`, evidence head `498a09aea97a`. Output: one row in the batch report.
20. **SOLR-3452**, recorded verdict `keep-open` on `solr-3452-draft-open`, evidence head `498a09aea97a`. Output: one row in the batch report.
21. **SOLR-3447**, recorded verdict `keep-open` on `solr-3447-draft-open`, evidence head `498a09aea97a`. Output: one row in the batch report.
22. **SOLR-3408**, recorded verdict `close-fixed` on `solr-3408-draft-close`, evidence head `498a09aea9`. Output: one row in the batch report.
23. **SOLR-3407**, recorded verdict `keep-open` on `solr-3407-draft-open`, evidence head `498a09aea97a`. Output: one row in the batch report.
24. **SOLR-3380**, recorded verdict `keep-open` on `solr-3380-draft-open`, evidence head `498a09aea97a`. Output: one row in the batch report.
25. **SOLR-3220**, recorded verdict `keep-open` on `solr-3220-draft-open`, evidence head `498a09aea97a`. Output: one row in the batch report.

## Decisions

| Decision | Answer or default | Condition and action | Owner |
| --- | --- | --- | --- |
| Standard | decided: the checks in this assignment, applied ticket by ticket | no sampling, no bulk pass | Nick |
| Own-host items | decided: deferred-own-host, never a pass from the host that did the closeout | the vm1 watch or another host audits it later | Nick |
| Acceptance | decided: the vm1 watch spot-checks at least 5 tickets per batch and imports the results; suspects enter `material/skip-audit/remediation-current.json` through the watch | auditors do not edit queue-current.json or remediation-current.json | Nick |

## Deliverable

`reports/skip-verdict-audit-batch-19.md`: a table with one row per item: ticket, result (pass, suspect, or deferred-own-host), the claim file found or none, the draft branch head, and for suspects exactly what is missing. End with a one-line count of pass, suspect, and deferred.

## Limits

- Audit only: do not edit the audited tickets' claims, drafts, queue records, or verdicts. No PR, comment, JIRA post or email. No force push.
- Commit as Nick Shanin with no trailers. No em dashes.
