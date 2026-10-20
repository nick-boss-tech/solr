# Receipt: PR review apache/solr #1875 (vm4)

- PR: https://github.com/apache/solr/pull/1875, open, not draft, base `8f7f617e6a` (main), head `ddcfcdff67` on `epugh/solr` branch `SOLR-16957`. 17 commits, 1 file changed, +197/-0 in the final diff.
- Fetched the head from the author's fork (`git fetch https://github.com/epugh/solr.git SOLR-16957`). The fork's branch is based on an older main, so the review diff was scoped to the PR's files against `8f7f617e6a`.
- Changed file: `solr/packaging/test/test_scenario_loosely_coupled_clusters.bats`, a rename of the earlier `test_user_managed_replication.bats` in the last commit.
- Base checks at `c8ab71430f` (git grep): `bats_helper.bash` (`common_clean_setup`, `save_home_on_failure`), `solr/packaging/build.gradle` (`SOLR3_PORT`, `SOLR2_PORT` used by other BATS tests), CLI options in `solr/core/src/java/org/apache/solr/cli/` (`solr-url`, `conf-dir`, `cloud`), `ReplicationHandler.java` (`fetchindex`, `enablereplication`, `details`).
- Gate: none run. BATS needs the packaging build and BATS libraries.
- Outcome: READY-WITH-NOTES. Draft: `drafts/pr-review/1875.md`.
