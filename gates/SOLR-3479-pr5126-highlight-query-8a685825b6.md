# Gate job: SOLR-3479 PR 5126 highlight-query fix, exact repair head 8a685825b6

- Supersedes: `gates/SOLR-3479-pr5126-highlight-query-23c159d06bd8.md` for the head it names. That job's first fix head `23c159d06bd86180e0bbd4b474517b60b693f926` failed Error Prone compile (`ReferenceEquality`, `-Werror`); see `reports/pr-5126-highlight-query-fix-repair-round-1.md`.
- Ticket/PR: SOLR-3479, https://github.com/apache/solr/pull/5126. Original PR head: `74b09fb443f060caac3f8ec9701909cec276cfb4`.
- Fix branch: `nick-boss-tech/solr:solr-3479-submit`. Exact head to gate: `8a685825b6202f45eb981d3b8e7f05b01a5c1829`.
- Module: `:solr:core`. Focused class `org.apache.solr.response.transform.TestHighlightAugmenterFactory`; sensitivity method `testImplicitQueryUsesHighlightQuery`; neighbor `org.apache.solr.highlight.TestUnifiedSolrHighlighter`. Seed `34795126` on every test leg.
- Legs, as in the superseded job: fail-before (original production, fix test file overlaid, target method), pass-after (full class, clean fix head), neighbor (TestUnifiedSolrHighlighter), tidy, compile with `-Pvalidation.errorprone=true`, module check `:solr:core:check -x test`.
- Leg 1 (fail-before) is unchanged in this job: its result stands from the superseded job, since the fail tree and test file are the same on both heads.
- Results for this head: `reports/pr-5126-highlight-query-fix-gate-round-1.md`. Completion is derived from `receipts/SOLR-3479-pr5126-highlight-query-round-1.md`.
- Host: vm4 (Linux). Runner `pool/bin/sweep-run.sh` under `/workspace/tools/.gradle-vm4.lock`. Claim: `claims/pr-5126-highlight-query-gate-vm4-round-1.md`.
