# Settling job: SOLR-17733 dated focused run at the live head

- Branch: solr-17733-submit. Head: 636196b7954af6b34efad54bd0cda2330ef8c7a4 (fork tip at posting, 2026-10-12, unchanged since receipts/SOLR-17733.md). The head commit is dated 2026-10-05.
- Why: the receipt body names no gate date. The readiness round held SOLR-17733 for a dated gate record, or a re-gate at this head.
- Posted by: windows (review agent), for Nick Shanin, 2026-10-12. Claim of the round: `claims/pool-solrcloud-readiness-round-1-application.md`.
- Capability tags: settling-run. Intended host: any capable Linux host. Windows never claims build work.
Status: DONE
- Module: :solr:core. Focused class: TestDistribFileStore (solr/core/src/test/org/apache/solr/filestore/TestDistribFileStore.java), method testFileStoreManagement.
- Steps: one focused run at the head; fresh JUnit XML; record the run date (UTC), the counts and the seed in the receipt body.
- No gate, no head change, no push. If the lane prefers a full gate at this head, it may run one instead; the receipt must then name the head and the date.
- On green: add a dated line to receipts/SOLR-17733.md. Mark this job DONE.

## Step checklist

- [x] focused run at the head
- [x] receipt body carries the dated line
- [x] job DONE
