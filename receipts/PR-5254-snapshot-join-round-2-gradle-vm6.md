# Receipt: PR 5254 snapshot join, round 2, Gradle legs (vm6, partial)

Written by vm6. Claim: `claims/pr-5254-snapshot-join-round-2-vm6.md`. Job: `gates/pr-5254-snapshot-join-round-2.md`.

Verdict: PARTIAL. The Gradle legs pass at `842b1fb426`. This is NOT GREEN for the gate: the Docker image builds, the BATS positive run, and the BATS old-comparison negative run are not executed here. The pinned head `b849945c88` is not proven (see below).

## Heads

- `b849945c88` (pinned H on `solr-18432-submit`): `./gradlew tidy` changed `solr/api/src/test/org/apache/solr/util/TestSolrVersion.java` (one line wrapped, the `for` loop in `testWithoutPrereleaseOrdering`). The gate requires tidy to change nothing, so H is not valid as proof. Not repaired on the submit branch; no push to it.
- `378248b76b` (F): same test file, same tidy change needed.
- `842b1fb426` on `nick-boss-tech/solr:solr-18432-gradle-only` (new branch, parent F, one commit: the tidy formatting fix, Nick Shanin identity, no trailers). Remote SHA verified. This is the head every result below is for.

## Positive Gradle legs at 842b1fb426 (job `pr5254-r2-gradle-842b1fb426-061432`, logs `/pr5254-r2-gradle-842b1fb426-061432/`)

| Step | Command | rc | Result |
| --- | --- | --- | --- |
| tidy | `./gradlew tidy` | 0 | no tracked changes after run; `git diff --check` clean |
| api | `:solr:api:cleanTest :solr:api:test --tests org.apache.solr.util.TestSolrVersion -Ptests.seed=18432C0DE` | 0 | TestSolrVersion 12 tests, 0 skipped, 0 failures, 0 errors. XML copy `/pr5254-r2-gradle-842b1fb426-061432/xml-api/`, sha256 d590b703deabf0912fe4f62bfa0f085cad6103ef46278bc048790d0d1e584375 |
| core | `:solr:core:cleanTest :solr:core:test --tests org.apache.solr.handler.admin.NodeSystemInfoProviderTest -Ptests.seed=18432C0DE` | 0 | NodeSystemInfoProviderTest 2 tests, 0 skipped, 0 failures, 0 errors. XML copy `/pr5254-r2-gradle-842b1fb426-061432/xml-core/`, sha256 4512a4a36d33175f5a4290b58a55f83b60cdec98e9bed0badb34de8f88ee0068 |
| check | `check -x test` | 0 | BUILD SUCCESSFUL in 16m 54s |

Environment: vm6, Linux aarch64, JDK 21.0.12.1+1, Gradle 9.7.0 from the wrapper, host lock via `pool/bin/sweep-run.sh`, standalone clone `/workspace/tmp/clone-5254` (not a worktree, so Antora did not refuse).

## Mutation legs at 842b1fb426 (job `mutations-842b1fb426-064149`, logs `/mutations-842b1fb426-064149/`)

Each mutation changes one line of `solr/api/src/java/org/apache/solr/client/api/util/SolrVersion.java`, from `prepare-mutant.py` in the gate. Diffs saved as `/mutations-842b1fb426-064149/mut-<leg>.diff`: one added and one removed line each.

| Leg | Mutation | Tests run | Failures | Failure message (first) | Matches the gate |
| --- | --- | --- | --- | --- | --- |
| A range | bypass range normalization | testPrereleaseRangeBoundaries, 1 | 1 | AssertionError: 10.1.0-SNAPSHOT | yes |
| B noop | disable prerelease removal | testWithoutPrereleasePreservesBuildMetadata, testWithoutPrereleaseOrdering, 2 | 2 | ComparisonFailure expected 10.1.0+build.123 but was 10.1.0-rc.1+build.123; AssertionError: 10.1.0 | yes |
| C metadata | discard build metadata | testWithoutPrereleasePreservesBuildMetadata, testWithoutPrereleaseOnRelease, 2 | 2 | ComparisonFailure expected 10.1.2+build.123 but was 10.1.2 (and 10.1.0+build.123 vs 10.1.0) | yes |
| D patch | collapse patch versions | testWithoutPrereleaseOrdering, 1 | 1 | AssertionError: 10.1.1-SNAPSHOT | yes |

Every mutation leg exited rc=1 with the counts the gate requires. No compile failure, skip or setup failure.

## Not done

- Docker image builds (release R, snapshot S, old-comparison M) and the BATS positive and negative runs. Need a host with Docker and BATS.
- The BATS test (`solr/packaging/test/test_snapshot_cluster_version.bats`) is not on `solr-18432-gradle-only`. It is only in H, which is not valid until the tidy fix is applied to it.
- The contributor branch and public PR 5254 were not touched.

## What is needed to finish

1. Put the same tidy formatting fix on `solr-18432-submit` (a commit on top of H, Nick's identity, no trailers, plain push), then gate that new head: tidy, api, core, check, the four mutations, and the BATS runs on a Docker/BATS host.
2. Or keep the Gradle-only branch as a separate proof and run the BATS legs at a head that includes the BATS file.
