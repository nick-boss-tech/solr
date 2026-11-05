# Encoded targeting-key follow-up implementation, round 1

Status: Implemented, Proof Pending.

- Owner goal: implement adjacent follow-up code/tests on separate branches. Assignment `assignments/solr-12613-adjacent-implementation-round-1.md`; Windows claim `claims/solr-12613-adjacent-windows-round-1.md`.
- Shared integration baseline `6ec2e527ad716bde74404b2037bb1a1e6c52f79c`: ordinary conflict-free merge of main `07ccc73cd24e26e59da169ca35b3fd5f64c35986` and PR 5110 head `73b30956ff2c0f7b16f6af3bceacf95bda8271b9`. The original PR is still open; comparisons to main include its original changes. Our delta is baseline-to-candidate.
- Fork branch `pr-5110-followup-encoded-key`, candidate `bd05d1ed39a005317f0a6655e4cd8fed078531b8`. Plain-pushed and independently verified with the fork-ref API. Commit author Nick Shanin; no trailers.
- Gate `gates/solr-12613-adjacent-encoded-key-round-1.md`; receipt target `receipts/PR-5110-adjacent-encoded-key-round-1.md`.

The filter now recognizes encoded targeting parameter names while preserving header conflicts, GET-only query targeting, directive consumption and unrelated-query header behavior. It handles malformed encoded query errors inside the filter to preserve 400 through authentication. Added five unauthenticated and two authenticated tests using raw Jetty query paths. No runtime/Gradle proof ran on Windows; scoped diff and diff --check were reviewed before commit.

Exact outbound files: `solr/core/src/java/org/apache/solr/servlet/NodeTargetProxyFilter.java`, `solr/core/src/test/org/apache/solr/servlet/NodeTargetProxyFilterTest.java`, `solr/core/src/test/org/apache/solr/servlet/NodeTargetProxyFilterAuthTest.java`. No follow-up changelog was fabricated before an issue/PR identity exists. Public packaging is still needed after proof/owner direction.

The C1 system-info production change was excluded because the inspected SOLR-18549 branch already rethrows SolrException. Other plan areas remain active; this report completes only the encoded-key implementation handoff. No PR or public comment was opened/posted and no contributor branch was changed.
