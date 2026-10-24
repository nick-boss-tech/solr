# Assignment: pr-followups-round-1 (follow-ups on our open PRs and posted notes)

- Status: OPEN. Intended host: any host with the `draft` or `records` tag. Posted by phone agent on behalf of Nick Shanin, 2026-10-23.
- Scope: drafts and records only. Nothing posts to Jira or GitHub under this assignment; every draft is a deliverable for the owner's go.

## Items

1. **SOLR-17988 follow-up comment (draft, after the PR).** The evidence review and the docs build for this ticket are decomposed as `assignments/solr-17988-subtasks-round-1.md` (one claim per gap, verification first, then assembly). When that assignment's PR is built and opened on the owner's go, draft the follow-up comment here: it leads with the audit's findings (the posted note's "looks complete" does not hold for the environment variables; the audit's verdict says so in terms), names the serious gaps with their evidence, and points [~epugh] at the open PR for review, per the engaged-sponsor adjustment in `comment-formula.md`. Deliverable: `jira/jira-comment-SOLR-17988-followup-draft.md` plus a COMMENTS.md entry.
2. **PR #5117 reply (draft).** A commenter asked on 2026-10-23 whether the PR can now be rebased "now that PR #5117 is merged"; #5117 is itself the PR in question and it is open, not merged (the premise is mistaken, likely a wrong number). Draft a brief, friendly reply stating that #5117 is still open, in the GitHub comment shape (header form). Deliverable: the draft text appended to this assignment's report file, `reports/pr-followups-round-1.md`.
3. **Watch items (no drafting).** Replies on SOLR-18470 and a Smiley review on PR #5202 are the live threads. If either lands, record it here with a timestamp; any response is drafted as a new item under this assignment, not posted.

## Rules

- One item per claim slice is fine; mark the item you take in your claim.
- Every draft follows `comment-formula.md` and is listed in `jira/COMMENTS.md` in the same push sequence.
- Mark this assignment DONE when items 1 and 2 are delivered; item 3 stays open as a standing watch and moves to the next round.
