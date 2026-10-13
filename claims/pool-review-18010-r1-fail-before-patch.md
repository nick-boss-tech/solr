# Claim: SOLR-18010 review patch round 1, fail-before premise fix (review side)

- Claimed by: windows (review coordinator), Claude Code on this host, working for Nick Shanin
- Capability tags: review (writes the patch)
- Assignment: `assignments/pool-vm2-18010-review-r1-fail-before-patch.md` (posted by vm2 at 2026-10-13T18:25Z).
- Scope: the test change to `SecurityConfHandlerTest.java` on `review-r1-solr-18010` (head `e99be67c9a6fe15f86306104843a30b35196f159`, parent `2b5ad801c9e88807337f4f0a3fdd689126ad37d1`). Prepared as a diff in the repo under `gates/`, checked against the code by reading, and summarized in the round report. The push to the fork, a plain fast-forward, waits for Nick's go-ahead.
- Choice to record: route 1 (force the temporary-file delete failure inside the test, portably) or route 2 (rename the test to what it pins and drop the claim about the `finally` change). The recommendation is recorded in the round report.
- Limits: no build, test or Gradle on this host. A patch counts as reviewed only after vm2 gates the pushed head. No change to `solr-18010-submit`, and no edit to PR #5118.
- Heartbeat: 2026-10-13T18:35Z (claim taken).
- Status: PREPARED 2026-10-13T19:00Z. Route 1 is recommended and saved as `gates/SOLR-18010-fail-before-route1.patch`; route 2 is the fallback, saved as `gates/SOLR-18010-fail-before-route2.patch`. Neither is pushed. The push to `review-r1-solr-18010` (a plain fast-forward from `e99be67c9a6f`) waits for Nick's go-ahead, and the vm2 gate waits for the push. Summary in `reports/18010-fail-before-route-round-1.md`.
