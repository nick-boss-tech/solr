# SOLR-16673 remaining Schema Designer issues

Review and implementation date: 2026-10-18 UTC
Ticket branch reviewed: `origin/solr-16673-submit` at `2adeae3bf8f14a64147d0dabc078b31ff378b4ab`
Implementation base: fork `origin/main` at `498a09aea97aac86c2cc7ac114113b946d249942`
Merge-base with `upstream/main`: `e044bf20b405b8ca985730be3553d77d1fc8ff31`

The Windows review was static. No build or test was run on this host. The two fixes and regression tests below are pushed to separate branches. Both full hardening gates remain open for Linux.

## Issue 2 premise: confirmed

In the analysis response builder, the base code casts the raw uniqueKey field value to `String` at `SchemaDesigner.java:1311`. A `Long` or `Integer` value therefore throws `ClassCastException` while the response is built. This confirms the numeric uniqueKey failure described in the ticket.

**Status:** implementation pushed; Linux gate pending.

- Branch: `solr-16673-uniquekey`
- Head: `70c6eb0c49008cf9518c456dedd6c4981642d52c`
- Change: convert each non-null uniqueKey value with `Object.toString()` before returning `docIds`.
- Regression test: `TestSchemaDesigner.testNumericUniqueKeyDocIds` changes the uniqueKey schema to `plong`, supplies a `Long` value, and expects `"42"` in the response.
- Gate: `gates/SOLR-16673-uniquekey.md`, status OPEN. No test, Error Prone, tidy, changelog parser, or module check has run.
- PR draft: `pr-drafts/solr-16673/unique-key.md`. Proof counts and date remain pending the gate receipt.

## Issue 3: nested child documents and `_root_`

`DocumentBuilder.unexpectedNestedDocException` checks `!schema.isUsableForChildDocs()` at `DocumentBuilder.java:404`. `IndexSchema.isUsableForChildDocs` requires `_root_` and the uniqueKey field to have the same field type name. When nested docs are enabled and `_root_` is absent, the base Schema Designer helper adds `_root_` as the literal `string` type at `SchemaDesignerConfigSetHelper.java:543`, even when the uniqueKey is numeric. That is a defect in the numeric-key nested-doc activation path.

The ticket comment does not record whether the analysis request enabled nested docs, so the exact reporter path remains unconfirmed. With nested docs disabled and no usable `_root_`, rejecting child documents is expected validation. The regression test exercises the defective activation path directly.

**Status:** implementation pushed; Linux gate pending.

- Branch: `solr-16673-rootfield`
- Head: `5fb792482fc3d8fef851513bb91eb3231409a033`
- Change: create an automatic `_root_` field with the uniqueKey field type, then persist settings and reload the temporary collection before indexing sample documents.
- Regression test: `TestSchemaDesigner.testAnalyzeNestedDocsWithNumericUniqueKey` enables nested docs with a `plong` uniqueKey, indexes a parent and child, checks that the analysis response has no update error, and checks that `_root_` is `plong` and usable for child docs.
- Gate: `gates/SOLR-16673-rootfield.md`, status OPEN. No test, Error Prone, tidy, changelog parser, or module check has run.
- PR draft: `pr-drafts/solr-16673/root-field.md`. Proof counts and date remain pending the gate receipt.

## Handoff and limits

- Both branches are based on fork `main`, not on the existing `solr-16673-submit` branch.
- The numeric doc ID response fix and the nested `_root_` type fix are separate patches and separate PR drafts.
- The root field patch covers an automatically added `_root_`. An explicitly declared incompatible `_root_` remains subject to schema validation.
- Keep the assignment OPEN until both Linux gates are green and the receipt and drafts are updated from their results.
- No PR was opened. No Jira or PR comment was posted.
