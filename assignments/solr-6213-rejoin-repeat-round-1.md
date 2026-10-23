# Pool assignment: SOLR-6213, repeat the rejoin regression test on another host

Status: DONE

- Capability tags: gate, premise-run.
- Category: Solr core, SolrCloud tests.
- Intended host: any pool Linux host. Builds go through `pool/bin/sweep-run.sh`.
- Posted by: vm2, 2026-10-23, at the direction of Nick.

## Why this is posted

The rejoin fix runs each leader rejoin on its own thread. The regression test passed once on the final head, and an earlier head left a thread running after the core was unregistered. That is fixed, but one pass does not show the test is stable.

## Items

1. Check out `solr-6213-rejoin-fix-test` at `64624f4726f49dc723e319f6c6aa52ba81f52189`. Run `org.apache.solr.cloud.ShardLeaderElectionRejoinTest` five times with random seeds. Record seed and result, and any `ThreadLeakError`.
2. Check out `solr-6213-rejoin-hook` at its head and copy `ShardLeaderElectionRejoinTest.java` from the test branch into it. Run the test twice. Both runs should fail with "stack grew by 12 frames over 4 rejoins".
3. On `solr-6213-rejoin-fix` at `bcea4f23c1944cf0383193c66fccdb9e6e624567`, run `DeleteReplicaTest`, `ZkControllerTest`, `ReplaceNodeTest` and `TestCloudRecovery`. Record counts.

## Decisions

| Decision | Answer or default | Condition and action | Owner |
| --- | --- | --- | --- |
| Fix any failure | decided: no | findings only; no code change | Nick |
| Post to Jira or GitHub | decided: no | do not post | Nick |

No row may leave the choice to the claimant.

## Deliverable

`reports/solr-6213-rejoin-repeat.md` with the results of items 1 to 3. Mark the claim DONE in the same push.

## Limits

- No Jira or GitHub posts or comments. No product code change. No force push.
- Commit as Nick Shanin with no trailers. No em dashes in anything authored.
