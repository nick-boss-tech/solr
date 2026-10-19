# Pool assignment: skip verdict cross-audit, batch 3 of 21

Status: CLAIMED (vm5, claims/pool-aab-skip-verdict-audit-03.md, 2026-10-19T15:59:51Z)

- Capability tags: review, draft.
- Intended host: any pool Linux host. Claim under `claims/` before starting (WORKFLOW.md).
- Posted by: main agent (vm1), 2026-10-19, at the direction of Nick.

Audit each recorded closeout verdict in this batch against the floor in `material/skip-audit/verdict-rubric.md`, the same checks the vm1 honesty watch runs. A verdict passes only if all of these hold: a claim in `claims/` naming the ticket carries a DONE line; the named draft branch exists on the fork and holds `drafts/<KEY>/record.md` and `drafts/<KEY>/jira-comment.md`; the record cites the pinned head it re-checked at and that head matches `evidence_head` in `material/skip-audit/queue-current.json`; the record names the Jira status read in that pass; the record gives a code path with file and line or a reproduction attempt and its result; the record is substantive (about 40 words or more, and not identical to another ticket's record); the verdict is one the rubric allows and the draft branch suffix matches it.

Cross-audit rule: never audit a verdict whose closeout claim or record names your own host as the worker. Mark it `deferred-own-host` in the report and leave it; another host or the vm1 watch takes it. When in doubt, the verdict is suspect and the report names what is missing; a thin or unverifiable pass is worse than a suspect.

## Items

1. **SOLR-10974**, recorded verdict `keep-open` on `solr-10974-draft-open`, evidence head `498a09aea97a`. Output: one row in the batch report.
2. **SOLR-10960**, recorded verdict `keep-open` on `solr-10960-draft-open`, evidence head `498a09aea97a`. Output: one row in the batch report.
3. **SOLR-10959**, recorded verdict `close-obsolete` on `solr-10959-draft-close`, evidence head `498a09aea97a`. Output: one row in the batch report.
4. **SOLR-10952**, recorded verdict `keep-open` on `solr-10952-draft-open`, evidence head `498a09aea97a`. Output: one row in the batch report.
5. **SOLR-10936**, recorded verdict `keep-open` on `solr-10936-draft-open`, evidence head `498a09aea97a`. Output: one row in the batch report.
6. **SOLR-10917**, recorded verdict `close-fixed` on `solr-10917-draft-close`, evidence head `498a09aea97a`. Output: one row in the batch report.
7. **SOLR-10904**, recorded verdict `close-obsolete` on `solr-10904-draft-close`, evidence head `498a09aea97a`. Output: one row in the batch report.
8. **SOLR-10896**, recorded verdict `keep-open` on `solr-10896-draft-open`, evidence head `498a09aea97a`. Output: one row in the batch report.
9. **SOLR-10889**, recorded verdict `close-obsolete` on `solr-10889-draft-close`, evidence head `498a09aea97a`. Output: one row in the batch report.
10. **SOLR-10856**, recorded verdict `keep-open` on `solr-10856-draft-open`, evidence head `498a09aea97a`. Output: one row in the batch report.
11. **SOLR-10841**, recorded verdict `keep-open` on `solr-10841-draft-open`, evidence head `498a09aea97a`. Output: one row in the batch report.
12. **SOLR-10806**, recorded verdict `keep-open` on `solr-10806-draft-open`, evidence head `498a09aea97a`. Output: one row in the batch report.
13. **SOLR-10759**, recorded verdict `keep-open` on `solr-10759-draft-open`, evidence head `498a09aea97a`. Output: one row in the batch report.
14. **SOLR-10751**, recorded verdict `keep-open` on `solr-10751-draft-open`, evidence head `498a09aea97a`. Output: one row in the batch report.
15. **SOLR-10726**, recorded verdict `keep-open` on `solr-10726-draft-open`, evidence head `498a09aea97a`. Output: one row in the batch report.
16. **SOLR-10712**, recorded verdict `close-fixed` on `solr-10712-draft-close`, evidence head `498a09aea97a`. Output: one row in the batch report.
17. **SOLR-10709**, recorded verdict `close-fixed` on `solr-10709-draft-close`, evidence head `498a09aea97a`. Output: one row in the batch report.
18. **SOLR-10706**, recorded verdict `keep-open` on `solr-10706-draft-open`, evidence head `498a09aea97a`. Output: one row in the batch report.
19. **SOLR-10650**, recorded verdict `close-fixed` on `solr-10650-draft-close`, evidence head `498a09aea97a`. Output: one row in the batch report.
20. **SOLR-10604**, recorded verdict `keep-open` on `solr-10604-draft-open`, evidence head `498a09aea97a`. Output: one row in the batch report.
21. **SOLR-10601**, recorded verdict `close-fixed` on `solr-10601-draft-close`, evidence head `498a09aea97a`. Output: one row in the batch report.
22. **SOLR-10597**, recorded verdict `keep-open` on `solr-10597-draft-open`, evidence head `498a09aea97a`. Output: one row in the batch report.
23. **SOLR-10592**, recorded verdict `keep-open` on `solr-10592-draft-open`, evidence head `498a09aea97a`. Output: one row in the batch report.
24. **SOLR-10534**, recorded verdict `keep-open` on `solr-10534-draft-open`, evidence head `498a09aea97a`. Output: one row in the batch report.
25. **SOLR-10533**, recorded verdict `keep-open` on `solr-10533-draft-open`, evidence head `498a09aea97a`. Output: one row in the batch report.

## Decisions

| Decision | Answer or default | Condition and action | Owner |
| --- | --- | --- | --- |
| Standard | decided: the checks in this assignment, applied ticket by ticket | no sampling, no bulk pass | Nick |
| Own-host items | decided: deferred-own-host, never a pass from the host that did the closeout | the vm1 watch or another host audits it later | Nick |
| Acceptance | decided: the vm1 watch spot-checks at least 5 tickets per batch and imports the results; suspects enter `material/skip-audit/remediation-current.json` through the watch | auditors do not edit queue-current.json or remediation-current.json | Nick |

## Deliverable

`reports/skip-verdict-audit-batch-03.md`: a table with one row per item: ticket, result (pass, suspect, or deferred-own-host), the claim file found or none, the draft branch head, and for suspects exactly what is missing. End with a one-line count of pass, suspect, and deferred.

## Limits

- Audit only: do not edit the audited tickets' claims, drafts, queue records, or verdicts. No PR, comment, JIRA post or email. No force push.
- Commit as Nick Shanin with no trailers. No em dashes.
