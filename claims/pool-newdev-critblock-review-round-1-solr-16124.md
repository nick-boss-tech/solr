# Claim: pool-newdev-critblock-review-round-1, unit 31 (SOLR-16124)

- Claimant host: vm4, matching `hosts/vm4.md`.
- Assignment: `assignments/pool-newdev-critblock-review-round-1.md`, unit 31, Newdev.
- Branch: `solr-16124-zk-port-prompt`, head `7b81166ae905bc12f3cd7d18a7265ae446d62e38` (ls-remote 2026-10-22T12:21:10Z, matches the table).
- Capability tags: review, gate.
- Status: DONE. Three review passes, each with a regate.
- Started: 2026-10-22T12:21:10Z (UTC).

Heartbeat: 2026-10-22T12:21:10Z

Result: 2026-10-22T12:57:02Z: pass 1 GREEN at 7b81166ae9 (vm4). Findings: the test checks the zoo.cfg file, not the listening ZooKeeper port (major); the proof leg fails on the collection assertion, not the new check (major); port picked by a close-and-reuse race (minor). Pass 2 next on the same head.

Heartbeat: 2026-10-22T12:57:02Z

Result: 2026-10-22T13:17:28Z: pass 2 GREEN at 7b81166ae9 (vm4). Pass 1 major finding narrowed: SolrZkServer reads zoo.cfg from the node's Solr home, which is the file the test checks, so the port is honored; a listening-port check is still missing. Pass 3 next on the same head.

Heartbeat: 2026-10-22T13:17:28Z

Result: 2026-10-22T13:44:43Z: pass 3 GREEN at 7b81166ae9 (vm4). Passes 1 to 3 recorded.

DONE: 2026-10-22T13:44:43Z. Deliverables: reports/newdev-critblock-review-round-1/solr-16124-zk-port-prompt.md (passes 1 to 3), receipts/SOLR-16124.md (passes 1 to 3), gates/SOLR-16124-review-pass-1.md, gates/SOLR-16124-review-pass-2.md, gates/SOLR-16124-review-pass-3.md. Final head 7b81166ae905bc12f3cd7d18a7265ae446d62e38. Open: no test that the embedded ZooKeeper listens on the chosen port; the proof leg does not isolate the zoo.cfg check.

Heartbeat: 2026-10-22T13:44:43Z
