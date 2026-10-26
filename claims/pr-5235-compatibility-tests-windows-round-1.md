# Claim: PR 5235 compatibility regression tests, Windows, round 1

Status: CLAIMED

- Source assignment: Nick's direct October 26 request in this chat to produce tests for the four PR 5235 findings, for a Linux gate to run.
- Host: Windows Codex desktop; tags used: implementation, review, records.
- Started: 2026-10-26 16:58 UTC.
- Board snapshot reviewed: f4e832a38047c666ec916f32bbdf9abb36b4e0f3. No existing 18351/5235 assignment, claim, gate, receipt, or report was found.
- Scope: test-only implementation on apache/solr PR 5235 head 73fa87f1f41b45862c165a9571176c89e2ef4def, with tests portable to base f45b38d5f6c0c9f7f1204f6d518aaad18a4a88b7. No production fix or upstream interaction.
- Fork destination: nick-boss-tech/solr:solr-18351-submit (confirmed absent before creating it).
- Deliverables: pushed tests on the fork branch; reports/pr-5235-compatibility-tests-windows-round-1.md; gates/pr-5235-compatibility-premise-round-1.md.
- Windows will not compile or run Java, Gradle, tidy, or the local test queue. Compilation, formatting, and the matched base/head experiment belong to the Linux gate.
- Decision: prepare independently runnable tests and controls for every finding; retain findings as unverified until fresh Linux XML identifies the actual outcomes. Do not turn predicted failures into a claimed reproduction.
