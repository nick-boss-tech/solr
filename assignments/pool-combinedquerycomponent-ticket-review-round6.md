# Pool assignment: CombinedQueryComponent Jira ticket draft review, round 6 confirmation

Capability tags: `review`. Intended host: windows. Staffing: 2 read-only subagents. Claim path: claims/pool-combinedquerycomponent-ticket-review-round6.md.

## Scope

Round 5 (reports/combinedquerycomponent-ticket-review-round5.md) confirmed the five round 4 settlements and returned STILL OPEN on four draft clauses. The coordinator applied the four round 5 replacements to `material/combinedquerycomponent-ticket-draft.md`:

- R5-1, line 20: the later-query clause now ends "(from code reading, not run)".
- R5-2, line 56: the later-query clause now carries "(from code reading, not run)" before ", where the merge reads it with no size check".
- R5-3, line 24: the partialResults header sentence now ends "(from code reading, not run)".
- R5-4, line 38: the skipped-shard sentence now says "that carries a per-query entry for the query", and ends "(from code reading; confirmed by run only for constructed responses; see Reproduction)".

## What to check

1. Each of the four edits matches the round 5 replacement text in substance and sits in the sentence it was written for. Read the draft at the current pr-prepare tip.
2. R5-4 is correct against the code at `d76fc14b9857`: a skipped shard that has a per-query entry contributes its count, exactness and approximate count before the `continue` at lines 399 to 400; a shard with no per-query entry contributes nothing.
3. The carry-through rule (round 3, carried through round 5): every general real-exit claim in the ticket text is labelled or stated as not settled. Sweep the whole draft.
4. The edits do not change any other sentence, and the draft's head, counts and links are unaffected.

Out of scope: the gate job wording (its owner is the gate coordinator), the optional notes R5-N1 to N3 in the round 5 report, and anything round 1 to 5 confirmed.

## Deliverable

`reports/combinedquerycomponent-ticket-review-round6.md`, verdict first: SATISFIED or STILL OPEN, with one line per item (confirmed, or the exact passage and the replacement text).

## Limits

Read-only apart from the claim and the report. No draft edit this round. No PR, comment, Jira or email. No build, test or gate. Commit as Nick Shanin, no trailers, no em dashes.
