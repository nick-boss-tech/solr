# solr-10131-submit

- Branch: origin/solr-10131-submit
- Head: b93cf24a9d99 (matches the listed head)
- Base: upstream/main at 9d7cc2884e8a (merge-base cabedd1d968, 16 commits behind)
- Scope: 3 commits. `solr/core/src/java/org/apache/solr/schema/UUIDField.java` (toInternal(String) plus new `looksLikeUuid`), `solr/core/src/test/org/apache/solr/schema/UUIDFieldTest.java` (new `testNonHexCharactersAreRejected`), `changelog/unreleased/SOLR-10131-uuidfield-validates-hex-digits.yml`, and `SOLR-10131-TESTING.md` (author's run-shape note, left in place).
- Verdict: Needs work
- Reviewer: review-agent A2 (claude-haiku-5-5, Claude Code), 2026-10-08

Nothing here was compiled, run, or tested. No Gradle. Every claim below was checked by reading code.

## Findings (ranked)

MEDIUM (verified): The ticket's reported defect is already fixed on base, so the branch changes a different behavior. The 2017 report is a 500 for input `1249948`. On base, `FieldType.createField` (`FieldType.java:305-307`) rethrows a `SolrException` unchanged ("BAD_REQUEST to fall through"), so the BAD_REQUEST from `toInternal` reaches the client as 400. The branch adds hex validation for a 36-character non-hex input, which the ticket does not mention. The research audit already marked this ticket obsolete (`research/pipeline/skips.md:544`). Owner call: should this ship under SOLR-10131, be split into its own ticket, or be closed as obsolete? Not decided here.

MEDIUM (verified): The new check changes read and delete paths, not only indexing. The check sits in `toInternal(String)` (`UUIDField.java:79`), which is also reached through `FieldType.readableToIndexed` (`FieldType.java:463-472`), `DeleteUpdateCommand.getIndexedId` (`DeleteUpdateCommand.java:57-63`), `IndexSchema.indexableUniqueKey` (`IndexSchema.java:391-392`, used by `RealTimeGetComponent.java:394` and `AddUpdateCommand.java:186`), and `getSpecializedRangeQuery` (`FieldType.java:1044-1045`). After upgrade, a document already indexed with a 36-character non-hex value stays in the index, but delete-by-id, realtime get, and term or range queries on that value return 400 instead of acting on it. The TESTING doc's risky-guess section covers only the update path. Owner call, with options: (a) keep the check strict on all paths, so legacy values are unreachable by id until reindexed; (b) apply the check only on adds, which needs the add path separated from the read path, since uniqueKey adds also go through `indexableUniqueKey`; (c) drop the hardening.

LOW (verified): `SOLR-10131-TESTING.md` line 31 says "Other UUID entry points (`toInternal(UUID)`, `createFields` through `StrField`) are unchanged." `createFields` is changed: `StrField.createFields` (`StrField.java:46`) calls `FieldType.createField`, which calls `toInternal(String)` (`FieldType.java:305`). Only `toInternal(UUID)` is untouched.

LOW (verified): `SOLR-10131-TESTING.md` line 25-26 says "the first three values are accepted" before the fix. The test lists four invalid values. The full-width value (`UUIDFieldTest.java:75`) is also accepted by the old length-and-dash check, because it has 36 units and dashes at 8/13/18/23.

LOW (verified): `testNonHexCharactersAreRejected` covers only `toInternal(String)`. It does not exercise the index path through `createFields`, or the delete and realtime-get paths where the behavior change lands (see the second MEDIUM finding).

LOW (verified, pre-existing, not introduced here): `testBadRequest` (`UUIDFieldTest.java:85-100`) has no `fail()` after the call, so it passes if no exception is thrown.

HYPOTHESIS: The TESTING doc says a non-hex value "is returned later as a 'UUID' that `java.util.UUID.fromString` rejects (clients that bind the value get an exception)". Server side is verified: `UUIDField.write` returns the stored string (`UUIDField.java:60-61`), and `DocsStreamer.java:240-241` keeps UUIDField out of the JavaBin known types because Solr never produces a UUID object. Whether a client's `UUID.fromString` call fails on such a value is hypothesis. Nothing was run to confirm it.

Verified correct, no action needed:
- `looksLikeUuid` (`UUIDField.java:89-105`) accepts exactly: length 36, `-` at positions 8/13/18/23, and `[0-9A-Fa-f]` everywhere else. `Character.digit` accepts non-ASCII digits, but `c > 'f'` rejects every non-ASCII character, so full-width digits are rejected.
- Upper-case hex is accepted and lower-cased; the test asserts this.
- Each of the four test inputs passes the old length-and-dash check, so the new test fails before the fix and passes after. Checked by reading the old code, not run.
- No existing test under `solr/**/src/test` indexes a non-hex UUID-shaped literal. The matches in `UUIDUpdateProcessorFallbackTest`, `DefaultValueUpdateProcessorTest`, `PostLogsToolTest`, `TestRawTransformer`, and `JSONWriterTest` are all hex.
- Changelog fragment matches the upstream format (title, `type: fixed`, authors, links), and the JIRA link is present.
- All three commits are authored as the ICLA identity, with no Co-Authored-By trailers.

Owner decisions posed (not decided, no patch made):
1. Does SOLR-10131 need this change at all, given the reported 500 is already a 400 on base? Options: ship under this ticket, split out, or close as obsolete.
2. If the hex hardening ships: strict on all paths (a), add-only (b), or drop (c). See the second MEDIUM finding.

## Not checked
- Nothing was compiled, run, or tested. No Gradle, no spotless, no test runs.
- The JDK behavior of `UUID.fromString` on a non-hex 36-character input was not checked. It is the basis of the hypothesis above.
- `research/jira-context/SOLR-10131.json` was read for the ticket text only. The ticket's own comments list is empty.
- The reference guide (`solr/solr-ref-guide/.../field-types-included-with-solr.adoc`) was not checked for UUIDField validation wording.
- `UUID.fromString` was searched across the whole `solr/` tree, and only test call sites turned up. No client code outside this repo was checked.
- Upstream conflicts were not checked. The branch is 16 commits behind `upstream/main`.
- Patches: none. No clear defect was found by reading. The open items are owner calls.
