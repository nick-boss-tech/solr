# Pool assignment: gradlew Jira ticket draft review

Capability tags: `review`. Intended host: windows. Staffing: 1. Claim path: claims/pool-gradlew-ticket-review.md.

## Material under review

`material/gradlew-ticket-draft.md` on this branch: a draft Bug ticket (title plus Jira wiki markup description) for the defect that `gradlew` converts `APP_HOME` and `CLASSPATH` with `cygpath --path --mixed` twice on the Cygwin and MSYS path, so the Gradle wrapper main class is not found under Git Bash. The evidence is on this branch at `reports/gradlew-git-bash-path-bug.md`, and the script itself can be read at the cited snapshot.

## Verification evidence (reports/gradlew-git-bash-path-bug.md, CAUSE CONFIRMED by code reading and probes)

Verified against main d76fc14b9857e77b830d7a82514b28c523a239c2 (the report's earlier ref 3f5d4c5bf8a has the identical lines in `gradlew`, `gradlew.bat` and `gradle/validation/`). All line numbers are in the root `gradlew`:

- Line 89: `APP_HOME` in POSIX form (`/c/Users/...`). Line 159: `APP_HOME` converted with `cygpath --path --mixed` (first conversion, correct). Line 162: `GRADLE_WRAPPER_JAR` built from the converted `APP_HOME`. Line 174: `CLASSPATH=$GRADLE_WRAPPER_JAR`, already in Windows form.
- Line 215: the Cygwin and MSYS block (`if "$cygwin" || "$msys"`). Lines 216-217: the defect, a second `cygpath --path --mixed` on `APP_HOME` and `CLASSPATH`. `cygpath --path` splits its input on `:`, so `C:/Users/...` becomes `C;C:/Users/...`, the exact string in the failing trace. Line 219: `JAVACMD` converted once with `cygpath --unix` (correct). Line 256: the final exec passes `-classpath "$CLASSPATH"`.
- Probes on the Windows Git Bash host: `cygpath --path --mixed "/c/Users/shaninna/dev/Solr-issues/wt/pr-prepare-suggester"` prints `C:/Users/shaninna/dev/Solr-issues/wt/pr-prepare-suggester`; the same command on that output prints `C;C:/Users/shaninna/AppData/Local/Programs/Git/Users/shaninna/dev/Solr-issues/wt/pr-prepare-suggester`, the trace's shape.
- `gradlew` was NOT executed: Gradle is blocked on the Windows host. The verdict rests on the line reads and the probes, which reproduce the trace's strings. Not verified in the report: the failing run itself; Cygwin (non-MSYS); the POSIX form of `APP_HOME` at line 89 is inferred from the code and the trace; the argument loop at lines 220 to 235 runs `cygpath --path` on POSIX-looking arguments and a colon in one would split the same way, which is outside the reported trace and outside the fix.
- Proposed fix (text only in the report, not applied anywhere): delete lines 216 to 218, keep line 219. Checks recorded in the report: the edited script passes `bash -n`; `gradle/validation/gradlew-scripts-tweaked.gradle` line 35 requires the text `GradlePropertiesGenerator` in `gradlew` and `gradlew.bat` (SOLR-16641), and its only occurrence in `gradlew` is line 179, untouched by the deletion; `gradlew.bat` has no `cygpath`, `cygwin` or `msys` text and normalizes `APP_HOME` once (set at line 33, normalized at line 36), so no equivalent defect; Linux and macOS never enter the block (lines 107 to 115 default `cygwin`, `msys` and `darwin` to false and set them only for `CYGWIN*`, `MSYS*` or `MINGW*`).
- Reproduction spec (report): Git for Windows Bash (MINGW64), JDK 21 with `JAVA_HOME` set, `timeout 30 bash -x ./gradlew -version`, judging the trace rather than the exit code. The fixed trace shows `APP_HOME=C:/Users/...` from one conversion, a `CLASSPATH` with no `C;` prefix, and no "Could not find or load main class" line.
- Duplicate check (main side, 2026-10-11, read-only Jira search): no existing ticket covers this defect. SOLR-14955 is the only `cygpath` hit and covers the Prometheus Exporter bin scripts. SOLR-16641 is the only `GradleWrapperMain` hit and is a circular task dependency in localSettings. The "Could not find or load main class" hits concern `bin/solr` or `bin/post` classes, not the Gradle wrapper.

## Review criteria

Check the draft in `material/gradlew-ticket-draft.md` against the evidence above and the standing text rules:

1. Title accuracy: the title states only what the evidence supports (gradlew, Git Bash and MSYS, the "Could not find or load main class org.gradle.wrapper.GradleWrapperMain" symptom, and the double cygpath conversion of `APP_HOME` and `CLASSPATH` as the cause).
2. Every line reference in the ticket matches the report and the script at d76fc14b9857: 89, 159, 162, 164, 174, 179, 215, 216 to 218, 219, 256.
3. The verification level is stated honestly and never overstated: the cause is confirmed by reading the script and by the cygpath probes; the failing run itself was not executed, because Gradle is blocked on the reporting host; the ticket does not claim a reproduced run anywhere.
4. The not-verified items are stated as such and kept out of the fix: Cygwin (non-MSYS) not checked separately, and the argument conversion loop at lines 220 to 235 named as a related item outside the reported failure.
5. The fix is stated completely: delete lines 216 to 218, keep line 219, with the reason nothing else changes (`APP_HOME` converted at line 159, `CLASSPATH` built from it at line 174, the WrapperDownloader at line 164 and the GradlePropertiesGenerator at line 179 receiving the same paths as today), the SOLR-16641 validation text check still passing, and `gradlew.bat` having no equivalent defect.
6. Jira wiki markup is correct throughout: code spans in `{{...}}`, bullets starting with " * ", headings `h2.`, literal blocks in `{noformat}`. No GitHub markdown anywhere in the ticket text.
7. Disclosure form: the AI header is in the Jira underscore form, `🤖 _AI text below_ 🤖 _(posted on behalf of Nick Shanin)_`, at the top of the description, and the AI assistance footer ("AI agents assisted with research, implementation, review, and drafting. Nick Shanin directed the work and takes responsibility for this contribution.") is the last element of the description.
8. No internal process vocabulary in the ticket text (no gate, receipt, ledger, claim, or review-round language), and no "we" for Nick plus agent.

## Fixes, deliverable, and limits

Fix wording and markup problems directly in `material/gradlew-ticket-draft.md`. Anything where the draft and the evidence disagree on a fact (a line number, the probe outputs, what was or was not run) is flagged in the report, not silently fixed.

Deliverable: `reports/gradlew-ticket-review.md`, with the verdict first: SATISFIED or STILL OPEN. List each fix made (before and after text), each evidence disagreement flagged, and the criteria checked clean.

Nick creates new Jira tickets himself. When this review lands SATISFIED, the final text of `material/gradlew-ticket-draft.md` joins the one consolidated email the main side sends Nick carrying every new-ticket text (the updated mergeIds text, this gradlew ticket, and the CombinedQueryComponent ticket if its premise confirms and it needs its own ticket), sent only once each included ticket has been completely reviewed. The reviewer does not file anything in Jira, does not create the ticket, and does not email anyone.

Rules: WORKFLOW.md binds (claim before work, hourly heartbeats, subagent cap of 6 to 7 across all hosts, and mark the claim DONE in the same push as the deliverable). No em dashes in authored text.
