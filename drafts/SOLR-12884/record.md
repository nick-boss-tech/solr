# Skip record: SOLR-12884

- Ticket: SOLR-12884, Admin UI doesn't show info about *Point fields in Schema browser screen.
- Verdict: keep-open.
- Draft branch: solr-12884-draft-open (verdict unchanged; this record replaces the audit-derived record on this branch).
- Pinned head: fork origin/main 498a09aea97aac86c2cc7ac114113b946d249942. Upstream sha not cited.
- Checked by: vm5 (pool Linux host), 2026-10-20 UTC. Redo of remediation batch 07, item 24 (`assignments/pool-aaa-skip-verdict-remediation-07.md`).

## Ticket reading

JIRA read 2026-10-20: Open, no resolution, created 2018-10-18, updated 2019-10-08, no fix versions, no links. The report: in the Schema browser, a point field (`pint`) shows nothing when "load term info" is clicked; `admin/luke` gives less information for a point field than for the `tint` equivalent. Five comments (2018-10-19 to 2019-03-18): dsmiley agrees to show useful information (his "+1" and the suggested text); arafalov sees a similar gap for `_str` fields; christopherball and jbernste propose computing a frequency table with streaming expressions (`random`, `col`, `freqTable`) instead; ctargett retitles the ticket. No decision is recorded on what the UI should show for point fields.

## Code reading

Read at the pin:

- `solr/core/src/java/org/apache/solr/handler/admin/LukeRequestHandler.java:803`: `if (ftype != null && !ftype.isPointField())` guards the per-term `docFreq` computation. Point fields therefore get no term-level document frequency in the Luke response, which is the gap the ticket describes.
- `solr/webapp/web/js/angular/controllers/schema.js:146-160` (`loadTermInfo`): the browser calls the Luke `field` endpoint with `fl` and `numTerms`, and builds the term info from `data.fields[name]`. For a point field the response carries no term-level counts, so the panel stays empty.

So the behavior the ticket reports is present at the pin, and the code shows where the information is omitted (by design, for point fields).

## History

No commit at the pin names SOLR-12884 (the evidence tool lists none). The point-field guard in Luke was not traced to its introduction.

## Reproduction attempts

Not run. This pass ran no builds or tests. The reproduction is to add a `pint` field and open the schema browser's term info for it at the pin; the empty panel is expected from the code read, but the rendered result was not checked in a browser.

## Gap analysis

- The gap is confirmed by reading; the rendered result in the browser has not been checked.
- The ticket asks for "useful information" for point fields; the comments give different candidates (a sampled frequency table, a streaming expression), and no choice is recorded.
- Whether Luke should return per-term data for point fields at all is a design choice for the maintainers.

## Verdict justification

keep-open: the omission is confirmed in the code, the UI behavior follows from it, and what information should replace it is an open design choice on the ticket. close-fixed and close-obsolete do not fit (nothing was fixed or removed); close-answered does not fit (the docs do not describe what point fields should show).

Missing for this verdict: the maintainer decision on what the schema browser and Luke should show for point fields, and a browser check of the current empty panel.

## Limits

This record does not establish a reproduction. It does not post anything to JIRA or GitHub. Commit linkage is by commit title or code path, as stated above.
</content>
