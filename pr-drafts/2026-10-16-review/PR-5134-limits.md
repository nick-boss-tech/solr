# PR 5134 body correction (draft, not live)

Head: `5c1124bdef95f4fa1d5bbbb6cf2dfd495111f528` on `solr-6831-submit`. The live PR body describes `4afed04987b2343af58261b83c4b49fda0af45c0`, the previous head. Replace the sentences below in the PR body when the PR is updated. Do not edit the live body from this draft.

## Limits, replace the last bullet

Current text:

> - The refinement stop reads the partial marker on shard refinement answers. With `partialResults=false` those answers are marked "omitted" instead, so the early stop does not apply there. An omitted answer with no pivot data is skipped, and the final response reports the omission in its header.

Replacement:

> - The refinement stop reads the partial marker on shard refinement answers. With `partialResults=false` those answers are marked "omitted" instead, so the early stop does not apply there. When a shard refinement answer is omitted, the final response carries the "omitted" header and returns no pivot data, the same as a tripped limit in the local pivot path.

## Proof, replace one sentence

Current text (in the paragraph that starts with the `testPivotFacetRefinementStopsWhenOmittedPartialResults` link):

> At this head the coordinator skips that answer and the request completes with the "omitted" header, sending at most three refinement requests.

Replacement:

> At this head the coordinator marks the response "omitted" and returns no pivot data, sending at most three refinement requests.

## Proof, replace the headline count

Current text:

> **`TestQueryLimits` passes 8 of 8 at head `4afed04987b2343af58261b83c4b49fda0af45c0`, and each production change has a test that fails without it.**

Replacement:

> **`TestQueryLimits` passes 9 of 9 at head `5c1124bdef95f4fa1d5bbbb6cf2dfd495111f528`, and each production change has a test that fails without it.**

Add one sentence at the end of the `testPivotFacetOmitsResultsWhenPartialResultsDisallowed` paragraph:

> `testPivotFacetRefinementWithholdsPivotDataWhenAnyShardOmits` covers the same contract for a refinement answer: the header reads "omitted" and no pivot data is returned. On the previous head this test failed on the pivot data assertion.

## Unchanged

- "What this change does" and the Behavior change paragraph already say that with `partialResults=false` no pivot data is returned. The change makes the refinement path match that.
- The Limits bullet about the check running between values is unchanged.
