# Report: SOLR-17241 related backup and ADDREPLICA response proof, round 1

Host: phone agent (claim claims/SOLR-17241-related-endpoint-proof-round-1-phone-agent.md), completing the job vm2 ran partially (gates/SOLR-17241-related-endpoint-proof-round-1-handoff-vm2.md). Verdict: PASS, supplemental proof complete. Receipt: receipts/SOLR-17241-related-endpoint-proof-round-1.md.

## Identities

- Production head (candidate): 86d86e3d3e54230b5f3e7b6aa758a189b3b2ed6b. Verified before and after the runs: PR apache/solr#5266 open at that head; fork refs/heads/solr-17241-submit at that head.
- Control: parent production 9859d8bb2e8d63061116b8281639a4beb1be480c, applied as the board's control-diff.patch (parent blobs of the only two production files the fix touches): solr/core/src/java/org/apache/solr/jersey/MediaTypeOverridingFilter.java SHA-256 7a3e48192f160fc2f0e27a330e7c891c6b6f2504a6404e9328a14b06fbfe102c; solr/core/src/java/org/apache/solr/jersey/MessageBodyWriters.java SHA-256 78a99153f792f014d70125a8c5a4d990db075469ddd0f9993e162d1b8e0bbeba.
- Probe: RelatedEndpointProofTest (org.apache.solr.handler), board artifact gates/artifacts/SOLR-17241-related-endpoint-proof-round-1/RelatedEndpointProofTest.java.txt, SHA-256 e1fb605b8e5aca85f4a6acaf50d18412ca5c168894c65c21c7f79125c9c9b9cd, byte-identical to the file vm2 executed. Run as a test-only overlay in a disposable worktree at the pinned head; production files matched the pinned head throughout the candidate legs, and the two control files were restored immediately after the control leg (final worktree status: only the untracked probe file).
- Environment: Linux x86_64, JDK 21.0.12.1+1 (vendored), Gradle 9.7.0 via solr-gradle.sh, flock on ~/workspace/tools/.solr-test-queue.lock, env -u NODE_ENV. Seed 17241C0FFEE17241 for all three invocations. Logs and fresh XML under ~/workspace/tools/gate-17241-proof/ on this host (probe-candidate.log, probe-control.log, suites.log, xml-candidate/, xml-control/, xml-suites/, steps.log with rc markers).

## Commands and results (2026-11-01 UTC)

1. Candidate probe: :solr:core:test --tests org.apache.solr.handler.RelatedEndpointProofTest, rc=0. Fresh XML (2026-11-01T05:19:27Z): 21 tests, 0 failures, 0 errors, 0 skipped.
2. Control probe (same command, control patch applied): rc=1. Fresh XML (2026-11-01T05:21:25Z): 21 tests, 9 failures, 0 errors, 0 skipped.
3. Neighboring suites on the candidate: :solr:core:test --tests org.apache.solr.jersey.MediaTypeOverridingFilterTest --tests org.apache.solr.handler.V2ApiIntegrationTest, rc=0. MediaTypeOverridingFilterTest: 8 tests, 0 failures. V2ApiIntegrationTest: 14 tests, 0 failures.

## Per-case outcome

The probe exercises real v2 endpoints over HTTP against a 3-node cluster, requests aimed at a named node with a direct client (no CloudSolrClient rerouting), replica placement pinned so the request target never becomes a hosting node mid-matrix. 21 cases: 16 backup/ADDREPLICA matrix cases (hosting and non-hosting, wt=javabin and wt=xml, sync and async), 4 JSON baselines, 1 non-hosting XML reload with omitHeader=true. Every case logs its topology and an operationCompleted effect line; backups are verified by restoring into a new collection and checking the 5 indexed documents; async cases assert the submitted request ID and poll to terminal success before checking the effect.

- Candidate: all 21 cases pass. Every backup and ADDREPLICA case reports operationCompleted=true; the XML reload returns application/xml with the response header omitted.
- Control: 12 pass, 9 fail. All 8 hosting cases and all 4 JSON baselines pass. The 9 failures are exactly the non-hosting format cases: backupNonHostingSyncJavabin, backupNonHostingAsyncJavabin, addReplicaNonHostingSyncJavabin, addReplicaNonHostingAsyncJavabin (RemoteSolrException: expected a javabin MIME type but got application/json), and backupNonHostingSyncXml, backupNonHostingAsyncXml, addReplicaNonHostingSyncXml, addReplicaNonHostingAsyncXml, reloadXmlOmitHeaderNonHosting (SAXParseException: content is not allowed in prolog; the XML parser received the JSON body). In every failing case the probe's effect line records operationCompleted=true before the response decoding fails: the backup was written and the replica was added on the parent code; only the response came back in JSON instead of the requested format. That is the response-serialization defect of SOLR-17241 / SOLR-17086, discriminated by route (non-hosting only) and format (javabin and XML, not JSON), not a fixture or setup failure.

vm2's earlier candidate pass (21/21 on its host, XML retained there) corroborates the candidate leg; vm2's control attempts never produced an outcome, so this control run is the first fail-before evidence for these endpoints.

## Limits

Local filesystem backup repository only; nothing here speaks to S3 or other repository types. The probe is a temporary test overlay, not part of the PR; whether a permanent test joins the branch is a separate implementation decision. SOLR-17086's full original report conditions are not claimed beyond the response-format path exercised here.
