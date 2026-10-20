# Gate job: SOLR-18458 review pass 3 (vm3)

Status: CLAIMED (claims/pool-newdev-critblock-review-round-1-solr-18458-submit.md, takeover line 2026-10-20T21:30:18Z)

- Branch: `solr-18458-submit`. Head: `344485485fb8e1706a1e04c02e373d8a97bcd1b8` (the head pass 1 and pass 2 reviewed; fork tip confirmed by ls-remote at pass 1). No repair is expected in pass 3; if the tip moved, stop and record it here.
- Prior gated head: `344485485f` (pass 1 and pass 2 regates, GREEN). Merge-base with upstream: `c8ab71430f`.
- Intended host: vm3 (takeover of the vm4 pass-3 slot after a stale heartbeat). Capability tags: gate.
- Module: `:solr:core`, `:solr:solrj`, `:solr:modules:ltr`, `:solr:modules:language-models`. Seed: `18458C0FFEE18458`.
- Diff scope against `c8ab71430f`: 60 files, as recorded in gates/SOLR-18458.md.
- Why: unit 3 of the critical-blocker review round, pass 3. The regate is the sign-off, even with no change.
- Step 3 (proof leg): not repeated here. Pass 2 ran it on the same head with the same result (2 of 8 tests fail on base as expected); the proof leg is recorded in gates/SOLR-18458-review-pass-2.md.
- Step 4 scope: the touched test classes from the diff, run in full, plus the minimal filters from gates/SOLR-18458.md. Per-module lists are in gates/logs/vm3/solr-18458-review-pass-3/ (core 28 classes, solrj 1, ltr 15, language-models 1). Pass 2 counted 30 core classes; the difference is recorded in the regate result.
- Runner: `/workspace/tools/gate-solr-18458-p3.sh` on vm3, under `flock /workspace/tools/.vm3-gradle.lock`. Worktree `/workspace/worktrees/solr-18458-p3`. Log: `gates/logs/vm3/solr-18458-review-pass-3/`.

Links: Ticket or parent: SOLR-18458. Category: 4? See pool/work-map.md (the parent review row). Siblings: the review round units 3 and 12 (pool-newdev-critblock-review-round-1). Supersedes: none.

## What to run

- Step 0: changelog YAML parse of `changelog/unreleased`.
- Step 1: tidy. If tidy changes a file, apply it as a gate repair and retry once from step 0.
- Step 2: Error Prone compile of the four modules, clean compile tasks.
- Step 4: clean and focused runs per module, counted from fresh JUnit XML with `/workspace/tools/count-junit.py`.
- Step 5: module checks `-x test` for the four modules.

Failures follow WORKFLOW.md, Gate repairs. A failed retry hands the job back.

## Decisions

| Decision | Answer or default | Condition and action | Owner |
| --- | --- | --- | --- |
| Pass 3 repair scope | default: WORKFLOW.md Gate repairs only | a finding outside that scope is recorded in the pass 3 section, not fixed | vm3, rung 4 |
| Pass 1 minor finding (silent debugQuery drop, no DeprecationLog) | default: recorded, not repaired | changes behaviour; not in repair scope | vm4 pass 1 |
