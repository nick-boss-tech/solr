# Skip record: SOLR-15279

- Ticket: SOLR-15279, .SolrClientNodeStateProvider Error on getting remote info, trying again. JIRA read 2026-10-19 (this pass, evidence tool): status Open, resolution None, created 2021-03-23, updated 2021-08-06, no fix versions, no links, one comment (2021-08-06) from another user reporting the same message on 8.4.0.
- Verdict: keep-open.
- Draft branch: solr-15279-draft-open (the existing keep-open branch; this record replaces its draft files).
- Pinned head: fork origin/main 498a09aea97aac86c2cc7ac114113b946d249942. Code read and grep run at this head (detached worktree).
- Source: remediation redo of batch 03, item 18, under assignments/pool-aaa-skip-verdict-remediation-03.md. The earlier record (vm2, from the C reading) cited a code path that is confirmed here; the reading is not taken as the check.

## Ticket reading

Three-node SolrCloud across AWS zones logs, many times a day, "Error on getting remote info, trying again" at INFO with an IOException ("IOException occurred when talking to server at: https://...:8983/solr"), and later a WARN. The reporter asks about the frequent log entries. A second user reports the same on 8.4.0.

## Grep and code reading at the pinned head

- `git grep -n "Error on getting remote info" 498a09aea97aac86c2cc7ac114113b946d249942 -- '*.java'` gives one match: solr/solrj-zookeeper/src/java/org/apache/solr/client/solrj/impl/SolrClientNodeStateProvider.java:290, `log.info("Error on getting remote info, trying again: ", e);`. So the message is still logged at INFO on the retry path, with the exception attached.
- `git grep -n "class SolrClientNodeStateProvider" 498a09aea97aac86c2cc7ac114113b946d249942 -- '*.java'` gives solr/solrj-zookeeper/src/java/org/apache/solr/client/solrj/impl/SolrClientNodeStateProvider.java:53, the class declaration.
- `git log --format='%h %ad %s' --date=short -S"Error on getting remote info" 498a09aea97aac86c2cc7ac114113b946d249942 -- solr` gives 58f9c79c6d (2020-04-20, "SOLR-14412 zkRun+https") and 75b1831967 (2018-11-29, "SOLR-12801"), the commits that introduced or changed the message. The message is older than the 8.8 release the reporter ran.

## History

- The message dates from 2018 and was in code the 2020 HTTPS change touched. No commit names SOLR-15279.

## Reproduction attempts

Not run. The message needs a remote node that is unreachable or slow to answer during a metrics-history poll. Reproducing that needs a multi-node cluster with a network fault between nodes, which this pass did not set up.

## Gap analysis

The log line is in the code at the pin, so the noise the reporter describes is still possible. What the ticket does not settle is whether the message is a defect or a log-level choice: the exception is logged at INFO on each retry, and the reporter's underlying network cause (zone networking or a host name) is not known. A change to the log level or the retry logging is a design choice, not a verified defect.

## Verdict justification

keep-open: the attempts made are the grep and code reads above at the pinned head and the Jira read. The missing piece is the reporter's underlying cause (which remote node fails, and why), and a maintainer decision on the log level or on whether the retry should be logged at all. No close verdict is supported, since the message is still present. No PR verdict is supported without showing the message is a defect rather than expected retry noise.

Missing for a closing or PR verdict: (1) the reporter's network cause (the failing host and error), or a reproduction; (2) a decision on the log level.

## Limits

This record does not reproduce the retry. It does not post anything to JIRA or GitHub. The draft comment is a draft until someone posts it by hand.
