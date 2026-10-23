# PR draft: SOLR-17840 (for Nick to approve; not opened)

Status: DRAFT. Not opened; opening needs Nick's approval. Branch `solr-17840-submit` at `6829ee47a3d699bfcb07c981189acc18172e725e`; base `apache/solr` `main` at `5b2ac4dea21a7c86e31e2ba0cea58c262dc558c8`. Gate: GREEN.

Title: SOLR-17840: Preserve LTR results and honor partialResults

🤖 *AI text below* 🤖 *(posted on behalf of Nick Shanin)*

https://issues.apache.org/jira/browse/SOLR-17840

## What happens today

**An LTR timeout drops four selected hits.** At merge-base `5b2ac4dea21a7c86e31e2ba0cea58c262dc558c8`, removing the test's `@Ignore("SOLR-17840")` in a disposable worktree reveals `mismatch: '4'!='0' @ response/numFound` for `timeAllowed=800`. Base [QueryComponent](https://github.com/nick-boss-tech/solr/blob/5b2ac4dea21a7c86e31e2ba0cea58c262dc558c8/solr/core/src/java/org/apache/solr/handler/component/QueryComponent.java#L1861-L1869) returns before publication; the regression expects four hits.

## What this change does

**Allowed partial hits survive; disallowed ones are omitted.** [QueryComponent](https://github.com/nick-boss-tech/solr/blob/6829ee47a3d699bfcb07c981189acc18172e725e/solr/core/src/java/org/apache/solr/handler/component/QueryComponent.java#L1861-L1869) checks limits before publication. With `partialResults=false`, the response is empty and marked `omitted`. If allowed, only an already-selected document fetch may finish after a limit trips; checks resume before transformers/subqueries in [DocsStreamer](https://github.com/nick-boss-tech/solr/blob/6829ee47a3d699bfcb07c981189acc18172e725e/solr/core/src/java/org/apache/solr/response/DocsStreamer.java#L121-L137) and [QueryLimits](https://github.com/nick-boss-tech/solr/blob/6829ee47a3d699bfcb07c981189acc18172e725e/solr/core/src/java/org/apache/solr/search/QueryLimits.java#L103-L123).

## Proof

**Both failures are reproduced; focused tests pass.** Verified 2026-10-23 at `6829ee47a3d699bfcb07c981189acc18172e725e`, seed `1BB02D3762B56611`.

- [TestLTRQParserPlugin](https://github.com/nick-boss-tech/solr/blob/6829ee47a3d699bfcb07c981189acc18172e725e/solr/modules/ltr/src/test/org/apache/solr/ltr/TestLTRQParserPlugin.java#L144-L176): base, ignore removed, 1/1 failed `4` vs `0`; head 15/15.
- [TestQueryComponentPartialResults](https://github.com/nick-boss-tech/solr/blob/6829ee47a3d699bfcb07c981189acc18172e725e/solr/core/src/test/org/apache/solr/handler/component/TestQueryComponentPartialResults.java#L67-L155): prior head failed `Disallowed partial results must not be published`; head 3/3.
- [TestPartialResultsFetch](https://github.com/nick-boss-tech/solr/blob/6829ee47a3d699bfcb07c981189acc18172e725e/solr/core/src/test/org/apache/solr/response/TestPartialResultsFetch.java#L47-L242): 7/7.
- [TestQueryLimits](https://github.com/nick-boss-tech/solr/blob/6829ee47a3d699bfcb07c981189acc18172e725e/solr/core/src/test/org/apache/solr/search/TestQueryLimits.java#L114-L160): 2/2.
- [ComponentStageLimitsTest](https://github.com/nick-boss-tech/solr/blob/6829ee47a3d699bfcb07c981189acc18172e725e/solr/core/src/test/org/apache/solr/search/ComponentStageLimitsTest.java#L95-L215): 4/4.

Total 31 passed, none skipped. Tidy, Error Prone compilation, and `:solr:core:check :solr:modules:ltr:check -x test` passed.

## Limits

**Full core and LTR suites were not run.** The new test uses a mocked searcher in the local component loop, not HTTP or distributed requests. Grouped/distributed handling and transformers are outside this change.

Changelog: [SOLR-17840-preserve-partial-results.yml](https://github.com/nick-boss-tech/solr/blob/6829ee47a3d699bfcb07c981189acc18172e725e/changelog/unreleased/SOLR-17840-preserve-partial-results.yml)

*AI-generated description. Nick Shanin directed the work and takes responsibility for this contribution.*
