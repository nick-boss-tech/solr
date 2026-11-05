# Draft package: explicit phrase slop and nested parser state

Status: DRAFT DELIVERED; source proof green, submission base and owner review pending. Prepared 2026-11-05. No public write.

Ticket disposition: EXISTING SOLR-12532 and SOLR-15611. One coherent PR addresses shared phrase-construction state. Do not create duplicate Jira tickets or claim to replace either full description silently. Each has its own proposed title/body below. Historical PR 280 for SOLR-15611 is CLOSED, not a merged resolution; reference its prior approach during submission review.

Branch `solr-12532-15611-phrase-slop`, head `44849d2c4ca0cae283c26bc45048f824ea0d53c4`, parent P `0af4f5b3f2784eba3693acdc33dc49f6c2802b6a`, the unchanged OPEN draft PR 5294. [Four-file delta](https://github.com/nick-boss-tech/solr/compare/0af4f5b3f2784eba3693acdc33dc49f6c2802b6a...44849d2c4ca0cae283c26bc45048f824ea0d53c4).

Outbound files: `changelog/unreleased/SOLR-12532-15611-explicit-phrase-slop.yml`, `solr/core/src/java/org/apache/solr/parser/SolrQueryParserBase.java`, `solr/core/src/java/org/apache/solr/search/ExtendedDismaxQParser.java`, `solr/core/src/test/org/apache/solr/search/TestMultiWordSynonyms.java`. Recommend submitting after PR 5294 lands, then arrange only this delta against updated main and obtain fresh proof if the commit changes. Alternatively use an explicitly dependent review. Do not silently include PR 5294's test repair in an independent main-based PR. No branch changed here.

## PR title

`SOLR-12532: Preserve explicit phrase slop during construction and after nested queries`

## PR body

🤖 *AI text below* 🤖 *(prepared for review by Nick Shanin)*

https://issues.apache.org/jira/browse/SOLR-12532

https://issues.apache.org/jira/browse/SOLR-15611

## Description

**Graph phrases can lose explicit slop, including after a nested parser.** Updating only a completed PhraseQuery misses graph query forms. Parser state from an earlier nested clause can also suppress later phrase handling.

## Solution

**Build each phrase with its requested slop and restore the previous setting afterward.** The base parser clears the prior nested-parser guard before constructing the phrase; the eDisMax path also applies slop during construction. A nested parser still owns its own phrase settings. [Scoped changes](https://github.com/nick-boss-tech/solr/compare/0af4f5b3f2784eba3693acdc33dc49f6c2802b6a...44849d2c4ca0cae283c26bc45048f824ea0d53c4).

## Tests

<details>
<summary>Linux verification at 44849d2c4ca0cae283c26bc45048f824ea0d53c4</summary>

- JDK 21.0.12.1, Gradle 9.7.0, seed `1253215611`: 94 tests passed across multiword synonyms, eDisMax and both parser suites; zero failures, errors or skips. Original-seed `testPf` also passed 1/1 at `B3B4534A1A4D09F4`.
- With original production restored and tests unchanged, four regressions fail at intended assertions; the nested-parser ownership control passes. Two separate mutations removing slop restoration each fail the compound-query assertion.
- Root tidy, YAML parse, Error Prone compilation and root `check -x test` passed, source clean; `NODE_ENV` was unset. A detached tidy retry completed after an interrupted preflight that ran no tests.
- [Verification record](https://github.com/nick-boss-tech/solr/blob/357ab091ff6e93a0d0ffa1b9129f482828b51844/receipts/SOLR-12532-15611-phrase-slop-round-1.md).

</details>

**This branch is stacked on [PR 5294](https://github.com/apache/solr/pull/5294).** Submission-base reconciliation remains pending. The guard-reset line is a coverage pin, not independently mutation-proven. General synonym-dependent phrase-boost ranking is separate work. Earlier [PR 280](https://github.com/apache/solr/pull/280) addressed the nested-parser case and closed without merging.

Changelog: [explicit phrase slop](https://github.com/nick-boss-tech/solr/blob/44849d2c4ca0cae283c26bc45048f824ea0d53c4/changelog/unreleased/SOLR-12532-15611-explicit-phrase-slop.yml).

## Checklist

- [ ] I have reviewed the contribution guidelines and this final submission.
- [ ] The Jira identity and PR title are confirmed for submission.
- [ ] Maintainer access to the fork branch is confirmed.
- [ ] The final submission base is main, or its dependency is explicit.
- [ ] The final head has completed the required checks described above.
- [x] Tests for this change are included in the prepared slice.
- [ ] Reference Guide documentation is included where required.
- [ ] Changelog treatment is confirmed for the final submission.

*AI-generated description. Nick Shanin directed the work and takes responsibility for this contribution.*

## Jira title/body for SOLR-12532 (proposed existing-ticket wording)

Title: Preserve explicit query-string slop when phrase analysis produces a graph

Body (Jira wiki markup):

Explicit slop such as {{~2}} must be used while constructing a phrase whose analyzer produces a token graph. Applying slop only to a completed {{PhraseQuery}} or {{MultiPhraseQuery}} does not cover graph-derived query forms. The original ticket's WordDelimiterGraphFilter case remains relevant; the prepared regression tests also use the existing multiword-synonym fixture.

Acceptance criteria:
 * Lucene and eDisMax graph phrases honor explicit slop.
 * Explicit slop overrides {{qs}} for that phrase.
 * Slop does not leak into subsequent phrases in a compound query.
 * Nested-parser-owned phrases retain their own settings.

The same PR also addresses [SOLR-15611|https://issues.apache.org/jira/browse/SOLR-15611]. Candidate {{44849d2c4ca0cae283c26bc45048f824ea0d53c4}} passed 94 parser/synonym tests and an original-seed compatibility test, tidy, Error Prone and root checks. Four regressions fail on original production; two isolated mutations expose slop-restoration failures. The separate guard reset is not independently mutation-proven. The branch depends on [PR 5294|https://github.com/apache/solr/pull/5294]; final main-based submission and release targeting remain pending. No broader ranking or performance claim is made.

🤖 _AI text above_ 🤖 _(prepared for review by Nick Shanin)_

## Jira title/body for SOLR-15611 (proposed existing-ticket wording)

Title: Preserve phrase slop after nested query parsing

Body (Jira wiki markup):

A nested query must not leave parser state that suppresses explicit slop on a later phrase in the same query. The ticket's example {{_query_:"{!term f=text}foo" "foo bar"~1}} establishes the expected boundary: the term sub-parser owns its clause, while the following phrase owns its slop.

Acceptance criteria:
 * A phrase after a nested query honors its explicit slop, including graph analysis.
 * A nested parser retains its own phrase settings.
 * Per-phrase slop is restored before parsing the next clause.

Prepared together with [SOLR-12532|https://issues.apache.org/jira/browse/SOLR-12532] at {{44849d2c4ca0cae283c26bc45048f824ea0d53c4}}. The combined 94-test run passes; the graph phrase after a sub-query fails on original production, while the ordinary first assertion already passes. Do not interpret that result as independent proof of the guard-reset line. The nested ownership control passes and the two restoration mutations discriminate. Earlier [PR 280|https://github.com/apache/solr/pull/280] closed without merging. The current branch is stacked on PR 5294; final submission-base and release decisions remain pending.

🤖 _AI text above_ 🤖 _(prepared for review by Nick Shanin)_
