# SOLR-9039 - hypothetical reproduction (nothing was compiled or run, and nothing here can run on this Windows box)

JIRA (Hoss, Kevin Risden): SSL with client authentication was found not to work in tests on macOS (SOLR-3854 era), so
the test framework switched clientAuth off there. Kevin Risden wrote "I'm 90% sure this works on master on a Mac on
adoptopenjdk 11.0.5" and Hoss suggested forcing SSL+clientAuth on every test and beasting to see what shakes out.
On `upstream/main` two workarounds remain, both citing SOLR-9039:
- `SolrTestCaseJ4.buildSSLConfig` rebuilds the `SSLRandomizer` with clientAuth probability 0.0 when `Constants.MAC_OS_X`.
- `TestMiniSolrCloudClusterSSL.testSslAndClientAuth` has `assumeFalse(..., Constants.MAC_OS_X)`.
The first one carries a comment asking that the second be updated together with it.

## Change
Both workarounds are removed, plus the two now-unused `org.apache.lucene.util.Constants` imports.

## Test
None new: removing the suppression means the existing randomized SSL/clientAuth tests and
`TestMiniSolrCloudClusterSSL.testSslAndClientAuth` run on macOS. The evidence has to come from a Mac run
(Hoss's suggestion: temporarily force clientAuth in `SolrTestCaseJ4` and beast).

## Guesses to verify first
- The macOS failure is gone with current Jetty 12 / JDK 21 (unverified; only Kevin's 2019 comment says so).
- `SSLRandomizer.ssl` / `debug` fields stay used by other code (they are fields of the same class, so unused fields are fine).
- Spotless / import order after removing `Constants` from `SolrTestCaseJ4` (the class otherwise has no `Constants` use).

## Fail-before
Not applicable (a test-gating removal). If macOS CI still fails, the revert is these two hunks.
