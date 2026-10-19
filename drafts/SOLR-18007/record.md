# SOLR-18007 closeout record

Verdict: keep-open (draft branch `solr-18007-draft-open`).

- Pinned head: fork `origin/main` `498a09aea97aac86c2cc7ac114113b946d249942`. Upstream history is not cited.
- JIRA checked 2026-10-19: status Open, no resolution, no fix versions, created and updated 2025-11-30. Description read in full (about 800 characters). No comments.
- Audit verdict on record: `deferred` (audit-1, 2026-10-04): maintainer CI flake, NoSuchFileException from Lucene in the suggester reload. Needs bisect or log data; no deterministic guess; not retried.

## Code paths read at the pinned head

- `solr/core/src/test/org/apache/solr/spelling/suggest/TestAnalyzedSuggestions.java`: exists; the test the audit names.
- `solr/core/src/test-files/solr/collection1/conf/solrconfig-phrasesuggest.xml:280-285`: the spellchecker named in the ticket (`fuzzy_suggest_analyzing_with_non_fuzzy_prefix_4`) and its `storeDir`.
- `solr/core/src/java/org/apache/solr/spelling/suggest/SolrSuggester.java:143` (reload from the stored lookup file) and `:196` (`reload()`): the reload path where the exception surfaces in the ticket's log.

## Attempts

- Searched the ticket for named test classes and identifiers: the evidence tool found none in the description. The test and the spellchecker were found by reading the audit note and the config.
- No test run. The ticket describes a flake seen once on CI and not reproduced by the reporter. A run of the suggester tests would show whether they pass, but not whether the exception from the report still occurs, without the failing seed or log.

## What the verdict does not cover

- Whether the exception still happens on main. Not tested.
- The CI log, the seed, or the commit where the failure was first seen. None are on the ticket.

## What discharges keep-open

The failing CI log or a seed from the report, plus the commit it was seen on. With either, a focused run on the pinned head can separate "still happens" from "fixed by an unrelated change".
