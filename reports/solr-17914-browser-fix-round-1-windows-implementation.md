# SOLR-17914 browser fix round 1: Windows implementation handoff

Status: IMPLEMENTED, PROOF PENDING. Static diagnosis and candidate patch only; no Windows Gradle or browser test execution.

- Assignment: `assignments/solr-17914-browser-fix-round-1.md`.
- Implementation claim: `claims/solr-17914-browser-fix-round-1-windows-implementation.md`.
- Claim snapshot: `72d090c5d4120867200339aeef191130124b0a58`.
- Owner direction: Nick asked the Windows main agent to try to make patches and hand them back to the gate. Linux execution remains separate.
- Candidate branch: `solr-17914-browser-fix-round-1`.
- Parent: `6170ba10ac5061203339fa0a96e714c7e64616c2`.
- Candidate head: `252e0a75bb0d6a9183c8cc879da60b809b52c242` (commit `SOLR-17914: await browser string resources in create collection tests`).
- Linux handoff: `gates/solr-17914-browser-fix-round-1-windows-candidate.md`.

## Finding and limits

All six missing strings are read through Compose resources. The code paths and the difference between the literal-message and resource-message tests support a resource-readiness race, not a demonstrated production layout defect. This attribution is based on source inspection and the existing round-1 receipt. It still needs a Linux before/after reproduction; it is not a claim that the candidate has passed.

The branch pins Compose 1.12.1 in `ui/gradle/libs.versions.toml:19`. Its [StringResources.kt](https://github.com/JetBrains/compose-multiplatform/blob/v1.12.1/components/resources/library/src/commonMain/kotlin/org/jetbrains/compose/resources/StringResources.kt#L23) initially supplies an empty string, while [ResourceState.web.kt](https://github.com/JetBrains/compose-multiplatform/blob/v1.12.1/components/resources/library/src/webMain/kotlin/org/jetbrains/compose/resources/ResourceState.web.kt#L6) starts resource loading in a coroutine. The same version's [ResourceState.kt](https://github.com/JetBrains/compose-multiplatform/blob/v1.12.1/components/resources/library/src/commonMain/kotlin/org/jetbrains/compose/resources/ResourceState.kt#L5) documents the web asynchronous behavior and the blocking behavior on other platforms. `getString` is a suspending resource load, which lets the browser complete the fetch instead of depending on the Compose test clock to drive network I/O.

## Per-failure classification

Paths below are relative to the candidate source tree; production line numbers are unchanged from the parent.

| Failure | Classification and source evidence | Candidate treatment |
| --- | --- | --- |
| `CreateCollectionFormTest.testShowsFieldsAndButtons`, missing `Cancel` | (a), test synchronization in a branch file. `CreateCollectionForm.kt:326` uses `action_cancel`; the test immediately queries literal text after mounting. | Load `action_cancel` before mounting, await composition, retain the original text assertion and every field assertion. |
| `CreateCollectionFormTest.testCreatePassesTheTypedValuesAndAdvancedRouter`, missing `Composite ID` | (a), test synchronization in a branch file. `CreateCollectionForm.kt:349-350` resolves both router labels when the advanced subtree appears. Scrolling is also a possible interaction constraint, not a measured original failure cause. | Load both router option strings before mounting, await composition, scroll the composite-ID node into view before clicking. Retain the implicit selection, every typed value, and the complete callback-value assertion. |
| `CreateCollectionErrorsTest.testNameExistsShowsTheNameError` | (a), test synchronization in a branch file. `CreateCollectionErrors.kt:29` resolves `error_collection_name_exists`. The separate form test supplying the identical literal error passed in the receipt. | Load that resource before mounting and await composition; retain the exact error-text assertion. |
| `CreateCollectionErrorsTest.testFailureWithoutMessageShowsTheFallback` | (a), test synchronization in a branch file. `CreateCollectionErrors.kt:35` resolves the fallback resource. The neighboring test supplying the literal server message passed. | Load the fallback resource before mounting and await composition; retain the exact fallback-text assertion. |
| `CollectionDetailsCardTest.testShowsCollectionDetails`, missing `Collection Details` | (b), inherited base test with the same readiness risk. `CollectionDetailsCard.kt:53` uses `collection_details`; the test at line 42 immediately queries the heading. | Not changed in this branch. Confirm the browser result at SOLR-17913 base `0a04d2239e` separately. |
| `CollectionDetailsCardTest.testShowsAutoAddReplicasState`, missing `Enabled` | (b), inherited base test with the same readiness risk. `CollectionDetailsCard.kt:68` uses `state_enabled`; the test at line 57 immediately queries the text. | Not changed in this branch. Confirm at the base separately. |

These are source-based classifications pending execution. No shared harness or environment defect has been established as category (c). If explicit resource loading fails, report its exception, resource URL/status, and browser console output as possible category (c), rather than replacing expected text or weakening assertions.

## Patch and review

Exactly two outbound files:

- `ui/shared/src/commonTest/kotlin/org/apache/solr/ui/views/collections/CreateCollectionFormTest.kt`.
- `ui/shared/src/commonTest/kotlin/org/apache/solr/ui/views/collections/CreateCollectionErrorsTest.kt`.

The candidate adds explicit resource readiness in the four failing dialog tests and a scroll before the router click. It keeps the production components, resource strings, repository code, base tests, shared harness, test names, test count, and all assertions. No ignore annotations, sleeps, dependency changes, screenshot fixtures, or changelog fragment are included. This test-only change introduces no new trust boundary or production behavior.

The relevant history is `6170ba10ac` (resource-backed error adapters and their tests), `27297913d9` (new design and router labels), and `125448970d` (scrollable form and final Create scroll). The diff from `0a04d2239e` to `6170ba10ac` does not change the card component or its tests. The source comparison establishes inheritance; the required base browser run is still outstanding.

## Validation and return to gate

`git diff --check` passed, and the exact two-file diff was reviewed (21 insertions, 1 deletion, the deletion replaced by the scrolled click). Author and committer are Nick Shanin; there are no attribution trailers. No compilation, Gradle, JVM tests, wasm tests, or screenshots were run here. Full proof remains pending at the pushed head.

The Linux gate must reproduce the original browser leg, compare the SOLR-17913 base, run the candidate full UI check, and report each failure individually. If the four dialog tests pass and the two inherited card tests remain red, report that partial result plainly; do not declare the full UI check green or draft a green browser proof line. Do not mix a base or shared-harness repair into this candidate.

The screenshot method agreed in `reports/solr-17914-browser-fix-round-1-main-agent-direction.md` remains applicable. The four screenshots are still pending Linux capture and are not supplied by this patch.

For a subsequent separately scoped SOLR-17913 repair, the concrete candidate is to load `Res.string.collection_details` and `Res.string.label_shard_count` before the first card test, and `Res.string.state_enabled` before the second, then await composition and preserve all original assertions. This is an unexecuted repair suggestion, not a patch or proof delivered in this round.

No source push moves the open PR branch `solr-17914-create-collection-dialog-new-design`; its observed remote head remains `6170ba10ac5061203339fa0a96e714c7e64616c2`. No public comments or PR edits are part of this handoff.

Local tooling note: the existing `research/push-fork-new-branch.ps1` still referenced the superseded PAT location. Its loader was corrected to the current workspace file and made to refresh inherited credentials in memory. This helper change is outside the Solr patch and does not expose or copy the PAT.
