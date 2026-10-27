# Jira comment draft: SOLR-18553

Status: POSTED 2026-10-27 as Jira comment 18132691. Shape: staged ask, evidence first. Addressee: Eric Pugh (reporter). Prepared by vm6, 2026-10-27, from `reports/solr-18553-plurality-audit-round-1.md` (pinned main `e59ce3c97d`). Posting needs Nick Shanin's explicit go. Revised 2026-10-27 after reading the PR 5064 comment linked from the ticket description.

---

I went through the v2 endpoint interfaces and the v1 handler parameters at apache/solr main {{e59ce3c97d}} for plurality and naming consistency. The v1 handlers were included because the "detail" family is spelled differently there too.

Plurality and naming:

 * The same role, "include more, toggled by a parameter", is named three ways. v2 uses {{detailed}} on collection listing ([ListCollectionsApi.java:37|https://github.com/apache/solr/blob/e59ce3c97ddbe030eafadf0623fdcd80c4529c31/solr/api/src/java/org/apache/solr/client/api/endpoint/ListCollectionsApi.java#L37]). v2 uses the {{xxxInfo}} suffix elsewhere: {{coreInfo}}, {{fieldInfo}}, {{sizeInfo}}, {{indexInfo}} ([SegmentsApi.java:57|https://github.com/apache/solr/blob/e59ce3c97ddbe030eafadf0623fdcd80c4529c31/solr/api/src/java/org/apache/solr/client/api/endpoint/SegmentsApi.java#L57], [CoreApis.java:44|https://github.com/apache/solr/blob/e59ce3c97ddbe030eafadf0623fdcd80c4529c31/solr/api/src/java/org/apache/solr/client/api/endpoint/CoreApis.java#L44]). v1 uses {{detail}} on the ZooKeeper info handler ([ZookeeperInfoHandler.java:76|https://github.com/apache/solr/blob/e59ce3c97ddbe030eafadf0623fdcd80c4529c31/solr/core/src/java/org/apache/solr/handler/admin/ZookeeperInfoHandler.java#L76]) and {{command=details}} on replication ([ReplicationHandler.java:1713|https://github.com/apache/solr/blob/e59ce3c97ddbe030eafadf0623fdcd80c4529c31/solr/core/src/java/org/apache/solr/handler/ReplicationHandler.java#L1713]).
 * The same concept has two path names: {{/schema-designer/configs}} ([SchemaDesignerApi.java:121|https://github.com/apache/solr/blob/e59ce3c97ddbe030eafadf0623fdcd80c4529c31/solr/api/src/java/org/apache/solr/client/api/endpoint/SchemaDesignerApi.java#L121]) and {{/configsets}} ([ConfigsetsApi.java:46|https://github.com/apache/solr/blob/e59ce3c97ddbe030eafadf0623fdcd80c4529c31/solr/api/src/java/org/apache/solr/client/api/endpoint/ConfigsetsApi.java#L46]). Both list configSets.

Consistency:

 * The path parameter for a collection is {{collName}} on 10 sites in 5 files and {{collectionName}} on 14 sites in 12 files ([CollectionPropertyApi.java:31|https://github.com/apache/solr/blob/e59ce3c97ddbe030eafadf0623fdcd80c4529c31/solr/api/src/java/org/apache/solr/client/api/endpoint/CollectionPropertyApi.java#L31] against [CollectionStatusApi.java:33|https://github.com/apache/solr/blob/e59ce3c97ddbe030eafadf0623fdcd80c4529c31/solr/api/src/java/org/apache/solr/client/api/endpoint/CollectionStatusApi.java#L33]).
 * camelCase segments {{/purgeUnused}} ([DeleteCollectionBackupApi.java:66|https://github.com/apache/solr/blob/e59ce3c97ddbe030eafadf0623fdcd80c4529c31/solr/api/src/java/org/apache/solr/client/api/endpoint/DeleteCollectionBackupApi.java#L66]) and {{/requestHandlers}} ([ConfigApi.java:76|https://github.com/apache/solr/blob/e59ce3c97ddbe030eafadf0623fdcd80c4529c31/solr/api/src/java/org/apache/solr/client/api/endpoint/ConfigApi.java#L76]), where the other multi-word segments are lowercase or hyphenated.

Not flagged: the {{/node/...}} singular paths against {{/cluster/nodes}}, which read as singleton against collection.

The ticket's description links to a PR 5064 review comment by dsmiley (https://github.com/apache/solr/pull/5064#discussion_r4230690636). In that comment he says he dislikes {{detailed}}, and in an earlier comment on the same PR he says he would have preferred {{details}}. That is one reviewer's preference, not a thread decision.

Eric, does this list meet the bar you had in mind for the ticket? The two v1 spellings ({{detail}} and {{command=details}}) are new in this version. Should they be in scope for this ticket, or handled separately?

Possible states for the ticket: close it once the two renames for the v2 names land; close it once the v1 names are in scope too; or keep it open for a wider v1 and v2 naming decision. The recommendation is the first: the v2 items are mechanical, and the v1 names are public and need their own decision.

🤖 _AI text above_ 🤖 _(reviewed by Nick Shanin)_
