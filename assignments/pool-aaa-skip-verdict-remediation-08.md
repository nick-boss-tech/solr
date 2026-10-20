# Pool assignment: skip verdict remediation, batch 8 of 8

Status: DONE (vm5, claims/pool-aaa-skip-verdict-remediation-08.md, closed 2026-10-20T07:05:32Z)

- Capability tags: review, draft.
- Intended host: any pool Linux host. Claim under `claims/` before starting (WORKFLOW.md).
- Posted by: main agent (vm1), 2026-10-19, at the direction of Nick.

The vm1 honesty audit marked every verdict in this batch suspect: the recorded verdict in `material/skip-audit/queue-current.json` rests on an earlier audit or reading, Jira was not re-read for the closeout, and the ticket was not re-checked at the pinned head. A verdict in this batch is provisional, not done. The full reasons per ticket are in `material/skip-audit/remediation-current.json` and in `reports/skip-verdict-audit.md`.

For each ticket, in the order listed: claim it as `claims/skip-<KEY>-<your host>.md`, then redo the closeout under steps 11 to 15 of `pool/cron-prompt.md` at the pinned head `498a09aea97aac86c2cc7ac114113b946d249942`. The earlier audit, B2, B3 or C reading is background only. The new record must cite the Jira status read in this pass, the code path with file and line or the reproduction attempt and its result, and the pinned head. Update the two draft files on the draft branch with a normal push, record the verdict with `queue_tool.py set`, and mark the claim DONE naming the remediation batch and the new draft branch head.

## Items

1. **SOLR-12844**, recorded verdict `keep-open` on `solr-12844-draft-open`. Start from: `material/skip-audit/remediation-current.json` (item SOLR-12844) and `reports/skip-verdict-audit.md`. Output: `claims/skip-SOLR-12844-<host>.md` DONE, updated drafts on the branch, and one line in the batch report.
2. **SOLR-12819**, recorded verdict `keep-open` on `solr-12819-draft-open`. Start from: `material/skip-audit/remediation-current.json` (item SOLR-12819) and `reports/skip-verdict-audit.md`. Output: `claims/skip-SOLR-12819-<host>.md` DONE, updated drafts on the branch, and one line in the batch report.
3. **SOLR-12808**, recorded verdict `keep-open` on `solr-12808-draft-open`. Start from: `material/skip-audit/remediation-current.json` (item SOLR-12808) and `reports/skip-verdict-audit.md`. Output: `claims/skip-SOLR-12808-<host>.md` DONE, updated drafts on the branch, and one line in the batch report.
4. **SOLR-12781**, recorded verdict `keep-open` on `solr-12781-draft-open`. Start from: `material/skip-audit/remediation-current.json` (item SOLR-12781) and `reports/skip-verdict-audit.md`. Output: `claims/skip-SOLR-12781-<host>.md` DONE, updated drafts on the branch, and one line in the batch report.
5. **SOLR-12748**, recorded verdict `keep-open` on `solr-12748-draft-open`. Start from: `material/skip-audit/remediation-current.json` (item SOLR-12748) and `reports/skip-verdict-audit.md`. Output: `claims/skip-SOLR-12748-<host>.md` DONE, updated drafts on the branch, and one line in the batch report.
6. **SOLR-12688**, recorded verdict `keep-open` on `solr-12688-draft-open`. Start from: `material/skip-audit/remediation-current.json` (item SOLR-12688) and `reports/skip-verdict-audit.md`. Output: `claims/skip-SOLR-12688-<host>.md` DONE, updated drafts on the branch, and one line in the batch report.
7. **SOLR-12659**, recorded verdict `keep-open` on `solr-12659-draft-open`. Start from: `material/skip-audit/remediation-current.json` (item SOLR-12659) and `reports/skip-verdict-audit.md`. Output: `claims/skip-SOLR-12659-<host>.md` DONE, updated drafts on the branch, and one line in the batch report.
8. **SOLR-12646**, recorded verdict `close-obsolete` on `solr-12646-draft-close`. Start from: `material/skip-audit/remediation-current.json` (item SOLR-12646) and `reports/skip-verdict-audit.md`. Output: `claims/skip-SOLR-12646-<host>.md` DONE, updated drafts on the branch, and one line in the batch report.
9. **SOLR-12645**, recorded verdict `close-obsolete` on `solr-12645-draft-close`. Start from: `material/skip-audit/remediation-current.json` (item SOLR-12645) and `reports/skip-verdict-audit.md`. Output: `claims/skip-SOLR-12645-<host>.md` DONE, updated drafts on the branch, and one line in the batch report.
10. **SOLR-12585**, recorded verdict `keep-open` on `solr-12585-draft-open`. Start from: `material/skip-audit/remediation-current.json` (item SOLR-12585) and `reports/skip-verdict-audit.md`. Output: `claims/skip-SOLR-12585-<host>.md` DONE, updated drafts on the branch, and one line in the batch report.
11. **SOLR-12542**, recorded verdict `keep-open` on `solr-12542-draft-open`. Start from: `material/skip-audit/remediation-current.json` (item SOLR-12542) and `reports/skip-verdict-audit.md`. Output: `claims/skip-SOLR-12542-<host>.md` DONE, updated drafts on the branch, and one line in the batch report.
12. **SOLR-12504**, recorded verdict `keep-open` on `solr-12504-draft-open`. Start from: `material/skip-audit/remediation-current.json` (item SOLR-12504) and `reports/skip-verdict-audit.md`. Output: `claims/skip-SOLR-12504-<host>.md` DONE, updated drafts on the branch, and one line in the batch report.
13. **SOLR-12487**, recorded verdict `keep-open` on `solr-12487-draft-open`. Start from: `material/skip-audit/remediation-current.json` (item SOLR-12487) and `reports/skip-verdict-audit.md`. Output: `claims/skip-SOLR-12487-<host>.md` DONE, updated drafts on the branch, and one line in the batch report.
14. **SOLR-12460**, recorded verdict `keep-open` on `solr-12460-draft-open`. Start from: `material/skip-audit/remediation-current.json` (item SOLR-12460) and `reports/skip-verdict-audit.md`. Output: `claims/skip-SOLR-12460-<host>.md` DONE, updated drafts on the branch, and one line in the batch report.
15. **SOLR-12446**, recorded verdict `keep-open` on `solr-12446-draft-open`. Start from: `material/skip-audit/remediation-current.json` (item SOLR-12446) and `reports/skip-verdict-audit.md`. Output: `claims/skip-SOLR-12446-<host>.md` DONE, updated drafts on the branch, and one line in the batch report.
16. **SOLR-12369**, recorded verdict `keep-open` on `solr-12369-draft-open`. Start from: `material/skip-audit/remediation-current.json` (item SOLR-12369) and `reports/skip-verdict-audit.md`. Output: `claims/skip-SOLR-12369-<host>.md` DONE, updated drafts on the branch, and one line in the batch report.
17. **SOLR-12363**, recorded verdict `keep-open` on `solr-12363-draft-open`. Start from: `material/skip-audit/remediation-current.json` (item SOLR-12363) and `reports/skip-verdict-audit.md`. Output: `claims/skip-SOLR-12363-<host>.md` DONE, updated drafts on the branch, and one line in the batch report.
18. **SOLR-12340**, recorded verdict `keep-open` on `solr-12340-draft-open`. Start from: `material/skip-audit/remediation-current.json` (item SOLR-12340) and `reports/skip-verdict-audit.md`. Output: `claims/skip-SOLR-12340-<host>.md` DONE, updated drafts on the branch, and one line in the batch report.
19. **SOLR-12315**, recorded verdict `keep-open` on `solr-12315-draft-open`. Start from: `material/skip-audit/remediation-current.json` (item SOLR-12315) and `reports/skip-verdict-audit.md`. Output: `claims/skip-SOLR-12315-<host>.md` DONE, updated drafts on the branch, and one line in the batch report.
20. **SOLR-12302**, recorded verdict `keep-open` on `solr-12302-draft-open`. Start from: `material/skip-audit/remediation-current.json` (item SOLR-12302) and `reports/skip-verdict-audit.md`. Output: `claims/skip-SOLR-12302-<host>.md` DONE, updated drafts on the branch, and one line in the batch report.
21. **SOLR-12300**, recorded verdict `keep-open` on `solr-12300-draft-open`. Start from: `material/skip-audit/remediation-current.json` (item SOLR-12300) and `reports/skip-verdict-audit.md`. Output: `claims/skip-SOLR-12300-<host>.md` DONE, updated drafts on the branch, and one line in the batch report.
22. **SOLR-12296**, recorded verdict `keep-open` on `solr-12296-draft-open`. Start from: `material/skip-audit/remediation-current.json` (item SOLR-12296) and `reports/skip-verdict-audit.md`. Output: `claims/skip-SOLR-12296-<host>.md` DONE, updated drafts on the branch, and one line in the batch report.
23. **SOLR-12268**, recorded verdict `keep-open` on `solr-12268-draft-open`. Start from: `material/skip-audit/remediation-current.json` (item SOLR-12268) and `reports/skip-verdict-audit.md`. Output: `claims/skip-SOLR-12268-<host>.md` DONE, updated drafts on the branch, and one line in the batch report.
24. **SOLR-12263**, recorded verdict `keep-open` on `solr-12263-draft-open`. Start from: `material/skip-audit/remediation-current.json` (item SOLR-12263) and `reports/skip-verdict-audit.md`. Output: `claims/skip-SOLR-12263-<host>.md` DONE, updated drafts on the branch, and one line in the batch report.
25. **SOLR-12262**, recorded verdict `keep-open` on `solr-12262-draft-open`. Start from: `material/skip-audit/remediation-current.json` (item SOLR-12262) and `reports/skip-verdict-audit.md`. Output: `claims/skip-SOLR-12262-<host>.md` DONE, updated drafts on the branch, and one line in the batch report.

## Decisions

| Decision | Answer or default | Condition and action | Owner |
| --- | --- | --- | --- |
| Verdict after the redo | decided: record the verdict the fresh evidence supports, even if it differs from the recorded one | if the verdict changes, use the draft branch name step 13 of the tick prompt requires for the new verdict and say so in the batch report | Nick |
| Acceptance | decided: a ticket is remediated only when the vm1 honesty audit re-checks it and passes it | the claimant marks the per-ticket claim DONE and writes the batch report line; nobody declares audit acceptance on the factory side | Nick |
| Evidence floor | decided: the earlier reading never counts as the re-check | if the fresh evidence cannot meet the rubric floor, record keep-open naming the missing piece instead of closing | Nick |

## Deliverable

`reports/skip-verdict-remediation-08.md`: one line per item, with the ticket, the claim file, the verdict recorded, the new draft branch head, and the evidence the new record cites.

## Limits

- No PR, comment, JIRA post or email. No force push. Draft branches take normal pushes only.
- One ticket at a time per host. Claim before work; the push is the lock.
- Commit as Nick Shanin with no trailers. No em dashes.
