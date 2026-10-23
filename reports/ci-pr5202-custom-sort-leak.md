# PR #5202 custom-sort leak follow-up

## Result

`DistributedQueryComponentCustomSortTest` passed once on the exact PR base `79b23df58daac14b92cc35f23b8b81f72098b4ba` with the CI seed `FFF5A3C3AAC69084`: 1 test, 0 failures, 0 errors, 0 skips. The assigned run did not reproduce on the base. No retry or full core/SolrJ suite was run.

The original [PR #5202 CI run 39903819776, job 120373474924](https://github.com/apache/solr/actions/runs/39903819776/job/120373474924) reported the leak in `DistributedQueryComponentCustomSortTest`. The assignment's earlier local PR-head run also passed once with this seed. This follow-up makes no PR changes and does not identify the still-unresolved factory-context release path described in `reports/teardown-objecttracker-leak.md`.

## Base run details

The scratch worktree was checked out at the exact base SHA above. The run used the phone-agent-2 Gradle lock and the local 15-percent inode pre-build guard. At guard time, 200,010 of 837,408 inodes were free (about 23.9 percent), with 31 GB free. After the run, 189,394 inodes remained free (about 22.6 percent), with 31 GB free. The scratch source worktree remained clean.

The Gradle invocation was `:solr:core:test --tests org.apache.solr.handler.component.DistributedQueryComponentCustomSortTest --console=plain -Ptests.seed=FFF5A3C3AAC69084`. It exited 0. The JUnit XML records 1 test and zero failures, errors, or skips.

Local evidence is preserved under `/workspace/gates/logs/phone-agent-2/ci-pr5202/`: `base-custom-sort.log`, `base-custom-sort.headroom.log`, and `base-custom-sort.xml/TEST-org.apache.solr.handler.component.DistributedQueryComponentCustomSortTest.xml`.

## Matching upstream main failures

The upstream Jenkins build notifications show the same tracked object types leaking in other test classes on `main`:

| Jenkins job and build | Date | Failed class and matching tracker report |
|---|---|---|
| [Solr-NightlyTests-main #1945](https://ci-builds.apache.org/job/Solr/job/Solr-NightlyTests-main/1945) | 2026-10-10 | `TestUserManagedReplicationWithAuth.classMethod`; 6 tracked objects, including `HttpJettySolrClient`. [Build notification](https://lists.apache.org/thread.html/mbr7qrttco3029hgmwd6lotr5yvbd390). |
| [Solr-NightlyTests-main #1931](https://ci-builds.apache.org/job/Solr/job/Solr-NightlyTests-main/1931) | 2026-09-26 | `PeerSyncTest.classMethod`; 11 tracked objects, including multiple `SolrMetricsContext` instances. [Build notification](https://lists.apache.org/thread.html/x3fv7t2q5qdg7qko8xlscxv9cpzwbv0o). |
| [Solr-Test-main #14946 through #14951](https://ci-builds.apache.org/job/Solr/job/Solr-Test-main/14946) | 2026-09-26 | Repeated `TestSolrQueryParser.classMethod` failures; 11 to 13 tracked objects, including `SolrMetricsContext`. [Build notification](https://lists.apache.org/thread.html/4krnnon7lx7j836t2g4nx9qdfk6tp6dm). |
| [Solr-NightlyTests-main #1923](https://ci-builds.apache.org/job/Solr/job/Solr-NightlyTests-main/1923) | 2026-09-18 | `TestDistributedSearch.classMethod`; 1 `HttpJettySolrClient`. [Build notification](https://lists.apache.org/thread.html/c8bono5k43hq4574cyb07w0wpfd79qbv). |
| [Solr-NightlyTests-main #1883](https://ci-builds.apache.org/job/Solr/job/Solr-NightlyTests-main/1883) | 2026-08-09 | `TestDistributedSearch.classMethod`; both `SolrMetricsContext` and `HttpJettySolrClient`. [Build notification](https://lists.apache.org/thread/mkdg54896hj1n9sq5z1fbyth2z6xzx1z). |

These Jenkins job/build numbers and names are the run and job identifiers exposed by the notifications; they are not GitHub Actions numeric run/job IDs. A check of the latest sampled GitHub Actions `main` pages found dependency-submission and Docker workflows, not Solr test suites. Jenkins `consoleText` endpoints returned HTTP 404, so the fetched mailing-list build notifications and their excerpts are preserved in `/workspace/gates/logs/phone-agent-2/ci-pr5202-main-scan/`, alongside `checked-main-ci.txt`.

## Assessment

The current evidence weighs against PR #5202 causing the reported leak: the same seeded class passes on the exact PR base, the assignment's earlier run passed on the PR head, and main has separate leak reports in other classes. These one-off seeded passes do not rule out an intermittent failure or establish a general teardown fix. The custom-sort factory-context release path remains unresolved.
