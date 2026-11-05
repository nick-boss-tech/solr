# SOLR-18615 / PR 5300 deeper review and packaged cache tests, round 1

Status: Implemented, Proof Pending

- Host: windows; review, implementation, records. Owner expressly requested deeper review, tests on a branch and Linux gate handoff.
- Claim: `claims/pr-5300-cache-tests-windows-round-1.md`, posted at `5378c640c5af8f8b7a829a7695572e7a54a76a54`.
- Public PR: https://github.com/apache/solr/pull/5300; live contributor head O remained `090c4dc298a521466a120d1f3c0f676398c36534`, open, at the pre-commit refresh.
- Fork branch: `nick-boss-tech/solr:pr-5300-solr-18615-cache-tests`.
- Pushed head H: `0bae701e819706680809b253e94b637faf2c330e`, ordinary commit directly on O, separately verified against the fork ref after push.
- Diff: https://github.com/nick-boss-tech/solr/compare/090c4dc298a521466a120d1f3c0f676398c36534...0bae701e819706680809b253e94b637faf2c330e
- Exact outbound file: `solr/packaging/test/test_adminconsole_urls.bats`, 73 insertions / 4 deletions. Canonical Git blob `125a62a8bb6eb005274c74715c6192e4eb8e89ff`.
- Requested gate: `gates/pr-5300-cache-tests-round-1.md`; required execution receipt: `receipts/PR-5300-cache-tests-round-1.md`.

## Deeper source assessment

No blocking production defect was established after revisiting the entire five-file PR. Traced `web.xml` static versus API servlet mappings, separate `LoadAdminUiServlet` version interpolation, both production/embedded resource servlet configurations, generated-client WAR inclusion, and unpacking of that WAR into the installed server distribution. The new cache policy is scoped to the existing static-resource servlet; API `<httpCaching>` remains a distinct contract. Jetty 12.1.14's ResourceService implements Last-Modified/If-Modified-Since validation and preserves the configured cache policy on 304. The page renderer has no Last-Modified validator and dynamically substitutes its version. A new header does not retroactively update older cached responses, supporting the query buster for the first upgrade. Actual browser cache upgrades and intermediary behavior were not executed or claimed.

Architectural fit: no new API, helper or abstraction; the two real servlet configurations remain aligned. Reuse: the existing packaging BATS suite exercises the shipped web.xml; the manually assembled Selenium Jetty is unsuitable for proving that file. Existing `CacheHeaderTest` and `NoCacheHeaderTest` cover a separate API caching contract and are gate neighbors. Prior board assertion-audit records inspected the older suite before this new cache test; they do not duplicate this follow-up. Security/compatibility: no new trust boundary, permissions, API format, dependency or production source change.

The original test has two false-positive shapes: header-only HEAD without successful-status proof, and an entirely negative HTML regex that can pass on an empty/error page. These are coverage findings, not a reproduced production defect. The ticket context was refreshed through Apache JIRA: SOLR-18615 is Open, updated November 5 at 21:34 UTC, with maintainer support for static no-cache and separate API caching.

## Implemented coverage

Split the original combined cache test into three named tests, producing five total tests with the existing launch/CSP checks:

1. Successful Solr startup, HTTP 200 and `Cache-Control: no-cache` on JS/CSS HEAD responses.
2. HTTP 200 with nonempty generated JS, real Last-Modified extraction, matching-validator HTTP 304 with no body and retained policy, then old-validator HTTP 200 whose full bytes equal the original response.
3. HTTP 200 actual HTML, positive nonempty versioned generated-client reference, no unresolved version placeholders or bare JS/CSS URLs, and successful retrieval of the extracted versioned client URL.

Tests use existing BATS assertions and per-test temporary files. Header parsing runs via BATS `run` and assertions; captured validated output is used for the subsequent conditional request. No sleeps, fixture class, external dependency, new production behavior, or changelog addition.

## Verification and handoff

The scoped diff and Git whitespace check passed. Author identity is Nick Shanin. The branch was committed and plain-pushed to Nick's fork in this session and the full remote SHA matched H. Contributor ancestry was retained. The public PR head did not move as a result of this fork-only push.

Windows executed no Gradle, BATS, browser, build or runtime mutation. The earlier seven successful upstream checks cover O only; they do not certify H. The gate requires original O suite passing (3 tests), expanded H suite passing (5 tests), API-cache neighbors, clean tidy and root check, plus isolated configuration/version mutations causing specifically named runtime assertion failures with H tests unchanged. Additional status/body/conditional assertions remain coverage pins unless separately discriminated; this report does not claim them mutation-proven.

A posted job is not a Linux execution claim or proof. Keep Implemented, Proof Pending until every required leg is green at H and the execution host posts its actual receipt with fresh logs/XML/TAP. Any source/test/formatting repair requires a new pushed head and superseding job. No public comment, JIRA write, public review submission or PR-body edit was made.
