🤖 *AI text below* 🤖 *(reviewed by Nick Shanin)*

https://issues.apache.org/jira/browse/SOLR-18563

## What happens today

**A facet on a newly added field, before a commit, fails on every replica after an additive Schema API change.**

PR #5194 skips the core reload for add-field, add-dynamic-field and add-copy-field. Without the reload, no searcher opens with the new schema. A facet on the new field then fails with `undefined field` on every replica until a commit. Before this change, the reload gave the replica that received the request a new searcher, so the facet passed there. The other replicas failed.

## What this change does

**After an additive change, every replica opens a searcher with the new schema.**

The node that receives the request opens a searcher before the request returns. A replica that loads the schema from ZooKeeper opens one when it takes the new schema. If the core is closing, the replica skips the open. No reload happens, so the merge-blocking fix in #5194 is kept.

## Proof

**The test fails on the head of #5194 and passes with this change.**

- `TestManagedSchemaAPI.testFacetOnNewFieldBeforeCommitOnEveryReplica` fails on the head with `undefined field` and passes with this change.
- With this change, `TestManagedSchemaAPI` passes 7 of 7 and `TestBulkSchemaAPI` passes 19 of 19. `TestSchemaChangeDuringMerge` passes.

Cost, on a two-replica collection: add-field takes a mean of 10.4 ms with the searcher open on a 20,000-document index, against 8.6 ms without it and 97 ms with the reload on main.

## A choice to check

Option 1 reopens the searcher on every replica. The alternative is to keep #5194 as written and document that facets and other queries on the new field can fail until a commit. That option is a visible change on every replica. This change takes option 1.

## Limits

- Upstream CI has not run on this change.
- The cost was measured on one shard, two replicas, and a 20,000-document index. Larger indexes and concurrent indexing are not measured.
- The closed-core guard is not covered by a test. It handles a race that the tests do not reproduce.

Changelog: [changelog/unreleased/SOLR-18563-additive-schema-api-no-reload.yml](https://github.com/nick-boss-tech/solr/blob/78cef4597ddfcceb4f411b7ccc6a0add65a52cbb/changelog/unreleased/SOLR-18563-additive-schema-api-no-reload.yml)
