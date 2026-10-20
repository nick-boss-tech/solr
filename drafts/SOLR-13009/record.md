# Skip record: SOLR-13009

- Ticket: SOLR-13009, ExtendedSolrQueryParser cannot handle with KeywordRepeat, Synonyms and minShouldMatch.
- Verdict: keep-open.
- Draft branch: solr-13009-draft-open (verdict unchanged; this record replaces the audit-derived record on this branch).
- Pinned head: fork origin/main 498a09aea97aac86c2cc7ac114113b946d249942. Upstream sha not cited.
- Checked by: vm5 (pool Linux host), 2026-10-20 UTC. Redo of remediation batch 07, item 20 (`assignments/pool-aaa-skip-verdict-remediation-07.md`).

## Ticket reading

JIRA read 2026-10-20: Open, no resolution, created 2018-11-22, updated 2022-05-12, fix version 8.2 set, no links, no comments. The report: on a field with a synonym set `[bier,brouw]` and a KeywordRepeat stemmer, `defType=edismax` with `mm=2<-1 5<-2 6<90%` returns the desired result for `q=bier` and `q=brouw` alone but not for a query that combines them, because the minShouldMatch counts differ for graph-token positions.

## Code reading

Read at the pin:

- `solr/core/src/java/org/apache/solr/search/ExtendedDismaxQParser.java:1106-1127`: the extended parser passes `enableGraphQueries` through to the clause builder, so graph (synonym) queries are built for edismax at the pin. The `mm` counting for graph tokens is in the same builder; the reading did not trace the minShouldMatch count for multi-position tokens.
- `dbd22a6ada` (SOLR-10423) introduced the `enableGraphQueries` schema switch; it is an ancestor of the pin and is not the fix for this ticket.
- No commit at the pin names SOLR-13009 (the evidence tool lists none), and `changelog/archive.md` has no entry for it.

## History

- The fix version 8.2 on the ticket is not evidence of a fix: no commit on the fork names the ticket, and the changelog does not record it.
- The minShouldMatch and graph-query history was searched by subject only.

## Reproduction attempts

Not run. This pass ran no builds or tests. The missing piece is a run at the pin: a field type with the synonym `[bier,brouw]` and KeywordRepeat, the query `q=bier brouw` (and each term alone) with `defType=edismax&mm=2<-1 5<-2 6<90%`, comparing the parsed query and the result counts.

## Gap analysis

- The fix version says 8.2, but the rubric needs the fix commit, its ancestry, and the changed code at the pin; none is identified.
- The graph query path is enabled at the pin, so the ticket's question is about the `mm` count for multi-position tokens, which the reading did not trace.
- The audit's "fixed upstream" note rests on the fix version alone.

## Verdict justification

keep-open: the ticket reports a defect with a fix version but no identified fix commit, and the minShouldMatch behavior for graph tokens has not been run or fully traced. close-fixed needs the fix commit named and checked; close-obsolete does not fit (the path is present).

Missing for this verdict: the fix commit for the 8.2 fix version (or a confirmation that none is on main), and a run of the example at the pin.

## Limits

This record does not establish a reproduction. It does not post anything to JIRA or GitHub. Commit linkage is by commit title or code path, as stated above.
</content>
