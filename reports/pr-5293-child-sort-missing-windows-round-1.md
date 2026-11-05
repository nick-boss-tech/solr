# SOLR-18622 childfield test patch and review correction, Windows round 1

Status: TEST PATCH PUSHED; LINUX REPRODUCTION REQUIRED. No production fix or runtime pass is claimed.

- Recorded: 2026-11-05 02:35 UTC; host: windows, Codex desktop.
- Authority: Nick requested patches for a PR-author handoff after the review of [PR 5293](https://github.com/apache/solr/pull/5293).
- Claim: `claims/pr-5293-child-sort-missing-windows-round-1.md`, posted at board commit `3b1946cc230b0de0e84dfc2757eba612c89e4d10`.
- Contributor parent and unchanged live PR head: `978e2b1e352c7c39a68f7b952e6ab12eabf80890`.
- Fork branch: `pr-5293-solr-18622-child-sort-missing`.
- Pushed head: `769ca6e177d77acd3f1993a4a24bd3257ae243c0`, separately confirmed through the fork ref API. Author: Nick Shanin. Contributor ancestry preserved, ordinary push, no public PR head changed.
- [Pinned diff](https://github.com/nick-boss-tech/solr/compare/978e2b1e352c7c39a68f7b952e6ab12eabf80890...769ca6e177d77acd3f1993a4a24bd3257ae243c0).

## Review correction and scope decision

The earlier local review alleged that a null child missing-value argument newly skipped children compared with Lucene 10.4. That finding is withdrawn. Actual 10.4 source already intersects children with values. Lucene 10.6's five-argument constructor delegates with null childMissingValue as well. The constructor Javadoc was insufficient evidence for the original claim.

The contributor's last commit deliberately keeps the parent missing-value argument null. Replacing it with the schema sentinel would reverse the author's explicit choice. The initial claim's proposed production edit is therefore withdrawn by this add-only report. Production code is unchanged; the implementation slice is executable test coverage and a Linux reproduction handoff. This is settled by decision ladder rung 1, source and commit history, with no owner decision needed.

## Delivered patch

Exactly two files, 93 added lines:

- `solr/core/src/test/org/apache/solr/search/join/TestNestedDocsSort.java`: four methods exercise int/long/float/double default, missing-first and missing-last settings; string first/last; both directions; mixed/all-missing children; empty parents; no matching child; excluded siblings. Parent fallback is pinned independently of schema child policy.
- `solr/core/src/test-files/solr/collection1/conf/schema.xml`: eight first/last numeric fields using existing point field types with guaranteed doc values. Existing schema, string fields and default numeric dynamic fields are reused. The alternative schema-sorts fixture was examined; retaining this suite's current schema avoids unrelated fixture changes.

No new public API, dependency, production code, security boundary, changelog, or documentation. The full original PR adaptations were reviewed in the earlier pass; this follow-up inspected the join call path, old/new release implementations and contributor history. Vector Explicit query migration and the shared grouping adapter remain appropriate on static review. Existing upstream CI has seven passes at the contributor head, not this test commit.

## Source concerns requiring Linux

1. Lucene 10.6 `ToParentDocValues.advanceExact` counts physical siblings at lines 310-333 but accumulates only filtered children with values. An excluded sibling without the field appears to trigger the numeric missing sentinel. `testFilteredChildrenDoNotSupplyMissingValues` distinguishes this from a real missing matching child.
2. `SortedDVs.ordValue` at lines 95-103 can return `Integer.MAX_VALUE` for MAX selection with STRING_LAST; `TermOrdValComparator.copy` treats only -1 as missing and otherwise looks up the ordinal. Descending string missing-first settings exercise that path in `testStringChildMissingFirst`.

Both are unverified runtime concerns. The new tests may expose dependency defects and are not asserted to pass. The next required step is Linux reproduction, with no established production change for Windows to implement. Do not call this a tested fix offer.

## Verification and public state

Windows ran no Gradle, tests, formatter, or compiler. Scoped diff/whitespace checks, exact staged-file checks, contributor ancestry, clean push and separate remote SHA verification passed. The PR head and empty conversation/reviews/inline-comments were refreshed at 2026-11-05 02:35 UTC. No equivalent offer exists and no comment has been posted.

Linux job: `gates/pr-5293-child-sort-missing-round-1.md`; execution-host receipt path: `receipts/pr-5293-child-sort-missing-round-1.md`. The public comment is drafted locally with the pinned diff and fast-forward commands. It states the runtime limitation. Public posting remains pending proof or Nick's explicit approval of that limited draft; standing clean-gate authorization is not yet satisfied.
