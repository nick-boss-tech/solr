# cuVS Lucene archived-index compatibility, round 3: CPU gate receipt

## Result: GREEN (vm2)

- Gate: `gates/cuvs-lucene-backcompat-round-3.md`, routed by `assignments/cuvs-lucene-cpu-proof-round-3.md`. Claim: `claims/cuvs-lucene-backcompat-gate-round-3-vm2.md` (board snapshot `3dc46ddd6689`).
- Package: `material/cuvs-lucene-backcompat-round-3/` at `e174c1a3e0dcfccf9d30a31847637733823df84b`, exported with git archive into a fresh runtime `/workspace/gates/cuvs-backcompat-round-3/material/cuvs-lucene-backcompat-round-3`.
- Source base: NVIDIA/cuvs PR 2702 at `23004d0f0de2014f11b09313f04fcf0de7643c3b`. Pinned driver inputs from round-2 package commit `2ad555a87a22acdf617f679ca03f5831f246b4a8`.
- Patch `backcompat.patch` SHA-256 `56cb5cb10f210e216c36fd413821dd1ddd931e5dd4ed26fc288299c928a84238` (verified before the run).
- Final test SHA-256 `642076a8446ad90f402ccac1e251903a1b3d990a72323223b3114c1e650cb534` (verified in `final.json`). Original test source SHA-256 `dfcc584643cffb42303984affc19ecb2a645b9f675b68005c02dacd9bd66fab9` (verified in `baseline.json`).
- Host vm2, Linux aarch64. JDK 21.0.12.1 at `/workspace/jdk/jdk-21.0.12.1+1` (CUVS_CPU_JDK). Python 3.11.17.
- Commands: `CUVS_CPU_JDK=/workspace/jdk/jdk-21.0.12.1+1 python3 reproduce.py --preflight-only` (rc 0, `preflight.log`), then `python3 reproduce.py` from the exported directory. No Gradle, Maven, Solr, native, or GPU build. The Java 22/native cuvs-java implementation was not downloaded or run. `testCreateIndices` was not invoked.
- Timestamps (UTC): claim pushed and run started at 21:45:40Z; `reproduce.log` last written 21:46:37Z. Preflight log in the same runtime.
- Log paths: runtime `/workspace/gates/cuvs-backcompat-round-3/`, full log `reproduce.log`, results `material/cuvs-lucene-backcompat-round-3/results/`.

## Acceptance

1. Preflight rc 0. Source and fixture blobs verified. Patch applied with whitespace errors rejected. Final test identity verified. google-java-format 1.27.0 without a rewrite. Full run rc 0 and the scripts printed PASS.
2. Compile with JDK 21 and `--release 21`: cuvs-api (44 sources, 95 classes) and modules 10.2 (42 sources, 64 classes), 10.3 (45, 67), 10.4 (50, 72), 10.5 (50, 72), all exit 0 (`matrix.json`).
3. Original `testReadIndices` and `testUpgradeIndices` pass on 10.2.2, 10.3.2, 10.4.0, and 10.5.1. Fixture counts 7, 10, 13, 16, as `baseline.json` and the verifier confirm.
4. `testUpgradeIndicesWithDeletesAndSparseVectors` passes on all four modules (`final.json` exit 0 per module). It checks 183 live documents and 173 vectors, original ID/value association before and after merge, and merge state.
5. Totals: 12 positive method executions across 138 fixture cases, 0 ignored, 0 assumption failures (verifier: "Verified: 12 positive methods, 138 fixture cases").
6. Data-fault controls (CPU-written reassociation, not a production mutation), on 10.2 and 10.4: the original read and upgrade methods pass on each. The new method fails on each with `vector for id 1: arrays first differed at element [0]; expected:<0.6655489> but was:<0.7275637>` (`mapping-control.json` exit 1 on both, expected failure `testUpgradeIndicesWithDeletesAndSparseVectors`). Two expected failures, no linkage or initialization error.
7. `verify_results.py` confirmed all recorded counts and the final test hashes (`verification.log`). No source or patch bytes changed.

## Evidence

Under `receipts/cuvs-lucene-backcompat-round-3/`: `matrix.json` (JDK 21 compile matrix, from the stdout lines of `reproduce.py`), `baseline.json`, `final.json`, `mapping-control.json`, `verification.log`, `reproduce.log`, `preflight.log`, and the per-variant compile and test logs under `results/`.

## Limits

This proves CPU file reading, upgrade, and the added sparse-vector and deletion coverage, and the two data-fault controls. It does not validate GPU construction or search, native integration, Solr integration, or release packaging. Round-2 linkage and query proof remains its own receipt. No source-repository push, public PR, or public comment was made. Public submission stays with Nick.
