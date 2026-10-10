# CLI, bin scripts and packaging round 1, part S4: SOLR-18132 and SOLR-18339 drafts

## Heads

- **SOLR-18132:** `ls-remote refs/heads/solr-18132-submit` is `54835cac6f8518d7110865d73ec8a37f18419f1d`. It matches the assignment and the receipt's gated head. The local remote-tracking ref is already at that SHA, so no fetch was needed.
- **SOLR-18339:** `ls-remote refs/heads/solr-18339-submit` is `47e53884609c0c7f84e85069881f9d820342e70e`. It matches the assignment and the receipt. The local remote-tracking ref is already at that SHA.
- The merge-base with the local `upstream/main` is `14c7aac0d151402b00259e2fb9bf5eed7049ec5d` for both. The diff file sets match the shapes the receipts describe.

## Per ticket

**SOLR-18132: draftable.**
- Draft `pr-drafts/cli/SOLR-18132.md`, 5,386 characters with links (3,881 with link targets stripped).
- Why: the gate is green at the head (CLIUtilsTest 7 of 7). The premise run on base `CLIUtils.java` fails exactly 1 of 7. BATS `test_ssl.bats` is 5 of 7 at the head, test 1 included. Tests 2 and 7 are stated as failing on main in the same environment and are not claimed.
- Draft content: the behavior change (an explicit `solr.ssl.enabled=false` now overrides an https `urlScheme` for tools, stated as intended); the launcher line's purpose (key-store-only SSL is derived after the export loop, `bin/solr` lines 116 to 120 and 205 to 209); and two Limits (no early failure, which the ticket requested; main only, because SOLR-18056 is absent from 9.10.1).
- No Choice section: none is recorded for this ticket, and none was added.

**SOLR-18339: draftable.**
- Draft `pr-drafts/cli/SOLR-18339.md`, 7,637 characters with links (4,811 with link targets stripped).
- Why: no Java in the diff. BATS `test_start_solr.bats` is 11 of 11 at the head, with both new tests. The two new tests fail on the base code and nothing else fails. The packaging module check passes (receipt).
- Draft content: the recorded Choice as "A choice to check" (the status wait sends no `--credentials`, and `StatusTool` rethrows auth errors, so a 401 from a secured node is a failed start; flagged for maintainers, not fixed), with the pointed question. The wildcard-bind certificate-name question is open in Limits. The `install_solr_service.sh` changes are covered. The ticket's timing precondition is not decided in the draft.
- Both drafts are above the formula's guide of about 3,500 characters once links are counted. The 18339 length is mostly citations.

## Status wait without lsof (from the 18339 diff)

The loop is at head `bin/solr` lines 1380 to 1397. The base lines are 1380 to 1410.
- **Base:** lsof is called only in the start wait. Line 1381 is the `lsof -v` check, and line 1387 is the `lsof -t -PniTCP` poll every 2 seconds. Without lsof, base prints a NOTE (line 1406), sleeps 10 seconds (line 1407), and prints the started message with the pid (lines 1408 to 1410) without checking Solr.
- **Head:** no lsof call remains in `bin/solr`. The start wait (the background branch only; the foreground exec path is unchanged) runs `run_tool status --solr-url <scheme>://<bind host>:<port> --max-wait-secs $SOLR_START_WAIT`. With or without lsof it behaves the same: no lsof check, no NOTE, no sleep, no fallback. The loop is in `StatusTool.waitToSeeSolrUp` (head lines 267 to 292). It calls the system info endpoint, retries every 2 seconds until the deadline, returns on the first success, and rethrows auth errors.
- **Spinner:** the 18339 diff removes the `spinner $!` call from the start wait (base line 1404). The `spinner()` definition (head line 453) and its stop-path call (head line 546) remain. So the spinner does not survive the start wait unchanged; this branch removes it from that path. This bears on the SOLR-7924 interaction, for part S1 or S6.
- **For SOLR-10390 (reported here, not in the draft):** on base, lsof appears only in that start wait. If 18339 lands first, `bin/solr` has no lsof call site left, so a 10390 probe would serve no call site in `bin/solr` unless the stop or status paths gain a port check.
- **For SOLR-12347 (reported here, not audited):** `SOLR_START_WAIT` defaults to `SOLR_STOP_WAIT` (head and base line 155; `SOLR_STOP_WAIT` defaults to 180 at head line 154). Raising the stop-wait default would also raise the start-wait default, unless `SOLR_START_WAIT` is set.

## SOLR-18132 Windows scope

- **How it is stated:** the draft says nothing about `solr.cmd`. It describes the `bin/solr` launcher line, and it claims no Windows result.
- **Why:** the receipt says the `solr.cmd` lines were dropped "by an owner-provisional call (on Windows the variables are inherited by child processes anyway)". Neither the receipt, the assignment nor the claim records that call as a public-facing scope item, so under the rule it is named here, not in the draft.
- **State at head:** `solr.cmd` and `solr.in.cmd` blobs are identical to base. The `CLIUtils` change is Java and applies to Windows too. By reading (not run; Windows builds are revoked), base `solr.cmd` lines 81 to 85 set `SOLR_SSL_ENABLED` with `set` before the JVM starts, so the environment carries it to the tool JVM. `EnvUtils` reads the environment at init (`EnvUtils.java` line 80).
- **Owner call, public-facing:** not recorded as such (owner decision 4).
- **SOLR-18339 on Windows:** `solr.cmd` is not in the diff. Its status call (base line 1181) passes no `--credentials` and no URL, and finds the server through the pid-file scan. The Unix wait passes an explicit URL on the bind host. The draft says only that `solr.cmd` already waits through `StatusTool` and is not changed.

## Self-check

- Both drafts: zero em or en dashes. Zero hits for gate, receipt, ledger, rc=0, JUnit XML, pre-fix, owed, round, control, premise, takeover, owner, verified, claim, spinner.
- Blob links: the 18132 draft uses `54835cac6f8518d7110865d73ec8a37f18419f1d` for head citations and `14c7aac0d151402b00259e2fb9bf5eed7049ec5d` for base citations, labeled base. The 18339 draft uses `47e53884609c0c7f84e85069881f9d820342e70e` for head citations and the merge-base for base citations, labeled base. Test counts were checked by counting `@Test` and `@test` entries at the heads (CLIUtilsTest 7; `test_start_solr.bats` 11; `test_ssl.bats` 7). Cited line ranges were read at the named SHAs.
- Only the two draft files were written.

## Receipt disagreements

1. **SOLR-18339 receipt:** "StatusTool takes credentials only from its CLI option". Incomplete. The probe's HTTP client also applies the customizer named by `solr.solrj.http.jetty.customizer` (`HttpJettySolrClient.java` `applyClientCustomizer`, lines 185 to 186 at `47e53884609c`). `bin/solr` adds that customizer through `AUTHC_CLIENT_BUILDER_ARG` when `SOLR_AUTH_TYPE=basic` (head lines 359 and 362), and `run_tool` passes `AUTHC_OPTS` (head line 501). `PreemptiveBasicAuthClientCustomizer` reads `solr.security.auth.basicauth.credentials` or `solr.httpclient.config` from system properties. So the probe can authenticate without `--credentials` when those settings are present. The draft states this. The 401 exposure stands for setups without them. Suggested receipt wording: "StatusTool takes credentials from its CLI option, or from the client customizer and system properties passed through `AUTHC_OPTS`".
2. **SOLR-18132 receipt:** the `solr.cmd` lines were dropped "by an owner-provisional call". Not a contradiction, but the receipt does not say the change depends on SOLR-18056. The SOLR-18056 changelog on main (`changelog/unreleased/SOLR-18056-urlScheme-csp.yml`) describes `solr.ssl.enabled` detection, which is the code path this branch now reaches (`ZkClientClusterStateProvider` lines 277 to 287). The draft names the dependency.
3. **SOLR-18132 receipt:** "the `bin/solr` launcher line is kept, because SSL implied by the key store alone is derived after the export loop". Confirmed by reading (export loop lines 116 to 120; derivation lines 205 to 209). Not a disagreement.

## Upstream drift (affects citations, not the claims)

- Local `upstream/main` is at `8e62c2686882` (October 9). It has changed `StatusTool.java` (193 lines), `CLIUtils.java` (48 lines), `bin/solr` (6 lines) and `solr.cmd` (18 lines) since the merge-base. The claims still hold on that upstream: `StatusTool` still rethrows auth errors without retry; `CLIUtils` still reads the `urlScheme` cluster property (now near line 312); `bin/solr` still waits on lsof (now lines 1384 to 1410). Line references in the drafts are at the merge-base and the head. Both branches need a rebase before any PR, and the drafts must be rechecked after it. `upstream` was not fetched.

## Owner decisions

1. **SOLR-18339 opening timing:** the ticket asks to port `bin/solr` only after SOLR-18336 has been live in 10.1 for some time. Whether SOLR-18336 is live in 10.1 was not checked. The draft does not decide this.
2. **SOLR-18339 Choice:** confirm that the no-credentials status wait may ship with the 401 exposure flagged for maintainers, or require a fix first. The draft asks maintainers, as recorded.
3. **SOLR-18339 HTTPS certificate-name question for wildcard binds:** still open. Decide whether to add an HTTPS BATS case before opening, or keep it stated as open (the draft currently does the latter).
4. **SOLR-18132 `solr.cmd` scope:** decide whether Limits should say that `solr.cmd` is unchanged and that Windows relies on the inherited environment (not run). Not in the draft now.
5. **SOLR-18132 precedence:** an explicit `solr.ssl.enabled=false` now overrides an https `urlScheme` for tools. No Choice is recorded. The draft states the behavior in "What this change does". Decide whether it should be asked as a Choice.
6. **SOLR-18132 scope:** the 9.10.1 port and the ticket's early-failure request are named as Limits, with an offer to open follow-ups on request. Decide whether to open them.

## Not checked

- No builds, Gradle, tests, BATS runs, `gh` writes, commits, pushes or posts. Gate and test results come from the receipts. The part read the test files and counted the test entries at the heads.
- The Jira context JSON was read for both tickets (read only). No JIRA call.
- Windows behavior was not run. The `solr.cmd` statements come from reading.
- How the probe's 401 becomes a `SolrException` with code 401 was not traced (the receipt asserts it). The `SOLR_AUTH_TYPE=basic` path with no credentials was read, not run. By reading, the customizer's setup throws `IllegalArgumentException`, which is not auth-related and would be retried until the timeout. The `EnvUtils` environment mapping was only partly traced.
- SOLR-18336's release state in 10.1: not checked.
- `upstream/main` was not fetched. The base is the merge-base, as the formula requires.
- Facts about SOLR-10390, SOLR-7924 and SOLR-12347 are read-only observations. Those tickets were not audited, and nothing about them is in the drafts.
