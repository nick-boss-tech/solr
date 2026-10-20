# Skip record: SOLR-9593

- Ticket: SOLR-9593, "ConcurrentDeleteAndCreateCollectionTest failures in nightly builds". JIRA status read 2026-10-20T13:08:23Z (UTC): Open, resolution None, fix versions 6.7 and 7.0 set, updated 2019-06-08.
- Verdict: close-obsolete. This replaces the recorded keep-open: the object type the failure reports (Apache `InternalHttpClient`) is no longer in the tree.
- Draft branch: solr-9593-draft-close.
- Pinned head: 498a09aea97aac86c2cc7ac114113b946d249942 (fork origin/main).
- Checked by: vm2, 2026-10-20T13:08:23Z (UTC).

## Ticket reading

The nightly run of `ConcurrentDeleteAndCreateCollectionTest` fails with "ObjectTracker found 10 object(s) that were not released!!!" and ten `InternalHttpClient` entries. The reporter has seen it only on nightly runs. One 2017 comment moved it to 6.5.

## Code reading (pinned head)

- The grep the verdict rests on: `git grep -l InternalHttpClient 498a09aea97aac86c2cc7ac114113b946d249942 -- solr`. Result: empty. The Apache HttpClient type named in the failure is not in the tree.
- The test class is still present (`solr/core/src/test/org/apache/solr/cloud/api/collections/ConcurrentDeleteAndCreateCollectionTest.java`, `@Nightly` at line 39). The release tracker is now `ObjectReleaseTracker` (`solr/solrj/src/java/org/apache/solr/common/util/ObjectReleaseTracker.java`), not `ObjectTracker`.
- Apache HttpClient was removed from the test infrastructure by SOLR-18188 (commit `31405c513d`, 2026), which the earlier metric-test records also reference for the same reason.

## History

- `31405c513d` (2026-06-10) SOLR-18188: removed Apache HttpClient from test infrastructure and tests. Ancestor of the pinned head (checked with `git merge-base --is-ancestor`).
- evidence.py at the pinned head: no commits naming SOLR-9593.

## Reproduction attempts

Not run. The failure reports an object type that no longer exists; a nightly run of the test would test the current clients, a different question.

## Gap analysis

The leak the ticket describes cannot occur in the reported form, because the client type is gone. Whether the test has any other leak under nightly settings is not raised here and would be a new report.

## Verdict justification

close-obsolete meets the rubric: the code the ticket is about (the Apache HttpClient type in the tracker report) is removed, and the grep under `solr/` at the pinned head is empty.

## Limits

This record does not establish that the test passes nightly on the pinned head. It does not name the commit that first removed each client use.
