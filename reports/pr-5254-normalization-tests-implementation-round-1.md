# SOLR-18432 prerelease normalization tests: implementation handoff, round 1

Status: Implemented, Proof Pending. Linux execution is requested by gates/pr-5254-normalization-tests-round-1.md.

## Revision and scope

- Authority: Nick requested additional tests and handoff to the gate agent after the PR 5254 review.
- Original contributor head O: d59169c9cc96dd60c976079d18ef4da56024bb26, dsmiley/solr:SOLR-18432-solrversion-prerelease.
- Pushed fork head F: 378248b76b7782ddb8037ab692f4211eb666336c, nick-boss-tech/solr:solr-18432-submit. One ordinary test-only commit directly above O; no history rewrite.
- Implementation claim: claims/pr-5254-normalization-tests-windows-round-1.md, posted and verified at 46331901b6d885874e8034ee894b40df6fa2ba17.
- Intake board snapshot: e768687d9514e4c3ffba60282499929e23dfc980; no competing implementation/gate claim existed for this slice.
- Sole outbound file: solr/api/src/test/org/apache/solr/util/TestSolrVersion.java, 47 added lines. The existing eight methods are retained; four methods are added for twelve total.
- Commit author: Nick Shanin, nick.boss.us@gmail.com. No attribution trailer is added. The original contributor's existing commit and changelog are unchanged.

## Added coverage

| New method | Behavior |
| --- | --- |
| testPrereleaseRangeBoundaries | SNAPSHOT and rc.1 with build metadata match the exact numeric release and inclusive bounds, reject strict bounds, and retain strict compareTo/equals afterward |
| testWithoutPrereleasePreservesBuildMetadata | Removing the qualifier retains +build.123 and leaves the original prerelease string and ordering intact |
| testWithoutPrereleaseOnRelease | Already released versions, including metadata and a nonzero patch, retain their full string through repeated normalization |
| testWithoutPrereleaseOrdering | Different qualifiers on the same release compare equally after normalization, while patch/minor/major increments still order correctly in both directions |

These test the public API without a production injection seam or changes to LATEST_STRING. There is no new distributed startup regression. The tests pin existing intended behavior; no additional defect in the reviewed contributor code was found or claimed.

## Windows validation and Linux proof

Reviewed the one-file diff and git diff --check; the committed worktree is clean. A separate GitHub ref API read confirms the pushed head is F. No Gradle task, formatter, compiler or test was run on Windows. The earlier upstream CI success covers O, not the new methods at F. No new assertion is called proven here.

The workspace review helper still referenced the superseded PAT file. Its loader was corrected to use the current workspace file and refresh inherited GH_TOKEN, as required by the bootstrap. This local tooling repair is outside the ticket checkout and is not in the outbound diff. The exact fork branch name was used for branch review because an older draft branch also matches the ticket number.

The gate requires tidy with a clean tree, twelve focused API tests and the two NodeSystemInfoProviderTest methods, root check excluding tests, and four isolated mutations with the new tests unchanged: bypass range normalization, return the original prerelease, discard metadata, and collapse patch versions. The gate states the exact expected executed counts and assertion failures and distinguishes discriminating assertions from compatibility pins. Fresh XML/logs and source/tree hashes are required. A compile/setup failure is not negative proof.

The receipt destination is receipts/PR-5254-normalization-tests-round-1.md. A formatting or source/test repair must be pushed and covered by a superseding job at its new head. The execution host records its own claim and receipt. This handoff does not claim that an OPEN job has been picked up or run.

## Public state and disposition

The public contributor branch remains at O; this fork push does not move PR 5254's head. No public PR/JIRA comment, body edit, review submission or merge was made. The new tests are available for Linux proof before any offer to the contributor. Status remains Implemented, Proof Pending until the exact-head gate completes.
