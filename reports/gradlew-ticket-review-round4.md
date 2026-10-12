# Review: gradlew ticket, round 4 confirmation

Verdict: **SATISFIED.** All three round 3 item 4 fixes are present in the current text and agree with the traces, the gradlew snapshot and the branch diff. The gradlew ticket text in `material/gradlew-ticket-draft.md` is final. It joins Nick's consolidated new-ticket email, which Nick sends. Nothing was filed in Jira and nothing was sent.

Scope: the three items of `assignments/pool-gradlew-ticket-review-round4.md`. Items 1 to 3 of round 3 are not re-opened.

## One line per item

1. **Source note date (draft line 5): CONFIRMED.** Line 5 says "the end-to-end run: the failing trace observed on main 498a09aea97, and the fix verified for wrapper startup on branch gradlew-msys-double-conversion at 192aacaaf89". It states no run date. The only other 2026-10-11 dates are a rule date (line 3, Nick's consolidated-email rule) and a Jira search date (line 7, duplicate check). Neither is a run claim. Line 44, "Confirmed by run", carries no date, and no other run date appears in the draft.
2. **Loop claim, draft line 51: CONFIRMED.** The line reads "The argument conversion loop at lines 222 to 242 runs once over {{-version}} without converting it; its {{cygpath}} call at line 231 is not reached." The traces agree:
   - `reports/gradlew-end-to-end-run/trace-fix.txt` lines 56 to 60 and `trace-repro.txt` lines 60 to 64 show `for arg in "$@"`, `case $arg in`, `false`, `shift`, `set -- -version`. The loop is entered once, the `-*` branch is false, and the `cygpath` call is skipped.
   - At `d76fc14b9857`, `gradlew` line 222 is `for arg do`, line 225 is `-*) false ;;`, line 231 is the `cygpath --path --ignore --mixed` call, and line 242 is `done`.
   - The Jira `{{ }}` markup is the draft's own code-span form. Lines 44 and 48 use it throughout, so the conversion is right for ticket text.
3. **Loop claim, run report line 33: CONFIRMED.** `reports/gradlew-end-to-end-run.md` line 33 reads "The argument conversion loop at lines 222 to 242 runs once over `-version` without converting it; its `cygpath` call at line 231 is not reached. The fix does not touch the loop." Both halves hold. The first half matches the traces, as in item 2. The second half matches the branch diff: `git diff d76fc14b9857 192aacaaf89 -- gradlew` is three deletions at lines 216 to 218 (the `APP_HOME` and `CLASSPATH` `cygpath` assignments and a blank line). Nothing at lines 222 to 242 changes.

## Note (not a finding)

After the fix the loop moves up by three lines, to 219 to 239 on the fixed branch. The draft's line 48 names the unfixed snapshot `d76fc14b9857` for these line numbers, and line 51 sits in the same section. No change is needed.
