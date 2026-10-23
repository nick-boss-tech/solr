# Review: SOLR-8614, branch solr-8614-xml-format-toggle (unit 44)

Head reviewed: 658872c20ab2722b3eb1ebe37f99893cab744680. Base: c8ab71430f. Ticket: SOLR-8614, "Add simple SolrClient.setUseXmlFormat".

## Pass 1

- The change matches the ticket: `SolrClient.setUseXmlFormat(boolean)` is a no-op on the base class and is overridden by the HTTP, Cloud, collection-scoped, concurrent-update and LB clients.
- Scope: the diff touches only the SolrJ client classes, the new test and the changelog entry.
- Design concern, not repaired (outside the repair scope): `LBSolrClient.clientFor` calls `HttpSolrClient.setUseXmlFormat` on every request when an override is set. That allocates a new writer and parser per request and rewrites the shared endpoint client's fields, so concurrent requests race on them. The result is the same type each time, so behaviour is correct, but it is wasteful. The fix would be to set the override once when the endpoint client is created.
- Test coverage: `SetUseXmlFormatTest` covers only `HttpJdkSolrClient`. Cloud, LB and concurrent-update delegation are not tested.
- Repairs: none.

Regate: `gates/SOLR-8614-review-pass-1.md`, GATE GREEN at 658872c20a on vm4.

## Pass 2

Review (2026-10-23, vm4), same head 658872c20a. - Re-read the diff at the same head 658872c20a. No new findings in pass 2.
- Pass 1 design and coverage notes stand, not repaired: per-request writer churn in `LBSolrClient.clientFor`, and no Cloud or LB delegation tests.
- Repairs: none.

Regate: `gates/SOLR-8614-review-pass-2.md`, GATE GREEN at 658872c20a on vm4.

## Pass 3

Review (2026-10-23, vm4), same head 658872c20a. - Regate on the same head 658872c20a. No new findings in pass 3.
- Pass 1 and pass 2 notes stand, not repaired: per-request writer churn in `LBSolrClient.clientFor`, and no Cloud or LB delegation tests.
- Repairs: none.

Regate: `gates/SOLR-8614-review-pass-3.md`, GATE GREEN at 658872c20a on vm4.

Final head: 658872c20ab2722b3eb1ebe37f99893cab744680. Unit 44 DONE with the three pass sections.
