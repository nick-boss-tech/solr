# Streaming expressions round 1: round roll-up

Claim: `claims/streaming-expressions-round-1.md` (commit `90052cb497e`). Assignment: `assignments/streaming-expressions-round-1.md` (commit `f55404e1127`). Part reports: `reports/streaming-expressions-round-1-s1.md` through `-s6.md`. Drafts: `pr-drafts/streaming/`.

Done 2026-10-10 by Claude Code (AI agent), working for Nick Shanin. Six subagents in parallel, split by ticket cluster, within the cap of six. No build, Gradle run, or test was run. No branch, live PR, JIRA item, or comment was touched. Nothing was posted. The receipts were not edited.

## Heads

All eleven branch heads match the assignment's expected tips (`ls-remote` on 2026-10-10). The three NO GATE branches match their receipts. Several local branch refs point elsewhere, so the `origin/` refs were used throughout. The owner should confirm which fork ref is authoritative before any push (part S6).

## Verdicts

| Ticket | Verdict | Draft | Owner decision |
|---|---|---|---|
| SOLR-9852 | Audit only: not draftable yet | none | None. First gate owed. |
| SOLR-10322 | Draftable at the moved head `80ce9d7a3c8a`; the 401 is not addressed | `pr-drafts/streaming/SOLR-10322.md` | Choice question, changelog file name, rebase before opening |
| SOLR-10882 | Audit only: not draftable yet | none | None. First gate owed. |
| SOLR-11922 | Owner call: keep narrowed coverage (recommended) or retire | none | Keep or retire; close the ticket as fixed by SOLR-10855 |
| SOLR-12505 | Draftable at `5f20e1171bc8`; field-name check first | `pr-drafts/streaming/SOLR-12505.md` | `on=` field name, Choice section |
| SOLR-12657 | HELD: min and max can be wrong on mixed fractional-second dates | `pr-drafts/streaming/SOLR-12657.md` | Ship with the Limits paragraph, or fix and re-gate (recommended) |
| SOLR-13524 | Live PR consistent on head; proof and receipt drift | none | Receipt fixes (main side); PR text edits (owner, public) |
| SOLR-14200 | Retire candidate (recommended) | none | Retire, or keep under a new title |
| SOLR-14231 | Draftable at `2f73d00a3996` | `pr-drafts/streaming/SOLR-14231.md` | Date, fail-without-fix run, length |
| SOLR-15326 | Retire candidate (recommended) | none | Retire, or keep as an ordering change after four fixes |
| SOLR-17433 / 17143 | Retire candidate (recommended) | none | Retire, or a narrow follow-up coordinated with the claimed contributor |

## Drafts written

- **SOLR-10322** (5,198 characters with links). Draftable with the head disclosed in Proof. The draft says plainly that the 401 is not addressed, and the PR must not be titled as a 401 fix. The premise narrows the PR: the change matters only when the context has no cache. Open points: check the log header for "same commit and seed" (the receipt gives no commit for `g10322-premise2.log`); the changelog file is `SOLR-10322-topic-stream-client-cache.yml`, not the template name; no Choice section (a live question exists: fail fast with a clear error when the context has no cache, versus the implemented route). If SOLR-17433 ships, the request timeout for this path becomes unbounded, and the proof must be re-run on the combined tree.
- **SOLR-12657** (4,153 characters). Held. The draft is accurate and states the edge case in its Limits. The parallel rollup compares ISO-8601 strings as text. `Instant.toString` prints fractional digits only when needed, so `2018-03-01T10:00:00Z` sorts after `2018-03-01T10:00:00.250Z`, though it is earlier, and the minimum or maximum can be wrong. Checked in the code: `MinMetric.java` and `MaxMetric.java` at head, and `DatePointField.java` line 249 on main. The receipt's tests use whole seconds only.
- **SOLR-12505** (4,128 characters). Draftable at head `5f20e1171bc8`. The premise holds on main: `FetchStream.java` has no `defType`, and `QParser.java` restricts local params to lucene and func. A "choice to check" section is included (`defType=lucene` versus `defType=terms`), and the ticket thread agreed the route in 2018, so the section can be cut. Security point, not in the public text: the `on=` field name is written into `{! df=<name> ...}` without escaping (`FetchStream.java` line 239, `rightKey`, checked by reading). SOLR-11501 restricts local params so end-user input cannot set parser options. Whether `on=` can carry end-user text depends on deployment.
- **SOLR-14231** (3,957 characters). Draftable at head `2f73d00a3996`. The premise holds on main, by reading. Open: the ticket text is missing (`research/jira-context/SOLR-14231.json` does not exist), so "What happens today" is derived from code, and the changelog title asserts a failure that could not be tied to the ticket symptom; no fail-without-fix run is recorded, so the base result is by reading only; the counts carry no verification date; no Choice section; over the guide by about 450 characters; the dropped test is stated as a limit.

## Retire calls, with evidence

- **SOLR-11922** (held). The 2018 NPE was an unguarded `toExpression` call fixed on master by SOLR-10855 (commit `463907a13c4`, 2017). No main test pins the guard. Recommendation: keep the test as narrowed coverage (one method, no production change), with the title saying it covers the guard and cannot fail on base. Remove `SOLR-11922-TESTING.md` first. Close the ticket as fixed by SOLR-10855 either way (public).
- **SOLR-14200.** The `shards.tolerant` crash does not exist on main. The replica-less slice is dropped silently (`TupleStream.java` lines 189 to 199), whatever `shards.tolerant` says. The branch fixes a manual-map NPE reachable only from `/stream` callers that send `<collection>.shards=`. Recommendation: retire. Keeping it means a new title and a new story.
- **SOLR-15326.** No tie collapse on main, and the missing-record premise does not reproduce on a healthy cluster. The branch has four issues: the tie-breaker keys off `qt` only, an extra schema request on every open, a docValues dependency on legacy schemas (conditional, not run), and a merge comparator that does nothing unless the key is in `fl`. Recommendation: retire, and ask the reporter for steps (public, the 2021 request has no answer).
- **SOLR-17433 / 17143.** On main the default cap is 600 seconds, not 60, so the ticket's symptom is already gone for default clients. The branch's one behavior change removes the total cap for every cache-built client, including the JDK path, which would then have no bound. It also discards seeded request timeouts, and the 17143 constructors duplicate the seed route. The maintainers asked for a different direction (a default request timeout of -1). Recommendation: retire the combined branch. If wanted, a narrow 17433 follow-up, coordinated with the contributor who claimed the ticket.

## Audit-only tickets

- **SOLR-9852.** The premise holds on main, by reading: `getColumns` returns null, and `getTypeInfo` throws. The branch's `getColumns` now lists columns, but `getTypeInfo` is still unsupported. Needs a first gate; a Limits line for `getTypeInfo` and for the shared statement (a metadata call closes the previous metadata result set); and removal of `SOLR-9852-TESTING.md`.
- **SOLR-10882.** The ticket's double and long case is already handled on main by BigDecimal normalization, so the branch must not claim it. The real defects on main are text or boolean mixed with numbers under sort (ClassCastException), and a null value under sort (NullPointerException). The branch fixes both. The null fix has no test. Needs a first gate, a null-value test (or the NPE claim dropped), removal of `SOLR-10882-TESTING.md`, and a narrow title.

## SOLR-13524 (live PR, consistency)

Head, title, changelog and counts match the receipt. Drift, all in text and records, none in code:
- The PR Proof names a stale head (`2b6f17e2622`), not the live head.
- Receipt line 5 cites a fork run at a different head (`1869191c4cff`, on the `ci/13524-orevaluator` branch).
- Receipt line 8 cites a CI run with the wrong date and head.
- The PR Proof's "12 of 15 test executions failed under randomization" on base has no base run in the receipt.
- The PR Proof names only OrEvaluatorTest. The And, ExclusiveOr and Recursive counts are not in the PR text.

Owner: any PR text edit is a public act. The receipt corrections are main-side work.

## Interactions and landing order

- **StreamExpressionTest:** 12657 and 10322 merge clean, with no overlapping methods. Suggested order: 12657 first, then 10322.
- **StreamDecoratorTest:** 11922 and 12505 merge clean. 12505 is the only gated ticket here.
- **StreamingTest:** 14200 and 15326 conflict at the same insertion point. Both are retire candidates. If the owner keeps both: 15326 first, then 14200 rebased with both blocks.
- **10322 and 17433:** no code dependency. If 17433 ships, the 10322 proof must be re-run on the combined tree.
- **13524 and 10882:** independent. 13524 does not change the premise of 10882, and 10882 does not change 13524's pinned behavior.
- **9852 and 14200:** independent.
- **`fetch()` and `defType`:** no other branch in the round adds a `fetch(` call, a `FetchStream` line, or a `defType`.
- **Base drift:** the branches are 37 to 66 commits behind `upstream/main`. A rebase changes the heads and needs new gates before opening. The premise holds on main for the files these branches touch.
- **Metric API:** 12657 changes `Metric.getValue()` from `Number` to `Object`. Other callers were not checked.

## Receipt disagreements (main-side fixes)

- **9852:** "premise unverified" should say the premise is confirmed by reading `upstream/main`.
- **10882:** "premise unverified" should say the ticket's case is already handled, and the real targets are the two defects above. The path is shorthand; use the full repo path.
- **11922:** the handoff note says nothing was run and there is no fix, which is stale and must not ship. The receipt does not give the mechanism (the SOLR-10855 guard).
- **13524:** receipt lines 5 and 8 cite the wrong heads and dates (see above).
- **14200:** the receipt omits that ParallelStream's all-gone case is also a raw IndexOutOfBounds on main.
- **15326:** the receipt omits the four branch issues.
- **17433:** the commit message's "reconciled onto current main" is wrong; the base is 66 commits behind main.
- **10322:** the premise log's commit and date are not in the receipt; check the header before publishing.

## Owner decisions

1. **SOLR-12657:** ship with the Limits paragraph as written, or fix the comparison to parse both values as `Instant` when both parse, then re-gate and redraft the Proof. Recommendation: fix and re-gate.
2. **SOLR-12505:** the `on=` field name. Options: escape or validate the name in code, with a test (recommended); a Limits sentence; or a deployment check first.
3. **SOLR-12505:** keep or cut the Choice section.
4. **SOLR-10322:** whether to add a Choice question (fail fast versus the own-cache route), and confirm the changelog file name.
5. **Rebase before opening (all four drafts).** A rebase changes every head and needs new gates. Or open on the older base with the drift stated.
6. **SOLR-11922:** keep the narrowed test (recommended), or retire. Close the ticket as fixed by SOLR-10855 (public).
7. **SOLR-14200:** retire (recommended), or keep under a new title.
8. **SOLR-15326:** retire (recommended). Ask the reporter for steps (public).
9. **SOLR-17433 / 17143:** retire (recommended). If wanted, a narrow follow-up coordinated with the claimed contributor.
10. **SOLR-9852 and SOLR-10882:** ship narrowed, as the part reports recommend, after the first gates.
11. **TESTING notes:** remove `SOLR-9852-TESTING.md`, `SOLR-10882-TESTING.md` and `SOLR-11922-TESTING.md` before any PR. Their text is internal vocabulary.
12. **SOLR-13524 PR text:** edit the stale head and the unsupported count (public). Owner's call.
13. **The silent skip** of replica-less and inactive slices (`TupleStream.java` lines 189 to 199) is the one record-loss path found on main. Raise it as its own question.
14. **Authoritative fork ref** for all seven local branches that differ from origin.

## Main-side work owed

- First gates: SOLR-9852 and SOLR-10882.
- Fail-without-fix base run for SOLR-14231, or the decision that none is owed.
- Re-gate SOLR-12657 if the fix is chosen.
- Check the SOLR-10322 premise log header.
- Receipt corrections listed above, for 9852, 10882, 11922, 13524, 14200, 15326, 17433 and 10322.

## Not done

- No build, Gradle run, or test. No `gh` write call. No commit to a submit branch. No live PR edit. No JIRA access.
- The three run logs for SOLR-11922, SOLR-14200 and SOLR-15326 were not found. Their settling claims rest on the receipts and code reading.
- The changelog YAML for the drafts was read by eye, not parsed by a tool.
