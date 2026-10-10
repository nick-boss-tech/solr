# Schema analysis round 1, part s1 (SOLR-9349, SOLR-14199)

Result: SOLR-9349 is draftable after one history fix (squash the commit messages). SOLR-14199 is held: a probable test failure outside the gate, an unstated behavior change, and a receipt count that does not match the source.

Claim: `claims/schema-analysis-round-1.md` (c87876a40f2). Heads checked against the live refs: `origin/solr-9349-submit` 1e79bb42123 and `origin/solr-14199-submit` e743c90da79 match the claim. `origin/solr-15357-submit` d717899b873 matches too. Base for 9349 is 14c7aac0d15; base for 14199 and 15357 is b5c71bc5573. upstream/main is 8e62c268688.

## Findings

**1. FIX (14199). SchemaVersionSpecificBehaviorTest expects the old omitNorms default for point types.**
File: `solr/core/src/test/org/apache/solr/schema/SchemaVersionSpecificBehaviorTest.java`, lines 60-64 (head e743c90da79).
Evidence: head `PointField.java` line 92 sets `properties |= OMIT_NORMS;` for every schema version. Base `PrimitiveFieldType.java` lines 31-34 set it only above schema 1.4. The test loops over versions 1.0 to 1.7 and checks the field `int`, which is `${solr.tests.IntegerFieldType}` (`schema-behavior.xml` lines 29 and 64). It expects `(v < 1.5F ? false : !(TextField))`, so 1.0 to 1.4 must report false. `SolrTestCaseJ4.randomizeNumericTypesProperties` picks point types 80% of the time. With point types, `int` reports true on 1.0 to 1.4 and the assertion fails. This is a reading of the code, not a run. The receipt does not list this class. PrimitiveFieldTypeTest was updated for the same change, so this one was missed.
Exact replacement for lines 60-64:
```java
          // 1.5: omitNorms default changed to true for non TextField
          // SOLR-14199: point fields always omit norms, regardless of version
          assertEquals(
              f + " field's type has wrong omitNorm for ver=" + ver,
              (v < 1.5F && !(field.getType() instanceof PointField))
                  ? false
                  : !(field.getType() instanceof TextField),
              field.omitNorms());
```
Then add a gate run of this class before any PR.

**2. FIX (14199). Stored-only point fields change existence query behavior, and nothing says so.**
File: `solr/core/src/java/org/apache/solr/schema/FieldType.java`, line 1074 on head (`} else if (!field.omitNorms()) {`), which replaces base lines 1073-1075 (`&& !isPointField()` carve-out).
Evidence: `SchemaField` strips OMIT_NORMS when `indexed="false"`, so a stored-only point field (no docValues) reports `omitNorms()` false. Base sent it to `getSpecializedExistenceQuery` (range query, zero matches). Head sends it to `FieldExistsQuery`, and `validateFieldExistsQuery` turns the IllegalStateException into BAD_REQUEST. Lucene check (javap on the jars in the local Gradle cache): `lucene-core` 10.4.0 `FieldExistsQuery.rewrite` throws whenever the field has FieldInfo but no norms, no vectors and no doc values. `lucene-core` 9.12.3 throws the same way only when `hasStrictlyConsistentFieldInfos` is true, which is true for leaves created by Lucene 9 or later. Point values do not count in either line. The changelog, the upgrade note and the draft Limits did not say this before the draft did.
Options, for the owner. (a) Keep it, state it (the draft does), and accept that stored-only point fields behave like trie and string fields. (b) Restore the old result for points with this replacement on line 1073-1074:
```java
    } else if (!field.omitNorms() && !(isPointField() && !field.indexed())) {
```
Option (b) gives base behavior for stored-only points and keeps the carve-out removal for indexed points.

**3. FIX (14199). The upgrade note says "a field" where the code checks only indexed fields.**
File: `solr/solr-ref-guide/modules/upgrade-notes/pages/major-changes-in-solr-11.adoc`, the new "Schema Changes" section (about lines 30-36 on head).
Evidence: head `PointField.java` line 101 checks `field.indexed() && !field.omitNorms()`. Base already refuses explicit `indexed="false"` with `omitNorms="false"` through `SchemaField` (`SchemaField.java` base conflicting-options check). The existing fixture `bad-schema-not-indexed-but-norms.xml` line 27 and `BadIndexSchemaTest` line 28 cover that on base. So "silently ignored" is wrong for non-indexed fields.
Exact replacement for the sentence that begins "A schema that explicitly sets":
"A schema that explicitly sets `omitNorms="false"` on a point field type, or on an indexed field or dynamic field of a point type, previously had the setting silently ignored."

**4. FIX (14199). Receipt count does not match the source.**
Receipt `receipts/SOLR-14199.md` (on origin/pr-prepare) says `TestSolrQueryParser 37 of 37 at the head, from fresh JUnit XML`. The head source has 36 `public void test` methods, and base has 36 too. `SolrTestCaseJ4` has no inherited test methods. The JUnit XML is not on disk. The draft carries a visible placeholder. Confirm the XML count, or correct the receipt, before the number goes public.

**5. FIX (9349). Commit messages on the branch name internal files and process.**
Commits on `origin/solr-9349-submit`: `16129c64b81` body reads "Hypothetical, unrun regression test; see SOLR-9349-TESTING.md." Subjects `38b6cf56d19` ("add hypothetical-reproduction handoff doc"), `1e79bb42123` ("remove the handoff doc before submission") and `bc685e2cc53` ("fix the changelog author entry"). These become public on the PR.
Exact replacement: squash to one commit with subject `SOLR-9349: refuse to delete the uniqueKey field through the Schema API` and a short plain body. This rewrites the fork branch, which is a force push to the fork. It needs the owner's go-ahead.

**6. NOTE (9349). The copy field checks already exist on base.**
File: `solr/core/src/java/org/apache/solr/schema/ManagedIndexSchema.java`. Base 14c7aac0d15 lines 508-516 refuse deleting a copy field source or destination (`copyFieldsMap.containsKey` and `isCopyFieldTarget`, plus dynamic copy fields). Head 1e79bb42123 adds only the uniqueKey check at lines 499-503. The draft says the copy field part was already covered. The JIRA comments (in `research/jira-context/SOLR-9349.json`) also name `df` and `qf` references, which stay open. The draft's Limits names them with a follow-up offer.

**7. NOTE (9349 with 15357). Sub-fields become copy field targets, so the 9349 check widens.**
Evidence: base `IndexSchema.java` lines 1554-1556: `isCopyFieldTarget(f)` is `copyFieldTargetCounts.containsKey(f)`. Branch 15357 counts currency sub-fields as targets (`IndexSchema.java` hunk near `incrementCopyFieldTargetCount`). Head ManagedIndexSchema `deleteFields` line 508 then refuses to delete a currency sub-field with the copy-field message. That is a behavior change in the 15357 path. It is outside 9349's scope. Part s2 should state it in the 15357 Limits or Proof.

**8. NOTE (interactions). Trial merges and landing order.**
Each head merged onto current `upstream/main` with `git merge-tree --write-tree --merge-base` and no ref written: 9349 applies clean (tree 709d8a3), 14199 applies clean (tree 1cbccab), 15357 applies clean (tree 24cdf03).
Pairwise merges commute, so the order does not change the result: 9349 with 15357 gives cded17a in both orders; 14199 with 15357 gives 154b0ce in both orders; 9349 with 14199 gives 52aefc8 in both orders.
Overlap is textual only. 9349 and 15357 both touch `ManagedIndexSchema.java`, at different lines (9349 near 499-503; 15357 near the removeCopyFieldTargetCount call, the decrement, and the replace-field copy rebuild). 14199 and 15357 both touch `FieldType.java` (14199 near 1074; 15357 adds `getSubFields` after `init`). 9349 and 14199 share no files.
Landing order: 9349, then 14199, then 15357. 15357 changes the copy-target count that 9349 relies on (finding 7), so it lands last and carries the note. Clean textual merges do not prove the code compiles. None of this was compiled.

**9. NOTE (14199). Test randomization.**
`TestSolrQueryParser` and the point-field changes depend on the randomized numeric type (point or trie, and docValues on or off). The receipt records no seed or configuration for that class, so the 37 count does not show which configuration ran. The draft says the run covers one configuration.

**10. NOTE (14199). Hygiene checks passed.**
No co-author or Claude trailers in either branch's commit messages. Changelog `type` is valid (`changed` for 14199, `fixed` for 9349, per `changelog/logchange-config.yml`). Author is "Nick Shanin" on both. The round-28 finding about the schema12 `*_norms` conflict is resolved on head: the four numeric `*_norms` lines are removed (`schema12.xml` lines 774-785), and no other file in `solr/` uses them.

**11. NOTE (14199). Schema API path.**
`SchemaField` constructor (`SchemaField.java` line 83) calls `type.checkSchemaField(this)`, so the new check also runs on Schema API field creation. No test covers that path.

**12. NOTE (housekeeping). Three temporary commit objects.**
`git commit-tree` created three unreferenced commit objects for the pairwise checks (messages tmp9, tmp14, tmp15). No ref was written. They will be pruned by garbage collection.

## Task results

**SOLR-9349. Verdict: DRAFTABLE after the history fix (finding 5).** The branch is 3 files, +30 lines. The uniqueKey check is at `ManagedIndexSchema.java` lines 499-503 on head. The receipt counts match the source: `TestBulkSchemaAPI` 16 on base and 17 on head, plus `TestFieldCollectionResource` 6, `TestFieldResource` 5 and `TestUniqueKeyFieldResource` 1, for 29 of 29. The changelog is valid. The draft is at `pr-drafts/schema-analysis/SOLR-9349.md`, written against head 1e79bb42123. The base failure is recorded only in the receipt (the premise log is not on disk), so the Proof says it as observed on base and marks the run date.

**SOLR-14199. Verdict: HELD.** The head matches the claim, and the branch is not PR-ready as recorded. Three reasons: finding 1 (a probable failure in `SchemaVersionSpecificBehaviorTest` that the gate did not run), finding 2 (a stored-only point behavior change that is not stated), and finding 4 (the 37 count does not match the source). The ledger's two Limits lines are not on disk (see Not checked). The draft is at `pr-drafts/schema-analysis/SOLR-14199.md`. It has bracketed HOLD and CONFIRM markers and must not be posted until finding 1 is fixed and gated, finding 4 is resolved, and the owner adds the two Limits lines. The receipt's "PR-ready" is not supported as it stands.

## Owner decisions

1. SOLR-14199: keep the stored-only point existence change (finding 2, option a), or restore base behavior (option b).
2. SOLR-14199: keep point types reporting omitNorms true on schema 1.4 and older, or keep the old default there and reject only explicit false. The draft asks maintainers this.
3. SOLR-14199: approve a new commit that fixes `SchemaVersionSpecificBehaviorTest` (finding 1), then a gate run before PR.
4. SOLR-9349: approve squashing the fork branch to one clean commit (finding 5). This is a force push to the fork.
5. SOLR-9349: keep the `df` and `qf` follow-up in Limits with the offer, or drop it.
6. Landing order: 9349, 14199, 15357 (finding 8). Confirm.
7. SOLR-14199: supply the two ledger Limits lines, since the file is not on disk.

## Not checked

- No builds, tests, Gradle, or `gh` calls, as the claim requires. No gate logs or JUnit XML are on disk (`g9349-premise.log`, `g9349-gate.log`, `g14199-*.log`), so the base failures and the 37 count are from receipts only.
- JIRA: no JIRA tool was available in this session. I read `research/jira-context/SOLR-9349.json` locally. For SOLR-14199 the only local record is the round-28 review, which says the JIRA summary is "Enforce omitNorms for PointFields" with no description or comments. I did not re-verify it.
- The ledger file `goal files/reviews-2026-10-05-round27-inventory-queue/14199.md` is not in the workspace. Its two Limits lines are unknown.
- Lucene: checked `FieldExistsQuery.rewrite` by disassembly for 10.4.0 and 9.12.3. I did not check in source that every stored-only field gets a FieldInfo during indexing.
- Other tests asserting omitNorms on point types under schema 1.4 or older: I searched the test trees. The only hit is `SchemaVersionSpecificBehaviorTest`. Luke flag strings were not checked.
- SOLR-7242 (cited in the 9349 JIRA comments) was not checked.
- A full three-way merge of all three branches was not computed. Only pairwise merges were run.
- Compilation was not checked. All merge results are textual.
