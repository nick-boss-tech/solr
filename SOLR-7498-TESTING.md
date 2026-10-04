# SOLR-7498 - hypothetical-reproduction handoff

**Read this first: nothing on this branch was compiled or run.** The research/implement pipeline has no Gradle access. The fix and test are best-guess; treat them as hypotheses.

- JIRA: https://issues.apache.org/jira/browse/SOLR-7498 - "Error adding field 'stream_size'='null' msg=For input string: "null" using ContentStreamUpdateRequest" (Swoorup Joshi). A later comment reports the same with SolrJ 8.11.1; `bin/post` works because it sets a numeric size.
- Branch: `solr-7498-submit` off `apache/solr` main `14c7aac0d15`
- Commits: fix + test, changelog fragment, this file (drop the doc before a PR)

## The bug, as understood
`ExtractionBackend.buildMetadataFromRequest` does `md.add(STREAM_SIZE, String.valueOf(request.streamSize))`. `streamSize` is a `Long` that is `null` when the content stream does not report a size (`ContentStream.getSize()` returns null, e.g. SolrJ `ContentStreamUpdateRequest` with a streamed body). `String.valueOf((Object) null)` is the text `"null"`, which then maps to the `stream_size` field and fails for numeric types. Still true on main (`ExtractionBackend.java` L55).

## What the branch changes
- `ExtractionBackend.buildMetadataFromRequest`: only add `stream_size` when the size is known (`ExtractionMetadata.add` already ignores null values).
- New `ExtractionBackendMetadataTest` (plain `SolrTestCase`, anonymous backend stub): unknown size gives no `stream_size`; `1234L` gives `"1234"`.

## What was guessed (verify these first)
1. `ExtractionBackend` has exactly the abstract methods stubbed in the test (`extract`, `extractWithSaxHandler`, `name`); a newer abstract method would break compilation. `ExtractionResult` is assumed to be a type in the same package.
2. `ExtractionRequest.builder()` accepts a missing size and `STREAM_NAME` is a constant in `ExtractingMetadataConstants`.
3. Whether other consumers of `stream_size` metadata rely on the `"null"` text (none found in the module).

## How to verify (Gradle required; not run here)
```
.\gradlew :solr:modules:extraction:spotlessApply
.\gradlew :solr:modules:extraction:test --tests "org.apache.solr.handler.extraction.ExtractionBackendMetadataTest"
```
Fail-before: revert only `ExtractionBackend.java`; `getFirst(STREAM_SIZE)` returns `"null"`.

## Not done
No JIRA comment, no PR. Changelog author is `Nick Shanin` per the ICLA note.
