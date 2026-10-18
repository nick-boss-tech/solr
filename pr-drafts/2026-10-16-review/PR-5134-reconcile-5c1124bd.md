# PR 5134 (SOLR-6831): reconciliation of the round 2 draft and the live body, at live head 5c1124bdef

Status: DRAFT. Not applied. The live PR body changes only when Nick approves.
Live head: 5c1124bdef95f4fa1d5bbbb6cf2dfd495111f528 (branch solr-6831-submit, commit "SOLR-6831: withhold pivot data when a shard refinement answer is omitted"). The round 2 draft (PR-5134-text.md) was written against 4afed04987, which is one commit behind.
Evidence run: 2026-10-18 by vm4, a focused test run, not a full gate. Log /workspace/gates/logs/vm4/SOLR-6831-live-5c1124bd.log; fail-before log /workspace/gates/logs/vm4/SOLR-6831-live-failbefore.log. Seed 6831C0FFEE6831.

## Test results

- TestQueryLimits at 5c1124bdef: tests=9, failures=0, errors=0, skipped=0 (fresh JUnit XML, 2026-10-18T05:33Z).
- Fail-before for the new test testPivotFacetRefinementWithholdsPivotDataWhenAnyShardOmits: with FacetComponent.java from 4afed04987 and the head test file, it fails with "no pivot data should be returned". So the new test depends on the 5c1124bd change.
- No full gate has run at 5c1124bd (tidy, Error Prone, module check, changelog). A receipt needs those steps first.

## Claim by claim

| Live body says | Evidence at 5c1124bd | Action |
| --- | --- | --- |
| "TestQueryLimits passes 9 of 9 at head 5c1124bd" | Confirmed by the run above (test-level only) | Keep the count. Say it is a test run until the full gate is written up. |
| "each production change has a test that fails without it" | Confirmed for the 5c1124bd change. The refinement change's test is not re-run at this head. | Keep, but only after the receipt. |
| "With the refinement change reverted (head ae76f048005), 7 tests with exactly 1 failure: testPivotFacetRefinementStopsWhenPartial ... 5 tripped refinement requests" | The receipt (receipts/SOLR-6831.md, the 2026-10-15 section on ae76f048005) says the single failure at that head is testPivotFacetFailsWhenPartialResultsDisallowed. testPivotFacetRefinementStopsWhenPartial passes without the fix there. | Remove, or replace with the receipt's wording. The live body misnames the test. |
| "The refinement stop ... With partialResults=false those answers are marked omitted instead, so the early stop does not apply there." (Limits) | haveCompleteResults (SolrQueryResponse.java L198 to L206) treats "omitted" as incomplete, and refinePivotFacets sets partialRefinement on any incomplete answer. So the early stop does apply to omitted answers. | Correct. Use the round 2 draft's sentence: an omitted shard answer stops refinement the same way a partial answer does. |
| "When a shard refinement answer is omitted, the final response carries the omitted header and returns no pivot data" (Limits) | Matches 5c1124bd: the new withholding code and the new test. | Keep. |
| "testPivotFacetRefinementStopsWhenOmittedPartialResults ... sending at most three refinement requests" | The round 2 receipt says the three-request bound is not reached in this shape, because the coordinator throws first at 2ee19dc646. Not re-run at 5c1124bd. | Keep the round 2 draft's wording: the test shows the outcome, not the count. |
| "testPivotFacetTruncatesWhenLimitsTrip ... verified 2026-10-14 at 24bd15ebb84" | Matches the receipt for 24bd15ebb84 (gate green 2026-10-14). | Keep. Link to 24bd15ebb84, not 4afed049. |
| Links to 4afed04987 for TestQueryLimits and the changelog | Those lines are at 4afed049, not at the live head. | Re-link to 5c1124bdef, with line ranges recomputed at that head. |

## Length

The live visible body is about 5,230 characters (links reduced to text), over the 3,500 guideline. The round 2 draft was about 3,500 by the same count. The reconciled body needs a further cut under 3,500 before it is applied.

## What is needed before the body can be applied

1. Done 2026-10-18: full gate green at 5c1124bdef (tidy, Error Prone, focused 9 of 9, module check), receipt section in receipts/SOLR-6831.md. The body can now cite the gate.
2. Recompute the links at 5c1124bdef, and fix the Limits sentence about omitted answers.
3. Nick approves the applied body.
