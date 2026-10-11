# Claim: SOLR-13097 merge conflict analysis (read-only)

- Claimed by: windows (review agent), Claude Code on this host, working for Nick Shanin
- Capability tags: review
- Assignment: `assignments/pool-solr-13097-conflict-analysis.md`
- Date: 2026-10-11
- Staffing: 1. One subagent computes the conflict surface with a trial merge in a scratch clone, which is removed afterwards.
- Scope: read-only. No change to the branch, no rebase on the real branch, no push to the branch ref, no live PR edit, no comment, no build, test or gate. The scratch clone is git only.
- Deliverable: `reports/solr-13097-conflict-analysis.md`, with the three tips and the merge-base up front, the conflict surface by file and hunk, the preserve list, and the body-claim flags.
- Heartbeat: 2026-10-11T13:47Z (claim taken).
- Status: ACTIVE.
