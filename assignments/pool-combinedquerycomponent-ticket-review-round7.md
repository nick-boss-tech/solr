# Pool assignment: CombinedQueryComponent Jira ticket draft review, round 7 confirmation

Status: DONE

Capability tags: `review`. Intended host: windows. Staffing: 2 read-only subagents. Claim path: claims/pool-combinedquerycomponent-ticket-review-round7.md.

## Scope

Round 6 (reports/combinedquerycomponent-ticket-review-round6.md) confirmed the four round 5 edits and returned STILL OPEN on the carry-through sweep, which found five unlabelled general claims. The coordinator applied the five round 6 replacements (d1 to d5) to `material/combinedquerycomponent-ticket-draft.md` in the same push as the round 6 report:

- d1, line 12 (title): scoped to a skipped partial shard "that sends a per-query entry".
- d2, line 20 (bullet 1, first sentence): the condition is "carries a per-query entry for that query but no sort_values", and the sentence ends with the code-reading label and "see Reproduction".
- d3, line 28 (mechanism lead-in): "each shard that sends a per-query entry has its contribution folded in ... (from code reading; see Reproduction):".
- d4, line 56 (the reproduction pins sentence): "(from code reading; confirmed by run only for constructed responses)".
- d5, line 70 (related paragraph): "(from code reading, not run in this ticket)".

## What to check

1. Each of d1 to d5 is in place as written in the round 6 report, in substance and in the sentence it was written for.
2. d2 and d3 are correct against the code at `d76fc14b9857`: a shard with a per-query entry is folded into the per-query totals before the skip at lines 399 to 400, and a shard with no entry is not. A shard with no entry still contributes its approximate count through the header value or the running-total fallback at lines 381 to 387; the draft must not say otherwise.
3. The carry-through rule, as in rounds 3 to 6: every general real-exit claim in the ticket text is labelled, scoped to shards with an entry, or stated as not settled. Sweep the whole draft at the current pr-prepare tip.
4. The edits change no other sentence, and the draft's head, counts and links are unaffected.

Out of scope: the gate job wording, the optional notes in the round 5 report, and anything rounds 1 to 6 confirmed.

## Deliverable

`reports/combinedquerycomponent-ticket-review-round7.md`, verdict first: SATISFIED or STILL OPEN, with one line per item and, for anything open, the exact passage and the replacement text.

## Limits

Read-only apart from the claim and the report. No draft edit this round. No PR, comment, Jira or email. No build, test or gate. Commit as Nick Shanin, no trailers, no em dashes.
