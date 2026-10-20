# Skip record: SOLR-13167

- Ticket: SOLR-13167, Duplicate Child Documents and undeterministic search.
- Verdict: close-answered.
- Draft branch: solr-13167-draft-close (verdict unchanged; this record replaces the earlier record on this branch).
- Pinned head: fork origin/main 498a09aea97aac86c2cc7ac114113b946d249942. Upstream sha not cited.
- Checked by: vm5 (pool Linux host), 2026-10-20 UTC. Redo of remediation batch 07, item 12 (`assignments/pool-aaa-skip-verdict-remediation-07.md`).

## Ticket reading

JIRA read 2026-10-20: Open, no resolution, created 2019-01-25, updated 2019-06-08, no fix versions, no links, no comments. The report: a product index in SolrCloud (two shards, two instances) with an unbounded number of child documents per product; the reporter syncs the child documents and gets duplicate child documents, and search results vary between runs.

## Code reading

Read at the pin:

- `solr/solr-ref-guide/modules/indexing-guide/pages/indexing-nested-documents.adoc:294-298`: "Solr demands that the `id` of _all_ documents in a collection be unique. Solr enforces this for root documents within a shard, but not for child documents to avoid the expense of checking. Clients should be very careful to *never* violate this."
- The statement is the documented behavior at the pin: Solr does not check child document id uniqueness, so duplicate child ids are the client's responsibility to avoid. This is the answer to the reporter's duplicate child documents. The nondeterministic results follow from the same duplicates across shards and are not a separate question the docs leave open.

## History

- No commit at the pin names SOLR-13167 (the evidence tool lists none).
- The guide page is at the pin; its sentence was read directly.

## Reproduction attempts

Not attempted. The answer is documentation; no build or test run is needed for a close-answered verdict.

## Gap analysis

- The docs answer the reporter's question: duplicate child ids are not checked, so they are a client responsibility.
- The reporter's specific sync logic is not in the ticket, so whether the reporter's client violates the documented rule cannot be checked here; the record does not claim it.

## Verdict justification

close-answered: the reference guide page `indexing-nested-documents.adoc:294-298` answers the question of why child documents duplicate: Solr does not enforce id uniqueness for child documents, and the client must avoid it.

Missing for this verdict: none for close-answered; the answer is documentation. The reporter's sync code would be needed to say whether that client violates the rule, which the close does not claim.

## Limits

This record does not establish a reproduction. It does not post anything to JIRA or GitHub. Commit linkage is by commit title or code path, as stated above.
</content>
