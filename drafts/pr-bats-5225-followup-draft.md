# DRAFT, NOT POSTED: PR description for the PR 5225 follow-up

Owner note (not part of the description): not yet reviewed by Nick Shanin. Branch `nick-boss-tech/solr:solr-5225-bats-followup` at `096da6567e3`, on top of apache/solr main `07fdebdda54a` (merge of PR 5225). Opening a PR needs Nick's go.

---

🤖 *AI text below* 🤖 *(not yet reviewed by Nick Shanin)*

**PR 5225's `allowPaths - backup` check accepts the generic backup failure on Java 24+.**

On Java 24 and later, the security manager is off. The backup is rejected by Solr's allowPaths check, and the coordinator reports only `Could not backup all shards`. The current assertion accepts `access denied|Could not backup all shards` on every Java version, so an unrelated backup failure passes the test on Java 24+.

**This change checks the evidence that each Java version produces.**

On Java before 24, the test requires `access denied`. On Java 24 and later, it requires the path-specific message `must be relative to SOLR_HOME` for the forbidden directory in `solr.log`. The Java check follows the rule in `solr/bin/solr`. The change is one file, nine lines added and two removed.

**Proof.** On JDK 21.0.12.1 (Linux aarch64), the assertion matches `access denied` and BATS reports no assertion failure. The Java 24 leg has not been run, because no JDK 24 is available. It is verified by code reading: `CreateCoreBackup.java:81` runs the allowPaths check on each shard, and `CollectionHandlingUtils.java:781` reports the wrapper with the shard exception as its cause. The local host's shutdown problem makes the file's `teardown` fail, on unchanged main too. This change does not touch `teardown`.

**The choice.** Should the Java 24+ check read `solr.log`, or should the API response carry the specific message? We recommend `solr.log` for now: the API response is the generic wrapper, and changing it would be a behavior change outside this test. Do you agree?

**Limits.** The Java 24 leg has not been run. The `solr.log` match depends on the coordinator logging the shard cause; that is confirmed by code reading only. Credit to PR 5225 for the original Java 24 fix.

*AI-generated description. Nick Shanin directed the work and takes responsibility for this contribution.*
