# Query parsing and schema analysis draft fidelity, slice s5 (A5)

Assignment: `assignments/pool-draft-fidelity-configsets-query-schema.md`. Claim: `claims/pool-draft-fidelity-configsets-query-schema.md`, slice A5 (query parsing SOLR-8977, 9048, 9149; schema SOLR-14199, 15357). Worktree `wt/pr-prepare-suggester` at d627304e96b. This report is read-only; no draft, receipt, claim, assignment, or branch was changed.

Drafts: `pr-drafts/query-parsing/SOLR-8977.md`, `SOLR-9048.md`, `SOLR-9149.md`; `pr-drafts/schema-analysis/SOLR-14199.md`, `SOLR-15357.md`.

Receipts: `receipts/SOLR-8977.md`, `SOLR-9048.md`, `SOLR-9149.md`, `SOLR-14199.md`, `SOLR-15357.md` (all present).

Round reports: `reports/query-parsing-round-1.md` with parts q1, q3 and q7 (the only parts that mention 8977, 9048 or 9149); `reports/schema-analysis-round-1.md` with parts s1 and s2 (the only parts that mention 14199 or 15357). Parts q2, q4, q5, q6, q8, s3 and s4 do not mention these keys.

Answers material: none. A grep of `material/` for 8977, 9048, 9149, 14199 and 15357 returned no hits.

Heads, from `git ls-remote origin refs/heads/<branch>` on the fork:

- solr-8977-submit: 395b24964fd05f2908c0340ef79c1ee4273775a8 (matches the draft)
- solr-9048-submit: b145018563cbc2ddd29e43a0fa8619545415b8c6 (matches)
- solr-9149-submit: 151dfed119ee0a6f5a922f3a914665e7b3281427 (matches)
- solr-14199-submit: e743c90da79a155fd6e6161233af5d924dc9dad1 (matches)
- solr-15357-submit: d717899b8736309bd72f54b11230f198ae226e7d (matches)

Base commits for pre-change citations (merge-base with the local `upstream/main`, 3f5d4c5bf8ac): 14c7aac0d151402b00259e2fb9bf5eed7049ec5d for 8977, 9048 and 9149; b5c71bc5573c4e31b4cee5a7965d73587fc0ae58 for 14199 and 15357. Both resolve. No upstream commit names these five keys.

## Verdicts

| Draft | Head checked | Verdict |
|---|---|---|
| SOLR-8977 | 395b24964fd (ls-remote matches) | DRIFT (3 items) |
| SOLR-9048 | b145018563c (ls-remote matches) | DRIFT (2 items) |
| SOLR-9149 | 151dfed119e (ls-remote matches) | DRIFT (1 item) |
| SOLR-14199 | e743c90da79 (ls-remote matches) | DRIFT (3 items) |
| SOLR-15357 | d717899b873 (ls-remote matches) | DRIFT (4 items) |

## SOLR-8977

Verdict: DRIFT (3 items).

1. Draft says (changelog fragment `changelog/unreleased/SOLR-8977-graph-negative-traversal-filter.yml`, lines 2-5): "so it no longer matches no documents on cores whose luceneMatchVersion disables the query parser's own pure negative handling."
   - Evidence: the draft's own text says the walk "returns only the start document". `GraphQueryParser.java` at head L60 defaults `returnRoot` to true (q3 finding 2). `receipts/SOLR-8977.md` line 7 records `expected:<2> but was:<1>`.
   - Replacement (changelog title, on the branch, so the head moves when it lands): "The graph query parser now makes a pure negative traversalFilter such as traversalFilter='-text:foo' queryable itself. On cores whose luceneMatchVersion is below 10.2.0, the traversal no longer stops at the starting documents."

2. Draft says: "The query parser repairs a pure negative query only when the core's luceneMatchVersion is 10.2.0 or later ([QParser.java](https://github.com/nick-boss-tech/solr/blob/395b24964fd05f2908c0340ef79c1ee4273775a8/solr/core/src/java/org/apache/solr/search/QParser.java#L114-L115)). The graph query adds the traversal filter as a MUST clause on each hop ([GraphQuery.java](https://github.com/nick-boss-tech/solr/blob/395b24964fd05f2908c0340ef79c1ee4273775a8/solr/core/src/java/org/apache/solr/search/join/GraphQuery.java#L227-L228)), so a filter that matches nothing ends the walk."
   - Evidence: the branch touches only `GraphQueryParser.java`, `GraphQueryTest.java` and the changelog (`git diff --stat` against 14c7aac0d15). QParser.java L114-115 and GraphQuery.java L227-228 read the same at base and head. They are pre-change symptom code, so the formula requires the base link and the words "at the base commit".
   - Replacement: "The query parser repairs a pure negative query only when the core's luceneMatchVersion is 10.2.0 or later ([QParser.java](https://github.com/nick-boss-tech/solr/blob/14c7aac0d151402b00259e2fb9bf5eed7049ec5d/solr/core/src/java/org/apache/solr/search/QParser.java#L114-L115), at the base commit). The graph query adds the traversal filter as a MUST clause on each hop ([GraphQuery.java](https://github.com/nick-boss-tech/solr/blob/14c7aac0d151402b00259e2fb9bf5eed7049ec5d/solr/core/src/java/org/apache/solr/search/join/GraphQuery.java#L227-L228), at the base commit), so a filter that matches nothing ends the walk."

3. Draft says (Limits): "The graph parser does not repair the root ([GraphQueryParser.java](https://github.com/nick-boss-tech/solr/blob/395b24964fd05f2908c0340ef79c1ee4273775a8/solr/core/src/java/org/apache/solr/search/join/GraphQueryParser.java#L41))."
   - Evidence: the root line is not changed by the branch. It is L41 at head and L40 at base (the branch adds an import above it). q3 finding 6 covers the gap.
   - Replacement: "The graph parser does not repair the root ([GraphQueryParser.java](https://github.com/nick-boss-tech/solr/blob/14c7aac0d151402b00259e2fb9bf5eed7049ec5d/solr/core/src/java/org/apache/solr/search/join/GraphQueryParser.java#L40), at the base commit)."

Verified, no DRIFT: the head SHA; the Proof counts (GraphQueryTest 3, TestScoreJoinQPScore 13 with 1 skip, TestScoreJoinQPNoScore 4, BJQParserTest 18; total 38) against the receipt; the date 2026-10-05; the test name and the test link (`GraphQueryTest.java` L157-188 at head, which holds the new test); the code link `GraphQueryParser.java` L45-48 at head (the `makeQueryable` block); the changelog link and that the file exists at head. The Choice section is a real choice with a live alternative (q3 owner decision 2). The Limits match q3 findings 5-7 and the round roll-up.

Optional notes, not blocking:
- The round roll-up (`reports/query-parsing-round-1.md`, Not done) says the 8977 version claim needs a Lucene 9.x and 10.x recheck before any draft uses it. q3 finding 7 read the Solr branch sources, but no Lucene check is recorded.
- "pure negative", "MUST clause" and "auto-fix" are compressed jargon.

## SOLR-9048

Verdict: DRIFT (2 items).

1. Draft says (changelog fragment `changelog/unreleased/SOLR-9048-blockjoin-empty-subquery.yml`, lines 2-3): "The parent and child block join query parsers no longer fail with a NullPointerException when the nested query parser produces no query, for example when all its terms are stop words."
   - Evidence: `FiltersQParser` is the base of the `{!filters}` parser (q3 finding 3). The branch changes `FiltersQParser.java` (L62-67 at head). The draft's Behavior change says `{!filters}` changes too. The title does not name it.
   - Replacement (changelog title, on the branch): "The parent, child and filters query parsers no longer fail with a NullPointerException when a nested query produces no query, for example when all its terms are stop words. The nested query then adds no constraint."

2. Draft says (Limits): "Both pass their query straight to the parent filter ([BlockJoinParentQParser.java L293](https://github.com/nick-boss-tech/solr/blob/b145018563cbc2ddd29e43a0fa8619545415b8c6/solr/core/src/java/org/apache/solr/search/join/BlockJoinParentQParser.java#L293))."
   - Evidence: `BlockJoinParentQParser.java` is not changed by the branch, so L293 is pre-change code that describes the gap that remains. The text is the same at base 14c7aac0d15, line 293. The formula requires the base link and the words "at the base commit".
   - Replacement: "Both pass their query straight to the parent filter ([BlockJoinParentQParser.java L293](https://github.com/nick-boss-tech/solr/blob/14c7aac0d151402b00259e2fb9bf5eed7049ec5d/solr/core/src/java/org/apache/solr/search/join/BlockJoinParentQParser.java#L293), at the base commit)."

Verified, no DRIFT: the head SHA; the Proof counts (BJQParserTest 19, BlockJoinNestedVectorsQParserTest 17, TestMmBoolQParserPlugin 10, TestFiltersQueryCaching 2; total 48) against the receipt; the NPE message and the one base failure (receipt line 7); the date 2026-10-05; the links `FiltersQParser.java` L62-67 and `BJQParserTest.java` L169-185 at head, which hold the code and test the text describes; `BlockJoinParentQParser.java` L283-284 at head (the `parseWithLegacyParam` path that the `which=` plus `v=` form uses). The Choice section gives a live alternative (q3 owner decision 4).

Optional notes, not blocking:
- The Limits plan sentence ("The plan is a separate follow-up PR for those two forms, submitted after this one is reviewed") states a plan that q3 owner decision 6 asks the owner to confirm. The formula's wording is "A follow-up ticket and PR can cover those two forms, on request." Use that wording unless the owner confirms the plan.
- "The `which` and `of` forms" can be read as the example's `which=` clause, which the fix covers. Writing "the `which=` and `of=` parameters" avoids the confusion.
- `BlockJoinParentQParser.java` L283-284 links the head. The text is the same at base and describes existing behavior, not the symptom, so the head link is acceptable.

## SOLR-9149

Verdict: DRIFT (1 item).

1. Draft says (Limits): "eDismax overrides the same slop method in [ExtendedDismaxQParser.java](https://github.com/nick-boss-tech/solr/blob/151dfed119ee0a6f5a922f3a914665e7b3281427/solr/core/src/java/org/apache/solr/search/ExtendedDismaxQParser.java#L1067-L1074) and does not call the base method."
   - Evidence: `ExtendedDismaxQParser.java` is not changed by the branch. L1067-1074 is the eDismax override that keeps the gap, so it is pre-change code. It reads the same at base 14c7aac0d15 (line 1067 checked). q1 finding 12.
   - Replacement: "eDismax overrides the same slop method in [ExtendedDismaxQParser.java](https://github.com/nick-boss-tech/solr/blob/14c7aac0d151402b00259e2fb9bf5eed7049ec5d/solr/core/src/java/org/apache/solr/search/ExtendedDismaxQParser.java#L1067-L1074), at the base commit, and does not call the base method."

Verified, no DRIFT: the head SHA; the Proof (the new assertion at `TestSolrQueryParser.java` L298-301 at head, inside `testNestedQueryModifiers`; TestSolrQueryParser 37 of 37 and 48 focused tests against the receipt; the date 2026-10-05); the code link `SolrQueryParserBase.java` L545-547 at head (the reset is L547); the changelog link and title. No Choice section, which is correct for a bug fix with no live alternative (q1).

Optional notes, not blocking:
- "TestSolrQueryParser 37 of 37" matches the receipt, but the head source has 36 test methods (q1 finding 6, the same +1 pattern that holds SOLR-14199). Confirm against the JUnit XML before posting. q1 rates the draft "draftable as is".
- Branch commit subjects carry handoff wording (q1 finding 2). They are not in the draft, but a reviewer sees them on the PR.
- "nested-query marker" and "slop" are compressed jargon.

## SOLR-14199

Verdict: DRIFT (3 items).

1. Draft says: "Changelog: `changelog/unreleased/SOLR-14199.yml`"
   - Evidence: the formula requires the changelog line to be a link to the fragment at the head SHA. The file exists at e743c90da79 with the title "Reject omitNorms=false on point field types and fields instead of silently ignoring it".
   - Replacement: "Changelog: [changelog/unreleased/SOLR-14199.yml](https://github.com/nick-boss-tech/solr/blob/e743c90da79a155fd6e6161233af5d924dc9dad1/changelog/unreleased/SOLR-14199.yml)"

2. Draft says (public text, three bracketed internal markers):
   - Proof: "`TestSolrQueryParser` [CONFIRM COUNT BEFORE POSTING: the gate receipt says 37 of 37, but the source has 36 test methods]. The numeric point type is randomized..."
   - Proof: "[HOLD: `SchemaVersionSpecificBehaviorTest` was not in this run. Fix its expectation and run it before posting.]"
   - Limits: "- [OWNER: add the two Limits lines from the ledger review. That file is not on disk.]"
   - Evidence: check 7 (gate, receipt, ledger, owner and hold vocabulary). The 37 count is the receipt's. s1 finding 4 says the head source has 36 test methods, and the JUnit XML is not on disk, so the count cannot be confirmed here. s1 finding 1 says `SchemaVersionSpecificBehaviorTest` probably fails at head (the branch does not change that test).
   - Replacement: in the Proof, delete the sentence "`TestSolrQueryParser` [CONFIRM COUNT BEFORE POSTING: ...]." Keep the next sentence as it stands: "The numeric point type is randomized in tests, so the run covers one of the point or trie configurations." Delete the HOLD line and the OWNER line in full. Add the `TestSolrQueryParser` count back only after the JUnit XML confirms it. Add the two Limits lines only when the owner supplies them; the ledger file is not on disk.

3. Draft says (What this change does): "Solr now rejects `omitNorms="false"` on point types and on indexed point fields." The branch changelog title says: "Reject omitNorms=false on point field types and fields instead of silently ignoring it".
   - Evidence: `PointField.checkSchemaField` (head L101) rejects only indexed fields. A non-indexed field with `omitNorms="false"` already fails to load on base through `SchemaField` (s1 finding 3). So "fields" and "silently ignoring it" overstate the change for non-indexed fields.
   - Replacement (changelog title, on the branch): "Reject omitNorms=false on point field types and indexed point fields instead of silently ignoring it"

Verified, no DRIFT: the head SHA; the Proof counts (BadIndexSchemaTest 26 of 26; PrimitiveFieldTypeTest 2 of 2) against the receipt; the one base failure, `testSevereErrorsForPointFieldOmitNorms`, which exists at head (receipt line 7); the date 2026-10-05; the code described: `PointField.init` (L78-93, the rejection at L83-88 and the OMIT_NORMS set at L92) and `PointField.checkSchemaField` (L96-110). The Choice section is a real choice (s1 owner decision 2) with the alternative stated. The Limits match s1 findings 3 and 11 and the stored-only existence change, which the draft states in What this change does.

Optional notes, not blocking:
- The stored-only point existence change is written as option (a) (keep and state it). s1 owner decision 1 is still open, so the owner should confirm (a) before posting.
- The Proof, Choice and Limits sections open without a bold one-line summary, which the formula's presentation rule asks for. Suggested openers: Proof, "**The new test fails on the base code and passes with this change.**"; Limits, "**Field checks cover indexed fields only. The Schema API path and stored-only point fields have no test.**"
- File names are not links. The formula asks for file links at the head SHA. Suggested links: [PointField.java](https://github.com/nick-boss-tech/solr/blob/e743c90da79a155fd6e6161233af5d924dc9dad1/solr/core/src/java/org/apache/solr/schema/PointField.java#L78-L110) for the two checks; [major-changes-in-solr-11.adoc](https://github.com/nick-boss-tech/solr/blob/e743c90da79a155fd6e6161233af5d924dc9dad1/solr/solr-ref-guide/modules/upgrade-notes/pages/major-changes-in-solr-11.adoc#L30-L35); [schema12.xml](https://github.com/nick-boss-tech/solr/blob/e743c90da79a155fd6e6161233af5d924dc9dad1/solr/core/src/test-files/solr/collection1/conf/schema12.xml).
- The branch's upgrade note (`major-changes-in-solr-11.adoc` L33) says "a field", which s1 finding 3 flags. The draft is accurate. The branch needs the s1 replacement, which moves the head.

## SOLR-15357

Verdict: DRIFT (4 items).

1. Draft says: line 1, an HTML comment beginning "<!-- INTERNAL. Remove this block before posting. ... HOLD. Before posting: (1) fix the sub-field names in CopyFieldSubFieldsTest (report FIX 1) ..."; and in the Proof, "Gate run at d717899b8736309bd72f54b11230f198ae226e7d, recorded 2026-10-07."
   - Evidence: check 7. The comment names report FIX items and owner decisions. "Gate run" is process vocabulary. The Proof should state the run plainly.
   - Replacement: delete line 1 in full. In the Proof, replace "Gate run at d717899b8736309bd72f54b11230f198ae226e7d, recorded 2026-10-07." with "Verified 2026-10-07 at head d717899b8736309bd72f54b11230f198ae226e7d." (Item 2 replaces the rest of the paragraph.)

2. Draft says: "**CopyFieldSubFieldsTest passes 4 of 4 at this head, but it shows no failure on the base code.**" and later "None of the four shows a failure on base."
   - Evidence: `receipts/SOLR-15357.md` line 7 classifies the proof as "classified, not a base-failure proof", inconclusive by construction. Two of the four methods call `FieldType.getSubFields`, which base lacks, so the class does not compile on base and no base run exists (s2 finding 3). "Shows no failure on the base code" implies a base result that does not exist.
   - Replacement (replaces the bold line and the paragraph under it):
     "**CopyFieldSubFieldsTest passes 4 of 4 at this head. The proof is inconclusive on the base code by construction.**"
     "Verified 2026-10-07 at head d717899b8736309bd72f54b11230f198ae226e7d. Two of the four methods call `FieldType.getSubFields`, which base does not have, so the class does not compile on base and no base run exists. The other two read real-time get and getInputDocument output, and they cannot run on base either, because they share the class."

3. Draft says (changelog title, branch `changelog/unreleased/SOLR-15357.yml` line 1): "The sub-fields of CurrencyFieldType, PointType and BBoxField copyField destinations are now recorded as copyField targets, so real-time get no longer returns them in documents"
   - Evidence: s2 finding 2. Currency sub-fields carry no docValues on base or on this branch (CurrencyFieldType L182 and L184 use `createField`), so real-time get has nothing extra to return for currency here. The draft's Limits say so: "Currency sub-fields have no docValues on this branch, so currency output does not change here." The title contradicts the draft.
   - Replacement (changelog title, on the branch): "The sub-fields of CurrencyFieldType, PointType and BBoxField copyField destinations are now recorded as copyField targets"

4. Draft says (Limits): "Real-time get decorates docValues fields without the copy target check ([RealTimeGetComponent.java L356-L357](https://github.com/nick-boss-tech/solr/blob/d717899b8736309bd72f54b11230f198ae226e7d/solr/core/src/java/org/apache/solr/handler/component/RealTimeGetComponent.java#L356-L357)). This change does not touch that path."
   - Evidence: the branch does not touch `RealTimeGetComponent.java`, and L356-357 read the same at base b5c71bc5573. This is the pre-change path that still returns the sub-fields, so the formula requires the base link and the words "at the base commit".
   - Replacement: "Real-time get decorates docValues fields without the copy target check ([RealTimeGetComponent.java L356-L357](https://github.com/nick-boss-tech/solr/blob/b5c71bc5573c4e31b4cee5a7965d73587fc0ae58/solr/core/src/java/org/apache/solr/handler/component/RealTimeGetComponent.java#L356-L357), at the base commit). This change does not touch that path."

Verified, no DRIFT: the head SHA; the receipt count 4 of 4 (four `@Test` methods in `CopyFieldSubFieldsTest.java` at head); the date 2026-10-07 (receipt, round 35); the produced-code links at head, each holding the code the text describes: `FieldType.java` L178-180 (`getSubFields` hook), `CurrencyFieldType.java` L206-219, `AbstractSubTypeFieldType.java` L143-155 (PointType), `BBoxField.java` L131-148, `IndexSchema.java` L1074-1080, `ManagedIndexSchema.java` L1037-1046 and L503 (the delete refusal, which says "referred to by at least one copy field directive"); the changelog link and that the file exists at head. The Choice section gives a real alternative (s2 owner decision 4, option b) and states its cost (the delete check). The Limits match s2 findings 4, 8 and 11 and owner decision 2.

Optional notes, not blocking:
- The head will move after the test-name fix (s2 finding 1: `CopyFieldSubFieldsTest.java` L122, L127-128, L156-157). Until then the two `/get` asserts cannot fail, so "passes 4 of 4" is accurate but weak. Update the Proof, the links and the head after the fix.
- "What happens today" says the filters keep the sub-fields. That is not traced for stored currency sub-fields on base. s2 finding 2 says currency output does not change here. Check before posting.
- "copyField target bookkeeping", "docValues" and "materialization" are compressed. A plain gloss would help.

## Not done

- No build, test, Gradle run, `gh` write, fetch, or Jira read. Heads were checked with `ls-remote` only.
- The JUnit XML, gate logs and premise logs named in the receipts are not on disk. Every count is from the receipts. The two `TestSolrQueryParser` counts (37, in 9149 and 14199) could not be checked against the 36 test methods in source.
- The ledger file for 14199 (`goal files/reviews-2026-10-05-round27-inventory-queue/14199.md`) is not under the Solr-issues root. Its two Limits lines are unknown.
- Lucene 9.x and 10.x were not rechecked; the roll-up asks for that for the 8977 version claim. Solr branch sources were read with `git show` at the base and head commits.
- Behavior claims (eDismax, the `/get` paths, the `{!filters}` NPE) were checked by reading code, not by running. Live Jira was not read, so the ticket text behind each "What happens today" was not checked.
- Commit subjects on the fork branches were not checked, except where the round reports name them.
- Parts q2, q4, q5, q6, q8, s3 and s4 were not read; a grep found no mention of these five keys in them.
