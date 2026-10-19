We checked this against Solr main at commit 498a09aea97 and could not confirm the failure yet.

What we checked:

- The Java HTTP/2 client named in the report has since been renamed (HttpJettySolrClient) and moved to Jetty 12. Its TLS setup is built from the configured SSL settings, but the connection code is different from the release in the report.

How we tried to reproduce it:

- Not attempted. The reproduction needs an HTTPS Solr server and a current client sending requests to it.

What this means:

- We cannot say from the report whether the broken pipe still happens with the current client. The client changed after the report's release, so the report may not apply to this code.

If you still see this:

- Please test with a current Solr release and send the client version, the Java version, the TLS settings, and the full exception. Please reopen with that information.
