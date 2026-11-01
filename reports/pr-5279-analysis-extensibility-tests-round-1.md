# Analysis handler extensibility tests and analyzer ownership

Status: Implemented, Proof Pending.

- Owner request: implement the two improvements from the PR 5279 review and hand the patch to a Linux gate agent.
- Source PR: https://github.com/apache/solr/pull/5279.
- Contributor head and patch parent: fd033eb55a4eb7c4a5d8c626ad099dd8137098fb, unchanged on the live PR read.
- Fork branch: nick-boss-tech/solr:pr-5279-analysis-extensibility-tests.
- Implementation head: 49ce5c0c10acb02cdfd828f8ad0f9588aa9d832e.
- Implementation claim: claims/pr-5279-analysis-extensibility-tests-windows-round-1.md.
- Gate job: gates/pr-5279-analysis-extensibility-tests-round-1.md.
- Expected receipt: receipts/PR-5279-analysis-extensibility-tests.md.

## Changes

Four new tests exercise the external subclass contract in the existing FieldAnalysisRequestHandlerTest and DocumentAnalysisRequestHandlerTest suites. Each handler uses an injected lowercase whitespace analyzer for index values and an injected lowercase keyword analyzer for query values. The input ONE TWO produces two index tokens; a query ONE TWO remains one token and matches neither index token, while a query ONE marks only the first index token as a match. Assertions check text, offsets, positions and positive and absent match markers. This distinguishes index routing, query output routing and match-term routing.

CustomAnalysisHandlers puts both subclasses in org.apache.solr.handler.custom, outside the production handler package. Its field subclass overrides protected analyzeValues, records the nullable field name and delegates to super. Both field-type and field-name requests must reach that override. The positive compile protects the visibility and override signatures as well as runtime behavior.

The helper is necessary to cross the package boundary; the existing same-package tests and FieldType analyzer injection do not do so. It borrows analyzers owned and closed by each test. No schema or configset was added. Both suites reuse their current core setup and a small shared assertion in AnalysisRequestHandlerTestBase, which delegates token checks to the existing assertToken helper.

The two analyzer hook Javadocs now state that handlers close streams, overrides manage analyzers they create, and query analyzer selection can occur separately for output and matching. Production behavior is unchanged by this follow-up.

## Outbound files

- solr/core/src/java/org/apache/solr/handler/AnalysisRequestHandlerBase.java
- solr/core/src/test/org/apache/solr/handler/AnalysisRequestHandlerTestBase.java
- solr/core/src/test/org/apache/solr/handler/FieldAnalysisRequestHandlerTest.java
- solr/core/src/test/org/apache/solr/handler/DocumentAnalysisRequestHandlerTest.java
- solr/core/src/test/org/apache/solr/handler/custom/CustomAnalysisHandlers.java

No changelog fragment is added for this tests/Javadoc follow-up; the contributor PR already carries no-changelog. The new commit uses Nick Shanin's ICLA identity with no attribution trailer. Contributor commits and ancestry are preserved.

## Review and proof limits

The scoped diff and new helper were reviewed, and git diff --check passed before commit. No Windows Gradle, compilation, formatter or local test queue ran. The prior upstream green CI covers only fd033eb55a4e, not this new head. The assertions are unexecuted until the named Linux gate supplies fresh logs and XML.

The gate requests both complete existing suites, tidy, root check -x test and three isolated mutations that bypass index selection, query output selection and query matching selection. These are mutation proofs for the contributor's extension contract. This follow-up does not claim a newly repaired production bug: overlaying the new tests on the contributor head should pass. The pre-PR base lacks the extension API, so a compilation error there would not count as runtime fail-before proof.

No contributor branch was changed and the public PR head did not move. No public response was posted.
