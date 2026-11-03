# Gate: PR 5265 v1 SPLITSHARD adapter (round 1)

Status: DONE (GREEN at 7a020053d19435e29036d96d2741ce890cef75c6; receipt receipts/PR-5265-v1-adapter-round-1.md; one disclosed pre-existing intermittent ShardSplitTest failure, reproduced at base)
Ticket: SOLR-16398
PR: apache/solr#5265
Branch: solr-16398-split-shard
Candidate: 17d2ebb3f08a9e6b8ba75c51ed875f3b06c3aca9
Base: 16b0d50218bf82dc0d28158a63602c2d3da2ef3a (previous PR head)
Claimed-by: phone-agent (at Nick's direction, 2026-11-02)

## Scope

Eric Pugh's review on PR #5265 (comment 6594600652, 2026-11-03T03:21:22Z) asked for
CollectionsHandler.SPLITSHARD_OP to delegate to the v2 SplitShard resource through
invokeWithV1Params, the way CREATESHARD_OP and DELETESHARD_OP delegate, so the
cross-parameter validation lives in one place and v1 traffic exercises the v2 code.
The candidate implements that delegation (option B as directed by Nick), preserving
two v1 shapes the resource's path parameters cannot express: split by split.key with
no shard named, and the createNodeSet parameter (now a SplitShardRequestBody field).

## Change under gate (one commit on the previous PR head)

- solr/api: SplitShardRequestBody gains a createNodeSet field.
- SplitShard resource: createRequestBodyFromV1Params (async deliberately unset) and
  invokeWithV1Params entry points; message building shared between the v2 method and
  the shard-less split-by-key submission; createNodeSet inserted into the message.
- CollectionsHandler: SPLITSHARD_OP lambda replaced by the delegation; the duplicated
  validation and the Map-returning copy are gone; the entry keeps its 5x timeout.
- SplitShardMessageMappingTest: 5 new tests (v1 param mapping incl. async unset,
  shard-path delegation message, split-by-key message with no shard entry, both v1
  shape rejections) and createNodeSet coverage in the common-properties test.

## Legs

1. tidy at the candidate (no diff).
2. :solr:core compileJava and compileTestJava with -Pvalidation.errorprone=true.
3. Focused suites at seed 16398C0DE: SplitShardAPITest, SplitShardMessageMappingTest,
   V2CollectionAPIMappingTest, TestCollectionAPIs, TestApiFramework.
4. Behavior suites: org.apache.solr.cloud.api.collections.ShardSplitTest and
   org.apache.solr.cloud.SplitShardTest (v1 SolrJ split paths incl. split.key).
5. :solr:core:check -x test at the candidate.

Out of scope: the Admin UI Selenium leg of the earlier CI-repair gate (receipt
receipts/PR-5265-ci-failure-repair-round-1.md) stays open for a host with a stock
Chrome/Chromium; this change touches no UI code.

## Receipt

Receipt target: receipts/PR-5265-v1-adapter-round-1.md
