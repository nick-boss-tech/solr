# PR 5131 (SOLR-5394): body draft, round 2 item 2

- Head: 74532d0f76a6d4bb59b3e156b28f1823c5661fba (live body read 2026-10-18T04:33Z; branch head matches).
- Visible characters: 3456 (limit 3,500).
- Proof numbers come only from receipts/SOLR-5394.md: SimpleFacetsTest 50 tests, 0 failures, 0 errors, 1 skipped at 74532d0f76 (fresh JUnit XML 2026-10-15T02:50:25Z); pre-fix failure on base b6b2b8f10e98 with `expected:<1> but was:<-1>`.
- The draft does not say the head is gated. The gate decision is Nick's.
- Changes from the stored body: the scope sentence now says explicit `facet.method=fcs` also reaches non-string single-valued fields such as BoolField. Trimmed under 3,500 visible characters: the 6e4c302 run sentence, the reference-guide sentence and the Dmitry Kan sentence are dropped.

---

🤖 *AI text below* 🤖 *(posted on behalf of Nick Shanin)*

https://issues.apache.org/jira/browse/SOLR-5394

## What happens today

**With facet.method=fcs and no local params, the segment tasks for a field all start at once.**

`parseParams` starts the per-field thread count at -1 before this change ([SimpleFacets.java#L183](https://github.com/nick-boss-tech/solr/blob/b6b2b8f10e9827e3b86e44649fb7966ce646c185/solr/core/src/java/org/apache/solr/request/SimpleFacets.java#L183)). A value of 0 or less means no limit in `PerSegmentSingleValuedFaceting` ([L128](https://github.com/nick-boss-tech/solr/blob/b6b2b8f10e9827e3b86e44649fb7966ce646c185/solr/core/src/java/org/apache/solr/request/PerSegmentSingleValuedFaceting.java#L128)), so each segment gets a task on the facet executor. The ticket reports 46 facet executor threads from two fields, per the reporter's estimate. The global `facet.threads` parameter does not reach this path ([SimpleFacets.java#L885](https://github.com/nick-boss-tech/solr/blob/b6b2b8f10e9827e3b86e44649fb7966ce646c185/solr/core/src/java/org/apache/solr/request/SimpleFacets.java#L885)). Links point at the merge base.

## What this change does

**The default per-field thread count changes from -1 (no limit) to 1.**

`parseParams` now starts at 1 ([SimpleFacets.java#L183](https://github.com/nick-boss-tech/solr/blob/74532d0f76a6d4bb59b3e156b28f1823c5661fba/solr/core/src/java/org/apache/solr/request/SimpleFacets.java#L183)). A `threads` local param still overrides it ([L208-L211](https://github.com/nick-boss-tech/solr/blob/74532d0f76a6d4bb59b3e156b28f1823c5661fba/solr/core/src/java/org/apache/solr/request/SimpleFacets.java#L208-L211)). With the default, one segment runs at a time on the facet executor. `{!threads=0}` still runs segments on the calling thread, `{!threads=N}` still runs N at a time, and `{!threads=-1}` still means unlimited.

This is a behavior change when the request uses `facet.method=fcs` and sets no `threads` local param. It covers single-valued string fields. Explicit `facet.method=fcs` also reaches other single-valued fields, such as BoolField. It also covers `facet.method=uif` requests that fall back to fcs (see Limits). Other requests are unaffected.

## Proof

**The new test fails on the base code. At head `74532d0f76a6d4bb59b3e156b28f1823c5661fba`, the class runs 50 tests with no failures.**

`SimpleFacetsTest.testFacetThreadsDefaultIsSingleThread` checks the parsed thread count for plain, keyed, `threads=N`, and exclude-tag cases (`{!ex=...}`, with and without `threads=3`). On base `b6b2b8f10e98` it fails with `expected:<1> but was:<-1>`. At head `74532d0`, verified 2026-10-15: 50 tests, 0 failures, 0 errors, and 1 skipped.

`testUifFallsBackToFcsForSingleValuedString` pins the existing uif-to-fcs fallback, so it is not fail-before proof.

## A choice to check

**The fcs default is 1 in this change. The other route makes `facet.threads` control the fcs path.**

- Implemented: segments still run on pool threads, one at a time. `facet.threads=0` does not switch fcs to the calling thread.
- Alternative: read `facet.threads` on the fcs path, so `facet.threads=0` runs segments on the calling thread. The cost is that `facet.threads` would then set parallelism between segments as well as between fields.

A 2014 comment on the ticket proposed a default of 1. The reporter expected `facet.threads=0` to stop the extra threads. Was a default of 1 the right call, or should `facet.threads` control this path?

## Limits

**The fcs path still ignores `facet.threads`, and performance is not measured.**

- The reporter's exact case (`facet.threads=0` with `facet.method=fcs`) still uses the facet executor, one segment at a time.
- No benchmark was run. On a multi-core host, explicit fcs over several segments may be slower than before.
- Per-field workaround: `facet.field={!threads=0}<field>` with `facet.method=fcs` counts that field's segments inline, with no segment tasks on the facet executor.
- Tests check the parsed value, not execution. `testUifFallsBackToFcsForSingleValuedString` only checks that the fallback resolves to fcs.

Changelog: [`changelog/unreleased/SOLR-5394-fcs-default-threads.yml`](https://github.com/nick-boss-tech/solr/blob/74532d0f76a6d4bb59b3e156b28f1823c5661fba/changelog/unreleased/SOLR-5394-fcs-default-threads.yml)

### AI assistance

AI agents assisted with research, implementation, review, and drafting. Nick Shanin directed the work and takes responsibility for this contribution.
