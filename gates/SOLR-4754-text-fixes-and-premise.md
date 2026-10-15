# Gate job: SOLR-4754 text fixes and premise run

Status: OPEN

- Branch: solr-4754-submit. Live tip: e4ea4bfde80ea027f005fe138b0285f6c2e5a689 (ls-remote, 2026-10-14). Gated head: the same (receipts/SOLR-4754.md, GATE GREEN 2026-10-11, compile-level proof shape).
- Filed by: the Windows review lane, 2026-10-14. Status: OPEN, not claimed.
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

- [ ] claim recorded under claims/ (WORKFLOW.md)
- [ ] branch changes 1 to 3 applied; tree diff against e4ea4bfde80e recorded
- [ ] premise run at the merge base (above), log path recorded
- [ ] changelog YAML parse
- [ ] tidy with clean tree afterwards (GATE INVALID guard)
- [ ] Error Prone compile of :solr:core:compileTestJava
- [ ] focused test ZkControllerTest.testNormalizeHostName at the new head
- [ ] module check :solr:core:check -x test
- [ ] push (fork lease pinned to e4ea4bfde80e, per AGENTS.md) and ls-remote verification

## On green

Write receipts/SOLR-4754.md as a new top section at the new head, mark this job DONE and the claim DONE. The review lane then re-checks pr-drafts/solrcloud/SOLR-4754.md against the new head and the premise result. It removes the HOLD comment only if the premise run shows the empty registration, and then fills in the Limits line that says the premise is read from the code.
