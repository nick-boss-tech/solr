# Pool assignment: skip verdict cross-audit, batch 11 of 21

Status: OPEN

- Capability tags: review, draft.
- Intended host: any pool Linux host. Claim under `claims/` before starting (WORKFLOW.md).
- Posted by: main agent (vm1), 2026-10-19, at the direction of Nick.

Audit each recorded closeout verdict in this batch against the floor in `material/skip-audit/verdict-rubric.md`, the same checks the vm1 honesty watch runs. A verdict passes only if all of these hold: a claim in `claims/` naming the ticket carries a DONE line; the named draft branch exists on the fork and holds `drafts/<KEY>/record.md` and `drafts/<KEY>/jira-comment.md`; the record cites the pinned head it re-checked at and that head matches `evidence_head` in `material/skip-audit/queue-current.json`; the record names the Jira status read in that pass; the record gives a code path with file and line or a reproduction attempt and its result; the record is substantive (about 40 words or more, and not identical to another ticket's record); the verdict is one the rubric allows and the draft branch suffix matches it.

Cross-audit rule: never audit a verdict whose closeout claim or record names your own host as the worker. Mark it `deferred-own-host` in the report and leave it; another host or the vm1 watch takes it. When in doubt, the verdict is suspect and the report names what is missing; a thin or unverifiable pass is worse than a suspect.

## Items

1. **SOLR-7804**, recorded verdict `close-fixed` on `solr-7804-draft-close`, evidence head `498a09aea9`. Output: one row in the batch report.
2. **SOLR-7793**, recorded verdict `keep-open` on `solr-7793-draft-open`, evidence head `498a09aea97a`. Output: one row in the batch report.
3. **SOLR-7773**, recorded verdict `close-fixed` on `solr-7773-draft-close`, evidence head `498a09aea97a`. Output: one row in the batch report.
4. **SOLR-7758**, recorded verdict `keep-open` on `solr-7758-draft-open`, evidence head `498a09aea97a`. Output: one row in the batch report.
5. **SOLR-7721**, recorded verdict `keep-open` on `solr-7721-draft-open`, evidence head `498a09aea97a`. Output: one row in the batch report.
6. **SOLR-7646**, recorded verdict `keep-open` on `solr-7646-draft-open`, evidence head `498a09aea97a`. Output: one row in the batch report.
7. **SOLR-7612**, recorded verdict `close-obsolete` on `solr-7612-draft-close`, evidence head `498a09aea97a`. Output: one row in the batch report.
8. **SOLR-7606**, recorded verdict `keep-open` on `solr-7606-draft-open`, evidence head `498a09aea97a`. Output: one row in the batch report.
9. **SOLR-7597**, recorded verdict `keep-open` on `solr-7597-draft-open`, evidence head `498a09aea97a`. Output: one row in the batch report.
10. **SOLR-7559**, recorded verdict `close-fixed` on `solr-7559-draft-close`, evidence head `498a09aea97a`. Output: one row in the batch report.
11. **SOLR-7536**, recorded verdict `keep-open` on `solr-7536-draft-open`, evidence head `498a09aea97a`. Output: one row in the batch report.
12. **SOLR-7526**, recorded verdict `keep-open` on `solr-7526-draft-open`, evidence head `498a09aea97a`. Output: one row in the batch report.
13. **SOLR-7521**, recorded verdict `keep-open` on `solr-7521-draft-open`, evidence head `498a09aea97a`. Output: one row in the batch report.
14. **SOLR-7517**, recorded verdict `close-fixed` on `solr-7517-draft-close`, evidence head `498a09aea97a`. Output: one row in the batch report.
15. **SOLR-7511**, recorded verdict `keep-open` on `solr-7511-draft-open`, evidence head `498a09aea97a`. Output: one row in the batch report.
16. **SOLR-7496**, recorded verdict `keep-open` on `solr-7496-draft-open`, evidence head `498a09aea97a`. Output: one row in the batch report.
17. **SOLR-7482**, recorded verdict `keep-open` on `solr-7482-draft-open`, evidence head `498a09aea97a`. Output: one row in the batch report.
18. **SOLR-7427**, recorded verdict `keep-open` on `solr-7427-draft-open`, evidence head `498a09aea97a`. Output: one row in the batch report.
19. **SOLR-7415**, recorded verdict `keep-open` on `solr-7415-draft-open`, evidence head `498a09aea97a`. Output: one row in the batch report.
20. **SOLR-7404**, recorded verdict `keep-open` on `solr-7404-draft-open`, evidence head `498a09aea97a`. Output: one row in the batch report.
21. **SOLR-7363**, recorded verdict `close-answered` on `solr-7363-draft-close`, evidence head `498a09aea97a`. Output: one row in the batch report.
22. **SOLR-7352**, recorded verdict `close-fixed` on `solr-7352-draft-close`, evidence head `498a09aea97a`. Output: one row in the batch report.
23. **SOLR-7347**, recorded verdict `keep-open` on `solr-7347-draft-open`, evidence head `498a09aea97a`. Output: one row in the batch report.
24. **SOLR-7343**, recorded verdict `keep-open` on `solr-7343-draft-open`, evidence head `498a09aea97a`. Output: one row in the batch report.
25. **SOLR-7295**, recorded verdict `keep-open` on `solr-7295-draft-open`, evidence head `498a09aea97a`. Output: one row in the batch report.

## Decisions

| Decision | Answer or default | Condition and action | Owner |
| --- | --- | --- | --- |
| Standard | decided: the checks in this assignment, applied ticket by ticket | no sampling, no bulk pass | Nick |
| Own-host items | decided: deferred-own-host, never a pass from the host that did the closeout | the vm1 watch or another host audits it later | Nick |
| Acceptance | decided: the vm1 watch spot-checks at least 5 tickets per batch and imports the results; suspects enter `material/skip-audit/remediation-current.json` through the watch | auditors do not edit queue-current.json or remediation-current.json | Nick |

## Deliverable

`reports/skip-verdict-audit-batch-11.md`: a table with one row per item: ticket, result (pass, suspect, or deferred-own-host), the claim file found or none, the draft branch head, and for suspects exactly what is missing. End with a one-line count of pass, suspect, and deferred.

## Limits

- Audit only: do not edit the audited tickets' claims, drafts, queue records, or verdicts. No PR, comment, JIRA post or email. No force push.
- Commit as Nick Shanin with no trailers. No em dashes.
