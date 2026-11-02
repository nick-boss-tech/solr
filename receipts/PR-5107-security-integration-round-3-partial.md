# Receipt: PR 5107 integrated security fixes, round 3 partial gate (phone agent)

Status: PARTIAL (by owner direction, the BATS legs were skipped, so no
green verdict was possible). All Java legs this host ran passed. The
root check failed on a task that fails identically at the contributor
head A; the details are below.

- Jobs: `gates/pr-5107-security-integration-round-3.md` as modified by
  `gates/pr-5107-security-integration-round-3-superseding-7e531b0.md`.
- Claims: vm6 claimed first and handed off after two host restarts
  (`claims/pr-5107-security-integration-round-3-vm6.md`); this host
  took over (`claims/pr-5107-security-integration-round-3-phone-agent.md`,
  started 2026-11-02T17:20:47Z, at Nick's direction).
- Head: F2 `7e531b083182366f8b6e7f00a3781ce3cbb713de` on fork branch
  `solr-8442-security-review-r2-submit`, ls-remote verified before and
  after the run. Ancestry re-verified in the runner preflight: F
  `4b04ba0c5f`, H2 `d264f75664`, A `7d193dac0f`, and C `25c88fb1c4` are
  all ancestors of F2.
- Checkouts: full clones `~/workspace/solr-clones/5107-r3` (head) and
  `~/workspace/solr-clones/5107-r3-pair` (pair), real .git directories.
  Toolchain: vendored JDK 21 and Gradle 9.7.0 via
  `~/workspace/tools/solr-gradle.sh`; seed `8442C0DE` throughout.
  Runner: `~/workspace/tools/5107-r3-gate-runner.sh`, output
  `~/workspace/tools/5107-r3-gate-runner.out` (ends PARTIAL GATE RUNNER
  DONE). Step logs `~/workspace/tools/5107-r3-logs/`; fresh JUnit XML
  `~/workspace/tools/5107-r3-xml/{sm,nosm,pair}/`. This host executes
  as uid 0; the gate's non-root requirement applies to the skipped
  BATS legs only.
- vm6's earlier legs stand as recorded in its claim: tidy at F and at
  F2 rc=0 with no changes (its log folders on vm6), and the
  generateCliDocs mismatch at F that F2 repairs.

## Legs run by this host at F2

- generateCliDocs: rc=0 and the tree stayed clean (0 files changed),
  confirming the F2 start-page synchronization. Log `gencli.log`.
- Compile and logging validator: `:solr:core:compileTestJava` and
  `:solr:core:validateLogCalls` both pass (rc=0). Log
  `compile-validate.log`. The logging repair therefore has its
  fixed-head validator pass; the defect's upstream failure at A is on
  record in the round 3 gate, as the gate prescribes.
- Default security-manager run (rc=0), fresh XML: SecureExampleCredentialsTest
  5 tests, 0 skipped; SecureExampleZkTest 3 tests, 0 skipped;
  CLIUtilsTest 10; AuthToolTest 3; AuthToolPicocliTest 3; SSLToolTest
  9 tests with 8 skipped (external-process methods skip under the
  security manager, as in round 2); IncludeFileEditorTest 7 tests with
  2 skipped. All required executions happened.
- No-security-manager run (rc=0), fresh XML: SSLToolTest 9 tests, 0
  failures, 0 errors, 0 skipped; IncludeFileEditorTest 7 tests, 0
  skipped; TestSolrCLIRunExample secure-setup methods 2 of 2;
  SolrCLIRunExamplePicocliTest secure-setup methods 2 of 2.
- Original-production pair (separate clone; only `SSLTool.java` and
  `SecureExampleCredentials.java` restored from A, verified by diff):
  all three target tests fail with their intended signatures at seed
  8442C0DE: testClientAuthenticationSettingsArePreserved,
  AssertionError; testShortExistingPemPassword, failure carrying
  IllegalArgumentException "Password files must contain one line of at
  least six characters" (the original six-character validation); 
  testRejectsUnusableAdministratorBeforeSaving, "Expected exception
  IllegalArgumentException but no exception was thrown." The pass side
  of the pair is the two runs above at F2. Log `pair.log`, XML in
  `5107-r3-xml/pair/`.
- Final tidy: rc=0, 0 tracked files changed. Final tree clean,
  `git diff --check` clean.

## Root check: failed on a pre-existing task

`check -x test` in the full clone: BUILD FAILED at
`:solr:webapp:verifyUiWasm`: "Checksum mismatch for solr-ui-wasm.zip:
expected 654494303e4af3b8bfb26a83d250603f19033820295591d88db0597991ce8ad2
but got c336b9dc7b3692d5b7d4c973f468355a900cf848b75c899783028e920375a798."
283 of 313 tasks executed before the failure stopped the build; tasks
after that point did not run. Log `check.log`.

Discriminator run by this host: the same single task at A
`7d193dac0f` (the contributor head, before any of the branch's fixes)
fails with the same expected checksum and a different downloaded hash
(e7c30fc527bb38247dfb0e8a1474c77b44e8edfda2407d2fae5e2e9d6786f461; log
`verifyuiwasm-at-A.log`). Two conclusions are supported: the failure
pre-exists at A and is not caused by this branch's delta (the delta
touches neither gradle.properties nor the webapp), and the artifact
served to this host is not byte-stable between downloads, so the leg
cannot pass here as served. No broader cause is claimed.

## Skipped legs (owner direction, 2026-11-02)

Not run: the packaging BATS suites in both parser modes (test_ssl and
test_secure_example, 8 + 3 cases each), the BATS original-production
pair, the shell matched pair (bin/solr restored from A), and the
non-root BATS account requirement. Runtime ZK TLS behavior, the
credential option's end-to-end shell behavior, the password rotation
flow, and the mTLS renewal flow therefore remain unverified by any
gate at F or F2. The upstream `No value present` startup exception
recorded in the gate was not exercised either way.

## PR follow-up draft (for Nick's review only; do not post)

> Quick status on the security-review branch for this PR: the fixes
> are now integrated with your November 2 head by merge, and the shell
> now forwards `--credentials` and `-u` to example starts, with early
> rejection for missing values or use on a plain start. The Java side
> is verified: the credentials, SSL tool, auth, and CLI utility suites
> pass with and without the security manager, and the three original
> defects fail before the fix and pass after. The packaging BATS
> suites have not been run on this combined head yet. One unrelated
> note: `check` currently stops at `:solr:webapp:verifyUiWasm` on a
> checksum mismatch for the prebuilt UI bundle, which reproduces at
> your head as well. Separately, per the guide, private trust anchors
> still need restoring after a truststore regeneration.

No PR comment, review, body edit, or Jira action was made. No
FedRAMP or FIPS claim is made anywhere in this record.
