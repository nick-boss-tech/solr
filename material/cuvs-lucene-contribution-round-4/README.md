# cuVS Lucene CPU test contribution, round 4: consolidated package

Four test files on NVIDIA/cuvs PR 2702 base `23004d0f0de2014f11b09313f04fcf0de7643c3b`, from two independent receipts. No production file is touched. Machine-readable identities are in `manifest.json`.

## Files

- `combined-tests.patch`: one unified patch against the base, the combined result of applying the two originals in order. SHA-256 in the manifest.
- `cpu-regression.patch` (order 1, package `2ad555a87a22acdf617f679ca03f5831f246b4a8`, receipt `receipts/CUVS-LUCENE-PR2702-CPU-ROUND-2.md`): three test files.
- `backcompat.patch` (order 2, package `e174c1a3e0dcfccf9d30a31847637733823df84b`, receipt `receipts/CUVS-LUCENE-BACKCOMPAT-ROUND-3.md`): `TestBackCompatIndices.java`.

## Reconstruct

1. Check out the base `23004d0f0de2014f11b09313f04fcf0de7643c3b` of NVIDIA/cuvs, or the round-2 package's `base/` tree.
2. Check each file's Git blob against `manifest.json` (`originalBlob`). The four originals are `2dc5e3b8`, `ba897c67`, `dcd6544f`, and `203b5746`.
3. Apply and check: `git apply --whitespace=error-all combined-tests.patch`. Then each file's blob must match `finalBlob`.
4. Equivalent sequential application: `git apply --whitespace=error-all cpu-regression.patch`, then `git apply --whitespace=error-all backcompat.patch`.

## Scope

The combined patch is a byte-level composition of two patches that touch disjoint files. It is not a single test run. The round-2 and round-3 receipts cover their own exact bytes. This package does not claim that both suites ran together.
