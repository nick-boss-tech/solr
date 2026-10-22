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
