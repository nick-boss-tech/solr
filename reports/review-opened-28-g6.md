# Review of the opened update drafts: group 6

Group 6: SOLR-13696 (PR 5088), SOLR-13943 (PR 5089), SOLR-14262 (PR 5090), SOLR-14718 (PR 5091). Read-only. Gate state comes from `receipts/` on `origin/pr-prepare`. PR state comes from `gh pr view` at review time. Branch reads use each PR head. Base for all four: merge base with upstream main.

Common checks: all four PRs are open drafts, base `main`, head `solr-<ticket>-submit`. All four heads match the gated head in their receipt. All four bodies match their drafts exactly, compared after removing the UTF-8 BOM and the final newline that my save step added. Cross-PR rulings: the SOLR-14718 Limits (ruling R3) pass. The SOLR-13943 stack wording fails (PR 5089, finding 1). No owner calls in this group.

## PR 5088, SOLR-13696

**Verdict: FIX FIRST**

1. Title (check 3). The PR title is "SOLR-13696: DimensionalRoutedAliasUpdateProcessorTest / RoutedAliasUpdateProcessorTest failures due commitWithin/openSearcher delays". The changelog title at head `da4fa6df1178`, `changelog/unreleased/SOLR-13696-dimensional-routed-alias.yml` line 1, is "Creating a dimensional routed alias through the V2 collections API no longer fails for missing router parameters, and time routed alias routing no longer throws a ClassCastException when a document timestamp is still a String". Corrected title: "SOLR-13696: " followed by that exact text.

2. Proof, not in the receipt (check 5). The receipt records "module check rc=0" and no tidy or Error Prone result. Current Proof bullet: "Tidy returns 0 with a clean tree. The Error Prone compile returns 0. `:solr:core:check -x test` returns 0." Corrected: "Module check: rc=0."

3. Proof, pre-fix items not in the receipt (check 5). The receipt cites gate rounds r5 (cast), r6 (future date), r7 ([shard] URLs) and the base run r3. It records no seeds and no hashes. The hashes do sit at the right points in branch history (each is the parent of the commit that makes that repair). The seeds cannot be matched.
   - Current: "Time-route fix: on pre-fix production head `98ad9d3fcc33` (seed `FB1F0CEBAAF65F30`), both Dimensional tests fail with `ClassCastException`." Corrected: "Time-route fix: the cast proof (gate r5) fails on its pre-fix tree."
   - Current: "Future-date repair: at `a4e0da422327` (seed `1676C9C3B647F0E6`), the test file from before that repair fails with the timeout shape." Corrected: "Future-date repair: the future-date proof (gate r6) fails on its pre-fix tree."
   - Current: "The `[shard]` repair: at `08f9384e47c0` (seed `9DBC31B7C732B317`), the test file from before that repair fails at the final placement check, in the final-loop assertion shape." Corrected: "The `[shard]` repair: the `[shard]` URLs proof (gate r7) fails on its pre-fix tree."
   - Limits, second bullet, current: "The create-alias fail-before is the investigation run on the untouched base `c3cdf7b46e8`, with the awaitsfix group enabled (seed `54689CC480DC14B0`). Both Dimensional tests fail at CREATEALIAS with "requires these params: [router.name, router.field]"." Corrected: move this into Proof as "Create-alias fix: the base run (gate r3) fails both Dimensional tests at CREATEALIAS." The seed and the quoted message go.

4. Choice section (formula section 4). The time-route sub-choice is a bundling question on a two-line change (`TimeRoutedAlias.java` lines 219-225). Its only alternative is a separate ticket, and no cost is named for it. Under the formula, narrow scope on a straightforward patch is not a choice. The fix is already stated in "What this change does".
   - Current heading: "**Two production fixes ride along with the test repair. Each could move to its own ticket and PR.**" Corrected: "**One production fix rides along with the test repair. It could move to its own ticket and PR.**"
   - Delete the block that starts "**The time-route cast.** Options considered:" and ends "Was keeping the time-route fix in this branch the right call?"
   - The create-alias sub-choice stays. Its alternative names a cost (the Dimensional suite stays broken) and the change alters the V2 create-alias message for every dimensional alias (`CreateAlias.java` lines 128-141).

Checks passed: head and names; body matches draft; every blob link in What this change does and Limits lands on the cited code at `da4fa6df1178` (RoutedAlias test L67, L72-L82, L360-L370; CategoryRouted L53; DimensionalRouted L282-L293, L615-L621, L644-L650; TimeRouted L75, L966; CreateAliasAPITest L308-L311); base claims hold at `c3cdf7b46e8` (2020-10-23 date, `ship_name_en`, `(Date)` cast at `TimeRoutedAlias.java` L221, `@Before` per method); `formattedRouteValues` is called only by dimensional aliases; counts 6/6, 2/2, 13/13 match the receipt; diff is 7 files plus the changelog, no stray files; changelog YAML valid; AI header and footer present; every section opens with a bold line; no em dash; no first-person plural.

## PR 5089, SOLR-13943

**Verdict: FIX FIRST**

1. Stack (cross-PR ruling, and formula section 2). What this change does, bullet 4, reads: "The change touches tests only. No production code changes." The PR diff against main (merge base `c3cdf7b46e8`) has 8 files, including `solr/core/src/java/org/apache/solr/cloud/api/collections/TimeRoutedAlias.java` and `solr/core/src/java/org/apache/solr/handler/admin/api/CreateAlias.java`. Those changes come from SOLR-13696. The stack appears only in the last Limits bullet. Checked: `1d0b8a0a73cd` is an ancestor of both heads, the 13696 head adds only the changelog after it, and this branch's own commits change two test files.
   - Insert as the first bullet of What this change does: "- Stack: this branch sits on the SOLR-13696 branch at `1d0b8a0a73cd`, which is an ancestor of this head. Until SOLR-13696 lands, this PR's diff against main also shows that PR's production and test changes (`TimeRoutedAlias.java`, `CreateAlias.java`, and the routed-alias test files)."
   - Replace bullet 4 with: "- The commits in this PR change two test files only, and no production code."

2. Proof, number not in the receipt (check 5). Current: "Before this branch was stacked on SOLR-13696, the same test failed in 3 of 5 runs. The failure recorded was an expected 3 and an actual 4 in `concurrentUpdates`". The receipt records no such run counts. Corrected: "The failure is the pre-existing timing race that the method comment describes." Keep the existing assertion link.

3. Proof, tidy and Error Prone not in the receipt (check 5). Current: "Tidy exits 0 with a clean tree. The Error Prone compile exits 0. `:solr:core:check -x test` exits 0." Corrected: "Module check: rc=0."

4. Title (check 3). Current: "SOLR-13943: TimeRoutedAliasUpdateProcessorTest.testDateMathInStart: multi-threaded race condition due to ZK assumptions". The method no longer lives in that class. It moved to `TimeRoutedAliasDateMathInStartTest.java` (line 85 at `cc155cf68e1d`). Corrected: "SOLR-13943: TimeRoutedAliasDateMathInStartTest.testDateMathInStart: multi-threaded race condition due to ZK assumptions". The changelog rule does not apply: the branch adds no fragment and the change is test-only.

Not a description finding: `TimeRoutedAliasDateMathInStartTest.java` line 133, a code comment, reads "Our own watcher". Changing it moves the head and needs a new gate, so leave it unless a re-gate is planned.

Checks passed: head `cc155cf68e1d` matches the receipt; names; body matches draft; anchors at head (class annotation `TimeRoutedAliasUpdateProcessorTest.java` L69-L70, new class L51, test L84-L160, first wait L109, comment L132-L134, poll loop L135-L159, `testPreemptiveCreation` L301-L302, method comment L678-L683, assertion L705); the `@AwaitsFix` at base L966-L967 holds; counts 6/6, 2/2, 13/13, 1/1 and the awaitsfix run (6 tests, sole failure `testPreemptiveCreation`) match the receipt; the 13696 extra commit is changelog-only; no stray files; no em dash; no first-person plural in the PR text.

## PR 5090, SOLR-14262

**Verdict: FIX FIRST**

1. Title (check 3). Current: "SOLR-14262: local commit is (silently - no rf support) ignored during replay". The changelog title at head `1e8d2b0075d7`, `changelog/unreleased/SOLR-14262-commit-ignored-header.yml` lines 1-2 (folded scalar), is "A commit that is skipped because the update log is not ACTIVE now reports commitIgnored in the response header instead of looking successful." Corrected title: "SOLR-14262: " followed by that exact text.

Cosmetic:

2. Choice section. The question "Is a `commitIgnored` header with the update log state the right way..." follows the previous paragraph with a single line break, so it renders inside that paragraph. Corrected: add a blank line before it.

3. Limits, cloud bullet. Current: "I can open a follow-up for the cloud case on request." Corrected: "A follow-up PR for the cloud case can be opened on request." This matches the other Limits.

Checks passed: head `1e8d2b0075d7` matches the receipt; names; body matches draft; What happens today (`DistributedUpdateProcessor.java` L1178-L1184) and What this change does (L1185-L1189) hold at head; the test link (`TestRecovery.java` L403-L422) holds; Proof 1/1, fail at the `commitIgnored` assertion, date 2026-10-07 and rc=0 match the receipt; changelog YAML valid, type `changed`, author Nick Shanin; diff is 3 files, no stray files; no em dash; no first-person plural. Not verified: the Limits claim that SOLR-5941 routes autocommits through this skip path. Its branch (`a4df7bfd214b`) touches `DistributedZkUpdateProcessor.java` and `CommitTracker.java`, but I did not trace the path.

## PR 5091, SOLR-14718

**Verdict: FIX FIRST**

1. Title (check 3). Current: "SOLR-14718: Multiple flaws in tracking which UpdateCommand is associated with a given failure logged by ErrorReportingConcurrentUpdateSolrClient: "cmd=add{,id=(null)}"". The changelog title at head `29c09959791a`, `changelog/unreleased/SOLR-14718-distributor-error-reports-its-document.yml` lines 1-2 (folded scalar), is "Failed distributed adds no longer log "cmd=add{,id=(null)}": the request keeps its own copy of the add command instead of the instance the request loader reuses and clears per document." Corrected title: "SOLR-14718: " followed by that exact text.

2. Citation contradicts its sentence (check 4). What happens today: "A distributed add queues its request with that same command object ([SolrCmdDistributor.java#L241-L264](https://github.com/nick-boss-tech/solr/blob/29c09959791aea2ee46f7b697e1590d19a48ef1f/solr/core/src/java/org/apache/solr/update/SolrCmdDistributor.java#L241-L264))." At head `29c09959`, lines 251 and 262 hold the copy (`final UpdateCommand reqCmd = cmd.clone();` and `new Req(reqCmd, ...)`), so the link shows the fixed code. The pre-change line is `submit(new Req(cmd, node, uReq, ...))`, line 259 of the merge base `9b3a84b1c460`. Corrected link: replace `29c09959791aea2ee46f7b697e1590d19a48ef1f` with `9b3a84b1c460981eab09d8ffaef776acc4a184f8` in that URL. The anchor L241-L264 stays and the sentence stays.

3. Limits citations not at the PR head (formula section 3). The SOLR-5939 links in the second and third Limits bullets (`StreamingSolrClients.java` L155-L169, `ConcurrentUpdateJettySolrClient.java` L92, `ConcurrentUpdateJdkSolrClient.java` L40, `ConcurrentUpdateBaseSolrClient.java` L301-L305 and L379-L380) use the SOLR-5939 head `f8d4bdbea518901e255ae119f3e5c43e7804a9bf` (origin `solr-5939-submit`), because that code is not on this branch. The anchors are correct at that SHA. Corrected: add this sentence to the end of the first Limits bullet: "The SOLR-5939 links in the second and third bullets point at the SOLR-5939 head, `f8d4bdbea518`, because that code is not on this branch."

4. Ruling R3 (SOLR-14718 Limits against SOLR-5939). Passes. The Limits contains neither "the reported document is the first one sent to that node" nor "Retries are not per document". It describes the post-5939 mechanism: "each request in a failed merged stream is named against its own document". The phrase "names only the first request in that stream" covers a residual case (a stream that reports no members). It does not describe the old mechanism as unchanged, so it is not flagged.

5. Proof wording (check 5; the counts are right). Current: "Test class `SolrCmdDistributorTest`, new test [`testFailedAddKeepsItsDocumentWhenTheCommandIsReused`]". The method is a private helper called from the existing `@Test` method `test()` (`SolrCmdDistributorTest.java` line 357). JUnit still counts 1/1. Corrected: "Test class `SolrCmdDistributorTest`, method `test()`, which now calls a new check, [`testFailedAddKeepsItsDocumentWhenTheCommandIsReused`] (L547-L575)."

Cosmetic:

6. Proof says "the gate's premise step". Corrected: "the first check in the gate".

Checks passed: head `29c09959791a` matches the receipt; names; body matches draft; What this change does anchors (`SolrCmdDistributor.java` L249-L251, L252-L256, L262); What happens today anchors (`JavabinLoader.java` L105-L123, `StreamingSolrClients.java` L138-L146, `Req.toString` L420-L424, the L241-L264 link aside); retry anchors (L141, L383); test lines L547-L575; Proof 1/1, fail on base, date 2026-10-07 and rc=0 match the receipt; changelog YAML valid; diff is 3 files, no stray files; no em dash; no first-person plural.
