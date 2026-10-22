# Review: SOLR-8767, branch solr-8767-submit (unit 26)

Head reviewed: 3b5f2d2357325ba993fe1d2716fafee55741fc1b. Base: 14c7aac0d1, the parent of the branch's first SOLR-8767 commit (e994551334). Ticket: SOLR-8767, "RealTimeGetComponent and stored/copyField exclusion".

## Pass 1

Review (2026-10-22, vm4):

- Ticket fit. The ticket asks that real-time get return stored fields even when they are copyField targets. The branch removes the output filters in `RealTimeGetComponent` (the `removeCopyFieldTargets` helper, the `toSolrDoc` skip, and the post-resolve pass). That matches the ticket's case: a direct value in a copyField target comes back from `/get`.
- Atomic-update path kept. The internal `toSolrInputDocument` still skips copyField targets (`RealTimeGetComponent.java`, the path used when an atomic update is merged). The commit message says this is deliberate, and the atomic test pins the result. Verified by reading the code, not a runtime check.
- Behaviour change, owner item (rung 5, scope). `/get` now returns copyField targets that used to be hidden. Clients that relied on the hidden fields will see them. The owner should confirm that this is wanted for every copyField target, not only the stored ones.
- Finding, major: no test covers `fl=*` (wildcard) for a copyField target. The explicit-`fl` tests pass, but the wildcard branch goes through `SolrReturnFields` and the output path changed too. The branch does not show that `fl=*` returns the target. Recorded, not repaired (a new test is outside the repair scope).
- Finding, minor: the atomic test uses a single-valued target (`author_s`). A re-fed copy into a multivalued target would show as a duplicate, and the test would not catch it. Recorded, not repaired.
- Finding, nit: the doc comment on `toSolrInputDocument` (`Fields that are targets of copy-fields are excluded`) is still accurate for the atomic path, so no change needed. Recorded only.
- Changelog: present, YAML parses (step 0), type `fixed`, JIRA link present.
- Tests. `TestRealTimeGet` has 7 tests now (4 existing and 3 new). With the base `RealTimeGetComponent.java` restored, the three new tests named above fail (proof leg), so the fix is exercised.

Repairs: none. The findings are recorded for pass 2 and the owner.

Regate: `gates/SOLR-8767-review-pass-1.md`, GATE GREEN at 3b5f2d2357 on vm4.

## Pass 2

Review (2026-10-22, vm4), same head 3b5f2d2357.

- Open pass 1 finding, major (no `fl=*` test), narrowed in code. The index path for wildcard fields (`SolrDocumentFetcher`, the `copyField` check around line 190) only excludes copy targets that are not stored, so a stored copy target is returned by a wildcard read after commit. The output filter that the branch removed was the only thing that hid stored targets on the real-time path. The behaviour is consistent for `fl=*`, but no test pins it. Still recorded, not repaired (a new test is outside the repair scope).
- Open pass 1 finding, minor (multivalued feedback not pinned), unchanged. Not repaired.
- Owner item (rung 5, scope), unchanged from pass 1: `/get` now returns copyField targets that used to be hidden.
- Repairs: none.

Regate: `gates/SOLR-8767-review-pass-2.md`, GATE GREEN at 3b5f2d2357 on vm4.

## Pass 3

Review (2026-10-22, vm4), same head 3b5f2d2357. No new findings against the head. Passes 1 and 2 findings stand: no `fl=*` test (major); multivalued feedback not pinned (minor); owner item on the output change (rung 5).

- Repairs: none.

Regate: `gates/SOLR-8767-review-pass-3.md`, GATE GREEN at 3b5f2d2357 on vm4.

Final head: 3b5f2d2357325ba993fe1d2716fafee55741fc1b. Unit 26 DONE with the three pass sections.

