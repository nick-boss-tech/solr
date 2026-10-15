# Claim: vm2 finish list round 1, section C (SOLR-17055 proof rerun at the fix commit)

- Claimed: 2026-10-14T23:30ZZ by vm2. Assignment: assignments/pool-vm2-finish-list-round-1.md, section C. Job: gates/SOLR-17055-sort-path-proof-rerun.md.
- Fix commit: 2fe0986158391908ac89564090eb97e245788023 (parent c2396f425fa2, fork tip verified by ls-remote at claim time).
- Runner: gates/g17055-proof-c-gate.sh (worktree /workspace/gates/solr-17055-fix). Seed 17055C0FFEE17055.
- Push on green: a fast-forward of solr-17055-submit from c2396f425f, done by vm2 after the legs are read, not by the runner.
- Status: CLAIMED, runner queued.

- 2026-10-15T00:13ZZ vm2 finish list section C: gate green at ; fork pushed (fast-forward from c2396f425f). Draft and receipt refresh left open.

- 2026-10-15T00:32ZZ vm2 finish list section C follow-up: claimed the two owed knn tests on 2fe0986158 (id desc already present in the landed head; this adds the maxScore pins). Runner gates/g17055-followup-gate.sh queued. Push only on green, as a fast-forward of solr-17055-submit from 2fe0986158.

- 2026-10-15T01:03Z vm2 finish list section C follow-up, run ended 01:00:13Z: local commit `5d6b81d86b` (not pushed). Focused `DistributedKnnTopKTest` rc 1 at the id-sort maxScore pin (`.response.maxScore:0.99756694!=null`); spotless, Error Prone compile and module check rc 0. Not GREEN; no fork push; not relaunched. Recorded in gates/SOLR-17055-sort-path-proof-rerun.md. Status: ENDED, not green, left for the main agent; section C follow-up not closed.
