Draft status: Linux full gate green on apache/main. Proof numbers come from receipts/SOLR-16673.md (round 2, branch solr-16673-rootfield-main, head 72e505cc20c7fd1c03e7a3cae98ad450f57d6ed5). Not opened: openings stay with Nick.

🤖 *AI text below* 🤖 *(posted on behalf of Nick Shanin)*

https://issues.apache.org/jira/browse/SOLR-16673

## What happens today

**Schema Designer can give nested documents an unusable `_root_` field with a numeric uniqueKey, and drops their children.**

When nested docs are enabled and `_root_` is absent, the helper creates `_root_` as `string` even if the uniqueKey is `plong` or `tlong`. Solr requires the two field types to match, so indexing child documents fails. Separately, the sample loader stored each nested document's children as a field instead of attaching them, so nested samples indexed without their children.

## What this change does

**Schema Designer creates `_root_` with the uniqueKey type, reloads the temporary collection before indexing, and attaches nested children.**

The regression test enables nested docs with a `tlong` uniqueKey, submits a parent with children, and checks that indexing succeeds and that `_root_` is usable for child documents. The changes are in `SchemaDesignerConfigSetHelper.java`, `SchemaDesigner.java`, and `DefaultSampleDocumentsLoader.java`, with the regression test in `solr/core/src/test/org/apache/solr/handler/designer/TestSchemaDesigner.java`.

## Proof

**Gate result: green.** The gate ran at the branch head on a base built on Apache `main` (`c8ab71430f`).

- Changelog YAML parse, tidy, and Error Prone compile all passed.
- With the three production files reverted to the base, `testAnalyzeNestedDocsWithNumericUniqueKey` fails with `Failed to index 1 sample docs into temp collection`. The class run shows 11 tests and 1 failure.
- Reverting only the helper and the schema designer gives `_root_ field must be defined using the exact same fieldType as the uniqueKey field (id) uses: tlong`.
- With the changes, `TestSchemaDesigner` runs 11 tests with 0 failures and 0 errors (seed `16673B00B516673B`, fresh JUnit XML).
- The module check (`:solr:core:check -x test`) passed.

## Limits

**This change covers an automatically added `_root_` field.**

An explicitly declared incompatible `_root_` remains subject to schema validation. Numeric document ID conversion in the analysis response is a separate change and draft.

### AI assistance

AI agents assisted with research, implementation, review, and drafting. Nick Shanin directed the work and takes responsibility for this contribution.
