# Pool assignment: skip verdict remediation, batch 11

Status: CLAIMED (vm2, claims/pool-aaa-skip-verdict-remediation-11.md, 2026-10-20T11:37:17Z)

- Capability tags: review, draft.
- Intended host: any pool Linux host. Claim under `claims/` before starting (WORKFLOW.md).
- Posted by: vm2, 2026-10-20, under the remediation rule in `pool/cron-prompt.md`: the unlisted open items had reached 25 (228 unlisted at posting, after batches 01 to 10).

The vm1 honesty audit marks each verdict in this batch suspect until it is re-checked: the recorded verdict in `material/skip-audit/queue-current.json` rests on an earlier audit or reading, Jira was not re-read for the closeout, and the ticket was not re-checked at the pinned head.

For each ticket, in the order listed: claim it as `claims/skip-<KEY>-<your host>.md`, then redo the closeout under steps 11 to 15 of `pool/cron-prompt.md` at the pinned head `498a09aea97aac86c2cc7ac114113b946d249942`. The earlier audit, B2, B3 or C reading is background only. The new record must cite the Jira status read in this pass, the code path with file and line or the reproduction attempt and its result, and the pinned head. Check any recorded close-* verdict with a grep or a fix commit before accepting it: batch 9 showed several recorded close verdicts that did not hold. Update the drafts on the draft branch with a normal push, record the verdict with `queue_tool.py set`, and mark the claim DONE naming the batch and the draft branch head.

## Items
1. **SOLR-10706**, recorded verdict `keep-open` on `solr-10706-draft-open`. Start from: `material/skip-audit/remediation-current.json` (item SOLR-10706) and `reports/skip-verdict-audit.md`. Output: `claims/skip-SOLR-10706-<host>.md` DONE, updated drafts on the branch, and one line in the batch report.
2. **SOLR-10650**, recorded verdict `close-fixed` on `solr-10650-draft-close`. Start from: `material/skip-audit/remediation-current.json` (item SOLR-10650) and `reports/skip-verdict-audit.md`. Output: `claims/skip-SOLR-10650-<host>.md` DONE, updated drafts on the branch, and one line in the batch report.
3. **SOLR-10604**, recorded verdict `keep-open` on `solr-10604-draft-open`. Start from: `material/skip-audit/remediation-current.json` (item SOLR-10604) and `reports/skip-verdict-audit.md`. Output: `claims/skip-SOLR-10604-<host>.md` DONE, updated drafts on the branch, and one line in the batch report.
4. **SOLR-10601**, recorded verdict `close-fixed` on `solr-10601-draft-close`. Start from: `material/skip-audit/remediation-current.json` (item SOLR-10601) and `reports/skip-verdict-audit.md`. Output: `claims/skip-SOLR-10601-<host>.md` DONE, updated drafts on the branch, and one line in the batch report.
5. **SOLR-10597**, recorded verdict `keep-open` on `solr-10597-draft-open`. Start from: `material/skip-audit/remediation-current.json` (item SOLR-10597) and `reports/skip-verdict-audit.md`. Output: `claims/skip-SOLR-10597-<host>.md` DONE, updated drafts on the branch, and one line in the batch report.
6. **SOLR-10592**, recorded verdict `keep-open` on `solr-10592-draft-open`. Start from: `material/skip-audit/remediation-current.json` (item SOLR-10592) and `reports/skip-verdict-audit.md`. Output: `claims/skip-SOLR-10592-<host>.md` DONE, updated drafts on the branch, and one line in the batch report.
7. **SOLR-10534**, recorded verdict `keep-open` on `solr-10534-draft-open`. Start from: `material/skip-audit/remediation-current.json` (item SOLR-10534) and `reports/skip-verdict-audit.md`. Output: `claims/skip-SOLR-10534-<host>.md` DONE, updated drafts on the branch, and one line in the batch report.
8. **SOLR-10533**, recorded verdict `keep-open` on `solr-10533-draft-open`. Start from: `material/skip-audit/remediation-current.json` (item SOLR-10533) and `reports/skip-verdict-audit.md`. Output: `claims/skip-SOLR-10533-<host>.md` DONE, updated drafts on the branch, and one line in the batch report.
9. **SOLR-9648**, recorded verdict `keep-open` on `solr-9648-draft-open`. Start from: `material/skip-audit/remediation-current.json` (item SOLR-9648) and `reports/skip-verdict-audit.md`. Output: `claims/skip-SOLR-9648-<host>.md` DONE, updated drafts on the branch, and one line in the batch report.
10. **SOLR-9645**, recorded verdict `keep-open` on `solr-9645-draft-open`. Start from: `material/skip-audit/remediation-current.json` (item SOLR-9645) and `reports/skip-verdict-audit.md`. Output: `claims/skip-SOLR-9645-<host>.md` DONE, updated drafts on the branch, and one line in the batch report.
11. **SOLR-9622**, recorded verdict `keep-open` on `solr-9622-draft-open`. Start from: `material/skip-audit/remediation-current.json` (item SOLR-9622) and `reports/skip-verdict-audit.md`. Output: `claims/skip-SOLR-9622-<host>.md` DONE, updated drafts on the branch, and one line in the batch report.
12. **SOLR-9599**, recorded verdict `keep-open` on `solr-9599-draft-open`. Start from: `material/skip-audit/remediation-current.json` (item SOLR-9599) and `reports/skip-verdict-audit.md`. Output: `claims/skip-SOLR-9599-<host>.md` DONE, updated drafts on the branch, and one line in the batch report.
13. **SOLR-9594**, recorded verdict `keep-open` on `solr-9594-draft-open`. Start from: `material/skip-audit/remediation-current.json` (item SOLR-9594) and `reports/skip-verdict-audit.md`. Output: `claims/skip-SOLR-9594-<host>.md` DONE, updated drafts on the branch, and one line in the batch report.
14. **SOLR-9593**, recorded verdict `keep-open` on `solr-9593-draft-open`. Start from: `material/skip-audit/remediation-current.json` (item SOLR-9593) and `reports/skip-verdict-audit.md`. Output: `claims/skip-SOLR-9593-<host>.md` DONE, updated drafts on the branch, and one line in the batch report.
15. **SOLR-9578**, recorded verdict `keep-open` on `solr-9578-draft-open`. Start from: `material/skip-audit/remediation-current.json` (item SOLR-9578) and `reports/skip-verdict-audit.md`. Output: `claims/skip-SOLR-9578-<host>.md` DONE, updated drafts on the branch, and one line in the batch report.
16. **SOLR-9573**, recorded verdict `keep-open` on `solr-9573-draft-open`. Start from: `material/skip-audit/remediation-current.json` (item SOLR-9573) and `reports/skip-verdict-audit.md`. Output: `claims/skip-SOLR-9573-<host>.md` DONE, updated drafts on the branch, and one line in the batch report.
17. **SOLR-9571**, recorded verdict `keep-open` on `solr-9571-draft-open`. Start from: `material/skip-audit/remediation-current.json` (item SOLR-9571) and `reports/skip-verdict-audit.md`. Output: `claims/skip-SOLR-9571-<host>.md` DONE, updated drafts on the branch, and one line in the batch report.
18. **SOLR-9550**, recorded verdict `keep-open` on `solr-9550-draft-open`. Start from: `material/skip-audit/remediation-current.json` (item SOLR-9550) and `reports/skip-verdict-audit.md`. Output: `claims/skip-SOLR-9550-<host>.md` DONE, updated drafts on the branch, and one line in the batch report.
19. **SOLR-9514**, recorded verdict `keep-open` on `solr-9514-draft-open`. Start from: `material/skip-audit/remediation-current.json` (item SOLR-9514) and `reports/skip-verdict-audit.md`. Output: `claims/skip-SOLR-9514-<host>.md` DONE, updated drafts on the branch, and one line in the batch report.
20. **SOLR-9492**, recorded verdict `keep-open` on `solr-9492-draft-open`. Start from: `material/skip-audit/remediation-current.json` (item SOLR-9492) and `reports/skip-verdict-audit.md`. Output: `claims/skip-SOLR-9492-<host>.md` DONE, updated drafts on the branch, and one line in the batch report.
21. **SOLR-9477**, recorded verdict `keep-open` on `solr-9477-draft-open`. Start from: `material/skip-audit/remediation-current.json` (item SOLR-9477) and `reports/skip-verdict-audit.md`. Output: `claims/skip-SOLR-9477-<host>.md` DONE, updated drafts on the branch, and one line in the batch report.
22. **SOLR-9468**, recorded verdict `close-fixed` on `solr-9468-draft-close`. Start from: `material/skip-audit/remediation-current.json` (item SOLR-9468) and `reports/skip-verdict-audit.md`. Output: `claims/skip-SOLR-9468-<host>.md` DONE, updated drafts on the branch, and one line in the batch report.
23. **SOLR-9428**, recorded verdict `keep-open` on `solr-9428-draft-open`. Start from: `material/skip-audit/remediation-current.json` (item SOLR-9428) and `reports/skip-verdict-audit.md`. Output: `claims/skip-SOLR-9428-<host>.md` DONE, updated drafts on the branch, and one line in the batch report.
24. **SOLR-9427**, recorded verdict `keep-open` on `solr-9427-draft-open`. Start from: `material/skip-audit/remediation-current.json` (item SOLR-9427) and `reports/skip-verdict-audit.md`. Output: `claims/skip-SOLR-9427-<host>.md` DONE, updated drafts on the branch, and one line in the batch report.
25. **SOLR-9426**, recorded verdict `keep-open` on `solr-9426-draft-open`. Start from: `material/skip-audit/remediation-current.json` (item SOLR-9426) and `reports/skip-verdict-audit.md`. Output: `claims/skip-SOLR-9426-<host>.md` DONE, updated drafts on the branch, and one line in the batch report.

## Decisions

| Decision | Answer or default | Condition and action | Owner |
| --- | --- | --- | --- |
| Verdict after the redo | decided: record the verdict the fresh evidence supports, even if it differs from the recorded one | if the verdict changes, use the draft branch name the tick prompt requires for the new verdict and say so in the batch report | Nick |
| Acceptance | decided: a ticket is remediated only when the vm1 honesty audit re-checks it and passes it | the claimant marks the per-ticket claim DONE and writes the batch report line; nobody declares audit acceptance on the factory side | Nick |
| Evidence floor | decided: the earlier reading never counts as the re-check | if the fresh evidence cannot meet the rubric floor, record keep-open naming the missing piece instead of closing | Nick |

## Deliverable

`reports/skip-verdict-remediation-11.md`: one line per item, with the ticket, the claim file, the verdict recorded, the new draft branch head, and the evidence the new record cites.

## Limits

- No PR, comment, JIRA post or email. No force push. Draft branches take normal pushes only.
- One ticket at a time per host. Claim before work; the push is the lock.
- Commit as Nick Shanin with no trailers. No em dashes.
