# Claim: PR 5225 BATS follow-up exact-head gate, round 1 (phone-agent-2)
- Claimant host: `phone-agent-2`, matching `hosts/phone-agent-2.md`.
- Status: LIVE.
- Job or assignment: `gates/pr-5225-bats-followup-round-1.md`.
- Owner direction: Nick Shanin confirmed `solr-5225-bats-followup` as the intended submission candidate and directed this remaining gate run.
- Slice: complete the remaining exact-head JDK 21 Commons CLI and Picocli legs, packaging check, and all three isolated discriminating controls; preserve logs and fresh test evidence; publish an evidence report and publish the completion receipt only if every gate criterion passes.
- Candidate: `nick-boss-tech/solr:solr-5225-bats-followup` at `35378004f1797c5b9ae3ba68d2503e4edb3840e8`; parent `096da6567e3db6d2bd141e3d2bf556ad1b3d2509`.
- Capability tags used: gate, bats, records.
- Started: 2026-10-26T01:30:42Z (UTC). Lease: live while heartbeats continue; stale 2 hours after the last heartbeat. Attempts: 1. Idempotency key: `pr-5225-bats-followup-round-1@35378004f1797c5b9ae3ba68d2503e4edb3840e8`.
- Links: parent PR #5225 and candidate branch above. Category: gate. Siblings: prior JDK 25 exact-head evidence listed by the gate; this claim completes the remaining gate. Supersedes: none.
- Restrictions: do not change candidate source under this gate, do not touch `gates/pr-5225-allowpaths-denial-round-1.md`, and do not post to a PR, Jira, or upstream. Mutations must remain in isolated scratch trees. Stop before free inodes reach 15%; do not delete files or caches without exact owner approval.
Heartbeat: 2026-10-26T01:30:42Z
Result: pending.
