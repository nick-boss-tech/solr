# Core admin round 1, part k4: 9750, 15805, 16108, 16849, 18278, 18317

Result: 9750 and 15805 are draftable after the FIX items below, 16849 is draftable as a regression test with an honest Proof, 16108 is held with no draft, 18278 is a retire with no draft, and 18317 is confirmed banked with no draft.

## Findings

1. **FIX, 15805 and 16108: Claude co-author trailers in two commits.** Commit `b2a463cf64f` (SOLR-15805 branch, under head `3432f950f0ae`) and commit `70ad7371b31` (SOLR-16108 head) both end with `Co-Authored-By: Claude Sonnet 5.5 <noreply@anthropic.com>`. The pr-formula rule says no "Claude" in authors, committers, or trailers. Replacement: delete the trailer line from both messages before any push or PR. Rewriting a fork branch needs your explicit OK. Nothing was changed.

2. **FIX, 16108: the branch does not fix the reported case, so do not claim it does.** Evidence: the JIRA reproduction uses one route value, `france`, for every document. In `SplitOp.java` at head `70ad7371b31`, `getHashHistogramFromRouteField` (lines 535 to 564) makes one bucket per distinct route value. With one bucket, `getSplits` takes the `counts.size() == 1` path (line 612) and bisects the shard range (lines 620 to 634). `SolrIndexSplitter.java` line 820 (unchanged on the branch) places each document by `sliceHash(routeValue)`. All documents with that value share one hash, so they all land in one half. Round 28 finding 1 (HIGH, `research/branch-reviews/round-28/SOLR-16108-review.md`) reached the same result. Replacement: no PR text may say the reporter's distribution is fixed. If the branch is kept, the changelog title at `changelog/unreleased/SOLR-16108.yml` line 1 should read: "SPLITSHARD with splitByPrefix on a collection that uses router.field now builds the split ranges from the route field values." and Limits should add: "Documents that share one route value still land in the same sub-shard, because a hash-range split cannot separate them."

3. **NOTE, 16108: behavior change not in the changelog.** When `router.field` is set, the `id_prefix` histogram is no longer read at all (`SplitOp.java` head lines 289 to 298). On base `14c7aac0d15` the `id_prefix` histogram was read first (lines 280 to 284). No test and no changelog line covers this. Replacement Limits line: "For collections with router.field, splitByPrefix no longer reads id_prefix buckets."

4. **NOTE, 16108: tests cover the helper only.** `SplitHandlerTest.java` lines 296 to 346 call `getHashHistogramFromRouteField` directly, and no split is run. The helper does not exist on base, so the receipt's "inconclusive by construction" is correct. Keep that wording. Count check: base has 3 `@Test`, head has 5, which matches the receipt.

5. **FIX, 9750: the changelog does not state the upgrade step.** `changelog/unreleased/SOLR-9750-graph-paramset-name.yml` lines 2 to 3 say the name changed but not that users must act. Anyone who set `/graph` defaults under `_ADMIN_GRAPH` loses them. Replacement for lines 2 to 3: "The paramset of the implicit /graph request handler is now named _GRAPH instead of _ADMIN_GRAPH, matching the naming of the other implicit paramsets. Users who set /graph defaults under _ADMIN_GRAPH must rename that paramset to _GRAPH."

6. **FIX before submission, 9750: process words in the history.** Commit `05a5622757c` ("add hypothetical-reproduction handoff doc", body "Hypothetical, unrun regression test; see SOLR-9750-TESTING.md") and commit `f97da6da14a` ("remove the hypothetical-reproduction handoff doc") net to no file at head. Replacement: squash those two commits away before the PR, so no handoff text reaches the PR commit list. This is a history rewrite and needs your OK.

7. **NOTE, 9750: the test reads a resource, not the handler.** `TestImplicitPlugins.java` lines 46 to 58 read `ImplicitPlugins.json` and check the value. They do not send a request to `/graph`. The draft's Limits says so. No change needed.

8. **NOTE, 15805: the ticket names a class that is gone.** `SolrDispatchFilter` is not in main. Commit `518455f3f36` (SOLR-18112, "SolrDispatchFilter is now SolrServlet") renamed it. The startup catch is now in `CoreContainerProvider.java`, lines 186 to 194 at head `3432f950f0ae`. That file has no diff between the branch base `b5c71bc5573` and upstream main. The draft cites `CoreContainerProvider`. No change to the branch.

9. **NOTE, 15805: the test accepts any RuntimeException.** `CoreContainerProviderTest.java` lines 56 to 58 use `expectThrows(RuntimeException.class, ...)`, so an unrelated startup failure would pass too. Replacement: keep as is, and keep the Limits line in the draft that says so. Tightening it to a `SolrException` check is optional and was not verified by a run.

10. **NOTE, 15805: the receipt's PASS does not record the base failure.** The receipt says "PASS (pre-fix proof step)" but not what failed. On base, `CoreContainerProvider.java` lines 186 to 193 log the exception and do not rethrow a RuntimeException, so `expectThrows` sees no exception. By reading, the first assertion fails on base. Replacement receipt line: "Pre-fix step: fails on the base code at the expectThrows assertion (by code reading; the failure text is not on disk)." The main side should confirm this from its log.

11. **NOTE, 15805 and 16887: same file, different hunks.** 16887 (head `42675f65d6f`) edits the OOM logging block in `CoreContainerProvider.java` around lines 214 to 234. 15805 edits lines 186 to 194. A trial merge of the two heads is clean (tree `180f20a0cf74`). Landing order: either order. Recheck after both move. This is k6's ticket, and I did not audit 16887.

12. **NOTE, 15805: no overlap with the lifecycle cluster.** 15805 changes only `CoreContainerProvider.java`, its new test, and its changelog. It has no `NodeConfig.java` or `SolrXmlConfig.java` hunk, so it does not touch 17297 or 17377.

13. **FIX before submission, 16849: the commit subject uses internal words.** Head commit `d612b055da2` has the subject "SOLR-16849: test-only salvage: pin that a read-only core serves segment info (the reported error was already fixed on main by SOLR-18083; the banked production change would only withhold ...)". Replacement subject: "SOLR-16849: Add a test that segment info works on a read-only core". Replacement body: "Adds one test for /admin/segments with coreInfo on a read-only core. No production change." This is a history rewrite and needs your OK.

14. **NOTE, 16849: a queued fail-before job is pending.** `research/test-queue/queue.json` has a pending SOLR-16849 job (enqueued 2026-10-03 18:04:07, `withFailBefore: true`). The test passes on base by design, so that stage would report NOT_PROVEN and leave the job queued. Replacement: the main side cancels that job, or marks fail-before not applicable, before any drain.

15. **NOTE, 16849: the on-disk run has no head.** `research/test-queue/results/SOLR-16849.json` (SUCCESS, 7 tests, 2026-10-03, log `SOLR-16849-20261003-094631.log`) records no head SHA. The receipt cites `g16849-gate.log` at `d612b055da2` (2026-10-04), which is not on disk. The draft's Proof cites only the receipt, with its date.

16. **NOTE, 18278: the diff keeps the Jetty dependency; retire.** At head `bf19c9fa449`, `HttpSolrClientProvider.java` lines 52 to 63 still check `instanceof HttpJettySolrClient.Builder` and cast the result with `(HttpJettySolrClient)` on line 63. With Jetty on the classpath, `HttpSolrClient.builder(null)` returns the Jetty builder (base `HttpSolrClient.java` lines 420 to 436), and `withBaseSolrUrl(null)` only stores null (lines 487 to 489). So there is no behavior change. `TestHttpSolrClientProvider.java` has no diff from base, so the 2 of 2 pass on base too. Replacement: none, retire. If you keep the branch, delete the comment at lines 49 to 50 ("so the provider does not hard-code the Jetty builder"), which is wrong given line 63.

17. **NOTE, 18278: the round 7 conflict is stale.** Round 7 said the file was rewritten upstream and would conflict. SOLR-18360 (`553da8f55e1`) is already in the branch base `14c7aac0d15`. A trial merge onto upstream main is clean (tree `1ce563cc294e`). Replacement: do not repeat round 7's conflict claim in any retire note.

18. **NOTE, 18317: compare the banked branch with its own base.** Base `0d2a4649c79` is 86 commits behind upstream main, where PR #5001 merged as `e432df19c4a` (2026-10-05). `git diff upstream/main origin/solr-18317-server-submit` shows 797 files, mostly drift. The useful diff is against `0d2a4649c79`: 8 commits, 16 files, +451 and -32. Replacement: any review or receipt for this branch uses that base.

19. **FIX before any server PR, 18317: the banked branch conflicts with main and still carries shipped UI work.** A trial merge onto upstream main (tree `648e5945d0c2`) has one conflict: `changelog/unreleased/SOLR-18317.yml` (add/add). `app.js` and `services.js` merge without conflict, but the banked branch still carries UI changes that #5001 already shipped. Replacement: a future server PR drops the three UI files and the UI changelog text, and uses a server-only title: "Standalone nodes treat a direct nodes=all request on the logging and system-info APIs as this node, instead of failing with a NullPointerException on ZooKeeper."

20. **NOTE, 18317: the earlier passing run is not this head.** `research/test-queue/results/SOLR-18317.json` (SUCCESS, 19 tests, 2026-09-27, run in the main source checkout) and `research/114-solr18317-research-note.md` lines 420 to 428 describe an earlier seven-file server package. Annotation counts at the banked head are `GenericV1RequestProxyTest` 10 and `V2SolrRequestBasedProxyTest` 9, against 7 and 7 in that run. These counts are approximate, since parameterized tests were not expanded. Replacement receipt line: "An earlier focused run (2026-09-27, 19 tests) covers an earlier seven-file package, not this head." The receipt's "no gate at this head" stands.

## Task results

**Heads and PR state.** Local remote-tracking refs match the claim's live table for all six: 9750 `f97da6da14aa`, 15805 `3432f950f0ae`, 16108 `70ad7371b31`, 16849 `d612b055da20`, 18278 `bf19c9fa4498`, 18317 `ae918a03fa7`. A read-only `gh pr list --author nick-boss-tech --state open` on 2026-10-10 lists no open PR for any of the six heads. PR #5001 (18317) is merged. All commit authors are Nick Shanin. Two commits carry Claude trailers (finding 1).

**SOLR-9750: draftable after findings 5 and 6.** The premise holds: the other implicit paramsets follow the endpoint rule (checked against `_ADMIN_SEGMENTS`, `_EXPORT`, and the rest of the list). The diff is four files and matches the inventory. The receipt's 11 tests match the code: `TestImplicitPlugins` 2 `@Test`, `TestSolrConfigHandler` 8 public test methods, `TestReqParamsAPI` 1 `@Test`. Draft: `pr-drafts/core-admin/SOLR-9750.md`, written against head `f97da6da14aa`. It poses one Choice (plain rename or a one-release fallback). You may drop it.

**SOLR-15805: draftable after finding 1 and the receipt wording in finding 10.** The premise holds on main, since `CoreContainerProvider` has not changed from the branch base. The change is small and the changelog is accurate. Draft: `pr-drafts/core-admin/SOLR-15805.md`, written against head `3432f950f0ae`. It poses one Choice (fail the context at startup, or keep it up and answer 503). You may drop it.

**SOLR-16108: held, no draft.** Verdict: needs work. The branch does not change the reported JIRA case (finding 2), and its tests are inconclusive by construction (finding 4). The branch also changes behavior without a changelog line (finding 3). The receipt says an owner call is open, but the question is not on disk. The round 28 review and this audit agree on the cause.

**SOLR-16849: draftable as a regression test, with an honest Proof.** The reported 503 is already fixed on main and on the branch base: `f2aaf8769fd` (SOLR-18083) is an ancestor of `14c7aac0d15`, and it changes both index-writer calls in `GetSegmentData.java` (lines 156 and 260). COLSTATUS sends its per-core request to `/admin/segments` (`ColStatus.java` line 204), which goes through that code. The new test passes on base, so the draft says it is a regression pin and not a fail-first proof. Draft: `pr-drafts/core-admin/SOLR-16849.md`, written against head `d612b055da20`. No changelog, since the change is test-only. The owner decides whether to open the PR or close the ticket as fixed by SOLR-18083 (see owner decisions). Findings 13 and 14 apply if the PR goes ahead.

**SOLR-18278: retire, no draft.** The audit agrees with rounds 7 and 12. The ticket is resolved as not a bug, and the diff changes no behavior with Jetty present (finding 16). No draft, because the audit does not argue against retiring.

**SOLR-18317: banked state confirmed, no draft.** Head `ae918a03fa7` and base `0d2a4649c79` match the banked record. The branch has 8 commits and 16 files (+451, -32), and no gate exists at this head. It conflicts with main only in the changelog, and it still carries UI work that #5001 already shipped (findings 18 to 20). The owner decides whether it stays banked.

## Owner decisions

1. **16108: hold, narrow, or close.** A same-key route value cannot be split by hash range, by construction (splitter line 820, base). Decide whether the ticket is narrowed to distinct route values, or closed. The receipt's open owner call is not on disk.
2. **16849: open the regression-test PR, or close as fixed by SOLR-18083.** The design record allows closing once the test is in a PR. If the PR goes ahead, reword the commit (finding 13) and cancel the queued job (finding 14).
3. **18278: retire.** Rounds 7 and 12 and this audit all say retire. The retire call is yours per the receipt.
4. **18317: keep banked.** A server PR only if a reviewer asks. It needs a rebase onto main and a server-only changelog (finding 19). The inventory row `solr-18317-submit` (retire candidate, #5001 merged) is not audited here.
5. **9750: plain rename, or keep `_ADMIN_GRAPH` as a one-release fallback.** The draft asks maintainers (Choice). Drop it if you prefer.
6. **15805: fail the context at startup, or keep it up with 503.** The draft asks maintainers (Choice). Drop it if you prefer.
7. **History rewrites on fork branches** (findings 1, 6, 13). Each needs your explicit OK. Nothing was rewritten.

## Not checked

- No builds, tests, or Gradle, per the rules. Test counts come from the receipts, plus code reading of `@Test` and `public void test` counts.
- Gate logs are not on disk: `g9750-gate.log`, `g9750-premise.log`, `g15805-harden.log`, `g16108-harden.log`, `g16849-gate.log`. The takeover log (18278, 2026-10-04) and the receipts ledger (16849, 2026-10-04) are not on disk either.
- The base failure text for 15805 and the fail-before outcome for 16108 are not on disk. Both are from code reading.
- How Jetty treats a servlet context that fails at startup (15805) was not checked.
- Only 16887 was checked for overlap in `CoreContainerProvider.java`. Other branches touching `ImplicitPlugins.json` (9750) or `SplitOp.java` (16108) were not swept.
- The term-dictionary path of `SolrIndexSplitter` (16108) was not traced. I read only the per-document hash at line 820.
- Remote heads were compared with the claim's live table. No `git ls-remote` was run.
- Lucene version claims: none appear in my drafts. Other text was not checked for Lucene versions.
- The 18317 count comparison is approximate (finding 20).
- Drafts were not counted exactly. Their sizes are 3,186, 3,256, and 2,481 characters.
