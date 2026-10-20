# Skip record: SOLR-4593

- Ticket: SOLR-4593, "Test failure in CollectionsAPIDistributedZkTest: collection already exists". JIRA status read 2026-10-20T15:50:45Z (UTC): Open, resolution None, fix versions 4.9 and 6.0 set, updated 2016-05-09.
- Verdict: keep-open (kept; re-read at the pinned head). The fix-version field is not a commit, and the 2013 failure needs a seeded run to confirm.
- Draft branch: solr-4593-draft-open.
- Pinned head: 498a09aea97aac86c2cc7ac114113b946d249942 (fork origin/main).
- Checked by: vm2, 2026-10-20T15:50:45Z (UTC).

## Ticket reading

One Jenkins run of `CollectionsAPIDistributedZkTest.testDistribSearch` failed with "collection already exists" for a new collection name. The run also had a JVM crash. The 2013 seed did not reproduce for the commenter.

## Code reading (pinned head)

- `solr/core/src/test/org/apache/solr/cloud/api/collections/CollectionsAPIDistributedZkTest.java`: the test class is present.
- `solr/core/src/java/org/apache/solr/cloud/api/collections/CreateCollectionCmd.java:129`: the create path refuses an existing collection name with BAD_REQUEST. That is the error the ticket reports, and it is still present.

## History

- evidence.py at the pinned head: no commits naming SOLR-4593.

## Reproduction attempts

Not run. The failure is intermittent and the seed did not reproduce in 2013; a seeded run on this head is the check.

## Gap analysis

The error path is intended (creating an existing collection is refused). The question is whether the test can reach it by a race in the test setup, which a seeded run would show. The fix-version field does not name the change.

## Verdict justification

keep-open meets the rubric: attempts made are the test-presence check and the create-path reading; the missing piece is a seeded run. close-fixed is not supported without a commit; close-obsolete is not supported since the test and path are present.

## Limits

This record does not run the test. It does not establish whether a race exists in the test setup.
