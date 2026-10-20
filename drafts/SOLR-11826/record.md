# Skip record: SOLR-11826

- Ticket: SOLR-11826, "TestPKIAuthenticationPlugin NPE". JIRA status read 2026-10-20T07:40:11Z (UTC): Open, resolution None, no fix versions, updated 2019-06-08.
- Verdict: keep-open (kept; re-read at the pinned head).
- Draft branch: solr-11826-draft-open.
- Pinned head: 498a09aea97aac86c2cc7ac114113b946d249942 (fork origin/main).
- Checked by: vm2, 2026-10-20T07:40:11Z (UTC).

## Ticket reading

Jenkins failures of `TestPKIAuthenticationPlugin` with an NPE, in a 7.2 nightly run. The reporter links SOLR-9401, which fixed some cases, and attached logs for one scenario. No comments.

## Code reading (pinned head)

- `solr/core/src/test/org/apache/solr/security/TestPKIAuthenticationPlugin.java` exists at the pinned head (239 lines). The test is in the tree, so the ticket is not obsolete.
- The ticket names no line of the NPE. The reporter's attached logs are not in the ticket text, so the failing line cannot be read from it.
- `git grep SOLR-9401` under `solr/core/src`: no hits. The earlier fix is not named in the code.

## History

- evidence.py at the pinned head: no commits naming SOLR-11826.

## Reproduction attempts

Not run in this pass. The failure is a flake reported from Jenkins logs, and a focused run of the test would show whether it still fails. That run is the missing piece.

## Gap analysis

The reporter's failure was intermittent. Without the logs, the NPE site cannot be matched to a line at the pinned head. A repeated focused run, with its seed and XML counts, is the next check.

## Verdict justification

keep-open meets the rubric: attempts made are the existence check and the reading of the ticket; the missing pieces are the attached logs (for the NPE site) and a focused run with counts. close-fixed is not met: no fix commit is named. close-cannot-reproduce is not met: no run was made.

## Limits

This record does not establish that the flake still occurs. It does not check later Jenkins runs.
