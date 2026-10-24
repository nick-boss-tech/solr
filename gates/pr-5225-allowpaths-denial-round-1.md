# Gate: PR 5225 backup path denial tests, round 1

Status: OPEN

- Tags: gate, bats, premise-run. Intended host: any capable Linux gate host with JDK 21 and JDK 25; serialize under the host build lock.
- Source: Nick's October 24 request for a follow-up branch, PR body draft, and JIRA ticket draft after David Smiley's invitation at https://github.com/apache/solr/pull/5225#issuecomment-6390146722.
- Fork branch: `nick-boss-tech/solr:solr-pr-5225-allowpaths-tests-submit`.
- Exact implementation head: `3a32098d870da94d663f2948a1ba4adf32f1675d`.
- Parent/proof base: apache/solr #5225 head `53ddbf0e857a74b8475b299b0907ac1582be8248`; its parent is main `13c693d805f6541f7cec684cd27d28eef7b3b614`.
- JIRA: new ticket drafted locally, not filed; do not invent a SOLR key or post a ticket.
- Outbound change relative to #5225: `solr/packaging/test/test_security_manager.bats` only.
- Current state: implemented and pushed, proof pending. No Windows Gradle, BATS, application test, or syntax-run evidence exists. `git diff --check` passed.
- Receipt target: `receipts/PR-5225-allowpaths-denial.md`; report/claim names should carry `pr-5225-allowpaths-denial-round-1`.

## Preflight and scope

Read live WORKFLOW.md and check existing claims and the remote head before claiming. Use the host's documented detached preflight and background runner; record toolchain versions, runner paths, raw logs, report/XML paths, and the exact command/environment per leg. Inspect the single-file diff against the parent. Do not touch #5225's upstream branch or any live upstream PR.

Run `tidy` at the pinned implementation head and require a clean tree. This is test-only: no new Java tests or production compile mutation belongs on the outbound branch. Check the BATS file with the available shell/BATS static tooling, recording its real result rather than treating a successful `bash -n` on raw BATS syntax as proof.

If a repair changes the file, commit with Nick's ICLA identity and no trailers, push plainly to this fork branch, and add a superseding gate record naming the new head. Do not silently carry old-head proof forward. The job and claim are add-only.

## Exact-head runtime matrix

Use a build JDK supported by this branch, and set SOLR_JAVA_HOME explicitly to the named runtime JDK. Ensure no include file overrides it. Record both runtime and build JDK versions. Set SOLR_PICOCLI explicitly for each leg so an inherited environment cannot select a different parser.

Four legs: JDK 21/commons-cli, JDK 21/picocli, JDK 25/commons-cli, JDK 25/picocli. Run the focused file, preserving fresh JUnit XML and complete Solr logs for every leg. Require Java 21 to execute both tests. Require Java 25 to skip only the Security Manager test and execute the explicitly disabled case. A startup/compile/environment failure is not an expected skip or pass.

Commons-cli task: `:solr:packaging:integrationTests --tests test_security_manager.bats`. Use explicit SOLR_PICOCLI=false. Force the task to execute afresh using Gradle's task `--rerun` option or another documented task-specific rerun mechanism; no cached report counts.

The picocli task has a prepopulated curated suite list; its `--tests` option appends rather than replacing that list. For a focused run, use a temporary init script outside the source checkout:

```groovy
gradle.projectsEvaluated {
  gradle.rootProject.project(':solr:packaging').tasks.named('integrationTestsPicocli') {
    testFiles = ['test/test_security_manager.bats']
  }
}
```

Run with that init script, explicit SOLR_PICOCLI=true, and `:solr:packaging:integrationTestsPicocli --rerun`. Preserve the init script in evidence. It narrows suite selection only and does not alter test assertions or production behavior. The test file itself must remain byte-identical to the pinned head in each final-head leg.

Check the forbidden-path log line against the actual path passed by BackupCmd to CreateCoreBackup: the current implementation expects `<backup-dir>-other/test-fail/COLL_NAME`. If runtime evidence shows a different valid path representation, retain full evidence and repair the assertion; do not fall back to a generic error match. Check whether log appends need a bounded wait. If they do, use the established helper and keep the operation's checkpoint and exact path requirement.

Then run `:solr:packaging:check -x test`, with the final formatting check and a clean tree. No repo-wide application test suite is requested.

## Discriminating failure control

This is a test-only change. Running the new tests against unchanged production is expected to pass; do not call that fail-before proof.

In a separate scratch tree based on the pinned head, replace the SolrPaths out-of-allowlist exception message for the forbidden backup with a distinctive unrelated backup failure. Keep the successful allowed backup intact. Run with the Security Manager disabled. The collection API must still fail with `Could not backup all shards`, but the new test must fail because there is no new path-allowlist diagnostic.

Also plant a matching path-denial line in solr.log before the test records its log checkpoint. Combine this with the unrelated-error mutation. The new test must still fail. Save the exact mutation and log-injection diff, the checkpoint and surrounding log, assertion failure, and executed test count. Never push these mutations to the fork.

For comparison with the original assertion, use #5225's test in a separate scratch tree with Security Manager explicitly disabled and the same unrelated-error mutation. This is a synthetic baseline. Verify whether its generic alternative accepts the unrelated failure; record the actual outcome. Do not claim a pre-existing failure rate or a production security defect. One well-classified comparison is enough; no unbounded retry loop.

## Completion

GREEN requires all four final-head legs, the expected skips only, packaging checks, clean formatting/tree, and a control that rejects the unrelated failure even with stale matching evidence. If a control does not discriminate, report it and repair before calling the assertions proven. A test run failure is investigated and repaired under WORKFLOW.md, not silently relaunched. An environment attempt that never starts tests may be repaired and relaunched once, with the reason recorded.

Publish an add-only report and the gate host's receipt at the exact verified head. Include command/JDK/parser matrix, fresh counts and skips, raw evidence paths, all mutations, and final fork SHA. Finish the receipt with an updated PR description draft that gives the observed results and limits. No PR opening, comment, JIRA write, or upstream write is authorized by this job.
