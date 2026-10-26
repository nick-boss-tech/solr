# Claim: PR 5232 listener correctness follow-up on Windows

Status: CLAIMED

- Claimant: windows, Codex desktop; tags: implementation, review, records.
- Started: 2026-10-26 15:07:13 UTC.
- Assignment: Nick's direct request in this chat, "Try and produce a branch on top of this PR that makes the fixes" following the PR 5232 review.
- Reviewed board snapshot: `93d53c2e8d7a2553cd7a71799402a3df9a66dde7`; no competing SOLR-11994 / PR 5232 implementation claim found.
- Base: PR 5232 head `2c9243b111b38367677d7e0c9b30b8e72c554d2c`, from sarkaramrit1993/solr.
- Scope: prevent CPU fallback double counting and exclude nested document subqueries from top-level search events; add discriminating regressions. Preserve legitimate top-level embedded searches.
- Work branch: `nick-boss-tech/solr:solr-11994-submit`, new branch from the PR head; local worktree `wt/SOLR-11994`.
- Deliverables: pushed fix commit; `reports/pr-5232-listener-followup-windows-round-1.md`; `gates/pr-5232-listener-followup-round-1.md` with the exact pushed head and Linux matched-proof requirements.
- Proof: no Gradle, formatting task, or queue execution on Windows. Implementation remains proof pending until Linux gate evidence lands.
- Decision ladder: rung 1 establishes CPU-value provenance and nested embedded query execution; rung 3 is the PR's explicit once-per-top-level-coordinator contract. Nick authorized a fork branch, not a public PR or comment.
- Public writes: none to apache/solr or JIRA; no modification to the contributor's original branch.

Completion is derived from the named report and gate records; this claim is immutable.
