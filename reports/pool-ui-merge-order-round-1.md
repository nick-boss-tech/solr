# Merge order for the seven vm6 pool-ui branches (decision record, round 1)

Decided by vm4, 2026-10-24, under `material/decision-ladder.md`. Nothing is opened as a PR. Opening a PR, posting a comment, or filing Jira stays with the owner.

## Decisions and the rung that settled each

1. **17850 and 17849 wait together.** Rung 1. `ui/shared/.../views/indexquery/IndexQueryContent.kt:38,69` on `solr-17849-index-query-screen` imports and calls `SearchHandlerForm`, which is added by 17850. The 17849 branch contains commit `887778ce5d` (17850) in its history. 17850's verdict is NEEDS CHRISTOS (the field set), so 17849 cannot land before 17850's field set is settled.
2. **17913 lands before 17914, via a stacked branch.** Rung 1 and rung 4. 17914 does not reference 17913's code (grep finds no reference). Both add `label_collection_name` and `label_replication_factor` with identical text, which is a duplicate resource name. Rung 4 choice: a new branch `solr-17914-create-collection-dialog-stacked` on top of `solr-17913-collection-details-card`, with those two duplicates removed from 17914's strings. Evidence: focused tests `CollectionDetailsCardTest` 2 of 2 and `CreateCollectionFormTest` 4 of 4 pass, and `spotlessCheck` passes, at head `5ea0055190c7e0857c42a3d4ef354e200c250b32` (log `/workspace/gates/logs/vm4/merge-order/stacked-17913-17914.log`). The stacked tree equals the green integration tree except for the 17849 and 17850 files, which it does not contain.
3. **17911, 17887 and 17909 are independent.** Rung 1. None of the three touches `strings.xml` or any file another branch touches (see `reports/pool-ui-integration-check-vm4.md`).

## Order

| Step | Branch | Depends on | Status |
| --- | --- | --- | --- |
| 1 | `solr-17911-file-content-viewer` (`8047b99ef0`) | none | ready for owner go |
| 2 | `solr-17887-file-tree` (`9c7f72aa0e`) | none | ready for owner go |
| 3 | `solr-17909-breadcrumb-wrap` (`8a2d511368`, gated at `77a38638b0`) | none | ready for owner go |
| 4 | `solr-17913-collection-details-card` (`0a04d2239e`) | none | ready for owner go |
| 5 | `solr-17914-create-collection-dialog-stacked` (`5ea0055190c7e0857c42a3d4ef354e200c250b32`) | step 4 | ready for owner go; the PR body must say it is stacked on 17913 |
| 6 | `solr-17850-search-handler-form` (`887778ce5d`) | Christos's field set | HELD: needs Christos (rung 5, the owner routes it) |
| 7 | `solr-17849-index-query-screen` (`b91f75ed93`) | step 6 | HELD: contains 17850 and depends on it |

Steps 1 to 3 are independent and may go in any order. Step 4 must go before step 5.

## Not decided here (rung 5)

- Whether 17849 should be split so that its header (`solr-17849-index-query-header`, `fb51fbcb23`) can land first. That is a scope choice for the owner and vm6, which holds the 17849 claim. It is not done here.
- The 17850 field set. Christos decides it, not the pool.

## Limits

- The 17914 stacked branch is a new branch. The old `solr-17914-create-collection-dialog` (`352cd12db8`) is not changed. Its vm6 receipt still names `a502747ec4`.
- The merge-order gate for the stacked branch is focused, not the full suite. The full suite was run on the integration tree (`reports/pool-ui-integration-check-vm4.md`).
