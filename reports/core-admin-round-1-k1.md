# Core admin round 1, part k1: LukeRequestHandler and QuerySenderListener

Result: SOLR-4989 and SOLR-13246 are draftable and PR-ready at their remote heads, with drafts written. SOLR-15024 is draftable with drafts written, after a Jira text check that could not be made here. SOLR-12916 is held (audit only, no draft). Most important FIX: do not push the local `solr-13246-submit` branch, which holds the defective received version, not the gated head.

Heads checked against the claim table, using the local `origin/*` refs (no fetch): 4989 `5bac95376cd4`, 15024 `f95b5010b3fe`, 13246 `6817c6c0c267`, 12916 `ebe5db374336`. All four match the claim table and the receipts. No live PR exists for these four, so no `gh` call was made.

## Findings

1. FIX. Local `solr-13246-submit` is not the gated head.
   - File: `solr/core/src/java/org/apache/solr/core/QuerySenderListener.java`, line 50, on local commit `065b36710adc`.
   - Evidence: line 50 calls `newSearcher.getName()`. At `6817c6c0c267`, `solr/core/src/java/org/apache/solr/search/SolrIndexSearcher.java` lines 2339 to 2340 define `getName()` as `return SolrIndexSearcher.class.getName();`, so the log would print a class name. The receipt describes this as the received defect (wrong getName, comment inside the log guard). `git merge-base --is-ancestor 6817c6c0c267 065b36710adc` exits 1, so the local branch is not a fast forward of `origin/solr-13246-submit`. It also carries a local only `SOLR-13246-TESTING.md` commit.
   - Replacement: none to write here. Do not push the local branch. The PR head is `6817c6c0c267d127e82e4a679245040f5ca9db64`, where line 51 reads `log.debug("QuerySenderListener sending requests to {}", newSearcher.getSearcherName());`.

2. FIX. Local `solr-4989-submit` is not the live branch either.
   - Evidence: `git merge-base --is-ancestor 5bac95376cd4 f31b4f91fcd8` exits 1. The local code commits `34ab2fc7627` and `11b93b8206e` have the same content as the remote `3becf78dd7e` and `5bac95376cd` (`git diff 3becf78dd7e 34ab2fc7627` is empty). The only difference from the remote tip is a local commit `f31b4f91fcd` that adds `SOLR-4989-TESTING.md` (29 lines). A push would need a force push and would send the handoff file.
   - Replacement: none in code. Do not push the local branch. The PR head is `5bac95376cd488678290d45b8a7a4c9ed2f089cd`.

3. FIX before any PR for 12916. The remote tip carries a process file at the repo root.
   - File: `SOLR-12916-TESTING.md`, added by commit `ebe5db37433` (24 lines).
   - Evidence: `git diff --stat cabedd1d9680 ebe5db374336` lists the file. Its heading reads "hypothetical reproduction (nothing was compiled or run)".
   - Replacement: remove the file in a main side commit before any PR. Not done here.

4. NOTE. The SOLR-15024 Jira text is not on disk.
   - Evidence: there is no `SOLR-15024.json` in `research/jira-context`, and the ASF Jira CSV has no SOLR-15024 row. The inventory row (`branch-focus-inventory-2026-10-08.md`, line 323) gives the topic "Admin UI doesnt' show CharFilters correctly". The draft's "What happens today" rests on the code.
   - Replacement: none in the draft. Before posting, confirm the ticket describes duplicate char filter keys in the Luke output. If it describes only the Admin UI, change the summary line.

5. NOTE. Stale element name in a 15024 test assertion.
   - File: `solr/core/src/test/org/apache/solr/handler/admin/LukeRequestHandlerTest.java`, lines 242 to 243, at `f95b5010b3fe`.
   - Evidence: both assertions count `lst[@name='charFilters']`, but the branch now emits `arr`. For this fixture the count is 0 either way, so the test still passes, but it no longer checks the shape.
   - Replacement, if taken: line 242 becomes `"0=count(//lst[@name='custom_tc_string']/lst[@name='indexAnalyzer']/arr[@name='charFilters'])",` and line 243 becomes `"0=count(//lst[@name='custom_tc_string']/lst[@name='queryAnalyzer']/arr[@name='charFilters'])");`. This moves the head, so it needs a new gate.

6. NOTE. The 15024 changelog overstates the client effect.
   - File: `changelog/unreleased/SOLR-15024.yml`, line 2, at `f95b5010b3fe`.
   - Evidence: "instead of only the last one" is true only for a client that keeps one value per JSON key. The base JSON output for two same-class char filters was not read, so the claim is unverified.
   - Replacement, if taken: "title: Luke schema info now emits charFilters as an ordered list, so an analyzer that uses the same char filter class more than once lists every entry." A text change still moves the head.

7. NOTE. Token filters keep the class-name key.
   - File: `solr/core/src/java/org/apache/solr/handler/admin/LukeRequestHandler.java`, lines 1030 to 1038, at `f95b5010b3fe` (`SimpleOrderedMap<Map<String, Object>> filters`).
   - Evidence: the branch leaves this unchanged. This is the wider filters question the gate left out. The draft's Limits asks it.
   - Replacement: none. The draft carries the question.

8. NOTE. The 12916 change also alters the XML path.
   - File: `solr/core/src/java/org/apache/solr/core/QuerySenderListener.java`, lines 108 and 118 to 119, at `ebe5db374336`.
   - Evidence: on main (`upstream/main`, lines 115 to 119), a plain `<str>` child of a queries list is logged and dropped. The branch turns an even run of plain values into a warming query, so an XML `<arr name="queries"><str>q</str><str>*:*</str></arr>` now becomes `q=*:*`. The branch's own note says "flag in review". The changelog title (`changelog/unreleased/SOLR-12916-query-sender-flat-queries.yml`, line 2) mentions only the Config API.
   - Replacement for the title, if the ticket is ever drafted: "QuerySenderListener accepts warming queries given as flat name and value lists, from the Config API and from XML, instead of ignoring them."

9. NOTE. The 12916 premise holds on main.
   - Evidence: main `QuerySenderListener.java` lines 118 to 119 drop the nested Config API form (`// also by nested lists in JSON from Config API`). `convertQueriesToList` came from SOLR-9359 (commit `3540e7cd3a8`). The branch note's "fixed by SOLR-9359" is right only for the NamedList form. The ticket's own example is still dropped on main.

10. NOTE. Unverified 12916 points.
   - Evidence: the branch note lists three open guesses: the Config API nesting is not round-tripped, the test calls only the static helper, and `rows` stays an Integer in the NamedList. The record shows no run of any kind, so there is no fail-before line.
   - Replacement: none; hold.

11. NOTE. Landing order for the LukeRequestHandler pair (4989 and 15024).
   - Evidence: the 4989 hunk is at `5bac95376cd4` lines 238 to 241. The 15024 hunk is at `f95b5010b3fe` lines 1008 to 1019. The hunks do not overlap. `git merge-tree --write-tree --name-only 5bac95376cd4 f95b5010b3fe` is clean (tree `2ebad6b2be05`). The merged `LukeRequestHandlerTest.java` has no repeated test name.
   - Order: either order merges. Recommended: 4989 first, then 15024. Once 4989 lands, `show=all` also carries the 15024 list shape in its schema section.

12. NOTE. Landing order for the QuerySenderListener pair (13246 and 12916).
   - Evidence: the 13246 hunk is at `6817c6c0c267` lines 50 to 52. The 12916 hunks are at `ebe5db374336` lines 108 to 119 and 131 to 158. `git merge-tree --write-tree --name-only ebe5db374336 6817c6c0c267` is clean (tree `4cd0532348c7`). The overlap was not audited twice.
   - Order: 13246 first (PR-ready). 12916 later, if ever drafted.

13. NOTE. Both bases trail main; a rebase needs a new gate.
   - Evidence: local `upstream/main` is `8e62c2686882` (not re-fetched). Merge base `97d973814336` (4989 and 13246) is 40 commits behind. Merge base `b5c71bc5573c` (15024) is 67 behind. Merge base `cabedd1d9680` (12916) is 37 behind.
   - Replacement: none. A rebase moves each head and needs a new gate.

14. NOTE. Receipt counts match the source.
   - Evidence (counted from source, not run): `LukeRequestHandlerTest` has 10 test methods at `5bac95376cd4` (receipt 10 of 10) and 9 at `f95b5010b3fe` (receipt 9 of 9; base 8). `TestQuerySenderNoQuery` has 4 test methods at `6817c6c0c267` (receipt 4 of 4; base 3). These JUnit 3 style methods carry no `@Test`, so a grep for `@Test` undercounts.

15. NOTE. Receipt dates are ledger dates.
   - Evidence: the 4989 and 13246 receipts say recorded 2026-10-06. The 15024 receipt says reviewed 2026-10-03. The drafts use those dates. Gate run dates are on the main side.

16. NOTE. Authors and trailers are clean.
   - Evidence: every commit in the four ranges is by Nick Shanin. None has a Co-authored-by or Claude trailer.

17. NOTE. The 4989 distributed path was read, not run.
   - Evidence: `LukeRequestHandler.java` lines 412 to 415 at `5bac95376cd4` copy the schema section from the first shard response. Shard requests reuse the request parameters (`handleDistributed` builds `ModifiableSolrParams` from `reqParams`).
   - Replacement: none. The draft's Limits says "read, not run".

18. NOTE. Unchanged searcher log line named in 13246 Limits.
   - Evidence: `solr/core/src/java/org/apache/solr/core/SolrCore.java` line 2823 at `6817c6c0c267` logs the searcher object on an error path. The branch leaves it alone. The draft's Limits names it.

## Task results

**SOLR-4989: draftable, PR-ready.** Head `5bac95376cd4` matches the receipt and the claim table. The change adds the schema section for `show=all` (`LukeRequestHandler.java` lines 239 to 241). Two new tests; the receipt reports 10 of 10 and one new-test failure on base. Draft: `pr-drafts/core-admin/SOLR-4989.md`. Suggested title: "SOLR-4989: show=all in LukeRequestHandler also returns the schema section". Limits in the draft: `BAD_REQUEST` with a doc id still applies, the distributed path was read but not run, and schema content is unchanged. Open item: Finding 2.

**SOLR-15024: draftable, gated (hardened), one open check.** Head `f95b5010b3fe` matches the receipt. The code matches the receipt's narrowing: only `charFilters` changes, and token filters are untouched. Draft: `pr-drafts/core-admin/SOLR-15024.md`. It poses one Choice (ordered list versus keys with an index) and one Limits question (token filters). Suggested title: "SOLR-15024: Luke emits charFilters as an ordered list". Open items: the Jira text (Finding 4). Findings 5 and 6 are optional and would need a new head and gate.

**SOLR-13246: draftable, PR-ready.** Head `6817c6c0c267` matches the receipt. The change is the debug line (`QuerySenderListener.java` lines 50 to 52), a new public `getSearcherName()` (`SolrIndexSearcher.java` lines 562 to 568), and a new test (`TestQuerySenderNoQuery.java` lines 60 to 78). Draft: `pr-drafts/core-admin/SOLR-13246.md`. Suggested title: "SOLR-13246: QuerySenderListener debug log names the searcher, not the reader". Open item: Finding 1. The per-test failure line on base is not on disk. The draft's Proof says the test fails on base because the message carries the reader text, which is the test's own last check.

**SOLR-12916: audit only, held, no draft.** Head `ebe5db374336` matches the record (fresh arrival, no gate). The premise holds on main (Finding 9). It is held because nothing has been run, the branch note lists unverified guesses (Finding 10), the XML path change needs a decision (Finding 8), and the handoff file must come out (Finding 3). Path to a draft: a fresh gate at a new head with a fail-before run on main, a Config API round trip or a stated limit, and a decision on the XML path.

## Owner decisions

1. SOLR-15024: ship `charFilters` as an ordered list (the draft), or keep the object and make repeated keys unique. The draft poses it.
2. SOLR-15024: take Findings 5 and 6 now (a new head and gate), or ship as is.
3. SOLR-15024: token filters in the same change, or a follow-up ticket and PR later. The draft offers the follow-up.
4. SOLR-12916: keep the XML path change, or limit the change to the Config API. Held until decided.
5. SOLR-4989 and SOLR-15024: rebase onto current main before any PR. Both trail main, and a rebase needs a new gate.

## Not checked

- SOLR-15024 Jira text: no packet on disk and no Jira tool loaded in this session. Packets were read for 4989 (`research/jira-context/SOLR-4989.json`), 13246 and 12916.
- No builds or tests (out of scope). Counts and base failure lines come from the receipts; the per-test base failure lines are on the main side.
- Admin UI in a browser: not run. Read only: `solr/webapp/web/js/angular/controllers/schema.js` lines 648 to 670 and 679.
- The base JSON output for two same-class char filters (Finding 6).
- The distributed path for 4989 (read only, Finding 17).
- Whether non-String values such as `rows=1` reach the query parameters for 12916. `addEventParms` was not read.
- Overlap between the 13246 `SolrIndexSearcher.java` hunk and the Search components round. That round was not read.
- `upstream/main` was not re-fetched; the local ref `8e62c2686882` was used.
- Whether each head compiles after a rebase.
- Lucene versions: none is named in the three drafts.
