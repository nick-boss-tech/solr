# Pool assignment: SOLR-18551 focused IndexFetcher mmap test rerun

Status: CLAIMED (claims/pool-solr-18551-mmap-test-rerun.md)

- Capability tags: settling-run.
- Intended host: vm2 (Linux). Claim under `claims/` before starting (WORKFLOW.md).
- Posted by: Windows research lane, 2026-10-17, at the direction of Nick Shanin.

## Items

1. **Rerun the focused regression test and fail-before proof after the test compile fix.** Use Nick's fork branch `solr-18551-submit` at commit `02c82ea6c8858495c4b1b2286167b77628d89485`. This adds the regression tests to contributor PR #5173 head `d05eabeee25a10f1dda6a5c9ae0882757f6050de` and renames the test helper to avoid the inherited `LuceneTestCase` method collision reported in the previous run (`reports/SOLR-18551-indexfetcher-mmap-test-run.md`). Run `./gradlew --no-daemon --console=plain -Ptests.workDir=/tmp/18551-rerun-tests -Ptests.seed=18551C0FFEE18551 -Dtests.seed=18551C0FFEE18551 :solr:core:cleanTest :solr:core:test --tests org.apache.solr.handler.IndexFetcherTest`. If all 3 tests pass, create a clean scratch checkout at pre-fix base commit `714d6ceff852e6dc743ef51ae094e4eaf8f3b3f9`, overlay only `solr/core/src/test/org/apache/solr/handler/IndexFetcherTest.java` from the assigned branch, and run the same focused command and seed. The fail-before run should fail at the `openInputCalls` assertion because the pre-fix implementation opens the file. Start from: `nick-boss-tech/solr`, branch `solr-18551-submit`, commit `02c82ea6c8858495c4b1b2286167b77628d89485`. Output: `reports/SOLR-18551-indexfetcher-mmap-rerun.md`.

## Decisions

| Decision | Answer or default | Condition and action | Owner |
| --- | --- | --- | --- |
| Head test result | decided: require 3 of 3 tests to pass | if compilation fails, any test fails, or any test is skipped, record the output and stop; otherwise continue to fail-before | Nick Shanin |
| Fail-before result | decided: require a test failure at the `openInputCalls` assertion | if the test passes, does not compile, or fails for another reason, record the output and stop; otherwise record the proof as established | Nick Shanin |
| Source changes | decided: none | if the run exposes a test or code issue, report it without editing or pushing source | vm2 claimant |

## Deliverable

`reports/SOLR-18551-indexfetcher-mmap-rerun.md`: record the tested branch and exact commit, test command and seed, head test counts and result, fail-before base and result, the failure assertion or failure classification, and links or paths to relevant logs. Include the report commit and head in the assignment closeout.

## Limits

- No PR, comment, JIRA or email. No force push.
- Do not edit or push source. Do not modify contributor PR #5173.
- Stop after recording a failed head run or an inconclusive fail-before run. Do not relaunch or broaden the run without a new assignment.
- Commit the report as Nick Shanin with no trailers. No em dashes.
