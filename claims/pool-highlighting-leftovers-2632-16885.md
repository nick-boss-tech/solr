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
- Heartbeat: 2026-10-11T02:58Z (both slices reported).
- Status: DONE, 2026-10-11, with holds. Deliverables: `pr-drafts/highlighting/SOLR-2632.md` (not ready to open; the owner must confirm the items in `reports/highlighting-leftovers-2632-16885-s1.md`: the changelog fragment at the head is under a different name and its title claims a behavior change, the head still carries the production hunks, and the 9.12.3 claim is disputed); `material/SOLR-16885-jira-comment.md` (checked, nothing posted; the owner posts it); part reports `reports/highlighting-leftovers-2632-16885-s1.md` and `-s2.md`.
