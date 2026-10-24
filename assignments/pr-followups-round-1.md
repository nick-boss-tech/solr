# Assignment: pr-followups-round-1 (follow-ups on our open PRs and posted notes)

- Status: OPEN. Intended host: any host with the `draft` or `records` tag. Posted by phone agent on behalf of Nick Shanin, 2026-10-23.
- Scope: drafts and records only. Nothing posts to Jira or GitHub under this assignment; every draft is a deliverable for the owner's go.

## Items

1. **SOLR-17988 follow-up note (review, then draft).** First, a cross-review of the audit: a host other than vm6 (the audit's author) re-checks the six headline gaps against the 9.x and 10.0 trees at the audit's cited lines and commits (`SOLR_PORT`, `SOLR_HOST`, `SOLR_ADMIN_UI_DISABLED` to `SOLR_UI_ENABLED`, `SOLR_REQUESTLOG_ENABLED`, `SOLR_WAIT_FOR_ZK`, `bin/post`), and records the verdict as a new section in the report file: confirmed, corrected, or not reproduced, per gap. The note is drafted only from gaps the review confirms. Then the draft: The property-transition audit is DONE and found real gaps: `reports/solr-17988-property-audit-round-1.md` on this board (name-level comparison of the 9.x and 10.0 trees). The posted note on the ticket invited exactly this: name the missing transitions. Draft the follow-up as a status note under `comment-formula.md`, naming the user-facing gaps with their evidence: `SOLR_PORT` to `SOLR_PORT_LISTEN` (silently ignored, upgrade notes silent, two 10.0 doc pages still using the old name), `SOLR_HOST` to `SOLR_HOST_ADVERTISE` (silently ignored; the note says deprecated where the code removes it), `SOLR_ADMIN_UI_DISABLED` replaced by the inverted `SOLR_UI_ENABLED` (undocumented; a user who disabled the UI gets it back with no warning), `SOLR_REQUESTLOG_ENABLED` and `SOLR_WAIT_FOR_ZK` renames (silently ignored, undocumented), and the `bin/post` removal missing from the upgrade notes. Deliverable: `jira/jira-comment-SOLR-17988-followup-draft.md` plus a COMMENTS.md entry.
2. **PR #5117 reply (draft).** A commenter asked on 2026-10-23 whether the PR can now be rebased "now that PR #5117 is merged"; #5117 is itself the PR in question and it is open, not merged (the premise is mistaken, likely a wrong number). Draft a brief, friendly reply stating that #5117 is still open, in the GitHub comment shape (header form). Deliverable: the draft text appended to this assignment's report file, `reports/pr-followups-round-1.md`.
3. **Watch items (no drafting).** Replies on SOLR-18470 and a Smiley review on PR #5202 are the live threads. If either lands, record it here with a timestamp; any response is drafted as a new item under this assignment, not posted.

## Rules

- One item per claim slice is fine; mark the item you take in your claim.
- Every draft follows `comment-formula.md` and is listed in `jira/COMMENTS.md` in the same push sequence.
- Mark this assignment DONE when items 1 and 2 are delivered; item 3 stays open as a standing watch and moves to the next round.
