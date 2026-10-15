# Gate job: SOLR-12085 changelog title correction, re-gate at the new head

- Branch: solr-12085-submit. Head: c8dba502339631218190b3b08115f64047d2551c (live tip, verified by ls-remote at posting, 2026-10-11; GATE GREEN at this head per receipts/SOLR-12085.md).
- Intended host: any capable Linux host, under the WORKFLOW.md claim rules. Windows never claims build work.
Status: DONE
- Capability tags: gate.
- Job type: branch-side changelog title correction on a gated branch, then a re-gate at the new head. Claimable end to end by vm1 or vm2 under the lane coordinators' authorization.
- Spec: reports/replication-backup-round-1-g2.md, finding 14, adopted in material/replication-backup-round-1-answers.md (SOLR-12085 entry) and carried as a status item in reports/replication-backup-convergence-round-1-response.md. The current title says "on every replication"; the wait runs only on the path at IndexFetcher.java line 622 at the head (`!isFullCopyNeeded && !fetchFromLeader`), so the title overstates the change.
- The fix: in `changelog/unreleased/SOLR-12085-index-fetcher-retained-commits.yml` at the head above, replace the title with exactly: "IndexFetcher no longer treats the files of commits kept by the deletion policy (maxCommitsToKeep > 1) as unused, which made a replica wait 30 seconds and then fall back to a full index copy on each replication that reaches the check." Keep the fragment's YAML shape (the block scalar form is fine); the parse step below verifies it. No other file changes: verify with `git diff` against c8dba502339 that the delta is this one fragment only, and stop and record here if anything else differs.
- Proof shape: none owed. The delta touches no production or test code, so the gate evidence at c8dba502339 carries over the title-only commit (the SOLR-16630 precedent: re-gate green at the new head, delta changelog-only; the answers record adopts the same moved-head rule for this commit). The focused run below still re-runs at the new head and is counted from fresh JUnit XML.
- Modules: :solr:core. Focused classes at the new head: IndexFetcherUnusedFilesTest and IndexFetcherPacketProtocolTest (packages read from the branch's test files by grep, per the FQCN rule; the receipt's counts at the gated head are 1 and 18, within a 44-test set whose remaining 25 are the skipped @Nightly TestReplicationHandler; the nightly suite stays skipped here, as in the receipt).
- Gate steps: changelog YAML parse of the edited fragment with a real YAML parser; tidy with the GATE INVALID guard (restore gradle/libs.versions.toml afterward; any tidy change to another file invalidates the delta check above); Error Prone compile for :solr:core with `-Pvalidation.errorprone=true`; focused tests; module check `:solr:core:check -x test`.
- Push: the title commit pushes plainly (fast-forward) to solr-12085-submit after the gate is green at the committed tree.
- On green: refresh receipts/SOLR-12085.md with the new head and a line recording that the delta from c8dba502339 is the title correction only, with the focused counts from fresh JUnit XML. Re-point the Proof and the changelog link in pr-drafts/replication-backup/SOLR-12085.md to the new head, as the convergence response's status item prescribes. Mark this job DONE and the claim DONE.
- Landing order note: the IndexFetcher cluster order (12246, then 12085, then 11650) applies at opening time and does not block this job.

## Step checklist

- [x] title replaced verbatim in SOLR-12085-index-fetcher-retained-commits.yml; delta against c8dba502339 is that fragment only
- [x] changelog YAML parse
- [x] tidy on the final tree; tree clean (GATE INVALID guard)
- [x] Error Prone compile (:solr:core)
- [x] focused tests at the new head: IndexFetcherUnusedFilesTest and IndexFetcherPacketProtocolTest, counted from fresh JUnit XML
- [x] module check (:solr:core)
- [x] pushed plainly to solr-12085-submit; receipt refreshed; draft Proof and changelog link re-pointed; job and claim DONE
