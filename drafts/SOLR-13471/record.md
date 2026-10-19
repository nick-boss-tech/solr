# Skip record: SOLR-13471

- Ticket: SOLR-13471, exceptions during response writing can cause javabin to write a corrupt or misleading response that may cause exceptions or non-sense data when unmarshaled by javabin.
- Verdict: keep-open.
- Draft branch: solr-13471-draft-open.
- Pinned head: fork origin/main 498a09aea97aac86c2cc7ac114113b946d249942. Code read at the pin in this pass.
- Source: the queue audit (round audit-1, 2026-10-04, confirmed-skip) and the earlier record (vm2, 2026-10-19, checked against upstream main c8ab71430f) are replaced by this pass.

## Ticket reading

JIRA read 2026-10-19 (this pass): Open, resolution None, created 2019-05-14, updated 2019-06-12, no fix versions. Links: blocker SOLR-13539 (Resolved), blocker SOLR-13331 (Closed), problem SOLR-13470 (Open). The description summarizes a hypothesis: an exception while the response is being written leaves a partial javabin stream that a Java client then reads as wrong data. The 2019-05-15 comment (noble.paul) reads a stack trace with a Boolean key in a SimpleOrderedMap; the later comments disagree on the cause and on whether the wire format needs a restart or error marker.

## Code reading

Read at the pin:

- `solr/core/src/java/org/apache/solr/response/JavaBinResponseWriter.java:52-62` (`write`): marshals `response.getValues()` straight to the `OutputStream` in a try-with-resources around `JavaBinCodec`. There is no intermediate buffer and no marker written before or after a failure.
- `solr/solrj/src/java/org/apache/solr/common/util/JavaBinCodec.java:1483-1491` (`close`): skips a second flush after a marshal failure, with a comment that a flush after a broken stream would throw the same exception again. `END_OBJ` at `:283` is a map-end sentinel used by the reader (`:346`, `:830-835`); the codec has no restart or error marker.
- `solr/core/src/java/org/apache/solr/servlet/HttpSolrCall.java:787-815` (`writeResponse`): the writer is called with `response.getOutputStream()` at about :814-815. If the stream has already been written, the output on the wire is partial.
- `HttpSolrCall.java:513` calls `writeResponse`; `:518` catches any `Throwable` from it and `:520` calls `sendError(ex)`, which tries to send an error response after the write has started.

What the reading does not show: whether bytes already written commit the response, so that `sendError` cannot replace the partial body; and what a Java client receives in that case. Those are run questions.

## History

- No commit at the pin names SOLR-13471 (evidence tool: none).
- The `close` comment about the second flush shows the codec's own failure handling was changed at some point; the commit was not traced in this pass.

## Reproduction attempts

Not run. No build or test run in this pass.

## Gap analysis

- The code path the ticket names is present: a write failure after output has started goes to `sendError` at `HttpSolrCall.java:520`, on a stream that may already carry bytes from `JavaBinResponseWriter.write`.
- Whether that yields a corrupt response for a Java client is not shown. It needs a run: a response writer that throws midway (for example, a field value whose marshal fails after some documents are written), with a SolrJ client reading the result, and the bytes on the wire.
- The design question in the ticket (a restart tag or a fail-fast marshal) is not settled by the code reading; the 2019 comments disagree on it.

## Verdict justification

keep-open: the code path is present at the pin, the wire result is not shown, and the ticket's design question is open. A close verdict needs a run showing the client gets a correct result or an error (close-cannot-reproduce), or a fix commit (close-fixed). A pr-needed verdict needs the failing run.

Missing for a closing or PR verdict: the mid-write failure run described above, with its command, the bytes the client reads, and the log path.

## Limits

This record does not establish a reproduction. It does not check whether the Jetty response is committed before `sendError` runs. It does not post anything to JIRA or GitHub.
