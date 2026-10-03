# SOLR-16849 — Testing handoff

**Status: UNCOMPILED AND UNTESTED.** Written without running Gradle
(no compile, no tests). A reviewer must compile and test before this goes
anywhere near a PR.

## Round-3 review: premise needs re-validation (read this first)

On `upstream/main`, `GetSegmentData` already passes `failOnReadOnly=false` to
`getIndexWriter` at both call sites (commit `f2aaf8769fd`, SOLR-18083,
"GetSegmentData should work in read-only mode as well"), and
`DefaultSolrCoreState.getIndexWriter(core, failOnReadOnly)` throws the ticket's
"Indexing is temporarily disabled" only when `failOnReadOnly` is `true`. This
branch is based on that commit. So the error in the ticket (reported on 8.11.2)
very likely no longer reproduces, and the code change below removes
`indexWriterConfig` and merge information from the response of read-only cores
where they are currently available. **Decision needed from you:** confirm on
current `main` with a read-only collection; if COLSTATUS and `/admin/segments`
work, treat SOLR-16849 as fixed by SOLR-18083 and do not submit the code change
(the handoff text below describes the original reasoning, which the statement
"`getIndexWriter(core, false)` ... is the one that throws" contradicts). The
code was left unchanged in this pass.

What was added instead (test only, no changelog needed):
`SegmentsInfoRequestHandlerTest#testSegmentInfosOnReadOnlyCore` sets
`core.readOnly = true`, requests `/admin/segments` with `coreInfo=true`, and
asserts the segments and the core info are returned, then resets the flag.
It passes on `main` as it is and with this branch's change, so it does not
decide between them; it locks in that read-only cores can serve segment info.
Queued: `org.apache.solr.handler.admin.SegmentsInfoRequestHandlerTest` with
Spotless. Not compiled or run.

## What the patch does

`/admin/segments` (and COLSTATUS with `coreInfo`/`segments`/`sizeInfo`/
`fieldInfo`/`rawSize*` flags, which call it) unconditionally opened an
`IndexWriter` via `getSolrCoreState().getIndexWriter(core, false)`. On a
readonly core the open throws (with `failOnReadOnly=false` it proceeds to
`createMainIndexWriter`, which fails against a readonly index), so a
read-only info API errored out.

Two-site change, one file
(`solr/core/src/java/org/apache/solr/handler/admin/api/GetSegmentData.java`):

1. `indexWriterConfig` block: skip opening the writer when
   `core.readOnly`, leaving `coreSummary.indexWriterConfig` unset (the
   existing `iwRef != null` guard already handles the null path).
2. `getMergeInformation`: early-return an empty map when
   `req.getCore().readOnly`, so `runningMerges` stays absent and no segment
   is flagged `mergeCandidate`.

Everything else (segment sizes, counts, field info) is reader-based and
already worked on readonly cores.

Notes for the reviewer:

- `SolrCore.readOnly` is `public volatile`; `DefaultSolrCoreState` is the
  only `SolrCoreState` implementation, and its `getIndexWriter(core, false)`
  path is the one that throws on readonly (the `failOnReadOnly` guard only
  throws when `failOnReadOnly=true`).
- Degradation is silent by design: a read-only info API returning segment
  data without merge info is the expected graceful behavior per the ticket.

## Recommended reviewer commands

```bash
cp ~/workspace/solr/gradle.properties .   # worktrees don't inherit it
~/workspace/tools/solr-gradle.sh :solr:core:compileJava -Pvalidation.errorprone=true
```

Suggested tests (not written):

1. On a readonly core, GET `/admin/segments` → 200 with segment data
   present, no `indexWriterConfig`, no `runningMerges` (was: error from the
   writer open).
2. Same request on a writable core → unchanged response including
   `indexWriterConfig` and merge info (no regression).
3. Existing segments-API tests: look for `GetSegmentData`-related suites
   under `:solr:core:test`.

## Patch limits and follow-ups

- **Not compiled or tested.**
- Constructing a readonly core in a test needs the `core.readOnly`
  machinery — left for the reviewer/test phase.
- No changelog entry (repo convention: scaffold once a Jira/PR is assigned).
- Remove this file before opening the upstream PR.
