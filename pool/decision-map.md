# Decision map

Classify an item before working it. The standing answers below settle most items; a question reaches the owner only if it passes the escalation test at the end.

## Intake (which pool)

1. Follow-ups on our open PRs and live UI claims: always in scope.
2. UI tickets with an assessment verdict of READY TO VOLUNTEER in the round reports: in scope for a staged ask.
3. Today's signals: a Jira updated today whose latest update is by a committer; an email received today; a CI failure from today; a PR posted today; a promise Nick has made. In scope to assess.
4. Everything else: out of scope by default. Priority labels (Blocker, Critical) and the newdev tag are not signals. An old ticket whose filer has moved on is cold.

## Work type and its standing answer

- Assessment (read the code and ticket, verdict with evidence): claim it, produce the per-ticket shape (ask, what exists, what is missing, verdict).
- Comment draft: draft it under `comment-formula.md`, save it under `jira/`, list it in `jira/COMMENTS.md`. Posting needs the owner's go, every time.
- Code change on a UI ticket: only after the staged ask has engagement and a named reviewer. Then it is ordinary implementation plus gate.
- Follow-up on our own open PR: implement at the scope the maintainer wrote; objections go to the owner first, with the design and the cost, never presented as constraints.
- Gate: run it under WORKFLOW.md. Gate state at the exact live tip settles arguments.

## Authority

- Standing: claims, assessments, drafts, receipts, pushes to Nick's own branches, edits to our own PR bodies and comments.
- Owner-approved, each time: opening a PR, flipping a draft PR ready, posting any new comment, filing a Jira ticket.
- Never: deleting a branch (retire it in the records instead), rewriting history for cosmetic commit defects, posting security findings anywhere public.

## Escalation test

Ask the owner only when all four hold: no standing answer in this map settles it; the decision ladder does not decide it; the deciding fact cannot be read from the code, the ticket, or the records; and the choice between defensible behaviors is genuine. Asking a settled question is a defect in the asking.
