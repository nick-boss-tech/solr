# Lucene block join repair prepared for Nick's review

Status: IMPLEMENTED, PROOF PENDING. No Lucene PR, issue or maintainer comment has been published.

- Fork created and separately verified: https://github.com/nick-boss-tech/lucene (parent apache/lucene).
- Local checkout: `C:\Users\shaninna\dev\Solr-issues\lucene`; sparse checkout of `lucene/join` and repository root files. `origin` is Nick's fork; `upstream` is apache/lucene.
- Base: main `aa366ae209ae8296dd70d2835c6d47f8fc201972`.
- Branch: `fix-block-join-missing-values`.
- Pushed head: `c015935c04a39bda74c898eb80a3e5d6c0a0eb8c`, independently confirmed by the fork ref API.
- [Pinned diff](https://github.com/nick-boss-tech/lucene/compare/aa366ae209ae8296dd70d2835c6d47f8fc201972...c015935c04a39bda74c898eb80a3e5d6c0a0eb8c).

## Problem and repair

`ToParentDocValues` counted all physical siblings when deciding whether a child was missing a value. It now visits selected children and asks the underlying doc values whether each selected child has a value. Excluded siblings therefore cannot inject the numeric missing sentinel. When missing values do not need to participate (numeric null sentinel, or a string selection in which missing cannot win), the existing conjunction of children and valued documents is retained to avoid scanning missing children unnecessarily.

When a missing string wins MIN/MAX selection, `SortedDocValues.advanceExact` now returns false, and `nextDoc`/`advance` skip that absent value. `ordValue` returns real ordinals only. This preserves the doc-values contract and lets the sort comparator apply its separately configured parent missing-value policy. No public API is added.

The shared iterator tracks the selected child stream, preserves repeated advanceExact calls, handles a false advanceExact on a non-parent before subsequent nextDoc/advance, and retains the existing fallback for parents with no valued matching children. The production change affects both numeric and string consumers of BlockJoinSelector; both are covered.

## Exact outbound files and test reuse

1. `lucene/join/src/java/org/apache/lucene/search/join/ToParentDocValues.java`.
2. `lucene/join/src/test/org/apache/lucene/search/join/TestBlockJoinSelector.java`: reuse CannedSortedDocValues/CannedNumericDocValues and bitset fixtures. Correct assertions that formerly counted excluded siblings as missing; enforce absence instead of invalid sentinel ordinals; add numeric filter and sorted absence cases for MIN/MAX, nextDoc/advance/advanceExact, repeated calls, empty parents, all-missing children and ignored children with values.
3. `lucene/join/src/test/org/apache/lucene/search/join/TestBlockJoinSorting.java`: extend the existing real-index missing-value test to exercise MAX string selection with null/FIRST/LAST parent policies in both directions. Add an actual numeric sort with an excluded missing sibling and a genuinely missing matching child, in both directions.

No new fixture, dependency, public API or Solr source change. No changelog placeholder: Lucene's guide expects a GITHUB issue/PR number in CHANGES.txt, which does not exist yet. Add the proper entry after a real number is assigned, with fresh checks for the resulting head.

## Evidence and remaining work

Static diff/whitespace, scoped staging, identity, ancestry and remote read-back checks passed. Windows ran no build, formatter, compiler or tests. New Lucene test outcomes are unknown until the Linux gate runs. Source correctness, architectural fit and reuse were reviewed; runtime, randomized iterator behavior and execution cost remain subject to Linux proof.

The earlier Solr receipt reproduces both defects on the unpatched 10.6 stack. Its 10.4 addendum establishes the filtered-child pass-to-fail regression, string missing-first wrong-order-to-exception change, numeric missing-policy improvement, and string missing-last pass on both. Those results do not prove this new Lucene repair.

Gate: `gates/lucene-block-join-missing-values-round-1.md`; receipt expected at `receipts/lucene-block-join-missing-values-round-1.md`. Main requires JDK 25. A maintenance backport/build compatible with Solr's Lucene 10.6/JDK 21 dependency remains necessary before Solr can consume a released repair; no such integration result is claimed.

## Contribution ownership

Lucene CONTRIBUTING.md targets main; maintainers handle maintenance backports. Its AI_POLICY.md permits coding assistance but requires human understanding and ownership. Nick must inspect and understand the patch, write the public PR body/comments in his own words, and submit it himself. No autonomous PR or AI-written Lucene maintainer message is prepared here. This technical handoff is an internal review aid, not ready-to-post PR prose.

Contributor status is sufficient to submit a fork PR. An eligible maintainer can review/approve and merge it. GitHub prevents a PR author from approving their own PR; the repository's other review requirements still apply. Nothing here claims that changing the author reduces the required substantive review.
