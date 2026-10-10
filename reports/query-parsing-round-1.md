# Query parsing round 1: round roll-up

Claim: `claims/query-parsing-round-1.md` (commit `c87876a40f2`). Assignment: `assignments/query-parsing-round-1.md` (commit `9bfcb2ff1c1`). Per-part reports: `reports/query-parsing-round-1-q1.md` through `-q8.md`.

Done 2026-10-09 by Claude Code (AI agent), working for Nick Shanin. Eight read-only subagents did the audit, split by family. No build, Gradle run, or test was run. No submit branch, live PR, JIRA item, or comment was touched. Nothing was posted. Live GitHub reads were read-only `gh pr view` calls.

## Heads

Every named head matches its live branch. The fetch moved four unnamed tips (11391, 16570, 17882 and the no-gate audits); those are audit-only.

## Correction to the claim and the record

- **PR 13202 (SOLR-13202) is merged, not awaiting merge.** Checked by me: `gh pr view 5012` returns `state: MERGED`, `mergedAt: 2026-10-07T15:03:48Z`, head `c0aec3a7ae0`. The merge commit `93e357bcf6b` is on upstream main. The claim table, the assignment, the receipt, and the inventory still say "awaiting merge". The claim's live-head row is therefore stale.
- The claim says 11761 extends `TestExtendedDismaxParser`. It does not (part q6).

## Per-ticket verdicts

| Ticket | Verdict | Draft | Head | What blocks it |
|---|---|---|---|---|
| SOLR-874 | Hold | `SOLR-874.md` | `ac9ab337537` | Three fixes in its own code: mixed chains still throw (`ipod - AND`), a loop is needed for dangling operators, `!` is not a NOT spelling; changelog title; then a re-run |
| SOLR-4824 | Held | `SOLR-4824.md` | `76c777e4661` | Bad `fuzzy.maxExpansions` values should return 400 but can become server errors (`LuceneQParser.java` lines 53-56); owner call on the parameter name |
| SOLR-6014 | Hold | `SOLR-6014.md` | `505849d2d4e` | Null guard at `QParser.java` line 216 (a stopword-only `{!dismax}` with cache or cost); owner call on the fq path. Line 216 is also touched by 15906 |
| SOLR-8977 | Draftable after a changelog fix | `SOLR-8977.md` | `395b24964fd` | Changelog says "no longer matches no documents"; the broken case returns the start documents. Owner Choice: repair the traversal filter on every core, or document a workaround below `luceneMatchVersion` 10.2.0 |
| SOLR-9048 | Draftable | `SOLR-9048.md` | `b145018563c` | Owner Choice: an all-stopword nested query matches everything (implemented) or nothing. Changelog must name the `{!filters}` parser, which shares the changed code |
| SOLR-9149 | Draftable as is | `SOLR-9149.md` | `151dfed119e` | Commit subjects carry handoff and gate words; squash before the PR |
| SOLR-10897 | Hold | `SOLR-10897.md` | `d9240d0750f` | `TestSimpleQParserPlugin.java` line 598 adds a stray `@Test` to `testQueryAnalyzerIsUsed`; delete it and rerun. Squash commits; subject `6a3828edafe` has a doubled key. Receipt says "20 of 20" but names 18 tests |
| SOLR-11391 | Held, audit only | none | `b0f15a22856` | Its Jira ticket is "JoinQParser for non point fields should use the GraphTermsCollector"; the branch makes an unknown join method return 400. Do not link the ticket. Owner: a new key, or drop the branch |
| SOLR-11761 | Draftable with one Choice | `SOLR-11761.md` | `41893ee9ce6` | The fail-before failure line is not on disk, so the draft says only that the test fails on base |
| SOLR-12212 | Draftable, hold until the grammar fix lands | `SOLR-12212.md` | `876953fdc92` | `QueryParser.jj` lines 237 and 240 call `getOccur()` and `getQuery()`; the rest of the code uses `occur()` and `query()`. Change them (and the pre-existing lines 231 and 232), regenerate the parser, then a fresh gate. Per-class count placeholder |
| SOLR-12532 | Draftable with scope stated | `SOLR-12532.md` | `69c06da4467` | Behavior is wider than word-delimiter graphs; eDisMax is unchanged. State the scope |
| SOLR-12608 | Hold, not for posting | `SOLR-12608.md` | `d1dd8a1f9f0` | The premise is not shown (the research note says SKIP, the round 28 review says not ready); the premise log is not on disk; the changelog claims a memory fix the run does not show |
| SOLR-12871 | Held | `SOLR-12871.md` | `c79a49320cd` | Changelog line 2 says rejection is "a clear 400 error instead of a Lucene UnsupportedOperationException". On main the client already gets the same 400 and message. The branch changes only the server log and the root class |
| SOLR-13202 | Merged; consistency pass only | none | `c0aec3a7ae0` | See the correction above. Body gaps for the owner: it omits `AuxIndexJoinQParserPlugin` from the parser list, omits an `auxIndexJoin` error-message change, has a Limits sentence a new test contradicts, and its Proof cites `b3ded4da758` |
| SOLR-13838 | Hold | none | `4520c25abe2` | No recorded pre-fix outcome; the queue row points at a commit before the reconcile. Static counts agree |
| SOLR-13903 | Hold | none | `73ef411dd11` | No recorded pre-fix outcome; the changelog omits the weight and training-order change |
| SOLR-15615 | Draftable after one comment fix | `SOLR-15615.md` | `be77267bf55` | `CloudMLTQParserTest.java` line 208 says docs 13 and 14 come from the other collection; they come from `COLLECTION`. Owner: confirm the fallback lookup's authorization and accept the unmeasured fan-out cost |
| SOLR-15906 | Held | none | `50139a8e979` | Two fixes that break existing tests (found by reading, not run): `QParser.java` line 495 hands sort and fl specs such as `{!func v=$sortfunc} desc` to the lucene parser, and a lone `)` after a `v` block now throws |
| SOLR-16130 | Live PR; consistency pass | none | `3c48dec4b79` | Head matches. PR 5004 conflicts with upstream main by one import line (local check). The body's "10/10" count is not in the receipt |
| SOLR-16267 | Draftable | `SOLR-16267.md` | `8f4b0c6d2fb` | Limits name `exists()`, `def()` and percentile, which have no direct assertion. Confirm `def()` and PR 1481 status before posting |
| SOLR-16570 | Not submittable, audit only | none | `974c44f9608` | No gate; the branch carries `SOLR-16570-TESTING.md`. Pairing with 17796 is test-file only and merges cleanly |
| SOLR-17280 | Draftable after a changelog fix | `SOLR-17280.md` | `40817c5cb7e` | Changelog scope is wider than "inside another filter". Filter-cache tradeoff is Limits material |
| SOLR-17311 | Draftable | `SOLR-17311.md` | `9f7524582f9` | Confirm the live-head NPE line before posting; the only on-disk failure output is from a superseded head |
| SOLR-17796 | Draftable after one comment fix | `SOLR-17796.md` | `661165d2673` | Comment fix in `QueryParser.jj` and `.java` (lines 236-237 and 251-252). Decide the negated-collapse question first. Set the PR title from the Jira body (AND), not the title (OR) |
| SOLR-17882 | Audit only, retire candidate | none | `d328eb392d9c` | The branch does not fix the symptom: `SyntheticSolrCore.initRestManager` returns an uninitialized `RestManager`. Retire or re-aim is the owner's call |

## Landing order and interactions

- **SolrQueryParserBase cluster (4824, 9149, 11761, 12532):** no textual conflicts, including against current main. Order: 9149, 11761, 12532, 4824.
- **Grammar pair (12212, 17796):** order 17796, then 12212. The second branch needs a fresh gate after it lands. Both changes touch `QueryParser.jj`.
- **Join family:** 8977 and 9048 touch separate code and share `BJQParserTest` and the `requireFromAndTo` helper; order them by the owner's decisions below. 11391 is held.
- **Childfield pair (12871, 17311):** clean merge in both orders (merge-tree exit 0, no overlapping hunks).
- **Dismax pair (874, 6014):** 874 changes `SolrPluginUtils`; 6014 changes `DisMaxQParser`. Hold both until their fixes land.
- **Auto-fix thread:** 15906 changes `QParser.java`. If it lands, 8977's and 12212's framings and tests may change. Part q7 has the detail.
- **Collapse pair (16570, 17796):** test-file only; merges cleanly.

## Owner decisions

1. **SOLR-8977:** repair the traversal filter on every core, or document a workaround below `luceneMatchVersion` 10.2.0.
2. **SOLR-9048:** an all-stopword nested query matches everything (implemented) or nothing.
3. **SOLR-4824:** the parameter name for the draft's Choice.
4. **SOLR-11391:** a new key for the GraphTermsCollector problem, or drop the branch. Do not link SOLR-11391 to its Jira ticket.
5. **SOLR-12212 and SOLR-17796:** the negated-collapse question (17796), and the grammar fix plus a fresh gate (12212).
6. **SOLR-13202 (merged):** whether to correct the merged PR's description in a follow-up. Any public description edit needs your direction.
7. **SOLR-15615:** the fallback lookup's authorization, and the unmeasured fan-out cost.
8. **SOLR-15906:** pick the stray-paren option, then a focused gate on four test classes after the fixes.
9. **SOLR-17882:** retire, or re-aim the branch.
10. **SOLR-6014:** the fq path.
11. **SOLR-12608:** whether to pursue the premise at all. The draft claims only what the run covers.
12. **History (several branches):** squash the commits whose subjects carry handoff or gate words, including 874, 9149, 10897, 11761 and 12532. Squashing rewrites fork branches and needs your go-ahead.

## Not done

No build, test, Gradle run, fetch, commit, or post. Gate logs and premise logs named in the receipts are not on disk, so every count is receipt-only. Lucene 9.x and 10.x were not rechecked in this round; the 8977 and 4824 version claims need that check before any draft uses them.
