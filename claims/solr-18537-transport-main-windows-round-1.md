# Claim: independent node transport follow-ups on main

- Assignment: `assignments/solr-12613-adjacent-implementation-round-1.md`; scope correction `gates/solr-12613-adjacent-scope-standdown-round-1.md`.
- Host Windows; tags implementation, review, records. Started 2026-11-05 02:40 UTC; attempts 1; lease two hours after last host activity.
- Board snapshot `7ca12852f997b4d5489d4d73a4f60a94a383f3da`; main `07ccc73cd24e26e59da169ca35b3fd5f64c35986`; live picker remains unchanged at `73b30956ff2c0f7b16f6af3bceacf95bda8271b9`.
- Slice: port the already-authored encoded-key fix and encoded-path coverage to separate main-based branches `solr-18537-encoded-key-followup` and `solr-18537-path-coverage-followup`. Do not inherit picker API expansion. Preserve old branches as records.
- Evidence: all three transport files and HttpSolrCall.java have identical blobs on main and old transport baseline `6ec2e527ad716bde74404b2037bb1a1e6c52f79c`; the existing changes can be transferred without source redesign. No overlapping live claim for these new branches was found in the reviewed snapshot.
- Decision ladder rung 3: explicit maintainer frontend-only scope settles separation; generic transport belongs to the already merged SOLR-18537 mechanism. These branch names identify provenance and do not claim a new Jira ticket or authorize public submission under the old issue.
- Deliverables: pushed branches with remote SHA read-back; `reports/solr-18537-transport-main-round-1.md`; `gates/solr-18537-encoded-key-main-round-1.md` and `gates/solr-18537-path-coverage-main-round-1.md`, with exact main baseline, proof legs and receipt paths. Remove the old neighbor test supplied only by the picker.
- No Windows Gradle, compiler, browser or test execution. No upstream or contributor writes, history rewrite, PR opening, GitHub/Jira comment, or duplicate v2 API work. Runtime status remains Implemented, Proof Pending.
