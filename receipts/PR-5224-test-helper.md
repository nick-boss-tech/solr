# Receipt: PR 5224 test helper, gate round 2 (vm6, Linux)

Gate: `gates/pr-5224-test-helper-round-2.md` (supersedes round 1). Claim: `claims/pr-5224-test-helper-gate-vm6-round-1.md`.
Exact gated head: `72a0daacd37478f3954272f4fa55511703af4b78` (fork `nick-boss-tech/solr:solr-18585-submit`). History: `1f3ed5658862` (Windows implementation, round 1) then `72a0daacd3` (tidy repair, Nick Shanin, no trailers, line wrapping only, 4 insertions and 2 deletions).
Host: vm6, Linux aarch64. JDK: `/workspace/jdk/jdk-21.0.12.1+1`. GRADLE_USER_HOME `/workspace/gradle-home`. NODE_ENV unset. Build lock `/workspace/tools/.gradle-vm6.lock`.
Clean checkout: full clone of the fork at `72a0daacd3` (`/tmp/vm6-antora-clone`), not a git worktree.

## Results

| Step | Command | Result |
|---|---|---|
| Preflight | `gate-preflight.sh` on a clean clone of `72a0daacd3` | exit 0: tidy leaves the tree unchanged (`gates/logs/vm6/pr-5224-r2/preflight.out`) |
| Focused | `./gradlew :solr:core:cleanTest :solr:core:test --tests org.apache.solr.search.TestScriptFilterIntervalsSource -Ptests.seed=6A68830B3A1BB525 -Ptests.workDir=/tmp/vm6-pr5224-tests` | rc 0. Fresh XML: tests=4, failures=0, errors=0, skipped=0. PASS: `testMatchesNullWhenNoTermInDocument`, `testMatchesSkipsRejectedLeadingIntervals`, `testMatchesFirstAcceptedIntervalIsNotLost`, `testMatchesNullWhenNoIntervalAccepted` |
| Required check | `./gradlew check -x test -Ptests.workDir=/tmp/vm6-pr5224-tests` | rc 0, BUILD SUCCESSFUL in 4m 46s |
| Diff check | `git diff --check 1f3ed5658862 72a0daacd3` | clean |
| Tree | `git status --short` at `72a0daacd3` | clean (0 changed files) |

Raw evidence (copied, not edited): `gates/logs/vm6/pr-5224-r2/clone/evidence/` holds the XML, `focused.log`, `check.log`, `preflight.out`, `preflight.log`.

## Earlier attempt in a git worktree (same head, not the proof)

- Focused: rc 0, 4 of 4 PASS, same seed, fresh XML.
- Required check: failed at `:solr:solr-ref-guide:buildLocalAntoraSite` with `Cannot find module '@asciidoctor/tabs'`.
- Apache main `07fdebdda5`, also in a git worktree, failed the same Antora step with "Local content source must be a git repository". So the failure is the host's worktree layout, not the PR.
- In a full clone the same Antora step passes at the gated head (BUILD SUCCESSFUL in 2m 35s). The clean-clone run above is the required proof.
- Host lesson: run gate checks in a full clone. Antora does not accept a git worktree as its content source.

## Noise

Both focused runs print `java.security.policy: error adding Permission, java.net.URLPermission` five times, with `Locale provider adapter "CLDR" cannot be instantiated`. It appears in both runs and does not change any result. It is not investigated here.

## Limits

- This gate proves the four focused methods pass at the head with the stated seed. It does not prove they fail on the parent helper: no fail-before run was done, and the gate does not claim one.
- The parent (apache/solr PR 5224 head `f28c6e79c8`) was not run.
- No write to apache/solr, mkhludnev/solr, the live PR, JIRA, or GitHub comments.

## Suggested upstream update (for Nick to review)

At `72a0daacd3` on the fork branch (Linux, JDK 21.0.12.1, seed `6A68830B3A1BB525`): the four `TestScriptFilterIntervalsSource` methods pass with zero failures, errors, or skips from fresh results, and `./gradlew check -x test` passes. The helper now borrows the reader and does not close it. The change is test-only. No fail-before claim is made.
