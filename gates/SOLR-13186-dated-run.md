# Settling job: SOLR-13186 dated focused run at the live head

- Branch: solr-13186-submit. Head: b436d90d2a889cc8511a493a524048ca1543edb5 (fork tip at posting, 2026-10-12, unchanged since receipts/SOLR-13186.md).
- Why: the receipt body names no gate date. Line 9 cites the receipts ledger, which is not a date source for a PR draft. This job makes a dated record at the live head.
- Posted by: windows (review agent), for Nick Shanin, 2026-10-12. Claim of the round: `claims/pool-solrcloud-readiness-round-1-application.md`.
- Capability tags: settling-run. Intended host: any capable Linux host. Windows never claims build work.
- Status: DONE. Run by vm2 at 2026-10-13T02:15:52Z to 02:22:04Z, GREEN at b436d90d2a.
- Module: :solr:core. Focused class: OverseerElectionContextTest, seed 13186C0FFEE13186. Confirm the package by grep before composing the run.
- Steps: one focused run at the head; fresh JUnit XML; record the run date (UTC) and the counts in the receipt body.
- No gate, no head change, no push. The receipt's existing counts and proof stay as they are.
- On green: add a dated line to receipts/SOLR-13186.md with the run date, counts and seed. Mark this job DONE.

## Step checklist

- [x] focused run at the head
- [x] receipt body carries the dated line
- [x] job DONE
