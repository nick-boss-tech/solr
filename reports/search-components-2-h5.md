# Part h5 report: rerank and consistency (SOLR-11470, 14451, 17539)

Result: 11470 and 14451 are draftable (drafts written), but both fork branches need a history cleanup before any PR (a Claude co-author trailer and internal handoff commits); 17539 passes the consistency pass on code and receipt, with one label FIX and several description NOTEs for the owner.

## Heads checked

Read only: `git ls-remote origin` on 2026-10-09, then the fork tracking refs.

| Ticket | Live fork head | Receipt head | Local branch ref in the shared repo |
|---|---|---|---|
| 11470 | f44c294da37 | f44c294da37 (matches) | 11857d1325d, 3 commits behind the live head |
| 14451 | e60891d8716 | e60891d8716 (matches) | fa3ec91f355, 2 commits behind |
| 17539 | f9d201a278b | bce505f45ac gated; live tip f9d201a278b is one docs commit past it (flagged, not resolved) | bce505f45ac, 1 commit behind |

The local refs are stale. The audit used the fork tracking refs, which match the live heads.

## Findings

1. FIX (11470, branch history). Three commits on `origin/solr-11470-submit` carry a Claude co-author trailer: 462345f78e1 (the fix), 5912508c530 (changelog), 11857d1325d (handoff doc). Evidence: `git log upstream/main..origin/solr-11470-submit --grep Co-Authored-By -i` lists all three, each with `Co-Authored-By: Claude Sonnet 5.5 <noreply@anthropic.com>`. Replacement: in a history rewrite (needs owner go-ahead; not done), remove that trailer line from those three messages, or squash the branch into commits without it. Standing rule: no Claude trailers on pushed commits.

2. FIX (11470 and 14451, branch history). Handoff commits are in the history. 11857d1325d "SOLR-11470: add hypothetical-reproduction handoff doc" adds `SOLR-11470-TESTING.md`, removed by f44c294da37. fa3ec91f355 "SOLR-14451: add hypothetical-reproduction handoff doc" adds `SOLR-14451-TESTING.md`, removed by e60891d8716. The final trees are clean (`git diff --stat upstream/main...origin/<branch>` shows no .md file). The subjects are internal vocabulary and show in a PR's commit list. Replacement: drop those two commits in the same rewrite as item 1. Owner decision.

3. FIX (17539 live body, needs owner go-ahead; check first whether a maintainer is commenting on #4998). The Proof says "The pre-fix proof is inconclusive by construction". Evidence: `gh pr view 4998` body, headRefOid f9d201a278b. The shared rules name "pre-fix proof as a label" as internal vocabulary. Replacement sentence: "The shipped tests cannot compile against the base code, because they call isCaching(), isDocValuesIteratorCacheEnabled() and createDocValuesIteratorCache(), which this PR adds. So the proof is inconclusive by construction."

4. NOTE (11470, wider behavior). `solr/core/src/java/org/apache/solr/handler/component/ResponseBuilder.java` lines 480-488 at f44c294da37 call `QueryUtils.makeQueryable` for every main query when `rankQuery` is set. `makeQueryable` unwraps a `WrappedQuery` (`QueryUtils.java` lines 185-189 on upstream/main), and that wrapper carries `cache` and `cost` local params (`QParser.java` lines 225-237 and 248). So under `rq`, a non-negative main query with `cache` or `cost` loses its wrapper. No test covers this, and I did not check whether base honors those params under `rq`. The draft states it under "What this change does". Narrowing it needs its own change; no replacement written.

5. NOTE (11470, Choice). The ticket (`research/jira-context/SOLR-11470.json`, "How to fix") proposes a `RankQuery#getMainQuery` accessor used in `makeQueryable`. The implemented route differs. The draft poses this as its "A choice to check" section. Owner decides whether to keep it.

6. NOTE (11470, Lucene and version claims). `TestReRankQParserPlugin.java` lines 305-308 say that under luceneMatchVersion before 10.2.0 the lucene parser behaves the same way. Checked on main: `QParser.java` lines 113-118 (gate on `Version.LUCENE_10_2_0`) and `SolrQueryParser.java` lines 31-35 (auto-fix in `getBooleanQuery`). Checked on the 9.x line (`upstream/branch_9x`): `QParser.java` has no `autoFixPureNegative` and `SolrQueryParser.java` has no `getBooleanQuery` override, so the comment holds for main only. The comment at `ResponseBuilder.java` lines 482-483 says a pure negative query "would otherwise rewrite to MatchNoDocsQuery". Not checked against Lucene 9.x or 10.x source (none in these trees). The draft does not repeat it. No code change requested.

7. NOTE (11470, changelog wording). `changelog/unreleased/SOLR-11470-negative-query-with-rq.yml` lines 8-9 say "so it no longer returns no results when the query parser itself did not fix the query". Unclear. Optional replacement: "A pure negative main query is made searchable before it is wrapped in a rank query, so rank queries no longer return no results for it."

8. NOTE (14451, round 28 P2 is addressed at head). `research/branch-reviews/round-28/SOLR-14451-review.md` P2 asked for the condition to be narrowed or tested. Head e60891d8716 now uses `if (facetRequest && rb.isDebugQuery())` (`DebugComponent.java` line 187), and `DebugComponentTest.java` lines 205-211 assert the results and timing modes. No replacement needed. The option semantics stay the owner's call (see Owner decisions).

9. NOTE (17539 receipt, wording findings). `receipts/SOLR-17539.md` line 10 says the round 9 review's four description-wording findings "remain" open. The live body already carries wording for findings 1 to 4 of `research/branch-reviews/round-8/SOLR-17539-review.md` (lines 33-64), matched by text: the Proof names the accessors and factory instead of a constructor; Limits says the heap effect is inferred, not measured; the body says "the two request paths" and Limits names UpgradeCoreIndex; Limits gives the four-array, per-field-per-document cost. Finding 5 (no Config API behavioral test) is still open. The receipt calls it "round 9", but `research/branch-reviews/round-10/README.md` line 5 says no round 9 folder exists locally. Replacement for the receipt line: "Wording findings 1 to 4 are reflected in the live body; the Config API test finding (5) is open." Main side to correct.

10. NOTE (17539 Proof drift). The live Proof has no head SHA or verification date. It omits the two probes in `receipts/SOLR-17539.md` line 8 and the neighbor TestRealTimeGet 4 of 4 in line 7. Replacement sentence to add after the counts: "Verified at bce505f45ac on 2026-10-05. The later commit f9d201a278b changes only the reference guide page. On the base code, a reflective variant of the new test fails 2 of 2, and the shipped TestConfigOverlay fails 1 of 2 on its new assertion. TestRealTimeGet passes 4 of 4 at the head."

11. NOTE (17539 A choice). The live section ends with an offer, not a pointed question. Replacement final sentence: "Was the switch the right call, or should the cache be bounded instead?"

12. NOTE (17539 changelog line). The body says "Changelog: changelog entry under changelog/unreleased (added)", with no file name and no link. Replacement: "Changelog: [changelog/unreleased/SOLR-17539.yml](https://github.com/nick-boss-tech/solr/blob/f9d201a278b9418ae71d8123ff9269edccd7776a/changelog/unreleased/SOLR-17539.yml)"

13. NOTE (17539 Limits). The Config API path has no test that sets the option and reads it after the reload (round 8 finding 5, lines 66-69). The body states the Config API behavior without a caveat. Replacement sentence for Limits: "No test sets query.enableDocValuesIteratorCache through the Config API and checks it after the reload."

14. NOTE (17539 CI evidence). Receipt line 9 cites runs 37351659760, 37572168384 and 37578751893. `gh pr checks 4998` (read 2026-10-09) lists runs 37578152252, 37578154048, 37578154082, 37578154148 and 37578154163, all pass or skipping. Run 37578751893 is not in that list, and `pr checks` prints no head SHA, so the head of the listed runs is not confirmed. Replacement: main side confirms each run's head SHA on the Actions page, then cites those run IDs. Round 30 finding 1 (only labeler had reported) no longer holds on the current checks.

15. NOTE (17539 head difference). Gated bce505f45ac against live f9d201a278b. `git diff --stat bce505f..f9d201a` shows one file, `caches-warming.adoc`, 1 insertion and 1 deletion (live line 274). Code and tests are identical. Flagged, not resolved. No replacement; the receipt already says so.

16. NOTE (17539 formula drift, low). The body has no bold one-line summary per section, uses `###` headings, and links no file. Owner choice when the description is edited for items 3 and 10 to 13.

17. NOTE (local refs). Local branch refs in the shared repo lag the fork tips: `solr-11470-submit` (11857d1325d, 3 behind), `solr-14451-submit` (fa3ec91f355, 2 behind), `solr-17539-submit` (bce505f45ac, 1 behind). A push from them would be rejected or would reset the branch. Not changed. Fast-forward only when authorized.

18. NOTE (interactions, no textual conflicts). Trial merges with `git merge-tree --write-tree` (no refs written) show no conflicts for 17539 against solr-8009-submit, solr-8767-submit, solr-8954-submit and solr-15018-submit; for 11470 against 14451, 17539, 15479 and 11310; and for 14451 against 17539. 17539's only `RealTimeGetComponent.java` change is one line at head line 354 (`reuseDvIters = docFetcher.createDocValuesIteratorCache();`). The nearest h1 hunks are at base lines 279-296 (8767), 66 and 1060-1090 (8009 and 8954), and 877-888 (15018). Whether 8767's `/get` response change reaches line 354 is not checked; h1 should confirm. 11470 and SOLR-15479 both add tests to `TestReRankQParserPlugin.java` (15479's test names do not collide with 11470's) and both touch the rerank path: 15479 edits `SolrIndexSearcher.java`, 11310 edits `ReRankCollector.java`. Relation noted only; not audited here.

## Task results

**SOLR-11470: draftable.** Draft at `pr-drafts/search-components/SOLR-11470.md`, written against f44c294da37. The live head equals the receipt head. The branch diff matches the receipt: one line in `ResponseBuilder.wrap`, one changelog file, and two new tests in `TestReRankQParserPlugin`. Method counts in source agree with the receipt: 13 tests in `TestReRankQParserPlugin` (11 on main plus 2 new) and 113 in `QueryEqualityTest`. The Proof rests on the `{!bool}` test, as the receipt requires. Not ready for a PR until items 1 and 2 are handled. The draft is about 3,900 characters with links, a little over the guide.

**SOLR-14451: draftable.** Draft at `pr-drafts/search-components/SOLR-14451.md`, written against e60891d8716. The receipt head equals the live head. `DebugComponentTest` has 7 test methods, matching 7 of 7. `TestCloudJSONFacetSKGEquiv` has 7 test methods, matching the receipt's 7, but no base run is recorded, and the draft says so. The round 28 P2 is addressed at head (item 8). The Choice section poses the option question as the owner's call. Not ready for a PR until item 2 is handled.

**SOLR-17539 (live PR #4998): consistency pass, passes with FIX and NOTEs.** PR state OPEN, mergeable MERGEABLE, mergeStateStatus CLEAN, headRefOid f9d201a278b (matches the live tip). Branch and receipt agree on the code; the only difference is the docs line (item 15). The body's claims match the code: default `true` (`boolVal(true)`), both bundled configsets set the option, UpgradeCoreIndex still builds a caching instance (line 173 at head), and the counts (2 of 2, 9 of 9, 2 of 2) match the receipt. Description drift is in items 3 and 10 to 13 and 16. No new PR text written, per the claim.

## Owner decisions

1. Authorize a history rewrite of `solr-11470-submit` and `solr-14451-submit`: remove the Claude trailer and drop the two handoff commits (items 1 and 2). Pushing the rewrite to the fork needs a force push. Not done.
2. 11470: keep or drop the Choice section (ticket accessor route against the implemented route). Also state the wrapper behavior in the description (the draft does) or narrow the change in a new commit (item 4).
3. 14451: confirm the wording of the option question. Implemented: facet shard requests get `debug=query` only when query debugging is on. Alternative: forward every debug mode. The draft's Choice section poses this.
4. 17539: authorize description edits for items 3 and 11 to 13, after checking whether a maintainer is commenting on #4998. Decide whether to add the Config API test (round 8 finding 5) or keep it stated as untested.
5. Main side: correct `receipts/SOLR-17539.md` lines 9 and 10, and confirm the run head SHAs on the Actions page (items 9 and 14).
6. Local refs: fast-forward them or leave them. Do not push from them (item 17).

## Not checked

- No builds, Gradle, or tests, per the shared rules. Proof counts were checked only against test method counts in source and against the receipts. Gate logs and JUnit XML are not on disk.
- GitHub run IDs and their head SHAs, beyond the `gh pr checks 4998` listing. The run IDs in the 11470 receipt (37324713458), the 14451 receipt (37655127276) and the 17539 receipt (37351659760, 37572168384, 37578751893) are not verified.
- Lucene rewrite behavior (`MatchNoDocsQuery`) against Lucene 9.x or 10.x source. No Lucene source is in these trees.
- JIRA text was read from `research/jira-context/` packets on disk, not re-fetched. The ticket may have changed since.
- The round 9 review for 17539 is not on disk. The round 8 file was matched by text.
- The 17539 `getDocValues()` OOM claim and the heap effect were not re-derived.
- Whether base honors `cache` or `cost` on a main query under `rq` (item 4).
- The semantic effect of 17539 on the `/get` path that 8767 changes (item 18).
- Maintainer comments on PR 4998 (not among the allowed `gh` calls).
- The changelog YAML parse for 11470 and 14451 (the receipts say it parses; the live CI changelog check ran only on 17539).
- The Config API reload claim: consistent with the reload path in `SolrConfigHandler.java` lines 255-268 on upstream/main, not run.
- Trial merges wrote no refs, but may have written loose tree objects.
