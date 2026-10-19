# Pool assignment: skip verdict remediation, batch 1 of 8

Status: DONE (redos recorded by vm4 for all 25; 19 cleared; six open items re-audited by vm3 and vm5 with gaps in reports/skip-verdict-audit.md; the open items need a redo fix by the claimant, not a new take).

- Capability tags: review, draft.
- Intended host: any pool Linux host. Claim under `claims/` before starting (WORKFLOW.md).
- Posted by: main agent (vm1), 2026-10-19, at the direction of Nick.

The vm1 honesty audit marked every verdict in this batch suspect: the recorded verdict in `material/skip-audit/queue-current.json` rests on an earlier audit or reading, Jira was not re-read for the closeout, and the ticket was not re-checked at the pinned head. A verdict in this batch is provisional, not done. The full reasons per ticket are in `material/skip-audit/remediation-current.json` and in `reports/skip-verdict-audit.md`.

For each ticket, in the order listed: claim it as `claims/skip-<KEY>-<your host>.md`, then redo the closeout under steps 11 to 15 of `pool/cron-prompt.md` at the pinned head `498a09aea97aac86c2cc7ac114113b946d249942`. The earlier audit, B2, B3 or C reading is background only. The new record must cite the Jira status read in this pass, the code path with file and line or the reproduction attempt and its result, and the pinned head. Update the two draft files on the draft branch with a normal push, record the verdict with `queue_tool.py set`, and mark the claim DONE naming the remediation batch and the new draft branch head.

## Items

1. **SOLR-17820**, recorded verdict `keep-open` on `solr-17820-draft-open`. Start from: `material/skip-audit/remediation-current.json` (item SOLR-17820) and `reports/skip-verdict-audit.md`. Output: `claims/skip-SOLR-17820-<host>.md` DONE, updated drafts on the branch, and one line in the batch report.
2. **SOLR-17763**, recorded verdict `close-fixed` on `solr-17763-draft-close`. Start from: `material/skip-audit/remediation-current.json` (item SOLR-17763) and `reports/skip-verdict-audit.md`. Output: `claims/skip-SOLR-17763-<host>.md` DONE, updated drafts on the branch, and one line in the batch report.
3. **SOLR-17751**, recorded verdict `keep-open` on `solr-17751-draft-open`. Start from: `material/skip-audit/remediation-current.json` (item SOLR-17751) and `reports/skip-verdict-audit.md`. Output: `claims/skip-SOLR-17751-<host>.md` DONE, updated drafts on the branch, and one line in the batch report.
4. **SOLR-17610**, recorded verdict `keep-open` on `solr-17610-draft-open`. Start from: `material/skip-audit/remediation-current.json` (item SOLR-17610) and `reports/skip-verdict-audit.md`. Output: `claims/skip-SOLR-17610-<host>.md` DONE, updated drafts on the branch, and one line in the batch report.
5. **SOLR-17509**, recorded verdict `keep-open` on `solr-17509-draft-open`. Start from: `material/skip-audit/remediation-current.json` (item SOLR-17509) and `reports/skip-verdict-audit.md`. Output: `claims/skip-SOLR-17509-<host>.md` DONE, updated drafts on the branch, and one line in the batch report.
6. **SOLR-17376**, recorded verdict `keep-open` on `solr-17376-draft-open`. Start from: `material/skip-audit/remediation-current.json` (item SOLR-17376) and `reports/skip-verdict-audit.md`. Output: `claims/skip-SOLR-17376-<host>.md` DONE, updated drafts on the branch, and one line in the batch report.
7. **SOLR-17259**, recorded verdict `keep-open` on `solr-17259-draft-open`. Start from: `material/skip-audit/remediation-current.json` (item SOLR-17259) and `reports/skip-verdict-audit.md`. Output: `claims/skip-SOLR-17259-<host>.md` DONE, updated drafts on the branch, and one line in the batch report.
8. **SOLR-17224**, recorded verdict `close-fixed` on `solr-17224-draft-close`. Start from: `material/skip-audit/remediation-current.json` (item SOLR-17224) and `reports/skip-verdict-audit.md`. Output: `claims/skip-SOLR-17224-<host>.md` DONE, updated drafts on the branch, and one line in the batch report.
9. **SOLR-17154**, recorded verdict `keep-open` on `solr-17154-draft-open`. Start from: `material/skip-audit/remediation-current.json` (item SOLR-17154) and `reports/skip-verdict-audit.md`. Output: `claims/skip-SOLR-17154-<host>.md` DONE, updated drafts on the branch, and one line in the batch report.
10. **SOLR-16786**, recorded verdict `keep-open` on `solr-16786-draft-open`. Start from: `material/skip-audit/remediation-current.json` (item SOLR-16786) and `reports/skip-verdict-audit.md`. Output: `claims/skip-SOLR-16786-<host>.md` DONE, updated drafts on the branch, and one line in the batch report.
11. **SOLR-16785**, recorded verdict `keep-open` on `solr-16785-draft-open`. Start from: `material/skip-audit/remediation-current.json` (item SOLR-16785) and `reports/skip-verdict-audit.md`. Output: `claims/skip-SOLR-16785-<host>.md` DONE, updated drafts on the branch, and one line in the batch report.
12. **SOLR-16609**, recorded verdict `keep-open` on `solr-16609-draft-open`. Start from: `material/skip-audit/remediation-current.json` (item SOLR-16609) and `reports/skip-verdict-audit.md`. Output: `claims/skip-SOLR-16609-<host>.md` DONE, updated drafts on the branch, and one line in the batch report.
13. **SOLR-16548**, recorded verdict `keep-open` on `solr-16548-draft-open`. Start from: `material/skip-audit/remediation-current.json` (item SOLR-16548) and `reports/skip-verdict-audit.md`. Output: `claims/skip-SOLR-16548-<host>.md` DONE, updated drafts on the branch, and one line in the batch report.
14. **SOLR-16526**, recorded verdict `keep-open` on `solr-16526-draft-open`. Start from: `material/skip-audit/remediation-current.json` (item SOLR-16526) and `reports/skip-verdict-audit.md`. Output: `claims/skip-SOLR-16526-<host>.md` DONE, updated drafts on the branch, and one line in the batch report.
15. **SOLR-17086**, recorded verdict `keep-open` on `solr-17086-draft-open`. Start from: `material/skip-audit/remediation-current.json` (item SOLR-17086) and `reports/skip-verdict-audit.md`. Output: `claims/skip-SOLR-17086-<host>.md` DONE, updated drafts on the branch, and one line in the batch report.
16. **SOLR-17003**, recorded verdict `keep-open` on `solr-17003-draft-open`. Start from: `material/skip-audit/remediation-current.json` (item SOLR-17003) and `reports/skip-verdict-audit.md`. Output: `claims/skip-SOLR-17003-<host>.md` DONE, updated drafts on the branch, and one line in the batch report.
17. **SOLR-16930**, recorded verdict `keep-open` on `solr-16930-draft-open`. Start from: `material/skip-audit/remediation-current.json` (item SOLR-16930) and `reports/skip-verdict-audit.md`. Output: `claims/skip-SOLR-16930-<host>.md` DONE, updated drafts on the branch, and one line in the batch report.
18. **SOLR-16772**, recorded verdict `keep-open` on `solr-16772-draft-open`. Start from: `material/skip-audit/remediation-current.json` (item SOLR-16772) and `reports/skip-verdict-audit.md`. Output: `claims/skip-SOLR-16772-<host>.md` DONE, updated drafts on the branch, and one line in the batch report.
19. **SOLR-16758**, recorded verdict `keep-open` on `solr-16758-draft-open`. Start from: `material/skip-audit/remediation-current.json` (item SOLR-16758) and `reports/skip-verdict-audit.md`. Output: `claims/skip-SOLR-16758-<host>.md` DONE, updated drafts on the branch, and one line in the batch report.
20. **SOLR-16657**, recorded verdict `keep-open` on `solr-16657-draft-open`. Start from: `material/skip-audit/remediation-current.json` (item SOLR-16657) and `reports/skip-verdict-audit.md`. Output: `claims/skip-SOLR-16657-<host>.md` DONE, updated drafts on the branch, and one line in the batch report.
21. **SOLR-16652**, recorded verdict `keep-open` on `solr-16652-draft-open`. Start from: `material/skip-audit/remediation-current.json` (item SOLR-16652) and `reports/skip-verdict-audit.md`. Output: `claims/skip-SOLR-16652-<host>.md` DONE, updated drafts on the branch, and one line in the batch report.
22. **SOLR-16792**, recorded verdict `keep-open` on `solr-16792-draft-open`. Start from: `material/skip-audit/remediation-current.json` (item SOLR-16792) and `reports/skip-verdict-audit.md`. Output: `claims/skip-SOLR-16792-<host>.md` DONE, updated drafts on the branch, and one line in the batch report.
23. **SOLR-17133**, recorded verdict `keep-open` on `solr-17133-draft-open`. Start from: `material/skip-audit/remediation-current.json` (item SOLR-17133) and `reports/skip-verdict-audit.md`. Output: `claims/skip-SOLR-17133-<host>.md` DONE, updated drafts on the branch, and one line in the batch report.
24. **SOLR-16355**, recorded verdict `keep-open` on `solr-16355-draft-open`. Start from: `material/skip-audit/remediation-current.json` (item SOLR-16355) and `reports/skip-verdict-audit.md`. Output: `claims/skip-SOLR-16355-<host>.md` DONE, updated drafts on the branch, and one line in the batch report.
25. **SOLR-16305**, recorded verdict `keep-open` on `solr-16305-draft-open`. Start from: `material/skip-audit/remediation-current.json` (item SOLR-16305) and `reports/skip-verdict-audit.md`. Output: `claims/skip-SOLR-16305-<host>.md` DONE, updated drafts on the branch, and one line in the batch report.

## Decisions

| Decision | Answer or default | Condition and action | Owner |
| --- | --- | --- | --- |
| Verdict after the redo | decided: record the verdict the fresh evidence supports, even if it differs from the recorded one | if the verdict changes, use the draft branch name step 13 of the tick prompt requires for the new verdict and say so in the batch report | Nick |
| Acceptance | decided: a ticket is remediated only when the vm1 honesty audit re-checks it and passes it | the claimant marks the per-ticket claim DONE and writes the batch report line; nobody declares audit acceptance on the factory side | Nick |
| Evidence floor | decided: the earlier reading never counts as the re-check | if the fresh evidence cannot meet the rubric floor, record keep-open naming the missing piece instead of closing | Nick |

## Deliverable

`reports/skip-verdict-remediation-01.md`: one line per item, with the ticket, the claim file, the verdict recorded, the new draft branch head, and the evidence the new record cites.

## Limits

- No PR, comment, JIRA post or email. No force push. Draft branches take normal pushes only.
- One ticket at a time per host. Claim before work; the push is the lock.
- Commit as Nick Shanin with no trailers. No em dashes.
