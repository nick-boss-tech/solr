# Skip record: SOLR-13346

- Ticket: SOLR-13346, Phrase matching on boolean queries.
- Verdict: keep-open.
- Draft branch: solr-13346-draft-open.
- Pinned head: fork origin/main 498a09aea97aac86c2cc7ac114113b946d249942. Code read at the pin in this pass.
- Source: the queue audit (round audit-1, 2026-10-04, confirmed-skip) is replaced by this pass.

## Ticket reading

JIRA read 2026-10-19 (this pass): Open, resolution None, created 2019-03-25, updated 2019-06-08, no fix versions, no links, no comments. The report: with the eDisMax parser, phrase matching (`pf`) is built over the whole query rather than over the sub-queries of boolean operators; the example is `(gear AND cycle) OR (black AND cycle)`.

## Code reading

Read at the pin:

- `solr/core/src/java/org/apache/solr/search/ExtendedDismaxQParser.java:297-321` (`addPhraseFieldQueries`): collects `normalClauses` from the parsed clause list for the whole query, skipping field clauses, phrase clauses, and the bare words `AND`, `OR`, `NOT`, `TO`. The phrase boosts are then built from that one list (`:341-348`, `addShingledPhraseQueries`). The grouping of a parenthesized sub-expression is not used when the list is built.
- So for the ticket's example the phrase shingles come from the flat list of the whole query, which is the behavior the ticket describes. The code reading agrees with the report.

Not checked in this pass: a run of the example through the parser at the pin, to see the exact phrase queries produced.

## History

- No commit at the pin names SOLR-13346 (evidence tool: none).
- The history of `addPhraseFieldQueries` was not traced in this pass.

## Reproduction attempts

Not run. The ticket describes a parser output that the code reading supports; no build or test run in this pass.

## Gap analysis

- The behavior is present at the pin. It is not obsolete and not fixed.
- Whether the phrase boost should be per boolean group is a design choice (the ticket is labeled as a feature). The ticket has no comments and no maintainer decision, so the rubric's pr-needed route (a change is needed) is not settled: the change would alter scoring for every query with boolean operators, which is a decision for the maintainers.
- Missing piece: a maintainer decision on per-group phrase boosting, and, if agreed, a test that shows the phrase queries for the example at the pin.

## Verdict justification

keep-open: the code path the ticket describes is present at the pin, but whether the scoring should change is an open design question with no decision in the ticket. A close verdict does not fit (the behavior exists); pr-needed needs the maintainers' decision on scoring first.

Missing for a closing or PR verdict: the maintainer decision on the scoring change, and the parser output for the example at the pin (a run or a unit test).

## Limits

This record does not run the parser. It does not trace the history of `addPhraseFieldQueries`. It does not post anything to JIRA or GitHub.
