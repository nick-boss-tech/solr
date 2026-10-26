# PR 5108: deterministic reverse shard merge regression, round 3

Status: Implemented, Proof Pending.

Owner direction, 2026-10-26: implement a targeted regression for Houston Putman's multi-shard reverse merge question, then hand off the Linux gate. Implementation claim: `claims/pr-5108-reverse-merge-regression-round-3-windows.md`, posted at board commit `01f0580f5353a6a64cb4805107832a96661f32e9`. The initial board snapshot was `dc64c75f92c4e63022f697d07b65fe719965d6ea`; refresh `a4a266213194aa6ccff0af8b063d0b6c803bd57e` changed only this claim among the relevant PR/ticket records.

## Pushed patch

- Fork branch: `nick-boss-tech/solr:solr-17786-reverse-cursor-submit`.
- Old head: `fde070281383f113a331a9ec5ccde173b66fac3c`.
- New head: `c5a16393d2b0d2ad8b05f66697a906733e551b69`.
- Commit: `SOLR-17786: test reverse cursor selection across competing shards`.
- Author/committer: Nick Shanin, `nick.boss.us@gmail.com`, no attribution trailers.
- Scope: 105 added lines in `solr/core/src/test/org/apache/solr/cloud/DistribCursorPagingTest.java` only. No production, dependency, guide, or changelog changes.
- Commit link: `https://github.com/nick-boss-tech/solr/commit/c5a16393d2b0d2ad8b05f66697a906733e551b69`.

The worktree was clean before its fast-forward from the earlier local implementation to the offered, gated parent. After the patch, the reviewed outbound diff contained only the named test file. `git diff --check` passed. The existing nonstandard-branch commit wrapper supplied the ICLA identity. A plain fork-only push with the expected old head landed, and a separate GitHub ref read returned the exact new SHA. The worktree is clean after the commit/push.

## Tests added

`testReverseCursorMergeAcrossShards` and `testReverseCursorMergeWithTiesAcrossShards` choose IDs whose collection-router target is known, then inspect exact shard contents through leader-local `distrib=false` requests. The first shard contains ordinals `[0,3,6,9,10]`; the second contains `[1,2,4,5,7,8]`. An ordinal is identified by its ordered ID prefix, independent of the suffix used for routing.

Paging backward from document 10 with rows=3 forces the coordinator to choose `[9,8,7]` from shard candidate lists `[9,6,3]` and `[8,7,5]`, returning exactly `[7,8,9]`. This verifies bounded global selection as well as presentation. Expected IDs are predetermined by construction. The tie variant uses `int = ordinal / 3`, putting documents 6, 7 and 8 on both shards with equal primary sort values and splitting that tie at the page cutoff.

Both tests run both field-fetch modes and both multithreaded request settings, compare the previous/next markers against the matching forward page, verify navigation in both directions, short first/last pages, exhaustion, and rows=1. Request overrides explicitly use `wrapDefaults(extras, base)` because the suite's `p(base, extras)` preserves values already present in base. No claim of actual parallel collector execution follows merely from setting the request flag.

## Linux handoff

New job: `gates/pr-5108-reverse-merge-round-3.md`, OPEN to a capable Linux gate host, posted and byte-verified at board commit `911ee2642e8ffb0d80aba69dbad7eef7c08d8bae`, also the live tip observed by the poster. It pins the new head and requires focused positives on seeds `5108C0DE` and `17786C0DE`, existing cursor suites, SolrJ parsing, Error Prone, tidy, module checks, and two independent coordinator-only mutations:

1. Revert only the coordinator's effective merge sort to the original requested sort. Both new tests must detect wrong selected document identities while shard order and final presentation reversal remain intact.
2. Leave the primary merge comparator reversed but restore the original unique-key comparator at the coordinator. The tied regression must detect wrong selected identities.

Mutations are isolated proof checkouts, never submit-branch changes. Compilation/setup failures do not count as witnesses. The prior receipt proves only the parent. Runtime outcomes for the new tests and mutations are pending, and this report does not assume that the mutations have failed.

The public PR remains at `32f80171030ca1108cd20292f50232d2729ed930`; this push advances the offered fork branch, not the author's PR head. No public comment or JIRA write was made. No Gradle, compiler, formatter, JUnit test, or local Windows queue was run. The change introduces no production or security boundary.
