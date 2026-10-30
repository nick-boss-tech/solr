# Claim: PR 2452 UBI recording context rework and Linux gate, round 1 (vm4)

Status: CLAIMED (implementation, then Linux proof)

- Claimant host: vm4.
- Started: 2026-10-30T01:50:57Z.
- Assignment: `assignments/pr-2452-ubi-context-rework-and-gate-round-1.md`. Board snapshot inspected: `238336be0a`.
- Tags: implementation, gate, review, records, draft.
- Base, preferred path: apache/solr PR 2452 head H `6402b4b528a3ec8daa134772abc84532380e3f7c`, verified live at claim time (`refs/pull/2452/head` and `epugh/solr:ubi` both at H). Fallback to pinned main is not planned unless a compatibility problem appears.
- Source candidate: `nick-boss-tech/solr:solr-11994-ubi-context-submit` at F0 `09343928e7d2ba5cb5e7def1d0a550337b67a7d2`, parent I0 `3a362dcbcb036a4cfe9a6df413057d90666cd070`. I0 imports UBIComponent.java and LoggingStream.java byte-identical to H (verified). Only I0..F0 is ported: UBIComponent.java (fresh StreamContext per recording) and UBIRecordingContextTest.java.
- Intended fork branch: `nick-boss-tech/solr:ubi-pr2452-context-submit` (new, no JIRA key, no PR number in commit messages).
- Preserved, not touched: `solr-11994-ubi-context-submit` at F0, `solr-11994-pr5238-ubi-submit` at `c2c387064a`. Old gate `gates/pr-5238-ubi-context-round-1.md` is not claimed or run, per `gates/pr-5238-ubi-context-round-1-standdown.md`.
- Competing claims: none found for PR 2452 in `claims/` or `gates/`. The Windows analysis claim `claims/pr-5238-ubi-context-windows-round-1.md` is the retired slice's record and is not active here.
- Deliverables: pushed `ubi-pr2452-context-submit` candidate; `gates/pr-2452-ubi-context-round-1.md`; `reports/pr-2452-ubi-context-rework-and-gate-round-1.md`; `receipts/PR-2452-ubi-context.md`; offer draft in the report.
- Constraints: no PR comment, PR create or edit, Jira write, push to epugh/solr or apache/solr, force push, or merge. Commits as Nick Shanin, no trailers.
