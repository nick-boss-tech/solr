# Skip record: SOLR-17751

- Ticket: SOLR-17751, "SolrException: Unable to read response info for shard". JIRA status read 2026-10-19 (UTC): Open, resolution none, created 2025-05-02, no fix version, no comments.
- Verdict: keep-open. Unchanged from the earlier record.
- Draft branch: solr-17751-draft-open.
- Pinned head: fork origin/main 498a09aea97aac86c2cc7ac114113b946d249942 (2026-10-07). Upstream sha not cited.
- Checked by: vm4, 2026-10-19 13:16-13:20 UTC.

## Ticket reading

The reporter saw many "Unable to read response info for shard" errors during about an hour on 2025-04-29, on Solr 9.6.1, in a request handler with a multi-shard collection. The stack trace in the ticket stops at the wrapper message. The full description was read through the JIRA REST API (3926 characters): it contains no "Caused by" line and no other exception text. The ticket has no comments and no reproduction steps.

## Code reading

- `solr/core/src/java/org/apache/solr/util/SolrResponseUtil.java:66`: `catch (Exception ex)` covers the whole response read, including `Objects.requireNonNull` at line 47.
- `SolrResponseUtil.java:67-68`: if the request allows partial shard results (`HttpShardHandler.getShardsTolerantAsBool`), the error returns null and is dropped.
- `SolrResponseUtil.java:69-74`: otherwise the code throws `SolrException` with the message "Unable to read <key> info for shard: <shard>" and the original exception as its cause.

What this shows at the pinned head: any exception from reading a shard's response is turned into this one message, and the original type and message appear only as the cause. The ticket's log line does not show the cause, so the failing read is not identified from the ticket. The code is unchanged in the sense that matters here: this is the same throwing path the ticket quotes.

## History

- `git grep -n 'Unable to read response' 498a09aea97...` on the pinned head: no hits. The wording in the ticket is produced by `SolrResponseUtil` with a key and shard name, so the grep for the full phrase does not match.
- No commit on the pinned head names SOLR-17751 (evidence tool: no hits).

## Reproduction attempts

Not attempted. A reproduction needs a shard response that fails in the same way (for example a malformed or missing response header on one shard). The ticket does not say which shard response failed, so no input is known. This pass is review-only and runs no builds.

## Gap analysis

The one-hour burst is not explained by the ticket. The code path that raises the message is present and unchanged. The message hides the cause by design: the cause is in the server log of the reporter's cluster, which the ticket does not include.

Missing piece for a verdict change: the `Caused by:` chain from one of the 2025-04-29 15:00 errors on the reporter's cluster, or a shard-response reproduction that shows the same cause. Without it, neither a close nor a PR has a concrete change to make.

## Verdict justification

keep-open meets the rubric: the attempts made are listed above (code path read, no repro), and the missing piece is named: the underlying exception in the reporter's log. The rubric's pr-needed needs a code-path line that causes the failure; the catch-all is the reporting path, not the cause.

## Limits

This record does not establish a cause. It does not show that the catch-all can hide a real bug, and it does not propose a change to the message. It does not post anything to JIRA or GitHub.
