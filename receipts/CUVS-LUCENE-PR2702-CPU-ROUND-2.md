# cuVS Lucene PR 2702: CPU regression proof, round 2 receipt

Independent CPU proof for the test patch `cpu-regression.patch` on NVIDIA/cuvs PR 2702 at base `23004d0f0de2014f11b09313f04fcf0de7643c3b`. Gate `gates/cuvs-lucene-pr2702-cpu-round-2.md`, claim `claims/cuvs-lucene-pr2702-cpu-gate-round-2-vm2.md`.

## Result: GREEN

- Package commit `2ad555a87a22acdf617f679ca03f5831f246b4a8` (`material/cuvs-lucene-pr2702-round-2/`, exported with git archive into a fresh runtime directory).
- Patch SHA-256 `d9ad46585c65846cf9b0d4f0ccffbf882e79be593f15897d8775115de810b154`, verified before the run.
- Host vm2 (Linux aarch64). JDK 21.0.12.1 (`/workspace/jdk/jdk-21.0.12.1+1`, set as CUVS_CPU_JDK). Python 3.11.17. Git 2.34.1.
- Runtime: `/workspace/gates/cuvs-pr2702-cpu-round-2/material/cuvs-lucene-pr2702-round-2`. Full log `/workspace/gates/cuvs-pr2702-cpu-round-2/reproduce.log`, started after the 21:12Z claim, finished 21:13Z.
- Commands: `CUVS_CPU_JDK=/workspace/jdk/jdk-21.0.12.1+1 python3 prepare_sources.py`, then `format_check.py`, then `python3 reproduce.py` (which runs compile_matrix.py, audit_matrix.py, run_cpu_tests.py --mutations). Both preflight scripts exited 0 before the claim. Preflight and the full run used no Gradle, Maven, Solr, or CUDA.

## Acceptance

1. Formatter: google-java-format 1.27.0 with `--set-exit-if-changed`, exit 0, no rewrite. Source blobs match `source-manifest.json` after `prepare_sources.py` (verified: applied the exact test patch).
2. Compile with `--release 21`, all four modules exit 0: module 10.2 (42 sources, Lucene 10.2.2), 10.3 (45, 10.3.2), 10.4 (50, 10.4.0), 10.5 (50, 10.5.1).
3. Whole-jar linkage, zero failure records on all four:

| Module | Classes | Checked constants and types | Member constants | Member uses | Failures |
| --- | --- | --- | --- | --- | --- |
| 10.2 | 64 | 692 | 338 | 556 | 0 |
| 10.3 | 67 | 703 | 343 | 562 | 0 |
| 10.4 | 72 | 723 | 346 | 566 | 0 |
| 10.5 | 72 | 729 | 346 | 566 | 0 |

4. `TestCompatKnnFloatVectorQuery`, seed `18556C0D`: 4 of 4 pass on each module, 16 positive tests in total, 0 failures, 0 skipped.
5. Mutation controls (mask dropped on 10.2 and 10.4):
   - Original one-test suite: 1 of 1 passes on both modules.
   - Patched suite on both modules: 4 run, exactly 3 failures: `testFilterIsForwarded`, `testDeletedDocumentsAreExcluded`, `testFilterAndDeletesAreIntersected`. These six are the expected new-test failures, not an initialization or linkage failure.

Evidence records added with this receipt (under `receipts/cuvs-lucene-pr2702-cpu-round-2/`): `matrix.json`, `linkage-summary.json`, `cpu-tests.json`, `format.json`, `reproduce.log`.

## Limits

- This is CPU compilation, static linkage, and query-adapter proof for the test patch and the inspected PR head. It is not a GPU run, a native cuVS build, an on-disk-format check, or an NVIDIA release.
- Timestamps: claim pushed 21:12Z (start). The log last written 21:13Z (finish).
- No upstream submission, no fork push, no Jira or GitHub comment. The owner keeps all public submission decisions.
