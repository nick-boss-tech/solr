# Claim: SOLR-18555 integrated phase-r4 SolrJ and QUERY gate
- Claimant host: phone-agent-2, matching `hosts/phone-agent-2.md`.
- Status: CLAIMED. Gate the integrated head exactly as defined in the job.
- Job: `gates/SOLR-18555-phase-r4-solrj-query.md`.
- Branch: `solr-18555-query-client-support` at `07953eab473f3f05853916f7efb4a42edc1edb0c`, rechecked by ls-remote at claim time on 2026-10-23.
- Base: `7c859a4d0eeae897542e21046e059f6fca45a6e8`. Prior integrated gate: none.
- Capability tags: gate, npm.
- Scope: the exact 13-file diff listed in the gate job; modules `:solr:core`, `:solr:solrj`, `:solr:solrj-jetty`, and `:solr:solr-ref-guide`; focused classes and seed remain those in the gate file. Proof leg: none requested.
- Category: 2 Search and query components, 3 Highlighting, and 4 SolrJ and clients. Siblings: paused parent phase-r4 gates and phase-r3 claim; do not edit or transfer those records.
- Started: 2026-10-23T07:42:00Z (UTC). Attempts: 1. Idempotency key: SOLR-18555-phase-r4-solrj-query@07953eab473f3f05853916f7efb4a42edc1edb0c.
Heartbeat: 2026-10-23T07:42:00Z.
