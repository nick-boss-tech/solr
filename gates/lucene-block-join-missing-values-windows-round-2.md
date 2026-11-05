# Lucene block join Windows proof: typed paging repair

Status: CLAIMED (windows, Codex desktop)

Supersedes `gates/lucene-block-join-missing-values-windows-round-1.md` for its old source head. All proof requirements and the Windows-only owner authorization from that job remain in force at this new pin.

- Repository/branch: `nick-boss-tech/lucene`, `fix-block-join-missing-values`.
- Exact pushed and separately verified head: `55c3174bd154b5cc987d9b24e1c77043e23fe71e`.
- Upstream base: `aa366ae209ae8296dd70d2835c6d47f8fc201972`.
- Claim: `claims/lucene-block-join-missing-values-windows-proof-round-1.md`.
- Final execution-host receipt: `receipts/lucene-block-join-missing-values-windows-round-1.md`; it must identify this superseding gate and exact final head.

The first focused attempt at `a9ce0a45074ac2708c2c0ade7cc67162870d2e88` compiled production but failed to compile the newly added paging test: IndexSearcher's four-argument searchAfter returns TopDocs, not TopFieldDocs. No tests ran. The new commit uses the five-argument typed overload with doDocScores=false. Join tidy passed before the ordinary commit and push. This is a diagnosed test compilation repair, not a production assertion failure or runtime negative proof.

Run fresh positive focused suites, full join module, root `check -x test`, and all four individually named runtime fail-before methods from round 1, seed `18622C0DE`, Temurin 25.0.5+8 and Gradle 9.8.1. The fail-before worktree has this exact head with only ToParentDocValues.java restored from the pinned upstream base; both test files stay unchanged. Retain source-diff, arguments, logs and fresh XML per leg. The previous setup/compilation failures remain separate non-runtime outcomes. Diagnose any new failure before retrying; source repairs require another pushed head and superseding record. No public contribution is authorized.
