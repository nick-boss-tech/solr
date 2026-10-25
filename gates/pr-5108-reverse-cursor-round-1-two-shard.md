# Gate: SOLR-17786 / PR 5108 reverse cursor candidate, round 1 two-shard

Status: OPEN

Intended host: any capable Linux CPU gate host, serialized behind its build lock.
Tags: gate, premise-run, settling-run. Repairs require implementation capability.
Owner request: Nick directed a gate of only this pinned candidate and authorized the exact tidy repair plus the gate-required test-fixture topology correction. No product logic, new tests, PR, Jira, comment, or ticket-state action is authorized.

## Immutable input

- Fork: `https://github.com/nick-boss-tech/solr.git`.
- Branch: `solr-17786-reverse-cursor-submit`.
- Head: `2c8af559c8456ded97811455622fb06786ae74ba`.
- Candidate commit parent: `a3d97ca294f43adce99557fe011da90cd2528ce9`; this commit contains only the test-fixture topology correction.
- Public PR #5108 head: `32f80171030ca1108cd20292f50232d2729ed930`, unchanged.
- Upstream merge base: `f3a798e424bb933ba988c8d316d0e8f35c8b6e0f`.
- Supersedes: `gates/pr-5108-reverse-cursor-round-1-post-tidy.md` and, transitively, `gates/pr-5108-reverse-cursor-round-1.md`. The post-tidy job's immutable claim `claims/pool-phone-agent-2-gate-pr-5108-reverse-cursor-round-1-post-tidy.md` is superseded by the new claim below before any gate test runs.
- Fixture repair commit: `2c8af559c8456ded97811455622fb06786ae74ba` (parent `a3d97ca294f43adce99557fe011da90cd2528ce9`), only three lines in `DistribCursorPagingTest`'s constructor: `sliceCount = 2; fixShardCount(2);`. This is a test-fixture-only adjustment, not product logic or a new test. It changes the standard runtime topology from one shard/two cores to two shards/one core each.
- Topology must be confirmed from the actual run: expect two named shards and one node/replica per shard.
- Multithreaded-path check: the test loops both `multiThreaded` values; `core/src/test-files/solr/solr.xml` sets `indexSearcherExecutorThreads=4`. In source, `QueryComponent` propagates `CommonParams.MULTI_THREADED`, `MultiThreadedSearcher.allowMT` requires no post-filter, no segment termination, the true request flag, and a non-null executor, and `SolrIndexSearcher` enters `searchCollectorManagers`. Verify the execution path and actual two-shard layout in run evidence; the parameter/config setting alone is not concurrency proof.
- Nick authorized this exact fixture correction as required by the gate's topology clause. No other source or test changes are authorized.
- Solr ticket: SOLR-17786. Lucene #14775 is Open and unmerged; this branch does not require it or change dependencies.
- Expected receipt: `receipts/SOLR-17786.md`, owned by the executing host, with this job path and exact final tested head.
- Expected claim: `claims/pool-phone-agent-2-gate-pr-5108-reverse-cursor-round-1-two-shard.md`, created only after a clean detached preflight. It must name the superseded post-tidy claim.
- Source and tests only; no authentication or security-reproduction packet is part of this job.

Check the live branch head and existing claims/receipt before claiming. If the branch moved, stop using this input and reconcile through a new add-only job. Read `WORKFLOW.md`, `START-HERE.md` and the host capability file. Do not run concurrently with another gate on the host. Run a detached clean tidy preflight at this new head before creating the new claim.

## Patch and contract

The branch completes SortSpec.reverse using existing SortField comparator APIs, retaining extraction/selector and missing-value semantics. Competitive skipping is disabled for inverse comparators. SearchHandler installs the inverse sort for reverse tokens, so collectors, MT reduction, and distributed merge agree. Local final DocLists reverse IDs and scores together; shards retain collector order, and the coordinator reverses response positions before boundary selection and field fetch.

Zero-row requests return both non-null markers at the supplied boundary. Forward tokens retain their existing list encoding. Backward tokens use the direction map and `<` initial token, passed through the same cursorMark parameter. The abandoned cursorMarkReverse parameter is removed. SolrJ has a previous-cursor getter. A guide section and changelog are included.

## Preflight and compilation

Use a clean detached checkout at the exact head, with JDK 21 and the normal Solr test security policy. Record Java, Gradle, OS, checkout head, original cleanliness, and the actual log paths. No Windows execution is authorized.

Run the detached formatting preflight required by this board. Source was hand-formatted on Windows. If tidy changes files, do not count proof at the old head: commit only ticket-scoped formatting under Nick's identity, plain-push to this fork branch, and add a superseding gate job pinned to the new SHA before execution. Preserve this job and record the old/new head. The same rule applies to code/test repairs. Never force, alter the public PR head, or edit a claimed job's pinned head.

Compile with the unchanged Lucene pin:

```bash
./gradlew :solr:core:compileTestJava :solr:solrj:compileTestJava
```

Fix or record a compile/check failure rather than retrying it as noise. The Windows source review checked the Lucene 10.4.0 release's method/constructor signatures but did not compile. Any claimed compilation success must come from this run.

## Positive proof

Run the existing cursor suites as well as the new tests, with assertions enabled by the standard harness:

```bash
./gradlew :solr:core:test --tests org.apache.solr.search.SortSpecReverseTest --tests org.apache.solr.search.CursorMarkTest --tests org.apache.solr.CursorPagingTest --tests org.apache.solr.cloud.DistribCursorPagingTest -Ptests.seed=5108C0DE
./gradlew :solr:solrj:test --tests org.apache.solr.client.solrj.response.QueryResponseTest -Ptests.seed=5108C0DE
./gradlew :solr:core:test --tests org.apache.solr.search.SortSpecReverseTest --tests org.apache.solr.search.CursorMarkTest --tests org.apache.solr.CursorPagingTest.testReverseCursorPaging --tests org.apache.solr.cloud.DistribCursorPagingTest.testReverseCursorPaging -Ptests.seed=17786C0DE
```

Preserve separate fresh XML/output snapshots for every invocation; do not count cached/up-to-date output as a fresh second-seed run. The tests use both multiThreaded parameter settings, both single-pass/two-phase distributed fetching, eight ordinary/field/function/varying-score sorts, short pages, zero rows, exhaustion, and backwards/forwards resumption. The native collector test covers both MIN/MAX numeric/string selectors and missing values with a low hit threshold.

Confirm the distributed harness has more than one shard. Record its actual shard layout. Test solr.xml sets indexSearcherExecutorThreads=4; confirm the MT collector/reduction path is exercised rather than reporting parameter presence as a concurrency proof. If the selected layout does not exercise that path, name and resolve the coverage limitation through a scoped harness repair and a new pinned input.

## Discriminating proof

The submitted public PR does not compile; its current-head CI already confirms that build blocker. Do not describe it as a runtime fail-before result for these new tests. Use independent disposable checkouts of the compiling candidate for these mutations, never the positive checkout or pushed branch:

1. In QueryResult.setCursorMarkNoResults's zero-row branch, restore the old behavior: reverse input sets `(input, null)` and forward input sets `(null, input)`. Run `CursorMarkTest.testZeroRowCursorMarks`. Expect a null-marker assertion failure. A compile or setup failure is not the expected witness.
2. Remove only the local result-normalization block in QueryComponent.doProcessUngroupedSearch. Run `CursorPagingTest.testReverseCursorPaging`. Expect returned IDs in reverse order where a preceding page is expected in normal order. This separates presentation from boundary extraction.
3. Remove only the response-position reversal immediately after shardDocQueue.resultIds(offset) in QueryComponent.setResultIdsAndResponseDocs. Run `DistribCursorPagingTest.testReverseCursorPaging`. Expect reversed IDs or cursor round-trip mismatch. Keep the sort inversion and shard order unchanged.

Use seed 5108C0DE for each witness, with the same module and exact method filter. Capture each mutation diff, run command, failure assertion, seed, and fresh XML. Restore a pristine positive head for final positive/check evidence. If a mutation passes, report that assertion as an unproven pin and investigate; do not invent fail-before proof or repeatedly run different seeds hoping for failure. Other assertions may remain pins if their discrimination has not been established.

## Checks and finishing

```bash
./gradlew :solr:core:check :solr:solrj:check :solr:solr-ref-guide:check -x test
```

No dependency upgrade or license regeneration is expected. Record whether tasks executed or were up-to-date, and keep the final checkout clean. Do not introduce unrelated formatting, dependency, or workflow changes. Timing/performance is not proved by this correctness job; competitive skipping is deliberately disabled for backward sorts and benchmarking remains open.

The receipt must state the final branch head, all commands and actual JUnit totals/skips/failures, mutation outcomes, source/check log locations, and any remaining limitations. Its final section is a PR-description draft using the board's pr-formula.md with verified counts only. Forward encoding compatibility is intentional; backward cursors require upgraded participating nodes. Keep public submission pending Nick's explicit direction. No PR/comment/JIRA write is authorized by this gate.
