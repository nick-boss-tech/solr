# Gate job: SOLR-7520 tidy fix (formatting-only commit owed at a6dc792b36)

- Branch: solr-7520-submit. Head: `a6dc792b365b8bbbfd61b1f43555a26a322ba91a` (fork tip, confirmed by ls-remote on 2026-10-15 at filing). If the tip has moved at claim time, stop and record the new tip here; the diff check below is written against this head's tree.
Status: TIDY DIFF CHECKED, formatting commit and re-verification pending by hand (claims/pool-vm2-gate-backlog-7520-tidy.md)
- Capability tags: gate.
- Module: `:solr:core`. Focused class: `org.apache.solr.search.AnalyticsMergeStrategyTest` (methods `test` and `testGroupedSecondPhase`). Seed: `7520C0FFEE7520`.
- Why: the moved-tip verification at this head (top section of `receipts/SOLR-7520.md`, 2026-10-15) records tidy NOT CLEAN, as at the previous head. Tidy joins the wrapped `assertShardCount` call in `testGroupedSecondPhase` onto one line (one line in `AnalyticsMergeStrategyTest.java`) and changes nothing else. The change was restored in the verification worktree, not committed, so the pushed tree is the untidied one and the tests on record ran on the pushed tree. A gate at this head would stop on the GATE INVALID guard; the formatting-only commit is owed first, and this job lands it.

## What to run

Claim first. Check the branch out at the head above in a fresh worktree and copy `gradle.properties` into it before building. Take the blocking flock on the test queue for every Gradle run; never run two builds at once. On vm1 use `~/workspace/tools/solr-gradle.sh` only; on another host use its wrapper per its host file.

- Step 1: apply tidy on `:solr:core` at the head. Restore `gradle/libs.versions.toml` afterward if tidy touched it.
- Step 2: inspect the resulting diff before committing anything. It must be exactly the one formatting join: the wrapped `assertShardCount` call in `testGroupedSecondPhase` in `solr/core/src/test/org/apache/solr/search/AnalyticsMergeStrategyTest.java` joined onto one line, and nothing else in any file. If tidy changes anything more than that, stop: restore the tree, record what tidy actually changed in this file, and escalate to the main agent. Do not commit a wider formatting change under this job.
- Step 3: commit the formatting change on its own, formatting-only, under the Nick Shanin identity with no trailers, subject naming the ticket (for example "SOLR-7520: apply tidy formatting to AnalyticsMergeStrategyTest"). No other file rides in this commit.
- Step 4: light re-verification at the new head, in the same worktree:
  - Changelog YAML parse of `changelog/unreleased/SOLR-7520-grouping-postfilter-complete.yml` with a real parser. The fragment is unchanged by this job; parse it for the record.
  - Tidy again at the new head: it must leave the tree clean (nothing further to join). If it changes anything, the job is not done; record it here and stop.
  - Error Prone compile: `:solr:core:compileJava :solr:core:compileTestJava` with `-Pvalidation.errorprone=true`.
  - Focused run: `org.apache.solr.search.AnalyticsMergeStrategyTest` at seed `7520C0FFEE7520`, counted from fresh JUnit XML. Expected: 2 tests, 0 failures (the sweep's head run at a6dc792b36).
  - The sweep's proof legs at a6dc792b36 stand and are NOT re-run: with `CommandHandler.java` reverted to the merge-base `14c7aac0d151402b00259e2fb9bf5eed7049ec5d`, each phase's test fails with its own "analytics section missing" message, and that evidence carries over a formatting-only delta in a test file.
- A run that executed tests and failed is not relaunched by the claimant. Record the failure here (test, seed, reproduce line) and escalate to the main agent.

## On green

1. Push plainly (fast-forward) to solr-7520-submit. The branch backs the owner's own draft PR #5138; pushes to his own PR branches carry standing authorization.
2. Refresh `receipts/SOLR-7520.md` with a new top section: the new head, the formatting-only nature of the commit (the one-line join, verified by diff), the step 4 results with the focused count from the fresh XML, and the note that the proof legs carry over from a6dc792b36.
3. Re-point PR #5138's body to the new head (read it through the API before and after the edit): update the head citations and remove the owed-formatting note its Proof currently carries. The Proof's run record stays as recorded for a6dc792b36, with the light re-verification at the new head added.
4. Mark this job DONE and the claim DONE in the same push as the receipt, and append one line to the takeover log recording the new head.

## Coordination

`gates/SOLR-7520.md` (the full gate at a6dc792b36) also stands open. This job moves the head by design, by exactly the formatting commit. It lands first: after it lands, the full gate's claimant records the new tip in that file and runs the full gate at the new head. Do not run both jobs against the same tree at the same time.
