# CLI, bin scripts and packaging round 1, part S3: script parsing and Windows construction (SOLR-17029 and SOLR-17598)

## Per ticket

**SOLR-17029: draftable.** Evidence shape: BATS (a script-only change, no Java).
- Draft `pr-drafts/cli/SOLR-17029.md`, 4,857 characters with link targets (3,916 without them).
- Head check: `git ls-remote origin refs/heads/solr-17029-submit` returned `4a98ef0a89d16ec5a645a1c38fbe25dc28c87e62`, which matches the assignment and the receipt. The commit is present locally, so no fetch was needed.
- Base citations use the merge-base with `upstream/main`, `14c7aac0d151402b00259e2fb9bf5eed7049ec5d`.

**SOLR-17598: draftable by construction only.** Evidence shape: construction proof. The draft says the change is verified by construction and that the Windows run is still to do.
- Draft `pr-drafts/cli/SOLR-17598.md`, 4,388 characters with link targets (3,485 without).
- Head check: `ls-remote` returned `8c91cf047a96fee86dd6164b81715d1ed51ebfc4`, which matches.
- Base citations use the merge-base, `97d973814336101e12475558d7419321c743de79`.

## Construction argument for SOLR-17598

Line numbers are at the head; base line numbers are in brackets.

1. **Percent expansion before block boundaries: holds.** Base line 896 (`IF [%SOLR_LOGS_DIR%] == [] (`) and line 899 (`set SOLR_LOGS_DIR=%SOLR_LOGS_DIR:"=%`) put the value into block text. The new block (line 897 `IF NOT DEFINED`, line 900 `set "SOLR_LOGS_DIR=!SOLR_LOGS_DIR:"=!"`) carries no value text. The only percent expansion left in the block is `%SOLR_SERVER_DIR%` inside a quoted set (line 898), so parentheses in that path stay inside quotes.
2. **Delayed expansion for the script body: holds.** Line 21: `IF "%OS%"=="Windows_NT" setlocal enabledelayedexpansion enableextensions`. The condition is true on every Windows NT system. The script already uses `!...!` at line 906 and line 1174.
3. **Other bracket idiom:** only line 375, `IF [%1]==[] goto run_special_command`, the same at base. It tests `%1`, not a path, sits outside any block, and is unchanged. No other bracket test on a path variable exists.
4. **The post-strip empty check does not restore the old defined-but-empty defaulting.** Base never defaulted a quote-only value. `[""]` is not equal to `[]`, so base takes the ELSE branch, and `set X=` clears the variable without setting a default. The new check at line 903 gives such a value the default. This is new behavior for that case, and the draft states it as a behavior change.
5. **Undefined and plain values: same results as before.** An undefined variable gets the default through `IF NOT DEFINED`, as the bracket test did. A plain value is stored unchanged.
6. **Later uses of `SOLR_LOGS_DIR`** (lines 1110 to 1174) are all inside double quotes or delayed expansion. Confirmed.
7. **Stop path: confirmed.** For stop, line 805 does not jump to start. The `:stop_solr` code (lines 808 to 876) never reads `SOLR_LOGS_DIR`. Line 876, `IF "!IS_RESTART!"=="0" goto done`, leaves the script before the start label at line 881. A restart reads the block, because `IS_RESTART=1` falls through.
8. **Not settled by reading:** the receipt's claim that the old test "splits even a quoted spaced value". The part could not confirm it from the cmd parse rules. The draft does not depend on it.
9. **Not covered by the argument:** values containing `!` or `^`. The draft says so.

## Self-check

- Both drafts: zero em dashes and zero en dashes (byte scan).
- Process words (gate, receipt, ledger, rc=0, JUnit XML, pre-fix, owed, round, draftable, PR-ready, premise, control, claim, handled): none in either.
- Head references: every blob link uses a full 40-character SHA. The 17029 draft has 6 links to the head `4a98ef0a89d16ec5a645a1c38fbe25dc28c87e62` and 2 base links to `14c7aac0d151402b00259e2fb9bf5eed7049ec5d`, labeled base. The 17598 draft has 7 links to the head `8c91cf047a96fee86dd6164b81715d1ed51ebfc4` and 1 base link to `97d973814336101e12475558d7419321c743de79`, labeled base. No short SHAs appear. Every cited line number was spot-checked against the blob content.
- Changelog links point at the head SHA. The 17598 changelog file is `SOLR-17598-solr-cmd-logs-dir.yml`, so the draft uses that name, not the formula's `SOLR-<ticket>.yml` pattern.
- Length: both drafts exceed the guide's roughly 3,500 characters with links. Without the link targets, 17029 is 3,916 and 17598 is 3,485.

## Receipt and assignment disagreements

1. **Assignment, starting state for 17029 and the Interactions entry:** "the `-a`/`--jvm-opts` path go through the same parser". At head there is no `-a` option. The only `-a` tokens in `solr/bin/solr` are the `read -a` builtins (lines 484 and 487). The only route is `--jvm-opts` (lines 866 to 872), and `start_solr` is called with `ADDITIONAL_CMD_OPTS` at line 1482. The shared parser is used at line 741 for `SOLR_OPTS` and at line 1282 for `--jvm-opts`. The draft names `--jvm-opts` only, and says the ticket's `-a` example no longer applies.
2. **Assignment:** "the split honors quotes and backslash escapes only before whitespace, quotes and backslashes". This holds outside quotes. Inside double quotes the escapes are `"`, `\`, `$` and backtick (lines 687 to 737). The draft states both.
3. **Receipt, 17029:** test 12 "passes on both trees, since it pins the behaviour against the eval route the branch replaced rather than against main". The result is the same, but the reason is different. The base code (line 681, unquoted expansion) never re-expands dollar signs, so test 12 guards behavior main already has. The eval route exists only in the intermediate commit `5df6b6acb1d`, not in base. The draft uses the second reason.
4. **Receipt, 17598:** "restores the old defined-but-empty defaulting" and "undefined, plain, and defined-but-empty values behave exactly as before". Base does not default a quote-only value (see item 4 of the construction argument). The new check adds that default. The draft says: "Before this change, it was cleared and no default was set."
5. **Receipt, 17598:** "splits even a quoted spaced value". Not confirmed by reading. The draft does not rely on it.
6. **Receipt, 17029:** "First verification (2026-10-04, log `g17029-bats2.log`)" with "Verified head: `4a98ef0a89d1`". The head commits are timestamped 2026-10-04 14:22 to 14:24 UTC, the same day as the run. The logs are not in this worktree, so the part could not confirm the run was made at `4a98`. The draft gives the counts as the receipt does, with the date.
7. **Changelog for 17598** (commit `64170deffc5`): the title says the tool "no longer fails to parse a `SOLR_LOGS_DIR` environment variable whose value contains spaces or parentheses." The change is verified only by construction, and the title states a fixed behavior. Flagged for the owner.
8. **Receipt, 17029:** "The one deviation in that run, the pre-existing test 5". Per the instruction, the draft says nothing about test 5. Owner decision 1 below.

## Owner decisions

1. **17029 Proof:** publish only the 2026-10-04 counts (as drafted), or also state the 2026-10-08 result? The 2026-10-08 run included the test 5 failure that the draft leaves out.
2. **17029 Choices:** no Choice section was drafted, because the alternatives were ruled out on correctness grounds (eval rewrites `$` and backticks; a wider escape set would break Windows paths). Confirm, or add one.
3. **17029 Limits:** keep the `-e` example path, the trailing-backslash and UNC cases as Limits (as drafted), or fix them before opening.
4. **17598 post-strip check:** keep it (quote-only values now get the default, stated as a behavior change), or drop it to match base exactly.
5. **17598 changelog title:** keep "no longer fails" before the Windows run, or soften it.
6. **17598 "variable ignored" report:** `solr.cmd` lines 907 to 908 at head unconditionally set `SOLR_LOGS_DIR` when `SOLR_HOME` contains the example directory (base line 905 has the same line). This may explain the report, but it has not been verified. Check before making any claim about that report.
7. **17598 timing:** run the Windows confirmation first (a spaced, parenthesized `SOLR_LOGS_DIR`, then `bin\solr.cmd start`), or open with the Limits as drafted.
8. **Length:** both drafts run long only because of full-SHA links. Accept, or shorten.

## Not checked

- No builds, Gradle, tests or BATS runs. The parser and `cmd.exe` behavior were checked by reading the code only, not executed.
- Run logs (`g17029-bats2.log`, `g17029-control.log`, `g17029-reverify.log`, `g17029-rerun-head.log`, `g17029-rerun-control.log`) and the 17598 review notes are not in this worktree. The counts come from the receipts.
- No Windows run. Not checked: `!` and `^` in values, and the exact parse of a quoted spaced value in the old test.
- The `-e` example path: only `RunExampleTool.java` lines 820 to 880 were read. The wrapper sends the value with outer double quotes through commons-exec. How that is handled on each OS was not verified.
- The 9342 and 17029 composition on the launch line, and `solr.cmd` parity for 17029, were not audited. They are outside this part's two tickets.
- Line endings: `solr.cmd` has no CR bytes in git at base or head. Whether checkout converts them was not checked.
- Jira: read-only access to `research/jira-context/SOLR-17029.json` and `SOLR-17598.json`. Nothing was posted.
- One command left a copy of `solr8c.cmd` in the system temp folder. It was removed. All other temporary files are in the scratchpad.
- Only the two draft files were written. No commits, pushes, posts or `gh` write calls.
