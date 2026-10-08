# solr-8088-submit

- Branch: origin/solr-8088-submit
- Head: 2398c9bea085 (reviewed and unchanged; no patch)
- Base: upstream/main (merge-base cabedd1d968, 16 commits behind)
- Scope: 5 files, +104. `solr/core/.../search/grouping/distributed/command/SearchGroupsFieldCommand.java` (new static `checkGroupable`, called from `Builder.build()`, rejects a non-numeric multiValued group field with BAD_REQUEST), `TopGroupsFieldCommand.java` (same check in its builder), new `SearchGroupsFieldCommandTest.java` (builder-level, two cases), changelog fragment (type `fixed`), `SOLR-8088-TESTING.md` (author's unrun note, left in place).
- Verdict: Nearly (at 2398c9bea085; the check is consistent with the ticket's "clear 400 up front" scope)
- Reviewer: review-agent A1, 2026-10-08

## Findings (ranked)

MEDIUM (hypothesis, not traced): the 400 may not reach the client as a 400 on a distributed request. The check runs in the shard-side `SearchGroupsFieldCommand.Builder.build()` and in the coordinator-side `TopGroupsFieldCommand` builder. A shard's BAD_REQUEST may be re-wrapped by the coordinator's shard-error handling, so the HTTP status a user sees could differ from the "clear 400" the changelog promises. The only test is builder-level (`SearchGroupsFieldCommandTest`), so the end-to-end status is not covered. The owner should decide whether an end-to-end check is wanted before this ships. Not patched, because it needs a distributed test.

MEDIUM (hypothesis, not traced): the rule rejects by schema flag (`multiValued`), not by the data in the segments. The author's premise is that every multiValued non-numeric docValues field is typed SORTED_SET and fails in `TermGroupSelector` ("expected=SORTED"). That fits the error text in the JIRA, but I did not confirm Lucene's typing for the multiValued case with at most one value per document, nor the uninverted (non-docValues) multiValued path. If either can group today, this rejects a working case. Not patched, because the scope is a design choice.

verified (checked against the code and tests):
- `build()` requires `field`, `groupSort`, and `topNGroups`, and the test sets all three (`SearchGroupsFieldCommand.java` lines 69-76).
- Test schema: `cat` is `multiValued="true"` and `id` is `multiValued="false"` (`schema.xml` lines 540 and 631), so both new test cases hit the intended branches.
- The existing distributed grouping tests do not group on a multiValued field. `TestDistributedGrouping` groups on `a_idv`, `a_i1`, `a_n_tdt1`, `a_s_dvo`, and `a_b_dvo`. The `*_s_dvo` and `*_b_dvo` dynamic fields are declared `multiValued="false"` (`schema.xml` lines 803 and 805). `DocValuesNotIndexedTest` groups only on the single-valued `fieldsToTestGroupSortFirst` and `...Last` lists. Its multiValued fields are used only for sort and facet tests.
- Numeric fields keep the ValueSource path (`getNumberType() != null` is excluded), so `tdate_a` and the `i` fields are unaffected.
- `checkGroupable` is package-private static, and `TopGroupsFieldCommand` is in the same package, so the call compiles by reading.

LOW: the new error text is a long concatenation. Its wording is fine. The message says "group on a single valued (e.g. copyField) field instead", which is accurate advice.

LOW: the javadoc on `checkGroupable` describes the Lucene error. That explains why the check exists, not what the change is. It is within AGENTS.md's rule.

## Not checked
- Nothing compiled, formatted, or run. No Gradle, no tests.
- The HTTP status for a distributed request (MEDIUM 1).
- Lucene's SORTED_SET typing for multiValued fields with at most one value per document, and the uninverted path (MEDIUM 2).
- The changelog `type: fixed` for a change that is really a validation error. Style only.
