# SOLR-18610 web identity policy test: implementation handoff, round 1

Status: IMPLEMENTED, PROOF PENDING

- Host: windows (Codex desktop), 2026-10-29
- Claim: claims/pr-5264-web-identity-test-round-1-windows.md
- Fork branch: nick-boss-tech/solr:solr-18610-submit
- Pushed and REST-verified head: dbacd6951e7692d1b55920eddb239284df33b3b0
- Parent: contributor PR 5264 head dbb3d85d72dd27889d8b93f10d1f4c2f54aa5e0e
- Gate: gates/pr-5264-web-identity-test-round-1.md
- Outbound delta from contributor: one file, 152 lines, solr/modules/s3-repository/src/test/org/apache/solr/s3/S3WebIdentitySecurityManagerTest.java

The new test leaves the module's useSecurityManager=false setting in place. Its parent runs a local STS HTTP stub, then starts a separate JVM with the Security Manager and the real shipped policy. The direct WebIdentityTokenFileCredentialsProvider uses a dummy token and role and must return exactly the stub's session credentials. The parent also checks that the expected web identity action and token were sent. A failure to construct the SDK's default Apache 5 HTTP client therefore fails the child and the test, rather than falling through to another credential provider.

Exclusive policy loading prevents default/user policy grants from masking the bug. A fresh user.home, removal of inherited AWS/proxy/JVM-option variables, fixed region and local endpoint prevent dependence on a developer's AWS configuration. The child has a bounded timeout and cleanup; the local server is stopped in finally. Java 24+ skips before starting the fixture.

Source review confirmed AWS SDK 2.54.6's STS service model uses query protocol and the response namespace used by the stub. SDK endpoint configuration supports the aws.endpointUrl global property. The previous review already traced the three exact Apache5HttpClient permission checks. Existing infrastructure searched: S3MockContainerRule, SolrProcessManagerTest, SolrTestCase, ExternalPaths, module build and randomized test/security-policy wiring. No existing reusable STS fixture was found. The new test adds no dependency or build configuration and uses no Docker.

Validation on Windows: manual source/diff review, ticket-only worktree status, clean git diff --check, scoped commit and plain fork push, REST head verification. No Gradle, compilation, test process, or local test queue was run. The test is not yet proven to compile or pass. The required Linux gate must validate child classpath and endpoint setup, one executed Java 21 pass, original-policy failure, tidy and root check. Any repair must be committed/pushed and proven at its final head.

No public comment or PR was created, no public body was changed, and the contributor's branch was not modified. This is a test contribution on Nick's fork, ready for Linux proof before an offer to the contributor.
