# SOLR-17370 premise verdict, batch 2 (unit 1): NOT REPRODUCED under probe

Ticket: SOLR-17370, Open, Critical. "Search with facets throws
IllegalAccessException if some string fields contain numeric values."
Reporter field: type string, stored, multiValued, docValues false.
Ticket stack: a Lucene `LongPoint$1` query object reached
`Utils.getReflectWriter` through `JavaBinCodec` while writing a search
response, failing with `IllegalAccessException` (access violation, from
public Lookup).

## Verdict

The premise does not reproduce on current main under the probe below.
No branch, no implementation, no PR, no Jira or GitHub post. Unit 2
(the conditional 17370 implementation) is skipped: its condition, a
reproduced premise, did not occur.

## Harness fix verified

The round-1 run (vm6, 2026-10-27) never ran the premise: core init
failed on unset `solr.tests.*` properties. This run used the recorded
fix: a `SolrTestCase` test (never `SolrTestCaseJ4`) that boots from the
standard test solrconfig calls `SolrTestCaseJ4.newRandomConfig()` from
a JUnit 4 `@BeforeClass` (static import, required because the base is
`SolrTestCase` itself). The core booted and the test ran twice, so the
harness failure mode from round 1 is resolved.

## Probe run

- Head: apache main `83d560030f` (SOLR-18585), fresh worktree
  `solr-worktrees/17370-premise`, scratch probe test only, nothing
  committed or pushed.
- Command: `flock <test-queue lock> solr-gradle.sh :solr:core:test
  --tests "org.apache.solr.search.facet.TestSolr17370PremiseProbe"`.
- Run 1: BUILD SUCCESSFUL, 1 test, 0 failures, seed
  `1707749D2E484380`. Run 2 (with count assertions): BUILD SUCCESSFUL,
  1 test, 0 failures, seed `75A0DC2AA947A9D`.
- Evidence: fresh JUnit XML
  `solr/core/build/test-results/test/TEST-org.apache.solr.search.facet.TestSolr17370PremiseProbe.xml`
  in the worktree, plus run logs `~/workspace/tools/17370-premise-probe.log`
  and `17370-premise-probe2.log` on the phone agent host.
- Docs indexed: a multiValued stored string field `cat` holding
  `123`, `456`, `789`, `abc`, `xyz` (numeric-looking values mixed with
  alpha), plus a second field `subject`, matching the triage shape
  (string field with numeric values, faceted with a second field).

Each variant ran the search and serialized the full
`SolrQueryResponse` values through `JavaBinCodec.marshal`, the exact
step `JavaBinResponseWriter.write` performs (the ticket's failure
point). All six serialized cleanly:

- legacy facet.field on `cat` plus `subject`: OK, 326 bytes; counts
  verified in run 2 (`cat` 123=2, 456=1, so the marshalled response
  carried real facet counts, not an empty shell)
- legacy facet.field on `cat` with mincount 1: OK, 280 bytes
- JSON terms facet on `cat` with a terms sub-facet on `subject`: OK,
  367 bytes
- pivot facet `cat,subject`: OK, 590 bytes
- facet.field on `cat` plus facet.query `cat:123`: OK, 290 bytes
- facet.field on `cat` with debug=all (exercises the facet debug
  paths): OK, 1040 bytes

## Code reading

No response path was found that puts a `Query` object into a search
response: legacy facet sections carry counts, pivot `queries` entries
carry counts, JSON facet responses carry counts, and debug sections
carry strings (`parsed_filter_queries` is a list of query strings).
This matches the one maintainer comment on the closed prior PR #3346
(Smiley, 2026-05-21): the mechanism by which a Point query would reach
the serializer was unknown, and a test would help. PR #3346 itself
offered only a defensive fallback in `getReflectWriter`, no test, and
closed unmerged.

## Scope limits, stated plainly

- Single-node core only. The ticket's stack came from a SolrCloud
  node, but the response-writing path is the same code, and the
  distributed facet sections carry the same count shapes.
- The ticket never supplied the minimal reproduction Jan Hoydahl asked
  for (2024-07-28), and no query in the ticket text beyond "search
  with facets" is known. A different facet shape, filter, or schema
  detail from the reporter's deployment could still trigger it.
- This verdict kills the premise as stated (string field with numeric
  values plus faceting), not the possibility of a Query object leaking
  into some other response shape.

## Next

Unit 2 (SOLR-17370 implementation) is skipped. The batch-2 queue moves
to unit 3 (SOLR-7351 premise rescope).
