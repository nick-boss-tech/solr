# Claim: PR 5235 JDK raw-body capture repair, Windows, round 2

Status: CLAIMED

- Source: follow-up to Nick's request for regression tests and his round-1 gate result supplied in this chat.
- Host: Windows Codex desktop. Tags: implementation, review, records.
- Started: 2026-10-26 18:23 UTC.
- Board snapshot: 7c153810307077fef3ec87a28f55c6128a6619f5. The prior Linux gate is completed by its receipt and outcome deliverables; no separate PR 5235 JDK follow-up claim is present.
- Scope: repair only TestLegacyContentStreamCompatibility's HTTP capture fixture. Retain exact body assertions; separate the old JDK stream content type from the body regression. Preserve numeric embedded IDs from a7215a5bf1c79b3b7dbcbf9289aac2a43e31fbcd.
- Source evidence: base HttpJdkSolrClient.java lines 305-309 labels legacy streams application/x-www-form-urlencoded; lines 326-333 still create a body publisher from the stream. DebugServlet calls getParameterMap before reading getInputStream (ServletFixtures.java lines 242-244 and 270-277). Form parsing can consume the body before capture; this is a source-based hypothesis until the next Linux comparison.
- Fork destination: nick-boss-tech/solr:solr-18351-submit, based on a7215a5bf1c79b3b7dbcbf9289aac2a43e31fbcd.
- Deliverables: pushed test-only commit; reports/pr-5235-jdk-raw-capture-windows-round-2.md; gates/pr-5235-jdk-raw-capture-round-2.md.
- No production fix, Gradle/Java execution on Windows, upstream/public write, or alteration of the completed gate host's receipt/report.
