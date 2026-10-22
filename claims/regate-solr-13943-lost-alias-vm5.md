# Claim: SOLR-13943 regression test for the lost alias update (vm5)

- Claimant host: vm5.
- Status: ACTIVE.
- Job or assignment: none. Request from Nick in chat, 2026-10-22: add a regression test for the lost-alias case to PR 5089 and re-gate.
- Slice: test code only on `solr-13943-submit` (PR 5089, head aa14590303d059025fe7b5baaaf6ad784ad53787). No production change on this branch.
- Capability tags used: implementation, gate.
- Started: 2026-10-22T04:38:16Z (UTC).
- Lease: live while heartbeats continue; stale 2 hours after the last heartbeat. Attempts: 1. Idempotency key: testFixingStartKeepsOtherAliases@aa14590303.
- Links: Ticket or parent: SOLR-18562 (fix in PR 5191, fork SpookyJumpyBeans, head 07d000c2b3, not on this branch); parent PR 5089 (SOLR-13943). Category: update processing and atomic updates. Siblings: PR 5088 (SOLR-13696, same TimeRoutedAlias file). Supersedes: none.

Plan, recorded before the first run:
- Defect: TimeRoutedAlias.validateRouteValue applies the fixed router.start with the snapshot `aliases` captured before the write, not the `a` the AliasesManager passes in, so an alias created on other nodes in the meantime is dropped.
- Test: testFixingStartKeepsOtherAliases in TimeRoutedAliasDateMathInStartTest, modeled on the regression test in PR 5191. It is @AwaitsFix(SOLR-18562) because the fix is not on this branch; the annotation is removed when the fix lands.
- Runs, in order: chain (tidy, Error Prone compile, awaitsfix run of the new method, normal class run, :solr:core:check -x test); then mutation run with the PR 5191 production hunk applied locally and not pushed, awaitsfix on, expected to pass.
- Push to solr-13943-submit only if the chain and the mutation run match the plan.
