# Gate job: SOLR-16155 review pass 3 (vm3)

Status: CLAIMED (claims/pool-newdev-critblock-review-round-1-solr-16155-submit.md, takeover line 2026-10-20T23:11:49Z)

- Branch: `solr-16155-submit`. Head: `0881de1ed68b847c781abd7ccb56196cdbf15ecd` (ls-remote unchanged since pass 1). If the tip moved, stop and record it here.
- Prior gated head: `0881de1ed6` (pass 1 and pass 2 GREEN). Base: `14c7aac0d1`.
- Intended host: vm3 (takeover of vm5's stale pass-3 slot). Capability tags: gate.
- Module: `:solr:core`. Focused classes: `JavabinLoaderTest`, `DocumentBuilderTest`, `TolerantUpdateProcessorTest`. Seed: default (no seed pinned, as in passes 1 and 2).
- Diff scope: unchanged from pass 1.
- Why: unit 12, pass 3. The regate is the sign-off, even with no change.
- Runner: `/workspace/tools/gate-solr-16155-p3.sh` on vm3, under `flock /workspace/tools/.vm3-gradle.lock`. Worktree `/workspace/worktrees/solr-16155-p3`. Log: `gates/logs/vm3/solr-16155-review-pass-3/`.

Links: Ticket or parent: SOLR-16155. Category: 1, Update processing and atomic updates (DocumentBuilder and JavabinLoader are in the update path). Siblings: review round units 3 and 13. Supersedes: none.

## What to run

- Step 0: changelog YAML parse of `changelog/unreleased`.
- Step 1: tidy. If tidy changes a file, apply it as a gate repair and retry once from step 0.
- Step 2: Error Prone compile of `:solr:core` main and test, clean compile tasks.
- Step 3, proof leg: not repeated; the base-versus-head difference was shown in pass 1 by the test expectations changing with the message shape.
- Step 4: clean and focused run of the three classes, counted from fresh JUnit XML.
- Step 5: `:solr:core:check -x test`.

Failures follow WORKFLOW.md, Gate repairs. A failed retry hands the job back.

## Decisions

| Decision | Answer or default | Condition and action | Owner |
| --- | --- | --- | --- |
| Pass 3 repair scope | default: WORKFLOW.md Gate repairs only | a finding outside that scope is recorded in the pass 3 section, not fixed | vm3, rung 4 |
| Exception text dropped from the response (pass 1 minor note) | default: recorded, not repaired | changes behaviour; the changelog discloses it; owner's call | vm5 pass 1 |
