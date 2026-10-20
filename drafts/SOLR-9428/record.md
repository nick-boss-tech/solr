# Skip record: SOLR-9428

- Ticket: SOLR-9428, "TestManagedSchemaAPI Objects not released test failures". JIRA status read 2026-10-20T14:05:02Z (UTC): Open, resolution None, updated 2019-11-21. Linked: SOLR-8067 (Open).
- Verdict: keep-open (kept; re-read at the pinned head).
- Draft branch: solr-9428-draft-open.
- Pinned head: 498a09aea97aac86c2cc7ac114113b946d249942 (fork origin/main).
- Checked by: vm2, 2026-10-20T14:05:02Z (UTC).

## Ticket reading

Jenkins runs of `TestManagedSchemaAPI` report "ObjectTracker found 1 object(s) that were not released" and a failed config-name lookup for the collection. The 2016 patch logs the root cause. A 2017 comment links the probable cause to a ZooKeeper session expiry under `readConfigName`, and asks for retry logic like SOLR-8662.

## Code reading (pinned head)

- `solr/core/src/java/org/apache/solr/rest/ManagedResourceStorage.java:99-112`: the config name is read from `zkStateReader.getClusterState().getCollection(collection).getConfigName()` with no retry. A failure is logged and rethrown as a `SolrException` ("Failed to load config name for collection ... due to"). The 2016 message text is replaced.
- The retry the 2017 comment asks for is not in this path. The read is from the cached cluster state, which the two comments did not trace.

## History

- evidence.py at the pinned head: no commits naming SOLR-9428.

## Reproduction attempts

Not run. The flake needs a seeded run of `TestManagedSchemaAPI` under the JIRA's conditions; a single run does not show the object leak.

## Gap analysis

The code path has no retry, so a ZooKeeper error during the read would still fail the request. Whether that is the cause of the leak reported is not shown: the leak itself would need a seeded run.

## Verdict justification

keep-open meets the rubric: attempts made are the code-path reading of ManagedResourceStorage.java:99-112; the missing piece is a seeded run and a trace of the leak. close-fixed and close-obsolete are not supported: the path is present. close-cannot-reproduce is not supported: no run was made.

## Limits

This record does not establish the leak's cause. It does not check SOLR-8067.
