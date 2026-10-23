# Claim: SOLR-10928 request hardening
- Claimant host: phone-agent-2, matching hosts/phone-agent-2.md.
- Status: ACTIVE.
- Job or assignment: assignments/solr-10928-request-hardening.md.
- Slice: owner-directed request-level coverage and docs hardening of solr-10928-submit.
- Capability tags used: implementation, review, settling-run.
- Started: 2026-10-23T03:34:51Z.
- Lease: stale after 2 hours without heartbeat. Attempts: 1. Idempotency key: SOLR-10928-request-hardening@1ff57c1b21c22031513f733b641496b77057ea55.
- Links: Ticket SOLR-10928. Category Search and query components. Siblings SOLR-7390, SOLR-5791, SOLR-7992 elevation tests. Supersedes the coverage limits of gates/SOLR-10928-phase-elevate-q.md, not its terminal result or claim.
- Initial checks: origin/solr-10928-submit is exactly 1ff57c1b21c22031513f733b641496b77057ea55; product worktree clean. Existing ticket gate and claim are DONE; no active overlapping claim. Previous four helper tests/gate will not be represented as full-request proof.
- Safety: no competing Gradle process; 35 GB disk free, 21 GB memory available. Standard host lock and disk/inode guard required for builds. JAVA_HOME=/usr/lib/jvm/java-21-openjdk-amd64.
- Staffing: one main implementation agent, one read-only independent coverage reviewer; no concurrent builds.
- Decision ladder: cost is effort, add real request tests rather than preserve the known helper-only limitation. No behavior redesign or public text actions.
Heartbeat: 2026-10-23T03:34:51Z
