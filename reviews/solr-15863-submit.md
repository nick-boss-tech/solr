# solr-15863-submit

- Branch: origin/solr-15863-submit
- Head: 4bda993525ed
- Base: upstream/main at 9d7cc2884e8a (merge-base 97d973814336, 19 commits behind)
- Scope: 3 commits. `BackupCmd.java` (+20/-1, min-version aggregation in `aggregateResults`), `BackupProperties.java` (+5, `setIndexVersion`), `IncrementalShardBackup.java` (+12, reads the commit's segments and sets `indexVersion`), `BackupCoreAPITest.java` (+23, new shard-level test), `AbstractIncrementalBackupTest.java` (+6, collection-level assertion), changelog `SOLR-15863-backup-index-version.yml` (+8)
- Verdict: Needs work (small: a discriminating test for the new collection-level behaviour)
- Reviewer: claude-haiku-5-5 (Claude Code), 2026-10-08

Nothing here was compiled, formatted, or run. No Gradle, no `drain`, no test runs. Every claim below rests on reading the diff and the code at the listed head.

## Delta check against earlier disposition

- Handoff: test-categories round at this head, labelled a pin on the Linux side. Bulk review (older snapshot): not re-read here; the handoff says it predates this head.
- The label "pin" is accurate for the collection-level test (finding 1). It does not show the fix.
- Head unchanged: `origin/solr-15863-submit` is at `4bda993525ed`, as listed.

## Ordering check (verified)

The aggregation runs inside `incrementalCopyIndexFiles` (`BackupCmd.java:119`), which calls `aggregateResults` (line 296). `backupMgr.writeBackupProperties(backupProperties)` runs later, at line 170. So the minimum version set by `aggregateResults` is written to `backup_N.properties`. This is the right order.

The default is unchanged: `BackupProperties.create` still sets `Version.LATEST` (upstream `BackupProperties.java:83`). The branch changes the value only when at least one shard response reports `indexVersion`.

## Findings (ranked)

1. **MEDIUM, verified. The collection-level assertion does not discriminate.** `AbstractIncrementalBackupTest` now asserts `persistedProps.getIndexVersion() == Version.LATEST`. On `upstream/main` the value is also `Version.LATEST`, because `BackupProperties.create` defaults to it and nothing overrides it. The test index is written by the running Lucene, so the minimum is also `LATEST`. The assertion passes with or without the fix. It is a pin of current behaviour, as the handoff says, but it gives no evidence that the aggregation works.

2. **MEDIUM, verified. The minimum-version selection has no test.** The logic at `BackupCmd.java` (around lines 345-359) has three paths that no test reaches: mixed shard versions (min must win), an unparseable value (warn and ignore), and an absent field (keep the running version). The only discriminating new test is `BackupCoreAPITest.testIncrementalBackupReportsSegmentLuceneVersion`, which checks the shard response field. It does not check the aggregation.

3. **MEDIUM, verified. Fail-before is likely INCONCLUSIVE for the shard test (gate risk).** `BackupCoreAPITest` reads `response.indexVersion`, which is a new field in `IncrementalShardBackup.IncrementalShardSnapshotResponse` (`IncrementalShardBackup.java`, `+@JsonProperty("indexVersion")`). On the merge-base the field does not exist. The fail-before overlay (per `AGENTS.md`) replaces only `src/test` on the old base, so the test would not compile there, which the queue reports as INCONCLUSIVE, not as a real failure. Hypothesis, not run. Finding 1 is also non-discriminating, so neither new test would give a PASS fail-before on its own. A test that compiles on the base and fails on it is needed for a PASS.

4. **LOW, verified. The changelog is broader than the code.** The title says "backup properties now record the oldest Lucene version of the backed up segments instead of the running Lucene version." The change lives only in the copy-files (incremental) path. The other strategies, if any, keep the running version. The changelog should say "incremental backups".

5. **LOW, verified. Null guard present.** `getMinSegmentLuceneVersion()` can return null for an empty commit, and `IncrementalShardBackup` guards it (`if (minVersion != null)`). `aggregateResults` guards null shard values and catches `ParseException`. The read pattern (`SegmentInfos.readCommit(dir, indexCommit.getSegmentsFileName())`) matches `ReplicationAPIBase.java:165` and `IndexFetcher.java:877`.

6. **LOW, hypothesis. `Version.parse` is not used elsewhere in `solr/core`.** A grep of `upstream/main` finds no existing caller. The `ParseException` catch matches Lucene's documented signature, but this was not checked against the Lucene jar in this checkout.

7. **LOW, not checked. Public response shape.** `indexVersion` is a new JSON property on the shard backup response. Whether the generated OpenAPI spec or a schema test needs an update is not checked.

8. **LOW, verified. Pre-existing shape, not new.** `aggregateResults` dereferences `backupProps` before the `if (backupProps != null)` check at line 362. The check is now redundant but was already there. Leave it alone in this ticket.

## Owner calls (not decided here)

- Ticket scope. The JIRA (SOLR-15863, "Backups do not store correct information in backup_N.properties") also shows `indexFileCount=0` and `indexSizeMB=0.0` in its example. This branch does not touch file counts or sizes. Owner decides whether those are in scope for this ticket or a separate one.
- Semantics. The branch records the oldest segment version. The JIRA example (8.10.1 segments recorded as 8.11.0) supports that reading. Confirming it is the owner's call, not a review finding.

## Proposed change (not applied; for the owner)

Add a test that the gate can count as fail-before: extract the minimum-version selection into a small static helper that takes shard version strings, then unit-test it with mixed versions, one unparseable value, and an absent value. Keep `AbstractIncrementalBackupTest` as a pin. Narrow the changelog to incremental backups.

## Not checked

- Not compiled, formatted, or run. Test pass or fail on base and on head is a hypothesis.
- Lucene `Version.parse` signature not checked against the jar (finding 6).
- OpenAPI or schema test implications of the new response property (finding 7).
- Whether the shard response is serialized to a NamedList with `indexVersion` present (the `shardResp.get("indexVersion")` read depends on it). Not traced through the core admin handler.
- Nothing pushed or posted to GitHub or JIRA.
