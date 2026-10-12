# Settlement: CombinedQueryComponent ticket review round 4 (main side, 2026-10-11)

Round 4 returned **STILL OPEN** on item 1: items 2 and 3 were confirmed, and the open item carried five prescribed fixes: findings A1 and A2 (blocking, unlabelled general real-exit claims in the draft), findings A3 and A4 (should-fix, the same label missing on the score-only sort claims), and finding B1 (should-fix, a comma splice in the gate job). All five are applied here, exactly as prescribed. Findings B2 and C1 were notes marked optional; they are not applied, and nothing in them is a prescribed fix.

## Finding A1: draft line 50, Reproduction (blocking)

Applied to `material/combinedquerycomponent-ticket-draft.md`, the Partial skip bullet, exactly as prescribed.

Before:

> ...and it is not the shape a real {{timeAllowed}} exit produces (see Symptom).

After:

> ...and it is not the shape a real {{timeAllowed}} exit produces (from code reading, not run; see Symptom).

## Finding A2: draft line 56, End to end paragraph (blocking)

Applied to `material/combinedquerycomponent-ticket-draft.md`, the first sentence of the End to end paragraph, exactly as prescribed.

Before:

> End to end, no traced shard path produces the constructed shape.

After:

> End to end, no traced shard path produces the constructed shape (from code reading, not run).

## Finding A3: draft line 20, Symptom bullet 1 (should-fix)

Applied to `material/combinedquerycomponent-ticket-draft.md`, the score-only sort sentence in the first Symptom bullet, exactly as prescribed.

Before:

> under a score-only sort an exit cannot stop in sort-value preparation and stops in the search itself,

After:

> under a score-only sort an exit cannot stop in sort-value preparation and stops in the search itself (from code reading, not run),

## Finding A4: draft line 56, End to end paragraph (should-fix)

Applied to `material/combinedquerycomponent-ticket-draft.md`, the score-only sort sentence in the End to end paragraph, exactly as prescribed.

Before:

> under a score-only sort the exit cannot stop in sort-value preparation, so it stops in the search itself with the same search handler products;

After:

> under a score-only sort the exit cannot stop in sort-value preparation, so it stops in the search itself with the same search handler products (from code reading, not run);

## Finding B1: gate job line 31, comma splice (should-fix)

Applied to `gates/combinedquerycomponent-real-exit-premise.md`, the "Expected under the code reading" line, exactly as prescribed: the comma splice before "The expected outcome" is now a full stop.

Before:

> Exit on query index 0 under a score-only sort gives no per-query entry and no skip, The expected outcome under the code reading for Q3 is a NullPointerException at line 411: ...

After:

> Exit on query index 0 under a score-only sort gives no per-query entry and no skip. The expected outcome under the code reading for Q3 is a NullPointerException at line 411: ...

## Next

Round 5 confirmation is posted as `assignments/pool-combinedquerycomponent-ticket-review-round5.md`, scoped to these five settlements only. If it returns SATISFIED, the ticket text is final and completes the set for the consolidated new-ticket email.
