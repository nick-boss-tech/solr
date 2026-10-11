# Pool assignment: write the PR drafts for the newly gated backlog branches

Capability tags: `draft`. Staffing: up to 3. Claim path: claims/pool-drafts-newly-gated-backlog.md (slice-level claims by ticket are fine).

Background: the gate backlog rounds have gated a set of branches green whose PR drafts do not exist yet. Each draft is written from the ticket's receipt on this branch (receipts/<ticket>.md), which records the gated head, the counts and the proof result, following pr-formula.md (bold summary opening each section, citations as blob links at the head the receipt names, plain language, no internal process vocabulary, verification dates in Proof). Where a receipt records a packaged head that differs from the fork tip (SOLR-10364), draft at the receipt's head and note the packaging delta in the deliverable report, not in the draft. Follow each category's answers material for the Limits and Choice content where it exists.

Scope (8 drafts): SOLR-4502 (pr-drafts/core-admin/), SOLR-12916 (pr-drafts/core-admin/), SOLR-16499 (pr-drafts/core-admin/), SOLR-9091 (pr-drafts/replication-backup/), SOLR-9382 (pr-drafts/replication-backup/), SOLR-10364 (pr-drafts/solrj/), SOLR-10882 (pr-drafts/streaming/), SOLR-9852 (pr-drafts/streaming/).

Deliverable: the eight draft files, plus a short report at reports/drafts-newly-gated-backlog.md noting any receipt gap or head discrepancy found while drafting. Rules: WORKFLOW.md binds (claim before work, mark the claim DONE in the same push as the deliverables). No builds or test runs. No em dashes in authored text.
