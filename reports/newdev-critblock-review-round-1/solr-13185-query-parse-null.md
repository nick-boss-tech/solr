# Review: SOLR-13185, branch solr-13185-query-parse-null (unit 30)

Head reviewed: 84523828bba8eb780472d037af3ff60a919d34eb. Base: c8ab71430f. Ticket: SOLR-13185, "NPE in query parsing because of missing null check".

## Pass 1

- Ticket fit. A `complexphrase` query with no query string reaches `getString()` as null and fails in `parse()` with an NPE. The branch returns a null query for that case. The commit title says the result is "no query". That fits the ticket's NPE.
- Scope. One line in `ComplexPhraseQParserPlugin.java` and one test. Nothing else.
- Finding, major: the test checks only that the response has a `//result` element. It does not pin what a null query returns (no documents, or all documents). Whether a null `Query` gives the user the intended result is not shown by the branch. Recorded, not repaired (a stronger assertion is outside the repair scope).
- Finding, minor: no changelog file. Recorded, not repaired.
- Proof leg: on base the test fails with the ticket's NPE ("s" is null). The proof is real.
- Repairs: none.

Regate: `gates/SOLR-13185-review-pass-1.md`, GATE GREEN at 84523828bb on vm4.

## Pass 2

Review (2026-10-22, vm4), same head 84523828bb. - Open pass 1 finding, major, confirmed in code. `QueryComponent` normalizes a null parsed query to `MatchNoDocsQuery` (`solr/core/src/java/org/apache/solr/handler/component/QueryComponent.java:191-195`), so the intended result of a query with no string is zero documents. The test asserts only that a `//result` element exists, so it would still pass if the query matched everything. A `numFound='0'` assertion would pin the result. Recorded, not repaired (a changed assertion is a test change outside the repair scope).
- Open pass 1 finding, minor, unchanged: no changelog file. Not repaired.
- Repairs: none.

Regate: `gates/SOLR-13185-review-pass-2.md`, GATE GREEN at 84523828bb on vm4.

## Pass 3

Review (2026-10-22, vm4), same head 84523828bb. - No new findings against the head. The pass 1 and pass 2 findings stand: the test does not assert zero results for the null query, although Solr normalizes a null query to MatchNoDocsQuery (`QueryComponent.java:191-195`); no changelog file.
- Repairs: none.

Regate: `gates/SOLR-13185-review-pass-3.md`, GATE GREEN at 84523828bb on vm4.

Final head: 84523828bba8eb780472d037af3ff60a919d34eb. Unit 30 DONE with the three pass sections.
