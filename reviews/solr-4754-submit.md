# solr-4754-submit

- Branch: origin/solr-4754-submit
- Head: d2e9038881f6 (reviewed and unchanged; no patch)
- Base: upstream/main (merge-base cabedd1d968, 16 commits behind)
- Scope: 4 files, +42/-1. `solr/core/.../cloud/ZkController.java` (`normalizeHostName` becomes package-private static and throws SERVER_ERROR when the result is blank), `solr/core/src/test/.../cloud/ZkControllerTest.java` (one new unit test, no cluster), changelog fragment, `SOLR-4754-TESTING.md` (author's unrun note, left in place).
- Verdict: Ready for review (code read only; nothing compiled or run)
- Reviewer: review-agent A1, 2026-10-08

## Findings (ranked)

No defects found. The author's guesses were checked against source:

- verified: `normalizeHostName` has one call site, `ZkController.java:314`, in the constructor (`this.hostName = normalizeHostName(cloudConfig.getHost())`). The new throw therefore fails node startup with the clear error, as the ticket asked.
- verified: `URLUtil.URL_PREFIX` is `^([a-z]*?://).*` (`solrj/.../URLUtil.java:33`). `hasScheme("http://")` matches, and `removeScheme` returns "". The `http://` and `https://` cases in the test hit the new check.
- verified: `AddressUtils.getHostToAdvertise()` (`solr/core/.../util/AddressUtils.java:34`) returns an address string from `InetAddress.getHostAddress()` or the loopback address. It does not return blank in practice, so a normal start with no `host` set is unaffected (author's guess 2).
- verified: the test's imports and helpers resolve. `SolrException` is imported (line 46), and `assertFalse` and `expectThrows` are used elsewhere in the same file. The test is in the same package, so the package-private static method is accessible.
- verified (behavior change, intended): a whitespace-only host such as `"   "` now also throws. Before, it was registered as a host. This is consistent with the ticket's "fail loudly" request and is not covered by the test. LOW.
- LOW: the error text says to set `host` (`-Dhost=<name>`). That is correct for the `host` property, but the message does not name `solr.xml`. Style only.

## Not checked
- Nothing compiled, formatted, or run. No Gradle, no tests.
- Behavior on a node whose `getHostToAdvertise()` resolves to a non-IP string (not reachable by reading the method; not explored).
- Whether any other path (outside the constructor) registers a host without going through `normalizeHostName`. Grep found none in `solr/core/src/java`.
