# SOLR-18405 SLF4J lazy logging on hot paths: measurement

Ticket: SOLR-18405, "Adopt SLF4J 2.x lazy-argument-evaluation logging on hot paths" (Open, Minor, reporter Eric Pugh, updated 2026-08-26). Eric's comment: adding a lambda has a cost, so the change should be limited to expensive calls, which is a judgment call.
Pinned main: `e59ce3c97ddb`. Scope: `solr/core/src/java`.

## Measurement

1. `git grep -n -E '\blog\.(trace|debug)\('` on `solr/core/src/java`: 760 calls.
2. Nine of the 760 are commented-out lines. The guard and argument counts below include them; they do not change the reading.
3. Guarded: a call is counted as guarded if `isTraceEnabled(` or `isDebugEnabled(` appears within the 3 lines before it. 253 calls are guarded.
4. Unguarded: 507 calls. Of these, a regex for method-call or concatenation arguments matches 7 lines. Four of the seven are commented out (`ExportBuffers.java:107`, `:160`) or pass only a string literal (`SolrRequestInfo.java:99`, `:132`). `PeerSync.java:684` passes a field-accessor result (`sdoc.getField(ID)`). `TransactionLog.java:733` passes `fos.size()`.
5. `PeerSyncWithLeader.java:162` calls `msg()`, which reads the ZooKeeper base URL. It sits under `if (debug)` (line 161), a local flag, so it is not counted as unguarded.

## Reading

- 507 unguarded calls mostly pass identifiers, fields, or literals. Those are cheap to evaluate, so they do not need a lambda.
- No call in the seven-line match is clearly expensive on a hot path. The closest is `msg()` at `PeerSyncWithLeader.java:162`, and it is already behind a flag.
- The ticket's example (`someExpensiveMethod()` under a guard) and the 127-guard figure are consistent with the 253 guards measured here.
- A regex cannot tell cheap accessors from expensive work. A real audit would need to read each hot-path call, and the measurement above does not replace that.

## Verdict

No concrete offender found in `solr/core/src/java` by this measurement. The ticket's premise (unguarded expensive arguments on hot paths) has little support at this SHA. A rule to flag new unguarded method calls (the ticket's option 3) is possible but would need a false-positive review first. No code change is proposed.

## Not checked

- `solr/solrj`, `solr/modules`: not in scope of this measurement.
- Hot-path call sites in `SolrIndexSearcher` and the update processors: not read individually.
