# Assignment: SOLR-13059 diagnosis, round 1

Status: CLAIMED (claims/pool-solr-13059-diagnosis-round-1.md)

Capability tags: [gate, premise-run]. Intended host: either Linux host (vm1 or vm2); Windows never claims build work. Posted by the main side, 2026-10-14, at Nick's direction: the diagnosis he was offered as a go or no-go is posted to the pool as an assignment instead.

Claim first: before starting, file a claim under `claims/` naming this assignment (for example `claims/pool-solr-13059-diagnosis-round-1.md`), per WORKFLOW.md. Heartbeat and staleness follow the pool rules.

This is a diagnosis only. No code changes, no fix branch, no Jira activity, no PR. Any fix is a later assignment.

## Grounding

Read the shaped brief before claiming: `solr-13059-diagnosis-brief-2026-10-09.md` in the goal records (hidden_files under the apache-solr-contribution-submission goal on the main host). The facts below come from that brief and are stated as of 2026-10-09; re-verify each one at claim time before relying on it.

- SOLR-13059: open Bug, created 2018-12-11, "TimeRoutedAliasUpdateProcessorTest rarely fails to see collection just created." The recorded failure is testSliceRouting getting "no core retrieved" for a collection the test just created.
- The Time subclass of the routed-alias test family carries its own `@AwaitsFix(SOLR-13059)`, separate from the base-class `@AwaitsFix(SOLR-13696)`. SOLR-13943 leaves the 13059 annotation untouched and ships as decided; nothing about this assignment blocks it.
- SOLR-13943 stacked gate recorded GREEN at cc155cf68e1 on 2026-10-09; its awaitsfix run of the Time class was 5 of 6 with only the pre-existing testPreemptiveCreation timing race failing. In an earlier round 38 spot-check on the unstacked branch that method failed 3 of 5 runs.
- SOLR-13696 gate r7 recorded GREEN at 1d0b8a0a73cd on 2026-10-09. A changelog lane and a gate r8 were moving that branch afterward, so its head at claim time is not the r7 head; PR #5088 is its live PR.

## Scope

1. Base: the current SOLR-13696 submit head at claim time. Verify it by ls-remote and against PR #5088 before building anything, and record the head in the report. No merge to main is needed or waited on.
2. Run TimeRoutedAliasUpdateProcessorTest with the awaitsfix group enabled, in repeated runs at several seeds: enough repetitions to state a failure rate for testPreemptiveCreation and to settle whether the original testSliceRouting "no core retrieved" symptom still reproduces at all.
   - Before composing any run command, grep the branch for the test file and copy its exact package and module from what the grep returns. Compose the run in a separate step from the grep, never from the expected package.
   - Sequence behind live gates on the single test-queue flock. Take the blocking flock like every other gate run; never run two Gradle builds at once.
   - Build with `~/workspace/tools/solr-gradle.sh` on vm1, or the claiming host's own wrapper per its host file. Copy `gradle.properties` into every new worktree before building.
   - Count results from fresh JUnit XML only. A test claim stands on the run log and the fresh XML, nothing else.
3. Classify each failure as one of: test polling or timing race in the test itself, production state-propagation race (collection creation visibility), or environment. State the evidence for each classification from the logs and XML.

## Stop condition

The deliverable is `reports/solr-13059-diagnosis-round-1.md`: the verified 13696 head used as base, the run matrix (seeds, repetitions, per-method outcomes counted from fresh XML), the testPreemptiveCreation failure rate, the testSliceRouting verdict (still reproduces, or does not reproduce in these runs), the classification of every failure seen, and a fix-shape estimate (small test fix, production work, or still unreproducible). Stop there. If the runs produce a fix idea, record it as the estimate only; do not implement it under this assignment.

## On completion

1. `reports/solr-13059-diagnosis-round-1.md` is committed on this branch with the contents above.
2. Mark the claim for this assignment DONE, naming the report.
3. Append one line to the takeover log (`files/remaining-branches-takeover-log.md` in the goal records) recording the classification outcome in one sentence.
