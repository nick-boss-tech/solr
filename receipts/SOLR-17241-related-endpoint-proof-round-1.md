# Receipt: SOLR-17241 related endpoint proof, round 1 (supplemental)

Verdict: PASS. Claimed and run by the phone agent (claim claims/SOLR-17241-related-endpoint-proof-round-1-phone-agent.md), completing vm2's partial run. Full record: reports/SOLR-17241-related-endpoint-proof-round-1.md. This receipt supplements receipts/SOLR-17241.md and does not replace it.

- Production head: 86d86e3d3e54230b5f3e7b6aa758a189b3b2ed6b (PR apache/solr#5266 head and fork branch confirmed identical before and after).
- Control: parent blobs of MediaTypeOverridingFilter.java and MessageBodyWriters.java (parent 9859d8bb2e8d), applied from the board's control-diff.patch.
- Probe: RelatedEndpointProofTest, SHA-256 e1fb605b8e5aca85f4a6acaf50d18412ca5c168894c65c21c7f79125c9c9b9cd, test-only overlay; seed 17241C0FFEE17241; JDK 21.0.12.1+1.
- Candidate: probe 21/21 pass; MediaTypeOverridingFilterTest 8/8; V2ApiIntegrationTest 14/14.
- Control: probe 12 pass, 9 fail. The failures are the 8 non-hosting javabin and XML backup/ADDREPLICA cases plus the non-hosting XML omitHeader reload; in each, the operation completed and the response arrived as JSON instead of the requested format (javabin MIME mismatch; XML parser rejecting the JSON body). Hosting cases and JSON baselines pass on the control.
- Limits: local filesystem backup repository only; the probe is not part of the PR.

## Proposed PR-body replacements (for the main agent to apply)

Replace: "SOLR-17086 reports the same null-request path for backup and ADDREPLICA. The shared fallback may affect those operations, but they have not been tested here."

With: "SOLR-17086 reports the same null-request path for backup and ADDREPLICA. At this head both endpoints were exercised from hosting and non-hosting nodes with wt=javabin and wt=xml, synchronously and asynchronously; every case returned the requested format with the operation verified complete (backups in a local filesystem repository, checked by restore). With the two response-writing files reverted to their parent state, the non-hosting javabin and XML cases fail with a JSON body in place of the requested format while the operation itself still completes, which is the defect this change fixes."

Replace: "The new integration proof covers javabin reloads. XML format selection is unit-tested; non-hosting XML serialization has no direct integration proof."

With: "The integration proof covers javabin reloads and a non-hosting XML reload with omitHeader=true, which returns the requested XML with the response header omitted; that reload case fails on the parent response-writing code with a JSON body."
