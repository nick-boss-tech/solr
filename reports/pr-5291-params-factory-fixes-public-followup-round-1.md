# PR 5291 factory fixes: green proof and existing-comment update

Status: GATE GREEN at H2; existing upstream comment updated and separately verified.

- Authority: Nick's direct chat instruction on 2026-11-05, "We got a receipt, if it looks good then we can update our comment on this PR." The required proof was reviewed before the conditional public write. Original implementation claim: `claims/pr-5291-params-factory-fixes-round-1-windows.md`.
- Receipt reviewed from board snapshot `b4d6aac56497373ed0bc4f3d408221f5985970f7`: `receipts/PR-5291-params-factory-fixes-round-1.md`, plus `gates/pr-5291-params-factory-fixes-round-2.md` and all requirements of round 1. A later fetched board tip `5504819e20e` changed only the unrelated SOLR-18624 original-seed receipt.
- Contributor/live PR head H0: `600e0ec4afd82004a94852c419892897be14e6f0`. Implementation H1: `f2952c41c7c023aa6e79868df9efe8c65b02c993`. Tested and offered H2: `199911b298fac80f4cea5e3de553675b64f6a6a9` on `nick-boss-tech/solr:solr-18621-params-factory-fixes`.
- H2 is an ordinary formatting-only child of H1: one expectThrows call wrapped in SolrParamTest.java. Its full diff, Nick Shanin identity and contributor ancestry were checked. git diff --check passed. Fork refs API reads before posting matched H2. The public PR remains at H0.

## Proof assessment

Accepted: 44 parameter tests across five SolrJ suites and 20 core consumer tests across CoreAdminHandlerTest and StreamToolTest, all with zero failures/errors/skips; all six required adapted-baseline isolation methods fail at their intended first-element array assertion and pass at H2; uncast ModifiableSolrParams.of(null) compiles and returns empty params; Error Prone compilation across solrj/test-framework/core/benchmark; clean post-repair tidy; root check -x test successful at H2 in a verified-head normal clone, with the execution host CA truststore for its egress relay.

The original preflight stale-daemon journal lock occurred before any test ran and was resolved, then formatting was repaired under a superseding exact-head job. All accepted proof covers H2. The changelog cross-branch Python validation script crashed during branch discovery before reading entries. The repository's logchangeGenerate parser successfully parsed the entry and produced the expected authors/summary, and plain YAML also parsed. This satisfies the requested repository-parser validation; the failed Python script is disclosed rather than counted as a pass. The public comment discloses the root-check execution condition and changelog substitution.

The runtime negative leg is explicitly an adapted original shallow-wrapper algorithm under the executable renamed API, not an unmodified H0 suite. Input-copy and secondary assertions remain coverage pins. The ofPairs naming alternative remains a contributor/maintainer API choice and is presented that way. No remaining patch, scope or evidence concern prevented offering the tested changes.

## Public update and read-back

- Updated existing comment ID `6651292142`, authored by nick-boss-tech, using PATCH; no duplicate comment was created.
- URL: https://github.com/apache/solr/pull/5291#issuecomment-6651292142
- Stored update time: `2026-11-05T07:07:22Z`.
- The original comment said the CI failure was unrelated, linked PR 5294, and promised a forthcoming review. The update retains that CI context, then offers the two fixes, a pinned H0-to-H2 diff, exact verification counts/limits, and git switch/fetch/merge --ff-only commands pinned to H2. It opens with the live comment-formula disclosure and asks the contributor about the ofPairs compatibility alternative. Null-to-empty-string behavior is explicitly retained.
- Before posting: refreshed live PR, existing comment body/author/update time, fork head and inline discussion; OPEN at H0, two conversation comments, zero reviews and zero inline comments; no equivalent offer or incorporation existed.
- After posting: a separate GET compared the stored body exactly to the local draft; exact match. A separate PR/discussion read confirmed unchanged OPEN/H0, two conversation comments, zero reviews and zero inline comments. Snapshot saved locally.
- Local draft: `research/branch-reviews/2026-11-05-pr-batch/PR-5291-comment-update.md`. Read-back: `PR-5291-comment-readback.json` in that directory. Verifier: `verify-pr5291-comment.ps1`. Review record: `PR-5291-deeper-review.md`.

The Windows host ran no Gradle or tests. No PR body, review approval, merge, JIRA post, contributor branch or upstream code was changed. Await contributor review of the offered fixes.
