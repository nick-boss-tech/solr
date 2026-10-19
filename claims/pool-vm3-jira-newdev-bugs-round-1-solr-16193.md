# Claim: pool-vm3-jira-newdev-bugs-round-1, slice SOLR-16193 (item 10 only)

- Claimant host: vm3, matching `hosts/vm3.md`.
- Status: CLAIMED. Slice: item 10 only, SOLR-16193. Other items are not claimed by vm3.
- Job or assignment: `assignments/pool-jira-newdev-bugs-round-1.md`, item 10.
- Base: apache/main at `c8ab71430f`.
- Capability tags used: implementation, gate.
- Started: 2026-10-19T20:00Z (UTC).

Heartbeat: 2026-10-19T20:00Z

Progress (vm3, 2026-10-19T20:10Z), code reading on `c8ab71430f`, no run yet:
- The ticket expects a non-200 when the suggester build fails. The reporter sees `suggest.build=true&suggest=true` return 200 while the log shows `IllegalStateException: suggester was not built`.
- That message is Lucene's, thrown by the lookup when no searcher was built. It is not in the Solr sources.
- Build path: `solr/core/src/java/org/apache/solr/spelling/suggest/SolrSuggester.java` `build` (line 170) only rethrows `AlreadyClosedException`; a build that yields no dictionary entries leaves the lookup unbuilt.
- Query path: `SuggestComponent.process` (around line 287) calls `getSuggestions` with no catch; `SolrSuggester.getSuggestions` (line 220) has no catch either. So the 200 needs a catch I have not found yet.
- Next step: reproduce with a suggester over an empty or unstored field in the existing suggest tests, to see which path returns 200.
