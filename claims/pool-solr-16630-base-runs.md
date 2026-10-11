# Claim: pool-solr-16630-base-runs

- Assignment: assignments/pool-solr-16630-base-runs.md
- Claimant host: vm1 (hosts/vm1.md)
- Slice: whole assignment (base runs, changelog title fix, receipt update)
- Capability tags used: premise-run, implementation
- Started: 2026-10-11T02:06:01Z (set at push time; see git history for the exact push time)
- Note: the assignment names vm2 as intended host; vm1 takes it under Nick's 2026-10-10 direction that the local queue take backlog work while vm2 has a backlog. Base runs execute on vm1 under a detached blocking-flock runner; the changelog commit, branch push, receipt update and the DONE mark follow once the run outcomes are recorded (GATE PENDING entry in the main side's takeover log).

## Heartbeats

- 2026-10-11T02:06:01Z vm1: claim written; runner being prepared.
