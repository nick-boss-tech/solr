# Pool assignment: SOLR-5735, the pull-replica timeout (ChaosMonkeyNothingIsSafeWithPullReplicasTest)

Status: OPEN

- Capability tags: gate, premise-run.
- Category: Solr core, SolrCloud tests.
- Intended host: any pool Linux VM host. Builds go through `pool/bin/sweep-run.sh` under the host's build lock.
- Posted by: vm2, 2026-10-23, at the direction of Nick. Decision ladder (`material/decision-ladder.md`) applied at rungs 1 and 2; the open question below is handed off, not settled.

## Update, 2026-10-23 (vm2)

Item 1 is answered without a pool run: the timeout reproduces only on the network file system. The pull replica failed to copy the index with "Too many links" (IndexFetcher line 1224, hard link). On local disk, the same seed passes. Item 2 still applies: rerun the test with the seed on local disk, not the network file system.

## Items

1. **Find why the pull-replica test times out.** On `upstream/main` at `b73f03976fa17b857bc0a81bc8af2c6b1c31e0bb`, run `ChaosMonkeyNothingIsSafeWithPullReplicasTest` with `-Ptests.nightly=true -Dtests.seed=E4EFD2B86C0B81D4`. It fails with "Cluster state still in flux after 30 retry intervals" from `waitForThingsToLevelOut` (`solr/test-framework/src/java/org/apache/solr/cloud/AbstractFullDistribZkTestBase.java`, around line 2107), called from `ChaosMonkeyNothingIsSafeWithPullReplicasTest.java` line 264. Start from the seeded log of that run. At the timeout, record the cluster state (`collections/collection1/state.json`), which replicas are not active, and the log line where each one last changed state. Output: `reports/solr-5735-pull-replica-timeout.md`.
2. **Check whether the seed is deterministic (ladder rung 2).** Rerun the same test with the same seed three more times and record pass or fail for each run in the report. Do not change any code for this item.

## Decisions

| Decision | Answer or default | Condition and action | Owner |
| --- | --- | --- | --- |
| Fix the test or the product | decided: none in this assignment | findings only; no code change in this item | Nick |
| Comment on the Jira ticket | decided: no | the held comment is reviewed separately; do not post | Nick |
| Branch for a finding | default: no branch | if a finding needs a branch, name it `solr-5735-<topic>` and push only with Nick's word | Nick |

No row may leave the choice to the claimant.

## Deliverable

`reports/solr-5735-pull-replica-timeout.md`: the cluster state at the timeout, the replicas that are not active with their last state change (log line numbers), and the pass or fail count from item 2. Mark the claim DONE in the same push.

## Limits

- No Jira or GitHub posts or comments. No product code change. No force push.
- Commit as Nick Shanin with no trailers. No em dashes in anything authored.
