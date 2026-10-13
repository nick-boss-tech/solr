# Claim: SOLR-18541 fail-before for the two new merge-totals tests, analysis round 1

- Claimed by: windows (review coordinator), Claude Code on this host, working for Nick Shanin
- Capability tags: review
- Trigger: `gates/SOLR-18541-review-r1-fail-before-new-tests.md` recorded FAILED at finding step 4: the two new tests (`skippedPartialShardWithoutCountClearsExactness`, `skippedPartialShardWithoutHitsClearsExactness`) PASS on the merge-base production at `a93f9f6a997e`, where the job expected them to fail (run 2026-10-13T22:53Z to 22:59Z, seed `C0FFEE42C0FFEE42`, log `logs/vm2/18541-failbefore-focused.log`).
- Scope: read why the two tests pass at the merge-base, state whether they pin the side-branch rule at all, and propose the test change that would make each discriminate. Written as a report and a proposed test patch under `gates/`. Not pushed, not gated, not moved onto any branch. The main side decides which expectation stands.
- Limits: no build, test or Gradle on this host; no PR, comment or branch change; nothing posted.
- Heartbeat: 2026-10-14T00:10Z (claim taken).
- Status: IN PROGRESS.
