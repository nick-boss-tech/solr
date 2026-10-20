# Skip record: SOLR-12300

- Ticket: SOLR-12300, Unpopulated SolrDocument using Custom DocTransformer. JIRA read in this pass: Open, resolution none, fix versions none, 2 comments (2018-07-08, a reply that could not reproduce on 7.4 and names getExtraRequestFields as the workaround).
- Verdict: keep-open.
- Draft branch: solr-12300-draft-open.
- Pinned head: fork origin/main 498a09aea97aac86c2cc7ac114113b946d249942 (checked at this head for this record).
- Source: the queue entry (audit verdict confirmed-skip, audit date 2026-10-19) is background only; the reading below is fresh at the pin.

## Ticket reading

The ticket: a custom DocTransformer applied to three or more fields in fl returns only the id field; with two fields the documents are returned in full. The reporter says Solr 6.4.2 did not show it. The 2018 reply could not reproduce on 7.4, says the custom transformer shadows the underlying fields, and says that implementing getExtraRequestFields to return the field names makes them come back. The description was read through the Jira REST record.

## Code reading

- solr/core/src/java/org/apache/solr/search/SolrReturnFields.java:145-150: a transformer passed to the constructor adds its getExtraRequestFields to the requested fields, and only then (no wildcard request).
- SolrReturnFields.java:187-193 (deferred rename augmenters) and :377-383 (transformer created from the fl parse): when the request is not a wildcard, the transformer's extra fields are added to fields; otherwise they are not.
- solr/core/src/java/org/apache/solr/search/SolrDocumentFetcher.java:778-800 (RetrieveFieldsOptimizer, constructed at :339-341): the fetcher chooses how to load the stored or docValues fields for a document from the returned field set; the choice depends on which fields are requested.

So the requested set, and therefore what the fetcher loads, depends on getExtraRequestFields. A transformer that does not return its fields leaves them out of the fetch set, which matches the id-only result for the transformer case in the ticket. The 2-versus-3-field threshold the ticket reports is not explained by these lines; it would need the optimizer's branch taken for each field count, which was not traced.

## History

- No commit at the pin names SOLR-12300 (git log --grep at the pin: none).
- The file history of SolrReturnFields.java and SolrDocumentFetcher.java was not traced in this pass.

## Reproduction attempts

Not run. A reproduction needs the blank transformer from the ticket (a DocTransformer that returns no extra fields) at the pin, a select with two and then three transformed fields plus id, and the returned fields. Not built or run in this pass.

## Gap analysis

The code shows why the transformer's own fields can be left out of the fetch when getExtraRequestFields is empty, and that the documented workaround adds them. The count-dependent behavior the ticket reports (two fields work, three do not) is not explained by reading, and the 2018 reply could not reproduce it on 7.4.

## Verdict justification

keep-open: the mechanism for the shadowing is present, but the count-dependent symptom and whether the behavior is intended (the workaround is in the transformer API) are unresolved. close-cannot-reproduce needs a run attempt, and none was made. close-fixed and close-obsolete are not supported: no fix or removed code was found.

Missing for a closing verdict: a run at the pin with the blank transformer and two and three transformed fields, recording the returned fields, and a maintainer decision on whether the shadowing without getExtraRequestFields is the intended contract (in which case the ticket can close as answered by the API).

## Limits

This record does not run the query, does not trace the fetch optimizer branches, and does not post anything to JIRA or GitHub.
