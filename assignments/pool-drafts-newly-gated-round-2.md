# Pool assignment: write the PR drafts for the round 2 newly gated branches

Capability tags: `draft`. Intended hosts: windows or vm3. Staffing: 1. Claim path: claims/pool-drafts-newly-gated-round-2.md (slice-level claims by ticket are fine).

Background: three more branches have gated green and their PR drafts do not exist yet. Each draft is written from the ticket's receipt on this branch (receipts/<ticket>.md), which records the gated head, the counts and the proof result, following pr-formula.md (bold summary opening each section, citations as blob links at the head the receipt names, plain language, no internal process vocabulary, verification dates in Proof). Where a receipt records a packaged head that differs from the fork tip, draft at the receipt's head and note the packaging delta in the deliverable report, not in the draft. Follow each category's answers material for the Limits and Choice content where it exists.

Scope (3 drafts):

- SOLR-14187 (pr-drafts/solrj/): draft at the receipt's packaged head 29023ec1ecabc2cd0a87675bdcdccf5d71e719b8. The gate is GREEN by a main-side proof decision recorded in receipts/SOLR-14187.md and gates/SOLR-14187.md: the branch test does not compile at merge-base, so the premise rests on a base-compilable scratch vehicle that shows the credential drop on base, with the branch test passing at head. State that proof shape plainly in the draft's Proof section; do not write that the branch test fails on base.
- SOLR-11356 (pr-drafts/solrj/): draft at the receipt's packaged head 6120dae28d04a07763f44d4bc5e9d9793af025f6. The packaging commit is local to vm2 and not pushed; the fork tip is 8474e5a3a26d8aacd25d9fc9838c627bc6c5b14f. Note that delta in the deliverable report only.
- SOLR-6430 (pr-drafts/build-docs/): docs-only change; the receipt is GREEN at c0c6e5dd21c4eee60257db6b85b2fec7f01489b7 on a docs-shaped gate (changelog lint and the reference guide build; no Java tests apply). The draft's Proof states exactly that.

Deliverable: the three draft files, plus a short report at reports/drafts-newly-gated-round-2.md noting any receipt gap or head discrepancy found while drafting. Rules: WORKFLOW.md binds (claim before work, mark the claim DONE in the same push as the deliverables). No builds or test runs. No em dashes in authored text.
