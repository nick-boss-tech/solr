# solr-8939-submit

- Branch: origin/solr-8939-submit
- Head: a855a2d8965c (matches the listed head)
- Base: upstream/main at 9d7cc2884e8a (merge-base 97d973814336, 19 commits behind)
- Scope: 3 commits, 5 files (+134/-3). `QueryComponent.java` (+15/-1: `idToString` at line 1457, used at line 1445), `StoredFieldsShardRequestFactory.java` (+3/-1: uses `QueryComponent.idToString` at line 79), `QueryComponentIdFormatTest.java` (+36), `StoredFieldsShardRequestFactoryTest.java` (+76), changelog `SOLR-8939-date-unique-key-millis.yml` (+7, type `fixed`)
- Verdict: Close (the format is one the shard parses. The gap is a proof item: no test runs the round trip.)
- Reviewer: claude-haiku-5-5 (Claude Code), 2026-10-08

Nothing here was compiled, formatted, or run. No Gradle, no `drain`, no test runs. Every claim below rests on reading the diff and the code at the listed head.

## Delta check against earlier disposition

- Bulk review `research/branch-reviews/round-28/SOLR-8939-review.md` (verdict Close) was written at snapshot `253045c6ac3`, an ancestor of the head. The delta is the grouping stored-fields path: `StoredFieldsShardRequestFactory` now calls `QueryComponent.idToString` (commit `a855a2d8965`), plus its test. The round-28 review already noted the advance and that this change was in the head.
- Round-28's "no blocking findings" still holds. The shard-side mechanism is checked below.

## Verified code facts

- `idToString` (line 1457) formats a `java.util.Date` as `toInstant().toString()`, which is ISO-8601 UTC with milliseconds when they are non-zero. Other ids keep `id.toString()`, so String unique keys are unchanged.
- The shard reads `ShardParams.IDS` at `QueryComponent.java` near line 1584. For point-field unique keys it builds the query with `idField.getType().getFieldQuery(null, idField, id)`. For `DatePointField`, that resolves through `getExactQuery` to `DateMathParser.parseMath` (`DatePointField.java`, lines 161-163), which accepts the ISO form.
- The base `Date.toString()` form (for example `Wed Jan 01 ...`) is not in that form, so the fix uses the format the consumer parses.

## Findings (ranked)

1. **LOW, verified by reading. The output format matches the consumer.** The shard parses the `ids` string with the field type's point-field query path, which uses `DateMathParser`, and that accepts the ISO-8601 output of `Instant.toString()`. The fix and the consumer agree on the format.

2. **LOW, proof. No round-trip test.** `QueryComponentIdFormatTest` and `StoredFieldsShardRequestFactoryTest` check the formatter and the request strings. Nothing checks that a shard turns the `ids` string back into the same document with its millisecond part. A shard-side or distributed test would show the round trip. Not run.

3. **LOW, verified. New public surface.** `QueryComponent.idToString` is `public static`. The grouping factory is in another package, so the access level is needed. The owner may still want the name and placement reviewed, since it is new public API.

## Owner calls (not decided here)

- None blocking.

## Interactions with other branches

- None. This branch shares no code with the eDisMax or SQL branches.

## Not checked

- Not compiled, formatted, or run.
- Dates outside years 0000-9999, where `Instant.toString()` uses an extended form. Not tested; likely outside practical range. Not traced through `DateMathParser`.
- The mechanism of the truncation in the JIRA ticket was not re-derived. This review checks the format and the consumer, not the ticket's symptom.
- Spotless and Error Prone were not run.
- No GitHub or JIRA writes.
