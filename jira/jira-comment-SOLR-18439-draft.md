[~epugh], I traced two current-main v2 routes against the error convention in this ticket.

 * {{GET /api/cores/{coreName}/replication/files?generation=...}} returns a {{FileListResponse}} with {{status=ERROR}} and {{message="invalid index generation"}} for a missing generation. A file-list {{IOException}} returns {{status=ERROR}}, a message, and {{exception}}. [Source|https://github.com/apache/solr/blob/13c693d805f6541f7cec684cd27d28eef7b3b614/solr/core/src/java/org/apache/solr/handler/admin/api/ReplicationAPIBase.java#L142-L164]
 * {{POST /api/cores/{coreName}/replication/backups}} catches non-{{SolrException}} snapshot failures and returns {{status=ERROR}}, an {{Error encountered while creating a snapshot}} message, and {{exception}}; {{SolrException}} is rethrown. [Source|https://github.com/apache/solr/blob/13c693d805f6541f7cec684cd27d28eef7b3b614/solr/core/src/java/org/apache/solr/handler/admin/api/SnapshotBackupAPI.java#L79-L97]

These paths return response objects rather than reaching the mapper that sets an HTTP error status and {{error.code}}/{{error.msg}}. This confirms the mismatch at source/control-flow level; I did not run a server request. The file-list helper is also used by v1, whose current test expects the legacy {{status=ERROR}}/{{message}} body. Jan's latest note on [PR #4899|https://github.com/apache/solr/pull/4899#issuecomment-5822703480] raises that compatibility issue and suggests 409 rather than 404 for a missing generation.

For a missing or expired generation, which behavior do you want: 404 or 409 for v2, and should the v1 response remain unchanged?

🤖 _AI text above_ 🤖 _(reviewed by Nick Shanin)_
