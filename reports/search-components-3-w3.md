# Search components round 1, sub-batch 3, part w3 (SOLR-8240 and SOLR-10424)

Result: SOLR-8240 and SOLR-10424 are both draftable (drafts written at fad7a1dd8e23 and e629ab8bb293); settle three FIX items first (claim wording, one 8240 doc sentence, landing order).

Scope: read only. No builds, no tests, no gh write calls, no posting, no commits, no ref changes. Live heads re-checked with a read-only `git ls-remote` on 2026-10-09: `solr-8240-submit` is `fad7a1dd8e23` and `solr-10424-submit` is `e629ab8bb293`, both matching the claim.

## Findings

1. FIX, claim wording. File: `claims/search-components-3.md`, line 53 (Part w3). The claim says "8240 makes JsonLoader honor the field mapping params." The branch does the opposite. It rejects the combination with a 400. Evidence: `JsonLoader.java` at `fad7a1dd8e23` lines 249-255 throws BAD_REQUEST when `mapUniqueKeyOnly` is true and `f` is present. The ticket proposes this: `research/jira-context/SOLR-8240.json`, Description: "It's proposed to explicitly throw an error if mapUniqueKeyOnly=true and f=.. are supplied." Replacement: "8240 makes JsonLoader reject f combined with mapUniqueKeyOnly=true with a 400 error, as the ticket proposes."

2. FIX, branch doc (owner or branch author applies; I did not edit the branch). File: `solr/solr-ref-guide/modules/indexing-guide/pages/transforming-and-indexing-custom-json.adoc` at `fad7a1dd8e23`, line 68. Current text: "Field mapping parameters (`f`) cannot be combined with this parameter: a request that supplies both is rejected with a 400 error, since the mappings would otherwise be silently ignored." Two problems. (a) "supplies both" misses inherited values. Evidence: `SolrPluginUtils.setDefaults` (upstream main, lines 145-150) applies params set values as defaults; `RequestHandlerBase` lines 246-249 sets the useParams context; `ImplicitPlugins.json` lines 28-31 uses `_UPDATE_JSON_DOCS` for `/update/json/docs`; the techproducts `params.json` line 5 sets `mapUniqueKeyOnly` to true. (b) "silently ignored" overstates. The `f` selection still decides which values reach `df`; only the target names are dropped. Evidence: `JsonRecordReader.getInst` (solrj, lines 48-62) builds records from the mappings; `JsonLoader.java` `getDocMap` lines 309-329 keeps only the uniqueKey, the srcField copy, and the `df` values. Replacement for line 68: "Field mapping parameters (`f`) cannot be combined with this parameter. A request that sends `f` while `mapUniqueKeyOnly` is true is rejected with a 400 error, including when the value comes from `initParams` or a params set. Send `mapUniqueKeyOnly=false` to map fields, because the field names in `f` would otherwise be dropped."

3. FIX, landing order and composition. The two changes work the same `mapUniqueKeyOnly` default from two sides. The files are disjoint (see Interaction below), but the behavior is not independent. By code reading only (nothing run):
   - Neither change: the sample's `/update/json/docs` stores the id and the `_src_` copy. Mapped names in `f` are dropped with no error.
   - 10424 only: every field is indexed under its own name. `f` works without `mapUniqueKeyOnly`. A field the sample schema lacks fails the request.
   - 8240 only: the sample still stores only the id and `_src_`. Every request on the sample that sends `f` now gets a 400, unless it sends `mapUniqueKeyOnly=false`. This is worse for `f` users than the current silent drop.
   - Both: every field is indexed; `f` works; an explicit `mapUniqueKeyOnly=true` with `f` gets the 400.
   Evidence: `params.json` line 5 (base `cabedd1d968`, and still at `fad7a1dd8e23`); `ImplicitPlugins.json` lines 28-31 and 36-39; `SolrPluginUtils.setDefaults` lines 145-150. Replacement, landing order: "Land SOLR-10424 before, or together with, SOLR-8240." The 8240 draft already says "Until SOLR-10424 removes that default, `f` requests to the sample fail with this error."

4. FIX, local branch refs are older than the live heads. Do not push from them. Local `solr-8240-submit` is `c250eb4e8d03`. It is not an ancestor of the live `fad7a1dd8e23` (checked with `git merge-base --is-ancestor`, exit 1). It lacks the `df` line in `JsonLoaderTest.java` (line 207 at `fad7`) and the doc sentence (line 68), and it has `SOLR-8240-TESTING.md` (40 lines). Local `solr-10424-submit` is `06de62e5594`. It is an ancestor of the live `e629ab8bb293` (exit 0), so a fast-forward would work. It still has the `resolve("conf")` path defect (`TechproductsJsonDocsParamsTest.java` line 31 at `06de62e5594`) and `SOLR-10424-TESTING.md`. Replacement for any push plan: "Head of record: origin/solr-8240-submit at fad7a1dd8e2397330e0973e809aeba60490d2f08; origin/solr-10424-submit at e629ab8bb293eb54249b426b69a559f1da0f08d7. Align local refs before any push." I changed no refs.

5. NOTE, 10424 behavior change that the receipt does not cover. Evidence: `managed-schema.xml` line 232 has the `*` dynamic field commented out. Line 180 defines `text` as indexed, not stored. The techproducts `solrconfig.xml` has no add-unknown-fields chain (the `_default` `solrconfig.xml` has one at line 891; the sample's chains at lines 1114-1165 do not). So after this change a posted field the schema lacks fails the request, where it was dropped before. Not run. The 10424 draft states this in "What this change does" and in Limits. Owner: accept and state it (the draft does this), or keep the default.

6. NOTE, test coverage gap. File: `solr/solrj/src/test/org/apache/solr/client/solrj/SolrExampleTests.java`, `testArbitraryJsonIndexing` at lines 995-1025 (upstream main). It uses the techproducts configset (line 108) and posts to `/update/json/docs` (line 1005). By reading, its assertions still hold: two documents, and the `_src_` values are kept. A document with no id gets a generated uniqueKey. Not run. The 10424 receipt covers only `TechproductsJsonDocsParamsTest`. The draft names this test in Limits.

7. NOTE, 8240 premise base commit not recorded. File: `receipts/SOLR-8240.md`, line 7. The receipt records the premise failure but no base commit. The branch's merge base with upstream main is `e432df19c4a50b9d54d7fba545397b859cc54f98`. The draft says "the code before this change" and does not name a SHA. Owner: confirm the premise run used `e432df19c4a`.

8. NOTE, 8240 date. Receipt line 4 gives the branch date 2026-10-05. Receipt line 9 gives the ledger date 2026-10-05. The draft's Proof says "Verified 2026-10-05 at this head," using the ledger date. The receipt does not state a separate gate date. Owner: confirm.

9. NOTE, 8240 error text. File: `JsonLoader.java` at `fad7a1dd8e23`, lines 251-254. The message says "Field mappings (f) are ignored when mapUniqueKeyOnly=true." The names are dropped and the selection still applies, so "ignored" is loose. Optional replacement: "Field mappings (f) cannot be used with mapUniqueKeyOnly=true; set mapUniqueKeyOnly=false to map fields." The test (`JsonLoaderTest.java` line 213) checks only that the text contains "mapUniqueKeyOnly," so by reading the wording change does not affect it.

10. NOTE, stale inventory row. File: `branch-focus-inventory-2026-10-08.md`, line 219 (SOLR-10424). The row says "awaiting pipeline" and lists `SOLR-10424-TESTING.md` with "(4 files total)." The live tip has 3 files: `git diff --stat upstream/main...e629ab8bb293` lists the test, the params file, and the changelog. Replacement for the row: "3 files total," with the TESTING.md entry removed. The status is the lead's call. The 8240 row (line 209, "4 files total") matches.

11. NOTE, Jira cross-reference. `research/jira-context/SOLR-10424.json`, comment 15956280 (Noble Paul): "I guess your /update/json/docs is configured with mapUniqueKeyOnly=true ... SOLR-8240 is same I think." The 10424 draft links SOLR-8240 in Limits. No change needed.

## Task results

SOLR-8240: Draftable, with the FIX items above. Draft: `pr-drafts/search-components/SOLR-8240.md`, written against `fad7a1dd8e23`. Proof uses the receipt counts: JsonLoaderTest 32 of 32, TestInitParams 7 of 7. The new test fails on the earlier code with "Expected exception SolrException but no exception was thrown" (receipt line 7). The draft's Choice (400 versus accepting the request with a warning) has a live alternative and a real cost, so it stays. The Limits name the "Setting JSON Defaults" gap as a planned follow-up. That plan needs the owner's agreement. The draft states the change as the ticket proposes it, not as "honor" (see Finding 1). Verdict: draftable after FIX 2 and the landing-order decision (FIX 3). Lucene: none named; no 9.x or 10.x check needed.

SOLR-10424: Draftable. Draft: `pr-drafts/search-components/SOLR-10424.md`, written against `e629ab8bb293`. The Proof matches the fixed test only: TechproductsJsonDocsParamsTest 1 of 1, with the base commit `cabedd1d968` failing on the `mapUniqueKeyOnly` assertion (receipt line 7). The shipped `b48f405` test used `resolve("conf")` and is not claimed (receipt line 8; the defect is confirmed by the file contents at `b48f405` line 31). The draft's Choice (drop the default or keep a named id-only set) has a live alternative and a cost. The Limits state the unknown-field change (Finding 5) and the solrj test (Finding 6). Verdict: draftable; land with or after 8240 (FIX 3). Lucene: none named.

Interaction (8240 with 10424): Files are disjoint. 8240 touches `JsonLoader.java`, `JsonLoaderTest.java`, the ref guide page, and one changelog file. 10424 touches `params.json`, `TechproductsJsonDocsParamsTest.java`, and one changelog file. Trial merges with `git merge-tree --write-tree` (no ref written) were clean (exit 0) for each tip onto the local upstream main ref `8e62c2686882`, and for the two tips together. Caveat: the local upstream ref may be stale. 8240 is 44 commits behind it and 10424 is 37 behind it. Behavior does interact (Finding 3).

## Owner decisions

1. Landing order: SOLR-10424 before or with SOLR-8240. The review recommends this, because 8240 alone turns `f` requests to the sample into 400s.
2. SOLR-8240 Choice: keep the 400 (the ticket's proposal), or accept the request with a warning. Confirm the draft's Choice text.
3. SOLR-8240 docs: apply the Finding 2 replacement and the Finding 9 wording on the branch before submission, or keep the Limits follow-up. The draft's "planned follow-up pull request" needs your agreement.
4. SOLR-10424 Choice: drop the default (as implemented) or keep an id-only named set. Confirm the draft's Choice text.
5. SOLR-10424 unknown-field change: accept and state it (the draft does), or change the sample schema or keep the default.
6. Local refs: align `solr-8240-submit` and `solr-10424-submit` with the live heads before any push. Approval needed for any reset.
7. Verify batch: decide whether `SolrExampleTests` (solrj) is run before submission.
8. Confirm the 8240 premise base commit (`e432df19c4a`) and the 2026-10-05 date (Findings 7 and 8).
9. Inventory row for 10424 (Finding 10): lead's update.

## Not checked

- The gate logs named in the receipts (`g8240-gate.log`, `g8240-premise-df.log`, `g10424-gate.log.fix`, `g10424-premise-head.log`, `g10424-premise-base.log`) were not found under `research/`. I searched `research/` for names containing "8240" and "10424." I did not search elsewhere on disk. All proof numbers come from the receipts.
- Test result JSON for the two tickets was not found by the same search.
- The GitHub run IDs in the receipts (37402547404 for 8240; 37739524791 for 10424) were not checked. I made no gh calls. No PR number is known for either branch, and the claim allows gh only for a live-PR pass.
- Whether a PR exists for either branch: not checked.
- Nothing was run. The 400 path, the inherited-default behavior, the unknown-field refusal, and the sample's indexed output come from reading code and schema, not from a run.
- `JsonRecordReader` was read through `getInst` only. I did not trace it through the streaming parser.
- Live upstream main was not fetched. The local ref `8e62c2686882` may differ.
- The v2 `/update` path also uses `_UPDATE_JSON_DOCS` (`ImplicitPlugins.json` line 38). I read it as a reference only and did not trace it.
- The changelog fragments were checked against the type list in the changelog directory, not against `dev-docs/changelog.adoc`.
- The tutorial in `tutorial-techproducts.adoc` (lines 182-192) posts `books.json` to `/json/docs`. I read its console text only and did not check it against the new field behavior.
