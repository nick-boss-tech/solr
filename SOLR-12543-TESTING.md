# SOLR-12543 - hypothetical-reproduction handoff

**Read this first: the changed test on this branch was written without being compiled or run.** The research/implement pipeline has no Gradle access. Treat the fix and test as hypotheses.

- JIRA: https://issues.apache.org/jira/browse/SOLR-12543 - "Export Handler errors come back with HTTP 200" (Varun Thacker, 2018). SOLR-12542 (non-docValues sort gives no response) is folded in as a duplicate: its empty-body symptom is gone, only the HTTP-status part remains.
- Branch: `solr-12543-submit` off `apache/solr` main `14c7aac0d15`
- Commits: fix + test, changelog fragment, this file (drop the doc before a PR)
- Research note: `research/pipeline/research-notes/SOLR-12543.md` in the Solr-issues workspace

## The bug, as understood
`ExportWriter.writeException` writes `{"responseHeader":{"status":400},"response":{"numFound":0,"docs":[{"EXCEPTION":"..."}]}}` into the response body, but the HTTP status line is already 200
(e.g. `curl /export?q=*:*` with no `sort` -> `HTTP/1.1 200 OK` + "No sort criteria was provided."). Normal `/select` errors give 400.
`ExportHandler.handleRequestBody` also swallows search exceptions into `rsp.setException(e)` and always adds the `ExportWriter`, which then prints them in-body.

## What the branch changes (deliberately narrow)
- `ExportHandler.handleRequestBody`: before doing anything, if `sort` is absent throw `SolrException(BAD_REQUEST, "No sort criteria was provided.")`; if `fl` is absent throw `SolrException(BAD_REQUEST, "export field list (fl) must be specified.")`.
  These are exactly the two conditions `ExportWriter._write` used to report in-body (same messages), so no new rejections, only a different channel. Because the exception propagates out of the handler, normal request handling returns HTTP 400.
- `TestExportWriter.testExportRequiredParams`: replaced the "message appears in the response string" assertions with `expectThrows(SolrException)` + `BAD_REQUEST` + message check.

## What was NOT changed / guesses (verify these first)
1. **Other in-body errors still return HTTP 200**: scoring-sort (`score`), missing `rq={!xport}`, "must have DocValues" (SOLR-12542 case), stream-expression parse errors. They are detected after the writer starts; fixing them needs more
   validation moved into the handler or a buffered first write. Left alone to keep the in-body EXCEPTION format that `/stream` + `ExportTool` readers consume.
2. **Assumed `h.query` rethrows `rsp.getException()`** (TestHarness behavior). If the harness instead returns the error body, the `expectThrows` assertions must be rewritten.
3. `req.getParams().get("sort")` is assumed equivalent to "`sortSpec == null`" in `ExportWriter` (sort could come from handler defaults, which `getParams()` includes). If sort can also come from another place (e.g. `rq`/xport), the early check could reject valid requests - check `testExportRequiredParams` third case and the rest of `TestExportWriter`.
4. A real HTTP-level assertion (status 400 via SolrJ against a cluster) was not added; `TestExportWriter` is a harness-level test.
5. Existing clients that parse the old 200 + EXCEPTION body for these two cases will now get an error response instead (behavior change; worth flagging in the PR).

## How to verify (Gradle required; not run here)
```
.\gradlew :solr:core:spotlessApply
.\gradlew :solr:core:test --tests "org.apache.solr.handler.export.TestExportWriter"
```
Fail-before: revert only `ExportHandler.java`; `testExportRequiredParams` should fail (no exception thrown).

## Not done
No JIRA comment, no PR. Changelog author is `Nick Shanin` per the ICLA note.
