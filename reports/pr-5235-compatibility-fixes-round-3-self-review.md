# PR 5235 round 3: implementation, test and gate self-review

Status: REVIEW COMPLETE, LINUX PROOF PENDING

Reviewed head: 5f279c7aa44a993bd999be1d4dfc6ecab7942bc8. Original PR: 9ff44cdd0b818c6f1296d69651732f794acd76b7. Legacy base: f45b38d5f6c0c9f7f1204f6d518aaad18a4a88b7. Board snapshot: 903d8365a887e0a2f83181176c013789e843f68c; no Linux round-3 claim at that snapshot. Nick explicitly requested this self-review to avoid wasting the large gate.

## Finding: correct one expected failure in the gate instructions

The gate predicts that testGenericLegacyStreamsRetainAllDocuments indexes zero documents on O. The original source instead rejects the update before that assertion:

1. GenericSolrRequest.getContentWriter returns its null field (GenericSolrRequest.java:120-123).
2. Original EmbeddedSolrServer has no generic legacy fallback; the request writer writes no bytes for this request, leaving an empty stream list.
3. SolrRequestParsers.buildRequestFrom calls setContentStreams only for a nonempty list (O, SolrRequestParsers.java:208-210).
4. ContentStreamHandlerBase.handleRequestBody rejects a null stream iterable, absent commit/rollback, with BAD_REQUEST and message "missing content stream" (O, ContentStreamHandlerBase.java:91-94).

Therefore the intended O failure for this method is the missing-content exception from client.request, not a zero-document assertion. F must still index exactly both documents. This is new compatibility coverage, not an additional independently established base-to-PR regression: the old base also lacks generic embedded fallback. Do not claim this fifth case was demonstrated to pass on B.

The other embedded unnamed-stream test remains the confirmed regression: O indexes one document instead of two. The focused total remains 18 methods; predicted O totals remain 12 failures and 6 controls passing. Exception failures may be counted as errors by a particular XML summarizer; preserve failures and errors separately and classify per method. A superseding gate addendum records this instruction correction with the same source/test head, commands and receipt target. It requires no additional execution or restart of valid work.

## Implementation review

- Compared SolrRequest, JavaBinRequestWriter, XMLRequestWriter and CommandOperation directly with legacy B, as well as the implementation delta against O. Default writer null semantics and the two built-in stream fallback bodies match the old behavior. JavaBin's nonempty UpdateRequest guard and XML's UpdateRequest-null branch are retained.
- Traced Jetty and JDK POST paths to RequestWriter.getContentWriterOrStreams. Explicit writer comes first; a null writer reaches virtual getContentStreams on the custom request writer; only then are streams adapted. The restored default does not preempt that hook. The shared GET validation also retains a legacy-body rejection path.
- Restored API methods have the same public/static names, erased parameters, List return type and IOException declaration as B. JSON/javabin selection, singletonCommands handling, null-stream rejection and response error accumulation are unchanged. ApiBag delegates to the same implementation, with no SolrJ dependency on core.
- Embedded ContentStreamUpdateRequest receives the old unconditional direct pass-through, retaining names, sizes, types, order and all stream objects. For other requests, an explicit writer precedes generic legacy fallback. Multipart writers still buffer each named part separately. Normal UpdateRequest inherits a null stream collection and continues to use the selected JavaBin/XML serializer. GET behavior is unchanged.
- Checked ConfigSetAdminRequest.Upload's legacy stream and explicit writer paths against the restored fallback. The legacy super.getContentWriter call returning null is handled by the built-in request writer fallback; explicit upload writers retain priority.
- No newly introduced resource ownership, dependency, production logging or trust-boundary change was found. Direct legacy streams retain the original ownership path; the adapter closes streams it opens; buffered explicit writer handling is retained.

No additional actionable production-code defect was found in this source review.

## Test review

- Core: four methods, numeric IDs compatible with the fixture copy fields; each request is isolated by deleteByQuery and committed before querying. Exact document count and ordered ID assertions remain. The multipart control throws if its legacy hook is touched, and checks both parts are indexed. Generic legacy coverage exercises the newly explicit fallback.
- API: two methods use reflection so the same tests compile on O, where methods are absent. They resolve the exact erased signatures, invoke them, check the List return descriptor, parsed command and empty error response. This is reflective compatibility proof, not an executed precompiled-client linkage test.
- HTTP: twelve methods; all eight body/hook regressions retain their exact assertions. Four controls each run both JavaBin/XML custom stream writers and require zero legacy-hook invocations with an explicit writer. Both clients and form/plain content types are covered. Reusing the immutable marker writer is safe; every client is closed.
- RawBodyServlet reads readAllBytes before any servlet parameter parsing. HEAD does not overwrite the captured POST, state is reset before every request and after every method, and the POST response is valid XML. The earlier DebugServlet body-consumption defect is not reintroduced.
- Expected original regression failures do not abort remaining focused commands; cleanTest and per-step XML archival prevent stale evidence. Author-added Jetty methods are included in the neighboring full-class run. The inherited EmbeddedSolrServerTest suite has a Mockito assumption; the receipt must disclose any skips, as already required by the job.
- Neighboring test classes and module task paths exist at F. Root check arguments are separate and NODE_ENV is unset explicitly. No test scope expansion, weaker assertion or retry was added by this review.

## Result and limits

The offered branch and test contents are unchanged at 5f279c7aa44a993bd999be1d4dfc6ecab7942bc8. git diff --check against O passed again. Remote fork and live PR read-backs still matched the reviewed heads; discussion was empty. No Gradle, Java compilation or test execution ran on Windows. Formatting and runtime results remain for the existing Linux gate. This review is not a green execution result.
