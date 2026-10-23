# Pool assignment: release-path round 3 isolated factory-interrupt test

Status: CLAIMED (vm4, 2026-10-23T20:30:00Z; claim `claims/pool-teardown-release-path-round-3-test-proof.md`)

- Capability tags: premise-run.
- Category: Core admin and cores (10), with Test quality (13).
- Intended host: any pool Linux host. Claim under `claims/` before running.
- Posted by: windows, 2026-10-23, following Nick's request to investigate the hand-back and review round 3.
- Parent: `assignments/pool-teardown-release-path-review-round-3.md`.
- Start from: `reports/teardown-release-path-review-round-3.md`.
- Branch: `solr-18564-release-path-r3-test`.
- Head: `68fd05e53002f8c0a1fbff65a5623b5fea96028d`.
- Links: Ticket or parent: SOLR-18564 / release-path investigation. Siblings: round-2 failed proof, PR 5192, PR 5213, f0adb05094. Supersedes: none; the failed round-2 run stays stopped.

## Items

1. **Preserve provenance.** Confirm the scratch head and that its only change from ee1eb2474f is `CoreContainerReleasePathTest.java`. Compare `ObjectReleaseTracker.java` and `SolrTestCase.java` against Apache 79b23df58d: both must be unchanged. Obtain vm4's original `candidate-interrupted-wait.log`, its matching XML, and tidy/factory pass logs if accessible, and archive them with hashes on coordination. If those old logs are unavailable, explicitly leave exact attribution of the original four entries unverified; do not substitute the new run for the old one.

2. **One focused invocation on the changed test.** Under the Linux host's normal Gradle lock and resource guards, run the command below once. The new test waits for three sibling clients and two sibling contexts to release before it interrupts the deliberately blocked factory. Preserve all five owner messages, both tracker snapshots, XML, stdout/stderr, actual SHA and exit code. No retry after a failing run. A sibling-precondition timeout or body/final class tracker failure is a real failure; record it and stop. Do not run the other three proof methods or any baseline/full-suite/CI job under this assignment.

```sh
./gradlew :solr:core:test --tests org.apache.solr.core.CoreContainerReleasePathTest.testInterruptedContainerWaitReturnsBeforeFactoryClose --no-daemon --max-workers=2 -Ptests.jvms=1 -Ptests.seed=FFF5A3C3AAC69084 '-Ptests.timeoutSuite=600000!' -Ptests.useSecurityManager=true -Ptests.file.encoding=ISO-8859-1
```

3. **Record the result and release resources.** Publish a result section and raw evidence archive. If successful, it proves the isolated early-return experiment completes cleanly at that head; it does not prove the original CI stall, safety of simultaneous sibling interruption, or the unrun client/container-tail/baseline legs. Remove this job's own scratch checkout and attributable build processes after preserving evidence, following host rules.

## Decisions

| Decision | Answer or default | Condition and action | Owner |
| --- | --- | --- | --- |
| Scope | Decided: one repaired test method, once | No other test invocation, CI rerun or full suite | Round-3 review step 6 |
| Tracker fallback | Decided: excluded | If either tracker/framework source differs from Apache 79b23df58d, record and stop before running | Proof isolation |
| Failure | Decided: preserve and stop | No semantic repair, expected-value change or failed-test relaunch | Parent review |
| Formatting | Default: tidy once before the focused invocation is allowed on Linux | If it changes only this test, commit/push mechanically, record old/new heads and test the repaired head; if it touches other files or the branch moved, stop | Existing repair workflow |
| Old Linux evidence | Decided: preserve if accessible, otherwise state the gap | No fabricated identities or completion times from the hand-back summary | Review evidence limits |
| Interpretation | Decided: clean isolated test is bounded evidence | Do not mark other paths green or attribute CI's 60-second stall from this result | Source review |

## Deliverable

`reports/teardown-release-path-round-3-test-proof.md`, with actual head, command, timestamps, exit, fresh XML, stdout/stderr and tracker output. Preserve a public evidence archive or exact accessible raw paths and hashes. Update this assignment and its claim together. Parent review claim is terminal and must not be edited.

## Limits

- Linux only; no Windows builds. No PR branch movement or force push.
- No retries, full suites, CI reruns, public PR/JIRA actions or tracker fallback changes.
- No manual tracker clearing, manual untracking, or force-closing siblings to make the test pass.
- Commit as Nick Shanin with no trailers. No em dashes or en dashes.
