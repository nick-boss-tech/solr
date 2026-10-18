# Claim: pool-vm5-skip-repro-16430-5628-round-1

- Claimant host: vm5, matching a file in `hosts/`.
- Status: ACTIVE; mark DONE with the deliverable paths when the work lands.
- Job or assignment: none. Coordinator run from the skip queue. The handed-off tickets are in `material/skip-audit/overnight-queue-2026-10-18.md`; this claim covers only SOLR-16430 and SOLR-5628.
- Slice: SOLR-16430 (`LocalFSCloudIncrementalBackupTest`, two runs) and SOLR-5628 (`TestDistribDocBasedVersion` with the `commit(); // work arround SOLR-5628` line removed in the repro worktree only, two runs, then reverted).
- Capability tags used: sweep.
- Started: 2026-10-18T02:10:03Z (UTC).

Heartbeat: 2026-10-18T02:19Z. Worktree `/workspace/worktrees/jira-main` at fork `origin/main` 498a09aea9. Run 16430-r1 is still compiling. Logs and XML in `/workspace/gates/logs/vm5/jira/`. The remaining runs are queued in `/workspace/tools/vm5-jira-seq2.sh`.

Heartbeat: 2026-10-18T03:12Z. All four queued runs finished (`SEQ.done` 02:46Z). Worktree `/workspace/worktrees/jira-main` is clean at 498a09aea9; the SOLR-5628 workaround line is restored.

Result 16430-r1: `:solr:core:test --rerun --tests LocalFSCloudIncrementalBackupTest`, head 498a09aea9, PASS (7 run, 1 skipped, 0 failures), seed 2D8913536249D12A, log `/workspace/gates/logs/vm5/jira/16430-r1.log`, XML `16430-r1.xml/`. Conclusive: no. A pass does not show the intermittent failure is absent.

Result 5628-mut-r1: same build with the workaround line removed, `--tests TestDistribDocBasedVersion`, head 498a09aea9, PASS (2 run, 0 failures), seed 5C08B289F49822BA, log `/workspace/gates/logs/vm5/jira/5628-mut-r1.log`, XML `5628-mut-r1.xml/`. Conclusive: no. One pass on a removed workaround is a weak signal.

Result 5628-mut-r2: same as 5628-mut-r1, PASS (2 run, 0 failures), seed A724BBA2BE293D7E, log `/workspace/gates/logs/vm5/jira/5628-mut-r2.log`, XML `5628-mut-r2.xml/`. Conclusive: no, same reason.

Result 16430-r2: `LocalFSCloudIncrementalBackupTest` again, head 498a09aea9, PASS (7 run, 1 skipped, 0 failures), seed 4A3BAEC6CC3D5463, log `/workspace/gates/logs/vm5/jira/16430-r2.log`, XML `16430-r2.xml/`. Conclusive: no, same reason as 16430-r1.

Runner note: all four runs were launched by `/workspace/tools/vm5-jira-run.sh`, which takes `/workspace/tools/.vm3-gradle.lock`, not this host's `/workspace/tools/.gradle-vm5.lock`, and is not `pool/bin/sweep-run.sh`. The vm5 lock was free when the pool tick checked it at 03:10Z. The runs ended 02:46Z, so this did not overlap any pool-tick build.

Git history: the SOLR-5628 workaround line was added in `b221115ebc` (SOLR-5628). The test file's last change is `d5ee8873eb` (SOLR-18332), which is unrelated.

Missing for a conclusive verdict: the ticket's failure signature, to choose a variant that separates the explanations. Neither ticket is reproduced. The claim stays ACTIVE with this as its next step.

Append one heartbeat line at least once an hour while the claim is active, and push it.
