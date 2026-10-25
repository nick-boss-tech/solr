DRAFT, NOT POSTED. Owner note (not part of the comment): Nick has not reviewed this text. Do not post until he does. The footer's "reviewed by Nick Shanin" wording is proposed until that review. Shape: scoping. Evidence: reports/critical-comments-round-1-batch-09.md.

----

[~dsmiley] This ticket asks each SolrRequest to declare whether it may be retried. CloudSolrClient and LBSolrClient currently infer retry safety from different signals, and they disagree. SOLR-18402 is linked and covers the related consolidation.

The decision the ticket turns on is whether the declaration belongs on the request type or on each call, and which default applies to a request that does not declare one. That changes retry behaviour for every SolrJ user, so I have not drafted an implementation.

🤖 _AI text above_ 🤖 _(reviewed by Nick Shanin)_
