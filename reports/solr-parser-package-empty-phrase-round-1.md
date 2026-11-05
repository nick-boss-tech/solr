# Draft package: SOLR-18314 eDisMax empty phrase cleanup

Status: DRAFT DELIVERED; runtime proof reported green, two evidence clarifications and submission strategy pending. Prepared 2026-11-05, not yet reviewed by Nick. No public write.

Ticket disposition: EXISTING SOLR-18314. Proposed title/body below describe only its safe eDisMax portion. They are NOT a replacement for the live ticket's broader DisMax/single-term proposal. Prefer coordination with Ian Springer's OPEN PR 4652 before opening a competing PR. David's November 3 comment accepts the empty-clause portion but is hesitant about the DisMax ranking change: https://github.com/apache/solr/pull/4652#issuecomment-6608422285 . Preserve Ian's credit. A separate linked ticket is an option only after owner/maintainer agreement; no new key invented.

Branch `solr-18314-edismax-empty-phrase`, head E3 `1ea8ceb2d5253fef6c2e05a3ee800687dea700ba`, ancestor/base P `0af4f5b3f2784eba3693acdc33dc49f6c2802b6a` (OPEN PR 5294). [Three-file delta](https://github.com/nick-boss-tech/solr/compare/0af4f5b3f2784eba3693acdc33dc49f6c2802b6a...1ea8ceb2d5253fef6c2e05a3ee800687dea700ba).

Outbound: `changelog/unreleased/SOLR-18314-edismax-empty-phrase.yml`, `solr/core/src/java/org/apache/solr/search/ExtendedDismaxQParser.java`, `solr/core/src/test/org/apache/solr/search/TestExtendedDismaxParser.java`. Changelog credits Ian Springer and Nick Shanin. PR 4652 remains OPEN at `c122384ba230eecd0a6f2ba13a15c02908c9f0fc`; E3 is an extraction based on P, not a fast-forward offer to Ian's branch. Arrange the approved delivery after PR 5294 or as an explicit dependent proposal and verify any new source head. No branch changed here.

Evidence clarifications: the job names root tidy but the receipt records `:solr:core:tidy`; executor must establish the requested root coverage or run the missing check. Add exact preserved log/XML locations for round 3. Do not modify the executor's receipt or rerun valid tests solely to document locations.

## PR title (if a separate extraction is agreed)

`SOLR-18314: Omit empty eDisMax phrase boosts while preserving analyzed phrases`

## PR body

🤖 *AI text below* 🤖 *(prepared for review by Nick Shanin)*

https://issues.apache.org/jira/browse/SOLR-18314

## Description

**Empty eDisMax phrase boosts can prevent the match-all query simplification.** With `q=*:*` and `pf` configured, an empty optional clause keeps the parsed query as a BooleanQuery. A simple analyzed single term can also leave an empty boost clause.

## Solution

**Skip absent eligible clauses and omit empty analyzed phrase queries.** Keep analysis for a single raw clause: `wi-fi` and a multiword synonym can still produce a meaningful phrase. [Implementation and tests](https://github.com/nick-boss-tech/solr/compare/0af4f5b3f2784eba3693acdc33dc49f6c2802b6a...1ea8ceb2d5253fef6c2e05a3ee800687dea700ba).

**This extracts the eDisMax portion of Ian Springer's [PR 4652](https://github.com/apache/solr/pull/4652).** It leaves classic DisMax boosts unchanged and claims no measured CPU improvement. The changelog credits Ian and Nick. The broader single-term ranking change remains separate.

## Tests

<details>
<summary>Linux results at 1ea8ceb2d5253fef6c2e05a3ee800687dea700ba; formatting reconciliation pending</summary>

- Seed `18314E`, JDK 21, Gradle 9.7.0: eDisMax 43/43, DisMax 3/3 and multiword synonyms 6/6, no failures, errors or skips.
- Two empty-clause regressions fail on original production. The wi-fi and existing grackle-synonym controls pass there in both `sow` modes. A temporary one-raw-clause early return makes each preservation control fail independently; tests remain unchanged.
- Core tidy and Error Prone compilation passed. Changelog generation parsed the entry; the standalone validator crashed before reading entries. Root `check -x test` passed after repairing a corrupted downloaded UI zip with the exact pinned checksum and retrying a transient Antora download.
- The recorded tidy command is module-scoped; confirmation of the requested root tidy coverage and exact log/XML locations remains pending. [Verification record](https://github.com/nick-boss-tech/solr/blob/357ab091ff6e93a0d0ffa1b9129f482828b51844/receipts/SOLR-18314-existing-synonym-control-round-3.md).

</details>

**The branch is stacked on [PR 5294](https://github.com/apache/solr/pull/5294).** Coordinate delivery with PR 4652 and reconcile the submission base before opening it. This does not settle all single-term phrase-boost behavior.

Changelog: [eDisMax empty phrase cleanup](https://github.com/nick-boss-tech/solr/blob/1ea8ceb2d5253fef6c2e05a3ee800687dea700ba/changelog/unreleased/SOLR-18314-edismax-empty-phrase.yml).

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

## Jira title (proposed scoped wording for existing ticket)

Omit empty eDisMax phrase boosts without dropping phrases produced by analysis

## Jira body (proposed scoped addition, not replacement; Jira wiki markup)

The eDisMax empty-clause portion can be handled separately from SOLR-18314's broader proposal to remove single-term DisMax phrase boosts. With {{q=*:*}} and {{pf}} configured, an empty optional phrase clause prevents the match-all simplification. Empty analyzed boost queries should not be added.

Acceptance criteria for this portion:
 * Skip phrase generation when there are no eligible clauses.
 * Do not attach an empty analyzed phrase query.
 * Keep a real phrase boost when a single raw clause analyzes to several tokens, including {{wi-fi}} and the existing {{grackle}} synonym, in both {{sow}} modes.
 * Leave classic DisMax scoring unchanged.

Candidate {{1ea8ceb2d5253fef6c2e05a3ee800687dea700ba}} passed 52 tests. Both empty-clause regressions fail on original production; both preservation controls pass there and independently fail with an over-aggressive one-clause skip. Core tidy, Error Prone and root checks passed. Root tidy coverage and exact evidence locations still need reconciliation; no CPU gain or released-version range was measured.

This is an extraction from Ian Springer's [PR 4652|https://github.com/apache/solr/pull/4652], credited to Ian and Nick, and depends on the test baseline in [PR 5294|https://github.com/apache/solr/pull/5294]. Agree delivery with the original author before a separate PR. The ticket's classic DisMax ranking proposal remains unresolved and is not silently removed by this scoped addition.

🤖 _AI text above_ 🤖 _(prepared for review by Nick Shanin)_
