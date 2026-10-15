# Pool assignment: mergeIds Jira ticket draft review, round 3 confirmation

Status: DONE

Capability tags: `review`. Intended host: windows. Staffing: 1. Claim path: claims/pool-mergeids-ticket-review-round3.md.

## Scope: confirmation of one correction only

Round 2 (reports/mergeids-ticket-review-round2.md) returned STILL OPEN on one item: the fix option 1 parenthetical in `material/mergeids-ticket-draft.md` listed the four accumulators with numFound before maxScore, while the code accumulates maxScore (1168-1170) before numFound (1171). Items 2 to 4 were confirmed and are not re-opened. The main side settled item 1 for the reviewer and corrected the draft; the settlement is recorded in the "Main-side settlement (2026-10-11)" section appended to reports/mergeids-ticket-review-round2.md.

## Item to confirm

1. Draft line 49, fix option 1: the parenthetical now reads `{{approximateTotalHits}} / {{maxScore}} / {{numFound}} / {{hitCountIsExact}}`. Confirm the order matches the accumulation order at cabedd1d968059215188f4e7563fb303241899ed (approximateTotalHits at 1160-1165, maxScore at 1168-1170, numFound at 1171, hitCountIsExact fold at 1173-1175, all before the skip at 1193-1194), that the membership is unchanged (the same four accumulators), and that no other text in the draft changed with this correction.

Nothing else is in scope. Do not re-open round 1 or round 2 items, and do not edit the draft; flag instead.

## Deliverable

`reports/mergeids-ticket-review-round3.md`, verdict first: SATISFIED or STILL OPEN, one line for the item above (confirmed, or the exact passage and the evidence it disagrees with).

When this review lands SATISFIED, the main side passes the final text of `material/mergeids-ticket-draft.md` to Nick, and Nick files the ticket. The reviewer does not file anything in Jira and does not create the ticket.

Rules: WORKFLOW.md binds (claim before work, hourly heartbeats, subagent cap of 6 to 7 across all hosts, and mark the claim DONE in the same push as the deliverable). No em dashes in authored text.
