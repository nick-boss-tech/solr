# Pool assignment: skip verdict remediation, batch 6 of 8

Status: DONE (vm5, claims/pool-aaa-skip-verdict-remediation-06.md, 2026-10-20T01:41:26Z; deliverables in reports/skip-verdict-remediation-06.md)

- Capability tags: review, draft.
- Intended host: any pool Linux host. Claim under `claims/` before starting (WORKFLOW.md).
- Posted by: main agent (vm1), 2026-10-19, at the direction of Nick.

The vm1 honesty audit marked every verdict in this batch suspect: the recorded verdict in `material/skip-audit/queue-current.json` rests on an earlier audit or reading, Jira was not re-read for the closeout, and the ticket was not re-checked at the pinned head. A verdict in this batch is provisional, not done. The full reasons per ticket are in `material/skip-audit/remediation-current.json` and in `reports/skip-verdict-audit.md`.

For each ticket, in the order listed: claim it as `claims/skip-<KEY>-<your host>.md`, then redo the closeout under steps 11 to 15 of `pool/cron-prompt.md` at the pinned head `498a09aea97aac86c2cc7ac114113b946d249942`. The earlier audit, B2, B3 or C reading is background only. The new record must cite the Jira status read in this pass, the code path with file and line or the reproduction attempt and its result, and the pinned head. Update the two draft files on the draft branch with a normal push, record the verdict with `queue_tool.py set`, and mark the claim DONE naming the remediation batch and the new draft branch head.

## Items

1. **SOLR-13698**, recorded verdict `keep-open` on `solr-13698-draft-open`. Start from: `material/skip-audit/remediation-current.json` (item SOLR-13698) and `reports/skip-verdict-audit.md`. Output: `claims/skip-SOLR-13698-<host>.md` DONE, updated drafts on the branch, and one line in the batch report.
2. **SOLR-13687**, recorded verdict `keep-open` on `solr-13687-draft-open`. Start from: `material/skip-audit/remediation-current.json` (item SOLR-13687) and `reports/skip-verdict-audit.md`. Output: `claims/skip-SOLR-13687-<host>.md` DONE, updated drafts on the branch, and one line in the batch report.
3. **SOLR-13678**, recorded verdict `close-fixed` on `solr-13678-draft-close`. Start from: `material/skip-audit/remediation-current.json` (item SOLR-13678) and `reports/skip-verdict-audit.md`. Output: `claims/skip-SOLR-13678-<host>.md` DONE, updated drafts on the branch, and one line in the batch report.
4. **SOLR-13654**, recorded verdict `keep-open` on `solr-13654-draft-open`. Start from: `material/skip-audit/remediation-current.json` (item SOLR-13654) and `reports/skip-verdict-audit.md`. Output: `claims/skip-SOLR-13654-<host>.md` DONE, updated drafts on the branch, and one line in the batch report.
5. **SOLR-13627**, recorded verdict `keep-open` on `solr-13627-draft-open`. Start from: `material/skip-audit/remediation-current.json` (item SOLR-13627) and `reports/skip-verdict-audit.md`. Output: `claims/skip-SOLR-13627-<host>.md` DONE, updated drafts on the branch, and one line in the batch report.
6. **SOLR-13618**, recorded verdict `keep-open` on `solr-13618-draft-open`. Start from: `material/skip-audit/remediation-current.json` (item SOLR-13618) and `reports/skip-verdict-audit.md`. Output: `claims/skip-SOLR-13618-<host>.md` DONE, updated drafts on the branch, and one line in the batch report.
7. **SOLR-13616**, recorded verdict `keep-open` on `solr-13616-draft-open`. Start from: `material/skip-audit/remediation-current.json` (item SOLR-13616) and `reports/skip-verdict-audit.md`. Output: `claims/skip-SOLR-13616-<host>.md` DONE, updated drafts on the branch, and one line in the batch report.
8. **SOLR-13591**, recorded verdict `close-obsolete` on `solr-13591-draft-close`. Start from: `material/skip-audit/remediation-current.json` (item SOLR-13591) and `reports/skip-verdict-audit.md`. Output: `claims/skip-SOLR-13591-<host>.md` DONE, updated drafts on the branch, and one line in the batch report.
9. **SOLR-13572**, recorded verdict `keep-open` on `solr-13572-draft-open`. Start from: `material/skip-audit/remediation-current.json` (item SOLR-13572) and `reports/skip-verdict-audit.md`. Output: `claims/skip-SOLR-13572-<host>.md` DONE, updated drafts on the branch, and one line in the batch report.
10. **SOLR-13570**, recorded verdict `close-fixed` on `solr-13570-draft-close`. Start from: `material/skip-audit/remediation-current.json` (item SOLR-13570) and `reports/skip-verdict-audit.md`. Output: `claims/skip-SOLR-13570-<host>.md` DONE, updated drafts on the branch, and one line in the batch report.
11. **SOLR-13505**, recorded verdict `close-obsolete` on `solr-13505-draft-close`. Start from: `material/skip-audit/remediation-current.json` (item SOLR-13505) and `reports/skip-verdict-audit.md`. Output: `claims/skip-SOLR-13505-<host>.md` DONE, updated drafts on the branch, and one line in the batch report.
12. **SOLR-13501**, recorded verdict `close-obsolete` on `solr-13501-draft-close`. Start from: `material/skip-audit/remediation-current.json` (item SOLR-13501) and `reports/skip-verdict-audit.md`. Output: `claims/skip-SOLR-13501-<host>.md` DONE, updated drafts on the branch, and one line in the batch report.
13. **SOLR-13486**, recorded verdict `keep-open` on `solr-13486-draft-open`. Start from: `material/skip-audit/remediation-current.json` (item SOLR-13486) and `reports/skip-verdict-audit.md`. Output: `claims/skip-SOLR-13486-<host>.md` DONE, updated drafts on the branch, and one line in the batch report.
14. **SOLR-13481**, recorded verdict `close-obsolete` on `solr-13481-draft-close`. Start from: `material/skip-audit/remediation-current.json` (item SOLR-13481) and `reports/skip-verdict-audit.md`. Output: `claims/skip-SOLR-13481-<host>.md` DONE, updated drafts on the branch, and one line in the batch report.
15. **SOLR-13471**, recorded verdict `keep-open` on `solr-13471-draft-open`. Start from: `material/skip-audit/remediation-current.json` (item SOLR-13471) and `reports/skip-verdict-audit.md`. Output: `claims/skip-SOLR-13471-<host>.md` DONE, updated drafts on the branch, and one line in the batch report.
16. **SOLR-13470**, recorded verdict `keep-open` on `solr-13470-draft-open`. Start from: `material/skip-audit/remediation-current.json` (item SOLR-13470) and `reports/skip-verdict-audit.md`. Output: `claims/skip-SOLR-13470-<host>.md` DONE, updated drafts on the branch, and one line in the batch report.
17. **SOLR-13455**, recorded verdict `keep-open` on `solr-13455-draft-open`. Start from: `material/skip-audit/remediation-current.json` (item SOLR-13455) and `reports/skip-verdict-audit.md`. Output: `claims/skip-SOLR-13455-<host>.md` DONE, updated drafts on the branch, and one line in the batch report.
18. **SOLR-13416**, recorded verdict `keep-open` on `solr-13416-draft-open`. Start from: `material/skip-audit/remediation-current.json` (item SOLR-13416) and `reports/skip-verdict-audit.md`. Output: `claims/skip-SOLR-13416-<host>.md` DONE, updated drafts on the branch, and one line in the batch report.
19. **SOLR-13390**, recorded verdict `keep-open` on `solr-13390-draft-open`. Start from: `material/skip-audit/remediation-current.json` (item SOLR-13390) and `reports/skip-verdict-audit.md`. Output: `claims/skip-SOLR-13390-<host>.md` DONE, updated drafts on the branch, and one line in the batch report.
20. **SOLR-13346**, recorded verdict `keep-open` on `solr-13346-draft-open`. Start from: `material/skip-audit/remediation-current.json` (item SOLR-13346) and `reports/skip-verdict-audit.md`. Output: `claims/skip-SOLR-13346-<host>.md` DONE, updated drafts on the branch, and one line in the batch report.
21. **SOLR-13310**, recorded verdict `keep-open` on `solr-13310-draft-open`. Start from: `material/skip-audit/remediation-current.json` (item SOLR-13310) and `reports/skip-verdict-audit.md`. Output: `claims/skip-SOLR-13310-<host>.md` DONE, updated drafts on the branch, and one line in the batch report.
22. **SOLR-13305**, recorded verdict `keep-open` on `solr-13305-draft-open`. Start from: `material/skip-audit/remediation-current.json` (item SOLR-13305) and `reports/skip-verdict-audit.md`. Output: `claims/skip-SOLR-13305-<host>.md` DONE, updated drafts on the branch, and one line in the batch report.
23. **SOLR-13304**, recorded verdict `close-obsolete` on `solr-13304-draft-close`. Start from: `material/skip-audit/remediation-current.json` (item SOLR-13304) and `reports/skip-verdict-audit.md`. Output: `claims/skip-SOLR-13304-<host>.md` DONE, updated drafts on the branch, and one line in the batch report.
24. **SOLR-13291**, recorded verdict `keep-open` on `solr-13291-draft-open`. Start from: `material/skip-audit/remediation-current.json` (item SOLR-13291) and `reports/skip-verdict-audit.md`. Output: `claims/skip-SOLR-13291-<host>.md` DONE, updated drafts on the branch, and one line in the batch report.
25. **SOLR-13283**, recorded verdict `keep-open` on `solr-13283-draft-open`. Start from: `material/skip-audit/remediation-current.json` (item SOLR-13283) and `reports/skip-verdict-audit.md`. Output: `claims/skip-SOLR-13283-<host>.md` DONE, updated drafts on the branch, and one line in the batch report.

## Decisions

| Decision | Answer or default | Condition and action | Owner |
| --- | --- | --- | --- |
| Verdict after the redo | decided: record the verdict the fresh evidence supports, even if it differs from the recorded one | if the verdict changes, use the draft branch name step 13 of the tick prompt requires for the new verdict and say so in the batch report | Nick |
| Acceptance | decided: a ticket is remediated only when the vm1 honesty audit re-checks it and passes it | the claimant marks the per-ticket claim DONE and writes the batch report line; nobody declares audit acceptance on the factory side | Nick |
| Evidence floor | decided: the earlier reading never counts as the re-check | if the fresh evidence cannot meet the rubric floor, record keep-open naming the missing piece instead of closing | Nick |

## Deliverable

`reports/skip-verdict-remediation-06.md`: one line per item, with the ticket, the claim file, the verdict recorded, the new draft branch head, and the evidence the new record cites.

## Limits

- No PR, comment, JIRA post or email. No force push. Draft branches take normal pushes only.
- One ticket at a time per host. Claim before work; the push is the lock.
- Commit as Nick Shanin with no trailers. No em dashes.
