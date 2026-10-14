# Retire packs, round 1 (final), review host report

Date: 2026-10-13. Worktree HEAD = origin/pr-prepare = 5ecd784df17dcc439e63ab30270dfe82a28dd24c.
Assignment: assignments/pool-retire-round-final-reports-round-1.md (origin/pr-prepare).
Starting record: reports/retire-candidates-review-round.md with rr1 and rr2.

## Method and bounds

- Git read-only: show, ls-remote, merge-base, diff, ls-tree, grep, cat-file. No build, test, Gradle or gate.
- No Jira access. No GitHub writes.
- PR states and fork run status came from the public GitHub REST API, unauthenticated. gh is not logged in on this host, and gh_token.env was not opened.
- Nothing was written to the repository. The nine packs and the index are in this file, not at reports/retire-final-*.md. No claim was written under claims/ either. The coordinator should record the claim and the DONE mark.
- The receipt for SOLR-18277 is HELD. Its text is in section "SOLR-18277 receipt (held)" below. It was not written to receipts/.
- Update, 2026-10-13 (main side): the owner ruled on SOLR-18277 the same day. The receipt is written (receipts/SOLR-18277.md, with the CI commit SHAs recorded there). The disposition is RETIRED and the three branches are left in place; none is deleted. The owner's general lean is to retire and leave, or rename, rather than delete. Owner steps 1 and 2 in pack 9 are done; step 4 (deletion) is superseded by this ruling and does not happen.

## Main moved after the round

Live upstream main (ls-remote) is d38e0f1623728bdd305e76a598f013f1bf32e449. The round and the receipts cite d76fc14b9857. Five commits sit between them: f3a798e424b, 597ed677408, b5e8ae18c52, cf4a66669d3, d38e0f16237. None touches a candidate path (StatsComponentTest, SplitShardCmd, ShardSplitTest, SplitShardCmdCleanupTest, LTRQParserPlugin, SyntheticSolrCore, ConcurrentLRUCache, cross-dc-manager, SolrMessageProcessor, HttpSolrClient, EmbeddedSolrServer, DocsStreamer, stream.js, services.js, changes-to-html.gradle). Checks below use live main unless a record says otherwise.

## Live tips (ls-remote origin, 2026-10-13)

| Branch | Live tip | Record | Match |
|---|---|---|---|
| solr-17722-submit | fee3a26beb3c8289057ac41d94dfc8d83a969b71 | receipt, rr1 | yes |
| solr-17882-submit | d328eb392d9c9acf3d925bb20729798e0d17720f | receipt, rr1 | yes |
| solr-17372-submit | 048862fda8ab76f36f259c6849ec5c5a8cfafabe | receipt, rr1 | yes |
| solr-12743-submit | 1bb4b227dfe5e212d47a8d63ba57b74091b9e395 | receipt, rr1 | yes |
| solr-17825-submit | 0ef08ec86c650af99a51029da29e4c36503a70c4 | receipt, rr1 | yes |
| solr-10198-submit | 9448cda146f7e09f3389551c4ef883a63d415eeb | receipt, rr2, PR #4965 head | yes |
| solr-18119-submit | 723022d35fcede32de2a884c29e5aa0ff08e3f1f | receipt, rr2, PR #4999 head | yes |
| solr-9759-submit | 31e702622dae43a36ed8d7ec8477c1547e5ebc42 | receipt, rr2 | yes |
| solr-18277-submit | 17c0e64481285e8683c6bf974d9c7f9dfc73555a | receipt, rr2, PR #4959 head | yes |
| ci/solr-18277 | 35c9e9952285c3d4f4c428b62da637ceb6e48c6b | rr2 | yes |
| ci/solr-18277-proof | 44c35a3deafd765893ab24f2d41df95a3723efee | rr2 | yes |
| solr-18119-jvm | 660faedd026ddc77f8736a4d1ab5cb89a4253a4a | receipt, PR #5061 head | yes |

## PR and run state (public API, 2026-10-13)

- #4959 (SOLR-18277): closed, merged 2026-10-05T10:46:07Z. Head 17c0e644812. Merge commit 0cc328310f8.
- #4965 (SOLR-10198): closed, merged 2026-10-07T13:19:33Z. Head 9448cda146f7. Merge commit e97c2a0884cb.
- #4999 (SOLR-18119-submit): open. Its only review is CHANGES_REQUESTED, submitted 2026-10-05T12:13:45Z. No later review is listed.
- #5061 (SOLR-18119-jvm): open. Head 660faedd026.
- #5062 (SOLR-18523-submit): open. Head 26678c3737c.
- #5034 (graalpy python fallback, the hold named in rr2): closed unmerged 2026-10-09T04:46:01Z.
- #5048 (SOLR-9759 fix on main): merged 2026-10-08T22:46:49Z. Merge commit c6f910d3ccf.
- No PR with head nick-boss-tech:<branch> under apache/solr for solr-17722-submit, solr-17882-submit, solr-12743-submit, solr-17825-submit, solr-17372-submit or solr-9759-submit.
- Fork runs (nick-boss-tech/solr), conclusion and head checked: 37237885304, 37238381991, 37238383951 (ci/solr-18277-proof at 44c35a3, success); 37237887493 (ci/solr-18277 at 35c9e95, success); 37639175484 (ci/17372-statscomponent at 933a41eb35f, success). Test counts for the 18277 runs are as rr2 reports them from its log read. I did not open the logs.

## Index

| Candidate | Verdict | Pack | Owner step it waits on |
|---|---|---|---|
| SOLR-17722 | RETIRE CLEAN | 1 | Delete solr-17722-submit |
| SOLR-17882 | RETIRE CLEAN (premise dead; retire or re-aim is the owner's call) | 2 | Owner accepts premise-dead; delete solr-17882-submit or re-aim |
| SOLR-12743 | RETIRE CLEAN | 3 | Delete solr-12743-submit |
| SOLR-17825 | RETIRE CLEAN | 4 | Delete solr-17825-submit (optional SOLR-18300 link) |
| SOLR-10198 | RETIRE CLEAN (merged) | 5 | Delete solr-10198-submit |
| SOLR-18119-submit | RETIRE CLEAN, conditional | 6 | Close PR #4999, delete solr-18119-submit, once PR #5061 lands or is accepted |
| SOLR-9759 | RETIRE CLEAN (superseded by #5048) | 7 | Delete solr-9759-submit |
| SOLR-17372 | RETIRED (owner ruling 2026-10-14), premise NOT SUPPORTED | 8 | Done: retired and labeled 2026-10-14; solr-17372-submit left in place, no deletion |
| SOLR-18277 | RETIRED (owner ruling 2026-10-13) | 9 | Done: receipt written 2026-10-13; branches left in place, no deletion |

---

## 1. SOLR-17722 (solr-17722-submit): RETIRE CLEAN

Head checked: fee3a26beb3c8289057ac41d94dfc8d83a969b71.

Premise state. receipts/SOLR-17722.md: "Status: NO GATE by finding. The premise is false at the merge-base: the base-only wire probe passed and verified `wt=javabin` on the captured request, not `wt=json`." It also says "No topic-head run, packaging, or full gate was performed." rr1 gives RETIRE CLEAN.

What the branch holds. Three files, 109 insertions, from merge-base 56ec140e363:
- solr/cross-dc-manager/.../SolrMessageProcessor.java (+8): sets wt=javabin on the request params in prepareIfUpdateRequest.
- SolrMessageProcessorTest.java (+24): a mock-params test.
- SOLR-17722-TESTING.md (+77): says nothing was compiled or run.

Gainable. None. On live main, HttpSolrClient.initializeSolrParams (lines 153-166) copies the request params and then calls `params.set(CommonParams.WT, parserToUse.getWriterType())`. That overwrites the branch's value before every send. The callers on main are HttpJdkSolrClient (line 244), HttpJettySolrClient (line 706) and ConcurrentUpdateJettySolrClient (line 196). The branch's test checks a mock's params, not the wire.

Taken from it. Nothing on main. The probe source is not on this branch. The probe evidence is in the receipt (vm3 log path, seed 9891B4F8740AE16E).

Owner step. Delete `solr-17722-submit` on origin. No PR to close. No Jira note is needed for the deletion.

Gap. One base-side run is recorded (vm3, 1 test, PASS). No topic-head run and no VM1 result exist. This does not block deletion, because the hunk is overwritten on the wire path whatever the base run showed. That is my code reading, and I checked the call sites above.

## 2. SOLR-17882 (solr-17882-submit): RETIRE CLEAN, premise dead

Head checked: d328eb392d9c9acf3d925bb20729798e0d17720f.

Premise state. receipts/SOLR-17882.md: "Status: NO GATE as a fix, by finding: the premise is dead. The round 12 and round 15 reviews found the ticket's LTR coordinator failure does not reproduce as a defect this branch fixes, and the branch is a retire candidate." It also says "Disposition (retire the branch, or re-aim it) is an owner call still pending."

What the branch holds. From merge-base 56ec140e363, two files:
- LTRQParserPlugin.java (+22/-1): a synchronized modelStore(SolrCore) fallback that reads the store from the core's RestManager, and its call site.
- SOLR-17882-TESTING.md (+99). No test class.

Gainable. Nothing. On live main, SyntheticSolrCore.initRestManager (line 85) returns a bare `new RestManager()` with the comment "We do not expect RestManager ops on Coordinator Nodes". LTRQParserPlugin line 177 still calls mr.getModel on a null store. The branch's fallback turns the failure into NOT_FOUND, as its own Javadoc says. It does not make coordinator queries work. The new lock on each model lookup was not measured.

Taken from it. Nothing.

Owner step. Retire: delete `solr-17882-submit`. Re-aim is a different decision (a solr/core design change). No PR. No Jira note is needed for the deletion.

Gap. The round did not read the round 12 and round 15 goal files, which are not in this tree. "Premise dead" therefore rests on the receipt's citation of them. The round also did not check whether coordinator nodes reach the LTR parser in a supported layout. The owner must accept the premise-dead call before deletion. The supported-layout question matters only for re-aim.

## 3. SOLR-12743 (solr-12743-submit): RETIRE CLEAN

Head checked: 1bb4b227dfe5e212d47a8d63ba57b74091b9e395.

Premise state. receipts/SOLR-12743.md: "Status: NO GATE, and none is possible. The branch is RETIRED as obsolete on the main side's record." It adds: "the file is absent on current main, checked directly, so the patch has nothing to apply to."

Checked on live main. `git ls-tree` for solr/core/src/java/org/apache/solr/util/ConcurrentLRUCache.java returns nothing. A grep for ConcurrentLRUCache under solr/ returns nothing.

What the branch holds. From merge-base 86bc6f29224:
- ConcurrentLRUCache.java (+17/-3): a lock-ordering change inside the deleted class.
- OPEN-QUESTIONS-SOLR-12743.md (+11): a handoff note marked for removal.

Gainable. Nothing. Main's replacement is CaffeineCache.java. The hunk was not measured against it. The receipt records that the reporter saw the leak continue with the 2019 patch.

Taken from it. Nothing.

Owner step. Delete `solr-12743-submit`. No PR. No Jira note is needed for the deletion. If the owner wants the ticket to carry the retirement, this draft is for the owner to post or discard (not posted):
"Branch solr-12743-submit is retired. Its patch targets ConcurrentLRUCache, which the Caffeine migration removed (apache/solr PR #4516). Whether the leak reproduces with the Caffeine-based caches is still open and needs a fresh investigation using the ticket's heap-dump approach."

Gap. Owner question 2 in the receipt (fresh investigation) stays open. Deleting the branch does not answer it. The OPEN-QUESTIONS file goes with the branch, but its content is in the receipt and the inventory row.

## 4. SOLR-17825 (solr-17825-submit): RETIRE CLEAN

Head checked: 0ef08ec86c650af99a51029da29e4c36503a70c4.

Premise state. receipts/SOLR-17825.md: "Status: NO GATE, and none is owed. The branch is RETIRED as obsolete on the main side's record." It gives the reason: "cross-dc-manager no longer carries the kafka213 runtime dependency or any beanutils dependency on current main (the Kafka 4 upgrade removed them), and the ticket is Resolved upstream (fixed in 10.1). The branch's own guard task would fail check on main now."

Checked on live main:
- solr/cross-dc-manager/build.gradle has no commons-beanutils and no kafka213 reference.
- No kafka213 catalog entry exists under gradle/.
- The Kafka 4 commit 8c750e25b11 is an ancestor of main.
- The only beanutils pin on main is gradle.lockfile line 7, `commons-beanutils:commons-beanutils:1.9.4=ratDeps`. That is the build-time RAT classpath.

What the branch holds. From merge-base 86bc6f29224: build.gradle (+31, a beanutils 1.11.0 constraint and a verifyCommonsBeanutilsFloor task wired into check) and SOLR-17825-DECISION.md (+21, "remove before any PR"). The branch references `libs.apache.kafka.kafka213`, which does not resolve on main, so the change cannot apply.

Gainable. Nothing.

Taken from it. Nothing.

Owner step. Delete `solr-17825-submit`. No PR. The decision note goes with the branch, but its reasoning is in the receipt and inventory row 458. Optional SOLR-18300 link (the owner's call). Draft, not posted: "Fixed by the Kafka 4 upgrade in SOLR-18300. The beanutils floor branch is retired because cross-dc-manager no longer depends on beanutils on main."

Gap. The transitive beanutils absence on the cross-dc runtime classpath was not resolved, because no build ran. This does not block deletion, because the branch cannot apply to main. The "Resolved, fixed in 10.1" status comes from the receipt. I did not check Jira.

## 5. SOLR-10198 (solr-10198-submit, branch item): RETIRE CLEAN, merged

Head checked: 9448cda146f7e09f3389551c4ef883a63d415eeb (equals PR #4965 head).

Premise state. receipts/SOLR-10198.md: "Status: MERGED. The pull request for this branch was merged into apache/solr main on 2026-10-07; ... There is nothing to draft." It also says "the merged head equals the live tip."

What the branch holds. Five files from merge-base 0d2a4649c79 (+293/-5): changelog SOLR-10198.yml, EmbeddedSolrServer.java, DocsStreamer.java, TestEmbeddedSolrServerStreamingTypes.java, EmbeddedSolrNoSerializeTest.java. Checked: `git diff 9448cda e97c2a0884cb` on those five paths is empty, and `git diff 9448cda upstream/main` on those five paths is empty.

Gainable. Nothing. Taken from it. All of it, merged as e97c2a0884cb.

Owner step. Delete `solr-10198-submit` on origin. There is no PR to close, because #4965 is merged. Jira note: none needed. The receipt says the ticket is closed out. I did not check Jira. The inventory row 388 still reads "live PR #4965". The owner may update that row.

Gap. None for the deletion.

## 6. SOLR-18119-submit (solr-18119-submit): RETIRE CLEAN, conditional

Head checked: 723022d35fcede32de2a884c29e5aa0ff08e3f1f (equals PR #4999 head).

Premise state. receipts/SOLR-18119.md, section for solr-18119-submit: "Status: RETIRE CANDIDATE, superseded by solr-18119-jvm on 2026-10-08. No gate at its live tip in this line." It adds: "That PR is still open; closing it is a public action and an owner call." Its PR carries "a reviewer's requested changes, applied and pushed at this tip on 2026-10-05." The API confirms the one CHANGES_REQUESTED review and no later review.

What the branch holds. One file: gradle/documentation/changes-to-html.gradle (+10/-9). It resolves python3 through project.externalTool, throws a GradleException when python3 is missing, and honours -Ppython3.exe. Live main's copy equals the merge-base copy, so the hunk is not on main.

The pair:
- solr-18119-jvm (660faedd026, PR #5061, open) replaces the Python route with a Java task (ChangesToHtml.java, ChangesToHtmlTask.java), deletes changes2html.py, and has no python call in changes-to-html.gradle. Checked.
- solr-18523-submit (26678c3737c, PR #5062, open) removes the javadoc crawl. It does not change the Python route. Its changes-to-html.gradle still holds the base Python code.

Gainable. Nothing while #5061 lands or is accepted. Taken from it. Nothing.

Owner steps, in order:
1. Close PR #4999 on apache/solr. This is a public action for the owner.
2. Delete `solr-18119-submit` on origin.

Condition. Retire-clean only while the shipped pair lands or is accepted. The Python route is removed only by #5061. The owner checks this by reading PR #5061 state (for example `gh pr view 5061 --repo apache/solr --json state,mergedAt`) and by confirming that upstream main's changes-to-html.gradle has no python3 call. If #5061 closes without landing, this hunk becomes the only Python fallback. Revisit before deleting. The owner should confirm whether the condition is #5061 alone (my reading) or the pair as a whole (the round's wording).

The g6 hold on #4999 (PR #5034) ended when #5034 closed unmerged on 2026-10-09.

Jira note. None for the submit branch. The jvm PR carries the ticket's single submission comment. Nothing is drafted.

Gap. The verdict stays conditional until #5061 moves.

## 7. SOLR-9759 (solr-9759-submit): RETIRE CLEAN, superseded on main

Head checked: 31e702622dae43a36ed8d7ec8477c1547e5ebc42.

Premise state. receipts/SOLR-9759.md: "Status: NO GATE. No gate for this branch is recorded on the main side." Its TESTING note says "Nothing here was compiled or run." rr2 says "superseded on main by c6f910d3ccf, retire recommended."

Main side. c6f910d3ccf is "SOLR-9759: Admin UI should post streaming expressions (#5048)", merged 2026-10-08. It is an ancestor of live main. The branch's three code files (stream.js, services.js, AdminUiStreamScreenTest.java) are identical to main's at c6f910d3ccf, and so to live main. Main's stream.js (line 70) calls Query.queryPost, a form-encoded POST defined in services.js (line 380). Its error path prints "HTTP <status> <statusText>" (stream.js line 73). The branch's guessed change is on main in equivalent form.

What the branch holds. Five files from merge-base cabedd1d968: SOLR-9759-TESTING.md, the changelog fragment SOLR-9759-admin-ui-stream-post.yml (uppercase, where main's is lowercase), AdminUiStreamScreenTest.java, stream.js, services.js.

Gainable. The branch test's positive assertion: a long padded expression still returns all three documents. Main's test (testLargeExpressionSucceedsViaUi, a 20,000-character fq) asserts EOF and does not check documents. rr2 judged the positive assertion not worth a job by itself.

Taken from it. The fix, through #5048.

Owner step. Delete `solr-9759-submit`. No PR (none under apache/solr with that head). Jira note: none needed. The inventory row 436 still reads "awaiting pipeline"; the owner may update it.

Gap. No run of the branch test exists, and no run of main's test is in these records. Neither blocks deletion, because the branch carries no fix that main lacks.

## 8. SOLR-17372 (solr-17372-submit): RETIRED (owner ruling 2026-10-14), premise NOT SUPPORTED; branch left in place, no deletion

Head checked: 048862fda8ab76f36f259c6849ec5c5a8cfafabe.

Is NOT SUPPORTED the current verdict? Yes. receipts/SOLR-17372.md, section "Premise verdict at the ticket seed (2026-10-12, vm2)": "Verdict: NOT SUPPORTED. Leg A (current main d76fc14b9857, no tolerance hunk) passes at the ticket's own seed, so the reported percentile failure does not reproduce there. The job's rule applies: a pass on leg A kills the premise." Leg A is "org.apache.solr.handler.component.StatsComponentTest.testPercentiles, 1 test, 0 skipped, 0 failures, 0 errors" at seed 2074D8EC40F42163. The section "Main-side disposition (2026-10-12)" says "Disposition: RETIRE PENDING OWNER WORD." Commit 1f86ab22b0a (2026-10-12) reads "vm2 premise verdict: SOLR-17372 not supported at the ticket seed; job and claim DONE". The rr1 SALVAGE FIRST (2026-10-11) predates this run. Per the assignment the receipt governs. The top of the receipt file, dated 2026-10-08, says "GATE FAILED ... on the premise", which agrees.

What the branch holds. Three commits after e2cdb2d7e8ae: 3257831180f (the tolerance hunk), 2869441fb32 (a TESTING.md handoff), 048862fda8a (removes that doc). Net diff: StatsComponentTest.java only, +6/-1. Live main line 2298 reads `assertEquals(Double.parseDouble(p), pVal, 1.0D);`. The hunk changes that to a tolerance of `Math.max(1.0D, expected * 0.10D)`.

What the retire case rests on. The hunk is the ticket's own suggested fix (rr1 cites Jira comment 17866876; I did not re-read Jira). Its only reason to exist is that the assertion fails at the reported seed. At that seed, on current main, the assertion passes without the hunk. The hunk would change a passing assertion and fix no failure. That closes the salvage. The salvage was triggered only by leg A, and leg A passed.

Taken from it. Nothing.

Owner step. Delete `solr-17372-submit` after the owner's word. There is no PR (none under apache/solr with that head; the receipt says no PR draft exists). Jira note (optional, owner only, not posted): "Premise check on current main (d76fc14b9857): StatsComponentTest.testPercentiles passes at the reported seed 2074D8EC40F42163 without the tolerance change, so the reported failure does not reproduce there. The fork branch solr-17372-submit is retired. No PR was opened."

The receipt's owner options: (i) ship as stabilization with a rewritten Proof, (ii) hold as moot, (iii) re-aim at a distributed-path premise. NOT SUPPORTED closes the premise-based salvage only. Option (i) is a stabilization choice with no failure behind it. The owner should pick (ii) or deletion knowingly.

Gaps, not required for retire. Leg B (the branch head with the hunk) did not reach a result; the receipt says it is not required. Leg C was not run. The premise is one run at one seed on d76fc14b. Live main has moved five commits, and none touches StatsComponentTest.

Owner ruling (2026-10-14): RETIRED. The owner retired the ticket under the general retire rule stated the same morning: a retired branch is labeled retired in the records and left in place, never deleted. The "Owner step. Delete" line above is superseded: `solr-17372-submit` stays at 048862fda8ab, not deleted and not renamed. The receipt's option (i) is closed by this ruling. The ruling is recorded in `receipts/SOLR-17372.md`.

## 9. SOLR-18277 (three branches): RETIRED (owner ruling 2026-10-13; branches left in place, no deletion)

Heads checked: solr-18277-submit 17c0e644812, ci/solr-18277 35c9e995228, ci/solr-18277-proof 44c35a3deaf.

Premise state. The receipt currently in the tree, receipts/SOLR-18277.md, says "Status: GATE GREEN (hardened) at the live tip, and nothing to open" and "Proof: INCONCLUSIVE, recorded as such in the harden record." The material answers file says "DISCUSS 6: delete all three branches. Recommendation: delete all three." The receipt does not state the run-by-run proof, which is why the record is held below.

Merged state. PR #4959 merged on 2026-10-05 as 0cc328310f8. The three code files on the branch (SplitShardCmd.java, ShardSplitTest.java, SplitShardCmdCleanupTest.java) are identical to live main (checked). rr2 reports the changelog file as identical too.

What the branches hold:
- solr-18277-submit (4 files, from merge-base 0d2a4649c79): the fix, now on main.
- ci/solr-18277 (35c9e995228, one commit on 17c0e644812): adds .github/workflows/fork-test-runner.yml (48 lines). Fork tooling only.
- ci/solr-18277-proof (44c35a3deaf, one commit on base 0d2a4649c79): the same workflow file, plus ShardSplitTest.java with two @AwaitsFix lines removed. SplitShardCmd is base. Those two lines are already absent on main.

Proof analysis. SplitShardCmdCleanupTest (5 tests, checked) calls SplitShardCmd.buildCleanupShardStateUpdates. That method is absent at base, so the class cannot compile there. Its fail-before is INCONCLUSIVE by construction. The two ShardSplitTest tests (testSplitAfterFailedSplit and testSplitAfterFailedSplit2) pass on base production code in all three base runs. They do not pin the fix.

Gainable. Nothing in code. The evidence and the workflow file are the only things a reader could want, and the receipt below carries the evidence.

Taken from it. Merged as 0cc328310f8.

Owner steps:
1. Write the receipt text below on the main side (held here). DONE 2026-10-13: receipts/SOLR-18277.md carries the completed record.
2. Record 35c9e995228 and 44c35a3deaf in that receipt, so fork-test-runner.yml can be recovered. DONE 2026-10-13, in the receipt.
3. Confirm the Jira status is Fixed. I did not check it, because I have no Jira access. rr2 flagged it as open.
4. Delete `solr-18277-submit`, `ci/solr-18277` and `ci/solr-18277-proof` on origin. SUPERSEDED by the owner's ruling of 2026-10-13: retire, do not delete. The branches are left in place.

No PR needs closing, because #4959 is merged. No Jira note is needed for the deletion.

Gaps. Jira status is not checked here. receipt-cited log g4959-harden2.log is not in this tree, so the gate detail is cited, not checked. The fork run logs were not opened here; conclusions and heads were checked through the API, and the test counts are rr2's. None of these blocks deletion once the receipt is written, but the Jira check should come first.

### SOLR-18277 receipt (held at pack time; written to receipts/SOLR-18277.md on 2026-10-13 with the retired disposition and the CI SHAs recorded)

```
# SOLR-18277 gate receipt

- Status: GATE GREEN (hardened) at the gated head, and merged. PR #4959 merged to apache/solr main on 2026-10-05 as squash commit 0cc328310f8. The branch holds no content that main lacks. Deleting the branches is the owner's call.
- Gated head: 17c0e64481285e8683c6bf974d9c7f9dfc73555a (branch solr-18277-submit, PR #4959 head).
- Gate log: g4959-harden2.log (main-side harden record, 2026-10-04). Tidy clean; Error Prone compile passes; module check passes.
- Focused count: SplitShardCmdCleanupTest 5 of 5 at the gated head.
- Proof state:
  - No fail-before proof exists for the fix. SplitShardCmdCleanupTest calls SplitShardCmd.buildCleanupShardStateUpdates, which is absent at base 0d2a4649c79, so the class does not compile on base. Its fail-before is INCONCLUSIVE by construction.
  - The ShardSplitTest tests do not pin the fix. testSplitAfterFailedSplit and testSplitAfterFailedSplit2 pass on base production code in every run below.
  - Fork runs (workflow "Fork test runner", filter ShardSplitTest.testSplitAfterFailedSplit* with -Dtests.nightly=true), counts as read from the run logs:
    - 37237885304, head 44c35a3deafd (ci/solr-18277-proof: base production plus the branch's ShardSplitTest): success, 2 tests.
    - 37238381991, same head and filter: success, 2 tests.
    - 37238383951, same head and filter: success, 2 tests.
    - 37237887493, head 35c9e9952285 (ci/solr-18277: PR head plus runner), same filter with -Ptests.iters=5: success, 10 test invocations.
  - Result: no run in this record fails on base and passes at the fix. SOLR-18277 has no fail-before pin.
- Workflow file: .github/workflows/fork-test-runner.yml (48 lines) exists only on ci/solr-18277 (35c9e9952285) and ci/solr-18277-proof (44c35a3deafd). It is fork tooling, not part of the merged fix.
- Audit scope: retire question only. No PR draft.
- Recorded in the main side's test queue file (harden record, 2026-10-04).
```

---

## Verdict counts and open decisions for the owner

- RETIRE CLEAN: SOLR-17722, SOLR-17882 (premise-dead; retire or re-aim), SOLR-12743, SOLR-17825, SOLR-10198, SOLR-18119-submit (conditional on PR #5061), SOLR-9759.
- SOLR-17372: retire on NOT SUPPORTED, pending the owner's word. Option (i) in the receipt is not closed by it. Owner word given 2026-10-14: retired, labeled in the records, branch left in place and not deleted; option (i) is closed by the ruling.
- SOLR-18277: RETIRED, owner ruling 2026-10-13. The receipt is written; the branches are left in place and none is deleted.
- Owner-only actions: close PR #4999; delete the branches named in each pack; any Jira note. Nothing was posted, deleted, closed or pushed.

## Not done

- No repository write: no packs under reports/, no claim under claims/, no receipts/SOLR-18277.md.
- No build, test, Gradle or gate run.
- No Jira access. Jira states (SOLR-18277 Fixed, SOLR-17825 Resolved in 10.1, SOLR-10198 closed) come from the records.
- Gate and run logs (g4959-harden2.log, the vm2 and vm3 logs, the 18277 fork logs) are not in this tree and were not opened.
- Round 12 and round 15 goal files (SOLR-17882) are not in this tree.
- PR states are from the unauthenticated public API. gh is not logged in on this host.
