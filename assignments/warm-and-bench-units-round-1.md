# Assignment: more warm and bench units (round 1)

Status: OPEN
Tags: premise-run, draft, records

Four separately claimable units. Drafts follow the comment formula and the scoped-comments list if the ticket is on it; nothing posts; every text needs the owner's explicit go.

## Unit 1 (misc box, bench): SOLR-18571 SolrIndexSearcher footprint measurement

Jan Høydahl's ticket asks how many SolrIndexSearcher instances are retained and why. The missing input is a measurement, not a comment: bench a standalone Solr on the claiming host, open and reload searchers against a small index under the ticket's described pattern, and report retained searcher count and heap attribution over time. Deliverables: `reports/misc-solr-18571-searcher-footprint-round-1.md` with the method, the numbers, and the raw-data location; and, only if the numbers answer the ticket's question, `jira/jira-comment-SOLR-18571-draft.md` carrying them. Frame limits like the round-1 benchmark: one host, one run shape, no generalization.

## Unit 2: SOLR-18473 PULL nomenclature audit (Jan Høydahl's ticket)

Audit where the replica-type terminology in code, config, and docs diverges from the ticket's described problem (PULL replicas described with leader-follower language). Each divergence cited to file and line. Deliverables: `reports/solr-18473-pull-nomenclature-audit-round-1.md` and, if the divergences are concrete, `jira/jira-comment-SOLR-18473-draft.md` with the list and an offer to do the rename.

## Unit 3: SOLR-18369 BulkV2 equivalence audit (Eric Pugh's ticket)

Establish, from the code, whether the bulk v2 APIs cover the v1 bulk operations the ticket names, operation by operation. Deliverables: `reports/solr-18369-bulkv2-equivalence-audit-round-1.md` and, if gaps are real, `jira/jira-comment-SOLR-18369-draft.md` carrying the gap table and asking which gaps he wants closed first.

## Unit 4: SOLR-18460 CoreAdmin v2 test port scoping (Eric Pugh's ticket)

Scope what porting the CoreAdmin API tests to v2 involves: the test classes in scope, the v2 endpoints they would exercise, and the blockers, if any, where a v2 endpoint is missing. Deliverables: `reports/solr-18460-coreadmin-v2-testport-scoping-round-1.md` and, if the scope is clean, `jira/jira-comment-SOLR-18460-draft.md` carrying the scope and offering the port.
