# PR 5130 (SOLR-4374): body draft, round 2 item 1

- Head: 433e6c6ba9905d90e9367bd9f6b566c4832b1406 (live body read 2026-10-18T04:33Z; branch head matches).
- Visible characters: 3465 (limit 3,500).
- Proof numbers come only from receipts/SOLR-4374.md: ReturnFieldsTest 17 of 17 at f0e23fca1b74 (fresh JUnit XML 2026-10-15T01:42:14Z) and the base run with the branch test file (1 failure, testDigitLeadingFieldName).
- Changes from the stored body: trimmed to under 3,500 visible characters. The catch-all behavior change and the colon rule are kept. The "nick line" sentence is replaced with a plain statement that the later commit changes only the changelog fragment (receipt: diff verified by content).

---

🤖 *AI text below* 🤖 *(posted on behalf of Nick Shanin)*

https://issues.apache.org/jira/browse/SOLR-4374

## What happens today

**A top-level item in `fl` that starts with a digit is not read as a field name.**

Before this change, [getFieldName](https://github.com/nick-boss-tech/solr/blob/cabedd1d968059215188f4e7563fb303241899ed/solr/core/src/java/org/apache/solr/search/SolrReturnFields.java#L219-L238) accepted a name only when its first character can start a Java identifier. A name such as `1001` fails that test and falls through to the function parser, which reads the number as a constant. So `fl=1001` returns the number 1001 under the key `1001`, not the value of a field named `1001`. The link points at the merge-base commit, before this change.

## What this change does

**A top-level item that starts with a digit is read as a field when the schema has a field with that name.**

When the normal name check fails, the parser tries the name against the schema. The lookup covers explicit fields and dynamic field matches ([helper](https://github.com/nick-boss-tech/solr/blob/433e6c6ba9905d90e9367bd9f6b566c4832b1406/solr/core/src/java/org/apache/solr/search/SolrReturnFields.java#L240-L264), [call site](https://github.com/nick-boss-tech/solr/blob/433e6c6ba9905d90e9367bd9f6b566c4832b1406/solr/core/src/java/org/apache/solr/search/SolrReturnFields.java#L286-L289)). If the name is not in the schema, the parser position is restored and the item is handled as before.

Behavior change, stated openly: a catch-all dynamic field, such as `*`, matches every name. In a schema with one, a top-level number such as `fl=1.5` is read as a field name instead of a constant. Without a catch-all, a number is read as a field only when the schema has a field with that exact name, or a dynamic field pattern such as `1*` that matches it. The same check applies before a colon: `fl=1001:x` is read as a rename with the key `1001` when the schema has a field named `1001`.

## Proof

**The new test fails on the base code and passes with this change.**

[ReturnFieldsTest](https://github.com/nick-boss-tech/solr/blob/433e6c6ba9905d90e9367bd9f6b566c4832b1406/solr/core/src/test/org/apache/solr/search/ReturnFieldsTest.java#L399-L406) adds `testDigitLeadingFieldName`, which checks `fl=1001_s,id` against the `*_s` dynamic field. With the branch test file on the base source, the class runs 17 tests with one failure, in `testDigitLeadingFieldName`. With this change, at head `f0e23fca1b74b3ead96b8aec4601a916dcd49035`, `ReturnFieldsTest` passes 17 of 17, verified 2026-10-15 at that head. The later commit `433e6c6ba99` changes only the changelog fragment. At that head the fragment parses, the formatting check leaves the tree unchanged, and the core main and test sources compile with the error checks CI uses.

A second test, [testDigitLeadingNumberWithoutSchemaFieldStaysConstant](https://github.com/nick-boss-tech/solr/blob/433e6c6ba9905d90e9367bd9f6b566c4832b1406/solr/core/src/test/org/apache/solr/search/ReturnFieldsTest.java#L408-L415), passes on the base source too. It pins existing behavior.

## A choice to check

**The ticket asks to remove the identifier check. This change keeps the check and adds a schema lookup.**

The other route, which the ticket suggests, is to accept every name that starts with a digit as a field. That is simpler. Its cost is that a top-level number in `fl` stops being a constant. This change keeps constants and reads a number as a field only when the schema has that field. Its cost is the catch-all case above. Was the schema lookup the right call?

## Limits

**The test covers a dynamic field, not the ticket's exact case.**

- The test uses `1001_s`. The ticket's example is a field defined as `1001`. That exact case is not tested.
- A rename such as `x:1001` is not changed. The source name in a rename still uses the stricter check. A follow-up can cover it if maintainers want that.
- The colon rule also applies before a function. With a schema field `1001`, `fl=1001:sum(a,b)` returns the function under the key `1001`.
- No other test class was run for this change.

Changelog: [changelog/unreleased/SOLR-4374-digit-leading-field-in-fl.yml](https://github.com/nick-boss-tech/solr/blob/433e6c6ba9905d90e9367bd9f6b566c4832b1406/changelog/unreleased/SOLR-4374-digit-leading-field-in-fl.yml#L1-L7)

### AI assistance

AI agents assisted with research, implementation, review, and drafting. Nick Shanin directed the work and takes responsibility for this contribution.
