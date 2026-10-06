# SOLR-11479 - hypothetical reproduction (nothing was compiled or run)

JIRA: ADDREPLICA with `property.coreNodeName=foo` fails core creation ("coreNodeName foo does not exist in shard").
Skip audit (Tier 3) note said "superseded: AddReplica has an explicit coreNodeName parameter". That is true, but
the property variant is still passed to CREATE as a core property while the cluster state replica gets a generated
name, so the two disagree. `AddReplicaCmd.assignReplicaDetails` already treats `property.name` as a fallback for
`name`; `property.coreNodeName` had no such fallback.

## Change
`assignReplicaDetails`: blank `coreNodeName` falls back to `property.coreNodeName`. The replica is then created in
cluster state under that name and the CREATE call carries the same value.

## Test (guessed)
`CollectionsAPISolrJTest.testAddReplicaWithCoreNodeNameProperty` (Hoss's test shape from the ticket): add a replica
with `withProperty("coreNodeName", ...)`, expect a replica of that name in shard1.

## Guesses to verify first
- `AddReplica.withProperty` exists with this signature; line length of the `getReplica` line (run spotlessApply).
- The `totalReplicas > 1` guard only looks at `coreNodeName`; `property.coreNodeName` with several replicas is not guarded.
- Alternative design: reject `property.coreNodeName` with a 400 instead of aliasing.

## Fail-before
Revert the new fallback: CREATE fails with the ticket's chicken/egg error.
