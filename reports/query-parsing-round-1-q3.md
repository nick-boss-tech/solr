# Query parsing round 1, part q3: join family (SOLR-8977, 9048, 11391 audit)

Result: SOLR-8977 and SOLR-9048 are draftable once two changelog wording fixes land in their branches. SOLR-11391 is held: its Jira ticket describes a different problem. Separately, upstream main already contains SOLR-13202 (PR #5012), which contradicts the claim's "awaiting merge".

Heads checked (read only): origin/solr-8977-submit `395b24964fd`, origin/solr-9048-submit `b145018563c`, origin/solr-11391-submit `b0f15a22856`. All match the claim table. Local branch refs are behind origin (see finding 11).

## Findings

1. **FIX (11391, hold).** File: `changelog/unreleased/SOLR-11391-join-unknown-method-bad-request.yml` lines 6-8 (links block); code at `solr/core/src/java/org/apache/solr/search/JoinQParserPlugin.java` line 220 (head `b0f15a22856`).
   Evidence: `research/jira-context/SOLR-11391.json` has Summary "JoinQParser for non point fields should use the GraphTermsCollector", and the description is about join speed on non-point fields. None of its 18 comments mention an unknown method, 400, or IllegalArgumentException (grep). The branch's commit subjects and changelog describe an unknown `method` value returning 400. The inventory row (`branch-focus-inventory-2026-10-08.md` line 153) repeats the branch wording, not the ticket wording.
   Replacement: do not link SOLR-11391 to this change. Replace the links block with the key the owner chooses, for example `links:` / `  - name: SOLR-<new key>` / `    url: https://issues.apache.org/jira/browse/SOLR-<new key>`, or drop the branch. No new Jira ticket is created here.

2. **FIX (8977 changelog).** File: `changelog/unreleased/SOLR-8977-graph-negative-traversal-filter.yml` lines 2-5 (title text), head `395b24964fd`.
   Evidence: the title says the fix "so it no longer matches no documents". That is wrong for the default. `GraphQueryParser.java` line 60 defaults `returnRoot` to true, so the broken case returns the start document. The receipt (`receipts/SOLR-8977.md` line 7) records `expected:<2> but was:<1>`, and the Jira example returns numFound 1.
   Replacement (lines 2-5): "The graph query parser now makes a pure negative traversalFilter such as traversalFilter='-text:foo' queryable itself. On cores whose luceneMatchVersion is below 10.2.0, the traversal no longer stops at the starting documents."

3. **FIX (9048 changelog and description).** File: `changelog/unreleased/SOLR-9048-blockjoin-empty-subquery.yml` lines 2-3, head `b145018563c`.
   Evidence: `FiltersQParser` is the base of the `{!filters}` parser (`FiltersQParserPlugin.java` line 25, `NAME = "filters"`, which constructs `FiltersQParser` directly). The fix therefore changes `{!filters}` too, and the changelog and the branch description name only block join parsers. No test covers `{!filters}` on the branch.
   Replacement (lines 2-3): "The parent, child and filters query parsers no longer fail with a NullPointerException when a nested query produces no query, for example when all its terms are stop words. The nested query then adds no constraint." The draft already states this.

4. **NOTE (9048 Limits).** The `which` and `of` forms still pass a possibly null query. `BlockJoinParentQParser.java` line 293 (`return subQuery(...).getQuery();`, no null check) and line 283-284 (empty-clause path). `BlockJoinChildQParser.java` line 52 adds `parents` as MUST_NOT. Not run. The Jira title ("underneath query parser yields no clauses") covers this form too. Replacement: the draft's Limits names it with a follow-up plan (done).

5. **NOTE (8977 Proof).** The `doGraph` assertion added at `GraphQueryTest.java` lines 118-124 passes on the base code. The test configuration sets luceneMatchVersion to `${tests.luceneMatchVersion:LATEST}` (`solr/core/src/test-files/solr/collection1/conf/solrconfig.xml` line 46), and the build sets that property to the Lucene base version, 10.4.0 (`build.gradle` lines 109-113 and `gradle/testing/randomization.gradle` line 203, upstream/main). So the auto-fix is on and the query is already queryable. The receipt's first premise round did not discriminate (`receipts/SOLR-8977.md` line 7) and agrees. Only the new method discriminates. The draft says so. No branch change needed.

6. **NOTE (8977 Limits).** The root query has the same gap. `GraphQueryParser.java` line 41 passes `rootNodeQuery` without `makeQueryable`, and `GraphQuery.java` line 110 stores it as `this.q`. On cores below 10.2.0 a pure negative `v=` root matches nothing. Not run. The draft names it in Limits with a follow-up offer.

7. **NOTE (8977 version claim, both lines checked).** `QParser.java` lines 114-115 (head `395b24964fd`, same on upstream/main) use `Version.LUCENE_10_2_0`.
   - Has the check: `upstream/main` (apache-lucene 10.4.0, `gradle/libs.versions.toml` line 39), `upstream/branch_10x`, `upstream/branch_10_1` (pins 10.4.0).
   - No check, no `makeQueryable` in `SolrQueryParser.java` or `GraphQueryParser.java`: `upstream/branch_10_0` (pins 10.3.2) and `upstream/branch_9x` and `upstream/branch_9_11` (the 9.x line has no `LUCENE_10_2_0` constant, which is expected).
   So the claim "fixed for cores at 10.2.0 or later" holds on main and the 10.x lines that carry the check, and the 9.x and 10.0 lines have no repair at all. The draft is scoped to main and says so in Limits. No change needed in the branch.

8. **NOTE (assignment wording).** The assignment says SOLR-8977 changes "GraphQueryParser and BJQParserTest". The branch diff against base `14c7aac0d15` touches `GraphQueryParser.java`, `GraphQueryTest.java`, and the changelog only. `BJQParserTest` is a regression count in the receipt (18 tests), not a changed file. Replacement wording for the assignment line: "SOLR-8977 changes GraphQueryParser.java and GraphQueryTest.java."

9. **NOTE (receipts and proof counts).** Gate logs named in the receipts (`g8977-gate.log`, `g8977-premise.log`, `g9048-gate.log`, `g9048-premise.log`) are not on disk (searched the workspace). Every count in the drafts comes from the receipts only. The 8977 receipt's "38 of 38" includes one skipped test in `TestScoreJoinQPScore`, and the raw pass and skip split is not on disk.

10. **NOTE (11391 receipt and inventory are stale).** `receipts/SOLR-11391.md` line 4 names the live tip `825d7f81d12` and says the branch "still carries its SOLR-11391-TESTING.md handoff note". At live head `b0f15a22856` that note is removed, and the branch diff against base is three files. The inventory row (line 153) still says 4 files. Main side should refresh both. Not a draft blocker on its own.

11. **NOTE (local branch refs are behind origin).** Local `solr-8977-submit` is `208c25e`, `solr-9048-submit` is `c329ff3`, and `solr-11391-submit` is `825d7f8`. Origin heads are `395b249`, `b145018`, and `b0f15a2`. The drafts use the origin heads. The local refs are shared with other worktrees, so do not fast-forward them without asking.

12. **FIX (lead: claim table and assignment, SOLR-13202).** `upstream/main` contains `93e357bcf6b` "SOLR-13202: Return 400 instead of 500 for join queries missing from/to parameters (#5012)", dated 2026-10-07. It changes the same nine files as the 13202 branch (`git show --stat`), and its `JoinQParserPlugin.java` hunk matches the branch hunk. The claim table and the assignment both say #5012 is "APPROVED and awaiting merge". Verify the PR state with q4's read-only `gh` call before any 13202 wording is drafted. Effect on this part: `ScoreJoinQParserPlugin.requireFromAndTo` (line 359 on upstream/main) is already on main, and none of the three branches here calls or changes it.

13. **NOTE (landing order and overlaps).**
   - 8977 (`GraphQueryParser.java`, `GraphQueryTest.java`), 9048 (`FiltersQParser.java`, `BJQParserTest.java`), and 11391 (`JoinQParserPlugin.java`, `TestScoreJoinQPNoScore.java`) share no files. A `git merge-tree --write-tree` against current upstream/main is clean for each. Any order works among the three.
   - Shared test class: none among the three. The live join branches overlap elsewhere (`CrossCollectionJoinQueryTest` is shared by 13202 and 16130, which is q4).
   - 11391 and 13202 both touch `JoinQParserPlugin.java`, at different hunks (line 146 for 13202, line 220 for 11391). No textual overlap. 13202 is already on main (finding 12), so 11391 applies on top of it.
   - Shared helper: none of the three calls `requireFromAndTo`.

14. **NOTE (15906 interaction).** `SOLR-15906` changes `QParser.java` only in hunks at new lines 396-467 and 492-520 (`getParser` and the local-params `v` path). It does not touch `autoFixPureNegative` (constructor, lines 114-117) or `subQuery` (line 287). The tests here do not use `v` with trailing text: the 9048 test `{!parent ... v=$sub}` has no trailing text, and 8977 and 11391 do not use `v`. If 15906 lands, the tests and framings of 8977, 9048, and 11391 do not change. Read only, not run.

15. **NOTE (11391 code, for the owner's decision).** The change is small and consistent with the rest of the file. Line 220 calls `parseMethodString`, the same helper the static path already uses to return 400 (`JoinQParserPlugin.java` lines 256-276 on the branch, `parseMethodString` at line 268). The test's `assertQEx(String, String, SolrQueryRequest, ErrorCode)` overload exists (`SolrTestCaseJ4.java` lines 1069-1073 on the branch). The premise that `Method.valueOf` throws IllegalArgumentException matches main (line 223). Not run. The code is not the problem; the ticket linkage is (finding 1).

## Task results

**SOLR-8977: draftable.** Draft at `pr-drafts/query-parsing/SOLR-8977.md`, written against head `395b24964fd`. Condition: finding 2 must be fixed in the branch before submission. The receipt's framing holds on main: the symptom is repaired for luceneMatchVersion 10.2.0 or later, and the branch closes cores below that. The proof is consistent with the code (the new method is the only discriminating test). One Choice for the owner (options below). Scope limits are in the draft's Limits.

**SOLR-9048: draftable.** Draft at `pr-drafts/query-parsing/SOLR-9048.md`, written against head `b145018563c`. Conditions: finding 3 (changelog and description must name `{!filters}`) and finding 4 (in Limits, done). The all-stopword semantics is a genuine Choice: it matches the existing meaning of "no clauses" (an empty `v` already takes that path), with a live alternative of matching nothing. The Jira text itself asks for the proper behavior. The receipt's proof is consistent: 19 BJQParserTest tests with exactly one failure on base.

**SOLR-11391: held, audit only, no draft.** The branch changes an error path (unknown `method` returns 400). Its Jira ticket describes GraphTermsCollector performance on non-point join fields. The change cannot carry that ticket. The code is correct as read, the merge is clean against main, and no gate or premise run exists. Owner decision (below).

Interactions: 8977, 9048, and 11391 are disjoint and merge cleanly against main. 15906 leaves their tests and framings unchanged. The `requireFromAndTo` helper and the 13202 overlap are already on main (finding 12).

## Owner decisions

1. SOLR-11391: keep the 400 change as its own Jira ticket (only if you ask for one), or drop the branch. It cannot go under SOLR-11391 as written.
2. SOLR-8977: the graph parser repairs its traversal filter on every core (implemented), or it documents a `*:*` workaround for cores below 10.2.0 and leaves results alone.
3. SOLR-8977: the root `v` gap (finding 6) goes in this PR or in a follow-up. The draft offers a follow-up.
4. SOLR-9048: an all-stopword nested query matches everything (implemented, same as an empty `v`), or matches nothing.
5. SOLR-9048: keep the `{!filters}` behavior change in this PR with the changelog wording in finding 3, or narrow the fix to the block join parsers.
6. SOLR-9048: confirm the follow-up plan for the `which` and `of` forms (stated in the draft).
7. Lead: confirm the state of PR #5012 (upstream/main commit `93e357bcf6b`) before any 13202 wording.

## Not checked

- Live Jira. Only the hydrated packets in `research/jira-context/` were read (SOLR-11391 packet: Updated 2021-08-13, 18 comments). The 2026-08-18 CSV export has no SOLR-11391 row.
- Gate and premise logs (none on disk). Counts come from the receipts.
- No build, no tests, no `gh` (per the claim). Compile claims rest on the receipts plus grep checks of the APIs the new tests call.
- The `which`/`of` NPE, the root `v` gap, and the no-repair status of the 9.x and 10.0 lines were read from code, not run.
- Lucene constants were checked by their use in Solr sources and the version pins in `gradle/libs.versions.toml`. No Lucene jar or source was read, and the exact message "Query must not be null" comes from the receipt.
- The changelog validator (`dev-tools/scripts/validate-changelog-yaml.py`) was not run.
- Live state of PR #5012 and PR #5004 (q4 owns those reads).
- Whether the 8977 and 9048 new tests fail on base and pass with the change: the receipts say so; I did not re-run them.
