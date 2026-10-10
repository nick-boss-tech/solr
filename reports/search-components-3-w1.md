# Search components round 1, sub-batch 3, part w1: SolrReturnFields pair (SOLR-4374, SOLR-7390)

Result: both tickets are draftable. Drafts are in `pr-drafts/search-components/SOLR-4374.md` (against 801c62290f79483e714e88f907a6564e504dfca5) and `SOLR-7390.md` (against 7463dd006a7825db46f00b8a4790650c5bb51e61). The receipts hold, the two changes do not overlap, and either landing order gives the same merged tree, which is not built. The most important FIX: the local branches carry unpushed commits that add the internal test notes file, so they must not be used as the outbound heads.

## Findings

1. FIX. The local branches are not the live heads. Commands run: `git rev-parse` on the branch names, `git diff --name-status origin/solr-4374-submit solr-4374-submit` (output `A SOLR-4374-TESTING.md`), and `git diff --name-status 72caa363e29b origin/solr-7390-submit` (output `D SOLR-7390-TESTING.md`). The local `solr-4374-submit` is 1f9a223c6f19. Its history is the pre-rewrite chain (daeab73741a, 4609ea3f098) plus a notes commit (1f9a223c6f1), and its tree differs from the live head only by SOLR-4374-TESTING.md (25 lines). The local `solr-7390-submit` is 72caa363e29b. Its history is the pre-rewrite chain (604f66e352f, 59536ca7b98) plus a notes commit (72caa363e29), and its tree differs from the live head only by SOLR-7390-TESTING.md (39 lines). The remote-tracking refs `origin/solr-4374-submit` (801c62290f7) and `origin/solr-7390-submit` (7463dd006a7) match the claim table. The inventory line 597 records the 4374 head move. Replacement: "Outbound heads: 801c62290f79483e714e88f907a6564e504dfca5 (SOLR-4374) and 7463dd006a7825db46f00b8a4790650c5bb51e61 (SOLR-7390). Do not commit or push from the local branches until they are moved to these heads, with owner approval." Note that `branch-work.ps1 commit` runs `add -A`, so the notes file would ship if committed from these branches.

2. FIX (SOLR-4374 Proof). The receipt (`receipts/SOLR-4374.md`, Proof line) names the failing test, `testDigitLeadingFieldName`, and a count of one failure. It does not record the assertion message. The gate logs are not on disk (see Not checked). Replacement: the draft's Proof line as written, which names the test and the counts and no message. Do not add an assertion message unless the gate log is pulled from the main side.

3. FIX (SOLR-4374 Limits). The test uses the dynamic field `1001_s` (`solr/core/src/test/org/apache/solr/search/ReturnFieldsTest.java` lines 399 to 406 at 801c62290f7). The ticket's example is a field defined as `1001` (`research/jira-context/SOLR-4374.json`, Description). The test schema, `schema12.xml`, defines only `*_s` (line 742 at 0cc328310f8). So the ticket's exact case is not tested. Replacement Limits bullet: "The test uses 1001_s. The ticket's example is a field defined as 1001. That exact case is not tested." Owner options are in Owner decisions.

4. FIX (SOLR-4374 scope). Rename forms are not covered. The key path reads the name with `StrParser.getId`, which needs an identifier start (`solr/core/src/java/org/apache/solr/search/StrParser.java` lines 188 to 192 at upstream/main 8e62c2686882). The new call runs only at the top level (`SolrReturnFields.java` lines 286 to 289 at 801c62290f7). So `fl=x:1001` is still read as a constant. Evidence is code reading only, not a run. Replacement Limits bullet: "A rename such as x:1001 is not changed. The rename path still reads names with the stricter check."

5. FIX (SOLR-7390 What this change does). The throw reaches every core, and one existing assertion changes. At `0cc328310f8`, the else branch in `SolrReturnFields.java` (lines 386 to 389) was empty. At 7463dd006a7 the same lines hold the throw statement (lines 387 to 388). Registration of `[elevated]` is in `QueryElevationComponent.java` lines 232 and 239 at upstream/main, so a core without that component has no factory for it, and `fl=id,[elevated]` now returns 400. The base test asserted `[xxxxx]` was ignored (`ReturnFieldsTest.java` base lines 292 to 297), and the head now expects the exception at lines 293 to 300. Replacement: the draft's two behavior bullets as written.

6. FIX (SOLR-7390 Proof provenance). The receipt says the gate ran at 72caa363e29, not at the live head. Checks run: `git diff --stat 72caa363e29b origin/solr-7390-submit` shows only the deletion of SOLR-7390-TESTING.md (39 lines). `git diff 604f66e352f 4ca1ce4da15` and `git diff 59536ca7b98 7463dd006a7` are both empty, so the code and changelog trees are identical. The claim "code content identical" holds. 72caa363e29 is a local commit only and is not on origin. Replacement: the draft's sentence "The run used an earlier commit on this branch with identical source and test files." The draft does not cite the local SHA. Owner may choose a re-run at the live head (Owner decisions).

7. NOTE (SOLR-7390 receipt wording). The receipt says the neighbor battery ran "at the same head", which is ambiguous between 72caa363e29 and 7463dd006a7. The draft says only that the neighbor classes ran "on the same source", which holds for both. Replacement: none. Ask the main side to name the neighbor run's head in the receipt.

8. NOTE (SOLR-4374 behavior beyond the ticket). `IndexSchema.getFieldTypeNoEx` falls back to dynamic fields (`solr/core/src/java/org/apache/solr/schema/IndexSchema.java` lines 1458 to 1462 at upstream/main). A wildcard `*` dynamic field matches every name, so a top-level number such as `fl=1.5` is read as a field name under such a schema. Evidence: wildcard dynamic fields exist in test schemas `schema-minimal.xml` line 20 and `schema-root.xml` line 28 (0cc328310f8). The default configset `_default` has none (grep empty). No existing ReturnFieldsTest case covers a top-level number; the only numeric case is `sum(1,1)` inside a function. Replacement: the draft's "Behavior change, stated openly" paragraph, and the Choice section, both as written.

9. NOTE (SOLR-7390 typeless bracket). For `fl=[foo=bar]` the type name is null. The transformer bag is built without thread safety (`SolrCore.java` lines 3087 to 3088, `PluginBag.java` lines 129 to 130), so `get(null)` returns null, and the throw at `SolrReturnFields.java` line 388 gives "Unknown DocTransformer: null". Replacement: the Limits bullet as drafted. A wording change needs a code change and a re-gate, so it is an owner choice.

10. NOTE (landing order and overlap). Both orders give the same tree. Commands: `git merge-tree --write-tree --merge-base=0cc328310f8 origin/solr-4374-submit origin/solr-7390-submit` and the reverse order. Both exited 0 with tree 20679cf27977a8205dd5281e84080678c37acca3. Against the 4374 tip, the merged tree differs only by the 7390 hunks: `SolrReturnFields.java` merged lines 413 to 420, `ReturnFieldsTest.java` merged lines 30 to 36 (import) and 290 to 303 (testTransformers). The 4374 hunks are at its own lines 240 to 264, 286 to 289, and 399 to 406. No line ranges overlap. Both target files are identical at both bases and at upstream/main, so both patches apply cleanly. Landing order: no dependency. Either lands first, and the second needs no conflict resolution. The merged tree has not been built.

11. NOTE (bases). The 4374 base (cabedd1d968) is 37 commits behind upstream/main (8e62c2686882). The 7390 base (0cc328310f8) is 45 commits behind. No upstream commit since either base touches the two target files (`git log` over those files is empty). Test counts match the number of `public void test` methods: 16 at 801c62290f7 and 15 at 7463dd006a7. Three base methods (testMovePk, testWhiteboxSolrDocumentConversion, testWhitespace) have no `@Test` annotation, and the receipt counts include them. Replacement: none.

12. NOTE (inventory). Inventory line 193 says SOLR-4374 has "4 files total". The live branch has 3 files (changelog, SolrReturnFields.java, ReturnFieldsTest.java). The fourth is the local notes file in Finding 1. Replacement: none; Finding 1 covers it.

13. NOTE (checks that pass). Both changelog fragments use valid types (`fixed` for 4374, `changed` for 7390, per `changelog/logchange-config.yml`). `dev-docs/changelog.adoc` says only `name` is required under authors. Neither fragment has "ICLA pending" or "Solr Issues Workspace". No commit in either range has a Claude or Co-Authored-By trailer. Commit authors are Nick Shanin. Replacement: none.

## Task results

**SOLR-4374: draftable.** Draft `pr-drafts/search-components/SOLR-4374.md`, written against 801c62290f7. The receipt matches the live head, and the Proof uses only the receipt's test name and counts. The draft states the Choice (keep the schema lookup, or accept every name that starts with a digit, as the ticket proposes), the catch-all behavior change, the untested exact case, and the rename gap. Open items: Findings 1 to 4 and 8. Landing: no dependency on 7390 (Finding 10).

**SOLR-7390: draftable.** Draft `pr-drafts/search-components/SOLR-7390.md`, written against 7463dd006a7. The receipt's claim that the gate ran on identical code is confirmed (Finding 6). The draft states the wider reach (Finding 5), the typeless-bracket message (Finding 9), and the Choice from the ticket's second comment (Ryan McKinley, 2015-04-15, `research/jira-context/SOLR-7390.json`, comment 14495672). The round-28 review (`research/branch-reviews/round-28/SOLR-7390-review.md`, finding 1) raises the same question. The review was read as context only.

## Owner decisions

1. Local branches: approve moving `solr-4374-submit` and `solr-7390-submit` to the live heads (or dropping the local-only commits) before any commit or push (Finding 1).
2. SOLR-7390 Proof: keep the drafted "earlier commit with identical source" sentence, or run the focused proof at 7463dd006a7 first. A run is a verify action, so it is your call.
3. SOLR-4374 Choice: keep the schema lookup (drafted), or switch to the ticket's proposal. The second is a code change and needs a re-gate.
4. SOLR-4374 exact case: keep the Limits line (drafted), or add a test that defines a field named `1001`. The second needs a schema change and a re-gate.
5. SOLR-4374 rename form: keep it in Limits with a follow-up offer (drafted), or extend the change now (code change, re-gate).
6. SOLR-7390 behavior: keep the 400 with the drafted Choice, or return to silence like unknown fields.
7. SOLR-7390 `null` message: accept and state it (drafted), or change the message (code change, re-gate).
8. Landing order: either order works, and there is no dependency. The merged tree (20679cf27977) is not gated. Decide whether it needs its own run before both PRs open.

## Not checked

- No builds, tests, or gate logs. The gate logs named in the receipts (g4374-gate.log, g4374-premise.log, g7390-gate.log, g7390-premise.log, g7390-neighbors.log) are not on disk. A search of `research/`, `research/test-queue`, and this worktree found none. Receipt counts were not reproduced.
- Base behavior for `1001_s` and `1001` was not run. The receipt does not say which 4374 base assertion failed. I did not trace the function parser or the SyntaxError fallback (`SolrReturnFields.java` lines 412 to 419) to decide which assertion fails, so the draft does not name one.
- Live heads were not re-queried with `git ls-remote` in this pass. The local origin refs match the claim table (801c62290f7, 7463dd006a7).
- No `gh` calls were made. No PR number is recorded for either ticket, and `gh pr list` is not on the allowed call list. Live PR state and CI were not checked. The GitHub run numbers in the receipts (37626579871, 37401341483) were not checked.
- Jira was read from the hydrated packets in `research/jira-context`. Those files do not record a fetch date. Live Jira was not queried.
- Other test classes that send unregistered bracket names were not swept. The receipt covers only the three classes named.
- The merged tree was not compiled. The textual merge is clean, but compile-level interaction is unchecked.
- Spotless and tidy results come from the receipts only.
- Lucene: no draft names Lucene behavior, so the 9.x and 10.x check does not apply.
- No draft was posted or pushed. Nothing was committed.
