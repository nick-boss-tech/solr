# BATS assertion hardening implementation, round 1

Status: IMPLEMENTED, PROOF PENDING.
Owner direction: Nick requested a patch branch and a Linux gate handoff after the static review.
Claim: claims/bats-assertion-hardening-implementation-round-1-windows.md.
Review: reports/bats-scripts-assertion-review-round-1.md and its three unit reports.

## Pushed candidate

Fork branch: [solr-bats-assertion-hardening-submit](https://github.com/nick-boss-tech/solr/tree/solr-bats-assertion-hardening-submit).
Exact head: `0d7c6f4e6cb6d2211b59ed9eed36db90182b122e`.
Base: apache main `0d6b2e32f598d772aed094038022a6a588fdfc5a`.
Commit: Harden packaging BATS assertions against unrelated failures.
Author/committer: Nick Shanin <nick.boss.us@gmail.com>, no trailers.
Push: normal fork push; GitHub ref read back at the exact local SHA. Worktree is clean.

The change is confined to 14 files under solr/packaging/test: 13 existing files (one helper and 12 BATS suites) and the new test_bats_assertions.bats. There are no Java/launcher/build changes. No public PR head moved, and no PR, comment, or Jira action occurred. No changelog was added because there is no assigned Jira/PR number; repository AGENTS.md explicitly forbids an entry before one is assigned.

## What changed

| Review entries | Candidate change |
| --- | --- |
| U1-C1 / U2-C1 attempted-file summaries | Correct the authenticated post target to COLL_NAME. Add fresh successful HTTP queries with exact document-count checks to previously unguarded single-file, optimize, and verbose post tests. Existing later queries already guard the extraction/no-type/directory tests; their scripts are not changed merely to repeat an existing check. |
| U1-C2 / U2-C2 empty commit after failed log batches | Require command success, reject Batch sending failed, and require positive indexed log records. The authenticated shared collection queries file_s:* so the earlier monitor document cannot satisfy the check. |
| U1-C3 failed ZK reads accepted as deletion | Add assert_zk_child_absent, which requires a successful listing then rejects an exact child token. Replace all seven negative existence checks identified in the review. Positive existence helpers retain their behavior. |
| U1-C4 mode assertion exit | Require exit 1 rather than arbitrary nonzero for the --exitcode mode mismatch. |
| U2-C3 / U3-C3 degraded health accepted | Add assert_healthy, requiring command success and the collection-level healthy status before nested shard statuses. Use it for normal health tests and rolling-upgrade health polling. |
| U2-C4 stale export artifact | Remove the earlier output.json before the next export to that same path, and require successful exits for each successful export case. |
| U2-C5 negative-only output | Require successful quiet args posting with no ERROR/WARNING; require the verbose export failure to identify SolrCloud mode rejection and exit 1 after an explicit standalone precondition. |
| U3-C1 / U3-S1 generic TLS failures | Replace generic connection/closed-channel alternatives with certificate-specific causes. The shared assertion rejects connection refusal even if other output matches. Capture verbose Java causes; require curl's peer-verification exit 60 for untrusted-certificate probes. |
| U3-C2 generic auth ERROR | Require stream exit 1 and an HTTP-401/Unauthorized diagnostic in verbose output, not just ERROR. |
| U1-S1 failed curl accepted as no compression | Require successful HTTP requests with curl -f, preserve errors, and match the actual encoding header case-insensitively for its conventional capitalization. |
| U2-S1 negative document count | Require successful HTTP select and a positive numeric numFound. |
| U3-S2 failed stop accepted as graceful stop | Require stop exit zero and assert both endpoints are stopped. |

Nine new helper regression cases cover failed/matching child lookups, collection versus shard health, healthy-looking output from a failed command, and certificate causes versus connection refusal, bare closed channels, and unrelated TLS errors.

## Verification and limits

Windows verification: reviewed the full tracked diff and the complete new test file; git diff --check passed; outbound paths were checked before staging; author/committer and fork ref were read back; the worktree is clean. No Gradle, BATS, Solr, Docker, mutation or product test ran on Windows.

The candidate is intentionally proof pending. In particular, Java TLS wrappers can omit the certificate cause from client output: this is why the gate must exercise JDK 21 and 25 and must not restore a broad Server refused connection or bare ClosedChannelException alternative. The export-mode and streaming-401 diagnostics also need confirmation through the real integration paths. If evidence requires a fresh server-log assertion, it must be tied to this operation and traced to the observed producer before being committed and re-gated.

The underlying production PostTool count and PostLogsTool error handling are unchanged. This branch hardens the tests to detect their false-success symptoms; changing those public CLI behaviors would be separate work.

## Handoff

Gate: gates/bats-assertion-hardening-round-1.md.
Intended host: a capable Linux BATS gate host with JDK 21 and 25, Docker, curl/OpenSSL/keytool and the normal Solr packaging toolchain.
Completion requires final-head passing normal legs, real discriminating controls, formatting/check results and a receipt. Merely publishing this implementation report is not gate proof.

PR 5225's candidate, test_security_manager.bats and its gate/control ownership are untouched. The old static review's findings remain immutable evidence, with the candidate mapping recorded here.

