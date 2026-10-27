# SOLR-18325 binary compatibility attribution review Windows round 1

Status: CLAIMED

- Host: windows. Tags: review, records.
- Started: 2026-10-27 16:51:41 UTC.
- Source: owner forwarded assignments/vm2-solr-18325-owner-decision-round-1.md and asked to continue the Solr 10 feasibility investigation.
- Board snapshot: d5c92b3f35d26f458f4a8e81d02f2cf09b488612.
- Read receipt: receipts/SOLR-18325-solr10-feasibility.md, round 2 at candidate 8bfca8edd30acf11bdf9d6a733312e97f99ec78a.
- Scope: identify whether the builder failure is inherited; confirm the test oracle design and existing CoreAdmin tracking; post a bounded Linux control job. No production shim, requirement waiver, public communication or receipt rewrite.
- Decision ladder: source history identifies an independent builder refactor (rung 1), but the matched unchanged-base artifact control is missing (rung 2). Keep the binary compatibility requirement and obtain that control before choosing a source repair.
- Deliverables: reports/solr-18325-binary-attribution-windows-round-1.md and gates/solr-18325-binary-attribution-round-1.md.
- Windows does not run Gradle or the local test queue. Next executable step belongs to a capable Linux host.
