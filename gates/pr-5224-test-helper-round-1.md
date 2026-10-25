# Gate: PR 5224 test helper reader lifetime, round 1

Status: OPEN

- Tags: gate. Intended host: any capable Linux gate host; serialize under the host's build lock.
- Source: Nick's October 25 request to build a helper without the premature reader closure and test it.
- Fork branch: `nick-boss-tech/solr:solr-18585-submit`.
- Exact implementation head: `1f3ed5658862b8fd682fcb999a1d3dd12863957f`.
- Parent/proof base: apache/solr PR 5224 head `f28c6e79c82a9fb74c3e07607b86e74b3d4331d0`, branch `mkhludnev/solr:intervals-script`.
- Outbound delta relative to that parent: `solr/core/src/test/org/apache/solr/search/TestScriptFilterIntervalsSource.java` only, 15 additions and 13 deletions.
- State: Implemented, Proof Pending. `git diff --check` passed on Windows; no build, formatting task, or test ran there.
- Implementation claim: `claims/pr-5224-test-helper-windows-round-1.md`.
- Implementation report: `reports/pr-5224-test-helper-windows-round-1.md`.
- Receipt target: `receipts/PR-5224-test-helper.md`.

## Preflight

Read live WORKFLOW.md and START-HERE.md, check current claims, and confirm the fork head before claiming. Fetch the pinned fork branch into a clean detached Linux checkout with the complete source tree. The implementation host used a sparse checkout; do not copy its sparse settings to the gate host. Record the host, build lock, JDK/toolchain, detached checkout, background runner, logs, and result paths in the gate claim.

Inspect the one-file diff against the parent. The helper now borrows a `LeafReaderContext` and does not create or close a reader. In each of the four test methods, the `DirectoryReader` must remain open until all match iteration and assertions finish, and must close before its `Directory`. Preserve every existing assertion and the four existing test methods.

## Named formatting and proof tasks

At the pinned head, run `./gradlew tidy` and require a clean tree. A tidy repair must be committed with Nick Shanin's ICLA identity and no trailers, pushed plainly to this fork branch, and recorded in a new superseding gate file naming the new head. Do not edit this job or carry proof from an older head forward.

Run the focused class with fresh results:

```sh
./gradlew :solr:core:cleanTest :solr:core:test --tests org.apache.solr.search.TestScriptFilterIntervalsSource -Ptests.seed=6A68830B3A1BB525
```

The seed matches the prior successful upstream CI run for the parent. Require all four existing methods to execute, with zero failures, errors, or skips:

- `testMatchesSkipsRejectedLeadingIntervals`
- `testMatchesFirstAcceptedIntervalIsNotLost`
- `testMatchesNullWhenNoIntervalAccepted`
- `testMatchesNullWhenNoTermInDocument`

Retain the complete test log, fresh `solr/core/build/test-results/test/TEST-org.apache.solr.search.TestScriptFilterIntervalsSource.xml`, and class output. An up-to-date/cache-only run, compile failure, or environment refusal is not a test pass.

Then run `./gradlew check -x test` as required by this branch's AGENTS.md, recording the real result and any independent baseline failure. Do not silently exclude failing checks. Finish with `git diff --check` and a clean tree at the exact verified head.

## Interpretation and completion

This is a test resource-ownership repair, with production code and assertions unchanged. No deterministic failure of the parent helper was reproduced. Tiny postings can be buffered and can allow the old code to pass after reader closure. Do not call a passing parent or passing new helper fail-before proof, and do not claim an observed production defect or measured flake rate.

GREEN requires correct lexical resource ownership in all four methods, the four focused test methods passing from fresh XML, clean tidy, successful required checks, and a clean tree. If tests execute and fail, investigate and repair under WORKFLOW.md rather than blindly retrying. If an environment attempt never executes tests, repair and relaunch at most once with the reason recorded.

Post the gate host's receipt with the exact final fork SHA, commands, JDK, seed, fresh counts/skips, raw evidence paths, and limits. If a repair changes the head, publish the new scoped commit and superseding gate before proving that head. The receipt's final section should give a concise suggested upstream update with observed results, for Nick to review.

No write to apache/solr, mkhludnev/solr, or the live PR, and no public comment, PR opening, or JIRA write is authorized by this gate.
