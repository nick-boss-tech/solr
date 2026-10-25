# Comment draft: SOLR-5850

- Status: DRAFT (not posted). Posting needs the owner's go.
- Shape: stale-check status note
- Addressee: [~thelabdude] — the ticket assignee and a maintainer who participated in the original patch discussion.

[~thelabdude], the ticket names `ConcurrentUpdateSolrServer`, which was removed in SOLR-6895. However, current main still has a related bounded-queue failure path: `ConcurrentUpdateJettySolrClient` requeues an update with `queue.add(upd)` when its destination or parameters change, and `ConcurrentUpdateBaseSolrClient` throws `IllegalStateException("Queue is full")` when that queue is full ([requeue path|https://github.com/apache/solr/blob/13c693d805f6541f7cec684cd27d28eef7b3b614/solr/solrj-jetty/src/java/org/apache/solr/client/solrj/jetty/ConcurrentUpdateJettySolrClient.java#L79-L91], [queue behavior|https://github.com/apache/solr/blob/13c693d805f6541f7cec684cd27d28eef7b3b614/solr/solrj/src/java/org/apache/solr/client/solrj/impl/ConcurrentUpdateBaseSolrClient.java#L156-L163]). I have not shown that this is the same path as the old HTTP-client report, and I did not reproduce it with a current client.

Is the reported failure still reproducible with a current SolrJ client, and if so can you provide the exact client/version, queue and thread settings, and a minimal failing test or stack trace?

 * Keep it open for a current-client reproduction if the queue-full failure still occurs.
 * Close as obsolete only if the old failure no longer applies to supported clients.

I recommend keeping it open pending that version-specific check; removal of the named class alone does not establish that the queue-full symptom is gone. SOLR-7571 is linked as related, not as a duplicate.

🤖 _AI text above_ 🤖 _(reviewed by Nick Shanin)_
