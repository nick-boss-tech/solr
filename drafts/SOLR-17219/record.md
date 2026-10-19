# Skip record: SOLR-17219

- Ticket: SOLR-17219, "Exceptions occur while Solr reads some core's configset (java.io.IOException: Error opening /configs/<path>)". JIRA status Open, resolution none, created 2024-03-28, last updated 2024-11-19, checked 2026-10-19 UTC.
- Verdict: keep-open.
- Draft branch: solr-17219-draft-open.
- Pinned head: fork origin/main `498a09aea97aac86c2cc7ac114113b946d249942`.
- Checked by: vm5, 2026-10-19 UTC.

## Ticket reading

Three Solr 9.5 nodes and one ZooKeeper; 200 collections on one configset created through the Configsets API. After a node restart, recovery fails intermittently with `java.io.IOException: Error opening /configs/<set>/lang/stopwords_fi.txt`, and the cores stay down. The file differs between runs. The reporter saw no retry. Comments: gjactat reports the problem is absent on 9.0, 9.3, 9.4.1, present on 9.6.0, absent on 9.6.1 and 9.7.0; chrisyoung reports it on 9.5 and 9.6 with random collections affected.

## Code reading (at `498a09aea97aac86c2cc7ac114113b946d249942`)

- `solr/core/src/java/org/apache/solr/cloud/ZkSolrResourceLoader.java:80-112`: the ZooKeeper resource open retries only `KeeperException.SessionExpiredException` (after a 1 s sleep). The `catch (Exception e)` at line 109-110 wraps every other failure as `new IOException("Error opening " + file, e)` with no retry. That message matches the reported one.
- Line 92-93: when the node does not exist the loop breaks, so a missing file is not retried either.
- `git log -L95,130:solr/core/src/java/org/apache/solr/cloud/ZkSolrResourceLoader.java`: the catch structure dates from SOLR-9291 (2016, 42e1caf2bf). No change to this method in 2024.
- The configset read path also reaches `ZkSolrResourceLoader` through the analysis factories that load `lang/stopwords_*.txt`. The failing file is one of those.

So at this head, any ZooKeeper failure other than session expiry during a resource read (for example a connection loss while many cores recover at once) is not retried, and the error text is the one in the ticket. That is a code-path argument for the symptom, not a reproduction.

## History

- `42e1caf2bf` (2016-07-11), SOLR-9291: the session-expiry retry; the reason for the retry-only-on-session-expiry rule.
- `git log --since=2024-03 --until=2024-12` on the resource loader and the configset path: no commit names SOLR-17219 or fixes the `Error opening` path. The 9.6.1 change the reporters observed is not identified.
- The audit verdict (2026-10-04) says the issue was "fixed upstream in a release". The fixing commit is not identified in this fork, so this record does not use the close-fixed verdict.

## Reproduction attempts

Not attempted. The reproduction needs a three-node SolrCloud, one ZooKeeper, a configset with language resources, about 200 collections on it, and a node restart under load. That setup is larger than a closeout run; the first step would be a single-node ZooKeeper connection drop during a resource read, which would show whether the non-session-expiry path is reachable without retry. That is the missing piece.

## Gap analysis

The reported error text and the no-retry path match at `ZkSolrResourceLoader.java:109-110`. What is not established: (1) which ZooKeeper failure the reporter hit (connection loss, a missing node while the set was being rewritten, or something else); (2) what changed in 9.6.1 that the reporters say fixes it. Without (1) a fix cannot be shaped, and without (2) the close verdict has no commit to cite.

## Verdict justification

keep-open: the attempts (the code reads with file and line, the history of the method, the search of 2024 history for the fix) are named, and the missing pieces are the failing ZooKeeper error type and the 9.6.1 fixing commit. close-fixed needs a fixing commit on main; none is identified. close-cannot-reproduce needs a reproduction on the pinned head; none was run. pr-needed needs the failing path shown and a change decided; the failing ZooKeeper error is not shown.

## Limits

This record does not reproduce the failure. It does not identify the 9.6.1 change. It does not check the attached stack trace on the ticket (not in the fetched text).
