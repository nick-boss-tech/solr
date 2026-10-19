# Skip record: SOLR-16306

- Ticket: SOLR-16306, "Faceting over a DateRangeField gives empty result". JIRA status Open, resolution none, fix versions none, created 2022-07-22, updated 2023-01-25. Checked 2026-10-19 UTC.
- Verdict: keep-open.
- Draft branch: solr-16306-draft-open.
- Pinned head: fork origin/main 498a09aea97aac86c2cc7ac114113b946d249942. No upstream sha cited.
- Checked by: vm2 (pool Linux host), 2026-10-19 UTC, for the remediation redo of this ticket (claim claims/skip-SOLR-16306-vm2.md).

## Ticket reading

The reporter facets (facet.field) on a single-valued DateRangeField (validrange_dr) and gets an empty list. They expect one facet value per unique from-to range. The example response shows 38 documents with ranges such as [2021-06-22T12:18:23.000Z TO 2999-12-31T23:59:59.999Z]. Faceting on a normal date field works. The report has no comments and names no test class.

## Code reading

- solr/core/src/java/org/apache/solr/schema/AbstractSpatialFieldType.java:229-231 (origin/main 498a09aea97): getUninversionType(SchemaField) returns null. DateRangeField extends this spatial base, so there is no uninverted type for term-based faceting on the field.
- solr/core/src/java/org/apache/solr/request/SimpleFacets.java:541: the FCS branch treats a point field, or a numeric field with a single value, as numeric facets. DateRangeField is not a point field (it is a spatial field), so the numeric branch does not apply, and the term-based branch is used.
- The terms that term-based faceting reads for DateRangeField are the prefix-tree cells of the shape index, not the from and to values of each range. That is why the facet list comes back empty or without the expected ranges. This is the reading from the code and from the field type's design; it was not checked against a term dump in this pass.
- solr/solr-ref-guide/modules/query-guide/pages/faceting.adoc:425 says only that the dv range-faceting method is not supported for DateRangeField. It does not say that field faceting is unsupported.

Commands at the pinned head:
- git grep -n 'DateRangeField' origin/main -- solr/solr-ref-guide | grep facet: faceting.adoc:425 only.
- git grep -l -i 'facet' origin/main -- solr/core/src/test with DateRange: no test found.

## History

- Commits on origin/main naming SOLR-16306: none.
- Commits touching AbstractSpatialFieldType.getUninversionType: not traced in this pass.

## Reproduction attempts

Not attempted. A reproduction needs a core with a DateRangeField and documents indexed with ranges, then facet.field on it. That is a test run of a few minutes, and it was not run in this pass.

## Gap analysis

The empty result follows from the field's indexed terms: the facet reads prefix cells and the range values are not terms. Whether this is a bug (faceting should produce the ranges, which would need a new facet method) or a documented limitation (the field is not meant for field faceting) is a product decision, not something the code settles.

## Verdict justification

keep-open meets the rubric: the pinned head sha is cited, the Jira status and date are read in this pass, code paths are cited with file and line, and the attempt and the missing piece are listed. The missing pieces are (1) a reproduction with a DateRangeField core to confirm the empty result on this head, and (2) a decision on whether field faceting over DateRangeField should be supported or documented as unsupported. A close verdict needs a documented answer or a fix; a pr-needed verdict needs that decision.

## Limits

This record does not reproduce the empty result. It does not dump the indexed terms for a DateRangeField. It does not decide whether the feature should exist.
