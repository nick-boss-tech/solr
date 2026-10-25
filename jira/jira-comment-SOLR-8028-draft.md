DRAFT, NOT POSTED. Owner note (not part of the comment): Nick has not reviewed this text. Do not post until he does. The footer's "reviewed by Nick Shanin" wording is proposed until that review. Shape: stale-check. Evidence: reports/critical-comments-round-1-batch-02.md.

----

[~lvchuanwen] This ticket reports a ConcurrentModificationException in JavaBinCodec when a client reuses SolrInputDocument instances or their field value lists. The 2017 question about instance reuse has no answer on the ticket.

Is this still seen on a supported Solr release? If the cause is client-side reuse, the reporter's answer would close it.

🤖 _AI text above_ 🤖 _(reviewed by Nick Shanin)_
