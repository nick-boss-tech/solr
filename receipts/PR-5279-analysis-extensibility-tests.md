# Gate receipt: PR 5279 analysis handler extensibility tests, round 1 (Linux gate)

Status: GREEN at the exact final head.

## Identity

- PR: https://github.com/apache/solr/pull/5279 (no Jira ticket).
- Fork branch: nick-boss-tech/solr:pr-5279-analysis-extensibility-tests.
- Gate job: gates/pr-5279-analysis-extensibility-tests-round-1.md (Status OPEN at claim time).
- Superseding gate (after the tidy repair): posted by the claiming host, pinned to the final head below.
- Claimed head H (original gate pin): 49ce5c0c10acb02cdfd828f8ad0f9588aa9d832e.
- Final head H2 (tested): 78a9bef183e710833517a32c1691dfd2c2d9a7df, verified by ls-remote as the live fork tip and by the worktree HEAD after the gate; parent is contributor head fd033eb55a4eb7c4a5d8c626ad099dd8137098fb.
- Gate claim: claims/pr-5279-analysis-extensibility-tests-gate-phone-agent.md (phone agent).
- Repair report: posted by the claiming host before positive proof (tidy reflow, formatting only).
- Worktree: ~/workspace/solr-worktrees/5279-gate, detached at H2, clean after the gate; git diff --check clean.
- Host: Linux, JDK 21.0.12.1+1, Gradle 9.7.0 via ~/workspace/tools/solr-gradle.sh, serialized under ~/workspace/tools/.solr-test-queue.lock.
- Seed: 5279C0DE throughout.

## Tidy repair (before positive proof)

The first tidy run at H reformatted one file, so the run stopped GATE INVALID per the gate's repair rule. The diff was verified formatting only (javadoc line-wrap reflow, 2 lines, in AnalysisRequestHandlerBase.java, one of the five outbound files). It was committed as Nick Shanin <nick.boss.us@gmail.com>, no trailers, no PR number, and plain-pushed fast-forward to H2; the remote head was verified by ls-remote. A repair report and a superseding gate pinned to H2 were posted before positive proof. No other branch file changed.

## Positive proof (all at H2, verified from logs and fresh JUnit XML)

1. Root tidy: rc=0, 0 tracked files changed by tidy.
2. Focused tests, `:solr:core:test --tests org.apache.solr.handler.FieldAnalysisRequestHandlerTest --tests org.apache.solr.handler.DocumentAnalysisRequestHandlerTest -Ptests.seed=5279C0DE`: BUILD SUCCESSFUL. Fresh JUnit XML timestamps 2026-11-02T01:58:32Z (DocumentAnalysis) and 2026-11-02T01:58:44Z (FieldAnalysis). FieldAnalysisRequestHandlerTest: 11 tests, 0 failures, 0 errors, 0 skipped. DocumentAnalysisRequestHandlerTest: 6 tests, 0 failures, 0 errors, 0 skipped. All four new methods identified in the fresh XML: testCustomAnalyzers and testCustomAnalyzersShowMatch in each class.
3. Root check: the worktree run failed on one task only, `:solr:solr-ref-guide:buildLocalAntoraSite`, with Antora refusing a git worktree (".git is a file, not a directory"); no yarn or kotlinWasm task appears in that log, and npm fetching worked, so this was not the gate's anticipated yarn split and not a branch defect. The root check was therefore rerun in a normal clone at the same head H2 (~/workspace/solr-clones/5279-rootcheck): `./gradlew check -x test` BUILD SUCCESSFUL in 3m 29s, 555 actionable tasks (174 executed, 2 from cache, 379 up-to-date), no FAILED tasks. The clone's first attempt died mid-Antora with no live process (environment death, no test task involved, root check runs -x test); it was relaunched once and completed. This leg is GREEN via the clone run, with the worktree Antora failure disclosed here, not dropped.
4. Final tree clean, diff-check clean, remote branch read matches H2.

## Discriminating mutation proof (fresh F worktrees at H2, never committed)

Each leg changed only the stated production call sites in the two handler files, ran the four-method filter, and kept fresh XML:

- Index leg: 2 failures per class, exactly as the gate specifies.
- Query leg: 2 failures per class, exactly as the gate specifies.
- Match leg: 1 failure per class, exactly as the gate specifies.

Mutation diffs and logs retained under ~/workspace/tools/5279-gate-mut-*.

## Verdict

GREEN. The four new test methods pass at H2, fail under each discriminating mutation as specified, and the full root check passes in a normal clone. The one worktree-only Antora failure is an environment shape (git worktree), not a branch defect, and is disclosed above.

## Limits

- This gate covers the Linux gate round 1 scope only; it is not a review of the contributor's patch design.
- The root-check leg rests on the normal-clone rerun because Antora rejects git worktrees; a host running the root check from a normal clone will see the same GREEN result directly.
- No fail-before claim is made: per the gate, the contributor head P is expected to pass these tests (tests/Javadoc follow-up over an existing extension API), so no pre-PR-base failure is cited as evidence.

## Public actions

None. No PR, no comment, no Jira. Posting is Nick's later decision.
