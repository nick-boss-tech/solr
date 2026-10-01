# SOLR-16849 — Testing handoff

**Status: UNCOMPILED AND UNTESTED.** Written without running Gradle
(no compile, no tests). A reviewer must compile and test before this goes
anywhere near a PR.

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
