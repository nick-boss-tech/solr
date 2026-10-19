We checked this against Solr main at commit 498a09aea9. The split of the v2 logging API by verb is done. The part that is not done is the all-nodes level change through v2.

What we checked:

- Reading logging levels is a GET and changing them is a PUT, each with its own permission.
- The v2 level-change endpoint still acts only on the node that receives it. The code has a TODO for the "nodes" parameter, which is waiting on the proxy work that has now landed (SOLR-16738).
- The Admin UI still uses the v1 handler to change the level on every node.

What this means: a v2 change to every live node is still needed. The proxy the work depends on is on main now, so the remaining change is the endpoint and the Admin UI wiring.

If you need this sooner: a test that sets the level on all nodes through the v2 endpoint and reads it back on each node would show the gap on main.
