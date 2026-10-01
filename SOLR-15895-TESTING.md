# SOLR-15895 — Testing handoff

**Status: UNCOMPILED AND UNTESTED.** Written without running Gradle
(no compile, no tests). A reviewer must compile and test before this goes
anywhere near a PR.

## What the patch does

Creating a managed resource (stopwords/synonyms via the Schema REST API)
with characters illegal in Windows filenames (e.g. `:`) succeeded silently:
the stored filename got truncated at the first illegal char and left as a
0-byte file, while DELETE later 500'd with
`java.nio.file.InvalidPathException: Illegal char <:>` — the resource was
stuck, undeletable via REST or Windows tools. The creation path never
validated that the resourceId maps to a legal filename.

Fix (`solr/core/src/java/org/apache/solr/rest/RestManager.java`):
`registerManagedResource` now rejects resourceIds whose stored filename form
(`resourceId.replace('/','_')`, matching
`ManagedResourceStorage.getStoredResourceId`) contains filename-illegal
characters (`< > : " | ? *` and control chars 0x00-0x1F) with
`SolrException(ErrorCode.BAD_REQUEST)` and a clear message. Existing
prefix/reserved-endpoint checks are untouched (they use SERVER_ERROR, which
is kept). The blacklist is OS-independent — it does not rely on `Path.of`
throwing, which only fails on Windows. This covers both static registration
and REST-API-created resources, which both flow through
`registerManagedResource`.

## Recommended reviewer commands

```bash
cp ~/workspace/solr/gradle.properties .   # worktrees don't inherit it
~/workspace/tools/solr-gradle.sh :solr:core:compileJava -Pvalidation.errorprone=true
```

## Test ideas (not written)

- Unit test calling `registerManagedResource` with a resourceId containing
  `:` (e.g. `/schema/analysis/stopwords/test : list`) → expect
  SolrException with BAD_REQUEST; a normal id (e.g.
  `/schema/analysis/stopwords/english`) registers fine.
- Confirm the reserved-prefix and reserved-endpoint checks still behave as
  before.
- Existing RestManager / managed-resource test suites for regressions.

## Patch limits and follow-ups

- **Not compiled or tested.**
- No changelog entry (repo convention: scaffold once a Jira/PR is assigned).
- Remove this file before opening the upstream PR.
