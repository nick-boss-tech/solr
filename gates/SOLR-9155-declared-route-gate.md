# Gate job: SOLR-9155 declared InterruptedException route

- Branch: solr-9155-submit. Head: dbd88c84ff246631875afc010c1c4dff4e50c28a (fork tip at posting, 2026-10-12). Prior head: 9f08d033023331dedfd555e8f014e6f0c5a384db (receipts/SOLR-9155.md is not on the branch yet; the earlier gate ran at the wrapping route). Merge-base: 97d973814336101e12475558d7419321c743de79.
- Change since the prior head: getLeader declares `throws InterruptedException` and rethrows it, instead of wrapping it in a SolrException with the flag restored. The test expects InterruptedException. The changelog title is updated to match.
- Posted by: windows (review agent), for Nick Shanin, 2026-10-12. Claim of the round: `claims/pool-solrcloud-application-round-2.md`. Work item: `assignments/pool-solrcloud-vm2-round-2.md`.
- Capability tags: gate. Intended host: vm2 (Linux). Windows never claims build work.
- Status: CLAIMED by vm2 at 2026-10-13T02:46Z (claims/pool-vm2-gate-backlog-9155.md); gate running under the vm2 serial queue, not yet at GATE END.
- Module: :solr:core. Focused class: org.apache.solr.cloud.ZkControllerGetLeaderTest (confirm the package by grep).
- Compile check the gate must confirm: the register path (ZkController.java line 1485, inside a method that declares `throws Exception`) and the rejoin path (line 2604, inside a `catch (Exception e)`) compile unchanged. No other callers exist in solr/core/src.
- Proof shape: on the merge-base, getLeader is private, so the test does not compile there. Record the compiler message. That is the premise; it is inconclusive by construction, and the receipt must say so.
- Steps: changelog YAML parse; tidy with the GATE INVALID guard; Error Prone compile; focused test at the head (fresh JUnit XML, record the seed); module check `:solr:core:check -x test`.
- On green: write receipts/SOLR-9155.md at the head, record the run date (UTC) in its body, and put the counts into the Proof bracket of pr-drafts/solrcloud/SOLR-9155.md. Mark this job DONE.

## Step checklist

- [ ] changelog parse
- [ ] tidy with the GATE INVALID guard
- [ ] Error Prone compile, with the caller compile check
- [ ] premise compile message recorded at the merge-base
- [ ] focused test at the head
- [ ] module check
- [ ] receipt written; draft Proof filled; job DONE
