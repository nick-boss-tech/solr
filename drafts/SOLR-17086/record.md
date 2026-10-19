# Skip record: SOLR-17086

- Ticket: SOLR-17086, "v2 Backup and Replica creation: HTTP 500 error messages". JIRA status Open, resolution unresolved, Affects 9.4, component Backup/Restore and v2 API, read from the JIRA issue page 2026-10-19 UTC.
- Verdict: keep-open.
- Draft branch: solr-17086-draft-open. This record supersedes solr-17086-draft-close, which said close-fixed and missed the second problem.
- Pinned head: fork origin/main `498a09aea97aac86c2cc7ac114113b946d249942`.
- Checked by: vm5, 2026-10-19 UTC.

## Ticket reading

Two problems on 9.4, both HTTP 500 with "An exception was not mapped due to exception mapper failure".

1. A v2 backup request (`POST /api/collections/<c>/backups/<name>/versions`, async) sporadically returns the 500. The trace is a `NullPointerException` in `MediaTypeOverridingFilter.filter` (via `V2ApiUtils.getMediaTypeFromWtParam`) because `solrQueryRequest` is null. The backup completes.
2. A v2 ADDREPLICA request (`POST /api/collections/demo/shards/shard1/replicas`, async, NRT) "has the same behavior": the reporter gets the same message at times, and the replica is created.

Attachments (addreplica-result.json, error1.json, error2.json) are not in the text fetched and were not read. The reporter's error1 and error2 are the full responses that would show the second trace.

## Code reading (at `498a09aea97aac86c2cc7ac114113b946d249942`)

Problem 1:

- `solr/core/src/java/org/apache/solr/jersey/MediaTypeOverridingFilter.java:69`: `(solrQueryRequest != null) ? solrQueryRequest.getParams() : null`. The guard. Before commit `c058ea5dac` the call at line 68 was unguarded.
- `solr/core/src/java/org/apache/solr/api/V2HttpCall.java:326`: `containerRequest.setProperty(RequestContextKeys.SOLR_QUERY_REQUEST, solrReq)` runs in `invokeJerseyRequest` (the Jersey dispatch, around line 305-330).

Problem 2:

- `solr/core/src/java/org/apache/solr/handler/admin/api/CreateReplica.java:66`: `public class CreateReplica extends AdminAPIBase implements CreateReplicaApi`. This is an annotated v2 API implementation, not a Jersey resource (no `jakarta.ws.rs` annotations or `@Path` in the class).
- `V2HttpCall.java`: the Jersey filter chain (including `MediaTypeOverridingFilter`) runs only in `invokeJerseyRequest`. The annotated v2 path is a different dispatch (`api.call` on the `Api` found in the ApiBag). No code path was found that sends ADDREPLICA through `MediaTypeOverridingFilter`.
- So the guard that fixes problem 1 does not run for ADDREPLICA, and the NPE trace in problem 2 has no code path at the pinned head that this reading explains.

## History

- `c058ea5dac` (2024-12-27), "SOLR-15751: Add v2 COLSTATUS and \"segments\" APIs (#2912)": adds the guard at `MediaTypeOverridingFilter.java:69`. Ancestor of the pinned head: yes. Subject does not name SOLR-17086.
- The ADDREPLICA v2 class `CreateReplica.java` history was not traced.
- No commit on the fork names SOLR-17086 (empty `git log --grep`).

## Reproduction attempts

1. Reporter's setup (comment, 2023-12-19): two-node SolrCloud in Kubernetes, collection with rf=1, backup and ADDREPLICA requests. Not run in this closeout.
2. Problem 2 on the pinned head: not attempted. The code reading above does not show a path, so a run would be the first test of it. The missing piece is an ADDREPLICA v2 request on a current build that returns the 500, with the stack trace.

## Gap analysis

Problem 1 is explained by the NPE at the parent of `c058ea5dac`, and the same line is guarded at the pinned head. Problem 2 is reported with the same message, but the code that produces the Jersey NPE does not run for the annotated ADDREPLICA API. Either the reporter's ADDREPLICA trace came from a different place (a different filter or handler that raises the same mapper message), or the report mixes two causes. Only the second trace (error1.json or error2.json) or a run on a current build separates these.

## Verdict justification

keep-open. The verdict cannot be close-fixed: the second reported problem is not shown to be fixed, and the code path for it is not found. It cannot be close-cannot-reproduce or pr-needed for the same reason: no reproduction was run for problem 2 and no path that produces it was found. The keep-open minimum is met: the attempts (the code reads for both problems, the history of the guard, the reporter's setup) and the missing piece (the ADDREPLICA trace or a current-build run) are named.

Problem 1 is fixed at the pinned head by `c058ea5dac`. A closeout that addressed only problem 1 would be partial.

## Limits

This record does not reproduce problem 2. It does not read the attachments. It does not check the 9.4 branch. It does not trace the ADDREPLICA v2 class history.
