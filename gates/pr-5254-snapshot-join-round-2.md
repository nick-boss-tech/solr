# Superseding gate: SOLR-18432 real snapshot-node join, round 2

Status: OPEN. This replaces gates/pr-5254-normalization-tests-round-1.md for the submit branch's new head. Do not start the old-head job or treat its result as proof of this head. Old records remain immutable.

- Required tags: gate, bats, review, records. Intended host: any capable Linux Docker/BATS gate host with JDK 21, Python 3, Git, Bash, host jq, Docker daemon, registry access for the branch's Docker base image, Gradle 9.7.0 and build dependencies. Allow headroom for image builds and two Solr containers at 1 GiB each. One build/gate at a time under the host lock and disk/inode guards.
- Fork branch: nick-boss-tech/solr:solr-18432-submit.
- Exact ordinary positive head H: b849945c88bf894d273455882b25d786a8c1e6d1. Verify remote ref before claiming/running.
- Earlier test head F: 378248b76b7782ddb8037ab692f4211eb666336c.
- Contributor head O: d59169c9cc96dd60c976079d18ef4da56024bb26, public PR https://github.com/apache/solr/pull/5254. Do not change the contributor branch or public PR.
- Implementation claim: claims/pr-5254-snapshot-join-windows-round-2.md.
- Implementation report: reports/pr-5254-snapshot-join-implementation-round-2.md.
- Execution host's receipt: receipts/PR-5254-snapshot-join-round-2.md. Add a completion/failure report. Leave this job and implementation claim unchanged.

Nick requested closing the direct startup coverage gap. H adds one opt-in packaging BATS test, solr/packaging/test/test_snapshot_cluster_version.bats, and registers its three image/version environment settings as task inputs in solr/packaging/build.gradle. It runs two real Solr processes, not fabricated live-node entries. The outgoing production source and LATEST_STRING remain unchanged. Since H still declares the numeric version 11.0.0, the integration leg necessarily uses controlled compiled variants. Report those synthetic trees honestly; do not say that an unmodified H binary advertises SNAPSHOT or that these locally built release-version binaries are an official published release.

## Claim and repairs

Read current WORKFLOW.md/START-HERE.md and this ticket's latest records, check for competing execution claims, verify H, and perform the detached tidy preflight before claiming execution. Record host, JDK/cache, build lock, all worktrees, runner, log/XML/TAP paths and container/image resource plan in an add-only claim.

If tidy changes H, or a test/source fix is required, commit the repair using Nick's identity, plain-push it and add a superseding job pinned to the new head before proof. No proof at an old head covers a repair. Record/investigate executed-test failures before any repair/rerun. A pre-test environment refusal may be relaunched once under WORKFLOW.md. Do not skip a named leg, weaken the warning assertion, or call setup failure negative proof.

## Ordinary head validation

At clean H, require `./gradlew tidy` rc 0 and no changes, then git diff --check. Run:

```bash
./gradlew :solr:api:cleanTest :solr:api:test --tests org.apache.solr.util.TestSolrVersion -Ptests.seed=18432C0DE
./gradlew :solr:core:cleanTest :solr:core:test --tests org.apache.solr.handler.admin.NodeSystemInfoProviderTest -Ptests.seed=18432C0DE
./gradlew check -x test
```

Require twelve API tests and two core tests, all executed with zero failures/errors/skips, fresh XML, and a successful root check with no further exclusions. Preserve each leg's logs/XML before cleanTest overwrites them. Record any setup/environment condition accurately.

Carry all four API mutation legs from the immutable [round-1 job](https://github.com/nick-boss-tech/solr/blob/2d63ac357e21c07ddbdf1d3e8daa2bd42e33f8dc/gates/pr-5254-normalization-tests-round-1.md) forward at H, replacing that job's F with H throughout. The exact preparation code, commands and expected failure sites are in its mutation section. Require respectively 1, 2, 2 and 1 executed tests, all failing only at their named assertions. Stage only each intended production mutation before execution so checkWorkingCopyClean does not mistake the fixture for a test-created modification; record cached diff/tree and unchanged test hashes. Unchanged production in H remains the positive control. Do not reuse old-head results.

## Build the release, snapshot and old-comparison variants

Use three separate detached worktrees at H: R (release, unchanged), S (snapshot fixture), and M (snapshot with old comparison). The BATS file must have the exact H bytes in every worktree. Keep the ordinary H validation checkout separate and clean.

R retains LATEST_STRING = 11.0.0 and the fixed comparison. In S and M, run this preparation from a script saved outside the checkout. Use argument snapshot for S and old-check for M:

```python
from pathlib import Path
import sys

assert sys.argv[1] in ('snapshot', 'old-check')
p = Path('solr/api/src/java/org/apache/solr/client/api/util/SolrVersion.java')
s = p.read_text()
old = 'public static final String LATEST_STRING = "11.0.0";'
assert s.count(old) == 1
p.write_text(s.replace(old, old.replace('11.0.0', '11.0.0-SNAPSHOT'), 1))

if sys.argv[1] == 'old-check':
    p = Path('solr/core/src/java/org/apache/solr/cloud/ZkController.java')
    s = p.read_text()
    old = 'if (ourVersion.withoutPrerelease().lessThan(clusterVersion.withoutPrerelease())) {'
    assert s.count(old) == 1
    p.write_text(s.replace(old, 'if (ourVersion.lessThan(clusterVersion)) {', 1))
```

Stage only the fixture edits and record the cached diff, synthetic tree and file hashes. S must differ from H only in the version string. M must differ from S only in that comparison. Both must retain H's test exactly. These are isolated proof fixtures, not changes to merge/push onto the submit branch.

Build R from its own worktree:

```bash
./gradlew :solr:docker:dockerTag -Dversion.release=11.0.0 -Psolr.docker.imageName=solr-18432-release:round2
```

Build S and M from their respective worktrees, with no version.release override:

```bash
./gradlew :solr:docker:dockerTag -Psolr.docker.imageName=solr-18432-snapshot:round2
./gradlew :solr:docker:dockerTag -Psolr.docker.imageName=solr-18432-snapshot-old-check:round2
```

Record each build's exit code/log, distribution and API/core JAR hashes, synthetic source tree, Docker image ID and base image digest. Verify source diffs against H and against each other. Pass the immutable local image IDs, not just the tags, into the test runs below. Never pull these fixture images from a registry or substitute a tag-only rename for recompiling SolrVersion. Do not push images.

## Positive direct startup and replication regression

From the ordinary clean H checkout, set SOLR_RELEASE_IMAGE to R's recorded image ID, SOLR_SNAPSHOT_IMAGE to S's recorded image ID and SOLR_RELEASE_VERSION=11.0.0. Run:

```bash
./gradlew iTest --tests test_snapshot_cluster_version.bats
```

Require exactly one executed BATS test, zero skips/failures/errors and fresh JUnit/TAP. The suite skips only when both image variables are absent; a skip is not acceptance. Explicitly supplied images with missing Docker/jq or wrong versions must fail, not get waived. Preserve outputs before another iTest run.

Require the retained artifacts to show:

- The first real node's /live_nodes data contains solrVersion 11.0.0 before starting the second node. Its collection and committed document already exist.
- The joining real node's /live_nodes data contains solrVersion 11.0.0-SNAPSHOT, read back from the shared ZooKeeper server, with no fabricated entries.
- CLUSTERSTATUS shows exactly two ACTIVE replicas of shard1 on the two expected node names.
- A query through the snapshot node with distrib=false returns the indexed document (numFound 1), so the check cannot pass by forwarding the query to the release node.
- The snapshot container stops without exit 137, and its copied, flushed solr.log lacks the exact false older-than-cluster warning.

Artifacts are under solr/packaging/build/test-output/snapshot-cluster-version/1 plus the normal BATS JUnit output. They include image IDs, live-node JSON, cluster status, local query response, Solr logs, Docker console logs and inspect output. Teardown removes only uniquely named test containers/network and their anonymous volumes. Confirm no test resources remain after each leg.

## Negative direct startup regression

Run the identical BATS test from H, keeping R's release image ID and SOLR_RELEASE_VERSION identical, but set SOLR_SNAPSHOT_IMAGE to M's recorded old-comparison image ID. Use the same iTest command.

Changing SOLR_SNAPSHOT_IMAGE to the different immutable image ID must execute integrationTests again, not reuse an UP-TO-DATE result. Require fresh output and exactly one executed test and exactly one test failure, specifically the final refute_output assertion because the snapshot Solr log contains:

```text
Our Solr version 11.0.0-SNAPSHOT is older than cluster version 11.0.0
```

All earlier startup, advertised-version, collection/replica, local-data and graceful-stop assertions must pass. Retained JSON and logs must demonstrate those steps, with the same BATS source hash as the positive run. Joining alone is insufficient: the old code also allows this same-major/minor node to join and merely warns. Compilation, Docker build failure, missing images/tools, wrong node versions, timeout, missing replica/data, absent log, exit 137 or a skipped/passing test do not constitute negative proof. Preserve the actual failure output and log line.

## Completion

GREEN requires ordinary H validation, all four H-based API mutations, the positive direct BATS run and the discriminating old-comparison BATS failure. End by checking the ordinary H checkout is clean and its local/remote head is still H. A changed head needs a new job.

The execution host's receipt records all heads/trees, synthetic diffs, source/test/JAR/image hashes, commands, seeds, counts and exact failures, fresh XML/TAP/log paths/hashes, fixture build conditions, cleanup and any repairs. Distinguish ordinary exact-head checks from the controlled S/M integration variants. The direct test is implemented but remains unproven until this paired run succeeds. No public comment, PR edit, review submission, JIRA write or merge is part of this gate.
