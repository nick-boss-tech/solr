# Gate job: SOLR-5821 guide wording correction for the numFound invariant, docs gate

- Branch: solr-5821-submit. Head: b8af2d1ce2f5f963250feafccd9fc698ae268289 (live tip, verified by ls-remote at posting, 2026-10-11; matches receipts/SOLR-5821.md, VERIFIED docs-only at this head). Merge-base with main: 14c7aac0d151402b00259e2fb9bf5eed7049ec5d.
- Intended host: any capable Linux host, under the WORKFLOW.md claim rules. Windows never claims build work.
- Status: OPEN, unclaimed.
- Capability tags: gate.
- Job type: one-sentence wording correction on a docs-only branch, then the docs-shaped gate at the new head. Claimable end to end by vm1 or vm2 under the lane coordinators' authorization.
- Spec: reports/build-docs-convergence-round-1-response.md, item BD-R1-09 (held branch-side there; the replacement text is recorded verbatim in that record).
- The fix: in `solr/solr-ref-guide/modules/query-guide/pages/common-query-parameters.adoc`, the branch's third added sentence now reads "When the replicas are in sync and have the same searcher, `numFound` is not affected." Replace that sentence with exactly: "`numFound` is not affected when the replicas are in sync and hold the same committed documents." No other file changes: verify with `git diff` against b8af2d1ce2f5 that the delta is that one sentence in that one file, and stop and record here if anything else differs.
- Proof shape: none owed. The delta is wording in a documentation page; the branch's verification evidence at b8af2d1ce2f5 (Antora build, rendered page checked) carries over the sentence swap, and the build below re-verifies the page at the new head.
- Gate steps (docs shape, as in receipts/SOLR-5821.md): build the reference guide at the new head with `:solr:solr-ref-guide:buildLocalAntoraSite`, run from a temp branch in the main checkout (Antora rejects a linked worktree); the build finishes BUILD SUCCESSFUL with no Antora errors or warnings; the rendered common-query-parameters.html contains the replacement sentence and no longer contains the old "same searcher" sentence. No changelog entry exists on this branch and none is added; no Java step applies.
- Push: the wording commit pushes plainly (fast-forward) to solr-5821-submit after the build is green at the committed tree.
- On green: refresh receipts/SOLR-5821.md with the new head and a line recording the BD-R1-09 sentence replacement and the build result. Update pr-drafts/build-docs/SOLR-5821.md as BD-R1-09 prescribes: the Proof and Limits re-point at the new head, and the current "does not state the sync invariant" phrasing comes out. Mark this job DONE and the claim DONE.

## Step checklist

- [ ] sentence replaced verbatim in common-query-parameters.adoc; delta against b8af2d1ce2f5 is that sentence only
- [ ] reference guide build at the new head: BUILD SUCCESSFUL, no Antora errors or warnings
- [ ] rendered common-query-parameters.html checked: new sentence present, old sentence absent
- [ ] pushed plainly to solr-5821-submit; receipt refreshed; draft re-pointed at the new head with the sync-invariant phrasing removed; job and claim DONE
