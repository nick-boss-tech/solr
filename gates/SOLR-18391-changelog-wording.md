# Gate job: SOLR-18391 changelog fragment wording, the alias case

- Branch: solr-18391-graceful-create-submit. Head: adcda10b501cbea9f92119bae8e91187da77d9da (live tip, GATE GREEN per receipts/SOLR-18391.md). Merge-base with upstream main: c3cdf7b46e8cfff3673f76d881f32cf8e7b00622 (per the receipt).
- Branch note: solr-18391-submit (3000eeede7ad) is a different, held branch. Its fragment, changelog/unreleased/SOLR-18391.yml, does not carry the sentence below and is not part of this job.
- Intended host: any capable Linux host, under the WORKFLOW.md claim rules. Windows never claims build work.
Status: DONE
- Capability tags: implementation, gate.
- Job type: a changelog wording correction on a gated branch, then the full hardening gate at the new head. The claiming host writes the patch under the patch-loop directive (2026-10-12).
- Spec: reports/pr-drift-round-1.md, owner decisions: the changelog says a failed alias write "removes the completed collection", but `DeleteCollectionCmd.java` lines 237 to 244 refuse that delete when an alias was applied. The wording needs a branch commit.
- The inaccuracy: in `changelog/unreleased/SOLR-18391-collection-creation-failure-cleanup.yml` at the head, the title ends "; a create whose final alias write fails now removes the completed collection instead of leaving it behind". At the same head, `solr/core/src/java/org/apache/solr/cloud/api/collections/DeleteCollectionCmd.java` lines 237 to 244 throw a BAD_REQUEST SolrException ("remove or modify the aliases before removing this collection") when the collection is referenced by an alias other than its own name. In the alias case the completed collection is not removed; the delete is refused.
- The fix: rewrite the alias clause of the fragment title so it matches the code. Keep the first clause (a creation that fails part way leaves no half-created collection behind, and the error names the cause). The replacement clause must not claim the completed collection is removed when an alias references it; state that it is removed only where no alias references it, or that in the alias case it stays because the delete is refused. The exact wording is the claimant's; record it in this file and in the receipt. Keep the fragment valid YAML (quote the scalar or use block style if the wording requires it; the parse step below verifies). No production or test change is part of this job; stop and record here if the wording seems to need one.
- Body check: read the live PR body for this branch and any SOLR-18391 draft on pr-prepare for a sentence making the same claim. Do not edit the body in this job; quote any matching sentence in this file's completion note, for the drift settlement.
- Proof shape: none owed. The delta is a fragment title only and changes no behavior. The focused run below still re-runs at the new head and is counted from fresh JUnit XML.
- Modules: `:solr:core`. Focused classes at the new head (the receipt's gate set, with its counts at adcda10b501): org.apache.solr.cloud.CreateCollectionCleanupTest (5 of 5), org.apache.solr.cloud.DeleteCoreRemnantsOnCreateTest (5 of 5), org.apache.solr.cloud.OverseerCollectionConfigSetProcessorTest (26 of 26), org.apache.solr.cloud.api.collections.CreateCollectionCmdRetryTest (4 of 4), org.apache.solr.cluster.placement.impl.PlacementPluginIntegrationTest (8 of 8).
- Gate steps: changelog YAML parse of the edited fragment with a real YAML parser; tidy with the GATE INVALID guard (restore gradle/libs.versions.toml afterward; re-run tidy on the final tree after the last edit, before committing); Error Prone compile for `:solr:core` with `-Pvalidation.errorprone=true`; focused tests; module check `:solr:core:check -x test`.
- Push: the wording commit pushes plainly (fast-forward) to solr-18391-graceful-create-submit after the gate is green at the committed tree.
- On green: refresh receipts/SOLR-18391.md at the new head (the old and new fragment titles, the focused counts from fresh JUnit XML). Record the replacement wording and any body sentence named for the drift settlement in this file, and mark this job and the claim DONE.

## Completion note (the claimant fills this in)

- Replacement fragment title: "... ; a create whose final alias write fails now removes the completed collection instead of leaving it behind, unless an alias with a different name still refers to it, in which case the removal is refused and the collection stays"
- Body sentence named for the drift settlement (quoted), or none found: the live PR body (apache/solr #5027) does not claim removal in the alias-applied case. Its Choice 1 reads "If the alias write ... still fails after the bounded retry, the cleanup ... deletes a collection that was created successfully and is otherwise working", and the next sentence says "One case differs: if an alias attempt was applied and the retries still end in an error, the cleanup's delete is refused ... so the collection and the alias both stay". No body edit is owed.

## Step checklist

- [x] fragment alias clause rewritten to match DeleteCollectionCmd.java lines 237 to 244; delta against adcda10b501 is the fragment only
- [x] live PR body and drafts checked; any matching sentence quoted in the completion note
- [x] changelog YAML parse
- [x] tidy on the final tree; tree clean (GATE INVALID guard)
- [x] Error Prone compile (`:solr:core`)
- [x] focused tests at the new head, counted from fresh JUnit XML
- [x] module check (`:solr:core`)
- [x] pushed plainly to solr-18391-graceful-create-submit; receipt refreshed; completion note filled; job and claim DONE
