# Gate: PR 5291 / SOLR-18621 parameter factory fixes, round 1

Status: OPEN. Implemented, Proof Pending; no Linux execution claim exists at posting.

- Intended host: any capable Linux gate host with JDK 21, the branch's pinned Gradle 9.7.0 wrapper/dependencies, and the build tools required by root check. Serialize under the host build lock.
- Capability tags: gate, implementation, review, records.
- Fork repository: `nick-boss-tech/solr`. Branch: `solr-18621-params-factory-fixes`.
- Exact H1: `f2952c41c7c023aa6e79868df9efe8c65b02c993`, independently confirmed through the fork refs API after push.
- Contributor H0: `600e0ec4afd82004a94852c419892897be14e6f0`; PR 5291 base/merge-base `82abb86bb5b7326fe48c117e4de69981a43e4251`. H1 is one ordinary child commit of H0. The upstream PR stays at H0.
- Implementation claim: `claims/pr-5291-params-factory-fixes-round-1-windows.md`. Report: `reports/pr-5291-params-factory-fixes-round-1.md`.
- Receipt reserved: `receipts/PR-5291-params-factory-fixes-round-1.md`, owned by the executing gate host.
- Seed: `5291C0DE`. Modules: solrj, test-framework, core, benchmark.
- Scope: 11 files, +143/-26 against H0; the original PR's two production factory files, two test files, six helper files and changelog. No dependency change.

## Claim and preflight

Refresh the board, live PR and exact fork ref; check for an existing live gate claim/receipt. Require H1 before running. Record Linux host, clean task-specific checkout, JDK, runner, build lock, commands and fresh log/XML locations in a new add-only claim. Run the detached tidy preflight. A posted job is not an execution claim or a pass. Never run concurrent builds in the same checkout or on a host already holding a gate lock.

Windows has run only source inspection and git whitespace/scope checks. It ran no compiler, Gradle, formatter or tests. Upstream CI does not cover H1. Nick has confirmed that TestMultiWordSynonyms.testPf is unrelated and tracked in another PR; do not add a synonym reproduction or repair to this gate.

## Required positive proof at H1

Run each build command through the host's standard gate runner, retain separate logs and record exit codes. Preserve a pristine H1 checkout for the positive leg.

1. Run `./gradlew tidy` and `git diff --check`; require no tracked changes. Validate `changelog/unreleased/SOLR-18621.yml` with the repository's changelog parser/check and retain output. A formatting repair changes the head and requires the repair route below.
2. Compile the changed source/test sets and all renamed helper consumers with Error Prone:

   `./gradlew :solr:solrj:compileJava :solr:solrj:compileTestJava :solr:test-framework:compileJava :solr:core:compileJava :solr:core:compileTestJava :solr:benchmark:compileJava -Pvalidation.errorprone=true`

   Require `ModifiableSolrParamsTest.testOf` to compile with the exact uncast `ModifiableSolrParams.of(null)` call present in H1. This is source compatibility proof, not a runtime fail-before claim. Do not add a cast as a repair.
3. Run the focused suites and the neighboring parameter suites:

   `./gradlew :solr:solrj:cleanTest :solr:solrj:test --tests org.apache.solr.common.params.SolrParamTest --tests org.apache.solr.common.params.ModifiableSolrParamsTest --tests org.apache.solr.common.params.ShardParamsTest --tests org.apache.solr.common.params.CommonParamsTest --tests org.apache.solr.common.params.CommonAdminParamsTest -Ptests.seed=5291C0DE --rerun-tasks`

   Require every selected class and all newly added methods to execute with zero failures, errors or skips; record fresh XML counts by suite. In particular testOf must return an empty result for the uncast null call.
4. Run neighboring production/test-framework helper consumers:

   `./gradlew :solr:core:cleanTest :solr:core:test --tests org.apache.solr.handler.admin.CoreAdminHandlerTest --tests org.apache.solr.cli.StreamToolTest -Ptests.seed=5291C0DE --rerun-tasks`

   Require zero failures/errors and record any pre-existing assumption skips individually. Every targeted class must execute; an entirely skipped class is incomplete proof. Do not broaden this to the full core suite.
5. Run `./gradlew check -x test`. Require success and record any execution environment condition beside the result.

## Required negative proof: original shallow-wrapper algorithm

Use a separate disposable H1-based checkout. Keep every H1 test unchanged and keep both ofPairs signatures so the tests compile. Restore only the original H0 shallow-wrapper algorithm in SolrParams.ofPairs, mechanically adapting its internal factory call to the renamed API. Retain the empty-input fast path and replace exactly:

```java
    return new ImmutableSolrParams(namesAndValues);
```

with:

```java
    return new MultiMapSolrParams(
        Collections.unmodifiableMap(ModifiableSolrParams.ofPairs(namesAndValues).getMap()));
```

This is a synthetic production baseline using H0's algorithm with the H1 API scaffold. Save its exact diff and source/test hashes; label it as adapted rather than claiming the unmodified H0 test suite failed. Do not change MultiMapSolrParams, the tests or the mutable pair builder. Do not remove the API or inject an exception.

Run:

`./gradlew :solr:solrj:cleanTest :solr:solrj:test --tests org.apache.solr.common.params.SolrParamTest.testOfPairsGetParamsIsIsolated --tests org.apache.solr.common.params.SolrParamTest.testOfPairsIteratorIsIsolated --tests org.apache.solr.common.params.SolrParamTest.testOfPairsMapViewIsIsolated --tests org.apache.solr.common.params.SolrParamTest.testOfPairsMapCopyIsIsolated --tests org.apache.solr.common.params.SolrParamTest.testOfPairsWriteMapIsIsolated --tests org.apache.solr.common.params.SolrParamTest.testOfPairsModifiableCopyIsIsolated -Ptests.seed=5291C0DE --rerun-tasks`

Require all six methods to execute and each to fail at its intended array-isolation assertion: original `a` values must be `[1, 2]` but are observed with `changed` in the first slot. Compilation failure, missing API, startup failure, skip, or an unrelated assertion is not proof. Record each intended failure from fresh logs/XML. The positive six must pass at clean H1. Secondary assertions after the first failing assertion, input-copy tests and other contract checks remain coverage pins unless separately mutation-proven; do not describe all added assertions as fail-before proven.

The null-literal issue is deliberately separate: restoring H0's of(String...) overload would be expected to cause a compiler ambiguity, which is appropriate evidence for M2 but cannot count as one of these six runtime failures. No additional compilation-negative leg is required once H1's original uncast call compiles and runs.

## Failures and repairs

- An executed test failure must be investigated from its logs/XML before any repair/retry. Preserve the failed run and reason; do not silently rerun or weaken acceptance criteria.
- If setup prevented all selected tests from executing, record the cause and exact repair; one relaunch is allowed under WORKFLOW.md. Never count setup interruption as a regression or a pass.
- A source, test or tidy repair requires a scoped ordinary commit on this fork branch using Nick Shanin's identity, no trailers, and no force push. Read back its remote SHA and post a superseding add-only exact-head gate before treating proof as covering that new head. The current gate cannot certify a changed head. Keep the contributor ancestry.
- An API preference belongs in the report; do not silently rename the factories back, add a null cast, change null semantics, or edit the contributor's branch as a gate repair.

## Finish

Verify a clean H1 positive checkout and separately read back the fork ref, requiring H1. Write the receipt with exact tested SHA, host/JDK/Gradle, UTC times, commands/exits, logs, XML counts, six intended runtime failures, positive results, source-compatibility result, changelog validation, tidy and root check. Include a concise description draft and remaining limits. Add a completion report for the gate claim; do not edit the job or implementation claim. If anything required is missing, keep the result pending and name that leg.

The gate authorizes no PR/JIRA comment, PR body edit, merge, contributor branch write or upstream push. After green, the original review owner will refresh the live PR/discussion and consider the separate public follow-up under root AGENTS.md.
