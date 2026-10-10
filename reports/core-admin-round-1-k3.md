# Core admin round 1, part k3: core lifecycle and NodeConfig

Result: 12007 and 17377 are drafted and draftable (12007 needs Nick's route call, 17377 needs its base failure line pasted); 17297 is drafted but held for an owner ruling on the reverse order; 4502 and 5011 are audit only and not ready; 11431's live PR head matches the branch, and its body needs two fixes.

Read only. Nothing built, tested, committed, posted, or pushed. Heads used are the origin refs (they match the claim's live heads): 4491f5162c1d (4502), f20ffe480781 (5011), 968fad873c4b (11431, PR #5002), bdeba582fd63 (12007), c0ab38fc0a8b (17297), 22b5f209a11a (17377). Local branch refs for 11431, 12007 and 17297 are stale; see NOTE 15.

Drafts: `pr-drafts/core-admin/SOLR-12007.md`, `pr-drafts/core-admin/SOLR-17297.md`, `pr-drafts/core-admin/SOLR-17377.md`. The 17297 and 17377 drafts carry bracketed placeholders that must be filled before posting.

## Findings

1. **FIX** (live PR #5002 body, Limits, first bullet, CloudSolrClient sentence). Evidence: `solr/solrj/src/java/org/apache/solr/client/solrj/impl/CloudSolrClient.java` (upstream/main 8e62c2686882) sends a 503 route error into the communication-error block at line 729. That block marks the cached state maybe stale, may refresh it, and retries through `requestWithRetryOnStaleState` (lines 757 to 771, retry guarded by `retryCount < MAX_STALE_RETRIES`). The 404 at line 798 sits in a different stale-state retry, so the body's "treats a 503 like a 404 in its stale-state retry" is not accurate, and the retry itself is not mentioned. Replacement: "`CloudSolrClient` treats a 503 route error as a communication error. It marks the cached state of the collection as maybe stale, may refresh it, and retries the request up to its stale retry limit (`CloudSolrClient.java`, lines 729 to 771). A 500 does not take that path."

2. **FIX** (live PR #5002 body, Proof, two spots). Process wording: "re-run in this round" (Proof, second bullet) and "verified against the base code in this round" (the "No PeerSync test is added" bullet). Shared rules bar internal process vocabulary in public text. Replacements: "Base comparison, run with this head's test files against the base production code:" and, in the PeerSync bullet, "checked against the base code;" in place of "verified against the base code in this round;".

3. **FIX** (4502 branch, readiness). `SOLR-4502-TESTING.md` (21 lines, added at tip 4491f5162c1d) is in the branch diff: `git diff --name-status cabedd1d9680 origin/solr-4502-submit` shows `A SOLR-4502-TESTING.md`. It must not ship. Remove it in the ticket commit before any PR. Not done here.

4. **FIX** (5011 branch, readiness). `SOLR-5011-TESTING.md` (28 lines, tip f20ffe480781) is added by the branch, same as NOTE 3. Remove before any PR.

5. **FIX** (17297 and 17377 drafts, Proof). The base failure line is not in any receipt or on disk. The 17297 receipt records only "PASS (pre-fix proof step)"; the 17377 receipt says "exactly 1 failure ... with the ticket's shape" without the message. Each draft has a placeholder. Paste the observed base line from the base run before posting. Do not write the message from memory.

6. **FIX** (17297 draft and any PR text, behavior change). The module-first order changes which copy wins when a module and a shared lib jar both provide a class with the same name. The module copy now wins. Evidence: `solr/core/src/java/org/apache/solr/core/NodeConfig.java` lines 228 to 229 (order swapped at c0ab38fc0a8b); `SolrResourceLoader.java` lines 270 to 271 append new URLs after the old ones, so earlier-added jars are searched first. Neither the changelog nor the branch states this. The draft states it. Keep that sentence in any PR text.

7. **NOTE** (live PR #5002 body, Limits, fourth bullet). Bare "#5010" in "Interaction with #5010 (SOLR-12998)". Same repo, so it resolves, but the formula asks for full links. Optional replacement: "[#5010](https://github.com/apache/solr/pull/5010)". Checked: `origin/solr-12998-submit` tip is 62a17a116b5b, the same as the live #5010 head in the open-PR list.

8. **NOTE** (live PR #5002 body, citations). Class and file names are in backticks with no links, and the Changelog line is not a link. The formula's presentation rule asks for file citations as links at the head SHA (968fad873c4b35fff0605ae88dbecca16d1dfa2f). The template itself shows the changelog path in backticks, so Nick should pick which rule wins.

9. **NOTE** (live PR #5002 body, length). Roughly 5,000 characters by eye, over the 3,500 guide. Not counted exactly. Trim candidates are the #5010 bullet and the PeerSync paragraph.

10. **NOTE** (4502, guard coverage). The guard is in the public `create` only: `solr/core/src/java/org/apache/solr/core/CoreContainer.java` lines 1503 to 1507 at tip 4491f5162c1d. Premise holds: `shardHandlerFactory` is assigned only in `loadInternal()` (CoreContainer.java line 789 on upstream/main), and `SearchHandler` reads it at `inform` (SearchHandler.java line 180). `registerCore` is protected (CoreContainer.java line 1442). `SyntheticSolrCore.createAndRegisterCore` also calls `registerCore` and is not guarded; I did not trace whether it can run before load.

11. **NOTE** (4502, design alternative). Alan Woodward's comment in the Jira (jira-context/SOLR-4502.json) proposes calling `load()` from the constructor. The branch takes the guard instead. This is a live alternative for the owner (owner list).

12. **NOTE** (5011, shared schema risk, not checked by run). With `shareSchema` on, `ConfigSetService` caches the `IndexSchema` per config set (`solr/core/src/java/org/apache/solr/core/ConfigSetService.java` line 269, lookup near 300 to 320). `IndexSchema` keeps the loader it was built with (`solr/core/src/java/org/apache/solr/schema/IndexSchema.java` lines 127 and 203). After 5011, closing core A closes the loader that a cached schema still holds for core B. The option is off by default (`SolrXmlConfig.java` lines 364 to 365, `boolVal(false)`). I did not trace whether B's request path uses that loader after the close. A first gate must cover this or the close must be narrowed.

13. **NOTE** (5011, test hook and unverified guess). `SolrResourceLoader.isClosed()` (5011 tip, line 917) is package-private and exists only for the test. The TESTING note's first guess ("nothing uses the old core's loader after close") is unverified.

14. **NOTE** (12007, timing). The test relies on a 500 ms sleep in the fixture (`SolrCoreCleanupOnCloseTest.java` line 52) and on 30 s and 10 s wait loops (lines 117 and 124). Acceptable, but it is timing based.

15. **NOTE** (local refs and main-side notes). Local branches differ from origin: `solr-11431-core-init-503` at a51a0339b005 (origin 968fad873c4b), `solr-12007-submit` at 50a0052c1489 (origin bdeba582fd63), `solr-17297-submit` at 8159d227245c (origin c0ab38fc0a8b). There is no local `solr-17377-submit`. This audit used origin refs only. The main-side `pipeline/HANDOFF.md` line 89 and the 12007 entry in `pipeline/queue.json` describe an older test and a TESTING file the branch no longer has. Refresh those notes when the PR is prepared.

16. **NOTE** (dates in Proof lines). The receipts give record dates (takeover log), not run dates. The drafts say "verified" with those dates: 12007 2026-10-08, 17377 2026-10-05, 17297 2026-10-04 (gate; tidy re-check 2026-10-07), 11431 2026-10-06 (from its PR body). Nick should confirm the run dates or change the wording to "recorded".

17. **NOTE** (17297 changelog title, about 190 characters). Shorter replacement that keeps the scope: "Lucene SPI plugins in the shared lib directory no longer fail with ClassNotFoundException when modules are enabled". The title is accurate for the shared lib direction only.

18. **NOTE** (17297, reverse order, owner decision). Round 28 review, finding 1 (HIGH): a module SPI can lose its helper classes when the shared lib setup later replaces the loader (`addURLsToClassLoader`, lines 263 to 281). The receipt says a probe test confirmed this at c0ab38fc0a8, and that the probe was not committed. The probe log is not on disk, so this is recorded as reported, not re-run. The draft carries it as a Choice and a Limits line.

19. **NOTE** (17377, module path untested). The changelog says "from a module or the shared lib directory". The test covers only the shared lib directory (round 28, LOW). The draft's Limits says so. A module-backed test would need a new gate.

20. **NOTE** (17377, unverified package-prefix question). Round 11 review: "Not verified: whether a `package:`-prefixed singleton class can appear in solr.xml; if it can, it is not loadable at node-config time under any option." Still open. Owner list.

21. **NOTE** (NodeConfig hunks, 17297 against 17377). Textual conflict, the only one among the six branches (trial merge `git merge-tree --write-tree`). 17297 swaps the two calls at lines 228 and 229 (base order: `setupSharedLib()` at 228, `initModules()` at 229). 17377 adds `validateClusterSingletonClasses()` after `initModules()`, which is line 231 at its tip. Resolution: `initModules();`, then `setupSharedLib();`, then `validateClusterSingletonClasses();`. The two changes are semantically independent, because validation runs after both loader steps in either order.

## Task results

**SOLR-4502 (audit only, not ready, no draft).** The premise holds on upstream/main 8e62c2686882: the factory is null until `loadInternal()`, and the public `create` has no guard. The branch guard (tip 4491f5162c1d) sits before `inFlightCreations` is touched, which is the right place. The TESTING note's three guesses: the null marker is reliable; no caller creates cores on an unloaded container in the 14 files that construct `CoreContainer` (I sampled the `create` callers in those files, not every line); `shutdown()` null-checks the factory at CoreContainer.java line 1299, but I did not trace the other fields. Blockers: no gate (the receipt says NO GATE), the TESTING file (FIX 3), and the owner call on guard versus `load()` in the constructor (NOTE 11). A premise run must show that `create` on an unloaded container produces a core whose search fails with the NPE, or otherwise reproduces the ticket. The first gate must run the new test and the rest of `TestCoreContainer`.

**SOLR-5011 (audit only, not ready, no draft).** The premise holds: no `resourceLoader` close appears in `SolrCore.java` on upstream/main. Each reload gets a fresh loader: `FileSystemConfigSetService.java` lines 77 to 81, `ZkConfigSetService.java` lines 74 to 89, the reload path at `CoreContainer.java` line 1964, and `SyntheticSolrCore` loads its own config set. The branch closes the loader after the post-close hooks (tip lines 1892 to 1905), and 12007 changes an earlier part of the same method, so the two do not collide (trial merge clean). Open risks: NOTE 12 (shared schema) and NOTE 13. Blockers: no gate, FIX 4 (TESTING file). A first gate must show: `CoreCloseResourceLoaderTest` fails on base at its first `assertTrue` (the loader is not closed) and passes at head; and a second test with two cores on one config set and `shareSchema` on, where unloading one core leaves the other able to load a lazy lib class.

**SOLR-11431 (consistency pass; live PR #5002; no draft).** The live PR is open, not a draft, and its head 968fad873c4b matches the branch tip and the receipt. The read returned no reviews and no comments. CI: changelog pass, "Run Solr Tests using Crave.io resources" pass, "gradle check" pass, labeler pass, "generate" skipping. Body facts checked against upstream code, all hold: `SyncStrategy` passes `cantReachIsSuccess` true (`SyncStrategy.java` lines 225 to 231); `PeerSync` counts 503 and 404 as success for GET_VERSIONS only when the flag is set (`PeerSync.java` lines 398 to 420); `checkRetry` retries 404, 403 and 503 (`SolrCmdDistributor.java` line 572); `ResponseUtils` attaches traces and logs ERROR only for 500 and codes below 100 (`ResponseUtils.java` lines 84 and 118 to 119); `LBSolrClient` retry set includes 503 and 500 (`LBSolrClient.java` line 133). The branch diff is 2 commits (503 status change, changelog) and touches only CoreContainer, SolrCoreInitializationException and TestCoreContainer, so PeerSync is unchanged as the body says. The three TestCoreContainer expectations match the diff. The body needs FIX 1 and FIX 2, and NOTES 7 to 9.

**SOLR-12007 (draftable, PR-ready with one Choice).** Receipt gate at bdeba582fd63, matches the tip. Premise holds on base: `SolrCore.java` line 1840 called the background overload on close, and the factory closed later (line 1871 at tip). Draft: `pr-drafts/core-admin/SOLR-12007.md`, head bdeba582fd63. The Choice poses the inline route against a background cleanup with a close-side guard to maintainers. Nick should confirm the route before posting, because the draft already takes the inline route. No open PR from the fork matches this branch. NOTES 14 and 15 apply.

**SOLR-17297 (drafted, HELD).** The premise holds: the base constructor ran `setupSharedLib()` before `initModules()` (NodeConfig.java lines 228 to 229 on base), and the loader replacement closes the old loader (`SolrResourceLoader.java` lines 263 to 281). The test covers only the shared lib direction (`TestCoreContainer.java` lines 466 to 506 at tip). The reverse order is the open owner decision (NOTE 18). The precedence flip is a behavior change the branch does not state (FIX 6). Draft: `pr-drafts/core-admin/SOLR-17297.md`, head c0ab38fc0a8b. Held until Nick picks the narrow or wide fix, and until the base failure line is pasted (FIX 5). If the fix is widened, the Proof and the Choice change and a new gate is needed. NOTE 17 applies to the changelog title.

**SOLR-17377 (drafted, draftable).** The receipt gate is at tip 22b5f209a11a (41 focused tests). The branch ships option 1 of the three options in the round 11 design section (move the check to the end of the `NodeConfig` constructor). Verified in code: the parse-time check is gone from `SolrXmlConfig.java` (lines 656 to 686 at tip); `validateClusterSingletonClasses()` runs at `NodeConfig.java` lines 229 to 231 and is defined at lines 240 to 256; a missing class and a wrong-type class still fail with the same messages, because `SolrResourceLoader.findClass` lets `ClassCastException` through on the full-name path, which reaches the catch. The draft states option 1 as the route taken, with no Choice section, as the assignment asks. Open items: FIX 5 (base line), NOTE 19 (module test), NOTE 20 (package prefix), NOTE 21 (conflict with 17297).

**Cluster and landing order.** Trial merges (`git merge-tree --write-tree --name-only`) between all six branches and with 15805 (`origin/solr-15805-submit`, touches only `CoreContainerProvider.java` and its test) are clean, except 17297 against 17377 in `NodeConfig.java` (NOTE 21). Suggested order: 11431 first (live; its body fixes go in first, no code change). Then 4502 once its TESTING file is removed. Then 5011 and 12007, in either order, each after its own gate and open questions. Then 17297 after Nick's ruling, then 17377 rebased onto it with the three-line `NodeConfig` result. 15805 is independent of all six.

## Owner decisions

1. 17297: ship the narrow fix and track the reverse order as a follow-up (the draft as written), or widen it now (new gate needed). The draft is held until this is chosen.
2. 12007: keep the inline cleanup on close (the branch and the draft), or have close wait on a background cleanup. Confirm the route before posting.
3. 4502: keep the `create` guard (branch), or call `load()` from the constructor (Alan Woodward's proposal). Also confirm that a 500 is the right status for this misuse.
4. 5011: gate it at all before deciding the `shareSchema` question. Either add a shared schema test or narrow the close.
5. 11431: approve the body edits (FIX 1 and FIX 2 at least). The description belongs to Muse, and there are no maintainer comments, so the edit can go through once Nick approves.
6. 17377: confirm that the draft's no-Choice form stands (option 1 is not reopened). Decide whether to add a module-backed test (needs a new gate). Answer the package-prefix question before posting.
7. Base failure lines for 17297 and 17377: paste from the base runs, or hold both drafts.

## Not checked

- No builds, tests or gates. The gate logs named in the receipts are not on disk: g11431r29-gate.log, g12007r38-gate.log, g17297-harden.log, g17297r35-probe.log, g17377-gate.log, and the takeover-log entries. So the 17297 probe result and all base failure lines are unverified.
- No gate exists for 4502 or 5011 (receipts say NO GATE).
- Jira text was read from `research/jira-context/` for 4502, 5011, 12007, 17297 and 17377. There is no packet for 11431 on disk, so its ticket text was not read.
- upstream/main (local ref 8e62c2686882) was used as it stands. I did not fetch. Branch merge bases differ (cabedd1d9680, 14c7aac0d151, 56ec140e3636).
- Trial merges checked textual conflicts only. Semantic interactions were read from code, not run.
- Changelog YAML was not parsed by me. The receipts say it parses.
- Test counts: static method counts agree with the implied totals (17297 and 17377 each add one test method). Nothing was re-run.
- Not traced: `IndexSchema.loader` use at request time after a core closes; `SyntheticSolrCore` callers before load; `SolrResourceLoader.getURLs` picking up `lib/classes` for the 17377 test; `ZkSolrResourceLoader` internals.
- PR #5002 check logs were read as status lines only, not opened.
- The open-PR list (`gh pr list --author nick-boss-tech --state open`) shows no PR from the fork for 4502, 5011, 12007, 17297 or 17377.
- Lucene versions: no draft names a version, so the version rule does not apply.
