# Work store: Jira day scan

- Status: OPEN. Standing store, not a single task. It stays OPEN; one claim covers one day.
- Capability tags: `records`, `review`. Intended host: any host holding those tags. At most one scan runs at a time across all hosts; the crawl stays slow by design.
- What this store is: the Jira-signals pool made concrete. A scan of one UTC day builds that day's commenter list from the API and extracts the fresh committer activity on the tickets the day touched. The work lives in `receipts/`, one folder per date. This store replaces guessing who is active with a list we built ourselves.

## Claiming a date is creating its folder

- Each date gets a folder: `receipts/<YYYY-MM-DD>/`. Creating the folder is the claim. To claim a day, push `receipts/<YYYY-MM-DD>/CLAIM.md` naming your host and the start time. If the folder already exists, the day is taken; stand down. A rejected push on the same path means the same thing.
- The day is DONE when `receipts/<YYYY-MM-DD>/receipt.md` exists in the folder. Completion is derived from the deliverable, the same as a gate receipt; nobody edits a status line.
- One live date claim per host.
- The walk goes backwards one day at a time. The next claimable day is the day before the oldest date folder on the board. The starting folder is 2026-10-23. How far back the walk goes is the owner's call; the 3-month window he named reaches about 2026-07-24.

## Cross-host preflight (before any Jira reads)

- Refresh the live `coordination-ui` branch, then inspect every `receipts/YYYY-MM-DD/` scan folder. A folder with `CLAIM.md` but no `receipt.md` is an unresolved live claim, regardless of its date or host; do not start another scan while one exists. If another host owns it, stand down. If it is your own claim, proceed only with that already-claimed day; do not open a second day.
- For a new scan, use only the next backward UTC date from the walk above, and verify its folder is absent before creating `CLAIM.md`. Push the create-only claim before reading Jira. If the folder appeared, the claim path already exists, or the push loses a race/is rejected, refresh and stand down; never force-push, rebase a competing claim into place, or overwrite a claim.
- After filing and publishing `receipt.md`, refresh the branch before claiming the next day. A failed scan remains unresolved until its failure is accurately recorded and the owner resolves the claim; do not silently retry it or advance to another date.

## The scan (one claim per day)

- Scope: SOLR tickets updated on the claimed UTC day. Query: `project = SOLR AND updated >= "<day>" AND updated < "<next day>"` against `https://issues.apache.org/jira/rest/api/2/search`, paginated at 100.
- For every ticket in scope, fetch all comments from `/rest/api/2/issue/<key>/comment`, paginated at 100, anonymous reads, about 0.15 seconds between ticket calls, no parallel fetching. Apache's Jira has no commenter index and no `commentedBy` JQL function (verified 2026-10-24); scanning and filtering is the method.
- Aggregate every commenter: username, display name, comment count, ticket count. Mark automation accounts (the ASF subversion and git services bot, username jira-bot) as automation, not people.
- Extract detail for comments CREATED on the scan day by the watch list: epugh (Eric Pugh), malliaridis (Christos Malliaridis), janhoy (Jan Høydahl), gerlowskija (Jason Gerlowski), dsmiley (David Smiley). The owner extends the watch list; the aggregate table is how new active names get noticed.

## The folder (the store's memory)

A finished day folder holds:

1. `receipt.md`: scope counts (tickets updated, comments scanned, unique commenters); the day's commenter table with automation marked; the fresh watch-list comments (person, ticket, timestamp, short excerpt, comment link); and candidate work lines drawn only from those fresh comments, each naming the ticket, the person, and the shape of the follow-up under the staged ask in `comment-formula.md`. A fresh comment that already settles its ticket is recorded as settled, with no candidate.
2. `commenters.json`: the day's aggregated commenter list, so later filters never re-crawl a finished day.
3. `target-comments.json`: the extracted watch-list comment records for the day.

## Grabbing work from the store

- Follow-ups are claimed as their own units under the normal rules, citing the day folder they came from. Resolve a ticket from its receipt first, then the live ticket.
- Nothing in this store posts anything: no Jira comments, no GitHub text, no PRs. Public steps follow the staged ask and need the owner's go as usual.
- Read-only against Jira. No token is needed and none is stored in any file.

## Seed

2026-10-23 is the starting folder, filed from the owner's smoke run (38 tickets, 149 comments, 31 commenters). The flat file `receipts/jira-day-scan-2026-10-23.md` is that run's original filing of the same receipt and stays as history; the folder is canonical from here on.
