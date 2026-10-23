# Gate job: SOLR-17840 (submit branch revalidation)

Status: DONE (GREEN at `6829ee47a3d699bfcb07c981189acc18172e725e`)

- Branch: `nick-boss-tech/solr:solr-17840-submit`. Verified fork tip on 2026-10-23: `6829ee47a3d699bfcb07c981189acc18172e725e`.
- Prior head: `8e172c9a2dd86de34fbcedbc9f22fec9970d285b`. Merge-base with `upstream/main`: `5b2ac4dea21a7c86e31e2ba0cea58c262dc558c8`.
- Intended host: phone-agent-2. Capability tags: implementation, review, gate, premise-run.
- Modules: `:solr:core` and `:solr:modules:ltr`. Focused classes: `TestQueryComponentPartialResults`, `TestPartialResultsFetch`, `TestQueryLimits`, `ComponentStageLimitsTest`, and `TestLTRQParserPlugin`. Seed: `1BB02D3762B56611`, one test JVM.
- Diff scope for the final fix: `solr/core/src/java/org/apache/solr/handler/component/QueryComponent.java` and new `solr/core/src/test/org/apache/solr/handler/component/TestQueryComponentPartialResults.java`. The submission branch also contains the existing QueryLimits/DocsStreamer fetch-scope patch, unchanged LTR regression, and changelog from the two earlier SOLR-17840 commits.

Links: SOLR-17840. Category: Search and query components, secondary Test quality. Siblings: SOLR-17631, SOLR-18539, SOLR-18541. Supersedes the prior handback validation for the `solr-17840-submit` branch; the historical candidate receipts remain unchanged.

## Why this gate was needed

The new response-exposure fix could retain already-selected documents when `partialResults=false`. The final patch checks the limit before publishing the result context. The test distinguishes the behavior with a searcher that returns one selected hit, then trips the injected limit. The original LTR timeout regression remains unchanged.

## What ran

0. Changelog YAML parsed as a mapping.
1. Root `tidy` and committed-head `tidy` both returned 0; committed source stayed unchanged.
2. Core and LTR production/test compilation with `-Pvalidation.errorprone=true` returned 0.
3. Proof: kept the new regression and restored only `QueryComponent.java` to prior head `8e172c9a2dd86de34fbcedbc9f22fec9970d285b`. Fresh XML: 1 executed test, 1 intended failure, 0 skipped/errors, exact assertion `Disallowed partial results must not be published`. Restored final source before head tests.
4. Fresh head XML: `TestQueryComponentPartialResults` 3/3, `TestPartialResultsFetch` 7/7, `TestQueryLimits` 2/2, `ComponentStageLimitsTest` 4/4, and `TestLTRQParserPlugin` 15/15. Total: 31 passed, 0 skipped, 0 failures, 0 errors.
5. `:solr:core:check :solr:modules:ltr:check -x test` returned 0. The full core and LTR test suites were not run.

Additional exact-base proof for the PR description: on Apache main `5b2ac4dea21a7c86e31e2ba0cea58c262dc558c8`, in a disposable worktree with only the `@Ignore("SOLR-17840")` annotation removed, the unchanged LTR timeout test failed with `mismatch: '4'!='0' @ response/numFound`; 1 test, 0 skipped, 1 failure, 0 errors. The temporary annotation change was not part of the branch.

Logs and fresh XML: `/workspace/phone-agent-2-logs/gate-17840-submit-omitted/`. Additional current-base proof: `/workspace/phone-agent-2-logs/gate-17840-base-proof-20261023/`. The source worktree is clean at the pushed head. No gate repair or retry was needed.

## Decisions

| Decision | Answer | Owner |
| --- | --- | --- |
| Base for the submission and gate | Apache upstream `main` at `5b2ac4dea21a7c86e31e2ba0cea58c262dc558c8`, selected by Nick under rung 5 of `material/decision-ladder.md` | Nick |

No PR, GitHub/Jira comment, or ready-state change was made. No force push was used.
