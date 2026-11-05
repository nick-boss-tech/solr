# SOLR-12613 receipt audit and picker isolation repair

Status: IMPLEMENTED, PROOF PENDING at F2. Round-1 receipt remains NOT GREEN and is preserved. Host: Windows Codex desktop, 2026-11-05. Claim: `claims/solr-12613-pr-head-lifecycle-isolation-round-2-windows.md`.

## Receipt audit

Read the actual round-1 receipt, failure verdict, evidence index, fresh candidate XML and pinned source at board `3a860c33f4f5ac750c7c80cac7cffaded38a2297`. Refreshed tip `3a926cc3bd7c7dfb72116a51863d056542837a0a` added only an unrelated reconnect claim; our repair claim landed at `92dd7efb642d9b3589b6d8e6e52c8c873d22e4e0`. No competing lifecycle repair claim was found before implementation. WORKFLOW's change now explicitly permits verified Linux Selenium hosts, including phone-agent-2.

The receipt is insufficient for a tested offer: P picker 4/4 passed, but F1 picker was 8 tests with 6 failures and 2 passes. Independent inspection of the fresh F1 XML confirms the exact execution order:

1. `testCloudLandingShowsNodesAndEmptyNodeSelector`: PASS.
2. `testSystemInfoFailureStillRefreshesLocalCoreMenu`: PASS.
3. Every remaining method (navigation, restored initial request, reload selection, same-node response ordering, logging-level badge, cross-node response ordering) fails in `clearPersistedNodeSelection`, waiting for visible `node-selector-item` at line 40. Each failure is before its test body.

The receipt also records all four intended old-production assertion failures (three reached in isolated runs after combined setup masking), four complete paired neighbor selections of 17/17 at the two seeds, tidy, Error Prone, exact-source client generation and root check. Those are useful partial results, not a green candidate or proof for F2. The wasm checksum failure was correctly recorded as setup and repaired with a pin-matching cached archive. The older Windows AddCore timeout did not reproduce on either actual head at the old seed; its historical cause remains unproven.

## Diagnosed setup boundary and repair

The new metadata-failure test deliberately settles MainController to non-cloud mode and hides the picker. Its finally block restores generated API functions but does not restore the app's mode/selection state. ChromeDriver is class-scoped (`AdminUiTestBase.startClusterAndBrowser`, @BeforeClass). Picker @Before originally navigated directly to the same `index.html#/` and waited for the picker. Hash navigation can preserve the previous Angular app, so the wait itself can prevent the next test from reaching its own fresh-page helper.

The existing [openPage helper at F1](https://github.com/nick-boss-tech/solr/blob/f02bb7bb06decce976c3ed2fdfbf5907764c2183/solr/webapp/src/test/org/apache/solr/webapp/AdminUiTestBase.java#L313-L338) explicitly documents this boundary and forces refresh when the same document is active. The six consecutive setup failures immediately after the metadata test are consistent with this source-level isolation defect. Confirmation that the correction clears the runtime failure belongs to round 2; no successful rerun is inferred.

Replaced the picker's three-line direct-navigation prelude with `openPage("", By.id("index"))`, retaining the subsequent sessionStorage clear. Reuses existing fresh-app/local-selection/wait behavior; adds no helper, skip, timeout, driver reset, thread filter, dependency or production change. All four regression bodies and assertions remain unchanged. Tests still exercise normal generated API construction and the existing CDP capture boundary.

## Pushed source

| Item | Value |
| --- | --- |
| Contributor PR/base P | https://github.com/apache/solr/pull/5110 at `6b5d830132b941faa85856347099ade8b190be0e`, still OPEN |
| Fork branch | `nick-boss-tech/solr:pr-5110-core-menu-lifecycle` |
| Failed gate head F1 | `f02bb7bb06decce976c3ed2fdfbf5907764c2183` |
| Repair/current F2 | `b0e096ae19ad637107227e1bc04e951b9023a680` |
| F1..F2 | One test file, +1/-3; no production JS change |
| Commit | Ordinary Nick Shanin author/committer, no trailers; only parent F1 |
| P..F2 | Same two-file lifecycle offer: app.js and AdminUiNodePickerTest.java |

[Repair diff](https://github.com/nick-boss-tech/solr/compare/f02bb7bb06decce976c3ed2fdfbf5907764c2183...b0e096ae19ad637107227e1bc04e951b9023a680). [Complete author offer](https://github.com/nick-boss-tech/solr/compare/6b5d830132b941faa85856347099ade8b190be0e...b0e096ae19ad637107227e1bc04e951b9023a680).

Expected old local/fork F1 and clean task worktree were checked before repair. Commit helper restricted the new change to the picker test and ran diff --check. The ordinary fork push advanced F1 to F2; a separate GitHub refs API read returned the full F2 SHA, and the task tree is clean. app.js is byte-identical between F1 and F2, verified by diff --exit-code. The approved contributor head remains P; neither approval nor contributor source changed. Eric's approval and intended merge are not a waiver of this fork's failed gate.

## Fresh proof and publication

New job: `gates/solr-12613-pr-head-lifecycle-round-2.md`, receipt target `receipts/SOLR-12613-pr-head-lifecycle-round-2.md`. It supersedes round 1 execution after the investigated failure and inherits all required legs, using F2 plus unchanged original P. Full candidate picker at `5110C0AE` must pass 8/8, with no setup timeouts. The four-method combined fail-before run must now reach all four intended assertions without isolating methods to work around contamination. All required positive, neighbor, negative, formatting/compile/client/root proof is fresh at the named inputs; no F1 result is transferred as F2 success.

Comment revision: `reports/solr-12613-pr-head-lifecycle-comment-draft-round-2.md`. This supersedes the F1 conditional draft for publication. It links F2 and carries hypothetical post-green wording only. No public comment/PR/Jira action was made; no claimed successful browser run. Windows ran no Gradle, compiler, formatter, Selenium or test queue. Next step is exact-head Linux proof; a posted job is not execution.
