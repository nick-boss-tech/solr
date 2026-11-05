# PR 5291 review, round 1

Status: item 1 complete. Review performed at the live PR head, with a final state refresh at 2026-11-05 05:53 UTC. This claim covers the review only; no adjacency scan slice is claimed.

## Reviewed revision

- PR: [apache/solr#5291](https://github.com/apache/solr/pull/5291), `SOLR-18621: Add SolrParams.of(String...) and ModifiableSolrParams.of(String...)`.
- State: OPEN, ready for review, one commit, mergeable with `UNSTABLE` checks.
- Author: David Smiley (`dsmiley`).
- Base: `82abb86bb5b7326fe48c117e4de69981a43e4251`.
- Head: `600e0ec4afd82004a94852c419892897be14e6f0`, verified against the live PR and fetched `refs/pull/5291/head`.
- Scope: 11 files, 97 additions, 35 deletions. One conversation comment; no reviews or inline comments.

## Verdict: Needs work

The implementation centralizes the repeated pair-building logic and changes only the six helper definitions named by SOLR-18621. Two API issues need resolution before maintainer review.

### M1. MEDIUM: The immutable factory exposes mutable value arrays

[`SolrParams.of(String...)` wraps only the map](https://github.com/apache/solr/blob/600e0ec4afd82004a94852c419892897be14e6f0/solr/solrj/src/java/org/apache/solr/common/params/SolrParams.java#L526-L528). `MultiMapSolrParams.getParams` returns the stored array ([line 73](https://github.com/apache/solr/blob/600e0ec4afd82004a94852c419892897be14e6f0/solr/solrj/src/java/org/apache/solr/common/params/MultiMapSolrParams.java#L73)), and `getMap` exposes it as well ([line 87](https://github.com/apache/solr/blob/600e0ec4afd82004a94852c419892897be14e6f0/solr/solrj/src/java/org/apache/solr/common/params/MultiMapSolrParams.java#L87)). For a multi-pair result, changing `p.getParams("a")[0]` changes the value subsequently returned by `p.get("a")`. The test checks map `put` and `remove` only ([SolrParamTest.java:421-433](https://github.com/apache/solr/blob/600e0ec4afd82004a94852c419892897be14e6f0/solr/solrj/src/test/org/apache/solr/common/params/SolrParamTest.java#L421-L433)). This violates the Javadoc and the ticket's unmodifiable-result contract. Copy arrays and prevent mutable array exposure; add a mutation assertion.

### M2. MEDIUM: The new overload breaks source compatibility for `of(null)`

The existing `ModifiableSolrParams.of(SolrParams)` accepts null and returns an empty object ([line 58](https://github.com/apache/solr/blob/600e0ec4afd82004a94852c419892897be14e6f0/solr/solrj/src/java/org/apache/solr/common/params/ModifiableSolrParams.java#L58)). Adding [`of(String...)`](https://github.com/apache/solr/blob/600e0ec4afd82004a94852c419892897be14e6f0/solr/solrj/src/java/org/apache/solr/common/params/ModifiableSolrParams.java#L81) makes the old `ModifiableSolrParams.of(null)` source call ambiguous. The in-tree test now casts null to `SolrParams` ([ModifiableSolrParamsTest.java:59](https://github.com/apache/solr/blob/600e0ec4afd82004a94852c419892897be14e6f0/solr/solrj/src/test/org/apache/solr/common/params/ModifiableSolrParamsTest.java#L59)). This is a source-compatibility break for existing callers. Reconsider the overload shape or explicitly accept the compatibility impact.

## Behavior and design assessment

- Zero pairs produce empty params. Odd counts throw `IllegalArgumentException`. Duplicate names retain all values in order. Tests cover these paths.
- Null name/value normalization matches the JIRA text. The PR author questions this choice in the only conversation comment and suggests treating null pairs as absent. The ticket and implementation currently agree; this remains a design preference, not a demonstrated defect.
- The modifiable factory returns a mutable `ModifiableSolrParams`, and the tests exercise `set`.
- The six delegated helpers are `SolrTestCaseJ4.params`, `CollectionsHandler.params`, `CoreAdminHandler.params`, `SyncStrategy.params`, `StreamTool.params`, and `SolrBenchState.params`. Their callers are unchanged, as scoped by the ticket. The SolrJ params package is a reasonable shared extension point.
- No new trust boundary or serialization behavior was found.

## Checks

The PR checks captured at head `600e0ec4afd82004a94852c419892897be14e6f0` show:

- Crave core tests failed: 6,244 tests, one failure, 272 skipped. The failure was `org.apache.solr.search.TestMultiWordSynonyms.testPf`, seed `B3B4534A1A4D09F4`, on merge commit `fe84bf4e484`. This review did not determine its cause. [Run](https://github.com/apache/solr/actions/runs/41911581722)
- Gradle precommit `check`, SolrJ tests, Solr script tests, changelog validation, and labeler passed. [check](https://github.com/apache/solr/actions/runs/41911581766), [SolrJ](https://github.com/apache/solr/actions/runs/41911582103), [script](https://github.com/apache/solr/actions/runs/41911581610), [changelog](https://github.com/apache/solr/actions/runs/41911581785), [labeler](https://github.com/apache/solr/actions/runs/41911581965).
- Windows review policy prohibited local compilation, formatting, or tests; none were run here.

No code, branch, PR, JIRA, or formal review action was taken. Local evidence: `research/branch-reviews/2026-11-05-pr-batch/evidence/PR-5291/`. Local review: `research/branch-reviews/2026-11-05-pr-batch/PR-5291-review.md`.
