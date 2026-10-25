# Claim: pr-5126-query-examples-vm4-round-1 (DONE)

- Claimant host: vm4, matching `hosts/vm4.md`.
- Supersedes: `claims/pr-5126-query-examples-vm4-round-1.md` (state change as a new file, per WORKFLOW.md).
- Status: DONE. Slice Q only; slice R is not taken here.

Result: 2026-10-25T16:41:07Z: REPRODUCED only with hl.weightMatches=false (implicit query with bq=body_t:cats highlights cats in doc 2 via the transformer; hl.q=fox removes it). Default weightMatches=true does not reproduce in any row. Report: `reports/pr-5126-query-examples-round-1.md`.

DONE: 2026-10-25T16:41:07Z. Deliverables: `reports/pr-5126-query-examples-round-1.md`, `reports/pr-5126-reply-with-examples-round-1.md` (draft, not posted), `reports/pr-5126-query-examples-round-1-evidence/`, branch `solr-3479-pr5126-query-examples-r1` at `022eb6d7811cd876afde3b84115696cb1c6af50e`.
