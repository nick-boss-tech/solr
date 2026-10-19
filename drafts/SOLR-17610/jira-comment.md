We checked Solr main at commit 498a09aea9 (October 7, 2026). We could not find code in Solr that looks up the client's host name on each request, which would explain a fixed 10-second delay. The security policy shipped with the default startup allows name lookups for any host, so the policy alone does not block them.

What we checked:

- Solr code for reverse DNS lookups on the request path, and for the remote host value that the request log and authorization code can read.
- The default security policy and the startup script settings for the security manager.

How we tried to reproduce it:

- We did not run a reproduction for this check. Reproducing it needs CentOS 7 with OpenJDK 11 and two name servers that give different answers for the client.

What this means:

- The cause looks like it is in the Java runtime or the name service setup on that host, not in Solr's request handling. We have not confirmed that.

If you still see this:

- Please take a thread dump (for example with jstack) during one of the delayed requests and attach it, along with the contents of /etc/nsswitch.conf and the name server setup on the Solr host. The blocked frame in the dump would show where the wait happens.
