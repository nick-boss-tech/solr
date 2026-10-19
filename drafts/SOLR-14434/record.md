# Skip record: SOLR-14434

- Ticket: SOLR-14434, "Add documentation for adding multiterm analyzers in Schema API". JIRA status Open, resolution none, fix versions none, created 2020-04-24, updated 2020-05-27. Checked 2026-10-19 UTC.
- Verdict: keep-open.
- Draft branch: solr-14434-draft-open.
- Pinned head: fork origin/main 498a09aea97aac86c2cc7ac114113b946d249942. No upstream sha cited.
- Checked by: vm2 (pool Linux host), 2026-10-19 UTC, for the remediation redo of this ticket (claim claims/skip-SOLR-14434-vm2.md).

## Ticket reading

The ticket asks for three documentation items: how to add a multiterm analyzer through the Schema API (the report says the XML and JSON property names differ in casing); a note that the analysis/field API ignores multiterm analysis and so does not show the analysis of wildcard, prefix, and range queries; and the multiterm example itself. It has no comments.

## Code reading

- solr/solr-ref-guide/modules/indexing-guide/pages/analyzers.adoc:169-196 (origin/main 498a09aea97): the general multiterm analyzer is documented, with an XML example (`<analyzer type="multiterm">`). This covers the third item in general form.
- solr/solr-ref-guide/modules/indexing-guide/pages/schema-api.adoc: `git show origin/main:<page> | grep -c -i multiterm` returns 0. The Schema API page does not describe a multiterm analyzer in its JSON form, so the first item is not met.
- The analysis/field note (the second item) was not located in the pages read; a search of the analysis-related pages for multiterm was not run in this pass.

Commands at the pinned head:
- git show origin/main:solr/solr-ref-guide/modules/indexing-guide/pages/analyzers.adoc | grep -n -i multiterm: lines 169 and 185.
- git show origin/main:solr/solr-ref-guide/modules/indexing-guide/pages/schema-api.adoc | grep -c -i multiterm: 0.
- git log --format='%h %ad %s' --date=short --grep='SOLR-14434' origin/main: no output.

## History

- Commits on origin/main naming SOLR-14434: none.

## Reproduction attempts

Not applicable. The ticket asks for documentation, and the check is the text at the pinned head, recorded above.

## Gap analysis

The general multiterm documentation exists in analyzers.adoc. The Schema API part (the JSON form of the multiterm analyzer, with the correct property name) and the analysis/field note are not in the Schema API page. Whether the analysis/field page mentions the limitation was not searched.

## Verdict justification

keep-open meets the rubric: the pinned head sha is cited, the Jira status and date are read in this pass, code paths are cited with file and line, and the attempts and the missing piece are listed. close-fixed does not hold, because the ticket's Schema API item is not met (0 mentions on the Schema API page). close-answered does not hold, since the Schema API question is not answered by the guide. The missing pieces are (1) the Schema API JSON example for a multiterm analyzer, and (2) the analysis/field note, which needs a search of the analysis pages.

## Limits

This record does not check the rendered guide. It does not search every analysis page for the analysis/field limitation. It does not verify the JSON property names against the Schema API code.
