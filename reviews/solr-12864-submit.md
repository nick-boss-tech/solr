# solr-12864-submit

- Branch: origin/solr-12864-submit
- Head: 8c5455d3d242 (matches the listed head)
- Base: upstream/main at 9d7cc2884e8a (merge-base cabedd1d968, 16 commits behind)
- Scope: 2 commits. `solr/core/src/test/org/apache/solr/handler/JsonLoaderTest.java` (+37: `testEchoDocsWithMapUniqueKeyOnly`), `SOLR-12864-TESTING.md` (kept in place). Test-only: no production code changed.
- Verdict: Nearly
- Reviewer: review-agent A2 (claude-haiku-5-5, Claude Code), 2026-10-08

Nothing here was compiled, run, or tested. No Gradle. Every claim below was checked by reading code. Patches: none.

## Premise check (hypothetical-reproduction handoff)

The JIRA symptom is that `/update/json/docs?mapUniqueKeyOnly=true&df=text&echo=true` echoes an empty `text` list. The code does not reproduce that symptom, and the test pins the working behavior.

- VERIFIED: `JsonLoader.getDocMap` (`JsonLoader.java:295-327`) builds the echo copy inside `handle`. With `mapUniqueKeyOnly`, `deepValues` is filled from `result.values()` (`:320-321`) before the handler returns, so the `df` list is a copy and not a view over the reused record map. The JIRA's `[]` shape therefore does not occur on this base.
- VERIFIED: `srcField` is removed from `result` before `deepValues` is built (`:317-321`), so the `df` list holds only the id and the other values.
- VERIFIED: echo mode never calls the processor (`JsonLoader.java:272-278`), so `p.addCommands` stays empty, as the test asserts.
- VERIFIED: the nested-list shape the TESTING doc asked about is correct. `JsonRecordReader` keeps an array-valued field as a `List` (`solrj/.../JsonRecordReader.java:337-346`, `putValue` at `:470-485`). So `c` holds a `List`, and that list is one element of `deepValues`, which gives `["1","b",["d","e"]]` as the test expects.

## Findings (ranked)

LOW (owner call): The test is a pin, not a fix. It passes on main by reading, so the fail-before stage will report `NOT_PROVEN` and the job stays queued. This is the same question as SOLR-10641: is a passing pin accepted without a fail-before proof, or is it dropped? Not decided here.

No defects found in the test by reading.

## Verified correct (by reading; not run)

- Imports. `ArrayList`, `Arrays`, `List`, `Map`, `ModifiableSolrParams`, `ContentStreamBase`, `SolrQueryRequest`, `SolrQueryResponse`, and `BufferingRequestProcessor` are imported in `JsonLoaderTest.java` (lines 19-37).
- The `srcField` branch. `load` builds a `RecordingJSONParser` when `srcField` is set and the split is `/` (`JsonLoader.java:250-260`), which is the test's configuration. `_src_` appears in the copy only when `srcField` is set, so `assertEquals(src, containsKey("_src_"))` holds.
- The expected values. Record 1 is `id, a, c` and record 2 is `id, f`, so the `LinkedHashMap` order gives `["1","b",["d","e"]]` and `["2","g"]`. The original keys `a` and `f` are absent from the copies.
- The response key. `rsp.add("docs", docs)` (`JsonLoader.java:275`) is the key the test reads.
- The changelog is absent, which is correct for a test-only branch.
- All commits are authored as the ICLA identity, with no Co-Authored-By trailers.

## Owner decisions

1. Accept a passing pin at `NOT_PROVEN`, or drop the branch (see the LOW finding).

## Not checked

- Nothing was compiled or run.
- `JsonRecordReader`'s record-reuse ordering was verified only at the `putValue` and array-handling lines, not across a full stream.
- The `req(SolrParams)` helper from `SolrTestCaseJ4` was not traced, but existing tests in the same file call it the same way.
- Upstream conflicts. The branch is 16 commits behind `upstream/main`.
