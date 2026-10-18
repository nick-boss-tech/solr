# Claim: pool-skip-repro-8380-round-1

- Claimant host: vm5, matching a file in `hosts/` (`hosts/vm5.md`).
- Status: DONE; see the DONE line below.
- Job or assignment: `assignments/pool-skip-repro-8380-round-1.md`.
- Slice: the whole job (two runs of CollectionsAPIDistributedZkTest at the pinned head, then items 2 to 4 of the reading).
- Capability tags used: sweep, review.
- Started: 2026-10-18T05:01:39Z (UTC).
- Head: fork origin/main 498a09aea9. Worktree: `/workspace/worktrees/vm5-8380` (detached at that head).

Heartbeat: 2026-10-18T05:01:39Z

Result: 2026-10-18T05:11Z: run1, `:solr:core:test --rerun --tests org.apache.solr.cloud.api.collections.CollectionsAPIDistributedZkTest` at 498a09aea9. PASS: 17 tests, 0 failures, 0 errors, seed E0858E83DA2CFAFB. Zero signature matches in the XML or the Gradle log. Log `/workspace/gates/logs/vm5/skip-8380/run1.log`, XML `/workspace/gates/logs/vm5/skip-8380/run1.xml/TEST-org.apache.solr.cloud.api.collections.CollectionsAPIDistributedZkTest.xml`. Conclusive as a single pass only: the ticket's rate is about 1 in 3, so one pass does not rule the failure out.
Headroom: pre 05:04:24Z 3.6G free, 454516 inodes free; end 05:11:08Z 3.6G free, 454511 inodes free.
Tooling note: `pool/bin/sweep-run.sh` copies XML only when newer than the headroom log, and it rewrites that log last, so the XML was skipped. Copied by hand.
Cadence note: item 1 asks for two runs; the Run cadence row reads as one run, then one more with the failing seed on failure. Run 2 is unseeded, within the two-run cap, to meet item 1.

Result: 2026-10-18T05:13:54Z: run2, same command at 498a09aea9. PASS: 17 tests, 0 failures, 0 errors, seed A0043A38F768B0A7. Zero signature matches. Log `/workspace/gates/logs/vm5/skip-8380/run2.log`, XML `/workspace/gates/logs/vm5/skip-8380/run2.xml/`. Conclusive as no failure in 2 runs (the Status of the 500 default); no third run.
Headroom: pre 05:11:55Z 3.6G free, 454511 inodes free; end 05:13:54Z 3.6G free, 454505 inodes free.
Items 2 to 4: no signature matches in any output; proxy and header-limit reading in the deliverable.

DONE: 2026-10-18T05:15Z. Deliverables: `reports/2026-10-18-skip-repro/D1-8380.md`.

Append one heartbeat line at least once an hour while the claim is active, and push it.
