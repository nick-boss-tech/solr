# Claim: SOLR-18601 stopped-gate repair, Windows round 2

Status: CLAIMED, implementation and records only.

- Started: 2026-10-28 04:00 UTC (October 28 00:00 Eastern).
- Owner scope: continue the requested patch/gate handoff using Nick's supplied failed-gate update. No public posting.
- Host: windows; implementation, review, records. Linux gate execution remains with a capable gate host.
- Board snapshot: a8ae208b8d60b1a59623ac60ebf0e2c57917288d. Read receipt receipts/PR-5253-cleanup-round-1.md and superseding gate gates/pr-5253-cleanup-superseding-98d1127-round-2.md. The receipt stops vm2's execution at H, not green.
- Base H: 98d1127e4f5d4d149992e1dc494b95928bac057a, verified fork solr-18601-submit head. Retain gate formatting and constructor repairs. Clean local ticket branch fast-forwarded to H without resetting files.
- Decision ladder rung 1: zero is possible in a buffered add's log envelope because the command version is not set before buffering; the serialized document retains its nonzero source _version_. Replay reconstructs the document and versionAdd recovers that source version when the command version is zero. Thus distinct envelope versions are not a record count. Use the actual buffered-operations gauge and verify replayed source versions and document effects. Do not change production version assignment merely to satisfy a test oracle.
- Decision ladder rungs 1/3: handle recoverable AssertionError after confirmed rule installation, clean up, and wrap it as a reported SERVER_ERROR with the original cause to avoid Overseer error-reset retries. Cleanup assertion failures are suppressed on the original failure and do not resume target processing. Do not catch all Error or attempt resource-intensive cleanup after fatal JVM/resource errors.
- Deliverables: pushed repair head; reports/pr-5253-cleanup-repair-round-2.md; gates/pr-5253-cleanup-repair-round-3.md. New exact-head positive/negative proof required, including all outstanding legs. Failed receipt remains authoritative history; no green result is claimed.
- No Gradle, compiler, formatting task or tests on Windows. No retired local queue.
