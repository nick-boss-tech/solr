# Claim: SOLR-18555 integrated phase-r4 SolrJ and QUERY gate
- Claimant host: phone-agent-2, matching `hosts/phone-agent-2.md`.
- Status: HANDED BACK (standard runner cannot express the authorized no-new-fragment, no-proof job).
- Job: `gates/SOLR-18555-phase-r4-solrj-query.md`.
- Branch: `solr-18555-query-client-support` at `07953eab473f3f05853916f7efb4a42edc1edb0c`, rechecked by ls-remote at claim time on 2026-10-23.
- Base: `7c859a4d0eeae897542e21046e059f6fca45a6e8`. Prior integrated gate: none.
- Capability tags: gate, npm.
- Scope: the exact 13-file diff listed in the gate job; modules `:solr:core`, `:solr:solrj`, `:solr:solrj-jetty`, and `:solr:solr-ref-guide`; focused classes and seed remain those in the gate file. Proof leg: none requested.
- Category: 2 Search and query components, 3 Highlighting, and 4 SolrJ and clients. Siblings: paused parent phase-r4 gates and phase-r3 claim; do not edit or transfer those records.
- Started: 2026-10-23T07:42:00Z (UTC). Attempts: 1. Idempotency key: SOLR-18555-phase-r4-solrj-query@07953eab473f3f05853916f7efb4a42edc1edb0c.
Heartbeat: 2026-10-23T07:42:00Z.

Heartbeat: 2026-10-23T07:44:40Z.
ENDED: 2026-10-23T07:44:40Z. Standard runner capability blocker identified before any build: v2 requires an explicit changelog path and a failing proof; original runner also requires proof and permits no fragment only for test-only diffs. The job specifies no new fragment and no proof. Owner stop condition applied; no workaround, product change, repair, build, or retry. Head and 13-file diff confirmed; step 0 found no new fragment.
Deliverables: `gates/SOLR-18555-phase-r4-solrj-query.md`; top section of `receipts/SOLR-18555.md`; local preflight evidence `/workspace/phone-agent-2-logs/SOLR-18555-phase-r4-solrj-query/preflight.json`.
Next action: provide an approved standard runner supporting this exact job, then resume at step 1 on the pinned head. No DONE mark or green result is claimed.
