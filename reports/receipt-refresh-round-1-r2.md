# Receipt refresh round 1, part r2: SOLR-16570 and SOLR-8088

Result: Neither ticket is draftable, so no draft is written. SOLR-16570 is on hold because the branch fixes a docValues only NPE, while the ticket describes an empty index NPE that SOLR-16611 already fixed on the base. SOLR-8088 is on hold because its check misses the TextField case in the ticket, and the changelog title is wider than the code.

Checked at worktree `fdafefeab92`. Live heads read with `git ls-remote` (read only): `solr-16570-submit` at `9d3466101a29`, `solr-8088-submit` at `567efa9b78c3`. Both match the claim table. Base for both branches is `cabedd1d968`, so base line numbers below apply to both.

## Findings

1. **FIX, SOLR-16570: the ticket does not describe the branch's bug.**
   Evidence: `research/jira-context/SOLR-16570.json` (workspace root) has the summary "collapse w/ nullPolicy=expand + hint=top_fc can NPE on empty collection", and the reproduction uses an empty index. Comment 17740345 says SOLR-16611 fixed it. `changelog/archive.md:643` at the base says the no-segments NPE in `{!collapse hint=top_fc}` is fixed by SOLR-16611 (commit `0cc18eb4cae`, already in base history). The fix is in the base code too: `CollapsingQParserPlugin.java:579-584` at `9d3466101a29` returns `DocValues.emptySorted()` when the values are null. The branch changes a different NPE: `Map.of(collapseField, type)` with a null type, at `CollapsingQParserPlugin.java:621-628`. The new test (`TestCollapseQParserPlugin.java:1027-1059`) uses three documents, the default nullPolicy, and no empty index.
   Replacement: no text change in the branch. Owner picks the ticket (owner decision 1). If a new ticket is used, the changelog file name changes too. The title at `changelog/unreleased/SOLR-16570-collapse-top-fc-docvalues-only.yml:2` is accurate for the branch and can stay.

2. **NOTE, SOLR-16570: a case the test does not cover.**
   Evidence: `CollapsingQParserPlugin.java:618-624` leaves `type` null when `f.indexed()` is false, so a string field that is not indexed but has docValues takes the new branch too. The test uses only the indexed, uninvertible=false field (`schema11.xml:321`, test line 1029).
   Replacement (Limits line): "A string field that has docValues but is not indexed takes the same path. No test covers it."

3. **NOTE, SOLR-16570: stale comment.**
   Evidence: `CollapsingQParserPlugin.java:606-609` says "This forces the use of the top level field cache for String fields." That is no longer always true.
   Replacement: "This forces the use of the top level field cache for String fields that can be uninverted. A String field that has docValues and cannot be uninverted is read from its docValues directly."

4. **NOTE, both tickets: local branch refs lag the fork.**
   Evidence: local `solr-16570-submit` is at `5676646936d` (still has the handoff commit). Local `solr-8088-submit` is at `2398c9bea08` (still has the handoff file). The fork refs `origin/solr-16570-submit` (`9d3466101a2`) and `origin/solr-8088-submit` (`567efa9b78c`) are the live heads.
   Replacement: use the `origin/` refs in any command. Update local refs only with owner direction.

5. **FIX, SOLR-8088: the check misses the ticket's case.**
   Evidence: `SearchGroupsFieldCommand.java:84` fires only when `getNumberType() == null && multiValued()`. The ticket error is "unexpected docvalues type SORTED_SET for field 'ip' (expected=SORTED)" (`research/jira-context/SOLR-8088.json`). Comment 14905923 says "It's not a numeric field, it's a TextField." `TextField.java:147-149` maps every TextField to `SORTED_SET_BINARY`. `IndexSchema.java:428-429` uses that mapping for uninvertible fields, and `SolrIndexSearcher.java:234` wraps the reader with it. The test schema is version 1.0 (`schema.xml:28`), and `FieldType.java:193` makes uninvertible the default below 1.7. So a single valued TextField such as `subject` (`schema.xml:548`) reaches the same SORTED_SET grouping path, and the check does not fire because the field is not multiValued. The gate test uses only `cat` (`schema.xml:631`, multiValued string).
   Replacement, option A: widen the predicate at `SearchGroupsFieldCommand.java:84` so it also rejects a field that is not numeric, has no docValues, and whose type uninverts to `SORTED_SET_BINARY`, and add a test on `subject` that expects BAD_REQUEST. Not built or run here, so it needs a proof run. Option B: keep the multiValued scope and add to Limits: "A single valued TextField that uninverts to SORTED_SET is not covered."

6. **FIX, SOLR-8088: the changelog title is wider than the code.**
   Evidence: `changelog/unreleased/SOLR-8088-grouping-multivalued.yml:1` says "on a multiValued field ... instead of an IllegalStateException". Numeric multiValued fields never reached that exception. `SearchGroupsFieldCommand.java:115` and `:130` call `getValueSource`, which calls `SchemaField.checkFieldCacheSource` (`SchemaField.java:285-290`). That throws BAD_REQUEST "can not use FieldCache on multivalued field" at base.
   Replacement (option B): "Distributed grouping on a multiValued field that is not numeric now fails with a clear error instead of an IllegalStateException from Lucene docValues." Under option A, rewrite to match the new predicate.

7. **NOTE, SOLR-8088: "400" is not checked at the client.**
   Evidence: the builder runs on shards. `QueryComponent.java:431` and `:433` choose the phase from `group.distributed.first` and `group.distributed.second`, and the builder calls are at `:1652` and `:1719`. How the coordinator returns a shard BAD_REQUEST was not checked. This is open item 13 in `reports/search-components-4-s6.md`.
   Replacement: drop "400" from the title (finding 6). Limits line: "A unit test calls the builder only. A distributed request was not run."

8. **NOTE, SOLR-8088: changelog author key.**
   Evidence: `changelog/unreleased/SOLR-8088-grouping-multivalued.yml:4` has `nick: nick-boss-tech`. The 16570 and 17796 fragments have no nick line. The `nick` key is valid upstream (74 uses under `upstream/main` changelog/unreleased).
   Replacement: delete line 4 to match the other two fragments, unless the owner wants the key (owner decision 3).

9. **NOTE, SOLR-8088: the Javadoc quotes an unchecked Lucene message.**
   Evidence: `SearchGroupsFieldCommand.java:79-82` quotes "unexpected docvalues type SORTED_SET". Lucene source is not in this repo. This repeats finding 14 in `reports/search-components-4-s6.md`.
   Replacement: "Term based grouping reads single valued docValues. A multiValued field would fail deep inside Lucene, so reject it here with a clear message instead."

## Task results

**SOLR-16570: HOLD (not draftable as this ticket).** The head matches (`9d3466101a29`, handoff file absent). The change to `getTopFieldCacheReader` is correct by reading. The null type case (`CollapsingQParserPlugin.java:621-624`) returns the slow reader, so the `Map.of` NPE at line 628 is avoided for docValues fields. Reach is limited to docValues fields, because the validation at `CollapsingQParserPlugin.java:355` runs first and rejects the other combinations. The existing cases at `TestCollapseQParserPlugin.java:1008-1024` and `:1221-1251` still hit that check. ExpandComponent takes the hint only from the collapse filter (`ExpandComponent.java:142-155`), so `expand.field` alone does not reach the branch. Both ExpandComponent call sites (`:219-220`, `:403-404`) now read docValues. Their null checks (`:399` and `CollapsingQParserPlugin.java:580`) are unchanged. Test count: the file has 20 `public void test` methods at the head (19 at base plus the new one). Fifteen have `@Test`. The new method has none, like four existing methods, so the count assumes the runner collects unannotated test methods, which the file already relies on. The rewrap at `:1038-1044` is a formatting fix, because the one line call was 104 characters. Pairing with SOLR-17796 is clean at the new head. The only shared file is the test class. A three-way merge from base blob `d8284e1fd1d6` with the 17796 tip gives exit 0, 22 test methods, and no duplicate names. Not compiled. The ticket linkage is the blocker (finding 1).

**SOLR-8088: HOLD (not draftable).** The head matches (`567efa9b78c`, handoff file absent). `SearchGroupsFieldCommandTest` has two `@Test` methods (lines 33 and 51). On base, `testMultiValuedFieldIsRejected` fails because `build()` does not throw. `testSingleValuedFieldIsAccepted` passes on base. The 2 of 2 count and the single base failure match the file by reading. The check is correct for the multiValued string case (`cat`). The ticket's own case is not covered (finding 5). The title's numeric scope and the "400" wording are not supported (findings 6 and 7).

## Owner decisions

1. SOLR-16570: which Jira ticket carries the docValues only NPE. Options: a new ticket, or rescope SOLR-16570 and cite SOLR-16611 for the empty index case. No draft until chosen.
2. SOLR-8088: option A (widen the check and add a single valued TextField test) or option B (keep the multiValued scope and name the TextField gap in Limits). No draft until chosen.
3. SOLR-8088: keep or drop the `nick` line (finding 8).
4. SOLR-16570: name the not indexed docValues case in Limits, or add a test (finding 2).

## Not checked

- No builds, tests, `gh` calls, fetches, or commits. The gate logs (`g16570-gate.log`, `g8088-gate.log`) and JUnit XML are not on disk. The 20 of 20 and 2 of 2 counts come from the receipts. I checked only the method counts in the files.
- Lucene behavior (`DocValues.checkField`, `TermGroupSelector`) is outside this repo. The IllegalStateException claim and the single valued TextField path rest on standard Lucene behavior, not on a run.
- Whether SOLR-16611's null fallback covers the ticket's empty index plus nullPolicy=expand case at base. This was checked by reading the code and the changelog entry, not by a run.
- How a shard BAD_REQUEST reaches the client through the coordinator (finding 7).
- Other tests that load `schema11.xml` (26 files) were not reviewed. The new dynamic field suffix is used only by the new test.
- The changelog YAML was read by eye, not parsed.
- The 17796 merge result was not compiled.
- Live Jira was not read. Jira facts come from the `research/jira-context/` packets.
- No draft was written for either ticket.
