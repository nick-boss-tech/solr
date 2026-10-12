# Gate job: SOLR-6430 drop the changelog fragment, docs gate at the new head

- Branch: solr-6430-submit. Head: c0c6e5dd21c4eee60257db6b85b2fec7f01489b7 (live tip, verified by ls-remote at posting, 2026-10-11; this is the packaged head, GREEN for the docs-shaped gate per receipts/SOLR-6430.md). Merge-base with main: cabedd1d968059215188f4e7563fb303241899ed.
- Intended host: any capable Linux host, under the WORKFLOW.md claim rules. Windows never claims build work.
- Status: OPEN, unclaimed.
- Capability tags: gate.
- Job type: branch commit deleting the changelog fragment (an adopted owner decision), then the docs-shaped gate at the new head. Claimable end to end by vm1 or vm2 under the lane coordinators' authorization.
- Spec: reports/build-docs-convergence-round-1-response.md, item BD-R1-13, carrying the build-docs round 1 owner decision (drop the fragment, recommended; recorded in reports/build-docs-convergence-round-1.md at BD-R1-13). The response record names the file `changelog/unreleased/SOLR-6430.yml`; the fragment actually on the branch at the head above is `changelog/unreleased/SOLR-6430-doc-numeric-missing-sort.yml`, and that is the file this job deletes.
- The fix: delete `changelog/unreleased/SOLR-6430-doc-numeric-missing-sort.yml`. No other file changes: verify with `git diff` against c0c6e5dd21c4 that the delta is that one deletion only, and stop and record here if anything else differs. The guide page edit stays; this branch becomes a docs-only change with no changelog fragment, which the receipt refresh records.
- Proof shape: none owed. The delta removes a file the gate at c0c6e5dd21c4 did not depend on for its premise (the premise was verified against main there); the steps below re-run the gate's own checks at the new head.
- Gate steps (docs shape, as in receipts/SOLR-6430.md and gates/SOLR-6430.md): changelog validation with `logchangeLint` at the new head (exit 0; this also re-verifies the draft's changelog-lint sentence against a tree with no SOLR-6430 fragment); the local reference guide build at the new head passes and the generated field-type page still contains the missing-value explanation. No Java tests apply or are run for this docs-only branch.
- Push: the deletion commit pushes plainly (fast-forward) to solr-6430-submit after the gate steps pass at the committed tree.
- On green: refresh receipts/SOLR-6430.md with the new head and a line recording that the changelog fragment was dropped under BD-R1-13, with the lint and build results at the new head. Update pr-drafts/build-docs/SOLR-6430.md as BD-R1-13 prescribes: delete the draft's Changelog line (it points at the deleted fragment), and confirm its changelog-lint sentence matches the new head's run. Mark this job DONE and the claim DONE.

## Step checklist

- [ ] changelog/unreleased/SOLR-6430-doc-numeric-missing-sort.yml deleted; delta against c0c6e5dd21c4 is that deletion only
- [ ] changelog validation (logchangeLint) at the new head, exit 0
- [ ] reference guide local build at the new head passes; generated field-type page still contains the missing-value explanation
- [ ] pushed plainly to solr-6430-submit; receipt refreshed noting the drop; draft Changelog line deleted and its lint sentence re-verified; job and claim DONE
