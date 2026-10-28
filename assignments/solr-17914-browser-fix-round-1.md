# Assignment: SOLR-17914 browser-leg fix and screenshots, round 1

Status: CLAIMED (vm4, claim `claims/solr-17914-browser-fix-round-1-linux-gate.md`, started 2026-10-28T06:58Z). Previously OPEN. Standing handoff from the round-1 browser check (`receipts/SOLR-17914-browser-check-round-1.md`, verdict red).

Posted 2026-10-28 by vm4 at Nick Shanin's direction. Intended hosts: a host with the `ui` and `gate` tags (vm4, vm5, or vm6). Claim before work. Source changes ARE in scope for this assignment, which is the explicit exception to the settling-run rule.

## Purpose

PR #5258 (SOLR-17914, create collection dialog, new design) has its wasm browser leg red at head `6170ba10ac5061203339fa0a96e714c7e64616c2`: 6 of 70 browser tests fail, all with "could not find any node" errors, while the same tests pass on `:shared:jvmTest`. Owner also requires screenshots of the dialog.

## Facts from round 1

- Fresh-XML counts: `wasmJsBrowserTest` total 70, skipped 3, failures 6, errors 0. `jvmTest` all green. Spotless green.
- Failing browser tests and first error text:
  1. `CreateCollectionFormTest.testShowsFieldsAndButtons`: node "Cancel" not found.
  2. `CreateCollectionFormTest.testCreatePassesTheTypedValuesAndAdvancedRouter`: mouse injection failed, node "Composite ID" not found.
  3. `CreateCollectionErrorsTest.testNameExistsShowsTheNameError`: node "A collection with this name already exists" not found.
  4. `CreateCollectionErrorsTest.testFailureWithoutMessageShowsTheFallback`: node "Could not create the collection." not found.
  5. `CollectionDetailsCardTest.testShowsCollectionDetails`: node "Collection Details" not found. NOT in this PR's diff; inherited from SOLR-17913 commit `0a04d2239e`.
  6. `CollectionDetailsCardTest.testShowsAutoAddReplicasState`: node "Enabled" not found. Same origin as 5.
- Passing in the browser run: `HttpCollectionsRepositoryTest` 5 of 5, `CreateCollectionFormTest` 7 of 9, `CreateCollectionErrorsTest` 1 of 3.

## Work

1. Claim, then set up a clean worktree at `6170ba10ac`, the head of `solr-17914-create-collection-dialog-new-design`. Put fix commits on a NEW branch `solr-17914-browser-fix-round-1` pushed to the fork. Do not push to `solr-17914-create-collection-dialog-new-design`, because PR #5258 is open as a draft on it and its head is the owner's call.
2. Find the cause before changing anything. Run only the browser leg on the head with the same env as round 1 (`CHROME_BIN`, `LD_LIBRARY_PATH=/workspace/tools/mesa-egl/lib`, `flock` on `/workspace/tools/.gradle-vm4.lock`). Record in the receipt whether `CollectionDetailsCardTest` also fails on its base `0a04d2239e` in the browser leg. Split the failures into: (a) problems in this branch's files, (b) problems shared with the SOLR-17913 base, (c) harness or environment problems.
3. Fix category (a) on this branch with the smallest change that makes the test pass for the right reason. Do not weaken or delete assertions, and do not mark tests as ignored.
4. Category (b) or (c) is NOT fixed here. Record it, and escalate to the main agent: a fix in SOLR-17913 base code or in the shared test harness is a separate step the owner directs.
5. Screenshots (owner requirement): capture the create-collection dialog in these states: empty, advanced section open, name-exists error, generic failure. Store the PNGs under `receipts/SOLR-17914-screens/` on the board. The capture method must be agreed with the main agent before any scratch code is added. If the browser run cannot capture them, record that in the receipt and stop at that point rather than improvising.
6. Rerun the full `./gradlew -p ui check --no-build-cache --no-daemon --rerun-tasks` at the new head. Report the fresh browser-leg counts from the XML.

## Deliverables

- Commits on `solr-17914-browser-fix-round-1` (based on `6170ba10ac`): author and committer Nick Shanin, no trailers, message `SOLR-17914: <summary>`, no em dashes or en dashes in text, push fast-forward only.
- `receipts/SOLR-17914-browser-fix-round-1.md`: the cause per failing test (category a, b, or c), the commits, the fresh browser-leg counts, the verdict, and the screenshot status.
- If green: `drafts/pr-5258-proof-line-update.md` with the exact replacement for the "Browser verification: not done." line. The PR body edit is not part of this assignment.
- If red: the receipt records the remaining failures with names and first error text. Stop there.
- Screenshot PNGs under `receipts/SOLR-17914-screens/`, or the receipt states why they are missing.

## Boundaries

- No PR opens or edits, no PR body edits, no Jira or GitHub comments. Those stay with the main agent and the owner.
- No force pushes. No pushes to `solr-17914-create-collection-dialog-new-design` or any other ticket's branch.
- One Gradle build at a time under the host lock.
- Heartbeat the claim at least hourly. Clean up scratch worktrees after the closeout is pushed; keep logs under `/workspace/gates/logs/<host>/solr-17914-browser-fix-round-1/`.
