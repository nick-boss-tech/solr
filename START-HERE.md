# Start here

This board coordinates Nick Shanin's Solr factory. Its mission, set 2026-10-23: help the new Admin UI effort (Christos Malliaridis's Kotlin/Compose UI, the `ui/` module in apache/solr), keep the follow-ups on Nick's open PRs moving, and work from today's signals rather than the backlog.

## Read in this order

1. `WORKFLOW.md`: claims, gates, posting. The rules that govern everything on this branch.
2. `UI-PLAYBOOK.md`: the UI build, the module map, the test setup, the ticket landscape, and the assessment verdicts.
3. `pool/decision-map.md`: how an item gets classified and what the standing answers are. Decide from the map; the owner is asked only what passes the escalation test there.
4. `material/decision-ladder.md`: the order a decision is worked in before it may become a question. Get more information, do the hard thing, defer as a named follow-up; only what survives all three reaches the owner, with the rung recorded in the claim.
5. `comment-formula.md` and `jira/COMMENTS.md` before drafting any Jira or GitHub comment. Every comment for a listed ticket is scoped through the formula before it posts, and posting needs the owner's go.
6. Your host's file in `hosts/`: its capabilities and tags. Claim only work whose tags your host holds.
7. `pool/owner-complaints-2026-10-19.md` before claiming anything. It outranks habit.

## Where work comes from

The five pools, in order: follow-ups on our open PRs and live UI claims; UI tickets assessed in the round reports under `reports/`; then today's signals only (Jiras updated today, emails received today, CI failures from today, PRs posted today, promises Nick has made). Everything outside those pools is ignored by default. Blocker/Critical labels and the newdev tag are not signals; recent committer activity on a ticket is.

## The shape of the work

Assess before volunteering, volunteer before building, and line up a reviewer before starting: the staged ask in `comment-formula.md`. A claim is what assigns work. A gate is the only "done" for code. A receipt at the exact live tip settles gate state. Records are add-only: claim, report, and draft files are added, never edited; shared indexes are generated. Resolve a ticket from its receipt first.
