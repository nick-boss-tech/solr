# Report: PR 2452 UBI recording context fix, rework and Linux gate, round 1 (vm4)

Status: Implemented, Proof Pending. Test legs green at `bbc00109d0`. Root check blocked by a pre-existing forbidden-API violation in the PR head.

## Heads and base

- Contribution base B: apache/solr PR 2452 head `6402b4b528d3ec8daa134772abc84532380e3f7c`, live at claim time. Upstream main at check time: `9859d8bb2e`. The preferred path was used; the main-based fallback was not needed.
- Candidate F: `bbc00109d07b9d53e4c30659c28e626c48dcfca4` on `nick-boss-tech/solr:ubi-pr2452-context-submit`, verified on the remote. Three commits above the PR head's parent, two above B:
  - `37be6eedfe` "UBI: isolate recording context per request": the ported fix from `09343928e7` (`solr-11994-ubi-context-submit`), `UBIComponent.java` and `UBIRecordingContextTest.java`.
  - `bbc00109d0` "UBI: format recording context test": one rewrapped line in `testDisabledRecordingLeavesNoRecord`, formatting only. Needed because tidy changed the test at `37be6eedfe`.
- Final test blob: `886244b0025ac22b747dfcaa3bf1b5a963bfc618`. Ported `UBIComponent.java` blob: `b08770ef5f41c352a4098618924b03e3f7a3e5ce`, identical to the source fix.
- Preserved: `solr-11994-ubi-context-submit` at `09343928e7`, `solr-11994-pr5238-ubi-submit` at `c2c387064a`, `refs/pull/2452/head`.

## Fix

`UBIComponent.recordQuery` builds a fresh `StreamContext` per recording. It copies the read-only initialization entries and the SolrClientCache, then puts the request's `ubi-query` in that copy. The shared component context is no longer mutated per request. The diff against B is 8 lines in `UBIComponent.java` and the new test file.

## Legs (Linux, standalone clone `/workspace/worktrees/pr2452-clone`, JDK 21.0.12.1, Gradle 9.7.0, `--no-daemon --no-build-cache`, host lock)

Driver: `/workspace/gates/logs/vm4/pr2452-ubi-context-round-2/run.sh`. Gate: `gates/pr-2452-ubi-context-round-2.md`.

| Leg | Head | Result (fresh XML) |
|---|---|---|
| Fail-before, seed 11994C01 | B with final test overlaid | 5 run, 2 failed, 0 skipped. Both overlap cases fail with expected `[query-B, query-A]`, actual `[query-B, query-B]`. Controls pass. |
| Pass-after, seed 11994C01 | F | 5 / 0 / 0 / 0 |
| Pass-after, seed 11994C02 | F | 5 / 0 / 0 / 0 |
| Neighbors, seed 11994C01 | F | `UBIComponentTest` 7, `UBIComponentStreamingQueriesTest` 3, `LoggingStreamTest` 4, `TestSolrConfigHandler` 8, `TestSubQueryTransformerDistrib` 1. All 0 failures, 0 errors, 0 skipped. |
| Tidy | F | Passed. Changed one line in the test at `37be6eedfe`, which is why `bbc00109d0` exists. Clean at `bbc00109d0`. |
| `git diff --check` | B..F | Clean. |
| Root `check -x test`, NODE_ENV unset | F | FAILED in `:solr:core:forbiddenApisMain` (rc 1, 6m35s). |

Logs: `/workspace/gates/logs/vm4/pr2452-ubi-context-round-2/` (`baseline-fail-before-xml/`, `test-pass-c01-xml/`, `test-pass-c02-xml/`, `neighbors-c01-xml/`, `check.log`).

## Root check failure: pre-existing in the PR head

- Violation: `Forbidden method invocation: java.util.Collections#singleton(**) [Use Set.of()]` in `UBIComponent.java`.
- The same call is at line 391 of PR head B, `Set<String> fields = Collections.singleton(schema.getUniqueKeyField().getName());`. The fix commit moved it to line 395.
- Confirmed by running `:solr:core:forbiddenApisMain` on B alone: it fails with the same violation (`baseline-forbidden-main.log`, BUILD FAILED). So the repair did not cause it.
- Consequence: the root check cannot pass at F without touching a line outside the race fix, and the same is true of PR 2452 at its own head.

## Attempts that did not run tests

- Round-1 driver (`/workspace/gates/logs/vm4/pr2452-ubi-context-round-1/`): a wrong full SHA was typed for B and the baseline failed at once (`misfire-1/`). The seed-1 retry then failed in `cleanTest` (`test-pass-c01-attempt1-cleantest-setup-failure.log`) and seed 2 did the same (`test-pass-c02-attempt1-cleantest-setup-failure.log`). Both cleanTest failures came from orphan Gradle daemons from this gate holding a file in `build/test-results/test/binary`. Those daemons were stopped, and the driver now reaps them before each step. Test XML copied after the cleanTest failure was stale and is not cited.
- Round 1 proved seed 1, seed 2 and the neighbors at `37be6eedfe` (5/5, 5/5, 23 green). Those results are superseded by round 2 at `bbc00109d0` and are not cited for the final head.

## Limitations

- No Windows run.
- Not a proof of the full UBI draft, a listener migration, background ingestion, multi-shard merging, redaction, or production overhead.
- The root check is not green at F.

## Decision needed before an offer

The candidate is test-green but the root check is blocked by a violation in the PR head's own code. Options:

1. Replace `Collections.singleton(...)` with `Set.of(...)` on the affected line of `UBIComponent.java` as a separate commit on the candidate, push it, and add a superseding gate. This changes a line outside the race fix, so it needs approval.
2. Offer the race fix as is, and report to the PR author that the head fails `forbiddenApisMain` on that line. This does not claim a green check.

## Offer draft (not posted; wording depends on the decision above)

PR 2452 UBI recording context. A proposed narrow fix: each recording builds its own StreamContext, so overlapping requests stop sharing the single `ubi-query` slot. A test in `UBIRecordingContextTest` fails on the PR head with overlapping local and distributed requests recording the wrong query ID, and passes with the fix at `bbc00109d0`. This addresses recording-context isolation only; it does not address readiness of the full draft. Take-in on the PR head: the candidate is two commits above `6402b4b528`, and the first one is the fix. Ancestry against the live author head must be verified before any fast-forward is suggested. Note that the PR head currently fails `forbiddenApis` at `UBIComponent.java` (`Collections.singleton`, use `Set.of`).

## Round 3 completion (phone agent, 2026-11-01)

Status update: this section supersedes the Status line at the top of this report and the round 1 and 2 root check entries. The round 3 gate is complete at candidate F `b48c4509a0375f93080f8be50520c18dba23aa29`, with one scope limit stated at the end: the two yarn-dependent root check tasks remain unproven, on every host tried so far, for an environment reason.

- Round 3 candidate: F is one commit above the round 2 candidate `bbc00109d0`: `b48c4509a0` "UBI: use Set.of for the unique key field set", the separate static-check commit vm4's decision record chose (option (a) in the decision section above). Base B unchanged: `6402b4b528d3ec8daa134772abc84532380e3f7c`. At closeout the fork branch `ubi-pr2452-context-submit` was verified live at F, the phone agent's clone stood clean at F, and `git diff --check B..F` was clean.
- Test legs at F (vm4, before its host crash; recorded in the handoff in `claims/pr-2452-ubi-context-rework-vm4-round-1.md`): fail-before on B, 2 of 5 failed at the identity comparison (expected `[query-B, query-A]`, actual `[query-B, query-B]`); pass-after 5/5 at seeds `11994C01` and `11994C02`; neighbors 23/23; tidy clean.
- Root `check -x test` at F (phone agent; real clone, not a worktree; JDK 21.0.12.1, Gradle 9.7.0 via solr-gradle.sh under the test-queue flock; NODE_ENV unset; logs in `~/workspace/tools/gate-2452-round3/` on the phone agent VM):
  - Attempts 1 and 2 failed before any task ran: the sandbox egress CA had rotated its key under the same name, so Gradle downloads failed PKIX validation with the truststores then in use. Environment, not the candidate. The fix is the combined store `~/workspace/tools/truststore-with-hatch-ca.jks` (vendored JDK cacerts plus the current Hatch CA from the system bundle).
  - Attempt 3 (git worktree, fixed truststore) got past dependency downloads and failed only on `:rat`, the known worktree trap: rat reads the git index only when `.git` is a directory, so in a worktree it scans files CI excludes. Environment, not the candidate.
  - Attempt 4 (real clone, unmodified `check -x test`): 587 actionable tasks, 583 executed, BUILD FAILED only on `:kotlinWasmNpmInstall`. The UI build's bundled yarn 1 cannot fetch npm packages through this VM's egress (TLS closes inside the CONNECT tunnel); the same hard limit forced the PR 5258 wasm leg onto vm6. Every other task passed, including `:solr:core:forbiddenApisMain` executed at F: the round 2 blocker is fixed by the `Set.of` commit.
  - Attempt 5 (excluding `:kotlinWasmNpmInstall`): BUILD FAILED only on its sibling `:kotlinWasmToolingSetup`, which runs the same yarn install.
  - Attempt 6 (excluding both yarn tasks): BUILD SUCCESSFUL in 5m 13s, 595 actionable tasks (194 executed, 2 from cache, 399 up-to-date), rc=0 (`root-check6.log`).
- Verdict, stated with its limit: at F, every root check task that does not depend on the yarn npm install is green on this VM, and the test proof pair (fail-before, pass-after at two seeds, neighbors, tidy, diff check) is complete. The unmodified root check with the yarn tasks included is proven nowhere yet; the yarn-dependent tasks (`:kotlinWasmNpmInstall`, `:kotlinWasmToolingSetup`, and anything downstream of them) need a host whose egress lets yarn fetch npm packages. That leg is posted as a narrow assignment: `assignments/pr-2452-ubi-context-yarn-leg-round-1.md`.
- The offer draft above stands as a draft. Nothing was posted to PR 2452; an offer comment needs Nick's go, as before.
