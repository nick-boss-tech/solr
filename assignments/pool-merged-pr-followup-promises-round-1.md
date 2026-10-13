# Pool assignment: audit of merged PR descriptions for follow-up promises, round 1

Capability tags: `review`. Intended host: any review-side host. No build work. This assignment is self-contained for a reviewer joining cold.

## Purpose

Every pull request Nick Shanin (GitHub `nick-boss-tech`) has had merged into `apache/solr` carries a description written to the formula in `pr-formula.md`. Some of those descriptions promise future work: a named follow-up PR or fix, a Limits or Choice line offering or planning a later change, or a stated plan to submit something after the merge. This audit reads the final description of every merged PR, extracts each promise, and checks it against what has actually happened since: later PRs, branches on the fork, Jira tickets, and the records on this branch (`receipts/`, `reports/`, `pr-drafts/`). The result is a running ledger of promises and their states, so nothing promised to maintainers in a merged description is silently dropped.

## Enumeration

Derive the merged set from the GitHub API; do not trust any fixed list. Query the pull requests of `apache/solr` with author `nick-boss-tech`, state closed, and keep only those with a merge timestamp (`merged_at` set) and base `main`. The seed list below is a cross-check only; if the API returns a merged PR that is not on it, that PR is in scope, and if a seed PR does not come back as merged, say so in the report.

Seed list: #4939, #4953, #4959, #4995, #5001, #4965, #5070 (SOLR-4841), #5012 (SOLR-13202), plus anything merged after those that the API returns.

Read each merged PR's final description text: the body as it stands now on the closed PR, not a draft on this branch and not a receipt's summary of it. Quote promises verbatim from that body.

## Known promise shapes to seed the reading

Verify each of these against the live body text and quote it verbatim in the ledger. Treat anything else the bodies contain the same way.

- PR #5100 (SOLR-18532): a Limits line naming the follow-up on SOLR-9865, whose branch drops its overlapping rollback hunk when prepared for opening, because #5100 lands first.
- SOLR-17393 and SOLR-9637: the merge-only scope on 17393 names the per-shard top-count cut as a follow-up, and 9637 follows as its own PR once 17393 lands, rebased onto it. Find which merged description carries this and quote it.
- PR #5104 (SOLR-2681): a Limits line on the nested form with a follow-up pull request planned.
- PR #5061 (SOLR-18119): fragment anchor validation carried as a named follow-up promise rather than code. Only if #5061 has merged by the time the audit runs; if it is still open, record it as pending-merge and out of this round's ledger, with a note that it enters the ledger when it merges.
- PR #5090 (SOLR-13943): a Limits promise tied to the SOLR-13059 follow-up (the test-only fix for `TimeRoutedAliasUpdateProcessorTest`).

Absence of a promise is also recorded, per PR, so the ledger covers the whole merged set and a later reader can tell "checked, no promise" from "not checked".

## Deliverable

`reports/merged-pr-followup-promises-round-1.md`, carrying the full ledger. One entry per merged PR with:

- the PR number, its Jira ticket, and its merge date;
- each promise quoted verbatim, or the line "No follow-up promise in the description";
- the promise's state: discharged (with the evidence: the later PR, branch, ticket or record that discharges it), open (nothing has discharged it yet), or not yet due (its trigger has not happened, for example a promise that starts when another PR merges);
- for each open promise, what discharging it takes: the concrete next step and whose call it is.

## Recurrence

This audit repeats whenever another of our PRs merges, and whenever the owner asks. The report file is the running record: later rounds update the same ledger in place rather than starting a new file, re-derive the merged set from the API (new merges enter as new entries), re-check the states of promises still open or not yet due, and date each round's pass at the top of the file.

## Bounds

- Read-only against GitHub and this branch. No tickets filed, no PRs opened, no comments posted, no descriptions edited.
- Discharges are proposed in the report for the main side and the owner; this assignment executes none of them.
- Voice rules for the report: no em dashes, never "we" for the owner plus agents, plain language. Public-text rules do not bind this internal report's wording, but keep internal process vocabulary out of any text the report drafts for later public use.
