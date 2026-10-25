# PR 5224 test helper reader lifetime: implementation round 1

Status: IMPLEMENTED, PROOF PENDING

Nick requested a helper that avoids returning matches backed by a closed reader. The test-only fix is pushed to `nick-boss-tech/solr:solr-18585-submit` at `1f3ed5658862b8fd682fcb999a1d3dd12863957f`. A separate GitHub refs API read confirmed that exact SHA after the push.

Parent: PR 5224's reviewed head `f28c6e79c82a9fb74c3e07607b86e74b3d4331d0`. The contributor's branch and upstream PR were not changed.

## Change

Only `solr/core/src/test/org/apache/solr/search/TestScriptFilterIntervalsSource.java` differs from the parent (15 additions, 13 deletions).

The private `matches` helper takes a borrowed `LeafReaderContext` and forwards to the source. It no longer creates or closes a reader. Each test opens its `DirectoryReader` beside the directory in try-with-resources, performs all match assertions inside that scope, and closes the reader before the directory. A short helper comment states the ownership requirement.

All four existing cases and their assertions are preserved: rejected leading candidates followed by accepted candidates; the first accepted candidate retained exactly once; all candidates rejected; and a term absent from the requested document. No production code, dependencies, changelog, or guide content was changed by this follow-up.

Decision ladder rung 1: reader-backed postings must be consumed during the reader's lifetime, as shown by the dependency trace in the October 25 review. No production behavior choice or owner question remains.

## Verification and handoff

`git diff --check` passed. The isolated checkout was clean before editing and contained only this outbound file before committing. The commit uses Nick Shanin's ICLA identity and no attribution trailers. The fork push is plain and non-forcing.

No Gradle, formatter, Java compilation, test, or local Windows queue ran. The earlier green upstream CI run proves the parent merge revision, not this new commit. No deterministic failure of the old helper was reproduced, so this change does not claim fail-before proof or an observed production query failure.

`gates/pr-5224-test-helper-round-1.md` requests exact-head Linux tidy, the focused `TestScriptFilterIntervalsSource` class with seed `6A68830B3A1BB525`, and `check -x test`, with fresh XML and raw logs. Receipt target: `receipts/PR-5224-test-helper.md`. This remains proof pending until that receipt succeeds at the exact final branch head.

Implementation claim: `claims/pr-5224-test-helper-windows-round-1.md`. The implementation deliverables are this report and that gate; the gate claim belongs to the capable Linux host that takes it.

No public comment, approval, thread resolution, PR opening, JIRA write, or write to either upstream repository was made.
