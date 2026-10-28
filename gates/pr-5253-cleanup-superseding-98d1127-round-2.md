# Gate: SOLR-18601 / PR 5253 migration cleanup, superseding round 2 at 98d1127

Status: OPEN. Immutable once posted. Supersedes `gates/pr-5253-cleanup-superseding-cc8c423-round-1.md` (board `3c03ad95773`) for the exact head below, which in turn superseded `gates/pr-5253-cleanup-round-1.md` (board `3e84be3f668`). The original job and round 1 stay unchanged.

## Why superseded

Round 1 pinned G `cc8c42399909fb8df85c94376a042d291fd56292`. Its tidy preflight was clean, and its focused leg failed before any test ran:

- `solr/core/src/java/org/apache/solr/cloud/api/collections/MigrateCmd.java:580`: `error: constructor ZkNodeProps ... cannot be applied to given types`. The cleanup message passed `Map<String,Object>` values (`expectedRule`, `restoreRule`) into the `String...` constructor. `SliceMutator` reads both keys as maps, and a null `restoreRule` means remove the rule.
- Once that was repaired, `solr/core/src/test/org/apache/solr/cloud/MigrateRouteKeyTest.java` (8 call sites) failed to compile: `SolrInputDocument(String...)` cannot take the numeric `n_ti` values.

Record: `/workspace/gates/logs/vm2/solr18601-cleanup/steps.txt`, `focused.log`, `compile-H.log`, `compile-H2.log`.

Repair commit: `98d1127e4f5d4d149992e1dc494b95928bac057a` on `nick-boss-tech/solr:solr-18601-submit`, parent G. Author Nick Shanin `<nick.boss.us@gmail.com>`, subject `SOLR-18601: build the failed-migration cleanup message as a map and add a test document helper`, no attribution trailers. Plain fast-forward push; live ref read back as `98d1127...`.

Changes in the repair (relative to G):
- `MigrateCmd.cleanupFailedMigrate`: builds a `HashMap` with the same keys and values, then `new ZkNodeProps(props)`. Keys and semantics unchanged; null `restoreRule` still means remove.
- `MigrateRouteKeyTest`: private static helper `document(Object... fieldsAndValues)` builds a `SolrInputDocument` with `addField`; eight `n_ti` documents route through it. Assertions and data unchanged.

Tidy at H was run after the repair: rc 0, the tree was then reformatted by tidy only in these two files, and the formatted lines are committed in H. The positive legs below run tidy again at H, which must be clean before proof.

The only other `SolrInputDocument` constructions in the test file use string-only arguments and are unchanged. Compilation of `:solr:core:compileTestJava` at H is required again as part of the focused leg.

## Exact heads

- Positive head H: `98d1127e4f5d4d149992e1dc494b95928bac057a`.
- Original PR head O (negative A base): `32b91612edde057c19bf1a7963e8276b816551df`, unchanged. Contributor branch and public PR not modified.
- Negative A harness: overlay H's `TestInjection.java` and `MigrateRouteKeyTest.java` onto O, then the exact patch-A preparation from the original job.
- Negative B: separate clone at H, then the exact one-line `cleanupFailedMigrate` mutation from the original job.

## Requirements

Use the original job's requirements verbatim, with H in place of F. Round 1's requirements apply unchanged, including: tidy clean at H, focused fifteen tests at seed 5253C0DE, the three cloud regressions at 5253BEEF and 5253CAFE, neighbors at 5253C0DE, root check `./gradlew check -x test` and `:solr:solr-ref-guide:checkSiteLinks` as separate results, final cleanliness and remote read-back, negative A at O with exactly three executed failures at their named assertions, negative B with three executed failures.

Each step's exit status must be captured from Gradle directly. Gate rules: one build at a time under `/workspace/tools/.gradle-vm2.lock`; assertions enabled by the test task; fresh XML copied before the next cleanTest overwrites it; no silent exclusions; a compile error, skip, or setup failure is never counted as negative proof.

## Completion

GREEN requires all legs at H and both negative legs. Any further source or test repair needs another pushed head and superseding job.
