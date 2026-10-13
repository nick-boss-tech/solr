# QueryComponent real-exit premise and SOLR-18010 permission legs G1 and G2: round 1

Trigger: pr-prepare tip `d561f246823`, recorded from `0007e72`. vm2's queue pass closed the QueryComponent real-exit premise and the SOLR-18010 permission legs G1 and G2. Round claim: `claims/pool-pr-prepare-tip-d561f24-round-1.md`.

Scope: records and one draft edit. No gate relaunched, no status line changed by this round, no PR, comment or live body touched, no fork branch changed, no build, test or Gradle run.

## 1. QueryComponent real-exit premise: the closure does not settle the width

- No plain QueryComponent exit was run. The vm1 claim still reads "CLAIMED, run queued", with no result or log.
- The record cites a run of CombinedQueryComponent, at head `d76fc14b9857` with seed `C0FFEE42C0FFEE42`. That code is the SOLR-18541 side, not the SOLR-18539 mergeIds code, and the record does not report the exiting shard's `numFoundExact`.
- The width question asks what `numFoundExact` a real plain QueryComponent exit produces for a skipped shard with zero hits. TRUE would mean Option B misses that shape. The record does not answer it.

Verdict: the width stays open. The settling fact is the plain QueryComponent exit result, from the vm1 run that is still queued.

## 2. SOLR-18010 permission legs G1 and G2

Head check: `solr-18010-submit` is `2b5ad801c9e88807337f4f0a3fdd689126ad37d1`, as recorded.

- G1 (permission test, reverted copy). The receipt quotes the assertion: `expected:<[OWNER_READ, OWNER_WRITE, GROUP_READ]> but was:<[GROUP_READ, OWNER_READ, OTHERS_READ, OWNER_WRITE]>`, which is 0640 expected and 0644 observed. The head is `2b5ad801c9e` with the copy removed, the seed is `18010C0FFEE18010`, and the log is `logs/vm2/18010-perms-g1-reverted.log`, on vm2 only. No run date is recorded for it. An earlier run at `9e3ccafe39` also reports 0644. The code reading predicts 0600 (the JDK 21 temporary-file helper gives owner-only), and the cause of 0644 is unexplained.
- G2 (`MultiAuthPluginTest`). Six of six, with zero failures, zero errors and zero skipped, at `2b5ad801c9e`, seed `18010C0FFEE18010`, dated 2026-10-13T18:35Z. The test names are not recorded, but the source has six `@Test` methods, none ignored.
- The three-part evidence (`SecurityConfHandlerTest` 4 of 4, `BasicAuthStandaloneTest` 1 of 1, `V2SecurityAPIMappingTest` 5 of 5, each with zero skipped and zero failures, from fresh XML at `2b5ad801c9e`) is recorded and rerun at 18:38Z.
- G4a (symlink). The premise run confirmed that the symlink became a regular file and the link's target was unchanged. The new `security.json` was not read back, so the run is short of the gate file's spec.
- G4b (bind mount). Still skipped, for lack of mount rights.
- G3 is still owed, and the vm1 claim for G1 and G2 is still listed as CLAIMED.

Draft edit applied to `pr-drafts/core-admin/SOLR-18010.md`: the stale sentence "A check with the permission step reverted has not been completed at this head, so no failure value is stated here" is replaced with "With the permission copy removed, the test reports 0644 where 0640 was expected." This is the test report from the named record. It is not a cause, and the draft does not give one.

Verdict on input 6: OPEN, not DONE GREEN. The open legs are G4b (no mount rights) and G3 (after any code change). The 0644 value is recorded but its cause is not explained.

## 3. What this round changes for the owner

- SOLR-18539 width: still open. The QueryComponent plain-exit run must report `numFoundExact` for the zero-hit skipped shard. Until then, Option B and any-skip are both unsettled.
- SOLR-18010 permission value: the record gives 0644, the code reading gives 0600. The record has no date, and the log is on vm2 only. Copying the log's assertion lines onto the branch would settle what the run reported. The cause still needs an explanation.
- SOLR-18010 bind mount: needs a Linux host with mount rights.
- The vm1 claim for the permission legs is still listed as CLAIMED, which is a record correction for the vm1 lane.
