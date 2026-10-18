# Claim: pool-skip-repro-flake-locate-round-1

- Claimant host: vm5, matching a file in `hosts/` (`hosts/vm5.md`).
- Status: DONE 2026-10-18T06:55Z (deliverable below).
- Job or assignment: `assignments/pool-skip-repro-flake-locate-round-1.md`.
- Slice: the whole assignment, 17 tickets (SOLR-2715 through SOLR-18007), at the pinned head `498a09aea9`.
- Capability tags used: sweep.
- Started: 2026-10-18T06:33:01Z (UTC).

Heartbeat: 2026-10-18T06:33:01Z

Result: 2026-10-18T06:53:14Z: head 498a09aea9 (worktree `/workspace/worktrees/vm5-skip-flake-locate`, detached). Five located classes run once each with `--rerun` under the host lock, all BUILD SUCCESSFUL: DeleteReplicaTest (seed 80D3237B6848C6DD, 7/0/0/0), UpdateParamsTest (AF03155EB66B7756, 1/0/0/0), TestReplicationHandler for SOLR-9426 (420562093AFD92BD, 25/0/0/25) and for SOLR-12220 (265F41BA244CFB3, 25/0/0/25), EnumFieldTest (364457875F164E62, 15/0/0/1), TestAnalyzedSuggestions (D60B805699E59882, 2/0/0/0). Conclusive: each run passed, no failure to reproduce. Logs under `/workspace/gates/logs/vm5/skip-flake-locate/`.

Result: 2026-10-18T06:55Z: headroom for every run, `df -h /` 3.6G free and `df -i /` 454453 to 454455 inodes free, before and after each run (sweep start 06:37:49Z, end 06:53:14Z). Above the 2 GB and 10% guards.

Result: 2026-10-18T06:55Z: twelve tickets not run. Ten have no class in the JIRA description (SOLR-2715, 2720, 3944, 5948, 7646, 9182, 11208, 11214, 12369, 12748); two of those name a class only in the summary (2715 and 2720), escalated. Two name classes not on main at this head (SOLR-11168 `ConcurrentUpdateSolrClientTest`, SOLR-11259 `SharedFSAutoReplicaFailoverTest`), recorded "not on main".

DONE: 2026-10-18T06:55Z. Deliverables: `reports/2026-10-18-skip-repro/A2-flake-locate-round-1.md`.
