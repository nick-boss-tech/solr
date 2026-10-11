# Build-docs and metrics draft fidelity, slice 4

Assignment: `assignments/pool-draft-fidelity-cli-security-builddocs-metrics.md`. Claim: `claims/pool-draft-fidelity-cli-security-builddocs-metrics.md` (windows review agent, slice 4). Slice drafts: `pr-drafts/build-docs/SOLR-17252.md`, `pr-drafts/build-docs/SOLR-17752.md`, `pr-drafts/build-docs/SOLR-17842.md`, `pr-drafts/metrics/SOLR-17987.md`. Round 1 sources: `reports/build-docs-misc-round-1.md` and its parts `-g1` to `-g6`; `reports/metrics-round-1.md` and its parts `-m1` and `-m2`. Receipts: `receipts/SOLR-17252.md`, `SOLR-17752.md`, `SOLR-17842.md`, `SOLR-17987.md`. Read-only. No build, test, gate, commit, push, PR, comment or Jira call.

Answers material: a grep of `material/` for 17252, 17752, 17842 and 17987 found no hits, and no answers file covers these tickets. A second grep for the topical names (EDITOR, emacs, TestShardResponseLogging, release-benchmark, disabledRegistries, SolrResponseUtil, jetty) found hits only in two 5939/5941 files, which are not these tickets.

Head check (`git ls-remote origin refs/heads/<branch>`, live tip compared with the draft):

| Ticket | Branch | Live tip | Draft names | Match |
|---|---|---|---|---|
| SOLR-17252 | solr-17252-submit | d730a266a0425b2609f4bcf21756555d3d3b8f52 | d730a266a042 | yes |
| SOLR-17752 | solr-17752-submit | e5c0a64f993c7ce5e9a594f5a6eb0679925e9de9 | e5c0a64f993c | yes |
| SOLR-17842 | solr-17842-submit | 008973f6313374517e20a0c347d0fb4885afe1dd | 008973f63133 | yes |
| SOLR-17987 | solr-17987-submit | 38abf6423126112cf8a451f4d3fedea0920eae90 | 38abf6423126 | yes |

Merge-bases with upstream main read at `3f5d4c5bf8ac96fec3a3219398b0c52c5418d48c` (`git merge-base`): SOLR-17252 `14c7aac0d151402b00259e2fb9bf5eed7049ec5d`; SOLR-17752 `56ec140e3636d5f4150fa87fbf7103529536ac99`; SOLR-17842 `14c7aac0d151402b00259e2fb9bf5eed7049ec5d`; SOLR-17987 `cabedd1d968059215188f4e7563fb303241899ed`. All cited SHAs resolve (`git cat-file -t`). The three fork head SHAs of SOLR-17252, SOLR-17752 and SOLR-17842 are not ancestors of upstream main, so an apache/solr blob link at a head SHA would depend on fork-network sharing; the formula asks for nick-boss-tech/solr links.

## Verdicts

| Draft | Head checked | Verdict |
|---|---|---|
| SOLR-17252 | d730a266a042 (ls-remote matches) | DRIFT (2 items) |
| SOLR-17752 | e5c0a64f993c (ls-remote matches) | DRIFT (4 items) |
| SOLR-17842 | 008973f63133 (ls-remote matches) | DRIFT (3 items) |
| SOLR-17987 | 38abf6423126 (ls-remote matches) | DRIFT (6 items) |

Owner flags on SOLR-17987 (from the claim): the draft carries neither flag. Flag (a), the `jetty` registry, is not stated at all (item 1). Flag (b), the environment variable name, is stated as the head has it, but not as a departure from the documented convention (item 2).

## SOLR-17252

Verdict: DRIFT (2 items).

1. Draft says: "[releaseWizard.yaml line 450](https://github.com/apache/solr/blob/3f5d4c5bf8ac96fec3a3219398b0c52c5418d48c/dev-tools/scripts/releaseWizard.yaml#L450)" and "[line 1534](https://github.com/apache/solr/blob/3f5d4c5bf8ac96fec3a3219398b0c52c5418d48c/dev-tools/scripts/releaseWizard.yaml#L1534)" and "([lines 196-199](https://github.com/apache/solr/blob/3f5d4c5bf8ac96fec3a3219398b0c52c5418d48c/dev-tools/scripts/releaseWizard.py#L196-L199))" (paragraph at line 13, "On main").
   Evidence: these are pre-change symptom citations. The formula (`pr-formula.md`, presentation rule) says they link the merge-base commit and the text says so. The draft links upstream main `3f5d4c5`. At the merge-base `14c7aac0d151`, `releaseWizard.yaml` line 450 is `cmd: "{{ editor }} .asf.yaml"` (same as main), but the editor command for the news file is at line 1532 there; line 1534 is `stdout: true`. Main has it at 1534 (the yaml changed by two lines on main). `releaseWizard.py` lines 196-199 hold the same `get_editor()` warning at the merge-base and on main.
   Replacement (whole paragraph at line 13, replacing the current paragraph):
   "The wizard runs `{{ editor }}` in the same terminal for steps such as editing `.asf.yaml` ([releaseWizard.yaml line 450](https://github.com/nick-boss-tech/solr/blob/14c7aac0d151402b00259e2fb9bf5eed7049ec5d/dev-tools/scripts/releaseWizard.yaml#L450)) and a later file edit ([line 1532](https://github.com/nick-boss-tech/solr/blob/14c7aac0d151402b00259e2fb9bf5eed7049ec5d/dev-tools/scripts/releaseWizard.yaml#L1532)). Before this change, `get_editor()` warns only when EDITOR is exactly `vi`, `vim`, `nano`, `pico` or `emacs`, and then uses the value anyway ([lines 196-199](https://github.com/nick-boss-tech/solr/blob/14c7aac0d151402b00259e2fb9bf5eed7049ec5d/dev-tools/scripts/releaseWizard.py#L196-L199)). A value such as `/usr/bin/nano` or `nano -x` gets no warning. The links in this paragraph point to the merge-base commit, `14c7aac0d151`, which is the code before this change."

2. Draft says (Choice list, line 36): "- Move the check to the point where the wizard starts an editor. The cost is that a bad EDITOR fails partway through a run, after release state exists."
   Evidence: `receipts/SOLR-17252.md` line 8 records the owner position Q3 as "the check stays at startup, before release state exists". `reports/build-docs-misc-round-1.md` owner decision 8 (line 89) and `reports/build-docs-misc-round-1-g3.md` owner decision 1 (line 34) recommend moving the check into `check_prerequisites()` (head line 1380, before `ReleaseState` and `state.save()` at 1392). The Choice lists no option for that earlier placement.
   Replacement (insert as a new bullet directly above the quoted bullet):
   "- Move the check earlier, into `check_prerequisites()`, so a refused EDITOR stops the run before the release state file is written. The cost is a small code move. The rc file is still written first."

Optional notes, not blocking:
- The HOLD line at the top names owner decisions. It is internal and must be deleted before posting.
- The branch has no changelog fragment (`git diff --stat 14c7aac0d151 d730a266` shows three files, none under `changelog/`). The title check has no fragment to match. Commit subjects are "SOLR-17252: fail fast for terminal-attached editors" and "SOLR-17252: match terminal editors by command name, not exact EDITOR string". The draft has no Changelog line, which is consistent with no fragment. Confirm whether dev tooling needs one.
- The Limits could name that the check was read, not run, on Windows paths (`reports/build-docs-misc-round-1-g3.md`, Not checked).
- Plain language: "basename", "terminal-attached", "rc file", "release state file" are compressed; one-line glosses would help.
- Head line checks held: `is_terminal_editor` at 194-199, the exit at 206-211, `state.save()` at 1392, the menu loop at 1412-1414, `check_prerequisites()` at 1380. The docs lines 56-58 (`releasing.adoc`) and 52-54 (`README.md`) are the lines the branch adds, so head links are correct for them.
- Proof counts (21 checks, all passed, 2026-10-07) match `receipts/SOLR-17252.md` line 5-6. The harness log is not on disk (round 1 cross-cutting section).

## SOLR-17752

Verdict: DRIFT (4 items).

1. Draft says (line 5): "Title: Log the node and response key names, not the shard request, in the corrupted response warning"
   Evidence: `changelog/unreleased/SOLR-17752.yml` at e5c0a64 (line 1): "title: The warning logged for a corrupted shard response no longer prints the shard request or the response, so the query text no longer appears in it".
   Replacement: "Title: The warning logged for a corrupted shard response no longer prints the shard request or the response, so the query text no longer appears in it"

2. Draft says (paragraph at line 13): "([SolrResponseUtil.java#L55](https://github.com/apache/solr/blob/e5c0a64f993c7ce5e9a594f5a6eb0679925e9de9/solr/core/src/java/org/apache/solr/util/SolrResponseUtil.java#L55))", and "([SolrCore.java#L3004](https://github.com/apache/solr/blob/e5c0a64f993c7ce5e9a594f5a6eb0679925e9de9/solr/core/src/java/org/apache/solr/core/SolrCore.java#L3004))", and "([solrconfig.xml#L597](https://github.com/apache/solr/blob/e5c0a64f993c7ce5e9a594f5a6eb0679925e9de9/solr/server/solr/configsets/_default/conf/solrconfig.xml#L597))" with "/select and /query".
   Evidence: (a) At head e5c0a64, `SolrResponseUtil.java` line 55 is `RESPONSE_HEADER_PARTIAL_RESULTS_KEY))) {`. The warning is at lines 58-61. At the merge-base `56ec140e3636`, line 55 is `log.warn("corrupted response on {} : {}", srsp.getShardRequest(), solrResponse);`, so the pre-change citation belongs at the merge-base. (b) These are pre-change symptom citations. The branch does not touch `SolrCore.java` or `solrconfig.xml` (diff base to head is three files). The formula says merge-base links on nick-boss-tech/solr with the text saying so. (c) The `SolrCore.java` link covers only the EXPLICIT branch (line 3004); the ALL branch is at 3006-3007. (d) The `solrconfig.xml` link covers only `/select` (line 597); `/query` is at line 605, and the text names both.
   Replacement (whole paragraph at line 13, replacing the current paragraph):
   "When a shard response lacks the expected section and does not set partial results, the warning logs the shard request and the full response at WARN ([SolrResponseUtil.java#L55](https://github.com/nick-boss-tech/solr/blob/56ec140e3636d5f4150fa87fbf7103529536ac99/solr/core/src/java/org/apache/solr/util/SolrResponseUtil.java#L55)). These links point to the merge-base commit, `56ec140e3636`, which is the code before this change. The response header can echo the request params. SolrCore adds them when echoParams is explicit or all ([SolrCore.java#L3004-L3007](https://github.com/nick-boss-tech/solr/blob/56ec140e3636d5f4150fa87fbf7103529536ac99/solr/core/src/java/org/apache/solr/core/SolrCore.java#L3004-L3007)), and the default configset sets explicit on `/select` ([solrconfig.xml#L597](https://github.com/nick-boss-tech/solr/blob/56ec140e3636d5f4150fa87fbf7103529536ac99/solr/server/solr/configsets/_default/conf/solrconfig.xml#L597)) and on `/query` ([solrconfig.xml#L605](https://github.com/nick-boss-tech/solr/blob/56ec140e3636d5f4150fa87fbf7103529536ac99/solr/server/solr/configsets/_default/conf/solrconfig.xml#L605)). The ticket notes that a query can carry customer data."

3. Draft says (Limits): "[SuggestComponent.java#L181](https://github.com/apache/solr/blob/e5c0a64f993c7ce5e9a594f5a6eb0679925e9de9/solr/core/src/java/org/apache/solr/handler/component/SuggestComponent.java#L181)"
   Evidence: pre-change code that the branch does not touch (line 181 is `log.info("SuggestComponent prepare with : {}", params);` at both the merge-base and head). It is linked at the fork head SHA on apache/solr. The formula says merge-base link on nick-boss-tech/solr with the text saying so.
   Replacement (the whole Limits bullet, replacing the current bullet):
   "- Other log lines print request params at INFO or DEBUG, for example [SuggestComponent.java#L181](https://github.com/nick-boss-tech/solr/blob/56ec140e3636d5f4150fa87fbf7103529536ac99/solr/core/src/java/org/apache/solr/handler/component/SuggestComponent.java#L181), at the merge-base commit. They are not changed here. I can open that follow-up on request."

4. Draft says (Proof, line 25): "**TestShardResponseLogging passed 1 of 1 at head e5c0a64f993c7ce5e9a594f5a6eb0679925e9de9.**" (no date)
   Evidence: no verification date is in the draft. `receipts/SOLR-17752.md` line 4 gives "plain push from a49a4a35502, 2026-10-04", and line 10 gives the DONE entry date 2026-10-04. The receipt gives no gate run time (round 1 `-g1` line 54). The head commit `e5c0a64` is authored and committed 2026-10-05 04:34:08 +0000, so 2026-10-04 cannot be the date of a run at this head unless the receipt date refers to an earlier run. The main side must confirm the date against the gate log (`g17752-gate.log`, not on disk) before posting.
   Replacement (sentence at line 25, replacing the current bold sentence): "**TestShardResponseLogging passed 1 of 1 at head e5c0a64f993c7ce5e9a594f5a6eb0679925e9de9, verified 2026-10-04.**"

Optional notes, not blocking:
- The Proof says "On the unpatched code the test fails at the node-name assertion" without naming the base. The premise run in `receipts/SOLR-17752.md` line 7 used `14c7aac0d151`; the merge-base is `56ec140e3636`. Name the base used once the rebase decision is made. The assertion name (line 69) is a reading of the test, not a receipt line.
- The HOLD line at the top names the owner decision. Delete it before posting.
- "What this change does" links are correct at head: `SolrResponseUtil.java` lines 58-61 and 84-94, `TestShardResponseLogging.java` lines 34-70 (the node check is line 69, the term check line 70), and the changelog file exists at head with 7 lines.
- Plain language: "top-level key names", "echoParams", "partial results" are compressed; a short gloss would help.

## SOLR-17842

Verdict: DRIFT (3 items).

1. Draft says (paragraph at line 19): "names one or two existing benchmark classes per area"
   Evidence: the table at `release-benchmarking.md` lines 41-48 at 008973f names three classes for search (`SimpleSearch`, `NumericSearch`, `ExitableDirectoryReaderSearch`) and one for each other area.
   Replacement (phrase replacing "names one or two existing benchmark classes per area"): "names one to three existing benchmark classes per area"

2. Draft says (paragraph at line 13): "([README](https://github.com/apache/solr/blob/3f5d4c5bf8ac96fec3a3219398b0c52c5418d48c/solr/benchmark/README.md))", "([jmh-profilers.md](https://github.com/apache/solr/blob/3f5d4c5bf8ac96fec3a3219398b0c52c5418d48c/solr/benchmark/docs/jmh-profilers.md)" and "[jmh-profilers-setup.md](https://github.com/apache/solr/blob/3f5d4c5bf8ac96fec3a3219398b0c52c5418d48c/solr/benchmark/docs/jmh-profilers-setup.md))"
   Evidence: these are pre-change symptom citations. They link upstream main `3f5d4c5` on apache/solr, and the text does not say so. The formula says merge-base link on nick-boss-tech/solr with the text saying so. Content is the same at the merge-base and main: `git diff` of `solr/benchmark` between `14c7aac0d151` and `3f5d4c5` changes only `gradle.lockfile`; the README has no "release" text at the merge-base; `docs/` holds the two profiler notes at both.
   Replacement (whole paragraph at line 13, replacing the current paragraph):
   "The module README explains how to run JMH and write results ([README](https://github.com/nick-boss-tech/solr/blob/14c7aac0d151402b00259e2fb9bf5eed7049ec5d/solr/benchmark/README.md)), but it does not mention release comparisons. The `docs/` folder holds only two profiler notes ([jmh-profilers.md](https://github.com/nick-boss-tech/solr/blob/14c7aac0d151402b00259e2fb9bf5eed7049ec5d/solr/benchmark/docs/jmh-profilers.md), [jmh-profilers-setup.md](https://github.com/nick-boss-tech/solr/blob/14c7aac0d151402b00259e2fb9bf5eed7049ec5d/solr/benchmark/docs/jmh-profilers-setup.md)). These links point to the merge-base commit, `14c7aac0d151`, which is the code before this change. The ticket asks for published benchmark data for each release, comparisons across releases and against other search engines, and vector search numbers on external leaderboards."

3. Draft says (Limits, line 29): "and no vector search accuracy numbers. The ticket asks for those."
   Evidence: `reports/build-docs-misc-round-1-g3.md` (ticket read, line 42) records the ask as "vector search numbers on external leaderboards". The word "accuracy" is not in the record.
   Replacement (phrase replacing "no vector search accuracy numbers"): "no vector search numbers"

Optional notes, not blocking:
- The HOLD line names the `<build>` placeholder in the guide's example (line 69), which bash reads as a redirect. Round 1 `-g3` says the fix is a branch commit, not a draft edit. The draft text does not depend on it, but the example does not run as written until fixed.
- "The README gains one line" (paragraph at line 19): the diff adds two lines (the link sentence and a blank line). Content is one line.
- No Changelog line: the branch has no changelog. Consistent with the docs-only position in round 1; say so or leave it out.
- Title "Add a release benchmarking guide (instructions only)": no changelog fragment to match. Commit subjects are "SOLR-17842: document release benchmark workflow" and "SOLR-17842: add benchmark area table and example command, move README pointer".
- The Proof date (2026-10-08) matches the receipt line 5 and 4 (light gate relaunch finished 2026-10-08). The light-gate log is not on disk.
- Plain language: "JMH", "primaryMetric.score", "release comparisons" need one-line glosses.

## SOLR-17987

Verdict: DRIFT (6 items). The draft carries neither owner flag; both are listed below.

1. Owner flag (a), `jetty` registry: not stated in the draft. Draft says: no mention of the example or the changelog title (the draft's Limits end at "With metrics off, asking for a registry under a null name now throws...").
   Evidence: `changelog/unreleased/SOLR-17987-disable-metrics-by-registry.yml` at 38abf642 line 2 title: "Metrics can be disabled per registry with -Dsolr.metrics.disabledRegistries=jvm,jetty; those registries get no-op meter providers." The ref guide `solr/solr-ref-guide/modules/deployment-guide/pages/metrics-reporting.adoc` adds `-Dsolr.metrics.disabledRegistries=jvm,jetty` and `SOLR_METRICS_DISABLEDREGISTRIES=jvm,jetty` (diff lines 106-107). `git grep` of `"jetty"` and `solr.jetty` at 38abf642 in `solr/core/src/java` and `solr/solrj/src/java` finds only `solr.jetty.*` configuration property names, no registry. Round 1: `reports/metrics-round-1-m1.md` Q2 and owner decision 5; `-m2.md` owner decision 5.
   Replacement (new Limits bullet, added after the bullet "With metrics off, asking for a registry under a null name now throws a NullPointerException instead of returning the no-op provider."):
   "- The reference guide example and the changelog title use `jetty`. This code creates no `solr.jetty` registry, so that example disables nothing. The example should use a registry this code creates, such as `node` or `overseer`."

2. Owner flag (b), environment variable: stated as the head has it, but the draft does not say it departs from the documented convention or that it works only through a mapping line. Draft says (line 15): "The environment variable `SOLR_METRICS_DISABLEDREGISTRIES` sets the same property. The reference guide documents the setting."
   Evidence: the ref guide's OTLP section (`metrics-reporting.adoc` lines 328-330 at 38abf642) gives the rule: replace "." with "_", convert camelCase to UPPER_SNAKE_CASE, make all letters uppercase. That gives `SOLR_METRICS_DISABLED_REGISTRIES`, which needs no line. The head adds `SOLR_METRICS_DISABLEDREGISTRIES=solr.metrics.disabledRegistries` to `solr/solrj/src/resources/EnvToSyspropMappings.properties` (diff, line 39). Without that line the variable is ignored (`reports/metrics-round-1-m1.md` Q5). Round 1 owner decision 1 recommends the rename.
   Replacement (replace the two sentences quoted above with): "The environment variable `SOLR_METRICS_DISABLEDREGISTRIES` sets the same property, through a mapping line added to `EnvToSyspropMappings.properties`. Its name does not follow the rule the reference guide gives for OTLP settings, which would be `SOLR_METRICS_DISABLED_REGISTRIES`. The reference guide documents the setting."

3. Title line missing (formula check 5). Draft starts with the AI header and the Jira link (line 3), and has no "Title:" line. The other drafts in this batch carry one.
   Evidence: the changelog title at 38abf642 (item 1) is the title to match. It names `jetty`, so the branch fragment needs the same fix as item 1.
   Replacement (insert as a new line after line 1, before the Jira link): "Title: Metrics can be disabled per registry with -Dsolr.metrics.disabledRegistries; those registries get no-op meter providers."
   Note: the changelog fragment must change on the branch to this same title before posting. That is a branch commit, not a draft edit.

4. Limits omit a round 1 limit: the new test does not cover the property or the environment variable. Draft Proof names only the test's assertions; Limits do not mention the gap.
   Evidence: `SolrMetricManagerTest.java` lines 77-93 at 38abf642 pass `List.of("quiet", " solr.noisy ", "")` straight to the three-argument constructor. `reports/metrics-round-1-m2.md` Q8 lists "the loader-constructor property path" and "the environment-variable path" as not covered; `reports/metrics-round-1-m1.md` "The gate cannot settle" lists the same.
   Replacement (new Limits bullet): "- The new test passes the list straight to the constructor. It does not read the system property or the environment variable, so the code that reads them has no test."

5. Limits omit a round 1 limit: the JVM metrics code starts when `jvm` is listed. Draft does not mention it.
   Evidence: `SolrMetricManager.java` lines 183-185 at 38abf642 call `new OtelRuntimeJvmMetrics().initialize(this, JVM_REGISTRY)` whenever the global flag is on, and `meterProvider("solr.jvm")` returns the no-op for a listed name (lines 470-471). So `initialize` runs against a no-op provider. Whether `RuntimeTelemetry` tolerates that is not verifiable by reading (`reports/metrics-round-1-m1.md` G1; `-m2.md` Q6(a)).
   Replacement (new Limits bullet): "- When `jvm` is listed, the JVM metrics code still starts, against a no-op provider, because the global switch is on. No test covers that start, so it has not been shown to work with the OpenTelemetry runtime instrumentation."

6. Choice rationale overstates: "The property keeps every metrics switch in one form." Draft says (Choice, line 31): "The property keeps every metrics switch in one form."
   Evidence: the on-off flag is `<metrics enabled>` in `solr.xml` (`reports/metrics-round-1-m1.md` Q2, `SolrXmlConfig.java` lines 701-706 at 38abf642). The environment name `SOLR_METRICS_ENABLED` maps through `solr.xml` substitution (`-m1` Q5). So the global switch is not in the same form as the per-registry list.
   Replacement (replace that sentence with): "The cost is that the on-off flag stays in `solr.xml` while this list is a system property."

Optional notes, not blocking:
- No HOLD line. Round 1 (`reports/metrics-round-1.md` lines 13 and 57-61) says the branch is not draftable until owner decisions on the environment name, the CPU breaker, the proof label and the example are settled. Consider a HOLD line like the other drafts.
- The CPU breaker choice (throw or degrade; `-m1` owner decision 2, `-m2` owner decision 1) is a live alternative that the Choice section does not list. Optional to add.
- The overlap with `solr.metrics.jvm.enabled` (`-m1` owner decision 3) is not stated. Optional Limit.
- The alternative in the Choice (a `solr.xml` element) has no stated cost. The formula asks for "the alternative's cost".
- The behavior change text matches the code: the CPU breaker message "JVM metrics disabled. Cannot calculate CPU usage" at `CPUCircuitBreaker.java` lines 108-114 at 38abf642. The "null name" NullPointerException Limit matches `SolrMetricManager.java` lines 468-471 (name prefixed before the enabled check).
- The Proof counts (18 tests, 0 failures, 2026-10-10) match `receipts/SOLR-17987.md` lines 3 and 6. The gate log is not on disk (`-m1` Main-side work owed).
- The Proof avoids "gate green"; the receipt's INCONCLUSIVE fail-before is stated as such. Good.
- The test range `#L77-L93` ends one line before the method's closing brace (line 94). Trivial.

## Not done

- No build, Gradle, test, gate run, `test-queue` call, commit, push, PR call, comment, or Jira access. Only `git` reads and `ls-remote`.
- The gate logs and harness logs named by the receipts (`g17252r35-harness.log`, `g17752-*.log`, `g17842-lightgate.log`, `g17987-gate.log`) were not opened and not re-searched. Round 1 reports say they are not on disk under the searched paths. Proof claims rest on the receipts.
- Ticket text (Jira) was not re-read. Ticket claims in the "What happens today" paragraphs rest on the round 1 reads.
- Fork head SHAs were checked for local presence and for the live `ls-remote` tip, and for ancestry against upstream main. Links were not fetched online. Anchors were not rendered.
- The em dash check found no U+2014 in the four drafts. Replacement text was written without em dashes or internal vocabulary.
