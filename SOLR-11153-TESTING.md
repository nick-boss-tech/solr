# SOLR-11153 - hypothetical-reproduction handoff

**Read this first: nothing on this branch was compiled or run.** The research/implement pipeline has no Gradle access. The fix and test are best-guess; treat them as hypotheses. This ticket was originally skipped as "diagnosability only"; a later spot-check of the skips showed the NPE is still present on `apache/solr main`.

- JIRA: https://issues.apache.org/jira/browse/SOLR-11153 - "Incomplete schema results in mysterious error" (Shawn Heisey, 2017, 19 comments, patch never committed). A minimal schema whose `<schema>` element has no `name` attribute ended in an NPE in `SchemaXmlWriter`.
- Branch: `solr-11153-submit` off `apache/solr main` (`upstream/main` at branch time)
- Commits: fix + test, changelog fragment, this file (drop the doc before a PR)

## The bug, as understood
`IndexSchema` reads `name = rootNode.attributes().get("name")` with no default, so `getSchemaName()` is null for a schema lacking the attribute (only a log line mentions it; `version` already defaults to 1.0). `SchemaXmlWriter.writeResponse` then does `schemaProperties.get(IndexSchema.NAME).toString()` (and the same for `VERSION`), which throws an NPE when `wt=schema.xml` is requested. `GetSchema` already null-checks the name, so only the XML writer is exposed. The ticket's original patches defaulted the name in `IndexSchema`; I took the narrower route of making the writer tolerate absent attributes so the schema still round-trips as written.

## What the branch changes
- `SchemaXmlWriter.writeResponse`: writes the `name` and `version` attributes only when the property is present.
- New `SchemaXmlWriterMissingNameTest` (`SolrTestCaseJ4`, `org.apache.solr.response`): feeds `SchemaXmlWriter` a response whose `schema` map has no name or version, expects well-formed `<schema ...>` output without a `name=` attribute and no exception.

## What was guessed (verify these first)
1. That the production map from `IndexSchema.getNamedPropertyValues()` really lacks the `name` key when the attribute is absent (rather than containing a null value); the writer handles both. The test builds the map by hand instead of loading a name-less schema, which would be a stronger test (needs a new test schema file or a schema-API based setup).
2. `SchemaXmlWriter.writeResponse(Writer, SolrQueryRequest, SolrQueryResponse)` is usable directly from a test in the same package without a full request cycle.
3. Maintainers may prefer the ticket's approach (default name "example", log a warning), or rejecting a schema with no name at load time with a clear message. Not decided here.

## How to verify (Gradle required; not run here)
```
.\gradlew :solr:core:spotlessApply
.\gradlew :solr:core:test --tests "org.apache.solr.response.SchemaXmlWriterMissingNameTest"
```
Fail-before: revert only `SchemaXmlWriter.java`; the test hits the NPE.

## Not done
No JIRA comment, no PR. Changelog author is `Nick Shanin` per the ICLA note.
