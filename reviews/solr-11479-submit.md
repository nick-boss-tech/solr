# solr-11479-submit

- Branch: origin/solr-11479-submit
- Head: e6fdc9ed5d50 (reviewed); patch pushed on top: 7e538e844c4 (tip now 7e538e844c4)
- Base: upstream/main (merge-base cabedd1d968, 16 commits behind)
- Scope: 4 files, +55 lines at reviewed head. `solr/core/.../cloud/api/collections/AddReplicaCmd.java` (alias in `assignReplicaDetails`), `solr/core/src/test/.../cloud/CollectionsAPISolrJTest.java` (one new test), `changelog/unreleased/SOLR-11479-addreplica-property-corenodename.yml`, `SOLR-11479-TESTING.md` (author's hypothetical-reproduction note, left in place).
- Verdict: Nearly (at 7e538e844c4)
- Reviewer: review-agent A1, 2026-10-08

## Findings (ranked)

HIGH (patched in 7e538e844c4): multi-replica collision. The `totalReplicas > 1` guard in `AddReplicaCmd` (around line 141) checks only `CORE_NODE_NAME`. `assignReplicaDetails` now aliases `property.coreNodeName` for every position returned by `buildReplicaPositions`, so ADDREPLICA with `property.coreNodeName=foo` and two replicas gives both the same coreNodeName. Each CREATE gets `coreNodeName=foo`, and the cluster-state replica is written under that name (`replicaProps` put, around line 261). Verified by reading. Before the patch the same request silently ignored the property. The patch extends the existing BAD_REQUEST guard to the property key, matching the plain `coreNodeName` path. The alias design itself is unchanged. verified (guard gap, by reading); patch by reading, not run.

MEDIUM (hypothesis): the premise that `property.coreNodeName` reaches the core as `coreNodeName` and produces the ticket's "does not exist in shard" error is not confirmed here. Verified: `ZkController` (around lines 2258-2265) checks the core's coreNodeName against cluster state, and the CREATE path sends the standard `CoreAdminParams.CORE_NODE_NAME` param after the change (`AddReplicaCmd` lines 329-331). Not traced: how CREATE turns `property.*` params into core properties. The Linux-side premise run against the base is the check.

MEDIUM (direction call, posed, not decided): single-replica `property.coreNodeName` is aliased (current code) versus rejected with 400. The TESTING note lists both. The owner should choose. The patch closes only the multi-replica hole, which is needed under either choice.

LOW: the guard message still says `'coreNodeName' parameter` when the property form triggers it. Left as is to keep the patch small.

LOW: the new test covers only the single-replica alias. Nothing tests the multi-replica rejection added by the patch. Suggested test (not added, unrun): ADDREPLICA with `nrt=2` and `property.coreNodeName`, expect 400.

LOW (checked, OK): TESTING "guesses" resolved by reading. `AddReplica.withProperty` exists (`CollectionAdminRequest.java` ~2440). `getCollectionState`, `waitForActiveCollection(name, shards, replicas)`, `addReplicaToShard`, and `assertNotNull` are used elsewhere in the same test file. Test line lengths are under 100 columns. Changelog type `fixed`, ICLA author, JIRA link present.

## Not checked
- Nothing compiled, formatted (spotless), or run. No Gradle, no tests.
- Core-side CREATE property handling not traced end-to-end (see MEDIUM).
- Overseer SliceAddReplica behavior on a duplicate name not traced; the collision is concluded from the name-keyed put.
- Changelog YAML not checked against the logchange schema.
- The patch's line wrap follows google-java-format by eye only.
