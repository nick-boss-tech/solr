# Claim: write the PR drafts for the round 2 newly gated branches (3 drafts)

- Claimed by: windows (draft agent), Claude Code on this host, working for Nick Shanin
- Capability tags: draft
- Assignment: `assignments/pool-drafts-newly-gated-round-2.md`
- Date: 2026-10-11
- Slice: one, one subagent (the assignment's staffing is 1).
  - SOLR-14187 (solrj): draft at the receipt's packaged head `29023ec1ecab`. The proof shape is stated plainly: the branch test does not compile at the merge-base, so the premise rests on a base-compilable scratch vehicle.
  - SOLR-11356 (solrj): draft at the receipt's packaged head `6120dae28d04`. The packaging commit is local to vm2 and not pushed; the fork tip is `8474e5a3a26d`. The delta goes in the report.
  - SOLR-6430 (build-docs): docs-only; the receipt is green on a docs-shaped gate. The Proof says exactly that.
- Deliverables: `pr-drafts/solrj/SOLR-14187.md`, `pr-drafts/solrj/SOLR-11356.md`, `pr-drafts/build-docs/SOLR-6430.md`, and the part report `reports/drafts-newly-gated-round-2.md`.
- Scope: draft text only. No PR, comment, Jira write or submit-branch edit. No builds, Gradle, tests or gate runs.
- Heartbeat: 2026-10-11T05:47Z (claim taken).
- Status: ACTIVE.
