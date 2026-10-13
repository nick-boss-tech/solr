# Gate job: SOLR-12651 changelog title, re-gate at the new head

- Branch: solr-12651-submit. Head: 404f13b501c97d6c6b65743a27f54f4f38363afa (fork tip at posting, 2026-10-12). Prior gated head: f3131d1ee8463388c68f18deab3c4ac3262ead83 (receipts/SOLR-12651.md, live-tip gate).
- Change since the prior gated head: the changelog title in changelog/unreleased/SOLR-12651-restore-cleanup-on-failure.yml now states the cleanup as it covers it (replacement recorded in reports/solrcloud-round-1-p2.md, finding 9). No Java change.
- Posted by: windows (review agent), for Nick Shanin, 2026-10-12. Claim of the round: `claims/pool-solrcloud-readiness-round-1-application.md`.
- Capability tags: gate. Intended host: any capable Linux host. Windows never claims build work.
- Status: OPEN, unclaimed.
- Module: :solr:core. Focused class: TestLocalFSCloudBackupRestore (seed from the receipt: 12651C0FFEE12651). Confirm the package by grep before composing the run.
- Landing order: the draft lands after SOLR-9598 (adopted, material/replication-backup-round-1-answers.md). SOLR-9598's own default call is still open with its owner, so this job does not settle the order.
- Steps: changelog YAML parse; tidy with the GATE INVALID guard; Error Prone compile; focused tests at the head (fresh JUnit XML); module check `:solr:core:check -x test`.
- The lane may use a changelog-only carry-over if its rule allows one. The receipt must name the new head and say which route was taken.
- On green: refresh receipts/SOLR-12651.md with the new head, mark this job DONE and the claim line for SOLR-12651 DONE.

## Step checklist

- [x] changelog parse
- [x] tidy with the GATE INVALID guard
- [x] Error Prone compile
- [x] focused tests at the head
- [x] module check
- [x] receipt refreshed; job DONE

DONE: GATE GREEN at 404f13b501c97d6c6b65743a27f54f4f38363afa (vm2, 2026-10-13, logs/vm2/queue-g12651-gate-20261013T003052Z.out). Receipt: receipts/SOLR-12651.md.
