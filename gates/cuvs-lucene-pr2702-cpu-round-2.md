# Gate: cuvs-lucene PR 2702 acceptance-mask CPU regression, round 2

Status: OPEN

- Intended host: any capable Linux CPU gate host. No GPU is needed or requested.
- Capability tags: `gate`, `records`.
- Owner direction: Nick, 2026-10-24, authorized the implementation and a CPU-only gate handoff in the originating chat.
- Implementation claim: `claims/cuvs-lucene-pr2702-cpu-fix-round-2.md` (Windows).
- Upstream: NVIDIA/cuvs PR 2702, **`23004d0f0de2014f11b09313f04fcf0de7643c3b`**.
- Package branch: `nick-boss-tech/solr:coordination-ui`, immutable package commit **`2ad555a87a22acdf617f679ca03f5831f246b4a8`**.
- Material: `material/cuvs-lucene-pr2702-round-2/` at that commit.
- Patch: `cpu-regression.patch`, SHA-256 **`d9ad46585c65846cf9b0d4f0ccffbf882e79be593f15897d8775115de810b154`**.
- Exact code identity: the pinned upstream commit plus this patch; `source-manifest.json` records the original blobs and the three resulting test-source blobs. This is a durable patch package, not an unpublished local-only commit or a changed upstream PR head.
- Phase: independent CPU compilation, static linkage and query-adapter regression proof. No GPU or on-disk-format proof is implied.
- Receipt to add on completion: `receipts/CUVS-LUCENE-PR2702-CPU-ROUND-2.md`.

## Claim and inputs

Read the live board bootstrap and check for another live claim for this gate before taking it. Claim only this gate, as `claims/cuvs-lucene-pr2702-cpu-gate-round-2-<host>.md`, after the formatting preflight below. Record host, JDK version, Python version, the package commit and patch digest, source revision, runner/runtime directory, stdout/stderr path and start time. Keep the host heartbeat under `hosts/`; do not edit this gate or the implementation claim.

Use a fresh runtime directory owned by the gate claimant. Copy the material directory from a clean board snapshot at **`2ad555a87a22acdf617f679ca03f5831f246b4a8`** into that runtime directory. Do not run the scripts in a shared board checkout and do not substitute a newer upstream PR revision or a mutable branch tip.

Requirements: Python 3, git, network access to public GitHub raw files and Maven Central, and an existing **JDK 21** installation. Set `CUVS_CPU_JDK` to its absolute root. `CUVS_CPU_GIT` may name git if it is not on PATH. The scripts do not install a JDK. Prefer JDK 21 because that is the PR's minimum CPU runtime and the implementation host already ran JDK 22.

Verify the patch SHA-256 first. Run `prepare_sources.py` and `format_check.py` as preflight. They verify the source Git blob IDs, apply exactly the three-file test patch, verify the resulting blobs and run Google Java Format 1.27.0 without editing. If preflight changes or rejects source, record a repair finding; do not silently reformat/rebase the package under this gate's original identity.

## Required CPU proof

After successful preflight and claiming, run the provided entrypoint from the copied material directory:

```bash
python3 reproduce.py
```

It executes only source preparation, the pinned formatter, direct javac, class-file inspection, and JUnit on a plain CPU JVM. No CUDA tools, native cuVS implementation, native build, Maven build, Solr build, Gradle or local Solr test queue. Do not run the automatic Gradle reproduction strings printed inside Lucene's expected mutation-failure logs.

The proof compiles the real Java 21 public cuVS API from the same upstream revision, excluding `module-info.java` and the Java 22/native implementation. Then it compiles every production source selected by each versioned module POM. This is intentional CPU proof, not official Maven artifact packaging or a native-capable release build.

Acceptance requirements, all mandatory:

1. The formatter exits 0 without rewriting any patched source. Exact resulting source blobs still match `source-manifest.json`.
2. All four production source sets compile with `--release 21`: module 10.2 on Lucene 10.2.2, 10.3 on 10.3.2, 10.4 on 10.4.0, and 10.5 on 10.5.1. Save `matrix.json`, per-module source lists, commands and logs.
3. All four whole-jar inventories report **zero** Lucene linkage failures. The scanner checks every class and all Lucene references, exact descriptors, owner kind, inherited resolution and instruction/method-handle staticness; do not replace it with a grep or a sampled check. Save all four full inventories and their summary.
4. `TestCompatKnnFloatVectorQuery` runs **four tests on each module, 16 positive tests total**, seed **`18556C0D`**. No failure or GPU-related skip counts as success. Both query rewriting and direct approximate-adapter paths must execute.
5. On each of the two CPU mutations, the original one-test suite passes and the patched suite runs four tests with **exactly these three expected failures**: `testFilterIsForwarded`, `testDeletedDocumentsAreExcluded`, `testFilterAndDeletesAreIntersected`. Mutations discard the acceptance mask using valid `Bits`/`AcceptDocs` calls. These are required fail-before proofs, not failures to fix. An initialization/linkage failure is not acceptable mutation discrimination. The original segment-count case still passes.

The entrypoint checks these outcomes automatically, including JUnit counts and exact failing method names. Keep raw logs as well as JSON summaries. Do not narrow the modules, remove the direct-adapter assertions, accept an exact-search fallback as evidence of correct approximate forwarding, or use a skipped test as proof.

## Failure decisions and completion

If a positive compile, linkage or JUnit leg fails, preserve the logs and investigate the cause before any rerun. Treat a source or test repair as a new input requiring a new published patch/package identity and replacement gate. Do not relaunch an unchanged failing run or silently alter versions, seed, assertion scope or dependency APIs. An environment failure before JUnit executes may be repaired once with its reason recorded.

On success, add the receipt with package/upstream/patch identities, source blob verification, host/JDK/Python, exact commands, timestamps, log paths, source/class/reference counts, 16 positive tests, original mutation controls, the six expected new-test mutation failures, and the no-GPU/no-native limits. State plainly that this is independent CPU proof for the test patch and the inspected PR head, not a new NVIDIA release or complete GPU/file-format validation.

Publish the receipt and any compact evidence as new records on coordination-ui using the normal add-only board workflow. The gate host owns its receipt. Do not edit indexes, old claims, this gate, the upstream PR, Jira or public comments. Do not push source code to NVIDIA or a fork. The owner retains all public submission decisions.
