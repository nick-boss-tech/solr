# Gate: SOLR-17914 Windows candidate, browser fix round 1

Status: SUPERSEDED by `receipts/SOLR-17914-wasm-test-resources.md` (green at PR #5258 head `bb7a4f32eb`). The candidate `252e0a75bb` is not used. This file does not replace the original assignment's acceptance rules.

- Parent assignment: `assignments/solr-17914-browser-fix-round-1.md`.
- Implementation report: `reports/solr-17914-browser-fix-round-1-windows-implementation.md`.
- Required tags: `ui`, `gate`. Intended host: any capable Linux UI gate host with Chromium and the established software-renderer setup.
- Candidate fork branch: `solr-17914-browser-fix-round-1`.
- Exact candidate head: `252e0a75bb0d6a9183c8cc879da60b809b52c242`.
- Original dialog head: `6170ba10ac5061203339fa0a96e714c7e64616c2`.
- Inherited SOLR-17913 base: `0a04d2239ee1c9a1dfe68cbe84f094ca231130b9`.
- Outbound code: only `CreateCollectionFormTest.kt` and `CreateCollectionErrorsTest.kt` under `ui/shared/src/commonTest/kotlin/org/apache/solr/ui/views/collections/`.
- Claim before execution. One build at a time under the claiming host's established build lock; heartbeat its host record at least hourly.
- Logs: `/workspace/gates/logs/<host>/solr-17914-browser-fix-round-1/`, including commands, environment, exit codes, browser console, and copied fresh XML per input.

## Why this candidate exists

The original browser receipt has six missing resource-backed strings while JVM tests pass. Compose 1.12.1 loads web resources asynchronously with an initial empty string. The candidate explicitly loads the required resource strings before mounting the dialog in the four failing dialog tests, awaits composition, and scrolls to the router before clicking. It preserves every original assertion, callback-value expectation, and test. No production source, shared harness, or inherited card test was changed. The diagnosis is source-based and awaits browser confirmation.

## Required execution, separated by input

1. Verify the fork candidate head and clean tree; perform the UI-specific formatting preflight with `./gradlew -p ui :shared:spotlessCheck --no-daemon --rerun-tasks`. Do not silently change a tested head. Any formatting or scoped repair commit must be pushed and named as a new input in an add-only board record before counting proof for it.
2. Reproduce the original browser leg at `6170ba10ac5061203339fa0a96e714c7e64616c2` in a separate clean worktree: `./gradlew -p ui :shared:wasmJsBrowserTest --no-build-cache --no-daemon --rerun-tasks`. Use the round-1 environment: `CHROME_BIN` set to the actual browser, `LD_LIBRARY_PATH=/workspace/tools/mesa-egl/lib`, and the host's build lock. On vm4 the recorded browser was `/workspace/tools/chrome-156/chrome-linux-arm64/chrome` and lock `/workspace/tools/.gradle-vm4.lock`. Other hosts must record their actual equivalents and any material difference. Preserve fresh XML and first error text for all six tests; the previous receipt is historical evidence, not the new run.
3. Run the browser leg at the unchanged SOLR-17913 base `0a04d2239e` with the same environment and command. Record whether each of the two `CollectionDetailsCardTest` methods fails there. Do not fold a base repair into the dialog candidate. Resolve a resource/environment exception with evidence before classifying it as category (c).
4. At the exact candidate head, run the full original gate command: `./gradlew -p ui check --no-build-cache --no-daemon --rerun-tasks`, under the same host lock and browser environment. Report task exit status and fresh XML totals, skips, failures, and errors separately for wasm browser tests and JVM tests; report formatting status. All four previously failing dialog tests must pass, and none of the previously passing tests may regress.
5. Capture the four screenshots by the method agreed in `reports/solr-17914-browser-fix-round-1-main-agent-direction.md`: real Chromium captures of the production form in `SolrTheme`, with an isolated temporary wasm browser fixture permitted for empty, advanced-open, name-exists, and generic-failure states. Keep fixture code out of fix commits. Publish PNGs under `receipts/SOLR-17914-screens/`; record head, viewport, device scale, browser, exact fixture setup and capture command. If the agreed browser method cannot capture them, record the blocker and stop at that point, without substituting JVM or design-reference images.

## Attribution and closeout

- Classify each named failure as (a) branch files, (b) SOLR-17913 base, or (c) shared harness/environment, using the reproduction evidence. The implementation report proposes (a) for the four dialog test-readiness failures and (b) for the two unchanged card tests, but execution must confirm or correct that attribution.
- Fixes here remain limited to category (a). Shared harness changes and base changes require a separate owner-directed slice. Do not disable tests, remove assertions, substitute tags for text checks, or report stale XML as new proof.
- If the four dialog tests are green and the inherited two card tests remain red, report the exact partial result. The full gate is still RED, and no green browser-proof line is drafted. This is a valid closeout of the execution handoff, with the base follow-up clearly identified; it is not full assignment completion.
- The executing host owns `receipts/SOLR-17914-browser-fix-round-1.md`, as the original assignment requires. Include all input SHAs, source and executed attribution, per-test before/after results, exact commands/environment, exit codes, XML counts, remaining failures with first error text, and screenshot status.
- Only if the full required gate is green, produce `drafts/pr-5258-proof-line-update.md` with the exact replacement proof line. PR body edits remain outside this job.
- Push candidate repair commits only to `solr-17914-browser-fix-round-1`, fast-forward only. Never push to the open PR branch `solr-17914-create-collection-dialog-new-design`. No GitHub/Jira comments, PR edits, or other ticket branch changes.
