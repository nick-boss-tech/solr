# SOLR-2681 nested-function highlighting receipt (separate branch, not #5104)

Status: GATE PASSED on the branch head (vm4). Draft PR and ticket are in pr-drafts/highlighting-nested/ and not filed or opened.

- Branch: solr-2681-nested-submit on https://github.com/nick-boss-tech/solr. Head: cec42ef4f061ff7b81a4610a91ee76aa83b806ff. Base: apache/solr main c8ab71430f.
- Changes: solr/core highlighter (DefaultSolrHighlighter) and HighlighterTest, plus a changelog fragment. Separate from #5104 (head a3b1ea7994).
- Environment: vm4, Linux, JDK 21.0.12.1, Gradle 9.7.0 from the branch wrapper, `NODE_ENV` unset. Seed 2681C0FFEE2681.

## Gate counts at the head

- Tidy: rc=0, zero changed paths. Log: /workspace/gates/logs/vm4/SOLR-2681-nested-tidy.log, SOLR-2681-nested-tidy-status.log.
- Error Prone compile (`-Pvalidation.errorprone=true`, :solr:core main and test): rc=0. Log: SOLR-2681-nested-errorprone.log.
- Focused HighlighterTest: tests=36, failures=0, errors=0, skipped=0. Log: SOLR-2681-nested-focused.log.
- `:solr:core:check -x test`: rc=0. Log: SOLR-2681-nested-check.log.

## Fail-before leg

- Upstream main highlighter (c8ab71430f) with the new test: testHighlightQueryInsideNestedFunctions fails, `REQUEST FAILED`, xpath expecting `<em>lorem</em> ipsum`, on the product assertion. Log: SOLR-2681-nested-fail-before.log.
- The first failing assertion stops the test, so the sub and sum-in-product assertions were not run on the base.
