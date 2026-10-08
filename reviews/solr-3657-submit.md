# solr-3657-submit

- Branch: origin/solr-3657-submit
- Head: 14edaca577c0 (matches the listed head; fork tip unchanged after fetch 2026-10-08)
- Base: upstream/main at 9d7cc2884e8a (merge-base 97d973814336, 19 commits behind, 4 commits ahead)
- Scope: 4 commits, 3 files. `DocumentBuilder.java` (the copy-field `addField` call is wrapped in `try`; a `SolrException` is rethrown with its code and a `copyField destination '<dest>': ` prefix; any other `RuntimeException` becomes `BAD_REQUEST` with the same prefix), `DocumentBuilderTest.java` (vector4 to vector5 expectation updated for the prefix and one more cause level; new test `copyField_unparseableValueForNumericDestination_shouldThrowException`), changelog `SOLR-3657-copyfield-error-names-destination.yml` (`type: changed`). The two test-only commits after the round-28 inspected head are `47b47b1a760` and `14edaca577c`.
- Verdict: Nearly (unchanged from the bulk verdict). The code-level issues in the bulk review are resolved at this head or are cosmetic. The fail-before proof is not established, because nothing is run in this round; the Linux gate supplies it.
- Reviewer: claude-haiku-5-5 (Claude Code), round 36 Group B review, 2026-10-08

Nothing here was compiled, formatted, or run. No Gradle, no `drain`, no test runs. The fail-before claim below is a reading, not a run. Every claim rests on reading the diff and the code at the listed head.

## Delta check against the bulk review

Bulk review `research/branch-reviews/round-28/SOLR-3657-review.md` (Nearly, TASK-03) inspected `47b47b1a760`, one commit behind the listed head. The listed head `14edaca577c0` is one further test-only commit. So this review covers a head the bulk pass did not see. Dispositions:

- Bulk LOW 1 (the new test is narrow and order dependent; it passes only because `range_facet_l` is the first `id` destination; suggest asserting both field names and the code): **dropped at this head.** Commit `14edaca577c` made the test derive the first destination from the schema (`DocumentBuilderTest.java`, `schema.getCopyFieldsList("id").get(0)`) and asserts both `copyField destination '<first>': ` and `Error adding field 'id'='not-a-number'`, plus `400`. The order dependence on names is gone. See finding 2 for what it still assumes.
- Bulk LOW 2 (the destination prefix is redundant where the inner message already names the destination; the vector test reads `copyField destination 'vector5': Error while creating field 'vector5{…}'`): **confirmed, cosmetic.** See finding 3.
- Bulk LOW 3 (the cause chain is one level deeper; the vector test needed `getCause().getCause().getCause()`): **confirmed.** The vector4 test was updated (`DocumentBuilderTest.java:448`). The bulk review did not search other consumers; this review did. See finding 4.
- Bulk LOW 4 (two catch blocks, no behavioural gain from the second): **changed.** The second block does change behavior: it adds the destination prefix to every non-`SolrException` `RuntimeException`, including the numeric parse failure the new test pins. Its HTTP status (`BAD_REQUEST`) is the same as the outer `catch (Exception)`. So the two blocks are not redundant; they are redundant only in status. See finding 3.
- Bulk note on `SolrException` metadata (not carried to the new exception): **confirmed, consistent with the base.** The outer wrapper behaves the same way. See finding 5.

## Findings (ranked)

1. **Checked, no defect. Two places a copy-field message change could break existing tests do not.** (a) `DocumentBuilderTest.java:425` pins the exact message for `vector3` (`msg=The copy field destination must be a DenseVectorField: vector_f_p`). That message is thrown at `DocumentBuilder.java:369`, which is in the copy-field loop before the new `try` (`:390`). The wrapper does not see it, so the expectation still holds. (b) `DenseVectorFieldTest.java:712-790` asserts `getCause().getCause()` on `vector_byte_encoding`, which is a direct field, not a copy destination. Its errors come from the main loop, so the depth is unchanged. The only copy-field depth assertion is the one the branch updated.

2. **LOW, verified (comment overstates). The test is independent of copy-field names and order, but not of the first destination's type.** The assertion requires the first `id` destination to fail with a `NumberFormatException`-style error. The test comment says the value "fails in every destination of 'id' in this schema" and that reordering `schema.xml` "does not break this test". Reordering alone does not break it. Adding a non-numeric `id` copy field ahead of the numeric ones would. The four `id` destinations (`solr/core/src/test-files/solr/collection1/conf/schema.xml:890,891,895,896`: `range_facet_l`, `id_i1`, `range_facet_l_dv`, `range_facet_i_dv`) were not checked for their field types; the names suggest numeric types. Fix the comment to say the first destination must be numeric, or assert the field type from the schema.

3. **LOW, verified (cosmetic, and the second catch does real work). The prefix repeats the destination in the vector message, and the second catch adds the prefix to non-Solr exceptions.** `DocumentBuilderTest.java:446` now reads `copyField destination 'vector5': Error while creating field 'vector5{…}'`, which names the destination twice in effect. The second catch (`DocumentBuilder.java`, the `catch (RuntimeException ex)` in the copy-field block, `:401-405`) produces the prefix the new numeric test checks. Its status matches the outer `catch (Exception)` at the base, so it adds no new masking.

4. **LOW, verified. Cause depth changed for copy-field errors.** One more level is now in the chain for copy-field failures. The only in-repo test that reads it is `DocumentBuilderTest.java:448`, updated in this branch. No other test reads copy-field cause depth (grep of `solr/core/src/test` for `getCause().getCause()` with copy, vector, DocumentBuilder, or dense terms found only the three `DenseVectorFieldTest` direct-field assertions, see finding 1).

5. **LOW, verified (consistent with base). `SolrException` metadata is not carried.** The new `SolrException` for a copy-field failure is built with `(code, message, cause)`. Any metadata on the original `SolrException` is dropped. The base outer wrapper does the same, so this is not a new loss. Nothing in the repo reads it for field errors.

6. **Checked, acceptable. Status codes are preserved.** The first catch keeps `SolrException.ErrorCode.getErrorCode(ex.code())`. The second maps to `BAD_REQUEST`, as the base does for the same exceptions.

## Owner calls (not decided here)

None needed. The changelog's statement ("names the destination field as well as the source field") matches the code.

## Proposed fixes (not applied; the owner decides)

- Finding 2: change the test comment to say the first `id` destination must fail, and optionally assert its field type against the schema.
- Finding 3: optional. If the duplicated destination text matters, drop the prefix when the inner message already names the destination (the vector case). The numeric case needs the prefix.
- Finding 5: optional, and consistent with the base; no change proposed.

## Group C note (handoff reference)

The Group B row for this branch says the delta aspect "is covered in group C notes of the handoff document." The round-36 handoff's Group C table has no SOLR-3657 row, and no SOLR-3657 note was found in `inventory/`. The closest workspace note is `research/pipeline/HANDOFF-task3-skiplist.md:342-345`. That entry records the earlier head `0a42e3c63d11` and says "updated cause-chain assertion; no numeric-destination regression". It does not cover `14edaca577c0` or the order-independence change. Not duplicated here. The Linux side should confirm whether the Group C note exists and, if so, which head it covers.

## Not checked

- Nothing compiled, formatted, or run. The fail-before claim (the new test fails on the old message, because the old message has no `copyField destination` prefix) rests on reading the assertion against the old code.
- The field types of the four `id` copy destinations in the test schema (finding 2).
- `FieldType.createFields` and `addField` internals for numeric destinations beyond the `DocumentBuilder` wrapper.
- The changelog's authorship and ICLA wording were checked against the convention, not against an external record.
