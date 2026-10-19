# Pool assignment: skip verdict remediation, batch 2 of 8

Status: CLAIMED (claims/pool-aaa-skip-verdict-remediation-02.md, 2026-10-19T17:08:58Z)

- Capability tags: review, draft.
- Intended host: any pool Linux host. Claim under `claims/` before starting (WORKFLOW.md).
- Posted by: main agent (vm1), 2026-10-19, at the direction of Nick.

The vm1 honesty audit marked every verdict in this batch suspect: the recorded verdict in `material/skip-audit/queue-current.json` rests on an earlier audit or reading, Jira was not re-read for the closeout, and the ticket was not re-checked at the pinned head. A verdict in this batch is provisional, not done. The full reasons per ticket are in `material/skip-audit/remediation-current.json` and in `reports/skip-verdict-audit.md`.

For each ticket, in the order listed: claim it as `claims/skip-<KEY>-<your host>.md`, then redo the closeout under steps 11 to 15 of `pool/cron-prompt.md` at the pinned head `498a09aea97aac86c2cc7ac114113b946d249942`. The earlier audit, B2, B3 or C reading is background only. The new record must cite the Jira status read in this pass, the code path with file and line or the reproduction attempt and its result, and the pinned head. Update the two draft files on the draft branch with a normal push, record the verdict with `queue_tool.py set`, and mark the claim DONE naming the remediation batch and the new draft branch head.

## Items

1. **SOLR-16253**, recorded verdict `keep-open` on `solr-16253-draft-open`. Start from: `material/skip-audit/remediation-current.json` (item SOLR-16253) and `reports/skip-verdict-audit.md`. Output: `claims/skip-SOLR-16253-<host>.md` DONE, updated drafts on the branch, and one line in the batch report.
2. **SOLR-16200**, recorded verdict `keep-open` on `solr-16200-draft-open`. Start from: `material/skip-audit/remediation-current.json` (item SOLR-16200) and `reports/skip-verdict-audit.md`. Output: `claims/skip-SOLR-16200-<host>.md` DONE, updated drafts on the branch, and one line in the batch report.
3. **SOLR-16167**, recorded verdict `keep-open` on `solr-16167-draft-open`. Start from: `material/skip-audit/remediation-current.json` (item SOLR-16167) and `reports/skip-verdict-audit.md`. Output: `claims/skip-SOLR-16167-<host>.md` DONE, updated drafts on the branch, and one line in the batch report.
4. **SOLR-16020**, recorded verdict `keep-open` on `solr-16020-draft-open`. Start from: `material/skip-audit/remediation-current.json` (item SOLR-16020) and `reports/skip-verdict-audit.md`. Output: `claims/skip-SOLR-16020-<host>.md` DONE, updated drafts on the branch, and one line in the batch report.
5. **SOLR-15997**, recorded verdict `keep-open` on `solr-15997-draft-open`. Start from: `material/skip-audit/remediation-current.json` (item SOLR-15997) and `reports/skip-verdict-audit.md`. Output: `claims/skip-SOLR-15997-<host>.md` DONE, updated drafts on the branch, and one line in the batch report.
6. **SOLR-15948**, recorded verdict `keep-open` on `solr-15948-draft-open`. Start from: `material/skip-audit/remediation-current.json` (item SOLR-15948) and `reports/skip-verdict-audit.md`. Output: `claims/skip-SOLR-15948-<host>.md` DONE, updated drafts on the branch, and one line in the batch report.
7. **SOLR-15858**, recorded verdict `keep-open` on `solr-15858-draft-open`. Start from: `material/skip-audit/remediation-current.json` (item SOLR-15858) and `reports/skip-verdict-audit.md`. Output: `claims/skip-SOLR-15858-<host>.md` DONE, updated drafts on the branch, and one line in the batch report.
8. **SOLR-16306**, recorded verdict `keep-open` on `solr-16306-draft-open`. Start from: `material/skip-audit/remediation-current.json` (item SOLR-16306) and `reports/skip-verdict-audit.md`. Output: `claims/skip-SOLR-16306-<host>.md` DONE, updated drafts on the branch, and one line in the batch report.
9. **SOLR-16122**, recorded verdict `keep-open` on `solr-16122-draft-open`. Start from: `material/skip-audit/remediation-current.json` (item SOLR-16122) and `reports/skip-verdict-audit.md`. Output: `claims/skip-SOLR-16122-<host>.md` DONE, updated drafts on the branch, and one line in the batch report.
10. **SOLR-16094**, recorded verdict `keep-open` on `solr-16094-draft-open`. Start from: `material/skip-audit/remediation-current.json` (item SOLR-16094) and `reports/skip-verdict-audit.md`. Output: `claims/skip-SOLR-16094-<host>.md` DONE, updated drafts on the branch, and one line in the batch report.
11. **SOLR-16084**, recorded verdict `keep-open` on `solr-16084-draft-open`. Start from: `material/skip-audit/remediation-current.json` (item SOLR-16084) and `reports/skip-verdict-audit.md`. Output: `claims/skip-SOLR-16084-<host>.md` DONE, updated drafts on the branch, and one line in the batch report.
12. **SOLR-16077**, recorded verdict `keep-open` on `solr-16077-draft-open`. Start from: `material/skip-audit/remediation-current.json` (item SOLR-16077) and `reports/skip-verdict-audit.md`. Output: `claims/skip-SOLR-16077-<host>.md` DONE, updated drafts on the branch, and one line in the batch report.
13. **SOLR-16062**, recorded verdict `keep-open` on `solr-16062-draft-open`. Start from: `material/skip-audit/remediation-current.json` (item SOLR-16062) and `reports/skip-verdict-audit.md`. Output: `claims/skip-SOLR-16062-<host>.md` DONE, updated drafts on the branch, and one line in the batch report.
14. **SOLR-15977**, recorded verdict `keep-open` on `solr-15977-draft-open`. Start from: `material/skip-audit/remediation-current.json` (item SOLR-15977) and `reports/skip-verdict-audit.md`. Output: `claims/skip-SOLR-15977-<host>.md` DONE, updated drafts on the branch, and one line in the batch report.
15. **SOLR-15970**, recorded verdict `close-fixed` on `solr-15970-draft-close`. Start from: `material/skip-audit/remediation-current.json` (item SOLR-15970) and `reports/skip-verdict-audit.md`. Output: `claims/skip-SOLR-15970-<host>.md` DONE, updated drafts on the branch, and one line in the batch report.
16. **SOLR-15879**, recorded verdict `close-obsolete` on `solr-15879-draft-close`. Start from: `material/skip-audit/remediation-current.json` (item SOLR-15879) and `reports/skip-verdict-audit.md`. Output: `claims/skip-SOLR-15879-<host>.md` DONE, updated drafts on the branch, and one line in the batch report.
17. **SOLR-15848**, recorded verdict `close-fixed` on `solr-15848-draft-close`. Start from: `material/skip-audit/remediation-current.json` (item SOLR-15848) and `reports/skip-verdict-audit.md`. Output: `claims/skip-SOLR-15848-<host>.md` DONE, updated drafts on the branch, and one line in the batch report.
18. **SOLR-15801**, recorded verdict `close-answered` on `solr-15801-draft-close`. Start from: `material/skip-audit/remediation-current.json` (item SOLR-15801) and `reports/skip-verdict-audit.md`. Output: `claims/skip-SOLR-15801-<host>.md` DONE, updated drafts on the branch, and one line in the batch report.
19. **SOLR-15727**, recorded verdict `keep-open` on `solr-15727-draft-open`. Start from: `material/skip-audit/remediation-current.json` (item SOLR-15727) and `reports/skip-verdict-audit.md`. Output: `claims/skip-SOLR-15727-<host>.md` DONE, updated drafts on the branch, and one line in the batch report.
20. **SOLR-15704**, recorded verdict `close-obsolete` on `solr-15704-draft-close`. Start from: `material/skip-audit/remediation-current.json` (item SOLR-15704) and `reports/skip-verdict-audit.md`. Output: `claims/skip-SOLR-15704-<host>.md` DONE, updated drafts on the branch, and one line in the batch report.
21. **SOLR-15690**, recorded verdict `close-obsolete` on `solr-15690-draft-close`. Start from: `material/skip-audit/remediation-current.json` (item SOLR-15690) and `reports/skip-verdict-audit.md`. Output: `claims/skip-SOLR-15690-<host>.md` DONE, updated drafts on the branch, and one line in the batch report.
22. **SOLR-15679**, recorded verdict `keep-open` on `solr-15679-draft-open`. Start from: `material/skip-audit/remediation-current.json` (item SOLR-15679) and `reports/skip-verdict-audit.md`. Output: `claims/skip-SOLR-15679-<host>.md` DONE, updated drafts on the branch, and one line in the batch report.
23. **SOLR-15672**, recorded verdict `keep-open` on `solr-15672-draft-open`. Start from: `material/skip-audit/remediation-current.json` (item SOLR-15672) and `reports/skip-verdict-audit.md`. Output: `claims/skip-SOLR-15672-<host>.md` DONE, updated drafts on the branch, and one line in the batch report.
24. **SOLR-15586**, recorded verdict `keep-open` on `solr-15586-draft-open`. Start from: `material/skip-audit/remediation-current.json` (item SOLR-15586) and `reports/skip-verdict-audit.md`. Output: `claims/skip-SOLR-15586-<host>.md` DONE, updated drafts on the branch, and one line in the batch report.
25. **SOLR-15585**, recorded verdict `keep-open` on `solr-15585-draft-open`. Start from: `material/skip-audit/remediation-current.json` (item SOLR-15585) and `reports/skip-verdict-audit.md`. Output: `claims/skip-SOLR-15585-<host>.md` DONE, updated drafts on the branch, and one line in the batch report.

## Decisions

| Decision | Answer or default | Condition and action | Owner |
| --- | --- | --- | --- |
| Verdict after the redo | decided: record the verdict the fresh evidence supports, even if it differs from the recorded one | if the verdict changes, use the draft branch name step 13 of the tick prompt requires for the new verdict and say so in the batch report | Nick |
| Acceptance | decided: a ticket is remediated only when the vm1 honesty audit re-checks it and passes it | the claimant marks the per-ticket claim DONE and writes the batch report line; nobody declares audit acceptance on the factory side | Nick |
| Evidence floor | decided: the earlier reading never counts as the re-check | if the fresh evidence cannot meet the rubric floor, record keep-open naming the missing piece instead of closing | Nick |

## Deliverable

`reports/skip-verdict-remediation-02.md`: one line per item, with the ticket, the claim file, the verdict recorded, the new draft branch head, and the evidence the new record cites.

## Limits

- No PR, comment, JIRA post or email. No force push. Draft branches take normal pushes only.
- One ticket at a time per host. Claim before work; the push is the lock.
- Commit as Nick Shanin with no trailers. No em dashes.
