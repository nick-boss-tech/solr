We checked this against Solr main at commit 498a09aea97 and could not confirm the problem yet.

What we checked:

- The package API endpoints are now declared under /api/cluster/package. The code the report names, which rewrote the /solr prefix in package URLs, is not in the file at the pinned commit.

How we tried to reproduce it:

- Not attempted. The check needs a running node whose host name starts with "solr", and a plugin registration through the package API.

What this means:

- We have not yet confirmed whether registration still fails for host names that start with "solr" on current Solr.

If you still see this:

- Please send the Solr version, the host name pattern, the URL you use for the package API, and the error returned. Please reopen with that information.
