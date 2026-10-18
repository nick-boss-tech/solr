# Active skip audit queue

This is the shared, current tracker for the 1,027-ticket Solr issue queue. The latest full queue state is [`queue-current.json`](queue-current.json), refreshed from `research/pipeline/queue.json` after audit-7 on 2026-10-17; it contains the status and audit evidence for every ticket. Batch evidence files are stored beside it.

The 2026-10-07 [`queue-snapshot-2026-10-07.json`](queue-snapshot-2026-10-07.json) remains as an immutable import snapshot. Use `queue-current.json` for coordination and ticket selection.

## Current state

Counts below are from `audit.ps1 status` after audit-7. “Remaining” counts unaudited entries still in `status: skip`; reopened entries move to `revisit`.

| Tier | Current skips | Audited | Remaining |
| --- | ---: | ---: | ---: |
| 1 | 392 | 392 | 0 |
| 2 | 155 | 107 | 48 |
| 3 | 143 | 143 | 0 |
| 4 | 35 | 35 | 0 |
| 5 | 23 | 23 | 0 |

Recorded verdict totals: 173 branched, 639 confirmed-skip, 63 deferred, and 2 reopen. Two formerly skipped items are now `revisit`: SOLR-4944 and SOLR-8877. Tier 2 had 88 unaudited skips before audit-6; 48 remain after audit-7.

## Latest audits

- **Tier 2, audit-7 (2026-10-17):** 20 reviewed; 14 confirmed-skip and 6 deferred (SOLR-8171, SOLR-7947, SOLR-7867, SOLR-7249, SOLR-6890, SOLR-6836). Evidence: [`tier2-audit-2026-10-17-b.json`](tier2-audit-2026-10-17-b.json), checked against `upstream/main` at `c8ab71430f6e7547c773da596101d680862310ff`.
- **Tier 4, audit-5 (2026-10-17):** 20 reviewed; 16 confirmed-skip, 3 deferred (SOLR-12234, SOLR-10197, SOLR-6335), and 1 reopened (SOLR-4944). Evidence: [`tier4-audit-2026-10-17.json`](tier4-audit-2026-10-17.json).
- **Tier 2, audit-6 (2026-10-17):** 20 reviewed; 10 confirmed-skip, 9 deferred (SOLR-9645, SOLR-9599, SOLR-9260, SOLR-9197, SOLR-9129, SOLR-8757, SOLR-8729, SOLR-8632, SOLR-8478), and 1 reopened (SOLR-8877). Evidence: [`tier2-audit-2026-10-17.json`](tier2-audit-2026-10-17.json), checked against `upstream/main` at `c8ab71430f6e7547c773da596101d680862310ff`.

## How to use the queue

- A `skip` means earlier triage did not identify a quick implementation. It does not mean the ticket is complete, removed, or forgotten. Keep every ticket in the full queue and retain its audit evidence.
- `confirmed-skip` means the recorded reason still holds for the evidence checked. Re-audit when current code, linked branches, or ticket evidence changes.
- `deferred` means the queue entry stays visible while a specific missing reproduction, data point, or linked branch update is outstanding.
- `reopen` changes the queue status to `revisit`; handle it as an active candidate or document why it must wait.
- Select work from the latest queue, check current `upstream/main`, and check active branches/claims before implementing. When multiple VMs are working in parallel, post a non-overlapping assignment and claim the slice under `claims/`.
- After each audit batch, update the source queue and evidence, refresh this shared snapshot, and commit the change to `coordination`. Do not post JIRA or GitHub comments as part of an audit unless separately authorized.
