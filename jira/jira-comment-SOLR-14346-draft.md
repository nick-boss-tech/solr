DRAFT, NOT POSTED. Owner note (not part of the comment): Nick has not reviewed this text. Do not post until he does. The footer's "reviewed by Nick Shanin" wording is proposed until that review. Shape: stale-check. Evidence: reports/critical-comments-round-1-batch-06.md.

----

[~tiaotiaoba] This ticket reports that LbHttpSolrClient does not revive a zombie server, because the check sends a query without a core name. A 2021 comment says that putting a collection name in the base URL avoids the problem.

Is this still seen on a supported SolrJ release with a root base URL? If it is, the zombie check request from the current client would show where it fails.

🤖 _AI text above_ 🤖 _(reviewed by Nick Shanin)_
