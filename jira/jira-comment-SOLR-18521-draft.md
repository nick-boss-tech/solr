DRAFT, NOT POSTED. Owner note (not part of the comment): Nick has not reviewed this text. Do not post until he does. The footer's "reviewed by Nick Shanin" wording is proposed until that review. Addressee: Jan Høydahl (Jira user janhoy), reporter and assignee. Evidence: reports/solr-18521-picocli-scoping-round-1.md.

----

[~janhoy] I counted the connection options on apache/solr main at {{13c693d805f}}, before the SOLR-18509 to SOLR-18519 ports land. The count is larger than the ticket states in three places:

 * The exclusive {{ConnectionOptions}} group is on 16 tool classes, not three: create, delete, version, and also config, export, healthcheck, package, post, post-logs, stream, and the five snapshot tools.
 * {{ZkConnectionOptions}} is a non-exclusive mixin on 10 classes. It re-declares {{--credentials}} at ZkConnectionOptions.java:56.
 * There are two static {{resolveSolrUrl}} helpers, CLIUtils.java:257 and ConnectionOptions.java:80. They differ for a {{--zk-host}} value that is not a ZooKeeper string: CLIUtils throws, and ConnectionOptions does not.

The {{zkHost}} environment fallback appears in 8 places across 7 files.

I counted from the code. I did not build or run the CLI.

Does this count match the scope you want before the refactor, or should the snapshot tools keep their own group?

🤖 _AI text above_ 🤖 _(reviewed by Nick Shanin)_
