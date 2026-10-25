# Comment draft: SOLR-6683

- Status: DRAFT (not posted). Posting needs the owner's go.
- Shape: staged ask (last activity is a committer's)
- Addressee: [~elyograg] — the ticket's final commenter, who asked for a concrete scenario.

[~elyograg], I re-read the original request and the reporter's test: after applying SOLR-6359 to 4.7, the reporter set `numRecordsToKeep=10000` and `maxNumLogsToKeep=100` and said it worked, subject to the stated transaction-log overlap condition. The [Solr 5.1.0|https://solr.apache.org/docs/5_1_0/changes/Changes.html] and [Solr 6.0.0|https://solr.apache.org/docs/6_0_0/changes/Changes.html] release notes list SOLR-6359 as allowing the number of UpdateLog records and logs kept to be configured; current PeerSync reads the configured record count ([UpdateLog setting|https://github.com/apache/solr/blob/13c693d805f6541f7cec684cd27d28eef7b3b614/solr/core/src/java/org/apache/solr/update/UpdateLog.java#L381-L407], [PeerSync use|https://github.com/apache/solr/blob/13c693d805f6541f7cec684cd27d28eef7b3b614/solr/core/src/java/org/apache/solr/cloud/RecoveryStrategy.java#L574-L580]).

Is that enough evidence to resolve the original request for configurable UpdateLog retention to support a larger PeerSync window?

 * Close the original request as fixed in 5.1.0, where the reporter's requested retention controls shipped.
 * Keep it open only if the original >100-document gap remains independently reproducible despite those controls on a supported release.
 * Track the later question about full snapshot recovery leaving the old transaction log separately if it remains a problem.

I recommend closing the original configurability request as fixed; the reporter confirmed the configuration worked, while the later log-cleanup question is distinct and should not keep this request open.

🤖 _AI text above_ 🤖 _(reviewed by Nick Shanin)_
