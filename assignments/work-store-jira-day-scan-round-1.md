# Work store: Jira day scan

- Status: OPEN. Standing store, not a single task. It stays OPEN; one claim covers one day.
- Capability tags: `records`, `review`. Intended host: any host holding those tags. At most one scan claim runs at a time across all hosts; the crawl stays slow by design.
- What this store is: the Jira-signals pool made concrete. A scan of one UTC day builds that day's commenter list from the API and extracts the fresh committer activity on the tickets the day touched. The scan's results are filed as receipts, and work is grabbed from the receipts. This store replaces guessing who is active with a list we built ourselves.

## The scan (one claim per day)

- Claim file: `claims/work-store-jira-day-scan-<YYYY-MM-DD>.md`. Days are claimed newest first unless the owner names a catch-up range.
- Scope: SOLR tickets updated on the claimed UTC day. Query: `project = SOLR AND updated >= "<day>" AND updated < "<next day>"` against `https://issues.apache.org/jira/rest/api/2/search`, paginated at 100.
- For every ticket in scope, fetch all comments from `/rest/api/2/issue/<key>/comment`, paginated at 100, anonymous reads, about 0.15 seconds between ticket calls, no parallel fetching. Apache's Jira has no commenter index and no `commentedBy` JQL function (verified 2026-10-24); scanning and filtering is the method.
- Aggregate every commenter: username, display name, comment count, ticket count. Mark automation accounts (the ASF subversion and git services bot, username jira-bot) as automation, not people.
- Extract detail for comments CREATED on the scan day by the watch list: epugh (Eric Pugh), malliaridis (Christos Malliaridis), janhoy (Jan Høydahl), gerlowskija (Jason Gerlowski), dsmiley (David Smiley). The owner extends the watch list; the aggregate table is how new active names get noticed.

## The receipt (the store's memory)

Deliverable per day: `receipts/jira-day-scan-<YYYY-MM-DD>.md`, containing:

1. Scope counts: tickets updated, comments scanned, unique commenters.
2. The day's commenter table, automation marked.
3. The fresh watch-list comments: person, ticket, timestamp, a short excerpt, and the comment link.
4. Candidate work lines drawn only from those fresh comments: each names the ticket, the person, and the shape of the follow-up under the staged ask in `comment-formula.md`. A line is a candidate, not an instruction; a fresh comment that already settles its ticket is recorded as settled, with no candidate.

## Grabbing work from the store

- Follow-ups are claimed as their own units under the normal rules, citing the day-scan receipt they came from. Resolve a ticket from its receipt first, then the live ticket.
- Nothing in this store posts anything: no Jira comments, no GitHub text, no PRs. Public steps follow the staged ask and need the owner's go as usual.
- Read-only against Jira. No token is needed and none is stored in any file.

## Seed

`receipts/jira-day-scan-2026-10-23.md` is already filed from the owner's smoke run (38 tickets, 149 comments, 31 commenters) and is the shape reference for later receipts.
