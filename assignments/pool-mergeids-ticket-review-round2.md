# Pool assignment: mergeIds Jira ticket draft review, round 2 confirmation

Capability tags: `review`. Intended host: windows. Staffing: 1. Claim path: claims/pool-mergeids-ticket-review-round2.md.

## Scope: confirmation only

Round 1 (reports/mergeids-ticket-review.md) returned STILL OPEN with two flagged disagreements (D1, D2), one framing confirmation (O1), and four fixes (F1 to F4) applied to `material/mergeids-ticket-draft.md`. The main side adjudicated every item against the verification evidence; the settlements and the one draft correction are recorded in the "Main-side adjudication (2026-10-11)" section appended to reports/mergeids-ticket-review.md. This round confirms only the items below. Do not re-open the criteria that round 1 checked clean (title, numbers and lines outside the passages named here, markup, disclosure form, process vocabulary, "we"), and do not edit passages outside this scope; flag instead.

## Items to confirm

1. D1 correction, fix option 1 in `material/mergeids-ticket-draft.md`: the parenthetical now lists `{{approximateTotalHits}} / {{numFound}} / {{maxScore}} / {{hitCountIsExact}}` accumulation to move below the skip. Confirm the list is complete against the accumulation order in the evidence (approximateTotalHits at lines 1160-1165, maxScore at 1168-1170, numFound at 1171, hitCountIsExact fold at 1173-1175, all before the skip at 1193-1194), and that the correction changed nothing else in the Fix options section.
2. D2 settlement, no ticket change: the Mechanism sentence "The only downward adjustment, {{numFound--}} at line 1225" stands as scoped to `mergeIds`. The adjudication's counter-evidence is `regularFinishStage` (lines 754-770, the decrement at 762), a fields-retrieval finish-stage adjustment that fires only for documents in the merged list, so it cannot compensate for a wholesale skip. Confirm the reasoning reads correctly against the code at cabedd1d968059215188f4e7563fb303241899ed, or flag where it does not.
3. O1 settlement, no ticket change: the `numFoundExact=true` framing stands. The ticket claims the merged count includes a shard that contributed no documents and is not the count of the merged result; it does not claim a shard's own count is inexact. Confirm the Symptom and title text still say only that, and that nothing in the adjudicated passages overstates it.
4. F1 to F4 passages as they now stand in the draft: the AI header line (F1), the Symptom parenthetical naming SOLR-14758 and SOLR-15595 (F2), the Reproduction shard2 values including `{{numFoundExact=true}}` (F3), and the option 2 closing sentence (F4). Confirm each matches the evidence values and facts recorded in the adjudication section, with no number, line reference, or claim altered.

## Deliverable

`reports/mergeids-ticket-review-round2.md`, verdict first: SATISFIED or STILL OPEN, one line per item above (confirmed, or the exact passage and the evidence it disagrees with). If a passage in scope is wrong, flag it with before and after text rather than editing the draft in this round.

When this review lands SATISFIED, the main side passes the final text of `material/mergeids-ticket-draft.md` to Nick, and Nick files the ticket. The reviewer does not file anything in Jira and does not create the ticket.

Rules: WORKFLOW.md binds (claim before work, hourly heartbeats, subagent cap of 6 to 7 across all hosts, and mark the claim DONE in the same push as the deliverable). No em dashes in authored text.
