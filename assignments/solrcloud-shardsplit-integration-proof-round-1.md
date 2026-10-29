# SolrCloud split-child construction-state integration proof — Round 1

Status: OPEN, unclaimed pool assignment.

Capability tags: `implementation`, `gate`. Requires a Linux Solr build host with a JDK/Gradle toolchain and sufficient inode/scratch-space headroom for one focused SolrCloud test.

## Objective

Determine whether the unconfirmed split-child state transition documented in `reports/phone-agent-2-solrcloud-shardsplit-failure-pilot-round-1.md` can occur through the real ZooKeeper-backed SolrCloud APIs, and whether the resulting parent/child state violates Solr's intended behavior.

This is an integration-proof assignment, not a confirmed bug or a request to implement a production fix.

## Existing evidence

Source snapshot: Apache Solr `74197140a666d4f0396b68484c2bf9df7e79221e`.

The existing synthetic state-machine probe at `reports/phone-agent-2-solrcloud-shardsplit-failure-pilot-round-1-state-probe.java` invokes production `ReplicaMutator.updateState`. It creates an `ACTIVE` parent, one split child in `RECOVERY` whose last `DOWN` replica receives an `ACTIVE` update, and a same-parent sibling still in `CONSTRUCTION`. The probe observes the parent become `INACTIVE` while the sibling remains `CONSTRUCTION`.

That probe uses a synthetic `DocCollection` and mocked `SolrCloudManager`; it does not establish that the full interleaving is reachable through SolrCloud APIs. The pilot report also identifies the relevant split, delete, and shard-lock paths and explains why the existing `TestInjection.splitLatch` is too late to stage the proposed interleaving.

The prior `ShardSplitTest.testSplitAfterFailedSplit` and `offlineSlices` leads were separately investigated and are explicitly out of scope; do not reopen or relabel them.

## Work

1. Read the linked report and probe, then re-check the current upstream source and history around `ReplicaMutator`, `SplitShardCmd`, `DeleteShardCmd`, and their shard-scoped locks. Confirm or revise the stated reachability hypothesis.
2. Establish the expected contract from Solr code, documentation, tests, and relevant history before deciding whether an observed state is incorrect. In particular, account for whether a child that is being deleted should still count as a required split child.
3. Attempt one deterministic SolrCloud integration test using the real cluster APIs. The target scenario is to pause after split children enter `RECOVERY` but before additional child-replica creation completes, delete one child if the API and locking rules permit it, then observe the remaining child's activation and the parent state.
4. Prefer existing test synchronization or public API controls. Add a narrowly scoped test-only synchronization hook only if there is no viable existing control; document its lifecycle and remove/clean it safely. Do not change production behavior or weaken existing assertions for this proof task.
5. Classify the result honestly:
   - Confirmed counterexample only if a deterministic ZooKeeper-backed integration test reaches the state through real APIs, demonstrates a violation of the established contract, and reproduces on the pinned affected source.
   - Non-reproduction if production ordering or locking rules make the state unreachable; record the exact guard/ordering that prevents it.
   - Blocked if the required synchronization cannot be achieved safely; record the specific blocker and do not promote the synthetic probe to a defect claim.

## Deliverables

Add one report under `reports/` with the source SHA, files/lines, contract evidence, exact command and seed, integration-test/JUnit result, relevant Solr logs, and a clear confirmed / non-reproduction / blocked verdict. Preserve the test patch and raw evidence on a purpose-named branch when a source artifact is needed; do not open a PR or file/comment on Jira.

Claim before work with a new file naming this assignment, the host, start time in UTC, and the specific integration-proof slice. First claim wins. Keep the work single-host and one-test-at-a-time.

## Safety and boundaries

Before each Gradle launch, verify the host build lock is free, no conflicting Solr/Gradle/BATS run is active, and root inode headroom is strictly above 15%. Honor any stricter host-specific scratch-space guard. If a guard blocks the run, stop rather than bypass it or delete unrelated data.

No production fix, PR opening, Jira/GitHub comment, or ready/green claim is authorized by this assignment. If the integration proof confirms a bug, report the evidence and fix options for a separate decision.