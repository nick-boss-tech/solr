# Assignment: discuss the open pool work with Nick (round 1)

Status: OPEN. Posted by vm4, 2026-10-25, at Nick Shanin's direction. For the phone agent. Capability tags: records, draft.

This is a discussion request. Do not claim, build, gate, or push anything except the outcome file named below. Nick picks the work.

## Ask

1. List the open assignments on coordination-ui that match your tags. For each, give one line: what it asks for, its intended host, and whether it waits on Nick.
2. Ask Nick which one he is interested in. Wait for his choice before filing any claim.
3. Write the reply to `reports/vm4-open-work-discussion-round-1.md` and set this file's Status line to DONE in the same push.

## Starting point from vm4

vm4 surveyed `pr-prepare` on 2026-10-25, which has a different open set from coordination-ui. Re-check the branch before quoting any of these.

- SOLR-18551 branch review, 9.x leg (`assignments/pool-solr-18551-branch-review-round-1.md`). Claimed by vm2 in `claims/pool-solr-18551-branch-review-round-1-9x-10x.md`, last heartbeat 2026-10-17T18:00Z. Stale by the 2-hour rule, so a takeover needs a check that the gate log has stopped growing.
- SOLR-13059 test fix, round 1 (`assignments/pool-solr-13059-fix-round-1.md`). Open, no claim. Test-only change on `solr-13696-submit`.
- 2026-10-16 code review, round 1, items 3 to 5 (SOLR-6975, SOLR-6759, SOLR-8939). Open. Items 4 and 5 already have vm1 live-head gates marked DONE, so check those first.
- 2026-10-16 code review, round 2, item 4 (SOLR-18010, undisclosed write access to `SOLR_HOME`). Open, no claim. Read from code only so far.
- eDisMax draft patches P1 to P6 (`assignments/pool-edismax-patches-apply-round-1.md`). Open, no claim. Either Linux host may take it.

Not suggested: the SOLR-17268 re-gate, because its required fork tip no longer matches the branch; and the admin UI premise runs, which a Windows claim holds.
