# Skip record: SOLR-9578

- Ticket: SOLR-9578, "Implement document level security in Solr". JIRA status read 2026-10-20T13:16:41Z (UTC): Open, resolution None, updated 2026-10-10. Linked: SOLR-1834 (Open, the duplicate target).
- Verdict: keep-open (kept; re-read at the pinned head). This is a feature request, and its design is a maintainer decision.
- Draft branch: solr-9578-draft-open.
- Pinned head: 498a09aea97aac86c2cc7ac114113b946d249942 (fork origin/main).
- Checked by: vm2, 2026-10-20T13:16:41Z (UTC).

## Ticket reading

The ticket asks for document-level authorization in Solr so that Sentry (and similar systems) can filter results per user without custom search components. Six comments (2017-2019) discuss a SearchComponent, a query-parser filter as in ManifoldCF, and integration with Alfresco. No design has been agreed. JIRA marks it a duplicate of SOLR-1834, which is Open.

## Code reading (pinned head)

- A case-insensitive grep for document-level security terms (`documentlevel`, `document level`, `docLevel`) under `solr/core/src/java` and `solr/solrj/src/java`: no hits.
- The authorization framework has rule-based plugins (`solr/core/src/java/org/apache/solr/security/RuleBasedAuthorizationPlugin.java`, `RuleBasedAuthorizationPluginBase.java`, `ExternalRoleRuleBasedAuthorizationPlugin.java`, `MultiAuthRuleBasedAuthorizationPlugin.java`). They authorize requests by path and permission, not by document, so the per-document filter the ticket asks for is not present.

## History

- evidence.py at the pinned head: no commits naming SOLR-9578.

## Reproduction attempts

Not applicable: this is a missing feature, so there is nothing to reproduce. The check was the grep and the plugin reading above.

## Gap analysis

The feature is absent and the design is open: a SearchComponent, a query-parser filter applied per request, or an authorization hook that returns a filter. Choosing one is a design decision.

## Verdict justification

keep-open meets the rubric for an open feature with a design question: the attempts made are the grep and the plugin reading; the missing piece is the design choice, which belongs to the maintainers. close-fixed, close-obsolete and close-cannot-reproduce do not apply to an absent feature. pr-needed is not chosen, since the design is not agreed.

## Limits

This record does not choose a design. It does not check the duplicate SOLR-1834 beyond the JIRA link.
