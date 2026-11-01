# SOLR-14414 scope note: the New Admin UI umbrella (round 1, records only)

Date: 2026-11-01. Source: Jira read this run plus the board census
(material/ui-tickets.md on coordination-ui, enumerated 2026-10-23) and a
code read on local apache/solr main at e55d2e7bcb. No Jira post, no
GitHub post, no build. This record closes the last unclaimed
records-shaped item in the census: every other unassessed open census
ticket already has a round-2 assessment claim on coordination-ui
(17572, 17585, 17598, 17657, 17898, 18178, 18191, 18214), and 18214
additionally carries claims, receipts, and gates on coordination.

## Ticket state on Jira (read 2026-11-01)

- SOLR-14414, "New Admin UI": Open, unassigned, reporter Marcus Eagan,
  labels new-ui, pull-request-available, ui, priority Major, created
  2020-04-18, last updated 2025-09-01, no fix version.
- No fork branch, no board receipt, no board draft, no open PR from
  nick-boss-tech. The only apache/solr PRs referencing the ticket are
  both merged: #2605 (Introduce new UI, SIP-7) and #3387 (clickable
  links in the footer). The pull-request-available label reads stale
  against that: nothing open carries the ticket.
- Links: references SOLR-8071 (Resolved) and SOLR-15592 (Resolved).

## Two eras in one ticket

The ticket text is the 2020 proposal: an Angular rewrite in
lucene-solr/solr/webapp2, started on weekend nights, explicitly not at
feature parity, with three essential components missing, no V2 API
coverage, and no packaging story. The April 2020 comments argue the
shape, not the code: Noble Paul gave a -1 to merging it in-tree and
argued for an external project shipped as a package, Ishan
Chattopadhyaya sketched the same external-plus-dependency shape, Jan
Hoydal pushed the discussion to a SIP, and David Smiley preferred a
separate project but accepted keeping the code in-tree until it was
clear it was not getting maintained. The thread then stalls: Jan Hoydal
2021-08-06 calls it abandoned and unlikely to overtake the old UI, and
the YASA parallel effort (kezhenxu94, Eric Pugh's feature comparison)
never bridges the gap either.

The live line starts 2025-02-08, when Christos Malliaridis committed
bb27d3e4 "SOLR-14414: Introduce new UI (SIP-7) (#2605)": a Compose
Multiplatform module targeting browser (wasmJs) and desktop. That
commit, not the 2020 Angular proposal, is what the census's new-ui
ticket set tracks: 67 tickets by label plus reporter, 28 not
resolved or closed at census time, 19 assessed in the four 2026-10-23
rounds.

## Current main evidence (e55d2e7bcb, read this run)

- ui/ exists as an independent sub-project (shared, webApp,
  desktopApp, Kotlin DSL build files). Its most recent commit in this
  checkout is c78939a620 "Make UI module independent sub-project
  (#4650)".
- solr/webapp, the old AngularJS Admin UI, still exists alongside it.
  The old UI has not been replaced; the two live side by side.
- No webapp2 directory exists anywhere in the tree. The 2020 proposal
  never landed in that form.

## Scope finding

SOLR-14414 is a tracker, not a claim-sized unit. Claiming the umbrella
as one implementation item would repeat the 2020 mistake of treating a
whole-UI replacement as a single piece of work. The claim-sized units
are its children, and that is already the working shape: the
pool-ui-tickets-round-1 assignment takes one ticket per claim, and the
Christos reply rule (2026-10-27) routes each of his replies to its own
assignment. This record adds no new claim and proposes no new
assignment.

Two questions stay open for a maintainer, not for this record:

1. Whether the ticket text should be re-scoped to the SIP-7 Compose
   module (the thing actually being built) or kept as the historical
   umbrella with the 2020 Angular proposal standing as written. The
   census already treats it as the umbrella; rewording it is a
   maintainer call, and any Jira edit emails every watcher, so it
   should not be done as a drive-by.
2. Whether the pull-request-available label should be dropped, since
   both referencing PRs are merged and no open PR carries the ticket.

## Next step

None from this record. The next UI records-shaped unit, if the queue
returns to item 4, is a fresh read of one of the round-2 assessment
claims once it lands, or a scope note for a newly opened new-ui
ticket. Do not claim SOLR-14414 itself.
