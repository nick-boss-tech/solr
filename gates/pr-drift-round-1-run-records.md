# Gate job: PR drift round 1, the missing run records

- Spec: reports/pr-drift-round-1.md, owner decisions section. Four live PR bodies carry run claims that no record backs. This job makes the runs and records them; it does not edit any PR body. Where a run cannot back a claim, the completion note at the end of this file names the sentence for cutting, and the cutting happens in the drift settlement, not in this job.
- Intended host: vm2 or vm1, under the WORKFLOW.md claim rules. Windows never claims build work.
- Status: OPEN (posted 2026-10-12 by the main side).
- Capability tags: gate.
- Job type: record runs only. The four items are independent: a claimant may take one item or all four, and records per-item status in this file. No branch changes, no pushes to a submit branch.
- Common rules: every run goes through the claiming host's normal gate route (on vm1, ~/workspace/tools/solr-gradle.sh; never ./gradlew), counts come from fresh JUnit XML, and the seed used is recorded with the result. A base run means the branch's test tree against the branch's merge-base production; compute the merge-base with upstream main at claim time and record it. Each item's result is written into the ticket's receipt on pr-prepare.

## Item (a): SOLR-18280, TestReplicationHandler at the head and the base comparison

- Branch: solr-18280-submit. Head: 0da92abd9e96616ed817a25da47078c753766119 (live tip; receipts/SOLR-18280.md records NO GATE and no local counts).
- Missing record: the body claims a run dated 2026-10-04, a "2 of 2", "24 of 25" runs (twice), and a base comparison. None of it is recorded (reports/pr-drift-round-1.md).
- Runs, module `:solr:core`, focused class org.apache.solr.handler.TestReplicationHandler:
  1. At the head 0da92abd9e96, counted from fresh JUnit XML.
  2. The base comparison the body cites: the same class, the branch's test tree against merge-base production.
- Record both results in receipts/SOLR-18280.md. In the completion note, state for each body claim named above whether it is now recorded or should be cut, quoting any sentence that should be cut.

## Item (b): SOLR-7394, the two fail-before runs

- Branch: solr-7394-recovery. Head: 9857d9ee80297784bd7dea9968a5943fe4d7a3e6 (GATE GREEN per receipts/SOLR-7394.md; head counts on record: ZkShardTermsRecoveryFailureTest 1 of 1, RecoveryStrategyTest 1 of 1).
- Missing record: the fail-before claims for ZkShardTermsRecoveryFailureTest and RecoveryStrategyTest. The receipt's recorded proof covers ShardTermsTest only.
- Runs, module `:solr:core`: org.apache.solr.cloud.ZkShardTermsRecoveryFailureTest and org.apache.solr.cloud.RecoveryStrategyTest, each with the branch's test tree against merge-base production, counts and failure content from fresh JUnit XML.
- Record the results in receipts/SOLR-7394.md. If a class does not fail on base production, the completion note names the body's fail-before sentence for that class for cutting, quoted.

## Item (c): SOLR-18178, the DeleteBackupCmdTest base run

- Branch: solr-18178-verify. Head: b75e7d4d3c464ceeb9f14fdaf7711212adcccac2 (GATE GREEN per receipts/SOLR-18178.md).
- Missing record: the body sentence about DeleteBackupCmdTest has no run record behind it (reports/pr-drift-round-1.md).
- Run, module `:solr:core`: org.apache.solr.cloud.api.collections.DeleteBackupCmdTest with the branch's test tree against merge-base production, counted from fresh JUnit XML.
- Record the result in receipts/SOLR-18178.md, or name the sentence for cutting in the completion note, quoted.

## Item (d): SOLR-11431, the TestCoreContainer total

- Branch: solr-11431-core-init-503 (the branch name is not the plain submit form). Head: 968fad873c4b35fff0605ae88dbecca16d1dfa2f (GATE GREEN per receipts/SOLR-11431.md, which records TestCoreContainer at 25 tests, 0 failures, 3 skipped, from fresh JUnit XML at seed 11431C0FFEE002).
- The question: the body says 25 tests, and the source at the head has 24 `@Test` methods (counted in the source at 968fad873c4b on 2026-10-12; reports/pr-drift-round-1.md names the same question).
- Run, module `:solr:core`: focused org.apache.solr.core.TestCoreContainer at the live tip. Count the testcases in the fresh JUnit XML and the `@Test` methods in the source at that head, and record both counts in receipts/SOLR-11431.md with what accounts for the difference (a parameterized or repeated invocation, or a counting error).
- The completion note states the true total the body should carry.

## Completion note (the claimant fills this in)

- Item (a) SOLR-18280: head run recorded / base comparison recorded / sentences named for cutting:
- Item (b) SOLR-7394: ZkShardTermsRecoveryFailureTest fail-before recorded / RecoveryStrategyTest fail-before recorded / sentences named for cutting:
  - Run failure, 2026-10-13T09:45Z: the base leg `7394-base` returned rc=1 at `:solr:core:compileTestJava`, before any test ran (log `logs/vm2/prdrift-7394-base.log`, 18 errors, e.g. `ShardTermsTest.java:98` cannot find `recoveryFailed(String)` on base `ShardTerms`). The head test tree does not compile against base production, so neither fail-before result exists. Not relaunched; left for the main side to rule on the fail-before sentences.
- Item (c) SOLR-18178: base run recorded / sentence named for cutting:
- Item (d) SOLR-11431: true TestCoreContainer total:

## Step checklist

- [ ] item (a): TestReplicationHandler at the head and the base comparison run; results in receipts/SOLR-18280.md; completion note filled
- [ ] item (b): both fail-before runs on base production; results in receipts/SOLR-7394.md; completion note filled
- [ ] item (c): DeleteBackupCmdTest base run; result in receipts/SOLR-18178.md, or the sentence named for cutting; completion note filled
- [ ] item (d): TestCoreContainer rerun at the live tip; XML testcase count and source `@Test` count reconciled in receipts/SOLR-11431.md; completion note filled
- [ ] job and claim DONE
