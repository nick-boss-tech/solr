# Pool assignment: skip verdict remediation, batch 12

Status: CLAIMED (vm2, claims/pool-aaa-skip-verdict-remediation-12.md, 2026-10-20T14:20:40Z)

- Capability tags: review, draft.
- Intended host: any pool Linux host. Claim under `claims/` before starting (WORKFLOW.md).
- Posted by: vm2, 2026-10-20, under the remediation rule in `pool/cron-prompt.md`: the unlisted open items had reached 25 (203 unlisted at posting, after batch 11).

The vm1 honesty audit marks each verdict in this batch suspect until it is re-checked: the recorded verdict in `material/skip-audit/queue-current.json` rests on an earlier audit or reading, Jira was not re-read for the closeout, and the ticket was not re-checked at the pinned head.

For each ticket, in the order listed: claim it as `claims/skip-<KEY>-<your host>.md`, then redo the closeout under steps 11 to 15 of `pool/cron-prompt.md` at the pinned head `498a09aea97aac86c2cc7ac114113b946d249942`. The earlier audit, B2, B3 or C reading is background only. The new record must cite the Jira status read in this pass, the code path with file and line or the reproduction attempt and its result, and the pinned head. Check any recorded close-* verdict with a grep or a fix commit before accepting it. Update the drafts on the draft branch with a normal push (fetch the branch first if it exists), record the verdict with `queue_tool.py set`, and mark the claim DONE naming the batch and the draft branch head.

## Items
1. **SOLR-9400**, recorded verdict `close-obsolete` on `solr-9400-draft-close`. Start from: `material/skip-audit/remediation-current.json` (item SOLR-9400) and `reports/skip-verdict-audit.md`. Output: `claims/skip-SOLR-9400-<host>.md` DONE, updated drafts on the branch, and one line in the batch report.
2. **SOLR-9398**, recorded verdict `keep-open` on `solr-9398-draft-open`. Start from: `material/skip-audit/remediation-current.json` (item SOLR-9398) and `reports/skip-verdict-audit.md`. Output: `claims/skip-SOLR-9398-<host>.md` DONE, updated drafts on the branch, and one line in the batch report.
3. **SOLR-9390**, recorded verdict `keep-open` on `solr-9390-draft-open`. Start from: `material/skip-audit/remediation-current.json` (item SOLR-9390) and `reports/skip-verdict-audit.md`. Output: `claims/skip-SOLR-9390-<host>.md` DONE, updated drafts on the branch, and one line in the batch report.
4. **SOLR-9369**, recorded verdict `keep-open` on `solr-9369-draft-open`. Start from: `material/skip-audit/remediation-current.json` (item SOLR-9369) and `reports/skip-verdict-audit.md`. Output: `claims/skip-SOLR-9369-<host>.md` DONE, updated drafts on the branch, and one line in the batch report.
5. **SOLR-9366**, recorded verdict `close-obsolete` on `solr-9366-draft-close`. Start from: `material/skip-audit/remediation-current.json` (item SOLR-9366) and `reports/skip-verdict-audit.md`. Output: `claims/skip-SOLR-9366-<host>.md` DONE, updated drafts on the branch, and one line in the batch report.
6. **SOLR-9361**, recorded verdict `keep-open` on `solr-9361-draft-open`. Start from: `material/skip-audit/remediation-current.json` (item SOLR-9361) and `reports/skip-verdict-audit.md`. Output: `claims/skip-SOLR-9361-<host>.md` DONE, updated drafts on the branch, and one line in the batch report.
7. **SOLR-9352**, recorded verdict `keep-open` on `solr-9352-draft-open`. Start from: `material/skip-audit/remediation-current.json` (item SOLR-9352) and `reports/skip-verdict-audit.md`. Output: `claims/skip-SOLR-9352-<host>.md` DONE, updated drafts on the branch, and one line in the batch report.
8. **SOLR-9329**, recorded verdict `keep-open` on `solr-9329-draft-open`. Start from: `material/skip-audit/remediation-current.json` (item SOLR-9329) and `reports/skip-verdict-audit.md`. Output: `claims/skip-SOLR-9329-<host>.md` DONE, updated drafts on the branch, and one line in the batch report.
9. **SOLR-4674**, recorded verdict `keep-open` on `solr-4674-draft-open`. Start from: `material/skip-audit/remediation-current.json` (item SOLR-4674) and `reports/skip-verdict-audit.md`. Output: `claims/skip-SOLR-4674-<host>.md` DONE, updated drafts on the branch, and one line in the batch report.
10. **SOLR-4669**, recorded verdict `keep-open` on `solr-4669-draft-open`. Start from: `material/skip-audit/remediation-current.json` (item SOLR-4669) and `reports/skip-verdict-audit.md`. Output: `claims/skip-SOLR-4669-<host>.md` DONE, updated drafts on the branch, and one line in the batch report.
11. **SOLR-4659**, recorded verdict `keep-open` on `solr-4659-draft-open`. Start from: `material/skip-audit/remediation-current.json` (item SOLR-4659) and `reports/skip-verdict-audit.md`. Output: `claims/skip-SOLR-4659-<host>.md` DONE, updated drafts on the branch, and one line in the batch report.
12. **SOLR-4635**, recorded verdict `close-fixed` on `solr-4635-draft-close`. Start from: `material/skip-audit/remediation-current.json` (item SOLR-4635) and `reports/skip-verdict-audit.md`. Output: `claims/skip-SOLR-4635-<host>.md` DONE, updated drafts on the branch, and one line in the batch report.
13. **SOLR-4610**, recorded verdict `keep-open` on `solr-4610-draft-open`. Start from: `material/skip-audit/remediation-current.json` (item SOLR-4610) and `reports/skip-verdict-audit.md`. Output: `claims/skip-SOLR-4610-<host>.md` DONE, updated drafts on the branch, and one line in the batch report.
14. **SOLR-4593**, recorded verdict `keep-open` on `solr-4593-draft-open`. Start from: `material/skip-audit/remediation-current.json` (item SOLR-4593) and `reports/skip-verdict-audit.md`. Output: `claims/skip-SOLR-4593-<host>.md` DONE, updated drafts on the branch, and one line in the batch report.
15. **SOLR-4558**, recorded verdict `keep-open` on `solr-4558-draft-open`. Start from: `material/skip-audit/remediation-current.json` (item SOLR-4558) and `reports/skip-verdict-audit.md`. Output: `claims/skip-SOLR-4558-<host>.md` DONE, updated drafts on the branch, and one line in the batch report.
16. **SOLR-4533**, recorded verdict `keep-open` on `solr-4533-draft-open`. Start from: `material/skip-audit/remediation-current.json` (item SOLR-4533) and `reports/skip-verdict-audit.md`. Output: `claims/skip-SOLR-4533-<host>.md` DONE, updated drafts on the branch, and one line in the batch report.
17. **SOLR-4522**, recorded verdict `close-fixed` on `solr-4522-draft-close`. Start from: `material/skip-audit/remediation-current.json` (item SOLR-4522) and `reports/skip-verdict-audit.md`. Output: `claims/skip-SOLR-4522-<host>.md` DONE, updated drafts on the branch, and one line in the batch report.
18. **SOLR-4501**, recorded verdict `close-fixed` on `solr-4501-draft-close`. Start from: `material/skip-audit/remediation-current.json` (item SOLR-4501) and `reports/skip-verdict-audit.md`. Output: `claims/skip-SOLR-4501-<host>.md` DONE, updated drafts on the branch, and one line in the batch report.
19. **SOLR-4499**, recorded verdict `keep-open` on `solr-4499-draft-open`. Start from: `material/skip-audit/remediation-current.json` (item SOLR-4499) and `reports/skip-verdict-audit.md`. Output: `claims/skip-SOLR-4499-<host>.md` DONE, updated drafts on the branch, and one line in the batch report.
20. **SOLR-4486**, recorded verdict `keep-open` on `solr-4486-draft-open`. Start from: `material/skip-audit/remediation-current.json` (item SOLR-4486) and `reports/skip-verdict-audit.md`. Output: `claims/skip-SOLR-4486-<host>.md` DONE, updated drafts on the branch, and one line in the batch report.
21. **SOLR-4427**, recorded verdict `close-fixed` on `solr-4427-draft-close`. Start from: `material/skip-audit/remediation-current.json` (item SOLR-4427) and `reports/skip-verdict-audit.md`. Output: `claims/skip-SOLR-4427-<host>.md` DONE, updated drafts on the branch, and one line in the batch report.
22. **SOLR-4423**, recorded verdict `close-fixed` on `solr-4423-draft-close`. Start from: `material/skip-audit/remediation-current.json` (item SOLR-4423) and `reports/skip-verdict-audit.md`. Output: `claims/skip-SOLR-4423-<host>.md` DONE, updated drafts on the branch, and one line in the batch report.
23. **SOLR-4414**, recorded verdict `keep-open` on `solr-4414-draft-open`. Start from: `material/skip-audit/remediation-current.json` (item SOLR-4414) and `reports/skip-verdict-audit.md`. Output: `claims/skip-SOLR-4414-<host>.md` DONE, updated drafts on the branch, and one line in the batch report.
24. **SOLR-4390**, recorded verdict `close-fixed` on `solr-4390-draft-close`. Start from: `material/skip-audit/remediation-current.json` (item SOLR-4390) and `reports/skip-verdict-audit.md`. Output: `claims/skip-SOLR-4390-<host>.md` DONE, updated drafts on the branch, and one line in the batch report.
25. **SOLR-4382**, recorded verdict `close-fixed` on `solr-4382-draft-close`. Start from: `material/skip-audit/remediation-current.json` (item SOLR-4382) and `reports/skip-verdict-audit.md`. Output: `claims/skip-SOLR-4382-<host>.md` DONE, updated drafts on the branch, and one line in the batch report.

## Decisions

| Decision | Answer or default | Condition and action | Owner |
| --- | --- | --- | --- |
| Verdict after the redo | decided: record the verdict the fresh evidence supports, even if it differs from the recorded one | if the verdict changes, use the draft branch name the tick prompt requires for the new verdict and say so in the batch report | Nick |
| Acceptance | decided: a ticket is remediated only when the vm1 honesty audit re-checks it and passes it | the claimant marks the per-ticket claim DONE and writes the batch report line; nobody declares audit acceptance on the factory side | Nick |
| Evidence floor | decided: the earlier reading never counts as the re-check | if the fresh evidence cannot meet the rubric floor, record keep-open naming the missing piece instead of closing | Nick |

## Deliverable

`reports/skip-verdict-remediation-12.md`: one line per item, with the ticket, the claim file, the verdict recorded, the new draft branch head, and the evidence the new record cites.

## Limits

- No PR, comment, JIRA post or email. No force push. Draft branches take normal pushes only.
- One ticket at a time per host. Claim before work; the push is the lock.
- Commit as Nick Shanin with no trailers. No em dashes.
