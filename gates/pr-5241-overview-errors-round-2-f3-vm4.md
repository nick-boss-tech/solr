# Superseding gate: PR 5241 core overview errors, round 2, formatting-repaired head (vm4)

Status: OPEN. Immutable once posted. Supersedes `gates/pr-5241-overview-errors-round-2.md` for final proof only at the head below. Round 2's requirements are unchanged.

- Ticket / PR: SOLR-16398 / https://github.com/apache/solr/pull/5241.
- Fork / branch: `nick-boss-tech/solr:solr-16398-submit`.
- Exact pinned head: `3b36f32e93d513c58ff14c48db303a296218f84a` (remotely verified by ls-remote after a plain fast-forward push).
- Direct parent: `56365b43ee72977d1934b5a5c833e1071f75c557` (round-2 repaired head, which the gate file names).
- Reason for the superseding job: `./gradlew tidy` at the round-2 head reformatted one line of `AdminUiCoreOverviewErrorsTest.java` (a 100-column wrap of the `getBytes(StandardCharsets.UTF_8)` argument). Per the round-2 gate, a tidy change requires a scoped formatting repair commit and a superseding job before final proof. The repair commit is formatting only, Nick Shanin identity, no trailers, 2 insertions and 1 deletion in that one file. Runs at `56365b43ee` are not proof of `3b36f32e93`.
- Original PR head and fail-before base: `aa8272ef089c39df449c519551ef439557b876f8` (unchanged).
- Claim: `claims/pr-5241-overview-errors-gate-vm4-round-2.md` (round-2 claim, host vm4).

## Legs

Everything in `gates/pr-5241-overview-errors-round-2.md` applies at `3b36f32e93`, with these concrete differences:

1. Positive worktree `/workspace/worktrees/gate-pr5241-r2` at `3b36f32e93`. The delta from `80ca9374f4` is the repaired test file plus the formatting commit, and from `56365b43ee` it is the single wrapped line. Run `git diff --check` and `./gradlew tidy`, and require a clean tree afterwards. Then run `./gradlew :solr:webapp:forbiddenApisTest`.
2. Focused browser class and neighbor classes with the explicit driver setting from round 2: host `JAVA_TOOL_OPTIONS=-Dwebdriver.chrome.driver=/workspace/tools/chromedriver-156.0.8078.4/chromedriver-linux-arm64/chromedriver`, `--no-daemon`, `-Dtests.selenium.chrome.binary=<Chrome 156 headless shell>`, `-Ptests.selenium=true`, `-Ptests.seed=5241C0DE`. Confirm the property reaches the forked test JVM from the test XML or log.
3. `env -u NODE_ENV ./gradlew check -x test` at `3b36f32e93`, plus `node --check` on the controller and a final `git diff --check` and clean status.
4. Fail-before worktree at `aa8272ef089c39df449c519551ef439557b876f8` with only the test file from `3b36f32e93` overlaid. Record its hash. Same focused command and seed.

The round-2 browser setup rule still applies: if the focused browser leg refuses setup, record it and stop before the neighbor and fail-before legs. Do not relaunch blind.

## Evidence

Logs under `/workspace/gates/logs/vm4/pr-5241-overview-round-2/` (the tidy leg of the aborted attempt is kept as `interrupted-attempt-1-tidy` and is not proof). Use fresh method-level XML for counts. The completion report and receipt update follow the round-2 gate's evidence section.

Until the receipt is GREEN at `3b36f32e93` with a matching remote tip, the contribution stays **Implemented, Proof Pending**. No PR, comment, JIRA, or contributor-branch action is part of this gate.
