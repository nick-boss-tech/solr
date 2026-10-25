DRAFT, NOT POSTED. Owner note (not part of the comment): Nick has not reviewed this text. Do not post until he does. The footer's "reviewed by Nick Shanin" wording is proposed until that review. Addressee: Jason Gerlowski (Jira user gerlowskija), reporter. Evidence: reports/solr-17708-double-auth-trace-round-1.md.

----

[~gerlowskija] I traced the double authorization on apache/solr main at {{13c693d805f}}. It is confirmed for one container-level and one core-level v2 endpoint.

 * Layer one is in {{HttpSolrCall.call()}}, HttpSolrCall.java:480-490, before dispatch. The cited range in the ticket (L534-L544) has moved.
 * Layer two is {{SolrRequestAuthorizer.filter()}}, SolrRequestAuthorizer.java:54-91. Both Jersey applications register it, the container app and the core app.
 * Container level: {{ClusterFileStore.getFile}}, ClusterFileStore.java:159-160, {{FILESTORE_READ_PERM}}.
 * Core level: {{GetSchemaField.getFieldInfo}}, GetSchemaField.java:82-83, {{SCHEMA_READ_PERM}}.

The override in the 2025 comment cannot be applied as written: {{shouldAuthorize()}} is private (HttpSolrCall.java:608), and the Jersey route match happens after layer one.

Should the fix remove the layer-two check, or skip layer one only for requests Jersey will serve? Or is one of these already planned?

🤖 _AI text above_ 🤖 _(reviewed by Nick Shanin)_
