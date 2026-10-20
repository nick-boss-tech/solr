# Skip record: SOLR-9645

- Ticket: SOLR-9645, "removing collection while it's recovering blows up cluster". JIRA status read 2026-10-20T12:41:48Z (UTC): Open, resolution None, updated 2019-06-08. Superseded SOLR-9639 (Closed).
- Verdict: keep-open (kept; re-read at the pinned head).
- Draft branch: solr-9645-draft-open.
- Pinned head: 498a09aea97aac86c2cc7ac114113b946d249942 (fork origin/main).
- Checked by: vm2, 2026-10-20T12:41:48Z (UTC).

## Ticket reading

Deleting a collection while its replicas are recovering floods the heap with requests that look like `/get`. The reporter says the removal is stuck (explained in SOLR-9639) but not the flood, and gives a reproduction: roll back the SOLR-9639 commit and run the test. No comments.

## Code reading (pinned head)

- `solr/core/src/java/org/apache/solr/cloud/api/collections/DeleteCollectionCmd.java:150`: the delete waits for the collection state to disappear (`waitForState(..., 60 seconds, Objects::isNull)`). The removal wait is present.
- The reproduction needs the SOLR-9639 commit, which is not in this tree (grep `SOLR-9639` under `solr/`: no hits). Without it the rollback cannot be done here.

## History

- evidence.py at the pinned head: no commits naming SOLR-9645.

## Reproduction attempts

Not run. The reproduction depends on reverting SOLR-9639, a commit this tree does not name, and on the test the reporter ran, which was not located.

## Gap analysis

The flood source is not identified: the ticket names `/get` traffic during recovery, and the code path for that was not traced in this pass. A run on the reverted commit is the missing piece.

## Verdict justification

keep-open meets the rubric: attempts made are the removal-wait reading and the SOLR-9639 search; the missing piece is the reverted-commit reproduction. close-fixed and close-obsolete are not supported: no fix commit is named and the removal path is present.

## Limits

This record does not identify the flood source. It does not check the Solr 6.x history of SOLR-9639.
