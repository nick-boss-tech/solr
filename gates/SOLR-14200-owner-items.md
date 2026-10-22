# Gate job: SOLR-14200 owner-items round 1 (item 3, empty shard list)

Status: DONE

- Branch `solr-14200-submit`. Commit `c373a718ad6` on `10991af9bd`.
- Host: vm5. Logs: `/workspace/gates/logs/vm5/solr-14200-owner-items-2/`.

## Steps

1. `:solr:solrj-streaming:compileTestJava`: rc=0.
2. `:solr:solrj-streaming:test --tests StreamingTest`: rc=0. XML: 39 tests, 2 skipped, 0 failures. New tests `testSignificantTermsStreamMissingManualShardMappingFailsClearly` and `testDeepRandomStreamMissingManualShardMappingFailsClearly` run and pass.

An earlier run at `10991af9bd` failed only because the new tests expected `IllegalArgumentException`; the stream wraps it in `IOException` on read. Fixed in `c373a718ad6`.

Mutation (rung 2): the two stream files restored from `10991af9bd`, then the same test class. Result: `testSignificantTermsStreamMissingManualShardMappingFailsClearly` and `testDeepRandomStreamMissingManualShardMappingFailsClearly` FAILED, rc=1. Both files restored and checked clean before any further change.
