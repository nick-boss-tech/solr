# Skip record: SOLR-13933

- Ticket: SOLR-13933, Cluster mode Stress test suite. JIRA read 2026-10-19 (this pass, evidence tool): status Open, resolution None, created 2019-11-14, updated 2021-07-14, no fix versions, no links; eight comments, the last on 2021-07-14, which describes a stress harness in an external repository.
- Verdict: keep-open.
- Draft branch: solr-13933-draft-open (the existing keep-open branch; this record replaces its draft files).
- Pinned head: fork origin/main 498a09aea97aac86c2cc7ac114113b946d249942. Grep run at this head (detached worktree).
- Source: remediation redo of batch 05, item 14, under assignments/pool-aaa-skip-verdict-remediation-05.md. The earlier record (vm2, from the C reading) described the workflows as "only a nightly Docker build"; that is corrected below.

## Ticket reading

The ticket asks for a stress test suite of many nodes and many collection API and Overseer operations, run nightly and published, to find stability problems, benchmark collection operations, and check changes. The thread (ichattopadhyaya, 2019-11 to 2021-07) describes a harness developed in an external repository (solr-bench), not in this tree.

## Grep and listing at the pinned head

- `git ls-tree --name-only 498a09aea97aac86c2cc7ac114113b946d249942 solr/benchmark/src/java/org/apache/solr/bench/` lists bench classes (SolrBenchState, SolrGenerate, Docs, index, lifecycle, generators); these are JMH-style benchmark tasks, not a cluster stress suite.
- `git ls-tree --name-only 498a09aea97aac86c2cc7ac114113b946d249942 .github/workflows/` lists sixteen workflows, among them docker-nightly.yml, docker-test.yml, solrj-test.yml, bin-solr-test.yml and others. None of them is a cluster-scale stress run. (The earlier record's "only a nightly Docker build" was too narrow; the list has more workflows, none of them a stress harness.)
- `git grep -l -i "stress" 498a09aea97aac86c2cc7ac114113b946d249942 -- '.github' 'solr/benchmark'` returns one file, solr/benchmark/src/resources/words.txt, which is a word list. No stress harness is in the tree.

## History

- No commit on the pin names SOLR-13933.

## Reproduction attempts

Not applicable. There is no suite in the tree to run.

## Gap analysis

The requested suite is absent from this repository. The thread says the harness lives in an external project, so the decision is where the suite lives and how it runs nightly (the thread asks whether Apache Jenkins can run it). That is a project decision, not a defect in the code.

## Verdict justification

keep-open: the attempts made are the listing and grep above at the pinned head, the Jira read, and the thread read. The missing piece is the project decision on where the stress suite lives and how it runs, and the harness itself. close-obsolete and close-fixed are not supported: the request has not been met and is not obsolete. pr-needed is not supported: this is a feature request with no defect to fix in this tree.

Missing for a closing verdict: the decision on the suite's location, or a split into a tracked external project.

## Limits

This record does not run a stress suite. It does not post anything to JIRA or GitHub. The draft comment is a draft until someone posts it by hand.
