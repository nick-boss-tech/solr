# Assignment: solr-18470-framing-reconciliation-round-1 (the collection parameter, after Smiley's dev-list reply)

- Status: OPEN. Intended host: any host with the `review` tag. Posted by phone agent on behalf of Nick Shanin, 2026-10-24.
- Scope: analysis and drafting only. Nothing is posted, and no posted comment is edited, under this assignment. Every outward text comes back as a draft for the owner's go.
- One claim covers the whole item; it is one unit of work.

## Background, all of it on the record

- SOLR-18470 plan comment (Jira comment 18131208, posted 2026-10-23): states PR #5202 covers SOLR-18446 only, cites Smiley's dev-list advice, gives the plan for the sibling design question, and asks three questions.
- PR #5202 (SOLR-18446, open and ready at head 1c0a942ff86): URL path governs scope on v1 and V2 paths; the `collection` parameter cannot override the path and can only narrow the set the path names. Eric Pugh reviewed it with four comments and formally requested Smiley's review.
- Smiley's dev-list reply (2026-10-24 ~00:03 MDT, Gmail 1a16c83cfc2ffa84), visibly exasperated: a Blocker with a fix version means it is meant to block THAT release or force a conversation as it nears, and his example of the backwards-compat concern that cannot be backported is dropping the `collection` param, aimed at Solr 11, which is not soon. He said he will not respond to review asks from Nick ("crickets may mean eventually but definitely not now") and named the #5202 review request as one PR too many on his pile.
- Standing rules the draft must honor: authored text never uses "we" for Nick plus the agent; Jira text is Jira wiki markup with the footer `🤖 _AI text above_ 🤖 _(reviewed by Nick Shanin)_`; a Jira edit emails every watcher, so an edit is a notification event, not a silent fix.

## What to work out

1. Read the 18470 comment, the #5202 description and Pugh's review threads, and Smiley's reply side by side, and state the framing gap in one paragraph: where the posted comment's "narrowing" language diverges from Smiley's "deprecation for Solr 11" frame, and what a reader who holds his frame would reasonably take the posted comment to be asking for.
2. State what Smiley's reply actually demands of each artifact, separately: of PR #5202 (nothing new is asked of it by that email beyond the review he has declined for now), of the 18470 plan (its scope question is the release-blocking conversation for Solr 11, not a work-now item), and of Nick's selection posture (solicited reviewers before work, which the factory's standing rules already say).
3. Recommend, with reasons, the delivery vehicle: a new short comment on SOLR-18470, or an edit of the posted comment. The default presumption is a new comment, not an edit, because an edit re-notifies watchers and rewrites the record of what Smiley already read; say what would have to be true for the edit to be the better vehicle.

## Deliverable

`reports/solr-18470-framing-reconciliation-round-1.md` plus the draft itself at `jira/jira-comment-SOLR-18470-followup-draft.md`. The draft is short (a long comment to an overloaded reviewer is the opposite of the point) and does four things in plain language: acknowledges the review pile and the solicited-reviewer rule without arguing with Smiley's email; states plainly that the deprecation question for Solr 11 is the project's call and that #5202 claims only the narrowing step; states what Nick will and will not do next on 18470 (no further work without a reviewer who said yes); and asks Smiley what shape of plan, if any, he would accept hearing about before Solr 11 work is real. No new scope is offered, no deadline is set, no apology is performed beyond what the facts support.
