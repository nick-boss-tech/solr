# Receipt: PR 5255 alias-lock follow-up (SOLR-18589), gate round 4 (vm5)

- Gate job: `gates/pr-5255-alias-lock-followup-round-4.md`. Claim: `claims/pr-5255-alias-lock-followup-gate-round-4-vm5.md`. Supersedes rounds 1 to 3 (`gates/pr-5255-alias-lock-followup-round-1.md` and the round-2 and round-3 claims with their verdicts).
- Head: `52dacc23b5bb67d254c111c948733da7bc4a9f81` on `nick-boss-tech/solr:solr-18589-submit`. Parent and negative proof base: `1edd6a113b2a9e1b029f509c9279f4fb58683bba`. The head is three commits above round 1 head `fd49245a38ba779f78840477dbf6db34b92c5f6b`.
- Commits above round 1, each a fast-forward by Nick Shanin with no trailers: `c09411a16c` (tidy formatting of `AliasCollectionApiLockTest.java`), `bf5098b3da` (test compile repair: five chained `setFollowAliases(true).process*` calls on a `void` setter replaced by a helper), `52dacc23b5` (test read-order repair: `waitFor` deletes a finished async status, so both tests now poll for FAILED and read it once).
- Production code: the round-1 production change is unchanged. The three round-4 commits touch only `solr/core/src/test`. Diff against parent: 19 files, 338 insertions, 11 deletions.
- Host: vm5, Linux aarch64, JDK 21.0.12.1+1 at `/workspace/jdk/jdk-21.0.12.1+1`, Gradle 9.7.0 through the wrapper, `GRADLE_USER_HOME=/workspace/gradle-home`, `NODE_ENV` unset, host lock `/workspace/tools/.gradle-vm5.lock`.
- Logs and XML: `/workspace/gates/logs/vm5/pr-5255-alias-lock-followup-round-4/` (`results.txt` is the timeline; `xml-*` directories hold the fresh test XML).

## Results at 52dacc23b5

| Step | Command | Result | Evidence |
|---|---|---|---|
| Tidy | `./gradlew tidy` | exit 0, tree clean | `tidy-positive.log` |
| Focused, seed 5255C0DE | two focused classes | 6 run, 0 failures, 0 errors, 0 skipped | `focus-5255.log`, `xml-focus-5255/` |
| Focused, seed 18589C0DE | two focused classes | 6 run, 0 failures, 0 errors, 0 skipped | `focus-18589.log`, `xml-focus-18589/` |
| Neighboring suites, seed 5255C0DE | seven neighboring classes | 71 run, 0 failures, 0 errors, 1 skipped (see Exceptions) | `neighbors-5255.log`, `xml-neighbors-5255/` |
| Root check | `./gradlew check -x test` in full clone `/tmp/pr5255-clone` | BUILD SUCCESSFUL in 5m 39s, exit 0, tree clean | `root-check-clone.log` |
| Negative proof | parent worktree with the two test files overlaid from `52dacc23b5` (hashes in `failbefore-test-hashes.txt`) | 6 run: 4 intended failures, 2 passing controls | `negative-5255.log`, `xml-negative-5255/` |

## Negative proof, per test (parent production code)

- `testMultiCollectionAliasReturnsBadRequest`: fails at line 80, `expected:<400> but was:<500>`.
- `testAsyncMultiCollectionAliasPreservesErrorDetails`: fails at line 93, the stored exception is not a `NamedList`.
- `testAliasRetargetedAfterLockDoesNotDeleteUnlockedCollection`: fails at line 135, `DELETE must reject a collection different from its lock`.
- `testAliasRetargetedWhileWaitingDoesNotDeleteUnlockedCollection`: fails at line 152, `TimeoutException: Async delete did not fail` after 30 seconds. The assertion does not record the final state, so this is the observed absence of FAILED, not an observed COMPLETED.
- Controls, passing on the parent: `testStableAliasStillDeletesCollectionAndAlias`, `testAliasedCommandWaitsForCollectionLock`.

## Exceptions and limits

1. Not a clean zero-skip result. The neighboring run skipped `CollectionsAPISolrJTest.testCreateAndDeleteShard`, which is `@AwaitsFix(SOLR-13021)` and disabled by default. The file is unchanged from the parent (`solr/core/src/test/org/apache/solr/cloud/CollectionsAPISolrJTest.java`, annotation at line 313). The job's bar is zero skips, so this run is recorded as a deviation for the owner to accept or reject. It is not counted as a pass under that bar. Probe run afterward with `-Ptests.awaitsfix=true`, seed `5255C0DE`, in the gate worktree at `52dacc23b5` (`probe-awaitsfix.log`, BUILD SUCCESSFUL): 1 run, 0 failures, 0 errors, 0 skips. The skip hides no failure at this head. The probe changed no source.
2. Interrupted runs, no results from them: neighbors attempt 1 (session teardown during compile), root check attempt 1 in the worktree (session teardown at `ecjLintMain`), and negative attempt 1 (VM crash). Each was relaunched once. Neither root check attempt 1 nor the worktree run is evidence.
3. Root check in the worktree `/workspace/worktrees/gate-pr5255-vm5` failed at `:solr:solr-ref-guide:buildLocalAntoraSite`. A git worktree is not a valid Antora content source, the same host layout issue recorded in `receipts/PR-5224-test-helper.md`. The check was run in a full local clone instead, and passed.
4. Fail-before test on a worktree at the parent: `negative-5255.log` is the second attempt, after the VM crash. The first attempt left no results.

## Verdict

Every required step ran at the exact head and passed, except the one inherited skip in item 1. The four negative findings are discriminated on the parent, and the two controls pass. Under the job's strict zero-skip bar this is not a clean GREEN. The owner decides whether the inherited `@AwaitsFix` skip is acceptable for this gate.

## Suggested contributor follow-up (not posted)

Diff: `git diff 1edd6a113b2a9e1b029f509c9279f4fb58683bba 52dacc23b5bb67d254c111c948733da7bc4a9f81`, 19 files. The head is a fast-forward of the contributor's parent, so the contributor can apply it on `sarkaramrit1993/solr:alias-lock-key-18589` with:

```
git fetch https://github.com/nick-boss-tech/solr.git solr-18589-submit
git merge --ff-only 52dacc23b5bb67d254c111c948733da7bc4a9f81
```
