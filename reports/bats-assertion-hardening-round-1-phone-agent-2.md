# BATS assertion-hardening round 1 — phone-agent-2 report

**Status: INCOMPLETE.** This is the requested best-effort non-Docker subleg only. The overall gate remains OPEN; no GREEN receipt was written. The original claim remains LIVE, and the Docker-dependent case is explicitly unrun/blocked.

Candidate: `nick-boss-tech/solr:solr-bats-assertion-hardening-submit`, final head `0d7c6f4e6cb6d2211b59ed9eed36db90182b122e`, base `0d6b2e32f598d772aed094038022a6a588fdfc5a`. The fork ref and detached worktree remained at the exact head and clean; no source changes or candidate push were made.

## Normal matrix

All four authorized non-Docker legs executed through the serialized phone-agent-2 runner. The legacy subset was the 12 handoff-listed files excluding `test_rolling_upgrade.bats`; the Picocli selection was verified in the BATS transcripts as the four intended files: `test_bats_assertions.bats`, `test_delete_collection.bats`, `test_healthcheck.bats`, and `test_postlogs.bats`.

| Leg | UTC start–end | Result | Exit |
|---|---|---:|---:|
| JDK 21 / legacy | 2026-10-26 19:09:47–19:23:03 | 62 cases: 55 pass, 6 fail, 1 skip | 1 |
| JDK 25 / legacy | 2026-10-26 20:01:18–20:12:52 | 62 cases: 55 pass, 6 fail, 1 skip | 1 |
| JDK 21 / Picocli | 2026-10-26 19:28:59–19:32:09 | 18 cases: 17 pass, 1 fail, 0 skip | 1 |
| JDK 25 / Picocli | 2026-10-26 20:16:16–20:19:04 | 18 cases: 17 pass, 1 fail, 0 skip | 1 |

Both legacy legs failed the same six cases: `absent child requires a successful listing`; `use different hostname when not checking peer-name`; `start solr with mTLS needed`; `start solr with mTLS wanted`; `test keystore reload`; and `searching solr without credentials fails with error`. Both Picocli legs failed only the helper-listing case.

The helper test expected the text `lookup unavailable`, but its captured output was empty, so the hardened diagnostic assertion failed. The stream test did receive HTTP 401, but the client text was `non ok status: 401, message:null`, which the new regexp does not match. These are unresolved assertion mismatches, not passes.

The four SSL-related failures did not establish certificate-specific causes. Fresh JDK 25 snapshots show Solr listeners bound to `127.0.0.1` while ZooKeeper clients attempted `localhost/[::1]` and received `Connection refused`. In the mTLS-needed test, the failing command was the second-node `solr start -z localhost:${ZK_PORT} -p ${SOLR2_PORT}`, before the later TLS assertion. A snapshot also contains a server-side `(certificate_required) Empty client certificate chain` diagnostic, but it does not convert that failed start command into a passing test. The peer-name, mTLS-wanted, and keystore-reload failures likewise returned connection-refused diagnostics rather than their expected certificate causes.

The one legacy skip was `check stop command doesn't hang`: `timeout utility is not available`. No other skip or zero-test result was observed.

## Docker case and controls

`test_rolling_upgrade.bats` was not selected or executed; it is **BLOCKED/UNRUN**, not skipped or passed. Docker is not installed/on PATH, so this host could not start a daemon or run the test. The required image availability/digests were not checked. No Docker case is counted as passing, and the overall gate must remain incomplete.

No isolated synthetic discriminating controls were run. Each remains outstanding: helper-guard mutants; posting-count/no-op add; postlogs batch-add and HTTP/select failures; TLS refusal/curl exit-7 injections; degraded-health state; deletion lookup failure and exit-code mode; export/auth/stream diagnostic injections; and quiet-post/compression/graceful-stop failures. The normal helper regression cases for TLS refusal, closed channel, handshake, and unrelated TLS errors ran as part of the Picocli subset, but they are not substitutes for the isolated injected controls. The normal matrix already has unresolved failures, so none of the controls is claimed as proof.

## Environment, final checks, and evidence

Host toolchain: Ubuntu 24.04.4; Gradle wrapper/daemon 9.7.0 with JDK 21.0.12.1; child runtimes JDK 21.0.12.1 and 25.0.4.1; system BATS 1.10.0, while Solr's Gradle `downloadBats` runner actually used bundled BATS 1.8.2; Bash 5.2.21; Python 3.12.3; Git 2.43.0; curl 8.5.0; OpenSSL 3.0.13. Docker was not installed/on PATH.

Final `tidy :solr:packaging:check -x test` completed with exit 0 (`:solr:packaging:check` ran); `git diff --check` from base to final head was clean. The build log records `errorProneSkipped`; Error Prone did not execute. The final worktree was clean, the candidate remote ref was unchanged, the build lock was free, and resource guards did not reach their 15% inode floor (final root headroom: 182,144 free of 837,408; `/dev/shm`: 3.4 GiB free).

Evidence root on phone-agent-2: `/workspace/gates/logs/phone-agent-2/bats-assertion-hardening-round-1/`. The JDK 21 legacy bundle is `jdk21-legacy.log`, `jdk21-legacy-junit.raw.xml`, `jdk21-bats-test-output-artifacts.tar.gz`, `jdk21-legacy.command.txt`, `jdk21-legacy.headroom.log`, and `jdk21-legacy.inode-monitor.tsv`. The JDK 25 legacy bundle is `jdk25-legacy.log`, `jdk25-legacy-junit.raw.xml`, `jdk25-legacy-test-output-artifacts.tar.gz`, `jdk25-legacy.monitor-wrapper.command.txt`, `jdk25-legacy.headroom.log`, and `jdk25-legacy.resource-monitor.tsv`. The JDK 25 Picocli bundle is `jdk25-picocli.log`, `jdk25-picocli-junit.raw.xml`, `jdk25-picocli-test-output-artifacts.tar.gz`, `jdk25-picocli.monitor-wrapper.command.txt`, `jdk25-picocli.headroom.log`, and `jdk25-picocli.resource-monitor.tsv`. The common `bats-hardening.init.gradle`, preflight logs, and final-check logs are also retained there.

**Evidence gap:** the JDK 21 Picocli raw JUnit report, full transcript, command, and resource logs were retained, but its separate `test-output-picocli` bundle was not archived before the JDK 25 Picocli task reused that output directory; no retry was made on the failing head. The complete JDK 21 Picocli bundle is therefore unavailable. The retained files are `jdk21-picocli.log`, `jdk21-picocli-junit.raw.xml`, `jdk21-picocli.command.txt`, `jdk21-picocli.headroom.log`, and `jdk21-picocli.inode-monitor.tsv`.
