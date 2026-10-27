# Jira comment draft: SOLR-18553

Status: DRAFT (not posted). Shape: staged ask. Addressee: Eric Pugh (reporter). Prepared by vm6, 2026-10-27, from `reports/solr-18553-plurality-audit-round-1.md` (pinned main `e59ce3c97d`). Posting needs Nick Shanin's explicit go.

---

I went through the v2 endpoint interfaces at apache/solr main {{e59ce3c97d}} for plurality and naming consistency. Four items have a file and line each. The two plurality items:

 * {{detailed}} is the only adjective-named "return more" flag. Elsewhere the same role uses the {{xxxInfo}} suffix ({{coreInfo}}, {{fieldInfo}}, {{sizeInfo}}, {{indexInfo}}). [ListCollectionsApi.java:37|https://github.com/apache/solr/blob/e59ce3c97ddbe030eafadf0623fdcd80c4529c31/solr/api/src/java/org/apache/solr/client/api/endpoint/ListCollectionsApi.java#L37], against [SegmentsApi.java:57|https://github.com/apache/solr/blob/e59ce3c97ddbe030eafadf0623fdcd80c4529c31/solr/api/src/java/org/apache/solr/client/api/endpoint/SegmentsApi.java#L57] and [CoreApis.java:44|https://github.com/apache/solr/blob/e59ce3c97ddbe030eafadf0623fdcd80c4529c31/solr/api/src/java/org/apache/solr/client/api/endpoint/CoreApis.java#L44].
 * The same concept has two path names: {{/schema-designer/configs}} ([SchemaDesignerApi.java:121|https://github.com/apache/solr/blob/e59ce3c97ddbe030eafadf0623fdcd80c4529c31/solr/api/src/java/org/apache/solr/client/api/endpoint/SchemaDesignerApi.java#L121]) and {{/configsets}} ([ConfigsetsApi.java:46|https://github.com/apache/solr/blob/e59ce3c97ddbe030eafadf0623fdcd80c4529c31/solr/api/src/java/org/apache/solr/client/api/endpoint/ConfigsetsApi.java#L46]). Both list configSets.

And two consistency items:

 * The path parameter for a collection is {{collName}} on 10 sites in 5 files and {{collectionName}} on 14 sites in 12 files ([CollectionPropertyApi.java:31|https://github.com/apache/solr/blob/e59ce3c97ddbe030eafadf0623fdcd80c4529c31/solr/api/src/java/org/apache/solr/client/api/endpoint/CollectionPropertyApi.java#L31] against [CollectionStatusApi.java:33|https://github.com/apache/solr/blob/e59ce3c97ddbe030eafadf0623fdcd80c4529c31/solr/api/src/java/org/apache/solr/client/api/endpoint/CollectionStatusApi.java#L33]).
 * camelCase segments {{/purgeUnused}} ([DeleteCollectionBackupApi.java:66|https://github.com/apache/solr/blob/e59ce3c97ddbe030eafadf0623fdcd80c4529c31/solr/api/src/java/org/apache/solr/client/api/endpoint/DeleteCollectionBackupApi.java#L66]) and {{/requestHandlers}} ([ConfigApi.java:76|https://github.com/apache/solr/blob/e59ce3c97ddbe030eafadf0623fdcd80c4529c31/solr/api/src/java/org/apache/solr/client/api/endpoint/ConfigApi.java#L76]), where the other multi-word segments are lowercase or hyphenated.

I checked the {{/node/...}} singular paths against {{/cluster/nodes}} and did not flag them; they read as singleton against collection.

Eric, the PR 5064 review thread prefers {{details}} to {{detailed}}. Does this list meet the bar you had in mind for the ticket?

Possible states for the ticket: close it once the two renames (F1 and F3) land; close it once all four items land; or keep it open for a wider v2 naming decision. The recommendation is the second: F2 and F4 are mechanical, and F1 and F3 rename public endpoints, so those wait for the ticket reporter.

🤖 _AI text above_ 🤖 _(reviewed by Nick Shanin)_
