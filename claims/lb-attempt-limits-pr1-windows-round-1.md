# Claim: LB attempt limits, PR 1 implementation

Status: CLAIMED (direct owner instruction, 2026-10-26)

- Host: windows, Codex desktop.
- Tags: implementation, review, records.
- Started: 2026-10-26 21:34 UTC.
- Reviewed board snapshot: e9bc0cdb59a77c4048294f030ddce28a94b8d2c0.
- Source base: apache/solr main 0d6b2e32f598d772aed094038022a6a588fdfc5a.
- Authority: Nick sent the mailing-list scope proposal and explicitly requested implementation of PR 1 only. PRs 2 and 3 wait for mailing-list feedback.
- Branch: nick-boss-tech/solr:lb-attempt-limits-pr1 (descriptive branch pending a JIRA/PR number; no unrelated ticket key reused).
- Scope: make existing LBSolrClient attempt limits consistent across sync/async and zombie fallback; identifiable exhaustion with last failure; deterministic dispatch-count regressions; concise API documentation. Preserve retry safety and defaults.
- Exclusions: CloudSolrClient policy/API, server settings, shard propagation, global retry budget, retry classification consolidation (SOLR-18402 / PR 4829), request retriability (SOLR-18341), public PR/JIRA/mail writes.
- Deliverables: fork implementation commit, reports/lb-attempt-limits-pr1-windows-round-1.md, gates/lb-attempt-limits-pr1-round-1.md.
- Proof: no Gradle, tests, or local test queue on Windows. Post an exact-head Linux gate with focused tests, baseline discrimination and formatting/check requirements. Remain Implemented, Proof Pending until a matching receipt succeeds.
- Decision ladder: rung 3, explicit owner scope; rung 1, retain the existing limit API and retry-eligibility contract. No matching board assignment or claim was found in the reviewed snapshot; no matching limit ticket appeared in the JIRA search. Repository AGENTS.md forbids adding a changelog before an actual JIRA/PR number is assigned.

The claim is add-only. Completion is recorded in the report and gate named above.
