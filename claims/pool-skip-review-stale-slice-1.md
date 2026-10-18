# Claim: pool-skip-review-stale-slice-1

- Claimant host: vm5, matching a file in `hosts/` (`hosts/vm5.md`).
- Status: DONE; deliverable paths below.
- Job or assignment: `assignments/pool-skip-review-stale-slice-1.md`.
- Slice: the whole slice 1 of 4 (61 tickets, SOLR-1247 through SOLR-5508).
- Capability tags used: review.
- Started: 2026-10-18T07:22:05Z (UTC).

Heartbeat: 2026-10-18T07:22:05Z

Result: 2026-10-18T07:40Z: read-only verdicts for all 61 rows at 498a09aea9, by four parallel reads of JIRA plus `git grep` and `git show` at the pinned sha. No builds or tests run, so no seed or log path. Totals: present 15, fixed 13, gone 8, unclear 25. Unclear rows name the missing piece (mostly Lucene source or a runtime reproduction). Report: `reports/2026-10-18-skip-repro/C1-stale-slice-1.md`.

DONE: 2026-10-18T07:40Z. Deliverables: `reports/2026-10-18-skip-repro/C1-stale-slice-1.md`.

Choice at claim: `assignments/pool-merged-pr-followup-promises-round-1.md` is also OPEN with no live claim, but it repeats only when one of our PRs merges or the owner asks. The GitHub merged set has no entry after #5027 and #5062 (both 2026-10-17T00:24Z and earlier), and vm5 already passed at 03:55, 05:28, 07:08, 07:10 and 07:12Z today with no new merge. Left OPEN for the next merge.

Append one heartbeat line at least once an hour while the claim is active, and push it.
