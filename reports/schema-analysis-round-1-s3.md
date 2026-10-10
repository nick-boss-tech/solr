# Round 1 part s3 (SOLR-10131, SOLR-15712, SOLR-15945): SOLR-15945 draftable with two branch-text FIXes; SOLR-10131 and SOLR-15712 held

Verdicts: 10131 HOLD (the ticket's own symptom is already fixed on base). 15712 HOLD (possible duplicate of SOLR-15777, and a compatibility FIX is needed). 15945 DRAFTABLE (draft at `pr-drafts/schema-analysis/SOLR-15945.md`, written against head `adb0fd450c06c07f49ccd187c3d810410a76c5b3`, after two FIXes to the branch text).

Heads read (local refs, no fetch): `origin/solr-10131-submit` fd495167cd0, `origin/solr-15712-submit` cb988a2ff4c, `origin/solr-15945-submit` adb0fd450c0. Merge bases: 10131 and 15712 at `cabedd1d968` (upstream/main at the time); 15945 at `14c7aac0d15` (66 commits behind upstream/main).

## Findings

**F1. FIX (SOLR-10131, scope). Ticket symptom is already fixed on base.**
- Where: Jira packet `research/jira-context/SOLR-10131.json` (Summary field); branch change `solr/core/src/java/org/apache/solr/schema/UUIDField.java` lines 79-84 at head fd495167cd0.
- Evidence: the Jira summary is "Solr returns 500 instead of 400 from update with bad value for UUID". On base, `FieldType.createField` rethrows a `SolrException` unchanged (`cabedd1d968`, `solr/core/src/java/org/apache/solr/schema/FieldType.java` lines 306-307: `catch (SolrException se) { throw se; }`). That line was added by `c1cfaec00d3` (2023-11-25, SOLR-10653, "to avoid error code erasure"). Base `UUIDField.java` lines 79-85 throw BAD_REQUEST for the ticket's 7-character value `1249948`. The branch fixes a different gap: base accepts 36-character values that contain non-hex characters. The inventory row title ("UUIDField accepts non-hex characters in 36-char values") follows the branch, not the Jira.
- Replacement: none until the owner picks the scope (Owner decision 1). If the PR goes ahead under SOLR-10131, its description must say: "The 500 in the ticket title no longer occurs; SOLR-10653 returns 400 for it. This change covers a separate gap: 36-character values with non-hexadecimal characters were accepted before this change."

**F2. NOTE (SOLR-10131, query paths change too).**
- Where: `UUIDField.java` lines 79-84 (head) use the same `toInternal` that query code calls. Base call sites: `FieldType.java` line 464 (`readableToIndexed`), lines 1044-1045 (range bounds), `handler/component/QueryComponent.java` line 1609 (ids lookup), `search/facet/FacetFieldProcessorByEnumTermsStream.java` line 188 (facet prefix). All at `upstream/main`.
- Evidence: base already returns 400 for other wrong-length values on these paths. With this branch, a query, range, facet prefix, or id lookup with a 36-character non-hex value gets 400 instead of zero hits. This is consistent with the length rule, but it is a behavior change.
- Replacement (if the change proceeds): add to the changelog title or the PR description: "Queries, range bounds, facet prefixes, and id lookups that use a 36-character value with non-hexadecimal characters on a UUID field now get a 400 error."

**F3. NOTE (SOLR-10131, comment wording).**
- Where: `UUIDField.java` line 100 (head fd495167cd0).
- Evidence: the comment says `digit()` accepts non-ASCII digits. The rejection comes from the `c > 'f'` test on line 99, not from `digit()`.
- Replacement: `        // Character.digit also accepts full width digits; the c > 'f' test rejects them`

**F4. FIX (SOLR-15712, compatibility). The core guard fails hard for every luceneMatchVersion.**
- Where: `solr/core/src/java/org/apache/solr/schema/CollationField.java` lines 94 and 103 (head cb988a2ff4c).
- Evidence: the ICU sibling warns for schemas below Lucene 9.0 and fails from 9.0 on. See `solr/modules/analysis-extras/src/java/org/apache/solr/schema/ICUCollationField.java` line 97 (`UDVAS_FORBIDDEN_AS_OF = Version.LUCENE_9_0_0`) and lines 125-126 (`failHardOnUdvas = schema.getDefaultLuceneMatchVersion().onOrAfter(UDVAS_FORBIDDEN_AS_OF)`). Its test `TestICUCollationFieldUDVAS.java` uses a warn ceiling of 8.12.0. With the branch as written, an old schema that sets `useDocValuesAsStored="true"` on a CollationField stops loading.
- Replacement (exact code, mirrors the ICU precedent):
  - Add `import org.apache.lucene.util.Version;` after `import org.apache.lucene.util.ResourceLoader;`.
  - Keep the comment at lines 85-86 and `UDVAS_MESSAGE` at lines 87-88. After line 86, add:
    ```java
      static final Version UDVAS_FORBIDDEN_AS_OF = Version.LUCENE_9_0_0;
      private boolean failHardOnUdvas;
    ```
  - In `checkSchemaField` (line 94) use `assertWarnOrFail(UDVAS_MESSAGE, false, failHardOnUdvas);`.
  - In `init` (line 100), first line: `failHardOnUdvas = schema.getDefaultLuceneMatchVersion().onOrAfter(UDVAS_FORBIDDEN_AS_OF);`, and line 103 becomes `assertWarnOrFail(UDVAS_MESSAGE, false, failHardOnUdvas);`.
  - Add a test in the style of `TestICUCollationFieldUDVAS` (warn below 9.0, fail at 9.0).
- If the owner keeps the hard fail, the changelog must say so (see F8).

**F5. FIX (SOLR-15712, premise and duplicate). The ticket's reporter appears to use the ICU type, which is already guarded.**
- Where: Jira packet `research/jira-context/SOLR-15712.json` (comments dated 2022-02-02 and 2022-02-03); `ICUCollationField.java` lines 97-131 at `upstream/main`.
- Evidence: a commenter names `ICUCollationField` and says the report is "almost certainly a duplicate of SOLR-15777". The SOLR-15777 guard is already in base for the ICU type. This branch extends the same rule to the core `CollationField`, which is a real gap but not the reporter's type as far as the comments show.
- Replacement for any future PR text: "The ICU collation field has had the same rule since SOLR-15777. This change applies the rule to CollationField." The owner must decide whether 15712 stays a separate core change or closes as a duplicate (Owner decision 3).

**F6. NOTE (SOLR-15712, tidy drift is gone at the tip).**
- Where: `TestCollationFieldDocValues.java` lines 193-194 (head cb988a2ff4c).
- Evidence: the receipt (`receipts/SOLR-15712.md`, read from `origin/pr-prepare`) names tidy drift at 555f9cab6b7. Commit cb988a2ff4c already contains the rewrap (`git diff 555f9cab6b7 cb988a2ff4c`, 2 lines). The first line is 100 columns, and adding " a" makes it 102, so the greedy 100-column fill is in place. The tip also removes `SOLR-15712-TESTING.md`. Tidy has not been run. This is a line-length check only.
- Replacement: none for the code. The receipt is stale and should be refreshed to cb988a2ff4c.

**F7. NOTE (SOLR-15712, fail-before by reasoning).**
- Where: `TestCollationFieldDocValues.java` lines 196-211 (head).
- Evidence: `schema-collate-dv.xml` is schema version 1.7 (line 21). On base, `useDocValuesAsStored` defaults on for version 1.6 and later (`FieldType.java` line 190), and `CollationField.enableDocValuesByDefault` returns true (`CollationField.java` lines 245-247). So on base, `fl=*` should return the `sort_*` fields, and the count assertion on line 210 should fail. This was not run. The gate must confirm it.
- Replacement: none.

**F8. FIX (SOLR-15712, changelog title, after F4 is decided).**
- Where: `changelog/unreleased/SOLR-15712-collationfield-no-docvalues-as-stored.yml` line 2 (head).
- Evidence: the title does not say that an explicit `useDocValuesAsStored="true"` now changes load behavior.
- Replacement (warn-gated version from F4): `  CollationField no longer defaults useDocValuesAsStored, so fl=* does not decode binary collation keys as UTF-8 text. Setting useDocValuesAsStored=true on a CollationField warns below luceneMatchVersion 9.0 and fails from 9.0 on.`
- Replacement (if the owner keeps the hard fail): end the title with "Setting useDocValuesAsStored=true on a CollationField now fails to load."

**F9. FIX (SOLR-15945, changelog title is wider than the fix).**
- Where: `changelog/unreleased/SOLR-15945.yml` line 1 (head adb0fd450c0). The title line is `title: DateRangeField and SpatialRecursivePrefixTreeFieldType fields declared with indexed="false" no longer fail to load because of the norms and index-options checks`.
- Evidence: a field with `indexed="false"` and docValues still fails. `FieldType.checkSchemaField` calls `checkSupportsDocValues` when the field has docValues (`solr/core/src/java/org/apache/solr/schema/FieldType.java` lines 1194-1197 at `14c7aac0d15`). The base `checkSupportsDocValues` throws (lines 1212-1215). Neither `DateRangeField` nor the prefix-tree type overrides it (grep of `checkSupportsDocValues` in the schema package at `upstream/main`). The round-2 review, finding 2, says the same.
- Replacement: `title: Stored-only DateRangeField and SpatialRecursivePrefixTreeFieldType fields (indexed="false") no longer fail to load because of the norms and index-options checks`

**F10. FIX (SOLR-15945, misleading test schema comment).**
- Where: `solr/core/src/test-files/solr/collection1/conf/schema-nonindexed-spatial.xml` line 26 (head).
- Evidence: the comment says "no norms or index options exist for these, so the type's invariants hold". The checks are skipped for these fields, not satisfied. The "exist" wording is also a Lucene-level claim that was not checked.
- Replacement: `  <!-- stored-only: the norms and index-options checks do not apply to these fields, so the type skips them -->`

**F11. NOTE (SOLR-15945, Lucene-level code comment).**
- Where: `solr/core/src/java/org/apache/solr/schema/AbstractSpatialPrefixTreeFieldType.java` line 150 (head).
- Evidence: the comment "norms and index options only exist on indexed fields" is a Lucene-level statement. It was not checked against the Lucene 9.x or 10.x source, which is not in this environment. The Solr-side fact is checked: `SchemaField.indexOptions()` returns `IndexOptions.NONE` for a field that is not indexed (base lines 513-516).
- Replacement (Solr-side wording only): `      // the norms and index-options checks apply only to indexed fields`

**F12. NOTE (SOLR-15945, base is 66 commits behind upstream/main).**
- Where: merge base `14c7aac0d15` (2026-10-03).
- Evidence: trial merges with `git merge-tree --write-tree` (no ref written) are clean. Onto `upstream/main`: tree `580fd6c1fa5`. With `origin/solr-18134-submit`: tree `ecf34fcfa16`. No upstream commit after `14c7aac0d15` touches the four files that 15945 changes. A rebase changes the head, so the draft's head SHA and blob links must be updated then.
- Replacement: none now. Update the draft's head SHA when the branch is rebased.

**F13. NOTE (SOLR-15945, receipt claim on SOLR-15403 is not established).**
- Where: `receipts/SOLR-15945.md` line 8 (read from `origin/pr-prepare`).
- Evidence: the receipt says the change "resolves the SOLR-15403 duplicate per the branch's handoff note". The branch has no handoff note: the diff against base is four files. The round-2 review (`research/branch-reviews/round-2/SOLR-15945-review.md`) and the round-28 review say the local packet does not establish a link. `research/pipeline/candidates.csv` line 140 shows SOLR-15403 (Open, 2021) describing the same `indexOptions` check on this path.
- Replacement for the receipt sentence: "SOLR-15403 describes the same index-options check. Duplicate status is not established." The draft uses this position.

**F14. NOTE (SOLR-15945, Proof source).**
- Evidence: the receipt says the pre-fix proof step passed at this head. It does not record the base failure line or the fail-before verdict. The draft's Proof says the test fails on base because the schema does not load. That statement is derived from base code (`AbstractSpatialPrefixTreeFieldType.java` lines 149-158 at `14c7aac0d15`) and from the ticket text. The gate log `g15945-harden.log` is not on disk.
- Replacement: none to the draft. Owner decision 8 asks for the log check before posting.

**F15. NOTE (cross-check 15945 and 18134, no shared files).**
- Evidence: the 15945 changes are four files: `changelog/unreleased/SOLR-15945.yml`, `AbstractSpatialPrefixTreeFieldType.java`, `schema-nonindexed-spatial.xml`, `NonIndexedSpatialFieldTest.java`. The 18134 changes are 14 files, including its own changelog and the analysis package. The intersection is empty. The trial merge of the two heads is clean (see F12).

## Task results

**SOLR-10131: HOLD, not drafted.** Live tip fd495167cd0. The inventory and receipt name b93cf24a9d9. The tip adds "remove handoff doc", which deletes `SOLR-10131-TESTING.md` (34 lines), so the receipt is stale. The branch changes three files against `cabedd1d968`: `UUIDField.java`, `UUIDFieldTest.java`, and the changelog. The new test `testNonHexCharactersAreRejected` (`UUIDFieldTest.java` lines 68-81) fails on base by reasoning: base `toInternal` accepts all four bad values with no exception, so `expectThrows` fails. That is not a pin. The premise does not match the Jira summary (F1). A premise run must show four things: (a) on base, an update with the ticket's 7-character value returns 400, not 500 (expected from code, so the ticket title is already fixed); (b) on base, an update with a 36-character value containing a non-hex character returns 200 and stores it, which is the real gap; (c) on head, the same update returns 400, and a query or facet with that value returns 400 (F2); (d) `testNonHexCharactersAreRejected` fails on base and passes on head. The gate must run `UUIDFieldTest`, `UuidAtomicUpdateTest` (added by SOLR-10653), the test classes that use the 16 test schemas that declare `solr.UUIDField`, plus spotless and Error Prone compile. Owner decisions 1 and 2 must be settled before any draft.

**SOLR-15712: HOLD, not drafted.** Live tip cb988a2ff4c. The receipt names 555f9cab6b7. The receipt's tidy drift is already fixed at the tip (F6). The branch changes `CollationField.java`, `TestCollationFieldDocValues.java`, and the changelog. Three points block a draft or a gate. First, the core guard fails hard at every luceneMatchVersion, which breaks old schemas and is out of line with the ICU precedent (F4, F8). Second, the reporter's comments point to the ICU type, which has had the SOLR-15777 guard since base, so the ticket may be a duplicate (F5). Third, the new test `testDocValuesAreNotUsedAsStored` should fail on base (F7), but that is by reasoning. A premise run must show: on base, a core `CollationField` with docValues and default `useDocValuesAsStored` (schema version 1.7), one document with a binary collation key, and a `fl=*` request that either throws the decode error or returns `sort_*` values; on head, neither happens. The gate must run `TestCollationFieldDocValues`, `TestCollationField`, and a new warn/fail test in the style of `TestICUCollationFieldUDVAS`. The ticket also says `backup` fails. The branch test covers `fl=*` only, so the backup path is not covered (see Not checked).

**SOLR-15945: DRAFTABLE.** Head adb0fd450c0 (full SHA `adb0fd450c06c07f49ccd187c3d810410a76c5b3`). The receipt says gated green: `NonIndexedSpatialFieldTest` 1 of 1 at the head, tidy clean, Error Prone compile passes, module check passes. The fix is an early return in `AbstractSpatialPrefixTreeFieldType.checkSchemaField` (head lines 149-152). It skips the omitNorms and index-options checks for fields with `indexed="false"`. The superclass checks still run first. Checked against the diff: the four changed files, the `SchemaField` and `FieldType` claims at `upstream/main`, and the two subclasses (`DateRangeField`, `SpatialRecursivePrefixTreeFieldType`) named in the changelog. The draft (`pr-drafts/schema-analysis/SOLR-15945.md`, about 3,200 characters without URLs) has these parts: the stored-only scope, a Choice section (load stored-only fields, or keep refusing them with a clearer error), and Limits (docValues-only still fails, presence-only assertions, SOLR-15403 relation). The draft has no em or en dashes and no internal process words. Apply F9 and F10 to the branch text before posting. Owner decisions 5 to 8 apply.

## Owner decisions

1. SOLR-10131 scope. The Jira title is the 500 symptom, which SOLR-10653 already fixed. Options: (a) submit the non-hex check under SOLR-10131 with a description that says the 500 is already fixed; (b) re-scope the ticket first; (c) hold. No new Jira ticket unless someone asks.
2. SOLR-10131 query behavior. Accept the new 400 for queries, ranges, facet prefixes, and id lookups with 36-character non-hex values (F2), or limit the check to writes.
3. SOLR-15712 duplicate status. Is it a duplicate of SOLR-15777? If yes, close it as a duplicate and decide whether the core analog is its own change. If no, the PR names SOLR-15777 as the precedent.
4. SOLR-15712 compatibility. Keep the hard fail at every luceneMatchVersion (current branch), or use the ICU gate: warn below 9.0, fail from 9.0 (F4). Recommended: the ICU gate.
5. SOLR-15945 scope of the fix. Load stored-only spatial fields (implemented, drafted as the Choice), or keep refusing them with a clearer message.
6. SOLR-15945 and SOLR-15403. Duplicate, related, or unrelated? Decide whether SOLR-15403 goes in the changelog links.
7. SOLR-15945 rebase. The branch is 66 commits behind upstream/main. A rebase changes the head SHA used in the draft (F12).
8. SOLR-15945 before posting. Confirm the base failure line and the fail-before verdict from the gate log `g15945-harden.log`, and confirm that 2026-10-03 is the run date and not the ledger entry date. The draft's Proof depends on both.

## Not checked

- No builds, tests, Gradle, `gh` calls, fetches, or posts. Branch heads were read from local refs and the claim's live-head table. Nothing was committed.
- Live Jira was not read. The Jira facts come from local packets: `research/jira-context/SOLR-10131.json` (last updated 2019-06-08), `SOLR-15712.json` (2022-02-03), `SOLR-15945.json` (2022-05-12), and `research/pipeline/candidates.csv`.
- The gate log `g15945-harden.log` and the 15712 and 10131 test results are not on disk. Every fail-before statement in this report is by reasoning from code, not from a recorded run.
- Tidy, spotless, and Error Prone were not run. Formatting was judged only by line length. No added Java line in 10131, 15712, or 15945 is over 100 columns. Changelog titles are over 100 columns, which YAML does not format.
- Lucene 9.x and 10.x were not checked, because no Lucene source is in this environment. No draft names Lucene behavior. The code comment in F11 is the only Lucene-level statement in the 15945 diff, and the replacement avoids it.
- SOLR-15712 backup path was not traced. The branch test covers `fl=*` only.
- SOLR-10131: the other test classes that index UUID values were not all read. The 16 test schemas that declare `solr.UUIDField` were counted, not read.
- SOLR-15945: the stored-only load path (createFields and query behavior for `indexed="false"`) was not traced beyond the gate's one test, which checks presence of stored values only.
