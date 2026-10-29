# Receipt: SolrJ V2Request default parser, wire capture (vm6, Linux)

- Assignment: `assignments/solr-v2request-default-parser-round-1.md`, questions 1 and 2. Claim: `claims/solr-v2request-default-parser-wire-vm6.md`. Host: vm6.
- Result: the mismatch recorded in SOLR-16398 round 3 does not reproduce on the current head, and the explanation recorded there is wrong. Not a binary-compatibility issue.
- CORRECTION (2026-10-29, later): the round 3 failure is real and intermittent. It is a server-side routing bug that leaves `wt` unhonored when the receiving node hosts no replica of the collection. See `receipts/solr-wt-ignored-non-hosting-node-round-1.md`. The "not reproduced" conclusion below is superseded.
- Raw evidence: `/workspace/gates/logs/vm6/v2probe/` (wire log, probe sources, Jetty stderr, probe test XML).

## Evidence

A. Client-side wire capture (JDK client, HTTP/1.1 listener). Both legs send `POST /api/collections/c/shards/shard1/split?wt=javabin`, `Content-Type: application/json`, body `{"numSubShards": 2}`, and no `Accept` header. Log: `wire.log`. Method: `listener.py` (answers JSON) and `Consumer.java`, compiled against the built SolrJ classes in `/workspace/tmp/inspect-core/solr/solrj/build/classes/java/main` with JDK 21 (`/workspace/jdk/jdk-21.0.12.1+1`). The listener answers JSON to everything, so both legs fail the mime check by design. This capture only shows the request.

B. In-process parameters from the Jetty client family the test cluster uses. `initializeSolrParams` returns `wt=javabin` for the default leg and the explicit leg (`JettyParams.java`, output recorded in `jetty.err` and the claim). The sends fail with `frame_size_error` because the listener does not speak HTTP/2 cleartext; the parameters are computed before the send.

C. Real cluster, current head `b135a03` (command branch), temporary test `ProbeDefaultParserTest`, gate run `solr16398-probe-default`, rc 0. The test was removed from the branch afterwards; its XML is kept in the logs.
- `PROBE client=CloudJettySolrClient`
- `PROBE default OK`: the default split returned javabin, which the default parser read.
- `PROBE explicit-javabin RemoteSolrException: Can't lock parent slice for splitting`: a probe artifact. The default leg had already split `shard1`, and the explicit leg split it again. It says nothing about the parser.
- `PROBE default-get-collections OK`.

D. Earlier, round 3 (`solr16398-r3-green`): the default split and move-replica calls failed with `Expected mime type in [javabin, octet-stream] but got application/json`. The JUnit XML from that run was overwritten by later runs, so the message is quoted from the transcript only. The same request shape does not reproduce on the current head (C). The cause is not identified.

## Conclusion

- Not binary. Nothing here involves removed classes or changed descriptors.
- No current evidence of a default-parser mismatch in SolrJ. The client sends `wt=javabin` for a default `V2Request`, and the real server returned javabin for it.
- The earlier statement that the default parser "does not send `wt=javabin`" is wrong. Corrected in `claims/SOLR-16398-command-jaxrs-fix-r3-vm6.md`, decision 2.
- Recommendation: close the assignment as not reproduced. Reopen only if a reviewer reproduces the round 3 failure on another head.

## Limits

- One run; one split and one move request for the default leg, plus GET `/collections`.
- The round 3 failure stays unexplained. The listener capture does not use the Jetty HTTP/2 path, which is the test client's transport, so the on-wire check is HTTP/1.1 only.

AI-generated draft. Nick Shanin directed the work and takes responsibility for this contribution.
