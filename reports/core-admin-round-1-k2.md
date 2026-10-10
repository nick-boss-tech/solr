# Core admin round 1, part k2: request path and security

Result: SOLR-17708 is draftable, with its draft written and one changelog title fix needed first. SOLR-18010 is held with no draft. SOLR-12849 (PR #5011) and SOLR-13097 (PR #5016) are consistency passes only: both live heads match their branches, but neither live tip has a gate record, and PR #5016 is open with changes requested and conflicts with main.

Heads checked: PR #5011 live head `6b92223bc24` and PR #5016 live head `f0e7395f58f` (read-only `gh pr view`). The 17708 and 18010 heads are the local `origin/*` refs `10a7fa07a79` and `c3685bb37d9`, which match the claim table. Gated heads are as the receipts state, and each one exists in the repo. Trial merges used the local `upstream/main` ref `8e62c268688`, with no fetch.

## Findings

1. FIX. PR #5011 (SOLR-12849): the live tip has no gate record, and it holds the only discriminating test.
   - Where: live head `6b92223bc24`. Receipt `receipts/SOLR-12849.md`, lines 3 and 4, records a gate at `ed73600d877` only (AliasPostBodyTest 3 of 3, module check).
   - Evidence: `ed73600d877` is not an ancestor of `6b92223bc24`, so the gated commits were rewritten. Gated to tip, the diff adds `HttpSolrCallCollectionParamTest.java` (90 lines, 2 tests), adds 20 lines and a fourth test to `AliasPostBodyTest.java`, adds 2 comment lines at `HttpSolrCall.java` line 129, and changes the changelog title. The test comment in `AliasPostBodyTest.java` says the end-to-end cases pass on base and the method-level test is the one that fails on base. No receipt records a run of the new class.
   - Replacement: no PR text. Receipt owed: "Live tip `6b92223bc24`: gate owed. Focused classes HttpSolrCallCollectionParamTest 2 tests and AliasPostBodyTest 4 tests, tidy, Error Prone compile, module check."

2. FIX. PR #5011 body, Proof section: the counts are not in the receipt.
   - Where: PR #5011 body, "## Proof".
   - Evidence: the body says tidy, Error Prone, the focused tests, and `:solr:core:check -x test` pass at `cd7bd799c91` (2026-10-04) and at `6b92223bc24` (2026-10-06). It also cites a base-code failure run on the fork's GitHub Actions runner. The receipt records only AliasPostBodyTest 3 of 3 at `ed73600d877`. Proof numbers come only from the receipt.
   - Replacement: hold the Proof section until the main side records a gate at `6b92223bc24`. Then cite that receipt's head and counts. Description edits are Nick's call. Check with him first if a maintainer is commenting.

3. FIX. Record for PR #5016 (SOLR-13097): "closed out" does not match GitHub.
   - Where: `receipts/SOLR-13097.md`, line 3 ("The PR was closed out on 2026-10-06").
   - Evidence: `gh pr view 5016` gives state OPEN, reviewDecision CHANGES_REQUESTED (janhoy, 2026-10-06 00:05 UTC), mergeable CONFLICTING, mergeStateStatus DIRTY, updatedAt 2026-10-07 12:41 UTC. A later janhoy COMMENTED review is dated 2026-10-06 21:58 UTC.
   - Replacement: "Open, not closed. Review decision CHANGES_REQUESTED (janhoy, 2026-10-06). Merge state CONFLICTING with main."

4. FIX. PR #5016 conflicts with main in the upgrade notes.
   - Where: `solr/solr-ref-guide/modules/upgrade-notes/pages/major-changes-in-solr-10.adoc`, branch tip `f0e7395f58f`.
   - Evidence: main already has `== Solr 10.2` at line 37, with a "Snapshot commands" subsection. The branch adds a second `== Solr 10.2` heading with `=== Security` above `== Solr 10.1`. A three-way `git merge-file` (main, base `c3e18f1e455`, tip) gives one conflict block at that heading. GitHub also reports CONFLICTING and DIRTY.
   - Replacement: delete the inserted `== Solr 10.2` heading line. Place the `=== Security` block, unchanged, after the paragraph "The default command line interface is not changed." and before `== Solr 10.1`. Repeat the trial merge after main is fetched.

5. FIX. PR #5016 tip has no gate record, and the receipt's fix commit is not in the live history.
   - Where: `receipts/SOLR-13097.md`, lines 3 and 4 (gated `38e306c2cd5`, fix `8cc61e00e60`); tip `f0e7395f58f`.
   - Evidence: neither `38e306c2cd5` nor `8cc61e00e60` is an ancestor of the tip. The same subjects appear under new SHAs, for example `e869956cdf4` for the `String.formatted` fix. The live test file has no `formatted(` call. `CoreScopedAuthStandaloneTest.java` has 5 test methods at the tip and 3 at `8cc61e00e60`. The two added tests are `testV2RequestUsesCoreScopedRules` and `testWildcardRuleAppliesOnlyWhereNoScopedRuleGoverns`. The PR body's "5 of 5 ... verified at head f0e7395f58f" and its base failure counts ("3 of 5 methods", "4 failing test executions") are not in any receipt.
   - Replacement (record): "Gate owed at `f0e7395f58f`: CoreScopedAuthStandaloneTest, 5 tests. Receipt fix SHA `8cc61e00e60` is not in the live history; the same fix is `e869956cdf4`."

6. FIX. PR #5016 body uses internal process wording.
   - Where: PR #5016 body, "## Proof", first bullet, "Added in this round".
   - Replacement: "Two of these cases were added later".

7. FIX. SOLR-17708 changelog title overstates the change.
   - Where: `changelog/unreleased/SOLR-17708-jaxrs-single-authorization.yml`, lines 1 and 2 (the folded title) at `10a7fa07a79`.
   - Evidence: the title says JAX-RS v2 APIs "are now authorized once per request". The admin-remote route still checks twice: `V2HttpCall.java` line 166 sets `ADMIN_OR_REMOTEPROXY`, lines 383 to 400 run the Jersey app after `HttpSolrCall.java` lines 480 to 482 have already authorized.
   - Replacement: `title: JAX-RS v2 APIs are authorized once on the local request path instead of twice`. Keep `type`, `authors` and `links` unchanged. Parse the YAML again at the new head on the main side.

8. NOTE. SOLR-17708 structural exception: the record is not on disk.
   - Where: `receipts/SOLR-17708.md`, the Proof line names "the round 35 review's other finding (a deliberate structural exception for one API family)".
   - Evidence: the round 35 report is not on disk. I searched `research/`, `env/`, `material/` and the worktree. The nearest record is `research/branch-reviews/round-28/SOLR-17708-review.md`, finding 1. It names the same route, `ADMIN_OR_REMOTEPROXY`, as a duplicate check that remains. Finding 2 (denial test) is covered by `testJaxRsApiDenialStillHolds`. Finding 3 (handoff file) is removed in `10a7fa07a79`.
   - Replacement: none. The draft's Limits names the remote route. If round 35 disposed of a different finding, that Limits line must change.

9. NOTE. SOLR-17708 behavior change for unmatched v2 paths.
   - Where: `solr/core/src/java/org/apache/solr/api/V2HttpCall.java` lines 228 to 232.
   - Evidence: with no `Api` and action ADMIN or PROCESS, the first check is skipped. The JAX-RS filter runs only after a resource matches (`JerseyApplications.java` line 42 registers it in `CoreContainerApp`, which `SolrCoreApp` extends). An unmatched path therefore gets Jersey's not-found response, with no plugin call.
   - Replacement: none needed. The draft's "What this change does" states it.

10. NOTE. Landing order for the request-path cluster (12849, 13097, 17708).
    - Textual overlap: none. `HttpSolrCall.java` hunks are 12849 at lines 129 to 130 and 785 onward (`addCollectionParamIfNeeded`), 13097 at lines 219 to 237 (`getAuthorizationCollectionsList`), and 17708 at line 608 (visibility only). Pairwise trial merges of the tips are clean.
    - Semantic overlap: 13097 changes the list that both the first check and the JAX-RS filter use. `V2HttpCall` passes `getAuthorizationCollectionsList()` as `COLLECTION_LIST`. After 17708, the JAX-RS filter is the only check for JAX-RS requests, so 13097's core-name list reaches those requests through the filter.
    - Order: any order merges. Re-run 13097's v2 case, `testV2RequestUsesCoreScopedRules`, on the combined tip. 12849 is independent of the other two.

11. NOTE. SOLR-18010 is not ready: the gated head and the live tip differ in substance.
    - Where: receipt `receipts/SOLR-18010.md`, lines 3 and 4, gate at `fadc5ee31e3` (BasicAuthStandaloneTest 1 of 1). Live tip `c3685bb37d9` adds one commit.
    - Evidence: `fadc5ee31e3` only strips the `"":{"v":0}` marker. The round 12 review (`research/branch-reviews/round-12/SOLR-18010-review.md`) says the corruption stays in place at that head. The tip adds the edit lock (`SecurityConfHandlerLocal.java` lines 50 and 58 to 64) and a temp file with atomic move (lines 122 to 130). It also adds 257 lines to `SecurityConfHandlerTest.java`, including `testConcurrentEditsToLocalSecurityJson` and `testConcurrentPersistConfLeavesOneWholeDocument`. None of that is gated. The settling run named in the receipt is not on disk.
    - Replacement: none. HOLD. Main side: gate `c3685bb37d9`, record the receipt, and read the settling run before any draft.

12. FIX. SOLR-18010 changelog title claims more than is settled.
    - Where: `changelog/unreleased/SOLR-18010-security-json-concurrent-edits.yml`, line 1.
    - Evidence: the title says concurrent edits "can no longer corrupt the file or overwrite each other". The lock is per standalone handler instance. Cloud mode uses `SecurityConfHandlerZk` (`CoreContainer.java` line 851), which the change does not touch. The concurrent-edit claim is not settled while the settling run is missing.
    - Replacement (hold until the settling run is read): `title: Standalone security.json edits are serialized and written atomically`.

13. NOTE. PR #5016 Limits states a design position that is not verified here.
    - Where: PR #5016 body, "## Limits", second bullet (shards and `blockUnknown=false`).
    - Evidence: the body says a target core with no governing permission, and no wildcard permission, "can return its documents" to a credential-free sub-request when `blockUnknown=false`. It calls that the plugin's designed behavior and "not a gap this PR opens". The probe is not on disk, and I did not check the plugin code for this path.
    - Replacement: none here. Owner decision, below.

14. NOTE. Heads, PR state, and changelog authors for the other tickets.
    - No apache/solr PR has head `solr-17708-submit` or `solr-18010-submit` (`gh pr list --head`, empty). This matches the inventory "gated, no PR".
    - PR #5011 is OPEN, `headRefOid` `6b92223bc24`, matches the claim table and the branch. Its title matches the changelog title.
    - The changelogs for 12849, 13097, 17708 and 18010 all use author "Nick Shanin". No placeholder author.

## Task results

**SOLR-12849 (PR #5011): consistency pass, not ready on the record.** The live head `6b92223bc24` matches the branch and the claim table, and the PR is open with a clean trial merge. The only gate is at the older head `ed73600d877`, which is not in the live history. The discriminating test class has never been gated, and the PR Proof cites counts that are not in the receipt (Findings 1 and 2). Gate owed at the live tip. No draft, per the assignment.

**SOLR-13097 (PR #5016): consistency pass, not consistent with the record.** The live head `f0e7395f58f` matches the branch. The record says "closed out", but GitHub shows an open PR with changes requested and a merge conflict with main (Finding 3). The conflict is in the upgrade notes (Finding 4). The gate covers 3 of the 5 test methods at the older fix commit, and the two added tests have no gate (Finding 5). Rebase and gate are owed. No draft, per the assignment.

**SOLR-17708: draftable.** The gate is green at the live tip `10a7fa07a79`, which matches `origin/solr-17708-submit` and the receipt. No PR exists. The draft is at `pr-drafts/core-admin/SOLR-17708.md`, and its Proof names head `10a7fa07a79`. The draft carries the remote-route exception in Limits, as the assignment asks, and states the change for unmatched v2 paths. Ready to open after the changelog title fix (Finding 7). No "A choice to check" section: the ticket's suggested route is the one implemented, and the draft has no live alternative a maintainer would plausibly pick. The round 28 review finding on the denial test is addressed at this head.

**SOLR-18010: held, no draft.** The gated head `fadc5ee31e3` only strips the internal marker. The live tip `c3685bb37d9` adds the lock and atomic write, plus a 257-line test change, and none of it is gated (Finding 11). The settling run that the receipt says settles the concurrent-edit claim is in a takeover record that is not on disk. The concurrent-edit claim cannot be settled here, so the assignment says to hold. The 18010 files do not overlap with 12849, 13097 or 17708.

## Owner decisions

1. SOLR-17708: confirm that the round 35 "structural exception" is the admin-remote route (`ADMIN_OR_REMOTEPROXY`). If so, ship with the Limits line and a follow-up offer. If not, change the Limits before opening.
2. SOLR-17708: accept that a v2 path matching no JAX-RS resource no longer gets the plugin's answer and gets Jersey's not-found response. The draft states it.
3. SOLR-18010: confirm the lock and atomic write at the live tip as the submitted fix, in place of the marker-only head. Main side gates the tip and reads the settling run before any draft.
4. SOLR-13097: put the upgrade note in the 10.2 section, for a branch_10x backport as the PR body says, or in the main-only 11 page. The rebase follows from that choice.
5. SOLR-13097: confirm the Limits statement on `blockUnknown=false` sub-requests ("designed", "not a gap this PR opens") before the body stays as written.
6. SOLR-13097: the PR is open with changes requested. Decide whether it stays open for janhoy's re-review, and correct the "closed out" record.
7. SOLR-12849 and SOLR-13097: main-side gates at the live tips are owed. Gate runs are Gradle, so they need your explicit verify approval.
8. Landing order: any order works for the request-path trio. Re-run 13097's v2 case on the combined tip.

## Not checked

- No builds, Gradle, or test runs (rule). Compilation of the new 12849 test class, the 13097 added tests, the 18010 test additions, and the 17708 test was not checked. The gate receipts for 17708 are the only compile evidence.
- The 17708 test date "2026-10-08" comes from the takeover-log entry named in the receipt, which is not on disk. Confirm the run date from the log before publishing the draft.
- The round 35 report and the takeover log are not on disk. I searched `research/`, `env/`, `material/` and the worktree. The 17708 disposition and the 18010 settling run are unchecked.
- JIRA: this session has no Apache JIRA access. 17708 and 18010 were read from the hydrated files in `research/jira-context/` (dated 2026-10-04 and 2026-10-03). No hydrated JIRA text is on disk for 12849 or 13097.
- The PR #5011 and PR #5016 body claims (base failure runs, GitHub Actions runs, the shards probe, the failing execution counts) were not verified. The probe is not on disk.
- Three-way trial merge of 12849, 13097 and 17708 was not run. Only pairwise merges were run.
- Trial merges use the local `upstream/main` ref `8e62c268688`, with no fetch. Main is 76 commits past the merge base for 12849 and 13097, 49 for 17708, and 66 for 18010, so conflicts may differ on current main.
- Whether the v2 path `/cores/{core}/select` resolves to a v2 API object or a JAX-RS resource at the 17708 and 13097 tips was not checked. It decides which check covers that path after 17708 lands.
- The 17708 remote-route identification comes from the code and the round 28 review, not from the round 35 report.
- The 13097 docs claim about `shards` (only the receiving core is checked) was not checked against code.
- No draft here names a Lucene version, so the Lucene version rule did not apply.
