# Pool assignment: focused Linux proof for teardown release paths, round 2

Status: CLAIMED (vm4, 2026-10-23T19:20:00Z; claim `claims/pool-teardown-release-path-round-2-linux-proof.md`)

- Capability tags: implementation, premise-run.
- Category: Core admin and cores (10), with SolrJ and clients (4) and Test quality (13).
- Intended host: any pool Linux host with the named capabilities. Claim under `claims/` before starting.
- Posted by: windows, 2026-10-23, continuing Nick's request to investigate the round-2 assignment.
- Parent: `assignments/pool-teardown-release-path-round-2.md`.
- Start from: `reports/teardown-release-path-round-2.md` and the original `reports/teardown-objecttracker-leak.md`.
- Candidate branch: `solr-18564-release-path-r2`.
- Candidate head: `ee1eb2474f6650f9c8f8a51c6729383496a4256e`.
- Before branch: `solr-18564-release-path-r2-before` at `f92d77151295b5b237230843e3897e4c441da26d`; its production tree must equal `79b23df58daac14b92cc35f23b8b81f72098b4ba`.
- Links: Ticket or parent: SOLR-18564 / teardown release-path investigation. Siblings: PR 5192, `fix-interrupted-http-client-close` at f0adb05094, SOLR-18506. Supersedes: none.

## Items

1. **Build and exercise the prepared candidate.** Start from the exact candidate head. Use the host's normal Linux build lock and resource guards. Run `./gradlew tidy` once, then the four method-filtered commands in `reports/teardown-release-path-round-2.md`, in this order: factory timeout, interrupted container wait, real container timeout, interrupted client close. No full suites. Preserve fresh XML and stdout/stderr after each invocation. A test failure ends this item and this assignment; record it and stop, without relaunch. The real container timeout method takes about 120 seconds by design. This is scratch investigation, not a full submission gate.

2. **Record fail-before without a retry.** Only after item 1's four commands pass, use a separate disposable Linux worktree at the before branch's pinned head. It contains the same three test files and none of the three production changes. Run one final invocation below. It intentionally asks Gradle to finish both explicitly filtered tasks even if one task fails; it is a single baseline experiment, not a relaunch. Expected: factory timeout assertion fails, container-tail assertions fail, and interrupted client close leaves a tracked client at the class check. The interrupted-container diagnostic is excluded from this last run, because it was observed in item 1 and its baseline variant may instead be run as a successful diagnostic before this final invocation if needed. Once the baseline invocation ends, preserve evidence and stop, including on an unexpected failure. Do not run another invocation after that failure.

```sh
./gradlew :solr:core:test --tests org.apache.solr.handler.component.HttpShardHandlerFactoryReleaseTest.testExecutorTimeoutStillClosesMetrics --tests org.apache.solr.core.CoreContainerReleasePathTest.testTimeoutStillClosesContainerTail :solr:solrj-jetty:test --tests org.apache.solr.client.solrj.jetty.HttpJettySolrClientTest.testCloseReleasesClientWhenInterrupted --continue --no-daemon --max-workers=2 -Ptests.jvms=1 -Ptests.seed=FFF5A3C3AAC69084 '-Ptests.timeoutSuite=600000!' -Ptests.useSecurityManager=true -Ptests.file.encoding=ISO-8859-1
```

3. **Reconcile the mechanism.** Output the actual tracker text before and after each candidate, the exact source tree and command for every run, and the observed exception/interrupt status. Determine whether interrupted container wait actually returns with root/plugin/handler closed and factory still outstanding. Do not promote that experiment to proof of the historical CI cause: the exact original Jetty stall remains unobserved. Update the parent report and status with the bounded results.

## Decisions

| Decision | Answer or default | Condition and action | Owner |
| --- | --- | --- | --- |
| Scope and authority | Decided: focused scratch experiments only | No PR-branch movement, public communication, JIRA filing, full suites or CI reruns | Nick's parent assignment |
| Baseline | Decided: explicit Apache 79b23df58d, not fork origin/main | Confirm before production tree is identical and the three test files match candidate before running | Windows preparation |
| Formatter changes | Default: mechanical formatting in the six scoped files may be committed/pushed fast-forward on the candidate branch | Record original/repaired heads before running tests; if formatting touches other files or the remote head moved, record and stop. Apply corresponding test-only formatting to the before proof branch with a recorded commit if needed; preserve its production-tree identity. No semantic repair or changing expectations is authorized here | WORKFLOW.md repair principles |
| Compile or test failure | Decided: record the logs, actual head, and failure; stop without relaunch | This assignment does not authorize compile repairs or test retries. Hand back implementation with the precise error | Parent assignment step 5 |
| Expected baseline failures | Decided: one planned final invocation after successful candidate proof | Its nonzero exit is expected only when the named leak/cleanup assertions explain it; preserve it and stop. No retry | Windows proof design |
| Nested wait interpretation | Decided: distinguish pending close from skipped close | A factory finally cannot execute until its preceding close steps finish. Do not call an intermediate tracker snapshot proof of a permanently skipped close | Source analysis |
| Submission state | Decided: no full gate receipt or ready-for-PR verdict | Publish the focused evidence and state remaining resource-termination limits | Nick's parent assignment |

## Deliverable

Append a dated Linux section to `reports/teardown-release-path-round-2.md` with commands, heads, XML/log paths and actual tracker output. Preserve logs or a public evidence archive on coordination, including hashes. Update this assignment and its claim in the same push. Mark the parent DONE only if its before/after reproduction requirements are met; otherwise mark HANDED BACK with the exact missing evidence. Windows claim is terminal and must not be edited.

## Limits

- No Gradle on Windows. No full core, SolrJ or Jetty suite, no repeated seeded sweeps, no CI rerun.
- No weakening the release assertions or the class-level tracker check. Do not include the AutoCloseable tracker candidate in either tree.
- No semantic repairs, silent expectation edits, or repeated failing runs.
- No new PR, comment, JIRA entry or email. No force pushes. No writes to apache/solr.
- Commit as Nick Shanin with no attribution trailers. No em dashes or en dashes in authored records.
