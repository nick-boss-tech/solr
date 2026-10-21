# Decision log, 2026-10-21 (vm3)

Decisions taken with the ladder (material/decision-ladder.md) on the open review items. Each entry gives the rung that settled it. None of these is an owner question; the items that remain open are named as work, not as questions.

## D1. SOLR-17433, request timeout on cached SolrJ clients (review unit 19)

- Question: the branch sets an unbounded request timeout on every client built by `SolrClientCache`. Is that wider than the ticket?
- Rung 1 (code): every caller of `SolrClientCache` is a streaming, graph, SQL or commit-stream path: `StreamHandler`, `ExportHandler`, `GraphHandler`, `CrossCollectionJoinQuery`, `StreamTool`, `GatherNodesStream`, `CommitStream`, `ModelCache`, the SQL module and `ConnectionImpl`/`ResultSetImpl`. No caller uses the cache for ordinary SolrJ requests. So "every cached client" means every streaming client.
- Rung 3 (intent): the ticket says the reporter ideally wants no timeout while a stream keeps returning content.
- Decision: keep the branch behaviour (no request timeout on cached clients). Keep the 60 second connect and socket floors, which the ticket does not ask to remove. Record the behaviour in the PR body as stated behaviour.
- Remaining limit, not a question: no test runs a stream past 60 seconds. The receipt says so.

## D2. SOLR-17297, module-stage SPI orphaning (review unit 18)

- Question: the ticket's classloading failure moved. Does the branch remove it?
- Rung 2 (probe): the round-35 probe ran at head `c0ab38fc0a` and confirmed that a module-stage SPI class is orphaned when the shared-lib step closes the module loader (receipts/SOLR-17297.md, round 35).
- Rung 1 (code): `SolrResourceLoader.addToClassLoader` closes the previous loader on every call. The reorder moves the close; it does not remove it.
- Decision: the branch does not fully resolve the ticket's mechanism. Record it as an open defect of the ticket, with the probe as the evidence. The fix is an implementation change (one combined add, or no close of a loader still in use). It is not a review repair and is not an owner question.

## D3. SOLR-16914, Japanese synonym docs (review unit 15)

- Question: the branch documents the synonym behaviour. Does that resolve the Critical bug report?
- Rung 3 (intent): the ticket reports a config that fails. The intent is that the config works.
- Decision: a docs-only branch does not resolve the report. Keep unit 15 NEARLY. The next step is a rung 2 probe: reproduce the reporter's config at the branch head. The guide build still needs an npm host.

## D4. SOLR-18552, solr.cmd default on the 9.x line (review units 4 and 5)

- Question (from vm5's NEEDS-OWNER note): add `sharedArenaMaxPermits=64` to `solr.cmd` on the 9.x line?
- Rung 1 (code): the main-line branch sets the option in both launchers: `solr/bin/solr:726` and `solr/bin/solr.cmd:793` on `origin/solr-18552-submit`. The 9.x branch sets `bin/solr` only.
- Decision: yes, add the `solr.cmd` line on the 9.x branch for parity with main. The changelog entry follows the main line too. This is applied by the unit 5 claim owner, under the fork-branch push rule. It is not an owner question.

## Not decided here

- Any PR opening or flip to ready, and any public JIRA or PR comment. These stay with the owner under the standing rules.
