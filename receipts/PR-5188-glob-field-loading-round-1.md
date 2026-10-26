# PR 5188 glob RTG field loading and lazy prefetch: receipt, round 1 (vm6)

- Gate: `gates/pr-5188-glob-field-loading-round-1-superseding-51e92b6.md`. Claim: `claims/pr-5188-glob-field-loading-round-1-vm6.md`. Repair rounds: `reports/pr-5188-glob-field-loading-repair-round-{1..4}.md` (round 1 was the tidy repair; round 4 is the final).
- Host: vm6, Linux aarch64, Temurin JDK 21.0.12.1+1, Gradle wrapper from the branch, build lock `/workspace/tools/.gradle-vm6.lock`. Seed `5188C0DE` (test printed the seed as `[5188C0DE:9B63294A879A3AA9]`).
- Final head F4: `51e92b63fddb97db83a3962f758bde3c79f1c4dd` (fork `nick-boss-tech/solr:pr-5188-glob-field-loading-followup`, read back after push). Tree `4de16ea713`. Author Nick Shanin, no trailers.
- Candidate head before repairs: `b71a1687bf98cf7b4b3edf635bfb89f25e4af8e2`. Repair commits: `b428596` (tidy), `53ad2cc` (test xpaths), `7ac2bc8` (whitespace-normalized checks), `51e92b6` (round-4 decisions). Diffstat candidate to F4: 3 files, 33 insertions, 26 deletions. Production files are unchanged from the candidate.
- Pre-fix tree: `e07455a0f953e702da27f677c51ea894b7bafc3a` production (verified identical) plus F4's two test blobs `BasicFunctionalityTest.java` and `TestPseudoReturnFields.java`.
- Logs: `/workspace/gates/logs/vm6/pr5188-round-1-superseding-4/` (driver, `status.txt`, one log and one `*-xml/` directory per leg).

## Legs (UTC start-end, exit)

| Leg | Result | Notes |
| --- | --- | --- |
| tidy at F4 | 0 (17:31:31 to 17:33:44) | tree clean afterwards (`tidy-tree: 0 changed`) |
| pre-fix RTG, P | 1 (17:33:46 to 17:35:57) | `testGlobRTGStoredFieldLoading` fails: `unrelated_s should remain lazy` (the predicted loading assertion) |
| pre-fix prefetch, P | 1 (17:35:57 to 17:38:09) | `testGlobHighlightPrefetch` fails: `original: source_* did not prefetch source_s` (the predicted prefetch assertion) |
| post-fix focused, F4 | 0 (17:38:09 to 17:41:08) | `BasicFunctionalityTest` 2 tests, 0 failures, 0 errors, 0 skipped; `TestPseudoReturnFields.testGlobRTGUncommittedFields` passes |
| post-fix suites, F4 | 0 (17:41:08 to 17:43:44) | `BasicFunctionalityTest` 25/0/0/0; `TestRetrieveFieldsOptimizer` 1/0/0/0; `ReturnFieldsTest` 16/0/0/0; `TestPseudoReturnFields` 35/0/0/0 (tests/failures/errors/skipped) |
| compile with Error Prone, F4 | 0 (17:43:44 to 17:50:18) | `:solr:core:compileJava :solr:core:compileTestJava -Pvalidation.errorprone=true` |
| check, F4 | 0 (17:50:18 to 17:57:27) | `:solr:core:check -x test` |

## Assertion evidence

- Pre-fix RTG: `AssertionError: unrelated_s should remain lazy` (the loaded-field case; response-shape checks pass first, after the xpath fix in repair round 2/3).
- Pre-fix prefetch: `AssertionError: original: source_* did not prefetch source_s`. This is the glob-path prefetch failure.
- Post-fix: every RTG case (cold and warm cache, glob and explicit, transformer input, fl=*, pseudo-only controls), the prefetch case for `original` (eager `id`, `title`, `source_s`; lazy `unrelated_s`; highlight snippet `<em>keyword</em>`), and the uncommitted method (tlog and fq-forced searcher) pass.

## Limits (state these in the PR body)

1. **Unified snippet not proven.** `hl.method=unified` returns no snippet for this document on base `e07455a` and on head F4, for every `fl` selection (probe `/workspace/gates/logs/vm6/pr5188-round-1-probe-unified/`, rung 2 of the decision ladder). The final test therefore checks the unified eager fields and lazy state, but not unified snippet output. The gate's original criterion asked for unified snippets; **this is a narrowing that the owner must accept or reject.**
2. Round 4 changed the expected response form from `str` to a name-and-value match, and removed commits in the uncommitted test. Both are documented in `reports/pr-5188-glob-field-loading-repair-round-4.md`.
3. Full `:solr:core:test` is outside this gate. Other modules are not covered.
4. The `java.security.policy` warnings from the security-manager run are not investigated.
5. Upstream PR 5188 head was not re-read in this round.

## Proposed PR-description summary (draft, not posted)

RTG and lazy prefetch now honor glob field selection. RTG loads only the stored fields its response and transformers need; lazy prefetch resolves glob matches before adding highlight fields and the unique key. Two regression methods fail on the base and pass on the head (`testGlobRTGStoredFieldLoading`, `testGlobHighlightPrefetch`), and `TestPseudoReturnFields.testGlobRTGUncommittedFields` pins the uncommitted and realtime-searcher behavior. Unified highlighting returns no snippets in this harness, on base and head; the PR does not claim unified snippet output. AI-generated description. Nick Shanin directed the work and takes responsibility for this contribution.
