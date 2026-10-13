# Claim: SOLR-18368 re-gate at the live tip (vm2, gate backlog coordinator)

- Claimed: 2026-10-13T10:52Z by vm2. Job: gates/SOLR-18368-tip-regate.md (restock of 2026-10-13T10:39Z).
- Head at claim: a7ec9a1b65c05b2e359e9a186fc2938fe7b7ca7d (solr-18368-submit live tip). Last green gate: 1acaced80b2.
- Module: :solr:solrj. Focused class: org.apache.solr.client.solrj.impl.CloudHttp2SolrClientMultiConstructorTest (package confirmed by grep at the tip).
- Capability tags: gate (no implementation; the tip is a test removal, no fix commit, no fork push).
- Runner: gates/g18368-gate.sh, worktree /workspace/gates/solr-18368-gate (detached).
- Status: CLAIMED, runner queued.
