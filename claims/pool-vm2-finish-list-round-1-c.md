# Claim: vm2 finish list round 1, section C (SOLR-17055 proof rerun at the fix commit)

Status: STALE 2026-10-17 (no heartbeat within 2 hours, WORKFLOW.md rule 4; linked job assignments/pool-vm2-finish-list-round-1.md: Status: CLAIMED (claims/pool-vm2-finish-)

- Claimed: 2026-10-14T23:30ZZ by vm2. Assignment: assignments/pool-vm2-finish-list-round-1.md, section C. Job: gates/SOLR-17055-sort-path-proof-rerun.md.
- Fix commit: 2fe0986158391908ac89564090eb97e245788023 (parent c2396f425fa2, fork tip verified by ls-remote at claim time).
- Runner: gates/g17055-proof-c-gate.sh (worktree /workspace/gates/solr-17055-fix). Seed 17055C0FFEE17055.
- Push on green: a fast-forward of solr-17055-submit from c2396f425f, done by vm2 after the legs are read, not by the runner.
- Status: CLAIMED, runner queued.

- 2026-10-15T00:13ZZ vm2 finish list section C: gate green at ; fork pushed (fast-forward from c2396f425f). Draft and receipt refresh left open.

- 2026-10-15T00:32ZZ vm2 finish list section C follow-up: claimed the two owed knn tests on 2fe0986158 (id desc already present in the landed head; this adds the maxScore pins). Runner gates/g17055-followup-gate.sh queued. Push only on green, as a fast-forward of solr-17055-submit from 2fe0986158.

- 2026-10-15T01:03Z vm2 finish list section C follow-up, run ended 01:00:13Z: local commit `5d6b81d86b` (not pushed). Focused `DistributedKnnTopKTest` rc 1 at the id-sort maxScore pin (`.response.maxScore:0.99756694!=null`); spotless, Error Prone compile and module check rc 0. Not GREEN; no fork push; not relaunched. Recorded in gates/SOLR-17055-sort-path-proof-rerun.md. Status: ENDED, not green, left for the main agent; section C follow-up not closed.

- 2026-10-15T01:16ZZ vm2 finish list section C follow-up: gate RED, not pushed, not relaunched. Head 5d6b81d86b (local commit on 2fe0986158, not on the fork). Log logs/vm2/queue-g17055-followup-gate-20261015T003834Z.out. Spotless, compile and module check passed; DistributedKnnTopKTest 1 test, 1 failure: at line 95 (sort=score desc, fl=id), the distributed response has maxScore 0.99756694 and the single-node control has null (compareResponses, BaseDistributedSearchTestCase line 996). This is a distributed-versus-control difference in maxScore for a score-sorted knn request, and it is the behavior question 3 of reports/solr-17055-implementation-choice-round-1.md raised. Needs a main-side decision: either the control comparison excludes maxScore for knn, or the fix changes the distributed value. Fork branch solr-17055-submit stays at 2fe0986158.

- 2026-10-16T17:07Z vm2 wind-down: handed to vm1 under assignments/pool-vm2-handoff-to-vm1-round-2.md (no further vm2 work on this claim).

STALE: no live work by vm2 since 2026-10-18; marked by the vm2 coordinator so the pool tick does not resume it. Work not finished, see the job or assignment file.
