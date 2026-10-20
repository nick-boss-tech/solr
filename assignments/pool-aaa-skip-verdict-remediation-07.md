# Pool assignment: skip verdict remediation, batch 7 of 8

Status: CLAIMED (vm5, claims/pool-aaa-skip-verdict-remediation-07.md, 2026-10-20T03:10:49Z)

- Capability tags: review, draft.
- Intended host: any pool Linux host. Claim under `claims/` before starting (WORKFLOW.md).
- Posted by: main agent (vm1), 2026-10-19, at the direction of Nick.

The vm1 honesty audit marked every verdict in this batch suspect: the recorded verdict in `material/skip-audit/queue-current.json` rests on an earlier audit or reading, Jira was not re-read for the closeout, and the ticket was not re-checked at the pinned head. A verdict in this batch is provisional, not done. The full reasons per ticket are in `material/skip-audit/remediation-current.json` and in `reports/skip-verdict-audit.md`.

For each ticket, in the order listed: claim it as `claims/skip-<KEY>-<your host>.md`, then redo the closeout under steps 11 to 15 of `pool/cron-prompt.md` at the pinned head `498a09aea97aac86c2cc7ac114113b946d249942`. The earlier audit, B2, B3 or C reading is background only. The new record must cite the Jira status read in this pass, the code path with file and line or the reproduction attempt and its result, and the pinned head. Update the two draft files on the draft branch with a normal push, record the verdict with `queue_tool.py set`, and mark the claim DONE naming the remediation batch and the new draft branch head.

## Items

1. **SOLR-13278**, recorded verdict `close-answered` on `solr-13278-draft-close`. Start from: `material/skip-audit/remediation-current.json` (item SOLR-13278) and `reports/skip-verdict-audit.md`. Output: `claims/skip-SOLR-13278-<host>.md` DONE, updated drafts on the branch, and one line in the batch report.
2. **SOLR-13270**, recorded verdict `keep-open` on `solr-13270-draft-open`. Start from: `material/skip-audit/remediation-current.json` (item SOLR-13270) and `reports/skip-verdict-audit.md`. Output: `claims/skip-SOLR-13270-<host>.md` DONE, updated drafts on the branch, and one line in the batch report.
3. **SOLR-13263**, recorded verdict `keep-open` on `solr-13263-draft-open`. Start from: `material/skip-audit/remediation-current.json` (item SOLR-13263) and `reports/skip-verdict-audit.md`. Output: `claims/skip-SOLR-13263-<host>.md` DONE, updated drafts on the branch, and one line in the batch report.
4. **SOLR-13247**, recorded verdict `keep-open` on `solr-13247-draft-open`. Start from: `material/skip-audit/remediation-current.json` (item SOLR-13247) and `reports/skip-verdict-audit.md`. Output: `claims/skip-SOLR-13247-<host>.md` DONE, updated drafts on the branch, and one line in the batch report.
5. **SOLR-13242**, recorded verdict `keep-open` on `solr-13242-draft-open`. Start from: `material/skip-audit/remediation-current.json` (item SOLR-13242) and `reports/skip-verdict-audit.md`. Output: `claims/skip-SOLR-13242-<host>.md` DONE, updated drafts on the branch, and one line in the batch report.
6. **SOLR-13237**, recorded verdict `keep-open` on `solr-13237-draft-open`. Start from: `material/skip-audit/remediation-current.json` (item SOLR-13237) and `reports/skip-verdict-audit.md`. Output: `claims/skip-SOLR-13237-<host>.md` DONE, updated drafts on the branch, and one line in the batch report.
7. **SOLR-13233**, recorded verdict `keep-open` on `solr-13233-draft-open`. Start from: `material/skip-audit/remediation-current.json` (item SOLR-13233) and `reports/skip-verdict-audit.md`. Output: `claims/skip-SOLR-13233-<host>.md` DONE, updated drafts on the branch, and one line in the batch report.
8. **SOLR-13216**, recorded verdict `keep-open` on `solr-13216-draft-open`. Start from: `material/skip-audit/remediation-current.json` (item SOLR-13216) and `reports/skip-verdict-audit.md`. Output: `claims/skip-SOLR-13216-<host>.md` DONE, updated drafts on the branch, and one line in the batch report.
9. **SOLR-13195**, recorded verdict `keep-open` on `solr-13195-draft-open`. Start from: `material/skip-audit/remediation-current.json` (item SOLR-13195) and `reports/skip-verdict-audit.md`. Output: `claims/skip-SOLR-13195-<host>.md` DONE, updated drafts on the branch, and one line in the batch report.
10. **SOLR-13192**, recorded verdict `keep-open` on `solr-13192-draft-open`. Start from: `material/skip-audit/remediation-current.json` (item SOLR-13192) and `reports/skip-verdict-audit.md`. Output: `claims/skip-SOLR-13192-<host>.md` DONE, updated drafts on the branch, and one line in the batch report.
11. **SOLR-13175**, recorded verdict `keep-open` on `solr-13175-draft-open`. Start from: `material/skip-audit/remediation-current.json` (item SOLR-13175) and `reports/skip-verdict-audit.md`. Output: `claims/skip-SOLR-13175-<host>.md` DONE, updated drafts on the branch, and one line in the batch report.
12. **SOLR-13167**, recorded verdict `close-answered` on `solr-13167-draft-close`. Start from: `material/skip-audit/remediation-current.json` (item SOLR-13167) and `reports/skip-verdict-audit.md`. Output: `claims/skip-SOLR-13167-<host>.md` DONE, updated drafts on the branch, and one line in the batch report.
13. **SOLR-13166**, recorded verdict `keep-open` on `solr-13166-draft-open`. Start from: `material/skip-audit/remediation-current.json` (item SOLR-13166) and `reports/skip-verdict-audit.md`. Output: `claims/skip-SOLR-13166-<host>.md` DONE, updated drafts on the branch, and one line in the batch report.
14. **SOLR-13094**, recorded verdict `keep-open` on `solr-13094-draft-open`. Start from: `material/skip-audit/remediation-current.json` (item SOLR-13094) and `reports/skip-verdict-audit.md`. Output: `claims/skip-SOLR-13094-<host>.md` DONE, updated drafts on the branch, and one line in the batch report.
15. **SOLR-13066**, recorded verdict `close-fixed` on `solr-13066-draft-close`. Start from: `material/skip-audit/remediation-current.json` (item SOLR-13066) and `reports/skip-verdict-audit.md`. Output: `claims/skip-SOLR-13066-<host>.md` DONE, updated drafts on the branch, and one line in the batch report.
16. **SOLR-13061**, recorded verdict `keep-open` on `solr-13061-draft-open`. Start from: `material/skip-audit/remediation-current.json` (item SOLR-13061) and `reports/skip-verdict-audit.md`. Output: `claims/skip-SOLR-13061-<host>.md` DONE, updated drafts on the branch, and one line in the batch report.
17. **SOLR-13056**, recorded verdict `keep-open` on `solr-13056-draft-open`. Start from: `material/skip-audit/remediation-current.json` (item SOLR-13056) and `reports/skip-verdict-audit.md`. Output: `claims/skip-SOLR-13056-<host>.md` DONE, updated drafts on the branch, and one line in the batch report.
18. **SOLR-13038**, recorded verdict `keep-open` on `solr-13038-draft-open`. Start from: `material/skip-audit/remediation-current.json` (item SOLR-13038) and `reports/skip-verdict-audit.md`. Output: `claims/skip-SOLR-13038-<host>.md` DONE, updated drafts on the branch, and one line in the batch report.
19. **SOLR-13021**, recorded verdict `keep-open` on `solr-13021-draft-open`. Start from: `material/skip-audit/remediation-current.json` (item SOLR-13021) and `reports/skip-verdict-audit.md`. Output: `claims/skip-SOLR-13021-<host>.md` DONE, updated drafts on the branch, and one line in the batch report.
20. **SOLR-13009**, recorded verdict `keep-open` on `solr-13009-draft-open`. Start from: `material/skip-audit/remediation-current.json` (item SOLR-13009) and `reports/skip-verdict-audit.md`. Output: `claims/skip-SOLR-13009-<host>.md` DONE, updated drafts on the branch, and one line in the batch report.
21. **SOLR-12944**, recorded verdict `keep-open` on `solr-12944-draft-open`. Start from: `material/skip-audit/remediation-current.json` (item SOLR-12944) and `reports/skip-verdict-audit.md`. Output: `claims/skip-SOLR-12944-<host>.md` DONE, updated drafts on the branch, and one line in the batch report.
22. **SOLR-12918**, recorded verdict `keep-open` on `solr-12918-draft-open`. Start from: `material/skip-audit/remediation-current.json` (item SOLR-12918) and `reports/skip-verdict-audit.md`. Output: `claims/skip-SOLR-12918-<host>.md` DONE, updated drafts on the branch, and one line in the batch report.
23. **SOLR-12900**, recorded verdict `keep-open` on `solr-12900-draft-open`. Start from: `material/skip-audit/remediation-current.json` (item SOLR-12900) and `reports/skip-verdict-audit.md`. Output: `claims/skip-SOLR-12900-<host>.md` DONE, updated drafts on the branch, and one line in the batch report.
24. **SOLR-12884**, recorded verdict `keep-open` on `solr-12884-draft-open`. Start from: `material/skip-audit/remediation-current.json` (item SOLR-12884) and `reports/skip-verdict-audit.md`. Output: `claims/skip-SOLR-12884-<host>.md` DONE, updated drafts on the branch, and one line in the batch report.
25. **SOLR-12850**, recorded verdict `keep-open` on `solr-12850-draft-open`. Start from: `material/skip-audit/remediation-current.json` (item SOLR-12850) and `reports/skip-verdict-audit.md`. Output: `claims/skip-SOLR-12850-<host>.md` DONE, updated drafts on the branch, and one line in the batch report.

## Decisions

| Decision | Answer or default | Condition and action | Owner |
| --- | --- | --- | --- |
| Verdict after the redo | decided: record the verdict the fresh evidence supports, even if it differs from the recorded one | if the verdict changes, use the draft branch name step 13 of the tick prompt requires for the new verdict and say so in the batch report | Nick |
| Acceptance | decided: a ticket is remediated only when the vm1 honesty audit re-checks it and passes it | the claimant marks the per-ticket claim DONE and writes the batch report line; nobody declares audit acceptance on the factory side | Nick |
| Evidence floor | decided: the earlier reading never counts as the re-check | if the fresh evidence cannot meet the rubric floor, record keep-open naming the missing piece instead of closing | Nick |

## Deliverable

`reports/skip-verdict-remediation-07.md`: one line per item, with the ticket, the claim file, the verdict recorded, the new draft branch head, and the evidence the new record cites.

## Limits

- No PR, comment, JIRA post or email. No force push. Draft branches take normal pushes only.
- One ticket at a time per host. Claim before work; the push is the lock.
- Commit as Nick Shanin with no trailers. No em dashes.
