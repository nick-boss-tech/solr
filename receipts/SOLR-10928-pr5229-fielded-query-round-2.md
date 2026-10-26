# SOLR-10928 PR 5229 fielded-query round-2 receipt (vm6)

- Gate: `gates/SOLR-10928-pr5229-fielded-query-round-2.md`. Claim: `claims/SOLR-10928-pr5229-fielded-query-round-2-vm6.md`.
- Executing host: vm6, Linux, JDK `21.0.12.1+1` (Temurin), build lock `/workspace/tools/.gradle-vm6.lock`.
- Head: `5275653c66edc2610ab90046927d1c30fbe6f0f4` on `nick-boss-tech/solr:solr-10928-submit`, verified by `git ls-remote` before and after the run. Tree `770f807ad66f4ee4c94cb9a1fd51be98405e6d4d`.
- Fixed fail-before base: `b73f03976fa17b857bc0a81bc8af2c6b1c31e0bb`, tree `d893b020fcd5b34360588d6c70961a0621dd7b5c`. Only `QueryElevationComponentTest.java` was transplanted from the head, blob `ca908819164bf731b78d28069b7a36fa9f4c544b`, checked by `hash-object` in the runner.
- Preflight: tidy clean at the head, exit 0 (`/workspace/gates/logs/vm6/preflight/SOLR-10928-pr5229-fielded-query-round-2/preflight.log`).
- Run: `/workspace/gates/logs/vm6/SOLR-10928-pr5229-fielded-query-round-2-rerun/runner.log`, evidence `/workspace/tmp/solr-10928-fielded-proof.KyHyBk` (manifest, `steps.tsv`, XML, logs and the two worktrees are retained there). Started 2026-10-26 02:56 UTC; finished before 04:58 UTC.

## Standard legs

| Leg | rc | Notes |
| --- | --- | --- |
| tidy | 0 | Clean-tree guard passed after tidy |
| errorprone (`compileJava`, `compileTestJava`, `-Pvalidation.errorprone=true`) | 0 | `errorprone.log` |
| check (`:solr:core:check -x test`) | 0 | `BUILD SUCCESSFUL in 8m 56s`, 65 tasks |

## Matched focused runs

Each seed ran `cleanTest` and then the three focused classes at the head, then `cleanTest` and the new method alone at the base.

| Seed | Side | Class | Tests | Failures | Errors | Skipped | rc |
| --- | --- | --- | --- | --- | --- | --- | --- |
| 10928ABC0001 | head | `QueryElevationComponentTest` | 24 | 0 | 0 | 0 | 0 |
| 10928ABC0001 | head | `QueryElevationComponentParamTest` | 4 | 0 | 0 | 0 | 0 |
| 10928ABC0001 | head | `DistributedQueryElevationComponentTest` | 1 | 0 | 0 | 0 | 0 |
| 10928ABC0001 | base | `QueryElevationComponentTest.testElevateQOverridesFieldedSearchQuery` | 1 | 1 | 0 | 0 | 1 |
| 10928ABC0002 | head | `QueryElevationComponentTest` | 24 | 0 | 0 | 0 | 0 |
| 10928ABC0002 | head | `QueryElevationComponentParamTest` | 4 | 0 | 0 | 0 | 0 |
| 10928ABC0002 | head | `DistributedQueryElevationComponentTest` | 1 | 0 | 0 | 0 | 0 |
| 10928ABC0002 | base | `QueryElevationComponentTest.testElevateQOverridesFieldedSearchQuery` | 1 | 1 | 0 | 0 | 1 |

The base rc of 1 is the expected assertion failure: the runner's `check-xml.py` requires exactly one failure, zero errors, zero skips, and the override's assertion in the failure text.

## Assertion signatures

- Both seeds, base: `java.lang.AssertionError` on `xpath=//result[@name='response'][@numFound='4']`, with the response's `numFound="2"` and the two original search matches (IDs 3 and 6). Elevated IDs are absent from the base response.
- Both seeds, head: `testElevateQOverridesFieldedSearchQuery` is present in `TEST-...QueryElevationComponentTest.xml` and passes.

## Limits

- This receipt does not cover full `:solr:core:test`, which is outside the gate.
- The first run (`/workspace/gates/logs/vm6/SOLR-10928-pr5229-fielded-query-round-2/runner.log`) reached the same end state at the same head, but its evidence was lost when `/tmp` was cleared, so this receipt rests only on the rerun above.
- The earlier `receipts/SOLR-10928.md` proves only `f2001e640a`.
- Nothing was posted to GitHub, JIRA or the PR, and the PR's draft status is unchanged.
