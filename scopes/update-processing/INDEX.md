# Update processing DISCUSS items: scoping index

Scoped 2026-10-09 for the owner. Claim: `claims/update-processing-scope-the-eight.md`. Assignment: `assignments/update-processing-scope-the-eight.md`. Two subagents wrote the per-item reports, and the lead agent compiled this index.

Read-only: no code changes, commits, builds, test runs, or posts. Each report tags its evidence (code at a named ref, diff, JIRA record, hypothesis, or read-only and not run). Sizes marked "estimate" have no code behind them yet. The owner decides from this table. The reports are the evidence behind it.

## Index

| Ticket | Question | Broader option, size | Pin feasibility | Sized recommendation | Differs from TESTING.md? |
|---|---|---|---|---|---|
| SOLR-5065 | Locale handling and suggester inference | (a) locale-aware parse: about 20-30 production lines in 3-4 files, changes the `parsePossibleDouble` signature (estimate). (b) suggester pin: test only, about 25-40 lines (estimate) | (a): yes, using the existing French chain, but the result depends on the locale decision. (b): yes, a unit test on `guessFieldType` that fails on main | Ship narrow. Add the (b) suggester test to this branch now, with its fail-before run | Yes. TESTING.md says add the suggester test only if maintainers ask. The subagent says the same helper also drives the designer's type-change check, so the test should go in now |
| SOLR-6065 | The ticket's cloud test for the max-docs limit | Test only: one new cloud test class, about 90-130 lines (estimate). No production change expected | Yes. Modeled on `LeaderTragicEventTest`. The JVM-wide limit is restored in `finally` | Include, it is contained | No |
| SOLR-7504 | Chain placement and the uncovered shape | Default-value change for atomic documents: about 5-8 lines in 2 files, affecting every DefaultValue user (estimate). Moving the counter cannot cover the shape | Yes: a clone-then-count chain with an atomic document. Not run | Ship narrow: a Limits entry, and narrow the changelog title that says "fixed" | Stronger than TESTING.md says: see the decisions below |
| SOLR-12245 | Target detail in the client response | Logs-only alternative: revert about 20 production lines and add one log line (estimate). The branch as it stands keeps the response detail | Response: yes, a unit test exists. Logs: needs a cluster or log capture | Still a judgment call. The one fact that settles it: whether the coordinator's log line already names collection and shard (a main-side run). If the response detail is kept, fix the duplicated host in `describe()` first | The lean to keep the response detail is supported, but the subagent says it needs the duplicate-host fix |
| SOLR-12705 | Counting processors on the atomic path | Option (a), exclude counters from the atomic-operand path: 2 production files, about 10-15 lines, one test (estimate). Option (b), state and pin the behavior: test only, about 25-35 lines | (a): yes, plain value on the count chain. (b): yes, `{add: 1}` and `{remove: 1}` on the count chain. Not run | Option (a), by the subagent's reading. The subagent calls (a) the narrow option, which reverses the naming used in the other reports | Yes. TESTING.md recommends (b). See the decisions below |
| SOLR-14718 | The second flaw: a batch error can name the wrong document | Association fix in `StreamingSolrClients`: about 40-70 lines (estimate), plus a real-server test of about 40-50 lines | Only a real-server test works. The mock cannot reach the association. Not run | Ship narrow, with a Limits note that the reported document can be the wrong one, and that retries are not per document | No, but the retry finding adds to the Limits note |
| SOLR-16356 | The second close-race ERROR (expiration task commit) | Close-order wait (1-3 lines) or a guard (about 10 lines in 2 files), plus a 50-80 line latch test (estimate) | Needs a blocking test processor. Timing-sensitive. Not run | Still a judgment call: whether the ticket's "DBQ during core close" scope covers the expiration task's commit trace | No |
| SOLR-16910 | Scope of the `SolrCore.Request` logging fix | P1 docs only (0 lines). P2 MDC marker (35-60 production lines, and the branch's test file would be rewritten). P3 deprecate the processor (new class, 38 referencing files) | No pin until a behavior is chosen. `RequestLoggingTest` is the model | Ship narrow. The ticket does not record a desired behavior, so the broader work cannot be sized to a decision | No |

## Decisions for the owner

1. **SOLR-12705, option (a) or (b).** TESTING.md says (b). The subagent says (b) publishes `{add: n}` as a stated behavior, but the merger applies that as an append, not a count. The subagent also finds that under (b), a combined tree with SOLR-7504 skips the 7504 rejection for single-map operations. The branch is marked "Ready for final review". A run showing what a single-valued count field does with an appended `{add: 1}` would settle it.
2. **SOLR-5065, the locale decision and the suggester test.** Whether an explicit locale should change which exponent syntax is accepted is a ticket decision. The Hostetter comment (13718511) says yes, and the Rowe and Krupansky comments say the syntax should not depend on the locale. Separately, the subagent recommends adding the suggester unit test now, which changes TESTING.md's "only if maintainers ask".
3. **SOLR-6065, the audit's blocking item.** The audit says the single-core test is not enough. The subagent recommends the cloud test as contained. The owner must either accept the cloud test or record the single-core scope as the decision. The `mergeIndexes` path has no mapping, and it is not covered by either test.
4. **SOLR-7504, the changelog title.** The title says "fixed", but under the documented chain any atomic update that omits the source field writes 0 into the count. That is stronger than the audit says, and it is not in the reporter's example. The subagent says the only real fix is in default-value handling, which changes every DefaultValue user.
5. **SOLR-14718, the Limits note and audit finding 1.** The clone fix turns an empty error into a confidently wrong one for every asynchronous failure after the first document in a request. The subagent also reads audit finding 1's mechanism as wrong: the mock throws synchronously, so the clone test fails on the old code regardless of timing. A run with the clone reverted would settle that.
6. **SOLR-12245, the response detail.** Keep the detail in the client response (the branch) or move it to the log only. The response detail reaches clients only on forward-to-leader and cross-shard failures, and on the streaming path it repeats the host. The log-line run settles whether the logs already carry it.
7. **SOLR-16356, the ticket's scope.** Whether "if we have a DBQ running and the core closes" covers the expiration task's commit trace. If yes, the close-order change is the smaller fix. If no, ship narrow and name the second trace in Limits.
8. **SOLR-16910, the desired behavior.** Whether `/update` should be logged once (as now), twice (P2), or by a summary processor (P3). The ticket does not say, so nothing beyond the branch's WARN fix can be pinned until someone records it.

## Interactions

- **SOLR-12705 and SOLR-16655:** both change `FieldMutatingUpdateProcessor.java` and `ParsingFieldUpdateProcessorsTest.java`. The merge-tree check shows conflict markers in the first file. Whichever lands second must rebase.
- **SOLR-12705 and SOLR-7504:** no shared file. In a combined tree, 12705 bypasses the 7504 rejection for single-map operations on a counted field.
- **SOLR-6065 and SOLR-18505:** a textual conflict in the import block of `DirectUpdateHandlerTest.java`.
- **SOLR-5065 and SOLR-16673:** both edit the same two parse files, one line each. No textual conflict, but they must be read together.
- **SOLR-12245 and SOLR-5939:** if 5939 lands, one stream failure produces one error per merged request, so `describe()` would repeat inside one message.
- **SOLR-14718 and the others:** no shared changed line with SOLR-12705, SOLR-7504 or SOLR-16356. The 14718 test would sit next to the clone test in `SolrCmdDistributorTest`, a likely textual conflict.
- **SOLR-16910:** five other submit branches change `SolrCore.java` (SOLR-12007, SOLR-15003, SOLR-17047, SOLR-5011, SOLR-8628). Their hunks were not inspected, so a P2 or P3 change there needs a merge check.

## Claims that need a main-side run

None of these was run. Each is a read-only check for the main side.

- **SOLR-5065:** the exponent separator for every locale (`DecimalFormatSymbols.getExponentSeparator()`), the fr_FR result on main, and whether the suggester pin fails before the fix.
- **SOLR-6065:** the cloud test itself, including the reflection path, which has never been gated. Whether Lucene 10.4.0's `addIndexes` throws the same limit message. The Lucene source is not in this checkout (`gradle/libs.versions.toml:39`), so the claim that `mergeIndexes` is unmapped in practice is also unverified.
- **SOLR-7504:** the end-to-end count reset to 0 for an atomic update that omits the source field.
- **SOLR-12245:** the coordinator's log line for a forward-to-leader failure, and the composed streaming message with the duplicated host.
- **SOLR-12705:** what a single-valued count field does with an appended `{add: 1}`. The effects of the Ignore and RemoveBlank processors on atomic operands.
- **SOLR-14718:** whether the first document is resubmitted and the failing one dropped on a ForwardNode retry. Whether the clone test fails on the old code (audit finding 1).
- **SOLR-16356:** whether the expiration task's commit trace still logs after the branch. Whether a close-order wait deadlocks.
- **SOLR-16910:** nothing for scoping. The missing piece is a desired behavior recorded on the ticket.
