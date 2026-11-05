# Report: encoded-path gate failure repaired and handed back

Status: Implemented, Proof Pending.

- Owner request: patch the failure and hand it back to the gate.
- Assignment `assignments/solr-12613-adjacent-implementation-round-1.md`; claim `claims/solr-18537-path-coverage-repair-windows-round-2.md`.
- Failed receipt `receipts/SOLR-18537-path-coverage-main-round-1.md` is retained. Its positive methods and old mutation stopped on Jetty 400 before the intended assertions, so neither the forwarding coverage nor mutation requirement was satisfied.

| Branch | Previous head | Pushed and independently read-back head | New gate |
| --- | --- | --- | --- |
| `solr-18537-path-coverage-followup` | `59ad950e1ac14d628e711e82746ee37c489cf151` | `0fad9847cc89973c751b1732aa22a259764610b2` | `gates/solr-18537-path-coverage-main-round-2.md` |
| `solr-12613-adjacent-integration` | `dabf21456845c0ae44b05b31e3893a5849dfc281` | `84743822082ed2c409d87e40a6f8142e2a240042` | `gates/solr-12613-scoped-integration-round-2.md` |

## Diagnosis and repair

Main pins Jetty 12.1.14. Its HttpURI contract marks an encoded percent as ambiguous, so the original success path `/a%20b%23c%25d%2523e` was invalid for the normal servlet endpoint independently of the proxy's correctness. The old mutation introduced the same rejected boundary. Evidence: https://github.com/jetty/jetty.project/blob/jetty-12.1.14/jetty-core/jetty-http/src/main/java/org/eclipse/jetty/http/HttpURI.java and the gate's actual failing receipt. The pre-existing accepted space/hash path passed in the original 14-method suite.

Only `solr/core/src/test/org/apache/solr/servlet/NodeTargetProxyFilterTest.java` changed on the path branch. The two successful forwarding cases now use `/a%20b%23c`, retain the query marker `space # % +`, foreign header and consumed directives, and check `_forwardedCount=1` before final raw URI equality. HttpSolrCall preserves the original collection name in its remote URL (origCorename assignment and getCoreUrl), so the full collection URI remains the expected assertion. No production code or URI-compliance configuration is changed.

New `testAmbiguousEncodedPathsRemainRejected` uses the existing raw Jetty client for `%25` and `%2523` paths under header and query targeting. All four requests must return 400 with the ambiguity reason. Percent-path rejection is explicit coverage rather than an impossible success expectation. Literal percent remains in the accepted query-value check.

The new isolated mutation is `new URI(null, null, path, null).getRawPath().replace("%23", "%24")` in HttpSolrCall.sendRemoteProxy. It corrupts the encoded hash to an encoded dollar while retaining a valid URI. Both new forwarding methods must reach the echo handler, pass forwarding-count validation and fail exactly on URI equality. An HTTP 400, setup/compile failure or timeout does not satisfy that proof. Other query/header/rejection assertions remain positive coverage pins unless independently discriminated.

## Combined candidate and evidence limits

The combined branch first merged the encoded-key head `a676e7a392244e7cf4a35719fc64383b65cd947f` at `d4165343f3260717fc30dc5830350e1aab1b3c50`, then the path repair at the final head. Both merges were conflict-free and ordinary. The existing green encoded-key branch is unchanged. Its actual round 2 receipt proves 19+6 focused and 2+9 neighboring tests, four named fail-before assertions, tidy and root check at that exact head. Its draft's broader wording that every new test fails before the fix should not be copied: only the four named methods have that negative proof.

The frontend-only baseline stays `4e8aa913fce4a40b529fe89d8b5dd7930c29a86e`; lifecycle stays `7f8d251d6c27200f3804f8b4e1298b9646f6cab4`; UI request migration stays `be369c41c633d639ad66a7291a4d82891ea9836c`. The combined final diff still has no solr/api changes against main `07ccc73cd24e26e59da169ca35b3fd5f64c35986`; the only main-relative solr/core files are the filter and its two test classes. Contributor ancestry is retained; the combined UI baseline remains synthetic and requires reconciliation with the author's actual revision before public submission.

Static inventories are 17 filter methods plus 4 auth on the independent path branch, and 22 filter plus 6 auth on the combined branch. The combined browser classes retain 3 native-contract, 8 picker, 11 node-screen, 6 logging-screen and 2 standalone logging methods. These are source counts, not executed results. The repair adds one rejection method; the original 23 authored methods across baseline/slices now total 24.

The path round 2 job requires fresh baseline/candidate suites, valid-path mutation proof, tidy and root check at the repaired head, with verified clone checkout and logs/XML. The combined round 2 job supersedes the stale combined input and requires actual matching receipts for all five inputs before execution. The UI jobs are still separate prerequisites; no green result is inferred from this source merge. A source/formatting repair requires a new head and superseding job.

Windows reviewed the file-scoped diff, upstream URI/forwarding code, Git ancestry, exact file scopes, whitespace, clean task trees, test inventories and fork heads. Windows ran no Gradle, compiler, browser or tests. No upstream/contributor push, force-push, public GitHub/Jira action, dependency/fixture addition or URI-policy relaxation occurred. The gate host owns its receipts and the next runtime verdict.
