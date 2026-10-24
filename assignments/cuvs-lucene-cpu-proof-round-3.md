# Assignment: cuvs-lucene CPU proof reopened for round 3

Status: OPEN

- Intended host: any capable Linux CPU gate host, one gate at a time.
- Capability tags: gate, records.
- Owner direction: Nick confirmed the vm2 round-2 pass and asked to reopen the assignment for the new implementation input.
- Board snapshot reviewed: `28f8c7f8165cb6d5a4fc094103add8e4a8e82c1c`.
- Canonical gate job: `gates/cuvs-lucene-backcompat-round-3.md`. This assignment routes that existing job; it is not a second gate or a second run request.

## Completed input

Round 2 is GREEN. Receipt `receipts/CUVS-LUCENE-PR2702-CPU-ROUND-2.md`, introduced by `c6e0a5564bb6a9e6dda9b446b216965be9034e46`, covers package `2ad555a87a22acdf617f679ca03f5831f246b4a8` and patch SHA-256 `d9ad46585c65846cf9b0d4f0ccffbf882e79be593f15897d8775115de810b154`.

The vm2 Linux JDK 21 run verified preflight without a formatter rewrite, all four compiles and whole-jar linkage audits, 16 positive query-adapter tests, and both mask-drop controls with exactly the expected failures. Keep that gate and receipt intact. Do not rerun round 2 to close this assignment.

## Reopened input

- Board branch: `coordination-ui` in `nick-boss-tech/solr`.
- New immutable package head: `e174c1a3e0dcfccf9d30a31847637733823df84b`.
- Package: `material/cuvs-lucene-backcompat-round-3/`.
- Patch: `backcompat.patch`, SHA-256 `56cb5cb10f210e216c36fd413821dd1ddd931e5dd4ed26fc288299c928a84238`.
- Upstream source base remains NVIDIA/cuvs PR 2702 at `23004d0f0de2014f11b09313f04fcf0de7643c3b`.
- Implementation report: `reports/cuvs-lucene-backcompat-round-3.md`.
- Existing round-3 gate was posted at `28f8c7f8165cb6d5a4fc094103add8e4a8e82c1c`.

The new test covers archived-index upgrades with deletions, vectorless documents, and exact document-ID/vector-value association before and after merging. Local CPU proof passes 12 method executions across 138 fixture cases. Two separate data-fault controls pass the existing methods and fail the new value assertion. Independent Linux JDK 21 proof remains pending. This is an exported patch package on the board; no new NVIDIA source branch or public submission was created.

## Take this to completion

Resolve the canonical gate's live claim and `receipts/CUVS-LUCENE-BACKCOMPAT-ROUND-3.md` before work. If already claimed, do not duplicate it; if the matching receipt is GREEN, this assignment is complete by derivation.

Otherwise follow `gates/cuvs-lucene-backcompat-round-3.md` exactly: export the pinned package into a fresh runtime outside Git checkouts, set an absolute JDK 21 root in CUVS_CPU_JDK, run `python3 reproduce.py --preflight-only`, then add the host-specific gate claim named there and run `python3 reproduce.py`. Use that one claim for this assignment and its gate. Do not add a second implementation or gate claim just for this routing record.

Acceptance, failure handling, output counts and evidence requirements are those of the canonical gate. The executing host adds `receipts/CUVS-LUCENE-BACKCOMPAT-ROUND-3.md` and its evidence as the completion record. Gate and assignment state is derived from that receipt; no shared-index or prior-record edits are needed.

CPU only. No Gradle, Maven, Solr or native build, GPU validation, source-repository push, Jira comment, GitHub comment or NVIDIA submission. Public submission remains Nick's decision. Round-2 success does not cover this new patch.
