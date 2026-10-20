# Pool assignment: skip verdict remediation, batch 10

Status: CLAIMED (vm5, claims/pool-aaa-skip-verdict-remediation-10.md, 2026-10-20T07:06:38Z)

- Capability tags: review, draft.
- Intended host: any pool Linux host. Claim under `claims/` before starting (WORKFLOW.md).
- Posted by: vm5, 2026-10-19, under the remediation rule in `pool/cron-prompt.md`: the unlisted open items reached 25 after the cross-audit suspects of batches 03, 06, 17 and 21 were appended to `remediation-current.json`.

The vm1 honesty audit marked every verdict in this batch suspect: the recorded verdict in `material/skip-audit/queue-current.json` rests on an earlier audit or reading, Jira was not re-read for the closeout, and the ticket was not re-checked at the pinned head. A verdict in this batch is provisional, not done. The full reasons per ticket are in `material/skip-audit/remediation-current.json` and in `reports/skip-verdict-audit.md`.

For each ticket, in the order listed: claim it as `claims/skip-<KEY>-<your host>.md`, then redo the closeout under steps 11 to 15 of `pool/cron-prompt.md` at the pinned head `498a09aea97aac86c2cc7ac114113b946d249942`. The earlier audit, B2, B3 or C reading is background only. The new record must cite the Jira status read in this pass, the code path with file and line or the reproduction attempt and its result, and the pinned head. Update the two draft files on the draft branch with a normal push, record the verdict with `queue_tool.py set`, and mark the claim DONE naming the remediation batch and the new draft branch head.

## Items

1. **SOLR-11688**, recorded verdict `close-fixed` on `solr-11688-draft-close`. Start from: `material/skip-audit/remediation-current.json` (item SOLR-11688) and `reports/skip-verdict-audit.md`. Output: `claims/skip-SOLR-11688-<host>.md` DONE, updated drafts on the branch, and one line in the batch report.
2. **SOLR-11675**, recorded verdict `keep-open` on `solr-11675-draft-open`. Start from: `material/skip-audit/remediation-current.json` (item SOLR-11675) and `reports/skip-verdict-audit.md`. Output: `claims/skip-SOLR-11675-<host>.md` DONE, updated drafts on the branch, and one line in the batch report.
3. **SOLR-11672**, recorded verdict `keep-open` on `solr-11672-draft-open`. Start from: `material/skip-audit/remediation-current.json` (item SOLR-11672) and `reports/skip-verdict-audit.md`. Output: `claims/skip-SOLR-11672-<host>.md` DONE, updated drafts on the branch, and one line in the batch report.
4. **SOLR-11659**, recorded verdict `close-obsolete` on `solr-11659-draft-close`. Start from: `material/skip-audit/remediation-current.json` (item SOLR-11659) and `reports/skip-verdict-audit.md`. Output: `claims/skip-SOLR-11659-<host>.md` DONE, updated drafts on the branch, and one line in the batch report.
5. **SOLR-11655**, recorded verdict `keep-open` on `solr-11655-draft-open`. Start from: `material/skip-audit/remediation-current.json` (item SOLR-11655) and `reports/skip-verdict-audit.md`. Output: `claims/skip-SOLR-11655-<host>.md` DONE, updated drafts on the branch, and one line in the batch report.
6. **SOLR-11627**, recorded verdict `keep-open` on `solr-11627-draft-open`. Start from: `material/skip-audit/remediation-current.json` (item SOLR-11627) and `reports/skip-verdict-audit.md`. Output: `claims/skip-SOLR-11627-<host>.md` DONE, updated drafts on the branch, and one line in the batch report.
7. **SOLR-11583**, recorded verdict `keep-open` on `solr-11583-draft-open`. Start from: `material/skip-audit/remediation-current.json` (item SOLR-11583) and `reports/skip-verdict-audit.md`. Output: `claims/skip-SOLR-11583-<host>.md` DONE, updated drafts on the branch, and one line in the batch report.
8. **SOLR-11577**, recorded verdict `keep-open` on `solr-11577-draft-open`. Start from: `material/skip-audit/remediation-current.json` (item SOLR-11577) and `reports/skip-verdict-audit.md`. Output: `claims/skip-SOLR-11577-<host>.md` DONE, updated drafts on the branch, and one line in the batch report.
9. **SOLR-10974**, recorded verdict `keep-open` on `solr-10974-draft-open`. Start from: `material/skip-audit/remediation-current.json` (item SOLR-10974) and `reports/skip-verdict-audit.md`. Output: `claims/skip-SOLR-10974-<host>.md` DONE, updated drafts on the branch, and one line in the batch report.
10. **SOLR-10960**, recorded verdict `keep-open` on `solr-10960-draft-open`. Start from: `material/skip-audit/remediation-current.json` (item SOLR-10960) and `reports/skip-verdict-audit.md`. Output: `claims/skip-SOLR-10960-<host>.md` DONE, updated drafts on the branch, and one line in the batch report.
11. **SOLR-10959**, recorded verdict `close-obsolete` on `solr-10959-draft-close`. Start from: `material/skip-audit/remediation-current.json` (item SOLR-10959) and `reports/skip-verdict-audit.md`. Output: `claims/skip-SOLR-10959-<host>.md` DONE, updated drafts on the branch, and one line in the batch report.
12. **SOLR-10952**, recorded verdict `keep-open` on `solr-10952-draft-open`. Start from: `material/skip-audit/remediation-current.json` (item SOLR-10952) and `reports/skip-verdict-audit.md`. Output: `claims/skip-SOLR-10952-<host>.md` DONE, updated drafts on the branch, and one line in the batch report.
13. **SOLR-10936**, recorded verdict `keep-open` on `solr-10936-draft-open`. Start from: `material/skip-audit/remediation-current.json` (item SOLR-10936) and `reports/skip-verdict-audit.md`. Output: `claims/skip-SOLR-10936-<host>.md` DONE, updated drafts on the branch, and one line in the batch report.
14. **SOLR-10917**, recorded verdict `close-fixed` on `solr-10917-draft-close`. Start from: `material/skip-audit/remediation-current.json` (item SOLR-10917) and `reports/skip-verdict-audit.md`. Output: `claims/skip-SOLR-10917-<host>.md` DONE, updated drafts on the branch, and one line in the batch report.
15. **SOLR-10904**, recorded verdict `close-obsolete` on `solr-10904-draft-close`. Start from: `material/skip-audit/remediation-current.json` (item SOLR-10904) and `reports/skip-verdict-audit.md`. Output: `claims/skip-SOLR-10904-<host>.md` DONE, updated drafts on the branch, and one line in the batch report.
16. **SOLR-10896**, recorded verdict `keep-open` on `solr-10896-draft-open`. Start from: `material/skip-audit/remediation-current.json` (item SOLR-10896) and `reports/skip-verdict-audit.md`. Output: `claims/skip-SOLR-10896-<host>.md` DONE, updated drafts on the branch, and one line in the batch report.
17. **SOLR-10889**, recorded verdict `close-obsolete` on `solr-10889-draft-close`. Start from: `material/skip-audit/remediation-current.json` (item SOLR-10889) and `reports/skip-verdict-audit.md`. Output: `claims/skip-SOLR-10889-<host>.md` DONE, updated drafts on the branch, and one line in the batch report.
18. **SOLR-10856**, recorded verdict `keep-open` on `solr-10856-draft-open`. Start from: `material/skip-audit/remediation-current.json` (item SOLR-10856) and `reports/skip-verdict-audit.md`. Output: `claims/skip-SOLR-10856-<host>.md` DONE, updated drafts on the branch, and one line in the batch report.
19. **SOLR-10841**, recorded verdict `keep-open` on `solr-10841-draft-open`. Start from: `material/skip-audit/remediation-current.json` (item SOLR-10841) and `reports/skip-verdict-audit.md`. Output: `claims/skip-SOLR-10841-<host>.md` DONE, updated drafts on the branch, and one line in the batch report.
20. **SOLR-10806**, recorded verdict `keep-open` on `solr-10806-draft-open`. Start from: `material/skip-audit/remediation-current.json` (item SOLR-10806) and `reports/skip-verdict-audit.md`. Output: `claims/skip-SOLR-10806-<host>.md` DONE, updated drafts on the branch, and one line in the batch report.
21. **SOLR-10759**, recorded verdict `keep-open` on `solr-10759-draft-open`. Start from: `material/skip-audit/remediation-current.json` (item SOLR-10759) and `reports/skip-verdict-audit.md`. Output: `claims/skip-SOLR-10759-<host>.md` DONE, updated drafts on the branch, and one line in the batch report.
22. **SOLR-10751**, recorded verdict `keep-open` on `solr-10751-draft-open`. Start from: `material/skip-audit/remediation-current.json` (item SOLR-10751) and `reports/skip-verdict-audit.md`. Output: `claims/skip-SOLR-10751-<host>.md` DONE, updated drafts on the branch, and one line in the batch report.
23. **SOLR-10726**, recorded verdict `keep-open` on `solr-10726-draft-open`. Start from: `material/skip-audit/remediation-current.json` (item SOLR-10726) and `reports/skip-verdict-audit.md`. Output: `claims/skip-SOLR-10726-<host>.md` DONE, updated drafts on the branch, and one line in the batch report.
24. **SOLR-10712**, recorded verdict `close-fixed` on `solr-10712-draft-close`. Start from: `material/skip-audit/remediation-current.json` (item SOLR-10712) and `reports/skip-verdict-audit.md`. Output: `claims/skip-SOLR-10712-<host>.md` DONE, updated drafts on the branch, and one line in the batch report.
25. **SOLR-10709**, recorded verdict `close-fixed` on `solr-10709-draft-close`. Start from: `material/skip-audit/remediation-current.json` (item SOLR-10709) and `reports/skip-verdict-audit.md`. Output: `claims/skip-SOLR-10709-<host>.md` DONE, updated drafts on the branch, and one line in the batch report.

## Decisions

| Decision | Answer or default | Condition and action | Owner |
| --- | --- | --- | --- |
| Verdict after the redo | decided: record the verdict the fresh evidence supports, even if it differs from the recorded one | if the verdict changes, use the draft branch name step 13 of the tick prompt requires for the new verdict and say so in the batch report | Nick |
| Acceptance | decided: a ticket is remediated only when the vm1 honesty audit re-checks it and passes it | the claimant marks the per-ticket claim DONE and writes the batch report line; nobody declares audit acceptance on the factory side | Nick |
| Evidence floor | decided: the earlier reading never counts as the re-check | if the fresh evidence cannot meet the rubric floor, record keep-open naming the missing piece instead of closing | Nick |

## Deliverable

`reports/skip-verdict-remediation-10.md`: one line per item, with the ticket, the claim file, the verdict recorded, the new draft branch head, and the evidence the new record cites.

## Limits

- No PR, comment, JIRA post or email. No force push. Draft branches take normal pushes only.
- One ticket at a time per host. Claim before work; the push is the lock.
- Commit as Nick Shanin with no trailers. No em dashes.
