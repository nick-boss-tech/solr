# Claim: pool-solr-16630-base-runs

- Assignment: assignments/pool-solr-16630-base-runs.md
- Claimant host: vm1 (hosts/vm1.md)
- Slice: whole assignment (base runs, changelog title fix, receipt update)
- Capability tags used: premise-run, implementation
- Started: 2026-10-11T02:06:01Z (set at push time; see git history for the exact push time)
- Note: the assignment names vm2 as intended host; vm1 takes it under Nick's 2026-10-10 direction that the local queue take backlog work while vm2 has a backlog. Base runs execute on vm1 under a detached blocking-flock runner; the changelog commit, branch push, receipt update and the DONE mark follow once the run outcomes are recorded (GATE PENDING entry in the main side's takeover log).

## Heartbeats

- 2026-10-11T02:06:01Z vm1: claim written; runner being prepared.
- 2026-10-11T02:14:38Z vm1: base runs finished; run 1 at the CI seed on the merge-base failed premise-shaped and the job stopped early per the assignment.

## Status

- DONE, 2026-10-11. Base run 1 reproduced the premise on the merge-base at the CI seed (SolrServerException caused by ClosedChannelException at the add, PULL stopped before the first successful add; XML g16630-baseruns-run1.xml on vm1). Changelog title corrected in branch commit 9c77a4db09d7a890f62dc9a9ffb2bf5613245da7, pushed to fork solr-16630-submit (fast-forward). Receipts: receipts/SOLR-16630.md carries the base-run outcome and the new head; draft pr-drafts/flaky-fixes/SOLR-16630.md re-headed with the Proof stating the base failure.
