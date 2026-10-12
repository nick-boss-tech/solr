# Review: CombinedQueryComponent Jira ticket draft, round 7

Verdict: **STILL OPEN.** The five round 6 edits (d1 to d5) are confirmed. The full carry-through sweep found seven more items. Two firm labels and one accuracy correction are applied in this push, and they need a confirmation pass. Five items are predictions about proposed fixes. Round 6 excluded fix-design sentences from the sweep's scope, and this sweep counts them. That scope question is for the coordinator to settle before those five are applied.

Scope: the assignment `assignments/pool-combinedquerycomponent-ticket-review-round7.md`, at draft commit `9f2e7282907`, with the edits in this push. Two read-only subagents did the work: one confirmed d1 to d5 against the round 6 text and the code, and one swept the whole draft. Nothing was run, posted or filed.

## d1 to d5: CONFIRMED

- **d1, line 12 (title):** "counts a skipped partial shard that sends a per-query entry in the per-query totals".
- **d2, line 20 (bullet 1, first sentence):** the condition is "carries a per-query entry for that query but no sort_values", and the clause ends with the code-reading label and "see Reproduction".
- **d3, line 28 (mechanism lead-in):** "each shard that sends a per-query entry has its contribution folded in before the partial-results skip (from code reading; see Reproduction):".
- **d4, line 56 (the reproduction pins sentence):** labelled, "(from code reading; confirmed by run only for constructed responses)".
- **d5, line 70 (related paragraph):** labelled, "(from code reading, not run in this ticket)".

The code check agrees: the folds at lines 341 to 343 run only when the shard has an entry, the approximate block at lines 381 to 387 runs for every non-exception shard, and the skip at lines 399 to 400 comes after both.

## Sweep items applied in this push (need confirmation)

- **Line 44 (firm).** "Nothing in the skip depends on a total (from code reading, not run): its inputs are..."
- **Line 56 (firm).** "...the folds at lines 342 and 343 fire on every merge in which a shard sends a per-query entry (from code reading, not run)."
- **Lines 20 and 56 (accuracy).** A shard with no per-query entry still reaches the approximate block. With no response header, it adds the running total at line 384, not a header value. The two sentences now read "the approximateTotalHits value carried in its response header, or with no header the running total, can still be added at the merge for it (from code reading, not run)". Line 20 and line 56 use the same wording, and the earlier "contributes its approximate count" phrasing is not used.

## Sweep items NOT applied: scope decision needed

The sweep also lists five fix-design or prediction sentences, which round 6 excluded as fix design:

- Line 62: "...so a skipped shard never enters that query's totals." Proposed: "...so, if the change lands, a skipped shard never enters that query's totals (proposed; not run)."
- Line 62: "after the reorder, a query whose shards are all skipped contributes a count of zero." Proposed: "...would contribute a count of zero (proposed; not run)."
- Line 66: "Correcting the totals when the skip fires instead means reconstructing what the skipped shard added after the fact" gets "(from code reading, not run)".
- Line 66: "Testing the skip before accumulating leaves each query's totals describing the same set of shards that query merges." gets "(proposed; not run)".
- Line 66: "the fallback fires for complete shards too, not only skipped ones, so a reorder alone leaves the double count." gets "(from code reading, not run)" and "(proposed; not run)".

These are predictions about a fix, not claims about a real exit. Applying them is safe, but it changes what the carry-through rule covers. The coordinator should decide whether fix design is in scope. If it is, the five edits are the sweep's replacement text, and the draft needs another confirmation pass.

## Notes

- The sweep notes that the Reproduction lead-in says "Confirmed by run", while the second sentence of bullet 1 reads "7 instead of 3 in the constructed reproduction". It suggests "(confirmed by run, constructed case below)". Optional.
- Round 6's sweep and this sweep classify the same sentences differently. Once the scope decision is made, the round should use one classification from then on.

## Not checked

- The probe source, the gate job wording (its owner is the gate coordinator) and the master receipts ledger, the takeover log and security material.

## Convergence line

STILL OPEN. Round 7 closes when the coordinator has settled the fix-design scope and a confirmation pass covers the applied edits.
