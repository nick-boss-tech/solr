# Receipt: PR 5238 listener UBI contract, Linux round 2 (vm4)

- Gate: `gates/pr-5238-ubi-contract-round-2-vm4.md`, superseding `gates/pr-5238-ubi-contract-round-1.md`. Claim: `claims/pr-5238-ubi-contract-linux-round-2-vm4.md`. Report: `reports/pr-5238-ubi-contract-linux-round-2.md`.
- Ticket: SOLR-11994. Apache PR 5238 head `aa03c9544968f1d9de9b6b36e2823bcbe76268b9` is the parent.
- Exact tested and pushed head: `c2c387064a83def34cece4163eccded28318caeb` on `nick-boss-tech/solr:solr-11994-pr5238-ubi-submit`. Remote head verified after all runs. The head is the author's `de4d222bb1` plus one formatting commit by vm4.
- Verdict: GREEN on Linux at `c2c387064a`. Positive proof only. Not a fail-before proof.

## Environment

- Host vm4, Linux aarch64, Temurin JDK 21.0.12.1, Gradle 9.7.0 through the repo wrapper, `--no-daemon`, `--no-build-cache`.
- Standalone full clone at `/workspace/worktrees/gate-5238-clone`, detached at `c2c387064a`. Root check run with `env -u NODE_ENV`.

## Results (fresh XML)

| Run | Tests | Skipped | Failures | Errors |
|---|---|---|---|---|
| `SearchRequestListenerUbiTest`, seed `5238AB1` | 9 | 0 | 0 | 0 |
| `SearchRequestListenerUbiTest`, seed `5238AB2` | 9 | 0 | 0 | 0 |
| `SearchRequestListenerTest`, seed `5238AB1` | 19 | 0 | 0 | 0 |
| `PingRequestHandlerTest` | 6 | 0 | 0 | 0 |
| `TestSolrConfigHandler` | 8 | 0 | 0 | 0 |
| `TestSolrRequestInfo` | 3 | 0 | 0 | 0 |
| `TestSubQueryTransformerDistrib` | 1 | 0 | 0 | 0 |

- Tidy: BUILD SUCCESSFUL, tree clean after tidy at `c2c387064a`.
- Root `check -x test` with NODE_ENV unset: BUILD SUCCESSFUL.
- `git diff --check` against the parent: clean. Scope: only `SearchRequestListenerUbiTest.java` changed against the parent.
- Logs: `/workspace/gates/logs/vm4/pr5238-ubi-contract-linux-round-2/`.

## Repair

- `c2c387064a` "PR 5238: tidy UBI contract test formatting". Rewraps two `cluster.getSolrClient().request(...)` calls in `SearchRequestListenerUbiTest.java`. Formatting only. Pushed fast-forward to the fork after the main agent authorized it.

## Not covered

- Round 1 head `de4d222bb1` is not proven, and round 1 has no test result.
- PR 2452 integration, persistent sinks, core reload and unload, asynchronous lifetime, grouped or ID-omitting result handling, redaction, and production overhead.
- Windows.
- The host crashed three times during this round. Interrupted attempts ran no tests. See the report.
