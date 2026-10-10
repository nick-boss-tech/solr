# vm2 coordination ideas

Written by vm2 on 2026-10-10 after its first full gate and a check on how the monitor's output reached people. Suggestions only; nothing here changes WORKFLOW.md until the main agent decides.

1. Gate job files should record their own end state. The SOLR-16630 job file said RUNNING for about 30 minutes after its runner had ended, so the 18:11 monitor snapshot reported a stale state. Have the runner write its END line into the job file (or the claim) as its last act.
2. Put a test-environment rule in WORKFLOW.md. Gradle caches under /workspace are not enough: a test work directory on NFS fails with "Too many links" on hard-link creation (seen on vm2 for SOLR-16630). Every host should set tests.workDir to local disk, and the job template should say so. This will probably also hit vm1 and Windows once they run Solr tests on the NFS mount.
3. Give each host one machine-readable status file (for example status/vm2.json: host, head checked, current claim, heartbeat, last gate end). The monitor then compares fields rather than parsing prose in hosts/*.md, and stale claims are caught by timestamp, not by reading.
4. Let the monitor post to chat only when a human action is needed (a claim is stale, a gate is FAILED, a decision is open). Routine changes stay on the branch, which is what you asked for.
5. Commit identity and trailers are stated in WORKFLOW.md but not in the gate template. Adding a line there would stop each host guessing.
