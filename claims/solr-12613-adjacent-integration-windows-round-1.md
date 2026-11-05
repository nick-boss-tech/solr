# Claim: compose the adjacent follow-up patches for integration proof

- Assignment `assignments/solr-12613-adjacent-implementation-round-1.md`; active owner goal to implement adjacent code/tests, with separate branches allowed.
- Host windows; tags implementation, review, records. Started 2026-11-05 UTC (November 4 Eastern). Attempts 1, lease 2 hours after last activity.
- Scope: create a separate integration candidate containing the four published slices. Preserve each independent branch/head and merge ancestry, resolve any real integration conflicts explicitly, plain-push the result and post an exact-head Linux integration gate. No speculative new feature scope is assigned.
- Baseline `b45bae5da3a151c7b439ff4e42009c383b538fae`, main plus picker plus the already-gated browser prerequisites.
- Inputs, independently re-read from the live fork: encoded-key `bd05d1ed39a005317f0a6655e4cd8fed078531b8`, path-coverage `96747864862bf716daf451e02a7c24194d41c267`, lifecycle `a331e7faf96c8c215f2d623f4865d4cbabca6492`, request-targeting `67cc03dfbed1866216c8cda8c31a65beb2c5672e`.
- Target branch `pr-5110-followup-integration`; local worktree `wt/PR-5110-integration`.
- Rationale: C3 initialization and C4 ready-request pinning form the combined selected-node contract. Individual branch proof must not be described as proof of the composed tree. The two core test changes also share a file and need a real merge check.
- Gate state rechecked at board `5387161baba446f3db23dca4b57847a0a528d824`: no required Linux receipt or execution claim is present. Windows remains forbidden from Gradle, compilers, browser runs and tests. Source composition and static Git checks only.
- Deliverables `reports/solr-12613-adjacent-integration-round-1.md` and `gates/solr-12613-adjacent-integration-round-1.md`, receipt target `receipts/PR-5110-adjacent-integration-round-1.md`. Independent slice jobs stay required; no proof or public-post authorization transfers to the combined head.
