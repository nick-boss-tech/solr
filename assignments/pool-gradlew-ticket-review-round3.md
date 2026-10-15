# Pool assignment: gradlew Jira ticket draft review, round 3 confirmation

Status: DONE

Capability tags: `review`. Intended host: windows. Staffing: 1. Claim path: claims/pool-gradlew-ticket-review-round3.md.

## Scope: three record fixes and the upgraded verification passages

Round 2 (`reports/gradlew-ticket-review-round2.md`) returned STILL OPEN with the draft `material/gradlew-ticket-draft.md` clean on all seven adjudicated items; the remaining findings were wording fixes in the supporting records. The main side applied the three fixes; the settlement record is the "Main-side settlement (2026-10-11)" section appended to `reports/gradlew-ticket-review-round2.md`.

Round 2 read the draft before its verification passages were upgraded from `reports/gradlew-end-to-end-run.md` (the end-to-end run Nick executed himself on the Windows Git Bash host). Its confirmations on items 3 and 5 and its closing verification-level paragraph describe the pre-upgrade text and are superseded, as flagged in the settlement. This round therefore has two parts: confirm the three fixes, and confirm the draft's current verification passages against the run report. Nothing else is in scope; do not re-open items round 2 confirmed against text that has not changed, and do not edit the draft or the reports; flag instead.

## Items to confirm

1. **Report wording, "exactly the string" (round 2 item 2).** `reports/gradlew-git-bash-path-bug.md`, the "Why the second call breaks" paragraph, now reads "That is the same shape as the failing trace". Confirm against the probe output in the same report (which carries the MSYS root segment after `C;`) and against the traced values in `reports/gradlew-end-to-end-run.md`: same shape, not the same string, in both comparisons.
2. **Report line 3, blocked-on-Windows wording (round 2 standing check).** The parenthetical "(no Gradle is allowed on this host)" is deleted; the sentence now reads "`gradlew` was not executed in this investigation, so the failing run itself is not reproduced here". Confirm no passage in the report states or implies that Gradle is blocked on Windows, in any wording. Statements that builds are not run on that host for pool work, and the record that this verification run was made by Nick himself, are not block claims.
3. **Adjudication wording (round 2 item 1 note).** `reports/gradlew-ticket-review.md` line 41, the F1 paragraph of the main-side adjudication, now says "The only `APP_HOME` conversion before the block is line 159". Confirm against the script at main `d76fc14b9857e77b830d7a82514b28c523a239c2`: line 153 also runs `cygpath`, on `GRADLE_TEMPDIR`, so the qualifier is required and the corrected sentence is true.
4. **Upgraded verification passages against the run report.** Every passage in `material/gradlew-ticket-draft.md` that states the verification level (the source note and the Reproduction section) matches `reports/gradlew-end-to-end-run.md` exactly: the command (`timeout 30 bash -x ./gradlew -version`), the two refs (main at `498a09aea97`; branch `gradlew-msys-double-conversion` at `192aacaaf89`), the traced `APP_HOME` and `CLASSPATH` values on both legs, the error line `Error: Could not find or load main class org.gradle.wrapper.GradleWrapperMain`, exit codes 1 and 0, Gradle 9.7.0 on the fixed leg, and the not-run list (check, build and any test task; Cygwin non-MSYS; `gradlew.bat`; Linux and macOS; the argument conversion loop at lines 222 to 242). The draft states the run's scope plainly as wrapper startup (`-version` only) and presents no passage as broader than the run. Confirm also that no passage in the draft claims Gradle is blocked on Windows, in any wording.

## Deliverable

`reports/gradlew-ticket-review-round3.md`, verdict first: SATISFIED or STILL OPEN, one line per item above (confirmed, or the exact passage and the evidence it disagrees with).

When this round lands SATISFIED, the gradlew ticket review is complete and the final text of `material/gradlew-ticket-draft.md` joins the one consolidated email the main side sends Nick carrying every new-ticket text, sent only once each included ticket has been completely reviewed. The earlier run-instructions step no longer applies: the end-to-end run has already been made by Nick and is recorded in `reports/gradlew-end-to-end-run.md`. The reviewer does not file anything in Jira, does not create the ticket, and does not email anyone.

Rules: WORKFLOW.md binds (claim before work, hourly heartbeats, subagent cap of 6 to 7 across all hosts, and mark the claim DONE in the same push as the deliverable). No em dashes in authored text.
