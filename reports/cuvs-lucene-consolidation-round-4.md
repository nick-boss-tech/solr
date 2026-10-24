# cuVS Lucene consolidation, round 4

Assignment: `assignments/cuvs-lucene-01-consolidate-round-4.md`. Claim: `claims/cuvs-lucene-01-consolidate-round-4-vm2.md` (vm2, snapshot `c5c758b0f6c`).

## Source state

- Live NVIDIA/cuvs PR 2702 read through the GitHub API: still a draft and open, head `23004d0f0de2014f11b09313f04fcf0de7643c3b`, which is the pinned base. Last updated 2026-10-15. No drift, so no changed-input gate is required.
- Inputs: `cpu-regression.patch` SHA-256 `d9ad46585c65846cf9b0d4f0ccffbf882e79be593f15897d8775115de810b154` (package `2ad555a87a22acdf617f679ca03f5831f246b4a8`), and `backcompat.patch` SHA-256 `56cb5cb10f210e216c36fd413821dd1ddd931e5dd4ed26fc288299c928a84238` (package `e174c1a3e0dcfccf9d30a31847637733823df84b`). Both receipts are GREEN.

## Application and blob checks

- Original blobs of the four files match the packages' manifests: `TestCompatKnnFloatVectorQuery.java` `2dc5e3b8`, the two `TestLuceneCompat.java` bridges `ba897c67` and `dcd6544f` (round-2 `source-manifest.json`), and `TestBackCompatIndices.java` `203b5746` (round-3 `patch-manifest.json`).
- Sequential application with `git apply --whitespace=error-all` succeeded in order: cpu-regression, then backcompat.
- Final blobs match the manifests: `0a2628c1` (TestCompatKnnFloatVectorQuery), `fa04413d` (leaf-reader-102), `9bdc09a5` (leaf-reader-103), `ee3cd673` (TestBackCompatIndices).
- The combined `git diff` touches exactly the four test files (216 insertions, 1 deletion). No production file or dependency version is changed.
- `combined-tests.patch` applies cleanly to the base with `git apply --check --whitespace=error-all`. After applying it, the four final blobs are the same as the sequential result.

## Receipt mapping

| File | Covered by | Scope of that receipt |
| --- | --- | --- |
| TestCompatKnnFloatVectorQuery.java | `CUVS-LUCENE-PR2702-CPU-ROUND-2` | 16 positive tests across four modules; two mask-drop controls with six expected failures |
| leaf-reader-102-bits TestLuceneCompat.java | `CUVS-LUCENE-PR2702-CPU-ROUND-2` | part of the round-2 compile and test set for the query-adapter tests |
| leaf-reader-103-acceptdocs TestLuceneCompat.java | `CUVS-LUCENE-PR2702-CPU-ROUND-2` | part of the round-2 compile and test set for the query-adapter tests |
| TestBackCompatIndices.java | `CUVS-LUCENE-BACKCOMPAT-ROUND-3` | 12 positive method executions over 138 fixture cases; two reassociation controls on 10.2 and 10.4 |

The two suites are separate and their controls are separate. This report does not describe them as one combined run.

## Result

The package is GREEN on the assignment's checks: all four file identities and the base are accounted for, no production file changed, and the combined patch applies cleanly. No replacement gate is needed because the live head has not moved. Published as `material/cuvs-lucene-contribution-round-4/` on coordination-ui. Public submission remains Nick's decision.
