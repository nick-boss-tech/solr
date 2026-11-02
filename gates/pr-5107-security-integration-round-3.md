# Gate: PR 5107 integrated security fixes, round 3

Status: DONE (PARTIAL at the superseding head F2 `7e531b083182366f8b6e7f00a3781ce3cbb713de` via `gates/pr-5107-security-integration-round-3-superseding-7e531b0.md`; receipt `receipts/PR-5107-security-integration-round-3-partial.md`. BATS legs skipped by owner direction.)

## Scope and exact inputs

- Owner authorization: Nick requested patching the November 2 review and returning the result to the gate.
- Tags: gate, bats, review, records. Intended host: any capable Linux gate host, one build at a time under its host lock, with JDK 21.0.12.1, Gradle 9.7.0 from this branch, OpenSSL, keytool, curl and BATS dependencies. **Packaging must execute as a non-root account.** Windows does not execute this job.
- Fork branch: `nick-boss-tech/solr:solr-8442-security-review-r2-submit`.
- Required fixed head F: **4b04ba0c5f412403ac46db64364d90324b90b55d**, independently verified by GitHub ref API after plain push.
- Contributor A: `7d193dac0f4abc441488f0ac2a9a734983d4fcd2`, Apache PR https://github.com/apache/solr/pull/5107. Its parent C is `25c88fb1c432120b98f7af668cf58be0052d13d0`.
- Old gate head H2: `d264f756644ac2fb8b56c09c42a0c737198c148a`. Integration merge M `d2d2bd269060877814ba1152c650b39bb2a60b70` has parents H2 and A; F adds the scoped shell/logging/test/help repair above M. Both histories must remain ancestors.
- Implementation report: `reports/pr-5107-security-integration-round-3.md`. Implementation claim: `claims/pr-5107-security-integration-windows-round-3.md`.
- Supersedes `gates/pr-5107-security-review-fixes-round-2.md` and `gates/pr-5107-security-review-fixes-round-2-superseding-d264f75664.md` for the current branch. Preserve both and their failure report `reports/pr-5107-security-review-fixes-round-2-gate-round-1.md`. Their partial unit proof is not proof at F.
- Add execution claim `claims/pr-5107-security-integration-round-3-<host>.md` after preflight. Add dedicated receipt `receipts/PR-5107-security-integration-round-3.md`; preserve `receipts/SOLR-8442.md` and other rounds. This posting does not assign a Linux executor.
- Store fresh logs/XML/TAP under `/workspace/gates/logs/<host>/pr-5107-security-integration-round-3/` (or record the actual host-equivalent absolute path) in distinct per-leg folders. Seed `8442C0DE` throughout.

## Preflight, failure policy and execution account

Read live board instructions, current claims, gate runner and host resource rules. Refresh fork and public PR heads. Verify F and both ancestries; record PR movement without silently changing this input. Fork remote must be verified as nick-boss-tech/solr before any push; do not use an origin that points to apache/solr. No force-push, public PR/JIRA action, contributor mutation or shared-index edits.

The previous BATS run executed as root and nine cases failed before their targets. Before any new BATS leg record `id -u` and the execution account; require nonzero uid. If the VM's login is root, prepare a separate unprivileged account/checkout with the needed permissions and build dependencies, or hand the job to a suitable host. Do not add `--force` to product tests or disable security checks to accommodate the host. The unsupported credentials option was separately repaired in F; do not treat that old failure as environmental.

Run mandatory detached tidy preflight. Then regenerate CLI docs with `./gradlew :solr:solr-ref-guide:generateCliDocs` and require the start page to match its annotations. The start page was hand-synchronized on Windows. If tidy or generation changes files, permit only deterministic formatting or generated-doc synchronization in the reviewed outbound files: commit ordinary repair above F, plain-push to the fork, separately verify the full SHA, and add a superseding gate pinned to that head **before positive proof**. Unexpected generated changes outside this slice require diagnosis/handback; no bulk unrelated updates. All subsequent F references mean that final re-pinned head.

Record tool versions, checkout/JAVA_HOME, environment, source status and runner invocation. Existing diagnosed dependency provisioning (including the named BATS tarballs from the previous report) may be reused with source/version records. Never retry an executed failure silently. Product or new assertion failures require a failure report/implementation handback; a diagnosed environment repair may be followed by an explicitly recorded rerun. Preserve first-failure logs. Require fresh test outputs, not cached task success, and archive before subsequent runs overwrite them.

## Compile and Java proof

First run the compile and the exact validator that failed upstream:

```bash
./gradlew :solr:core:compileTestJava :solr:core:validateLogCalls
```

Default security-manager run:

```bash
./gradlew :solr:core:test --tests org.apache.solr.cli.SecureExampleCredentialsTest --tests org.apache.solr.cli.SSLToolTest --tests org.apache.solr.cli.AuthToolTest --tests org.apache.solr.cli.AuthToolPicocliTest --tests org.apache.solr.cli.IncludeFileEditorTest --tests org.apache.solr.cli.CLIUtilsTest --tests org.apache.solr.cli.SecureExampleZkTest -Ptests.seed=8442C0DE
```

Require five credentials methods and three SecureExampleZkTest methods to execute. Record method-level skips in SSL/editor tests; expected external-process skips do not satisfy the next required variant. New CLIUtils TLS fallback/independence methods must execute. Source-property/configuration tests are not a substitute for actual ZK TLS BATS.

Explicit external-program variant:

```bash
./gradlew :solr:core:test --tests org.apache.solr.cli.SSLToolTest --tests org.apache.solr.cli.IncludeFileEditorTest --tests org.apache.solr.cli.TestSolrCLIRunExample.testSecureSetupExample --tests org.apache.solr.cli.TestSolrCLIRunExample.testSecureSetupCustomPasswords --tests org.apache.solr.cli.SolrCLIRunExamplePicocliTest.testSecureSetupExample --tests org.apache.solr.cli.SolrCLIRunExamplePicocliTest.testSecureSetupCustomPasswords -Ptests.useSecurityManager=false -Ptests.seed=8442C0DE
```

Require SSLToolTest 9 tests, zero failures/errors/skips; editor all seven; both secure-example methods in each parser class. The Java example executor substitutes plaintext MiniSolrCloudCluster ZooKeeper, so do not call this runtime ZK TLS proof. In SSLToolTest specifically verify client-auth settings preservation, short existing encrypted PEM success, wrong-PEM rejection without writes and short new-keystore-password rejection without writes.

## Positive packaging and unresolved startup diagnosis

Run both files through the normal task with explicit inherited parser mode, serialized and as the verified non-root account. Archive logs/TAP/XML after each mode:

```bash
env SOLR_PICOCLI=false ./gradlew :solr:packaging:integrationTests --tests test_ssl.bats --tests test_secure_example.bats
```

```bash
env SOLR_PICOCLI=true ./gradlew :solr:packaging:integrationTests --tests test_ssl.bats --tests test_secure_example.bats
```

Require **8 SSL + 3 secure-example cases per mode**, all executed with no skips. Do not use integrationTestsPicocli, whose preloaded curated list would widen this slice. Verify parser mode and exact selected files from actual outputs.

- Invalid admin: both `--credentials alice:alice` and `-u alice:alice` must reach the Java policy error before HTTP/ZK credentials, TLS, zoo_data or second home exist. Unsupported shell syntax, root refusal or compilation failure is not policy proof.
- Option validation: absent values, a following option instead of a value, and credentials on a plain start must fail before a server starts.
- Existing secure-example test now starts with valid long-form credentials; verifies actual ZK TLS with hostname checking; rejects plaintext; retains all role/ACL/distributed-query assertions; rejects an unrelated CA with valid digest credentials, then succeeds with the correct CA; changes the admin password through the API; restarts using short-form credentials; rejects the old password; then restarts using saved credentials without an option. All those stages must execute. The correct-CA success must accompany the rejection so a dead server is not mistaken for a trust check. Record the actual TLS/trust failure from verbose logs if available; a NoAuth failure is insufficient.
- Renewal: initial required-mTLS startup, certificate regeneration/reinstallation, restart, successful client-certificate request and failed no-client-certificate connection. The negative curl lacks --fail, so an HTTP 401/403 cannot satisfy TLS rejection. Record the TLS failure and preceding successful request.

Current upstream CI at A (run 41448182482) failed this example after both servers started with `ERROR: No value present`. Its node logs show successful ZK connections and Jetty starts; they do not establish a TLS cause. The F test enables existing `--verbose` on example starts specifically to retain the wizard stack. **If this exception recurs, preserve the complete stack, first failing command, both node logs, TAP/XML, process/port state, UID, JDK and checkout; stop and hand back the diagnosed failure.** Check actual stack origin before considering process scanning or empty system-info results. Do not relabel it an environment issue, disable the assertion, make an unproven production workaround, or rerun silently. A green package leg requires the whole workflow, not just two started nodes.

## Original-production and shell matched pairs

Keep final fixed-head tests unchanged. Use separate disposable checkouts, retain original failure outputs, rebuild the relevant distribution so an old binary is not tested accidentally, and record diffs and test hashes.

### Earlier three production bugs

From final F restore **only** `SSLTool.java` and `SecureExampleCredentials.java` under `solr/core/src/java/org/apache/solr/cli/` from A. They are identical in A and C; use A for current integration context. Leave new shell/logging code, tests and TLS changes intact. Compile and run:

```bash
./gradlew :solr:core:test --tests org.apache.solr.cli.SSLToolTest.testClientAuthenticationSettingsArePreserved --tests org.apache.solr.cli.SSLToolTest.testShortExistingPemPassword --tests org.apache.solr.cli.SecureExampleCredentialsTest.testRejectsUnusableAdministratorBeforeSaving -Ptests.useSecurityManager=false -Ptests.seed=8442C0DE
```

All three must execute with their intended defects. Preservation and admin rejection should fail their intended assertions; short PEM may be a JUnit runtime error due to original six-character validation where conversion should succeed. Missing APIs, compilation/setup/transport failures are not fail-before proof. Then rebuild and run the same two packaging files once with SOLR_PICOCLI=false. Expect exactly two failures: mTLS renewal and early admin-policy rejection; all other cases, including the new shell value-validation case and supplied valid credentials/restarts, must pass. Document failure points and unchanged tests.

### Shell integration bug

In another checkout at final F restore **only** `solr/bin/solr` from A. Keep fixed Java, new tests and all TLS changes. Rebuild packaging and run `test_secure_example.bats` with SOLR_PICOCLI=false. All three cases should fail at the unsupported credentials option instead of reaching their expected behavior; record the actual early shell error for each. No servers should be created by these three commands. This baseline is separate from the preceding production baseline and is not an expected positive run.

The logging defect already has a current-head upstream failure at A. A passing fixed-head validateLogCalls plus the reviewed two-line movement is sufficient for that repair; no synthetic mutation is requested.

## Final checks and receipt

At final fixed F run:

```bash
./gradlew tidy
./gradlew check -x test
```

Require clean source status and diff --check, no blanket task exclusions. Use the documented standalone clone method if Antora cannot handle a worktree .git file. Record any diagnosed NODE_ENV or other environment correction beside the actual result; an earlier interrupted/failed run stays recorded. Source or generated-file changes invalidate proof and require a pushed, superseding pinned head before a full successful gate.

Receipt must name every leg, exact heads, fork ref verification, actual tests/skips/failures, both parser modes and non-root account, Java/TLS/credential results, original-production and shell failure signatures, fresh absolute log/XML/TAP paths, formatting/root results, and any unresolved runtime exception. Final section: concise PR follow-up draft **for review only; do not post**. Mention the private trust-anchor regeneration limit and make no FedRAMP/FIPS claim. Green requires every positive/check leg and both negative pairs; until then **Implemented, Proof Pending**.
