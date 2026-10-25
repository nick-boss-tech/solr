# Comment draft: SOLR-7517

- Status: DRAFT (not posted). Posting needs the owner's go.
- Shape: closure status note
- Addressee: [~markrmiller] — ticket reporter and assignee; no comments have been added to the issue.

[~markrmiller], I checked the current `CorePropertiesLocator` write path: an `IOException` is logged and then propagated as `SolrException(BAD_REQUEST)`, rather than silently allowing the operation to continue ([current code|https://github.com/apache/solr/blob/13c693d805f6541f7cec684cd27d28eef7b3b614/solr/core/src/java/org/apache/solr/core/CorePropertiesLocator.java#L93-L109]). That log-and-throw change is in [SOLR-10719|https://issues.apache.org/jira/browse/SOLR-10719] and appears in the [Solr 6.6.1 release notes|https://solr.apache.org/docs/6_6_1/changes/Changes.html]. I did not run an unwritable-filesystem reproduction.

Does this evidence support closing SOLR-7517 as fixed for the silent-failure behavior?

 * Close as fixed in a shipped line containing SOLR-10719.
 * Keep it open only if a specific released version still silently continues after failing to write `core.properties`; please identify the version and failure log.

I recommend closing as fixed: the changed failure path is present in current main and the fix shipped in Solr 6.6.1. The earlier SOLR-4914 persistence refactor is not the change that added this exception.

🤖 _AI text above_ 🤖 _(reviewed by Nick Shanin)_
