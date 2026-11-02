# SOLR-16793 phase 2 gate, round 1: FAILED on a pre-existing base condition (not a branch defect)

- Gate: `assignments/solr-16793-overseer-queue-metrics-round-1.md`, phase 2 (separate claim).
- Claim: `claims/solr-16793-gate-phone-agent.md` (Status DONE, FAIL; see below).
- Branch: `nick-boss-tech/solr:solr-16793-submit` at `747afc8153f703b778c5ed4848b36bc611d0e5d5`, verified by ls-remote before and after the run; nothing pushed, branch content unchanged.
- Base (fix commit parent): `498a09aea97aac86c2cc7ac114113b946d249942`.
- Diff scope, base to head (unchanged by this run): `OverseerCollectionConfigSetProcessor.java` (one line), `OverseerQueueLengthTest.java` (new, extends SolrTestCase), `changelog/unreleased/SOLR-16793-overseer-queue-length.yml`.
- Host: phone agent, Linux, JDK 21 via `~/workspace/tools/solr-gradle.sh`, one build at a time through the test-queue flock. Seed for the proof legs (not reached): `16793C0FFEE16793`.
- Gate worktree: `~/workspace/solr-worktrees/16793-gate` at the exact head, clean before and after.

## Legs run

| Leg | Command | Result |
|---|---|---|
| Changelog YAML parse | `check-changelog-yaml.sh changelog/unreleased/SOLR-16793-overseer-queue-length.yml` | rc=0 |
| Root tidy | `./gradlew tidy` | rc=0, 0 tracked files changed (log `~/workspace/tools/16793-gate-tidy.log`) |
| Error Prone compile | `./gradlew :solr:core:compileJava :solr:core:compileTestJava -Pvalidation.errorprone=true` | BUILD FAILED in 3m 43s (log `~/workspace/tools/16793-gate-compile.log`) |

## The compile failure

`compileTestJava` fails on two Error Prone `DirectInvocationOnMock` warnings in
`solr/core/src/test/org/apache/solr/handler/admin/api/CoreReplicationAPITest.java:213,217`
(direct calls on `mockCore`), promoted to errors by `-Werror`. That file is
untouched by the branch: the base-to-head diff for that path is empty, and its
last change is `e044bf20b4` (SOLR-18226) in the base's ancestry.

## Pre-existing status, proven by a base run (not asserted)

A scratch worktree at the base `498a09aea97` ran the identical compile command
(log `~/workspace/tools/16793-basecheck-compile.log`): BUILD FAILED in 2m 32s,
same file, same lines, same check, same `-Werror` promotion. The failure is a
property of the base tree, not of the branch. The scratch worktree was removed
afterwards.

## Legs not run

The pre-fix proof, the head focused test, and `:solr:core:check -x test` did
not run: the gate stops at a failed compile leg, and a failure is investigated
as a repair, not silently repeated. No proof pair exists for this head, and no
green receipt is written. The phase 1 focused-test pass (vm5, its own seeds)
is not reused as phase 2 evidence.

## Verdict

GATE FAILED, cause: pre-existing Error Prone failure in an untouched base test
file. This is not a verdict on the branch: the one-line production change and
the new test were never reached by a test leg here, and nothing in this run
shows them broken.

## Retry condition

Do not retry this gate unchanged: it will fail identically at the same
compile leg. Retry only after `CoreReplicationAPITest` is fixed on main (or on
a rebased branch) so that the Error Prone compile passes, or after Nick
re-scopes the compile leg. The assignment stays OPEN for phase 2; this report
is the handback, not a receipt.

No PR, no GitHub comment, no Jira write. No branch change, nothing re-pushed.
