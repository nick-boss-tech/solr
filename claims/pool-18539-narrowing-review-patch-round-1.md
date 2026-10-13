# Claim: SOLR-18539 review patch narrowing to Option B, round 1

- Claimed by: windows (review coordinator), Claude Code on this host, working for Nick Shanin
- Capability tags: review (writes the patch), draft
- Task: the narrowing task in the premise addendum of `reports/four-new-prs-strong-review-round-1.md` (SOLR-18539 section), recorded after the plain QueryComponent real-exit run (`gates/queryComponent-real-exit-shape-premise.md`). The run supports canonical Option B.
- Branch: `review-r1-solr-18539` at `fbf7610644d178049e1110ffc0ec34dad8c5b32f` (checked by ls-remote before work). The PR branch `mergeids-skipped-shard-totals` at `4eb996121804b043f1ee53f5bb7e22b9c3021b0a` is NOT touched.
- Scope: production `QueryComponent.mergeIds` and `QueryComponentPartialResultsTest`, as the task lists. Prepared as a patch file under `gates/`, checked by reading, then pushed to the fork branch as a plain fast-forward (owner allowed fork pushes on 2026-10-13). The gate runs on a Linux host, not here.
- Limits: no build, test or Gradle on this host; no PR opened, closed, flipped or commented on; no PR branch moved; the branch move onto the PR branch is NOT proposed until the re-gate is green.
- Heartbeat: 2026-10-14T00:10Z (claim taken).
- Status: PATCH PREPARED 2026-10-14T00:40Z. `gates/SOLR-18539-narrowing-round-1.patch` (dry-run clean against `fbf7610644d1`) and its gate job `gates/SOLR-18539-narrowing-round-1-gate.md` (OPEN for vm2). Not pushed: the fork push wrapper does not accept the branch name `review-r1-solr-18539`, and the route for that push is an owner decision. Not claimed as done until the push and the gate.
