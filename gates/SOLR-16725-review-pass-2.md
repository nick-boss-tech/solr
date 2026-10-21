# Gate job: SOLR-16725 review pass 2 (unit 14, vm3)

Status: CLAIMED (claims/pool-newdev-critblock-review-round-1-solr-16725-submit.md, 2026-10-21T02:25:28Z)

- Branch: `solr-16725-submit`. Head: `550af283134e2f` (unchanged from pass 1). Base: `14c7aac0d1`.
- Intended host: vm3. Capability tags: gate.
- Module: `:solr:core`. Focused class: `org.apache.solr.cloud.api.collections.LocalFSCloudIncrementalBackupTest`. Seed: `16725C0FFEE16725`.
- Why: the pass 2 regate, sign-off on the same head. Adds a proof leg at this head, because the receipt's base failure was recorded at be1838ef8c, before the test changed.
- Runner: `/workspace/tools/gate-solr-16725-p2.sh`. Logs: `gates/logs/vm3/solr-16725-review-pass-2/`.

Links: Ticket or parent: SOLR-16725. Category: 10, Core admin and cores. Siblings: none known. Supersedes: none.

## What to run

- Step 0: changelog YAML parse. Step 1: tidy. Step 2: Error Prone compile with `--rerun`.
- Step 3, proof leg: base `14c7aac0d1` with `ClusterStatus.java` reverted to base and the head's test file overlaid. Expected: `assertReplicaCountTypesMatch` fails on a count type or value mismatch for a restored collection.
- Step 4: focused head run, counted from fresh XML.
- Step 5: `:solr:core:check -x test`.

## Decisions

| Decision | Answer or default | Condition and action | Owner |
| --- | --- | --- | --- |
| Repair scope | default: WORKFLOW.md Gate repairs only | a finding outside that scope is recorded, not fixed | vm3, rung 4 |
