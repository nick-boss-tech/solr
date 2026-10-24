# cuvs-lucene archived-index compatibility, round 3

Status: IMPLEMENTED, CPU PROOF PENDING

Source: NVIDIA/cuvs PR [2702](https://github.com/NVIDIA/cuvs/pull/2702), unchanged head `23004d0f0de2014f11b09313f04fcf0de7643c3b`. Owner direction: keep implementing while round-2 CPU proof is assigned. Claim: `claims/cuvs-lucene-backcompat-round-3.md`, posted at `c261d698752dfc5e789da7cb535e19217b759ae5`.

## Outcome

The existing archived-index read and CPU upgrade tests pass on all four supported Lucene variants. No additional production defect was reproduced. The contribution is a one-file test patch covering deletion and sparse-vector remapping, plus exact vector-value association with stored document IDs. Production code is unchanged. Round-2 patch, package, claim and gate are unchanged; this follow-up applies independently to the same upstream head and touches a different file.

## Contract and history

At the pinned head, `java/cuvs-lucene/src/test/java/com/nvidia/cuvs/lucene/TestBackCompatIndices.java` lines 89 and 108 read and upgrade every compatible archived fixture. Lines 185-205 establish 200 documents, ID order, dimension 8, and Random(42) values. The released-index generator uses the same contract. The original methods check counts, codec identity and self-search recall, but can miss ID/vector reassociation when each incorrect association is internally consistent with search.

The file was introduced by [ee335be1fec13445a96bc08f5011020200984bfc](https://github.com/NVIDIA/cuvs/commit/ee335be1fec13445a96bc08f5011020200984bfc), then changed by [9f64416c2f956e9f1fb48ffa3bed17ed4a8e35c1](https://github.com/NVIDIA/cuvs/commit/9f64416c2f956e9f1fb48ffa3bed17ed4a8e35c1), which fixes merge/maxConn handling. History is recorded in the material directory. Current main returned 404 for this test file when checked; the patch targets the existing PR rather than claiming another implementation of its production fixes.

Decision ladder: rung 1 selects the documented fixture contract and CPU upgrade path. Rung 2 runs that path, then a data-fault control that distinguishes count/self-search coverage from exact ID/value coverage. No production change is warranted by passing behavior.

## Patch

`material/cuvs-lucene-backcompat-round-3/backcompat.patch` changes only `java/cuvs-lucene/src/test/java/com/nvidia/cuvs/lucene/TestBackCompatIndices.java`.

In the patched file, line 143 starts `testUpgradeIndicesWithDeletesAndSparseVectors`. It deletes every third old ID (67 documents), adds 50 documents with ten vectorless gaps, and checks 183 live IDs and 173 vector-bearing IDs. Lines 191 and 199 check both before and after forceMerge; line 230 checks every float value against the value belonging to that stored ID. The test also pins the presence of deletions and multiple segments before merging and one segment with no deletions afterward. Existing codec and search checks run on the merged index.

- Patch SHA-256: `56cb5cb10f210e216c36fd413821dd1ddd931e5dd4ed26fc288299c928a84238`.
- Before Git blob: `203b57469d9122573c6bff0203e307a1aaf311f4`.
- After Git blob: `ee3cd6734e410a58aa08d703b7f944e210bfdf0c`.
- Final test SHA-256: `642076a8446ad90f402ccac1e251903a1b3d990a72323223b3114c1e650cb534`.

## Executed CPU evidence

Windows, Temurin 22.0.2, direct javac --release 21 and JVM, seed `18556C0D`. All positive methods ran without ignored tests or assumption skips. Production/API proof jars are the exact round-2 compiled jars, with local SHA-256 fingerprints recorded in `results/local-inputs.json`. The reproduction script reconstructs and rebuilds these sources in a fresh directory.

| Lucene module | Runtime version | Compatible fixtures | Original read + upgrade methods | New method |
| --- | --- | ---: | --- | --- |
| 10.2 | 10.2.2 | 7 | 2 pass | pass |
| 10.3 | 10.3.2 | 10 | 2 pass | pass |
| 10.4 | 10.4.0 | 13 | 2 pass | pass |
| 10.5 | 10.5.1 | 16 | 2 pass | pass |

This is 12 passing method executions: 92 original fixture read/upgrade cases and 46 new upgrade cases. Archives include the published 25.12.0 per-field format and 26.08.0 codecs, as well as the PR's codec fixtures. They are read on the CPU; append/merge uses CPU fallback.

For Lucene 10.2 and 10.4, `FixtureMappingControl.java` separately writes valid CPU indexes with stored IDs cyclically shifted relative to the vectors. Each control preserves counts and can find its own vector. On both controls the two existing methods pass and the new method fails on the value association assertion: ID 1 expects 0.6655489 but sees 0.7275637. The control runner rejects a different failure shape. These are deliberate data faults, not production-code mutations or failures observed in a genuine archived index. Other new assertions are behavior pins without individual mutation proof.

A fresh reconstruction smoke verifies all pinned source/fixture blobs, patch application with whitespace errors rejected, the exact patched blob, and google-java-format 1.27.0. Its full test phase was not rerun; the separately executed matrix and controls above are the local evidence. The Linux gate must run the complete fresh reproduction.

Round-2 independent proof is now GREEN: `receipts/CUVS-LUCENE-PR2702-CPU-ROUND-2.md` at board snapshot `21e01d9834ff77a66aef7287d8c5ee4c2da47c62` records Linux aarch64, JDK 21.0.12.1, package `2ad555a87a22acdf617f679ca03f5831f246b4a8` and its exact patch digest. That receipt does not cover this new file-compatibility test.

## Limits and next handoff

Independent Linux JDK 21 proof is pending under `gates/cuvs-lucene-backcompat-round-3.md`; its receipt must identify this patch and immutable package commit. The round-2 gate proves only its original package.

The cuvs-java API proof jar omits Java 22/native implementations. Thin jars are not Maven release artifacts. CPU reads of GPU-produced archives and CPU merges do not validate GPU construction/search, native integration, Solr integration or release packaging. No Gradle, Maven, CUDA/native build, GPU fixture generation, Solr edit, source-repository push or public submission occurred. The supplied upstream note remains a draft requiring Nick's review and posting approval.
