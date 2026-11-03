# Gate: cuvs-lucene test port to NVIDIA/cuvs PR 2970, round 5

Status: OPEN

- Intended host: any capable Linux CPU gate host other than vm6 (vm6 wrote the port). One gate at a time on the host.
- Capability tags: gate, records.
- Owner direction: Nick, 2026-11-03, chose option 1 of `reports/cuvs-lucene-readiness-review-round-4.md` (port to PR 2970) and asked for the CPU proof to be handed off as a gate.
- Source: NVIDIA/cuvs PR 2970 at `3602ef0a8047ef222ad994a316d69ffec26e98bd`.
- Immutable package commit: `57e8a6c989ec63c306d571cd85435d38403cfdce` on nick-boss-tech/solr, coordination-ui history.
- Package path: `material/cuvs-lucene-pr2970-port-round-5/`.
- Patch: `port.patch`, SHA-256 `3185ea28f53520e9fd1e8b72d83b15d3f912c2cb88b6591c3c97989d19a8c36d`. Final blobs: `TestCompatKnnFloatVectorQuery.java` `f2692a1f698a3974994315772355327edb340530`, `TestBackCompatIndices.java` `cfcab142c250f96aed4131d61b5056271ac2635f`, the two `TestLuceneCompat.java` `fa04413d7e5537ba8cd03242908d5306c5a7673d` (102 bits) and `9bdc09a5865d21acb40a8812d867bdbc1293c592` (103 acceptdocs).
- Harness: `reproduce.py`, SHA-256 `ae14bc0960becb9987bfca7d9b5f68ec4228d5994bad21df1c21b1d8f2a6db28`.
- Report: `reports/cuvs-lucene-pr2970-port-round-5.md`.
- Claim: add a host-specific `claims/cuvs-lucene-pr2970-port-gate-round-5-<host>.md` after preflight. No edits to this gate or other claims.
- Required receipt: `receipts/CUVS-LUCENE-PR2970-PORT-ROUND-5.md`, written only by the executing gate host.

## Preflight and execution

Read WORKFLOW.md and the gate rules, this job, the report and the package README. Stand down if a live claim or receipt already covers this job. Record the board snapshot.

Before claiming, re-check the PR 2970 head read-only through the GitHub API. If it is no longer `3602ef0a` (moved or merged), do not run; record that in a short report so the port can be refreshed. A merged PR with the same head is fine: the pinned blobs still resolve.

Export the package directory with `git archive` from the exact package commit into a fresh runtime outside any Git checkout. The run needs Python 3, git, network access (raw.githubusercontent.com and Maven Central) and JDK 21, CPU only. Set `CUVS_CPU_JDK` to the absolute JDK 21 root. Delete the exported `results/` directory first: it is the implementer's dry run, and the gate writes its own. From the exported directory:

`python3 reproduce.py --preflight-only`

Preflight must exit zero: 113 source blobs verified, patch digest, `git apply --whitespace=error-all`, four final blobs, every Maven Central jar against its published SHA-1, and google-java-format 1.27.0 with `--skip-javadoc-formatting` and no change. Then claim and run:

`python3 reproduce.py`

Capture stdout and stderr to an absolute log outside the runtime. No Maven, Gradle, native cuVS, CUDA or GPU. Do not invoke GPU-only methods. Use no test exclusions, source edits, formatter rewrites or retry seeds. A test failure is classified with `pool/guides/failure-atlas.md` and investigated, not rerun. Preserve failed outputs.

## Acceptance

- Exact PR head, package commit, patch digest, harness digest and final blobs match this job.
- The cuvs-java API and both modules compile with JDK 21 `--release 21`, exit 0 (Lucene 10.2.0 and 10.3.2, as the PR 2970 module POMs pin).
- Query tests, seed `18556C0D`, on each module: PR 2970's class passes 3 of 3; the ported class passes 6 of 6. With the acceptance mask dropped in that module's `CompatKnnFloatVectorQuery` variant: PR 2970's class still passes 3 of 3, and the ported class fails exactly `testFilterIsForwarded`, `testDeletedDocumentsAreExcluded` and `testFilterAndDeletesAreIntersected`, as assertion failures (not linkage or initialization errors). Totals: 18 positive executions, 6 expected failures.
- Archived-index tests on each module, over the 7 released fixtures: PR 2970's `testReadIndices` and `testUpgradeIndices` pass; the ported class's two plus `testUpgradeIndicesWithDeletesAndSparseVectors` pass. Every `CPU_RESULT` line reads `run=1 failures=0 ignored=0 assumptions=0`. Totals: 10 method executions, 70 fixture cases.
- Data-fault control on each module: read and upgrade pass on the reassociated control index; the new method fails with a vector-for-ID array mismatch. Two expected failures. This is a data-fault control, not a production mutation.
- `reproduce.py` exits 0 and prints `VERIFIED` with the counts above and `PASS`.

## Receipt and evidence

Write the receipt GREEN only when every criterion passes; otherwise RED or PARTIAL with the exact failing leg. Record PR head as rechecked, package commit, digests, host and architecture, JDK and Python versions, commands, UTC start and finish, runtime and log paths, and the counts. Put the gate's `results/` JSON files, `summary.json` and the full log under `receipts/cuvs-lucene-pr2970-port-round-5/`. Do not edit this gate, the report, the package or other claims.

Limits to state: CPU proof of the test logic on Lucene 10.2.0 and 10.3.2 only. No GPU construction or search, native integration, Solr, or release packaging. Lucene 10.4 and 10.5 are not in PR 2970; their evidence stays with PR 2702 rounds 2 and 3. No upstream push, PR, or comment is authorized by this job.
