# Search components 4, part s6: SOLR-8088, 9595, 10694, 12044, 13851, 18196 and live PR 5029 (SOLR-18506)

Result: SOLR-12044 is draftable once its changelog type is fixed (draft written); 8088, 9595, 10694, 13851 and 18196 are audit only; live PR 5029 has a failing upstream check and text drift, so no public reply until the owner acts.

## Findings

1. FIX, SOLR-12044 changelog type. File: `changelog/unreleased/SOLR-12044-matchall-docset-filter.yml` line 3 at `c92bbf9349b12e96918646a73bfaa36248b3d523` (`type: optimized`). Evidence: `dev-docs/changelog.adoc` on upstream/main lists `added, changed, fixed, deprecated, removed, dependency_update, security, other`; a `git grep` of upstream/main changelog files finds no `type: optimized`. Replacement: `type: changed`. Effect: the fix is a new commit, so the head moves. Re-run the changelog check there and update the draft's Proof head and links to the new head. The draft does not name the type.

2. FIX, SOLR-18506 failing upstream check on live PR 5029. Evidence: `gh pr checks 5029` shows `Run Solr Tests using Crave.io resources  fail  14m1s  .../actions/runs/37384974639/job/112037885655`. Other checks: changelog pass, gradle check pass, labeler pass, generate skipping. `gh pr view` shows `headRefOid` 77c019e1ff0e922e3c2350379dece841651b1d44 (matches the branch tip and the receipt), `mergeable` MERGEABLE, `mergeStateStatus` UNSTABLE, state OPEN. The Oct 5 review (`research/branch-reviews/SOLR-18506-review.md`, LOW 7) recorded only the labeler check at that time, so the state has changed. Replacement: none in text. Action: read run 37384974639 (its head SHA and the failing test) before any reply, and do not describe the PR as validated upstream.

3. FIX, SOLR-18506 PR body, Proof section, internal word. Live text: "Gate: tidy clean, Error Prone :solr:core:compileTestJava clean, :solr:core:check -x test green." The shared rules ban "gate" in public text. Replacement: "Local checks at this head: tidy clean, Error Prone compile of the test sources clean, and the module check (`:solr:core:check -x test`) green." The receipt says only "module check passes", and the gate log is not on disk, so confirm the task name or drop it.

4. FIX, SOLR-18506 PR body, Proof, no verification date. The formula asks for "verified <date> at this head". Receipt: the light gate at 77c019e1ff0 is dated 2026-10-05. The date of the 32-run build gate at 3c098439882 is not in the receipt. Replacement for "3 forced runs pass on this head after a comment-only correction.": "3 forced runs pass at this head, 77c019e1ff0, after a comment-only correction, verified 2026-10-05."

5. FIX, SOLR-18506 PR body, Proof, no statement that no before and after pair exists. The formula requires saying so for a test-only change. Receipt: "The failure never reproduced locally on unmodified main (17 planning runs); the fix argument is structural." Add to the Proof: "No before and after pair is claimed. The failure never reproduced on unmodified main in 17 local planning runs, so the fix rests on the structural argument above."

6. NOTE, SOLR-18506 PR body, "32 forced full-class runs pass on the original head 3c098439882". The receipt says "32 of 32 recorded runs green across seeds, repeats, and CPU load runs" at the build head. "full-class" comes from the Oct 5 review, not the receipt. Owner decision: confirm from the gate log (not on disk), or use the receipt wording.

7. NOTE, SOLR-18506 PR body, model numbers. "1,243 trials (0.62%)" and "about 0.05% of trials" come from a standalone model (`research/branch-reviews/SOLR-18506-review.md`, the model table, and LOW 6). They are not in the receipt, and the shared rule says Proof numbers come only from the receipt. The PR labels them as a model. Owner decision: keep them as labeled model results, or cut them.

8. NOTE, SOLR-18506 PR body, "(solrbot PR #4976)". Neither the review nor the Jira packet names solrbot; both say PR #4976 on branch_9x. Replacement: "(PR #4976 on branch_9x)", unless the attribution is confirmed.

9. FIX, SOLR-18506 Jira text drift. `research/jira-context/SOLR-18506.json` (updated 2026-10-05T21:34) still carries the superseded mechanism in Summary and Description: an eviction notification race, and a first-cache eviction inherited through warm. The review (HIGH 1) and the receipt both call that wrong, and say v1 must not be used and v2 awaits the owner's paste. The live PR body already uses the corrected mechanism, so the two disagree. Action: the owner pastes v2 to Jira. The live Jira text was not read.

10. NOTE, SOLR-18506 commit history. Commit `3c098439882` subject still reads "...flakiness from Caffeine eviction notification race". The PR title is corrected. If the merge takes commit messages, the wrong mechanism enters history. Owner decision: squash merge with the PR title and body. No force push.

11. NOTE, SOLR-18506 PR body "What happens today", wording could be read as a count. The paragraph says warm-time victims can be the second cache's own entries, then says warm resets the counter. Test-tree `ThinCache.java` line 263 (`evictions.reset()`) runs after the warm puts, which is why warm-time evictions are not counted. Suggested replacement for the sentence "Occasionally Caffeine evicts one of the second cache's own entries instead": "Warming can also evict one of the second cache's own entries, but warm resets that counter afterwards, so only put(103) can leave a count." Source checked: `ThinCache.java` lines 229 (warm), 263 (reset), 267 (prior copy).

12. NOTE, SOLR-8088 premise gap. The Jira text says the reporter's field is "a TextField" and shows the SORTED_SET error. It never says the field is multiValued. The new check (`SearchGroupsFieldCommand.java` line 83 at the tip, `field.multiValued()`) covers only multiValued fields, so a single-valued text field with the same error still fails. The changelog wording is accurate for what the code does. Owner decision: keep the scope, or find the reporter's schema first.

13. NOTE, SOLR-8088 "clear 400" claim. The changelog says the change "now fails with a clear 400". The test calls only the Builder. Not checked: whether the coordinator returns 400 to the client when shards return it. Name this in Limits, or add a cluster test before submission.

14. NOTE, SOLR-8088 Javadoc. The Javadoc above `checkGroupable` (line 83 at the tip) quotes Lucene's message text "unexpected docvalues type SORTED_SET". The Lucene source is not in this repo, so the quote was not checked against Lucene 9.x or 10.x. Replacement: "Term based grouping reads single valued docValues. A multiValued field would fail deep inside Lucene, so reject it here with a clear message instead."

15. NOTE, SOLR-8088 handoff file removed. Moved head: receipt `2398c9bea08` (labeled "live tip" in the receipt, now stale) to tip `567efa9b78c` ("remove handoff doc"). The only change is deletion of `SOLR-8088-TESTING.md`. The three guesses in that file are still open on the tip: (1) the failure is multiValued (SORTED_SET), not missing docValues: not verifiable here; (2) `cat` is multiValued and `id` is single-valued: verified, schema line 631 (`cat` multiValued="true") and line 540 (`id` multiValued="false"); (3) the Builder check fires before the shard-side failure: the Builder call is in `QueryComponent.java` line 1652, inside `doProcessGroupedDistributedSearchFirstPhase` (line 1631), so the check runs first in that phase (read, not run).

16. NOTE, SOLR-9595 scope. The ticket names MultiDocValues. The branch caches only `terms()` and `getLiveDocs()`. On main, `getNumericDocValues` (line 163), `getBinaryDocValues` (169), `getSortedNumericDocValues` (175) and `getNormValues` (325) still carry "TODO cache?". Replacement for Limits if submitted: "Numeric, binary, sorted numeric and norm doc values are still built on each call." Or narrow the ticket.

17. NOTE, SOLR-9595 eager live docs. Constructor line 134 (`liveDocs = MultiBits.getLiveDocs(in);`) runs for every wrapper, even when `getLiveDocs()` is never called. Small cost. Say so in the description, or make it lazy. Evidence that the cache lives long enough: `SolrIndexSearcher.java` line 400 wraps once per searcher.

18. NOTE, SOLR-9595 test. `testTermsAndLiveDocsAreCached` uses `assertSame`, so it fails on the base code by construction, because `MultiTerms` and `MultiBits` build new objects per call. No receipt, not run. Moved head: `7ff1350ab7b` to `78f5524476a`, which only deletes `SOLR-9595-TESTING.md` (`git diff --stat` shows that one file).

19. NOTE, SOLR-10694 moved head. Receipt `093d90c62de` to tip `64e86811548`: `29a4051de1c` removes `SOLR-10694-TESTING.md`; `64e86811548` rewraps one Javadoc paragraph in `CSVResponseWriter.java` (lines 450-452). This folds in the receipt's tidy-only DRIFT flag. The code is otherwise the receipt head's code.

20. NOTE, SOLR-10694 premise confirmed on main. `TabularResponseWriter.java` lines 121 (`writeNamedList`), 136 (`writeMap`), 139 and 142 (`writeArray`) have empty bodies, so the CSV writer drops those cells. The fix path (`writeCellVal`, line 454 at the tip) is the right place. `Utils.toJSONString(Object, int)` and the `MapWriter` and `IteratorWriter` types exist.

21. NOTE, SOLR-10694 scope. The ticket title covers all response formats. The branch changes the CSV writer only. Other writers were not checked. Replacement for Limits if submitted: "Only the CSV writer is changed. Other response writers are not checked or changed." Or narrow the ticket.

22. NOTE, SOLR-10694 untested paths. Multi-valued elements that are maps (the `mvPrinter` path) have no test. The expected CSV string in the new test was not run. Verified as not affected: the existing `[explain]` expectation in `TestCSVResponseWriter.java` (around lines 350-353) has no explain value on its documents, so `writeNull` runs and `writeCellVal` does not.

23. FIX before any PR, SOLR-13851 Claude trailer. Commit `b60f4d642d1` carries "Co-Authored-By: Claude Sonnet 5.5 <noreply@anthropic.com>". The standing rule bans Claude trailers. Removing it rewrites the fork branch; owner decision.

24. FIX before any PR, SOLR-13851 handoff file. The tip carries `SOLR-13851-TESTING.md`, added in `6444a38ce6e` ("Add testing handoff (remove before upstream PR)"). Remove it before any PR.

25. NOTE, SOLR-13851 receipt. The receipt names no gate log, no gate date and no pre-fix proof. It records `TestIndexSearcher 6 of 6`, which matches the branch (base 5 tests, tip 6). Held status stands.

26. NOTE, SOLR-13851 base age. The branch base `56ec140e363` is 77 commits behind upstream/main. Rebase before any PR.

27. NOTE, SOLR-13851 held behavior change. `getFirstMatch` replaces an `assert` with a thrown `IllegalStateException` (`SolrIndexSearcher.java`, around lines 929-936 at the tip). That changes behavior with assertions off too. The held reason stands. Not audited further.

28. NOTE, interaction 12044 and 13851. A trial merge (`git merge-tree --write-tree`, no ref written) of `origin/solr-12044-submit` with `origin/solr-13851-submit` auto-merges `SolrIndexSearcher.java` and conflicts in `TestIndexSearcher.java`, because both add a test at the same spot (`CONFLICT (content)`, tree `73ce679798984efa23833b6e141683694e549541`). Resolution: keep both tests and both import sets. Whichever lands second resolves it.

29. NOTE, SOLR-18196 redundant. Both test blobs on `f3248fe2310` are identical to upstream/main (`8e62c268688`) and to the merged commit `b5c71bc5573`. `SimpleOrderedMapTest` has 18 public void test methods (14 with `@Test`, 4 with only the `test` name), and `QueryResponseTest` has 6 `@Test` methods, so the receipt's 6 and 18 match.

30. NOTE, SOLR-18196 local ref. Local `refs/heads/solr-18196-submit` is at `b629827f1a8`, behind the live tip `f3248fe2310` on origin. Use the origin ref.

31. NOTE, SOLR-18506 consistency that holds. Head `77c019e1ff0` matches the branch tip, the receipt, and the PR head. The PR body's mechanism matches the code: `CaffeineCache.java` line 127 (async default true) and line 131 (`Runnable::run`); `ThinCache.java` line 263 (reset) and 267 (prior copy); `setMaxSize` (line 344) runs `cleanUp`; `gradle/libs.versions.toml` line 62 pins Caffeine 3.2.4. The test's 101 + 25 + 1 = 127 puts and the "at most 126 held" wording match the test body (`TestThinCache.java` line 158 `backing.setMaxSize(200)`, line 165 `put(103)`). The live body has no dashes that I saw.

## Task results

- SOLR-8088: audit only, no gate. Receipt head `2398c9bea08` moved to `567efa9b78c` (removes the handoff file). Code otherwise unchanged. Verdict: held for proof (no gate, premise partly open). Not drafted. Findings 12 to 15.
- SOLR-9595: audit only, no gate. Moved `7ff1350ab7b` to `78f5524476a` (handoff file removed). Verdict: scope gap against the ticket title, plus an unverified fail-before. Not drafted. Findings 16 to 18.
- SOLR-10694: audit only, no gate. Moved `093d90c62de` to `64e86811548` (handoff removed, tidy rewrap folded in). Premise confirmed on main. Verdict: scope gap (CSV only). Not drafted. Findings 19 to 22.
- SOLR-12044: receipt confirmed. Gated green at `c92bbf9349b12e96918646a73bfaa36248b3d523`; the only change from the old receipt head is the handoff removal. Fail-before recorded (6 tests, 1 failure, the new test; the base file has 5 tests). Verdict: draftable. Draft written at `pr-drafts/search-components/SOLR-12044.md` (3,458 characters, names the head). One FIX first: finding 1. Interaction with 13851: finding 28.
- SOLR-13851: audit only. Head `b60f4d642d1` unchanged in the record. Verdict: stays held (public `getFirstMatch` behavior). Before any PR: remove the handoff file and the Claude trailer, and rebase. Findings 23 to 28.
- SOLR-18196: audit only. Already merged via PR #4995 (`b5c71bc5573`); the tip `f3248fe2310` has test blobs identical to main. Verdict: retire candidate, owner call. Not drafted. Findings 29 and 30.
- SOLR-18506 (live PR 5029): consistency pass. Head `77c019e1ff0` matches branch, receipt and PR. Verdict: text and check mismatch; hold public replies. Findings 2 to 11 and 31. No PR text drafted.

## Owner decisions

1. SOLR-12044: approve the changelog type change to `changed`. Then re-check the changelog at the new head and decide whether to keep the choice section.
2. SOLR-18506: read run 37384974639 (head SHA and failing test) before any reply. Choose wording for findings 3 to 8. Paste Jira v2, not v1. Decide on the commit subject in `3c098439882` (finding 10).
3. SOLR-13851: decide whether to rewrite the fork branch to drop the Claude trailer and the handoff file, and to rebase. Keep it held.
4. SOLR-18196: delete the branch or keep it as a candidate. The Jira reporter still needs to confirm.
5. SOLR-8088: keep the builder-level check only, or add a cluster test for the 400 claim. Decide whether the ticket's multiValued premise holds (finding 12).
6. SOLR-9595: narrow the ticket to Terms and live docs, or also cache the doc values.
7. SOLR-10694: narrow the ticket to CSV, or check and cover the other writers.

## Not checked

- Live heads: no fresh `ls-remote` or fetch. I used the origin remote-tracking refs on disk, which match the claim table for all seven branches.
- Gate logs: none on disk, so all counts come from receipts.
- No build, no test, no compile. API names were checked by reading.
- Lucene 9.x and 10.x: the Lucene source is not in this repo. No Lucene behavior claim was checked against either line. The 12044 draft makes no Lucene claim.
- Jira: no live Jira read. Packets used: `research/jira-context` for 8088, 9595, 10694, 12044, 18196 and 18506. No packet exists for 13851.
- PR 5029 failing run: head SHA, failing test name and log not read (outside the allowed `gh` calls).
- SOLR-18506 Jira v2 text: not found on disk in this pass.
- SOLR-8088: no sweep of the about 142 `group.field` references in core tests against multiValued fields. Many are variable-driven. A grep found no grouping on `cat`.
- SOLR-10694: the XML, JSON, Python, PHP, Ruby and Velocity writers were not checked for the same stubs.
- SOLR-12044: the 91 two-argument `getDocSet` call sites were not checked for the filter invariant.
- SOLR-13851: `getFirstMatch` behavior beyond the diff was not audited.
- SearchHandler was not touched.
