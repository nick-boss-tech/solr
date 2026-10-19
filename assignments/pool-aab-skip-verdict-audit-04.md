# Pool assignment: skip verdict cross-audit, batch 4 of 21

Status: CLAIMED (claims/pool-aab-skip-verdict-audit-04.md)

- Capability tags: review, draft.
- Intended host: any pool Linux host. Claim under `claims/` before starting (WORKFLOW.md).
- Posted by: main agent (vm1), 2026-10-19, at the direction of Nick.

Audit each recorded closeout verdict in this batch against the floor in `material/skip-audit/verdict-rubric.md`, the same checks the vm1 honesty watch runs. A verdict passes only if all of these hold: a claim in `claims/` naming the ticket carries a DONE line; the named draft branch exists on the fork and holds `drafts/<KEY>/record.md` and `drafts/<KEY>/jira-comment.md`; the record cites the pinned head it re-checked at and that head matches `evidence_head` in `material/skip-audit/queue-current.json`; the record names the Jira status read in that pass; the record gives a code path with file and line or a reproduction attempt and its result; the record is substantive (about 40 words or more, and not identical to another ticket's record); the verdict is one the rubric allows and the draft branch suffix matches it.

Cross-audit rule: never audit a verdict whose closeout claim or record names your own host as the worker. Mark it `deferred-own-host` in the report and leave it; another host or the vm1 watch takes it. When in doubt, the verdict is suspect and the report names what is missing; a thin or unverifiable pass is worse than a suspect.

## Items

1. **SOLR-10512**, recorded verdict `keep-open` on `solr-10512-draft-open`, evidence head `498a09aea97a`. Output: one row in the batch report.
2. **SOLR-10511**, recorded verdict `close-fixed` on `solr-10511-draft-close`, evidence head `498a09aea97a`. Output: one row in the batch report.
3. **SOLR-10497**, recorded verdict `close-fixed` on `solr-10497-draft-close`, evidence head `498a09aea97a`. Output: one row in the batch report.
4. **SOLR-10407**, recorded verdict `close-obsolete` on `solr-10407-draft-close`, evidence head `498a09aea97a`. Output: one row in the batch report.
5. **SOLR-10395**, recorded verdict `keep-open` on `solr-10395-draft-open`, evidence head `498a09aea97a`. Output: one row in the batch report.
6. **SOLR-10363**, recorded verdict `keep-open` on `solr-10363-draft-open`, evidence head `498a09aea97a`. Output: one row in the batch report.
7. **SOLR-10334**, recorded verdict `keep-open` on `solr-10334-draft-open`, evidence head `498a09aea97a`. Output: one row in the batch report.
8. **SOLR-10332**, recorded verdict `keep-open` on `solr-10332-draft-open`, evidence head `498a09aea97a`. Output: one row in the batch report.
9. **SOLR-10326**, recorded verdict `close-obsolete` on `solr-10326-draft-close`, evidence head `498a09aea97a`. Output: one row in the batch report.
10. **SOLR-10325**, recorded verdict `keep-open` on `solr-10325-draft-open`, evidence head `498a09aea97a`. Output: one row in the batch report.
11. **SOLR-10284**, recorded verdict `keep-open` on `solr-10284-draft-open`, evidence head `498a09aea97a`. Output: one row in the batch report.
12. **SOLR-10256**, recorded verdict `keep-open` on `solr-10256-draft-open`, evidence head `498a09aea97a`. Output: one row in the batch report.
13. **SOLR-10242**, recorded verdict `keep-open` on `solr-10242-draft-open`, evidence head `498a09aea97a`. Output: one row in the batch report.
14. **SOLR-10201**, recorded verdict `keep-open` on `solr-10201-draft-open`, evidence head `498a09aea97a`. Output: one row in the batch report.
15. **SOLR-10197**, recorded verdict `keep-open` on `solr-10197-draft-open`, evidence head `498a09aea97a`. Output: one row in the batch report.
16. **SOLR-10112**, recorded verdict `keep-open` on `solr-10112-draft-open`, evidence head `498a09aea97a`. Output: one row in the batch report.
17. **SOLR-10110**, recorded verdict `keep-open` on `solr-10110-draft-open`, evidence head `498a09aea97a`. Output: one row in the batch report.
18. **SOLR-10096**, recorded verdict `close-answered` on `solr-10096-draft-close`, evidence head `498a09aea97a`. Output: one row in the batch report.
19. **SOLR-10084**, recorded verdict `keep-open` on `solr-10084-draft-open`, evidence head `498a09aea97a`. Output: one row in the batch report.
20. **SOLR-10041**, recorded verdict `close-obsolete` on `solr-10041-draft-close`, evidence head `498a09aea97a`. Output: one row in the batch report.
21. **SOLR-10030**, recorded verdict `keep-open` on `solr-10030-draft-open`, evidence head `498a09aea97a`. Output: one row in the batch report.
22. **SOLR-10022**, recorded verdict `close-obsolete` on `solr-10022-draft-close`, evidence head `498a09aea97a`. Output: one row in the batch report.
23. **SOLR-10016**, recorded verdict `keep-open` on `solr-10016-draft-open`, evidence head `498a09aea97a`. Output: one row in the batch report.
24. **SOLR-10010**, recorded verdict `keep-open` on `solr-10010-draft-open`, evidence head `498a09aea97a`. Output: one row in the batch report.
25. **SOLR-9978**, recorded verdict `keep-open` on `solr-9978-draft-open`, evidence head `498a09aea97a`. Output: one row in the batch report.

## Decisions

| Decision | Answer or default | Condition and action | Owner |
| --- | --- | --- | --- |
| Standard | decided: the checks in this assignment, applied ticket by ticket | no sampling, no bulk pass | Nick |
| Own-host items | decided: deferred-own-host, never a pass from the host that did the closeout | the vm1 watch or another host audits it later | Nick |
| Acceptance | decided: the vm1 watch spot-checks at least 5 tickets per batch and imports the results; suspects enter `material/skip-audit/remediation-current.json` through the watch | auditors do not edit queue-current.json or remediation-current.json | Nick |

## Deliverable

`reports/skip-verdict-audit-batch-04.md`: a table with one row per item: ticket, result (pass, suspect, or deferred-own-host), the claim file found or none, the draft branch head, and for suspects exactly what is missing. End with a one-line count of pass, suspect, and deferred.

## Limits

- Audit only: do not edit the audited tickets' claims, drafts, queue records, or verdicts. No PR, comment, JIRA post or email. No force push.
- Commit as Nick Shanin with no trailers. No em dashes.
