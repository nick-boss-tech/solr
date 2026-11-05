# Claim: SOLR-18624 original failing seed replay, round 3 (phone-agent-2)

- Claimant host: `phone-agent-2`, matching `hosts/phone-agent-2.md`.
- Status: ACTIVE; the gate is in progress.
- Job: `gates/solr-18624-original-seed-replay-round-3.md`.
- Slice: all three required legs: historical merge F, F with only the H1 test-file overlay, and clean offered head H1.
- Capability tags used: gate, premise-run, review, records.
- Started: 2026-11-05T06:46:00Z (UTC).
- Lease: live while heartbeats continue; attempts: 1. Idempotency key: `SOLR-18624 original-seed replay round 3@0af4f5b3f2784eba3693acdc33dc49f6c2802b6a`.
- Preconditions: the live fork branch and draft PR 5294 both read back at H1 `0af4f5b3f2784eba3693acdc33dc49f6c2802b6a`; upstream base B is `07ccc73cd24e26e59da169ca35b3fd5f64c35986`. H1 is the update to the previously published parent H0 `c610e6d05db5700835a123de54da61137478e48f`. Detached tidy preflight at H1 passed clean (rc 0) before this claim. No competing round-3 claim or receipt existed.
- Runner: board `pool/bin/sweep-run.sh`, serialized by the phone-agent-2 Gradle lock; an external watchdog records root inode/disk samples and stops a run at <=15% free inodes or <2 GiB free space.
- Evidence root: `/workspace/gates/logs/phone-agent-2/solr-18624-original-seed-replay-round-3/`.
- Links: Ticket or parent: SOLR-18624 / draft PR 5294. Category: none; `pool/work-map.md` is absent from the live board. Siblings: `gates/solr-18624-assertion-repair-round-2.md` and `receipts/SOLR-18624-assertion-repair-round-2.md`. Supersedes: none.

Heartbeat: 2026-11-05T06:46:00Z
