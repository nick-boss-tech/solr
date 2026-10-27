# Claim: PR 5241 forbidden servlet writer repair, Windows round 2

Status: CLAIMED.

- Claimant: windows, Codex desktop; tags: implementation, review, records.
- Started: 2026-10-27 03:50:44 UTC (October 26, America/New_York).
- Assignment: continuation of Nick's patch-and-gate request after he relayed the gate's forbiddenApisTest failure at `AdminUiCoreOverviewErrorsTest.java:169`.
- Board snapshot: `39afc1b23109908e34f8571c78e8dbc35e3d25f1`. Linux gate claim remains `claims/pr-5241-overview-errors-gate-vm4-round-1.md`; this claim repairs source only and does not take over the gate.
- Source/fork head checked: `80ca9374f48f292af159c7e9600019b5947adc49`, clean local worktree `wt/SOLR-16398`, fork branch `solr-16398-submit`.
- Decision ladder rung 1: `gradle/validation/forbidden-apis/jakarta.servlet.jakarta.servlet-api.all.txt:28` explicitly forbids ServletResponse.getWriter(). Replace the fixture's writer with getOutputStream() and explicit UTF-8 bytes. Do not suppress the signature or change production behavior or assertions.
- Deliverables: scoped repair commit pushed and separately verified; add-only repair report `reports/pr-5241-forbidden-api-repair-windows-round-2.md`; superseding job `gates/pr-5241-overview-errors-round-2.md` pinned to the repaired SHA, retaining the original fail-before production base.
- Prior check failure is real and remains in the gate history. Earlier tidy/Node/whitespace successes cover only the old head. No browser success is inferred from the reported chromedriver setup retry.
- Windows runs no Gradle, browser tests, or local test queue. Gate host retains ownership of runtime setup, raw evidence, and the receipt.

This add-only claim extends the implementation record, not the Linux gate claim. Completion follows from its named deliverables.
