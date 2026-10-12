# Gate job: SOLR-11288 changelog title, re-gate at the new head

- Branch: solr-11288-submit. Head: cbf0c678f277d6f50d75408d4889d3b664469905 (fork tip at posting, 2026-10-12). Prior gated head: cd094c3c623fadfb08afef173312d394e4407ffb (receipts/SOLR-11288.md).
- Change since the prior gated head: the changelog title in changelog/unreleased/SOLR-11288-set-of-split-duplicates.yml now names the blank-only behavior (replacement recorded in reports/solrcloud-round-1-p4.md, finding 1). No Java change.
- Posted by: windows (review agent), for Nick Shanin, 2026-10-12. Claim of the round: `claims/pool-solrcloud-readiness-round-1-application.md`.
- Capability tags: gate. Intended host: any capable Linux host. Windows never claims build work.
- Status: OPEN, unclaimed.
- Module: :solr:core. Focused class: TestCollectionAPI (seed from the receipt: 11288C0FFEE11288). Confirm the package by grep before composing the run.
- Steps: changelog YAML parse; tidy with the GATE INVALID guard; Error Prone compile; focused tests at the head (fresh JUnit XML); module check `:solr:core:check -x test`.
- The lane may use a changelog-only carry-over if its rule allows one. The receipt must name the new head and say which route was taken.
- On green: refresh receipts/SOLR-11288.md with the new head, update the changelog link in pr-drafts/solrcloud/SOLR-11288.md (already points to cbf0c678f27), mark this job DONE and the claim line for SOLR-11288 DONE.

## Step checklist

- [ ] changelog parse
- [ ] tidy with the GATE INVALID guard
- [ ] Error Prone compile
- [ ] focused tests at the head
- [ ] module check
- [ ] receipt refreshed; job DONE
