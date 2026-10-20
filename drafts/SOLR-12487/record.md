# Skip record: SOLR-12487

- Ticket: SOLR-12487, Possible corruption in tlog after restart of Solr. JIRA read in this pass: Open, resolution none, fix versions none, 2 comments (2018-06-14, from the reporter and a committer; no fix noted).
- Verdict: keep-open.
- Draft branch: solr-12487-draft-open.
- Pinned head: fork origin/main 498a09aea97aac86c2cc7ac114113b946d249942 (checked at this head for this record).
- Source: the queue entry (round audit-1, 2026-10-04, audit verdict confirmed-skip) is background only; the reading below is fresh at the pin.

## Ticket reading

The ticket: after a restart on Solr 6.5.1, one shard fails to create its core during tlog replay. The log shows "Unexpected log entry or corrupt log" warnings, then "Unable to create core", with the root cause a java.lang.StackOverflowError inside JavaBinCodec.readMap while reading the tlog. The reporter's reading is that the tlog is corrupt (for example a missing end tag). The shard then stays down with "Error Instantiating Update Handler". The full description was read from the Jira REST issue record; the evidence tool output is truncated at 1200 characters.

## Code reading

- solr/core/src/java/org/apache/solr/update/UpdateLog.java:1604-1607 (LogReplayer, reverse read path): catches ClassCastException only and logs "Unexpected log entry or corrupt log"; any other Throwable, including an Error such as StackOverflowError, is not caught here.
- solr/core/src/java/org/apache/solr/update/UpdateLog.java:1914-1919 (reverse read of the old tlog): catches ClassCastException, then Exception; no catch for Error.
- solr/core/src/java/org/apache/solr/update/UpdateLog.java:2347-2353 (REPLAY_ERR path): catches ClassCastException, then Exception; no catch for Error. A StackOverflowError thrown while the codec reads an entry propagates out of the replay.
- solr/core/src/java/org/apache/solr/update/TransactionLog.java:578, 756, 933: the tlog entries are read with codec.readVal(...). No catch for StackOverflowError appears in TransactionLog.java or UpdateLog.java (git grep at the pin: no "StackOverflow" hit under solr/core/src/java or solr/solrj/src/java; no catch of Throwable or Error in those two files).
- solr/core/src/java/org/apache/solr/core/CoreContainer.java:1752-1763 (create failure path): wraps the failure as SolrException "Unable to create core [...]" with the original cause, after closing the partial core.

So the failure mechanism in the ticket (an Error from the codec escaping tlog replay and failing core creation) is still present in the code at the pin.

## History

- No commit at the pin names SOLR-12487 (git log --grep at the pin: none).
- The file history of UpdateLog.java at the pin ends with 090f5b995c (dependency update), 554a731c60 (formatting), and 1af33ac699 (SOLR-18164). None is a fix for this failure; their diffs were not traced in this pass.

## Reproduction attempts

Not run. A reproduction needs a tlog truncated or corrupted so that JavaBinCodec recurses deeply enough to overflow the stack, then a core reload at the pin, and recording whether the core fails to load. That was not built or run in this pass.

## Gap analysis

The code path the ticket describes is present at the pin: a StackOverflowError from reading a tlog entry is not caught by the replay logic, so a corrupt tlog can still prevent core creation. Whether the overflow is reachable from a corrupt file of the kind the reporter describes is not established without a run. No fix commit or removed-code path closes the ticket.

## Verdict justification

keep-open: the failure path is present at the pin, so close-fixed and close-obsolete are not supported. close-cannot-reproduce needs a run attempt, and none was made.

Missing for a closing verdict: a reproduction at the pin with a corrupt tlog that shows whether core load fails (and, if so, whether the replay should skip the entry instead), or a fix commit that makes the replay tolerate the Error.

## Limits

This record does not run a corrupt-tlog reproduction, does not trace the file-history diffs, and does not post anything to JIRA or GitHub.
