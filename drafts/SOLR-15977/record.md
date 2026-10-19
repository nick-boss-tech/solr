# Skip record: SOLR-15977

- Ticket: SOLR-15977, "Solr - portuguese synonyms are not working". JIRA status Open, resolution none, fix versions none, created 2022-02-03, updated 2024-12-20. Checked 2026-10-19 UTC.
- Verdict: keep-open.
- Draft branch: solr-15977-draft-open.
- Pinned head: fork origin/main 498a09aea97aac86c2cc7ac114113b946d249942. No upstream sha cited.
- Checked by: vm2 (pool Linux host), 2026-10-19 UTC, for the remediation redo of this ticket (claim claims/skip-SOLR-15977-vm2.md).

## Ticket reading

The reporter says Portuguese synonyms are ignored while English synonyms work. The schema file is described as attached; it is not in the report text, and it is not available to this pass. Comment 2022-05-17 (eribeiro) says the schema defines two Portuguese analyzers, and that the one in text_br applies a stemming filter before the synonym filter, which makes the synonyms hard to match. Comment 2024-12-20 (epugh) says the ticket does not meet the bar for a release blocker. No commit names the ticket.

## Code reading

- The behaviour depends on the analyzer chain in the user's schema, not on Solr code paths. There is no code change at the pinned head that the report points to.
- solr/solr-ref-guide/modules/indexing-guide/pages/filters.adoc:2591-2593: the RemoveDuplicatesTokenFilter section gives a token-stream example in which the synonym filter runs before the stem filter ("Synonym Filter to Stem Filter"). The example shows a synonym-first chain; it does not state a rule that stemming before synonyms breaks matching.
- The reference guide does not give an explicit rule on filter order for Portuguese analysis. The language-analysis page (language-analysis.adoc) covers Portuguese stemmers, but was not read for ordering advice in this pass.

Commands at the pinned head:
- git grep -n -i 'synonym' origin/main -- solr/solr-ref-guide/modules/indexing-guide/pages/ | grep -i 'stem\|order\|before\|after': the filters.adoc examples above.
- git grep -n 'SOLR-15977' origin/main: no output.

## History

- Commits on origin/main naming SOLR-15977: none.

## Reproduction attempts

Not attempted. The reporter's schema, the query, and the documents are not in the report text, so a run would test a guessed chain, not the reporter's. The comment's cause (stemming before synonyms) could be checked with a test chain on the pinned head, but that would not confirm the reporter's schema.

## Gap analysis

The likely cause is the analyzer order the commenter describes: a stemmer before the synonym filter changes the term that the synonym list must match. This is consistent with the guide's synonym-first example. The cause is not confirmed for this report, because the schema attachment is not available.

## Verdict justification

keep-open meets the rubric: the pinned head sha is cited, the Jira status and date are read in this pass, code paths are cited with file and line, and the attempt and the missing piece are listed. The missing piece is the reporter's schema (the attachment), so the analyzer order can be checked against their chain. A close-answered verdict needs the reference guide to answer the question explicitly, which the guide does not do for this ordering; a close-answered would also need the reporter's schema confirmed.

## Limits

This record does not confirm the reporter's analyzer chain. It does not show a failing synonym match on the pinned head. It does not establish a general ordering rule from the guide.
