# Review: SOLR-8614, branch solr-8614-xml-format-toggle (unit 44)

Head reviewed: 658872c20ab2722b3eb1ebe37f99893cab744680. Base: c8ab71430f. Ticket: SOLR-8614, "Add simple SolrClient.setUseXmlFormat".

## Pass 1

- The change matches the ticket: `SolrClient.setUseXmlFormat(boolean)` is a no-op on the base class and is overridden by the HTTP, Cloud, collection-scoped, concurrent-update and LB clients.
- Scope: the diff touches only the SolrJ client classes, the new test and the changelog entry.
- Design concern, not repaired (outside the repair scope): `LBSolrClient.clientFor` calls `HttpSolrClient.setUseXmlFormat` on every request when an override is set. That allocates a new writer and parser per request and rewrites the shared endpoint client's fields, so concurrent requests race on them. The result is the same type each time, so behaviour is correct, but it is wasteful. The fix would be to set the override once when the endpoint client is created.
- Test coverage: `SetUseXmlFormatTest` covers only `HttpJdkSolrClient`. Cloud, LB and concurrent-update delegation are not tested.
- Repairs: none.

Regate: `gates/SOLR-8614-review-pass-1.md`, GATE GREEN at 658872c20a on vm4.
