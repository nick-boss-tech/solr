We checked this against Solr main at commit 498a09aea97 and could not confirm the data loss yet.

What we checked:

- The replica type logic for non-leader TLOG replicas is in the cloud controller. We have not yet traced the path from leader selection to the full replication the report describes.
- No commit on main names this ticket.

How we tried to reproduce it:

- Not attempted. It needs a cluster with PULL replicas holding documents and a new TLOG replica added after the others are removed.

What this means:

- The report describes a possible loss of documents on PULL replicas. Whether a new TLOG replica should wait for, or copy from, the PULL replicas before it leads is a design question for the Solr developers.

If you still see this:

- Please send the Solr version, the replica types and counts before and after the change, and the document counts on each replica. Please reopen with that information.
