# Correction: test-XML sources cited by vm5's repro reports (2026-10-25, vm5)

Reports are add-only, so this correction sits beside them. It concerns three reports that cite JUnit XML under the scratch worktrees:

- `reports/rc3-tlogreplica-rootcause-round-1.md`
- `reports/rc3-tlogreplica-peersync-probe-round-1.md`
- `reports/rc3-coreadmin-deleteinstance-rootcause-round-1.md` (its own XML path)

## What happened

Each focused run in a scratch worktree writes its XML into `solr/core/build/test-results/test/`. Later runs in the same worktree overwrote those files. The TLOG XML in `/workspace/worktrees/tlog-rc3-tip` and in `/workspace/worktrees/tlog-main` was replaced by the CoreAdmin runs (01:39 and 01:51 UTC). The `reports/rc3-tlogreplica-rootcause-round-1.md` statement that the XML "is under" those worktrees is therefore no longer true.

## What still holds, and what does not

- The logs survive and are the record for these runs: `/workspace/smoke-rc3/tlog-repro/rc3-tip-run1.log` (2 failed, seed 3635CC89D23D0929) and `/workspace/smoke-rc3/tlog-repro/main-run1.log` (3 failed, seed 9D6BB87C4F145405). Their `FAILED` lines match the method lists in the report.
- The failure messages in the reports were read from the XML before it was overwritten. The probe report's control messages were read at about 01:21 UTC, before the overwrite. The control XML no longer exists, so those messages can be checked only from the probe-report narrative, not from a file. The control's methods are confirmed by the surviving log lines.
- The loop's XML for runs 001 to 007 under `/workspace/smoke-rc3/tmp/` was not overwritten, and the loop-report counts come from it.
- The CoreAdmin XML from both trees survives and is archived at `/workspace/smoke-rc3/repro-archive/coreadmin-2026-10-25/` (`rc3-tip/` and `main/`).

## Process fix for later repro runs

Archive each run's XML to a per-run directory before the next run in that worktree, and cite that archive path, not the worktree.
