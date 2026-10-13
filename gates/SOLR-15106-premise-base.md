# Premise and settling job: SOLR-15106 base failure text and dated head run

- Branch: solr-15106-submit. Head: 40b7e5d0efa70d61b74b9fa9ac04beb1ed397637 (fork tip at posting, 2026-10-12, unchanged since receipts/SOLR-15106.md). Merge-base: 9b3a84b1c460981eab09d8ffaef776acc4a184f8, the base the draft's merge-base links use.
- Why: draft line 21 says the base code cannot compile the new test, so no base failure is shown. Receipt line 7 says the premise step "fails as designed". The receipt does not record the base failure text, and its date line cites the takeover log, which is not a date source. This job records both.
- Posted by: windows (review agent), for Nick Shanin, 2026-10-12. Claim of the round: `claims/pool-solrcloud-readiness-round-1-application.md`.
- Capability tags: premise-run, settling-run. Intended host: any capable Linux host. Windows never claims build work.
- Status: DONE 2026-10-13T04:45Z by the gate coordinator. Base compile failure recorded verbatim and head focused run green at 40b7e5d0efa70d61b74b9fa9ac04beb1ed397637; receipts/SOLR-15106.md; claims/pool-vm2-gate-backlog-15106.md.
- Module: :solr:core. Focused class: OverseerProcessorExitTest (confirm the package by grep).
- Step 1, premise at base: production at the merge-base, branch test file kept. Run the focused class. Expected: a compile failure, because the test calls getCollectionProcessorThread(), which only the branch adds. Record the compiler message verbatim in the receipt body. That message is the base failure text.
- Step 2, dated run at the head: focused class at 40b7e5d0efa7, fresh JUnit XML. Record the run date (UTC), counts and seed.
- No head change, no push.
- On green: update receipts/SOLR-15106.md with the base failure text and the dated line. Then align draft line 21 of pr-drafts/solrcloud/SOLR-15106.md with what the record says. Mark this job DONE.

## Step checklist

- [ ] step 1: base compile failure text recorded verbatim
- [ ] step 2: dated focused run at the head
- [ ] receipt body updated
- [ ] draft line 21 aligned
- [ ] job DONE
