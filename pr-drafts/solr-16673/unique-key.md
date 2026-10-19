Draft status: Linux full gate pending. Update the Proof section from `receipts/SOLR-16673.md` before any public use.

🤖 *AI text below* 🤖 *(posted on behalf of Nick Shanin)*

https://issues.apache.org/jira/browse/SOLR-16673

## What happens today

**A numeric unique key can abort Schema Designer analysis while it builds the response.**

At the base head, the response builder casts the raw uniqueKey value to `String`. When the value is a `Long` or `Integer`, analysis throws `ClassCastException` before returning the document IDs. [Base code at the failing cast](https://github.com/nick-boss-tech/solr/blob/e044bf20b405b8ca985730be3553d77d1fc8ff31/solr/core/src/java/org/apache/solr/handler/designer/SchemaDesigner.java#L1311)

## What this change does

**Analysis responses now return each non-null uniqueKey value as text.**

The regression test sets the uniqueKey field to `plong`, supplies a `Long`, and expects the response to contain `"42"`. [Response conversion](https://github.com/nick-boss-tech/solr/blob/70c6eb0c49008cf9518c456dedd6c4981642d52c/solr/core/src/java/org/apache/solr/handler/designer/SchemaDesigner.java#L1309-L1315) [Regression test](https://github.com/nick-boss-tech/solr/blob/70c6eb0c49008cf9518c456dedd6c4981642d52c/solr/core/src/test/org/apache/solr/handler/designer/TestSchemaDesigner.java#L314-L334)

## Proof

**The Linux proof run is pending.**

The gate will run `org.apache.solr.handler.designer.TestSchemaDesigner` at the branch head and on base production code with the regression test. The receipt will record the fail-before exception and focused test counts. Add the gate date and counts here before posting.

## Limits

**This change covers document IDs in the analysis response.**

The nested `_root_` field type issue is addressed in a separate branch and draft.

Changelog: [SOLR-16673-unique-key.yml](https://github.com/nick-boss-tech/solr/blob/70c6eb0c49008cf9518c456dedd6c4981642d52c/changelog/unreleased/SOLR-16673-unique-key.yml)

### AI assistance

AI agents assisted with research, implementation, review, and drafting. Nick Shanin directed the work and takes responsibility for this contribution.
