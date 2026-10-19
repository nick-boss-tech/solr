# Claim: pool-jira-newdev-small-round-1, item 2 (SOLR-17727)

- Claimant host: vm4, matching a file in `hosts/`.
- Status: DONE (stopped, no branch). Item 2 of `assignments/pool-jira-newdev-small-round-1.md`: the Server header should return Solr's version, branch `solr-17727-server-header` from apache/main c8ab71430f.
- Job or assignment: `assignments/pool-jira-newdev-small-round-1.md`, item 2.
- Slice: item 2 only. Item 3 (SOLR-18123) recorded as stopped in the deliverable.
- Capability tags used: implementation, gate.
- Started: 2026-10-19T18:59:12Z (UTC).

Heartbeat: 2026-10-19T18:59:12Z

Result: $NOW: stopped. The Server header is disabled on purpose in solr/server/etc/jetty.xml (solr.jetty.send.server.version defaults to false); exposing Solr's version needs a server-side customizer that could not be verified without a packaged start. No branch. Section in the round deliverable.

DONE: $NOW. Deliverables: the item 2 section of reports/2026-10-19-jira-newdev/B-small-round-1.md.
