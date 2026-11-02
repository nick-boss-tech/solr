# Failure report: PR 5107 security-review fixes round 2 Linux gate, round 1

- Branch: `nick-boss-tech/solr:solr-8442-security-review-r2-submit` at H2
  `d264f756644ac2fb8b56c09c42a0c737198c148a` (tidy-repaired head; see below).
  Contributor C `25c88fb1c432120b98f7af668cf58be0052d13d0`.
  Superseding gate: `gates/pr-5107-security-review-fixes-round-2-superseding-d264f75664.md`.
  Original gate `gates/pr-5107-security-review-fixes-round-2.md` untouched.
- Gate runner: `~/workspace/tools/5107-gate-runner.sh`, worktree
  `~/workspace/solr-worktrees/5107-gate`, seed `8442C0DE`. Runner ended
  GATE RUNNER DONE, GATE FAILED (packaging false).
- Verdict: NOT GREEN. No green receipt. The Java-side unit legs all passed;
  the packaging legs are blocked, for two distinct reasons below. Steps 6-10
  (packaging true mode, fail-before unit + packaging, final tidy, root check)
  did not run: the gate stops at a failed leg, as the 16793 and 18587 gates
  did. No test at H counts as proof for H2; every leg below ran at H2.

## Tidy repair (before the run below)

- The first run at H stopped GATE INVALID at the tidy guard: tidy reflowed
  `SSLTool.java` (production, 2 hunks) and `SSLToolTest.java` (test, 1 hunk).
  Both were proven formatting-only by identical whitespace-stripped token
  streams (md5 of the whitespace-stripped files equal before and after).
- Repair committed as Nick Shanin, no trailers, as `d264f75664` and
  plain-pushed fast-forward to the fork branch (verified by ls-remote).
  An earlier push attempt to `origin` (which points at apache/solr) failed
  403 and changed nothing.

## Legs that ran, verified from logs and fresh XML/TAP

- Step 0 tidy guard at H2: rc=0, 0 tracked files changed.
- Step 1 `:solr:core:compileTestJava`: BUILD SUCCESSFUL (1m 10s).
- Step 2 default security-manager unit run: BUILD SUCCESSFUL (1m 5s).
  Fresh XML: SecureExampleCredentialsTest 5 tests, 0 failures (gate requires
  5 methods executed); SSLToolTest 9 tests, 8 skipped (the external-program
  methods, expected to skip under the security manager); AuthToolTest 3/3;
  AuthToolPicocliTest 3/3; IncludeFileEditorTest 7 tests, 2 skipped.
  Copies: `~/workspace/tools/5107-unit-sm-*.xml`.
- Step 3 no-security-manager unit variant (`-Ptests.useSecurityManager=false`):
  BUILD SUCCESSFUL (1m 55s). Fresh XML: SSLToolTest 9 tests, 0 failures,
  0 errors, 0 skipped (exactly what the gate requires); IncludeFileEditorTest
  7/0/0/0; TestSolrCLIRunExample 2/2 (both secure-setup methods);
  SolrCLIRunExamplePicocliTest 2/2. Copies: `~/workspace/tools/5107-unit-nosm-*.xml`.
- Step 4 bats tarball seeding: the four tarballs (bats-core 1.8.2,
  bats-support 0.2.0, bats-assert 2.0.0, bats-file 0.3.0) downloaded into
  `solr/packaging/build/bats-downloads/`; the packaging task then ran bats
  from `build/bats-libs/`, so seeding worked.
- Step 5 packaging BATS, `SOLR_PICOCLI=false`: BUILD FAILED (3m 15s),
  `:solr:packaging:integrationTests` failed with bats exit value 1.
  Report XML (`~/workspace/tools/5107-pack-false/test-output/report.xml`):
  test_ssl.bats 8 tests, 8 failures; test_secure_example.bats 2 tests,
  2 failures. TAP (`test-output.txt`): 10 `not ok` lines, no passes.

## Diagnosis: two distinct causes, neither a patch regression

1. Nine failures are the root-user environment refusal, identical for base
   and head by construction. `bin/solr` lines 1015-1020: on `start`/`restart`,
   if EUID is 0 and FORCE is false, it prints "WARNING: Starting Solr as the
   root user..." and exits 1. The only override is the `--force` argument;
   there is no environment-variable bypass. This VM runs as root, and the
   BATS tests invoke `solr start` without `--force`, so every node start
   fails before any branch code runs. Affected: all 8 SSL cases (including
   the 7 pre-existing ones and the new renewal case, which fails at its
   initial `solr start`) and secure-example case 10 (its section contains
   the root warning twice). This is the same cause recorded for the 18552
   bats leg on this host. A rerun needs a non-root execution environment
   or an isolated, explained harness repair that passes `--force`
   (committed and re-gated at the new head, per the gate's repair rule);
   the gate does not authorize weakening the check silently.
2. One failure is a genuine test/implementation gap, not environmental.
   The new invalid-administrator case runs
   `solr start -e secure-setup --no-prompt ... --credentials alice:alice`
   and expects the policy error 'administrator username and password must
   differ'. But `bin/solr`'s argument parser has no `--credentials` case:
   the only match in the file is the generic error at line 895
   ("`$1 is not supported by this script`"), so the invocation fails at
   argument parsing on any head. The branch diff does not touch `bin/solr`,
   so this case cannot pass on C or H2 as written. Handback: either add a
   `--credentials` passthrough to `bin/solr` (a production change outside
   the current diff, needing its own review) or correct the test's
   invocation. The gate makes no ad hoc change here.

## State after the run

- Worktree still at H2, clean; fork ref still H2. Branch unchanged by the
  gate run itself (the only push was the tidy repair above).
- Logs: `~/workspace/tools/5107-gate-runner.out`,
  `5107-gate-tidy.log`, `5107-gate-compile.log`, `5107-gate-unit-sm.log`,
  `5107-gate-unit-nosm.log`, `5107-gate-pack-false.log`; archived
  `~/workspace/tools/5107-pack-false/test-output/` (report.xml,
  test-output.txt, failure snapshots).
- Claim: `claims/pr-5107-security-review-fixes-round-2-phone-agent.md`,
  marked DONE FAIL with this report.
- No PR, no Jira/GitHub post.
