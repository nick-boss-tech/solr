# Assignment: Critical tickets comment coverage (round 1)

Status: OPEN
Tags: draft, records, sweep

Owner directive (2026-10-24): every open Critical-priority ticket gets a comment drafted, or a recorded reason it does not, and implementation candidates get identified for follow-up work. The census is `material/critical-tickets-census-2026-10-24.md`: 86 tickets in 9 batches of 10, stalest first. This is the priority-Critical set; the Blocker set is a separate line and out of scope here.

## Claims

One claim covers one batch: `claims/critical-comments-round-1-batch-<NN>.md`. Work batches in order unless a later batch is unclaimed and an earlier one is claimed.

## Per-ticket work

For every ticket in the batch:

1. Read the ticket in full (description and all comments) and check this board and GitHub for existing owner work on it (branch, PR, receipt, earlier draft).
2. Record a disposition in the batch report: the ticket's current state in one line, any existing owner PR or branch, and one of:
   - a comment draft, written to `jira/jira-comment-<KEY>-draft.md` on this branch, in Jira wiki markup, first person, with the footer disclosure, saying only what the ticket's own evidence supports; or
   - a no-draft verdict with a reason code: `already-told` (the owner's comment or PR is already on the ticket and current), `active-thread` (a maintainer conversation is live and a new comment would add nothing), `security-route` (the ticket's substance is security-sensitive; record the flag only, no public draft), `covered-by-link` (a linked ticket's draft covers the same ground; name it).
3. Choose the comment kind by the ticket's shape:
   - Stalest first is the default: a Bug untouched for over 2 years gets the stale-check shape (is this still seen on a supported release; here is what the ticket's own evidence shows; the still-wanted route stays open).
   - A Patch Available ticket gets a support or status-check note aimed at moving the existing patch, never a competing offer.
   - A ticket with existing owner work gets a status note only if the ticket has not already been told about that work.
   - A design-sized ticket (New Feature, SIP, broad Improvement) gets a scoping note: the decision the ticket actually turns on, stated as a question, with no implementation promised.
   - A ticket whose last activity is a committer's gets the staged-ask shape from `comment-formula.md`.
4. Implementation triage for the same ticket, one line in the batch report: `reproduce-first` (a premise run on current main decides whether the problem still exists), `implementation-candidate` (the defect and fix shape are clear from the ticket), `design-first` (needs a maintainer decision before code), or `not-tractable` (with the reason). Name the evidence the triage rests on.

## Deliverables per batch

- `reports/critical-comments-round-1-batch-<NN>.md`: one record per ticket as above; every ticket in the batch appears exactly once.
- One draft file per drafted ticket under `jira/`.

## Rules that still bind

Draft production is uncapped; posting is not part of this assignment and every text needs the owner's explicit go. No routed review asks to Smiley. Security-sensitive substance is flagged, never drafted publicly. Comments on tickets on the scoped-comments list follow that list's texts where one exists. If a batch's evidence shows a ticket is a duplicate of another ticket in the census, say so in both records instead of drafting twice.
