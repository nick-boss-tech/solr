# Claim: PR 5225 JDK 21 packaging check cross-host diagnostic (phone-agent-2)
- Claimant host: `phone-agent-2`, matching `hosts/phone-agent-2.md`.
- Status: LIVE.
- Owner direction: Nick Shanin asked to clean up and try the remaining PR 5225 gate legs after the cross-host JDK 21 run.
- Slice: run `:solr:packaging:check -x test` at the same exact fork head `nick-boss-tech/solr:solr-5225-bats-followup` / `096da6567e3db6d2bd141e3d2bf556ad1b3d2509`, using build JDK 21.0.12.1, after the successful JDK 21 BATS Commons CLI and Picocli test-file legs. Preserve command output and headroom log under `/workspace/gates/logs/phone-agent-2/pr5225-crosshost-jdk21/`.
- Decision ladder: rung 3 (Nick's direct instruction for additional gate legs).
- Restrictions: diagnostic only; no source or product-branch edits, no JDK 25 claims, no full-gate or GREEN claim, no PR or Jira action. This does not replace or take over vm2's live assignment.
- Build safety: use the host's `sweep-run.sh` lock and inode guard. Current free inodes are 178,944 (about 21%). If the guard refuses or headroom approaches 15%, stop without cleanup or retry.
- Started: 2026-10-26T00:28Z (UTC). Attempts: 1.