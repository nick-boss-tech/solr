DRAFT, NOT POSTED. Owner note (not part of the comment): Nick has not reviewed this text. Do not post until he does. The footer's "reviewed by Nick Shanin" wording is proposed until that review. Addressee: David Smiley (Jira user dsmiley), reporter. This ticket is covered-by-link from SOLR-14311. Evidence: reports/critical-comments-round-1-batch-05.md and the Jira comments of 2020-03-03 to 2020-03-06.

----

[~dsmiley] This ticket asks for protections so a shared IndexSchema does not reference a SolrCore. The 2020 comments describe two options: give each schema its own SolrResourceLoader, or mark components that need core state as unsupported with an error.

The decision the ticket turns on is which of those two options the project wants, because the first changes the resource loader lifecycle and the second changes which components work with shareSchema. Neither is drafted here as an implementation.

🤖 _AI text above_ 🤖 _(reviewed by Nick Shanin)_
