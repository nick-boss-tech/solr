# Claim: SOLR-17680 v2 two-router end-to-end test (vm2, gate backlog coordinator)

- Claimed: 2026-10-13T08:16:46Z by vm2. Job: gates/SOLR-17680-v2-two-router-test.md.
- Head at claim: ce59bbe112d063e3d3c93b3be6a1a5269a38fc37 (solr-17680-submit). Merge-base: 56ec140e3636d5f4150fa87fbf7103529536ac99.
- Capability tags: gate, implementation.
- Status: BLOCKED 2026-10-13T08:19:49Z by vm2. The negative case in the job asserts a 400 for a category router with no maxCardinality, but that value is optional at this head and nothing returns router.1.maxCardinality. No code change, no commit, no gate, no fork push. Owner decision needed (see the job file, "Blocked at claim").
