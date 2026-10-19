# Skip record: SOLR-7559

- Ticket: SOLR-7559, Cannot use Faceting Feature while using/mlt handler. JIRA status not recorded in the C reading. JIRA was not re-read for this record.
- Verdict: close-fixed.
- Draft branch: solr-7559-draft-close.
- Pinned head: fork origin/main 498a09aea97aac86c2cc7ac114113b946d249942. Upstream sha not cited.
- Checked by: vm2 (pool Linux host), 2026-10-19 UTC, from the C reading (reports/2026-10-18-skip-repro/C2, slice 2, pinned head unchanged).

## Ticket reading

The summary as recorded in the queue: Cannot use Faceting Feature while using/mlt handler.

## Code reading

fixed | Commit 335ba21352 (SOLR-7883, 2022-09-15) changed MoreLikeThisHandler.java:218 from a SimpleFacets built without a ResponseBuilder to one built with it (lines 223-229). SpatialHeatmapFacets.java:55 dereferences rb.req, so the NPE path is closed. No commit cites SOLR-7559 and no test covers facet.heatmap, so this is inferred.

## History

- `335ba21352`: cited in the C2 reading; ancestor of the pinned head: yes (checked).

## Reproduction attempts

Not attempted. The C scope ran no builds or tests.

## Gap analysis

The code reading shows the reported behavior no longer applies at the pinned head, as described above.

## Verdict justification

close-fixed: a change on main addresses the reported behavior, and the changed code is present at the pinned head (cited in the code reading). Linkage is by code path or commit title, as the C reading states.

Missing for this verdict: No reproduction was run; the C scope ran no builds.

## Limits

This record does not establish a reproduction. It does not post anything to JIRA or GitHub. Commit linkage is by commit title or code path where the subject does not cite the ticket.
