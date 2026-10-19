Draft status: Linux full gate pending. Update the Proof section from `receipts/SOLR-16673.md` before any public use.

🤖 *AI text below* 🤖 *(posted on behalf of Nick Shanin)*

https://issues.apache.org/jira/browse/SOLR-16673

## What happens today

**Schema Designer can give nested documents an unusable `_root_` field with a numeric uniqueKey.**

When nested docs are enabled and `_root_` is absent, the helper creates `_root_` as `string` even if the uniqueKey is `plong`. Solr requires those field types to match, so child document indexing can return the nested document type mismatch. [Base `_root_` creation](https://github.com/nick-boss-tech/solr/blob/e044bf20b405b8ca985730be3553d77d1fc8ff31/solr/core/src/java/org/apache/solr/handler/designer/SchemaDesignerConfigSetHelper.java#L543)

## What this change does

**Schema Designer creates `_root_` with the uniqueKey type and reloads the temporary collection before indexing.**

The regression test enables nested docs with a `plong` uniqueKey, submits a parent and child, and checks for no update error and a usable `plong` `_root_`. [Field type selection](https://github.com/nick-boss-tech/solr/blob/5fb792482fc3d8fef851513bb91eb3231409a033/solr/core/src/java/org/apache/solr/handler/designer/SchemaDesignerConfigSetHelper.java#L541-L548) [Reload before indexing](https://github.com/nick-boss-tech/solr/blob/5fb792482fc3d8fef851513bb91eb3231409a033/solr/core/src/java/org/apache/solr/handler/designer/SchemaDesigner.java#L811-L813) [Regression test](https://github.com/nick-boss-tech/solr/blob/5fb792482fc3d8fef851513bb91eb3231409a033/solr/core/src/test/org/apache/solr/handler/designer/TestSchemaDesigner.java#L314-L344)

## Proof

**The Linux proof run is pending.**

The gate will run `org.apache.solr.handler.designer.TestSchemaDesigner` at the branch head and on base production code with the regression test. The receipt will record the fail-before mismatch and focused test counts. Add the gate date and counts here before posting.

## Limits

**This change covers an automatically added `_root_` field.**

An explicitly declared incompatible `_root_` remains subject to schema validation. Numeric document ID conversion in the analysis response is addressed in a separate branch and draft.

Changelog: [SOLR-16673-root-field.yml](https://github.com/nick-boss-tech/solr/blob/5fb792482fc3d8fef851513bb91eb3231409a033/changelog/unreleased/SOLR-16673-root-field.yml)

### AI assistance

AI agents assisted with research, implementation, review, and drafting. Nick Shanin directed the work and takes responsibility for this contribution.
