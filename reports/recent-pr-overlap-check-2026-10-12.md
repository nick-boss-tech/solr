# Recent PR overlap check, 2026-10-12

Read-only check run 2026-10-12 (main side). Question: do recently opened pull requests on apache/solr overlap with this side's work, by ticket, by area, or by file.

## Window and counts

- Source: GitHub API, apache/solr pull requests, state all (open and closed), sorted by creation date, paginated in full.
- Window: created on or after 2026-08-15. Pull requests pulled: 374.
- Excluded as ours: 68 pull requests authored by nick-boss-tech. Checked against the rest: 306.
- Our key set: every SOLR number with a file under receipts/ on pr-prepare (321 tickets), plus the three new tickets SOLR-18539, SOLR-18540 and SOLR-18541. 324 keys in total.
- Changed files were read from the API for all 306 checked pull requests (the file sweep behind classes B and C).

## Class A: same ticket (6)

A non-ours pull request whose title names a SOLR key in our set.

1. **PR #5065, OPEN, highest priority.** "SOLR-18010: Fix security.json corruption from parallel edits", by sarkaramrit1993, created 2026-10-09, head branch SOLR-18010-security-json-corruption, base main, still open. This is the same ticket and the same defect as this side's branch solr-18010-submit, which is GATE GREEN at c3685bb37d9d (receipts/SOLR-18010.md) and not yet opened. Two implementations of one fix exist; theirs is the open PR.
2. **PR #4782, merged 2026-08-24.** "SOLR-18368: remove CloudSolrClient.Builder.withInternalClientBuilder", by serhiy-bzhezytskyy, created 2026-08-21. Same ticket as this side's SOLR-18368 salvage branch (receipts/SOLR-18368.md). The merged change removes a deprecated builder method; this side's live tip is a docs-only change to the basic authentication guide page, so the two do not collide in files, but any new PR under SOLR-18368 opens on a ticket upstream has already acted on.
3. **PR #4779, merged 2026-08-24.** "SOLR-18109: move SolrPluginUtils debug methods to DebugComponent", by serhiy-bzhezytskyy, created 2026-08-21. Same ticket as this side's solr-18109-submit, a test-only salvage that is GATE GREEN at b19395e1f60a (receipts/SOLR-18109.md). The production move is upstream now; the salvage carries tests only.
4. **PR #4789, merged 2026-08-22.** "SOLR-18356: Remove DocsStreamer.convertLuceneDocToSolrDoc(Document, IndexSchema)", by serhiy-bzhezytskyy, created 2026-08-21. Already settled on this side: receipts/SOLR-18356.md records the branch RETIRED as obsolete because this PR landed the identical 12-line removal. No action left.
5. **PR #5034, closed unmerged 2026-10-09.** "SOLR-18119 GraalPy python fallback for the build", by janhoy, created 2026-10-06. Same ticket as this side's live pair, PR #5061 and PR #5062 (SOLR-18119 and SOLR-18523), both ready. The maintainer's own fallback PR was closed without merging, two days before this side's pair was rebuilt; no live collision.
6. **PR #5048, merged 2026-10-08.** "SOLR-9759: Admin UI should post streaming expressions", by epugh, created 2026-10-06. Already settled on this side: the Admin UI round 1 audit records SOLR-9759 as superseded on main by this PR. This side's solr-9759-submit branch was never gated or run (receipts/SOLR-9759.md). No action left beyond the retire round.

## Class B: same area, no key match

By title: no non-ours pull request in the window names QueryComponent, CombinedQueryComponent, gradlew, mergeIds, numFound, approximateTotalHits or hitCountIsExact in its title. The title half of class B is empty.

By changed files, one flag, which is also a class C flag:

1. **PR #5019, OPEN.** "SOLR-18496: Read the renamed unique key when merging single-pass shard responses", by serhiy-bzhezytskyy, created 2026-10-03, base main. Its changed files include both QueryComponent.java and CombinedQueryComponent.java, and its subject is the merge step itself (reading the renamed unique key while merging shard responses). That is the same merge code the SOLR-18539 and SOLR-18541 fixes change, but a different defect: nothing in its title or files concerns partial-shard totals, the approximateTotalHits fallback, or the hitCountIsExact fold. Overlap is by file and merge area, not by fix. If it merges first, the two new-ticket fix branches rebase over its merge changes.

## Class C: same files as the three new tickets' fixes (4)

The fix files are QueryComponent.java (SOLR-18539), CombinedQueryComponent.java (SOLR-18541) and gradlew (SOLR-18540). Four non-ours pull requests in the window touch the first two files:

1. **PR #4761, merged 2026-08-28.** "SOLR-18373: remove NamedList.asShallowMap/get(String,int) and SolrParams.toNamedList", by serhiy-bzhezytskyy, created 2026-08-19. Touches CombinedQueryComponent.java and QueryComponent.java among its files. Merged before the new-ticket work was based; part of the base the fixes sit on.
2. **PR #4809, merged 2026-09-01.** "Tighten SolrQueryResponse.getResponseHeader/addResponseHeader to SimpleOrderedMap", by serhiy-bzhezytskyy, created 2026-08-25. Touches CombinedQueryComponent.java and QueryComponent.java among its files. Also merged, also base drift only.
3. **PR #5019, OPEN.** Listed under class B above. Touches both component files. The only open class C flag on CombinedQueryComponent.java.
4. **PR #5108, OPEN.** "SOLR-17786: Support cursorMark in the reverse direction", by HoustonPutman, created 2026-10-12. Touches QueryComponent.java. Its subject is cursor marks, a different part of the file from mergeIds; overlap is by file only.

For gradlew specifically, class C is empty: no non-ours pull request in the window changed gradlew or gradlew.bat, by title or by file.

## Closing summary

- Class A is not empty: 6 flags, of which one is a live duplicate effort (PR #5065, SOLR-18010, open against this side's gated, unopened branch), two are already settled in this side's own records (SOLR-18356, SOLR-9759), and three are merged or closed upstream work on tickets this side also holds (SOLR-18368, SOLR-18109, SOLR-18119).
- Class B is empty by title and has one files-based flag (PR #5019).
- Class C is not empty: 4 flags, two merged and two open (PR #5019, PR #5108). For gradlew, class C is empty.
- No pull request in the window, by any author, duplicates the three new tickets SOLR-18539, SOLR-18540 or SOLR-18541, and none touches the partial-shard totals, fallback or exactness-fold behaviour they describe.
