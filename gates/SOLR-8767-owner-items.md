# Gate job: SOLR-8767 owner-items round 1 (item 4, fl=* and multivalued tests)

Status: DONE

- Branch `solr-8767-submit`. Test commit `46e758551c3` on `3b5f2d2357` (no production change).
- Host: vm5. Logs: `/workspace/gates/logs/vm5/solr-8767-owner-items/`.

## Steps

1. Tidy: rc=0 (formats the new test lines).
2. `:solr:core:compileTestJava` and `:solr:core:test --tests TestRealTimeGet`: rc=0. XML: 9 tests, 0 failures. New: `testCopyFieldTargetReturnedWithWildcardFl`, `testMultiValuedStoredFieldReturnedFromRealTimeGet`.

## Mutation (rung 2)

`RealTimeGetComponent.java` restored from base `14c7aac0d15`, then `TestRealTimeGet`: rc=1. Failed: `testCopyFieldTargetReturnedWithWildcardFl` (new), `testCopyFieldTargetSetDirectly`, `testCopyFieldTargetSetDirectlyAfterCommit`, `testAtomicUpdateOfDocumentWithCopyFieldTarget`. The file was restored and checked clean before the commit.

`testMultiValuedStoredFieldReturnedFromRealTimeGet` passes on the base. It is a pin on multivalued stored output, not a test of the copyField change, and it is labelled that way in the receipt. The schema has no stored, multivalued copyField target (`where_s_multi_not_uninvert` is `stored="false"`), so a discriminating multivalued copy-target test would need a schema change, which is outside this item.

## Result

GATE GREEN at `46e758551c3`. Owner item for the output change stays decided as keep (rung 3).
