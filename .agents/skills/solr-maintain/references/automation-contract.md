# Bounded maintenance automation design

Candidate jobs: census delta collection, review/receipt freshness reconciliation, monthly-index refresh, handbook chapter update proposals and a digest of meaningful changes. Source scans remain with the existing project; a metadata delta is not a completed review.

Persist the last successful snapshot/cursor, collection boundary, repository/PR/head identity, source hashes and emitted-event keys. Bound requests, changed items and elapsed time per run. Deduplicate events by repository, PR, revision and event type; include discussion/check identity when the same head can acquire new evidence. A failed or partial collection must not replace the last good cursor or imply that records disappeared.

Generate a new snapshot for a refresh and preserve the old one. Treat merges as candidates for chapter reassessment: check the merged diff and later source before changing architecture claims. Keep candidate chapter text and speculative connections out of canonical project policy until reviewed.

Use read-only collection and local draft outputs by default. Public publication, branch changes and recurring scheduling follow the user's actual authorization. Do not route retired Windows test queues or unassigned builds through automation. Independent review should cover no-change deduplication, moved heads, late checks/comments, partial responses, restart recovery and protected draft holds.

This package supplies the design and assignment only. It installs no scheduler, creates no recurring job and makes no live gateway call. The next implementation must record the selected host, cadence, output destination, state store and failure notification behavior before deployment.
