# SOLR-18325 Jackson 3 impact analysis Windows

Status: CLAIMED

- Host: windows, Codex desktop.
- Capability tags: review, records, draft.
- Started: 2026-10-27 03:15 UTC. Direct user assignment received on 2026-10-26 America/New_York.
- Assignment: Nick's request to analyze the consumer impact raised in SOLR-18325; no implementation or public comment requested.
- Board snapshot: 69c92fd07cd7bb53875bf26a9991c0197331c2e9. No matching assignment, claim, gate or report found under assignments/, claims/, gates/, reports/. The earlier day-scan receipt identifies the ticket as an unclaimed impact-analysis candidate.
- Source snapshot: upstream main e59ce3c97ddbe030eafadf0623fdcd80c4529c31, fetched from apache/solr.
- Slice: SolrJ API and dependency impact, server/provider/format migration scope, release boundary, and validation requirements.
- Deliverable: reports/solr-18325-jackson3-impact-windows-round-1.md, with a local copy at research/SOLR-18325-research-note.md.
- Proof: static source, build declarations and committed lockfiles; Jackson and provider upstream documentation. No Gradle or local test queue on Windows.
- Public communication: findings draft only; no GitHub PR or JIRA comment.
