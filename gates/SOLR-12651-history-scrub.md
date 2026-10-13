# Gate job: SOLR-12651 history scrub (remove the Claude trailers), then the full gate

- Branch: solr-12651-submit. Fork tip at posting: `404f13b501c97d6c6b65743a27f54f4f38363afa` (verified by ls-remote 2026-10-13).
- Status: DONE 2026-10-13 by vm2 (coordinator). Rewritten head de3f3604f0ee12935080c8f3d418b6bd1be3013e pushed to solr-12651-submit (force-with-lease from 404f13b501c9); full gate GREEN 07:09:06Z to 07:28:43Z. Receipt: receipts/SOLR-12651.md. Claim: claims/pool-vm2-gate-backlog-12651-history-scrub.md.
- Capability tags: gate.
- Intended host: any capable Linux host (vm1 or vm2), under the WORKFLOW.md claim rules. Windows never claims build work.
- Job type: history rewrite (trailer removal and two message fixes, no code change), then the full hardening gate at the rewritten head.
- Source: the round 2 fix pass on `pr-drafts/solrcloud/SOLR-12651.md` found three commits that carry `Co-Authored-By: Claude Sonnet 5.5`. The critical review named only SOLR-15674. Pass this job before the 12651 draft's Proof can cite a new head.
- Authorization: the force-push allowance on fork submit branches (AGENTS.md, Nick, 2026-10-12). Use `--force-with-lease` pinned to the tip read in the same run.

## The census, to re-check at claim time

Commits on the branch since the merge-base `14c7aac0d151` that carry a Co-Authored-By trailer:

- `13e6789badd` "SOLR-12651: add hypothetical-reproduction handoff doc": trailer.
- `37f5989b9d1` "SOLR-12651: add changelog fragment": trailer.
- `db8ba836d8d` "SOLR-12651: clean up a partially restored collection when RESTORE fails": trailer, and the body line "Hypothetical, unrun regression test; see SOLR-12651-TESTING.md."

If the tip has moved past `404f13b501c9`, stop and record the new tip here before rewriting.

## The rebuild

Use the commit-tree rebuild pattern from the SOLR-15674 scrub:

1. Rebuild each commit after the merge-base, in order, reusing each commit's exact tree, author name, author email and author date.
2. Message changes, and only these:
   - delete the `Co-Authored-By` line from all three commits;
   - in `db8ba836d8d`, replace the body line "Hypothetical, unrun regression test; see SOLR-12651-TESTING.md." with a plain statement of what the test checks, and no reference to a testing file;
   - in `13e6789badd`, change the subject to "SOLR-12651: add restore test notes" (the commit's content is unchanged).
3. Verify before pushing: the rebuilt tip's tree equals `404f13b501c9`'s tree; each rebuilt commit's tree equals its source commit's tree; a case-insensitive grep for "claude" over the rebuilt range's full messages returns nothing; no rebuilt commit carries a Co-Authored-By trailer.
4. Push with `--force-with-lease` pinned to the tip read in this run. If the lease fails, skip the push and record it here.

## Gate at the rewritten head

Run the full hardening gate at the rewritten head, following `receipts/SOLR-12651.md`:

- Changelog YAML parse.
- Tidy, with the GATE INVALID post-tidy guard.
- Error Prone compile.
- Focused TestLocalFSCloudBackupRestore with fresh JUnit XML. Grep the branch for the exact package before composing the run spec.
- Module check for the module the branch touches.
- Base-code run is the separate job `gates/SOLR-12651-base-run.md`. It must be re-pointed at the rewritten head, because the head changes.

On green, refresh `receipts/SOLR-12651.md` with the new head, the scrub record (old tip, the three rebuilt hashes, the tree checks), and the gate counts from JUnit XML. Mark this job DONE.

## Checklist

- [x] census re-checked at claim time
- [x] history rebuilt with the message changes listed above and nothing else
- [x] tree checks passed
- [x] force-with-lease push pinned to the tip read in the run
- [x] full hardening gate green at the rewritten head
- [x] receipt refreshed; base-run job re-pointed; job DONE
