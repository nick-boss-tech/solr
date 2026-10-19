We checked this against Solr main at commit 498a09aea97 and could not confirm the cause yet.

What we checked:

- The packaged Jetty configuration (server/etc/jetty.xml) has the block that enables handling of X-Forwarded- headers commented out. Without it, Jetty does not use the scheme sent by the proxy when it builds redirect URLs.
- Solr's own servlet code does not build the redirect from / to /solr/. The admin UI's login flow uses the scheme of the page the browser is on.

How we tried to reproduce it:

- Not attempted yet. Reproducing it needs the packaged server behind a proxy that sends X-Forwarded-Proto: https.

What this means:

- The likely cause is the commented-out forwarded-headers setting in the default Jetty configuration. We have not confirmed that on a running server.

If you still see this:

- A reproduction on a current release, with the Solr version, the proxy configuration, and the response Location header for a request to / over https, would let us confirm the cause. Please reopen with that information.
