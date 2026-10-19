# Pool assignment: skip verdict cross-audit, batch 10 of 21

Status: OPEN

- Capability tags: review, draft.
- Intended host: any pool Linux host. Claim under `claims/` before starting (WORKFLOW.md).
- Posted by: main agent (vm1), 2026-10-19, at the direction of Nick.

Audit each recorded closeout verdict in this batch against the floor in `material/skip-audit/verdict-rubric.md`, the same checks the vm1 honesty watch runs. A verdict passes only if all of these hold: a claim in `claims/` naming the ticket carries a DONE line; the named draft branch exists on the fork and holds `drafts/<KEY>/record.md` and `drafts/<KEY>/jira-comment.md`; the record cites the pinned head it re-checked at and that head matches `evidence_head` in `material/skip-audit/queue-current.json`; the record names the Jira status read in that pass; the record gives a code path with file and line or a reproduction attempt and its result; the record is substantive (about 40 words or more, and not identical to another ticket's record); the verdict is one the rubric allows and the draft branch suffix matches it.

Cross-audit rule: never audit a verdict whose closeout claim or record names your own host as the worker. Mark it `deferred-own-host` in the report and leave it; another host or the vm1 watch takes it. When in doubt, the verdict is suspect and the report names what is missing; a thin or unverifiable pass is worse than a suspect.

## Items

1. **SOLR-8171**, recorded verdict `keep-open` on `solr-8171-draft-open`, evidence head `498a09aea97a`. Output: one row in the batch report.
2. **SOLR-8158**, recorded verdict `close-obsolete` on `solr-8158-draft-close`, evidence head `498a09aea97a`. Output: one row in the batch report.
3. **SOLR-8117**, recorded verdict `close-obsolete` on `solr-8117-draft-close`, evidence head `498a09aea97a`. Output: one row in the batch report.
4. **SOLR-8106**, recorded verdict `keep-open` on `solr-8106-draft-open`, evidence head `498a09aea97a`. Output: one row in the batch report.
5. **SOLR-8098**, recorded verdict `keep-open` on `solr-8098-draft-open`, evidence head `498a09aea97a`. Output: one row in the batch report.
6. **SOLR-8089**, recorded verdict `close-obsolete` on `solr-8089-draft-close`, evidence head `498a09aea97a`. Output: one row in the batch report.
7. **SOLR-8087**, recorded verdict `close-obsolete` on `solr-8087-draft-close`, evidence head `498a09aea97a`. Output: one row in the batch report.
8. **SOLR-8079**, recorded verdict `keep-open` on `solr-8079-draft-open`, evidence head `498a09aea97a`. Output: one row in the batch report.
9. **SOLR-8076**, recorded verdict `close-obsolete` on `solr-8076-draft-close`, evidence head `498a09aea97a`. Output: one row in the batch report.
10. **SOLR-8067**, recorded verdict `keep-open` on `solr-8067-draft-open`, evidence head `498a09aea97a`. Output: one row in the batch report.
11. **SOLR-8030**, recorded verdict `keep-open` on `solr-8030-draft-open`, evidence head `498a09aea97a`. Output: one row in the batch report.
12. **SOLR-8024**, recorded verdict `close-fixed` on `solr-8024-draft-close`, evidence head `498a09aea97a`. Output: one row in the batch report.
13. **SOLR-8005**, recorded verdict `keep-open` on `solr-8005-draft-open`, evidence head `498a09aea97a`. Output: one row in the batch report.
14. **SOLR-7998**, recorded verdict `keep-open` on `solr-7998-draft-open`, evidence head `498a09aea97a`. Output: one row in the batch report.
15. **SOLR-7992**, recorded verdict `keep-open` on `solr-7992-draft-open`, evidence head `498a09aea97a`. Output: one row in the batch report.
16. **SOLR-7982**, recorded verdict `keep-open` on `solr-7982-draft-open`, evidence head `498a09aea97a`. Output: one row in the batch report.
17. **SOLR-7973**, recorded verdict `close-obsolete` on `solr-7973-draft-close`, evidence head `498a09aea97a`. Output: one row in the batch report.
18. **SOLR-7953**, recorded verdict `close-fixed` on `solr-7953-draft-close`, evidence head `498a09aea97a`. Output: one row in the batch report.
19. **SOLR-7947**, recorded verdict `keep-open` on `solr-7947-draft-open`, evidence head `498a09aea97a`. Output: one row in the batch report.
20. **SOLR-7932**, recorded verdict `keep-open` on `solr-7932-draft-open`, evidence head `498a09aea97a`. Output: one row in the batch report.
21. **SOLR-7927**, recorded verdict `keep-open` on `solr-7927-draft-open`, evidence head `498a09aea97a`. Output: one row in the batch report.
22. **SOLR-7899**, recorded verdict `keep-open` on `solr-7899-draft-open`, evidence head `498a09aea97a`. Output: one row in the batch report.
23. **SOLR-7867**, recorded verdict `keep-open` on `solr-7867-draft-open`, evidence head `498a09aea97a`. Output: one row in the batch report.
24. **SOLR-7846**, recorded verdict `keep-open` on `solr-7846-draft-open`, evidence head `498a09aea97a`. Output: one row in the batch report.
25. **SOLR-7806**, recorded verdict `keep-open` on `solr-7806-draft-open`, evidence head `498a09aea97a`. Output: one row in the batch report.

## Decisions

| Decision | Answer or default | Condition and action | Owner |
| --- | --- | --- | --- |
| Standard | decided: the checks in this assignment, applied ticket by ticket | no sampling, no bulk pass | Nick |
| Own-host items | decided: deferred-own-host, never a pass from the host that did the closeout | the vm1 watch or another host audits it later | Nick |
| Acceptance | decided: the vm1 watch spot-checks at least 5 tickets per batch and imports the results; suspects enter `material/skip-audit/remediation-current.json` through the watch | auditors do not edit queue-current.json or remediation-current.json | Nick |

## Deliverable

`reports/skip-verdict-audit-batch-10.md`: a table with one row per item: ticket, result (pass, suspect, or deferred-own-host), the claim file found or none, the draft branch head, and for suspects exactly what is missing. End with a one-line count of pass, suspect, and deferred.

## Limits

- Audit only: do not edit the audited tickets' claims, drafts, queue records, or verdicts. No PR, comment, JIRA post or email. No force push.
- Commit as Nick Shanin with no trailers. No em dashes.
