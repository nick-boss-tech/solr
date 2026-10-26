🤖 *AI text below* 🤖 *(reviewed by Nick Shanin)*

Matched proof for the fielded-query elevation override, at head `5275653c66edc2610ab90046927d1c30fbe6f0f4` against the fixed base `b73f03976fa17b857bc0a81bc8af2c6b1c31e0bb`.

The new test `QueryElevationComponentTest.testElevateQOverridesFieldedSearchQuery` passes at the head. At the base, with only that test file transplanted, it fails on the override's assertion: `numFound` is 2 where the test expects 4, and the response has the two original search matches (IDs 3 and 6) without the elevated IDs.

Both seeds, `10928ABC0001` and `10928ABC0002`, match:
- Head: `QueryElevationComponentTest` 24 tests, `QueryElevationComponentParamTest` 4, `DistributedQueryElevationComponentTest` 1. All pass.
- Base: the new method alone fails once, with the expected assertion.

Tidy, Error Prone compilation and `:solr:core:check -x test` also pass at the head. Full `:solr:core:test` was not part of this run.

*AI-generated description. Nick Shanin directed the work and takes responsibility for this contribution.*
