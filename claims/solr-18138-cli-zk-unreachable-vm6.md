# Claim: SOLR-18138 CLI when ZooKeeper is unreachable (vm6)

- Claimant host: vm6, matching `hosts/vm6.md`.
- Status: CLAIMED.
- Job or assignment: none on the board. Chosen from Jira SOLR-18138 (reporter Eric Pugh, Open, Minor, created 2026-02-26, no comments), from the no-PR list in the fresh-Eric review. Picked on Nick's go, 2026-10-27.
- Slice: read the CLI ZK connection path at apache/solr main `e59ce3c97ddb`; decide the CLI-side message; draft a Jira comment only.
- Capability tags used: review, draft.
- Started: 2026-10-27T01:34:33Z (UTC). Code reading began a few minutes before this claim was pushed.
- Lease: live while heartbeats continue; stale 2 hours after the last heartbeat. Attempts: 1. Idempotency key: solr-18138@e59ce3c97d.
- Links: Ticket: SOLR-18138. Category: review. Siblings: SOLR-18145 (DONE), others in the no-PR list.
- Decision ladder: rung 1 (code at the pinned SHA: SolrZkClient connect path, CLIUtils.getSolrZkClient, ToolBase error output).

Heartbeat: 2026-10-27T01:34:33Z
