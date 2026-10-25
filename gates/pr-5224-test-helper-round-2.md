# Gate: PR 5224 test helper reader lifetime, round 2 (supersedes round 1)

Status: RUNNING. Host: vm6 (Linux, JDK 21.0.12.1+1). Tags: gate.
Supersedes: `gates/pr-5224-test-helper-round-1.md`, which named head `1f3ed5658862b8fd682fcb999a1d3dd12863957f`.

- Exact gated head: `72a0daacd37478f3954272f4fa55511703af4b78` (fork `nick-boss-tech/solr:solr-18585-submit`).
- Why a new head: the round-1 preflight (`tidy` on a clean clone of `1f3ed5658862`) changed one file, `solr/core/src/test/org/apache/solr/search/TestScriptFilterIntervalsSource.java`, in two line-wrap places. Per the round-1 gate's repair rule, the output was committed as `72a0daacd3` ("Tidy TestScriptFilterIntervalsSource line wrapping", Nick Shanin, no trailers) on top of `1f3ed5658862`, pushed as a fast-forward. The change is formatting only: 4 insertions and 2 deletions, no production code or assertion changed.
- Parent/proof base: apache/solr PR 5224 head `f28c6e79c82a9fb74c3e07607b86e74b3d4331d0` (unchanged).
- Claim: `claims/pr-5224-test-helper-gate-vm6-round-1.md`.

## Steps (in order, all at `72a0daacd3`)

1. Preflight: `gate-preflight.sh` on a clean clone of the exact head. Must exit 0 (tidy leaves the tree unchanged). Log: `gates/logs/vm6/pr-5224-r2/preflight.out`.
2. Focused run with fresh results:
   `./gradlew :solr:core:cleanTest :solr:core:test --tests org.apache.solr.search.TestScriptFilterIntervalsSource -Ptests.seed=6A68830B3A1BB525 -Ptests.workDir=/tmp/vm6-pr5224-tests`
   Requirement: all four methods (`testMatchesSkipsRejectedLeadingIntervals`, `testMatchesFirstAcceptedIntervalIsNotLost`, `testMatchesNullWhenNoIntervalAccepted`, `testMatchesNullWhenNoTermInDocument`) execute, with zero failures, errors, or skips, read from fresh `build/test-results/test/TEST-org.apache.solr.search.TestScriptFilterIntervalsSource.xml`.
3. `./gradlew check -x test -Ptests.workDir=/tmp/vm6-pr5224-tests`, recording the real result and any independent baseline failure.
4. `git diff --check` and a clean tree at `72a0daacd3`.

## Limits

- No write to apache/solr, mkhludnev/solr, the live PR, JIRA, or GitHub comments.
- No deterministic failure of the parent helper is claimed; this gate proves the new helper's test lifetime, not a production defect.

Receipt target: `receipts/PR-5224-test-helper.md`.
