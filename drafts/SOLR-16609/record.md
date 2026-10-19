# Skip record: SOLR-16609

- Ticket: SOLR-16609, "Unable to set properties when creating config/collection in Solr Cloud environment". JIRA status read 2026-10-19 (UTC): Open, resolution none, created 2023-01-03, updated 2023-01-25, no fix version. One comment, from dsmiley (2023-01-10), who points to `bin/solr config ... set-user-property` and says the 400 error looks like a terminal or payload issue.
- Verdict: close-answered.
- Draft branch: solr-16609-draft-close (replaces solr-16609-draft-open, whose verdict was keep-open).
- Pinned head: fork origin/main 498a09aea97aac86c2cc7ac114113b946d249942 (2026-10-07). Upstream sha not cited.
- Checked by: vm4, 2026-10-19 14:06-14:07 UTC.

## Ticket reading

The reporter wants three properties set on a new collection or configset: `updateHandler.autoCommit.maxTime=60000`, `updateHandler.autoCommit.openSearcher=true`, and `updateHandler.autoSoftCommit.maxTime=30000`. They tried passing them through SolrJ `CollectionAdminRequest.Create` and an update call, and got a 400. The ticket is a support question about how to set them.

## Code reading

Not a code question. The relevant documentation is the Config API page at the pinned head.

- `solr/solr-ref-guide/modules/configuration-guide/pages/config-api.adoc:146-148`: `set-property` and `unset-property` are the commands for common properties.
- `config-api.adoc:160-171`: the predefined list, which includes `updateHandler.autoCommit.maxDocs`, `updateHandler.autoCommit.maxTime`, `updateHandler.autoCommit.openSearcher`, `updateHandler.autoSoftCommit.maxDocs`, and `updateHandler.autoSoftCommit.maxTime`. All three of the reporter's properties are listed.
- `config-api.adoc:228-231`: the request pattern is `{"set-property":{"<property>": "<value>"}}`. The page gives the V1 and V2 request forms in its tabs right after that example.
- `solr/solr-ref-guide/modules/configuration-guide/pages/commits-transaction-logs.adoc:167-172`: the autoCommit and autoSoftCommit settings these properties map to.

The page does not say that the properties can be passed at create time. It documents them as settable through the Config API on an existing collection.

## History

- No commit on the pinned head names SOLR-16609 (evidence tool: no hits).

## Reproduction attempts

Not attempted. The question is answered by the documentation; a reproduction would test the SolrJ create call, which the docs do not promise to support. This pass is review-only.

## Gap analysis

The reporter's goal is to set the commit settings for a collection. The documented way is to set them after the collection exists, with the Config API `set-property` command, or with `bin/solr config`. The reporter tried to do it at creation through `CollectionAdminRequest.Create`, which the docs do not describe as a way to set these properties. The 400 response is consistent with the request payload being wrong for that endpoint, as the 2023 comment suggested, but the ticket does not include the request, so that is not confirmed.

## Verdict justification

close-answered meets the rubric: the reference guide answers the question (the page and the sentences above), and the draft links the page. The rubric asks for the docs link and the sentence that answers the question, both given. It is the support route, not a defect, and the earlier keep-open was for a missing reproduction that is not needed for a support answer.

## Limits

This record does not confirm why the SolrJ create call returned 400, because the request is not in the ticket. It does not say whether creating a collection with these properties in one call is wanted as a feature. It does not post anything to JIRA or GitHub.
