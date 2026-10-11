# Pool assignment: CombinedQueryComponent maximum fold, intent investigation

Capability tags: `review`. Intended host: windows. Staffing: 1. Claim path: claims/pool-combinedquerycomponent-maximum-fold-intent.md.

## Why this exists

The CombinedQueryComponent premise (reports/combinedquerycomponent-premise.md, three defects CONFIRMED BY RUN in its addendum) left one question open, and Nick has asked for a dig before ruling: when the per-query totals fold into the combined totals at lines 441 to 442 of CombinedQueryComponent.java (main d76fc14b9857), the fold takes the maximum of the per-query values. Is that the intended semantics, or a fourth defect?

The run evidence on the table: with two sub-queries carrying known errors, the merged approximateTotalHits was exactly the winning query's own (inflated) total; the losing query's error was invisible; the merged value never exceeded the largest per-query total and was never a sum. The crux to address head-on: the sub-queries run against the same shards and their result sets can overlap, so the true combined count is the size of a union that per-query totals alone cannot determine; maximum is a lower bound and sum is an upper bound. Which approximation did the author intend, and does the "approximate" framing of approximateTotalHits make the maximum a defensible choice?

## Questions to answer with evidence

1. **Origin.** Git history of the fold and of the per-query accumulation around it: which commit and which ticket introduced the maximum, and what that ticket and its discussion say about the combined totals. Blame the lines; read the commit messages and any linked ticket text reachable from this host.
2. **Documentation.** What the reference guide and any design text in the repository promise about a combined query's numFound and approximateTotalHits. Quote the passages.
3. **Existing tests.** Any test that asserts the combined total against the per-query totals (equal to the maximum, the sum, or anything else), in CombinedQueryComponent's test classes or the combined-query feature's tests generally.
4. **Consumers.** Who reads the combined totals downstream of the merge, and whether the choice between maximum and another fold changes any observable behavior beyond the reported number itself.
5. **The sibling.** How QueryComponent treats the analogous totals across shards (a sum), and whether the difference in shape between the two components reads as deliberate design for overlapping sub-queries or as an accident of implementation.

## Deliverable

reports/combinedquerycomponent-maximum-fold-intent.md: the evidence per question, then a verdict section headed RECOMMENDATION with one of: keep the maximum as intended behavior (the ticket draft then says so plainly, as a stated property), treat it as a fourth defect (the draft then names it with the fix), or pose it in the ticket as an open question for the maintainer (the draft's bracketed item becomes that question, worded for a reader). State the confidence and what evidence would change the recommendation. The ticket draft (material/combinedquerycomponent-ticket-draft.md) is NOT edited in this assignment; its bracketed open item stays until Nick rules on this report.

Rules: WORKFLOW.md binds (claim before work, hourly heartbeats, subagent cap of 6 to 7 across all hosts, mark the claim DONE in the same push as the deliverable). No em dashes in authored text. Read-only on code and branches; the only writes are the claim, the report, and heartbeat updates.
