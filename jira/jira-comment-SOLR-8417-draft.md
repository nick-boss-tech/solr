# Comment draft: SOLR-8417

- Status: DRAFT (not posted). Posting needs the owner's go.
- Shape: closure status note
- Addressee: [~ichattopadhyaya] — maintainer of the change that added the forwarding-loop limit.

[~ichattopadhyaya], current `HttpSolrCall` tracks internal remote forwards and throws `INVALID_STATE` with “No active replicas found” once the count exceeds the collection's replica count ([current guard|https://github.com/apache/solr/blob/13c693d805f6541f7cec684cd27d28eef7b3b614/solr/core/src/java/org/apache/solr/servlet/HttpSolrCall.java#L950-L955]). The change is [SOLR-13793|https://issues.apache.org/jira/browse/SOLR-13793], and both the [Solr 7.7.3|https://solr.apache.org/docs/7_7_3/changes/Changes.html] and [Solr 8.3.0|https://solr.apache.org/docs/8_3_0/changes/Changes.html] release notes say remote queries are limited to the number of replicas to prevent cascading calls. I did not reproduce the ticket's unreadable-core setup.

Does this support resolving SOLR-8417 as fixed in releases containing SOLR-13793?

 * Close as fixed in 7.7.3/8.3.0, where the forwarding limit shipped.
 * Keep it open only if all-unreadable cores still cause an unbounded loop on a later supported release; please provide that exact setup and log.

I recommend closing as fixed: the shipped guard directly stops the repeated forwarding behavior described here, although the specific unreadable-core configuration was not re-run.

🤖 _AI text above_ 🤖 _(reviewed by Nick Shanin)_
