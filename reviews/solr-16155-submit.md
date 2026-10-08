# solr-16155-submit

- Branch: origin/solr-16155-submit
- Head: 0881de1ed68 (matches the listed head; fork tip unchanged after fetch 2026-10-08)
- Base: upstream/main at 9d7cc2884e8a (merge-base 14c7aac0d151, 45 commits behind, 3 commits ahead)
- Scope: 3 commits, 6 files. `DocumentBuilder.java` (field and copy-field error messages drop values; cause kept; values logged at TRACE via `traceFieldError`), `JavabinLoader.java` (add-failure wrapper now `ERROR adding document [doc=<id>]`, cause kept), tests `DocumentBuilderTest.java`, `JavabinLoaderTest.java` (new), `TolerantUpdateProcessorTest.java`, changelog `SOLR-16155-no-field-values-in-update-errors.yml` (`type: changed`).
- Verdict: Not ready (unchanged from the bulk verdict). The 400-path redaction holds at the response level. The 500 path still exposes the cause chain, and the change drops the reason text for value-free errors without saying so.
- Reviewer: claude-haiku-5-5 (Claude Code), round 36 Group B review, 2026-10-08

Nothing here was compiled, formatted, or run. No Gradle, no `drain`, no test runs. Every claim rests on reading the diff and the code at the listed head. GitHub was read only (`gh pr view 1151`, state and files); nothing was written.

## Delta check against the bulk review

Bulk review `research/branch-reviews/round-28/SOLR-16155-review.md` (Not ready) was written at this head. No delta in the tree.

- Bulk F1 (HIGH, retained cause can carry values into traces): **confirmed for the 500 path; changed for the 400 path.** See finding 1 for the split.
- Bulk F2 (HIGH, competing upstream PR #1151): **changed from a code finding to an owner call.** It is a coordination decision, not a defect in this branch. Verified state and overlap; see owner call 1.
- Bulk test point (tests inspect only the top-level message): **confirmed.** `DocumentBuilderTest.testExceptionMessagesDoNotLeakFieldValues` and `JavabinLoaderTest.testIOExceptionDoesNotLeakDocumentContents` assert `getMessage()` only. The JavabinLoader test even builds an `IOException` whose message contains the secret, then leaves it attached as the cause without asserting on it.
- Bulk changelog point (overstates redaction): **confirmed for the 500 path; the top-level message claim is accurate.** See finding 2.

## Findings (ranked)

1. **HIGH on the 500 path, verified. The cause chain can reach the HTTP body and the server log.** `JavabinLoader.java:127-128` wraps the add failure as `SERVER_ERROR` with the original `IOException` as cause. `ResponseUtils.getErrorInfo` (`solr/core/src/java/org/apache/solr/servlet/ResponseUtils.java:84`) includes stack traces and each cause's `msg` (`:100`) only when the code is 500 or below 100, and logs `500 Exception` with the full chain (`:118-119`). So a 500-class add failure whose cause message embeds document text reaches the response body and the server log. The new JavabinLoader test simulates exactly that shape (`IOException("simulated failure for secret customer value")`) and asserts only the wrapper message. Whether real `processAdd` exceptions embed document text is a hypothesis.
   - **Changed, 400 path.** `DocumentBuilder.java:281-288` keeps the cause for both catch blocks. For a 400 (`BAD_REQUEST`, the common parse-failure case), `getErrorInfo` does not emit cause messages or traces (`printStackTrace` is false, `causedBy` is cleared at `:84-112`). So the client body for those errors is redacted at the wrapper message. Other loggers on the 400 path were not checked (hypothesis).
   - TRACE logging is deliberate. The head commit `0881de1` passes the exception itself to `log.trace` (`DocumentBuilder.java:106-108`), so when TRACE is on, the cause message and values both appear. That is an owner disclosure choice (owner call 2).

2. **MEDIUM, verified. The changelog does not disclose that the reason text is gone for every field error.** The old format appended `" msg=" + ex.getMessage()` (`DocumentBuilder.java` before this branch). The new format drops `msg` for all field errors, including value-free schema messages. `DocumentBuilderTest` shows the change: the `vector3` expectation was `... msg=The copy field destination must be a DenseVectorField: vector_f_p` and is now `ERROR: [doc=0] Error adding field 'vector3'`. On the 400 path, `getErrorInfo` does not emit the cause either, so clients lose the reason entirely. The changelog says only that values and contents are no longer included. The compatibility change should be stated, and the redaction boundary should be an explicit decision (owner call 2).

3. **LOW, verified. The copy-field message is also trimmed.** `DocumentBuilder.java` "Multiple values encountered for non multiValued copy field" drops `: originalFieldValue`. Correct for the redaction goal. It is the same reason-loss pattern as finding 2, with no value left in the message.

4. **LOW, verified. `describeDocument` prints `[doc=null]` when the unique key is missing from the document.** `JavabinLoader.java:148-153` returns `" [doc=" + document.getFieldValue(...) + "]"`. The changelog keeps the document id by design. The id is itself a document value, so whether ids count as "contents" is part of owner call 2. Cosmetic: a missing id reads `[doc=null]`.

5. **LOW, verified. The changelog overstates the 500-path result.** The title says error messages "no longer include the field values or the document contents". True for the top-level message. For the 500 path, the response body and log still carry the cause chain (finding 1). Reword after the fix, or narrow the claim to the top-level message.

## Owner calls (not decided here)

1. **Upstream PR #1151 (same ticket, same area).** Verified with a read-only `gh pr view 1151 --repo apache/solr`: state OPEN, last updated 2026-09-30. It changes 10 files, including four this branch also touches (`JavabinLoader.java`, `DocumentBuilder.java`, `DocumentBuilderTest.java`, `TolerantUpdateProcessorTest.java`), plus `DenseVectorField.java`, `FieldType.java`, `RankField.java`, `StrFloatLateInteractionVectorField.java`, and `DenseVectorFieldTest.java`. The bulk review's "broader field types" point holds. Whether to supersede this branch, fold it into #1151, or hold it is the owner's call. This review does not recommend one, and the branch should not ship independently while #1151 is open. The contents of #1151's diff were not read.
2. **Redaction boundary.** (a) Keep the cause chain for operators, but keep it out of 500 responses (see finding 1)? (b) Keep value-free reasons such as "The copy field destination must be a DenseVectorField" (finding 2)? (c) Keep TRACE logging of values (`traceFieldError`), and document it as the only place values appear? These are product decisions for the owner.
3. **Are document ids contents?** The changelog keeps the unique key in add-failure messages (`[doc=<id>]`). The owner decides whether ids are in scope for this ticket.

## Proposed fixes (not applied; the owner decides)

- Finding 1: on the 500 path, do not pass the raw `IOException` as the cause. Log it at DEBUG or TRACE and attach a value-free exception (class name only) to the `SolrException`. Apply the same to the `DocumentBuilder` catch blocks if a 500 `SolrException` can reach them.
- Finding 1 tests: walk the whole cause chain (`getCause()` until null) and assert that no message contains the sentinel. Add one case that goes through `ResponseUtils.getErrorInfo` for a 500 so the response body is covered, not only the Java exception.
- Finding 2: either keep value-free reasons (for example, the exception's simple class name, or a reason string that never carries input), or state the reason loss in the changelog and keep the vector test expectations explicit.
- Finding 5: narrow the changelog to the top-level message, or make it true for the response body after the fix.

## Not checked

- Nothing compiled, formatted, or run. The vector and DocumentBuilder test expectations are read, not executed.
- Upstream PR #1151's diff was not read (only its state and file list). The overlap is verified by file names, not content.
- Whether real `processAdd` `IOException`s carry document text (finding 1) is a hypothesis.
- Other loggers on the 400 path (request logging, update-processor logs) were not checked. Finding 1's 400 result is limited to `ResponseUtils.getErrorInfo`.
- The JIRA comments were not re-read; the bulk review's summary of them stands as context only.
- `TolerantUpdateProcessor` reports `t.getMessage()` only (`TolerantUpdateProcessor.java:148,170`), so tolerant responses are redacted at the top-level message. Checked; no finding.
