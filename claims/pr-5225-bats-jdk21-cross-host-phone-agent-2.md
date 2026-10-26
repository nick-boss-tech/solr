# Claim: PR 5225 JDK 21 cross-host BATS diagnostic (phone-agent-2)
- Claimant host: `phone-agent-2`, matching `hosts/phone-agent-2.md`.
- Status: LIVE.
- Owner direction: Nick Shanin requested a cross-host JDK 21 check on 2026-10-26T00:03Z, to compare with vm2's reported teardown failure.
- Slice: run one fresh `:solr:packaging:integrationTests --tests test_security_manager.bats` leg with `SOLR_PICOCLI=false` and `SOLR_JAVA_HOME=/usr/lib/jvm/java-21-openjdk-amd64` at `nick-boss-tech/solr:solr-5225-bats-followup` head `096da6567e3db6d2bd141e3d2bf556ad1b3d2509`. Use `SOLR_STOP_WAIT=30` to match the setting vm2 reported. Preserve the Gradle output, fresh JUnit XML, and test Solr logs under `/workspace/gates/logs/phone-agent-2/pr5225-crosshost-jdk21/`.
- Decision ladder: rung 3 (Nick's direct instruction for this diagnostic leg).
- Restrictions: diagnostic comparison only; no source or product-branch edits, no test mutations, no JDK 25 claim, no completion/green-gate claim, no PR or Jira action. This does not replace or take over vm2's live assignment.
- Build safety: use the host's `sweep-run.sh` lock and inode guard, and stop before launch if its 15% inode guard refuses.
- Started: 2026-10-26T00:04Z (UTC). Attempts: 1.