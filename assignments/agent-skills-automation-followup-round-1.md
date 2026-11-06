# Assignment: implement bounded skill maintenance automation after review, round 1

Status: OPEN (design available after foundation; deployment waits for an explicit schedule and destination).

Tags: implementation, review, records.
Intended host: any capable factory implementation host; execution location must match the chosen scheduler.

Use the reviewed solr-maintain automation specifications to build a read-only delta pipeline: new/moved/merged PR metadata, stale review/receipt detection, chapter refresh candidates and a digest of meaningful changes. Reuse the ongoing PR scan's census and ingestion workflow; do not duplicate its archaeology or claim it has finished.

Deliver a scoped implementation branch and reports/agent-skills-automation-followup-round-1.md. Define snapshot identity, a persisted cursor, deduplication keys, restart behavior, output paths, failure reporting and a bounded per-run budget. A merge is a trigger for reassessment, not automatic promotion of PR text into project policy. Preserve raw evidence and propose chapter changes for review.

Prove with offline fixtures that unchanged data creates no duplicate digest entries, a moved head invalidates the old review, and partial/API-failed collection does not replace the last good cursor or claim that items disappeared. Exclude retired Windows test queues and all unrequested public writes. Do not create an actual recurring automation until Nick specifies or approves cadence, host and output destination; no schedule has been created by the foundation task.
