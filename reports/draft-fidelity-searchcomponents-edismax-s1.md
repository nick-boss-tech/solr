# Search components draft fidelity, slice s1

Assignment: `assignments/pool-draft-fidelity-searchcomponents-edismax.md`. Claim: `claims/pool-draft-fidelity-searchcomponents-edismax.md`, slice C1 (SOLR-10424, 10492, 10694, 10844, 11129). Worktree `wt/pr-prepare-suggester` at d627304e96b, as the spawn message says (the brief's e84522fa5bc is the earlier claim commit).

Sources: drafts in `pr-drafts/search-components/SOLR-<n>.md`; receipts in `receipts/SOLR-<n>.md` (all five present). Limits and choice content checked against `reports/search-components-1.md` with parts `-f1` (SOLR-10844) and `-f2` (SOLR-10492, SOLR-11129), plus `-f3` item 10 and owner decision 2 for SOLR-11129; `reports/search-components-3-w3.md` (SOLR-10424); `reports/search-components-4-s6.md` (SOLR-10694, items 19 to 22 and 99). Answers material: a grep of `material/` for the five ticket numbers returned no hits. JIRA packets in `research/jira-context/` were read for the problem statements only.

Head check: `git ls-remote origin refs/heads/solr-<n>-submit` per draft. All five live tips match the head each draft names.

## Verdicts

| Draft | Head checked (ls-remote) | Verdict |
|---|---|---|
| SOLR-10424 | solr-10424-submit = e629ab8bb293eb54249b426b69a559f1da0f08d7 (match) | DRIFT (2 items) |
| SOLR-10492 | solr-10492-submit = ecf21e2192cb91e1ba4a44abce2c43ab8a5409b9 (match) | DRIFT (3 items) |
| SOLR-10694 | solr-10694-submit = 64e86811548ba1a9dbc5b2ce2ec9c33983f63c15 (match) | DRIFT (3 items) |
| SOLR-10844 | solr-10844-submit = e87515c56d048f96850d4586d6e0508dc177e4c7 (match) | DRIFT (3 items) |
| SOLR-11129 | solr-11129-submit = 6c1356bdff706328a8b78d39782f9e94845827d4 (match) | DRIFT (4 items) |

Checks that passed for all five: each head object exists in the worktree; each merge-base with the local upstream/main ref equals the base the draft or round report names (cabedd1d968 for 10424 and 10694, e2cdb2d7e8a for 10492, 22a8cfebbbdb for 10844, 14c7aac0d15 for 11129); every Proof count matches its receipt; each verification date is present (10424 2026-10-08, 10492 2026-10-05, 10694 2026-10-09, 10844 2026-10-05, 11129 2026-10-05); each changelog fragment exists at its head; no em dash in any draft; the AI header and footer are present; no gate, receipt, ledger, seed, claim, pool, assignment, subagent or submission wording in the visible text. The only internal wording is in HTML comments, flagged below.

## SOLR-10424

Verdict: DRIFT (2 items).

1. Draft says: the "What happens today" mechanism paragraph links the implicit handler, solrconfig and schema lines at head `e629ab8bb293`, and only the params.json link says "at the base".
   Evidence: these lines describe the pre-change symptom. ImplicitPlugins.json L28-L31 (`/update/json/docs` uses the set), solrconfig.xml L650-L654 (`initParams` df=text), managed-schema.xml L180 (`text` not stored). The branch changes only params.json, the test and the changelog (`git diff --stat cabedd1d968 e629ab8bb293`), so the same lines exist at the merge-base. `pr-formula.md`: citations to the pre-change symptom "link the merge-base commit, and the text says so".
   Replacement (whole paragraph, replaces the second paragraph under "What happens today"): "Before this change, the sample's `params.json` set `mapUniqueKeyOnly` to true in the `_UPDATE_JSON_DOCS` set ([params.json at the base](https://github.com/nick-boss-tech/solr/blob/cabedd1d968059215188f4e7563fb303241899ed/solr/server/solr/configsets/sample_techproducts_configs/conf/params.json#L3-L5)). The `/update/json/docs` handler reads that set ([implicit handler at the base](https://github.com/nick-boss-tech/solr/blob/cabedd1d968059215188f4e7563fb303241899ed/solr/core/src/resources/ImplicitPlugins.json#L28-L31)). The sample's default `df` is `text` ([solrconfig at the base](https://github.com/nick-boss-tech/solr/blob/cabedd1d968059215188f4e7563fb303241899ed/solr/server/solr/configsets/sample_techproducts_configs/conf/solrconfig.xml#L650-L654)). So the other values go into `text` with no field name. That field is indexed but not stored ([schema at the base](https://github.com/nick-boss-tech/solr/blob/cabedd1d968059215188f4e7563fb303241899ed/solr/server/solr/configsets/sample_techproducts_configs/conf/managed-schema.xml#L180)), so no field is indexed under its own name."

2. Draft says: "- [SOLR-8240](https://issues.apache.org/jira/browse/SOLR-8240) changes how `mapUniqueKeyOnly` treats `f`. Until this default is gone, requests to the sample that send `f` fail under that change, so the two are best landed together."
   Evidence: `reports/search-components-3-w3.md` FIX 3 and owner decision 1 recommend landing SOLR-10424 before or with SOLR-8240, because SOLR-8240 alone turns every `f` request to the sample into a 400. "Best landed together" does not state the order.
   Replacement: "- [SOLR-8240](https://issues.apache.org/jira/browse/SOLR-8240) changes how `mapUniqueKeyOnly` treats `f`. Until this default is gone, requests to the sample that send `f` fail under that change, so this change should land before, or together with, SOLR-8240."

Checked, no drift: Proof (TechproductsJsonDocsParamsTest 1 of 1 at head; base cabedd1d968 fails on the mapUniqueKeyOnly assertion; receipt lines 6 to 8). The test lines L30-L42 match the method. The changelog link resolves at e629ab8bb293 and its title matches the change. Choice has a live alternative (named id-only set) and a pointed question. Limits match w3 findings 5 and 6 (solrj test not run; unknown-field failure untested).

Optional notes, not blocking: the Limits section has no bold one-line summary (presentation rule, 2026-10-08). The body is about 4.3 KB with URLs, above the roughly 3,500-character guide.

## SOLR-10492

Verdict: DRIFT (3 items).

1. Draft says: "- facet.query with group.facet=true and group.field, but without group=true, still returns BAD_REQUEST. This change does not touch it."
   Evidence: SimpleFacets `global` is the request parameters (constructor L167-L168; FacetComponent L266-L267 passes `rb.req.getParams()`). facet.query calls `getGroupedFacetQueryCount` when group.facet is true (L316-L317). That method reads `global.get(GroupParams.GROUP_FIELD)` at L330, and the BAD_REQUEST follows it. A request-level group.field therefore returns grouped counts for facet.query, at base e2cdb2d (same read at L330) and at head. The throw happens only when group.field is given only as a local parameter. The facet.field fallback (L796) also reads request parameters only, so the local-only case throws for facet.field too. The Jira reporter's description says facet.query returned a grouped count (8) (`research/jira-context/SOLR-10492.json`). `reports/search-components-1-f2.md` Finding 5 makes the same error, so its owner decision 3 (make facet.query symmetric) is not needed.
   Replacement: "- A group.field given only as a local parameter, with group.facet=true and without group=true, still returns BAD_REQUEST for facet.field and facet.query. This change does not change that case."

2. Draft says: "Changelog: `changelog/unreleased/SOLR-10492-group-facet-refinement-counts.yml`"
   Evidence: the changelog line must be a link to the fragment at the head SHA (`pr-formula.md`, rules for filling it in). The fragment exists at ecf21e2192cb and its title matches the refinement change.
   Replacement: "Changelog: [`changelog/unreleased/SOLR-10492-group-facet-refinement-counts.yml`](https://github.com/nick-boss-tech/solr/blob/ecf21e2192cb91e1ba4a44abce2c43ab8a5409b9/changelog/unreleased/SOLR-10492-group-facet-refinement-counts.yml)"

3. Draft says (last line): "<!-- Drafted against head ecf21e2192cb91e1ba4a44abce2c43ab8a5409b9 (origin/solr-10492-submit). Remove this comment before posting. Owner items before posting: Finding 3 (squash the handoff-doc commits) and the owner call on Findings 5 and 6. -->"
   Evidence: internal review wording ("Owner items", "Finding 3", "Findings 5 and 6") sits in the draft text. `reports/search-components-1-f2.md` lists these as owner items, not public text.
   Replacement: delete the whole line (no replacement text).

Checked, no drift: Proof (TestDistributedGrouping 2 of 2, TestGroupingSearch 17 of 17, DistributedFacetPivotSmallTest 1 of 1, verified 2026-10-05; receipt lines 3 to 7). The new test case uses group=true, facet.limit 1 and 2, and overrequest off (TestDistributedGrouping.java, the 58-line insert). Base e2cdb2d L979 is the `numDocs` line the draft cites. Head L792-L797, L979-L996 and L1005-L1028 hold the cited code. The facet.field behavior-change bullet matches the receipt. The "What happens today" figures (facet.limit=50, 8, 233) are in the Jira packet, not the receipt, and match it. Limits on test coverage match f2 Finding 6.

Optional notes, not blocking: the StoredFieldsShardRequestFactory link (L66) points at e2cdb2d, which is the base, but the label does not say so. The file is unchanged by the branch, so the link is valid. Optional label: "[StoredFieldsShardRequestFactory.java L66, at the base](https://github.com/apache/solr/blob/e2cdb2d7e8ae0be4cf6cf0606206c67d89e7278f/solr/core/src/java/org/apache/solr/search/grouping/distributed/requestfactory/StoredFieldsShardRequestFactory.java#L66)". Plain words: "refinement", "overrequest" and "numDocs" appear without a gloss.

## SOLR-10694

Verdict: DRIFT (3 items).

1. Draft says: "The base class handles these values in methods that write nothing for CSV: [writeNamedList](...)#L121, [writeMap](...)#L136, and [writeArray](...)#L139 and [writeArray](...)#L142." All four links point at head `64e86811548`.
   Evidence: these stubs are the pre-change symptom. TabularResponseWriter.java is not in the branch diff stat, and the same stub lines appear at the merge-base cabedd1d968 (121, 136, 139, 142). The text does not say base. `pr-formula.md` requires the merge-base link for pre-change symptoms.
   Replacement: "The base class handles these values, at the merge-base, in methods that write nothing for CSV: [writeNamedList](https://github.com/nick-boss-tech/solr/blob/cabedd1d968059215188f4e7563fb303241899ed/solr/core/src/java/org/apache/solr/response/TabularResponseWriter.java#L121), [writeMap](https://github.com/nick-boss-tech/solr/blob/cabedd1d968059215188f4e7563fb303241899ed/solr/core/src/java/org/apache/solr/response/TabularResponseWriter.java#L136), and [writeArray](https://github.com/nick-boss-tech/solr/blob/cabedd1d968059215188f4e7563fb303241899ed/solr/core/src/java/org/apache/solr/response/TabularResponseWriter.java#L139) and [writeArray](https://github.com/nick-boss-tech/solr/blob/cabedd1d968059215188f4e7563fb303241899ed/solr/core/src/java/org/apache/solr/response/TabularResponseWriter.java#L142). The cell prints nothing, so the next value is written in that cell's column."

2. Draft says: "- The ticket asks for all response writers. The XML, Python, PHP, Ruby and Velocity writers are not changed here, and they were not checked for the same gap. I can open a follow-up ticket and PR for them on request."
   Evidence: `reports/search-components-4-s6.md` items 21 and 99 list the writers not checked as XML, JSON, Python, PHP, Ruby and Velocity. The draft leaves JSON out. The ticket says JSON and javabin already serialize these types, so JSON belongs in the not-checked list as s6 has it, not as a known gap.
   Replacement: "- The ticket asks for all response writers. The XML, JSON, Python, PHP, Ruby and Velocity writers are not changed here, and they were not checked for the same gap. I can open a follow-up ticket and PR for them on request."

3. Title mismatch (branch changelog, `changelog/unreleased/SOLR-10694-csv-structured-values.yml`, title line at 64e86811548). The changelog says "map, NamedList and iterator values". The draft's "What this change does" says "map, NamedList, iterator or array value", and the code covers arrays (CSVResponseWriter.java L454-L465, `val instanceof Object[]`). The fix is on the branch changelog, not in the draft.
   Replacement (branch changelog title line): "title: The CSV response writer writes map, NamedList, iterator or array values of a document as a JSON cell instead of dropping the cell and shifting the following columns."

Checked, no drift: Proof (TestCSVResponseWriter 4 of 4 at head, verified 2026-10-09; receipt lines 3 to 7). On the base code the new test `testStructuredValuesAreWrittenAsJsonCells` (TestCSVResponseWriter.java L359-L397) fails with one failure. Its three documents (map, NamedList, none) match the draft. CSVResponseWriter L418, L436 and L441 are the three call sites the draft cites. The changelog link resolves at 64e86811548. The Limits on untested iterator, array and multivalued-map paths match s6 item 22.

Optional notes, not blocking: `reports/search-components-4-s6.md` (and the roll-up `reports/search-components-4.md` line 20) says "No gate; audit only" and "Not drafted", and records the head as moved from 093d90c62de. The receipt now records a green gate at 64e86811548 and the draft exists, so the round status is stale. About 3.8 KB with URLs.

## SOLR-10844

Verdict: DRIFT (3 items).

1. Draft has no matching sentence. The changelog link carries the mismatch. The branch changelog (`changelog/unreleased/SOLR-10844-group-facet-numeric-bad-request.yml` at e87515c, title line) says "instead of empty counts or an internal error". The draft correctly omits "empty counts", and no receipt supports it (receipt lines 6 to 7 show only "expected 400 was 500" and the IllegalStateException). `reports/search-components-1-f1.md` FIX 2.
   Replacement (branch changelog title, folded scalar): "Grouped faceting (group.facet) on an unsupported numeric facet or group field (points based, docValues, or multi-valued) now fails with a clear 400 error."

2. Draft links SimpleFacets.java L799-L818 at e87515c. Lines 799-804 (the comment inside that link) say the collector "needs SORTED docValues on both fields" and that the failing cases "used to return empty counts". Lucene 10.4.0 uses SORTED_SET for a multi-valued facet field, and no record supports "empty counts" (`reports/search-components-1-f1.md` FIX 1).
   Replacement (branch code, replaces lines 799-804 at e87515c; lines 805-818 stay): 
   ```
       // SOLR-10844: the grouped facet collector cannot read numeric doc values or points fields, and
       // multi-valued numeric fields are not supported here. Reject these with a clear 400.
   ```

3. Draft says: "A Points facet field without doc values already returned a 400 before this check ([L471-L474](https://github.com/nick-boss-tech/solr/blob/e87515c56d048f96850d4586d6e0508dc177e4c7/solr/core/src/java/org/apache/solr/request/SimpleFacets.java#L471-L474))."
   Evidence: this describes pre-change behavior, and the branch's one-line import shifts the lines. At the merge-base 22a8cfebbbdb the same check is at L470-L473. `pr-formula.md`: the merge-base link, with the text saying so.
   Replacement: "A Points facet field without doc values already returned a 400 before this check ([L470-L473 at the merge-base](https://github.com/nick-boss-tech/solr/blob/22a8cfebbbdb026437d10b0aa47bb66ffdb23a9a/solr/core/src/java/org/apache/solr/request/SimpleFacets.java#L470-L473))."

Checked, no drift: head (live match). Proof: SimpleFacetsTest 49 tests, 0 failures, 0 errors, 1 skipped; Points and Trie with docValues fail with "expected 400 was 500" on base; Trie without docValues passes on base; forced configurations pass; verified 2026-10-05 (receipt lines 4 to 9). The new test (L609-L667, `testGroupFacetOnNumericFieldIsBadRequest`) matches the description. The guard (L805-L818) and its "before the collector" placement match. Choice: 400 now versus numeric support in the collector, a live alternative with a cost, with a pointed question. It matches f1 owner decision 3 and receipt line 9. Limits match f1 Findings 4 to 6 and the distributed-not-tested line. The Jira packet supports the grouping-on-Points and single-valued Trie with docValues claims (`research/jira-context/SOLR-10844.json`, description and comment 16056349).

Optional notes, not blocking: the Limits section has no bold one-line summary (presentation rule, 2026-10-08). The Choice sits near the narrow-scope line in `pr-formula.md` section 4. It stays, because the 400 route carries a behavior cost. About 3.6 KB with URLs.

## SOLR-11129

Verdict: DRIFT (4 items).

1. Draft says: "Changelog: `changelog/unreleased/SOLR-11129-facet-mincount-local-param.yml`"
   Evidence: the changelog line must be a link to the fragment at the head SHA. The fragment exists at 6c1356bdff70, and its title matches the draft's first bullet (mincount only, which is the default in `reports/search-components-1-f2.md` owner decision 6).
   Replacement: "Changelog: [`changelog/unreleased/SOLR-11129-facet-mincount-local-param.yml`](https://github.com/nick-boss-tech/solr/blob/6c1356bdff706328a8b78d39782f9e94845827d4/changelog/unreleased/SOLR-11129-facet-mincount-local-param.yml)"

2. Draft says (What this change does): "- facet.offset is read from local params too. See Limits." and (Limits): "- facet.offset: the shards apply a local facet.offset, and the coordinator reads it too, so a distributed request with a local offset applies the offset twice. No test covers this, and it is not fixed here."
   Evidence: FacetComponent.java L1403 at 6c1356bdff70 reads `this.offset = params.getFieldInt(field, FacetParams.FACET_OFFSET, 0);` from the local-first view. The shards apply the local offset too, so the coordinator applies it again (`reports/search-components-1-f2.md` Finding 1, read by code, not run). The draft presents the double application as shipped behavior. The round roll-up (`reports/search-components-1.md` line 19 and owner decision 4) and the draft's own HOLD comment say the fix or hold comes first. The recommended path is the one-line request-level read, with the offset stated as a Limit. Branch-side change for that path: FacetComponent.java L1403 becomes `this.offset = rb.req.getParams().getFieldInt(field, FacetParams.FACET_OFFSET, 0);`, with the comment "a local facet.offset is applied by the shards, so the coordinator keeps the request value here".
   Replacement, part a (What this change does): delete the bullet "- facet.offset is read from local params too. See Limits." (no replacement text).
   Replacement, part b (Limits): "- A local facet.offset is still not honored at the coordinator. The shards apply it, so a distributed request with a local offset can return the wrong page. Not changed here."
   If the owner takes a full fix with a focused test instead, both parts change, and the Proof needs that test's counts.

3. Draft says: "- Field facet settings are read from the local params first, then from the request ([FacetComponent.java L1397](https://github.com/nick-boss-tech/solr/blob/6c1356bdff706328a8b78d39782f9e94845827d4/solr/core/src/java/org/apache/solr/handler/component/FacetComponent.java#L1397))."
   Evidence: the code is `SolrParams.wrapDefaults(localParams, rb.req.getParams())` (L1397). A per-field request setting beats a plain local value, because the `f.<field>.` name is checked first (`reports/search-components-1-f3.md` items 5 and 10). The branch comment at L1396 overstates the order in the same way (f3 item 10). Owner decision 2 in f3 may change the order, which would change this bullet too.
   Replacement: "- Field facet settings from local params take precedence over plain request params, as they do on the shards. A per-field request setting, such as `f.<field>.facet.mincount`, still wins over a local value ([FacetComponent.java L1397](https://github.com/nick-boss-tech/solr/blob/6c1356bdff706328a8b78d39782f9e94845827d4/solr/core/src/java/org/apache/solr/handler/component/FacetComponent.java#L1397))."
   Branch-side comment at FacetComponent.java L1396 (replaces the current comment): "// local params (e.g. {!facet.mincount=1}fld) take precedence over plain request params, as they do on the shards; per-field request params (f.fld.facet.mincount) still win"

4. Draft ends with an HTML comment: "<!-- HOLD: do not post. Drafted against head 6c1356bdff706328a8b78d39782f9e94845827d4 (origin/solr-11129-submit). Finding 1 in the report must be decided first. ... Remove this comment before posting. -->"
   Evidence: `reports/search-components-1.md` "Pre-post cleanup in drafts": the HOLD comment must come out, and the draft cannot post until the offset fix lands.
   Replacement: delete the whole comment line (no replacement text).

Checked, no drift: head (live match). Proof: DistributedFacetLocalParamsMinCountTest 1 of 1, DistributedFacetExistsSmallTest 1 of 1 and DistributedFacetPivotSmallTest 1 of 1 at head; base 14c7aac0d15 fails the new test; verified 2026-10-05 (receipt lines 3 to 7). L907-L911 hold the coordinator minimum-count drop (`ent.getValue().minCount`), as f2 Finding 2 describes. The facet.zeros bullet matches f2 Finding 2. The Limits on untested local params match f2.

Optional notes, not blocking: f2 owner decision 6 (wider changelog title) is still open. The draft has no choice section. f3 owner decision 2 says the precedence call also covers SOLR-11129. If the owner lets the plain local value win, bullet 3 changes and a choice section may be needed.

## Notes for the lead (round reports, not draft text)

- `reports/search-components-1-f2.md` Finding 5 and owner decision 3 rest on the error described under SOLR-10492, item 1. Close owner decision 3 as not needed, and correct Finding 5.
- The branch-side items still open at the live heads, confirmed by reading the code: SOLR-10844 changelog title and code comment (SOLR-10844 items 1 and 2); SOLR-10694 changelog title (SOLR-10694 item 3); SOLR-11129 FacetComponent.java L1403 offset read and L1396 comment (SOLR-11129 items 2 and 3).
- `reports/search-components-4-s6.md` status for SOLR-10694 is stale (see the optional note above).

## Not done

- No build, test, Gradle, gate or test-queue run. No `gh` call, so the live PR state of these five was not read. No live JIRA read (local packets only). No commit, push, ref change, or edit outside this report.
- Gate and premise logs named in the receipts are not on disk, so Proof counts are receipt-only.
- Behavior claims (SOLR-11129 offset double application, SOLR-10492 local-only BAD_REQUEST, SOLR-10424 unknown-field failure, SOLR-10844 group-field rejections) are code reads, not runs.
- Merge-bases were computed against the worktree's local upstream/main ref, which may be behind upstream. They match the receipts and round reports that name them.
- Lucene behavior was not re-checked beyond what the round reports record. Distributed behavior and the SolrExampleTests and SimpleFacetsTest queue questions were not assessed beyond what the rounds record.
