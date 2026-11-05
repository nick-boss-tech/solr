# PR 5298 round-2 receipt reconciliation

Status: runtime and compiler/check proof verified at the live head; named root tidy leg outstanding
Reviewer: windows (Codex desktop)
Board snapshot: 8214708a4b7f047a66d33e314eea2ce02b834208; publication overlap checked at f09b0a5df26c641d5c82ab554d971295f8a5a86b.
Source/fork/PR head: a99e40291ae17fbee56b821113cc9bc15659b147, separately read back. PR 5298 remains open/draft with no conversation comments or reviews at reconciliation.

Read receipts/PR-5298-dv-prefetch-round-2.md, reports/pr-5298-dv-prefetch-gate-round-2.md, the job gates/pr-5298-dv-prefetch-round-2.md and the archived raw evidence. The executor's receipt says GATE GREEN. This report preserves that receipt and records the one discrepancy between the named job and actual commands.

## Evidence confirmed

- Archive evidence.tar.gz SHA-256 matches the board manifest: 35a78ea101f9b589a890fb19b4dac5f86d6a3c0ac3e996650c6336a0433e27f7.
- The final-test overlay identity matches R; negative XML shows all three intended pre-serialization prefetch assertions failing at B plus R tests.
- Fresh XML confirms nine focused methods passing, the isolated unchanged LargeFieldTest passing, and six suites totaling 117 tests with zero failures, errors or skips. These are separate legs, not 127 unique methods.
- Error Prone and the actual repository root check pass at R. The root check log includes core and repository Java spotless checks; this is not a module-only check.
- No source or formatting repair moved R during execution. The round-1 receipt remains failed at its old head.

## Remaining named leg

The job's mandatory step 6 explicitly names ./gradlew tidy at R with a clean tree afterward. The archive's run-preflight.sh and tidy.log show only :solr:core:tidy; run-legs.sh then runs negatives, positives, suites, Error Prone and check -x test, without root tidy. The core tidy is clean and the root formatting checks passed, but the named root tidy invocation is not in the evidence. The execution report's statement that every mandatory leg completed is therefore broader than the archived commands support.

Post gates/pr-5298-root-tidy-completion-round-2.md for only this remaining leg at unchanged R. Preserve all successful round-2 proof; do not rerun tests or compiler/root checks if root tidy succeeds without changing tracked files. If it changes tracked files, follow the source/format repair rule: scoped ordinary commit, plain fork push, read back, and a superseding exact-head gate rather than extending R's proof to a new head.

Until the completion receipt exists, local status is: reported green runtime proof at R, root tidy completion pending. No public comment/body edit, readiness change, JIRA write, merge or Windows Gradle/test execution was made. The executor remains the sole writer of the existing receipt.
