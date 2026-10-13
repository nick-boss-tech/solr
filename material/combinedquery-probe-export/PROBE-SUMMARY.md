# CombinedQueryComponent probe export: per-case summary

Export contents, all in this directory:

- `CombinedQueryComponentMergeTotalsTest.java`: the probe source, copied unchanged from the vm1 worktree `solr-worktrees/cqc-premise` (`solr/core/src/test/org/apache/solr/handler/component/CombinedQueryComponentMergeTotalsTest.java`). Git blob SHA: `d949d927f5877b9b2bd1044ba4f933be594a3472`.
- `gcqc-premise-TEST-org.apache.solr.handler.component.CombinedQueryComponentMergeTotalsTest.xml`: the JUnit XML from the probe run.
- `gcqc-premise3.log`: the premise log from the same run.

The probe was not re-run for this export. Line numbers below are lines of the exported probe source. Assertion messages are quoted as they read in the XML (the XML stores `<` and `>` escaped).

## A5: did cases M1 and M2 set maxHitsTerminatedEarly

- M1: FLAG SET.
- M2: FLAG SET.

M1, probe lines:

- Line 315 (shard1 header): `header(SolrQueryResponse.RESPONSE_HEADER_MAX_HITS_TERMINATED_EARLY_KEY, Boolean.TRUE),`
- Line 322 (shard2 header): `header(),` (the key is not set for shard2)

M2, probe lines:

- Line 349 (shard1 header): `header(SolrQueryResponse.RESPONSE_HEADER_MAX_HITS_TERMINATED_EARLY_KEY, Boolean.TRUE),`
- Line 356 (shard2 header): `header(),` (the key is not set for shard2)

Did the merged approximateTotalHits reach the response header: yes, in both cases. In both cases the test reads the merged value from the response header: M1 at lines 329 to 334 (`rb.rsp.getResponseHeader()` at line 333), M2 at lines 363 to 368 (`rb.rsp.getResponseHeader()` at line 367). The values the tests read there are in the XML failure messages:

- M1: `java.lang.AssertionError: M1 approximateTotalHits expected:<11> but was:<21>`
- M2: `java.lang.AssertionError: M2 approximateTotalHits expected:<5> but was:<7>`

## Run counts and seed in the XML

The run counts appear in the XML, in the `<testsuite>` line (line 2 of the XML), quoted verbatim:

`<testsuite name="org.apache.solr.handler.component.CombinedQueryComponentMergeTotalsTest" tests="7" skipped="0" failures="7" errors="0" timestamp="2026-10-11T22:17:01.423Z" hostname="htch-runtime" time="14.929">`

So: tests 7, skipped 0, failures 7, errors 0. All seven cases failed.

The seed `C0FFEE42C0FFEE42` appears in the XML (49 occurrences), for example XML line 6: `at __randomizedtesting.SeedInfo.seed([C0FFEE42C0FFEE42:9600FA90C372DEE0]:0)`. It also appears in the exported log, line 2: `seed=C0FFEE42C0FFEE42`, and line 42: `Running tests with randomization seed: tests.seed=C0FFEE42C0FFEE42`.

## How the sub-query count is read

The request builder is at probe lines 116 to 128. For each query string passed, it adds one `CombinerParams.COMBINER_QUERY` param (lines 118 to 121; line 119: `params.add(CombinerParams.COMBINER_QUERY);`). The sub-query count for a case is the number of query strings in that case's `newResponseBuilder` call.

## Case P: skippedPartialShardContributesNothing (method at line 175)

- Sub-queries: 1, `"q0"`. Builder call, line 205: `ResponseBuilder rb = newResponseBuilder(sortSpec, "q0");`
- maxHitsTerminatedEarly: shard1 SET, `Boolean.TRUE` (header at lines 184 to 190; the key is at line 187, the value at line 188). The same header sets `RESPONSE_HEADER_PARTIAL_RESULTS_KEY` to `Boolean.TRUE` (lines 185 to 186) and `RESPONSE_HEADER_APPROXIMATE_TOTAL_HITS_KEY` to `10L` (lines 189 to 190). Shard2 NOT SET: its header (line 197) sets only `RESPONSE_HEADER_APPROXIMATE_TOTAL_HITS_KEY` to `5L`.
- Shard inputs: shard1 combined list `docList(4, true, a1, a2)` (line 191), per-query list (line 192); shard2 combined list `docList(3, true, b1, b2, b3)` (line 198), per-query list (line 199).
- Outcome from the XML: FAILED. Assertion message verbatim: `java.lang.AssertionError: P numFound expected:<3> but was:<7>`
- Merged values as the test read them: numFound 7 (assertion at line 207). numFoundExact: NOT IN EXPORT (the test does not read it in this case). approximateTotalHits: NOT IN EXPORT for this case (the recorded failure is the line 207 assertion; the XML records no value for the lines 208 to 213 assertion; case P2 reads it on the same setup).

## Case P2: skippedPartialShardApproximateTotalCounted (method at line 219)

- Sub-queries: 1, `"q0"`. Builder call, line 248: `ResponseBuilder rb = newResponseBuilder(sortSpec, "q0");`
- maxHitsTerminatedEarly: shard1 SET, `Boolean.TRUE` (key at line 230, value at line 231; the header also sets partialResults `Boolean.TRUE` at lines 228 to 229 and approximateTotalHits `10L` at lines 232 to 233). Shard2 NOT SET: its header (line 240) sets only `RESPONSE_HEADER_APPROXIMATE_TOTAL_HITS_KEY` to `5L`.
- Shard inputs: shard1 combined list `docList(4, true, a1, a2)` (line 234), per-query list (line 235); shard2 combined list `docList(3, true, b1, b2, b3)` (line 241), per-query list (line 242).
- Outcome from the XML: FAILED. Assertion message verbatim: `java.lang.AssertionError: P2 approximateTotalHits expected:<5> but was:<15>`
- Merged values as the test read them: approximateTotalHits 15, read from the response header (assertion at lines 250 to 255; `rb.rsp.getResponseHeader()` at line 254). numFound: NOT IN EXPORT (this case does not assert it). numFoundExact: NOT IN EXPORT (the test does not read it in this case).

## Case F: fallbackUsesEachShardsOwnCount (method at line 145)

- Sub-queries: 1, `"q0"`. Builder call, line 162: `ResponseBuilder rb = newResponseBuilder(scoreSortSpec(), "q0");`
- maxHitsTerminatedEarly: shard1 SET, `Boolean.TRUE` (line 151: `header(SolrQueryResponse.RESPONSE_HEADER_MAX_HITS_TERMINATED_EARLY_KEY, Boolean.TRUE),`). Shard2 NOT SET: `header()` at line 158.
- Shard inputs: shard1 combined list `docList(2, true, a1, a2)` (line 152), per-query list (line 153); shard2 combined list `docList(3, true, b1, b2, b3)` (line 159), per-query list (line 160).
- Outcome from the XML: FAILED. Assertion message verbatim: `java.lang.AssertionError: F approximateTotalHits expected:<5> but was:<7>`
- Merged values as the test read them: numFound 5 (assertion at line 164; the recorded failure is the later assertion). approximateTotalHits 7, read from the response header (assertion at lines 165 to 170; `rb.rsp.getResponseHeader()` at line 169). numFoundExact: NOT IN EXPORT (the test does not read it in this case).

## Case X1: exactnessAllExactStaysExact (method at line 261)

- Sub-queries: 1, `"q0"`. Builder call, line 274: `ResponseBuilder rb = newResponseBuilder(scoreSortSpec(), "q0");`
- maxHitsTerminatedEarly: shard1 NOT SET: `header()` at line 266. Shard2 NOT SET: `header()` at line 270.
- Shard inputs: shard1 combined and per-query lists `docList(2, true, a1, a2)` (line 266); shard2 combined list `docList(3, true, b1, b2, b3)` (line 271), per-query list (line 272). Both shards pass `true` as the exact flag to `docList` (the `docList` helper sets it with `list.setNumFoundExact(exact)` at line 82).
- Outcome from the XML: FAILED. Assertion message verbatim: `java.lang.AssertionError: X1 numFoundExact expected:<true> but was:<false>`
- Merged values as the test read them: numFoundExact false (assertion at line 276: `assertEquals("X1 numFoundExact", Boolean.TRUE, rb.getResponseDocs().getNumFoundExact());`). numFound: NOT IN EXPORT (this case does not assert it). approximateTotalHits: NOT IN EXPORT (the test does not read it in this case).

## Case X2: exactnessAllInexactStaysInexact (method at line 281)

- Sub-queries: 1, `"q0"`. Builder call, line 298: `ResponseBuilder rb = newResponseBuilder(scoreSortSpec(), "q0");`
- maxHitsTerminatedEarly: shard1 NOT SET: `header()` at line 287. Shard2 NOT SET: `header()` at line 294.
- Shard inputs: shard1 combined list `docList(2, false, a1, a2)` (line 288), per-query list (line 289); shard2 combined list `docList(3, false, b1, b2, b3)` (line 295), per-query list (line 296). Both shards pass `false` as the exact flag to `docList` (line 82, as in X1).
- Outcome from the XML: FAILED. Assertion message verbatim: `java.lang.AssertionError: X2 numFoundExact expected:<false> but was:<true>`
- Merged values as the test read them: numFoundExact true (assertion at line 300: `assertEquals("X2 numFoundExact", Boolean.FALSE, rb.getResponseDocs().getNumFoundExact());`). numFound: NOT IN EXPORT (this case does not assert it). approximateTotalHits: NOT IN EXPORT (the test does not read it in this case).

## Case M1: maximumFoldWinningQueryErrorPassesThrough (method at line 309)

- Sub-queries: 2, `"q0"` and `"q1"`. Builder call, line 326: `ResponseBuilder rb = newResponseBuilder(scoreSortSpec(), "q0", "q1");`
- maxHitsTerminatedEarly: shard1 SET, `Boolean.TRUE` (line 315: `header(SolrQueryResponse.RESPONSE_HEADER_MAX_HITS_TERMINATED_EARLY_KEY, Boolean.TRUE),`). Shard2 NOT SET: `header()` at line 322.
- Shard inputs: shard1 combined list `docList(3, true, a1, a2, a3)` (line 316), per-query lists `List.of(docList(2, true, a1, a2), docList(10, true, a3))` (line 317); shard2 combined list `docList(4, true, b1, b2, b3, b4)` (line 323), per-query lists `List.of(docList(3, true, b1, b2, b3), docList(1, true, b4))` (line 324).
- Outcome from the XML: FAILED. Assertion message verbatim: `java.lang.AssertionError: M1 approximateTotalHits expected:<11> but was:<21>`
- Merged values as the test read them: numFound 11 (assertion at line 328; the recorded failure is the later assertion). approximateTotalHits 21, read from the response header (assertion at lines 329 to 334; `rb.rsp.getResponseHeader()` at line 333). numFoundExact: NOT IN EXPORT (the test does not read it in this case).

## Case M2: maximumFoldOtherQueryWinning (method at line 343)

- Sub-queries: 2, `"q0"` and `"q1"`. Builder call, line 360: `ResponseBuilder rb = newResponseBuilder(scoreSortSpec(), "q0", "q1");`
- maxHitsTerminatedEarly: shard1 SET, `Boolean.TRUE` (line 349: `header(SolrQueryResponse.RESPONSE_HEADER_MAX_HITS_TERMINATED_EARLY_KEY, Boolean.TRUE),`). Shard2 NOT SET: `header()` at line 356.
- Shard inputs: shard1 combined list `docList(3, true, a1, a2, a3)` (line 350), per-query lists `List.of(docList(2, true, a1, a2), docList(1, true, a3))` (line 351); shard2 combined list `docList(4, true, b1, b2, b3, b4)` (line 357), per-query lists `List.of(docList(3, true, b1, b2, b3), docList(1, true, b4))` (line 358).
- Outcome from the XML: FAILED. Assertion message verbatim: `java.lang.AssertionError: M2 approximateTotalHits expected:<5> but was:<7>`
- Merged values as the test read them: numFound 5 (assertion at line 362; the recorded failure is the later assertion). approximateTotalHits 7, read from the response header (assertion at lines 363 to 368; `rb.rsp.getResponseHeader()` at line 367). numFoundExact: NOT IN EXPORT (the test does not read it in this case).
