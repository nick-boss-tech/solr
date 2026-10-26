# Gate: PR 5108 deterministic reverse shard merge, round 3

Status: OPEN

- Intended host: any capable Linux CPU gate host with JDK 21, one gate at a time under its host build lock.
- Tags: gate, premise-run, review, records.
- Posted by: windows, 2026-10-26, following Nick's instruction to implement the targeted regression and hand off the gate.
- Implementation claim: `claims/pr-5108-reverse-merge-regression-round-3-windows.md`.

## Exact input and scope

- Fork: `https://github.com/nick-boss-tech/solr.git`.
- Branch: `solr-17786-reverse-cursor-submit`.
- Head: `c5a16393d2b0d2ad8b05f66697a906733e551b69`.
- Parent: `fde070281383f113a331a9ec5ccde173b66fac3c`, the previously offered and round-2-gated head.
- Delta: 105 added lines, only `solr/core/src/test/org/apache/solr/cloud/DistribCursorPagingTest.java`. No production, guide, dependency, or changelog changes.
- Public apache/solr#5108 remains at `32f80171030ca1108cd20292f50232d2729ed930`. Houston's question is `https://github.com/apache/solr/pull/5108#issuecomment-6425176011`.
- Existing receipt `receipts/SOLR-17786.md` is GREEN for the parent only. This job supersedes round 2 for proof of the new branch head; preserve all older records. Current status is Implemented, Proof Pending.
- Claim this job with a new file `claims/pr-5108-reverse-merge-round-3-<host>.md`, after checking competing claims, exact remote head, resource headroom, host build lock, and clean detached preflight. Do not run duplicate gates on this job.

## What the new tests establish

`testReverseCursorMergeAcrossShards` and `testReverseCursorMergeWithTiesAcrossShards` select deterministic IDs using the collection router, then query the actual shard leaders with `distrib=false` to verify exact placement. IDs have ordered prefixes `doc100` through `doc110`; suffixes select the intended shard without changing their relative order. The ordinal-to-shard fixture is:

- First shard: `[0,3,6,9,10]`.
- Second shard: `[1,2,4,5,7,8]`.

For `int asc,id asc`, paging backwards from document 10 with rows=3 makes the shards contribute `[9,6,3]` and `[8,7,5]`. The coordinator must select `[9,8,7]` and present exactly `[7,8,9]`. Expected IDs are hard-coded slices of the constructed IDs, not derived from a full distributed query. The tied variant stores `int = ordinal / 3`, so documents 6, 7, and 8 tie across shards and the page cutoff depends on reversing the unique-key comparison correctly.

Both tests exercise single-pass and two-phase field fetching with both `multiThreaded` request settings, compare both cursor marks against the equivalent forward page, go backward/forward, continue to a short first page and exhaustion, and request a one-row reverse page. The anchor request uses a one-document filter only to obtain its cursor; subsequent navigation uses the full query. Record actual shard names and IDs in the receipt. The request setting alone is not proof of parallel collectors; inspect and report the actual path if making a concurrency claim.

## Positive and formatting legs (Linux only)

Work at the exact head in a clean detached checkout. Preserve full commands, exit codes, seeds, fresh XML per invocation and output logs. Do not count UP-TO-DATE test output as fresh execution.

1. Detached preflight: `./gradlew tidy`; require a clean resulting tree. Check the parent-to-head delta is test-only and `git diff --check` passes. If tidy changes the test, make a scoped repair commit with Nick's ICLA identity, fast-forward push it to the same fork branch, verify the remote head, and add a superseding job pinned to that head before treating any later proof as final. Do not silently test a dirty repair under the old SHA.
2. Compile with Error Prone: `./gradlew :solr:core:compileJava :solr:core:compileTestJava -Pvalidation.errorprone=true`.
3. Focused new tests, seed A:

```sh
./gradlew :solr:core:test --tests org.apache.solr.cloud.DistribCursorPagingTest.testReverseCursorMergeAcrossShards --tests org.apache.solr.cloud.DistribCursorPagingTest.testReverseCursorMergeWithTiesAcrossShards -Ptests.seed=5108C0DE
```

4. Repeat that exact focused command with `-Ptests.seed=17786C0DE`.
5. Existing cursor/ordering suites plus both new tests at the final exact head:

```sh
./gradlew :solr:core:test --tests org.apache.solr.search.SortSpecReverseTest --tests org.apache.solr.search.CursorMarkTest --tests org.apache.solr.CursorPagingTest --tests org.apache.solr.cloud.DistribCursorPagingTest -Ptests.seed=5108C0DE
./gradlew :solr:solrj:test --tests org.apache.solr.client.solrj.response.QueryResponseTest -Ptests.seed=5108C0DE
```

6. On a pristine final checkout: `./gradlew :solr:core:check :solr:solrj:check -x test`. The guide is unchanged by this delta; the parent's guide proof remains historical evidence, not a new-head guide run. A guide rebuild is not required by this test-only job.

## Required discriminating mutations

Each mutation starts independently from the pristine exact head in a disposable checkout. Keep test code and shard-side sort setup unchanged. Capture each mutation patch and prove that only the intended coordinator behavior changed. Mutations never go to the submit branch. Run them serially with the host resource/build lock rules.

### M1: coordinator merges under the original comparator

In `QueryComponent.mergeIds`, immediately after `SortSpec ss = rb.getSortSpec();`, insert:

```java
if (rb.getCursorMark() != null && rb.getCursorMark().isReverse()) {
  ss = rb.getCursorMark().getSortSpec();
}
```

The cursor retains the original requested sort; `rb.getSortSpec()` is the inverse sort for backward requests. This deliberately changes only the coordinator's merge comparator. Leave `SearchHandler` inversion, shard collection order, and final response-position reversal intact.

Run the focused two-test command from leg 3 with seed `5108C0DE`. Both new tests must execute and fail at the exact reverse-page document assertion, with wrong selected identities, not merely a different order of the same three IDs. Include expected/actual IDs and compare their sets to distinguish selection from presentation. Compilation errors, missing tests, and setup failures are not discrimination. If either test passes or only presentation differs, investigate and strengthen the regression before claiming the intended proof.

### M2: coordinator fails to invert the unique-key tiebreaker

In a separate pristine checkout, immediately before the queue creation in `QueryComponent.mergeIds`, insert:

```java
if (rb.getCursorMark() != null && rb.getCursorMark().isReverse()) {
  sortFields = sortFields.clone();
  SortField[] original = rb.getCursorMark().getSortSpec().getSort().getSort();
  sortFields[sortFields.length - 1] = original[original.length - 1];
}
```

Keep the primary comparator reversed, all shard sorting unchanged, and final presentation reversal intact. This fixture puts `id` last in the sort. Run:

```sh
./gradlew :solr:core:test --tests org.apache.solr.cloud.DistribCursorPagingTest.testReverseCursorMergeWithTiesAcrossShards -Ptests.seed=5108C0DE
```

Require a document-selection assertion failure in the tied regression and record expected/actual IDs. A passing mutation is an unproven test property, not successful negative proof. Restore pristine state and finish with a fresh positive focused run and the final checks on the pinned clean head.

## Completion and repair rules

A GREEN receipt requires every named positive, formatting/check and mutation leg. Update `receipts/SOLR-17786.md` as the gating host with its reviewed blob SHA, preserve the earlier proof in the record, and identify this job, claim, final tested SHA, commands, JUnit totals, exact mutation failures, actual layout, log/XML locations, and limits. Add a round-3 report with evidence links. If the receipt's single writer needs to transfer to a different gate host, follow the live board ownership rules before writing; preserve another host's current evidence.

A failure requires an add-only verdict naming the claim and concrete failing leg. Repair and rerun only as a documented repair; do not repeat seeds until green. A test/formatting fix must be committed, pushed, and covered by an add-only superseding exact-head job. A newly exposed production defect needs diagnosis and a scoped implementation handoff; do not suppress it or fold an unexplained production change into this test-only gate.

No Windows build or test was run. No benchmark is requested. This gate assignment delegates Linux proof only; public PR/JIRA actions are outside the gate worker's deliverables.
