# Run job: SOLR-8628 premise run (is a write.lock-only index directory reachable?)

- Branch: solr-8628-submit. Head: ce8211e05e062444e7afc1c5ea33fb6eff581d8f (live tip, verified by ls-remote 2026-10-11; matches receipts/SOLR-8628.md).
- Intended host: any capable Linux host, under the WORKFLOW.md claim rules.
- Status: CLAIMED by vm3 at 2026-10-11T11:43:56Z (UTC); premise run only.
- Runner: vm3 (Linux, JDK 21, Gradle 9.7.0).
- Worktree: `/workspace/gates/solr-8628-premise-build`.
- Logs: `/workspace/gates/logs/vm3/8628-premise-*.log`.
- Capability tags: premise-run.
- Job type: premise run only. Receipt status is NO GATE, premise unverified. This job settles the premise; it runs no gate steps and removes nothing from the branch. A separate first gate job follows only if the premise holds, as stated below.
- What the branch does: in solr/core/src/java/org/apache/solr/core/SolrCore.java (`initIndex`, branch lines 856-874), a directory holding only `write.lock` counts as no index, so a core creates a fresh index over it. The branch's scenario test is `testCreateCoreOverIndexDirWithOnlyWriteLock` in solr/core/src/test/org/apache/solr/core/TestCoreContainer.java (org.apache.solr.core.TestCoreContainer in :solr:core; package read from the branch's test file by grep, per the FQCN rule). The branch still carries SOLR-8628-TESTING.md; this run does not remove it.
- Premise part 1, scope (reports/core-admin-round-1-k5.md, finding 17): verified on current upstream main `d76fc14b9857e77b830d7a82514b28c523a239c2` at 2026-10-11T11:46:26Z UTC. `CachingDirectoryFactory.exists` (lines 368-377) returns `directory.iterator().hasNext()` for a readable path, so an empty directory returns false and already works on main. A directory containing only `write.lock` has an entry and remains the premise under investigation. Evidence: `/workspace/gates/logs/vm3/8628-premise-scope.log`.
- Premise part 2, reachability (finding 18): the branch note says a crash leaves only `write.lock` because the first segments file appears only after the first commit. Lucene 10.4.0 is the version main pins. The close-without-explicit-commit/flush variant completed at 2026-10-11T11:54:13Z UTC (exit 0); after `writer.close()`, the directory contained exactly `segments_1` and `write.lock`, so this case was not write.lock-only. The abrupt-crash variant remains pending. Evidence: `/workspace/gates/logs/vm3/8628-premise-close-without-commit.log` and `/workspace/gates/logs/vm3/8628-premise-close-without-commit-headroom.log`. If neither case leaves a write.lock-only directory, the state the branch fixes is not shown to be reachable.
- Premise part 3, discrimination: run the branch's test `testCreateCoreOverIndexDirWithOnlyWriteLock` twice: once with production SolrCore.java at the merge-base cabedd1d968059215188f4e7563fb303241899ed (branch test kept), where creating the core over the write.lock-only directory must fail, and once at the branch head ce8211e05e06, where it must pass. Record both outcomes and the base failure text here, from the run's own log and fresh JUnit XML, checked by content, not count alone.
- Outcomes: if part 2 shows the state unreachable, or part 3 does not discriminate, record the outcome in receipts/SOLR-8628.md as NO GATE by finding with the run evidence, and mark this job and the claim DONE. If the premise holds, record it in the receipt and in this file, and mark this job and the claim DONE; the first gate (packaging with SOLR-8628-TESTING.md removed, then the standard steps) is a separate later job that the main side posts, not part of this run.
- If the premise holds, the corrected title on record for any later draft is "SOLR-8628: Create a new index over a directory that holds only write.lock" (finding 17), and the Lucene-side route from the ticket's 2017 comment (finding 19) stays the named alternative; neither is this run's business.

## Step checklist

- [x] part 1: empty-directory scope verified on current upstream main `d76fc14b9857e77b830d7a82514b28c523a239c2`; empty directory yields `hasNext() == false`. Evidence: `/workspace/gates/logs/vm3/8628-premise-scope.log`.
- [ ] part 2: reachability probe on Lucene 10.4.0 (close-without-commit and crash variants), directory contents recorded
- [ ] part 3: branch test against merge-base production (failure recorded by content)
- [ ] part 3: branch test at the branch head (pass recorded)
- [ ] receipt updated with the premise outcome, job and claim marked DONE
