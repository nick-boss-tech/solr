# Gate: SOLR-18601 / PR 5253 migration cleanup, superseding round 1 at cc8c423

Status: OPEN. Immutable once posted. Supersedes `gates/pr-5253-cleanup-round-1.md` (board `3e84be3f668`) for the exact head below. The original job stays unchanged; its requirements apply to this head except where this file names a difference.

## Why superseded

Detached tidy preflight at F `5cbffe2f243c4f2cf3606de9f2434981fc097019` exited 0 but rewrote three files, so F is not a clean tidy head. The gate rule requires a formatting repair and a superseding gate before any proof is counted. Earlier F-head runs were not counted as proof; the preflight tidy was the only run at F.

Tidy changes (whitespace-only, verified per file by comparing content with all whitespace removed):
- `solr/core/src/java/org/apache/solr/cloud/api/collections/MigrateCmd.java`: line wrap around `zkStateReader.getCollection(...)` chain and `params.set(...)` arguments.
- `solr/core/src/test/org/apache/solr/cloud/MigrateRouteKeyTest.java`: line wraps in query and add chains.
- `solr/core/src/test/org/apache/solr/cloud/overseer/SliceMutatorRoutingRuleTest.java`: wraps in `getCollection` chain and `Map.of` arguments.

Repair commit: `cc8c42399909fb8df85c94376a042d291fd56292` on `nick-boss-tech/solr:solr-18601-submit`, parent F `5cbffe2f243c4f2cf3606de9f2434981fc097019`, author Nick Shanin `<nick.boss.us@gmail.com>`, subject `SOLR-18601: apply tidy formatting to migration cleanup follow-up`, no attribution trailers. Plain fast-forward push; live ref read back as `cc8c423...`.

## Exact heads

- Positive head G: `cc8c42399909fb8df85c94376a042d291fd56292`.
- Original PR head O (negative A base): `32b91612edde057c19bf1a7963e8276b816551df`, unchanged. Contributor branch and public PR not modified.
- Negative B base: G, with the one-line prerequisite mutation from the original job.

## Requirements

Use the original job's acceptance, legs, seeds, commands, XML handling, and negative-leg rules verbatim, with G in place of F:

1. Clean tidy at G: `./gradlew tidy`, rc 0, no tracked change; `git diff --check`; changelog authors Boyi Dai and Nick Shanin by name, no nick line.
2. Focused suite at seed 5253C0DE: fifteen tests (7 MigrateRouteKeyTest, 3 MigrateCmdCleanupTest, 5 SliceMutatorRoutingRuleTest), zero failures, errors or skips.
3. The three new cloud regressions at seeds 5253BEEF and 5253CAFE.
4. Neighbors at seed 5253C0DE: CoreAdminOperationTest, CoreAdminHandlerTest, V2CoreAPIMappingTest, TestClusterStateMutator.
5. Root check `./gradlew check -x test`, plus `:solr:solr-ref-guide:checkSiteLinks`, recorded as separate results.
6. Final cleanliness at G: `git diff --check`, clean tracked and untracked state, remote ref read-back.
7. Negative leg A: separate detached clone at O. Overlay only the two F-side test and hook files at G (`TestInjection.java`, `MigrateRouteKeyTest.java`), then apply the exact Python preparation from the original job. Run the three named cloud methods at seed 5253C0DE. Expect exactly three executed tests, each failing at its named assertion.
8. Negative leg B: separate detached clone at G. Apply the exact one-line `cleanupFailedMigrate` mutation from the original job. Run `MigrateCmdCleanupTest` at seed 5253C0DE. Expect three executed tests, each failing as the original job describes.

Gate rules unchanged: one build at a time under `/workspace/tools/.gradle-vm2.lock`, assertions enabled by the test task, fresh XML preserved before each cleanTest overwrites it, no silent exclusions, no counting of passes as negative proof.

## Completion

GREEN requires all legs at G, both negative legs as specified, and the receipt in `receipts/PR-5253-cleanup-round-1.md` naming this superseding job. Any further source or test change needs another pushed head and superseding job.
