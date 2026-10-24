# Gate: cuvs-lucene archived-index compatibility, round 3

Status: OPEN

- Intended host: any capable Linux CPU gate host, one gate at a time.
- Capability tags: gate, records.
- Owner direction: Nick asked for continued implementation and authorized CPU-only gate handoffs. This is a separate follow-up to round 2; do not change or rerun the completed round-2 gate as part of this job.
- Source: NVIDIA/cuvs PR 2702 at `23004d0f0de2014f11b09313f04fcf0de7643c3b`.
- Immutable package commit: `e174c1a3e0dcfccf9d30a31847637733823df84b` on nick-boss-tech/solr, coordination-ui history.
- Package path: `material/cuvs-lucene-backcompat-round-3/`.
- Patch: `backcompat.patch`, SHA-256 `56cb5cb10f210e216c36fd413821dd1ddd931e5dd4ed26fc288299c928a84238`.
- Final test SHA-256: `642076a8446ad90f402ccac1e251903a1b3d990a72323223b3114c1e650cb534`, Git blob `ee3cd6734e410a58aa08d703b7f944e210bfdf0c`.
- Report: `reports/cuvs-lucene-backcompat-round-3.md`.
- Claim: add a host-specific `claims/cuvs-lucene-backcompat-gate-round-3-<host>.md` after preflight. No edits to this gate or other claims.
- Required receipt: `receipts/CUVS-LUCENE-BACKCOMPAT-ROUND-3.md`, written only by the executing gate host.

## Preflight and execution

Read current WORKFLOW.md and gate rules, this job, the report and package README. Resolve live claims and receipt first; stand down if this job is already covered or actively claimed. Record the board snapshot. Do not run concurrently with another gate on the host.

Export the material directory from the exact immutable package commit into a fresh, dedicated runtime outside any Git checkout. Do not reuse or modify the round-2 runtime. The reproduction needs Python 3, git, network access and JDK 21, all CPU only. Set CUVS_CPU_JDK to the absolute JDK 21 root. CUVS_CPU_GIT may identify git if needed. From the exported directory:

`python3 reproduce.py --preflight-only`

Preflight must exit zero, verify source/fixture Git blobs, patch application with whitespace errors rejected, final test identity and google-java-format 1.27.0 without a rewrite. Do not claim an input that needs source repair. Record a repair report instead. Then claim this exact input and run:

`python3 reproduce.py`

Capture stdout and stderr to an absolute log outside the source paths. The script retrieves pinned input drivers from round-2 package commit `2ad555a87a22acdf617f679ca03f5831f246b4a8`, rebuilds the API and all production variants with direct javac --release 21, verifies Maven dependency digests, then runs the original tests, the final test and data-fault controls. It ends by checking saved results with verify_results.py.

No Gradle, Maven, Solr, CUDA/native build or GPU fixture generation. No download or execution of the Java 22/native cuvs-java implementation. Never invoke testCreateIndices. Append/merge must use CPU fallback. Use no test exclusions, source edits, skip acceptance, formatter rewrites or retry seeds. A test failure is investigated as a repair; do not blindly rerun it. Preserve failed outputs and use a new runtime for an approved changed input.

## Acceptance

- Exact source head, package commit, patch digest and final test digest match this job.
- All four production variants and selected tests compile with JDK 21 and --release 21. API compilation uses actual pinned API sources. No errors masked as skips.
- Original testReadIndices and testUpgradeIndices pass on Lucene 10.2.2, 10.3.2, 10.4.0 and 10.5.1: eight method executions. Fixture counts are 7, 10, 13 and 16, respectively.
- testUpgradeIndicesWithDeletesAndSparseVectors passes on all four: four method executions. It checks 183 live documents, 173 vectors, original ID/value association before and after merge, and merge state. Positive totals: 12 methods and 138 fixture cases, with zero ignored tests or assumption failures.
- Separate CPU-written reassociation controls on 10.2 and 10.4: existing read and upgrade methods both pass on each; the new method fails on both specifically with a vector-for-ID array mismatch. These are data-fault controls, not a production-code mutation or a genuine archived-index failure. A linkage/init error or other assertion is not accepted as the expected failure.
- Full reproduce.py exits zero. verify_results.py confirms all recorded counts and final test hashes. No source or patch bytes changed.

## Receipt and evidence

Add the receipt with GREEN only after all criteria pass. Record package/source/digests, host/architecture, JDK and Python versions, command, UTC start/finish, absolute runtime/log paths, fixture counts, positive results and the two expected control failures. Include the baseline/final/mapping-control JSON, runtime and compile logs, local JDK 21 matrix.json, verifier output and full reproduce log under a new receipts evidence directory. Gate state is derived from the receipt; never edit the gate, implementation report, shared indexes, claims, or another host's receipt.

Label limits: this proves CPU file reading/upgrade and the added coverage. It does not validate GPU construction/search, native integration, Solr integration or release packaging. Round-2 linkage/query proof is already independently GREEN in its own receipt and remains separate. No source-repository push, public PR or comment is authorized by this job.
