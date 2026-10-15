# Gate job: SOLR-15863 changelog title capitalization, re-gate at the new head

- Branch: solr-15863-submit. Head: 78dff1d52f6cbb00b5ce04c20c064d478c259eb5 (fork tip at posting, 2026-10-12). Prior gated head: f381fd8d4dd1cc82f2bb899f11fdb02c2c62ccc5 (receipts/SOLR-15863.md).
- Change since the prior gated head: one line in changelog/unreleased/SOLR-15863-backup-index-version.yml (the title starts with a capital letter). No Java change.
- Posted by: windows (review agent), for Nick Shanin, 2026-10-12. Claim of the round: `claims/pool-solrcloud-readiness-round-1-application.md`.
- Capability tags: gate. Intended host: any capable Linux host. Windows never claims build work.
Status: DONE (GATE GREEN at 78dff1d52f6)
- Module: :solr:core. Focused classes at the head: BackupCmdTest, BackupCoreAPITest, LocalFSCloudIncrementalBackupTest. Seed from the receipt: 15863C0FFEE15863, nightly tests enabled. Confirm packages by grep before composing the run.
- Steps: changelog YAML parse; tidy with the GATE INVALID guard; Error Prone compile; focused tests at the head (fresh JUnit XML); module check `:solr:core:check -x test`.
- The lane may use a changelog-only carry-over if its rule allows one. The receipt must name the new head and say which route was taken.
- On green: refresh receipts/SOLR-15863.md with the new head, update the changelog link in pr-drafts/solrcloud/SOLR-15863.md (already points to 78dff1d52f6), mark this job DONE and the claim line for SOLR-15863 DONE.

## Step checklist

- [ ] changelog parse
- [ ] tidy with the GATE INVALID guard
- [ ] Error Prone compile
- [ ] focused tests at the head
- [ ] module check
- [ ] receipt refreshed; job DONE
