# Claim: BATS assertion-hardening round 1, non-Docker best-effort gate (phone-agent-2)
- Claimant host: `phone-agent-2`, matching `hosts/phone-agent-2.md`.
- Status: LIVE; scoped to non-Docker proof only. The overall gate remains OPEN/incomplete.
- Gate: `gates/bats-assertion-hardening-round-1.md`.
- Owner authorization: Nick's 2026-10-26 instruction authorizes best-effort execution of the non-Docker subset and explicit isolation of `test_rolling_upgrade.bats`; no GREEN claim is authorized while that case remains unrun.
- Candidate: `nick-boss-tech/solr:solr-bats-assertion-hardening-submit` at exact head `0d7c6f4e6cb6d2211b59ed9eed36db90182b122e`; base `0d6b2e32f598d772aed094038022a6a588fdfc5a`.
- Board snapshot read before claim: `f0f8a087062287494c737d9eab3b100b0f9aea62`; the changes since `82eabc8bf96b31aeadc981af8d396bb471843b21` were unrelated claims, and the gate, host rules, and relevant records were unchanged.
- Capability tags used: gate, bats, records.
- Started: 2026-10-26T19:08:09Z (UTC).
- Decision ladder: rung 3; Nick's explicit instruction settles the scope. The separate PR 5225 gate and candidate remain untouched.

## Preflight
Detached standard `pool/bin/gate-preflight.sh` ran at the exact candidate head before this claim, under the phone-agent-2 lock and inode guard. Exit 0; `tidy clean at 0d7c6f4e6cb6d2211b59ed9eed36db90182b122e`; Gradle reported `BUILD SUCCESSFUL in 1m 11s` (62 tasks). The preflight emitted a diagnostic that `/workspace/.env` is absent; the supplied host credential still authenticated the fetch, and preflight completed successfully. Logs: `/workspace/gates/logs/phone-agent-2/bats-assertion-hardening-round-1/preflight/` (`preflight.meta`, `preflight.log`, `preflight-tidy.log`).

## Scope and limits
Run the four normal matrix legs only for suites not requiring Docker: JDK 21 legacy, JDK 25 legacy, JDK 21 Picocli, and JDK 25 Picocli. The legacy subset is the 12 handoff-listed files excluding `test_rolling_upgrade.bats`; the Picocli subset is the specified four files `test_bats_assertions.bats`, `test_delete_collection.bats`, `test_healthcheck.bats`, and `test_postlogs.bats`. Verify actual selected files and preserve fresh BATS/JUnit evidence and complete logs per leg. Run any feasible non-Docker discriminating controls only in isolated scratch trees/wrappers, and list every control's actual result or non-execution in the report.

`test_rolling_upgrade.bats` declares one case and requires Docker plus both rolling-upgrade images (`SOLR_BEGIN_IMAGE` and `SOLR_END_IMAGE`). This host has no Docker executable/daemon; the case will not be run or counted as a passing skip. It is explicitly BLOCKED/UNRUN, so no GREEN receipt will be written and the overall gate stays INCOMPLETE.

Use the source branch's Gradle wrapper, JDK 21 for Gradle, task-scoped `JAVA_HOME` and `SOLR_JAVA_HOME` for each child runtime, explicit `SOLR_PICOCLI`, and the gate's init-script Picocli list replacement. Serialize each build through `pool/bin/sweep-run.sh`/`/workspace/tools/.gradle-phone-agent-2.lock` and the inode guard. Preserve each leg's command, UTC start/end, headroom, exact HEAD/source cleanliness, fresh XML, full BATS transcript, Solr logs/console logs, child env/parser and JVM fingerprints, tool versions, exit code, counts, and selected test files under `/workspace/gates/logs/phone-agent-2/bats-assertion-hardening-round-1/`. Stop at or below 15% free inodes; do not clean caches or artifacts without approval. Any candidate repair must follow the handoff's failure investigation and new-head/preflight/claim rules.
