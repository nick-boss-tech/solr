# Skip record: SOLR-13056

- Ticket: SOLR-13056, SortableTextField is trappy for faceting.
- Verdict: keep-open.
- Draft branch: solr-13056-draft-open (verdict unchanged; this record replaces the audit-derived record on this branch).
- Pinned head: fork origin/main 498a09aea97aac86c2cc7ac114113b946d249942. Upstream sha not cited.
- Checked by: vm5 (pool Linux host), 2026-10-20 UTC. Redo of remediation batch 07, item 17 (`assignments/pool-aaa-skip-verdict-remediation-07.md`).

## Ticket reading

JIRA read 2026-10-20: Open, no resolution, created 2018-12-11, updated 2022-04-01, no fix versions. Links: "Problem/Incident SOLR-11916 (Closed)"; "Reference SOLR-16139 (Open)". The report: distributed faceting over `SortableTextField` gives wrong counts with several shards (a reproduction on the `gettingstarted` cloud sample with three shards is described in the ticket). Four comments (2018-12-11 to 2019-03-01): toke asks for the limitation to be documented (as SOLR-11916 said); hossman argues that facet refinement should not run analysis; magibney attaches a patch that builds the refinement `TermQuery` directly, and notes a further problem: refinement runs against the inverted index while the first pass uses DocValues.

## Code reading

Read at the pin:

- `solr/core/src/java/org/apache/solr/request/SimpleFacets.java:991`: `numDocs` builds the term query with `ft.getFieldTermQuery(null, sf, term)`, without analysis. The non-analysis part of the proposed patch is therefore in the pin.
- `solr/solr-ref-guide/modules/indexing-guide/pages/field-types-included-with-solr.adoc:76`: the `SortableTextField` row describes docValues sorting on the first characters before analysis and the `maxCharsForDocValues` attribute. It does not mention the faceting limitation, so the documentation note that toke asked for is not present at the pin.
- The inverted-index versus DocValues refinement problem that magibney describes is not resolved by the code read: the first facet pass uses DocValues and the refinement uses the inverted index, and no code at the pin changes that pairing.

## History

- SOLR-11916 is closed and linked; its commits were not traced in this pass.
- SOLR-16139 (open, linked) is a later ticket; no commit at the pin names SOLR-13056 (the evidence tool lists none).

## Reproduction attempts

Not run. This pass ran no builds or tests. The ticket describes a reproduction on the cloud sample with three shards and a generated corpus; running it at the pin would show whether counts still differ.

## Gap analysis

- Two different remedies are on the ticket: document the limitation, or change the refinement path (DocValues or a matching index path). The ticket does not record which one the maintainers want.
- The documentation gap is confirmed by reading the pinned guide.
- The refinement-path question is a design decision and has a related open ticket (SOLR-16139).

## Verdict justification

keep-open: the documentation note is missing and the refinement design is open with a related open ticket, so neither a docs close nor a fix close applies. The choice between documenting the limitation and changing the refinement is a maintainer decision. close-answered would need the guide to answer the question, and it does not mention the faceting limitation.

Missing for this verdict: the maintainer choice between documenting the limitation and changing the refinement path, and a run of the ticket's sharded facet reproduction at the pin.

## Limits

This record does not establish a reproduction. It does not post anything to JIRA or GitHub. Commit linkage is by commit title or code path, as stated above.
</content>
