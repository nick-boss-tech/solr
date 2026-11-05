# Core-menu initialization and lifecycle implementation, round 2

Status: Implemented, Proof Pending.

- Fork `pr-5110-followup-selection-lifecycle`; new pushed head `a331e7faf96c8c215f2d623f4865d4cbabca6492`, separately read back after a plain fast-forward push from `e59bc25bf6dda7d3ca05ec6aad727d6ed7c51bd4`.
- UI baseline remains `b45bae5da3a151c7b439ff4e42009c383b538fae`, integrating main, picker and the earlier gated browser prerequisites. No input ancestry was rewritten.
- Gate `gates/solr-12613-adjacent-selection-lifecycle-round-2.md` supersedes/withdraws round 1; receipt `receipts/PR-5110-adjacent-selection-lifecycle-round-2.md`.
- Outbound files remain app.js and AdminUiNodePickerTest.java under the full paths named in the gate.

Source re-audit established that the parent started a core read while mode was still unknown. With a persisted remote selection, localNode was not yet known, so withNodes issued an untargeted read. Merely checking generation and selected name when the callback arrives does not prove the request used the intended target. The parent now waits for system/live-node initialization in cloud mode, loads locally after standalone detection, and retains a permitted local-core fallback after metadata discovery failure. A small parent-local clearing helper keeps target/menu-selection state consistent in both fallback paths. The earlier obsolete-response guards remain.

Two additional tests cover pre-bootstrap outgoing core headers during persisted-selection reload and a controlled system-info 403 followed by a real local core read. ChromeDriver is already the browser constructed by AdminUiTestBase, so the startup-only test uses a scoped CDP new-document script and removes its registration in finally. It records URL/target only, never authentication data. The failure fallback is controlled callback evidence, not an authenticated role reproduction.

The Linux job now requires all 8 picker methods, neighboring cloud/standalone suites, tidy/root checks and four intended fail-before assertions against the original UI baseline. No runtime or browser result was obtained on Windows. Diff/source scope were reviewed; diff --check passed. Commit identity is Nick Shanin, no attribution trailer; the full patch is pushed.

## Current rollup amendment

The round-1 implementation rollup remains a historical snapshot. Replace only its C3 head/gate/receipt with the values above. Other current heads stay encoded-key `bd05d1ed39a005317f0a6655e4cd8fed078531b8`, path-coverage `96747864862bf716daf451e02a7c24194d41c267`, request-targeting `4d8c0a5a94528514aea41c429bb5009fd82a9ffa`. The authored new-method inventory is now 19 across the four branches; it is not a passing-test count. Source file counts and branch baselines are unchanged. The original planning hold remains released by the active implementation goal.

No required Linux receipt or execution claim was present at the revalidated board snapshot before this update. Old-head C3 jobs cannot prove the new head. The active goal remains incomplete until the matching Linux receipts establish the named proof legs. No public PR/comment/Jira action was made, and the C4 logging choice remains in its unposted draft for maintainer review.
