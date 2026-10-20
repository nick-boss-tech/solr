# Skip record: SOLR-11864

- Ticket: SOLR-11864, "Create Collection API allows creating collection with trailing space". JIRA status read 2026-10-20T07:01:03Z (UTC): Open, resolution None, no fix versions, updated 2019-06-08.
- Verdict: keep-open. This replaces the recorded close-fixed: the check exists at the pinned head, but the commit that fixed it is not named.
- Draft branch: solr-11864-draft-open.
- Pinned head: 498a09aea97aac86c2cc7ac114113b946d249942 (fork origin/main).
- Checked by: vm2, 2026-10-20T07:01:03Z (UTC).

## Ticket reading

Creating a collection with a trailing space in its name succeeds, and the collection then cannot be deleted through the API, and its shards stay in recovery. The reporter asks for a trim or a validation error. No comments.

## Code reading (pinned head)

- `solr/solrj/src/java/org/apache/solr/client/solrj/util/SolrIdentifierValidator.java:30`: `identifierPattern = ^(?!\-)[\._A-Za-z0-9\-]+$`. A space is not in the allowed set, so a name with a trailing space does not match.
- `solr/core/src/java/org/apache/solr/handler/admin/api/CreateCollection.java:274`: `SolrIdentifierValidator.validateCollectionName(requestBody.name)`. This is the create path in the V2 API definition. The check is present at the pinned head, so the behavior the ticket reports should be rejected here.
- `solr/core/src/java/org/apache/solr/handler/admin/api/RestoreCollection.java:99`: the same validator on restore.

## History

- Blame of `CreateCollection.java:274` at the pinned head: fe55f7cd24 (SOLR-16825, 2023-10-03, migration of v2 definitions). That commit moved the line; it is not the commit that added the check.
- The validator's pattern history (path-scoped): 9166647918 (SOLR-9016, 2016, empty identifiers), f24810bdf1 (SOLR-8765, 2016). Neither names SOLR-11864, and both predate the ticket's 2018 report.
- No commit at the pinned head names SOLR-11864 (evidence.py).
- The commit that first put a collection-name validation call on the create path that the 2018 report used was not identified. Without it, the close-fixed verdict cannot name a fixing commit.

## Reproduction attempts

Not attempted. A run would call the create API with a name that has a trailing space at the pinned head and check the response. That is the check that would settle the current behavior.

## Gap analysis

The code path now rejects the name, which is the probable fix. The fix commit, and whether the v1 create path (not only the V2 definition) goes through the same check, are the open pieces. close-fixed needs both the commit sha and an ancestor check, and neither is in hand.

## Verdict justification

keep-open meets the rubric: attempts made are the validator and call-site reading and the history search; missing piece is the fixing commit sha, plus a run of the create API with a trailing-space name at the pinned head. close-fixed is not met, since no fixing commit is named (recorded verdict replaced).

## Limits

This record does not establish that the v1 create path rejects the name. It does not check the JIRA's later comments, of which there are none.
