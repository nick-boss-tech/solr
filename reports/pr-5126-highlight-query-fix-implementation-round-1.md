# PR 5126 highlight-query production fix, implementation round 1

Status: IMPLEMENTED, PROOF PENDING

- Authority: Nick requested a production fix, Linux proof and a plain-language comment following comment-formula.md, with a direct diff against David's PR.
- Implementation claim: `claims/pr-5126-highlight-query-fix-windows-round-1.md`, posted at board commit `ec7e70a6fc2f78c649f22f6ec119ec89bbe5b21a`.
- PR baseline: apache/solr#5126, `74b09fb443f060caac3f8ec9701909cec276cfb4`, still the live head when implementation began. State OPEN; three conversation comments, no submitted reviews.
- New separate fork branch: `solr-3479-submit`, worktree `wt/SOLR-3479`, local branch `wt-solr-3479-submit`.
- Fix commit: `23c159d06bd86180e0bbd4b474517b60b693f926`.
- Fork push completed normally; a separate GitHub ref read returned that exact full SHA. No public PR head was moved.
- Author: Nick Shanin, nick.boss.us@gmail.com; no attribution trailers.
- Parent: the exact PR baseline. The diagnostic branch `solr-3479-pr5126-query-examples-r1` at `022eb6d7811cd876afde3b84115696cb1c6af50e` is preserved separately.
- Incremental delta: two files, 103 additions, 1 deletion.

## Problem and implementation

The existing implicit transformer path used the full scoring query. vm4's measured Q evidence showed boost-only cats becoming a body snippet at hl.weightMatches=false, while explicit hl.q=fox and the existing highlighting component omitted it. Default true did not reproduce for the fixture. See `reports/pr-5126-query-examples-round-1-revision-1.md`; those green diagnostic tests assert the discrepancy and are not proof of this fix.

At the new head, HighlightAugmenterFactory.java:182 delegates implicit query selection to getMainHighlightQuery (line 190). The helper obtains the existing request's ResponseBuilder and QParser through SolrRequestInfo, checks the request and query identities against the ResultContext, then asks that parser for getHighlightQuery(). This mirrors the existing HighlightComponent's parser semantics without reparsing ordinary q or introducing an API on ResultContext. If request info, a matching builder, or a parser is unavailable, it returns the context query as before. Explicit request hl.q and transformer-local q retain the original override path.

Lifecycle checked against original source: SearchHandler.java:221-223 installs its ResponseBuilder on request info; QueryComponent supplies the parser/query; EmbeddedSolrServer.java:227-237 writes the response before clearing request info. ExportWriter.java:229-230 and 310-312 already uses that request-info builder and constructs its context from the same request and query. The identity checks avoid substituting an unrelated request/query for custom result contexts. This reasoning is source inspection, not a claim of distributed or export runtime proof.

## Exact outbound files

1. `solr/core/src/java/org/apache/solr/response/transform/HighlightAugmenterFactory.java`: +20/-1, parser selection and two imports.
2. `solr/core/src/test/org/apache/solr/response/transform/TestHighlightAugmenterFactory.java`: +83, three regression/control methods and small request/assertion helpers.

The original contributor's changelog stays unchanged; this is an incremental correction proposed for that still-open PR. No extra changelog fragment, research note, AGENTS edit, generated output or logging is included in the source branch. No dependency, endpoint, permission, filesystem or remote-fetch boundary changes. No new security finding.

## Tests and proof boundary

- testImplicitQueryUsesHighlightQuery (line 195): implicit, global hl.q=fox and local q=fox all exclude the boost-only body snippet; checks both weightMatches settings and component enabled/disabled, with a positive fox highlight and component comparison.
- testImplicitQueryUsesMainParser (line 213): q.alt with an explicit Lucene subquery, and a main q selecting eDisMax through local parameters, retain the original search parser's highlighting semantics.
- testExplicitHighlightQueryOverridesMainQuery (line 228): deliberately requested cats highlights still work, including a local q overriding global hl.q, at both weightMatches settings.
- Existing testRealTimeGet remains in the full class as a committed-document no-query fallback control. No support for uncommitted RTG is claimed.

Reviewed branch-work.ps1 diff and status: only those two files. git diff --check passed. Ran sync-report.ps1 as the workspace inventory check. Nothing was compiled, formatted with Gradle, or tested on Windows. Source was formatted by hand. Required Linux proof is separate and pending; the gate must first overlay these regression tests on the original PR production and record the expected cats assertion failure, then pass the full fixed class, neighboring Unified Highlighter tests, tidy, Error Prone compilation and module checks at the exact final pushed head.

Gate: `gates/SOLR-3479-pr5126-highlight-query-23c159d06bd8.md`.
Expected gate report: `reports/pr-5126-highlight-query-fix-gate-round-1.md`.
Expected phase receipt: `receipts/SOLR-3479-pr5126-highlight-query-round-1.md`.
Expected result-based comment: `reports/pr-5126-production-fix-reply-round-1.md`.

Direct branch comparison requested by Nick:
https://github.com/dsmiley/solr/compare/SOLR-3479-highlightDocTransformer...nick-boss-tech:solr-3479-submit

Pinned comparison for this exact proposed delta:
https://github.com/nick-boss-tech/solr/compare/74b09fb443f060caac3f8ec9701909cec276cfb4...23c159d06bd86180e0bbd4b474517b60b693f926

The gating host verifies the live branch comparison and the final head again before the reply draft. Formatting/source repairs require a new commit and fresh exact-head proof. No public PR/JIRA comment, original-comment edit, PR creation, or foreign-branch push is authorized. The reply must remain pending until the proof is complete and Nick reviews the actual draft.
