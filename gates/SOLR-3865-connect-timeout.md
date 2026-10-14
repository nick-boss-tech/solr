# Gate job: SOLR-3865 connect timeout and commit-message cleanup

- Branch: solr-3865-submit. Live tip: f483bc4c32e033ef797e16528b9e021dca56770a (ls-remote, 2026-10-14). Gated head: the same (receipts/SOLR-3865.md, GATE GREEN 2026-10-11).
- Filed by: the Windows review lane, 2026-10-14. Status: OPEN, not claimed.
- Intended host: a Linux host with the Gradle toolchain (capability tag: gate), under the WORKFLOW.md claim rules. Windows does not build.
- Why: owed before first gate per material/solrcloud-round-1-answers.md (SOLR-3865 section), part p5 findings 1 and 7. The gate ran without these items, so the gated head does not carry them.
- Module: :solr:solrj-zookeeper. Focused class: org.apache.solr.client.solrj.impl.ZkClientClusterStateProviderCloseTest (2 tests).

## Branch changes

1. Test only. In solr/solrj-zookeeper/src/test/org/apache/solr/client/solrj/impl/ZkClientClusterStateProviderCloseTest.java, method testCloseIsIdempotent: insert `provider.setZkConnectTimeout(200);` after the constructor line, the same line the first method uses.
2. Message only. Remove the body line "Hypothetical, unrun regression test; see SOLR-3865-TESTING.md." from commit d145b5db774. Rebuild the commits above it with the same trees, subjects, authors and dates. Apply the test line in the rebuilt chain. The final tree must differ from f483bc4c32e0 only by the one test line.
- Push: fork-lease push only, pinned to the old tip: `.\research\push-fork-lease.ps1 -Worktree '<ticket worktree>' -Branch 'solr-3865-submit' -ExpectedSha 'f483bc4c32e033ef797e16528b9e021dca56770a'` (AGENTS.md, fork submit branches). Never push to apache/solr.

## Proof shape

- Premise leg: production ZkClientClusterStateProvider.java reverted to the merge base cabedd1d9680, branch test kept. Both tests must fail with the connect-failure content (TimeoutException while connecting to 127.0.0.1:1), not a compile error or a hang. Record the time testCloseIsIdempotent takes to fail; expect well under 15 seconds now.
- Head leg: both tests pass at the new head. Counts from fresh JUnit XML after cleanTest.

## Gate steps

- [ ] claim recorded under claims/ (WORKFLOW.md)
- [ ] message-only rebuild and test line; tree diff against f483bc4c32e0 recorded (expect one test line)
- [ ] changelog YAML parse (changelog/unreleased/SOLR-3865-zk-provider-close-before-use.yml)
- [ ] tidy with clean tree afterwards (GATE INVALID guard)
- [ ] Error Prone compile of :solr:solrj-zookeeper:compileTestJava
- [ ] premise leg at the merge base (above)
- [ ] focused tests at the new head, fresh JUnit XML
- [ ] module check :solr:solrj-zookeeper:check -x test
- [ ] push (fork lease, pinned as above) and ls-remote verification

## On green

Write receipts/SOLR-3865.md as a new top section at the new head, mark this job DONE and the claim DONE. The review lane then re-checks pr-drafts/solrcloud/SOLR-3865.md: head citations move to the new head, and the Proof sentence about the 15-second failure takes the new figure.
