# Claim: SOLR-18623 failed-gate source and stress-fixture repair, round 3

Status: CLAIMED (Windows implementation; Linux proof pending)

- Claimant: windows, Codex desktop, 2026-11-05. Continues Nick's authorization to patch PR 5301 and hand it to the Linux gate.
- Input snapshot: receipt and report at `75bc158448ef0781d5ccbaf0f9abe35856a6b300`; refreshed board tip `ad7999e58c7af2387fc9f9436a40d8a150565ea3` before publishing. Existing phone-agent gate claim completed with GATE FAILED, not a live competing implementation claim.
- Failed head H2: `27a9cf105b25f1bf3d604534cf8bdd8674c470fc`; fork branch `solr-18623-reconnect-liveness`. Local clean worktree was fast-forwarded to H2. Preserve H0/H1/H2 ancestry and use ordinary commits and fork-only plain pushes.
- Failure evidence: `receipts/PR-5301-reconnect-liveness-round-1.md`, `reports/pr-5301-reconnect-gate-round-2.md`, and the evidence archive. Downloaded archive SHA-256 `80a8d934a581dfb6979c22943793795cfb7717044fd2691057582637f4c29e33` and all 40 manifest file hashes were checked locally before diagnosis.
- Tags: implementation, review, records, draft. No Windows Gradle, compiler, formatter or test execution.
- Source slice: add the required standard `assumeWorkingMockito()` guard to ConnectionManagerOrderingTest; make the nightly stress test drop proxy connections with the existing `halfClose()` API while preserving its listening socket. Keep all assertions, seed and timeouts. No production-code change is planned.
- Diagnosis: both raw stress logs show the replacement receiving repeated connection-refused responses from the proxy after close/reopen, with no fresh proxy acceptance. SocketProxy.reopen catches setup/bind errors and logs only at debug; the exact hidden exception was not recorded. Keeping its acceptor open removes this unnecessary listener lifecycle from a test of client connection loss. This is a source-supported fixture repair hypothesis, not a claim that a rerun already passed or that a specific bind exception was proven.
- Deliverables: scoped repair commit and verified remote SHA; `reports/pr-5301-reconnect-repair-round-3.md`; `gates/pr-5301-reconnect-liveness-round-3.md`; updated local and add-only board comment draft stating the failed/pending proof accurately.
- Linux proof: rerun the failed stress at seed 5301C0DE twice in separate fresh invocations; root source-pattern/root checks; all positive and negative legs at the new exact head. Classify the H0-reproduced FloatRangeFieldTest Error Prone warning separately; do not repair unrelated code or silently waive root check.
- Decision ladder: two established test-file changes are separable on Windows. Runtime confirmation stays with Linux. No new owner decision is required.
