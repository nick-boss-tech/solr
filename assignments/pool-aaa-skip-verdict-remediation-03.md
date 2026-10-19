# Pool assignment: skip verdict remediation, batch 3 of 8

Status: CLAIMED (vm5, claims/pool-aaa-skip-verdict-remediation-03.md, 2026-10-19T17:32Z)

- Capability tags: review, draft.
- Intended host: any pool Linux host. Claim under `claims/` before starting (WORKFLOW.md).
- Posted by: main agent (vm1), 2026-10-19, at the direction of Nick.

The vm1 honesty audit marked every verdict in this batch suspect: the recorded verdict in `material/skip-audit/queue-current.json` rests on an earlier audit or reading, Jira was not re-read for the closeout, and the ticket was not re-checked at the pinned head. A verdict in this batch is provisional, not done. The full reasons per ticket are in `material/skip-audit/remediation-current.json` and in `reports/skip-verdict-audit.md`.

For each ticket, in the order listed: claim it as `claims/skip-<KEY>-<your host>.md`, then redo the closeout under steps 11 to 15 of `pool/cron-prompt.md` at the pinned head `498a09aea97aac86c2cc7ac114113b946d249942`. The earlier audit, B2, B3 or C reading is background only. The new record must cite the Jira status read in this pass, the code path with file and line or the reproduction attempt and its result, and the pinned head. Update the two draft files on the draft branch with a normal push, record the verdict with `queue_tool.py set`, and mark the claim DONE naming the remediation batch and the new draft branch head.

## Items

1. **SOLR-15571**, recorded verdict `keep-open` on `solr-15571-draft-open`. Start from: `material/skip-audit/remediation-current.json` (item SOLR-15571) and `reports/skip-verdict-audit.md`. Output: `claims/skip-SOLR-15571-<host>.md` DONE, updated drafts on the branch, and one line in the batch report.
2. **SOLR-15544**, recorded verdict `keep-open` on `solr-15544-draft-open`. Start from: `material/skip-audit/remediation-current.json` (item SOLR-15544) and `reports/skip-verdict-audit.md`. Output: `claims/skip-SOLR-15544-<host>.md` DONE, updated drafts on the branch, and one line in the batch report.
3. **SOLR-15540**, recorded verdict `keep-open` on `solr-15540-draft-open`. Start from: `material/skip-audit/remediation-current.json` (item SOLR-15540) and `reports/skip-verdict-audit.md`. Output: `claims/skip-SOLR-15540-<host>.md` DONE, updated drafts on the branch, and one line in the batch report.
4. **SOLR-15522**, recorded verdict `keep-open` on `solr-15522-draft-open`. Start from: `material/skip-audit/remediation-current.json` (item SOLR-15522) and `reports/skip-verdict-audit.md`. Output: `claims/skip-SOLR-15522-<host>.md` DONE, updated drafts on the branch, and one line in the batch report.
5. **SOLR-15521**, recorded verdict `keep-open` on `solr-15521-draft-open`. Start from: `material/skip-audit/remediation-current.json` (item SOLR-15521) and `reports/skip-verdict-audit.md`. Output: `claims/skip-SOLR-15521-<host>.md` DONE, updated drafts on the branch, and one line in the batch report.
6. **SOLR-15481**, recorded verdict `keep-open` on `solr-15481-draft-open`. Start from: `material/skip-audit/remediation-current.json` (item SOLR-15481) and `reports/skip-verdict-audit.md`. Output: `claims/skip-SOLR-15481-<host>.md` DONE, updated drafts on the branch, and one line in the batch report.
7. **SOLR-15443**, recorded verdict `keep-open` on `solr-15443-draft-open`. Start from: `material/skip-audit/remediation-current.json` (item SOLR-15443) and `reports/skip-verdict-audit.md`. Output: `claims/skip-SOLR-15443-<host>.md` DONE, updated drafts on the branch, and one line in the batch report.
8. **SOLR-15420**, recorded verdict `keep-open` on `solr-15420-draft-open`. Start from: `material/skip-audit/remediation-current.json` (item SOLR-15420) and `reports/skip-verdict-audit.md`. Output: `claims/skip-SOLR-15420-<host>.md` DONE, updated drafts on the branch, and one line in the batch report.
9. **SOLR-15408**, recorded verdict `keep-open` on `solr-15408-draft-open`. Start from: `material/skip-audit/remediation-current.json` (item SOLR-15408) and `reports/skip-verdict-audit.md`. Output: `claims/skip-SOLR-15408-<host>.md` DONE, updated drafts on the branch, and one line in the batch report.
10. **SOLR-15388**, recorded verdict `keep-open` on `solr-15388-draft-open`. Start from: `material/skip-audit/remediation-current.json` (item SOLR-15388) and `reports/skip-verdict-audit.md`. Output: `claims/skip-SOLR-15388-<host>.md` DONE, updated drafts on the branch, and one line in the batch report.
11. **SOLR-15371**, recorded verdict `keep-open` on `solr-15371-draft-open`. Start from: `material/skip-audit/remediation-current.json` (item SOLR-15371) and `reports/skip-verdict-audit.md`. Output: `claims/skip-SOLR-15371-<host>.md` DONE, updated drafts on the branch, and one line in the batch report.
12. **SOLR-15350**, recorded verdict `close-obsolete` on `solr-15350-draft-close`. Start from: `material/skip-audit/remediation-current.json` (item SOLR-15350) and `reports/skip-verdict-audit.md`. Output: `claims/skip-SOLR-15350-<host>.md` DONE, updated drafts on the branch, and one line in the batch report.
13. **SOLR-15330**, recorded verdict `keep-open` on `solr-15330-draft-open`. Start from: `material/skip-audit/remediation-current.json` (item SOLR-15330) and `reports/skip-verdict-audit.md`. Output: `claims/skip-SOLR-15330-<host>.md` DONE, updated drafts on the branch, and one line in the batch report.
14. **SOLR-15310**, recorded verdict `close-obsolete` on `solr-15310-draft-close`. Start from: `material/skip-audit/remediation-current.json` (item SOLR-15310) and `reports/skip-verdict-audit.md`. Output: `claims/skip-SOLR-15310-<host>.md` DONE, updated drafts on the branch, and one line in the batch report.
15. **SOLR-15305**, recorded verdict `keep-open` on `solr-15305-draft-open`. Start from: `material/skip-audit/remediation-current.json` (item SOLR-15305) and `reports/skip-verdict-audit.md`. Output: `claims/skip-SOLR-15305-<host>.md` DONE, updated drafts on the branch, and one line in the batch report.
16. **SOLR-15304**, recorded verdict `keep-open` on `solr-15304-draft-open`. Start from: `material/skip-audit/remediation-current.json` (item SOLR-15304) and `reports/skip-verdict-audit.md`. Output: `claims/skip-SOLR-15304-<host>.md` DONE, updated drafts on the branch, and one line in the batch report.
17. **SOLR-15297**, recorded verdict `close-obsolete` on `solr-15297-draft-close`. Start from: `material/skip-audit/remediation-current.json` (item SOLR-15297) and `reports/skip-verdict-audit.md`. Output: `claims/skip-SOLR-15297-<host>.md` DONE, updated drafts on the branch, and one line in the batch report.
18. **SOLR-15279**, recorded verdict `keep-open` on `solr-15279-draft-open`. Start from: `material/skip-audit/remediation-current.json` (item SOLR-15279) and `reports/skip-verdict-audit.md`. Output: `claims/skip-SOLR-15279-<host>.md` DONE, updated drafts on the branch, and one line in the batch report.
19. **SOLR-15237**, recorded verdict `keep-open` on `solr-15237-draft-open`. Start from: `material/skip-audit/remediation-current.json` (item SOLR-15237) and `reports/skip-verdict-audit.md`. Output: `claims/skip-SOLR-15237-<host>.md` DONE, updated drafts on the branch, and one line in the batch report.
20. **SOLR-15234**, recorded verdict `keep-open` on `solr-15234-draft-open`. Start from: `material/skip-audit/remediation-current.json` (item SOLR-15234) and `reports/skip-verdict-audit.md`. Output: `claims/skip-SOLR-15234-<host>.md` DONE, updated drafts on the branch, and one line in the batch report.
21. **SOLR-15226**, recorded verdict `keep-open` on `solr-15226-draft-open`. Start from: `material/skip-audit/remediation-current.json` (item SOLR-15226) and `reports/skip-verdict-audit.md`. Output: `claims/skip-SOLR-15226-<host>.md` DONE, updated drafts on the branch, and one line in the batch report.
22. **SOLR-15151**, recorded verdict `keep-open` on `solr-15151-draft-open`. Start from: `material/skip-audit/remediation-current.json` (item SOLR-15151) and `reports/skip-verdict-audit.md`. Output: `claims/skip-SOLR-15151-<host>.md` DONE, updated drafts on the branch, and one line in the batch report.
23. **SOLR-15037**, recorded verdict `keep-open` on `solr-15037-draft-open`. Start from: `material/skip-audit/remediation-current.json` (item SOLR-15037) and `reports/skip-verdict-audit.md`. Output: `claims/skip-SOLR-15037-<host>.md` DONE, updated drafts on the branch, and one line in the batch report.
24. **SOLR-15028**, recorded verdict `keep-open` on `solr-15028-draft-open`. Start from: `material/skip-audit/remediation-current.json` (item SOLR-15028) and `reports/skip-verdict-audit.md`. Output: `claims/skip-SOLR-15028-<host>.md` DONE, updated drafts on the branch, and one line in the batch report.
25. **SOLR-15026**, recorded verdict `keep-open` on `solr-15026-draft-open`. Start from: `material/skip-audit/remediation-current.json` (item SOLR-15026) and `reports/skip-verdict-audit.md`. Output: `claims/skip-SOLR-15026-<host>.md` DONE, updated drafts on the branch, and one line in the batch report.

## Decisions

| Decision | Answer or default | Condition and action | Owner |
| --- | --- | --- | --- |
| Verdict after the redo | decided: record the verdict the fresh evidence supports, even if it differs from the recorded one | if the verdict changes, use the draft branch name step 13 of the tick prompt requires for the new verdict and say so in the batch report | Nick |
| Acceptance | decided: a ticket is remediated only when the vm1 honesty audit re-checks it and passes it | the claimant marks the per-ticket claim DONE and writes the batch report line; nobody declares audit acceptance on the factory side | Nick |
| Evidence floor | decided: the earlier reading never counts as the re-check | if the fresh evidence cannot meet the rubric floor, record keep-open naming the missing piece instead of closing | Nick |

## Deliverable

`reports/skip-verdict-remediation-03.md`: one line per item, with the ticket, the claim file, the verdict recorded, the new draft branch head, and the evidence the new record cites.

## Limits

- No PR, comment, JIRA post or email. No force push. Draft branches take normal pushes only.
- One ticket at a time per host. Claim before work; the push is the lock.
- Commit as Nick Shanin with no trailers. No em dashes.
