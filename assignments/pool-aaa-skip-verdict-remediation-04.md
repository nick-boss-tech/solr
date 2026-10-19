# Pool assignment: skip verdict remediation, batch 4 of 8

Status: OPEN (released 2026-10-19: the owner stood vm2 down and paused factory work; vm2's claim is RELEASED in claims/pool-aaa-skip-verdict-remediation-04.md. Items 1 to 21 are DONE per that claim's heartbeats and reports/skip-verdict-remediation-04.md; items 22 to 25 remain and are claimable by any available Linux host.)

- Capability tags: review, draft.
- Intended host: any pool Linux host. Claim under `claims/` before starting (WORKFLOW.md).
- Posted by: main agent (vm1), 2026-10-19, at the direction of Nick.

The vm1 honesty audit marked every verdict in this batch suspect: the recorded verdict in `material/skip-audit/queue-current.json` rests on an earlier audit or reading, Jira was not re-read for the closeout, and the ticket was not re-checked at the pinned head. A verdict in this batch is provisional, not done. The full reasons per ticket are in `material/skip-audit/remediation-current.json` and in `reports/skip-verdict-audit.md`.

For each ticket, in the order listed: claim it as `claims/skip-<KEY>-<your host>.md`, then redo the closeout under steps 11 to 15 of `pool/cron-prompt.md` at the pinned head `498a09aea97aac86c2cc7ac114113b946d249942`. The earlier audit, B2, B3 or C reading is background only. The new record must cite the Jira status read in this pass, the code path with file and line or the reproduction attempt and its result, and the pinned head. Update the two draft files on the draft branch with a normal push, record the verdict with `queue_tool.py set`, and mark the claim DONE naming the remediation batch and the new draft branch head.

## Items

1. **SOLR-15023**, recorded verdict `keep-open` on `solr-15023-draft-open`. Start from: `material/skip-audit/remediation-current.json` (item SOLR-15023) and `reports/skip-verdict-audit.md`. Output: `claims/skip-SOLR-15023-<host>.md` DONE, updated drafts on the branch, and one line in the batch report.
2. **SOLR-14966**, recorded verdict `close-fixed` on `solr-14966-draft-close`. Start from: `material/skip-audit/remediation-current.json` (item SOLR-14966) and `reports/skip-verdict-audit.md`. Output: `claims/skip-SOLR-14966-<host>.md` DONE, updated drafts on the branch, and one line in the batch report.
3. **SOLR-14881**, recorded verdict `keep-open` on `solr-14881-draft-open`. Start from: `material/skip-audit/remediation-current.json` (item SOLR-14881) and `reports/skip-verdict-audit.md`. Output: `claims/skip-SOLR-14881-<host>.md` DONE, updated drafts on the branch, and one line in the batch report.
4. **SOLR-14861**, recorded verdict `keep-open` on `solr-14861-draft-open`. Start from: `material/skip-audit/remediation-current.json` (item SOLR-14861) and `reports/skip-verdict-audit.md`. Output: `claims/skip-SOLR-14861-<host>.md` DONE, updated drafts on the branch, and one line in the batch report.
5. **SOLR-14784**, recorded verdict `keep-open` on `solr-14784-draft-open`. Start from: `material/skip-audit/remediation-current.json` (item SOLR-14784) and `reports/skip-verdict-audit.md`. Output: `claims/skip-SOLR-14784-<host>.md` DONE, updated drafts on the branch, and one line in the batch report.
6. **SOLR-14756**, recorded verdict `keep-open` on `solr-14756-draft-open`. Start from: `material/skip-audit/remediation-current.json` (item SOLR-14756) and `reports/skip-verdict-audit.md`. Output: `claims/skip-SOLR-14756-<host>.md` DONE, updated drafts on the branch, and one line in the batch report.
7. **SOLR-14668**, recorded verdict `keep-open` on `solr-14668-draft-open`. Start from: `material/skip-audit/remediation-current.json` (item SOLR-14668) and `reports/skip-verdict-audit.md`. Output: `claims/skip-SOLR-14668-<host>.md` DONE, updated drafts on the branch, and one line in the batch report.
8. **SOLR-14648**, recorded verdict `keep-open` on `solr-14648-draft-open`. Start from: `material/skip-audit/remediation-current.json` (item SOLR-14648) and `reports/skip-verdict-audit.md`. Output: `claims/skip-SOLR-14648-<host>.md` DONE, updated drafts on the branch, and one line in the batch report.
9. **SOLR-14627**, recorded verdict `keep-open` on `solr-14627-draft-open`. Start from: `material/skip-audit/remediation-current.json` (item SOLR-14627) and `reports/skip-verdict-audit.md`. Output: `claims/skip-SOLR-14627-<host>.md` DONE, updated drafts on the branch, and one line in the batch report.
10. **SOLR-14612**, recorded verdict `keep-open` on `solr-14612-draft-open`. Start from: `material/skip-audit/remediation-current.json` (item SOLR-14612) and `reports/skip-verdict-audit.md`. Output: `claims/skip-SOLR-14612-<host>.md` DONE, updated drafts on the branch, and one line in the batch report.
11. **SOLR-14601**, recorded verdict `keep-open` on `solr-14601-draft-open`. Start from: `material/skip-audit/remediation-current.json` (item SOLR-14601) and `reports/skip-verdict-audit.md`. Output: `claims/skip-SOLR-14601-<host>.md` DONE, updated drafts on the branch, and one line in the batch report.
12. **SOLR-14580**, recorded verdict `keep-open` on `solr-14580-draft-open`. Start from: `material/skip-audit/remediation-current.json` (item SOLR-14580) and `reports/skip-verdict-audit.md`. Output: `claims/skip-SOLR-14580-<host>.md` DONE, updated drafts on the branch, and one line in the batch report.
13. **SOLR-14530**, recorded verdict `keep-open` on `solr-14530-draft-open`. Start from: `material/skip-audit/remediation-current.json` (item SOLR-14530) and `reports/skip-verdict-audit.md`. Output: `claims/skip-SOLR-14530-<host>.md` DONE, updated drafts on the branch, and one line in the batch report.
14. **SOLR-14493**, recorded verdict `keep-open` on `solr-14493-draft-open`. Start from: `material/skip-audit/remediation-current.json` (item SOLR-14493) and `reports/skip-verdict-audit.md`. Output: `claims/skip-SOLR-14493-<host>.md` DONE, updated drafts on the branch, and one line in the batch report.
15. **SOLR-14487**, recorded verdict `keep-open` on `solr-14487-draft-open`. Start from: `material/skip-audit/remediation-current.json` (item SOLR-14487) and `reports/skip-verdict-audit.md`. Output: `claims/skip-SOLR-14487-<host>.md` DONE, updated drafts on the branch, and one line in the batch report.
16. **SOLR-14434**, recorded verdict `keep-open` on `solr-14434-draft-open`. Start from: `material/skip-audit/remediation-current.json` (item SOLR-14434) and `reports/skip-verdict-audit.md`. Output: `claims/skip-SOLR-14434-<host>.md` DONE, updated drafts on the branch, and one line in the batch report.
17. **SOLR-14427**, recorded verdict `keep-open` on `solr-14427-draft-open`. Start from: `material/skip-audit/remediation-current.json` (item SOLR-14427) and `reports/skip-verdict-audit.md`. Output: `claims/skip-SOLR-14427-<host>.md` DONE, updated drafts on the branch, and one line in the batch report.
18. **SOLR-14288**, recorded verdict `keep-open` on `solr-14288-draft-open`. Start from: `material/skip-audit/remediation-current.json` (item SOLR-14288) and `reports/skip-verdict-audit.md`. Output: `claims/skip-SOLR-14288-<host>.md` DONE, updated drafts on the branch, and one line in the batch report.
19. **SOLR-14282**, recorded verdict `keep-open` on `solr-14282-draft-open`. Start from: `material/skip-audit/remediation-current.json` (item SOLR-14282) and `reports/skip-verdict-audit.md`. Output: `claims/skip-SOLR-14282-<host>.md` DONE, updated drafts on the branch, and one line in the batch report.
20. **SOLR-14278**, recorded verdict `keep-open` on `solr-14278-draft-open`. Start from: `material/skip-audit/remediation-current.json` (item SOLR-14278) and `reports/skip-verdict-audit.md`. Output: `claims/skip-SOLR-14278-<host>.md` DONE, updated drafts on the branch, and one line in the batch report.
21. **SOLR-14226**, recorded verdict `keep-open` on `solr-14226-draft-open`. Start from: `material/skip-audit/remediation-current.json` (item SOLR-14226) and `reports/skip-verdict-audit.md`. Output: `claims/skip-SOLR-14226-<host>.md` DONE, updated drafts on the branch, and one line in the batch report.
22. **SOLR-14201**, recorded verdict `keep-open` on `solr-14201-draft-open`. Start from: `material/skip-audit/remediation-current.json` (item SOLR-14201) and `reports/skip-verdict-audit.md`. Output: `claims/skip-SOLR-14201-<host>.md` DONE, updated drafts on the branch, and one line in the batch report.
23. **SOLR-14183**, recorded verdict `keep-open` on `solr-14183-draft-open`. Start from: `material/skip-audit/remediation-current.json` (item SOLR-14183) and `reports/skip-verdict-audit.md`. Output: `claims/skip-SOLR-14183-<host>.md` DONE, updated drafts on the branch, and one line in the batch report.
24. **SOLR-14180**, recorded verdict `keep-open` on `solr-14180-draft-open`. Start from: `material/skip-audit/remediation-current.json` (item SOLR-14180) and `reports/skip-verdict-audit.md`. Output: `claims/skip-SOLR-14180-<host>.md` DONE, updated drafts on the branch, and one line in the batch report.
25. **SOLR-14178**, recorded verdict `keep-open` on `solr-14178-draft-open`. Start from: `material/skip-audit/remediation-current.json` (item SOLR-14178) and `reports/skip-verdict-audit.md`. Output: `claims/skip-SOLR-14178-<host>.md` DONE, updated drafts on the branch, and one line in the batch report.

## Decisions

| Decision | Answer or default | Condition and action | Owner |
| --- | --- | --- | --- |
| Verdict after the redo | decided: record the verdict the fresh evidence supports, even if it differs from the recorded one | if the verdict changes, use the draft branch name step 13 of the tick prompt requires for the new verdict and say so in the batch report | Nick |
| Acceptance | decided: a ticket is remediated only when the vm1 honesty audit re-checks it and passes it | the claimant marks the per-ticket claim DONE and writes the batch report line; nobody declares audit acceptance on the factory side | Nick |
| Evidence floor | decided: the earlier reading never counts as the re-check | if the fresh evidence cannot meet the rubric floor, record keep-open naming the missing piece instead of closing | Nick |

## Deliverable

`reports/skip-verdict-remediation-04.md`: one line per item, with the ticket, the claim file, the verdict recorded, the new draft branch head, and the evidence the new record cites.

## Limits

- No PR, comment, JIRA post or email. No force push. Draft branches take normal pushes only.
- One ticket at a time per host. Claim before work; the push is the lock.
- Commit as Nick Shanin with no trailers. No em dashes.
