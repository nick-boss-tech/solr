# Gate: SOLR-18624 phrase-slop and function-boost assertions, round 2

Status: OPEN

- Category: owner-directed follow-up implementation after branch review. Tags: gate, implementation, review, records.
- Intended host: any capable Linux CPU gate host with JDK 21, the pinned Gradle wrapper/dependencies, and the host gate runner. One build at a time under the host lock.
- Fork: `nick-boss-tech/solr:solr-18624-flaky-multiword-synonyms`.
- Exact head H1: `0af4f5b3f2784eba3693acdc33dc49f6c2802b6a`. Stop and reconcile if the live fork ref differs.
- Follow-up diff against H0: one test file, 36 additions and 6 deletions.
- Parent H0: `c610e6d05db5700835a123de54da61137478e48f`; upstream base B: `07ccc73cd24e26e59da169ca35b3fd5f64c35986`. Preserve both ancestors.
- Only outbound path: `solr/core/src/test/org/apache/solr/search/TestMultiWordSynonyms.java`.
- Implementation claim: `claims/solr-18624-assertion-repair-round-2-windows.md`.
- Report: `reports/solr-18624-assertion-repair-round-2.md`.
- Execution receipt: `receipts/SOLR-18624-assertion-repair-round-2.md`, owned by the Linux executor.
- Module: `:solr:core`. Focus: `org.apache.solr.search.TestMultiWordSynonyms.testPf`; full class `org.apache.solr.search.TestMultiWordSynonyms`; neighbor `org.apache.solr.search.TestExtendedDismaxParser.testPfPs`.
- Fixed seeds: `18624C0DE` for focused/repeated/mutation legs; `18624BEEF` for the full class and neighboring method.
- Windows ran no Gradle, compilation, formatting or tests. All assertions are source-reviewed coverage pins until executed and discriminated below.

## Purpose and expected behavior

H1 requires strict baseline score differences, so `boost_d` as a secondary sort cannot hide an omitted `bf` contribution. For `bar foo`, slop 2 must boost the reversed phrase in document 10; slop 0 must leave its score tied with document 13. Both synonym phrase paths must be present with slop 2. For `tropical cyclone foo`, pf2 creates the bigrams `tropical cyclone` and `cyclone foo`; check their exact boosted parsed clauses and all four matching IDs without requiring one arbitrary tied winner.

This is a test repair with no established production bug. H0 and H1 are both expected to compile; H0 can pass because its assertions are weaker. Do not require H1 to fail on unchanged production code, or call a missing API, compilation error, setup error, skipped test or the old flake a successful fail-before proof.

## Claim and preflight

Refresh this job, the fork ref and any gate claim/receipt. First claim wins; do not duplicate a live execution. Run the detached tidy preflight required by START-HERE/WORKFLOW before claiming; use a clean task-specific Linux checkout at H1. Record host, JDK, runner, seed, log and XML directories and UTC start. Retain fresh outputs per leg before another cleanTest overwrites them.

## Required legs

Run each command through the host's standard gate runner.

1. Run `./gradlew tidy` and `git diff --check`. Require a clean tree. Run `./gradlew :solr:core:compileJava :solr:core:compileTestJava -Pvalidation.errorprone=true`.
2. Focused positive/repetition leg at H1:

   `./gradlew :solr:core:cleanTest :solr:core:test --tests org.apache.solr.search.TestMultiWordSynonyms.testPf -Ptests.seed=18624C0DE -Ptests.iters=25 --rerun-tasks`

   Require 25 executions of testPf, zero failures/errors/skips; report actual counts and any generated iteration suffixes from fresh XML.
3. Full-class and neighbor leg at H1:

   `./gradlew :solr:core:cleanTest :solr:core:test --tests org.apache.solr.search.TestMultiWordSynonyms --tests org.apache.solr.search.TestExtendedDismaxParser.testPfPs -Ptests.seed=18624BEEF --rerun-tasks`

   Require every selected test to execute with zero failures/errors/skips. Record actual counts from fresh XML.
4. Query-shape diagnostic in a disposable checkout, separate from acceptance tests: capture IDs, scores and parsed queries for both final slop-2 requests at B or H0. Add only `debugQuery=true` and `sort=score desc,id desc` for the diagnostic. Expect `bar foo` to favor 10, and `tropical cyclone foo` to tie and return 13,12,11,10. Keep the full commands/temporary diagnostic diff and output. The original failure seed is not in SOLR-18624, so label this a deterministic demonstration of the invalid ordering assumption rather than a replay of the reported run. If the behavior differs, investigate before claiming the premise established.
5. Three independent compiling production mutations in disposable H1 checkouts, with H1's test file byte-identical and the API scaffold intact. Reset to H1 between mutations; retain each mutation diff, command, fresh log/XML and exact assertion outcome. Run each with:

   `./gradlew :solr:core:cleanTest :solr:core:test --tests org.apache.solr.search.TestMultiWordSynonyms.testPf -Ptests.seed=18624C0DE --rerun-tasks`

   - M1: for phrase parsing of exactly `bar foo` with slop 2, omit only the generated `bar foo` phrase path and retain `tropical cyclone foo` with slop 2. Keep the main matching query and the rest of the parser functional. Require the restored document-10 score advantage assertion to fail. This demonstrates why merely finding `~2` was insufficient.
   - M2: in eDisMax query assembly, omit the additive queries returned by `getBoostFunctions()`, retaining main and phrase queries. Require a new strict baseline score assertion to fail even though the secondary boost-field sort still yields IDs 13,12,11,10.
   - M3: omit only the `cyclone foo` pf2 bigram for the `tropical cyclone foo` request. Retain the `tropical cyclone` synonym bigram with slop 2 and boost 10. Require the new precise `cyclone foo` parsed-query assertion to fail.

   These are synthetic coverage mutations, not claimed real production defects. A mutation that passes is an unresolved coverage concern; do not label it proven or silently broaden the mutation. Record the outcome and investigate under the repair rules.
6. On restored clean H1, run `./gradlew check -x test`. Require success. Explain any material execution condition and keep unrelated environment interruptions distinct from patch/test failures.
7. Confirm H1's tree is clean and separately read the fork ref. A receipt covers only the exact tested SHA. No mutation may remain in a pushed branch or in the accepted checkout.

## Failure and repair rules

- Do not silently rerun an executed failure. Diagnose and record whether it is an assertion failure, changed query expectation, formatting issue or environment failure.
- If tidy or another check requires a scoped test-file repair, make an ordinary repair commit on the same fork branch, verify the new remote SHA and post a superseding gate naming it before fresh acceptance proof. Never issue a green receipt for a changed tree under H1's SHA.
- Investigate a diagnostic or mutation mismatch before changing expectations. Preserve unchanged acceptance tests for mutation proof; do not weaken assertions just to pass the gate.
- Environment refusal before tests may be relaunched once after a documented repair under WORKFLOW. An executed failing test follows the repair route.

## Receipt and completion

Write `receipts/SOLR-18624-assertion-repair-round-2.md` with exact SHA/ancestry, host/JDK/runner, UTC times, commands/exit codes/seeds, fresh logs and XML counts, query diagnostic output, all three mutation diffs and intended assertion failures, tidy/Error Prone/root-check results and final remote read-back. State any unproven assertion separately. Gate and claim records are add-only; completion is derived from the receipt. Include a factual PR-description draft with the results and limits, but do not publish it.

No PR opening, public comment, JIRA write, force-push or upstream/contributor-branch write is authorized by this gate. Status remains **Implemented, Proof Pending** until every required leg succeeds at the final offered head.
