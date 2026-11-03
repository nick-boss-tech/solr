# Implementation: SOLR-16398 JSON body error scope, Windows round 1

Status: Implemented, Proof Pending

- Authority: Nick requested patching PR 5285 and handing it off for gating.
- Claim: claims/pr-5285-body-reader-round-1-windows.md
- Parent: e6fbade6773b3bf78e95f50ecc2335276daca651
- New head: 176f29c71a5dbfab56c2ff03193b7c5ff8078f59
- Fork branch: nick-boss-tech/solr:solr-16398-malformed-body-400
- Public PR: https://github.com/apache/solr/pull/5285; the PR head moved by one ordinary commit. Contributor ancestry preserved; no force-push.
- Independent read-back: fork ref and live PR both returned 176f29c71a5dbfab56c2ff03193b7c5ff8078f59 after the push; comments/reviews remained empty.
- Gate: gates/pr-5285-body-reader-round-1.md
- Receipt reserved: receipts/pr-5285-body-reader-round-1.md (not an executed result)

## Change and scope

Addresses F1 in the local PR-5285-review.md. Removed the globally registered JsonProcessingExceptionMapper. CachingJsonMessageBodyReader.readFrom now converts JsonParseException and MismatchedInputException to BAD_REQUEST, preserving the original cause. Other Jackson failures are not classified as client input. The existing catch-all produces standard error responses, metrics and stashed exceptions; no change to endpoint permissions or remote operation code.

Exact outbound files relative to the PR parent:

- solr/core/src/java/org/apache/solr/jersey/JerseyApplications.java
- solr/core/src/java/org/apache/solr/jersey/JsonProcessingExceptionMapper.java (deleted)
- solr/core/src/java/org/apache/solr/jersey/MessageBodyReaders.java
- solr/core/src/test/org/apache/solr/jersey/PostRequestLoggingFilterTest.java
- solr/core/src/test/org/apache/solr/handler/V2ApiIntegrationTest.java

Reused the existing reader tests, Mockito context pattern, integration cluster and response-parser helper. Five added unit methods cover success caching, syntax/type errors, definition passthrough, and actual Jersey provider selection plus status for definition/output failures. The tiny FailingResponse getter creates a real serialization failure without a schema/configset or production endpoint. The integration test now asserts message content and adds malformed syntax through all three response formats. No new dependency or changelog fragment; the original fragment remains applicable.

## Review and evidence limits

Reviewed the exact diff and ran git diff --check successfully. Manually formatted source. No Windows Gradle, compilation, test, or formatting execution. The Linux gate owns tidy, Error Prone, focused/neighbor tests, root checks and two negative proofs: H0 exposes wrong mapper selection, B exposes original invalid-input 500. Runtime behavior remains unverified until the gate completes at this exact head; any older result is stale for H1.

Jersey API signatures for provider lookup and application injection were checked against the official API docs. The unit test exercises provider selection and the selected mapper's response with a context; it does not claim an HTTP serialization failure after commitment.

The public PR body still describes the removed mapper, old proof and overly broad scope. No body edit or comment was authorized/performed. After proof, Nick should review updated description wording and validation results before a public body edit. This implementation report is not a green gate or permission to publish.
