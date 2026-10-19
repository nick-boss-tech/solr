We looked at this against the current main branch (498a09aea97a). After a repeater fetches a new index, it sets its replication commit point from the fetched commit (solr/core/src/java/org/apache/solr/handler/IndexFetcher.java, around line 1049). That suggests a repeater configured with only replicateAfter=optimize should now serve the fetched commit, but no test covers that setup, so we have not confirmed it end to end.

We are leaving this open until a replication test confirms the optimize-only repeater case on main.
