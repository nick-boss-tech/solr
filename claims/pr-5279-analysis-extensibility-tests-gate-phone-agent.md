# Claim: PR 5279 analysis handler extensibility tests, Linux gate round 1 (phone agent)

- Gate job: `gates/pr-5279-analysis-extensibility-tests-round-1.md` (Status OPEN).
- Claimant host: phone agent (Linux, JDK 21.0.12.1+1, Gradle 9.7.0 via ~/workspace/tools/solr-gradle.sh, serialized under ~/workspace/tools/.solr-test-queue.lock).
- Started: 2026-11-01T18:40Z (approx., at claim post time).
- Fork branch: `nick-boss-tech/solr:pr-5279-analysis-extensibility-tests`.
- Exact head H: `49ce5c0c10acb02cdfd828f8ad0f9588aa9d832e`, verified by ls-remote as the live fork tip before claiming; parent is contributor head P `fd033eb55a4eb7c4a5d8c626ad099dd8137098fb` (H is P plus one test commit, 5 files).
- Checked before claiming: no Linux gate claim on coordination-ui (only `claims/pr-5279-analysis-extensibility-tests-windows-round-1.md`, implementation, no builds); no receipt `receipts/PR-5279-analysis-extensibility-tests.md`; gate Status OPEN; the nearby `gates/pr-5254-normalization-tests-round-1.md` was passed over because its pinned head is no longer the branch tip (tip adds the snapshot-join BATS commit, a separate Docker gate), and `gates/pr-5238-ubi-context-round-1.md` was passed over because its standdown file forbids claiming it.
- Worktree: `~/workspace/solr-worktrees/5279-gate`, detached at H, clean.
- Runner: `~/workspace/tools/5279-gate-runner.sh`, run in the foreground via the runtime's backgrounded exec (detached nohup launches died twice on the SOLR-16725 gate). Output: `~/workspace/tools/5279-gate-runner.out`. Logs and fresh JUnit XML copies under `~/workspace/tools/5279-gate-*`.
- Scope: tidy (clean required), focused `:solr:core` tests FieldAnalysisRequestHandlerTest + DocumentAnalysisRequestHandlerTest at seed 5279C0DE, root `check -x test` (with the current-CA truststore, as the PR 2452 round-3 gate did; the `:kotlinWasmNpmInstall` yarn leg is a known VM egress split if it fails, to be disclosed, never silently dropped), three mutation legs at fresh F worktrees (never committed).
- Public actions: none. No PR, no comment, no Jira.
- Status: CLAIMED.
