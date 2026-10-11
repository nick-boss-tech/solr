# Review confidence round 6: confirmation of the bodies fixed from rounds 4 and 5

Counts: **19 live bodies. 4 SATISFIED, 15 STILL OPEN.** Slice A (10 bodies): 1 SATISFIED, 9 STILL OPEN. Slice B (9 bodies): 3 SATISFIED, 6 STILL OPEN.

Read-only. Live bodies were read through the GitHub API and the fork tips with `git ls-remote` on 2026-10-11. No body was edited, no comment or review posted, no branch or draft edited, no build, test or gate run. The main side applies every replacement below.

## Head check

For all 19 bodies, the live API head equals the fork tip and the receipt head, or the receipt records a changelog-only or docs-only delta (SOLR-18530: gate `98e5368`, tip `14868bc`; SOLR-18532: gate `f1e5031`, tip `7dfd3d98`). No head mismatch. Merge-bases match the bases the bodies cite.

## Two record-wide issues for the main side to decide first

**C1. The date convention.** Several bodies date a head "2026-10-06", but the commit times are 2026-10-07 UTC, which is 2026-10-06 in MDT (UTC-6). The author offset on `3000eeede7a` is -06:00, so MDT is the author's zone. Affected bodies: SOLR-18178 (`b75e7d4d3c4`), SOLR-18129 (`657e443d866`), SOLR-16130 (`3c48dec4b79`), SOLR-12849 (`6b92223bc24`), SOLR-13568 (`ac5d60c214c`), SOLR-13706 (`590dd5c24d9`), and SOLR-18530 (`e28739b4069`, carried). The fix is either to record MDT as the convention, or to append " MDT" to each listed verification date. Each open item below that says C1 waits on this decision.

**C2. Fork runs cited "at this head" ran on CI commits.** Three fork runs cited as evidence of a body's head ran on CI commits, not the body's head. The differences are workflow-file-only, except where noted:
- Run `37569145964` (SOLR-18129, TestUtils): CI commit `19f7433f7a7`, which adds only `.github/workflows/fork-test-runner.yml`.
- Run `37581056636` (SOLR-16130): CI commit `4c151ce1b79`, workflow file only.
- Run `37568486924` (SOLR-13706): CI commit `8cb51cd86c5`, workflow file only.
- Runs `37225864392` and `37225860272` (SOLR-18129, "patched tree"): CI commit `61527cf7f76` (2026-10-04). This is an earlier state, not the head. It lacks 31 TestUtils lines, 16 TestSolrConfigHandler lines and 10 CommandOperation lines.

Each body item below that cites these runs says so.

## Slice A

### SOLR-18178, PR 4968: STILL OPEN

Head `b75e7d4d3c464ceeb9f14fdaf7711212adcccac2`, open, not draft. Receipt counts match.

- 1a. Process narration (formula section 3). Live: "- One earlier failure in this class was a pre-existing defect in the drive-letter test itself ...". The catch changed no shipped behaviour. **Replacement:** delete the bullet.
- 1b. C1. Live: "Verified at head b75e7d4d3c4 on 2026-10-06". **Replacement:** pending C1.

### SOLR-18391, PR 5027 (open PR): STILL OPEN

Head `adcda10b501cbea9f92119bae8e91187da77d9da`, open, not draft. Landed items and counts match the receipt.

- 2a. Record gap, unchanged from round 4. Live (Proof, paragraph 1): "Verified at the head adcda10b501:". The receipt dates only the tip check (2026-10-10) and the proof decision (2026-10-10), not the focused gate run. Formula section 3 requires a verification date. **Replacement:** "Verified at the head adcda10b501 on <DATE>:", with the date read from the focused-run gate log (vm2 `18391-check.log`, not in this worktree). If the date cannot be recovered, drop the date and record the formula gap.

### SOLR-18391, PR 4997 (superseded draft): SATISFIED

Fidelity only. The superseded line is correct, the production-change sentence matches the receipt, and the changelog link is at `3000eeede7a`. Verification 2026-10-04 matches receipt line 17. This verdict does not recommend reopening the draft as the live submission.

### SOLR-18129, PR 5000: STILL OPEN

Head `657e443d866d5bdfd7b3d7f3448e074a2ae75714`, open, ready. Landed items match.

- 4a. Live bold Proof line: "**Both test classes pass at the head, and the same cases fail on the base code and against the previous head in the expected ways.**" The receipt records no case-level base results and no run at `63b16895b5a`. The previous head is `f5715964a09`. **Replacement:** "**Both test classes pass at the head, and on base production code the focused tests fail at the test step.**"
- 4b. Live: "On the pre-fix proof tree the focused tests fail at the test step, and the patched tree passed in GitHub runs 37225864392 and 37225860272." This cites the earlier CI state (C2). **Replacement:** "On base production code the focused tests fail at the test step. An earlier state of this branch (CI commit `61527cf7f76`, 2026-10-04, before the list-form commit) passed in GitHub runs 37225864392 and 37225860272."
- 4c. Live: "TestUtils also passed on the fork's GitHub Actions runner at this head (run 37569145964, 2026-10-06)." This cites a workflow-only CI commit (C2). **Replacement:** "TestUtils also passed on the fork's GitHub Actions runner on this code (run 37569145964, CI commit `19f7433f7a7`, 2026-10-06; that commit adds only the workflow file)."
- 4d. Process vocabulary and plain language (formula). Live: "The base-code comparisons ran the same test classes against a tree with base production code and this PR's test files." and "pins last-value-wins for other commands in list form." **Replacements:** "The same test classes were also run against base production code with this PR's test files." and "checks that other commands keep last-value-wins in list form."
- 4e. C1. Live: "Verified at head 657e443d866 on 2026-10-06". **Replacement:** pending C1.

### SOLR-16130, PR 5004: STILL OPEN

Head `3c48dec4b79faed625c4fb79833d0975a55f3ed7`, open, ready. The fail-before sentence matches receipt line 7; counts 10 of 10 and base `70c1a28995d` match. The tree supports the `sortDocSet` mechanism; the routing condition at `SolrIndexSearcher` lines 1679 to 1684 was not read.

- 5a. Live: "The class also passed in a GitHub Actions run at this head." This cites a workflow-only CI commit (C2). **Replacement:** "The class also passed in a GitHub Actions run on this code (run 37581056636, CI commit `4c151ce1b79`, which adds only the workflow file)."
- 5b. C1. Live: "Verified at head 3c48dec4b79 on 2026-10-06". **Replacement:** pending C1.

### SOLR-18482, PR 5009: STILL OPEN

Head `49ca9099d8ebc2b43fb4482791fef736cc036d7a`, open, ready. Counts and the 2026-10-10 base leg (TestJsonFacetErrors 5 tests, 1 failure) match.

- 6a. Record conflict, unresolved from round 4. Live: "Verified at head 49ca9099d8e on 2026-10-06 (confirmation run)." Receipt line 4 says the head "is docs only; no Gradle gate was run for it." Receipt line 5 says the 2026-10-06 confirmation run was "at this head". The head is dated 2026-10-05T23:47:47Z, so the run may have used either SHA. **Replacement, after the main side decides:** if the 10-06 run used `49ca9099d8e`, "Verified at head 49ca9099d8e on 2026-10-06." (drop "(confirmation run)"). If it used `f09608f5446`, "Verified on 2026-10-06 at f09608f5446, the parent of the docs-only head 49ca9099d8e; TestJsonFacetErrors was also run at 49ca9099d8e on 2026-10-10."
- 6b. Presentation rule. The first sentence of "What happens today" repeats the bold summary. Live: "JSON range facets silently ignore terms-facet parameters that have no meaning for them. A request can carry ...". **Replacement:** delete that first sentence, so the paragraph opens "A request can carry `limit`, ...".

### SOLR-12849, PR 5011: STILL OPEN

Head `6b92223bc24f4ef5a6726ed986a240c6eacedf73`, open, ready. The summaries and the three plain-language replacements landed. `addCollectionParamIfNeeded` (`HttpSolrCall.java` line 785 onward) supports the body-only and unknown-name Limits claims.

- 7a. Live: "That base comparison ran on the fork's GitHub Actions test runner, against a tree with base production code and this PR's test files." The receipt names premise logs and no venue. **Replacement:** "That base comparison ran against a tree with base production code and this PR's test files."
- 7b. Live: "(local runs on 2026-10-04 and again on 2026-10-06: 4 tests, 0 failures, in a tree with base production code and this PR's `AliasPostBodyTest` only)". Receipt line 7 gives 4 of 4 with no run dates. **Replacement:** "(4 tests, 0 failures, in a tree with base production code and this PR's `AliasPostBodyTest` only)".
- 7c. Live: "Verified at head cd7bd799c91 on 2026-10-04 and re-verified at head 6b92223bc24 on 2026-10-06:". `cd7bd799c91` is absent from the receipt, which names only `6b92223bc24` (round 29 gate finished 2026-10-06; tip re-verified 2026-10-10). **Replacement:** "Verified at head 6b92223bc24 on 2026-10-06:".
- 7d. C1. The commit is 2026-10-07T04:20:44Z, which is 22:20 MDT on 2026-10-06. **Replacement:** pending C1.

### SOLR-13568, PR 5014: STILL OPEN

Head `ac5d60c214cf8a1c6a24c79d89d9d22f44e9d721`, open, ready. Counts (9 of 9; base 9 tests, 1 failure, `expected:<3> but was:<5>`) and the gate line match.

- 8a. C1. Live: "Verified at head ac5d60c214c on 2026-10-06 (tidy clean, ...)". The commit is 2026-10-07T03:54:32Z, which is 21:54 MDT on 2026-10-06. Receipt line 4 also dates the head 2026-10-06. **Replacement:** pending C1.
- Observation, not counted: "the cached entries are rarely hit" has no hit-rate record. Consider "this change does not measure how often those entries are hit."

### SOLR-13706, PR 5015: STILL OPEN

Head `590dd5c24d97c5c1b8f2ed262037c0a9c4ba6f77`, open, ready. The summary replacement and changelog link landed. Counts (PluginInfoTest 11 of 11, TestConfigHighlightOutput 1 of 1, TestSolrConfigHandler 8 of 8) match.

- 9a. Live summary: "**The affected suites pass at this head. On base code the new tests fail: two with the NullPointerException from the null key, and one on a key-name assertion.**" The bullets also give "(8 failing test executions under randomization)" and "(4 failing test executions)", and failure causes. Receipt line 7 records only "the gate's pre-fix proof step passed at this head". No counts or causes are on record. **Replacements:** summary "**The affected suites pass at this head, and the new tests fail on base code.**"; bullet 1 "* `PluginInfoTest`: 11 of 11 pass with this change. On base code the new cases fail."; bullet 2 "* `TestConfigHighlightOutput`: 1 of 1 passes with this change. On base code it fails." The counts and causes may be restored only if the main side confirms them from the premise log.
- 9b. Live: "PluginInfoTest also passed on the fork's GitHub Actions runner at this head." This cites a workflow-only CI commit (C2). **Replacement:** "PluginInfoTest also passed on the fork's GitHub Actions runner on this code (run 37568486924, CI commit `8cb51cd86c5`, which adds only the workflow file)."
- 9c. C1. The commit is 2026-10-07T03:25:43Z, which is 21:25 MDT on 2026-10-06. **Replacement:** pending C1.
- Not verified: the Limits claim "no code reads the serialized children by name" (Admin UI, SolrJ, core tests). The receipt is silent.

### SOLR-13097, PR 5016: STILL OPEN

Fidelity only; no readiness verdict. Open, CONFLICTING, `mergeStateStatus` DIRTY, `reviewDecision` CHANGES_REQUESTED, head `f0e7395f58f3c8d8c9066cd88baa6931d8b6f425`. The changelog link, upgrade-note link and the "three of the five methods fail" sentence landed. `getAuthorizationCollectionsList` (`HttpSolrCall.java`) returns `getCollectionsList()` when the core is null, so the "no serving core, unchanged" Limits claim holds.

- 10a. Live Limits: "Such sub-requests send no user credentials under BasicAuth with default settings; under JWT authentication the caller's token is forwarded, so the receiving core authenticates and authorizes the actual user." Receipt line 10 records three `blockUnknown` settings and names no authentication scheme. No BasicAuth or JWT run is recorded. **Replacement:** "Whether such sub-requests carry the caller's credentials depends on the authentication plugin in use." Keep the scheme-specific sentence only if the probe artifacts show which scheme each configuration used.
- Not verified: "the first listed name that has a governing permission" (a code claim in the same paragraph).

## Slice B

### SOLR-18505, PR 5028: STILL OPEN

Head `e28739b`, open, not draft. Round 4 replacements landed. The battery counts, fork run `37577283523` at `e28739b` and the `8ca33200e37` to `1c5a948d3e4` change (one test file) match.

- 1. Live: "This failure also showed up in a test run on an unrelated PR, which prompted the investigation on SOLR-18505." The receipt records only the focused run at `e28739b`, the root-cause battery on unmodified main, and fork run `37577283523`. No run on another PR is recorded. **Replacement:** delete this sentence.
- 2. Live: "...the class failed 6 of 6 forced re-executions on unmodified main (on both sides of the PR #5001 merge), five times at the [line 494](...) assertion of the base commit (22a8cfebbbdb) and once at the second sample, and..." Receipt line 10 gives 6 of 6 only. **Replacement for the clause:** "...the class failed 6 of 6 forced re-executions on unmodified main (on both sides of the PR #5001 merge). The first-sample assertion is at [line 494](https://github.com/nick-boss-tech/solr/blob/22a8cfebbbdb026437d10b0aa47bb66ffdb23a9a/solr/core/src/test/org/apache/solr/update/DirectUpdateHandlerTest.java#L494) of the base commit (22a8cfebbbdb), and the surviving index..."

### SOLR-18506, PR 5029: STILL OPEN (one record conflict)

Round 4 items 1 to 6 landed. Counts match the receipt (32 of 32; 3 forced runs at `77c019e`; 1,243 of 200,000 at capacity 100; 14 occurrences, 10 on `branch_9x`; 17 runs; 1 in 10,000).

- Live, "What happens today": "Not a timing race: the backing cache uses a same-thread executor, so evictions complete inside the put." The commit title conflicts: `3c098439882` (the build head) is titled "SOLR-18506: Fix TestThinCache.testSimple flakiness from Caffeine eviction notification race". Receipt line 9 supports the body. The comment-only commit at `77c019e` removed the head test comment that called it "that race is the flake", so the head code no longer conflicts. The title cannot change without a force push. **Replacement (main side decides, or keep the body as written and accept the title):** append to the end of the "What happens today" paragraph: "The commit title says race; the model in Proof shows the eviction is delivered synchronously, so this is not a timing race."

### SOLR-18119, PR 5061: STILL OPEN

Head `660faedd026`, open, ready. Round 4 items 1, 2, 4, 5 and 6 landed. Item 3 landed for the Windows sentences, but not for the Linux claims below. Counts (11 + 3 = 14; SHA-256; 185,623 and 0; about 9,365; 2026-10-08) match the receipt.

- a. Item 7 join. Live: "...[#4999](https://github.com/apache/solr/pull/4999) (fail loudly when python3 is missing), which this PR's route supersedes and which is still open and [#5034](...)". Round 4 specified the clause to end at "still open." **Replacement:** "...which this PR's route supersedes and which is still open; and [#5034](https://github.com/apache/solr/pull/5034) (now closed; it fell back to GraalPy, by Jan)."
- b. Live Proof: "The Gradle wiring was checked on Linux only. See Limits for the Windows state." Live Limits summary: "...and this head is checked on Linux only." The receipt says "on this VM" and never names Linux. **Replacements:** Proof "The Gradle wiring was checked on one VM only. See Limits for the Windows state."; summary "...and this head is checked on one VM only." Keep "Linux" only if the receipt first records the platform.
- c. Unchanged-file citations. The 10-file diff (`9becf6c6a154` to `660faedd026`) does not include `dev-tools/scripts/checkJavadocLinks.py` or the two dev-docs files, yet the body links them at `660faedd` and describes their current content. **Replacements:** in these three links, replace the SHA with `9becf6c6a154b73c3445b41393517df1208a811c`, keep the anchors, and add "base commit" to the link text: `checkJavadocLinks.py` L252-L257; `dev-docs/how-to-contribute.adoc` L36; `dev-docs/solr-source-code.adoc` L37.
- Verified in the head diff, no change: `ChangesToHtml.java` L38-L811 and `write()` L270-L274; `ChangesToHtmlTask.java` L39-L72; `PythonCompat.java` L28-L85; `BuildInfraPlugin.java` L61-L63; `changes-to-html.gradle` L20-L25; the changelog link.
- Not judged: the Jira history in "What happens today" and "Choice" (check before posting). Round 4 record items 5 and 6 (owner policy; PR #4999 still open) remain owner decisions.

### SOLR-18523, PR 5062: STILL OPEN

Head `26678c37`, open, not draft. Round 4 items 1 to 3 landed. The receipt matches (55 s, 8,243 files, `checkBrokenLinks` absent; 22,710 breakdown).

- 1. Citation convention. The 5-file diff (`9becf6c6a154` to `26678c37`) does not include `javac.gradle`, `changes-to-html.gradle`, the ref-guide `build.gradle`, `globals.gradle`, `gradle-precommit.yml` or `index.template.md`. The body links them at `26678c37` while describing the tree before the change. Under the round 5 rule, link the base commit. In each link below, replace `26678c3737ca6baa353609846b2f58749ab1448d` with `9becf6c6a154b73c3445b41393517df1208a811c`, keep the anchors, and add "(base commit)" to the link text:
  - `gradle/java/javac.gradle` L58-L60 and L69
  - `gradle/documentation/changes-to-html.gradle` L71-L77 and L108
  - `solr/solr-ref-guide/build.gradle` L58, L76, L464-L478, L475
  - `gradle/globals.gradle` L175
  - `.github/workflows/gradle-precommit.yml` L26
  - `solr/documentation/src/markdown/index.template.md` L27 and L28

  Keep the head SHA on `documentation.gradle` L87-L89 (the PR changes that file) and on the changelog link.

### SOLR-18530, PR 5098: SATISFIED

Round 5 Drift 1 and Drift 2 landed. The LeaderChanged link points at the base commit, as Drift 2 requires, because `DistributedZkUpdateProcessor` is not in the branch diff. The report's Drift 1 text (head SHA) is superseded; do not revert it. The other-shard clause is gone, and the other unchanged-code citations are at the base commit with "base commit" text. The test citation L102-L117 at head is in the diff. Counts (6 of 6 fixed, 5 of 5 base, 0 failures; checked 2026-10-10) match the receipt. The missing changelog fragment is not flagged. Carried: `e28739b4069` is dated 2026-10-06 in the receipt and body, but its commit timestamp is 2026-10-07 (C1).

### SOLR-18531, PR 5101: STILL OPEN

Head as recorded in the receipt. Round 5 Drift 1 and Drift 2 landed exactly. Counts (TestJettySolrRunner 4 with 2 failures, then 4 of 4; LeaderElectionIntegrationTest 1 and 0; TestCoordinatorRole 9 and 0; TestPullReplica 39 with 1 skipped) match the receipt. The Limits bullets are supported by the branch tree.

- Live, "What happens today": "`LeaderElectionIntegrationTest.testSimpleSliceLeaderElection` hit this once in CI, on an unrelated pull request. No assertion ran." The receipt has no CI run. The only record is `reports/flaky-tests-root-cause-round-1-t4.md` (run 37991927523), and no CI log is on disk. **Replacement:** delete that sentence, or first add the run to the receipt and keep it.
- Receipt side (not a body defect): `receipts/SOLR-18531.md` line 6 still repeats "releases its own reservation" and "after the stops complete". The body wording is correct per the tree.

### SOLR-3704, PR 5103: STILL OPEN

Head `de63d4e`, open, ready. Round 5-b Items 1 to 4 landed. Counts (HighlighterTest 36 of 36; base 1 failure with `Invalid Date String:'1343779201999'`; LukeRequestHandlerTest 8 of 8; TestPointFields 104 of 104; 2026-10-06) match the receipt. The `x_date_p` field matches `*_date_p` (docValues and stored), but `id` defaults to docValues=false, so the stored value is read and the "not covered" Limit holds in the default configuration.

- a. Record conflict. The PR title now reads "SOLR-3704: Fix highlighting on a DatePointField: the highlighter was handed epoch milliseconds and failed with 'Invalid Date String'." The changelog fragment at head, line 1, still reads "...handed epoch milliseconds (or a Date.toString()) and failed...". Fixing it is a branch edit followed by a re-gate, so this is for the main side. **Replacement for changelog line 1:** "Fix highlighting on a DatePointField: the highlighter was handed epoch milliseconds and failed with 'Invalid Date String'."
- b. Unchanged-file citations. The 4-file diff (`cabedd1d968` to `de63d4e`) does not include `SolrDocumentFetcher.java`, `LukeRequestHandler.java` or `IndexSchema.java`. Link the base commit and say so, with the same lines: `SolrDocumentFetcher` L735-L736 and L793-L796; `LukeRequestHandler` L788; `IndexSchema` L360 and L369 (`cabedd1d968059215188f4e7563fb303241899ed`). `DefaultSolrHighlighter` L474-L483 at head is L473-L482 at base, because the import at line 24 shifts later lines by one (hunk `@@ -23,0 +24 @@`). Keep the head links for `DatePointField` L161-L163, `DefaultSolrHighlighter` L828-L829 and `HighlighterTest` L91-L125.

### SOLR-2681, PR 5104: SATISFIED

Round 5-b Item 2 landed exactly: the scope sentence, "Queries with no query(...) function query take the same path as before", and the Behavior change line. The title keeps "top-level", per round 5-b. The settled follow-up is not flagged. Citations: base L295-L298 at `cabedd1d968` (merge-base); head L300-L303 and test L91-L109 are in the 3-file diff. Counts (36 of 36, verified 2026-10-09; base 1 failure) match the receipt.

## Not judged

- The Jira history in SOLR-18119 and SOLR-18523 (outside the record).
- Lucene facts in SOLR-18505 and SOLR-18523 (external; not checked).
- CI-sourced claims, except where flagged above.
- Length: SOLR-18119 and SOLR-18523 exceed the pr-formula guide of about 3,500 characters. This is not a fidelity item.

## Process

- The claim `claims/pool-review-confidence-round-6.md` was pushed earlier and is ACTIVE until this report is pushed with it.
- Read-only. Nothing was posted, edited or built.
