# CombinedQueryComponent round 8 labels, and round 9 prep (pr-prepare)

Verdict. Fourteen of fifteen round 8 items are applied to `material/combinedquerycomponent-ticket-draft.md`, each checked against the code at main `d76fc14b9857` by a read-only reviewer. C12 is not applied. The SOLR-15319 premise record supports SAME CASE, qualified. That verdict is for the main side to record, because the record has no run counts or top stack frame yet.

Claim: `claims/pool-combinedquery-round-8-labels-and-premise-review.md`. Round 8 verdict: `reports/combinedquerycomponent-ticket-review-round8.md`.

Process note: five read-only subagents ran. One of them wrote a scratch copy of the component source into the scratchpad and deleted it. No build, Gradle, test, or gate was run, and nothing was posted.

## Part (c) items

| Item | Passage | Verdict | Edit |
|---|---|---|---|
| C1 | Line 21, other order gives 8 | Confirmed | Label added: "(from code reading, not run)" |
| C2 | Line 22, "Only a mixed merge..." | Confirmed | Label added after the full stop |
| C3 | Line 22, all-exact and all-inexact | Confirmed | "(confirmed by run, cases X1 and X2 below)" |
| C4 | Line 40, "headered or not" | Confirmed | Label added, pointing to the fallback case |
| C5 | Line 40, arrival-order formula | Confirmed with edit | Sentence rewritten to count only shards that complete normally; cites HttpShardHandler.java line 440 |
| C6 | Line 40, 2 instead of 5 | Confirmed | Label added after "2 instead of 5" |
| C7 | Lines 42 to 43, numFoundExact | Confirmed | Label added to the paragraph |
| C8 | Line 54, existing tests | Confirmed | Label added: from a grep of the component's test classes, not run |
| C9 | Line 56, fallback wording | Confirmed | "any shard response that completes without the header" |
| C10 | Line 66, "fold over every shard" | Confirmed with edit | Narrowed to "every shard that sends a per-query entry", labelled |
| C11 | Line 66, fallback for complete shards | Confirmed | Labelled as confirmed by run, fallback case |
| C12 | Line 66, "case X1" for an exact merge | Not confirmed | Not applied (see below) |
| C13 | Line 68, maximum fold | Confirmed with edit | Adds the failed queue push caveat at line 434; labelled |
| C14 | Line 68, four documents, numFound 2 | Confirmed | Label added |
| Obs | Line 56, no size check | Labelled sentence added | Names the unguarded `docList.get(queryIndex)` at line 340 and the IndexOutOfBoundsException, labelled as code reading |

Count: fourteen edits applied (C1 to C11, C13, C14 and the observation). C12 is the only item not applied.

## Why C12 is not applied

The line-66 claim is correct in the code: two exact shards give a merged false. But the draft does not tag the exactness bullet at line 52 as case X1. Line 48 names X1 and X2 "in the two directions" without assigning either to a direction. A label that cites "case X1 below" cannot be verified against the draft as written. The fix is structural: tag the bullet at line 52, or cite it by its position. That is a draft change for round 9.

## Round 9 must check

- The labels added in this round, and that no new unlabelled claim was introduced.
- C12: tag line 52, then re-check the citation.
- The edited C13 sentence against the code at lines 433 to 434 and the ShardDocQueue push behavior, which this round did not read (the subagent noted it sits outside the listed sources).

## SOLR-15319 premise record (for the main side)

The record `gates/SOLR-15319-8051-premise.md` says the premise job FAILED at head `4bda91f46fc1` with a NullPointerException in `ExactStatsCacheMergeTest.testShardWithoutResponseIsSkipped`, at `ExactStatsCache.java:120`. Read-only checks:

- The 8051 receipt (`receipts/SOLR-8051.md`, line 7) records the same pre-fix exception in the global stats merge. Its line 8 names `doMergeToGlobalStats` calling `res.getException()` before the null-response check.
- At the 15319 head, line 120 is the `res.getException()` call, and no null-response guard precedes it.
- The 8051 test class exists at `e50a2aa3437`. It is absent at `4bda91f46fc1`.
- The draft's Limits line (`pr-drafts/search-components/SOLR-15319.md`, line 39) says "It is the case in SOLR-8051". The record supports the first clause. The "same case" wording is qualified: the match is by scenario and exception, not line for line.
- Open point: the test passes a non-null response with a null body. The top stack frame may be inside `SolrResponse.getException`, with line 120 as its caller. The record's parenthetical is ambiguous on this.

Still needed before the verdict is recorded: fresh JUnit XML counts (tests, failures, errors), the NPE's top frame from that XML, a diff of the run's test file against the 8051 head (adaptations, if any), and confirmation that no production file differs from `4bda91f46fc1` in the run tree. The seed is `15319C0FFEE15319` and the log is `logs/vm2/15319-head.log`. This round did not open the log.

## Not done

- No PR, comment, branch edit, live PR description edit, build, Gradle run, or test.
- No verdict was written to `receipts/SOLR-15319.md`.
