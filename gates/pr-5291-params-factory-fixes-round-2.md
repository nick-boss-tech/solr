# Gate: PR 5291 / SOLR-18621 parameter factory fixes, round 2 (superseding, tidy repair head)

Status: OPEN. Posted by the executing gate host under the repair route of `gates/pr-5291-params-factory-fixes-round-1.md`.

- Supersedes round 1 for execution: the round 1 detached tidy preflight at H1 `f2952c41c7c023aa6e79868df9efe8c65b02c993` was not clean. Tidy reformatted one line in `solr/solrj/src/test/org/apache/solr/common/params/SolrParamTest.java` (a single `expectThrows` call wrapped at the column limit). Formatting-only; no logic changed.
- Repair commit `199911b298fac80f4cea5e3de553675b64f6a6a9` ("SOLR-18621: Tidy SolrParamTest formatting", Nick Shanin identity, no trailers), an ordinary child of H1, plain-pushed to `nick-boss-tech/solr:solr-18621-params-factory-fixes` and read back through the fork refs API at the same SHA. Contributor ancestry preserved; the upstream PR stays at H0 `600e0ec4afd82004a94852c419892897be14e6f0`.
- New exact head for all proof: H2 `199911b298fac80f4cea5e3de553675b64f6a6a9`. No round 1 head result exists or transfers; every leg of the round 1 job runs at H2 unchanged: tidy and `git diff --check` clean, changelog validation, Error Prone compilation of the listed modules, the focused SolrJ suites and the core neighbor suites at seed `5291C0DE`, root `check -x test`, and the six-method negative leg restoring the H0 shallow-wrapper algorithm.
- Receipt target unchanged: `receipts/PR-5291-params-factory-fixes-round-1.md`, which will state both heads and the repair plainly.
