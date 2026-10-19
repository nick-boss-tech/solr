We checked this against Solr main at commit 498a09aea97 and could not confirm the cause yet.

What we checked:

- A hard commit opens a new searcher by default. The sample configuration sets it differently, and the report's configuration is not in the text we have.
- The transaction log keeps 100 records by default. After heavy ingest and an unclean shutdown, a restarted node may not have enough log records to replay every missing update.

How we tried to reproduce it:

- Not attempted. The symptom needs a multi-node cluster with heavy ingest and a node killed during it.

What this means:

- Both defaults are relevant to the report, but the step that decides which updates a recovered replica gets from its peers has not been checked yet.

If you still see this:

- Please send the Solr version, the solrconfig.xml update handler and autoCommit settings, and the replica states before and after recovery. Please reopen with that information.
