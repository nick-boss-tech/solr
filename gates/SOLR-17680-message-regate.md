# Gate job: SOLR-17680 message-only rewrite, re-gate at the new head

- Branch: solr-17680-submit. Head: ce59bbe112d063e3d3c93b3be6a1a5269a38fc37 (fork tip at posting, 2026-10-12). Prior head: f4b8ce83365442d775a1a746ef8e345eb2da2c03 (receipts/SOLR-17680.md, GATE GREEN there). Parent of the rewritten commit: dad43776642.
- Rewrite: message only. The body of the head commit "SOLR-17680: remove the testing handoff doc" was replaced. The tree is identical: `git diff f4b8ce83365 ce59bbe112d` is empty. Pushed with --force-with-lease pinned to f4b8ce83365 (AGENTS.md, Nick, 2026-10-12).
- Posted by: windows (review agent), for Nick Shanin, 2026-10-12. Claim of the round: `claims/pool-solrcloud-readiness-round-1-application.md`.
- Capability tags: gate. Intended host: any capable Linux host. Windows never claims build work.
- Status: OPEN, unclaimed.
- Module: :solr:core. Focused classes: org.apache.solr.cloud.CreateRoutedAliasTest and org.apache.solr.handler.admin.api.CreateAliasAPITest (confirm the packages by grep before composing the run). Expected counts from the receipt: CreateRoutedAliasTest 15 of 15, CreateAliasAPITest 13 of 13.
- Proof shape: with both production files at the merge-base 56ec140e3636d5f4150fa87fbf7103529536ac99 and the final tests in place, CreateRoutedAliasTest must show exactly 1 failure and CreateAliasAPITest exactly 1 failure. At the head both pass.
- Steps: changelog parse; tidy with the GATE INVALID guard; Error Prone compile; proof leg; focused tests at the head (fresh JUnit XML, record the seeds); module check `:solr:core:check -x test`.
- Carry-over: the tree is identical to the gated tree. If the lane records a carry-over instead of a full run, it must say so in the receipt with both SHAs.
- On green: refresh receipts/SOLR-17680.md with the new head, mark this job DONE, and mark the claim line for SOLR-17680 DONE.

## Step checklist

- [ ] changelog parse
- [ ] tidy with the GATE INVALID guard
- [ ] Error Prone compile
- [ ] proof leg at the merge-base production files
- [ ] focused tests at the head
- [ ] module check
- [ ] receipt refreshed; job DONE
