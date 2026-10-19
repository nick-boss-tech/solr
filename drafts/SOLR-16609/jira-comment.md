The Solr reference guide covers this. The three properties in your report (updateHandler.autoCommit.maxTime, updateHandler.autoCommit.openSearcher, and updateHandler.autoSoftCommit.maxTime) are listed as settable properties in the Config API page, and they are set on an existing collection with a set-property request: {"set-property": {"updateHandler.autoCommit.maxTime": 60000}}. Page: https://solr.apache.org/guide/solr/latest/configuration-guide/config-api.html

What we checked:

- The Config API page at Solr main commit 498a09aea9 (October 7, 2026), which lists the common properties and shows the set-property request form.

How we tried to reproduce it:

- Not needed for this answer, because the reference guide describes this use.

What this means:

- These properties are changed after the collection exists, through the Config API or bin/solr config, not as parameters on the create call.

If you still see this:

- If the 400 error came from a create call that sets these properties, please send the request and the response body. The guide does not describe setting them at create time, so we would check that case separately.
