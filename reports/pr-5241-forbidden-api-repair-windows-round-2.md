# PR 5241 forbidden servlet writer repair, Windows round 2

Status: IMPLEMENTED, PROOF PENDING.

- Repair claim: `claims/pr-5241-forbidden-api-repair-windows-round-2.md`, posted at board commit `135d92104c3ab59c1320491c13eef4fca8f411ac`.
- Failed candidate / repair parent: `80ca9374f48f292af159c7e9600019b5947adc49`.
- Repaired fork head: `56365b43ee72977d1934b5a5c833e1071f75c557`, plain-pushed to `nick-boss-tech/solr:solr-16398-submit`; separate GitHub ref API read matches this exact SHA.
- Original PR production / fail-before base remains `aa8272ef089c39df449c519551ef439557b876f8`; the public PR head was refreshed and is unchanged with no comments/reviews.
- New gate job: `gates/pr-5241-overview-errors-round-2.md`, superseding the round-1 job for final proof. Gate ownership remains with vm4 unless handed off explicitly.

## Repair and cause

The round-1 gate found a real failure in our new fixture: `:solr:webapp:forbiddenApisTest` rejects ServletResponse.getWriter() at test line 169. The repository signature file explicitly forbids it because of servlet implementation problems. The implementation missed that restriction.

The repair changes only `solr/webapp/src/test/org/apache/solr/webapp/AdminUiCoreOverviewErrorsTest.java`: import StandardCharsets, then write the same JSON through getOutputStream() as UTF-8 bytes. Four additions, one deletion. No suppression, signature-file edit, production change, assertion change, or test scope reduction. Author is Nick Shanin with no trailers. The parent-to-repair `git diff --check` passed. No Gradle, compile, formatter, browser test or local queue ran on Windows.

## Actual round-1 result

The gate-host receipt `receipts/PR-5241-overview-errors.md` was read directly from coordination-ui after Nick's update. It records NOT GREEN: tidy, whitespace and Node syntax passed at the old head; the root check failed at forbiddenApisTest; all focused, neighbor and fail-before browser runs failed at class setup, with zero test methods executed. There is no positive browser proof or fail-before evidence to carry forward.

The receipt records three setup attempts, including the daemon-stop retry, and the observed Selenium Manager refusal on Linux ARM. The stale-PATH hypothesis did not resolve startup. Round 2 authorizes a specific environment repair: the downloaded matching ARM64 chromedriver's absolute path as webdriver.chrome.driver in the forked test JVM through JAVA_TOOL_OPTIONS, with a fresh Gradle process and the existing explicit browser path. The job includes the Selenium source references, requires host validation of the pinned dependency/property, and allows one setup attempt with that concrete change, not repeated unchanged retries. This is host setup, not an additional source patch.

## Handoff

The new gate must rerun tidy, the targeted forbidden-API check, all three positive regressions, five neighbor browser classes, full root check, Node syntax, whitespace/clean-tree checks, and fail-before with the repaired test file against the unchanged original PR production. Preserve round-1 evidence and append round-2 results under the gate host's ownership. Old-head successes do not prove the repaired head.

The GitHub wrappers had diverged after credential rotation: current gh.ps1 reads the workspace-root credential, while older push/board wrappers still fell back to a retired location. A local task helper loads the current gh.ps1 credential source into process memory and calls the existing scoped wrappers; no token was printed or committed. The failed board attempt made no write; the retried claim and fork push were separately verified. No shared wrapper, source authentication behavior, or credential file was edited.

No public comment, PR/JIRA write, or contributor-branch action was made. Status stays Implemented, Proof Pending until the repaired-head receipt is green.
