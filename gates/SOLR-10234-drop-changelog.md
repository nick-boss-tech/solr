# Gate job: SOLR-10234 drop the test-only changelog fragment

Status: DONE

- Branch: solr-10234-submit. Live tip: eb8c4060d9436d7abfdcfdd0ba231a5b31debc01 (ls-remote, 2026-10-14). Gated head: the same (receipts/SOLR-10234.md, GATE GREEN 2026-10-12).
- Filed by: the Windows review lane, 2026-10-14. Status: DONE. GREEN at 8745afd00cf99597c24ee12764fc7b3d7dc7870e; fork force-with-lease pushed from eb8c4060d9436d7abfdcfdd0ba231a5b31debc01 and verified by ls-remote; receipts/SOLR-10234.md refreshed.
- Intended host: a Linux host with the Gradle toolchain (capability tag: gate), under the WORKFLOW.md claim rules. Windows does not build.
- Decision recorded: drop the fragment. Recommendation in material/solrcloud-round-1-answers.md (SOLR-10234 section, second call), adopted under the decision default. Basis: part p5 finding 16 (type `other` for a test-only change; dev-docs/changelog.adoc line 88 says most such changes are too minor for an entry).
- Keep: the suppression on the base classes (the recorded recommendation, same section).
- Module: :solr:core. Focused class: org.apache.solr.cloud.CloudTestBaseFileSystemsTest (1 test).

## Branch changes

1. Delete changelog/unreleased/SOLR-10234-cloud-tests-no-handle-limit.yml.
2. Message only: remove the body line "Hypothetical, unrun regression test; see SOLR-10234-TESTING.md." from commit 48784679f96. Rebuild the commits above it with the same trees, subjects, authors and dates. Only the commit messages and the deleted fragment may change.
- Push: fork-lease push only, pinned to the old tip: `.\research\push-fork-lease.ps1 -Worktree '<ticket worktree>' -Branch 'solr-10234-submit' -ExpectedSha 'eb8c4060d9436d7abfdcfdd0ba231a5b31debc01'` (AGENTS.md, fork submit branches). Never push to apache/solr.

## Proof shape

Unchanged from receipts/SOLR-10234.md: the premise leg at the merge base (1 run, 1 failure, "SolrCloudTestCase [ExtrasFS]") and the head leg (1 run, 0 failures). Counts from fresh JUnit XML after cleanTest. The new head changes no Java code, so the head leg must still run at the new head.

## Gate steps

- [x] claim recorded under claims/ (WORKFLOW.md)
- [x] branch changes 1 and 2 applied; tree diff against eb8c4060d9 recorded (expect the fragment only)
- [x] tidy with clean tree afterwards (GATE INVALID guard)
- [x] Error Prone compile of :solr:core:compileTestJava
- [x] focused test CloudTestBaseFileSystemsTest at the new head
- [x] module check :solr:core:check -x test
- [x] push (fork lease, pinned as above) and ls-remote verification

## On green

Write receipts/SOLR-10234.md as a new top section at the new head, mark this job DONE and the claim DONE. The review lane then re-checks pr-drafts/solrcloud/SOLR-10234.md: the draft has no changelog line, and its head citations move to the new head.
