# Build, docs and misc round 1, part G6: live PR consistency and retire confirmations

Read-only. Main read at `3f5d4c5bf8ac`. All six heads were fetched with explicit refspecs and matched. GitHub was read with `gh pr list` and `gh pr view` only. Nothing was built, tested, posted, pushed or edited, and no file was written in the repo. No draft is written for this group. Condensed by the lead from the subagent's final report.

PR numbers seen: #5061 (`solr-18119-jvm`, OPEN); #5062 (`solr-18523-submit`, OPEN); #4999 (`solr-18119-submit`, OPEN); #5001 (`solr-18317-submit`, MERGED). No PR exists for `solr-12743-submit` or `solr-17825-submit`.

Headline: no retire basis has come back on main. `ConcurrentLRUCache` is absent. `commons-beanutils` is declared in no build file, and `kafka213` appears nowhere. The SOLR-18317 change is on main. The two retirements and the live-PR consistency all stand.

## SOLR-18119, `solr-18119-jvm` (PR #5061, OPEN): consistent with the record

- Refs: head `660faedd026d` matched (ls-remote and PR headRefOid). Merge-base `9becf6c6a154`, 7 commits on top. Main is 9 commits past the merge-base, none touching the branch's files.
- Diff: 10 files, +1172 and -895, matching the PR's counts. The port is in build-infra (`ChangesToHtml.java`, `ChangesToHtmlTask.java`, `PythonCompat.java`, `BuildInfraPlugin.java`, `build.gradle`, two test files). `changes2html.py` is deleted. No live `changes2html` reference remains in the tip tree.
- Test count: 11 plus 3 = 14, matching the receipt. Not run.
- PR line anchors checked on the tip; all match. The scan files are not in the diff, as the PR body says.
- Changelog `changelog/unreleased/SOLR-18119.yml`: type `other`, author Nick Shanin, no placeholder.
- PR state: OPEN, not draft, headRefOid `660faedd026d`, 10 changed files, no GitHub review yet. CI "gradle check" passed on that head; changelog and labeler checks passed.

Receipt disagreements:
1. `receipts/SOLR-18119.md:9` names two of the ten files as the build-infra pair. Build-infra holds seven; the other three are the changelog fragment, `changes-to-html.gradle` and the deleted script.
2. `receipts/SOLR-18119.md:10` says the output was compared. The PR body says the outputs differ in one line (the header comment). The receipt should carry that.
3. `receipts/SOLR-18119.md:7` says "the review cycle is complete". That is the internal paired review. GitHub shows no review on #5061. The receipt should say so.

PR text (wording, not rewritten):
- The Proof heading "Same output" sits above a sentence saying the Python output differs in one line. The heading contradicts the body.
- "counted from the JUnit XML" describes method, not a label. Low risk.
- A Jan Høydahl quote dated 2026-10-08 is not in the local Jira packet; the packet's last Jan comment is 2026-10-06. Check it against Jira before reusing it. The other two quotes match the packet.

Owner items:
1. Converter home: keep in build-infra as written (recommended), or move to a release tool (the PR body offers this). Smiley raised it on Jira (2026-10-07) and on #4999 (2026-10-08). Answer it in the PR before a maintainer asks.
2. Changelog fragment: keep (recommended). The #4999 reviewer dropped one for a release-manager-only script; #5061 changes the documentation build for everyone.
3. The "Same output" heading fix is a public PR edit and needs the owner's authorization. Recommendation: fix it with the next authorized edit.

Not checked: tests and builds (by rule); the SHA-256 and Windows check (the PR says Linux only); rendered GitHub line links; the 2026-10-08 Jira comment; CI log contents.

## SOLR-18523, `solr-18523-submit` (PR #5062, OPEN, APPROVED): consistent with the record, with a scope change the receipt does not record

- Refs: head `26678c3737ca` matched. Merge-base `9becf6c6a154` equals the PR's baseRefOid, 6 commits on top. No drift on the five touched files since the merge-base.
- Diff: 5 files, +24 and -359, matching the PR. Deleted: `dev-tools/scripts/checkJavadocLinks.py` (277 lines), `gradle/validation/check-broken-links.gradle` (81 lines of task definitions), and the `apply from` line at root `build.gradle:181`. Added: a comment and a CI-only condition in `gradle/documentation/documentation.gradle` (lines 74-90): `if (isCIBuild) { check.dependsOn 'documentation' }`. **Local `:solr:documentation:check` no longer builds the site outside CI.** The PR body says so; the receipt does not.
- The tip tree has no reference to `checkBrokenLinks`, `checkJavadocLinks` or `check-broken-links` apart from the changelog title. The PR's claim that nothing runs `checkBrokenLinks` by name holds for the tip.
- Commits after the planted-run parent (`c0a3972d667`) change only a comment in `documentation.gradle` and the changelog title.
- PR anchors checked (`javac.gradle`, `documentation.gradle`, `solr-ref-guide/build.gradle`, `globals.gradle`, `gradle-precommit.yml`, `render-javadoc.gradle`, `index.template.md`, `checkJavadocLinks.py:252-257`). The quoted text matches. The PR cites `changes-to-html.gradle` 71-77; the warn-and-skip is at 71-74 and the `pythonExists` check at 108 (main).
- Anchor follow-up: #5062 says the companion "offers anchor checking as a follow-up change; it is not in either PR." #5061 says a follow-up "can restore the anchor check." Named, with no plan or owner. The 185,623 links and 0 broken measurement is in #5061's body, not #5062's.
- Changelog `changelog/unreleased/SOLR-18523.yml`: type `other`, author Nick Shanin; title equals the PR title.
- PR state: OPEN, not draft, APPROVED, 5 changed files. CI "gradle check" passed on that head; changelog check passed.

Receipt disagreements:
1. `receipts/SOLR-18523.md:5` describes a deletion of `checkJavadocLinks.py` and its wiring. The wiring is also in `check-broken-links.gradle`, and the branch adds a CI-only condition to `documentation.gradle`. The receipt omits the behavior change.
2. `receipts/SOLR-18523.md:6` cites the 185,623 measurement as the branch's. It is cited in #5061, not in this PR.

PR text (wording, not rewritten):
- "gate" is used as a process term about six times in the body (for example "The gate runs are at head 26678c3737c"), and in the `documentation.gradle` comment ("The gate covers this project's check path only"). The ban is on "gate" as an internal label; here it is a risk.
- "Record: reviews/solr-18523-doclint.md" links a fork file at commit `f11c126fd9d4`. That is internal review material in public text.
- "Happy to open a follow-up ticket" is an offer. No new Jira ticket unless someone asks.

Owner items:
1. The local-check change. (a) Keep as written, following Lucene #14905 and #15350, as the PR already asks maintainers (recommended, and record it in the receipt as a behavior change); (b) keep this project's check building the site everywhere (the 4m18s scan goes either way); (c) keep a smaller scan for pages outside the ref guide.
2. Anchor follow-up. (a) One sentence naming a plan (which PR, by whom) (recommended). Fragment anchors are the one check this removal loses. (b) Leave as is.
3. Public text edits (the "gate" wording, the Record link, the ticket offer) need explicit authorization. Recommendation: fix the Record link and the "gate" uses before maintainers reply.

Not checked: tests and builds (by rule); the Lucene PRs and planted-error runs (main-side records); the 185,623 measurement itself; rendered pages; CI logs beyond the check result.

## SOLR-18119, `solr-18119-submit` (PR #4999, OPEN, CHANGES_REQUESTED): retire candidate, confirmed as superseded on the record

- Refs: head `723022d35fce` matched. Merge-base `0d2a4649c79a` (older than the jvm branch's). PR baseRefOid `99cd97296ed6` is an ancestor of main. `changes-to-html.gradle` is unchanged on main since that base, so the PR applies cleanly.
- Diff: 1 file, +10 and -9. The `py3Executable` name is used consistently in the tip. The changelog entry is gone, as the PR says.
- PR state: OPEN, not draft, `reviewDecision` CHANGES_REQUESTED (janhoy, 2026-10-05, on commit `2da46c22bf2`). Nick replied on 2026-10-05 that both requests were done. No re-review since. CI passed on this head.
- Thread: janhoy (2026-10-06) "I implemented it in #5034 (based on this PR)". dsmiley (2026-10-08) "I wonder if we want gradle to invoke Python at all. The details of this PR then become moot." The thread never mentions #5061.
- **Two facts the record leaves out:** the PR is still OPEN with changes requested standing, and #5034 (not in this round) is based on it. GitHub still shows two open PRs for SOLR-18119 until #4999 closes.

Receipt disagreements:
1. `receipts/SOLR-18119.md:17` says "verified by a clean root project configuration run". The PR body describes task runs instead: `:solr:documentation:changesToHtml` with a nonexistent `python3.exe` fails with the new message, and the base script succeeds with a warning (dated 2026-10-04). The method differs.
2. `receipts/SOLR-18119.md:15` says "superseded by solr-18119-jvm on 2026-10-08". The supersession is in Jira and in #5061's body, which calls #4999 "a different route". The #4999 thread is silent. Not wrong; the receipt should say so.
3. `receipts/SOLR-18119.md:17` omits that #5034 depends on this PR and that a maintainer has questioned the Python route itself.

Owner items:
1. Close #4999 now? (a) Close now with a pointer to #5061 (a public action). (b) Hold open until maintainers pick a route (recommended): closing now strands #5034's context while the maintainer question is open. (c) Close after #5034 is settled.
2. Branch deletion: keep `solr-18119-submit` until #4999 closes, since its head is the branch tip (recommended).

Not checked: builds and tests (by rule); the 2026-10-04 controlled run itself; #5034; whether a configuration-only run happened.

## SOLR-18317, `solr-18317-submit` (PR #5001, MERGED): merged state confirmed; retire candidate

- Refs: head `fadbaee999f1` matched. Merge-base `0d2a4649c79a`. Merge commit `e432df19c4a5` (squash, single parent `0cc328310f8f`) is an ancestor of main.
- PR #5001: MERGED 2026-10-05, mergeCommit `e432df19c4a5`, headRefOid equal to the tip, 5 changed files.
- The merge commit's diff against its parent has the same five files and the same counts (146 and 8) as the branch's diff against its base. Blob IDs at the merge commit equal the tip for the changelog, `AdminUiLoggingStandaloneTest.java`, `app.js` and `controllers/logging.js`.
- **`services.js` is not byte-identical to the tip.** The merged file keeps main's `ConfigV2` factory where the branch base had `Config`. The branch's own +/- lines in `services.js` and `app.js` match the merged diff.
- On main today, `logging.js` is unchanged since the merge, and the nodes=all standalone guard is present (`logging.js:164-173`). Later main commits changed `app.js` and `services.js` (SOLR-9759, SOLR-18400, SOLR-18450). Those are not this branch's changes.
- Jira SOLR-18317: status Open. The merge did not close the ticket; the banked server-side variant remains.

Receipt disagreements: none on the merged state. The receipt does not record that the Jira ticket is still Open.

Owner items:
1. Delete `solr-18317-submit`: keep as a record until the server-side variant (Core admin's) is decided (recommended). No dependency either way was found.
2. Jira SOLR-18317 is Open while its Admin UI half is merged: leave as is (recommended). No Jira comment without the owner's go-ahead.
3. Do not use this tip as a base for Admin UI work. Its `services.js` is not what main has.

Not checked: the server-side variant (not touched, by rule); the `solr-18317-ready` companion; CI on the merged PR; Jira comments.

## SOLR-12743, `solr-12743-submit`: retire confirmed

- Refs: head `1bb4b227dfe5` matched. Merge-base `86bc6f292245`.
- Branch: 2 files. `solr/core/src/java/org/apache/solr/util/ConcurrentLRUCache.java` (the patch) and `OPEN-QUESTIONS-SOLR-12743.md` (11 lines, whose own commit says "remove before the PR").
- Main: `git cat-file -e 3f5d4c5bf8ac:solr/core/src/java/org/apache/solr/util/ConcurrentLRUCache.java` reports absent. No path on main contains ConcurrentLRU, and no Java file references `ConcurrentLRUCache`.
- Deletion commit on main: `ec218b20cc1`, "Replace ConcurrentLRUCache and Cache interface with Caffeine (#4516)". This matches the receipt's upstream reference.
- Caffeine-based caches are on main (for example `BlockCache.java`), which is the target for the fresh-investigation question.
- No PR exists for this branch. Jira SOLR-12743: status Open, last updated 2020-01-07. The retirement is internal; the ticket is still Open.

Receipt disagreements: none. `receipts/SOLR-12743.md:6` matches the main-side facts.

Owner items:
1. Delete `solr-12743-submit`: at the owner's call (recommended to delete). The patch cannot apply to any current base. Agents do not delete branches.
2. Fresh investigation against the Caffeine caches: (a) close the ticket as obsolete (a Jira write, needs authorization); (b) open a fresh investigation with the heap-dump approach against `CaffeineCache`; (c) leave Open. Recommendation: (c) until someone reproduces the leak on current main; then (b) if it reproduces, or (a) if not. No new Jira ticket unless asked.

Not checked: whether the leak persists on Caffeine (no run); the reporter's 2019 patch claim; upstream PR #4516 itself (confirmed by commit subject only).

## SOLR-17825, `solr-17825-submit`: retire confirmed; one receipt cause is wrong

- Refs: head `0ef08ec86c65` matched. Merge-base `86bc6f292245`.
- Branch: 2 files. `solr/cross-dc-manager/build.gradle` (+31: a commons-beanutils 1.11.0 constraint and a `verifyCommonsBeanutilsFloor` task wired into check) and `SOLR-17825-DECISION.md` (+21).
- Main `solr/cross-dc-manager/build.gradle` (97 lines): the dependencies block has `kafka.clients` at line 40 and no kafka213 or kafka streams line.
- `git grep -i beanutils` on main over the gradle, kts and toml files: no hits. `git grep kafka213` on main: no hits.
- The branch base had kafka213 (line 45), streams (line 46) and a test kafka213 (line 80). Commit `8c750e25b11`, "SOLR-18300 : Update apache.kafka to V4 (#4610)", removed them.
- Beanutils was not declared in the base tree either.
- Jira SOLR-17825: status Resolved, fix version 10.1, summary "Upgrade commons-beanutils jar to 1.11.0+ to fix CVE-2025-48734".
- No PR exists for this branch.

Receipt disagreements:
1. `receipts/SOLR-17825.md:6` says "(the Kafka 4 upgrade removed them)" after "any beanutils dependency". **The Kafka 4 upgrade removed kafka213 and streams, not beanutils.** No beanutils dependency existed in cross-dc-manager or in any build file at the base, so that upgrade removed none. The retirement still holds on "no beanutils declaration on main" and the Resolved ticket. The cause wording is wrong.
2. `receipts/SOLR-17825.md:6` says "the branch's own guard task would fail check on main now." Not verified. The guard throws only if commons-beanutils is absent from the runtime classpath, and transitive resolution was not run.
3. `receipts/SOLR-17825.md:7` names "the optional SOLR-18300 Jira link". SOLR-18300 is the ticket of `8c750e25b11`, the commit that removed kafka213, so the link has a real target.

Owner items:
1. Delete `solr-17825-submit`: at the owner's call (recommended to delete); it cannot apply to main.
2. The SOLR-18300 link on the Jira ticket: add it only if the owner wants the cause recorded (a public Jira write, needs authorization). If added, describe it as the Kafka 4 upgrade, not as a beanutils removal.

Not checked: transitive presence of commons-beanutils on the runtime classpath (no Gradle); the guard run; the SOLR-18300 Jira page (not in the packet); CVE details.
