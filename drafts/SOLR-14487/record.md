# Skip record: SOLR-14487

- Ticket: SOLR-14487, "collapse parser treats a zero field value as non-existent". JIRA status Open, resolution none, fix versions none, created 2020-05-15, updated 2020-05-27. Checked 2026-10-19 UTC.
- Verdict: keep-open.
- Draft branch: solr-14487-draft-open.
- Pinned head: fork origin/main 498a09aea97aac86c2cc7ac114113b946d249942. No upstream sha cited.
- Checked by: vm2 (pool Linux host), 2026-10-19 UTC, for the remediation redo of this ticket (claim claims/skip-SOLR-14487-vm2.md).

## Ticket reading

Collapsing on an integer field treats a document whose value is 0 as if it had no value. The reporter found it while writing a test, and worked around it with nullPolicy=expand. The report has no comments and names no test class; the reporter's test method is in a pull request, not in the report text.

## Code reading

- solr/core/src/java/org/apache/solr/search/CollapsingQParserPlugin.java:1076-1077 (origin/main 498a09aea97): the numeric collapse collector reads `collapseValues.advanceExact(contextDoc)` and only then takes the value. advanceExact reports whether the document has a value, so a stored 0 is a present value here, not a missing one.
- The same file reads segment values with advanceExact at lines 957 and 964, 1021 and 1025, 1134, 1248 and 1252, and 1300 and 1304. Those paths were not all traced for a zero-as-missing branch, so the code read does not rule the report out on every path.
- The nullPolicy handling (NullPolicy enum, lines 148-177) applies to documents that have no value; the report's workaround matches that path.

Commands at the pinned head:
- git show origin/main:solr/core/src/java/org/apache/solr/search/CollapsingQParserPlugin.java | sed -n 1076,1077p: advanceExact on the numeric doc values.
- git grep -n 'advanceExact' origin/main -- solr/core/src/java/org/apache/solr/search/CollapsingQParserPlugin.java: the paths listed above.
- git log --oneline --grep='SOLR-14487' origin/main: no output.

## History

- Commits on origin/main naming SOLR-14487: none.

## Reproduction attempts

Not attempted. The check is a collapse on an integer field where one document has 0 and another has no value, and then the collapsed result. That needs a test in TestCollapseQParserPlugin, which was not run in this pass.

## Gap analysis

The numeric collector distinguishes a stored zero from a missing value at the pin, which points away from the reported behaviour. Whether another collector path, or the expand and boosted paths, still treats zero as missing was not traced line by line, and no run confirms the result.

## Verdict justification

keep-open meets the rubric: the pinned head sha is cited, the Jira status and date are read in this pass, code paths are cited with file and line, and the attempts and the missing piece are listed. close-cannot-reproduce does not hold: the code path argument is partial (not all advanceExact paths were traced) and no reproduction was run. close-fixed does not hold: no commit names the ticket. The missing pieces are (1) the trace of the remaining collector paths for a zero-as-missing branch, and (2) a run of TestCollapseQParserPlugin with a zero-valued and a missing-valued document.

## Limits

This record does not reproduce the behaviour. It does not trace every collector path. It does not confirm the report's test from the pull request.
