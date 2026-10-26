# BATS assertion-hardening follow-up — phone-agent-2 (2026-10-26)

**Disposition: local follow-up findings only. The original gate remains OPEN/INCOMPLETE; no GREEN receipt exists.**

## Pinned baseline and local-only changes

The candidate remains `nick-boss-tech/solr:solr-bats-assertion-hardening-submit` at exact baseline `0d7c6f4e6cb6d2211b59ed9eed36db90182b122e` (base `0d6b2e32f598d772aed094038022a6a588fdfc5a`). The phone-agent-2 worktree stayed at that HEAD with local, uncommitted modifications only. No source commit or candidate-branch push was made.

The local patch contains the two narrow assertion fixes:

1. `solr/packaging/test/bats_helper.bash` checks the ZooKeeper listing command's status directly and preserves any diagnostic output; a failed listing still fails the absent-child check.
2. `solr/packaging/test/test_stream.bats` accepts the observed SolrJ wording `non ok status: 401, message:null` while retaining its ERROR and HTTP-401 requirements.

It also contains a separate, test-only TLS diagnostic adjustment in `solr/packaging/test/test_ssl.bats`: the missing-client-certificate probe records the server log's byte offset, rejects connection-refusal outcomes, checks the client-side closed-channel symptom, and requires a new server-log entry for the producer-side TLS cause. The exact local patch bytes are preserved beside this report as the [gzip-compressed patch artifact](bats-assertion-hardening-round-1-phone-agent-2-follow-up-2026-10-26.patch.gz); decompress it before applying. The IPv4-only JVM option was supplied by a temporary Gradle init script for these diagnostics only; it is not part of the source patch.

## Focused results

| Runtime | Helper regression + stream | SSL-only file with task-scoped IPv4 sockets |
|---|---:|---:|
| JDK 21.0.12.1 | 14/14 passed | 6/7 passed; one failed |
| JDK 25.0.4.1 | 14/14 passed | 6/7 passed; one failed |

The SSL-only run selected `test_ssl.bats` (7 cases) with `-Djava.net.preferIPv4Stack=true` applied only to the test task. In both JDK runs, the missing-client-certificate probe found the fresh server-side diagnostic `javax.net.ssl.SSLHandshakeException: (certificate_required) Empty client certificate chain`.

The remaining failure is the later client-hostname-verification probe in `start solr with mTLS needed`. Its client output contains `ClosedChannelException`, not the expected hostname/certificate diagnostic. Both JDKs' server logs instead show `SSLHandshakeException: (certificate_unknown) Endpoint Identification Algorithm HTTPS is not supported on the server side` (with a nested `CertificateException` carrying the same message). Solr documentation describes this setting as checking the originating client hostname against its certificate; this server-side unsupported-algorithm error does not establish that the intended hostname-mismatch check ran. It is therefore still a failure, not a pass, and the assertion was not broadened to accept it.

## Gate status and retained evidence

The prior report, gate definition, claim, and status are unchanged. The overall gate remains **OPEN/INCOMPLETE**; no GREEN receipt was added. `test_rolling_upgrade.bats` remains **BLOCKED/UNRUN** because this host has no Docker, and all eight discriminating-control groups remain unrun. The candidate remote ref remains `0d7c6f4e6cb6d2211b59ed9eed36db90182b122e`. No PR or Jira comments were posted.

The two focused BATS transcripts and both SSL-run transcripts, raw reports, and snapshots remain locally preserved under `/workspace/gates/logs/phone-agent-2/bats-assertion-hardening-round-1/follow-up-2026-10-26/`. The JDK 25 server diagnostic extract is `tls-evidence-jdk25-server-diagnostics.txt`. This coordination-branch record commit adds only this report and its patch artifact; it does not commit or push the candidate source changes.
