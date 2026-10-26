# Claim: PR 5225 JDK 21 Picocli cross-host BATS diagnostic (phone-agent-2)
- Claimant host: `phone-agent-2`, matching `hosts/phone-agent-2.md`.
- Status: LIVE.
- Owner direction: Nick Shanin requested trying the remaining test legs after the cross-host JDK 21 Commons CLI run; this claim covers only the JDK 21 Picocli leg.
- Slice: run fresh `:solr:packaging:integrationTestsPicocli --rerun` on `nick-boss-tech/solr:solr-5225-bats-followup` at exact head `096da6567e3db6d2bd141e3d2bf556ad1b3d2509`, with `SOLR_JAVA_HOME=/usr/lib/jvm/java-21-openjdk-amd64` and the gate's temporary suite-selection init script limiting the Picocli task to `test/test_security_manager.bats`. Preserve the init script, Gradle output, fresh BATS XML, and Solr logs under `/workspace/gates/logs/phone-agent-2/pr5225-crosshost-jdk21/`.
- Decision ladder: rung 3 (Nick's direct instruction for the additional leg).
- Restrictions: supplementary diagnostic only; no source or product-branch edits, no test mutations, no JDK 25 claim, no full-gate or GREEN claim, no PR or Jira action. This does not replace or take over vm2's live assignment.
- Build safety: use the host's `sweep-run.sh` lock and inode guard; do not remove the existing run evidence or generated worktree without explicit path-specific approval. Stop if the guard refuses.
- Started: 2026-10-26T00:24Z (UTC). Attempts: 1.