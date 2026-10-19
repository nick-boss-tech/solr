# Pool assignment: skip verdict remediation, batch 5 of 8

Status: DONE (vm5, claims/pool-aaa-skip-verdict-remediation-05.md, 2026-10-19T22:15Z; deliverable reports/skip-verdict-remediation-05.md)

- Capability tags: review, draft.
- Intended host: any pool Linux host. Claim under `claims/` before starting (WORKFLOW.md).
- Posted by: main agent (vm1), 2026-10-19, at the direction of Nick.

The vm1 honesty audit marked every verdict in this batch suspect: the recorded verdict in `material/skip-audit/queue-current.json` rests on an earlier audit or reading, Jira was not re-read for the closeout, and the ticket was not re-checked at the pinned head. A verdict in this batch is provisional, not done. The full reasons per ticket are in `material/skip-audit/remediation-current.json` and in `reports/skip-verdict-audit.md`.

For each ticket, in the order listed: claim it as `claims/skip-<KEY>-<your host>.md`, then redo the closeout under steps 11 to 15 of `pool/cron-prompt.md` at the pinned head `498a09aea97aac86c2cc7ac114113b946d249942`. The earlier audit, B2, B3 or C reading is background only. The new record must cite the Jira status read in this pass, the code path with file and line or the reproduction attempt and its result, and the pinned head. Update the two draft files on the draft branch with a normal push, record the verdict with `queue_tool.py set`, and mark the claim DONE naming the remediation batch and the new draft branch head.

## Items

1. **SOLR-14150**, recorded verdict `keep-open` on `solr-14150-draft-open`. Start from: `material/skip-audit/remediation-current.json` (item SOLR-14150) and `reports/skip-verdict-audit.md`. Output: `claims/skip-SOLR-14150-<host>.md` DONE, updated drafts on the branch, and one line in the batch report.
2. **SOLR-14140**, recorded verdict `keep-open` on `solr-14140-draft-open`. Start from: `material/skip-audit/remediation-current.json` (item SOLR-14140) and `reports/skip-verdict-audit.md`. Output: `claims/skip-SOLR-14140-<host>.md` DONE, updated drafts on the branch, and one line in the batch report.
3. **SOLR-14133**, recorded verdict `keep-open` on `solr-14133-draft-open`. Start from: `material/skip-audit/remediation-current.json` (item SOLR-14133) and `reports/skip-verdict-audit.md`. Output: `claims/skip-SOLR-14133-<host>.md` DONE, updated drafts on the branch, and one line in the batch report.
4. **SOLR-14117**, recorded verdict `close-obsolete` on `solr-14117-draft-close`. Start from: `material/skip-audit/remediation-current.json` (item SOLR-14117) and `reports/skip-verdict-audit.md`. Output: `claims/skip-SOLR-14117-<host>.md` DONE, updated drafts on the branch, and one line in the batch report.
5. **SOLR-14100**, recorded verdict `keep-open` on `solr-14100-draft-open`. Start from: `material/skip-audit/remediation-current.json` (item SOLR-14100) and `reports/skip-verdict-audit.md`. Output: `claims/skip-SOLR-14100-<host>.md` DONE, updated drafts on the branch, and one line in the batch report.
6. **SOLR-14083**, recorded verdict `keep-open` on `solr-14083-draft-open`. Start from: `material/skip-audit/remediation-current.json` (item SOLR-14083) and `reports/skip-verdict-audit.md`. Output: `claims/skip-SOLR-14083-<host>.md` DONE, updated drafts on the branch, and one line in the batch report.
7. **SOLR-14074**, recorded verdict `keep-open` on `solr-14074-draft-open`. Start from: `material/skip-audit/remediation-current.json` (item SOLR-14074) and `reports/skip-verdict-audit.md`. Output: `claims/skip-SOLR-14074-<host>.md` DONE, updated drafts on the branch, and one line in the batch report.
8. **SOLR-14063**, recorded verdict `keep-open` on `solr-14063-draft-open`. Start from: `material/skip-audit/remediation-current.json` (item SOLR-14063) and `reports/skip-verdict-audit.md`. Output: `claims/skip-SOLR-14063-<host>.md` DONE, updated drafts on the branch, and one line in the batch report.
9. **SOLR-14061**, recorded verdict `keep-open` on `solr-14061-draft-open`. Start from: `material/skip-audit/remediation-current.json` (item SOLR-14061) and `reports/skip-verdict-audit.md`. Output: `claims/skip-SOLR-14061-<host>.md` DONE, updated drafts on the branch, and one line in the batch report.
10. **SOLR-14027**, recorded verdict `keep-open` on `solr-14027-draft-open`. Start from: `material/skip-audit/remediation-current.json` (item SOLR-14027) and `reports/skip-verdict-audit.md`. Output: `claims/skip-SOLR-14027-<host>.md` DONE, updated drafts on the branch, and one line in the batch report.
11. **SOLR-14023**, recorded verdict `keep-open` on `solr-14023-draft-open`. Start from: `material/skip-audit/remediation-current.json` (item SOLR-14023) and `reports/skip-verdict-audit.md`. Output: `claims/skip-SOLR-14023-<host>.md` DONE, updated drafts on the branch, and one line in the batch report.
12. **SOLR-14004**, recorded verdict `keep-open` on `solr-14004-draft-open`. Start from: `material/skip-audit/remediation-current.json` (item SOLR-14004) and `reports/skip-verdict-audit.md`. Output: `claims/skip-SOLR-14004-<host>.md` DONE, updated drafts on the branch, and one line in the batch report.
13. **SOLR-13976**, recorded verdict `keep-open` on `solr-13976-draft-open`. Start from: `material/skip-audit/remediation-current.json` (item SOLR-13976) and `reports/skip-verdict-audit.md`. Output: `claims/skip-SOLR-13976-<host>.md` DONE, updated drafts on the branch, and one line in the batch report.
14. **SOLR-13933**, recorded verdict `keep-open` on `solr-13933-draft-open`. Start from: `material/skip-audit/remediation-current.json` (item SOLR-13933) and `reports/skip-verdict-audit.md`. Output: `claims/skip-SOLR-13933-<host>.md` DONE, updated drafts on the branch, and one line in the batch report.
15. **SOLR-13861**, recorded verdict `keep-open` on `solr-13861-draft-open`. Start from: `material/skip-audit/remediation-current.json` (item SOLR-13861) and `reports/skip-verdict-audit.md`. Output: `claims/skip-SOLR-13861-<host>.md` DONE, updated drafts on the branch, and one line in the batch report.
16. **SOLR-13853**, recorded verdict `keep-open` on `solr-13853-draft-open`. Start from: `material/skip-audit/remediation-current.json` (item SOLR-13853) and `reports/skip-verdict-audit.md`. Output: `claims/skip-SOLR-13853-<host>.md` DONE, updated drafts on the branch, and one line in the batch report.
17. **SOLR-13846**, recorded verdict `close-obsolete` on `solr-13846-draft-close`. Start from: `material/skip-audit/remediation-current.json` (item SOLR-13846) and `reports/skip-verdict-audit.md`. Output: `claims/skip-SOLR-13846-<host>.md` DONE, updated drafts on the branch, and one line in the batch report.
18. **SOLR-13837**, recorded verdict `keep-open` on `solr-13837-draft-open`. Start from: `material/skip-audit/remediation-current.json` (item SOLR-13837) and `reports/skip-verdict-audit.md`. Output: `claims/skip-SOLR-13837-<host>.md` DONE, updated drafts on the branch, and one line in the batch report.
19. **SOLR-13832**, recorded verdict `keep-open` on `solr-13832-draft-open`. Start from: `material/skip-audit/remediation-current.json` (item SOLR-13832) and `reports/skip-verdict-audit.md`. Output: `claims/skip-SOLR-13832-<host>.md` DONE, updated drafts on the branch, and one line in the batch report.
20. **SOLR-13825**, recorded verdict `keep-open` on `solr-13825-draft-open`. Start from: `material/skip-audit/remediation-current.json` (item SOLR-13825) and `reports/skip-verdict-audit.md`. Output: `claims/skip-SOLR-13825-<host>.md` DONE, updated drafts on the branch, and one line in the batch report.
21. **SOLR-13785**, recorded verdict `keep-open` on `solr-13785-draft-open`. Start from: `material/skip-audit/remediation-current.json` (item SOLR-13785) and `reports/skip-verdict-audit.md`. Output: `claims/skip-SOLR-13785-<host>.md` DONE, updated drafts on the branch, and one line in the batch report.
22. **SOLR-13781**, recorded verdict `close-obsolete` on `solr-13781-draft-close`. Start from: `material/skip-audit/remediation-current.json` (item SOLR-13781) and `reports/skip-verdict-audit.md`. Output: `claims/skip-SOLR-13781-<host>.md` DONE, updated drafts on the branch, and one line in the batch report.
23. **SOLR-13772**, recorded verdict `keep-open` on `solr-13772-draft-open`. Start from: `material/skip-audit/remediation-current.json` (item SOLR-13772) and `reports/skip-verdict-audit.md`. Output: `claims/skip-SOLR-13772-<host>.md` DONE, updated drafts on the branch, and one line in the batch report.
24. **SOLR-13709**, recorded verdict `keep-open` on `solr-13709-draft-open`. Start from: `material/skip-audit/remediation-current.json` (item SOLR-13709) and `reports/skip-verdict-audit.md`. Output: `claims/skip-SOLR-13709-<host>.md` DONE, updated drafts on the branch, and one line in the batch report.
25. **SOLR-13708**, recorded verdict `keep-open` on `solr-13708-draft-open`. Start from: `material/skip-audit/remediation-current.json` (item SOLR-13708) and `reports/skip-verdict-audit.md`. Output: `claims/skip-SOLR-13708-<host>.md` DONE, updated drafts on the branch, and one line in the batch report.

## Decisions

| Decision | Answer or default | Condition and action | Owner |
| --- | --- | --- | --- |
| Verdict after the redo | decided: record the verdict the fresh evidence supports, even if it differs from the recorded one | if the verdict changes, use the draft branch name step 13 of the tick prompt requires for the new verdict and say so in the batch report | Nick |
| Acceptance | decided: a ticket is remediated only when the vm1 honesty audit re-checks it and passes it | the claimant marks the per-ticket claim DONE and writes the batch report line; nobody declares audit acceptance on the factory side | Nick |
| Evidence floor | decided: the earlier reading never counts as the re-check | if the fresh evidence cannot meet the rubric floor, record keep-open naming the missing piece instead of closing | Nick |

## Deliverable

`reports/skip-verdict-remediation-05.md`: one line per item, with the ticket, the claim file, the verdict recorded, the new draft branch head, and the evidence the new record cites.

## Limits

- No PR, comment, JIRA post or email. No force push. Draft branches take normal pushes only.
- One ticket at a time per host. Claim before work; the push is the lock.
- Commit as Nick Shanin with no trailers. No em dashes.
