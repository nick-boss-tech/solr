# SOLR-17722 — Testing handoff

**Status: UNCOMPILED AND UNTESTED.** Written without running Gradle
(no compile, no tests). A reviewer must compile and test before this goes
anywhere near a PR.

## Round 4 review outcome (read this first)

See `research/branch-reviews/round-4/SOLR-17722-review.md`. **The premise is
unproven and the patch may be a no-op.** On `upstream/main`,
`HttpSolrClient.initializeSolrParams` (used by the Jetty, JDK and concurrent
update clients) copies the request params and then calls
`params.set(CommonParams.WT, parserToUse.getWriterType())`, so a mirrored
`wt=json` is replaced by the parser's writer type at send time. Reproduce the
failure on current `main` before shipping anything; if it does not fail, the
right outcome is a comment on the ticket.

Changes in this round (no behaviour change):

- Fixed the import order in `SolrMessageProcessor` (`spotlessCheck` would fail).
- Added `SolrMessageProcessorTest#handleItemForcesJavabinResponseWriter` (not
  compiled or run). It only proves the sanitizer sets `wt=javabin` on a mocked
  request's `ModifiableSolrParams`; it does not prove the ticket. It also pins
  that the edit applies to every request type, which the review questions.
- No changelog added, because the fix is not shown to change behaviour.
- Not changed: placement inside `prepareIfUpdateRequest` (applies to all request
  types), and `MirroredSolrRequest.setParams` as an alternative to the
  `instanceof ModifiableSolrParams` guard (the "no `setParams`" remark below is
  wrong: that static helper exists for `MirroredAdminRequest` and `UpdateRequest`).

## What the patch does

The CrossDC consumer replays mirrored `SolrRequest`s (with their original
params) against the secondary cluster via a `CloudSolrClient` that uses the
default **binary** response parser. If the original update was indexed with a
non-binary writer (e.g. `wt=json` on the update URL), that param was mirrored
too — and the consumer then tried to parse the JSON response as binary,
breaking the mirrored write.

Fix: at the top of `SolrMessageProcessor.prepareIfUpdateRequest` (which runs
for every mirrored request before it is sent), force `wt=javabin` on the
request params when they are a `ModifiableSolrParams` (the `UpdateRequest`
case and most other SolrJ request types). This aligns the response format
with the consumer client's binary parser regardless of what the original
request carried. No protocol or design changes — param-only sanitization,
next to the existing `_version_` cleanup in the same method.

File changed:
- `solr/cross-dc-manager/src/java/org/apache/solr/crossdc/manager/messageprocessor/SolrMessageProcessor.java`
  (~6 lines added + 2 imports)

## Recommended reviewer commands

```bash
cp ~/workspace/solr/gradle.properties .   # worktrees don't inherit it
~/workspace/tools/solr-gradle.sh :solr:cross-dc-manager:compileJava -Pvalidation.errorprone=true
```

Suggested tests (not written):

1. Unit-level: build an `UpdateRequest` (or `MirroredSolrRequest`) with
   `wt=json`, run it through the sanitization path, assert `wt` is `javabin`
   before execution.
2. Integration (reviewer-run): mirror an update with `?wt=json` to the
   primary and verify the secondary write succeeds without a binary-parse
   error.

## Patch limits and follow-ups

- **Not compiled or tested.**
- The `instanceof ModifiableSolrParams` guard means exotic `SolrParams`
  implementations keep their original `wt`; in practice mirrored requests use
  `ModifiableSolrParams`. A stricter alternative (rebuilding params) isn't
  possible — `SolrRequest` has no `setParams` — so this is the practical
  ceiling for a param-only fix.
- No changelog entry (repo convention: scaffold once a Jira/PR is assigned).
- Remove this file before opening the upstream PR.
