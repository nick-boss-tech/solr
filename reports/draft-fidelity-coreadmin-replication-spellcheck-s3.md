# Core admin draft fidelity, slice s3 (B3)

Assignment: `assignments/pool-draft-fidelity-coreadmin-replication-spellcheck.md`. Claim: `claims/pool-draft-fidelity-coreadmin-replication-spellcheck.md`, slice B3 (core-admin SOLR-17731, 18010, 4989, 6438, 8275). Tree: worktree HEAD d627304e96b (the task names this tree; the brief's claim commit e84522fa5bc was not used).

Sources: `reports/core-admin-round-1.md` and parts `-k1` to `-k6`; `reports/core-admin-answers-round-1.md` and parts `-c1`, `-c2`; `material/core-admin-round-1-answers.md` (owner decisions under the 2026-10-10 default). SOLR-18010 was held in round 1 and drafted later, so `reports/answers-and-receipts-round-2-r3.md` is also read for it. Receipts: `receipts/<ticket>.md`. Formula: `pr-formula.md`.

Live heads (`git ls-remote origin refs/heads/solr-<n>-submit`), each compared with the head the draft names:

- SOLR-17731: `f2b4ba164f565274286708d15989f1b1807b84c6`, matches the draft (`f2b4ba164f56`).
- SOLR-18010: `c3685bb37d9d74e4ed538d6d1b65bb1b60970677`, matches (`c3685bb37d9d`).
- SOLR-4989: `5bac95376cd488678290d45b8a7a4c9ed2f089cd`, matches.
- SOLR-6438: `8c77988d59175e0a59a32ef00dabffd3ca9a76fa`, matches.
- SOLR-8275: `e52e10fa50a374196d2c6adea26e5e55e37cdf57`, matches.

All five heads resolve in the worktree object store (`cat-file -t`).

Merge-bases used for pre-change links (`git merge-base <head> upstream/main`, with upstream/main at `3f5d4c5bf8ac` in this worktree; the older `8e62c2686882` gives the same result for 17731 and 6438): 17731 `e2cdb2d7e8ae`; 18010 `14c7aac0d151`; 4989 `97d973814336`; 6438 `14c7aac0d151`; 8275 `97d973814336`.

Citation rule applied (pr-formula.md, brief check 3): code the branch adds or edits links the head SHA. Pre-change symptom code, which the branch leaves alone, links the merge-base, and the text says so. Unchanged code cited as current behaviour in Limits or What this change does is listed as optional, not DRIFT.

Branch diffs against the merge-base (for the file-scope checks): 17731 touches 9 files; 18010 touches 4 (`SecurityConfHandlerLocal.java`, its test, `BasicAuthStandaloneTest.java`, changelog); 4989 touches 3; 6438 touches 3 (`MergeIndexes.java` and its test, changelog); 8275 touches 3 (`PrepRecoveryOp.java`, `TestPrepRecovery.java`, changelog). `RecoveryStrategy.java`, `MergeIndexesOp.java`, `DeleteAliasApi.java`, `CoreContainer.java` and `LukeRequestHandler.java` lines 235-238 and 215-218 are not edited by the branch in the cited regions.

## Verdicts

| Draft | Head checked | Verdict |
|---|---|---|
| SOLR-17731 | `f2b4ba164f56` (ls-remote match) | DRIFT (3 items) |
| SOLR-18010 | `c3685bb37d9d` (ls-remote match) | DRIFT (2 items) |
| SOLR-4989 | `5bac95376cd4` (ls-remote match) | DRIFT (1 item) |
| SOLR-6438 | `8c77988d5917` (ls-remote match) | DRIFT (1 item) |
| SOLR-8275 | `e52e10fa50a3` (ls-remote match) | DRIFT (1 item) |

## SOLR-17731

Verdict: DRIFT (3 items).

Checked and consistent: head; Proof counts (V2ResourcePathOverlapTest 2 of 2, both 405 on base, receipt); test behaviour (the test GETs the alias and checks `name` and `collections`, and POSTs a snapshot, lists it and deletes it, as the Proof says); the new-class and registration links (GetAliasByNameApi L33, CollectionsHandler L1207, CollectionSnapshotApis L37-L40 are head lines the branch produces); Choice (shared path versus router change, matches round 1 k6 NOTE 7 and the material's DISCUSS list item 11); Limits (other overlapping paths not searched; wire key `name` unchanged, matches k6 NOTE 6); INTERNAL block removed and Limits opener present (answers round 1, c1 and c2).

1. Draft says: "[OWED BEFORE POSTING: the branch needs its fixes first: the two license header lines, the two comments on the Jersey routing rule, and the changelog title. Then run `ListAliasesAPITest` at the new head. After that, update the head in the links and in this Proof.]"
   - Evidence: an internal process note in public text (brief check 7; pr-formula.md). The three branch fixes are still open at `f2b4ba164f56`: `solr/core/src/test/org/apache/solr/handler/admin/api/V2ResourcePathOverlapTest.java` line 16 reads ` */ package org.apache.solr.handler.admin.api;`; `CollectionSnapshotApis.java` lines 37-39 and `GetAliasByNameApi.java` lines 29-31 state the routing rule as fact (round 1 k6 FIX 3, FIX 4); the changelog title still says "reachable again" (item 3). No `ListAliasesAPITest` run is recorded for the new head (k6 FIX 2).
   - Replacement: delete line 25 entirely (no replacement text). Re-head the draft from the new branch tip after the fixes and the test run.

2. Draft says: "Each path is also the path of a DELETE endpoint: [CollectionSnapshotApis.java L58](https://github.com/nick-boss-tech/solr/blob/f2b4ba164f565274286708d15989f1b1807b84c6/solr/api/src/java/org/apache/solr/client/api/endpoint/CollectionSnapshotApis.java#L58) for snapshots and [DeleteAliasApi.java L28](https://github.com/nick-boss-tech/solr/blob/f2b4ba164f565274286708d15989f1b1807b84c6/solr/api/src/java/org/apache/solr/client/api/endpoint/DeleteAliasApi.java#L28) for aliases."
   - Evidence: both DELETE resources are the pre-change cause of the 405, and the text describes them as the before state. Neither is produced by the branch. At the base `e2cdb2d7e8ae`, the DELETE `@Path` is `CollectionSnapshotApis.java` L56 (`@Path("/collections/{collName}/snapshots/{snapshotName}")` above `interface Delete`); at head it is L58. `DeleteAliasApi.java` L28 (`@Path("/aliases/{aliasName}")`) is identical at base and head (file not in the branch diff).
   - Replacement: "Each path is also the path of a DELETE endpoint: [CollectionSnapshotApis.java L56](https://github.com/nick-boss-tech/solr/blob/e2cdb2d7e8ae0be4cf6cf0606206c67d89e7278f/solr/api/src/java/org/apache/solr/client/api/endpoint/CollectionSnapshotApis.java#L56) for snapshots and [DeleteAliasApi.java L28](https://github.com/nick-boss-tech/solr/blob/e2cdb2d7e8ae0be4cf6cf0606206c67d89e7278f/solr/api/src/java/org/apache/solr/client/api/endpoint/DeleteAliasApi.java#L28) for aliases. These two links point to the base commit e2cdb2d7e8ae, which is the code before this change. As the ticket describes, another resource answers with the 405, so the POST or GET handler never runs. The ticket shows both cases ([JIRA](https://issues.apache.org/jira/browse/SOLR-17731))."

3. Draft's changelog (`changelog/unreleased/SOLR-17731-v2-overlapping-resource-paths.yml`, linked at head) title says: "...are reachable again instead of returning HTTP 405 because a sibling resource with the same path claimed the request."
   - Evidence: the file exists at `f2b4ba164f56`, and its title is the title check target (brief check 4). The ticket says these APIs "exist in code but aren't accessible" (`research/jira-context/SOLR-17731.json`), so "again" claims a state that never existed. Round 1 k6 NOTE 5; answers round 1 branch correction 3.
   - Replacement (changelog lines 2-3, one folded line): "  The v2 APIs POST /collections/{name}/snapshots/{snapshotName} and GET /aliases/{aliasName} now answer instead of returning HTTP 405, because another resource with the same path was answering first."

Optional notes, not blocking:
- Choice says "The change is local to two API declarations." The branch also moves the alias GET handler from `ListAliases` into a new `GetAliasByName` class and re-registers it in `CollectionsHandler.java` (9 files in the diff). A plainer line: "The change is local to the two endpoint declarations and the alias GET handler."
- Proof "verified 2026-10-07" is the takeover-log record date in the receipt (the receipt names no run date). Acceptable as a record date.
- Plain language: "class-level path" and "router" are compressed. Fine for a JAX-RS reader.

## SOLR-18010

Verdict: DRIFT (2 items).

Checked and consistent: head (ls-remote match); Proof counts (SecurityConfHandlerTest 3 of 3: `testEdit` L55, `testConcurrentEditsToLocalSecurityJson` L203, `testConcurrentPersistConfLeavesOneWholeDocument` L271; BasicAuthStandaloneTest 1 of 1; V2SecurityAPIMappingTest 5 of 5, not in the diff), all matching the receipt; verification date 2026-10-05 (gate date in receipt); head links produced by the branch: `SecurityConfHandlerLocal.java` L44-L66 (edit lock, POST only) and L103-L147 (temp file, atomic move, fallback), `SecurityConfHandlerTest.java` L203-L262 and L271-L316, `BasicAuthStandaloneTest.java` L107-L124 (marker assertions at L118-L123); base links correct: `security.js` L1248-L1283 (loop, no wait), `SecurityConfHandler.java` L124 (`getSecurityConfig(true)`) and L151 (`persistConf`), `SecurityConfHandlerLocal.java` L86-L87 (`Files.newOutputStream` then write) at `14c7aac0d15`; no Choice section (round 1 answers and r3 NOTE 13 say none is owed); Limits match round 1 k2 finding 12 (standalone only, cloud mode untouched, one process); no em dashes, no internal vocabulary.

1. Draft says: "The write itself is not atomic. The file is opened for writing, which empties it, and the new content is written after that ([SecurityConfHandlerLocal.java](https://github.com/nick-boss-tech/solr/blob/14c7aac0d151402b00259e2fb9bf5eed7049ec5d/solr/core/src/java/org/apache/solr/handler/admin/SecurityConfHandlerLocal.java#L86-L87)). Two overlapping writes can leave the start of one document followed by the tail of another."
   - Evidence: the section's links (security.js, SecurityConfHandler.java, SecurityConfHandlerLocal.java) are pre-change code at the merge-base `14c7aac0d15`, which is the right SHA, but no sentence says so. pr-formula.md requires the text to say so.
   - Replacement: keep the paragraph and append this sentence at its end: " The links in this section point to the base commit, 14c7aac0d15, which is the code before this change."

2. Draft's changelog (`changelog/unreleased/SOLR-18010-security-json-concurrent-edits.yml`, linked at head) title says: "Standalone security.json edits are now serialized and written atomically, so concurrent edits (such as the Admin UI granting several permissions at once) can no longer corrupt the file or overwrite each other; the file also no longer carries the internal version metadata".
   - Evidence: the claim is wider than the code. The lock is per `SecurityConfHandlerLocal` instance (L44-L66), two Solr processes sharing one SOLR_HOME are not coordinated, and cloud mode uses `SecurityConfHandlerZk` (`CoreContainer.java` L851), which the branch does not touch. The draft's own Limits say so, so the title and the draft disagree. Round 2 part r3 FIX 8 gives this replacement. Round 1 answers hold this title until the settling run is read; r3 found that run is not on disk (r3 FIX 7). The main side must decide which governs. Either way the head moves after this edit, so the draft must be re-headed.
   - Replacement (changelog line 1): "title: Standalone security.json edits are serialized on each node and written atomically, so overlapping edits no longer lose each other's changes or leave a partly written file; the file also no longer stores the internal version marker"

Optional notes, not blocking:
- Limits links `CoreContainer.java#L851` at head. That file is not in the branch diff, so the same line at base is the convention for unchanged code: https://github.com/nick-boss-tech/solr/blob/14c7aac0d151402b00259e2fb9bf5eed7049ec5d/solr/core/src/java/org/apache/solr/core/CoreContainer.java#L851.
- The base failures of the two concurrent-edit tests are the receipt's record. The premise log (`g18010-premise.log`) is not on disk (r3 NOTE 9), so the draft's "fail on the base code" rests on the receipt. The draft does not quote a failure line, which is correct.
- The draft's Proof date (2026-10-05) is the gate date. The receipt also records a re-verification on 2026-10-10. Either date is supported.

## SOLR-4989

Verdict: DRIFT (1 item).

Checked and consistent: head (ls-remote match); Proof 10 of 10 and the new-test failure on base (receipt; the two new tests `testShowAllIncludesIndexFieldsAndSchema` L267 and `testShowIndexOmitsFieldsAndSchema` L276 exist at head); verification date 2026-10-06 (ledger date in the receipt); branch-produced links: `LukeRequestHandler.java` L239-L241 (the new `ALL` schema call) at head; Limits match round 1 k1 finding 17 and its task result, and the answers (BAD_REQUEST with a document id at head L215-L218, distributed path read not run, schema content unchanged); "index" section present at every style (`LukeRequestHandler.java` L199 at head); the 2018 comment exists in the ticket packet (`research/jira-context/SOLR-4989.json`, created and updated 2018-01-10); no Choice is owed (round 1 k1 and answers; the ticket's own suggestion); Limits opener present (answers c2). Round 1 said "needs a rebase before opening", and answers round 1 (adopted) said no rebase; the draft follows the answers. No em dashes, no internal vocabulary.

1. Draft says: "In [`handleRequestBody`](https://github.com/nick-boss-tech/solr/blob/5bac95376cd488678290d45b8a7a4c9ed2f089cd/solr/core/src/java/org/apache/solr/handler/admin/LukeRequestHandler.java#L235-L238), the `all` style falls into the same branch as the default, and that branch adds only the fields section."
   - Evidence: lines 235-238 are the pre-change symptom. They are identical at the base `97d973814336` (`} else if (ShowStyle.SCHEMA == style)` at 235, the `else` branch with the fields call at 237-238). The branch adds only L239-L241. The text does not say the link is the base.
   - Replacement (paragraph, replacing the whole paragraph that starts "The ticket asks for"): "The ticket asks for `show=all` to populate the index, fields and schema sections. In [`handleRequestBody`](https://github.com/nick-boss-tech/solr/blob/97d973814336101e12475558d7419321c743de79/solr/core/src/java/org/apache/solr/handler/admin/LukeRequestHandler.java#L235-L238), the `all` style falls into the same branch as the default, and that branch adds only the fields section. Those lines are at the base commit 97d973814336, the code before this change. A 2018 comment on the ticket reports that `show=all` still does not work."

Optional notes, not blocking:
- Limits cites `LukeRequestHandler.java` L215-L218 at head (the document branch, unchanged). Base lines are identical: 97d973814336 L215-L218.
- What this change does cites the existing merge at head L412-L415. Unchanged by the branch; the same code is at base L409-L412.
- Round 1 suggested PR title "SOLR-4989: show=all in LukeRequestHandler also returns the schema section" differs in wording from the changelog title. The draft carries no title line (answers round 1 item 8), so nothing in the draft conflicts.

## SOLR-6438

Verdict: DRIFT (1 item).

Checked and consistent: head (ls-remote match); Proof 4 of 4, 1 of 1, 44 of 44 and the base result "expected 400, got 500" (receipt); the test names and the three existing tests (`MergeIndexesTest` L65, L77, L90, L102 at head); the error text in the draft matches `MergeIndexes.java` L110-L112 at head exactly ("Only one of indexDir or srcCore can be specified, not both"); the check runs before any source opens (head L109-L113); branch-produced links (`MergeIndexes.java` L107-L113 at head); Choice (reject versus merge both; answers round 1 adopt "reject" and keep the Choice, round 1 k6 owner decision 6 is superseded by that answer); Limits (v1 has no test; clients must send one source; reference guide already presents them as alternatives, round 1 k6); INTERNAL block removed; Limits opener present; verification date 2026-10-05 (receipt). No em dashes, no internal vocabulary.

1. Draft says: "The v2 handler reads the source cores only when no directories are given ([MergeIndexes.java L114-L116](https://github.com/nick-boss-tech/solr/blob/8c77988d59175e0a59a32ef00dabffd3ca9a76fa/solr/core/src/java/org/apache/solr/handler/admin/api/MergeIndexes.java#L114-L116)). ... The v1 `mergeindexes` action builds the same request body ([MergeIndexesOp.java L39-L44](https://github.com/nick-boss-tech/solr/blob/8c77988d59175e0a59a32ef00dabffd3ca9a76fa/solr/core/src/java/org/apache/solr/handler/admin/MergeIndexesOp.java#L39-L44)), so it has the same gap."
   - Evidence: both are pre-change symptom code. The branch leaves `if (dirNames.isEmpty())` in place and adds its check above it (head L109-L113). The same block is at base `14c7aac0d15` L107-L109 (`if (dirNames.isEmpty()) {`, `var sources =`, the `Optional.ofNullable(requestBody.srcCores)` line). `MergeIndexesOp.java` is not in the branch diff, so L39-L44 are the same lines at base. The text does not say the links are base.
   - Replacement (paragraph, replacing the whole paragraph that starts "The v2 handler reads"): "The v2 handler reads the source cores only when no directories are given ([MergeIndexes.java L107-L109](https://github.com/nick-boss-tech/solr/blob/14c7aac0d151402b00259e2fb9bf5eed7049ec5d/solr/core/src/java/org/apache/solr/handler/admin/api/MergeIndexes.java#L107-L109)). When both are given, the directories are merged and the named cores are ignored. The request does not fail. The v1 `mergeindexes` action builds the same request body ([MergeIndexesOp.java L39-L44](https://github.com/nick-boss-tech/solr/blob/14c7aac0d151402b00259e2fb9bf5eed7049ec5d/solr/core/src/java/org/apache/solr/handler/admin/MergeIndexesOp.java#L39-L44)), so it has the same gap. These two links point to the base commit 14c7aac0d151, which is the code before this change. The ticket asks for an error at the least."

Optional notes, not blocking:
- The Proof says the new test "expected a 400 error and got a 500" on base. The test passes `indexDirs = ["some_dir"]` (MergeIndexesTest L80), a directory that does not exist, so the base 500 comes from the indexDir path opening that name. A reader may ask why; the receipt records the outcome only. Optional sentence: "On the base code the test's directory name does not exist, so the request fails on the indexDir path."
- "The request does not fail" is true for valid inputs. The test shows failure only because its names are fake.

## SOLR-8275

Verdict: DRIFT (1 item).

Checked and consistent: head (ls-remote match); Proof 3 of 3 and the base failure (receipt; `TestPrepRecovery.java` has three methods at head, `testTimeoutMessageNamesLastSeenState` at L90 asserts the replica name, "last seen" and "not in cluster state"); verification date 2026-10-06 (ledger date in receipt); branch-produced links in What this change does: `PrepRecoveryOp.java` L85 (`lastSeen`), L119-L123 (shard and replica missing), L166-L172 (state record), L224-L237 (timeout message) all match head; base message is `"Timeout waiting for collection state."` with `ErrorCode.SERVER_ERROR` (base `PrepRecoveryOp.java` L208-L209), and the head keeps the same error code, so "The error code and the HTTP status do not change" holds; the changelog title matches the code and the ticket's "recovering" contradiction (ticket packet `SOLR-8275.json`); Limits match round 1 k6 (receipt's three lines) and answers round 1 (no Choice owed, draft stands); the untested variants named in Limits are real (the shard-not-in-cluster text at L119 and the `onlyIfLeaderActive` suffix at L173-L174); INTERNAL block removed and Limits opener present. No em dashes, no internal vocabulary.

1. Draft says: "A replica asks its leader to wait until the replica reaches a given state ([RecoveryStrategy.java L959-L989](https://github.com/nick-boss-tech/solr/blob/e52e10fa50a374196d2c6adea26e5e55e37cdf57/solr/core/src/java/org/apache/solr/cloud/RecoveryStrategy.java#L959-L989))."
   - Evidence: the range misses the code. `RecoveryStrategy.java` has 945 lines at head `e52e10fa50a3` and at base `97d973814336`, so L959-L989 do not exist. The wait request is built in `sendPrepRecoveryCmd` at L907-L921 (state RECOVERING at L913, checkLive at L914, onlyIfLeader at L915, onlyIfLeaderActive at L920). The file is unchanged between base and head (empty diff), so the same lines are at the base. The round 1 report part k6 (SOLR-8275 task result, "lines 696 and 959-989") carries the same wrong range; line 696 is a PeerSync log line. The draft inherited it.
   - Replacement (paragraph, replacing the whole paragraph that starts "A replica asks its leader"): "A replica asks its leader to wait until the replica reaches a given state ([RecoveryStrategy.java L907-L921](https://github.com/nick-boss-tech/solr/blob/97d973814336101e12475558d7419321c743de79/solr/core/src/java/org/apache/solr/cloud/RecoveryStrategy.java#L907-L921)). That code is at the base commit 97d973814336, the code before this change. When that wait times out, the message names neither the replica nor the state the leader last saw. The message in the ticket says the node was asked to wait for "recovering" and then says it sees "recovering," which reads as a contradiction ([JIRA](https://issues.apache.org/jira/browse/SOLR-8275))."

Optional notes, not blocking:
- "the checkLive flag" in What this change does and "onlyIfLeaderActive" in Limits are compressed. Plainer: "the checkLive setting (whether the replica must be live)" and "the onlyIfLeaderActive setting".
- Round 1 k6 (`reports/core-admin-round-1-k6.md`, SOLR-8275 paragraph) carries the same wrong range; the lead may want that corrected.

## Not done

- No build, Gradle, test, BATS or gate run; no `test-queue` command. Proof counts and base failure lines are checked against the receipts only. The gate and premise logs named in the receipts (`g6438-premise.log`, `g8275-premise.log`, `g18010-premise.log`, `g4989` and `g17731` logs) are not on disk, so no count was re-run.
- Changelog YAML was read as text. No YAML parser was run, so the parse is by inspection.
- Trial merges and a drift check against current upstream main were not run. Merge-bases used the worktree's `upstream/main` (`3f5d4c5bf8ac`) and were checked against `8e62c2686882`.
- No `gh` call. Live PR state was not read (none of the five is named as a live PR in round 1). Only ls-remote heads were checked.
- JIRA: the hydrated packets in `research/jira-context/` were read for 4989, 8275, 17731 (specific lines) and 6438 (summary and description). The 18010 packet was not read; the draft cites the Admin UI code, not the ticket.
- The 17731 branch fixes (header, comments, changelog title, `ListAliasesAPITest` run) were checked only at the level needed for the draft's hold; the fix content is not re-audited here.
- Claim file not marked DONE and nothing committed or pushed, per the brief.
