# Review: SOLR-15326, branch solr-15326-submit (unit 11)

Head reviewed: dab6a466316940ea643c22dd9e56c9a63c0cb7fb (ls-remote 2026-10-20T15:17Z). Base: 14c7aac0d1. Commits: 0bbea939de (unique-key tie-breaker), 601b93d62d (test, client cache, changelog), dab6a46631 (skip the tie-breaker when the stream has no params). Diff: CloudSolrStream +69, StreamingTest +22, changelog +7. Receipt: gate green; retire candidate, owner call pending (not parked).

## Pass 1

Review (2026-10-20, vm5):

- Change. `CloudSolrStream` adds `ensureExportSortUsesUniqueKey()` on the open path. For `qt=/export` with a SORT that lacks the collection's unique key, it appends `<uniqueKey> asc` to the sort and appends the same comparator to the merge comparator, so the client merge matches the export handler's stable sort.
- Idempotence, verified. A second open finds the unique key already in SORT (`sortContainsField`) and does not append again; the comparator is appended only on the first open.
- Finding, minor (cost): each /export stream construction with a sort and no unique key makes an extra `SchemaRequest.UniqueKey` round trip to the cluster. Not measured. Recorded for the owner; not repaired (a design choice about caching the key).
- Finding, minor (failure path): a schema lookup error now surfaces as `IOException("Unable to resolve unique key for export sort")` at open. It was not a failure before. The message is clear. Recorded.
- Finding, coverage (not tested): the change does not check that the unique key is sortable (docValues). An /export sort on a non-docValues unique key would fail at the export handler. The branch's test uses the standard unique key. Recorded as a note, not a blocker.
- Changelog: `type: fixed`, author and link present (read at the head).

Gate (vm5, on dab6a46631):
- `:solr:solrj-streaming:compileJava :solr:solrj-streaming:test --tests org.apache.solr.client.solrj.io.stream.StreamingTest` (offline): rc=0. `StreamingTest` tests=35, skipped=1, failures=0, errors=0 (`/workspace/gates/logs/vm5/g15326-test.log`). Matches the receipt's count. The `ServiceConfigurationError` lines in the log are JVM security-policy startup noise, not test failures.

Outcome: green with minor notes; no blocker in pass 1.

Regate: gates/SOLR-15326-review-pass-1.md (DONE).

## Pass 2

Review (2026-10-20T15:30Z, vm5, second pass on head dab6a466316940ea643c22dd9e56c9a63c0cb7fb; ls-remote unchanged):

- Test assertion. `testCloudStreamExportSortAddsUniqueKeyTieBreaker` (StreamingTest, line 3244) opens an /export stream with `sort=a_i asc` and asserts that the stream's sort expression contains `id asc`. Finding, minor (weak assertion): `contains("id asc")` is a substring test, so a sort such as `uid asc` would also pass. The test does not check the merge comparator (the append in `ensureExportSortUsesUniqueKey`) and does not check the exported tuple order on a tie. The comparator alignment is therefore not covered by a test; it is verified by reading the code (pass 1, idempotence). Not repaired (a test-assertion change is in the repair scope only for up to two failing tests).
- Export handler sort. The appended clause is `id asc` on the test's collection, where `id` is the unique key and sorts on docValues in the test schema. The export handler accepts it (the test opens and reads the stream on the cluster; the open would fail otherwise). No new check.
- Skip count varies by seed. Pass 1 showed skipped=1; this pass shows skipped=2. The two skips are `testParallelExceptionStream` (`@Ignore` on the head, always skipped) and `testDaemonTopicStream` (`Assume.assumeTrue(!useAlias)`), and `useAlias` is set from `random().nextBoolean()` in the class setup (line 118). This pass ran the alias-enabled branch, so it covers a different branch than pass 1. Both are green; the receipt's count is seed-dependent and should say so.
- Rerun. `:solr:solrj-streaming:test --tests org.apache.solr.client.solrj.io.stream.StreamingTest` (offline): rc=0; tests=35, skipped=2, failures=0, errors=0 (`/workspace/gates/logs/vm5/g15326-test-p2.log`).

Repairs: none.

Regate: gates/SOLR-15326-review-pass-2.md (DONE, green with minor notes).
