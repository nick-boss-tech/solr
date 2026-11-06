# PR 5303 round-2 proof review and posted coverage offer

Review date: November 5, 2026, Eastern. Public comment posted 2026-11-06 00:50:06 UTC (November 5 Eastern).

## Revisions and receipt

| Item | Verified revision |
| --- | --- |
| Live PR 5303 head H2 | `fd106e02428bf1d619128029c73ea053fb4aedcb` |
| Live target-base tip B2 | `e1799d8a71726bd8ec2ad69f1e3a4b5c4f3e019f` |
| H2 parent / merge base B1 | `1fc38d1e40cb12512aba2b1990b22b04111dad2d` |
| Tested/offered F2 | `07328d6973c4bb3385ca6d50bfe871f8808c1550`, immediate child of H2 |
| Fork branch | `nick-boss-tech/solr:pr-5303-phrase-coverage-round-2` |
| Earlier candidate, unchanged | `256612b7f85dc831f9332d51182258d9594fcc1f`, not the offered head |
| Reviewed board snapshot | `9981127c1ae999d442c5f15bca806aa9896501ef` |
| Required job / actual result | gates/pr-5303-phrase-coverage-round-2.md / receipts/PR-5303-phrase-coverage-round-2.md |

The executor already handled Ian's amended head through a separate source-refresh claim, new ordinary candidate commit and superseding exact-head gate. No duplicate implementation or test run was started here. The round-1 receipt path is absent because the actual result is round 2; a missing round-1 filename is not missing proof. Both source-refresh and execution claims have additive DONE closeouts.

## Source, architecture and reuse assessment

Fetched H2 matches the live PR head; fetched F2 was separately verified through the fork refs API and native Git. Native parent lookup confirms F2's immediate parent is H2. F2 is authored/committed as Nick Shanin with no attribution trailer. H2..F2 touches exactly TestExtendedDismaxParser.java (+62/-21), and the full diff was read. It preserves the author's new pf/pf2/pf3/all-stopword-bq MatchAll checks, retains wireless, separates and expands wi-fi, and adds both-mode fielded-only and configured grackle graph coverage. qf=id isolates grackle's subject phrase to pf. The obsolete standalone MatchAll addition was correctly omitted because H2 already has broader coverage.

Production, changelog and configuration are inherited unchanged from H2. Existing schema, synonym mapping and request helpers are reused; no helper, analyzer fixture, public hook/API, dependency or security boundary is introduced. No additional concern was found in the offered test-only delta. This is not a fresh overall production/security approval of the broadened PR. The tests cover existing behavior; they are not a new production fix or performance claim.

## Exact-head proof review

The full actual receipt was compared with the superseding job:

- H2 selected suites: 157 tests, zero failures/errors/skips (ExtendedDismax 40, QueryEquality 113, DisMax 3, stable MultiWordSynonyms query-shape method 1).
- F2 same selected suites: 160 tests, zero failures/errors/skips (ExtendedDismax 43). All five required methods executed; no duplicate standalone MatchAll case.
- Root tidy, Error Prone compilation and standalone-clone root check passed at exact F2 with NODE_ENV unset; source stayed clean. Unlike the earlier E3 receipt, this receipt explicitly attests root tidy.
- M1 pre-PR parser: three intended regression failures and two passing preservation controls.
- M2 false-mode-only empty phrase regression: three intended false-mode failures after true-mode checks pass; both preservation controls pass.
- M3 one-raw-clause shortcut: both preservation controls fail as intended in true mode.
- M4 false-mode-only shortcut: both controls pass true mode and fail false mode as intended.
- M5 false-mode-only empty bq: retained MatchAll test fails at its intended false-mode assertion after true-mode checks pass.

All five mutations compiled, preserved the exact F2 test blob, reached runtime assertion failures with no test errors/skips and were restored. Raw logs/XML/mutation manifests are identified in the Linux receipt; they were not independently rerun on Windows. The proof is selected-suite plus root-check coverage, not a full Solr suite, benchmark or allocation measurement. The zero-clause early return is not independently proved as a performance optimization. H2/F2 were tested as pinned; F2 was not rebased onto sibling-line B2.

Disposition: **Proof Complete at F2; optional coverage offered to the contributor.**

## Public follow-up authorization and live reconciliation

Used Nick's standing clean-gate PR review-follow-up authorization from root AGENTS.md, not the gate's execution-only authority. There was no user draft-review hold on this comment. After reading the full receipt and current source delta, the exact offered head was independently verified. Live PR/base/branch/discussion and the fork ref were checked again immediately before posting; PR 5303 was OPEN at H2/base B2 on branch SOLR-18625, with zero conversation comments, reviews and inline comments, so no equivalent offer existed. No remaining patch/evidence/scope concern was identified.

The local draft pr-5303-offer.md follows comment-formula.md: prominent AI disclosure, first-person factual wording, no internal workflow vocabulary, exact results and limits, pinned one-file diff and fast-forward commands. It retains Ian's existing coverage and offers optional extra tests without claiming a new production fix or asking him to delay merging. No user patch-branch link was inserted into PR 5294's body; this separate coverage offer follows the standing instruction to include the pinned diff and commands.

Posted once using gh.ps1 pr comment with the absolute body-file path:

https://github.com/apache/solr/pull/5303#issuecomment-6673593528

A separate issues/comments GET confirmed the stored text exactly matches the reviewed draft and is authored by nick-boss-tech. A separate PR read confirmed H2/base B2 and OPEN state remained unchanged, comments=1 and reviews=0. Snapshots are pr-5303-before-offer.json, pr-5303-offer-stored.json and pr-5303-after-offer.json. Git instructions use the verified contributor branch and pinned F2 merge --ff-only; nothing was pushed to the contributor or upstream.

No new PR, live body edit, JIRA write, approval/merge or conversation resolution was performed. Await contributor/maintainer response; do not post a duplicate offer or rewrite the tested branch. Public offer publication is complete; acceptance or incorporation is not claimed.
