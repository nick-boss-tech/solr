# SOLR-16673 remaining issues: Windows premise review

Review date: 2026-10-18 UTC
Ticket branch: `origin/solr-16673-submit` at `2adeae3bf8f14a64147d0dabc078b31ff378b4ab`
Scope: static code-path review only. No build or tests were run. Linux owns any implementation, regression test, and gate work.

## Issue 2 premise: confirmed

In `SchemaDesigner.analyze`, sample documents are indexed at `SchemaDesigner.java:817`, then passed to `buildSchemaDesignerResponse` at lines 825-831. In that response builder, lines 1308-1314 obtain the uniqueKey name and cast each document's raw `getFieldValue(uniqueKeyField)` result to `String` before returning `docIds`.

The ticket comment says the supplied uniqueKey is a long or int. For that input, the raw value is numeric, so the cast at line 1311 throws `ClassCastException` while constructing the analysis response. This confirms the reported numeric uniqueKey premise.

**Status:** confirmed by code path. Hand off to a Linux host for the smallest response fix, a regression test that fails before the fix, and the required gate. This Windows review did not edit the submit branch.

## Issue 3: nested child documents and `_root_`

`DocumentBuilder.unexpectedNestedDocException` checks `!schema.isUsableForChildDocs()` at `DocumentBuilder.java:404` and emits the reported message at lines 407-410. `IndexSchema.isUsableForChildDocs`, lines 2194-2199, returns true only when `_root_` has a field type and that type name exactly matches the uniqueKey field type. Separately, an explicitly declared incompatible `_root_` is rejected during schema initialization at `IndexSchema.java:582-590`; that initialization check does not emit the reported BAD_REQUEST.

There is a Schema Designer defect for the ticket's numeric uniqueKey case. When nested docs are enabled and `_root_` is not explicitly declared, `SchemaDesignerConfigSetHelper.enableNestedDocsFields` creates `_root_` with the literal `string` type at line 543, regardless of the uniqueKey type. That produces a root field incompatible with a numeric uniqueKey.

`SchemaDesigner.analyze` creates or reloads the temp collection at lines 796-802, applies and persists a nested-doc toggle at lines 805-808, indexes sample documents at lines 815-818, and performs its conditional collection reload at lines 821-823. The explicit reload is after indexing. Whether the live collection sees the persisted `_root_` before indexing depends on managed-schema refresh behavior, so the Linux reproduction should check this ordering rather than assume it caused the ticket's error.

The ticket comment does not record whether the analysis request enabled nested docs. With the toggle left disabled and no usable `_root_`, rejecting child documents is expected validation. With the toggle enabled for a numeric uniqueKey, the helper's auto-added `string` root is a defect; if that schema is what indexing sees, `isUsableForChildDocs()` is false and the same BAD_REQUEST check follows. The explicit collection reload occurs after indexing, but whether the live collection sees the persisted schema before then depends on managed-schema refresh behavior.

**Status:** the incompatible `_root_` type is a confirmed defect in the numeric-key nested-doc activation path; the exact reporter path remains unconfirmed because the ticket comment does not record the toggle state or live schema refresh. Hand off to a Linux host to reproduce both toggle states against the pinned ticket input, check the update ordering, then implement the smallest fix, add a regression test, and gate it. Do not draft a JIRA comment until that run establishes whether the reporter left nested docs disabled.

## Handoff and limits

- The Windows claim covered only these premise reviews. No ticket submit branch was changed, and no build or test was run.
- The remaining Issue 2 and Issue 3 implementation and gate work is open for a Linux claimant.
- The assignment remains open for those remaining items. This report completes only the Windows review slice.
