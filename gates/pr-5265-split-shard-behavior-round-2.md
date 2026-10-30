# Gate: split-shard migration behavior repairs, round 2

Status: OPEN

- Target: PR 5265, SOLR-16398, `nick-boss-tech/solr:solr-16398-split-shard`.
- Exact candidate: `b1f09ab79a3d3f1943e62898512447f836342b34`.
- Parent/unfixed migration: `0495ffa2b00772189d2f92d83b32140f1b5e8b39`.
- Implementation claim: `claims/pr-5265-split-shard-behavior-round-2-windows.md`; report: `reports/pr-5265-split-shard-behavior-round-2.md`.
- Capability tags: gate, review, records. Intended host: any capable Linux gate host with JDK 21 and the pinned Solr build dependencies, running under its serialized build lock.
- Owner direction: patch the existing PR branch for the timeout, timing-output and conflicting-options findings. A normal fork push is authorized. SOLR-17241 remains separate and is not a prerequisite.
- State at posting: fork ref and live PR head independently read back equal to the candidate. Implementation is pushed; compilation, tests, formatting proof and mutation proof are pending.

## Repairs and acceptance criteria

1. Split-shard submits using `DEFAULT_COLLECTION_OP_TIMEOUT * 5`, matching the old handler's fifteen-minute default. New timeout overloads in `AdminAPIBase` retain the existing default for other consumers. `SplitShardMessageMappingTest.testSplitShardUsesExtendedTimeout` captures the actual runner argument; `testOtherCommandsRetainDefaultTimeout` asserts the existing four-argument accumulating helper still uses the default. No timed sleep or multi-minute split is needed.
2. `SplitShardResponse` extends `SubResponseAccumulatingJerseyResponse`, adds a map for timing, and is the endpoint's declared response type. The resource copies the returned timing NamedList into it. `testSplitShardPreservesTimingAndSubResponses` checks timing plus common response fields; `testSplitShardOmitsUnrequestedTiming` checks absence. `SplitShardAPITest.testSplitShard` requests timing and asserts both total time and the nested `splitParentCore.time` number through the regenerated typed client and real HTTP stack.
3. Validate body option conflicts before command submission: ranges/splitKey; numSubShards with either; splitFuzz with either. Five unit tests assert HTTP 400's SolrException code/message and no runner interactions. The generated-client test `testRejectsConflictingSplitOptions` asserts HTTP 400 for explicit ranges plus numSubShards. Valid mapping alternatives, empty/null bodies, async and ranges behavior remain covered.

## Required positive proof at the candidate

Read the branch and gate before claiming. Perform the detached tidy preflight and use a fresh isolated worktree at the full SHA. Record actual commands, JDK, host, UTC times, seed, exit codes and fresh log/XML paths. Do not use another task's worktree or cached XML.

1. Tidy at the exact head, including changed API and core files. Run the repo's `tidy` task under the host wrapper and verify no tracked changes remain. Windows formatted by hand and did not run Gradle.
2. Compile with Error Prone and regenerate the OpenAPI client: `:solr:api:compileJava :solr:solrj:compileJava :solr:core:compileJava :solr:core:compileTestJava -Pvalidation.errorprone=true`. Confirm the generated `ShardsApi.SplitShard` returns `SplitShardResponse` with timing accessible.
3. Focused `:solr:core:test` filters with seed `16398C0DE`:
   - `org.apache.solr.handler.admin.api.SplitShardMessageMappingTest`
   - `org.apache.solr.handler.admin.api.SplitShardAPITest`
   - `org.apache.solr.handler.admin.TestCollectionAPIs`
4. Neighboring coverage justified by the shared helper overloads: `org.apache.solr.handler.admin.api.CreateShardAPITest`, `org.apache.solr.handler.admin.api.DeleteShardAPITest`, and `org.apache.solr.handler.V2ApiIntegrationTest`, with the same seed. These may be included in the same test invocation as step 3. Record per-suite counts; no count is claimed from unexecuted source.
5. Module check `:solr:core:check -x test` at the same head. Inspect failures rather than silently excluding tasks.
6. Full patch `git diff --check`; clean tree; verify the remote branch and live PR still equal the tested SHA.

If tidy changes the patch, commit and normally push the repair to the existing fork branch, post a superseding gate naming its full SHA, and obtain fresh positive proof at that head. A receipt for `0495ffa2b0` is stale for this repair. Do not alter the original gate or another host's receipt.

## Required regression discrimination

Use separate disposable Linux worktrees or sequentially restored patches at the candidate. Keep the candidate response model and endpoint signatures so the regression assertions compile. These are controlled mutations restoring the three old behaviors, not an unchanged-parent runtime run; label them that way in the receipt. Never push a mutated tree.

- Timeout mutation: change only the split submission timeout from `DEFAULT_COLLECTION_OP_TIMEOUT * 5` to `DEFAULT_COLLECTION_OP_TIMEOUT`. Run `SplitShardMessageMappingTest.testSplitShardUsesExtendedTimeout`; it must fail because the captured runner timeout is 180000 rather than 900000.
- Timing mutation: omit the timing-copy block in `SplitShard.splitShard`, keeping the response type declaration. Run `SplitShardMessageMappingTest.testSplitShardPreservesTimingAndSubResponses`; it must fail for missing timing, with the common response still supported.
- Validation mutation: omit `validateSplitOptions(requestBody)` from the resource. Run the five `SplitShardMessageMappingTest.testRejects*` methods; they must fail because the resource accepts and submits the conflicting options. Do not run a real cloud split as a negative mutation probe.

Assertion or Mockito-verification failures establish discrimination. Compile errors, missing methods and setup failures do not. Record the exact mutant diff, command, seed, failure and raw log/XML locations. Restore each mutant, confirm candidate identity and a clean tree, and finish with the positive focused suite at the unmodified candidate. If a test passes on its mutation, investigate and repair the assertion; do not call it a proven regression.

## Deliverables and authority

- Add an immutable gate claim before execution, then a new report and `receipts/pr-5265-split-shard-behavior-round-2.md` only when every required leg passes. Record the full tested and live heads, all proof legs and remaining limits.
- If a required run fails, post a failure report and diagnosis tied to this job. Repair and supersede a changed head; never turn an unexplained failure into a pass.
- Update the existing PR branch with ordinary fork pushes only. No force push, PR body edit, public comment/review or JIRA write is authorized by this job. The body still cites the prior head and generic response type; prepare any corrected text locally after proof.
- SOLR-17241 and the non-hosting JavaBin path are excluded from this gate. The JSON-client proof here does not establish their correctness.
