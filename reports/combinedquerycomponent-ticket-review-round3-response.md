# Settlement: CombinedQueryComponent ticket review round 3 (main side, 2026-10-11)

Round 3 returned **STILL OPEN** on three items: finding A (one unlabelled general sentence in the draft), finding B (the gate job's expected-outcome line for Q3), and item 3 (M2's merged `numFound`, not checked because the probe assertion sits in the VM1 worktree). Items 1, 2 and 5 were confirmed and need no action. All three open items are settled here.

## Finding A: draft line 20 label

Applied to `material/combinedquerycomponent-ticket-draft.md`, first Symptom bullet, exactly as prescribed.

Before:

> The real {{timeAllowed}} exit is not the shape that produces this.

After:

> The real {{timeAllowed}} exit is not the shape that produces this (from code reading, not run).

The general claim now carries the same verification-level label as the scoped sentences that follow it in the same bullet.

## Finding B: gate job expected outcome for Q3

Applied to `gates/combinedquerycomponent-real-exit-premise.md`, the "Expected under the code reading" line, exactly as prescribed. The sentence "and Q3 decides whether line 411 throws on the null docs." is replaced with:

> The expected outcome under the code reading for Q3 is a NullPointerException at line 411: `docs` is null, since there is no per-query entry and nothing assigns it at line 341, and the loop calls `docs.size()` with no guard. No throw occurs earlier at line 399, because `sortFieldValues` defaults to an empty list at lines 394 to 396 and `needsUnmarshalling` is false, so the skip does not apply.

The job now states the prediction before the run, so its verdict can be read against it.

## Item 3: M2 merged `numFound`, checked on VM1

Checked. The assertion states 5, as the ticket draft does.

- File: `solr/core/src/test/org/apache/solr/handler/component/CombinedQueryComponentMergeTotalsTest.java`, in the VM1 worktree `~/workspace/solr-worktrees/cqc-premise`.
- Method: `maximumFoldOtherQueryWinning()`, starting at line 343.
- Line 362: `assertEquals("M2 numFound", 5, rb.getResponseDocs().getNumFound());`

The value round 3 could not check is the merged `numFound`, and the probe asserts it as 5, which is what the draft states. Item 3 is closed. For completeness, the same method also asserts the M2 header value at lines 363 to 368, `assertEquals("M2 approximateTotalHits", 5L, ...)`; that is the probe's expected-correct value, not the observed base outcome of 7 that the draft reports with the open question and that round 3 confirmed against the run addendum.

## Next

Round 4 confirmation is posted as `assignments/pool-combinedquerycomponent-ticket-review-round4.md`, scoped to these three settlements only. If it returns SATISFIED, the ticket text is final.
