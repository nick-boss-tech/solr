# Skip record: SOLR-15727

- Ticket: SOLR-15727, "Split-brain can occur with heavy ingest and violent shutdown". JIRA status Open, resolution none, fix versions none, created 2021-10-27, updated 2022-07-26. Checked 2026-10-19 UTC.
- Verdict: keep-open.
- Draft branch: solr-15727-draft-open.
- Pinned head: fork origin/main 498a09aea97aac86c2cc7ac114113b946d249942. No upstream sha cited.
- Checked by: vm2 (pool Linux host), 2026-10-19 UTC, for the remediation redo of this ticket (claim claims/skip-SOLR-15727-vm2.md).

## Ticket reading

The reporter runs a SolrCloud collection with 3 shards and 2 replicas per shard. Under heavy ingest, the reporter kills one node (docker kill), lets it come back, stops ingest, runs a hard commit, and then sees the hit count change from query to query. The expected result is the same count on every query. Six comments: maintainers ask whether openSearcher is set on hard commits; one commenter asks about the default and whether a collection reload helps; one reply notes that the transaction log keeps only a small number of documents by default, so recovery may not replay all of them.

## Code reading

- solr/core/src/java/org/apache/solr/update/CommitTracker.java:73 and :95 (origin/main 498a09aea97): openSearcher is a field set from the constructor. The default for openSearcher on hard commits is true, per the comment and the config path (not re-read here), so a hard commit opens a new searcher.
- solr/core/src/java/org/apache/solr/update/UpdateLog.java:383: numRecordsToKeep defaults to 100 (objToInt(info.initArgs.get("numRecordsToKeep"), 100)). The transaction log keeps 100 records by default, so an unclean shutdown after heavy ingest may leave more updates than the log can replay.
- Not read in this pass: the replica recovery path (PeerSync and the replay on recovery) that decides which updates a recovering replica takes from its leader, which is where split-brain between replicas would come from.

Commands at the pinned head:
- git grep -n 'numRecordsToKeep' origin/main -- solr/core/src/java/org/apache/solr/update/UpdateLog.java: lines 226, 339, 383, 400.
- git grep -n 'openSearcher' origin/main -- solr/core/src/java/org/apache/solr/update/CommitTracker.java: lines 73, 84, 95, 100.

## History

- Commits on origin/main naming SOLR-15727: none.
- Recovery code history (PeerSync, replay): not traced in this pass.

## Reproduction attempts

Not attempted. The symptom needs a multi-node SolrCloud run with heavy ingest, a kill of one node, and queries after recovery. That is a cluster test with timing, which was not set up in this pass.

## Gap analysis

The transaction-log default (100 records) and the hard-commit default are both real and both relevant to the report, but neither proves the split-brain. The missing link is the recovery path: whether a replica that comes back after an unclean shutdown ends with the same documents as its peers, and if not, which update it misses. That path was not read.

## Verdict justification

keep-open meets the rubric: the pinned head sha is cited, the Jira status and date are read in this pass, code paths are cited with file and line, and the attempts and the missing piece are listed. The missing pieces are (1) the replica recovery path (PeerSync and replay) read at the pinned head, and (2) a multi-node reproduction with the kill and ingest steps, or the logs from the reporter's run. A close verdict needs a fix or a long clean run; a pr-needed verdict needs the failing recovery step.

## Limits

This record does not reproduce the split-brain. It does not read the recovery path. It does not confirm the openSearcher default from the config loader.
