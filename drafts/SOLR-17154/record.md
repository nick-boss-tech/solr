# Skip record: SOLR-17154

- Ticket: SOLR-17154, "solr jdbc SQL encoding error". JIRA status read 2026-10-19 (UTC): Open, resolution none, created 2024-02-07, updated 2024-02-28, no fix version. Two comments: tuannh982 (2024-02-27) names Calcite CALCITE-2616 and a workaround that overrides the default charset; donghwan1020 (2024-02-28) reports that `=` works and `LIKE` does not.
- Verdict: keep-open. Unchanged from the earlier record.
- Draft branch: solr-17154-draft-open.
- Pinned head: fork origin/main 498a09aea97aac86c2cc7ac114113b946d249942 (2026-10-07). Upstream sha not cited.
- Checked by: vm4, 2026-10-19 13:53-13:53 UTC.

## Ticket reading

Through the Solr JDBC driver, a SQL `LIKE` with Korean text fails with a conversion error about the literal's charset. An equality test with the same text works, and English works. The Solr-side query is `SELECT a_s FROM collection1 WHERE a_s LIKE '...'`, run through the Calcite-based driver.

## Code reading

- `gradle/libs.versions.toml:24-25`: `apache-calcite = "1.42.0"`, `apache-calcite-avatica = "1.28.0"`. The pinned head uses Calcite 1.42.0.
- A search of the Solr SQL handler and the SolrJ SQL/JDBC code at the pin for `charset`, `ISO-8859`, `ISO_8859` and `UTF` (`solr/solrj/src/java/org/apache/solr/client/solrj/io/sql`, `solr/core/src/java/org/apache/solr/handler/sql`, `solr/core/src/java/org/apache/solr/handler/SQLHandler.java`): no hits. Solr sets no literal charset of its own.

What this shows at the pinned head: the literal's charset is decided by Calcite, and Solr does not set it. The upstream cause the ticket's comment names is CALCITE-2616 (literal charset always ISO-8859-1), which is the Calcite behavior the comment describes.

## History

- CALCITE-2616 on the Apache JIRA, read 2026-10-19 through the anonymous REST API: status Open, resolution none, no fix version. So the Calcite release Solr pins (1.42.0) does not carry a fix that the JIRA records.
- No commit on the pinned head names SOLR-17154 (evidence tool: no hits).

## Reproduction attempts

Not attempted. Reproducing the failure needs the JDBC driver and a collection with Korean text, which means a Solr test run; this pass is review-only.

## Gap analysis

The failure is in Calcite's literal handling, which Solr does not override. Solr-side, a workaround may exist: the comment says the default charset can be overridden in Calcite, and Solr might pass that setting through its JDBC connection properties. That has not been checked against the Calcite 1.42 property names or tested, so it is not a verified Solr fix.

Missing pieces for a verdict change: (1) a test on the pinned head that shows the `LIKE` failure and whether the charset override in the JDBC connection removes it; (2) the Solr owner's decision on whether to carry that override in Solr or wait for Calcite.

## Verdict justification

keep-open meets the rubric: the code path is read (Calcite 1.42.0 pin; no Solr charset code), the upstream status is checked (CALCITE-2616 open), and the missing pieces are named (a tested workaround and the decision on it). close-fixed is not met: nothing fixes this on main. pr-needed is not met yet: a Solr change would be a workaround, and it has not been shown to work.

## Limits

This record does not reproduce the failure and does not test the workaround. It does not change the Calcite JIRA. It does not post anything to JIRA or GitHub.
