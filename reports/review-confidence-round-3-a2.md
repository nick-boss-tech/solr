# Review confidence round 3, slice A2

Scope: live PRs #5014, #5015, #5016, #5028, #5029, #5030, #5031, #5061, #5062 on apache/solr, checked against each fork branch tip, the receipt on origin/pr-prepare, and the code at the head. Read on 2026-10-10. Method: read-only gh.ps1 calls (pr view, reviews, comments, check runs), git show, sed, and grep at the head. No builds, tests, Gradle, Selenium, or gate runs. No fetch was needed: every head commit was already in the local clone and matched ls-remote. No PR, branch, or Jira write.

Receipts: all eight tickets have one on the tip (SOLR-15823 covers #5030 and #5031). None is missing.

## Verdicts

Counts: CONSISTENT 0, DRIFT 9, UNREAD 0.

| PR | SOLR ticket | State | Head (equals fork tip) | Verdict |
|---|---|---|---|---|
| #5014 | SOLR-13568 | Open, not draft | ac5d60c214c | DRIFT |
| #5015 | SOLR-13706 | Open, not draft | 590dd5c24d9 | DRIFT |
| #5016 | SOLR-13097 | Open, not draft, changes requested, merge conflict (DIRTY) | f0e7395f58f | DRIFT |
| #5028 | SOLR-18505 | Open, not draft | e28739b4069 | DRIFT |
| #5029 | SOLR-18506 | Open, not draft, CI run at this head failed | 77c019e1ff0 | DRIFT |
| #5030 | SOLR-15823 | Open, not draft, merge state clean | 1f60cd39baa | DRIFT |
| #5031 | SOLR-15823 | Open, not draft, merge state clean, stacked on an earlier #5030 head | 6fa54c4de8c | DRIFT |
| #5061 | SOLR-18119 | Open, not draft, merge state clean | 660faedd026 | DRIFT |
| #5062 | SOLR-18523 | Open, not draft, merge state clean, approved | 26678c3737c | DRIFT |

No PR is merged or closed. Every DRIFT below names the exact line, the fact it conflicts with, and the fix. Tags: [SHAPE] formula shape, [PROOF] Proof number not in the receipt, [CITE] citation link, [FIT] cited lines do not show the claim, [FACT] state or fact, [VOCAB] internal vocabulary, [PROCESS] process narration, [NOTE] for the lead to decide.

## Shared findings (apply to the PRs named)

- S1 [SHAPE] Bold one-line summaries. #5014, #5015, #5016, #5028, #5029, #5030, #5031 have no bold line under any section heading. #5061 and #5062 have one per section. Fix for the seven: put one bold claim line under each `##` heading and keep the evidence below it.
- S2 [NOTE] Length guide (about 3,500 characters). Over: #5016 (4,625), #5028 (5,295), #5029 (3,971), #5031 (7,786), #5061 (9,862), #5062 (11,981), #5030 (14,262, a multi-choice big PR that the big-PR amendment allows). Within: #5014 (2,673), #5015 (3,272).
- S3 [NOTE] Plain language: long multi-clause sentences in the Limits of #5030, #5031, and #5062. Wording only.
- S4 Lucene versions: no body names a Lucene version, so the version check does not apply. #5061 and #5062 cite Lucene PRs only.
- S5 Receipt issues for the main side (not body defects): receipts/SOLR-13097.md says "The PR was closed out on 2026-10-06", but #5016 is OPEN. Its counts are CoreScopedAuthStandaloneTest 3 of 3 at 38e306c2cd5, while the live head is f0e7395f58f.

## Findings per PR

### #5014 (SOLR-13568): DRIFT

Title accurate. No citation links in the body. Verified at head: ExpandComponent.java lines 437 to 441 wrap the page group query in a WrappedQuery with setCache(false) before it is added to the filter list.

- [SHAPE] S1 applies.
- [SHAPE] Choice section has no question. Exact line: "...but if maintainers would rather keep cache hits available for repeated identical page requests, the switch is the route to take." The formula ends the section with a pointed question. Fix: add "Was always-off the right call?" or similar.
- [VOCAB] Proof line: "Verified at head ac5d60c214c on 2026-10-06 (local gate: tidy clean, ...". "gate" is internal vocabulary. Fix: "(tidy clean, Error Prone compile clean, ...)".
- [PROOF] "first verified at head 6b89f649071 on 2026-10-04" is not in receipts/SOLR-13568.md, which records only the 2026-10-06 gate at ac5d60c214c. Fix: remove the clause, or the main side adds it to the receipt.
- [NOTE] What happens today: "...fills the filter cache with one-off entries that evict useful ones." The test checks only the cache size (expected:<3> but was:<5>), so eviction is not shown. Fix: "...fills the filter cache with entries that are rarely reused."
- [NOTE] Limits: "For a popular first page that recompute is a real, recurring cost" sits beside "The cost ... was not benchmarked." Fix: "is a recurring cost that has not been measured."

Proof traced: TestExpandComponent 9 of 9; base failure on testPerPageGroupQueriesNotCached with expected:<3> but was:<5>; date and head match the receipt.

### #5015 (SOLR-13706): DRIFT

Verified at head: PluginInfo.writeMap groups children by child.type with computeIfAbsent (PluginInfo.java lines 211 to 216). The base SolrConfig highlight skip (base SolrConfig.java lines 986 to 987) is gone. Changelog type is "changed". The upgrade note is in major-changes-in-solr-11.adoc under "API Changes". The named tests exist in PluginInfoTest and TestConfigHighlightOutput.

- [TITLE] Current title: "SOLR-13706: Config API output for the "highlight" searchComponent is no longer dropped". It names only the highlight symptom. The change re-keys every plugin's children by type, which the changelog and Limits both state. Fix: "SOLR-13706: Key Config API plugin children by type so unnamed children serialize and highlight is no longer dropped".
- [PROOF] "On base code the new cases fail (8 failing test executions under randomization)" and "(4 failing test executions)" are not in receipts/SOLR-13706.md. Fix: drop the counts, or add them to the receipt.
- [PROOF] "the same test counts also passed on the fork's GitHub Actions test runner at head b062f3b1ffd on 2026-10-04". The receipt records one Actions run (37568486924) for PluginInfoTest at 590dd5c24d9. It does not record b062f3b1ffd, 2026-10-04, or the other two classes. Fix: "PluginInfoTest also passed on the fork's GitHub Actions runner at this head."
- [SHAPE] S1 applies.
- [NOTE] Proof bullet for TestSolrConfigHandler: "it navigates into a plugin's children in the /config output by the child's type, matching the new keying." The branch does not change that test, and I did not confirm type-based navigation in it. Fix: say the test passes unchanged, or cite the lines that do type lookups.
- [NOTE] Limits: "no code reads the serialized children by name (the Admin UI JavaScript, SolrJ, and the core tests were searched)". I spot-checked the Admin UI and SolrJ only, not all of the core tests.

Traced: 11 of 11, 1 of 1, 8 of 8 match the receipt counts.

### #5016 (SOLR-13097): DRIFT

Verified at head: HttpSolrCall.getAuthorizationCollectionsList (lines 219 to 236) returns the serving core name in standalone mode and is unchanged in cloud mode. RuleBasedAuthorizationPluginBase.authorize returns the first collection that has a governing permission (lines 111 to 124), as Limits says. BasicAuthPlugin blockUnknown defaults to true (line 54). The 10.2 upgrade note and the rule-based page sentence are present. The changelog type is "fixed". CoreScopedAuthStandaloneTest has five test methods, which matches the body's count.

- [SHAPE] S1 applies.
- [PROCESS] Proof: "Added in this round: a v2 request through `V2HttpCall` ...". Fix: "The test also covers a v2 request through `V2HttpCall` ...".
- [PROOF] "`CoreScopedAuthStandaloneTest`: 5 of 5 pass with this change, verified at head f0e7395f58f". The receipt records 3 of 3 at the gated head 38e306c2cd5 and says the live tip has moved. The five-method count matches the file, but the number is not in the receipt. Fix: the main side re-gates and updates the receipt, or the body drops the count.
- [PROOF] "3 of 5 methods fail on base", "(4 failing test executions under randomization)", and "re-run at head 4c08aa5751e on 2026-10-06 in three authentication configurations" are not in the receipt. Fix: same as above.
- [PROOF] "verified at head f0e7395f58f" has no date. The formula asks for date and head. Fix: add the date from the re-gate.
- [NOTE] S2 applies (4,625 characters).

Reviews: janhoy CHANGES_REQUESTED (2026-10-06T00:05:18Z, no review text). The inline threads were answered on 2026-10-06 (14:44 to 14:45), covering the shards question, the note placement, and the changelog type. The body matches those replies. Copilot's overview says "Findings: None", so there is no automated finding to verify. janhoy's last comment (2026-10-06T21:58:26Z) treats the shards case as a follow-up, and Limits names it. CHANGES_REQUESTED is still in effect.

### #5028 (SOLR-18505): DRIFT

No citation links. Verified at head: the test diff is one file (DirectUpdateHandlerTest.java). The wrapper in testExpungeDeletes (starting at line 488) overrides findMerges and findFullFlushMerges to return null and leaves findForcedDeletesMerges to the live policy. Sample requests use try-with-resources. The policy is restored in a finally block. The class sets TieredMergePolicyFactory.

- [SHAPE] S1 applies.
- [PROCESS] What this change does: "The trigger description and the wrapper were both tightened after a review round caught the per-segment misreading and the open full-flush entry point." Fix: delete the sentence.
- [VOCAB] Proof: "and the gate (tidy, Error Prone compile, `:solr:core:check -x test`) is green." Update: "Gate re-run at the new head:". Fix: "the checks (tidy, ...) pass" and "Checks re-run at the new head:".
- [VOCAB] "With seed `71E7F210A62A9B8C` it fails...", "the class with seed `71E7F210A62A9B8C` failed", "The premise is timing dependent, stated plainly:", and "a GitHub Actions corroboration run (37577283523) was dispatched". Seeds, run identifiers, and "premise" are internal vocabulary. Fix: describe the failure without the seed or the run ID, and write "The failure is timing dependent:".
- [PROOF] Not in receipts/SOLR-18505.md: "failed 6 of 6 forced re-executions on unmodified main ..., five times at the line 494 assertion"; "At 1c5a948d3e4 the same battery passed, and neighboring `MaxSizeAutoCommitTest` passed 3/3 and `DirectUpdateHandlerWithUpdateLogTest` passed 1/1"; the Actions run 37577283523 and head 71a978d5982. Fix: remove them, or add them to the receipt.
- [NOTE] Choice-like content sits in "What this change does": "Raising `deletesPctAllowed` instead would also block ..." and "`NoMergePolicy` was rejected ...". These are live alternatives, but there is no "A choice to check" section and no question. The lead decides whether they are a real choice. If so, move them into a choice section with a question.
- [NOTE] S2 applies (5,295 characters).

Traced: DirectUpdateHandlerTest 7 of 7 at e28739b; five forced runs and two random-seed runs, 7 of 7 each, at 8ca33200e37 (round 21 record). CI: gradle check run 37577263650 at e28739b4069 is a success, as are Crave, labeler, and changelog.

### #5029 (SOLR-18506): DRIFT

No citation links. Verified at head: the change is nine test lines in TestThinCache.java. backing.setMaxSize(200) uses the public CaffeineCache.setMaxSize (line 344). ThinCache.warm resets evictions (line 263). Caffeine is pinned at 3.2.4.

- [PROOF] Proof: "A standalone model ... against Caffeine 3.2.4 ..., 200,000 trials per configuration, fails the evictions check in 1,243 trials (0.62%) at capacity 100 and in none at 200". Limits: "(about 1 run in 10,000 in the model)". The receipt has no model and no trial counts. Fix: drop the model figures, or add them to the receipt.
- [PROOF] "3 forced runs pass on this head after a comment-only correction." No date. The receipt records the light gate on 2026-10-05 at 77c019e. Fix: add "verified 2026-10-05 at head 77c019e1ff0".
- [VOCAB] Proof: "Gate: tidy clean, Error Prone :solr:core:compileTestJava clean, :solr:core:check -x test green." Fix: "Checks: ...".
- [VOCAB] Proof: "The recorded seeds do not reproduce the failure on unmodified main, as expected for a failure that does not depend on the seed." Fix: "The failure never reproduced on unmodified main in 17 planning runs." (The receipt has that count.)
- [NOTE] What happens today: "Recorded 14 times in 30 days across unrelated PRs, ten on branch_9x, and the value was 1 every time." Limits: "Ten of the fourteen recorded occurrences were on branch_9x (solrbot PR #4976)". Neither count is in the receipt. Fix: cite the sweep, or drop the counts.
- [SHAPE] S1 applies.
- [NOTE] S2 applies (3,971 characters, slightly over).

CI: Crave run 37384974639 at 77c019e1ff0 concluded failure. The failed-step log names "GCSInstallShardTest > classMethod FAILED". I did not find TestThinCache in the failure lines. This is not a body defect, but the lead should read that run before treating the PR as green. Other checks passed.

Review: epugh asked on 2026-10-06T17:20:05Z whether a merged PR changes this one. Nick replied at 17:35:14Z: no change, and branch_9x now runs Caffeine 3.3.0, so a backport would need to read 3.3.0. The body does not mention 3.3.0, which is not a contradiction. epugh's comment at 19:07:45Z quotes that reply and asks nothing new in the visible text.

### #5030 (SOLR-15823, set-level PUT): DRIFT

Verified at head: config-edit on the set-level method (NodeLogging.java lines 91 to 92 at 85d8df82502, same content at the head). The V1 Logging factory is gone from solr/webapp/web/js. The nodes parameter (NodeLoggingApis.java lines 41 to 51). The unknown-node pre-check (NodeLogging.java lines 134 to 145). The failedNodes computation (lines 145 to 151). The membership check in RemoteRequestProxy (lines 121 to 131). The PKI needsAuthorization check (PKIAuthenticationPlugin.java lines 371 to 372). The GET still takes no nodes parameter (NodeLoggingApis.java lines 34 to 39). The three Limits bullets on authorization and partial failure hold against this code.

- [CITE] All 31 body links point at SHAs other than the head. 25 point at 100ad2e0df69 (the proof base commit), 1 at 85d8df82502, and 3 at the base commit e432df19c4a (NodeLogging.java line 47, NodeLogging.java lines 87 to 99, logging.js lines 164 to 175). The other two are a commit link and a PR link. Fix: re-point file links to 1f60cd39baae5dddbefcf6127b25955dc3518578. At head, every cited file matches except configuring-logging.adoc, which gained one line at 108; the cited lines 105 to 116 still hold the nodes text. The three base-commit links describe code that no longer exists at the head in that form. Keep them and say "at the base commit", or drop them. The lead chooses.
- [PROOF] "Verified 2026-10-05 at head 85d8df82502 (base e432df19c4a)." The PR head is 1f60cd39baa. That head adds one reference-guide sentence, per the receipt. Fix: "Verified 2026-10-05 at head 85d8df82502. The head 1f60cd39baa adds one reference-guide sentence after that."
- [PROOF] Selenium counts: "`AdminUiLoggingScreenTest` 3/3 and `AdminUiLoggingStandaloneTest` 1/1", "run 2026-10-05 on Windows with Chrome 153". Not in receipts/SOLR-15823.md. Fix: add to the receipt, or drop the counts. The Admin UI browser tests are green on the head in CI.
- [FIT] "The generated SolrJ client exposes `setNodes` and forwards the request body through the proxy ([NodeLogging.java:123-125], [V2SolrRequestBasedProxy.java:58-71])." The V2 lines remove the `nodes` parameter from the forwarded params (PARAM_NODES). They do not show the body being forwarded. Fix: "...and the proxy forwards the request with the `nodes` parameter removed (V2SolrRequestBasedProxy.java:58-71)."
- [FIT] Limits: "V1 applied the change on that node locally before it broadcast ([LoggingHandler.java:75-79])." The cited lines show only the local apply (line 78). The broadcast is on line 95 (`new GenericV1RequestProxy(cc, req, rsp).proxyRequest();`). Fix: cite lines 75 to 79 and line 95.
- [SHAPE] S1 applies.
- [NOTE] Choice 3 says #5031 is "stacked on this one". #5031 is stacked on 100ad2e0df69, an earlier head of this PR. Fix: "stacked on an earlier head of this PR".
- [NOTE] S2 applies (14,262 characters; three choices in a big PR, so the amendment allows the length).

CI: gradle check run 37572913857 at 1f60cd39baa is a success. Admin UI browser tests, New UI tests, Crave, labeler, and changelog are also success.

### #5031 (SOLR-15823, levels GET): DRIFT

Verified at head: NodeLogging.java lines 73 to 74 require config-read. logging.js line 129 passes an empty options object. The broadcast helper javadoc (NodeLogging.java lines 122 to 128) matches the claim. LoggingHandler line 91 passes null.

- [CITE] Eight links use caf3dbf4d8db, which is not the head: NodeLoggingApis.java lines 34 to 43, NodeLogging.java lines 74 to 82 and 122 to 166, LoggingHandler.java line 91, configuring-logging.adoc lines 118 to 124, and three test-file links. Content at head is identical for each (blob check). Fix: re-point them to 6fa54c4de8c9bbcc859cc57606ff7e4cb574dc81. The other links already use the head.
- [FACT] "This PR is the follow-up to the set-level PR, #5030: it is stacked on that PR's branch and is meant to land after it." This branch contains 100ad2e0df69, an earlier head of #5030. It does not contain the live head 1f60cd39baa (checked: not an ancestor). Fix: "it is stacked on an earlier head of that PR (100ad2e0df69), and it will be restacked when that PR's head settles."
- [PROOF] "The Admin UI Selenium suites were rerun at this head on Windows: `AdminUiLoggingScreenTest` 3/3 and `AdminUiLoggingStandaloneTest` 1/1 pass". Not in receipts/SOLR-15823.md. Fix: add to the receipt or drop.
- [PROOF] "Verified 2026-10-05 at head 6fa54c4de8c". The receipt records the re-gate with no date. Fix: confirm the date with the main side, or drop it.
- [VOCAB] "Premise, new tests against the companion branch's production code (head 100ad2e0df69, ...)" and "the premise below is unchanged". "Premise" is an internal label for the before-change run. Fix: "Before this change, new tests against ..." and "the earlier result is unchanged".
- [SHAPE] S1 applies.
- [NOTE] S2 applies (7,786 characters).

CI: gradle check run 37386321023 at 6fa54c4de8c is a success, as are Admin UI, New UI, Crave, labeler, and changelog.

### #5061 (SOLR-18119): DRIFT

Citations: all 17 links point at the head 660faedd026. I checked the cited ranges: ChangesToHtml.java 270 to 274 (write), ChangesToHtmlTask.java 39 to 72, BuildInfraPlugin.java 61 to 63, changes-to-html.gradle 20 to 25, PythonCompat.java 28 to 85, ChangesToHtmlTest 25 to 146 (11 tests), PythonCompatTest 24 to 40 (3 tests), checkJavadocLinks.py 252 to 257 (BROKEN ANCHOR), and the two dev-docs python3 notes (lines 36 and 37). All match. Verified: the converter has no Gradle imports; changes-to-html.gradle has no python call at this head; the Java task never looks for python3.

- [PROOF] "It produces `Changes.html` with SHA-256 `457d199d1b7e41c661a37c79281234aa3c1d1dd129a1639162c7cadf5698b5f3`. The Python original, run on the same tree's CHANGELOG.md under CPython 3.12.3, ...". Neither the hash nor the Python version is in receipts/SOLR-18119.md. Fix: drop them, or add them to the receipt.
- [PROOF] "Test merge with PR #5062's head `26678c3737c`: the merge is clean, with zero conflicts. ... The build-infra tests pass 14/14. The merged site's `Changes.html` has the same SHA-256 as above." Not in the receipt. Fix: same as above.
- [FACT] "Two other PRs take different routes: [#4999] ... and [#5034] (fall back to GraalPy, by Jan)." #5034 is CLOSED (checked read-only). #4999 is OPEN. Fix: "[#5034] (now closed; it fell back to GraalPy, by Jan)".
- [NOTE] Proof: "ChangesToHtmlTest 11/11, PythonCompatTest 3/3 ... 0 failures, 0 errors, 0 skipped". The receipt has "14 of 14" only. The total matches. The split is not in the receipt.
- [NOTE] Limits: "185,623 links with fragments, 0 broken" comes from receipts/SOLR-18523.md, not this ticket's receipt. "8,243 HTML files" and "about 9,365 false alarms" are in no receipt. Fix: cite the record, or drop the numbers.
- [NOTE] Choice: the Jira-sourced statements (Smiley on 2026-10-07, Hostetter, Jan on 2026-10-08, the July removal) are not checked. They are outside this slice.
- [NOTE] S2 applies (9,862 characters).

CI: gradle check run 37868184762 at 660faedd026 is a success. Labeler and changelog are also success. Crave is not in the rollup.

### #5062 (SOLR-18523): DRIFT

Verified at head: documentation.gradle lines 87 to 89 (isCIBuild block), changes-to-html.gradle lines 71 to 77 and 108 (python still a soft dependency, as stated), render-javadoc.gradle line 422, index.template.md lines 27 to 28, solr-ref-guide/build.gradle lines 58, 76, 464 to 478, and 475, documentation.gradle line 106, globals.gradle line 175 (checks env names, so CI=false still turns the gate on), gradle-precommit.yml line 26. The deleted files are absent at the head, and the root build.gradle has no reference to the removed script.

- [CITE] "Record: [`reviews/solr-18523-doclint.md`](https://github.com/nick-boss-tech/solr/blob/f11c126fd9d4769a5e72003194c8308af3f875f0/reviews/solr-18523-doclint.md)". The link is at f11c126fd9d, not the head, and the file is absent at the head. It is also an internal review record. Fix: remove the link and the "Record:" label.
- [VOCAB] "Lucene follow-ups: 13 later Lucene changes were checked against this tree. One applied here: the CI gate on the `check` edge." This is review-process narration, and the count is not in the receipt. Fix: "One later Lucene change applied here: the documentation build runs in `check` only on CI."
- [FORMAT] Bare cross-repo references resolve against apache/solr: "Lucene used this shape in #14905 and kept it in its Java port (#15350)." and "...accepted in #14905. The local-check trade is the gate Lucene added there and kept in #15350." Fix: full links to https://github.com/apache/lucene/pull/14905 and https://github.com/apache/lucene/pull/15350.
- [PROOF] Not in receipts/SOLR-18523.md: "BUILD SUCCESSFUL in 55s", "The built site has 8,243 HTML files", "produced the same 8,243-file site in 5m 54s", "BUILD FAILED in 2m 18s", "BUILD SUCCESSFUL in 1m 38s", "13 later Lucene changes", and "The scan alone took 4m 18s". The receipt holds only the 185,623 audit figure. Fix: main side updates the record, or the body drops these numbers.
- [PROOF] Limits: "it reports 22,710 problems: 15,450 public or protected items ..., 3,079 missing `@param`, 1,849 missing `@return`, 1,056 default constructors ..., 711 missing `@throws`, and 565 empty or tag-only comments." Not in the receipt. Fix: same as above.
- [FIT] "[`gradle/java/javac.gradle:58-60`] sets `-Xdoclint:all/protected`, `-Xdoclint:-missing` and `-Xdoclint:-accessibility` on every JavaCompile task, with `-Werror`." Lines 58 to 60 hold the doclint flags. `-Werror` is on line 69 and is added only when `javac.failOnWarnings` is true (default true). Fix: cite lines 58 to 60 and 69, and state the default.
- [VOCAB, borderline] "gate" is used for the CI check: "The gate covers this project's check only.", "The gate holds when the render runs", "The gate runs are at head ...". Fix: "check" or "CI check".
- [NOTE] S2 applies (11,981 characters).

Review: dsmiley APPROVED on 2026-10-09T04:15:57Z. His comment on 2026-10-09T04:17:16Z (no interest in addressing "missing") matches the Limits.

CI: gradle check run 37868555793 at 26678c3737c is a success. Labeler and changelog are also success.

## Reviewer and automated comments

- #5014: no reviews or comments.
- #5015: epugh left three inline comments (PluginInfo.java "Can we make it less verbose?", TestConfigHighlightOutput.java, and the upgrade note's v2 question). Nick fixed the first two in 51075ab7328 and c96d7eee3b7, both in the head history. I verified the computeIfAbsent grouping at the head. The v2 answer's code citations (GetConfigAPI.java, SolrConfigHandler.java) were not checked.
- #5016: janhoy CHANGES_REQUESTED; inline threads answered; body consistent with replies. Copilot automated overview: "Findings: None".
- #5028: none.
- #5029: epugh comments (see the #5029 section). Answered.
- #5030, #5031, #5061: none.
- #5062: dsmiley APPROVED, with a comment consistent with Limits.

## CI state

Every check-run head I read matched the PR head. No check is pending or action_required in any rollup.

| PR | Rollup (state) | Merge state and review |
|---|---|---|
| #5014 | gradle check, Solr Tests via Crave, labeler, changelog: SUCCESS. generate: SKIPPED | UNKNOWN; no review decision |
| #5015 | labeler: SUCCESS only. No build, test, or changelog check in the rollup | UNKNOWN; no review decision |
| #5016 | gradle check, Crave, labeler, changelog: SUCCESS. generate: SKIPPED | DIRTY (conflicts); CHANGES_REQUESTED |
| #5028 | gradle check, Crave, labeler, changelog: SUCCESS. generate: SKIPPED | UNKNOWN; no review decision |
| #5029 | Crave: FAILURE (run 37384974639 at 77c019e1ff0; GCSInstallShardTest classMethod). gradle check, labeler, changelog: SUCCESS. generate: SKIPPED | UNKNOWN; no review decision |
| #5030 | gradle check, Admin UI browser tests, New UI tests, Crave, labeler, changelog: SUCCESS. generate: SKIPPED | CLEAN; no review decision |
| #5031 | Same set as #5030, all SUCCESS | CLEAN; no review decision |
| #5061 | gradle check, labeler, changelog: SUCCESS. generate: SKIPPED | CLEAN; no review decision |
| #5062 | gradle check, labeler, changelog: SUCCESS. generate: SKIPPED | CLEAN; APPROVED |

"generate" is a renovate changelog job that was skipped, not failed. #5015 has no build check in its rollup, so its CI state is incomplete. That is a missing check, not a defect.

## Not checked

- No build, Gradle, test, Selenium, planted-error, or gate run. Test and Selenium results are judged against the receipt only.
- Jira-sourced statements in #5061 and #5062 (for example Smiley, Hostetter, Jan, and the July removal).
- Lucene PR statements (#14905, #15350, and the "13 later changes").
- The Actions run for #5029 beyond its failed-step names.
- The #5015 claim that no code reads serialized children by name (spot-checked only).
