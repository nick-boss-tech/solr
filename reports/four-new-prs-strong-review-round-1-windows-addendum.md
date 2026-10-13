# Strong review of the four new draft PRs, round 1: Windows host addendum

**Read `reports/four-new-prs-strong-review-round-1.md` first.** That is the canonical report for this assignment. Its claim (`claims/pool-pr-prepare-tip-39efc73-round-1.md`, pushed at `46ff7895f10`) landed before mine, so under first-push-wins mine stands down. I did not check the claims folder before starting, and the two reviews ran at the same time without knowing about each other. This file is the second, independent review of the same four PRs, kept whole instead of discarded, because it adds work the canonical report does not have: three patches on review branches with gate jobs, two settling-run jobs, test-by-test tables, and edits that were applied to the live PR bodies.

The two reviews agree on every verdict: #5116 SATISFIED; #5115, #5117 and #5118 NOT SATISFIED. They agree on the main findings: the exactness flag next to a skipped shard, the missing-entry crash in #5117 as a separate follow-up, keeping the union for `maxHitsTerminatedEarly`, the permission-copy log level, the undisclosed file replacement in #5118, and the 0644 fail-before value.

## Where the two reviews differ

| Point | Canonical report | This addendum | What settles it |
|-------|------------------|---------------|-----------------|
| Exactness rule for a skipped shard (#5115) | Option B: clear when the skipped shard reported hits **or an inexact count**; a skipped shard with an **exact zero** leaves the flag alone. | Clear on **any** skipped shard. `SearchHandler.shortCircuitedResults` sends an empty list (numFound 0, exact by default) for a shard that stopped before collecting anything, so a zero with an exact flag is the common timeout shape and carries no information. | `gates/queryComponent-real-exit-shape-premise.md` (plain `QueryComponent`, not yet run). The combined component's run already shows the analogous shape. |
| Exactness rule (#5117) | Clear on `docs == null`, hits, or an inexact count. | Clear on any skipped shard. | Same job, plus `gates/SOLR-18541-score-only-exit-input.md` from the canonical report. |
| What a patch is | Described, not written. | Written and pushed on three review branches, with gate jobs. | Gates. |
| #5118 `0644` | Blocking: the value cannot come from the code; gate G1. | Same observation (stated as a contradiction in the body); the Choice no longer repeats the number. The Proof line that carries it is unchanged and, per the canonical report, must not ship as written. | `gates/SOLR-18010-permissions-fail-before-and-multiauth.md` G1. |
| #5118 `MultiAuthPluginTest` | Found: runs the changed write path on every edit and is not in the focused run (G2). | **Missed.** Adopt G2. | G2. |
| Live bodies | Not edited (its round forbade it). | Edited and read back (see below). | Owner. |

If the settling run for `QueryComponent` shows that a real early-exit shard reports a non-zero hit count, the canonical report's Option B is enough and my patch is wider than needed (the zero-hit test flips back). If it shows zero hits with an exact flag, Option B misses the common shape.

## What I changed on GitHub

Four live PR bodies were edited with the REST endpoint (`gh pr edit` fails on this token's scopes) and each was read back and compared with the draft on this branch. The four edits were small: #5115 one Proof sentence; #5116 three sentences; #5117 the skip sentence, a Choice sentence and the Limits paragraph; #5118 the marker explanation, the Choice section and Limits. After the merge of the two reviews' draft edits, the live bodies of #5115, #5116 and #5118 were synced again from the merged drafts (the canonical report's draft edits had not reached the live bodies). One deviation from the canonical report's holds: the #5118 Limits now carries a hedged bullet on the symlink and bind-mount cases ("Neither case was run"), which its E11 would have held until the settling runs.

The rest of this file is the review as I wrote it. Where it says "report" for the round 1 report of this assignment, read "this addendum"; where it quotes verdicts, they match the canonical report.

Assignment: `assignments/pool-four-new-prs-strong-review-round-1.md`. Claim: `claims/pool-four-new-prs-strong-review-round-1.md`. Reviewer: windows host (Claude Code), 2026-10-13. Read-only against the code at the stated heads; no build and no Gradle ran on this host.

## Verdicts

| PR | Ticket | Live head | Verdict |
|----|--------|-----------|---------|
| #5115 | SOLR-18539 mergeIds skipped-shard totals | `4eb996121804` | **NOT SATISFIED.** Blocking: the exactness rule is wrong next to a skipped shard. Patch written, waiting for a Linux gate. |
| #5116 | SOLR-18540 gradlew under Git Bash | `58e07b4571fb` | **SATISFIED.** Three wording fixes applied. No code finding. |
| #5117 | SOLR-18541 CombinedQueryComponent merge totals | `d5ed34125db9` (side branch `fcd92f23ba65`) | **NOT SATISFIED.** Blocking: same exactness rule, plus a body claim about a path that cannot run. Patch written, waiting for a Linux gate. |
| #5118 | SOLR-18010 standalone security.json | `2b5ad801c9e8` | **NOT SATISFIED.** Blocking: replace semantics of the temp-file move are unsettled for symlinked and bind-mounted files; a settling run is posted. Smaller patch waiting for a Linux gate. |

Each NOT SATISFIED has a named way to reach SATISFIED in round 2 (see "Round 2" at the end). Nothing was posted to GitHub as a comment, review or state change. Four live PR bodies were edited (REST `PATCH`), each edit read back and compared with the draft on this branch.

## What the first pass missed, in one place

1. The exactness flag. #5115 and #5117 both change which shards feed `numFound`, and neither rule fully tracks it. A skipped shard that reported hits next to a merged shard leaves `numFoundExact` true although `numFound` now omits known hits. That is a new false claim, not a pre-existing one (on the base, `numFound` included those hits). The shard shapes a real timeout produces make it worse: they report zero hits or no count at all (sections below).
2. #5117: the body says a shard with no per-query entry adds 0. That code cannot run; the document loop dereferences the missing list right after.
3. #5117: the exactness fix turns an all-exact multi-query response from false to true for a `numFound` that can be below the number of documents returned. The body did not say so.
4. #5118: the body said the new file is created owner-only. The run it cites observed 0644, so the claim and the evidence disagree.
5. #5118: nothing tested the failure path that the posted offer comment advertises ("a failed write keeps the old file"), and nothing says what the move does to a symlink or a bind mount.

---

## SOLR-18539, PR #5115, mergeIds skipped-shard totals

Branch `mergeids-skipped-shard-totals`, head `4eb996121804b043f1ee53f5bb7e22b9c3021b0a`, merge-base `d76fc14b9857`.

### Claims verified, and how

- **Skip before every fold.** Read `QueryComponent.mergeIds` at head. The skip is at lines 1180 to 1185. The folds that follow are the approximate total (1188 to 1193), maxScore (1196 to 1198), numFound (1199) and the exactness fold (1201 to 1203). Nothing that feeds the merged totals sits above the skip. The folds that stay above it are `thereArePartialResults` (1144), `segmentTerminatedEarly` (1146 to 1154) and `maxHitsTerminatedEarly` (1156 to 1161), and the body says so.
- **Fallback.** Base line 1162 added `numFound`, the running count before the current shard was folded (the base folds `numFound` at 1171, after the fallback). Head adds `docs.getNumFound()`. Each iteration adds one shard's own count and nothing else touches the accumulator, so there is no double count across iterations.
- **Every line citation in the body.** Checked against the files at both commits: base L1160-L1175 and L1193-L1195; head L1180-L1203, L1188-L1193, L1238-L1243, L1241-L1243, L1144, L1156-L1161. All correct.
- **Numbers against the receipt.** 17 of 17 at `4eb996121804` and at `9e85ce5c16`; 7 of 17 fail on the base, the same seven at both heads; the dates and UTC offsets convert correctly (02:51 UTC is 20:51 MDT the day before). The two receipt entries behind "13 instead of 14" and "5 instead of 0" match.
- **The skip history.** The skip in its current form came from SOLR-15595 (`812de21e75d`, 2021), which rewrote an older `continue` (commented "fsv timeout yields empty sort_vlaues") and never mentions totals. Nothing in that commit shows that counting a skipped shard's totals was intended.
- **Live body equals the draft** (minus the staging notes) before my edit.

### Test audit: do the 17 tests pin what the body says?

| # | Test | On the base | What it pins |
|---|------|-------------|--------------|
| 1 to 3 | `includesPartialShardResult...` (implicit score, explicit score, doc sort) | passes | A partial shard is merged when no unmarshalling is needed. Existing tests. |
| 4 | `excludesPartialShardResultWhenUsingNonScoreOrDocSortField` | passes | The skip, by document count. Existing test. |
| 5 | `skippedPartialShardDocsAreAbsent` | passes | Same as 4 on the new fixture. Redundant, harmless. |
| 6 | `mergedNumFoundCountsOnlyMergedShards` | fails (5, expected 3) | numFound. Its exactness assertion is never reached on the base. At head it pins `numFoundExact` true next to a skipped shard that reported hits, which is the asymmetry in finding F1. |
| 7 | `mergedMaxScoreComesFromMergedShards` | fails (99.0, expected 3.0) | maxScore. |
| 8 | `mergedApproximateTotalHitsExcludesSkippedShard` | fails (11, expected 4) | Header-value path. Header 4 against numFound 3, so a sum of numFound fails it. |
| 9 | `approximateTotalHitsFallsBackToEachShardsNumFound` | fails (2, expected 5) | Fallback. Weak: both shards return as many documents as their numFound, so a fallback that used `docs.size()` passes. See F4. |
| 10 | `skippedShardNumFoundExactDoesNotFoldIntoMergedFlag` | fails on numFound (5, expected 3) | At head it kills the mutant "exactness fold left above the skip". On the base the exactness assertion is never reached, so the receipt's failure text is the numFound one. Pins the same asymmetry as 6. |
| 11 | `allShardsSkippedDoesNotClaimExactZero` | fails (5, expected 0) | The all-skipped guard, with hits. |
| 12 | `approximateTotalHitsMixesHeaderAndFallbackShards` | fails (13, expected 14) | Mixed headers. Same `docs.size()` weakness as 9. |
| 13, 16 | `mergedMaxScoreIsTheMaximum...` (two orderings) | passes | Max, not first or last. Existing behavior. |
| 14 | `mergedShardInexactCountMakesMergedFlagInexact` | passes | The exactness fold for merged shards. |
| 15 | `mergedPartialShardTotalsAreCounted` | passes | A partial shard that is merged (score sort) counts in full. Guards against skipping too much. |
| 17 | `allShardsSkippedWithNoHitsKeepsExactZero` | passes | The `hits > 0` condition of the guard. Wrong under finding F1 (below). |

The seven base failures are tests 6 to 12, which matches the receipt. The ten that pass on the base are 1 to 5 and 13 to 17, which matches the body.

Scope of the pinned behavior: only the skip when sort values are absent. The body states that scope plainly ("a shard is skipped only when its response is partial, it sent no sort values, and the sort uses a field other than score or doc") and does not imply more. Two things no test can reach, because `MockShardRequest` never supplies `sort_values`: a partial shard that has sort values and a field sort (merged and counted), and the interaction with `shards.info`. Both are noted, neither is blocking.

### Adversarial cases tried

- **Skip when all shards are skipped, none, and some.** Code survives. Zero merged shards leaves `maxScore` null, `numFound` 0, no approximate header (value 0, not written). One merged shard: totals are that shard's alone.
- **Skipped shard carries `maxHitsTerminatedEarly`.** The flag still folds (line 1156), so the response header says early termination while `approximateTotalHits` is the sum over merged shards only. Coherent enough, and the body says the flag folds from every shard. Left as it is.
- **Error shard, null `docs`, null header.** These `continue` earlier and leave `anyShardMerged` false with no skipped hits, so the exactness flag keeps its base behavior. Unchanged.
- **Duplicate ids across shards.** `ShardDocQueue.push` returns false only for a duplicate id (`insertWithOverflow` always returns true), so `numFound--` stays a duplicate adjustment. A skipped shard pushes nothing. Unchanged.
- **Shard timeout shapes.** `SearchHandler.shortCircuitedResults` (line 779) sends `new SolrDocumentList()` when a shard stopped before collecting anything: numFound 0 and `numFoundExact` true (the default, `SolrDocumentList` line 50). No `sort_values` follow. A shard that timed out during sort value preparation keeps the documents it found and sends an empty `sort_values` (`doFieldSortValues`, rethrown after adding the empty list). So both shapes reach the skip, and the first one reports **zero hits with an exact flag**. A rule that reads reported hits cannot see it. I did not run this on a plain `QueryComponent`; a settling run is posted (`gates/queryComponent-real-exit-shape-premise.md`). The combined component's run does show the analogous shape (combined response numFound 0).

### Findings

| ID | Severity | Finding | Applied |
|----|----------|---------|---------|
| F1 | **High, blocking** | `numFoundExact` stays true when a skipped shard reported hits and another shard was merged. `numFound` now omits hits the merge knows about, so the count is a lower bound and the flag claims otherwise. On the base `numFound` included those hits, so this is a new false claim. The rule `anyShardMerged == false && skippedShardReportedHits` also cannot see the common timeout shape (zero hits, exact). | Patch on `review-r1-solr-18539` (`fbf7610644d1`): any skipped shard clears the flag. Gate job `gates/SOLR-18539-review-r1-patch.md`. Not on the PR branch until green. |
| F2 | Low | Body: "Five are the values under What happens today" counted tests, while that section lists four values. | Applied to the live body and the draft: "Five fail on the values under What happens today (two of them on numFound 5 instead of 3)." |
| F3 | Low | The patch above changes the expected assertions of tests 6, 10, 11 and 17. Test 17 flipped by design: a zero reported by a shard that stopped early is where it stopped, not a count. | In the patch. |
| F4 | Medium | Tests 9 and 12 cannot tell `docs.getNumFound()` from `docs.size()` in the fallback. | In the patch: the second shard in test 9 now has numFound 8 with 3 documents. |
| F5 | Low | Mocks never supply `sort_values`, so a partial shard with sort values (merged, counted) is not covered. | Named here; not fixable without extending the mocks. |

### Position on the open question

The body asks whether `numFoundExact` should also go false when a skipped shard reported hits and other shards were merged. **Yes, and the rule should be wider: any skipped shard.** The skipped shard's hits are not in the count, and a shard that stopped early reports where it stopped, so a reported zero or a reported exact flag carries no information. Cost: only partial responses change, and they already carry `partialResults`. This also makes #5115 and #5117 agree (the #5117 side branch already clears on hits).

### Verdict

**NOT SATISFIED.** Blocking: F1. Reaching SATISFIED: the patched head gated green by a Linux host, the PR branch moved to it, and the body's Proof, Choice and Behavior-change passages rewritten against the new receipt. The Choice section then drops its question (the answer is in the code) or keeps one about whether the flag should also depend on the skipped shard's own count. The text to change is listed under "Round 2".

---

## SOLR-18540, PR #5116, gradlew under Git Bash

Branch `gradlew-msys-double-conversion`, head `58e07b4571fbbfc83c14bdd28283cd573acb0d7a`, merge-base `d76fc14b9857`.

### Claims verified, and how

- **Diff.** Three lines deleted (the second `APP_HOME` and `CLASSPATH` conversion and the blank line after them), plus the changelog fragment. The citations are right: line 159 (first conversion), lines 216 to 218 at the merge-base, lines 215 to 216 at the head.
- **Nothing reassigns `APP_HOME` or `CLASSPATH` between the first conversion and the block.** Read the script top to bottom: the first conversion (158 to 160) is followed only by the download check (162 to 172), `CLASSPATH=$GRADLE_WRAPPER_JAR` (174) and the properties generator (178 to 184). So at the block both are already in Windows form, and deleting the second conversion cannot leave a POSIX path behind.
- **The run base is identical to the merge-base.** The tree Nick ran (`498a09aea97`) has no difference in `gradlew` from `d76fc14b9857` (the diffstat between the two lists no `gradlew` entry). The run record states the two trees differ only at the three deleted lines.
- **Run record.** `reports/gradlew-end-to-end-run.md` and the receipt agree with the body: `C;C:/...` on the base with exit 1 and "Could not find or load main class", Gradle 9.7.0 and exit 0 on the fix tree, Linux `-version` exit 0, `bash -n` clean.
- **Claimed verification stays inside what was run.** The body says wrapper startup only, Git Bash/MSYS plus Linux, no `check`/`build`/test task, no Cygwin, no `gradlew.bat`, no macOS. Nothing implies a full build. The title is the Jira summary, as for the other PRs.

### Adversarial cases tried

- **Paths with spaces.** Both conversions quote their argument and every later use is quoted (`"$APP_HOME/..."`, `-classpath "$CLASSPATH"`). Removing a conversion cannot add a word split. Safe by reading; not run.
- **`CLASSPATH` already set in the environment.** Line 118 and line 174 assign it unconditionally, so a preset value is overwritten before the block, with or without the conversion. Safe.
- **Cygwin versus MSYS versus Git Bash.** `uname` maps `CYGWIN*` to `cygwin` and `MSYS*|MINGW*` to `msys`; Git Bash is MINGW, so `msys`. The first conversion and the block are both guarded by `cygwin || msys`, so the same flags that ran the first conversion run the block; no shell enters the block without having run the first conversion. Whether a second `cygpath --path --mixed` on `C:/...` is harmless under real Cygwin is not known and does not matter: the second conversion is gone.
- **A multi-element `CLASSPATH`.** `--path` would matter for a list, but the script only ever puts one jar there.
- **`JAVACMD`, the argument loop.** Untouched.

### Comparison with the Gradle wrapper script

The current Gradle start script template converts `APP_HOME` inside this block and has no earlier conversion; in generated wrappers that conversion is the only one. Solr's script differs: LUCENE-9266 added an early conversion so the wrapper jar download and the properties generator can run Java with Windows paths. That is why Solr has two conversions and the template has one. The deletion keeps the early conversion, which those steps need, and removes the one the template alone would have owned. It does not follow the template's placement, and it does not need to: the result is one conversion in both cases. Lucene's `main` `gradlew` takes a third shape (no `CLASSPATH` at all, `-jar` at exec). I fetched both through the web fetch tool, which summarizes the page; treat the exact line numbers of the Lucene script as unconfirmed. The body now says why the early conversion stays.

### Findings

| ID | Severity | Finding | Applied |
|----|----------|---------|---------|
| G1 | Low | "The main run failed" read as the main run of something. | Applied (live + draft): "The run on main failed". |
| G2 | Low | A reviewer would ask why the first conversion was kept rather than the second. | Applied: one sentence with a link to the head's lines 158 to 183. |
| G3 | Low | The argument-conversion loop in the same block did not convert anything on `-version`. The run record says so; the body did not. | Applied: one Limits bullet. |

No code finding.

### Verdict

**SATISFIED.** Residual risk is named in the body: Cygwin not run, no full build. Both are reasonable for a three-line deletion that cannot change a path no shell would reach.

---

## SOLR-18541, PR #5117, CombinedQueryComponent merge totals

Branch `combinedquerycomponent-merge-totals`, head `d5ed34125db9582b584bbfdd149a4de4b40650d0`; merge-base `597ed67740831566b708b793f52554b2e49e7e17` (`CombinedQueryComponent.java` is identical to the premise base `d76fc14b9857`). Side branch `solr-18541-combined-query-merge-totals`, head `fcd92f23ba65f304ee542c6fe0fec73d5d9e8ce2`.

### Claims verified, and how

- **Three folds and the skip placement, at both heads.** Merge-base: numFound fold 342, exactness fold 343, approximate block 381 to 387, skip 399 to 400. PR head: skip 389 to 393, then the totals (395 to 408). All body citations are correct, including 389 to 408 at the PR head. The side branch changes one place: lines 391 to 396 gain the exactness clear.
  - Fold 1 (skip before totals): at head nothing feeds `queryNumFound`, `queryApproximateTotalHits` or `hitCountIsExact` above the skip.
  - Fold 2 (fallback): `queryApproximateTotalHits += docs != null ? docs.getNumFound() : 0`, the shard's own per-query count.
  - Fold 3 (exactness): `if (hitCountIsExact && Boolean.FALSE.equals(...)) hitCountIsExact = false`, the `QueryComponent` form. The base wrote `hitCountIsExact && Boolean.FALSE.equals(...)`, inverted in both directions.
- **Numbers.** The seven base failures in the receipt (numFound 7 against 3; approximate 15 against 5, 7 against 5 twice, 21 against 11; the two inverted exactness assertions) are the seven the body lists. Seed `C0FFEE42C0FFEE42`, gate end 2026-10-13T05:10:53Z, which is 2026-10-12 23:10 MDT, matches "Verified 2026-10-12".
- **Fidelity of the seven tests to the probe** (`material/combinedquery-probe-export/`): every converted test asserts the value the probe recorded. F (5, 5), P (3, 5, 3 documents), P2 (5), X1 and X2 (true and false), M1 (11, 11) and M2 (5, 5) match line for line, including the header values (10 and 5) and the per-query lists. The two maximum tests state the current rule honestly (their comment says they pin it and do not settle it). Two fidelity notes, below.
- **Maximum-fold behavior** (premise report, runs M1 and M2): the maximum passes the winning query's total through, masks the other's, and never amplifies. Verified from the XML summary in the export.
- **Live body equals the draft** before my edit.

### The side branch exactness commit, on its merits

Condition at `fcd92f23ba65`: clear `hitCountIsExact` when a skipped shard has `docs != null && docs.getNumFound() > 0`. Case analysis:

| Case | Merged count | Correct flag | Side rule |
|------|--------------|--------------|-----------|
| No skipped shard | complete | exact (per shard flags) | unchanged, correct |
| Skipped shard reported hits | omits known hits | inexact | clears, correct |
| Skipped shard reported zero hits (an empty result from a shard that stopped early) | omits unknown hits | inexact | does not clear, **wrong** |
| Skipped shard sent no per-query entry (`docs == null`; the real exit above query 0) | omits unknown hits | inexact | does not clear, **wrong** |

The last two rows are the shapes real exits produce (run, premise report real-exit addendum, Q1: zero per-query entries, combined response numFound 0). The hits-only rule can only fire on a constructed shape, the same shape the body says real exits do not produce. **The correct rule is: any skipped shard clears the flag.** Patch on `review-r1-solr-18541` (`a93f9f6a997e`).

### The open maximum-fold question

The assignment names the `maxHitsTerminatedEarly` flag; the body's Choice is about `numFound` (largest single sub-query count against the distinct documents). Both are taken up.

**`maxHitsTerminatedEarly`: keep the union of the shards' flags.** Evidence: (a) `SolrQueryResponse` documents the flag as "the search was terminated early", and presence also implies `partialResults`; (b) the flag is written once and never reset (CQC lines 372 to 377), across every query and shard; (c) the only consumer of `approximateTotalHits` outside the response writer is `SpellCheckCollator` (line 164), which prefers the header value over the logged hits; it needs the header only when some shard stopped early, which is exactly when the flag is set; (d) the maximum run (M1, M2) shows that `approximateTotalHits` is the winning query's own total, so a winning-shard flag would let a losing sub-query's truncation go unreported even though RRF fuses documents from every sub-query. The union never hides a truncation. Cost: the header can say "terminated early" next to an approximate total that is the winner's exact sum; that is an overstatement of uncertainty, the safe direction.

**`numFound` maximum.** The body keeps the maximum and asks. My reading of the existing intent report (`reports/combinedquerycomponent-maximum-fold-intent.md`) holds: the repository settles nothing; `simpleCombine` labels its maximum a lower bound; the guide defines an inexact `numFound` as a lower bound on the real hits. That produces the next finding.

### Findings

| ID | Severity | Finding | Applied |
|----|----------|---------|---------|
| C1 | **High, blocking** | Exactness rule on a skipped shard is hits-only. Wrong for the zero-hit and the no-entry shapes (table above). | Patch on `review-r1-solr-18541` (`a93f9f6a997e`): any skipped shard clears the flag; two tests for the two missing shapes; changelog sentence. Gate job `gates/SOLR-18541-review-r1-patch.md`. |
| C2 | Medium | Body: "A shard with no approximateTotalHits header adds its own per-query count, and 0 when it sent no per-query entry." The `0` branch is dead: a shard with no per-query entry reaches the document loop (CQC line 418, `docs.size()`), which dereferences `null`. The sentence describes behavior that throws instead. | Applied (live + draft): the clause is removed. The null guards at lines 395 and 405 stay in the code; they are harmless. |
| C3 | Medium | Limits understate a confirmed crash. The body calls the unchecked per-query list access a "nearby gap"; the real-exit run confirmed it throws `IndexOutOfBoundsException` ("Index 1 out of bounds for length 1") for an exit above query 0, and the body's "code reading says" sentence predates the run. | Applied: Limits rewritten from the run, with a link to lines 338 to 340 at the head; says the run was on the code before the change and not repeated at this head; offers a follow-up on request. |
| C4 | Medium | The exactness fix turns an all-exact multi-query response from false to true, for a `numFound` that can be below the documents returned (the body's own example: numFound 2, four documents). The body did not say it. | Applied: one sentence in the Choice section. The behavior itself is an owner item (below). |
| C5 | Medium | The body says a skipped shard contributes nothing to "exactness". No test at the PR head pins that: the skipped shard in `skippedPartialShardContributesNothing` is exact and asserts nothing about the flag, so leaving the exactness fold above the skip passes all seven tests. | Closed by the side branch, which asserts the flag in that test. After the patch the claim is replaced by the any-skipped rule and tested by four tests. |
| C6 | Low | `skippedPartialShardApproximateTotalCounted` asserts the total is not counted. | Renamed `skippedPartialShardApproximateTotalExcluded` in the patch. |
| C7 | Low | A skipped shard still feeds `partialResults`, `segmentTerminatedEarly` and `maxHitsTerminatedEarly` (CQC 362 to 377). #5115 says so; #5117 did not. | Applied: one clause in "What this change does". |
| C8 | Low | `numFound--` at line 441 lowers the running maximum, not the query's own total, when a duplicate id is dropped. Pre-existing, flagged in the premise report as needing a run, and not in the body's Limits. Only duplicate ids across shards trigger it. | Named here; not added to the body (length). Fold into the follow-up ticket below if Nick wants one. |

### The real-exit crash: where does it belong?

Position: **a follow-up, named in Limits, with its own ticket if Nick files one; not in this PR.** Reasons: (1) it is a crash, not a wrong total, with its own symptom, its own test shape (a shorter per-query list) and its own risk; (2) it sits in lines this PR does not touch, so folding it in widens the diff and the review; (3) the fix is tiny and independent (check the list size before `get`), so nothing is lost by landing it separately; (4) the same lines also hide the unproven score-only NPE, so one follow-up ticket covers "the merge tolerates a shard's missing per-query entries" end to end (Q2 by run, Q3 unsettled). It should not be dropped: with it unfixed, a real exit above the first sub-query throws before any of this PR's totals logic runs, so the PR's behavior is only exercised by real shards that exit during the first sub-query. The body says this now. Filing the ticket is Nick's call (WORKFLOW.md: no new Jira without his direction).

### Two fidelity notes for the tests

- The probe's P test asserted `numFound`, approximate and size in one test, and P2 repeated the setup for the approximate value only, so each assertion would fail independently on the base. The PR keeps both. That explains the duplication; the rename (C6) fixes the misleading name.
- `largestSubQueryTotalWins...` pass the numFound-above-document-count case (`docList(10, true, a3)`), so they distinguish `getNumFound()` from `size()`. The fallback test in the same class does not, but the maximum tests cover it.

### Verdict

**NOT SATISFIED.** Blocking: C1 (patch written, waiting for the Linux gate) and the decision to put the side branch onto the PR branch, with the body rewritten to match. The owner item on multi-query exactness (below) is not blocking, because the body discloses it now.

---

## SOLR-18010, PR #5118, standalone security.json edits

Branch `solr-18010-submit`, head `2b5ad801c9e88807337f4f0a3fdd689126ad37d1`, merge-base `14c7aac0d151`. The live head equals the head in the assignment. Reviewed with `reports/solr-18010-overlap-comparison-round-1.md`, `reports/solr-18010-perms-body-round-1.md` and the receipt.

### Claims verified, and how

- **Numbers.** SecurityConfHandlerTest 4 of 4, BasicAuthStandaloneTest 1 of 1, V2SecurityAPIMappingTest 5 of 5, head `2b5ad801c9e` and "verified 2026-10-13": all in the receipt's permission re-gate section, from fresh JUnit XML. The fail-before text "0644 observed where 0640 was expected" is in the receipt. The base-failure descriptions for the two concurrency tests rest on `g18010-premise.log`, which the overlap report found missing on disk; the receipt is the only local source for them.
- **Lock scope.** The lock is `synchronized (editLock)` in `SecurityConfHandlerLocal.handleRequestBody` for POST only, using the same test (`httpMethod` is the enum `SolrRequest.METHOD`, compared with `.equals`) that the parent uses to decide an edit. So the lock covers exactly the code path that edits. `CoreContainer` builds one handler instance (`CoreContainer.java:857` to `858`) and registers it for both `/admin/authentication` and `/admin/authorization` (lines 911 and 914); the v2 `Modify*SecurityConfigAPI` classes call `handleRequestBody` on that same instance. Both entry points share the lock.
  - Serialized: the whole read, edit and write of `doEdit`, including its three-attempt loop, and `securityConfEdited()`.
  - Not serialized: reads (GET), which now see an atomic file; a second Solr process on the same SOLR_HOME; a second `CoreContainer` in one JVM (tests, embedded); command-line tools that write the file. The body names the first two; the others are in the same category.
  - Held while the request body is read (`ApiBag.readCommands` runs inside `doEdit`). A slow client with edit permission can stall other edits. Low; now named in Limits.
- **Atomic write.** Temp file via `Files.createTempFile` in the same directory, `Files.write`, optional permission copy, `Files.move(ATOMIC_MOVE, REPLACE_EXISTING)` with a plain-move fallback on `AtomicMoveNotSupportedException` only, `deleteIfExists` in `finally`. A reader sees the old or the new file on a file system with atomic rename. A failed serialization or write leaves the old file; a process crash leaves the old file plus a stray temp file.
- **Marker strip.** Standalone mode adds `"": {"v": N}` to each plugin config on every edit (`SecurityConfHandler.doEdit`, line 147), always 0 because `SecurityConfHandlerLocal.getSecurityConfig` never sets a version. `CoreContainer.readVersion` reads it only to skip a reload when it is above 0, which never holds in standalone. So removing it changes no reload behavior. The ticket's own sample shows that key in the corrupted file; the body now ties the strip to it.
- **Live body equals the draft** before my edit.

### Adversarial cases tried

| Case | Outcome |
|------|---------|
| Two overlapping edits | Serialized; both survive. Tested. |
| Two overlapping `persistConf` calls without the lock | Each writes its own temp file; last move wins; the file is one whole document. Tested. |
| Failure while serializing, after the temp file exists | Old file kept; temp deleted by `finally`. **Not tested at the PR head.** Patch adds the test. |
| `deleteIfExists` throws in `finally` | Replaces the `SolrException` from the write, hiding the cause. Patch handles it. |
| Permission copy fails (`IOException`) | The edit goes through; the new file keeps the Java default; logged at debug only. See the position below. |
| Non-POSIX file system | `UnsupportedOperationException` is caught, edit goes through; test is skipped by `assumeTrue`. |
| `security.json` is a symlink | `Files.move` replaces the link with a regular file; the link target is left stale. Behavior change from the old in-place write. **Not run.** |
| `security.json` is a single-file bind mount | `rename(2)` onto a mount point fails with EBUSY. That is not an `AtomicMoveNotSupportedException`, so the plain-move fallback does not run and the edit returns "Failed persisting". The old in-place write worked. **Not run.** |
| Owner, ACLs, extended attributes, hard links of the old file | Not carried over. Only permission bits are. |
| Power loss after the move | No `fsync` before the move; on filesystems that do not flush replace-by-rename data first, a short or empty file is possible. An empty `security.json` means no authentication. Low likelihood; named. |
| Stale temp files after a crash | `security.json<random>.tmp` stays in SOLR_HOME, mode owner-only at least on a plain file system. Harmless to core discovery. |

### The permission-preservation commit

- **POSIX-only behavior.** The copy uses `getPosixFilePermissions` and `setPosixFilePermissions`; on a file system without a POSIX view the first call throws `UnsupportedOperationException`, which is caught. The test is skipped there. Fine.
- **Debug-only failure path.** The `catch` covers `UnsupportedOperationException | IOException` and logs at debug. The two cases differ: the first is expected on Windows and should stay quiet; the second means the copy failed on a POSIX file system and the new file now has the Java default, which can change who can read the file. Patch: debug for the first, **warn** for the second.
- **Proceeding when the copy fails.** Right call. The Java default for a created temp file is owner-only on a POSIX default file system, so the fallback tightens rather than loosens; an admin who cannot mirror modes would otherwise be locked out of editing. But it must be visible, hence the warn. (The run that reverted the copy observed 0644, not 0600, on the test file system, so the default is not the same everywhere.)
- **Is the body's Choice posed fairly?** No, in two ways. It said the new file "is created owner-only on POSIX systems", which the cited run contradicts (0644 observed); and it asked "was copying the right call?", which only invites a yes. Rewritten (live + draft) to state the observed result, say that only permission bits are copied (not owner, ACLs, xattrs), say the failure is logged at debug, and ask two real questions: keep the bits, and should a failed copy stop the edit.
- **Is the changelog title's scope sentence accurate?** The title says edits are "serialized within one Solr process and written through a temporary file, so concurrent edits can no longer overwrite each other or leave a partly written file", and "the file also no longer carries the internal version metadata". True as scoped. It is two sentences long, longer than the other fragments. Left: it is a code-branch file and a nit.

### Findings

| ID | Severity | Finding | Applied |
|----|----------|---------|---------|
| S1 | **High, blocking until settled** | Replace semantics: the new write replaces the directory entry. A symlinked `security.json` would become a regular file; a single-file bind mount would make edits fail. Neither is mentioned, tested or run. | Limits bullet applied (live + draft, hedged: "Neither case was run"). Settling run posted (`gates/SOLR-18010-replace-semantics-premise.md`). If Q2 confirms EBUSY on a bind mount, the move needs a fallback to an in-place write on that failure, or the body must say edits fail there. Owner item. |
| S2 | Medium | The Choice was not posed fairly and carried a claim its evidence contradicts (see above). | Applied (live + draft). |
| S3 | Medium | The failure path advertised in the posted offer comment ("a failed write keeps the old file") has no test. | Patch: `testFailedWriteKeepsPreviousFileAndLeavesNoTemporaryFile` on `review-r1-solr-18010`. |
| S4 | Low | Failed copy logged at debug. | Patch: warn for `IOException`, debug for the no-POSIX case. |
| S5 | Low | `deleteIfExists` in `finally` can mask the write error. | Patch. |
| S6 | Low | The marker strip was unexplained in "What happens today". | Applied: two sentences tying it to the ticket's sample. |
| S7 | Low | No `fsync` before the move; the edit lock is held during the body read. | Applied as one Limits bullet. Not patched: the first is an owner design call, the second needs a refactor of the shared parent class. |
| S8 | Nit | `testConcurrentEditsToLocalSecurityJson` always takes at least 10 s when the code is right (`second.join(10 s)` times out by design). A wait on the thread being `BLOCKED` would be faster, but it would weaken the fail-before (a transient block on a class-load monitor would end the wait early). | Left; recorded. |

### What should happen to #5118 under each answer on #5065

Facts at 2026-10-13: #5065 is open, not a draft, one approval from a non-member, no maintainer review, a failing Crave check with no annotation, author silent for four days. The offer comment (id 6139135892, posted 2026-10-12) has had no reply.

- **Adopts both elements (forced-interleave test and temp-file move).** #5065 becomes a superset of #5118 except the permission copy and the marker strip. Tell the author the move changes the file mode (narrower on a default file system) and offer the permission commit and the failed-write test with it (draft below). Once #5065 carries them, close #5118 as superseded (Nick's call) and keep no second PR.
- **Adopts the test only.** #5118 stays a draft, rebases onto #5065 after it merges, drops the lock and the interleave test it duplicates, and becomes a follow-up about the write path (atomic move, permissions, marker). That follow-up needs its own ticket, because SOLR-18010 will be resolved by #5065. Nick files it, or a maintainer asks for it.
- **Adopts the move without the test.** Same as the previous answer, but #5118 supplies the forced-interleave test as a separate commit offered to #5065.
- **Declines both.** The lock is already in #5065, so #5118 conflicts textually (same override, same field, same import). Rebase #5118 onto #5065 after it merges, remove the lock, keep the write path as the follow-up above, with the atomic-write evidence (the settling run: five of five corruptions on the base) in the ticket text.
- **Does not answer.** Keep #5118 a draft. Do not flip it ready while #5065 is open: two PRs for one ticket with the same file overwrite each other's review attention. If #5065 merges without a reply, take the "declines both" route. If #5065 stalls beyond a week, Nick can ask Eric Pugh (the commenter on both the ticket and #5065) which PR he wants; that is a public comment and needs Nick's go-ahead.

Whichever applies, the ready flip stays with Nick.

### Verdict

**NOT SATISFIED.** Blocking: S1 (the replace semantics) until the settling run answers it; and the patch (S3 to S5) gated. Reaching SATISFIED: the premise job answers Q1 and Q2; the body names the answer; the patch is gated green and moved onto the branch.

---

## Decisions under the owner's ladder

| Question | Decision | Rule used |
|----------|----------|-----------|
| Exactness rule for a skipped shard (#5115, #5117) | Any skipped shard clears it. Patches written and posted for gating, on review branches, not on the PR branches. | Cost is effort: do the hard thing. |
| Is the zero-hit case settled for plain `QueryComponent`? | Decided from code reading (`shortCircuitedResults`, `SolrDocumentList` default) and the combined component's real-exit run. A run for the plain component is posted. | Information missing: get it. |
| Real-exit crash in #5117 | Follow-up named in Limits. Ticket is Nick's call. | Optional: defer as a named follow-up. |
| Multi-query `numFoundExact` | Owner item, disclosed in the body. | See below. |
| Permission copy failure | Keep proceeding, raise the log to warn. | Cost is effort. |
| fsync, slow-client lock hold, symlink and bind-mount behavior | Named in Limits; replace semantics settled by a posted run. | Information missing / optional. |
| Where patches live | Review branches `review-r1-solr-18539`, `review-r1-solr-18541`, `review-r1-solr-18010`. The live PR heads did not move, so the pinned head links and receipts in the bodies stay true. | A patch counts as reviewed only after a Linux host gates it. |

## Owner items

1. **Multi-query `numFoundExact` in #5117.** With the exactness fix, an all-exact response from several sub-queries reports true, where the old inverted fold reported false by accident, and `numFound` (the largest sub-query count) can be below the documents returned. My recommendation: report false whenever more than one sub-query contributes, until the count is decided. That is one line plus a test and matches the guide's contract that an inexact `numFound` is a lower bound. I did not write it because the body poses exactly this to the maintainers and the answer changes the proposed text. Say the word and I will add it to the patch.
2. **Bind-mount fallback in #5118**, if the settling run confirms EBUSY: fall back to an in-place write when the move fails, or document that edits fail on a single-file bind mount.
3. **Ticket for the missing-per-query-entry crash** (and the unproven score-only NPE, and the `numFound--` leak): file or decline.
4. **Whether to move each patched head onto its PR branch** after the Linux gate (it changes the PR heads, so the bodies' proof lines and links need a refresh at that time).
5. **The 10-second wait in `testConcurrentEditsToLocalSecurityJson`**: leave or replace.

## Drafts for the owner (not posted)

**Follow-up on #5065, if the author adopts the move (for Nick to approve):**

> Thanks for taking the interleave test. One thing to know about the temp-file move: the new file gets the default mode for a created file, so an edit can change who can read security.json (a file set to 0640 came out 0644 in my test run). In #5118 the edit copies the existing permission bits to the new file before the move, and there is a test for it. I can send that as a follow-up commit if you want it here.

No other comment was drafted. Nothing was sent.

## Housekeeping

- The `wt/pr-prepare` worktree held one unpushed local commit (`b801646095e`, a stale October 8 claim file for update-processing Group B). Moving the worktree to the fork's tip would have left it unreferenced, so I kept it as the local branch `backup/pr-prepare-claim-groupb-b801646`. It is not pushed and was not changed.
- `wt/pr-prepare` is now on a local branch `pr-prepare` that tracks the fork's, so the claim push worked through `branch.ps1`.
- Web fetches of the Gradle template and Lucene's `gradlew` came through a summarizing fetch tool; the Gradle conclusion (conversion inside the block, no earlier one) is consistent with the Gradle project's template as I know it, and the Lucene detail is marked unconfirmed in the report.

## Round 2

Post-gate work, in order (assignment: `assignments/pool-four-new-prs-strong-review-round-2.md`):

1. Linux hosts claim and run the three patch gates and the two settling runs.
2. Re-review each patched head against its receipt, then apply the matching body rewrite. The text to change, by PR:
   - **#5115**: Proof counts (18 tests, new failure list), the behavior-change paragraph ("numFoundExact follows the merged shards too, except..." becomes "a skipped shard clears numFoundExact"), the Choice section (drop it, or reduce it to whether the flag should also use the skipped shard's own count), L-links to the new head.
   - **#5117**: What this change does (exactness), Proof counts, Limits, links to the head that carries the side branch plus the patch.
   - **#5118**: Proof (5 tests in the class, the new test linked), Limits after Q1 and Q2, links to the new head.
3. Settle the owner items above.
