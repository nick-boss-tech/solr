# Gate job: SOLR-17433 review pass 1 (unit 19, vm3)

Status: DONE (GATE GREEN at head `42b6c4fc9158524ceb6b91aa2f4c6583a387c6f9`, 2026-10-21T05:24:51Z; receipts/SOLR-17433.md)

- Branch: `solr-17433-17143-submit`. Head: `42b6c4fc9158524ceb6b91aa2f4c6583a387c6f9`. Base: `14c7aac0d1`.
- Intended host: vm3. Capability tags: gate.
- Module: `:solr:solrj-streaming`. Focused class: `org.apache.solr.client.solrj.io.SolrClientCacheTest`. Seed: default.
- Diff scope against `14c7aac0d1`: 3 files, 55 insertions, 8 deletions: solr/solrj-streaming SolrClientCache.java, SolrClientCacheTest.java, solr-ref-guide streaming-expressions.adoc (+1). No changelog in the branch.
- Why: the pass 1 regate. The receipt's gate is from 2026-10-04 and predates the reconciliation commit's current tree.
- Runner: `/workspace/tools/gate-solr-17433-p1.sh`. Worktree `/workspace/worktrees/solr-17433-p1`. Log: `gates/logs/vm3/solr-17433-review-pass-1/`.

Links: Ticket or parent: SOLR-17433 (with SOLR-17143 folded in). Category: 4, SolrJ and clients. Siblings: none known. Supersedes: none.

## What to run

- Step 0: changelog YAML parse of `changelog/unreleased` (the branch adds none; the parse is the gate's check of the tree).
- Step 1: tidy. Step 2: Error Prone compile of `:solr:solrj-streaming` main and test, with `--rerun`.
- Step 4: focused `SolrClientCacheTest`, counted from fresh XML.
- Step 5: `:solr:solrj-streaming:check -x test`.

## Decisions

| Decision | Answer or default | Condition and action | Owner |
| --- | --- | --- | --- |
| Repair scope | default: WORKFLOW.md Gate repairs only | a finding outside that scope is recorded, not fixed | vm3, rung 4 |
| Unbounded request timeout on cached SolrJ clients | default: recorded as a behaviour change, not repaired | a choice about streaming timeouts; owner's call | owner |
