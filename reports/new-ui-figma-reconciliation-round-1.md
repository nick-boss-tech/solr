# New-UI Figma reconciliation, round 1

- Assignment: `assignments/figma-wrap-up-round-1.md` (items A1 and A6). Claim: `claims/figma-wrap-up-round-1-phone-agent.md`. Prepared by the phone agent, 2026-10-26.
- This report supersedes the verdicts in `reports/new-ui-index-query-components-round-1.md` and `reports/new-ui-collections-configsets-round-1.md`, which were written before the Figma node read and before the vm6 gates landed. Those files are not edited.

## Verification (A6), run 2026-10-26

- Every receipt named below exists on this board.
- Every canonical branch's gated head is an ancestor of its live tip (checked with `git merge-base --is-ancestor` against the fork). The gated-to-tip delta is either empty or the removal of factory working files only (`pr-draft/*.md`, and on `solr-17660-basic-list-main` the fork test-runner workflow). No `ui/` code changes sit above any gated head.
- Apache main stands at `e59ce3c97dd` and still has zero `ui/` commits since the branches' base `c8ab71430f` (`git rev-list --count c8ab71430f..main -- ui/` is 0). The clean-merge premise in the assignment still holds. Re-check before any PR is opened.

## Per-ticket state

Asks for the Figma tickets were posted to Christos Malliaridis on Jira on 2026-10-26, at the owner's direction after he asked for all of them; comment IDs are in `receipts/comment-ledger/2026-10-26.md`. The next step on each is his answer. PRs still need the owner's go and, under the current creation cap, would be created as drafts.

| Ticket | Figma node | Canonical branch (gated head, live tip) | Receipt | Next step |
| --- | --- | --- | --- | --- |
| SOLR-17911 file content viewer | 319-20114 (ZooKeeper file preview); 227-7661 (placement) | `solr-17911-file-content-viewer` (`8047b99ef0`, tip `39d7c64701c` after working-file strip) | `receipts/SOLR-17911.md` PASS | Christos's answer to comment 18132605; then owner go for a draft PR |
| SOLR-17887 file tree | 13-3972, 13-3977, 227-7661, 227-9235 | `solr-17887-file-tree` (`9c7f72aa0e`, tip `a22da1bfe72`) | `receipts/SOLR-17887.md` PASS | Christos's answer to comment 18132591; then owner go |
| SOLR-17909 breadcrumb | 1997-7453 | `solr-17909-breadcrumb-wrap` (`77a38638b0`, tip `b5fdbc23436`; gated-to-tip delta empty) | `receipts/SOLR-17909.md` PASS | Christos's answer to comment 18132604; then owner go |
| SOLR-17913 collection details card | none cited | `solr-17913-collection-details-card` (`0a04d2239e`, tip `4003196af10`) | `receipts/SOLR-17913.md` PASS | Christos's answer to comment 18132606; then owner go; SOLR-17914 stacks on this one |
| SOLR-17914 create collection dialog | none cited | `solr-17914-create-collection-dialog-stacked` (`5ea0055190`, tip equals gated head) | `receipts/SOLR-17914-stacked-on-17913.md` PASS, 53 tests | Christos's answer to comment 18132607; PR only after 17913, body must say stacked. The unstacked branch (`a502747ec4`, `receipts/SOLR-17914.md`) is not a PR vehicle |
| SOLR-17660 basic list | none cited | `solr-17660-basic-list-upstream` (`e1d7bfde5b`, tip equals gated head) | `receipts/SOLR-17660-upstream.md` PASS, 52 tests | Christos's answer to comment 18132608; then owner go. `solr-17660-basic-list-main` (`1e8aca0ffc`, `receipts/SOLR-17660.md`) is superseded and must not be opened: fork-main base, 1128-file diff against apache main |
| SOLR-17850 SearchHandler form | 156-4776 (Regular Request Builder) | `solr-17850-search-handler-form` (`887778ce5d`, tip `fa9e050e52f`) | `receipts/SOLR-17850.md` PASS | Held for Christos: comment 18132602 asks whether the Figma field set beyond the implemented q, fq, fl, sort, start, rows is a later slice. His answer unblocks this and 17849 |
| SOLR-17849 Index & Query screen | 137-12066 (header and large /select); umbrella 23-5124 is SOLR-17848 | `solr-17849-index-query-screen` (`b91f75ed93`, tip `375143456a0`) | `receipts/SOLR-17849.md` PASS, 18 tests | Held behind 17850 (the screen calls its form). Comment 18132601 leads with Jane Sandberg, who volunteered on the ticket |
| SOLR-17861 UI license check | not a Figma ticket | `solr-17861-ui-license-check` (`92aed103a06c`, tip `51bcb094b0a`) | `receipts/SOLR-17861.md` | Comment 18132574 asks Christos whether to open the checksum slice as a PR and whether LICENSE and NOTICE presence moves to SOLR-17970 |

## Notes

- Merge order stands as decided in `reports/pool-ui-merge-order-round-1.md`: 17911, 17887, 17909 independent; 17913 then 17914 stacked; 17660 upstream any time; 17850 then 17849 after Christos.
- The owner decisions O1 to O9 in the assignment are untouched by this report.
- Node `87-12957` (cited for SOLR-17896) was not found in the Figma file and no nearby node is substituted; SOLR-17896 shows Closed in the UI census.
