# Formula review: Jira comments posted 2026-10-23

Reviewed against comment-formula.md as adopted that evening (staged ask; status-note shape; Jira footer disclosure). Texts read from the draft files and, for SOLR-6213 and SOLR-5754, from Jira live. No edits made; this review is the discussion step Nick directed.

## Cross-cutting finding

All seven comments carry the header form of the AI disclosure. The formula itself records that already-posted text stands and the split applies forward, so no retroactive disclosure edit is proposed. The one standing exception to that rule is accuracy: a posted comment that is wrong gets edited at once. The single accuracy candidate (SOLR-6213, below) was checked and is correct.

## Per comment

### SOLR-18470 (18131208), plan comment

Deviations from the staged ask: it asks for a review commitment in the first touch; it poses three questions, two of them scope menus (first slice size, core path in or out), where the staged form poses a ladder and states our recommendation; and it has no evidence beat, because it is a design plan (its scope content appears as mechanism bullets rather than the files-I-expect-to-touch form). In substance it is strong, and it is the specimen the staged form was designed against. Edit proposed: none. The text is live in front of Smiley; rewriting questions he may already be answering would muddy the thread. The staged form governs the next comment, not this one's history.

### SOLR-18240 (18131214), status note

Passes the status-note shape: verified facts (9.11.0 shipped, guide notes published), conclusion put to the owner as his decision. Minor gap: the published-notes claim carries no link. Edit proposed: none.

### SOLR-17988 (18131215), status note

Passes. If the pool property-transition audit finds real gaps, the follow-up comment is where the formula applies. Edit proposed: none.

### SOLR-12833 (18131218), status note

Passes fully: timeline with the reopen reason quantified, follow-up PRs cited, recommendation stated (ready to close as Fixed), decision left with the owner. Edit proposed: none.

### SOLR-16450 (18131220), status note

Passes fully: answers the standing question from 2024 directly, both fix commits cited with hashes, the restored fixVersion named as our recommendation. Edit proposed: none.

### SOLR-6213 (18131122, as edited), clarification plus review ask

The appended ask reads, under the staged form, as the reconfirmation beat rather than a first ask: the PRs already exist, and Smiley's December comment on the ticket (he had a 3-retry limit written and offered it) is the engagement that precedes it. The formula's own rule is to reconfirm willingness when a PR is posted; this does that. Accuracy check: the second bullet says PR #5210 "runs leader election rejoins on their own thread, so a blocked rejoin no longer stalls the election loop." Verified against the PR diff: the change adds a dedicated single-thread rejoin executor (rejoins no longer nest on the caller's thread and grow the stack) plus the rejoin lock and close-before-cancel. The bullet is accurate. Edit proposed: none.

### SOLR-5754 (18131116), closure recommendation

Passes: the premise-scrutiny closure shape, production writer and copy-then-clear path cited at class and method level, conclusion no stronger than the trace, recommendation stated plainly. Edit proposed: none.

## The one open decision

Whether to apply the Jira footer swap retroactively to these seven despite the formula's forward-only rule. Cost of doing it: an edit notification to every watcher on seven tickets and a bumped timestamp on each, in exchange for disclosure placement consistency on texts that already disclose. Recommendation: no. The disclosure is present and honest in all seven; the footer governs from the next comment forward.
