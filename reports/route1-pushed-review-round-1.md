# SOLR-18010 route 1, as pushed: reading review and the Linux check, round 1

Trigger: pr-prepare tip `f8bee3fc2f1`, from `83b964b`. The route 1 test change was pushed to `review-r1-solr-18010` (commit `00cf8148c32`, recorded in `190ed37fb1b`). Round claim: `claims/pool-pr-prepare-tip-f8bee3f-round-1.md`.

Scope: reading only. Nothing built, run, or pushed by this round. No PR, comment or live body touched. No gate relaunched.

## Heads

- `review-r1-solr-18010` is `00cf8148c3218925994c43d3dea6bb8a4099a62e`. It is a fast-forward from `e99be67c9a6fe15f86306104843a30b35196f159`, checked with merge-base.
- `solr-18010-submit` is `2b5ad801c9e88807337f4f0a3fdd689126ad37d1`, unchanged.

## The pushed diff

- The pushed test diff matches `gates/SOLR-18010-fail-before-route1.patch`. The bodies are identical (114 lines). The only differences are git headers, hunk context, and CRLF line endings in the on-disk patch.
- The commit touches only `SecurityConfHandlerTest.java`.

## What the reading supports

- At the parent production `2b5ad801c9e8`, the unguarded `finally` throws `DirectoryNotEmptyException` when the temporary file has been replaced by a non-empty directory. So `assertThrows(SolrException)` fails. With the review patch's `finally` guard (at `e99be67c9a6f`), the assertions pass.
- Windows is confirmed by the JDK 21 source on this host (`WindowsFileSystemProvider`, lines 250 to 269): a non-empty directory maps to `DirectoryNotEmptyException`.
- Linux is not checked by source here. The JDK archive has no Unix filesystem sources. It rests on the `IOException` contract and on the JDK's behaviour, which the Linux gate must confirm.

## Corrections to the route report

- The route report's expected message, `expected:<SolrException> but was:<DirectoryNotEmptyException>`, uses the short class name. JUnit records the fully qualified name, so the actual text will read `java.nio.file.DirectoryNotEmptyException`. The gate should record the exact text, and this report's expected message should be read with that correction.

## What the Linux gate must show

Run at the vm2 gate, with the same seed recorded:
1. At the parent production `2b5ad801c9e8` with the `00cf8148c32` test overlaid: the test fails at the `assertThrows` line with a `DirectoryNotEmptyException`-class exception. Record the exact text.
2. At `00cf8148c32` (the review patch's production): the test passes, unskipped.

Until both results are recorded, the test stays unproven on Linux.

## Owner items

1. The route 1 push is recorded, but no record names the go-ahead. The previous round found none on the branch. Name the approval in a record, or confirm it.
2. vm2 runs the Linux gate at `00cf8148c32` and at the parent, as above. The G3 claim at `e99be67c9a6f` is stale for this purpose.
3. The route report's expected message should be corrected to the fully qualified name, once the gate records the exact text.
