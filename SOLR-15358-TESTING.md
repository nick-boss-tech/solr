# SOLR-15358 — Testing Handoff (external reviewer)

Ticket: https://issues.apache.org/jira/browse/SOLR-15358
("CurrencyFieldType doesn't support docValues", reporter: David Smiley)
Branch: `solr-15358-submit` (based on origin/main @ 56ec140e363)
Fix commit: 0969c9693f8 — "SOLR-15358: honor docValues on CurrencyFieldType sub-fields"

## What the patch does
`CurrencyFieldType.createFields` built its `___amount` / `___currency` sub-fields
via the singular `createField` form, which never produces docValues fields — so
`docValues="true"` on the sub-field types was silently ignored (masked further by
uninversion). The two calls now use the plural `createFields` form, so sub-field
types emit their docValues fields, or fail fast with the standard
`UnsupportedOperationException` guard when the sub-field type cannot honor
docValues. Two-line change; stored-field branch untouched.

## What to verify
1. Compiles: `:solr:core:compileJava -Pvalidation.errorprone=true`.
2. Behavior: schema with a CurrencyFieldType whose sub-field types declare
   `docValues="true"` (+ `uninvertible="false"` per the reporter's tip); index a
   doc; assert the segment carries docValues for the `___amount` and `___currency`
   sub-fields (e.g. a docValues-backed sort/facet that fails without them).
3. Regression: docValues off behaves as before — no new exceptions, no duplicate
   fields. Note the behavior change: a sub-field type that cannot produce
   docValues while the schema asks for them now throws instead of silently
   dropping (this is the intended fail-fast).
4. Existing suites: currency-related schema tests (e.g. CurrencyFieldType tests,
   if present) — run the schema package tests.

## AI disclosure
This change was drafted with AI assistance (Muse) and has not been compiled or
tested by the author. Human review, compilation, and test validation required
before any upstream PR.
