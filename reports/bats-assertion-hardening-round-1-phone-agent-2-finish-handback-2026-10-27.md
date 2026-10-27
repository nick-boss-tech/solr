# BATS assertion-hardening round 1 — phone-agent-2 finish handback (2026-10-27)

**Disposition: OPEN / INCOMPLETE. No GREEN receipt is issued.** This add-only handback records the final-head evidence and returns the remaining work for environment/TLS-policy reimplementation. The shared gate and original claim are not edited.

## Candidate

Fork branch `nick-boss-tech/solr:solr-bats-assertion-hardening-submit` is at `4d5d876d946a4f5ac174a7f68e16f4dd9b5f9f68`, based on pinned base `0d6b2e32f598d772aed094038022a6a588fdfc5a`. The candidate worktree was clean at final postrun. Commits since base are:

- `0d7c6f4e6c` — Harden packaging BATS assertions against unrelated failures.
- `de4cf59ec3` — Harden packaging BATS failure assertions.
- `db22aabe99` — test: assert fresh mTLS hostname diagnostic.
- `4d5d876d94` — Require successful no-compression BATS response.

All candidate changes are test/helper-only. No production code, upstream branch, PR, comment, or Jira record was changed. The candidate was pushed by normal fast-forward updates; there was no force push.

## Discriminating controls

| Group | Result and evidence |
|---|---|
| 1 — helper regressions | **Passed.** Nine normal helper tests passed; six isolated weakened-helper mutants each failed their selected regression. Evidence: `/workspace/gates/logs/phone-agent-2/bats-assertion-hardening-round-1/finish-2026-10-27/control-group-1-helper-head4-2026-10-27/` (`normal-nine-helper-regressions/` and `m1`–`m6`). |
| 2 — posting count | **Differential observed, with a base normalization caveat.** The old assertion shape accepted the zero-document outcome while the final candidate rejected it. The pinned-base authenticated test's `--name monitors` target returned HTTP 404 before reaching the comparison; the preserved normalized copy changes only that target to the test's created `COLL_NAME`, leaving the old assertion body unchanged. Do not describe it as an unmodified-base pass. Evidence: `control-group-2-posting-2026-10-27/base-normalized-control.md`, `base-normalized-run-retry3/` and `retry4/`, plus `control-group-2-posting-head4-2026-10-27/head-run/`. |
| 3 — PostLogs | **Candidate fault controls passed.** A corrected wire observer confirmed real HTTP/2 JavaBin add and form-encoded commit, both forwarded with HTTP 200, followed by an independent positive `file_s:*` probe. Normal and authenticated add-only 503 controls preserved successful commit and zero log-file documents; the candidate rejected the swallowed add failure. Normal and Picocli post-commit select controls first observed positive upstream `numFound`, then injected HTTP 503; candidate BATS rejected curl exit 22. Evidence: `control-group-3-postlogs-head4-prepared/g3-wire-observe-head4-commitflag1/`, `g3-batch-normal-head4/`, `g3-batch-auth-head4/`, `g3-select-failure-normal-head4/`, and `g3-select-failure-picocli-head4/`. **Still missing:** an old-base comparison under the same PostLogs fault, and a separate response-body-without-`numFound` control; the select mutation used a positive real upstream response followed by HTTP 503. |
| 4 — TLS refusal | **Incomplete.** The valid JDK 21 peer-name control established a successful TLS precondition, injected one generic connection refusal, and the candidate rejected it at `assert_tls_failure` (`test_ssl.bats:125`). The post-bootstrap client-hostname attempt failed during second-node startup at line 326 with zero injected events; it did not reach the target probe. Missing-client-cert, truststore, curl-exit-7, reload, and the other configured refusal targets were not demonstrated. Evidence: `control-group-4-tls-refusal-head4-prepared/runs/group4-21-peer-name-20261027T211056-969977/` and `runs/group4-21-client-hostname-20261027T211357-971707/`. |
| 5 — degraded health | **Passed.** A real two-shard collection retained one healthy shard and an active/down replica pair after node 2 stopped; the real healthcheck returned exit 0 with `status: degraded`, and the candidate rejected it at the healthy-status assertion. Evidence: `control-group-5-health-head4-prepared/runs/group5-health-head4-run3/`. |
| 6 — delete / exit status | **Passed.** With only `delete -c COLL_NAME` suppressed, the real pre-teardown ZooKeeper listing still contained `COLL_NAME` and exited 0; the candidate rejected the injected listing error. Separately, status 100 injected only for the exact `assert --exitcode` invocation was rejected where status 1 was required. Evidence: `control-group-6-delete-exitcode-head4-prepared/runs/delete-retry4/` and `runs/assert-head4/`. |
| 7 — export / auth diagnostics | **Passed.** The pinned-base export shape accepted a stale `output.json` after the repeated export was failed; the candidate rejected injected status 73 at `run -0`, with the old file's hash unchanged. The candidate rejected an unrelated non-cloud diagnostic despite expected status 1; a missing-expression stream fault failed its required 401 regex, and `strace` recorded zero connections to the Solr HTTP listener. Evidence: `control-group-7-export-auth-head4-prepared/group7-export-base-head4-retry2/`, `group7-export-head-head4-candidate1/`, `group7-noncloud-head-head4-noncloud1/`, and `group7-stream-head-head4-stream1/`. |
| 8 — quiet post / compression / stop | **Passed.** A real successful args-mode post was followed by an injected `WARNING`, rejected by the candidate. The base no-compression control passed; the candidate rejected an empty failed response after setup, with a positive HTTP 200 check in the hardened control. The stop-noop wrapper verified both Solr nodes were live, suppressed only the tested stop, and the candidate rejected the result. Evidence: `control-group-8-quiet-stop-head4-prepared/post-warning/run-head4/`, `stop-noop/run-head4/`, and `control-group-8-compression-head4-2026-10-27/{base-run,head-run}/`. |

Only valid targeted outcomes above are counted; failed harness/setup attempts remain preserved but are not treated as control results. Group 4 and the two explicit Group 3 gaps keep the discriminating-control gate incomplete.

## Final legacy matrix and TLS diagnosis

The final-head legacy legs each selected the exact 12 non-Docker BATS files and executed 62 cases:

| Runtime | Result |
|---|---|
| JDK 21 legacy | 57 passed, 4 failed, 1 known skip, 0 errors; runner exit 1. |
| JDK 25 legacy | 57 passed, 4 failed, 1 known skip, 0 errors; runner exit 1. |

The same four `test_ssl.bats` cases failed on both runtimes: `use different hostname when not checking peer-name` (line 117); `start solr with mTLS needed` (second-node start at line 326, before the negative probe); `start solr with mTLS wanted` (line 522); and `test keystore reload` (line 595). The expected specific TLS causes were not observed: the preserved client output includes `Server refused connection` / `java.net.ConnectException: Connection refused`. JDK 25 ZooKeeper diagnostics show attempts to `localhost/[0:0:0:0:0:0:0:1]:<port>` refused. This is consistent with a host loopback/address-family versus service-bind mismatch, not evidence that a certificate assertion should accept a refusal.

A previous focused SSL diagnostic run using task-scoped `-Djava.net.preferIPv4Stack=true` reduced the affected cases (6/7 passed on each JDK), but it is not final-matrix proof. Its remaining client-hostname probe recorded the server-side `SSLHandshakeException: (certificate_unknown) Endpoint Identification Algorithm HTTPS is not supported on the server side`. The current candidate's `test_ssl.bats:369–392` now asserts that fresh, operation-scoped producer diagnostic; its own comment correctly says this does **not** prove the intended client-hostname policy works. A focused run of that assertion on `db22aabe` passed, but it does not establish the actual hostname policy. Do not broaden assertions to generic `ClosedChannelException`, arbitrary SSL errors, or connection refusal.

The only matrix skip was the known unchanged `test_start_solr.bats` timeout precheck (`timeout utility is not available`). It is recorded as a skip, not a pass. Raw JUnit files remain preserved; literal backspace bytes made them invalid XML, so labeled sanitized derivatives replace only those bytes. JDK 21 had 286 such bytes; JDK 25 had 238.

The owner amendment states no current host has Docker. `test_rolling_upgrade.bats` was therefore excluded and is **UNRUNNABLE** here; no subleg handoff was created and it is not counted as a pass.

## Remaining gate items and handback

The JDK 21 and JDK 25 Picocli matrix legs were not launched after both legacy legs reproduced the same TLS failures. Final `tidy`, `git diff --check`, and `:solr:packaging:check -x test` were not run on the final head. Gradle reported `errorProneSkipped`; no Error Prone execution is claimed. The final-matrix README/runner records the Docker exclusion and no-GREEN rule.

For reimplementation, resolve the localhost/ZooKeeper family/bind setup on a capable gate host or in test-only runtime configuration, then rerun all four exact-head matrix legs. Separately decide whether the server-side client-hostname verification behavior is intended to be supported; its observed unsupported-algorithm diagnostic cannot demonstrate that policy. Finish the missing TLS/Group 3 controls and final checks. Do not weaken assertions to make the current failures green.

Postrun after JDK 25: candidate clean, no active build/test/Solr processes, host lock available, 138,114/837,408 root inodes free (16.48%), and 2.2 GiB free on `/dev/shm`. No further test was launched after this diagnosis.

## Evidence locations and fingerprints

Final matrix evidence root: `/workspace/gates/logs/phone-agent-2/bats-assertion-hardening-round-1/finish-2026-10-27/final-matrix-head4-2026-10-27/`. Each leg directory retains the run plan, prelaunch, raw and sanitized JUnit, full TAP, Solr-output archive, full runner log, exit code, and postrun state. JDK 25's added per-leg classification is `jdk25-legacy/attempt-classification.txt`.

- JDK 21 sanitized JUnit SHA-256: `b30f6236667f136fce80ba7a0107f44bd7f1b9a59c2d1c6fa1557b82e177381b`.
- JDK 21 full output archive SHA-256: `d2ed0f105ff4e1217c574136c8f647f5e55295b1510c46050a99a27570b23c09`.
- JDK 25 sanitized JUnit SHA-256: `2917d2f74f15c907194cd9511dcc0478de838debb2bc8d019511b1294892c75b`.
- JDK 25 full output archive SHA-256: `feccf993a19f6d6c6ecaf67e984fca3df69dfaefee9bb653a6fbf700fdd1a7d2`.
- Pinned-base and candidate mTLS comparisons: `base-mtls-jdk21/`, `head-mtls-jdk21/`, and `head-mtls-jdk21-db22aabe/` under the same finish evidence root.
- Docker/JDK host check: `/workspace/gates/logs/phone-agent-2/bats-assertion-hardening-round-1/finish-2026-10-27/docker-and-jdk25-host-check.txt`.

**Final status:** the original gate remains **OPEN / INCOMPLETE**. No receipt or GREEN claim was added.