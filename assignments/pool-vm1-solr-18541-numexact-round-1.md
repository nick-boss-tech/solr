# Pool assignment: SOLR-18541 maintainer-directed minimum change (numFoundExact false on multi-sub-query merges)

Status: DONE

- Capability tags: build, gate.
- Intended host: vm1. Claim under `claims/` before starting (WORKFLOW.md).
- Posted by: vm1 implementation lane, 2026-10-16, at the direction of the main agent, relaying a maintainer's direction on the ticket.

## Direction

Amrit Sarkar commented on Jira SOLR-18541 on 2026-10-16T04:47:56Z. The bug was also reported on dev@ against 10.1.0 RC2 and reproduced on the RC2 tarball. His table: a plain `/select` for cat OR dog reports the union count 5 with numFoundExact true; hybrid cat(3) + dog(2) with no overlap returns 5 documents but numFound 3; hybrid cat(3) + id:(3 4) with one overlap returns 4 documents but numFound 3. The response returns more documents than numFound says exist, because `CombinedQueryComponent.mergeIds` and `QueryAndResponseCombiner.simpleCombine` combine per-sub-query totals by maximum.

On PR #5117 he writes: "On the PR head, the repro above returns numFound=3, numFoundExact=true. The exactness fix makes the wrong count look exact. Either: fix the count: each shard reports its own union count and the coordinator sums them (shards don't share docs, so this is exact), or at minimum, set numFoundExact=false when more than one sub-query contributes, and drop the two largestSubQueryTotalWins* tests that lock in max(). Happy to help with the union count. I can take it as a follow-up JIRA if you'd rather keep #5117 scoped."

## Items

1. **Implement the minimum change in `CombinedQueryComponent.mergeIds`.** When more than one sub-query contributes to the merge, the merged `numFoundExact` is false. The merged numFound keeps the current maximum rule; the union count is not implemented here. Single-sub-query exactness behavior is unchanged. Start from: branch `combinedquerycomponent-merge-totals` at `a93f9f6a997e748514cf2575a6e94c5f1c01f85e`, file `solr/core/src/java/org/apache/solr/handler/component/CombinedQueryComponent.java`.
2. **Tests.** Drop the two `largestSubQueryTotalWins*` tests in `CombinedQueryComponentMergeTotalsTest`. Add or adjust a test pinning the new behavior: a multi-sub-query merge in the repro shape (two sub-queries with totals 3 and 2, all counts exact) asserting the merged numFoundExact is false while numFound stays the largest sub-query total. The single-sub-query exactness tests stay and keep passing.
3. **Changelog.** The change is behavior-visible. Extend the title of `changelog/unreleased/SOLR-18541-combined-query-merge-totals.yml` so the exactness sentence also names the one-sub-query condition.
4. **Gate.** Full hardening gate at the new head under `gates/SOLR-18541-numexact-gate.md`: changelog parse, tidy with the GATE INVALID guard, Error Prone compile, proof leg against merge-base production judged by content, focused `CombinedQueryComponentMergeTotalsTest` counted from fresh JUnit XML at seed 18541C0FFEE18541, module check `:solr:core:check -x test`.
5. **Records.** On green: `receipts/SOLR-18541.md` new top section, this assignment and the gate job and the claim DONE, one takeover log line.

## Decisions

| Decision | Answer or default | Condition and action | Owner |
| --- | --- | --- | --- |
| Union count in this PR | decided: no | The maintainer offered the union count as his own follow-up JIRA; this round implements only his stated minimum | maintainer direction, relayed by the main agent |
| `QueryAndResponseCombiner.simpleCombine` | decided: out of scope | It is not in this PR's diff (the diff is the changelog fragment, CombinedQueryComponent.java, and the test class). If the combiner computes exactness separately, flag it for the main side; do not expand the diff | main agent |
| PR #5117 body and any reply to the maintainer | decided: not this lane | The lane flags the falsified body sentences; the main side owns the body edit and any reply | main agent |

## Deliverable

`receipts/SOLR-18541.md` new top section plus this file's closing record: the code site changed, the tests dropped and added, the new head, the gate result per step with counts from fresh JUnit XML, the proof-leg content, and the flags for the main side (falsified PR body sentences, the prepared-body report's state, the simpleCombine scope flag if it arose).

## Limits

- No PR body edit, no comment, no JIRA write, no email. No force push; the branch push is a plain fast-forward.
- Production changes stay inside `CombinedQueryComponent.java`; test changes inside `CombinedQueryComponentMergeTotalsTest.java`; plus the changelog fragment title.
- Commit as Nick Shanin with no trailers. No em dashes.

## Closing record (2026-10-16, vm1)

- Implemented and pushed: head `81a88e40dd42f90fe6c67b2aeb6f678c437d7c0d` on `combinedquerycomponent-merge-totals` (plain fast-forward from `a93f9f6a997e`, ls-remote and PR API verified). Production: `CombinedQueryComponent.mergeIds` counts contributing sub-queries and clears the merged numFoundExact when more than one contributes. Tests: the two `largestSubQueryTotalWins*` tests dropped, `multipleContributingSubQueriesClearExactness` added. Changelog title extended with the one-sub-query condition.
- Gate: GREEN under `gates/SOLR-18541-numexact-gate.md` (9 of 9 at the head; proof leg B exactly one failure, the new test, on the prior head's production; proof leg A the 6 premise failures on merge-base production). Receipt: `receipts/SOLR-18541.md` top section.
- Flags handed to the main side: the falsified PR #5117 body sentences (the Choice's exactness question, the Proof count and head citations, the Limits sentence tying exactness to the Choice), the state of `reports/live-body-18541-prepared-round-1.md` (superseded, conflicts with the resolution), and the `simpleCombine` scope flag (named by the maintainer, not in this PR's diff, not changed).
