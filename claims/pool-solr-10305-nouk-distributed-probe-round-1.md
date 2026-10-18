# Claim: SOLR-10305 distributed requests without a uniqueKey, probe round 1

- Claimant host: vm3, matching `hosts/vm3.md`.
- Status: DONE 2026-10-18T10:20Z.
- Job or assignment: `assignments/pool-solr-10305-nouk-distributed-probe-round-1.md`.
- Slice: the whole assignment (head check, base server check, harness check, success-path shapes, verdict).
- Capability tags used: premise-run.
- Intended host note: the assignment names vm1, with vm2 as fallback. It is not marked vm1 only, and no live claim by vm1 or vm2 exists on `origin/coordination` at 2026-10-18T09:37Z. vm3 takes it under the cron rule for an OPEN assignment with a Linux intended host and no live claim.
- Started: 2026-10-18T09:37:32Z (UTC).
- Head check at claim time: `solr-10305-submit` is `52e32f892c5e8ccdb474c8fc222ce8203955efd3` (matches the assignment). Base `cabedd1d968059215188f4e7563fb303241899ed` is present locally.
- Source changes: none on any branch. Probe changes stay in detached worktrees. No PR, comment, JIRA or push of code.
- Gradle runs go through `pool/bin/sweep-run.sh` with the vm3 lock.

Heartbeat: 2026-10-18T09:37:32Z (claim).

Finding at start (2026-10-18T09:45Z): the two files the assignment names, `gates/SOLR-10305-probe-cloud-test.java.txt` and `gates/SOLR-10305-probe-results.txt`, are not on `origin/coordination` or on this host. The vm2 probe source and raw results are therefore not reused. The probe is rebuilt from the assignment's scope: a SolrCloudTestCase with one node, the cloud-minimal configset with `<uniqueKey>` removed (test-files configset `probe-nouk`), one collection, and 14 request shapes sent straight to the core URL with `shards=<core URL>` (the Harness step), so the server response is not hidden behind the cloud client. The probe lives only in the detached worktrees `/workspace/worktrees/solr-10305-head` and `/workspace/worktrees/solr-10305-base`, untracked, and is not committed.

Plan: run the probe at head `52e32f892c` first, then at base `cabedd1d96`, each through `pool/bin/sweep-run.sh` under the vm3 lock, with `-Ptests.seed=10305C0FFEE10305`. Read the server log in the JUnit XML for each base failure and record the first stack frame.

Result: 2026-10-18T10:01Z: head run 10305-probe-head-r3 rc 0 (ran, 14 shapes; S02 printed a false NPE from the probe's own print, fixed in r4). Earlier head runs r1 and r2 were compile failures in the probe, no tests run.
Result: 2026-10-18T10:04Z: head run 10305-probe-head-r4, seed 10305C0FFEE10305, rc 0, tests=1 failures=0 errors=0. Head 52e32f892c: all 11 distributed shapes 400 with "Distributed search requires a uniqueKey field"; non-shards shapes 200. Log /workspace/gates/logs/vm3/10305-probe-head-r4.log.
Result: 2026-10-18T10:13Z: base run 10305-probe-base-r1, seed 10305C0FFEE10305, rc 0, tests=1 failures=0 errors=0. Base cabedd1d96: all 11 distributed shapes 500 with server NPE, first frame QueryComponent.createMainQuery:793 (10 shapes) or createDistributedStats:776 (query=false); non-shards shapes 200. Log /workspace/gates/logs/vm3/10305-probe-base-r1.log. Conclusive for the failing half; the non-rejection half has no distributed success case on base (see the deliverable).

DONE: 2026-10-18T10:20Z. Deliverables: reports/SOLR-10305-nouk-distributed-probe.md, gates/SOLR-10305-probe-cloud-test.java.txt, gates/SOLR-10305-probe-results.txt, assignments/pool-solr-10305-nouk-distributed-probe-round-1.md (Status DONE). Probe worktrees removed after the push.
