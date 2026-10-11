# Pool assignment: SolrCloud drafts round 1, fidelity pass and missing drafts

Capability tags: `draft`. Intended host: windows. Staffing: 2. Claim path: claims/pool-solrcloud-drafts-round-1.md (slice-level claims by ticket are fine).

Background: the SolrCloud round 1 answers pass is adopted (material/solrcloud-round-1-answers.md, including the DISCUSS items adopted under the decision default recorded there). Some SolrCloud tickets already have drafts under pr-drafts/solrcloud/ and some do not. This round brings every in-scope draft in line with its receipt and its adopted answer, and writes the missing ones.

Scope: every SolrCloud round 1 ticket that has both an adopted answer in material/solrcloud-round-1-answers.md and a receipt at receipts/<ticket>.md.

- Draft exists: run a fidelity pass against the receipt and the adopted answer. Check the gated head, the counts, the proof result, the verification dates, the citation heads, and the Limits and Choice content. Fix drift in the draft directly.
- No draft: write the formula draft (pr-formula.md: bold summary opening each section, citations as blob links at the head the receipt names, plain language, no internal process vocabulary, verification dates in Proof) at pr-drafts/solrcloud/<ticket>.md, following the adopted answer for the Limits and Choice content.
- Where an adopted answer calls for a branch change that has not landed yet, the draft describes the branch as it stands and the report flags the gap; do not write the draft as if the change were already made. Live PRs and retire or close items named in the answers are out of scope for drafting; note them in the report and move on.

Deliverable: the corrected and new draft files, plus a report at reports/solrcloud-drafts-round-1.md listing every ticket checked, which drafts were corrected, which were written new, and every gap flagged. Rules: WORKFLOW.md binds (claim before work, mark the claim DONE in the same push as the deliverables). No builds or test runs. No em dashes in authored text.
