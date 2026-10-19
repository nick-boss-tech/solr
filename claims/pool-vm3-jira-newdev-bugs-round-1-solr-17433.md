# Claim: pool-vm3-jira-newdev-bugs-round-1, slice SOLR-17433 (item 13 only)

- Claimant host: vm3, matching `hosts/vm3.md`.
- Status: DONE. No branch (not reproduced by code reading). Slice: item 13 only, SOLR-17433. Other items are not claimed by vm3 from this session.
- Job or assignment: `assignments/pool-jira-newdev-bugs-round-1.md`, item 13.
- Capability tags used: implementation, gate.
- Started: 2026-10-19T21:15:00Z (UTC).

Heartbeat: 2026-10-19T21:15:00Z

## Result (vm3, 2026-10-19)

- Base: `c8ab71430f`. No branch pushed.
- The ticket's 60-second total timeout comes from `SolrClientCache` (`MIN_TIMEOUT = 60000`) in the 9.4 era. On the base, `SolrClientCache.newHttpSolrClientBuilder` (`solr/solrj-streaming/src/java/org/apache/solr/client/solrj/io/SolrClientCache.java`) sets the connection timeout to 60 s and the idle timeout to `max(60 s, builder default)`, which is 600 s, since `SolrHttpConstants.DEFAULT_SO_TIMEOUT` is 600000.
- The builder's `getRequestTimeoutMillis` falls back to the idle timeout (`solr/solrj/src/java/org/apache/solr/client/solrj/impl/HttpSolrClient.java` lines 597 to 602). So the Jetty total request timeout (`req.timeout`) is 600 s on the base, not 60 s.
- Not run: no live streaming test of longer than 60 s. The verdict is by code reading only.
- Verdict: not reproduced by code reading. No branch.

Heartbeat: 2026-10-19T21:25:00Z

DONE: 2026-10-19. Deliverables: this claim file (result section), the SOLR-17433 section in `reports/2026-10-19-jira-newdev/A-bugs-round-1.md`. No branch.

