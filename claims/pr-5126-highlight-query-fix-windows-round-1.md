# Claim: PR 5126 highlight-query production fix, Windows, round 1

- Claimant: windows, Codex desktop review chat.
- Started: 2026-10-25T17:23:09Z.
- Capability tags: implementation, review, draft, records. No Windows build or test execution.
- Authority: Nick explicitly requested a production fix, Linux gating, and a plain-language comment following comment-formula.md. The diff between David's PR and Nick's fix is explicitly requested, overriding the formula's default against unsolicited diff links. No public post is authorized.
- Board snapshot reviewed: `a8944a918b2e9fa8a382679f696aa1ab856458e9`.
- Baseline PR: apache/solr#5126, head `74b09fb443f060caac3f8ec9701909cec276cfb4`.
- Completed Q evidence: `reports/pr-5126-query-examples-round-1-revision-1.md`; diagnostic branch `solr-3479-pr5126-query-examples-r1` remains unchanged.
- Scope: create separate fork branch `solr-3479-submit` directly from the PR head; resolve the implicit highlight query using the search request's existing QParser; preserve explicit overrides and fallback contexts; add focused regression tests. Existing PR changelog stays unchanged. No RTG implementation or /export alias fix.
- Expected outbound files: HighlightAugmenterFactory.java and TestHighlightAugmenterFactory.java under solr/core.
- Decision ladder: rung 1 establishes the query-selection mismatch against HighlightComponent; rung 2 already supplies measured Q proof at weightMatches=false. Implement parser reuse rather than reparse q so alternate queries and local parser selection retain the original request parser.
- Deliverables completing this implementation claim: verified pushed fix SHA, `reports/pr-5126-highlight-query-fix-implementation-round-1.md`, and a phase-specific exact-head Linux gate under `gates/`. Build proof and result-based comment draft belong to the Linux gate claim and cannot be claimed green here.
- Separate Linux gate requires the final regression tests over the original production source to fail for the expected cats assertion, the unmodified final fix head to pass, and named tidy/Error Prone/module checks. The final reply must state the default-setting caveat and link the direct PR-to-fix diff.
