We checked Solr main at commit 498a09aea9 (October 7, 2026). The Solr Jetty client creates its HTTP connection with Jetty's default buffer pool and does not set a pool or direct buffer limit of its own. The growth in direct buffers described in this report would have to come from that pool.

What we checked:

- How the Jetty-based client builds its HTTP connection and buffer pool.
- Whether Solr sets any buffer pool or direct buffer option anywhere in the client code.

How we tried to reproduce it:

- We did not run a reproduction for this check. Reproducing it needs a sustained update load and a count of direct buffers over time.

What this means:

- The code shows where the buffers come from, but not whether they are released. That depends on the Jetty pool's behavior and its configured limits.

If you still see this:

- Please send the Solr version, the Jetty version it ships with, and a count of direct buffers (for example from the JVM buffer pool MXBeans) over the course of a load run with the update rate used. The count over time will show whether the growth is bounded.
