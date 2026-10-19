Draft status: Linux full gate green on apache/main. Proof numbers come from receipts/SOLR-16673.md (round 2, branch solr-16673-uniquekey-apache, head a494962d79a456c8c5c706e0ebc58870e19119b4). Not opened: openings stay with Nick.

🤖 *AI text below* 🤖 *(posted on behalf of Nick Shanin)*

https://issues.apache.org/jira/browse/SOLR-16673

## What happens today

**A numeric unique key can abort Schema Designer analysis while it builds the response.**

The response builder casts the raw uniqueKey value to `String`. When the value is a `Long` or `Integer`, analysis throws `ClassCastException` before it returns the document IDs. The cast is in `solr/core/src/java/org/apache/solr/handler/designer/SchemaDesigner.java`.

## What this change does

**Analysis responses now return each non-null uniqueKey value as text.**

The regression test indexes a document whose id is a `Long` (42) on the default string uniqueKey, and expects the response to contain `"42"`. The change is in `SchemaDesigner.java`, with the regression test in `solr/core/src/test/org/apache/solr/handler/designer/TestSchemaDesigner.java`.

## Proof

**Gate result: green.** The gate ran at the branch head on a base built on Apache `main` (`c8ab71430f`).

- Changelog YAML parse, tidy, and Error Prone compile all passed.
- With the production change reverted to the base, `testNumericUniqueKeyDocIds` fails with `ClassCastException: Long cannot be cast to String`. The class run shows 11 tests and 1 failure.
- With the change, `TestSchemaDesigner` runs 11 tests with 0 failures and 0 errors (seed `16673C0FFEE16673`, fresh JUnit XML).
- The module check (`:solr:core:check -x test`) passed.

## Limits

**This change covers document IDs in the analysis response.**

The nested `_root_` field type issue is a separate change and draft.

### AI assistance

AI agents assisted with research, implementation, review, and drafting. Nick Shanin directed the work and takes responsibility for this contribution.
