# Claim: SOLR-18010 permission fail-before and MultiAuthPluginTest, legs G1 and G2 only (vm1)

- Claimed: 2026-10-13T17:10Z by vm1. Job: gates/SOLR-18010-permissions-fail-before-and-multiauth.md.
- Capability tags: gate.
- Slice: legs G1 and G2 of the job only, at head `2b5ad801c9e88807337f4f0a3fdd689126ad37d1` of `solr-18010-submit`, seed `18010C0FFEE18010`. G1: the focused counts at the unreverted head from fresh JUnit XML (SecurityConfHandlerTest 4, BasicAuthStandaloneTest 1, V2SecurityAPIMappingTest 5, skipped 0), then `SecurityConfHandlerTest#testEditKeepsExistingSecurityJsonPermissions` with the permission block reverted to the state at `9e3ccafe39`, recording the permission set the assertion reports (the 0644 against 0600 question); the file is restored and the tree verified clean after the run. G2: `MultiAuthPluginTest` (6 tests) at the unreverted head. G3 stays for after the SOLR-18010 review patch gate (claimed by vm2) lands a code change; G4a and G4b are manual checks and stay open. The split is recorded in the job file.
- Host paths on vm1: worktree ~/workspace/solr-worktrees/strong-r1-18010-perms; runner ~/workspace/tools/gate-strong-review-vm1-chain.sh (chained, serial on the test-queue flock); log ~/workspace/tools/g18010-perms.log; fresh XML copies under ~/workspace/tools/g18010-perms-xml/.
- Status: CLAIMED, run queued in the chained runner.
- Heartbeat: 2026-10-13T17:10Z claim pushed, runner being launched.
