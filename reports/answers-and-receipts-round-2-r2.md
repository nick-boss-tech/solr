# r2: SOLR-12849 (live PR #5011) consistency check

Result: The live head (6b92223bc24f, PR OPEN, mergeStateStatus CLEAN) and the Proof counts (2 of 2, 4 of 4) match the refreshed receipt and the branch. The drift is one FIX: the PR body names a GitHub Actions venue for the base run that the receipt does not record and that cannot be confirmed from disk. Other items are NOTEs on dates, task names, and a missing verdict file.

## Findings

1. FIX. The base-run venue is not recorded in the receipt.
   - Where: live PR #5011 body, Proof, first bullet, "That base comparison ran on the fork's GitHub Actions test runner." Receipt `receipts/SOLR-12849.md` line 7, "Proof (logs g12849r29-premise.log and g12849r29-premise2.log)".
   - Evidence: the receipt names no runner, run ID, or URL. Neither premise log is on disk. A search for g12849r29, premise, and 12849 under research/, env/, tools/, worktrees/, fresh-solr-validation/, and wt/ found no logs. wt/SOLR-12849 is at ed73600d877 and holds no JUnit XML or gate log.
   - Replacement, receipt line 7, after the log names: "(run venue: <GitHub Actions run URL and ID, or local; main side to fill in>)". Until the main side confirms the Actions run, treat the PR body's venue sentence as unverified.

2. NOTE. The dates depend on a time zone that neither document states.
   - Where: receipt line 8, "gate finished 2026-10-06". PR body, Proof, "re-verified at head 6b92223bc24 on 2026-10-06" and "The second case, added on 2026-10-06".
   - Evidence: git records commit 6b92223bc24 with author and committer time 2026-10-07T04:20:44Z (UTC). The 2026-10-06 dates agree only when read as local time west of UTC (UTC-5 gives 2026-10-06 23:20).
   - Replacement, receipt line 8: "gate finished 2026-10-06 (local time; the commit is recorded at 2026-10-07 04:20 UTC)". For the PR body, no change if the local date is meant; if UTC is meant, the fragment "on 2026-10-06" should read "on 2026-10-07 (UTC)". No PR text is drafted here.

3. NOTE. The receipt does not name the check tasks the PR body names.
   - Where: receipt line 5, "tidy clean; Error Prone compile passes; module check passes." PR body, Proof, first sentence, which names `:solr:core:check -x test`.
   - Evidence: AGENTS.md says `check -x test` skips test tasks and is not behavioral proof, so the focused test counts carry the proof. The receipt does not record which tidy task ran.
   - Replacement, receipt line 5: "module check (`:solr:core:check -x test`) passes", and name the tidy task that ran (main side to fill in).

4. NOTE. The second test method also fails on base, which the receipt does not say.
   - Where: receipt line 7 cites only the first message. Test file `solr/core/src/test/org/apache/solr/servlet/HttpSolrCallCollectionParamTest.java` lines 57 to 68.
   - Evidence: by reading only. Base `addCollectionParamIfNeeded` (upstream/main, `solr/core/src/java/org/apache/solr/servlet/HttpSolrCall.java` lines 783 to 805) reads only URL params and replaces the value with the joined path list. With the body value "nosuchcoll", base returns "emptycollection,doccollection", so the second method should fail with expected:<[nosuchcoll]> but was:<[emptycollection,doccollection]>. No log confirms this.
   - Replacement, receipt line 7, append: "Both methods fail on base; the second with expected:<[nosuchcoll]>." Add only after the premise log confirms it.

5. NOTE. No verify-fail-before verdict is recorded.
   - Where: receipt line 7.
   - Evidence: AGENTS.md (Test Runs) requires a fail-before PASS verdict for tickets that change tests, stored as `research/test-queue/results/<ISSUE>.failbefore.json`. No SOLR-12849 result file is under research/test-queue/. The premise logs are the only base evidence the receipt cites.
   - Replacement, receipt line 7, append: "No verify-fail-before verdict is recorded; the premise logs are the base evidence." If the main side has a PASS verdict file, cite it instead.

## Task results

- Live head: `gh pr view 5011` returns headRefOid 6b92223bc24f4ef5a6726ed986a240c6eacedf73. It matches `origin/solr-12849-submit` (6b92223bc24), receipt line 4, and the claim table. Match.
- State: OPEN. mergeStateStatus CLEAN. Upstream checks were not read (see Not checked).
- Title: "SOLR-12849: keep a POST body's collection parameter when the path names multiple collections". It matches the changelog title minus the prefix. Match.
- Proof counts: PR body "HttpSolrCallCollectionParamTest: 2 of 2" and "AliasPostBodyTest: 4 of 4"; receipt line 6 gives the same. The branch has 2 `@Test` methods in HttpSolrCallCollectionParamTest.java (lines 47, 57) and 4 in AliasPostBodyTest.java (lines 77, 82, 87, 92). Match.
- Discriminating assertion: HttpSolrCallCollectionParamTest.java line 54, `assertEquals("bodycoll", call.solrReq.getParams().get(COLLECTION_PROP));`. Base reads only URL params and replaces the body value. The premise message `expected:<[bodycoll]> but was:<[emptycollection,doccollection]>` is the JUnit form of that replacement. Consistent by reading. The branch reads the body through `getQueryParams()` (HttpSolrCall.java lines 212 to 214, used at line 792).
- AliasPostBodyTest: class Javadoc (lines 29 to 38) labels all four cases as end-behavior pins and names HttpSolrCallCollectionParamTest as the failing case. This matches receipt line 7 and the PR body. The PR body's mechanism (SolrJ moves `collection` into the URL) is supported by `DEFAULT_URL_PARAM_NAMES` in `solr/solrj/src/java/org/apache/solr/client/solrj/impl/HttpSolrClient.java` (line 81 on the branch).
- Changelog: `changelog/unreleased/SOLR-12849.yml`, new, 7 lines: title, `type: fixed`, author Nick Shanin, link to SOLR-12849. Valid by reading. It is not parsed, since this machine has no Python. `fixed` appears in 58 existing fragments on upstream/main. The author is the ICLA name. The PR body names the same path. Match.
- Branch scope: 4 files against upstream/main (the changelog, HttpSolrCall.java, and the two tests). No stray files.
- Call sites: HttpSolrCall.java line 340 and `solr/core/src/java/org/apache/solr/api/V2HttpCall.java` line 198. The PR body's Limits statement that the v2 path calls the same method is consistent.
- Round-1 gap: `reports/core-admin-round-1-k2.md` Finding 2 said the PR Proof counts were not in the receipt. The refreshed receipt now records them, and they match.

## Not checked

- Gate logs (g12849r29-gate-dead1.log, g12849r29-gate2.log, g12849r29-premise.log, g12849r29-premise2.log) and the round-29 takeover log are not on disk. The gate-green claim, the counts, and the premise messages are not verified against logs.
- JUnit XML is not on disk, so "from fresh JUnit XML" is unverified. HttpSolrCallCollectionParamTest calls `assumeWorkingMockito()`, so a skip would not show as a pass. Check the XML for skipped cases.
- No builds, Gradle, or tests were run, per the rules. Tidy, Spotless, Error Prone, and compile were not re-checked.
- Upstream checks: `gh pr checks` is outside this task's read set. CLEAN is the merge state only.
- The base-run venue (GitHub Actions) cannot be confirmed from disk. `gh run list` is outside this task's read set.
- PR body sections other than Proof (What happens today, What this change does, Limits) were not audited. The formula's presentation rules (bold section openers, citation links at the head SHA) were not checked. No PR text was drafted.
- The changelog YAML was not parsed by a tool.
- No edits, no gh write calls, and nothing posted.
