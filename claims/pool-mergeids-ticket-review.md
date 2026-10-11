# Claim: mergeIds Jira ticket draft review

- Claimed by: windows (review agent), Claude Code on this host, working for Nick Shanin
- Capability tags: review
- Assignment: `assignments/pool-mergeids-ticket-review.md`
- Date: 2026-10-11
- Staffing: 1, the assignment's limit. One subagent reviews the staged draft against the evidence in the assignment.
- Scope: the staged ticket draft `material/mergeids-ticket-draft.md` may be edited for wording and markup only; any fact disagreement is flagged in the report, not fixed. No Jira filing, no email, no comment, no build, test or gate. No branch edits.
- Deliverable: `reports/mergeids-ticket-review.md` with the verdict first (SATISFIED or STILL OPEN), each fix made with before and after text, and each evidence disagreement flagged.
- Heartbeat: 2026-10-11T15:17Z (claim taken); review reported.
- Status: DONE, 2026-10-11. Deliverable: `reports/mergeids-ticket-review.md`, verdict STILL OPEN. Four wording and markup fixes were made in `material/mergeids-ticket-draft.md`. Two evidence disagreements are flagged: fix option 1 omits the approximate total that the code adds before the skip (a main-side decision), and the "only downward adjustment" statement holds only inside the merge. The verdict is not SATISFIED until the first is resolved. Nothing was filed, emailed or posted.
