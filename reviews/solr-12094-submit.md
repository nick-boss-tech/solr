# solr-12094-submit

- Branch: origin/solr-12094-submit
- Head: 8d957a73f4fb (matches the listed head; fork tip unchanged after fetch 2026-10-08)
- Base: upstream/main at 9d7cc2884e8a (merge-base c3cdf7b46e8c, 26 commits behind, 4 commits ahead)
- Scope: 4 commits, 3 files. `solr/solrj/src/java/org/apache/solr/common/util/JsonRecordReader.java` (per-object flag `recordEmittedBelow` set by a wrapping handler; a mapped field that follows an emitted record, outside a started record, throws `RuntimeException`), `TestJsonRecordReader.java` (new `testMappedFieldAfterSplitIsRejected`), changelog `SOLR-12094-json-record-reader-trailing-field.yml` (`type: fixed`). The testing handoff file was added and then removed in the branch (`8d957a73f4f`).
- Verdict: Nearly (unchanged from the bulk verdict). The mechanism is correct and covers every emission path that matters. Two items block "Ready": the error is an unchecked `RuntimeException` that reaches clients as HTTP 500, and the strict behavior partially indexes the request before failing. The second is an owner call.
- Reviewer: claude-haiku-5-5 (Claude Code), round 36 Group B review, 2026-10-08

Nothing here was compiled, formatted, or run. No Gradle, no `drain`, no test runs. Every claim rests on reading the diff and the code at the listed head.

## Delta check against the bulk review

Bulk review `research/branch-reviews/round-28/SOLR-12094-review.md` (Nearly) reviewed this head (`8d957a73f4f`). No delta.

- Bulk F1 (owner decision: confirm the post-split behavior contract): **confirmed as an owner call.** The throw is at `JsonRecordReader.java:415-422` and the test is at `TestJsonRecordReader.java:250-277`, as the bulk said. The optional buffering mode from the JIRA discussion is not implemented. See owner call 1.
- Bulk F2 (owner decision: confirm exception type): **changed from a typing question to a verified status-code defect.** See finding 1.
- Bulk evidence point (changelog describes the error but not its status): **confirmed.** See finding 3.

## Findings (ranked)

1. **MEDIUM, verified (code path). The error reaches clients as HTTP 500, not as a client error.** The reader throws a plain `RuntimeException` (`JsonRecordReader.java:415-422`). Its only caller, `JsonLoader`, calls `streamRecords` inside a method whose only parse-level catch is `catch (ParseException e)` mapped to `BAD_REQUEST` (`solr/core/src/java/org/apache/solr/handler/loader/JsonLoader.java:151-153`). A `RuntimeException` is not caught there. For a non-`SolrException`, `ResponseUtils.getErrorInfo` defaults to code 500 (`solr/core/src/java/org/apache/solr/servlet/ResponseUtils.java:70`) and includes the stack trace in the body for 500s (`:84`). So a client that sends fields in the "wrong" order gets a server error with a stack trace. The client-caused error should be a 400.
   - Proposed fix (not applied): throw `new SolrException(SolrException.ErrorCode.BAD_REQUEST, ...)`. `SolrException` is in solrj (`solr/solrj/src/java/org/apache/solr/common/SolrException.java`), so the reader can use it without a new dependency. Update `testMappedFieldAfterSplitIsRejected` to expect `SolrException` with code 400.

2. **MEDIUM, verified (code path). The strict error fires after earlier records were already applied.** `JsonLoader`'s handler calls `processor.processAdd(cmd)` for each record as the reader emits it (`JsonLoader.java`, the `handle` method of the `streamRecords` call, around lines 272-288). The offending field is only seen later in the stream. So a request that returns this error has already passed its earlier documents to the update chain, and nothing in the catch path rolls them back. The old code silently dropped the field and indexed everything. The new code fails the request on valid JSON, with partial indexing. Malformed JSON mid-stream already behaves this way, so the property is not new in kind. This branch makes it happen for a deliberately added check on valid input. The bulk review noted the JIRA's buffering alternative, which would avoid this. Owner call 1.

3. **LOW, verified. The changelog does not state the status or the partial-indexing effect.** `changelog/unreleased/SOLR-12094-json-record-reader-trailing-field.yml` says the reader "now throws an error" for a trailing mapped field. It should say which status the client sees and that earlier records in the same request may already be indexed. Reword after owner call 1 and finding 1.

4. **LOW, verified (checked, no issue). Child-record path is not affected.** Every object child goes through `walkObject` (`JsonRecordReader.java:336-380`). Child records (`isChildRecord`, set from `hasParentRecord()` at `:175`, for example `/|/a/b`) attach to the parent document through `addChildDoc2ParentDoc` and do not emit early, so they never set `recordEmittedBelow`. The existing `testNestedDocs` (`TestJsonRecordReader.java:281-`) has a mapped `/a/x` after the nested split and is therefore not broken by this change. Read, not run.

5. **LOW, verified (checked, no issue). Emission-path coverage.** Records are emitted from two sites: `handler.handle(values, splitPath)` at `:389` (own object) and the wrapped call at `:373-374` (descendant). Both are covered by the per-frame flag. Top-level array elements (`parse`, `:278-287`) are records themselves, with no enclosing frame, so no flag applies.

## Owner calls (not decided here)

1. **Strict error or buffering.** The JIRA discussion, as summarized in the bulk review, weighs a strict streaming error against an optional buffering mode. The branch implements the strict error only. Whether a mapped field after the split should fail the request (accepting partial indexing, finding 2), or whether the reader should buffer when needed so the field is kept, is the owner's decision. The bulk review's note that existing context favors streaming is recorded here; the review does not decide it.
2. **Exception type and status**, if owner call 1 keeps the strict error: confirm that BAD_REQUEST (400) is the intended status (finding 1). The bulk review's "confirm exception type" question reduces to this.

## Proposed fixes (not applied; the owner decides)

- Finding 1: `SolrException(BAD_REQUEST, ...)` in place of the `RuntimeException`, and update the test's expected type and code.
- Finding 2: no code fix proposed here. It follows from owner call 1. If the strict error stays, document the partial-indexing effect in the changelog (finding 3).
- Finding 3: reword the changelog after the decisions above.

## Not checked

- Nothing compiled, formatted, or run.
- Whether `processAdd` makes earlier documents visible before commit is not traced beyond `JsonLoader`. Finding 2 rests on the loader having no rollback in its error path, which was read, not executed.
- `JsonRecordReader.getAllRecords` was not traced separately from `streamRecords`. The test uses `getAllRecords`, so its behavior matches the streaming path only by reading.
- The JIRA discussion was not re-read; the buffering alternative comes from the bulk review.
- Other callers of `JsonRecordReader` were searched in `solr/core/src/java` and `solr/solrj/src/java` only. `JsonLoader` is the only one found.
