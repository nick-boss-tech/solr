# Claim: pool-newdev-critblock-review-round-1, unit 41 (SOLR-18481)

- Claimant host: vm4, matching `hosts/vm4.md`.
- Assignment: `assignments/pool-newdev-critblock-review-round-1.md`, unit 41, Newdev.
- Branch: `solr-18481-xff`, head `8f687ca04de6cfb6baa6075cb69fcb03737de918` (ls-remote 2026-10-23T00:31:31Z, matches the table).
- Capability tags: review, gate.
- Status: DONE. Three review passes, each with a regate.
- Started: 2026-10-23T00:31:31Z (UTC).

Heartbeat: 2026-10-23T00:31:31Z

Result: 2026-10-23T01:02:44Z: pass 1 GREEN at 8f687ca04d (vm4). Findings: no test sends a request through the proxy (major); the proof leg does not compile on base (major, a consequence). Pass 2 next on the same head.

Heartbeat: 2026-10-23T01:02:44Z

Result: 2026-10-23T01:20:58Z: pass 2 GREEN at 8f687ca04d (vm4). Pass 1 findings stand. Pass 3 next on the same head.

Heartbeat: 2026-10-23T01:20:58Z

Result: 2026-10-23T01:38:47Z: pass 3 GREEN at 8f687ca04d (vm4). Passes 1 to 3 recorded.

DONE: 2026-10-23T01:38:47Z. Deliverables: reports/newdev-critblock-review-round-1/solr-18481-xff.md (passes 1 to 3), receipts/SOLR-18481.md (passes 1 to 3), gates/SOLR-18481-review-pass-1.md, gates/SOLR-18481-review-pass-2.md, gates/SOLR-18481-review-pass-3.md. Final head 8f687ca04de6cfb6baa6075cb69fcb03737de918. Open: no end-to-end test that the upstream receives X-Forwarded-For.

Heartbeat: 2026-10-23T01:38:47Z
