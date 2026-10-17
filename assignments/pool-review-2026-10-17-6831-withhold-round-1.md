# Pool assignment: SOLR-6831 withhold pivot data on omitted distributed refinement (round 1)

Status: DONE (fix at 5c1124bdef95f4fa1d5bbbb6cf2dfd495111f528 on solr-6831-submit; deliverable reports/2026-10-16-pr-batch/SOLR-6831-withhold-round-1.md; draft pr-drafts/2026-10-16-review/PR-5134-limits.md)

- Capability tags: implementation, gate.
- Intended host: any Linux host with the Gradle toolchain (vm1, vm2, or vm3). Claim under `claims/` before starting (WORKFLOW.md). The Windows host never claims this work.
- Posted by: Windows review lane (review-round coordinator), 2026-10-17, at the direction of Nick Shanin. The behavior decision below is his.

## Decision

On 2026-10-17 Nick chose to withhold pivot data when a shard answers `omitted` during distributed pivot refinement. The coordinator then returns the response with the `omitted` header and no pivot data, the same as the local tripped-limit case. That case already withholds pivot data: `testPivotFacetOmitsResultsWhenPartialResultsDisallowed` passes at the head. The basis is the PR 5134 body's rule: "partial pivots are not handed back as if they were usable results."

## Grounding

- Branch `solr-6831-submit`, head `4afed04987b2343af58261b83c4b49fda0af45c0` (the PR 5134 head). If the tip has moved, stop and record the new tip.
- Code: `solr/core/src/java/org/apache/solr/handler/component/FacetComponent.java`, method `refinePivotFacets`, about lines 1023 to 1078. Today, a shard answer that is omitted and has no pivot data is skipped (lines 1049 to 1051). The counts from the other shards are still merged and returned.
- Model for the expected shape: the local trip in `PivotFacetProcessor`, and `testPivotFacetOmitsResultsWhenPartialResultsDisallowed` in `solr/core/src/test/org/apache/solr/search/TestQueryLimits.java`.
- Existing test on the branch: `testPivotFacetRefinementStopsWhenOmittedPartialResults` in `TestQueryLimits.java`, about lines 298 to 339. It asserts the omitted header and at most three refinement requests. Keep both assertions and add the no-pivot-data assertion.

## Items

1. **Head check.** `ls-remote origin refs/heads/solr-6831-submit` must equal `4afed04987b2343af58261b83c4b49fda0af45c0`. If it does not, stop and record the new tip in the deliverable. Output: section "Head" in the deliverable.

2. **Add the failing tests first.** In `TestQueryLimits`, extend `testPivotFacetRefinementStopsWhenOmittedPartialResults` to assert that no pivot data comes back. Add one test with two shards, one of which answers omitted, that asserts no pivot counts come back and the header reads omitted. Run the new assertions before the production change and record that they fail. Record the seed and the counts from fresh JUnit XML. Output: section "Tests before" in the deliverable.

3. **Implement the withhold rule.** In the coordinator's response build, when any shard refinement answer is omitted, return the response with the omitted header and no pivot data, in the same shape as the local trip. Leave the local-trip behavior unchanged. Keep the existing early stop on partial refinement answers. Commit the production change and the test change together in one commit on `solr-6831-submit`.

4. **Run the focused tests.** Run `TestQueryLimits` and the other pivot facet test classes on the branch, with the seed recorded. Count from fresh JUnit XML. If a test fails with a cluster startup error, the result is INCONCLUSIVE, not a failure of the fix. Record the error text and stop before pushing. Output: section "Tests after" in the deliverable.

5. **Push.** If the focused run passes, push the fix commit to `solr-6831-submit` as a plain fast-forward. Never force. Report the new head. The PR head moves when this lands, and the deliverable must say so.

6. **Draft the body correction (not live).** Write `pr-drafts/2026-10-16-review/PR-5134-limits.md`. It starts with the new head SHA. It corrects the Limits bullet, "An omitted answer with no pivot data is skipped, and the final response reports the omission in its header," to say that omitted refinement answers withhold pivot data and the response carries the omitted header. It also corrects any sentence in "What this change does" that contradicts that. Do not edit the live PR body.

## Decisions

| Decision | Answer or default | Condition and action | Owner |
| --- | --- | --- | --- |
| Behavior for an omitted distributed refinement answer | decided: withhold pivot data (Nick, 2026-10-17) | none | Nick |
| Local-trip behavior | decided: unchanged | if the fix would need to change the local-trip path, stop and escalate | main agent |
| Proof leg against the pre-fix base | decided: deferred. Nick reruns it later, with the cluster startup workaround. Do not run it in this assignment. Superseded 2026-10-17: the leg was run and is recorded in the Proof leg section of reports/2026-10-16-pr-batch/SOLR-6831-withhold-round-1.md; the pre-fix leg at 4afed04987 is conclusive | none | Nick |
| The new assertion fails at the head after the change | default: stop before the push and record the failing assertion | escalate to the main agent | main agent |
| A test fails with a cluster startup error | default: INCONCLUSIVE, no push | record the error text and escalate to the main agent | main agent |
| Live PR body or title edit | decided: none; the correction is a draft only | none | Nick |

No row may leave the choice to the claimant.

## Deliverable

`reports/2026-10-16-pr-batch/SOLR-6831-withhold-round-1.md`, with sections Head, Tests before, Tests after, Push, and Verdict. Each run gives its seed, log path, and counts from fresh JUnit XML. Also the draft `pr-drafts/2026-10-16-review/PR-5134-limits.md`. Set this file's Status to DONE in the same push as the deliverable.

## Limits

- No PR comment, no live title or body edit, no JIRA, no email. No force push.
- Commit as Nick Shanin with no trailers. No em dashes.
- Push only to `solr-6831-submit`, as a fast-forward, and only after a green focused run.
- Do not change the local-trip behavior.
