# Claim: draft fidelity review, CLI, Security, Build/docs and Metrics drafts (16 drafts)

- Claimed by: windows (review agent), Claude Code on this host, working for Nick Shanin
- Capability tags: review
- Assignment: `assignments/pool-draft-fidelity-cli-security-builddocs-metrics.md`
- Date: 2026-10-11
- Slices, one subagent each, all 16 drafts in scope:
  - Slice 1: CLI SOLR-9342, 16272, 16813, 17029.
  - Slice 2: CLI SOLR-17598 (NO GATE), 18132, 18339; Security SOLR-10627.
  - Slice 3: Security SOLR-18368 (Resolved upstream); Build-docs SOLR-3684, 5821, 16914.
  - Slice 4: Build-docs SOLR-17252, 17752, 17842; Metrics SOLR-17987 (two owner flags).
- Split: four subagents in one wave. The wave runs alongside the Highlighting leftovers claim (`claims/pool-highlighting-leftovers-2632-16885.md`), six subagents in total, which is the standing cap. The Streaming and SolrJ claim runs in the next wave.
- Deliverables: `reports/draft-fidelity-cli-security-builddocs-metrics.md` (one verdict per draft: CONSISTENT, or DRIFT with the exact replacement text per item), with part reports `reports/draft-fidelity-cli-security-builddocs-metrics-s1.md` to `-s4.md`.
- Scope: read-only on `pr-drafts/`, `receipts/` and the fork. No edits to drafts (the main side applies fixes). No PR, comment, submit-branch or PR-description write. No builds, Gradle, tests or gate runs. Live PRs are read with the read-only GitHub wrapper only.
- Heartbeat: 2026-10-11T02:36Z (claim taken).
- Status: ACTIVE.
