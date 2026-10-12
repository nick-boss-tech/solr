# Review: CombinedQueryComponent Jira ticket draft, round 6

Verdict: **STILL OPEN.** The four round 5 edits are confirmed. The carry-through sweep of the whole draft is not clean: it found five more unlabelled general claims, and they are applied in the same push as this report. Round 7 confirms those five edits and repeats the full sweep.

Scope: the assignment `assignments/pool-combinedquerycomponent-ticket-review-round6.md`, at draft commit `a0ad2fe935c`. Two read-only subagents did the work: one confirmed the four round 5 edits against the record and the code, and one swept the whole draft. Nothing was run, posted or filed.

## Round 5 edits: CONFIRMED

- **R5-1, line 20 (later-query clause):** labelled "(from code reading, not run)".
- **R5-2, line 56 (later-query clause):** labelled, before ", where the merge reads it with no size check".
- **R5-3, line 24 (partialResults header):** labelled, at the end of the sentence.
- **R5-4, line 38 (skipped shard):** now says "that carries a per-query entry for the query" and carries the label. The code check agrees: the fold at lines 341 to 343 sits inside the per-query block (338 to 344), so it runs only for shards with an entry.

The diff from `9989fbe46f5` to `a0ad2fe935c` changes only those four sentences.

## Correction to round 5

Round 5 said a shard with no per-query entry contributes nothing. That was too broad. Such a shard contributes its approximate count: the header value at lines 381 to 387, or the running total at line 384 when the header is absent. It contributes no count and no exactness. R5-4 is still accurate, because it speaks only of shards that carry an entry, and lines 20 and 56 already say the header value can still be added for the unprepared query. The phrase "nothing is contributed in that case" should not be reused in the ticket, the gate job or any reply.

## Carry-through sweep: NOT CLEAN

The sweep classified 21 sentences in scope: 9 labelled, 1 stated as not settled, 6 run results labelled as runs, and 5 unlabelled general claims. The five were applied in the same push:

- **d1, line 12 (title):** "counts a skipped partial shard that sends a per-query entry in the per-query totals". Scoped, since a title carries no label.
- **d2, line 20 (first sentence of bullet 1):** the condition now reads "carries a per-query entry for that query but no sort_values", and the sentence ends with "(from code reading; confirmed by run only for constructed responses; see Reproduction)". As written it also covered a shard with no entry, which contributes no count.
- **d3, line 28 (mechanism lead-in):** "each shard that sends a per-query entry has its contribution folded in before the partial-results skip (from code reading; see Reproduction):".
- **d4, line 56 (the reproduction pins sentence):** "(from code reading; confirmed by run only for constructed responses)" after "before it is skipped". The run pins the ordering only for the constructed responses.
- **d5, line 70 (related paragraph):** "(from code reading, not run in this ticket)" after "in the merged totals".

## Not checked

- The code for the d1 to d5 wording was not re-read by this round; round 7 checks it.
- The probe source, the gate job wording and the master receipts ledger, the takeover log and security material.

## Convergence line

STILL OPEN until round 7 confirms the five new edits and the full sweep is clean.
