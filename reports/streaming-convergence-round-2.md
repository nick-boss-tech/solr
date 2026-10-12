# Streaming expressions convergence, round 2

Verdict: **one draft converges, three stay open.** SOLR-12657 converges. SOLR-12505, SOLR-10882 and SOLR-14231 remain NOT CONVERGED, each on items that depend on a branch commit or a gate job that has not landed.

Counts: 10 register rows re-checked (ST-R1-08, 09, 21, 22, 23, 24, 26, 27, 32 and 33), 4 live heads, and the main-side commit `82088f66784`. Confirmed 6 (ST-R1-21, 22, 24, 26, 27 and 32). Disputed 1 (ST-R1-09). Still open 4 (ST-R1-08, 09, 23 and 33). New findings 1 note (ST-R2-01).

Scope: `assignments/pool-streaming-convergence-round-2.md`, checked against `reports/streaming-convergence-round-1.md`. One read-only subagent did the review. Heads were checked with `git ls-remote origin refs/heads/<branch>`. Nothing was edited, posted or built.

## Verdicts

- **SOLR-12505: NOT CONVERGED (not on HOLD).** The head `5f20e1171bc8` matches the draft, the receipt's gated head and the gate job. ST-R1-23 is open, so the draft stays NOT CONVERGED.
- **SOLR-12657: CONVERGED.** The live tip `23cc27a98925` matches the draft and the receipt.
- **SOLR-10882: NOT CONVERGED.** The tip `5d7d07a5b17` matches. Open: ST-R1-08 (the branch title) and ST-R1-09 (the proof date).
- **SOLR-14231: NOT CONVERGED.** The tip `2f73d00a3996` matches. Open: ST-R1-33 (blocking, the branch title).

## Item states

**Item 1, ST-R1-23 (the `on=` field name): STILL OPEN.** At `5f20e117`, `FetchStream.java` line 239 still appends the join key unescaped, and `init()` (around lines 140 to 165) splits on "=" with no escaping or validation. The only escaping test at head covers key values, not the field name. The fix gate job `gates/SOLR-12505-fetchstream-fix.md` is OPEN and unclaimed, and `receipts/SOLR-12505.md` still records `5f20e117` as the gated head. Owner decision 2 has only the recommended option (a) on record, adopted by the coordinator default; no owner ruling was found. No draft text is needed until the commit lands.

**Item 2, ST-R1-21, 22 and 24: CONFIRMED.** The replacement text is present word for word (draft lines 9, 31 and 41). The links check out: `QParser.java` lines 387 to 396 at `14c7aac`, `TermsQParserPlugin` line 74 and the separator at line 150 at base, and `FetchStream` base lines 239 to 246. The head's test and javadoc are at lines 946 to 1006, and the changelog path exists. The merge-base of `5f20e117` with `upstream/main` is `14c7aac`, so the Base bullet is right. The count drift is 72 commits, measured against the local `upstream/main`, which was not fetched. The Proof counts (56 tests, 5 skipped) rest on the receipt, and the gate log was not opened.

**Item 3, ST-R1-26 and 27 (SOLR-12657): both CONFIRMED.**
- ST-R1-26: lines 26 and 29 carry the register text. The diff from `16a0a69e839` to `23cc27a989` touches only `Metric.java`, `MinMetric.java`, `MaxMetric.java` and `MinMaxMetricTest.java`, so "the test file and FacetStream are unchanged" holds. The ranges check out: the base cast at `FacetStream` line 951, the head at lines 953 to 958, and the metric lines as cited. The base `Metric.getValue` returns `Number` at line 71, so the API note is right.
- ST-R1-27 rebuttal: `receipts/SOLR-12657.md` records "GATE RUNNER DONE 2026-10-11T04:04:02Z", which is 2026-10-10 22:04 MDT. The receipt carries an unambiguous UTC timestamp, so the rebuttal holds. The streaming response cites the body-date rule (C1, recorded in another lane) by name, and this review accepts that reliance.

**Item 4, titles.**
- **SOLR-10882, ST-R1-08: STILL OPEN.** The draft-side text is applied: the bold lines 7 and 13 and the Limits lines 34 and 36 match the register. The tip fragment `changelog/unreleased/SOLR-10882-array-sort-mixed-types.yml` still ends its title with "and no longer hits a NullPointerException when reporting a null value." The register replacement is not in place. By reading, the clause has some basis (the base non-Comparable message calls `value.toString()` on a null, and the head formats with `%s`), but no gated test covers it, so the register replacement stands.
- **SOLR-14231, ST-R1-33 (blocking): STILL OPEN.** The tip fragment `changelog/unreleased/SOLR-14231.yml` still reads "scoreNodes streaming expression no longer fails when it receives a single node or no nodes, and sends its /terms request as a POST." The gate job `gates/SOLR-14231-title-fix.md` is OPEN and unclaimed. The draft-side ST-R1-32 text is present and exact.
- **SOLR-10882, ST-R1-09 (the proof date): DISPUTED, STILL OPEN.** The Proof says "verified 2026-10-10". The register required the date from the gate log. The response took it from the takeover log and a claim heartbeat. `receipts/SOLR-10882.md` records no run date, and the body-date rule requires the receipt to carry one. The replacement is the register's: "With this change, at head `5d7d07a5b17`, ArrayEvaluatorTest runs 7 tests with 0 failures, 0 errors and 0 skipped." A date should come back only when `receipts/SOLR-10882.md` records the run date.

## Register

**ST-R2-01 | main-side record, no draft | note.** Commit `82088f66784` is titled "Gate jobs: SOLR-12505 FetchStream field name fix, SOLR-10882 and SOLR-14231 changelog title corrections". Its stat shows three gate files only, with no changelog or source files. Neither branch tip carries the corrected titles, and both gate jobs are OPEN. Suggested record wording: "Gate jobs added for the SOLR-12505 field-name fix and for the SOLR-10882 and SOLR-14231 title corrections." No draft replacement is needed.

## Not checked

- The gate logs and JUnit XML, which were not opened.
- The JIRA-sourced statements in the SOLR-12505 draft (attributions to Pugh, Bernstein and Smiley), since JIRA was not in this round's inputs.
- The takeover log and the master ledger, which were not opened.
- SOLR-9852 and SOLR-10322, which are out of scope.

## Convergence line

NOT CONVERGED for SOLR-12505, SOLR-10882 and SOLR-14231. SOLR-12657 converges. The open items wait on the field-name commit, the two title commits and the receipt date.
