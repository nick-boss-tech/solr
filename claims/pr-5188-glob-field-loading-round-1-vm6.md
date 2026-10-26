# Claim: PR 5188 glob field loading, Linux gate round 1 (vm6)

- Claimant host: vm6, matching `hosts/vm6.md`.
- Job: `gates/pr-5188-glob-field-loading-round-1.md` (OPEN; Windows implementation claim `claims/pr-5188-glob-field-loading-windows-round-1.md`).
- Pinned input: fork `nick-boss-tech/solr:pr-5188-glob-field-loading-followup`, candidate head `b71a1687bf98cf7b4b3edf635bfb89f25e4af8e2`, tests-only pre-fix `4ccd146b904b4574dbd412163f5c88b5f2e0de6b`. Fork tip read back as the candidate head before this claim. Upstream base `e07455a0f953e702da27f677c51ea894b7bafc3a`.
- Board snapshot: `dd21b62616c4ddf24c3e7a20c85b5b1ca11875a8`.
- Preflight: detached tidy at the candidate, `/workspace/gates/logs/vm6/preflight/pr5188-glob-field-loading-round-1`, exit 3. Tidy changed 3 files, formatting only: a javadoc rewrap in `SolrDocumentFetcher.java` and argument-list reflows in `BasicFunctionalityTest.java` and `TestPseudoReturnFields.java`. Patch: `tidy.patch` in that directory.
- Plan under the gate's repair rule: commit the tidy output as a plain fork commit on the same branch (Nick Shanin, no trailers). Re-run the matched proof on the new final head. The pre-fix leg uses original production `e07455a` with the final test files overlaid, and records those overlay hashes. A superseding gate and a repair report name the final head.
- Capability tags used: gate, implementation, records.
- Host: vm6, Linux aarch64, Temurin JDK 21.0.12.1+1, Gradle wrapper from the branch, build lock `/workspace/tools/.gradle-vm6.lock`.
- Started: 2026-10-26T15:14:39Z (UTC).
- Lease: live while heartbeats continue (in `hosts/vm6.md`); stale 2 hours after the last heartbeat. Attempts: 1. Idempotency key: pr5188-glob-round1@b71a1687bf98.

Result: none yet.
