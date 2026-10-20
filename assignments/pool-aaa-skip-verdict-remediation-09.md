# Pool assignment: skip verdict remediation, batch 9

Status: DONE (vm2, claims/pool-aaa-skip-verdict-remediation-09.md, closed 2026-10-20T08:27:25Z)

- Capability tags: review, draft.
- Intended host: any pool Linux host. Claim under `claims/` before starting (WORKFLOW.md).
- Posted by: main agent (vm1), 2026-10-19, at the direction of Nick.

The vm1 honesty audit marked every verdict in this batch suspect: the recorded verdict in `material/skip-audit/queue-current.json` rests on an earlier audit or reading, Jira was not re-read for the closeout, and the ticket was not re-checked at the pinned head. A verdict in this batch is provisional, not done. The full reasons per ticket are in `material/skip-audit/remediation-current.json` and in `reports/skip-verdict-audit.md`.

For each ticket, in the order listed: claim it as `claims/skip-<KEY>-<your host>.md`, then redo the closeout under steps 11 to 15 of `pool/cron-prompt.md` at the pinned head `498a09aea97aac86c2cc7ac114113b946d249942`. The earlier audit, B2, B3 or C reading is background only. The new record must cite the Jira status read in this pass, the code path with file and line or the reproduction attempt and its result, and the pinned head. Update the two draft files on the draft branch with a normal push, record the verdict with `queue_tool.py set`, and mark the claim DONE naming the remediation batch and the new draft branch head.

## Items

1. **SOLR-12236**, recorded verdict `close-fixed` on `solr-12236-draft-close`. Start from: `material/skip-audit/remediation-current.json` (item SOLR-12236) and `reports/skip-verdict-audit.md`. Output: `claims/skip-SOLR-12236-<host>.md` DONE, updated drafts on the branch, and one line in the batch report.
2. **SOLR-12234**, recorded verdict `keep-open` on `solr-12234-draft-open`. Start from: `material/skip-audit/remediation-current.json` (item SOLR-12234) and `reports/skip-verdict-audit.md`. Output: `claims/skip-SOLR-12234-<host>.md` DONE, updated drafts on the branch, and one line in the batch report.
3. **SOLR-12220**, recorded verdict `keep-open` on `solr-12220-draft-open`. Start from: `material/skip-audit/remediation-current.json` (item SOLR-12220) and `reports/skip-verdict-audit.md`. Output: `claims/skip-SOLR-12220-<host>.md` DONE, updated drafts on the branch, and one line in the batch report.
4. **SOLR-12166**, recorded verdict `keep-open` on `solr-12166-draft-open`. Start from: `material/skip-audit/remediation-current.json` (item SOLR-12166) and `reports/skip-verdict-audit.md`. Output: `claims/skip-SOLR-12166-<host>.md` DONE, updated drafts on the branch, and one line in the batch report.
5. **SOLR-12112**, recorded verdict `keep-open` on `solr-12112-draft-open`. Start from: `material/skip-audit/remediation-current.json` (item SOLR-12112) and `reports/skip-verdict-audit.md`. Output: `claims/skip-SOLR-12112-<host>.md` DONE, updated drafts on the branch, and one line in the batch report.
6. **SOLR-12093**, recorded verdict `keep-open` on `solr-12093-draft-open`. Start from: `material/skip-audit/remediation-current.json` (item SOLR-12093) and `reports/skip-verdict-audit.md`. Output: `claims/skip-SOLR-12093-<host>.md` DONE, updated drafts on the branch, and one line in the batch report.
7. **SOLR-12029**, recorded verdict `keep-open` on `solr-12029-draft-open`. Start from: `material/skip-audit/remediation-current.json` (item SOLR-12029) and `reports/skip-verdict-audit.md`. Output: `claims/skip-SOLR-12029-<host>.md` DONE, updated drafts on the branch, and one line in the batch report.
8. **SOLR-11983**, recorded verdict `keep-open` on `solr-11983-draft-open`. Start from: `material/skip-audit/remediation-current.json` (item SOLR-11983) and `reports/skip-verdict-audit.md`. Output: `claims/skip-SOLR-11983-<host>.md` DONE, updated drafts on the branch, and one line in the batch report.
9. **SOLR-11955**, recorded verdict `close-obsolete` on `solr-11955-draft-close`. Start from: `material/skip-audit/remediation-current.json` (item SOLR-11955) and `reports/skip-verdict-audit.md`. Output: `claims/skip-SOLR-11955-<host>.md` DONE, updated drafts on the branch, and one line in the batch report.
10. **SOLR-11954**, recorded verdict `keep-open` on `solr-11954-draft-open`. Start from: `material/skip-audit/remediation-current.json` (item SOLR-11954) and `reports/skip-verdict-audit.md`. Output: `claims/skip-SOLR-11954-<host>.md` DONE, updated drafts on the branch, and one line in the batch report.
11. **SOLR-11932**, recorded verdict `keep-open` on `solr-11932-draft-open`. Start from: `material/skip-audit/remediation-current.json` (item SOLR-11932) and `reports/skip-verdict-audit.md`. Output: `claims/skip-SOLR-11932-<host>.md` DONE, updated drafts on the branch, and one line in the batch report.
12. **SOLR-11927**, recorded verdict `keep-open` on `solr-11927-draft-open`. Start from: `material/skip-audit/remediation-current.json` (item SOLR-11927) and `reports/skip-verdict-audit.md`. Output: `claims/skip-SOLR-11927-<host>.md` DONE, updated drafts on the branch, and one line in the batch report.
13. **SOLR-11864**, recorded verdict `close-fixed` on `solr-11864-draft-close`. Start from: `material/skip-audit/remediation-current.json` (item SOLR-11864) and `reports/skip-verdict-audit.md`. Output: `claims/skip-SOLR-11864-<host>.md` DONE, updated drafts on the branch, and one line in the batch report.
14. **SOLR-11858**, recorded verdict `keep-open` on `solr-11858-draft-open`. Start from: `material/skip-audit/remediation-current.json` (item SOLR-11858) and `reports/skip-verdict-audit.md`. Output: `claims/skip-SOLR-11858-<host>.md` DONE, updated drafts on the branch, and one line in the batch report.
15. **SOLR-11827**, recorded verdict `keep-open` on `solr-11827-draft-open`. Start from: `material/skip-audit/remediation-current.json` (item SOLR-11827) and `reports/skip-verdict-audit.md`. Output: `claims/skip-SOLR-11827-<host>.md` DONE, updated drafts on the branch, and one line in the batch report.
16. **SOLR-11826**, recorded verdict `keep-open` on `solr-11826-draft-open`. Start from: `material/skip-audit/remediation-current.json` (item SOLR-11826) and `reports/skip-verdict-audit.md`. Output: `claims/skip-SOLR-11826-<host>.md` DONE, updated drafts on the branch, and one line in the batch report.
17. **SOLR-11816**, recorded verdict `keep-open` on `solr-11816-draft-open`. Start from: `material/skip-audit/remediation-current.json` (item SOLR-11816) and `reports/skip-verdict-audit.md`. Output: `claims/skip-SOLR-11816-<host>.md` DONE, updated drafts on the branch, and one line in the batch report.
18. **SOLR-11815**, recorded verdict `keep-open` on `solr-11815-draft-open`. Start from: `material/skip-audit/remediation-current.json` (item SOLR-11815) and `reports/skip-verdict-audit.md`. Output: `claims/skip-SOLR-11815-<host>.md` DONE, updated drafts on the branch, and one line in the batch report.
19. **SOLR-11814**, recorded verdict `keep-open` on `solr-11814-draft-open`. Start from: `material/skip-audit/remediation-current.json` (item SOLR-11814) and `reports/skip-verdict-audit.md`. Output: `claims/skip-SOLR-11814-<host>.md` DONE, updated drafts on the branch, and one line in the batch report.
20. **SOLR-11787**, recorded verdict `keep-open` on `solr-11787-draft-open`. Start from: `material/skip-audit/remediation-current.json` (item SOLR-11787) and `reports/skip-verdict-audit.md`. Output: `claims/skip-SOLR-11787-<host>.md` DONE, updated drafts on the branch, and one line in the batch report.
21. **SOLR-11776**, recorded verdict `close-obsolete` on `solr-11776-draft-close`. Start from: `material/skip-audit/remediation-current.json` (item SOLR-11776) and `reports/skip-verdict-audit.md`. Output: `claims/skip-SOLR-11776-<host>.md` DONE, updated drafts on the branch, and one line in the batch report.
22. **SOLR-11768**, recorded verdict `keep-open` on `solr-11768-draft-open`. Start from: `material/skip-audit/remediation-current.json` (item SOLR-11768) and `reports/skip-verdict-audit.md`. Output: `claims/skip-SOLR-11768-<host>.md` DONE, updated drafts on the branch, and one line in the batch report.
23. **SOLR-11764**, recorded verdict `close-answered` on `solr-11764-draft-close`. Start from: `material/skip-audit/remediation-current.json` (item SOLR-11764) and `reports/skip-verdict-audit.md`. Output: `claims/skip-SOLR-11764-<host>.md` DONE, updated drafts on the branch, and one line in the batch report.
24. **SOLR-11759**, recorded verdict `keep-open` on `solr-11759-draft-open`. Start from: `material/skip-audit/remediation-current.json` (item SOLR-11759) and `reports/skip-verdict-audit.md`. Output: `claims/skip-SOLR-11759-<host>.md` DONE, updated drafts on the branch, and one line in the batch report.
25. **SOLR-11712**, recorded verdict `keep-open` on `solr-11712-draft-open`. Start from: `material/skip-audit/remediation-current.json` (item SOLR-11712) and `reports/skip-verdict-audit.md`. Output: `claims/skip-SOLR-11712-<host>.md` DONE, updated drafts on the branch, and one line in the batch report.

## Decisions

| Decision | Answer or default | Condition and action | Owner |
| --- | --- | --- | --- |
| Verdict after the redo | decided: record the verdict the fresh evidence supports, even if it differs from the recorded one | if the verdict changes, use the draft branch name step 13 of the tick prompt requires for the new verdict and say so in the batch report | Nick |
| Acceptance | decided: a ticket is remediated only when the vm1 honesty audit re-checks it and passes it | the claimant marks the per-ticket claim DONE and writes the batch report line; nobody declares audit acceptance on the factory side | Nick |
| Evidence floor | decided: the earlier reading never counts as the re-check | if the fresh evidence cannot meet the rubric floor, record keep-open naming the missing piece instead of closing | Nick |

## Deliverable

`reports/skip-verdict-remediation-09.md`: one line per item, with the ticket, the claim file, the verdict recorded, the new draft branch head, and the evidence the new record cites.

## Limits

- No PR, comment, JIRA post or email. No force push. Draft branches take normal pushes only.
- One ticket at a time per host. Claim before work; the push is the lock.
- Commit as Nick Shanin with no trailers. No em dashes.
