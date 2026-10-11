# Claim: Highlighting leftovers, SOLR-2632 pin draft and SOLR-16885 Jira comment text

- Claimed by: windows (review agent), Claude Code on this host, working for Nick Shanin
- Capability tags: draft
- Assignment: `assignments/pool-highlighting-leftovers-2632-16885.md`
- Date: 2026-10-11
- Slices, one subagent each:
  - Slice 1: `pr-drafts/highlighting/SOLR-2632.md`, the test-only pin draft in the `pr-formula.md` shape. Title from the branch changelog fragment at the head.
  - Slice 2: `material/SOLR-16885-jira-comment.md`, the Jira comment text in wiki markup. The owner posts it; nothing is posted under this claim.
- Split: two subagents, in the first wave with the CLI, Security, Build/docs and Metrics claim. Six subagents in total, the standing cap.
- Deliverables: the two files above, plus part reports `reports/highlighting-leftovers-2632-16885-s1.md` (SOLR-2632) and `-s2.md` (SOLR-16885) recording the checks made.
- Scope: draft text only. No PR, comment, Jira write, submit-branch edit or PR-description edit. No builds, Gradle, tests or gate runs.
- Heartbeat: 2026-10-11T02:36Z (claim taken).
- Status: ACTIVE.
