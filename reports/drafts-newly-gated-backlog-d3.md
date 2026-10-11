# Newly gated drafts, part D3: SOLR-10882 and SOLR-9852 (streaming)

Drafts written: `pr-drafts/streaming/SOLR-10882.md`, `pr-drafts/streaming/SOLR-9852.md`.
Nothing committed, pushed, posted or built. No claim, receipt, assignment or host file was edited.
The only git action beyond reads: `git fetch --no-tags --no-write-fetch-head origin` of the two gated head commits by SHA, which writes objects to the shared object store and changes no refs.

## Heads

- SOLR-10882: receipt head `5d7d07a5b17bfbf9370fe8562ce7b5d9909118ee` (receipt line 5). Live fork tip by `ls-remote` at drafting: the same SHA. Match. Receipt line 4 still names `83fc3dfeb24b` as the tip; that is the tip before the packaging commit, which receipt line 5 says is now the fork head. The packaging commit removes only `SOLR-10882-TESTING.md`; the head holds three files against the merge-base (changelog, ArrayEvaluator.java, ArrayEvaluatorTest.java). No packaging delta to report for drafting.
- SOLR-9852: receipt head `5fe174425514852da8a949ecc7e6502274d3edb3` (receipt line 5). Live fork tip by `ls-remote`: the same SHA. Match. Original tip `31f58dbe8e6` (line 4) is before packaging (`adf23e3ab65` removes `SOLR-9852-TESTING.md`) and the tidy commit `5fe17442` (formatting of `DatabaseMetaDataImpl.java` only). The head holds three files against the merge-base (changelog, DatabaseMetaDataImpl.java, JdbcTest.java).

## Receipt numbers used

SOLR-10882 (`receipts/SOLR-10882.md`):
- Line 5: gated head. Line 6: merge-base (see discrepancy 2).
- Line 7: base run, ArrayEvaluatorTest tests=7 failures=1; arrayMixedTypesSortTest ClassCastException at ArrayEvaluatorTest.java:148; arrayMixedNumberTypesSortTest passes on base.
- Line 14: focused at head, tests=7 failures=0 errors=0 skipped=0.
- Line 16: scope note (boolean and string mixes and the missing-field path not tested), used for Limits.
- Verification date: not in the receipt, so the Proof has no date (discrepancy 1).

SOLR-9852 (`receipts/SOLR-9852.md`):
- Line 5: gated head. Line 6: merge-base `cabedd1d968`.
- Line 9: re-gate at `5fe174425514`, GATE RUNNER DONE 2026-10-11T03:18:56Z. This is the verification date used (2026-10-11).
- Line 13: proof leg on base, tests=12 skipped=1 failures=1; testDriverMetadata, "getColumns must return a result set".
- Line 14: focused at head, tests=12 skipped=1 failures=0 errors=0. The seed value on that line is a run identifier and is left out.
- Line 7 names testJDBCMethods for the premise leg. Reconciled: testJDBCMethods is a private helper called from testDriverMetadata (JdbcTest.java lines 605 to 606), and the new assertions sit in the helper (lines 691 to 707). The draft names both.
- Line 3 says 2026-10-10 for the first gate; that gate was invalid (tidy drift) and the re-gate date on line 9 is the one used.

## Title source

- SOLR-10882: `changelog/unreleased/SOLR-10882-array-sort-mixed-types.yml` at the gated head. Title: "The array stream evaluator with sort=asc|desc now fails with a clear error when the values mix incomparable types (for example text and numbers) instead of a ClassCastException, and no longer hits a NullPointerException when reporting a null value." The NPE clause has no test (receipt line 16; round report, streaming-expressions-round-1.md, item 44). Suggest narrowing the fragment title to drop the NPE clause, or add a null-value test. Not changed here.
- SOLR-9852: `changelog/unreleased/SOLR-9852-jdbc-getcolumns.yml` at the gated head. Title: "The Solr JDBC driver's DatabaseMetaData.getColumns now lists a collection's columns (from the SQL metadata.COLUMNS table) instead of returning null, and getTypeInfo throws SQLFeatureNotSupportedException instead of UnsupportedOperationException." It already says getColumns is implemented and not that the metadata gap is closed, as the round audit asked.
- The drafts carry no title line, matching the approved template and the other streaming drafts.

## Citations and checks

Each line range was read with `git show <sha>:<path>` and numbered before use.

SOLR-10882:
- ArrayEvaluator.java, merge-base `cabedd1d968`, lines 67 to 77: validation loop; line 75 is `value.toString()`. Checked.
- ArrayEvaluator.java, merge-base, lines 50 to 51: raw `compareTo` comparators. Checked.
- FieldValueEvaluator.java, merge-base, lines 91 to 99: returns null for a missing field at line 98. Checked.
- ArrayEvaluator.java, head `5d7d07a5`, lines 69 to 89: kind check. Checked.
- ArrayEvaluatorTest.java, head, lines 123 to 150: arrayMixedNumberTypesSortTest at 123 to 136, arrayMixedTypesSortTest at 138 to 150, assertion at 148 to 149. Checked.
- Changelog link at head. Checked to exist at that SHA.

SOLR-9852:
- DatabaseMetaDataImpl.java, merge-base, lines 755 to 758 (return null at 758) and 816 to 817 (throw at 817). Checked.
- DatabaseMetaDataImpl.java, head `5fe17442`, lines 755 to 777 (getColumns, quotePattern), 760 to 762 (select labels, 18 names), 833 to 836 (getTypeInfo). Checked.
- JdbcTest.java, head, lines 691 to 707 (new assertions in testJDBCMethods, helper starts at 609). Checked.
- ResultSetImpl.java, merge-base, lines 439 to 449: getObject(label) returns `tuple.get(label)`, null when absent. Checked by reading; not run.
- ConnectionImpl.java, merge-base, lines 73 to 74: one statement created and passed to DatabaseMetaDataImpl. Checked.
- StatementImpl.java, merge-base, lines 55 to 60: executeQueryImpl closes the current result set before the next query. Checked.
- Changelog links at head. Checked to exist at that SHA.

The merge-base links for pre-change code are labelled "merge-base" in the text, as the brief requires.

## Choice decisions

- SOLR-10882: included. Error versus fixed-order sort is a real design choice. The implemented error route carries a behavior change (two non-number classes such as java.util.Date and java.sql.Date now error; the base sorts them, by reading) that a maintainer could reject, so the fixed-order route is live.
- SOLR-9852: included. Camel-case labels (as getTables uses) versus the JDBC names is a real choice. The implemented route makes a lookup by the JDBC name return null (ResultSetImpl lines 439 to 449), and the JDBC-name route is the specified one. The other items (getTypeInfo still throws, the shared statement closes the open result set, no ORDER BY, catalog unused) are Limits, not choices, per pr-formula.md section 4. The statement-per-call alternative for the shared-statement issue is named only as a Limits fact, since getTables already behaves the same way.

## Receipt gaps and discrepancies

1. SOLR-10882 Proof has no verification date. The receipt has no run date. `gates/SOLR-10882.md` (line 5) and `claims/pool-vm2-gate-backlog-10882.md` give 2026-10-11T02:32:06Z, but the brief limits Proof dates to the receipt, so the date is left out. If the receipt gets a date line, the draft can take it.
2. SOLR-10882 merge-base. Receipt line 6, `gates/SOLR-10882.md` and the claim name `e044bf20b405`. Git shows `e044bf20` is an ancestor of `cabedd1d968`, 72 commits older. The branch's first commit `c70aeb33` has parent `cabedd1d968`, and `git merge-base` of the head and `cabedd1d968` is `cabedd1d968`. The proof leg therefore ran production at `e044bf20`. The eval main path, the eval test path and ArrayEvaluator.java are identical between `e044bf20` and `cabedd1d968` (`git diff --stat`), so the proof result holds for these files. The drafts link `cabedd1d968` as the merge-base. The receipt label should be corrected main-side; I did not edit it.
3. SOLR-10882 receipt line 4 names `83fc3dfe` as the live tip, verified before the packaging push. Current ls-remote shows `5d7d07a5`. Consistent with line 5; not a drafting problem.
4. SOLR-10882 changelog title claims the NPE fix, which has no test. Limits says so. The lead may narrow the fragment title.
5. SOLR-10882 boolean-mixed-with-numbers case is covered only by code reading (receipt line 16 names it as untested). The draft says it is checked by reading.
6. SOLR-9852 skipped=1 in JdbcTest. The receipt does not name the skipped test; the draft says "1 skipped" without naming it.
7. Nothing was run. The JDBC specification names, the ResultSetImpl key lookup behavior, and the behavior of any real JDBC client were read or stated from the code, not verified by a run. The JIRA packet is not in this worktree, so "What happens today" for both tickets comes from the code and receipts, not from ticket text.

## Length

- SOLR-10882 draft: 2,955 characters of rendered text (link targets removed), 4,023 with link targets.
- SOLR-9852 draft: 2,788 characters of rendered text, 4,581 with link targets. The guide's roughly 3,500 limit is met on rendered text; the link targets are full blob URLs with 40-character SHAs, as pr-formula.md requires.

## Holds

None for these two drafts. The only missing Proof item is the SOLR-10882 verification date (discrepancy 1).
