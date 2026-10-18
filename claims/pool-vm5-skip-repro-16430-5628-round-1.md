# Claim: pool-vm5-skip-repro-16430-5628-round-1

- Claimant host: vm5, matching a file in `hosts/`.
- Status: ACTIVE; mark DONE with the deliverable paths when the work lands.
- Job or assignment: none. Coordinator run from the skip queue. The handed-off tickets are in `material/skip-audit/overnight-queue-2026-10-18.md`; this claim covers only SOLR-16430 and SOLR-5628.
- Slice: SOLR-16430 (`LocalFSCloudIncrementalBackupTest`, two runs) and SOLR-5628 (`TestDistribDocBasedVersion` with the `commit(); // work arround SOLR-5628` line removed in the repro worktree only, two runs, then reverted).
- Capability tags used: sweep.
- Started: 2026-10-18T02:10:03Z (UTC).

Heartbeat: 2026-10-18T02:19Z. Worktree `/workspace/worktrees/jira-main` at fork `origin/main` 498a09aea9. Run 16430-r1 is still compiling. Logs and XML in `/workspace/gates/logs/vm5/jira/`. The remaining runs are queued in `/workspace/tools/vm5-jira-seq2.sh`.

Append one heartbeat line at least once an hour while the claim is active, and push it.
