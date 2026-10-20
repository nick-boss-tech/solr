# Skip record: SOLR-9369

- Ticket: SOLR-9369, "SolrCloud should not compare commitTimeMSec to see if replicas are in sync". JIRA status read 2026-10-20T14:41:15Z (UTC): Open, resolution None, updated 2019-06-08.
- Verdict: keep-open (kept; re-read at the pinned head). The in-sync premise is not present in the fetcher, but no fix commit is named, so the closeout is not ready.
- Draft branch: solr-9369-draft-open.
- Pinned head: 498a09aea97aac86c2cc7ac114113b946d249942 (fork origin/main).
- Checked by: vm2, 2026-10-20T14:41:15Z (UTC).

## Ticket reading

The replication code decides that two replicas are in sync by comparing commit timestamps (commitTimeMSec). The reporter says that is wrong for SolrCloud, where replicas commit at different times. A 2016 comment describes a case where it would save a short replication, and a 2016 comment points to SOLR-9446.

## Code reading (pinned head)

- `git grep -n -i "commitTimeMSec\|commitTime" 498a09aea97aac86c2cc7ac114113b946d249942 -- solr/core/src/java solr/solrj/src/java`: the commit timestamp is read in `IndexDeletionPolicyWrapper.java:478-482` (`getCommitTimestamp`) and used by `SolrDeletionPolicy.java:169` for retention, and logged in `IndexFetcher.java:524` ("Follower's version").
- The in-sync decision in the fetcher is by file size and checksum (`IndexFetcher.isIndexStale` at line 1306, and `compareFile`), not by commit time. So the premise the ticket attacks is not the sync check at this revision.
- The commit timestamp still has retention and logging uses.

## History

- evidence.py at the pinned head: no commits naming SOLR-9369.
- The commit that removed the timestamp comparison from the sync check is not identified here.

## Reproduction attempts

Not run. A run would show whether two SolrCloud replicas with different commit times are considered in sync by checksum.

## Gap analysis

The sync check no longer uses commit time, which is what the ticket asks for. A closeout needs the commit that made that change, and the remaining retention use is not the subject of the ticket. Whether the ticket is fixed or obsolete depends on that commit.

## Verdict justification

keep-open meets the rubric: attempts made are the grep and the fetcher reading; the missing piece is the commit that removed the comparison (for close-fixed) or a run. close-fixed is not supported without a named commit. close-obsolete is not supported: the timestamp is still used for retention.

## Limits

This record does not identify the change that removed the comparison. It does not check the SOLR-9446 history.
