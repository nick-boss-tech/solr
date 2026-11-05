# Follow-up submission packages: six paired PR/Jira drafts

Status: DRAFTS DELIVERED; branch arrangement and final submission checks remain pending. Prepared 2026-11-05, Windows Codex desktop. Claim: `claims/solr-followup-packages-round-2-windows.md`. Owner requested the remaining drafts and an assignment update. This supplies the draft portion of `assignments/solr-12613-pr-jira-packages-round-1.md` and adds the two parser follow-ups discussed with Nick. Continuation: `assignments/solr-followup-pr-jira-packages-round-2.md`.

Six candidate PRs, seven Jira title/body drafts because one PR addresses two existing tickets. None has been opened, filed, commented on, or published as a live-body edit. All AI wording is marked prepared for Nick's review, without claiming that review has occurred. Replace the disclosure with the approved reviewed wording only after actual review. Placeholders are explicit unfiled identities, never fabricated Jira keys.

## Packages and recommended delivery

| Candidate | Full paired draft | Ticket strategy | Remaining prerequisite |
| --- | --- | --- | --- |
| Encoded query keys | [Encoded key](solr-12613-package-encoded-key-round-1.md) | New linked bug, `<JIRA-KEY>`; SOLR-18537 stays resolved | Approve identity; add scoped changelog and verify final head |
| Encoded path coverage | [Paths](solr-12613-package-path-coverage-round-1.md) | New linked test task, `<JIRA-KEY>` | Approve identity; reconcile current main/test overlap and test-only changelog policy |
| Picker/core-menu lifecycle | [Lifecycle](solr-12613-package-lifecycle-round-1.md) | Prefer author offer under SOLR-12613; separate new bug only if needed | Reconcile revised PR 5110; preserve only missing delta; exact-head proof |
| UI request targeting/logs | [Request targeting](solr-12613-package-request-targeting-round-1.md) | Existing SOLR-18619, proposed wording | Reconcile PR 5110; confirm selected-node default in eventual PR; exact-head proof |
| Explicit phrase slop | [Phrase slop](solr-parser-package-phrase-slop-round-1.md) | Existing SOLR-12532 and SOLR-15611, proposed wording for each | Arrange after PR 5294 or explicit dependency; verify changed head |
| Empty eDisMax phrases | [Empty phrases](solr-parser-package-empty-phrase-round-1.md) | Existing SOLR-18314, scoped addition; preserve broader original | Coordinate with PR 4652; root tidy/evidence clarification; resolve PR 5294 dependency |

Six is an upper bound on separate submissions. The lifecycle slice may be absorbed into PR 5110; SOLR-18314 may be delivered through Ian's existing PR. Transport patches could be combined only with an explicit rationale and new combined-head evidence. Do not manufacture a PR for every branch.

## Exact branch/base inventory

All source refs below were read back from the fork API on 2026-11-05. No source branch or commit changed while drafting. Each package lists its exact outbound files.

| Role | Fork branch | Exact head | Scoped comparison base | Disposition |
| --- | --- | --- | --- | --- |
| Encoded keys | `solr-18537-encoded-key-followup` | `a676e7a392244e7cf4a35719fc64383b65cd947f` | `07ccc73cd24e26e59da169ca35b3fd5f64c35986` | Independent main-based candidate, 3 files |
| Path coverage | `solr-18537-path-coverage-followup` | `0fad9847cc89973c751b1732aa22a259764610b2` | `07ccc73cd24e26e59da169ca35b3fd5f64c35986` | Independent main-based candidate, 1 test file |
| Synthetic picker baseline | `solr-12613-frontend-only-baseline` | `4e8aa913fce4a40b529fe89d8b5dd7930c29a86e` | Main plus inherited picker history | Reference and proof prerequisite; no competing picker PR |
| Lifecycle | `solr-12613-selection-lifecycle-followup` | `7f8d251d6c27200f3804f8b4e1298b9646f6cab4` | Synthetic baseline `4e8aa913fce4a40b529fe89d8b5dd7930c29a86e` | Author offer/dependent candidate, 2-file delta |
| Request targeting | `solr-18619-ui-request-targeting-followup` | `be369c41c633d639ad66a7291a4d82891ea9836c` | Synthetic baseline `4e8aa913fce4a40b529fe89d8b5dd7930c29a86e` | Dependent SOLR-18619 candidate, 12-file delta |
| Combined integration | `solr-12613-adjacent-integration` | `84743822082ed2c409d87e40a6f8142e2a240042` | Synthetic baseline plus four slice inputs | Reference/proof only; no extra PR |
| Phrase slop | `solr-12532-15611-phrase-slop` | `44849d2c4ca0cae283c26bc45048f824ea0d53c4` | P `0af4f5b3f2784eba3693acdc33dc49f6c2802b6a` | Separate coherent parser candidate, 4-file delta |
| Empty phrases | `solr-18314-edismax-empty-phrase` | `1ea8ceb2d5253fef6c2e05a3ee800687dea700ba` | P `0af4f5b3f2784eba3693acdc33dc49f6c2802b6a` | Scoped extraction/author coordination, 3-file delta |

P remains the OPEN draft PR 5294 head. Those parser branches are siblings, not a stack on each other. They both touch ExtendedDismaxQParser.java; any new combination requires conflict review and exact-head proof.

## Live-context findings

- PR 5110 is OPEN at `6b5d830132b941faa85856347099ade8b190be0e`, advanced from the earlier `73b30956ff2c0f7b16f6af3bceacf95bda8271b9`. The inspected delta removes the added node API/proxy machinery and changes properties, threads and logging-level calls to callback-only generated signatures. Those fixes are already incorporated; do not offer them again. This is not a complete re-review of the new PR. Neither UI follow-up is arranged or proven against it yet.
- PR 4652 is OPEN at `c122384ba230eecd0a6f2ba13a15c02908c9f0fc`. David's November 3 comment accepts the empty-clause portion and objects to the broader DisMax ranking consequences. Preserve Ian's credit and coordinate delivery; E3 is not a fast-forward descendant of that contributor head.
- PR 5294 is OPEN at P. Already public, so it is not a seventh new PR. Its live related-work paragraph still says follow-up Linux verification is pending. This record does not authorize changing that body; prepare an exact correction for Nick after final reconciliation.
- Live Jira confirms SOLR-18537 resolved, SOLR-12532/SOLR-15611/SOLR-18314/SOLR-18619 open. SOLR-18619's existing body has the per-node migration scope; selected-node-default logging is proposed wording requiring maintainer confirmation, not an already accepted ticket change.
- Targeted Jira summary and GitHub searches found no separate matching encoded-key/path/picker follow-up submissions. They are discovery, not exhaustive deduplication. Historical PR 280 for SOLR-15611 is closed without merging; mention its prior approach in review. Refresh searches before any filing/opening.

## Evidence and exact limits

| Candidate | Recorded proof | Limits and pending work |
| --- | --- | --- |
| Encoded keys | `receipts/SOLR-18537-encoded-key-main-round-2.md`: 25 focused + 11 neighboring; tidy/root check; four intended fail-before methods | Only those four named methods have negative proof; variants in the loop are positive coverage. Changelog absent |
| Paths | `receipts/SOLR-18537-path-coverage-main-round-2.md`: original 14; candidate 21; neighbors 11; tidy/root check; two URI comparisons catch mutation | Test-only; other assertions positive coverage, not independently mutation-proven |
| UI baseline/lifecycle/requests | `receipts/SOLR-12613-browser-verification-round-1.md`: baseline contract/picker 7/7 and 3 intended mutation failures | Baseline neighbors 16/17; isolated pass does not clear suite failure. Linux/core/root checks and follow-up/combined exact-head receipts absent at discovery snapshot |
| Slop | `receipts/SOLR-12532-15611-phrase-slop-round-1.md`: 94 tests + original-seed 1/1; tidy/YAML/Error Prone/root; 4 failures on original production, ownership control passes; 2 restoration mutations | Guard reset not independently discriminated; stacked on P; no broad ranking claim |
| Empty phrases | `receipts/SOLR-18314-existing-synonym-control-round-3.md`: 52 tests; 2 regressions fail original production, 2 controls pass and independently fail mutation; core tidy/Error Prone/root check | Job says root tidy, receipt records module tidy. Executor must confirm coverage or do missing check; exact fresh-log/XML locations need recording. Not a blanket clean-submission claim yet |

Browser receipt provenance clarification: the downloaded artifact commit refers to the new wasm UI artifact, not generated OpenAPI client provenance. The positive log separately records the JS-client generation/sync/build tasks. Ask the browser executor to correct that label. Keep the neighbor timeout investigation with `assignments/solr-12613-selenium-handoff-round-1.md`; do not transfer its narrow past Windows execution exception to this drafting task.

## Submission order and finish boundary

1. Nick reviews paired wording and proposed new-ticket identities. No filing/opening is authorized here.
2. Arrange the two independent transport candidates on the agreed current main. Keep separate small reviews unless a combined scope is explicitly chosen. New source/changelog/base commits require appropriate fresh Linux proof.
3. Settle the PR 5294 dependency, then arrange the two parser deltas independently; SOLR-18314 first needs author coordination and evidence clarification.
4. Reconcile UI follow-ups with the actual revised/merged PR 5110 head, omitting incorporated work. Finish baseline neighbor reconciliation and the assigned exact-head UI proof. Keep the log default choice in the eventual request-targeting PR.

The draft-writing deliverable is complete. The original assignment's branch-arrangement finish bar is not complete. Continue under the round-2 addendum; preserve source history and all prior failed/partial receipts. SOLR-12260 remains research/design, and SOLR-18549 remains existing status work; neither adds another candidate here.
