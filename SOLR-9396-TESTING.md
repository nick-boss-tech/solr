# SOLR-9396 - hypothetical reproduction and fix (not run)

Nothing here was compiled or executed. The fix and the test were written by reading
`upstream/main`; treat every claim below as a guess to verify first.

## JIRA context
"[subquery] transformer doesn't automatically request needed fields, only other fields in fl can
be used as input to subquery" (Hoss, spun off SOLR-9377). The workaround recorded in the ticket is
to use `fl=*` or list every `$row.foo` field in `fl`. `TestRandomFlRTGCloud.SubQueryValidator`
still carries "HACK to work around SOLR-9396" and uses only `$row.id` for that reason.

## Bug mechanism
`SubQueryAugmenter` extends `DocTransformer` but never overrides `getExtraRequestFields()`.
`SolrReturnFields` adds a transformer's extra fields to the set loaded from the index (without
making them visible in the response). With no extras, a field that is only used as `$row.foo`
is not loaded, so `DocRowParams` finds no value and the subquery gets an empty parameter.

## Fix
`SubQueryAugmenter` scans its (prefix-shifted) subquery parameter values for `$row.field` and
`${row.field}` and returns those names from `getExtraRequestFields()`.

## What the test pins
`TestSubQueryTransformer.testRowFieldNotInFl`: `fl=name_s_dv,depts:[subquery]` with
`depts.q={!term f=dept_id_s v=$row.dept_ss_dv}`; the department docs must be found, and
`dept_ss_dv` must not appear in the response.

## What was guessed / verify first
- That the extra field is really loaded for a docValues-only field (`dept_ss_dv`) when it is
  not in `fl`; if the test fails on the match counts, check `RetrieveFieldsOptimizer` first.
- The last assertion (`dept_ss_dv` absent) assumes extras are not shown. If it fails, the
  hiding behaviour of `SolrReturnFields.wantsField` differs from my reading.
- RTG path (`/get`) is not covered by a new test; the transformer API is the same there.
- Parameters are only scanned for the literal `$row.` / `${row.` forms. A `$row.x` buried in a
  dereferenced parameter (`v=$other`) is not found.
- Fail-before: revert only `SubQueryAugmenterFactory.java`; `testRowFieldNotInFl` should fail
  on the match counts.
- Spotless formatting.
