# PR 5235 compatibility tests: Windows handoff, round 1

Status: IMPLEMENTED, PROOF PENDING (test-only premise package)

- Request: Nick asked for test cases for the four review findings so a Linux gate can run them.
- Source PR: https://github.com/apache/solr/pull/5235, still at `73fa87f1f41b45862c165a9571176c89e2ef4def` when work began.
- Unchanged 10.x proof base: `f45b38d5f6c0c9f7f1204f6d518aaad18a4a88b7`.
- Fork branch: `nick-boss-tech/solr:solr-18351-submit`.
- Pushed and remote-verified test head: `de1ffbbb55add66391eed68e4153322d6a8cc6c0`.
- Its only parent is the reviewed PR head. The delta is three added Java test classes, 372 lines; no production changes or changelog.
- Claim: `claims/pr-5235-compatibility-tests-windows-round-1.md`.
- Gate: `gates/pr-5235-compatibility-premise-round-1.md`.

## Tests and predicted outcomes

All expectations below are predictions from code inspection, not executed results. The gate must compile the tests and compare the same test bytes at both production revisions before confirming any finding.

| Review finding | Suite and methods | Expected at unchanged 10.x base plus tests | Expected at reviewed PR head plus tests |
| --- | --- | --- | --- |
| F1, dropped unnamed embedded streams | `org.apache.solr.client.solrj.embedded.TestEmbeddedContentStreamCompatibility.testUnnamedContentStreamsRetainAllDocuments` | pass; both IDs indexed | fail; only one document found |
| F2, removed public methods | `org.apache.solr.common.util.TestCommandOperationCompatibility.testReadCommandsTwoArgumentSignature` and `testReadCommandsThreeArgumentSignature` | both pass and parse a command | both fail with missing-method lookup |
| F3, legacy request body fallback | `org.apache.solr.client.solrj.jetty.TestLegacyContentStreamCompatibility.testJettyLegacyRequestWithJavaBinWriter`, `testJettyLegacyRequestWithXmlWriter`, `testJdkLegacyRequestWithJavaBinWriter`, `testJdkLegacyRequestWithXmlWriter` | all four pass | all four fail the captured POST-body comparison |
| F4, custom writer hook precedence | Same HTTP suite: `testJettyJavaBinWriterStreamOverride`, `testJettyXmlWriterStreamOverride`, `testJdkJavaBinWriterStreamOverride`, `testJdkXmlWriterStreamOverride` | all four send `custom-writer-body` | all four send `original-request-body` instead |
| Positive controls | Embedded `testNamedContentStreamsRetainAllDocuments`; HTTP `testJettyContentWriterControl` and `testJdkContentWriterControl` | all three pass | all three pass |

There are 14 methods: 2 embedded, 2 signature, 10 HTTP. The predicted head split is 11 non-passing regression methods and 3 passing controls; the predicted base result is 14 passing methods, with no skips on either leg. The gate must report each actual method outcome, including whether the runner categorizes missing-method exceptions as failures or errors.

## Design and limits

The embedded suite uses EmbeddedSolrServerTestRule and asserts the committed index contents, including both document IDs. It tests unnamed strings and otherwise identical named strings independently.

The HTTP suite uses SolrJettyTestRule and the existing DebugServlet fixture, sends synchronous requests through both HttpJettySolrClient and HttpJdkSolrClient, and asserts the actual POST body bytes and content type. The servlet returns a valid XML response, so parser or network failures are not swallowed. Each request clears captured state, including the fixture's separately reset requestBody. One counter verifies the custom writer hook was called in the expected successful path. The server has no collection because the servlet handles these requests directly; it needs no collection/configset setup.

The command suite performs runtime reflection so it still compiles when the methods are missing. It checks the original public parameter signatures and List return type, invokes each static method, and verifies its parsed output. This demonstrates missing runtime API symbols; it is not a separately compiled old-client binary producing NoSuchMethodError. No dependency was added and none of the tests uses PR-only replacement APIs, allowing identical source files to run against the unchanged base.

Windows checks: reviewed the three-file diff, confirmed the parent and outbound file list, and passed `git diff --check`. No Java, Gradle, tidy, formatting task, or local queue was run. These tests are uncompiled and unrun. The Linux host may need to repair a compile/fixture/style issue, but must preserve each intended scenario and then run the identical repaired tests on both legs.

No fix is implemented. A comparative failure establishes a review premise; it does not prove that a fix is ready. No GitHub comment, review submission, PR/body edit, JIRA write, or update to the maintainer's PR branch was made.
