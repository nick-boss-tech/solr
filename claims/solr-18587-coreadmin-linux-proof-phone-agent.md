# Claim: SOLR-18587 CoreAdmin unload Linux proof, phone agent

- Claimant host: phone agent, matching `hosts/phone-agent.md` capability tags (implementation, gate, review, records).
- Status: CLAIMED.
- Job: `assignments/solr-18587-coreadmin-unload-failed-reload-implementation-round-1.md`, Linux proof leg only (the Windows implementation slice is already recorded in `claims/solr-18587-windows-implementation-round-1.md` and `reports/solr-18587-coreadmin-windows-implementation-round-1.md`).
- Fork branch: `nick-boss-tech/solr:solr-18587-coreadmin-unload-failed-reload`.
- Head under proof: `13e997b3882c1af139b470a16a026e31c7bc837f` (two commits above RC3 base `749712b71940c9f96d824c533f32f5c56dcd3924`: `1de4f006f1` production fix in CoreContainer.java plus changelog, `13e997b388` test-only `useFactory(null)` in CoreAdminHandlerTest.java). Fork ref verified by ls-remote at claim time.
- Pre-claim checks: no Linux proof claim, report, or receipt for this head on coordination-ui; Windows claim/report exist and are proof-pending only; assignment Status OPEN.
- Legs, in order, seed `787C6229591C6F1E`, single method `org.apache.solr.handler.admin.CoreAdminHandlerTest.testDeleteInstanceDirAfterCreateFailure`, module `:solr:core`, fresh JUnit XML each leg, never committed:
  1. Head leg at `13e997b388`, must PASS.
  2. Intermediate leg: RC3 base production with only the head test file overlaid (tests whether the `useFactory(null)` test change alone passes without the production fix).
  3. Base leg: RC3 production and test, expected FAIL (already recorded by vm5 at this seed; re-run only if legs 1-2 leave the pair ambiguous).
- Deliverable: `reports/solr-18587-coreadmin-fix-round-1.md` (the assignment's report), with the probe/pair result, the diff, and the branch name and head SHA. No PR, no Jira/GitHub post.
- Worktree: `~/workspace/solr-worktrees/18587-proof`, clean at the head. Runner: `~/workspace/tools/18587-proof-runner.sh`, output `~/workspace/tools/18587-proof-runner.out`, holds the test-queue flock. Logs and XML copies under `~/workspace/tools/18587-proof-*`.
- Constraints: one build at a time through the test-queue flock; all Gradle through `~/workspace/tools/solr-gradle.sh`; no production edit in this unit (a repair, if needed, gets its own superseding treatment).
