# PR #5156 draft: SOLR-9396 body, trimmed (review item 2)

- Written against head: f62c06588f272834cae16f3f02c8383886b8f00d (live branch solr-9396-submit, the head the PR body cites after the 2026-10-16 update).
- Visible characters: 3,172 (body; markdown syntax and link URLs removed). Limit 3,500.
- Proof numbers: receipts/SOLR-9396.md for the base run (1 of 11 fails, the new test is the only failure) and reports/SOLR-9396-distrib-row-field-verify.md for the head counts (TestSubQueryTransformerDistrib 2 of 2, TestSubQueryTransformer 11 of 11, spotless clean, verified by vm2 2026-10-16). The base failure text for the distributed test is from the same report.
- Links still point at the earlier head dc862ccf for the production file. Re-point them to f62c06588f272834cae16f3f02c8383886b8f00d when this text is applied.
- Not applied to the live PR. Drafts only, per the assignment.

=== TITLE ===

(unchanged) SOLR-9396: The transformer asks the index for the fields its subquery references

=== BODY ===

🤖 *AI text below* 🤖 *(posted on behalf of Nick Shanin)*

https://issues.apache.org/jira/browse/SOLR-9396

## What happens today

**A `[subquery]` fails when it reads a `$row.` field that the outer `fl` does not list.**

A subquery can read a value from the parent document, as in `depts.q={!term f=dept_id_s v=$row.dept_id}`. The transformer does not ask the index for `dept_id`. If the outer `fl` does not list it, the field is missing and the transformer fails with `while invoking depts:[subquery] on doc=...` ([SubQueryAugmenterFactory.java L344-L365 before this change](https://github.com/nick-boss-tech/solr/blob/c3cdf7b46e8cfff3673f76d881f32cf8e7b00622/solr/core/src/java/org/apache/solr/response/transform/SubQueryAugmenterFactory.java#L344-L365)). The workaround is to list every referenced field in `fl`, or use `fl=*`.

## What this change does

**The transformer asks the index for the fields its subquery references.**

When the transformer is created, it scans the subquery's own parameter values for `$row.field` and `${row.field}` references and adds those names to the fields the request reads ([SubQueryAugmenterFactory.java L307-L362](https://github.com/nick-boss-tech/solr/blob/f62c06588f272834cae16f3f02c8383886b8f00d/solr/core/src/java/org/apache/solr/response/transform/SubQueryAugmenterFactory.java#L307-L362)). The response shape does not change: a field that `fl` does not list is read for the subquery but not written to the response ([TestSubQueryTransformer.java L257-L283](https://github.com/nick-boss-tech/solr/blob/f62c06588f272834cae16f3f02c8383886b8f00d/solr/core/src/test/org/apache/solr/response/transform/TestSubQueryTransformer.java#L257-L283)). The cost is one extra field read for each referenced field that `fl` did not name.

## Proof

**The new tests fail on the base code and pass with this change.**

- [TestSubQueryTransformer.testRowFieldNotInFl](https://github.com/nick-boss-tech/solr/blob/f62c06588f272834cae16f3f02c8383886b8f00d/solr/core/src/test/org/apache/solr/response/transform/TestSubQueryTransformer.java#L257-L283) is new. On the base `SubQueryAugmenterFactory.java` with this test file, it is the only failure: 1 of 11 fails, and 10 pass.
- [TestSubQueryTransformerDistrib.testRowFieldNotInFlDistributed](https://github.com/nick-boss-tech/solr/blob/f62c06588f272834cae16f3f02c8383886b8f00d/solr/core/src/test/org/apache/solr/response/transform/TestSubQueryTransformerDistrib.java#L244-L282) covers the distributed path. The outer `fl` is `name_s_dv,depts:[subquery]`, and the subquery reads `$row.dept_ss_dv`. On base it fails by its assertion ("dave works in both depts", TestSubQueryTransformerDistrib.java:280), and the other test in the class passes.
- At head `f62c06588f27`, TestSubQueryTransformerDistrib 2 of 2 and TestSubQueryTransformer 11 of 11 pass, counted from fresh JUnit XML, and spotlessJavaCheck is clean.

## A choice to check

**The transformer reads `$row.field` and `${row.field}` names from the subquery's own parameters, not from every field of the request.**

Options considered: scan the subquery's parameter values for `$row.field` and `${row.field}` references (implemented), or read every field whenever a `[subquery]` is present. The scan reads only the fields a subquery names. It cannot see a reference that reaches the subquery through another request parameter (see Limits). Reading every field needs no scan, but then every subquery reads every field. Was the scan the right call?

## Limits

- Only a `$row.field` or `${row.field}` reference written in the subquery's own parameters is scanned. A reference that reaches the subquery through another request parameter must still be listed in `fl`.
- The scan also matches the `${row.` spelling, which the query parser never treats as a row reference. That can add an unused field read. Narrowing it to `$row.` is a named follow-up.
- The real-time get path (`/get`) uses the same field parsing, but no test covers it.

Changelog: [changelog/unreleased/SOLR-9396-subquery-row-fields.yml](https://github.com/nick-boss-tech/solr/blob/f62c06588f272834cae16f3f02c8383886b8f00d/changelog/unreleased/SOLR-9396-subquery-row-fields.yml)

### AI assistance

AI agents assisted with research, implementation, review, and drafting. Nick Shanin directed the work and takes responsibility for this contribution.
