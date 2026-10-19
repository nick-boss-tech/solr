# Skip record: SOLR-14226

- Ticket: SOLR-14226, "SolrStream reports AuthN/AuthZ failures (401|403) as IOException w/o details". JIRA status Open, resolution none, fix versions none, created 2020-01-28, updated 2020-05-27. Checked 2026-10-19 UTC.
- Verdict: close-fixed (changed from keep-open; this redo supersedes the recorded verdict).
- Draft branch: solr-14226-draft-close (the earlier keep-open branch solr-14226-draft-open is not updated).
- Pinned head: fork origin/main 498a09aea97aac86c2cc7ac114113b946d249942. No upstream sha cited.
- Checked by: vm2 (pool Linux host), 2026-10-19 UTC, for the remediation redo of this ticket (claim claims/skip-SOLR-14226-vm2.md).

## Ticket reading

A SolrJ SolrStream call to a Solr node that rejects the request with 401 or 403 throws a generic IOException with no details, because the response body is parsed as the expected stream instead of being checked for the status. The reporter's test (hossman, 2020-01-28) shows the case. Two later comments (2020-01-30) link the test-only commits 517438e356d (master) and b2d8b784a30 (branch_8x), which add authn and authz edge-case tests for /stream in cloud mode. No commit that fixes the behaviour is linked.

## Code reading

- solr/solrj-streaming/src/java/org/apache/solr/client/solrj/io/stream/SolrStream.java, method constructParser (origin/main 498a09aea97): the response status is read from the response (`responseStatus`), and when it is 401 or 403 the code consumes the stream as an error message (consumeStreamAsErrorMessage) and throws an IOException that carries that message. The check is present at the pin (`grep -c 'statusCode == 401'` returns 1).
- The status check was added by commit c7b58302d33 (2023-08-22, "SOLR-16929 SolrStream propagates undecoded error message (#1852)"). That commit is an ancestor of the pinned head (git merge-base --is-ancestor).
- 517438e356d (2020-01-30, the test-only commit the ticket links) is also an ancestor. It adds tests and does not change SolrStream.

Commands at the pinned head:
- git grep -n 'statusCode == 401' origin/main -- solr/solrj-streaming/src/java/org/apache/solr/client/solrj/io/stream/SolrStream.java: one line in constructParser.
- git log --format='%h %ad %s' --date=short -S 'statusCode == 401' origin/main -- solr/solrj-streaming/src/java/org/apache/solr/client/solrj/io/stream/SolrStream.java (bounded, path-limited): c7b58302d33.
- git log --oneline --grep='SOLR-14226' origin/main: 517438e356d only (test-only).

## History

- 517438e356d (2020-01-30): authn and authz edge-case tests for /stream (test-only; the ticket's linked commit).
- c7b58302d33 (2023-08-22): SOLR-16929 SolrStream propagates undecoded error message. The fix for the 401/403 case; ancestor of the pinned head. Its commit body does not name SOLR-14226.

## Reproduction attempts

Not run in this pass. The reporter's test (testEchoStreamFail) is in the streaming test tree; a run of the class with a 401 or 403 response was not made.

## Gap analysis

The reported symptom (401 or 403 surfaced as an IOException without details) is addressed by the status check added in c7b58302d33. The link between this ticket and that commit rests on the symptom and the code path, not on a ticket reference, since the commit names SOLR-16929. The reporter's exact test was not run on the pin.

## Verdict justification

close-fixed meets the rubric in its minimum form: a commit on main fixes the reported problem (c7b58302d33), the fixing commit is an ancestor of the pinned head, and the changed code is still present at the head (SolrStream.java constructParser, the 401/403 check). The requirement not fully met is that the commit does not name this ticket, so the match is by symptom and code path, and the reporter's test was not run on the pin. The verdict should be read as close-fixed with that linkage stated as an inference.

## Limits

This record does not run the reporter's test. It does not confirm that SOLR-16929 and SOLR-14226 are the same issue. It does not check the branch_8x history.
