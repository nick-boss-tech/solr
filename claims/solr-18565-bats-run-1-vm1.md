# Claim: solr-18565-bats-run-1

- Claimant host: vm1, matching a file in `hosts/`.
- Status: DONE.
- Job or assignment: `assignments/solr-18565-bats-run-1.md`.
- Slice: the whole assignment (three bats runs on branch solr-18565-submit at head 2c747689c32bd17a6327ecd2b60999b972771269).
- Capability tags used: bats.
- Started: 2026-10-22T07:17:00Z (UTC).

Heartbeat: 2026-10-22T07:44:00Z

Result: 2026-10-22T07:44:00Z: all three runs done. Head verified at 2c747689c3 before running. On this host every run fails at `solr start` because the host runs as root and bin/solr refuses root without -force (no env override), so test 88 FAILS identically on the head (full run: 73 tests, 21 passed, 47 failed, 5 skipped; SSL file: 7 of 7 failed) and on the base control (7 of 7 failed), before the branch's changed assertion is reached. Full record: `reports/SOLR-18565-bats-run-1.md`.

DONE: 2026-10-22T07:44:00Z. Deliverable: `reports/SOLR-18565-bats-run-1.md` on the coordination branch. Large run logs stay on vm1 under `/home/hatch/workspace/tools/bats-18565-logs/`.
