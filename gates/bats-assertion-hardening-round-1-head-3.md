# Superseding head record — BATS assertion hardening round 1

- Host: `phone-agent-2`.
- Supersedes `gates/bats-assertion-hardening-round-1-head-2.md`.
- Candidate: `nick-boss-tech/solr:solr-bats-assertion-hardening-submit` at exact head `db22aabe997c9af6d6a8f1ef0184c9778d5705b5`; parent `de4cf59ec35c66b8fec1bd16da0ce94eef0eab0b`; pinned Apache base `0d6b2e32f598d772aed094038022a6a588fdfc5a`.
- The superseding commit is `test: assert fresh mTLS hostname diagnostic`. It changes only `solr/packaging/test/test_ssl.bats`; author and committer are both exactly `Nick Shanin <nick.boss.us@gmail.com>`, with no trailers. `git diff --check` passed, the BATS parser counted 7 tests in `test_ssl.bats`, and the worktree is clean. The fork branch was pushed as a normal fast-forward from `de4cf59`; the remote was verified at `db22aabe997c9af6d6a8f1ef0184c9778d5705b5`.

## mTLS hostname discrimination and repair

- The filtered `start solr with mTLS needed` case passed on pinned base `0d6b2e32f598d772aed094038022a6a588fdfc5a` with task-scoped IPv4 setup. Evidence is preserved under `gates/logs/phone-agent-2/bats-assertion-hardening-round-1/finish-2026-10-27/base-mtls-jdk21/`.
- The same filtered case executed on the pre-repair candidate head `de4cf59ec35c66b8fec1bd16da0ce94eef0eab0b` and failed at `test_ssl.bats:378`. The client reported `java.nio.channels.ClosedChannelException`; the JDK 21 Solr server log recorded `javax.net.ssl.SSLHandshakeException: (certificate_unknown) Endpoint Identification Algorithm HTTPS is not supported on the server side`, with the stack reaching `X509TrustManagerImpl.checkIdentity` during the port-33853 client-certificate handshake. The complete JUnit, TAP transcript, logs and failure snapshot are preserved under `gates/logs/phone-agent-2/bats-assertion-hardening-round-1/finish-2026-10-27/head-mtls-jdk21-attempt-2/`.
- The test-only repair replaces the generic client-side hostname regex with an operation-scoped byte offset and a fresh-log match for that exact observed producer diagnostic. It still requires the client failure to be `ClosedChannelException` and rejects connection-refusal diagnostics; it does not skip or accept an arbitrary SSL exception. The source comment explicitly records that the unsupported server-side endpoint-identification message is not proof that Solr's intended client-hostname policy works. Production behavior was not changed.
- No BATS or Gradle test has yet run on the new head `db22aabe997c9af6d6a8f1ef0184c9778d5705b5`. All subsequent proof is pinned to this exact head unless another superseding record is posted.

## Remaining gate state

- Pending: re-run the focused mTLS case on this new head; complete all eight discriminating control groups; run the final four-leg JDK 21/25 legacy and Picocli matrix on the final head; and run the required final `tidy`, `git diff --check`, and `:solr:packaging:check -x test` checks.
- Per Nick's Docker amendment, `test_rolling_upgrade.bats` is unrunnable by any current pool host. No subleg handoff will be created. This case is not passed or covered; the gate remains open and no GREEN receipt is implied.
- At the latest state check, root had 142,207 free inodes of 837,408; `/dev/shm` had 2,261,696,512 free bytes; no active build/test process was found and the documented host lock was free. No cleanup beyond the previously approved exact path was performed.
