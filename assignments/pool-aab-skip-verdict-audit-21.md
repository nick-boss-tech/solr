# Pool assignment: skip verdict cross-audit, batch 21 of 21

Status: DONE (vm5, claims/pool-aab-skip-verdict-audit-21.md). Deliverable: reports/skip-verdict-audit-batch-21.md, 3 pass, 1 suspect, 2 deferred-own-host.

- Capability tags: review, draft.
- Intended host: any pool Linux host. Claim under `claims/` before starting (WORKFLOW.md).
- Posted by: main agent (vm1), 2026-10-19, at the direction of Nick.

Audit each recorded closeout verdict in this batch against the floor in `material/skip-audit/verdict-rubric.md`, the same checks the vm1 honesty watch runs. A verdict passes only if all of these hold: a claim in `claims/` naming the ticket carries a DONE line; the named draft branch exists on the fork and holds `drafts/<KEY>/record.md` and `drafts/<KEY>/jira-comment.md`; the record cites the pinned head it re-checked at and that head matches `evidence_head` in `material/skip-audit/queue-current.json`; the record names the Jira status read in that pass; the record gives a code path with file and line or a reproduction attempt and its result; the record is substantive (about 40 words or more, and not identical to another ticket's record); the verdict is one the rubric allows and the draft branch suffix matches it.

Cross-audit rule: never audit a verdict whose closeout claim or record names your own host as the worker. Mark it `deferred-own-host` in the report and leave it; another host or the vm1 watch takes it. When in doubt, the verdict is suspect and the report names what is missing; a thin or unverifiable pass is worse than a suspect.

## Items

1. **SOLR-1785**, recorded verdict `keep-open` on `solr-1785-draft-open`, evidence head `498a09aea97a`. Output: one row in the batch report.
2. **SOLR-1780**, recorded verdict `keep-open` on `solr-1780-draft-open`, evidence head `498a09aea97a`. Output: one row in the batch report.
3. **SOLR-1658**, recorded verdict `close-fixed` on `solr-1658-draft-close`, evidence head `498a09aea97a`. Output: one row in the batch report.
4. **SOLR-1557**, recorded verdict `keep-open` on `solr-1557-draft-open`, evidence head `498a09aea97a`. Output: one row in the batch report.
5. **SOLR-1542**, recorded verdict `keep-open` on `solr-1542-draft-open`, evidence head `498a09aea97a`. Output: one row in the batch report.
6. **SOLR-1247**, recorded verdict `keep-open` on `solr-1247-draft-open`, evidence head `498a09aea97a`. Output: one row in the batch report.

## Decisions

| Decision | Answer or default | Condition and action | Owner |
| --- | --- | --- | --- |
| Standard | decided: the checks in this assignment, applied ticket by ticket | no sampling, no bulk pass | Nick |
| Own-host items | decided: deferred-own-host, never a pass from the host that did the closeout | the vm1 watch or another host audits it later | Nick |
| Acceptance | decided: the vm1 watch spot-checks at least 5 tickets per batch and imports the results; suspects enter `material/skip-audit/remediation-current.json` through the watch | auditors do not edit queue-current.json or remediation-current.json | Nick |

## Deliverable

`reports/skip-verdict-audit-batch-21.md`: a table with one row per item: ticket, result (pass, suspect, or deferred-own-host), the claim file found or none, the draft branch head, and for suspects exactly what is missing. End with a one-line count of pass, suspect, and deferred.

## Limits

- Audit only: do not edit the audited tickets' claims, drafts, queue records, or verdicts. No PR, comment, JIRA post or email. No force push.
- Commit as Nick Shanin with no trailers. No em dashes.
