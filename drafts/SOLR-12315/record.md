# Skip record: SOLR-12315

- Ticket: SOLR-12315, the number of docs in each group depends on rows. JIRA read in this pass: Open, resolution none, fix versions none, 2 comments (2018-05-05 reporter's debugging note; 2019-08-15 a reply that could not reproduce the problem and asked for the solr.home files).
- Verdict: keep-open.
- Draft branch: solr-12315-draft-open.
- Pinned head: fork origin/main 498a09aea97aac86c2cc7ac114113b946d249942 (checked at this head for this record).
- Source: the queue entry (round audit-1, 2018/2026 audit verdict confirmed-skip) is background only; the reading below is fresh at the pin.

## Ticket reading

The ticket: on SolrCloud 7.1.0 (3 nodes, 3 shards, 2 replicas), a grouped query returns the right number of documents per group only when rows is greater than 5; at smaller rows the groups come back short. The reporter's comment says that with rows=2 the coordinator sends no request to one shard in the EXECUTE_QUERY and GET_FIELDS stages. The 2019 reply says the problem did not reproduce for that commenter and asks for the reporter's configuration. The description was read in full through the Jira REST record.

## Code reading

- solr/core/src/java/org/apache/solr/handler/component/QueryComponent.java:836-846: the coordinator computes the per-shard rows (shards.rows if set, otherwise the request rows plus offset, with rows <= 0 left unchanged) and sets it on the shard request.
- QueryComponent.java:1729-1745: the shard-side collection limit for grouped requests (group.format=simple or group.main) is the group sort count plus offset (docsToCollect), with Integer.MAX_VALUE when the count is negative.
- QueryComponent.java:276-296: the group sort spec is built with weightSortSpec and set on the grouping spec.

These limits are the inputs to the per-shard group result; how they combine with the coordinator's merge across shards, and why a shard is skipped at low rows, was not traced in this pass. The reporter's observation that a shard receives no request in two stages is a claim about the coordinator's request planning, not yet checked against the code.

## History

- No commit at the pin names SOLR-12315 (git log --grep at the pin: none).
- The file history of QueryComponent.java was not traced in this pass.

## Reproduction attempts

Not run. The 2019 reply's unit test is not in the record and was not reproduced here. A reproduction needs a three-shard grouped query at the pin with rows 1 to 10 and a count of documents per group compared with the expected count, plus the shard request log for the stages the reporter names.

## Gap analysis

The coordinator's shard row and collection limits are present as the ticket describes the inputs. What is missing is the merge and request-planning trace, and a run showing whether a short group count is produced at the pin. Without that, neither a close verdict nor a defect confirmation is supported.

## Verdict justification

keep-open: a reproduction is not in the record, and the reporter's explanation (a shard skipped in two stages) is unverified against the code. close-cannot-reproduce needs a run attempt, and none was made. close-fixed and close-obsolete are not supported: no fix or removed code was found.

Missing for a closing verdict: a grouped-query run at the pin with rows from 1 to 10 on a three-shard collection, recording the group counts and the shard requests per stage, and the reporter's configuration (the 2019 reply asked for it).

## Limits

This record does not run the grouped query, does not trace the coordinator merge in full, and does not post anything to JIRA or GitHub.
