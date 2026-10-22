# SOLR-18565 bats run 1: results from vm1

Assignment: `assignments/solr-18565-bats-run-1.md`. Claim: `claims/solr-18565-bats-run-1-vm1.md`.

Host: `Linux htch-runtime 7.0.0-39-generic #1 SMP PREEMPT_DYNAMIC Thu Sep 24 08:42:15 UTC 2026 x86_64 x86_64 x86_64 GNU/Linux`; `/etc/os-release` PRETTY_NAME "Ubuntu 24.04.5 LTS". Per the Decisions table (host is not ubuntu-26.04): recorded here; the runs were still done and still count. One more host fact decides everything below: this host runs every process as the root user (uid 0).

Head verification before any run: `git ls-remote` for `refs/heads/solr-18565-submit` printed `2c747689c32bd17a6327ecd2b60999b972771269`, the required head (on this host the fork remote is named `fork`; the assignment writes `origin`). Base for item 3: the assignment prints `7892348272727b8530d26ad08db734712ab2dacb12` (41 characters, a typo); the verified origin/main tip `78923482727b8530d26ad08db734712ab2dacb12` was used instead. It is the SOLR-18565 branch's merge-base era main head (the PR-cap commit).

## Why every run fails the same way on this host

`solr/bin/solr` refuses to start as root: at line 1004 it exits when `$EUID` is 0 and `FORCE` is false, printing "WARNING: Starting Solr as the root user is a security risk and not considered best practice. Exiting." `FORCE` is set only by the `-force` argument (line 865); there is no environment override, and the bats tests call plain `solr start`. So on this host no bats test that starts Solr can pass: Solr never starts. In the head full run's output the root warning appears 38 times; test 87 and test 88 show it verbatim, and the other failures are the same start refusal or a downstream cascade of it (for example `test_assert.bats` failing on `solr assert --started` against a server that never started). Test 88 therefore fails on the head and on the base, identically, before either version reaches the curl hostname-mismatch assertion the branch changes. A true read of test 88 on this host needs the direct-bats-as-nobody pattern already used on this host for earlier bats work (stage the built distribution, run `bats` under `setpriv` as uid 65534); that is a different vehicle from the one this assignment prescribes, so it is recorded here as the follow-up option, not run.

## Environment preparation (no branch or code changes)

- `pool/bin/sweep-run.sh` reads its host values from `/workspace/AGENTS.md`, which does not exist on this host's layout; the file was created with Host id `vm1`, JAVA_HOME `/home/hatch/workspace/tools/jdk21/jdk-21.0.12.1+1`, GRADLE_USER_HOME `/home/hatch/.gradle`, matching this host's vendored toolchain. The Gradle 9.7.0 wrapper distribution was pre-seeded into the Gradle user home because the sandbox intercepts direct TLS to services.gradle.org (the first `--version` probe failed with a PKIX handshake error); the local proxy relay on 127.0.0.1:3129 was started, and each sweep ran under the host test-queue flock with `JAVA_TOOL_OPTIONS` carrying the IPv4-stack flag this host's Gradle runs require.
- `:solr:packaging:downloadBats` fetches four tarballs with `ant.get` and the same TLS interception killed the first attempt before any test ran (SSLHandshakeException at `solr/packaging/build.gradle` line 275). The task sets `skipexisting: true`, so the four archives were fetched with curl and staged into both worktrees' `solr/packaging/build/bats-downloads/`: bats-core 1.8.2 (sha256 0f2df311a536e625...), bats-support 0.2.0 (9bfa93d2db046e37...), bats-assert 2.0.0 (15dbf1abb98db785...), bats-file 0.3.0 (54d1c8b1908608ae...). Only `build/` output was touched; both worktrees' git status stayed clean.

## Head full run

- Status: DONE (third attempt; see attempts below). Command: `pool/bin/sweep-run.sh /home/hatch/workspace/solr-worktrees/solr-18565-head <log dir> solr-18565-head-full :solr:packaging:integrationTests`, sweep rc=1, build time 10m 51s.
- Counts from `solr/packaging/build/test-output/report.xml`: 73 tests, 21 passed, 47 failed, 5 skipped, 0 errors.
- Test 88 ("use different hostname when not checking peer-name", `test/test_ssl.bats`): FAILED. Failure at test_ssl.bats line 92: "`solr start' failed", with the root-user warning in the output. The branch's changed assertion is never reached.
- Log path: `/home/hatch/workspace/tools/bats-18565-logs/head-full-retry2/` (`solr-18565-head-full.log`, `solr-18565-head-full.headroom.log`, and `test-output-evidence/` holding a copy of the run's `test-output`).
- Attempts: attempt 1 (`logs/head-full/`) stopped at `downloadBats` on the TLS interception described above, no tests ran; attempt 2 (`logs/head-full-retry/`) was killed by a runtime restart about 73 seconds in, mid-build; attempt 3 is the counted run.
- Other failing tests (46): every one traces to the same root-start refusal described above; the full list is in the run's report.xml at the log path above. (This paragraph was truncated in the first posting of this report; the two sections below were missing from it and are added in this completion.)

## Head SSL file

- Status: DONE. Command: `pool/bin/sweep-run.sh /home/hatch/workspace/solr-worktrees/solr-18565-head <log dir> solr-18565-head-ssl :solr:packaging:integrationTests --tests test_ssl.bats` (the --tests option was accepted; no fallback needed).
- Counts from `solr/packaging/build/test-output/report.xml`: 7 tests, 0 passed, 7 failed, 0 skipped.
- Test 88 ("use different hostname when not checking peer-name"): FAILED at test_ssl.bats line 92, "`solr start' failed", with the root-user warning in the output. The branch's changed assertion is never reached.
- Log path: `/home/hatch/workspace/tools/bats-18565-logs/head-ssl/`

## Base SSL file

- Status: DONE (second attempt; the first stopped on a stale Gradle journal lock left by the head runs, cleared per the standing rule, and the retry is the counted run). Command: the same as the Head SSL file item, in the base worktree at `78923482727b8530d26ad08db734712ab2dacb12`.
- Counts from `solr/packaging/build/test-output/report.xml`: 7 tests, 0 passed, 7 failed, 0 skipped.
- Test 88: FAILED identically to the head (line 92, root-user warning). On this host the base and the head are indistinguishable: both die before the curl assertion.
- Log path: `/home/hatch/workspace/tools/bats-18565-logs/base-ssl-retry/`

## Completion note

Added by vm1 on 2026-10-22: the first posting of this report (commit 9830aafe9d) was truncated inside the Head full run section and lacked the Head SSL file and Base SSL file sections. The sections above come from the run evidence (report.xml and test-output.txt in the recorded log paths), re-verified at completion time. No runs were repeated for this completion, and no code or branch state changed.
