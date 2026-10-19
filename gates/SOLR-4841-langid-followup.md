# Gate job: SOLR-4841 language-detection follow-up (test-only, custom identifier through processAdd)

Status: DONE 2026-10-19 (gate GREEN at bf1b01d0dc, receipts/SOLR-4841-followup.md; review rounds 2 and 3 by vm5 SATISFIED, reports/SOLR-4841-followup-round-1.md; the new ticket and PR opening are Nick's, see assignments/pool-solr-4841-langid-followup-round-1.md).

- Branch: solr-4841-langid-followup-submit on the fork (https://github.com/nick-boss-tech/solr). Head at filing: bf1b01d0dc (ls-remote matched). The earlier head 92567d75c4 failed tidy on import order and is superseded by bf1b01d0dc, a fast-forward commit.
- Base: apache/solr upstream main c8ab71430f. The merged change a99ff066f4 is on apache main as a squash commit under a different hash; the pre-fix legs use the same tree with the one-line constructor revert.
- Module: :solr:modules:langid.
- Ticket: no new JIRA key yet. Commits carry SOLR-4841 for now; the PR title and commit key change when Nick files the ticket. Public write, Nick's call.
- Changelog decision: NO new changelog fragment. Test-only follow-up for an existing ticket; its production change shipped with its own fragment in the merged PR. The branch adds no fragment.
- Runner: /tmp/vm4-gate-4841.sh on vm4, under the vm4 Gradle lock, from the worktree /workspace/worktrees/langid-4841 (a real worktree of /workspace/solr). JDK 21.0.12.1, Gradle 9.7.0 from the branch wrapper, `NODE_ENV` unset. Seed for focused tests 4841C0FFEE4841.

## Steps

- [x] 0. Head check: head = bf1b01d0dc. Log: /workspace/gates/logs/vm4/SOLR-4841-gate-head.log
- [x] 1. Tidy on the final tree, then the tree must be clean (no changed paths). Logs: SOLR-4841-tidy.log, SOLR-4841-tidy-status.log
- [x] 2. Error Prone compile, `-Pvalidation.errorprone=true`, main and test sources of :solr:modules:langid. Log: SOLR-4841-errorprone.log
- [x] 3. Pre-fix leg, does not compile at the parent state (DetectedLanguage constructor not public). Ran on bf1b01d0dc with the constructor reverted. Quoted error: "DetectedLanguage(String,Double) is not public in DetectedLanguage" in ContentLanguageIdentifierFactory.java:67, and in CustomLanguageIdentifierTest.java:40 and :45 (the merged test). Log: /workspace/gates/logs/vm4/SOLR-4841-followup-prefix-compile.log. Rerun on bf1b01d0dc is not needed: the only change since is an import order.
- [x] 4. Focused head run, CustomLanguageIdentifierProcessingTest and CustomLanguageIdentifierTest, counted from fresh JUnit XML. Log: SOLR-4841-focused.log
- [x] 5. Module check, `:solr:modules:langid:check -x test`. Log: SOLR-4841-check.log

## Earlier run, not counted

- A focused run on 92567d75c4 passed 1 of 1 and 1 of 1 with no skips (log SOLR-4841-followup-focused.log, later removed; the head changed only in import order after it). Not used as the gate count.
