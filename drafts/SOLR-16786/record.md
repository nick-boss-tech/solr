# Skip record: SOLR-16786

- Ticket: SOLR-16786, Zookeeper connection timeout even after Solr trying to connect. Connection establishes after restarting the Solr service. JIRA status not recorded in the C reading. JIRA was not re-read for this record.
- Verdict: keep-open.
- Draft branch: solr-16786-draft-open.
- Pinned head: fork origin/main 498a09aea97aac86c2cc7ac114113b946d249942. Upstream sha not cited.
- Checked by: vm2 (pool Linux host), 2026-10-19 UTC, from the C reading (reports/2026-10-18-skip-repro/C4, slice 4, pinned head unchanged).

## Ticket reading

The summary as recorded in the queue: Zookeeper connection timeout even after Solr trying to connect. Connection establishes after restarting the Solr service.

## Code reading

ZK session-expiry re-registration is at solr/core/src/java/org/apache/solr/cloud/ZkController.java:417 (onDisconnect), :450-455 (onReconnect, returns early unless the session expired) and :503 (createEphemeralLiveNode). The ticket's repeated timeouts without a confirmed expiry are not shown in code or logs, so the code alone cannot decide this.

## History

- None cited in the C reading.

## Reproduction attempts

Not attempted. The C scope ran no builds or tests.

## Gap analysis

The C reading gives the missing piece; see the evidence below.

## Verdict justification

keep-open: the code reading does not settle the question; the C reading names the piece that would settle it.

Missing for this verdict: The missing piece named in the C reading (see the evidence below).

## Limits

This record does not establish a reproduction. It does not post anything to JIRA or GitHub. Commit linkage is by commit title or code path where the subject does not cite the ticket.
