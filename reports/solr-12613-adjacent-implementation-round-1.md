# Adjacent node-routing follow-ups: implementation rollup, round 1

Status: Implemented, Proof Pending. Source authoring and fork publication are complete for four independent slices; required Linux results are not yet present.

Owner direction: active goal to implement the adjacent plan using separate branches; releases the earlier implementation hold. Assignment `assignments/solr-12613-adjacent-implementation-round-1.md`; claim `claims/solr-12613-adjacent-windows-round-1.md`. The earlier planning deliverable is `reports/solr-12613-adjacent-followup-plan-round-1.md`.

## Branches and exact source state

| Area | Fork branch | Verified pushed head | Own delta |
| --- | --- | --- | --- |
| C2 encoded targeting keys | `pr-5110-followup-encoded-key` | `bd05d1ed39a005317f0a6655e4cd8fed078531b8` | 3 files, production fix + 7 new tests |
| C5 encoded second-hop paths | `pr-5110-followup-path-coverage` | `96747864862bf716daf451e02a7c24194d41c267` | 1 file, 2 coverage tests, production unchanged |
| C3 core-menu lifecycle | `pr-5110-followup-selection-lifecycle` | `e59bc25bf6dda7d3ca05ec6aad727d6ed7c51bd4` | 2 files, callback guard + 2 tests |
| C4 per-request reads / SOLR-18619 | `pr-5110-followup-request-targeting` | `4d8c0a5a94528514aea41c429bb5009fd82a9ffa` | 12 files, UI migration, 6 new tests, docs/changelog |

All heads were separately read back from the fork API after ordinary pushes and checked again with ls-remote. Each task worktree is clean. Every follow-up commit names Nick Shanin with the configured ICLA email and no attribution trailer. No history rewrite, contributor-branch change or upstream push occurred. Seventeen new test methods were authored in total; this is a source inventory, not an executed-test result.

## Integration baselines

- Core slices C2/C5 use `6ec2e527ad716bde74404b2037bb1a1e6c52f79c`, a conflict-free ordinary merge of main `07ccc73cd24e26e59da169ca35b3fd5f64c35986` and PR 5110 `73b30956ff2c0f7b16f6af3bceacf95bda8271b9`.
- UI slices C3/C4 use `b45bae5da3a151c7b439ff4e42009c383b538fae`, which adds the existing, separately gated Selenium/property prerequisite head `35b5c45ec6ddb8ec8d54b89bbf9ca97368d2a8ca` by another conflict-free ordinary merge. The prior receipt proves only those prerequisites. They were reused with their ancestry rather than duplicated as new fixes.
- PR 5110 remained OPEN at the same contributor head immediately before the last implementation push. Main and picker ancestry were verified for all four heads. Compare each candidate to its recorded baseline for its own delta; a main comparison currently contains the original unmerged picker change.
- When the picker lands upstream, reconcile each desired follow-up with the actual merge result. Any source/head change requires fresh exact-head proof before readiness. These are independent patches, not an already-tested combined series.

## Area dispositions and architectural scope

C1 was not duplicated: inspected SOLR-18549 fork head `b89664d164db46cdac432668e2d9a07ce95fbd37` already preserves SolrException in GetNodeSystemInfo. Its current author/gate remains responsible for that production change; the old claim of a newly available small fix was corrected by source evidence.

C2 recognizes escaped query keys and consumes them, preserving GET-only targeting and header/query conflict behavior. Parse errors are handled inside the filter to retain their HTTP status through authentication. C5 combines encoded paths with an actual additional collection hop and asserts `_forwardedCount=1`; it is coverage-only and has a separate mutation gate.

C3 captures generation and selected node before parent-menu core reads, discarding obsolete completions. Its tests send normal generated requests, hold the controller callbacks after network completion, then deliver controlled valid payloads in the opposite order. They cover a node change and repeated same-node refreshes; they do not claim a physical network outage.

C4 uses a small source-owned Angular $http helper with a fresh header map per read. It handles Metrics, per-node Nodes system information and log messages without changing server schemas or generated output. Its two concrete client families were examined: Metrics keeps its promise-facing parsed result and existing raw local behavior; system/log reads consume native JSON. Angular's existing interceptor carries authentication. The pending-target generated-client plugin is not claimed as a proven race and is retained for unaffected callers.

The C4 proposal preserves selected-node logs by default and offers a cloud-only All nodes control, origin labels, partial-failure visibility, recovery and untargeted standalone reads. Single-node badges are hidden while aggregating. Polls settle before the next is scheduled; obsolete/destroyed callbacks are dropped. Log-level broadcast stays separate. See `claims/solr-12613-adjacent-decision-ladder-round-1.md`: rungs 1-3 describe the two feature intents; rung 4 selects a reviewable proposal and keeps the product default open to the maintainer, as Nick specifically requested. The C4 report contains the unposted PR draft and question about always aggregating cloud messages versus explicit opt-in. No new public communication was authorized or posted.

## Required proof and records

| Area | Gate | Receipt target |
| --- | --- | --- |
| C2 | `gates/solr-12613-adjacent-encoded-key-round-1.md` | `receipts/PR-5110-adjacent-encoded-key-round-1.md` |
| C5 | `gates/solr-12613-adjacent-path-coverage-round-1.md` | `receipts/PR-5110-adjacent-path-coverage-round-1.md` |
| C3 | `gates/solr-12613-adjacent-selection-lifecycle-round-1.md` | `receipts/PR-5110-adjacent-selection-lifecycle-round-1.md` |
| C4 | `gates/solr-18619-request-targeting-round-1.md` | `receipts/SOLR-18619-request-targeting-round-1.md` |

Each gate names full head/baseline, fixed seed, fully qualified focused/neighbor tests, tidy/root checks, fresh logs/XML and exact failure criteria. C2/C3 require fail-before assertions on their integration baseline plus new tests. C5 requires both unmutated suites to pass plus an isolated valid-API path mutation. C4 keeps its feature scaffold intact for isolated header, origin, failed-node and stale-event mutations; missing APIs/controls do not count as proof. Positive assertions without a discriminating mutation remain coverage pins. No runtime/client-certificate claim is inferred from general green neighbors.

The four implementation reports are `reports/solr-12613-adjacent-encoded-key-round-1.md`, `reports/solr-12613-adjacent-path-coverage-round-1.md`, `reports/solr-12613-adjacent-selection-lifecycle-round-1.md`, and `reports/solr-18619-request-targeting-round-1.md`.

## Audited limits

At board `3f220236cec7685a39e3a97c694f31ea7e93512c`, all 11 assignment/claim/slice-report/gate files were fetched and their full normalized content compared with the local outbound drafts. The branch audit verified clean worktrees, exact file counts and baseline/main/picker ancestry, ICLA authorship, no trailers, diff --check and live fork heads. Source annotation counts match the gate inventories. None of the four required receipt paths exists at that snapshot.

Windows ran no Gradle, compiler, browser, reproduction or test job. All test outcomes remain unproven. Do not mark the active goal or the code phase complete based on pushed source or posted gate jobs. Source/format repairs must be pushed and gated at their own exact head. A posted gate is not an execution claim; the next required work is Linux proof and any repairs it establishes, followed by owner review of publication and the stated logging choice.
