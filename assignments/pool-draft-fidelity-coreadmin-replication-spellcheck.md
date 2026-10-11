# Pool assignment: draft fidelity review, Core admin, Replication and backup and Spellcheck drafts

Capability tags: `review`. Staffing: up to 4. Claim path: claims/pool-draft-fidelity-coreadmin-replication-spellcheck.md (slice-level claims by category are fine).

Purpose: the same fidelity pass as the Streaming and SolrJ round (which found drift in 13 of 20 drafts): each draft checked against its receipt and its live fork head before any opening. The answers material for these categories records adopted recommendations and, for Core admin and Replication, recommendations adopted under the owner decision default on 2026-10-10; check the drafts against those decisions as part of the Limits and Choice fidelity.

Scope: pr-drafts/core-admin/ (17 drafts), pr-drafts/replication-backup/ (7), pr-drafts/spellcheck/ (2).

For each draft: verify the head it names against the fork tip (git ls-remote), every Proof number and date against receipts/<ticket>.md, every citation anchor against the code at the named SHA (code the change produces links the head; the pre-change symptom links the merge-base and says so, per pr-formula.md), the title against the branch changelog fragment, and the Limits and Choice content against the category round report and any answers material. Flag internal process vocabulary in public text, missing verification dates, and any claim the receipt does not support. Deliverable: one verdict per draft (CONSISTENT, or DRIFT with the exact replacement text for each item). No edits under this assignment; the main side applies fixes. Rules: WORKFLOW.md binds (claim before work, subagent cap across hosts, mark the claim DONE in the same push as the deliverable). No builds or test runs.
