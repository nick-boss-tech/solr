# SOLR-4754 - hypothetical reproduction (NOT RUN)

Guessed, never compiled or executed. No Gradle was run.

JIRA: a 4.3 RC registered nodes in ZooKeeper with an empty host (`base_url: http://:8983/solr`) and did not fail.
Mark Miller band-aided the empty-string parsing; the "fail loudly as a last resort" half stayed open.

Change: `ZkController.normalizeHostName` is now package-private static and throws SERVER_ERROR when the result is blank
(e.g. `host=http://`, scheme with nothing after it). Null/empty input still falls back to `AddressUtils.getHostToAdvertise()`.

Test: `ZkControllerTest.testNormalizeHostName`.

Guesses to verify first:
- `URLUtil.hasScheme("http://")` matches the `[a-z]*?://` pattern and `removeScheme` yields "" (read from source, not run).
- `getHostToAdvertise()` never returns blank (it falls back to the loopback address).
- Whether failing the constructor is better than a WARN; the ticket asked for a startup error.
