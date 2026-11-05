# SOLR-12613 Add Core timeout: source diagnosis and separate repair candidate

Status: IMPLEMENTED, PROOF PENDING. Assignment `assignments/solr-12613-addcore-neighbor-timeout-implementation-round-1.md`; claim `claims/solr-12613-addcore-neighbor-timeout-windows-round-1.md`. Windows Codex desktop, 2026-11-05. No Gradle, compiler, formatter, Selenium or test queue ran on Windows.

## Receipt and failure evidence

Read round-2 receipt, assignment and handoff from board `efcffaefd4e41969baf131de5cbbd813d9c0b237`, with underlying receipt evidence pinned by the assignment at `268dde55a9b034280616d733919d2eb1ddc40a0d`. Root board rules/host instructions were unchanged from the previously read snapshot. No competing Add Core claim at intake. The new claim landed at `93d31ad4eacaf54237c35a06567e305dd7cf8a85`.

Round 2 remains NOT GREEN: F2 picker isolation is confirmed (8/8 and four intended failures together), but F2 neighboring selection at `5110C0AE` is 16/17 with `testAddCoreViaUi` failing to type into `add_name`. P at that seed and P/F2 at `12613F00D` passed 17/17. Do not infer a flake or a candidate regression from those comparisons.

Inspected the actual [captured page](https://github.com/nick-boss-tech/solr/blob/268dde55a9b034280616d733919d2eb1ddc40a0d/receipts/SOLR-12613-pr-head-lifecycle-round-2-evidence/steps/neighbors/f2-neighbors-5110C0AE/failure-debug/ui-failure-testAddCoreViaUi-001/page.html), [screenshot](https://github.com/nick-boss-tech/solr/blob/268dde55a9b034280616d733919d2eb1ddc40a0d/receipts/SOLR-12613-pr-head-lifecycle-round-2-evidence/steps/neighbors/f2-neighbors-5110C0AE/failure-debug/ui-failure-testAddCoreViaUi-001/screenshot.png) and empty browser console. Screenshot shows normal standalone Core Admin, `swapa` selected, Add form closed, other cores present. HTML has `.action.add.ng-hide` at line 195 and the Add button enabled at line 188. The input exists in the hidden form; the captured state does not show a crashed page or an authorization denial. Screenshot/HTML are a final state, not a callback timing trace.

## Source-level diagnosis and limit

The original `testAddCoreViaUi` navigates to bare `~cores`, waits for the Core Admin anchor, clicks Add and starts typing. [CoreAdminController at F2](https://github.com/nick-boss-tech/solr/blob/b0e096ae19ad637107227e1bc04e951b9023a680/solr/webapp/web/js/angular/controllers/cores.js#L20-L57) starts an asynchronous native status read. If no core was in the route and the response has cores, it sets the first core and unconditionally redirects to `/~cores/<first>`. The bare and core-specific routes instantiate CoreAdminController separately. An Add action can set `showAdd` and typed fields before that callback; the subsequent redirect replaces the view and loses that form. Parent menu requests explicitly pass indexInfo:false, while this controller's own read uses empty options.

This gives a concrete source race consistent with the captured first-core/closed-form state. Both the Core Admin controller and standalone test were byte-identical between P and F2, verified by diff, so this is not code newly introduced by the F2 isolation repair. The original failure's precise event order was not recorded. No diagnostic runtime ran here; Linux must establish the controlled reproduction and whether the original full neighboring selection now passes. This report does not claim the historical timeout's cause conclusively proven.

## Scoped implementation

New branch `nick-boss-tech/solr:solr-12613-addcore-initial-status` starts at exact F2. It does not modify `pr-5110-core-menu-lifecycle`, F2, the contributor branch, the approved PR, its comments or any failed receipt.

| Item | Value |
| --- | --- |
| Original contributor P | `6b5d830132b941faa85856347099ade8b190be0e` |
| Preserved F2 parent | `b0e096ae19ad637107227e1bc04e951b9023a680` |
| New candidate G | `75a18ecd07925a26a869dac8af8eed9ab4176bb9` |
| G parent | F2 only; ordinary Nick Shanin author/committer, no trailers |
| F2..G delta | `solr/webapp/web/js/angular/controllers/cores.js`; `solr/webapp/src/test/org/apache/solr/webapp/AdminUiCoreAdminStandaloneTest.java`; +108/-1 |
| Complete P..G offer | Four files: the above two plus existing app.js and AdminUiNodePickerTest; +370/-15 |
| Worktree | `C:/Users/shaninna/dev/Solr-issues/wt/SOLR-12613-addcore-initial-status` |

[Add Core repair diff](https://github.com/nick-boss-tech/solr/compare/b0e096ae19ad637107227e1bc04e951b9023a680...75a18ecd07925a26a869dac8af8eed9ab4176bb9). [Complete P-to-G proposal](https://github.com/nick-boss-tech/solr/compare/6b5d830132b941faa85856347099ade8b190be0e...75a18ecd07925a26a869dac8af8eed9ab4176bb9).

Production guard suppresses only the automatic default-core URL change while `showAdd` is true. First-core data is still selected/populated behind the form. When Add is closed, the normal redirect remains. Submit/cancel, existing remote mutation checks, zero-core automatic form handling, backend APIs, requests and permissions are unchanged. No sleep, timeout increase, skip, retry loop or changed original Add Core test is used to hide the failure.

Two methods extend the existing standalone class from 5 to 7. A shared local helper boots a fresh app with existing openPage, wraps the existing CoresV2 service, lets the real native request run, and holds only delivery of the first controller response (distinguished from parent menu reads by indexInfo). It opens Add and types values before releasing that response, then checks route, form visibility and both values. A second case keeps Add closed and checks default-core routing remains. Real response data is used; no invented core inventory, new fixture/dependency, Authorization headers or HTTP request objects are captured. Wrapper is restored before delivery and in finally. The closed-form case is a compatibility control, not an old-production bug expectation.

Existing cluster/home, ChromeDriver, browser waits, generated API and standalone fixtures are reused. Root AGENTS instructions apply; no nested webapp AGENTS existed at F2. New source files were not introduced. The contributor's existing unreleased feature entry stays untouched; no independent public ticket/PR or duplicate release fragment is created by this internal repair. Any standalone public packaging decision remains later work.

## Verification, source readbacks and next gate

Windows inspected the complete scoped diff, seven method inventory, routing/controller/helper source, captured screenshot/DOM/console, and original source equality. diff --check passed; long Java lines were hand-wrapped. The scoped helper verifies fork URL, branch, F2 ancestry, exact outbound list, Nick's identity and clean tree. Plain push created only the new fork branch. Separate GitHub refs API read returned G in full after push completion. P and F2 remain unchanged; a final live check is required before publication of any eventual offer.

New job `gates/solr-12613-addcore-initial-status-round-1.md` requires fresh proof at G. Deterministic Add case must fail on only restored F2 cores.js at the explicit route assertion, while normal-navigation control passes. A navigation-removal mutation must fail the compatibility control. Full 7-method standalone suite and the expanded 19-method neighbors must pass. The exact original 17-method selection at the old seeds must also pass, with logged method order, rather than counting only a changed selection. Existing lifecycle picker/combined original-production proof, root tidy/Error Prone/client/root checks and original-P controls are included for the complete offer.

Until that proof exists the source race repair is a candidate, not a green fix or a cleared historical timeout. Preserve both failed receipts and all controls. If the original Add Core timeout persists or the deterministic failure is not the intended assertion, investigate before retrying and do not waive it. The new assignment's implementation deliverables are complete with this pushed candidate and exact-head gate handoff; the required Linux proof is the explicit remaining leg. No public comment was posted.
