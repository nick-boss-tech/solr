# Skip record: SOLR-13501

- Ticket: SOLR-13501, Use Http2SolrClient in DUP and remove ConcurrentUpdateHttp2SolrClient.
- Verdict: keep-open. This replaces the earlier close-obsolete verdict on solr-13501-draft-close (5606bb4d3e).
- Draft branch: solr-13501-draft-open (new branch from origin/coordination; the close draft is left in place).
- Pinned head: fork origin/main 498a09aea97aac86c2cc7ac114113b946d249942. Code read at the pin in this pass.
- Source: the earlier B2 reading (vm2, 2026-10-19) is replaced by this pass.

## Ticket reading

JIRA read 2026-10-19 (this pass): Open, resolution None, created 2019-05-30, updated 2019-06-08, fix version 8.2 listed in the field. The description says Http2SolrClient removes the need for ConcurrentUpdateSolrServer and that DUP should use async and Http2SolrClient. The 2019-05-30 comments (markrmiller) say ConcurrentUpdateHttp2SolrClient has a race in offer(), that the concurrent client should be removed, and that the change needs benchmarking first because the concurrent client was added for performance.

## Code reading

Read at the pin:

- The class `ConcurrentUpdateHttp2SolrClient` is not defined in any Java file. `git grep -c 'class ConcurrentUpdateHttp2SolrClient' 498a09aea97aac86c2cc7ac114113b946d249942 -- '*.java'` returns no matches. The name appears in five non-code files only (CHANGELOG.md:365, :1270, :2789; changelog/archive.md; changelog/v10.0.0 and the 10.0 upgrade notes), which record the rename.
- The rename is in `2c55000296` (SOLR-18005, "SolrJ: consolidate Jetty-using classes to a jetty package (#3901)") and `a3b5af4e55` (SOLR-18005, "ConcurrentUpdateJettySolrClient (#3885)"), both ancestors of the pin. The changelog entry reads "Renamed ConcurrentUpdateHttp2SolrClient to ConcurrentUpdateBaseSolrClient, made abstract, with a new ConcurrentUpdateJettySolrClient as it's only implementation."
- The update path still uses a concurrent client: `solr/core/src/java/org/apache/solr/update/StreamingSolrClients.java:30-32` imports `ConcurrentUpdateBaseSolrClient` and `ConcurrentUpdateJettySolrClient`; `:48` holds an `HttpJettySolrClient`; `:70`, `:96`, `:102` keep `ConcurrentUpdateBaseSolrClient` instances; `:159` declares `Builder extends ConcurrentUpdateJettySolrClient.Builder`. `solr/core/src/java/org/apache/solr/update/SolrCmdDistributor.java:36` imports `ConcurrentUpdateBaseSolrClient`.
- `solr/core/src/java/org/apache/solr/update/processor/DistributedUpdateProcessor.java` has no reference to `Http2SolrClient`, `HttpJettySolrClient`, `ConcurrentUpdate*`, or any SolrClient type (grep at the pin is empty). DUP's distribution goes through the update package classes above.

## History

- `2c55000296` (SOLR-18005, consolidation to a jetty package): ancestor of the pin.
- `a3b5af4e55` (SOLR-18005, ConcurrentUpdateJettySolrClient): ancestor of the pin.
- No commit at the pin names SOLR-13501 (evidence tool: none).

## Reproduction attempts

Not run. No build or benchmark in this pass.

## Gap analysis

- The close-obsolete verdict in the earlier draft said the code is gone. The class name is gone from Java, but the concurrent-update design the ticket asks to remove is still the update path: `StreamingSolrClients` and `SolrCmdDistributor` use the concurrent client under the names `ConcurrentUpdateBaseSolrClient` and `ConcurrentUpdateJettySolrClient`. A rename is not the removal the rubric's close-obsolete requires ("removed, and a search of main finds no remaining use"), so close-obsolete does not hold.
- close-fixed does not hold either: no commit names the ticket, and SOLR-18005 is a rename under another ticket, not a change to DUP's use of the client.
- The ticket's design question (should the update path drop the concurrent client for async Http2/Jetty calls) is not settled. The 2019 comment asks for benchmarks before such a change; none is recorded.
- Missing piece: a decision on whether the rename satisfies the ticket, and, if the design change is wanted, a benchmark of the update path with and without the concurrent client, plus the offer() race the comment describes, shown at the pin.

## Verdict justification

keep-open: the old class is renamed and no Java code refers to the old name, but the concurrent client the ticket asks to remove is still the update path, and the ticket's design change is neither made nor settled. This is a recorded choice (decision ladder rung 4); the owner may rule that the rename closes the ticket, which would support close-obsolete with the scope stated.

Missing for a closing verdict: an owner ruling on the rename, or, for a fix, a benchmark and a change to StreamingSolrClients and SolrCmdDistributor.

## Limits

This record does not establish a reproduction and does not benchmark. It does not post anything to JIRA or GitHub.
