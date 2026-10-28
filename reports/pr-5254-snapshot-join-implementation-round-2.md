# SOLR-18432 direct snapshot-node join coverage: implementation, round 2

Status: Implemented, Proof Pending. Supersedes the round-1 unit-only handoff for the submit branch's new head. No Linux execution is claimed.

- Authority: Nick asked for another commit closing the review's direct snapshot-node startup coverage gap. The earlier four API unit methods did not exercise this path.
- New fork head H: b849945c88bf894d273455882b25d786a8c1e6d1, nick-boss-tech/solr:solr-18432-submit.
- Parent F: 378248b76b7782ddb8037ab692f4211eb666336c. This is an ordinary second commit, not a rewrite.
- Public contributor head O: d59169c9cc96dd60c976079d18ef4da56024bb26, PR 5254. This fork push does not move the public head.
- Implementation claim: claims/pr-5254-snapshot-join-windows-round-2.md, posted/verified at d76a41be227c8fc471997836f0eb8b85ac47e329.
- Intake board snapshot: 8e58ce59033c463ec349dc5435eb23c2409b4c4e, refreshed from 7f93a5fa3a9d217ba7d025a2bb3cf2c32ab20828. No execution claim or receipt existed for the old OPEN gate.

## Outbound changes

- solr/packaging/test/test_snapshot_cluster_version.bats: one opt-in BATS integration method running two actual Solr Docker containers against shared embedded ZooKeeper. It requires explicitly supplied local release/snapshot image IDs and a numeric release version. Missing image variables skip ordinary suites; explicitly supplied but invalid fixtures/tools/versions fail.
- solr/packaging/build.gradle: registers SOLR_RELEASE_IMAGE, SOLR_SNAPSHOT_IMAGE and SOLR_RELEASE_VERSION as BATS task inputs. Switching images must rerun the task instead of accepting UP-TO-DATE output from another fixture.

No production source, version constant, release process or contributor changelog is changed. The original four additional API unit tests remain intact. These test-only changes add no changelog fragment.

## Direct behavior and discriminating assertion

The first real Solr node starts with the numeric release version. The test reads its actual /live_nodes JSON through the supported zk cp CLI, creates a one-shard collection and indexes a committed document before starting the snapshot node. After the second node joins, it verifies that node's actual ZooKeeper version is the same numeric release plus -SNAPSHOT.

It adds a replica explicitly on the snapshot node, waits for exactly two ACTIVE replicas on the expected nodes, and queries the snapshot node with distrib=false. That query must return the document locally. The snapshot container is then stopped to flush logging; exit 137 is rejected, and the copied Solr log must not contain the false older-than-cluster warning. Container inspect/console/Solr logs, live-node JSON, cluster state, local query response and image IDs are retained. Cleanup is limited to uniquely named test containers/network and their anonymous volumes.

The warning assertion is essential. Before this PR a same-numeric-release snapshot node still joins; the comparison emits a warning rather than refusing startup. A successful join alone would therefore be a nondiscriminating test.

## Gate and limits

gates/pr-5254-snapshot-join-round-2.md replaces gates/pr-5254-normalization-tests-round-1.md at the new head. It requires the earlier twelve API tests, two core tests, tidy/root check and all four API mutations freshly at H. Its Docker/BATS legs build R (numeric release), S (H with only LATEST_STRING compiled as SNAPSHOT), and M (S with only ZkController's comparison restored to the pre-PR expression).

The positive run must execute the single real-node BATS test without skips and pass every assertion. The paired negative run uses the same release image and identical BATS bytes, but the old-comparison snapshot image. It must complete the advertised-version, startup, replication, local-data and stop steps, then fail specifically because the exact old warning is present. A build failure, missing tool/image, wrong version, setup timeout, absent data/log or skipped test is not negative proof.

The outgoing LATEST_STRING still equals 11.0.0. The snapshot binary is therefore a controlled compiled source variant of H, not an unmodified-H binary. The release-version binary is locally built from the same head, not an official published release artifact. This closes the test's implementation gap without introducing the production injection seam the ticket opposed; actual execution remains pending. The gate must record these synthetic source trees, diffs, JAR and image hashes honestly.

Windows reviewed the two-file diff (152 added lines) and whitespace; git diff --check passed and the committed worktree is clean. No Gradle task, formatter, compiler, Docker build, BATS or test ran here. No claim of passing integration proof is made. The receipt destination is receipts/PR-5254-snapshot-join-round-2.md; only a completed matching Linux receipt can settle proof.

No public comment, PR body/head edit, review submission, JIRA write or merge was made. A posted OPEN gate is available for a capable host to claim; it is not a statement that a host has picked it up.
