# PR 5107 security integration and gate repairs, round 3

Status: Implemented, Proof Pending.

Nick authorized patching the November 2 review and returning the result to the Linux gate. The fork branch `solr-8442-security-review-r2-submit` is now **4b04ba0c5f412403ac46db64364d90324b90b55d**. Plain push moved it from H2 `d264f756644ac2fb8b56c09c42a0c737198c148a`; a separate GitHub ref API read returned the exact new full SHA. Eric's public branch remains unchanged at A `7d193dac0f4abc441488f0ac2a9a734983d4fcd2`, OPEN/DRAFT.

## Ancestry and implementation

- Preserved H2 and A with ordinary merge `d2d2bd269060877814ba1152c650b39bb2a60b70`; no rebase, transplant, force-push or contributor-branch write. The merge was conflict-free and retained both the new ZooKeeper TLS paths and our three existing fixes.
- Added ordinary repair commit **4b04ba0c5f412403ac46db64364d90324b90b55d**, author/committer Nick Shanin, no trailers.
- `bin/solr start -e ...` now forwards `--credentials` and `-u` as separate array arguments to `run_example`. Rejects absent option values and credentials supplied to plain starts rather than silently ignoring them. Credentials before the example flag also work because scope validation follows argument parsing. Shell and picocli start help plus the corresponding reference page describe the option as Unix/macOS only.
- Moved the inexpensive ZooKeeper client-address selection immediately before the logging guard. This restores the validator's recognized directly preceding guard while keeping the log call guarded.
- Extended the existing BATS fixture: both credential aliases reject an unusable account before security state; missing values and plain-start misuse fail early; valid long-option startup, API password change, short-option restart, rejection of old credentials, and a subsequent restart with saved credentials are covered.
- The same example fixture explicitly verifies the ZooKeeper hostname with OpenSSL, rejects an unrelated CA through the real CLI with valid digest credentials, and then successfully lists with the correct CA. Both parser runs exercise the inherited parser for the rejection and explicitly exercise picocli for the succeeding listing.
- Enabled existing verbose diagnostics on example starts in BATS. The prior CI's `No value present` has no captured Java stack. This patch does not claim to fix that unexplained exception or change unrelated process/status handling on speculation.
- Added a scoped changelog fragment listing Nick Shanin by name only. The earlier three-fix fragment stays intact. Neither fragment purports to summarize Eric's entire new-feature PR.

## Exact new repair files (six)

1. `solr/bin/solr`
2. `solr/core/src/java/org/apache/solr/cli/StartCommand.java`
3. `solr/core/src/java/org/apache/solr/cloud/SolrZkServer.java`
4. `solr/packaging/test/test_secure_example.bats`
5. `solr/solr-ref-guide/modules/deployment-guide/pages/cli/solr-start.adoc`
6. `changelog/unreleased/SOLR-8442-example-shell-credentials.yml`

The complete delta above A is thirteen files: these six plus the existing `SSLTool.java`, `SecureExampleCredentials.java`, `SSLToolTest.java`, `SecureExampleCredentialsTest.java`, `test_ssl.bats`, `enabling-ssl.adoc`, and `SOLR-8442-security-setup-validation.yml`. No unrelated source files are in the repair. The generated start reference was synchronized by hand on Windows; the gate must regenerate it and commit/re-pin any resulting adjustment before proof.

## Assessment and verification limits

Correctness: checked shell dispatch, both option orders, value handling, example-only scope, credential persistence and startup call paths, password-update tests and the logging validator's previous-line rule. The early rejection test I added in round 2 now has the required shell path. The three earlier production repairs remain unchanged.

Architecture: transport TLS and digest ACL authorization remain independent. Source integration preserves A's changes. `StartCommand` remains help metadata; runtime starts still dispatch through the shell into `RunExampleTool`. No new public hook or generalized TLS framework is introduced.

Maintenance/reuse: retained the existing two-node BATS fixture and its generated HTTPS/ZK configuration, reused the same security API and curl assertions for password rotation, and did not add a new server harness or schema. Wrong-CA setup uses the fixture's existing OpenSSL dependency. No dependency was added.

Verification: `git diff --check` passed, worktree is clean, and the outbound file list, author/committer and fork ref were checked. No Gradle, formatting tool, shell execution, OpenSSL/keytool, server startup or runtime test ran on Windows. H2's partial unit proof does not cover this new combined head. No green receipt exists for round 2, and the failed gate's raw phone-host XML was not audited here.

## Handoff

New job: `gates/pr-5107-security-integration-round-3.md`. It supersedes both round 2 jobs for this current branch head. Receipt: `receipts/PR-5107-security-integration-round-3.md`; preserve older records. The job requires a non-root account, actual BATS execution in both parsers (8 SSL + 3 secure-example cases each), all inherited unit and fail-before proof, the shell regression pair, generated help synchronization, clean tidy, logging validation and root checks.

If the original `No value present` recurs, capture the verbose exception and stop for a diagnosed implementation handback. Current upstream CI artifact showed both nodes connecting to ZK and starting Jetty; it did not establish a TLS cause. Potential source origins in process scanning and system-info accessors remain hypotheses. Do not count an unexplained failure as environmental or weaken the assertions.

Claim: `claims/pr-5107-security-integration-windows-round-3.md`, board commit `6e554825ada73ffdf852de37a187e21e5b285cda`. No Linux host has been claimed by this posting. No PR comment/review/body edit, JIRA action or upstream write was made. No FedRAMP/FIPS claim. Extra private trust anchors still need restoring after truststore regeneration, as the guide explains.
