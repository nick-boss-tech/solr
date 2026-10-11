# Search components draft fidelity, slice s3

Assignment and claim files were not opened. The slice (SOLR-12543, 12556, 13245, 13876, 14381, in `pr-drafts/search-components/`) and the tree (HEAD `d627304e96b`) come from the lead's message. The brief's claim commit `e84522fa5bc` was not checked.

Category reports used, per ticket: 12543 and 13245 in `reports/search-components-3-w6.md` (summary rows in `search-components-3.md`); 12556 in `reports/search-components-1-f4.md` (summary `search-components-1.md`); 13876 in `reports/search-components-2-h2.md` (summary `search-components-2.md`); 14381 in `reports/search-components-4-s2.md` (summary `search-components-4.md`). Receipts `receipts/SOLR-<n>.md` exist for all five. `material/` has no file that names these tickets or the topic terms (processEmpty, maxScore, xsort, GroupCommand, daemon, boxCount, ExpandComponent).

Head checks, `git ls-remote origin refs/heads/solr-<n>-submit` on 2026-10-11. All five match the head the draft names.

- solr-12543-submit `88d236db6b7d4a538248b915447ea02e31ac83cf` (draft matches)
- solr-12556-submit `033ec65a0e1bc12fd16d65b77a5b19458c837040` (draft matches)
- solr-13245-submit `16e62ab654257b475ac07ee128686626bd1c8631` (draft matches)
- solr-13876-submit `2e110473dbada4f6a0e23a903a4e002da2b78143` (draft matches)
- solr-14381-submit `a28b3f672cb4daa7cc32833082b0f499f41e3a85` (draft matches)

Base commits: 12543 and 12556 proof base `14c7aac0d151402b00259e2fb9bf5eed7049ec5d` (exists locally). 13245 base `22a8cfebbbdb026437d10b0aa47bb66ffdb23a9a` and 13876/14381 base `b5c71bc5573c4e31b4cee5a7965d73587fc0ae58` are the merge-bases with upstream main `8e62c2686882` (computed with `git merge-base`).

## Verdicts

| Draft | Head checked | Verdict |
|---|---|---|
| SOLR-12543 | 88d236db6b7d (live matches) | CONSISTENT |
| SOLR-12556 | 033ec65a0e1b (live matches) | DRIFT (4 items) |
| SOLR-13245 | 16e62ab65425 (live matches) | DRIFT (2 items) |
| SOLR-13876 | 2e110473dbad (live matches) | DRIFT (1 item) |
| SOLR-14381 | a28b3f672cb4 (live matches) | DRIFT (2 items) |

## SOLR-12543

Verdict: CONSISTENT.

Checked: head matches live. Proof counts (3 of 3, 19 of 19 at head; 2 base failures with `expected:<400> but was:<200>`; date 2026-10-08) match `receipts/SOLR-12543.md` lines 4, 6 and 7. Code at `88d236db6b7d`: `ExportWriter.java` L180 writes `responseHeader` status 400 and L187 the EXCEPTION body (cited range L176-L192 holds both); no-sort check L221-223 and no-fl check L275-277; score-sort message L236-238; rq message L264; fl-with-score message L290; `ExportHandler.java` L136-L153 holds the handler check. Changelog `changelog/unreleased/SOLR-12543-export-bad-request-status.yml` exists at head; its title matches the draft's "What this change does" line. Limits and Choice match w6 (owner decisions 3 and 4).

Optional notes, not blocking:
- The Proof names `testMissingSortReturns400` and `testMissingFlReturns400` as the two base failures and `testLocalParamSortExports` as the base-passing guard. The receipt gives counts and the message only. The names are the only head methods that assert 400 (`TestExportHandlerHttpStatus.java` L78, L87, L96), so they are consistent, but confirm from the base run log (not on disk; w6 item 10).
- The head still carries three `Co-Authored-By` trailers and two handoff commits (w6 findings 2 and 3). The draft cannot fix that; the PR shows them until the authorized rewrite.

## SOLR-12556

Verdict: DRIFT (4 items).

1. Draft says: "`TestJsonFacetRefinement`: [CONFIRM BEFORE POSTING: test count; the file has 12 `@Test` methods, 11 running and 1 `@AwaitsFix`] 0 failures, 0 errors, 1 skipped."
- Evidence: `receipts/SOLR-12556.md` L6: "TestJsonFacetRefinement 13 tests, 0 failures, 0 errors, 1 skipped at the head". At `033ec65a0e1b` the test file has 12 `@Test` lines and 13 `public void test` methods; the 13th, `testIndexAscRefineConsistency` (L2241), has no `@Test`. f4 finding 3: unreconciled.
- Replacement: "`TestJsonFacetRefinement`: 0 failures, 0 errors, 1 skipped."

2. Draft says (bold line): "On the base production code, the class fails with the ticket symptom." and (paragraph): "On the base production code with this test file, the class fails with the ticket symptom, a bucket value mismatch in the merged top buckets. [CONFIRM BEFORE POSTING: name the failing test from the base run.]"
- Evidence: `receipts/SOLR-12556.md` L7: "FacetRequestSortedMerger.java at base 14c7aac0d151 with tests at head fails with the ticket symptom". The base scope is one file, not the production tree. The final-correction fail-before log `g12556-opt1b.log` is not on disk. f4 finding 4.
- Replacement (bold line): "**`TestJsonFacetRefinement` passes with this change. With `FacetRequestSortedMerger.java` at base `14c7aac0d151`, the class fails with the ticket symptom.**"
- Replacement (paragraph sentence, and delete the CONFIRM sentence after it): "With `FacetRequestSortedMerger.java` at base `14c7aac0d151` and this test file, the class fails with the ticket symptom, a bucket value mismatch in the merged top buckets."

3. Draft says (What this change does, with the changelog link): "Buckets known before refinement follow the `processEmpty` rule. Buckets first seen during refinement keep the old rule."
- Evidence: `changelog/unreleased/SOLR-12556-processempty-refinement-completeness.yml` at `033ec65a0e1b` lines 2-5 say that all buckets that were not refined against every shard are dropped (no "known before refinement" limit; `FacetRequestSortedMerger.java` L168-169 and L189-191), and that shards which never return the facet no longer drop buckets, which is no change from base (f4 finding 2). Fixing this is a changelog-only branch commit that needs owner approval (f4 owner decisions). Not a draft edit.
- Replacement (changelog title, exact): "JSON Facet field refinement with processEmpty:true now drops buckets known before refinement that were not refined against every shard that returned the facet, instead of returning them with partial stats. Buckets first seen during refinement keep their previous behavior."

4. Draft says: "NOT FOR POSTING (reviewer notes, remove before use)" and the block under it, with the CONFIRM markers.
- Evidence: the public file holds reviewer notes with internal vocabulary (gate logs, receipt, round 15 review, inventory, run number). Not postable as it stands.
- Replacement: delete the `---` line above the NOT FOR POSTING heading and everything after it. The post ends at the "AI assistance" paragraph.

Optional notes, not blocking:
- The Choice (late buckets keep partial stats, or apply `processEmpty` to them) is a real live alternative. f4 records that the owner already ruled option 1 in round 15, so keeping or dropping the Choice is the owner's call.
- Limits match f4 and the receipt ("returned with unrefined processEmpty stats, as on base"). The `@AwaitsFix` test name matches `TestJsonFacetRefinement.java` L1114, and the head comment at L1193-L1194 supports "needs a refinement protocol change".
- The local `solr-12556-submit` ref is stale (f4 finding 5). Not used here.

## SOLR-13245

Verdict: DRIFT (2 items).

1. Draft says: "A start with a name that a daemon on another replica of the collection already uses stops that daemon, then starts the new one."
- Evidence: `changelog/unreleased/SOLR-13245-daemons-visible-on-all-replicas.yml` at `16e62ab65425` lines 1-3 titles only the visibility change. The stop-and-replace is at `StreamHandler.java` L263-L264 (`if (daemons.containsKey(...)) { daemons.remove(...).close(); }`). w6 finding 7 and owner decision 2 require the changelog to state it.
- Replacement (changelog title, exact; use once owner decision 2 keeps this behavior): "Streaming daemon list, start, stop and kill requests now see the daemons of every replica of the collection on that node, no matter which replica the request is routed to. Starting a daemon with a name that another replica of the collection on that node already runs stops the running daemon first."

2. Draft says (What happens today): "[StreamHandler.java, base]"
- Evidence: the link is to `22a8cfebbbdb`, which is the merge-base with upstream main (checked). `pr-formula.md` L93-L94 requires the text to say the link is the merge-base commit. "base" does not say that.
- Replacement: replace the link label `[StreamHandler.java, base]` with `[StreamHandler.java at the merge-base]`. Keep the URL.

Optional notes, not blocking:
- Proof matches the receipt (2 of 2 at head; base failure text and `testAPIs` fallout, `receipts/SOLR-13245.md` L6-L7). `DaemonStreamApiTest.java` L84 creates the two-shard collection the draft describes; the test is at L204-L205.
- The Choice (node-local or collection-wide) matches w6 owner decision 1, and both alternatives are named.
- Branch comment layout from w6 FIX 6 is still at head: `StreamHandler.java` L93 orphan comment and the split comment at L113-L115. Not draft text.
- The "picked at random" and "the ticket's comments say ... two replicas" claims in What happens today were not checked against the ticket packet.

## SOLR-13876

Verdict: DRIFT (1 item).

1. Draft says: "Before this change, the expand component gave each group a fixed `Float.NaN` max score ([ExpandComponent.java]"
- Evidence: the link is to `b5c71bc5573c`, the merge-base with upstream main (checked; L787 holds `Float.NaN,`). `pr-formula.md` L93-L94 requires the text to say the link is the merge-base commit. The text does not.
- Replacement: replace the link label `[ExpandComponent.java]` with `[ExpandComponent.java at the merge-base]`. Keep the URL.

Optional notes, not blocking:
- All other checks pass. Head matches live. Proof matches the receipt (9 of 9 at head; 9 tests with 7 base failures, every one a maxScore assertion; `receipts/SOLR-13876.md` L6-L7; date 2026-10-07). Code at `2e110473dbad`: slice maximum L780-L787; coordinator L475-L520 passes expanded groups unchanged. `testExpandMaxScore` (L942-L992) covers a relevance sort (L956-L969) and a non-score `expand.sort` (L975-L990); the value 30.0 is at L969 and L989. Changelog `changelog/unreleased/SOLR-13876.yml` exists; its title matches the draft.
- Limits and Choice match h2 (owner decisions 1 and 3).
- Branch items h2 flagged, not in the draft: `TestExpandComponent.java` L945 still says NaN "is serialized as a missing maxScore", and commits `ce19cf78ae9` and `dfde3c56df8` carry process wording. A squash changes the head, so the draft's head and links then need a refresh.
- `ExpandComponent.java` L782-L783 comment still names the wrong score path (h2 note 3).

## SOLR-14381

Verdict: DRIFT (2 items).

1. Draft says: "Every grouped response types `matches` and `ngroups` as `long` in XML output, not only counts above the int range."
- Evidence: `changelog/unreleased/SOLR-14381.yml` at `a28b3f672cb4` ends its title at "SolrJ GroupCommand getMatches and getNGroups now return long and Long" and does not mention the XML type change (s2 finding 7; `TestGroupingSearch` head L249, L265, L331 changed from int to long).
- Replacement (changelog title, exact): "Grouping counts (matches, ngroups and the simple-format numFound) are now long values, so distributed grouping over more than Integer.MAX_VALUE documents no longer overflows. Shard responses carry the counts as Integer whenever they fit, so a previous-version coordinator keeps working during a rolling upgrade; SolrJ GroupCommand getMatches and getNGroups now return long and Long. Grouped responses now type matches and ngroups as long in XML."

2. Draft says (Proof): "On the production code from before the boxing change, both compat test methods fail with `ClassCastException: Long cannot be cast to Integer`."
- Evidence: `receipts/SOLR-14381.md` L7: "fails as designed on the earlier production with the fixed compat test, both failures java.lang.ClassCastException 'Long cannot be cast to Integer'". The receipt names no SHA, so "before the boxing change" is not supported (s2 finding 9; owner decision 3).
- Replacement: "On the earlier production code, with this test file, both failures are `java.lang.ClassCastException: Long cannot be cast to Integer`."

Optional notes, not blocking:
- Verified: `boxCount` at head L340-L344 (Integer when it fits, Long above). Base int fields: `ResponseBuilder.java` L231, `Grouping.java` L556, `GroupCommand.java` L112 (int getMatches) and L123 (Integer getNGroups). Proof counts match the receipt (L6-L7; earlier head `dd8d16441dc` counts from the receipt's 2026-10-03 row).
- Limits and Choice match s2 (owner decisions 1 and 2). The Choice is a real alternative (accessors beside the old getters).
- Branch comment still open (s2 FIX 1): `TopGroupsResultTransformerCompatTest.java` L45 and L111 still describe the per-group `totalHits` Integer cast. The draft does not repeat it.
- The textual conflict with the SOLR-17155 branch (s2 finding 4; `search-components-4.md` row) is not in the draft. Relevant once 17155 is public; the landing order is an owner call.

## Not done

- Assignment and claim files not opened; claim commit `e84522fa5bc` not checked; HEAD `d627304e96b` used as the lead asked.
- Gate and base-run logs are not on disk. Proof counts were checked against receipts only, and base failure test names against head test files only.
- Jira packets and ticket text not read. Ticket-derived claims (13245 "picked at random" and "two replicas") are unverified.
- No `gh` calls, so PR state and the apache/solr #5014 link were not checked.
- Citation ranges were checked by locating the named lines and tokens with `git grep` at each SHA, not by reading every cited line in full. Semantics of the 12556 and 13876 code paths were checked against the part reports.
- No builds, tests, or writes other than this report.
