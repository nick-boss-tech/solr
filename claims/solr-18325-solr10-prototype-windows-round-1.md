# SOLR-18325 Solr 10 feasibility prototype Windows round 1

Status: CLAIMED

- Host: windows, Codex desktop. Capabilities: implementation, review, records, draft.
- Started: 2026-10-27 03:28 UTC (see commit time for authoritative publication).
- Direct owner assignment: determine whether Jackson 3 is viable for Solr 10, scan contracts, prepare a candidate and hand Linux execution to a gate agent. No public comment or PR authorized.
- Board snapshot: 2840e973baa5adb2c68ad88cd0d1e486ef1e9071. Prior analysis is reports/solr-18325-jackson3-impact-windows-round-1.md; no competing prototype assignment or claim found.
- Baseline: apache/solr branch_10x f45b38d5f6c0c9f7f1204f6d518aaad18a4a88b7, not main/11.
- Scope: bounded SolrJ Jackson 3 prototype, preserve server Jackson 2 integration; independent read-only supported API scan, meaningful consumer compatibility probes, Linux dependency/build/test preparation and gate.
- Decision ladder: rung 2, a prototype and controlled consumer probes are needed; source package changes alone do not establish release viability. The direct user request authorizes this investigation.
- Fork source branch: solr-18325-submit. Windows will commit/push source and verify remote head; no Gradle, tidy or local test queue here.
- Deliverables: reports/solr-18325-solr10-feasibility-windows-round-1.md and gates/solr-18325-solr10-feasibility-round-1.md. Linux must record final tested head and retain separate client-only/full-server verdicts.
- Timestamp note: the Started line is approximate session bookkeeping; use GitHub commit time for the precise UTC publication time.
