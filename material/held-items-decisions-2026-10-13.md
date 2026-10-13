# Held items: decisions adopted and acted on (2026-10-13)

Coordinator decisions under the decision default (`material/coordinator-handoff.md`, "Deciding scope questions"): a recorded recommendation is adopted; where none is on record, the harder, more thorough option is taken. Where information was missing, the ladder's first rung (get more information) came first. Nothing here needed Nick's decision. Each item below records what was decided, what was pushed or edited, and what is still owed. Gate verdicts are not claimed until a Linux host records them.

## SOLR-16013: interrupt edge (code fix)

- **Decision:** code fix, not a Limits line. No recorded recommendation covered the edge, so the harder option applies. The fix is one line plus a test, and the draft's statement that the closes never run on an interrupted thread depends on it.
- **Edge:** `FutureTask.get()` returns without checking the interrupt once the future is done (JDK 21 `lib/src.zip`, `java/util/concurrent/FutureTask.java`, lines 187 to 192). An interrupt that lands after the overseer close has finished survives the wait and reaches `zkStateReader.close()` and `zkClient.close()`. The code comment in `ZkController.close()` says those closes never run interrupted.
- **Pushed:** `solr-16013-submit` from `408317d7e24` to `ecda7b862ba` (fast-forward, 2026-10-13). `ZkController.close()` clears the flag with `interrupted |= Thread.interrupted();` after the wait loop, and the existing `finally` restores it. New test: `OverseerCloseOrderingTest.testZkClientClosesUninterruptedWhenInterruptArrivesAfterOverseerCloseFinishes`.
- **Test strength:** the test interrupts the shutdown thread from the overseer close, which returns at once. Whether the edge is hit depends on scheduling, so the fail-before verdict may be NOT_PROVEN. That is recorded as a result, not a reason to relaunch repeatedly.
- **Owed:** hardening gate on a Linux host, `gates/SOLR-16013-interrupt-fix.md`. Receipt and draft stay at `408317d7e24` until it lands.
- **Unchanged:** the draft's pool-versus-serial "A choice to check" remains a maintainer-facing choice.

## SOLR-12998: proof leg (no new run)

- **Decision:** already decided on 2026-10-10 (`receipts/SOLR-12998.md`, line 3). The gate stands GREEN, the proof leg is inconclusive by construction, and no further proof run is owed. The round-3 hold ("the main side decides") was stale and is cleared.
- **Edited:** live PR #5010 body, Proof section, to match the receipt. Date set to 2026-10-10. The GitHub Actions run references and the 2026-10-04 runner claim are removed, because no receipt records them and the coordinator handoff says Actions is unavailable. The base comparison paragraph now says what the record says: the proof compile failed before any test ran.
- **Owner item, unchanged:** the CI paragraph about `TestUserManagedReplicationWithAuth` still reads as it did. `reports/pr-drift-round-1.md` item 5 asks keep-or-cut, and that is not a scope question.

## SOLR-7394: fail-before clauses (cut and deferred)

- **Decision:** cut the fail-before clauses for `ZkShardTermsRecoveryFailureTest` and `RecoveryStrategyTest`. Both classes fail to compile at the merge base, so no rescope can give a fail-before result for them (`receipts/SOLR-7394.md`, lines 12 to 14). Ladder rung 3 applies: these two proofs are optional beside the recorded `ShardTermsTest` proof (3 of 6 fail on the reset-to-0 code), so they are a named follow-up. The SOLR-14187 scratch-vehicle route was not used, because it would prove a reimplementation rather than the named classes.
- **Edited:** live PR #5003 body. The two fail-before clauses are cut. Limits names the follow-up.
- **Pushed:** `solr-7394-recovery` from `9857d9ee802` to `a92a0d89b20` (fast-forward, 2026-10-13). The changelog title now carries the recorded wording: "Clear the recovering shard-terms entry and restore the pre-recovery term when recovery fails permanently." The title change was already applied to the PR earlier.
- **Owed:** changelog YAML parse and diff check on a Linux host, `gates/SOLR-7394-changelog-title.md`. The receipt stays at `9857d9ee802` until then.

## SOLR-18280 (a): nightly head failure

- **Decision:** the head failure is classified as a clock-dependent timing flake. It is one failure in two valid nightly head runs. The 09:26Z run executed no tests (nightly group off) and is not counted.
- **Record:** the nightly record run at `0da92abd9e96` on 2026-10-13 (14:11Z to 14:23Z) passed 25 of 25 with seed `18280C0FFEE18280` (`receipts/SOLR-18280.md`). The base comparison at `0d2a4649c79a` (same production) passed 25 of 25 in the 12:55Z run.
- **Owed:** the seed and reproduce line for the failing 12:55Z head run are not recorded (`gates/SOLR-18280-nightly-record-run.md`, line 15). vm2 reads them from its own logs: `gates/SOLR-18280-failing-run-seed.md`. Do not relaunch.
- **Edited:** live PR #4996 body. Proof date set to 2026-10-13. The unrecorded 2026-10-04 date, "2 of 2" and "24 of 25, twice" counts, and the claim that the base tree also fails are removed. The Proof now states the recorded nightly results. Limits states the timeout without the reproduces-on-base claim, which the base record contradicts.

## SOLR-10667 and SOLR-12347 (vm1)

- **Decision:** already decided in `assignments/pool-vm2-gate-backlog-round-3.md`. vm1 takes both under the NPM cache workaround. vm2 leaves both alone. Windows runs neither.
- **Status:** the vm1 retry claims (`claims/pool-vm1-gate-10667-retry.md`, `claims/pool-vm1-gate-12347-retry.md`) have no heartbeat since 2026-10-11, past the 2-hour staleness rule in `WORKFLOW.md`. vm1 must refresh or take over the claims before it runs.
- **Check first:** the SOLR-10667 exclusion gate (`gates/SOLR-10667-exclusion.md`) is DONE green at `9ce9ef1db5e` with proof leg rc=0. vm1 should confirm whether it already covers the premise in `gates/SOLR-10667.md` before spending a run on it.

## SOLR-11431 (pr-drift item d)

- **Decision:** nothing owed. Recorded at 2026-10-13T10:55Z: TestCoreContainer total 25 (`receipts/SOLR-11431.md`). The body's 25 is correct.

## Live PR bodies edited (2026-10-13)

- #5003 (SOLR-7394), #4996 (SOLR-18280), #5010 (SOLR-12998): body text only, via the REST pulls endpoint. Each edit matched exactly one existing sentence or paragraph, and each read-back matched the intended body. No title, state, comment, or review change. The AI header and footer are unchanged.
