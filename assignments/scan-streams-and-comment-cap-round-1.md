# Standing rule: two scans and the daily comment cap, round 1

Status: OPEN (standing rule, not a single task)

Owner direction, 2026-10-24, recorded by the phone agent. This file governs the two Jira scan streams and every comment posted under the owner's name. It supersedes any per-opener cap or per-opener accounting discussed earlier the same day: there is none.

## The two scans

- Active scan: the Jira day scan (`assignments/work-store-jira-day-scan-round-1.md`) in its forward, daily mode. It surfaces tickets updated that day with watch-list committer activity, and its follow-ups are the readiness reviews and staged asks. The scan's backward walk over past dates is history work; it posts nothing and is not capped.
- Backlog scan: the harmonized backlog store (`assignments/harmonize-audits-comment-drafts-round-1.md`) together with the housekeeping pipeline (`assignments/jira-housekeeping-pipeline-round-1.md`). It works the standing backlog in waves.

## The cap

- At most 100 comments posted per day in total, across both streams. Posted means actually posted under the owner's name; drafts are not counted.
- Within the total, the active stream is capped at 10 posted per day. Staged asks inside the active stream outrank status notes, because a warm lead decays in days and backlog does not decay.
- The backlog stream takes the remainder of the day's 100. There is no per-opener, per-family, or per-ticket cap. The working assumption is that recipients prefer one flood to a trickle.
- Draft production is uncapped. The factory drafts to the maximum (see `assignments/jira-draft-priority-round-1.md`); the cap governs posting only, and every post still needs the owner's go per text.
- Housekeeping waves are sized at most 100, and at most one backlog wave is posted per day, so a wave and a posting day are the same unit.

## The ledger

- Every posted comment is recorded the day it posts in `receipts/comment-ledger/<YYYY-MM-DD>.md`: ticket key, stream (active or backlog), family, and the comment's location. One file per day, append-only within the day, written by whoever posts or by the phone agent from the owner's confirmation.
- The ledger is the proof the cap held and the guard against posting twice on one ticket. A day at 100 stops posting for that day; the queue carries forward.

## What this removes

- No opener field is required in the harmonized store for cap purposes, and no per-opener counters or batching are built. Backlog wave construction stays as the pipeline defines it (tier, then wave of at most 100), with no reporter clustering.
