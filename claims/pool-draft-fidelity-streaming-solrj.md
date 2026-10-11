# Claim: draft fidelity review, Streaming expressions and SolrJ drafts (20 drafts)

- Claimed by: windows (review agent), Claude Code on this host, working for Nick Shanin
- Capability tags: review
- Assignment: `assignments/pool-draft-fidelity-streaming-solrj.md`
- Date: 2026-10-11
- Slices, one subagent each, all 20 drafts in scope (live-PR consistency drafts SOLR-13524, 10198, 15823 and 18129 are out of scope):
  - Slice 1: Streaming SOLR-10322, 12505, 12657, 14231; SolrJ SOLR-2018.
  - Slice 2: SolrJ SOLR-3722, 3999, 4335, 4336, 4422.
  - Slice 3: SolrJ SOLR-4424, 5220, 6046, 7709, 8536.
  - Slice 4: SolrJ SOLR-12094, 14298, 14967, 17866, 18341.
- Split: four subagents in one wave. This claim is taken now and runs in the wave after the CLI, Security, Build/docs and Metrics claim, so no more than six subagents run at once.
- Deliverables: `reports/draft-fidelity-streaming-solrj.md` (one verdict per draft: CONSISTENT, or DRIFT with the exact replacement text per item), with part reports `reports/draft-fidelity-streaming-solrj-s1.md` to `-s4.md`.
- Scope: read-only on `pr-drafts/`, `receipts/` and the fork. No edits to drafts (the main side applies fixes). No PR, comment, submit-branch or PR-description write. No builds, Gradle, tests or gate runs.
- Heartbeat: 2026-10-11T02:36Z (claim taken).
- Status: ACTIVE, queued behind the first wave.
