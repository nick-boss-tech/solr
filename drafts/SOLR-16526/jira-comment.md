We checked Solr main at commit 498a09aea9 (October 7, 2026). The error comes from the HTTP client library when a node's advertised host contains an underscore. Solr passes the host to that library without checking it, so the first request between nodes fails.

What we checked:

- How Solr chooses and uses the advertised host, and whether it checks the value.
- The Docker deployment pages in the Solr reference guide, which use host names without underscores.

How we tried to reproduce it:

- We did not run a reproduction for this check.

What this means:

- A host name with an underscore cannot be used for the advertised host. Using a host name without underscores, or setting the advertised host to an IP address, should avoid the error. We have not tested that on this version.

If you still see this:

- Please check the value of SOLR_HOST and the host name the node reports in cluster status. If the host name contains an underscore, the workaround above applies. If it does not, send the Solr version and the full stack trace.
