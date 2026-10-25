DRAFT, NOT POSTED. Owner note (not part of the comment): Nick has not reviewed this text. Do not post until he does. The footer's "reviewed by Nick Shanin" wording is proposed until that review. Shape: stale-check. Evidence: reports/critical-comments-round-1-batch-03.md.

----

[~glebek] This ticket reports that LBHttpSolrClient cannot mark a node alive again when it was built from base root URLs, because its zombie check gets a 404. A 2018 comment notes that the check uses a standard query with no core, and another notes that it fails when a request parameter is used for authentication.

Is this still seen on a supported SolrJ release built from root URLs? If it is, the zombie check request from the current client would show where the 404 comes from.

🤖 _AI text above_ 🤖 _(reviewed by Nick Shanin)_
