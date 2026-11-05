# Stand-down: adjacent node follow-up gates require scope separation

Status: HANDED (record-only; listed gate jobs withdrawn pending clean scope and new exact-head jobs).

- Recorded by Windows, 2026-11-05 02:35 UTC; reviewed board snapshot `3b1946cc230b0de0e84dfc2757eba612c89e4d10`.
- Continues assignment `assignments/solr-12613-adjacent-implementation-round-1.md` and source claim `claims/solr-12613-adjacent-windows-round-1.md`. This is a scope correction, not a Linux execution claim or receipt.
- Owner signal: Nick reports that the picker contribution will exclude v2 changes and asks whether his own v2 work belongs on a separate branch.
- Public evidence: David Smiley requested changes at picker head `73b30956ff2c0f7b16f6af3bceacf95bda8271b9`, saying the contribution should be frontend-only using `X-Solr-targetNode`, with no v2 API changes. Review: https://github.com/apache/solr/pull/5110#pullrequestreview-5778764327 ; inline request to revert v2 `nodes` parameters: https://github.com/apache/solr/pull/5110#discussion_r4475714002 . The live picker remains OPEN at that head on this review.

## Decision and separate lanes

Decision ladder rung 3: follow the explicit maintainer scope. Existing v2 endpoints may still be called from the frontend; extending backend endpoint parameters, response models or aggregation contracts is separate API work.

1. Picker and its frontend lifecycle follow-up: consume the existing generic targeting header and existing endpoint contracts. Reconcile against the contributor's revised frontend-only head when available, with the generated JavaScript call signatures checked against that head.
2. SOLR-18619 UI request migration: retain a distinct ticket-scoped branch for header-based Metrics, Nodes and logging reads. Its current follow-up delta has no `solr/api/` or `solr/core/` changes, but its inherited baseline does. A fresh parent and proof are required before submission. Preserve Nick's requested maintainer choice about selected-node versus aggregate log viewing in the eventual PR draft.
3. Backend v2 work: use branches based on main and the matching existing tickets. Do not automatically move every removed picker API addition into a new contribution: first establish the ticket requirement and whether the generic header already covers the use case. Logging-level GET broadcast already has Nick's OPEN SOLR-15823 contribution, https://github.com/apache/solr/pull/5031 , verified head `c4d0dc38926fd9a1ab1a02c45775bec70cad6dc0`; avoid a duplicate implementation.
4. Generic transport encoded-key fix and encoded-path coverage: separate from backend API expansion. Port their scoped changes to main independently of the picker, then issue new exact-head jobs. Remove inherited-only neighbor-test dependencies when revising those jobs.

## Withdrawn gate inputs

Do not newly claim or execute these jobs. Earlier rounds superseded by these jobs remain superseded. No result at their old heads establishes proof for the forthcoming scope-separated branches.

| Job | Preserved branch head |
| --- | --- |
| `gates/solr-12613-adjacent-encoded-key-round-1.md` | `bd05d1ed39a005317f0a6655e4cd8fed078531b8` |
| `gates/solr-12613-adjacent-path-coverage-round-1.md` | `96747864862bf716daf451e02a7c24194d41c267` |
| `gates/solr-12613-adjacent-selection-lifecycle-round-2.md` | `a331e7faf96c8c215f2d623f4865d4cbabca6492` |
| `gates/solr-18619-request-targeting-round-2.md` | `67cc03dfbed1866216c8cda8c31a65beb2c5672e` |
| `gates/solr-12613-adjacent-integration-round-1.md` | `2dd6003d4b97e2b7eeeb762af361cf510851b087` |

All five heads inherit the old picker API expansion through their main-plus-picker baselines. The current picker diff changes 17 files under `solr/api/` and `solr/core/` relative to merge-base `ad26fdf2c6bee8168eae043b94833f05643d1995`. The UI baseline is `b45bae5da3a151c7b439ff4e42009c383b538fae`; the transport baseline is `6ec2e527ad716bde74404b2037bb1a1e6c52f79c`. Preserving old branches retains authored work; it does not make those API additions approved follow-up scope.

## Next deliverables and limits

Prepare fresh branches with explicit baseline-to-head file lists, reconcile generated-client signatures and neighboring tests, verify fork heads, and publish superseding Linux jobs. Source work remains Implemented, Proof Pending and now requires scope reconciliation. No new source branch is created by this record, no existing history is rewritten, and no public PR/comment/Jira action is taken. Windows ran no Gradle, compiler, browser or tests.
