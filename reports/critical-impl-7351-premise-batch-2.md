# SOLR-7351 premise rescope, batch 2 unit 3: SYMPTOM CONFIRMED

Ticket: SOLR-7351, Open, Critical. "MoreLikeThis component doesn't
work with dates." Reporter: Bogdan Marinescu.

Verdict: the defect is real, with the symptom round 1 found, not the
symptom the round-1 assignment stated. MLT on a date field does not
return an empty interesting-terms list; it throws
`Invalid Date String:'1577836800000'`. This run re-scoped the premise
to that actual symptom on current main and reproduced it exactly.

## Reproduction (this run)

- Head: apache/solr main `83d560030fde8451bb9ec3f4bab9bcd58bdd781e`
  (SOLR-18585: intervals script support), fresh worktree, scratch
  probe test only (removed after the run, no branch, no push).
- Probe: `SolrTestCase` test booting the standard test solrconfig and
  schema with the recorded harness fix (`SolrTestCaseJ4.newRandomConfig()`
  from a JUnit 4 `@BeforeClass`; core booted cleanly). Three docs
  (ids 1, 2, 3), each with a `mydate_date_p` DatePointField value
  (2020-01-01, 2020-01-02, 2020-01-03) and text in `name`.
- Control, MLT on `name` for id 1: succeeds, interestingTerms
  `[alpha, beta, gamma, delta]`.
- Date leg, MLT on `mydate_date_p` for id 1 (`mlt.fl=mydate_date_p`,
  `mlt.interestingTerms=list`, mintf=1, mindf=1, minwl=1): fails with
  `org.apache.solr.common.SolrException: Invalid Date String:'1577836800000'`.
- Run: `:solr:core:test --tests org.apache.solr.Solr7351PremiseProbeTest`,
  seed `78A8ACAEF698A220`, test time 1.211s, fresh JUnit XML
  (`TEST-org.apache.solr.Solr7351PremiseProbeTest.xml`,
  2026-11-01T19:27:14Z). Run log: `~/workspace/tools/7351-premise-probe2.log`
  (first attempt failed only on probe ids: the standard test schema
  randomizes `id` to a numeric type on that seed, so string ids were
  rejected before MLT ran; numeric ids fixed it).
- The two `classMethod` entries in the same XML are suite-teardown
  noise (unreleased SolrMetricsContext objects after the thrown
  exception), not part of the verdict.

## Mechanism (stack, exact)

```
MoreLikeThisHandler.handleRequestBody:184
  -> MoreLikeThisHelper.getMoreLikeThis:402
  -> getBoostedMLTQuery:431
  -> Lucene MoreLikeThis.like:561 -> retrieveTerms:731
  -> addTermFrequencies:816
  -> FieldType$DefaultAnalyzer (point-field branch, FieldType.java:590)
  -> PointField.toInternalByteRef
  -> DatePointField.readableToIndexed:193
  -> DatePointField.toNativeType:110
  -> DateMathParser.parseMath:233: "Invalid Date String:'1577836800000'"
```

Lucene MLT re-analyzes the indexed term for the date field, and the
term text it hands back is the epoch-millis decimal
(`1577836800000` for 2020-01-01T00:00:00Z). Re-encoding that string
goes through `readableToIndexed` -> `toNativeType`, which parses
every CharSequence with `DateMathParser.parseMath`, and that parser
rejects anything without an ISO `Z` form. So every MLT request over
a DatePointField dies in term extraction, before any query is built.
Round 1 (packaged server, 2026-10-27) saw the identical error for
both DatePointField and the legacy TrieDateField; this run pins the
mechanism for DatePointField on current main.

## Smallest fix shape (no implementation in this unit)

The narrowest surface is `DatePointField.toNativeType`: accept a pure
epoch-millis string (all digits, optionally negative) as a long
before falling through to `DateMathParser`. That is one method, and
it fixes the MLT path at the exact point that throws. Caveat, which
is why the scope call stays with the owner: it widens date-input
parsing everywhere `toNativeType` is used, not just for MLT, so a
maintainer may instead prefer an MLT-side change (skip or
special-case point-field terms in the MLT term path) that leaves
date parsing untouched. Either shape is small; the choice between a
parsing widening and an MLT-side carve-out is the decision to make
before an implementation unit runs.

## Scope and limits

- Single-node core, standard test schema and config, three docs.
  Not a sweep; TrieDateField was not re-run here (round 1 covered it
  with the same error).
- No implementation, no branch, no push, no PR, no Jira or GitHub
  action. Next step is the owner's scope call: authorize an
  implementation unit with one of the two fix shapes above, or leave
  the ticket at this confirmed-symptom record.
