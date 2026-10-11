# CLI and Security draft fidelity, slice 2

Assignment: `assignments/pool-draft-fidelity-cli-security-builddocs-metrics.md`. Claim: `claims/pool-draft-fidelity-cli-security-builddocs-metrics.md`, slice 2 (CLI SOLR-17598 NO GATE, 18132, 18339; Security SOLR-10627).

Slice drafts: `pr-drafts/cli/SOLR-17598.md`, `pr-drafts/cli/SOLR-18132.md`, `pr-drafts/cli/SOLR-18339.md`, `pr-drafts/security/SOLR-10627.md`.

Category round 1 reports checked: `reports/cli-bin-packaging-round-1.md` (roll-up), `-s3.md` (17598 construction argument and owner items), `-s4.md` (18132 and 18339, full read), `-s6.md` (landing and Windows parity table, by grep); `reports/security-round-1.md` (roll-up, full read) and `-s1.md` (10627, full read). Answers material: `material/` has no file that names 17598, 18132, 18339 or 10627 (grep, no hits).

Receipts used: `receipts/SOLR-17598.md`, `receipts/SOLR-18132.md`, `receipts/SOLR-18339.md`, `receipts/SOLR-10627.md`. All four exist in the worktree.

Jira context read only (no Jira call): `research/jira-context/SOLR-10627.json`, `SOLR-17598.json`, `SOLR-18132.json`, `SOLR-18339.json` in the main checkout. Used to check quoted ticket wording.

Source checks: `git show <sha>:<path>` from the Solr source checkout, exported to the scratchpad and read with line ranges. Read-only.

## Verdicts

| Draft | Head checked (`git ls-remote origin refs/heads/solr-<n>-submit`) | Verdict |
|---|---|---|
| SOLR-17598 | `8c91cf047a96fee86dd6164b81715d1ed51ebfc4` (matches draft) | DRIFT (1 item) |
| SOLR-18132 | `54835cac6f8518d7110865d73ec8a37f18419f1d` (matches draft) | CONSISTENT |
| SOLR-18339 | `47e53884609c0c7f84e85069881f9d820342e70e` (matches draft) | CONSISTENT |
| SOLR-10627 | `5a15dc0ba22083a294524210b3be007d870daa35` (matches draft) | CONSISTENT |

Merge-bases with the local `upstream/main` ref (`git merge-base`, not fetched): SOLR-17598 `97d973814336101e12475558d7419321c743de79` (matches the draft's "base" links); SOLR-18132, SOLR-18339 and SOLR-10627 `14c7aac0d151402b00259e2fb9bf5eed7049ec5d` (matches). All cited SHAs resolve (`cat-file -t` returns commit).

Changelog fragments exist at the cited head SHAs with the expected names: `solr-17598-submit` has `changelog/unreleased/SOLR-17598-solr-cmd-logs-dir.yml`; `SOLR-18132.yml`; `SOLR-18339.yml`; `SOLR-10627-reject-null-collection-permission.yml`. The 17598 and 10627 names carry a suffix, not the plain `SOLR-<n>.yml` pattern of `pr-formula.md`. Both match the branch files, and the round reports accepted them, so this is not drift.

## SOLR-17598

Verdict: DRIFT (1 item).

The limit is visible. The Proof says the change is "verified by construction" and "has not been run on Windows, and that run is still to do", and "The confirming run has not been done yet." That meets receipt line 7 ("any draft for this ticket must keep that limit visible"). The Limits keep the stop --all item, the "variable ignored" item, and the parse-order scope, as the assignment requires.

The draft has no receipt counts, and it makes no count it cannot source. The diff against the merge-base touches `solr/bin/solr.cmd` and the changelog only (`git diff --stat`), which matches "solr.cmd only". The cited blocks match the head: lines 895-903 hold the new IF NOT DEFINED, the delayed-expansion strip, and the post-strip check; line 21 holds `enabledelayedexpansion`; lines 876 and 881 hold the restart goto and `:start_solr`; line 375 holds the first-argument test; the uses of SOLR_LOGS_DIR at 1110-1174 are in quotes or delayed expansion. The base lines 896-900 hold the old bracket test and the old strip. The draft's claim that no test runs solr.cmd matches a grep of the tree. The Java references are `RunExampleTool.java` (line 354 picks the name `solr.cmd` on Windows), `AuthTool.java` (help text), and the `TestSolrCLIRunExample.java` executor override (line 97 asserts the name), which collects commands instead of running them. No `.bats` or `.gradle` file names solr.cmd.

1. Draft says: "The change is verified by construction. It has not been run on Windows, and that run is still to do."
   - Evidence: The brief requires a verification date (check 2 and check 7). The draft has none. `receipts/SOLR-17598.md` line 5 gives the construction proof as recorded 2026-10-06 and "re-verified against this exact head by the round 35 disposition (2026-10-07)". Line 7 gives the changelog check as 2026-10-06.
   - Replacement: "The change is verified by construction, checked against this head on 2026-10-07. It has not been run on Windows, and that run is still to do."

Optional notes, not blocking:
- The changelog fragment at the head (`changelog/unreleased/SOLR-17598-solr-cmd-logs-dir.yml`, title line 2) says "no longer fails to parse". That states a fixed behavior the receipt says has not been run. This is branch text, not draft text. It is owner decision 5 in `reports/cli-bin-packaging-round-1.md` (line 124), and round s3 item 7 flags it. If the owner softens it, this title is ready to paste: "bin/solr.cmd reads SOLR_LOGS_DIR through delayed expansion, so values with spaces or parentheses should no longer break its parsing."
- The bold sentence "The value is read only through delayed expansion, so the path never becomes part of the block text" holds for the changed lines (897-903). Later uses at 1110-1150 use `%SOLR_LOGS_DIR%` inside quotes. A tighter form, if wanted: "In the changed lines, the value is read only through delayed expansion, so the path never becomes part of the block text."
- The sentence "Every later use of SOLR_LOGS_DIR in the start code is inside double quotes or delayed expansion" does not name `START_OPTS`. `START_OPTS` carries the quoted value from lines 1110 and 1128 into the block that starts at 1167, and it is expanded at 1169 and 1173. It is safe for the same reason (the value stays inside double quotes). The draft could name it.
- The ticket's Additional Information says other Windows environment variables "may affect" the same way. The Limits do not name this. Optional Limits sentence: "This change covers SOLR_LOGS_DIR only. The ticket also says other environment variables on Windows may behave the same way."
- Plain language: "delayed expansion" and "block" are cmd.exe terms. A short gloss would help, for example "delayed expansion (cmd.exe reads the variable when the line runs)".
- Receipt correction for the main side (not draft drift): `receipts/SOLR-17598.md` line 6 says the post-strip check "restores the old defined-but-empty defaulting". Round s3 item 4 shows base does not default a quote-only value. The draft says "Before this change, it was cleared and no default was set", which matches base. The draft is right.

## SOLR-18132

Verdict: CONSISTENT.

Proof against `receipts/SOLR-18132.md`:
- Gate finished 2026-10-05 (line 5). The draft's "run on 2026-10-05" for CLIUtilsTest, the module check and Error Prone matches. Line 6: CLIUtilsTest 7 of 7 at the head. The draft matches.
- Line 7: the premise run against base CLIUtils.java gives 7 tests with exactly one failure, the second assertion of `testNormalizeSolrUrlFromZkHonorsSslEnabled` (expected https://ssl-node:8983, got http://ssl-node:8983). The draft matches.
- Line 8: test_ssl.bats 5 of 7 at the head, test 1 included. Tests 2 and 7 fail in this VM, the same pair that fails on pristine main in the 2026-10-04 control run. The draft names both tests ("use different hostname when not checking peer-name" at line 77 and "test keystore reload" at line 507 of the head file) and gives the 2026-10-04 date. The draft matches.
- The draft's BATS count of 5 of 7 for test_ssl.bats and the CLIUtilsTest count are the only counts. Both are sourced.

Citations checked at the named SHAs: `CLIUtils.java` base 265-266 (matches the base `getClusterProperty("urlScheme", "http")` line) and head 265 (`getUrlScheme()`); `ZkClientClusterStateProvider.java` head 277-287 (solr.ssl.enabled, then urlScheme, then http); `bin/solr` head 116-120 (export loop), 205-209 (key-store-only derivation), 212-216 (launcher line), 504 (run_tool passes SOLR_SSL_OPTS); `CLIUtilsTest.java` head 152-191 (the new test); `test_ssl.bats` head 33 (test 1) and 58-66 (no urlScheme property). Changelog head 7 lines (#L1-L7). All match the text.

Limits and Choice against the round reports: the Limits (no early failure; main only, SOLR-18056 absent from 9.10.1) match `reports/cli-bin-packaging-round-1-s4.md` and the receipt. The draft has no Choice section; round s4 item 5 and roll-up owner decision 6 leave the precedence question open as owner calls. Not DRIFT.

Optional notes, not blocking (owner decisions still open):
- Windows scope is not in the draft. Receipt line 9 records that the `solr.cmd` lines were dropped by an owner-provisional call. Round s4 ("SOLR-18132 Windows scope") says the call is not recorded as public-facing and leaves it as owner decision 4. If the owner wants it in Limits, this text is ready: "Windows: bin\solr.cmd is unchanged in this branch. Windows tools get the SSL setting from the environment that solr.cmd sets. This was checked by reading and has not been run on Windows."
- Precedence (an explicit `solr.ssl.enabled=false` now overrides an https `urlScheme` for tools) is stated as a behavior change, which is correct. Round s4 item 5 asks whether it should also be a Choice.
- Length: 5,386 characters with links (round roll-up says over the roughly 3,500 guide). Owner call.

## SOLR-18339

Verdict: CONSISTENT.

Proof against `receipts/SOLR-18339.md`:
- Line 5: test_start_solr.bats 11 of 11 including both new tests, recorded 2026-10-04. The packaging check passes (log `g18339-pkgcheck.log`). A control run on pristine main fails exactly those two tests. The draft says 11 of 11 with the 2026-10-04 date, exactly those two fail on main, and the packaging check passed on 2026-10-04. The draft matches.
- Line 5: the branch diff has no Java. The draft says the same.
- Line 6: round 35 (2026-10-07) made no code change and confirmed the 401 exposure by reading. The draft's Choice states that exposure. It matches.
- Line 7: the timing precondition (SOLR-18336) is the owner's call. The draft does not decide it, as the assignment requires.

Citations checked at the named SHAs: `bin/solr` head 1380-1389 (SOLR_HOST_BIND probe rules), 1391-1392 (run_tool status with --max-wait-secs), 1393-1397 (failure message, tail, exit 1), 501 (run_tool uses AUTHC_OPTS); `bin/solr` base 1381-1404 (lsof poll), 1394-1396 (timeout exit), 1406-1410 (NOTE, sleep 10, started line); `StatusTool.java` head 194-197 (Waiting and Started echoes), 267-292 (waitToSeeSolrUp), 276-278 (auth errors rethrown); `solr.cmd` head 1181 (StatusTool wait, no change in this branch); `install_solr_service.sh` base 199 (the lsof recommendation, removed by the branch); `taking-solr-to-production.adoc` base 75 (Red Hat lsof sentence, removed) and head 198 (sample line without pid); `tutorial-techproducts.adoc` head 77 and 84 (started lines); `test_start_solr.bats` head 31-39 (invalid JVM memory test) and 53-65 (bind-address test). The draft's wording matches each range. The draft's line for the basic-auth example (SOLR_AUTH_TYPE=basic with the credentials property in SOLR_AUTHENTICATION_OPTS) matches `bin/solr` head lines 358-362 and the property name in `PreemptiveBasicAuthClientCustomizer.java`. `CommonCLIOptions` head defines `--credentials` with an argument, which supports the draft's "puts the password on the command line" point.

Choice: the alternatives (pass credentials to the wait; retry on a 401) are live and each has a cost stated. The closing question is pointed. The Choice meets pr-formula section 4.

Limits: the wildcard-bind HTTPS question is open, and the two cases use HTTP only. Matches the receipt and round s4.

Optional notes, not blocking:
- `bin/solr` head line 1391 comment says "same path as bin/solr.cmd". Round s6 item 4 notes the URL selection differs (`solr.cmd` passes no `--solr-url`). This is branch text, not draft text. It is a comment fix for the owner if the branch is touched.
- Length: 7,637 characters with links (4,811 without link targets). Round roll-up owner decision 14.
- Plain language: "probe" and "wildcard bind" are used without a gloss. Optional: "a wildcard bind (0.0.0.0 or ::)".

## SOLR-10627

Verdict: CONSISTENT.

Proof against `receipts/SOLR-10627.md`:
- Line 6: 18 of 18 at the head. TestExternalRoleRuleBasedAuthorizationPlugin 11 (including the new test), MultiAuthPluginTest 6, TestAuthorizationFramework 1. The draft matches the four counts and the sum.
- Line 5: gate finished 2026-10-05. The draft's "the run finished 2026-10-05" matches.
- Line 7: on base, with base production code and head tests, the new test fails with "expected error"; guard testEditRules passes on base; round 2 repeats the failure with the final test text. The draft matches.
- Line 10 scope notes: the mirror rule is a Limit with the follow-up offer, and the legacy null entries keep loading with a load-time warning as a possible follow-up. Both are in the draft.
- Round s1 counted the test methods at the head (11 in the base class, consistent with the receipt). MultiAuthPluginTest and TestAuthorizationFramework were not verified by the round, and the gate log is not on disk, so those two counts rest on the receipt alone. The draft matches the receipt.

Citations checked at the named SHAs: `Permission.java` head 48-113 (load), 119-129 (validateOnEdit, BAD_REQUEST message names the permission), 121-124 (custom path permissions skip), 171-172 (list form becomes the string "null"); `AutorizationEditOperation.java` head 73-79 (set-permission load and validateOnEdit) and 133-136 (update-permission merge and delegate), base 73-78 (load only); `RuleBasedAuthorizationPluginBase.java` base 104-108 (ADMIN requests consult the null entries) and 110-123 (collection requests consult only that collection and `*`), head 346-359 (init, Permission.load); `PermissionNameProvider.java` head 41-61 (names with and without null) and 46-47 (read and update default to `*`); `SecurityConfHandler.java` head 134-151 (plugin edit before persist); `BaseTestRuleBasedAuthorizationPlugin.java` head 751-762 (the new test), and `TestExternalRoleRuleBasedAuthorizationPlugin` extends that class (head line 31-32). The changelog file exists at the head. All match.

Ticket wording: the Jira description (read only) says the API "should throw an error" and that a null collection "should be ignored" on a per-collection permission, and that the second bullet is about permissions where collection is not required. The Choice and the Limits match that wording.

Choice: the "ignore" alternative is live (it is the ticket's first bullet), and its cost is stated (the default `*` would apply the permission to every collection; `PermissionNameProvider.java` lines 46-47). The closing question is pointed. Meets pr-formula section 4.

Limits: match round s1 items 2 and 3 and the receipt. The update-permission test gap is stated, which is the owner's "accept untested" choice (roll-up owner decision 1 and s1 owner decision 3). Not DRIFT.

Optional notes, not blocking:
- The branch changelog title (`changelog/unreleased/SOLR-10627-reject-null-collection-permission.yml` lines 1-3) names set-permission only, says "such as read and update", and says "previously protected nothing". Round s1 item 3 proposes a replacement for the branch owner to commit: "The authorization set-permission and update-permission commands now reject "collection": null on the per-collection permissions read, update, schema-read and schema-edit, because a null collection never applies to a request routed to a collection." Branch text, not draft text. Roll-up owner decision 1.
- The receipt's "keep protecting nothing" (receipt line 10) overstates, because null entries apply to admin requests (round s1 item 1). The draft uses the accurate form. Main-side receipt correction.
- Length: 7,704 characters with links (5,198 without). Owner call (round s1 owner decision 6).
- Plain language: "per-collection permission" and "collection-routed request" are Solr terms. An optional gloss would help a new reader.

## Not done

- No build, Gradle run, BATS run, unit test, gate run, or test-queue command.
- No `gh` call of any kind (no PR, comment, review, or edit). No Jira call.
- No commit, push, claim update, or edit to any draft, receipt, report, or branch. This file is the only file written.
- Windows and cmd.exe behavior (17598, 18132 Windows scope) were checked by reading the code only. Nothing was run.
- Gate logs named in the receipts (`g17598`, `g18132-gate.log`, `g18132-premise.log`, `g18339-bats.log`, `g18339-pkgcheck.log`, `g10627-gate.log`, `g10627-premise.log`) are not on disk. Proof counts were checked against the receipts only.
- The round 35 disposition files named by `receipts/SOLR-17598.md` and `receipts/SOLR-18339.md` (under `files/`) were not opened.
- The local `upstream/main` ref moved after round s4 recorded it. I did not fetch. The line-level citations in the drafts are at the head or the merge-base, not upstream, so the upstream drift in round s4 does not change them. The drafts still need a recheck after any rebase.
- Round CLI parts s1, s2 and s5 were used by grep only (slice-ticket lines and the 18339 interactions); they cover other tickets. Security parts s2 to s4 were not read; they cover other tickets (SOLR-18368, 11678, 12161).
- Changelog YAML files were read by eye, not parsed by a tool. The 17598 construction argument was checked against the draft's description and the diff, not re-derived in full (round s3 owns that).
- Draft dash check: zero em dashes and zero en dashes in all four drafts. Process-vocabulary scan (gate, receipt, ledger, seed, claim, pool, assignment, subagent, submission, owed, premise, control, round, owner, takeover, guard, JUnit, pre-fix): no hits in any of the four drafts.
