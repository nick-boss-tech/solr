# GitHub comment draft: PR 5222 follow-up (archive check result)

Status: DRAFT (not posted). This is the report-back Nick promised on the PR at 2026-10-24T19:24:01Z. Evidence: `reports/vm5-release-tarball-dev-tools-check-round-1.md` and its addendum. Posting needs Nick Shanin's explicit go.

---

🤖 *AI text below* 🤖 *(reviewed by Nick Shanin)*

Following up on the `.gitattributes` question, with the archive check run.

I took this PR's head, added `/dev-tools export-ignore` beside `/dev-docs` in a local commit (not pushed), and made both archives with `git archive`. The listings differ by exactly 39 entries, all under `dev-tools/`; nothing else is missing from the scratch archive.

From the unpacked scratch tree (no `.git`):

* `./gradlew rat` passes (36 RAT tasks). The only `dev-tools` reference in the build is this PR's own exclusion in `rat-sources.gradle`, which does not need the directory to exist.
* `./gradlew check --continue` fails on two tasks, neither involving `dev-tools`. `:allHelpFilesExit` wants `dev-docs/gradle-help/workflow.txt`; `dev-docs` is already export-ignored at this head, so the unmodified tarball has the same gap (that part is inferred from the listing and the script, not run). And `:solr:core:test` fails the same five methods that fail on a checkout in the 10.1.0 RC3 smoke runs here (`TestTlogReplica` x3, `CoreAdminHandlerTest.testDeleteInstanceDirAfterCreateFailure`, `TestCoordinatorRole.testNRTRestart`), on one small ARM host.

So on this evidence, adding `/dev-tools` to `.gitattributes` does not change the ability to build or check the source release. The one real tarball gap the run exposed is the help-file check against the already-export-ignored `dev-docs`, which exists with or without the `dev-tools` line.
