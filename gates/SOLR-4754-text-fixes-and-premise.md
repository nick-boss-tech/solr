# Gate job: SOLR-4754 text fixes and premise run

Status: DONE

- Branch: solr-4754-submit. Live tip: e4ea4bfde80ea027f005fe138b0285f6c2e5a689 (ls-remote, 2026-10-14). Gated head: the same (receipts/SOLR-4754.md, GATE GREEN 2026-10-11, compile-level proof shape).
- Filed by: the Windows review lane, 2026-10-14. Claimed by vm1 on 2026-10-15 (claims/pool-vm1-gate-4754.md).
- Intended host: a Linux host with the Gradle toolchain (capability tag: gate), under the WORKFLOW.md claim rules. Windows does not build.
- Decision recorded: keep the scheme-only guard and its link. Recommendation in material/solrcloud-round-1-answers.md (SOLR-4754 section), adopted under the decision default. The disclosure that the ticket's empty value already falls back is in pr-drafts/solrcloud/SOLR-4754.md.
- Why: the text fixes and the premise run are owed per that answers section and part p5 findings 2, 3 and 4. None of them is in the gated head. The draft stays on HOLD until this job lands.

## Branch changes (message and text only; no new tests)

1. solr/core/src/java/org/apache/solr/cloud/ZkController.java, normalizeHostName, error text: replace the "-Dhost" wording with the property the shipped config reads, `solr.host.advertise` (solr/server/solr/solr.xml line 42 reads `${solr.host.advertise:}`; solr/bin/solr sets -Dsolr.host.advertise).
2. changelog/unreleased/SOLR-4754-fail-on-empty-host.yml, line 1: replace the title with "Fail node startup with a clear error when the configured host is only a URL scheme (for example host=http://), which would register an empty host name in ZooKeeper."
3. Message only: remove the body line "Hypothetical, unrun regression test; see SOLR-4754-TESTING.md." from commit 390b1938b50. Rebuild the commits above it with the same trees, subjects, authors and dates. The tree must not change except for item 1.

## Premise run (no branch change)

- At the merge base cabedd1d9680, start a node with solr.host.advertise=http:// and record the host name it registers in ZooKeeper. Expected: an empty name, per solr/core ZkController.java line 1016 at the base (the value is returned as is).
- If the registered name is not empty, stop and record the evidence in this file. Do not change the draft on that result.

## Proof shape

- testNormalizeHostName cannot fail on the base: the base does not compile the test (private method). Record the fail-before verdict as INCONCLUSIVE by construction. Do not claim a failing base run.
- Head leg: ZkControllerTest.testNormalizeHostName passes at the new head. Count from fresh JUnit XML after cleanTest.

## Gate steps

- [x] claim recorded under claims/ (WORKFLOW.md)
- [x] branch changes 1 to 3 applied; tree diff against e4ea4bfde80e recorded
- [x] premise run at the merge base (above), log path recorded
- [x] changelog YAML parse
- [x] tidy with clean tree afterwards (GATE INVALID guard)
- [x] Error Prone compile of :solr:core:compileTestJava
- [x] focused test ZkControllerTest.testNormalizeHostName at the new head
- [x] module check :solr:core:check -x test
- [x] push (fork lease pinned to e4ea4bfde80e, per AGENTS.md) and ls-remote verification

## On green

Write receipts/SOLR-4754.md as a new top section at the new head, mark this job DONE and the claim DONE. The review lane then re-checks pr-drafts/solrcloud/SOLR-4754.md against the new head and the premise result. It removes the HOLD comment only if the premise run shows the empty registration, and then fills in the Limits line that says the premise is read from the code.

## Outcome (vm1, 2026-10-16): DONE, GATE GREEN at 3409d4c2bd76

- New head: 3409d4c2bd767644618e4ee70d25fa3595bab62b on solr-4754-submit (one commit on top of e4ea4bfde80e; pushed with the lease pinned to e4ea4bfde80e and verified by ls-remote). Receipt: receipts/SOLR-4754.md, top section.
- Changes 1 and 2 applied in that commit. Tree diff against e4ea4bfde80e is exactly two files: solr/core/src/java/org/apache/solr/cloud/ZkController.java and changelog/unreleased/SOLR-4754-fail-on-empty-host.yml. The branch's test does not assert the old "-Dhost" wording (the only match for the message text in the tree is ZkController.java itself), so no test assertion was updated.
- Change 3 disposition: NOT DONE, by ruling. Item 3 is a message-only rebuild of commit 390b1938b50 and the commits above it with identical trees. The owner ruled on 2026-10-14 that branch history is not rewritten for cosmetic commit-message defects, so the rebuild was not performed; this section is the record the claim file already pointed at. The "Hypothetical, unrun regression test" body line therefore stays in the branch history; the commit trees are unchanged.
- Premise run: HELD, by run. At the merge base cabedd1d9680 a scratch test (PremiseRun4754Test, never committed) booted one node through MiniSolrCloudCluster with a solr.xml whose host entry is the shipped expression ${solr.host.advertise:} and the property set to http://. The node registered in ZooKeeper as the live node ":40479_solr"; ZkController.getHostName() returned "" (empty) and getNodeName() returned ":40479_solr". Log: ~/workspace/tools/g4754-premise.log on vm1 (1 test, 0 failures in the fresh JUnit XML; the registration lines are printed in the run output).
- Fail-before verdict: INCONCLUSIVE by construction, as this file directs. No failing base run is claimed.
- Gate steps at the new head (logs ~/workspace/tools/g4754-tidy.log and ~/workspace/tools/g4754-gate.log on vm1): changelog YAML parse OK rc 0; tidy rc 0 with only the two intended edits in the post-tidy tree; Error Prone compile of :solr:core:compileJava and :solr:core:compileTestJava (-Pvalidation.errorprone=true) rc 0; focused test org.apache.solr.cloud.ZkControllerTest.testNormalizeHostName from fresh JUnit XML after cleanTest, seed 4754C0FFEE4754: 1 run, 0 failures, 0 errors, 0 skipped; module check :solr:core:check -x test rc 0; post-gate tree clean.
- The draft was not edited, per the job. The review lane's re-check of pr-drafts/solrcloud/SOLR-4754.md against the new head and this premise result is the remaining step, and the premise condition for removing the HOLD comment (an empty registration shown by run) is met.
