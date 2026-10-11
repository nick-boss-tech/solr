# Search components draft fidelity, slice s2 (claim C2)

Assignment: `assignments/pool-draft-fidelity-searchcomponents-edismax.md`. Claim: `claims/pool-draft-fidelity-searchcomponents-edismax.md`, slice C2.
Slice drafts: `pr-drafts/search-components/SOLR-11153.md`, `SOLR-11310.md`, `SOLR-11364.md`, `SOLR-11470.md`, `SOLR-12044.md`.
Worktree: `C:\Users\shaninna\dev\Solr-issues\wt\pr-prepare-suggester`, HEAD d627304e96b. Read only. No builds, tests, gate runs, commits, pushes, or GitHub or Jira writes.

Round reports checked per ticket (the part whose sections cover the ticket):
- SOLR-11153: `reports/search-components-3-w6.md` (summary `search-components-3.md`)
- SOLR-11310: `reports/search-components-4-s3.md` (summary `search-components-4.md`)
- SOLR-11364: `reports/search-components-3-w4.md` (summary `search-components-3.md`)
- SOLR-11470: `reports/search-components-2-h5.md` (summary `search-components-2.md`)
- SOLR-12044: `reports/search-components-4-s6.md` (summary `search-components-4.md`)

Receipts: `receipts/SOLR-<n>.md` for all five. Answers material: `material/` grepped for each ticket number; no file mentions any of the five.

Heads (`git ls-remote origin refs/heads/<branch>`, live tip compared with the head each draft names):
- solr-11153-submit: 093df0d65bdce7444b3ef9a0da857b9c40edd00c, matches
- solr-11310-submit: e38ddec5279c465f65a2fa2de22ee9c92e0f5e31, matches
- solr-11364-submit: 562d3e7e685a06acd1fbe3baf2fd3bf168979067, matches
- solr-11470-submit: f44c294da37452462541771c48b2d85132cf5a16, matches
- solr-12044-submit: c92bbf9349b12e96918646a73bfaa36248b3d523, matches

Merge-bases used for the pre-change citation rule (`git merge-base`): 11153 e2cdb2d7e8ae0be4cf6cf0606206c67d89e7278f; 11310, 11364, 11470 14c7aac0d151402b00259e2fb9bf5eed7049ec5d; 12044 cabedd1d968059215188f4e7563fb303241899ed. All cited SHAs resolve (`cat-file -t`).

## Verdicts

| Draft | Head checked | Verdict |
|---|---|---|
| SOLR-11153 | 093df0d65bdce7444b3ef9a0da857b9c40edd00c (matches) | DRIFT (3 items) |
| SOLR-11310 | e38ddec5279c465f65a2fa2de22ee9c92e0f5e31 (matches) | DRIFT (2 items) |
| SOLR-11364 | 562d3e7e685a06acd1fbe3baf2fd3bf168979067 (matches) | DRIFT (2 items) |
| SOLR-11470 | f44c294da37452462541771c48b2d85132cf5a16 (matches) | DRIFT (1 item) |
| SOLR-12044 | c92bbf9349b12e96918646a73bfaa36248b3d523 (matches) | DRIFT (1 item) |

Checks that passed for all five: the head named in each draft equals the live tip; every changelog link points at a file that exists at the named head, with the file name matching; every Proof count matches its receipt (11153: 1, 15, 15, 2 with 1 skipped = 33; 11310: 4 of 4, 11 of 11; 11364: 1 of 1, 58 at 8b3fc5c8880; 11470: 13 of 13, 113 of 113; 12044: 6 of 6 with 1 failing on base); each Proof has a verification date (11153 2026-10-05; 11310 2026-10-03 and 2026-10-07; 11364 2026-10-08; 11470 2026-10-05; 12044 2026-10-09); each new-code citation (head SHA) shows the cited lines; each "base" citation shows the cited lines at the merge-base; every Choice and Limits point matches its round report; no internal process vocabulary and no em dash in any draft.

Title check note: the drafts have no separate title line, so the title check compares the changelog fragment title with the draft's bold lead and body.

## SOLR-11153

Verdict: DRIFT (3 items).

1. Draft says: "It also runs when Solr saves a managed schema ([IndexSchema.java](https://github.com/nick-boss-tech/solr/blob/093df0d65bdce7444b3ef9a0da857b9c40edd00c/solr/core/src/java/org/apache/solr/schema/IndexSchema.java#L449-L458))."
   Evidence: the changelog title at 093df0d (`changelog/unreleased/SOLR-11153-schema-xml-missing-name.yml`, lines 2-3) reads "Requesting a schema as XML (wt=schema.xml) no longer fails with a NullPointerException when the schema has no name attribute." It names only the wt=schema.xml path. The save path is in the fix (IndexSchema.java 449-458 calls SchemaXmlWriter.writeResponse; ManagedIndexSchemaFactory.java 390 calls persistManagedSchema). `w6` finding 4 gives the same replacement.
   Replacement (title value, lines 2-3 of the changelog file): "Writing a schema as XML no longer fails with a NullPointerException when the schema has no name attribute."
   Note: editing the changelog moves the head, so the draft's links and Proof head then need the new SHA and a fresh gate.

2. Draft says: "The writer runs for `wt=schema.xml` ([SchemaXmlResponseWriter.java](https://github.com/nick-boss-tech/solr/blob/093df0d65bdce7444b3ef9a0da857b9c40edd00c/solr/core/src/java/org/apache/solr/response/SchemaXmlResponseWriter.java#L28)). It also runs when Solr saves a managed schema ([IndexSchema.java](https://github.com/nick-boss-tech/solr/blob/093df0d65bdce7444b3ef9a0da857b9c40edd00c/solr/core/src/java/org/apache/solr/schema/IndexSchema.java#L449-L458)). One caller is [ManagedIndexSchemaFactory.java](https://github.com/nick-boss-tech/solr/blob/093df0d65bdce7444b3ef9a0da857b9c40edd00c/solr/core/src/java/org/apache/solr/schema/ManagedIndexSchemaFactory.java#L390)."
   Evidence: this is the pre-change symptom path. The branch changes only SchemaXmlWriter.java, its test and the changelog (`git diff --stat e2cdb2d7e8ae 093df0d65bdc`). The three cited files are identical at base and head, and the lines match (L28 the wt=schema.xml writer; L449-458 persist(); L390 persistManagedSchema(true)). pr-formula.md requires the merge-base link and the words "base" in the text. The draft links head and does not say so.
   Replacement: "The writer runs for `wt=schema.xml` ([SchemaXmlResponseWriter.java, base](https://github.com/apache/solr/blob/e2cdb2d7e8ae0be4cf6cf0606206c67d89e7278f/solr/core/src/java/org/apache/solr/response/SchemaXmlResponseWriter.java#L28)). It also runs when Solr saves a managed schema ([IndexSchema.java, base](https://github.com/apache/solr/blob/e2cdb2d7e8ae0be4cf6cf0606206c67d89e7278f/solr/core/src/java/org/apache/solr/schema/IndexSchema.java#L449-L458)). One caller is [ManagedIndexSchemaFactory.java, base](https://github.com/apache/solr/blob/e2cdb2d7e8ae0be4cf6cf0606206c67d89e7278f/solr/core/src/java/org/apache/solr/schema/ManagedIndexSchemaFactory.java#L390)."

3. Draft says: "Solr always sets a version. It uses 1.0 when the file has none ([IndexSchema.java](https://github.com/nick-boss-tech/solr/blob/093df0d65bdce7444b3ef9a0da857b9c40edd00c/solr/core/src/java/org/apache/solr/schema/IndexSchema.java#L514))."
   Evidence: IndexSchema.java L514 (`version = Float.parseFloat(... "1.0f")`) is unchanged from base (the file is not in the branch diff). It is pre-change code, so it links the merge-base. `w6` finding 5 confirms the default.
   Replacement: "Solr always sets a version. It uses 1.0 when the file has none ([IndexSchema.java, base](https://github.com/apache/solr/blob/e2cdb2d7e8ae0be4cf6cf0606206c67d89e7278f/solr/core/src/java/org/apache/solr/schema/IndexSchema.java#L514))."

Optional notes, not blocking: the file name carries a suffix (`SOLR-11153-schema-xml-missing-name.yml`), which the formula template does not show; accepted as the branch's own fragment name. The Choice section matches `w6` owner decision 5 (omit the attribute or warn with a default name). Branch history has two handoff commits (`w6` finding 3); a squash moves the head and needs owner go-ahead. The E link (SchemaXmlWriter.java L89-98 at head) is correct: that code is produced by the change.

## SOLR-11310

Verdict: DRIFT (2 items).

1. Draft says: "Every elevated document in the rerank window gets the boost, not only a leading run." and "It applies to every `rq` rerank with an elevation match, not only LTR."
   Evidence: the changelog title at e38ddec (`changelog/unreleased/SOLR-11310-ltr-elevated-docs.yml`, line 1) reads "Documents elevated with elevateIds are now kept at the top of the results when an LTR rerank query is used, instead of only the ones that happened to sort first by internal document id". The change is in the shared BoostedComp (ReRankCollector.java 324-333 at head). `s3` finding 1 shows the boost applies to every rq rerank (AbstractReRankQuery.java 105-110; QueryElevationComponent.java 572).
   Replacement (title value, line 1 of the changelog file): "Documents elevated with elevateIds now move to the top of the rerank window for every rq rerank, including LTR, not only the leading ones in the window"
   Note: editing the title moves the head (`s3` finding 1); the draft's links then need the new SHA.

2. Draft says: "With LTR, `LTRRescorer.getFirstPassDocsRanked` also sorts the window by internal document id before that loop runs ([source](https://github.com/nick-boss-tech/solr/blob/e38ddec5279c465f65a2fa2de22ee9c92e0f5e31/solr/modules/ltr/src/java/org/apache/solr/ltr/LTRRescorer.java#L146-L148))."
   Evidence: this is pre-change symptom code. LTRRescorer.java is unchanged between 14c7aac0d151 and e38ddec (`git diff --quiet` exit 0), and L146-148 (`Arrays.sort(hits, docComparator)`) read the same at base. The link should be the merge-base and the text should say so.
   Replacement: "With LTR, `LTRRescorer.getFirstPassDocsRanked` also sorts the window by internal document id before that loop runs ([base](https://github.com/nick-boss-tech/solr/blob/14c7aac0d151402b00259e2fb9bf5eed7049ec5d/solr/modules/ltr/src/java/org/apache/solr/ltr/LTRRescorer.java#L146-L148))."

Optional notes, not blocking: the Proof's "TestReRankQParserPlugin 11 of 11 at the same head" has no date of its own; `s3` gives 2026-10-03 for that count, so adding "checked 2026-10-03" would match the one-line-per-class rule. The Choice is correct in form and has a live alternative (option 2), but `s3` owner decision 1 must be settled before the PR opens. The new test is `testBoostedDocsSurviveLTRRescore` (TestLTRReRankingPipeline.java line 240 at head), as named. The base failure text `expected:<[1]> but was:<[0]>` matches the receipt.

## SOLR-11364

Verdict: DRIFT (2 items).

1. Draft says: "A field with useDocValuesAsStored=false is dropped when fl also has a glob, even when fl names it." and "When fl has a glob, a field named by its own name in fl is now returned, even if it has useDocValuesAsStored=false."
   Evidence: the changelog title at 562d3e7 (`changelog/unreleased/SOLR-11364-explicit-dv-field-with-glob.yml`, lines 2-3) reads "A field with useDocValuesAsStored=false that is named explicitly in fl is now returned even when fl also contains a glob such as fl=id,a*,a3." The alias form `fl=mydv:a3` is still not returned (SolrReturnFields.java 294 and 498-500 at base), and the draft's own Limits says so. `w4` finding 3 calls the title broader than the code; `w3` says the same.
   Replacement (title value, lines 2-3 of the changelog file): "A field with useDocValuesAsStored=false that is listed by its own name in fl is now returned even when fl also contains a glob such as fl=id,a*,a3."
   Note: editing the title moves the head (`w4` item 3).

2. Draft says: "In the test schema, `a3` has `stored=false`, `docValues=true` and `useDocValuesAsStored=false` ([schema](https://github.com/nick-boss-tech/solr/blob/562d3e7e685a06acd1fbe3baf2fd3bf168979067/solr/core/src/test/org/apache/solr/schema/TestUseDocValuesAsStored2.java#L69-L76))."
   Evidence: the test file's only hunk is `@@ -121,0 +122,10 @@` (the new checks), so L69-76 are base content, and they read the same at 14c7aac0d151 (the a3 add-field payload). This is pre-change symptom setup, so it should link the merge-base and say so.
   Replacement: "In the test schema, `a3` has `stored=false`, `docValues=true` and `useDocValuesAsStored=false` ([schema, base](https://github.com/nick-boss-tech/solr/blob/14c7aac0d151402b00259e2fb9bf5eed7049ec5d/solr/core/src/test/org/apache/solr/schema/TestUseDocValuesAsStored2.java#L69-L76))."

Verified and consistent: the code link SolrDocumentFetcher.java 883-892 at head (the pattern-matching branch of calcDocValueFieldsForReturn, which starts at line 862); the new checks at TestUseDocValuesAsStored2.java 122-130 at head; the testSchemaAPI count (one test method); the Limits lines match `w4` finding 4 (alias `fl=alias:a3` not returned; `fl=a3:a1` can return the raw a3 value when a1 is missing); the draft has no Choice section, which fits the formula (narrow scope is not a choice) and `w4` owner decision 3.

Optional notes, not blocking: the branch history has three commits with Claude co-author trailers and two handoff commit subjects (`w4` items 1 and 2); a squash to a new branch name is the owner's call and moves the head. The branch is 66 commits behind the local upstream/main ref (`w4` item 7; the receipt says 45); a rebase is an owner item and the focused run repeats after it. The 58-test count at 8b3fc5c8880 has no class list on disk (`w4` owner decision 4); the draft states the count only.

## SOLR-11470

Verdict: DRIFT (1 item).

1. Draft says: "The lucene query parser already makes a pure negative query searchable, but only when `luceneMatchVersion` is 10.2.0 or later ([SolrQueryParser.java](https://github.com/apache/solr/blob/8e62c2686882aa704480ab13b6a60ee8f7b5c8af/solr/core/src/java/org/apache/solr/search/SolrQueryParser.java#L31-L35), [QParser.java](https://github.com/apache/solr/blob/8e62c2686882aa704480ab13b6a60ee8f7b5c8af/solr/core/src/java/org/apache/solr/search/QParser.java#L113-L118)). The `{!bool}` parser builds the same kind of query without that step ([BoolQParserPlugin.java](https://github.com/apache/solr/blob/8e62c2686882aa704480ab13b6a60ee8f7b5c8af/solr/core/src/java/org/apache/solr/search/BoolQParserPlugin.java)), so `q={!bool must_not=term_s:ZZZZ}` with `rq` returns no documents on the base code."
   Evidence: these three files are pre-change code and are unchanged between the merge-base 14c7aac0d151 and the cited upstream main 8e62c26 (`git diff --quiet` exit 0; the L31-35 and L113-118 lines read the same at base). The branch does not touch them. pr-formula.md requires the merge-base link for pre-change code and the word "base" in the text. The draft links upstream main, which is not the merge-base, and does not say so. The claim itself holds: BoolQParserPlugin.java has no makeQueryable or negative handling, and the QParser gate is on LUCENE_10_2_0.
   Replacement: "The lucene query parser already makes a pure negative query searchable, but only when `luceneMatchVersion` is 10.2.0 or later ([SolrQueryParser.java, base](https://github.com/apache/solr/blob/14c7aac0d151402b00259e2fb9bf5eed7049ec5d/solr/core/src/java/org/apache/solr/search/SolrQueryParser.java#L31-L35), [QParser.java, base](https://github.com/apache/solr/blob/14c7aac0d151402b00259e2fb9bf5eed7049ec5d/solr/core/src/java/org/apache/solr/search/QParser.java#L113-L118)). The `{!bool}` parser builds the same kind of query without that step ([BoolQParserPlugin.java, base](https://github.com/apache/solr/blob/14c7aac0d151402b00259e2fb9bf5eed7049ec5d/solr/core/src/java/org/apache/solr/search/BoolQParserPlugin.java)), so `q={!bool must_not=term_s:ZZZZ}` with `rq` returns no documents on the base code."

Verified and consistent: ResponseBuilder.java 480-488 at head (wrap calls makeQueryable at 484); the new test at TestReRankQParserPlugin.java 303-335 at head (`testReRankWithPureNegativeMainQueryFromBoolParser`); the ticket's 52-document example (`research/jira-context/SOLR-11470.json`, "How to reproduce"); the wrapper behavior (QueryUtils.makeQueryable unwraps a WrappedQuery at 185-189); the Limits lines (no test for luceneMatchVersion before 10.2.0; the 9.x branch has no autoFixPureNegative in QParser and no getBooleanQuery override in SolrQueryParser, checked on upstream/branch_9x); the Choice (ticket accessor route versus the implemented route, `h5` item 5).

Optional notes, not blocking: the changelog title (lines 2-3) does not mention the wrapper unwrapping that the draft discloses; the draft is complete and the title is not wrong. `h5` item 7 calls the clause "when the query parser itself did not fix the query" unclear; the optional replacement is "A pure negative main query is made searchable before it is wrapped in a rank query, so rank queries no longer return no results for it." (`h5` cites lines 8-9; the clause is on lines 2-3). The Proof gives 2026-10-05 for the 13 of 13 count; "QueryEqualityTest 113 of 113 passes at the same head" has no date of its own (same as 11310). Before the PR: three commits carry Claude co-author trailers and two handoff commits are in the history (`h5` items 1 and 2); a rewrite moves the head and needs owner go-ahead.

## SOLR-12044

Verdict: DRIFT (1 item).

1. Draft says: "The cached path returns `absAnswer.intersection(filter)` ([SolrIndexSearcher.java](https://github.com/nick-boss-tech/solr/blob/c92bbf9349b12e96918646a73bfaa36248b3d523/solr/core/src/java/org/apache/solr/search/SolrIndexSearcher.java#L1504)). The uncached path combines the query with the filter and collects matching documents ([DocSetUtil.java](https://github.com/nick-boss-tech/solr/blob/c92bbf9349b12e96918646a73bfaa36248b3d523/solr/core/src/java/org/apache/solr/search/DocSetUtil.java#L123-L124))."
   Evidence: both are pre-change symptom code. The merge-base of the branch with upstream main is cabedd1d968059215188f4e7563fb303241899ed. DocSetUtil.java is unchanged on the branch (no diff). In SolrIndexSearcher.java the branch inserts 5 lines at 1481, so the cached line is base L1499 (`return positive ? absAnswer.intersection(filter) : filter.andNot(absAnswer);`, checked at cabedd1), and head L1504 is the same line.
   Replacement: "The cached path returns `absAnswer.intersection(filter)` ([SolrIndexSearcher.java, base](https://github.com/nick-boss-tech/solr/blob/cabedd1d968059215188f4e7563fb303241899ed/solr/core/src/java/org/apache/solr/search/SolrIndexSearcher.java#L1499)). The uncached path combines the query with the filter and collects matching documents ([DocSetUtil.java, base](https://github.com/nick-boss-tech/solr/blob/cabedd1d968059215188f4e7563fb303241899ed/solr/core/src/java/org/apache/solr/search/DocSetUtil.java#L123-L124))."

Verified and consistent: the shortcut at SolrIndexSearcher.java 1484-1487 at head (the new code, correctly linked to head, after the WrappedQuery unwrap at 1479-1481); the javadoc says "Should not be modified by the caller" (1470-1471); the new test `testMatchAllDocsWithFilterReturnsFilter` at TestIndexSearcher.java 172-197 at head; the Choice section is a live alternative (option two keeps the live-docs intersection) and matches `s6` owner decision 1; the changelog title (c92bbf9, lines 1-3) matches the code. The changelog link L1-L9 exists (9-line file).

Optional notes, not blocking: the changelog type on c92bbf9 is `optimized` (line 4), which is not a valid type (`s6` finding 1; the valid list has `changed`). The draft does not name the type, so this is not draft drift, but it must change before a PR, and the change moves the head; the Proof head and links then need the new SHA. `s6` interaction: the branch and solr-13851-submit conflict in TestIndexSearcher.java (both add a test at the same spot); keep both tests when the second one lands.

## Not done

- Live PR state, live Jira text and gh calls: not read. Jira facts were checked only against the local packets in `research/jira-context` (main workspace, read only) for 11153, 11310 and 11470.
- Gate logs and JUnit XML: not on disk for these tickets; all counts are from the receipts. No builds, tests, or compile checks were run.
- Changelog YAML parsing: not checked for any of the five.
- 12044: "With no filter, both paths already return the live-documents set" and the `getDocSetNC` / `getPositiveDocSet` / `DocSetUtil.createDocSet` path claims were checked by diff only, not traced through the call graph. The 91 two-argument `getDocSet` call sites were not checked (`s6` not-checked list).
- 11310: the "elevation priority order" wording in the What-this-change section was not traced beyond the compare function.
- Commit-history trailers and handoff commits (11364, 11470, 12044 notes) are reported from the round reports, not re-read.
