# PR #5139 draft: SOLR-7550 title and proof text (review item 1)

- Written against head: fcc8a874abbb8fbd465a835da6903f64de7009b2 (live branch solr-7550-submit, read through the API 2026-10-17).
- Visible characters: 3,491 (body; markdown syntax and link URLs removed). Limit 3,500.
- Proof numbers: receipts/SOLR-7550.md, section "Gate, vm2, 2026-10-15, at head fcc8a874abbb" (fresh JUnit XML, ts 2026-10-15T18:24:57Z).
- Links in the body still point at the earlier head 5f4570b20a. Re-point them to fcc8a874abbb when this text is applied.
- Not applied to the live PR. Drafts only, per the assignment.

=== TITLE ===

SOLR-7550: A versions-request 500 counts as success only when its error names SolrCoreInitializationException

=== BODY ===

🤖 *AI text below* 🤖 *(posted on behalf of Nick Shanin)*

https://issues.apache.org/jira/browse/SOLR-7550

## What happens today

**A replica whose core will not load can block leader election.**

A schema change, made while one node was down, left two nodes with cores that would not load. The two remaining nodes could not finish a leader election, because their versions requests to the broken cores got HTTP 500 ([ticket](https://issues.apache.org/jira/browse/SOLR-7550)). The remaining nodes ended in `recovery_failed`, and the cluster went offline.

A core that fails to initialize is reported as [`SolrCoreInitializationException`](https://github.com/nick-boss-tech/solr/blob/14c7aac0d151402b00259e2fb9bf5eed7049ec5d/solr/core/src/java/org/apache/solr/core/CoreContainer.java#L2222). On the merge base, PeerSync treats that 500 as a failure ([PeerSync.java L428-L431](https://github.com/nick-boss-tech/solr/blob/14c7aac0d151402b00259e2fb9bf5eed7049ec5d/solr/core/src/java/org/apache/solr/update/PeerSync.java#L428-L431)).

## What this change does

**PeerSync counts a versions request that fails with a core initialization failure as success, when `cantReachIsSuccess` is set. Every other 500 still fails.**

PeerSync checks the exception class in the error metadata ([PeerSync.java L424-L435](https://github.com/nick-boss-tech/solr/blob/5f4570b20ad4d830a4affdfd2e45527753093729/solr/core/src/java/org/apache/solr/update/PeerSync.java#L424-L435)).

Leader election passes the flag as true ([SyncStrategy.java L225-L232](https://github.com/nick-boss-tech/solr/blob/14c7aac0d151402b00259e2fb9bf5eed7049ec5d/solr/core/src/java/org/apache/solr/cloud/SyncStrategy.java#L225-L232)), and the `/get` sync request reads the flag from the caller, so the rule applies there. A caller who can set the flag already gets that success, so this adds no new outcome.

## Proof

**The focused tests pass at head `fcc8a874abbb`, and the proof leg fails.**

[`PeerSyncHandleResponseTest`](https://github.com/nick-boss-tech/solr/blob/5f4570b20ad4d830a4affdfd2e45527753093729/solr/core/src/test/org/apache/solr/update/PeerSyncHandleResponseTest.java#L61-L99) has three tests. An initialization failure on a versions request passes. Another 500 on a versions request fails. An initialization failure on an updates request fails.

- Head `fcc8a874abbb`, vm2, 2026-10-15: PeerSyncHandleResponseTest 3 of 3 and PeerSyncTest 1 of 1 pass, counted from fresh JUnit XML.
- Proof leg: with the core-initialization branch deleted from `PeerSync.java`, `testPeerSyncCountsCoreInitFailureFromVersionRequestAsSuccess` fails at `PeerSyncHandleResponseTest.java:70`; the other two pass.
- Module check `:solr:core:check -x test` passes.

## A choice to check

**Three ways to stop a broken core from blocking the election. This PR implements the first.**

1. **Count a 500 only when it names the initialization failure class (this PR).** Cost: a node that does not send the class name still blocks the election. When the name is present, the leader counts that replica as in sync, so it can miss updates that only that replica holds.
2. **Count every 500 for a versions request.** This was the first version of this PR. Cost: a replica that returns 500 for a runtime fault is also skipped, so the leader can miss updates only that replica holds.
3. **Return 503 for the initialization failure.** The existing 503 rule then applies. Cost: a client sees 503 instead of 500 for this error, and 503 suggests a retry.

Was option 1 the right call, or should option 3 be used?

## Limits

- The tests call `handleResponse` directly, so the path from a real broken core is not tested end to end.
- Older Solr versions may not send the class name; those nodes keep today's failure. Not checked here.
- The `/get` sync path is not tested.
- The two `ShardResponse` setters made public exist only for the test.

Changelog: [changelog/unreleased/SOLR-7550-peersync-500.yml](https://github.com/nick-boss-tech/solr/blob/5f4570b20ad4d830a4affdfd2e45527753093729/changelog/unreleased/SOLR-7550-peersync-500.yml)

### AI assistance

AI agents assisted with research, implementation, review, and drafting. Nick Shanin directed the work and takes responsibility for this contribution.
