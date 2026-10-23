# SOLR-17840 submission handback

Status: DONE for implementation, exact-head validation, and the authorized product-branch push. Coordination records and the draft PR description are for the coordinator to post. No PR, GitHub/Jira comment, or ready-state change was made.

## Result

Branch `nick-boss-tech/solr:solr-17840-submit` is verified at `6829ee47a3d699bfcb07c981189acc18172e725e`. The only new commit is `SOLR-17840: Omit selected documents when partial results are disabled`, authored and committed as Nick Shanin <nick.boss.us@gmail.com>, without trailers.

The push was fast-forward only, from `8e172c9a2dd86de34fbcedbc9f22fec9970d285b` to the validated head. No other branch was pushed. Apache upstream base remains `5b2ac4dea21a7c86e31e2ba0cea58c262dc558c8`; the two existing SOLR-17840 commits remain intact. The product worktree `/workspace/solr-17840-submit` is clean.

## Narrow change

`QueryComponent.doProcessUngroupedSearch` now checks post-search query limits before publishing `BasicResultContext`. It saves the check's return value, publishes selected documents only if that check permits it, then retains the existing early return for allowed partial results. `rb.setResult(result)` stays in its original position. With `partialResults=false`, the exception reaches the existing local SearchHandler short-circuit path before a response context exists, so the response is empty and marked `omitted` instead of exposing selected documents.

New `TestQueryComponentPartialResults extends SolrTestCase` exercises the real local SearchHandler component loop with a mocked searcher. The injected limit remains false until search returns a nonempty result. Three deterministic cases assert omission for `partialResults=false` and preservation for explicit `true` and the default. No wall-clock sleep or production test seam was added.

The existing LTR regression is unchanged from the starting candidate, including count, order, scores, timing, and fixtures. Its Git blob at both starting and final heads is `0f678c2e3c6fbd648b3408efd7fafca2e36d21d8`.

## Validation on the exact pushed head

Seed `1BB02D3762B56611`, one test JVM, at head `6829ee47a3d699bfcb07c981189acc18172e725e`. The shared host build lock and disk/inode guard were used. All evidence is from actual run logs and fresh archived JUnit XML, not summaries alone.

| Check | Result |
| --- | --- |
| Existing changelog YAML mapping | Pass |
| Precommit tidy, then committed-head tidy | Both rc=0; committed source unchanged |
| Core and LTR production/test compile with `-Pvalidation.errorprone=true` | rc=0 |
| Discriminating proof | New test retained; only QueryComponent restored to starting candidate `8e172c9a2dd86de34fbcedbc9f22fec9970d285b`. One executed test, one failure, no skips/errors: `Disallowed partial results must not be published`. Final production source restored before head tests. |
| TestQueryComponentPartialResults | 3 passed |
| TestPartialResultsFetch | 7 passed |
| TestQueryLimits | 2 passed |
| ComponentStageLimitsTest | 4 passed |
| Full TestLTRQParserPlugin class | 15 passed |
| `:solr:core:check :solr:modules:ltr:check -x test` | rc=0 |

Head focused total: 31 tests, zero skipped, zero failures, zero errors. The original LTR timeout method is included in its full 15-test class. No gate repair or retry was needed. Validation ran 2026-10-23 02:17:09Z through 02:28:10Z.

## Evidence paths

Root: `/workspace/phone-agent-2-logs/gate-17840-submit-omitted/`

- `state.json`: exact head, commands, return codes, counts, and UTC step timestamps.
- `1-tidy.log`, `2-errorprone.log`, `3-proof.log`, `4-head.log`, `4-extra-1.log`, `5-module-check.log`: complete build output.
- `3-proof-xml/test/TEST-org.apache.solr.handler.component.TestQueryComponentPartialResults.xml`: intended failing-before proof.
- `4-head-xml/test/TEST-*.xml`: fresh core focused results for all four selected classes, with response output under `test/outputs/`.
- `4-extra-1-xml/test/TEST-org.apache.solr.ltr.TestLTRQParserPlugin.xml`: fresh full LTR focused class results.
- `proof.patch`: exact temporary production-only reverse patch used for the proof.
- `push.log`, `remote-head.txt`: successful fast-forward push and remote ref readback.

Runner: `/workspace/tools/phone-agent-2-hardening-gate-v2.py`.
Configuration: `/workspace/phone-agent-2-logs/gate-17840-submit-omitted.json`.
Proof helper: `/workspace/phone-agent-2-logs/proof-17840-submit-omitted.py`.
Precommit formatting log: `/workspace/phone-agent-2-logs/17840-submit-omitted-precommit-tidy.log`.

## For the draft PR description

The formula-compliant draft is posted at `pr-drafts/SOLR-17840/PR-draft.md`. It states that the LTR timeout now preserves already-selected hits when partials are allowed, but returns no documents with `partialResults=false`. It cites the exact-base LTR failure and the failed-before regression proof, then reports the 31 focused passes and scoped module checks.

No concrete blocking concern was found in independent implementation review. Limits: this new test is a mocked-searcher component-loop regression, not a new HTTP or distributed integration test. The full core and LTR test suites were not run; the selected classes and the two module checks above were run. Existing grouped/distributed query behavior and limits first reached during document fetch were not broadened by this narrow follow-up.

## Additional exact-base proof for the PR draft

The unchanged LTR timeout regression was also run against Apache main `5b2ac4dea21a7c86e31e2ba0cea58c262dc558c8`. In a disposable worktree, only its `@Ignore("SOLR-17840")` annotation was removed. With seed `1BB02D3762B56611`, the single test failed with `mismatch: '4'!='0' @ response/numFound`: 1 executed, 0 skipped, 1 failure, 0 errors. The temporary annotation change was not part of the submit branch. Logs and fresh JUnit XML are in `/workspace/phone-agent-2-logs/gate-17840-base-proof-20261023/`.
