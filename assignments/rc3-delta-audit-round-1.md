# Assignment: rc3-delta-audit-round-1 (audit the Solr 10.1.0 RC2 to RC3 delta)

- Status: DONE (deliverable complete; items 1 to 4 claimed and DONE, closing ranking in the report). Intended host: any host with the `review` tag. Posted by phone agent on behalf of Nick Shanin, 2026-10-24.
- Deadline shape: the RC3 vote closes Tuesday 2026-10-27 11:00 UTC. Reports land in time for the owner to read them before the vote closes. Nothing public comes out of this assignment; a finding the owner may want on the vote thread is reported to him as a draft, never posted.
- One claim per item under the small-claims rule.

## The delta

RC2 was built at dfda5e1d4821958e60d13aa5ce3a0d4992319ed1 and its vote was cancelled: the cuvs module is broken against Lucene 10.4 and documenting that changed the artifacts. RC3 is built at 749712b71940c9f96d824c533f32f5c56dcd3924. The delta is 17 commits, 94 files, all on branch_10_1:

2db17cf258 SOLR-18450 relocate GET /api/cluster (Prithvi S); 3a86b4943b SOLR-18450 test_auth.bats; fd42ef4d7f SOLR-18031 bin/solr post --filetypes; e884a1bbd8 SolrTestCase sysprop reset (Smiley); fb56d1f6cc SOLR-17398 V1 to V2 links; c1e73c1392 SOLR-18400 plugins screen 500; a3603260ea SOLR-17820 CLUSTERSTATUS health (Pugh); c8c86434cd SOLR-18520 package uninstall; d43e574fd2 SOLR-18166 linkconfig removal (Gerlowski); a8cda55615 V2 API conventions docs (Pugh); aec4a3fbf2 and e7ef1a935f the two SOLR-18450 reverts (Andrzej); bafa4eb93b cuvs breakage documentation (#5182); 1a45846c45 and 3702672a00 dependencyChanges.py changes; bba4024eed and 749712b719 changelog entries and CHANGELOG.md regeneration for RC3.

## Items

1. **Revert completeness (SOLR-18450).** The relocation and its bats test went in and came back out inside the same delta. Verify the reverts are complete: no dangling references to the relocated endpoint in code, docs, or tests at the RC3 head; the endpoint behaves at RC3 exactly as it did at RC2. Any vestige is a finding with the leftover file and line.
2. **The cherry-picked fixes (8 commits).** For each of fd42ef4d7f, e884a1bbd8, fb56d1f6cc, c1e73c1392, a3603260ea, c8c86434cd, d43e574fd2, a8cda55615: read the diff against its ticket and its main-branch original where one exists. Check the backport is faithful (nothing dropped, nothing extra), the premise still holds on branch_10_1, and any test that came with the original came with the backport. One commit per claim; a clean verdict with the comparison cited is a complete deliverable.
3. **The cuvs note (bafa4eb93b).** The note that forced the respin: is it accurate (the module, the cause, the Lucene version), is it where a user upgrading to 10.1 will actually see it, and does it say what a user is supposed to do about it.
4. **The changelog state (1a45846c45, 3702672a00, bba4024eed, 749712b719).** The RC2 thread carried two complaints about the changelog: its dependency-upgrades list was not hand-edited and de-duplicated, and two biggish issues besides. Read the RC3 CHANGELOG.md against those complaints: is the dependency list now de-duplicated, do the cuvs note and the reverted SOLR-18450 appear or not appear as they should (a reverted feature must not be announced), and do the dependencyChanges.py changes explain the difference.

## Deliverable

`reports/rc3-delta-audit-round-1.md`: one section per claimed item, each ending in a verdict (CLEAN, or a finding with file, line, and the evidence). A closing section ranks any findings by whether they bear on the vote (artifact correctness) or are cosmetic.
