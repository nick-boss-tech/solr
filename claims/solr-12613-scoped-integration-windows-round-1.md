# Claim: compose the scope-separated adjacent follow-ups

- Assignment `assignments/solr-12613-adjacent-implementation-round-1.md`; follows `reports/solr-12613-frontend-scope-round-1.md` and `reports/solr-18537-transport-main-round-1.md`.
- Host windows; tags implementation, review, records. Started 2026-11-05 UTC; attempts 1; lease two hours after last host activity.
- Board snapshot `b86734c0a0e15d2e58e53040fcb1be37460dbda1`. Source inputs re-read from live fork refs before composition; all match the recorded heads. Required receipts are absent at this snapshot. This is source composition, not a gate execution claim.
- Branch `solr-12613-adjacent-integration`, worktree `wt/NODE-scoped-integration`, starting at frontend-only baseline `4e8aa913fce4a40b529fe89d8b5dd7930c29a86e`.
- Merge inputs: encoded keys `7b83425fe7ce704ee781f273cd099b4425b795af`; path coverage `59ad950e1ac14d628e711e82746ee37c489cf151`; lifecycle `7f8d251d6c27200f3804f8b4e1298b9646f6cab4`; SOLR-18619 UI requests `be369c41c633d639ad66a7291a4d82891ea9836c`.
- Decision ladder rung 3: compose the already authorized source scopes after the maintainer's API separation. Keep solr/api identical to main and allow only the three transport source/test files under solr/core. Retain the existing synthetic-baseline warning and Nick's logging choice. No new feature scope or manual source repair is planned.
- Deliverables: ordinary merges/plain fork push with independent head read-back; `reports/solr-12613-scoped-integration-round-1.md`; `gates/solr-12613-scoped-integration-round-1.md`, requiring the matching baseline and four slice receipts before execution. A formatting/source repair to an input requires recomposition and a new exact-head gate.
- Independent branches remain intact. No upstream/contributor write, history rewrite, PR creation, GitHub/Jira comment, Windows Gradle, compiler, browser or test execution. Status stays Implemented, Proof Pending.
