# Claim: pool-vm5-skip-repro-16430-5628-round-1

- Claimant host: vm5, matching a file in `hosts/`.
- Status: DONE 2026-10-18T03:45Z (verdicts keep-open for both tickets; deliverables below).
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

Heartbeat: 2026-10-18T03:37Z. Failure signatures from `skip-ticket-evidence/evidence.py` at `origin/main` 498a09aea9. SOLR-16430: `expected:<COMPLETED> but was:<RUNNING>` from `processAndWait(cluster.getSolrClient(), 20)` at `AbstractIncrementalBackupTest.java:517` in `testRestoreToOriginalSucceedsWithErrors` (line 494). The 2022 fix (9e72a75caa) moved a different call from 20 to 30. SOLR-5628: IndexWriter failure and stall with uncommitted docs at the end of `TestDistribDocBasedVersion`, found by seed in the 2014 ticket. No seed is valid at this head.

Result 16430-load-r1: `:solr:core:test --rerun --tests LocalFSCloudIncrementalBackupTest`, head 498a09aea9, run with two `nice -n 19` CPU-busy loops on the host, PASS (7 run, 1 skipped, 0 failures), seed F5D796538F63B4C8, log `/workspace/gates/logs/vm5/jira/16430-load-r1.log`, XML `16430-load-r1.xml/`. Slowest case `testRestoreToOriginalSucceedsWithErrors` 13.2 s, under the 20 s wait. Conclusive: no. Load did not push the wait over its limit, and a pass does not show the RUNNING state is absent.

Result 5628-mut-r3 to r6: `:solr:core:test --rerun --tests TestDistribDocBasedVersion` with the `commit(); // work arround SOLR-5628` line removed in the repro worktree, head 498a09aea9, four fresh seeds, all BUILD SUCCESSFUL: 66D54ADD1FBC0370, 2CC62960048BE36E, 43422EEDA46C6F09, C9B32D7322B8F225. Logs `/workspace/gates/logs/vm5/jira/5628-mut-r{3,4,5,6}.log`, XML for r6 only in `5628-mut-r6.xml/`. Reverted; repro worktree clean at 498a09aea9. Conclusive: no. With r1 and r2, eight passes without the workaround and no failure. The 2014 seed is not valid at this head, so the ticket's symptom is not reproduced.

Runner note: `pool/bin/sweep-run.sh` failed twice this tick. (a) On this host the login shell has no `JAVA_HOME`, so `gradlew` exited 1 before any BUILD line. The script reported this as "lock held", but `flock -n` on `.gradle-vm5.lock` succeeded. The later runs set `JAVA_HOME=/workspace/jdk/jdk-21.0.12.1+1`. (b) Its XML copy compares with `-newer` against the headroom log, which is rewritten at the end of the run, so the copy missed the XML for 16430-load-r1 and 5628-mut-r3 to r5. Those were copied by hand. The script was not changed.

Missing for a verdict: SOLR-16430 needs the macOS environment or the failing run's timing from the ticket. SOLR-5628 needs the full 2014 description (the failing seed and the svn revision), which is past the first 1200 characters the evidence helper prints, or a fail-before reproduction on a current base. The claim stays ACTIVE. Next tick decides keep-open with a record, or runs a different variant.

DONE: 2026-10-18T03:45Z. Verdicts keep-open for both tickets, recorded in `material/skip-audit/queue-current.json` at 498a09aea9.
- SOLR-16430: record `drafts/SOLR-16430/record.md`, comment draft `drafts/SOLR-16430/jira-comment.md`, branch `solr-16430-draft-open`.
- SOLR-5628: record `drafts/SOLR-5628/record.md`, comment draft `drafts/SOLR-5628/jira-comment.md`, branch `solr-5628-draft-open`.

Decision on the next step: the failing 20-second wait in `testRestoreToOriginalCollection` is at `AbstractIncrementalBackupTest.java:215` (backup), and the one at 516 is in `testRestoreToOriginalSucceedsWithErrors`. The 2014 seed in SOLR-5628 was not replayed on this head: the ticket says seeds are valid only for unchanged code, and the 2014 revision needs an ant-era build. Earlier note corrected: the SOLR-16430 failing call is at line 516, not 517.

Two items for the pool, not fixed here: `vm5-jira-run.sh` locks with `/workspace/tools/.vm3-gradle.lock`, which is vm3's lock name; and `pool/bin/sweep-run.sh` needs `JAVA_HOME` set on this host (see the runner note above).
