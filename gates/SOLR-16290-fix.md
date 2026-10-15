# Gate job: SOLR-16290 real facet fix (funded by standing calls round 1, item 4)

- Branch: solr-16290-submit. Head to build on: `ff760120c81792022458a5a290b6e58d57b9cd17` (fork tip, verified by `git ls-remote` on 2026-10-15 at filing). The branch today is a gated test-only pin at that head; this job turns it into the fix branch.
- Status: OPEN, unclaimed.
- Capability tags: implementation, gate.
- Intended host: a Linux host (vm1 or vm2). Claim first under `claims/` per WORKFLOW.md.
- Job type: implement the production fix on the branch, remove the `@AwaitsFix` pin, then the full hardening gate at the resulting head, a plain push on green, and the records refresh.

## Why this job exists

Standing calls round 1 (`reports/standing-calls-round-1.md`, item 4) settled the disposition of this branch: fund the real fix. Not a pin-only PR, and not banking. The pin stays on the branch as the regression test for the fix; the branch stays unopened until this gate is green. The disposition is also recorded in `receipts/SOLR-16290.md` and in `reports/branch-review-verdicts-landing-round-1.md` (last section). This job is the fix step that settlement names.

## The defect and the pin

- The pin is `@AwaitsFix` on `testNestedChildFacetWithExclusionKeepsBlockChildrenDomain` in `TestJsonFacetsWithNestedObjects.java` (line 355 on the branch). It expects the control test's output for a child facet under a block-join domain with a filter exclusion; the nested buckets come back empty instead.
- Recorded premise (`receipts/SOLR-16290.md`, log `g16290-premise.log`): at base `0cc328310f8` with only the branch test file and the awaitsfix group enabled, exactly 1 of 9 tests fails, the pin, with the nested buckets empty for all three author buckets; the control test passes. Seed `16290C0FFEE16290`. Facet production code on main has had no commit since that base (checked in the round 1 settlement), so the premise is expected to hold; re-confirm it in the fail-before leg below rather than assuming it.

## Scope

- Production code: `solr/core/src/java/org/apache/solr/search/facet/FacetProcessor.java`. At the base, `handleFilterExclusions` (about lines 190 to 222) rebuilds the base from the query and the filters minus the excluded tags, and `handleBlockJoin` (about lines 255 to 290) applies the parent-to-child transform. The question is their order and interaction for a child facet under a block-join domain. `FacetParser.java` (lines 209 to 245) parses `excludeTags` and `blockChildren` and is in scope if the trace leads there.
- First step: trace the pin's failing line. The round 1 record deliberately does not name a root cause; the trace is this job's first deliverable and is recorded in the receipt when the gate lands.
- Not settled by this job: how exclusions compose with block-join domains as a behavior question is a Choice for the fix's own PR draft, posed there with the implemented position stated. This job implements a fix and proves it; it does not pre-answer the draft's Choice.
- The branch is test-only today and carries no changelog fragment. A production change requires one: add the fragment for SOLR-16290 as part of the fix, following `dev-docs/changelog.adoc` (type `fixed`), and gate step 0 below parses it.

## Steps, in order

1. Claim under `claims/` (WORKFLOW.md), then confirm the base: `git ls-remote fork refs/heads/solr-16290-submit` must equal `ff760120c81792022458a5a290b6e58d57b9cd17`. If it has moved, stop and record the new tip in this file; do not build on an unrecorded head.
2. Trace the pin's failing line and record the trace. Before composing any run command, grep the branch for the test file and copy its exact package and module; compose the run in a separate step from the grep.
3. Implement the fix in a worktree at the confirmed head (copy `gradle.properties` into the worktree). Remove the `@AwaitsFix` annotation from the pin as part of the fix: at the resulting head the pin is an ordinary test. Commits under Nick Shanin identity, no trailer, no em dashes in messages.
4. Changelog YAML parse (gate step 0): parse the fragment this job adds with a real YAML parser (the repository validator `dev-tools/scripts/validate-changelog-yaml.py`, or the gate step 0 parser). An unquoted title containing `": "` is invalid YAML.
5. Tidy, with the GATE INVALID guard: run tidy; restore `gradle/libs.versions.toml` afterwards; then check `git status`. If tidy modified anything beyond that restore, commit the tidy result and re-run the gate on the committed tree; a gate never passes on an uncommitted tree.
6. Error Prone compile: `:solr:core:compileJava` and `:solr:core:compileTestJava` with `-Pvalidation.errorprone=true`, through the host's Gradle route for gates (on vm1, `~/workspace/tools/solr-gradle.sh`; never `./gradlew` there). rc must be 0.
7. Fail-before proof: on the pre-fix production (the base the receipt records, `0cc328310f8`, or the branch's merge-base if the trace shows they differ for the facet code in scope), with the resulting branch test file in place and the annotation removed, the pin test fails by content: the nested buckets empty, the same failure shape as the recorded premise. Counts from fresh JUnit XML. Failures in this leg are the pass condition, judged by content, not by count alone. The control test passes in this leg.
8. Focused tests at the resulting head, counted from fresh JUnit XML only, seed `16290C0FFEE16290`, module `:solr:core`: `TestJsonFacetsWithNestedObjects` in full. Acceptance: all tests pass, 0 failures, 0 errors, and the previously skipped pin now runs and passes (the receipt's head counts were 9 tests with 1 skipped, the pin; at the fixed head the skip count drops accordingly).
9. Module check: `:solr:core:check -x test`, rc 0.
10. Never two Gradle builds at once on a host: steps run serially, behind the test-queue flock (on vm1, `~/workspace/tools/.solr-test-queue.lock`) like every other gate run.

## Acceptance (all required)

- The pin passes with the `@AwaitsFix` annotation removed.
- The control test in the same class still passes.
- `TestJsonFacetsWithNestedObjects`, the facet suite the receipt names, is green at the resulting head.
- The fail-before leg shows the pin failing on the pre-fix production by content.

## On green

1. Push plainly (fast-forward, no force) to `fork` `solr-16290-submit`. Pushes to the owner's own branches carry standing authorization. Verify the new tip by `git ls-remote` after the push.
2. Refresh `receipts/SOLR-16290.md`: the fix's root cause from the step 2 trace, the new head, the gate steps and counts from the fresh JUnit XML, the fail-before result, and the changelog fragment added. The disposition line from round 1 stays; the status line updates to gate green as a fix.
3. Mark this job DONE with the new head and the gate outcome, and close the claim.
4. The branch stays unopened. Opening is a separate main-side step with the owner's explicit approval, and the draft for the fix poses the exclusions and block-join composition question as its Choice.

## On failure

- Record the failing step, the failing test and seed if a test ran, and the log path in this file. A gate that executed tests and failed is not relaunched until the main side has investigated (WORKFLOW.md). A run that never executed tests because of an environment refusal may be relaunched once, with the refusal recorded.
