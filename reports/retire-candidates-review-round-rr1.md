# Retire candidates review round, part RR1

Claim: `claims/pool-retire-candidates-review-round.md` (RR1 slice). Assignment: `assignments/pool-retire-candidates-review-round.md`.
Host: windows (review). Read-only: no build, no test, no gate, no push, no branch deletion, no PR write, no receipt edit. Worktree HEAD f56590a8660 (detached); this part is the only file written.

## Verdict counts

- RETIRE CLEAN: 4 (SOLR-17722, SOLR-17882, SOLR-12743, SOLR-17825)
- SALVAGE FIRST: 1 (SOLR-17372, conditional on one premise run that the record never made)

| Item | Live tip (origin) | Verdict |
|---|---|---|
| SOLR-17722 | fee3a26beb3c8289057ac41d94dfc8d83a969b71 | RETIRE CLEAN |
| SOLR-17882 | d328eb392d9c9acf3d925bb20729798e0d17720f | RETIRE CLEAN |
| SOLR-17372 | 048862fda8ab76f36f259c6849ec5c5a8cfafabe | SALVAGE FIRST |
| SOLR-12743 | 1bb4b227dfe5e212d47a8d63ba57b74091b9e395 | RETIRE CLEAN |
| SOLR-17825 | 0ef08ec86c650af99a51029da29e4c36503a70c4 | RETIRE CLEAN |

## Method (shared by all five)

- Live tips: `research\git-source.ps1 ls-remote origin refs/heads/<branch>`. All five match their receipts.
- Live main: `research\git-source.ps1 ls-remote upstream refs/heads/main` = d76fc14b9857e77b830d7a82514b28c523a239c2. Equal to local `upstream/main`.
- Merge-base: `git -C <source> merge-base upstream/main <tip>`. Branch-only commits: `git -C <source> log upstream/main..<tip>`.
- Main coverage: `git -C <source> merge-tree --write-tree upstream/main <tip>`, then `git -C <source> diff upstream/main <merged tree>`. If that diff equals the branch's own diff from its merge-base, main does not contain the branch's content.
- Category records: `branch-focus-inventory-2026-10-08.md` rows, and the receipt in `receipts/`.

## 1. SOLR-17722 (solr-17722-submit): RETIRE CLEAN

- Tip fee3a26beb3c, matches receipt. Merge-base 56ec140e3636d5f4150fa87fbf7103529536ac99, matches receipt. Branch-only commits: 0b10917f8c2 (code), 807f1a89c55 (testing handoff), fee3a26beb3 (import order, sanitizer test, premise note).
- Category: inventory line 456, "retire candidate, premise-dead; retire call pending". Receipt: NO GATE by finding.
- Diff against main: three files, 109 insertions. `SolrMessageProcessor.java` (+8, sets wt=javabin on ModifiableSolrParams in prepareIfUpdateRequest), `SolrMessageProcessorTest.java` (+24, handleItemForcesJavabinResponseWriter), `SOLR-17722-TESTING.md`. The merged-tree diff equals this delta, so main lacks the hunk.
- The hunk is a no-op on the wire on main. `solr/solrj/src/java/org/apache/solr/client/solrj/impl/HttpSolrClient.java` lines 153-163 (`initializeSolrParams`) copies the request params and then calls `params.set(CommonParams.WT, parserToUse.getWriterType())` before every send. The callers on main are HttpJettySolrClient (solrj-jetty, line 706), HttpJdkSolrClient (line 244) and ConcurrentUpdateJettySolrClient (solrj-jetty, line 196). The branch's set runs before that overwrite.
- Test: pins a mutation of a mock's params, not the wire. TESTING.md says it was never compiled or run. Not worth keeping as a regression pin, since it would pin a value the client overwrites.
- Evidence worth keeping: the probe (Solr17722WireProbeTest, seed 9891B4F8740AE16E, PASS, wt=javabin at merge-base) is recorded in the receipt with its log paths. The probe source is not on this branch, so the branch deletion does not remove it.
- Discrepancy: the assignment says the premise was shown by probes on vm3 then vm1 at both merge-base and branch head. The receipt records one base-only PASS on vm3, says no topic-head run was done, and says no VM1 result is claimed. The verdict rests on that base run plus the code read above.

## 2. SOLR-17882 (solr-17882-submit): RETIRE CLEAN

- Tip d328eb392d9c, matches receipt. Merge-base 56ec140e3636d5f4150fa87fbf7103529536ac99. Branch-only commits: 7f51fdd6c0f, 74d2726e29b (testing handoff), d328eb392d9 (publish-after-wire ordering, fallback note).
- Category: inventory line 170, "retire candidate, premise-dead; retire-or-re-aim call pending". Receipt: NO GATE as a fix, premise dead, owner call pending.
- Diff against main: `solr/modules/ltr/src/java/org/apache/solr/ltr/search/LTRQParserPlugin.java` (+22/-1, new synchronized `modelStore(SolrCore)` fallback and its call site) and `SOLR-17882-TESTING.md` (99 lines). No test class. The merged-tree diff equals this delta.
- Pins nothing main lacks. The branch's own note says the fallback does not fix the symptom: on main `solr/core/src/java/org/apache/solr/core/SyntheticSolrCore.java` lines 80-85 return a bare `new RestManager()` that is never initialized, with the comment "We do not expect RestManager ops on Coordinator Nodes". I confirmed this on main. The same finding is already recorded in `reports/query-parsing-round-1-q2.md` (findings 14-17) and `reports/query-parsing-round-1.md` line 44.
- Nothing gainable. The live question is the coordinator NPE on main (`LTRQParserPlugin.java` line 177 dereferences `mr`), which is a solr/core design change and an owner re-aim call, not a salvage from this branch. The branch's new lock on every named-model lookup was not measured and is not a gain.

## 3. SOLR-17372 (solr-17372-submit): SALVAGE FIRST

- Tip 048862fda8ab, matches receipt. Merge-base e2cdb2d7e8ae0be4cf6cf0606206c67d89e7278f, matches receipt. Branch-only commits: 3257831180f (tolerance hunk, clean subject), 2869441fb32 (handoff doc, process-word subject), 048862fda8a (removes the doc, process-word subject).
- Category: inventory line 252, "gated, no PR"; `reports/search-components-4-s5.md` (17372, finding 5 on commit subjects). Receipt: GATE FAILED on the premise.
- Diff against main: `solr/core/src/test/org/apache/solr/handler/component/StatsComponentTest.java` only (+6/-1). `testPercentiles` changes its tolerance from the fixed `1.0D` (main line 2298) to `Math.max(1.0D, expected * 0.10D)`. The merged-tree diff equals this delta.
- Why this is gainable and not closed: the hunk is the ticket's own suggested fix. Jira packet `research/jira-context/SOLR-17372.json`, comment 17866876 (2024-07-17): "allow 10% variance instead of a fixed 1.0". The packet's reproducing seed is 2074D8EC40F42163, which failed at the percentile assert with expected 60.0 and actual 58.29.
- Gap in the premise record: the receipt's base runs use seed 17372C0FFEE17372, which the record calls the "ticket seed" but does not define. No run at 2074D8EC40F42163 on base or on main appears in the record (`reports/search-components-4-s5.md`, finding 7). So "premise not real" is not shown at the Jira seed.
- The CI corroboration does not test the premise. GitHub run 37639175484 (job StatsComponentTest, success) ran on `ci/17372-statscomponent` at 933a41eb35f, which is 2869441fb32 plus a workflow file. That tree already has the tolerance hunk (line 2303).
- Salvage job (proposed, not run; Linux premise-run host, no build on Windows):
  - Test: `org.apache.solr.handler.component.StatsComponentTest.testPercentiles` in :solr:core, with the seed pinned to 2074D8EC40F42163.
  - Leg A (decisive): upstream/main d76fc14b9857 without the hunk. Expected FAIL at the percentile assert if the 2024 failure still reproduces (expected 60.0, actual near 58.3). Expected PASS means the premise is dead at the Jira seed, and the item is RETIRE CLEAN.
  - Leg B: head 048862fda8ab with the hunk, same seed. Expected PASS (the 10% tolerance gives 6.0 at 60.0).
  - If A fails and B passes: the fail-before proof exists. Salvage the hunk 3257831180f as owner option (i), in a clean outbound branch with no TESTING.md and no process-word subjects. The public PR is the owner's call. If A passes: retire.
  - Optional leg C: base e2cdb2d7e8ae at the same seed, to close the receipt's base claim.

## 4. SOLR-12743 (solr-12743-submit): RETIRE CLEAN

- Tip 1bb4b227dfe5, matches receipt. Merge-base 86bc6f292245e0566511a9d9c3fb8aaba933e564 (main is 431 commits ahead). Branch-only commits: 1c09b8d00fb (suppress cleanup while cache scans wait, in ConcurrentLRUCache.java), 1bb4b227dfe (OPEN-QUESTIONS-SOLR-12743.md).
- Category: inventory line 450, "retire candidate, retired in records; branch deletion pending". Receipt: NO GATE, retired.
- Merge with main: modify/delete conflict on `solr/core/src/java/org/apache/solr/util/ConcurrentLRUCache.java`. Main deleted it in ec218b20cc1 (#4516, Caffeine). `git grep ConcurrentLRUCache upstream/main -- solr` returns nothing.
- Doc and build check: the only other file is OPEN-QUESTIONS-SOLR-12743.md, a handoff note marked for removal before a PR. Its content (obsolete class, the reporter saw the leak continue with the 2019 patch, two owner questions) is already in the receipt and the inventory. No build change on the branch.
- Live-gap check: the hunk changes lock ordering inside the deleted class. Main's replacement is `solr/core/src/java/org/apache/solr/search/CaffeineCache.java`, and nothing on main carries the markAndSweep path. Not measured against Caffeine.

## 5. SOLR-17825 (solr-17825-submit): RETIRE CLEAN

- Tip 0ef08ec86c65, matches receipt. Merge-base 86bc6f292245e0566511a9d9c3fb8aaba933e564. Branch-only commits: 4b35e5a5165 (beanutils floor constraint and verifyCommonsBeanutilsFloor task in `solr/cross-dc-manager/build.gradle`), 0ef08ec86c6 (SOLR-17825-DECISION.md).
- Category: inventory line 458, "retire candidate, branch deletion pending Nick call". Receipt: NO GATE, retired.
- Merge with main: content conflict in `solr/cross-dc-manager/build.gradle`. Main's cross-dc-manager build has no kafka213 and no beanutils dependency, and `gradle/` has no kafka213 catalog entry. The branch's guard throws when beanutils is absent from runtimeClasspath, so the merged file would fail `check`. I did not resolve the runtime classpath (no build), so the transitive part rests on the decision note.
- Main's only beanutils pin is `gradle.lockfile` line 7, `commons-beanutils:commons-beanutils:1.9.4=ratDeps`. ratDeps is the RAT validation classpath (`gradle/validation/rat-sources.gradle`, lines 32-40 and 108), build-time only. This is not on the branch, so it is not a gain from it; noted for the main side only.

## Could not settle

- SOLR-17722: the head and VM1 probe runs named in the assignment are not in the receipt. Only the vm3 base run is recorded.
- SOLR-17372: the Jira seed was never run on base or main in the record. The SALVAGE verdict turns on Leg A.
- SOLR-17882: the round 12 and round 15 goal files were not read. I did not check whether coordinator nodes reach the LTR parser in a supported layout.
- SOLR-17825: transitive absence of beanutils on the cross-dc runtimeClasspath was not resolved (no build).
- Claim file: not edited (one-file limit). It still reads ACTIVE with heartbeat 2026-10-11T14:53Z. The parent should append the RR1 heartbeat and DONE mark with the deliverable.
- Branch deletions, the SOLR-18300 Jira link, and any PR action remain with Nick and the main side.
