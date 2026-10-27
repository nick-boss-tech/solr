# Focused BATS helper/stream split validation

**Status: focused validation passed for this separate branch only. The original BATS gate remains OPEN/INCOMPLETE; no GREEN receipt was written.**

## Commit and scope

Pinned starting baseline: `0d7c6f4e6cb6d2211b59ed9eed36db90182b122e`.

Published fork branch: `solr-pr-5225-helper-stream-assertion-fix`, head `baa0f7a88b1f2ea6c9ffb26f41fbe01b2e98b724`, parent exactly the pinned baseline. The commit author and committer are both `Nick Shanin <nick.boss.us@gmail.com>`.

The commit changes only:

- `solr/packaging/test/bats_helper.bash` — preserve failed child-listing diagnostics while still rejecting an unsuccessful listing.
- `solr/packaging/test/test_stream.bats` — recognize the observed SolrJ HTTP-401 wording while retaining the specific error and HTTP-401 requirements.

No `test_ssl.bats` change is included. `git diff --check` passed; the committed diff contains exactly those two paths.

## Focused commands and results

Both legs used the host-locked `sweep-run.sh` and the root/tmpfs resource monitor. Gradle ran under JDK 21; the gate init script set the BATS task's child `JAVA_HOME`/`SOLR_JAVA_HOME` to the requested runtime. The exact Gradle arguments were:

```text
-I /workspace/gates/logs/phone-agent-2/bats-assertion-hardening-round-1/bats-hardening.init.gradle
-Dbats.runtime.java.home=/usr/lib/jvm/java-21-openjdk-amd64 (JDK 21 leg) or /usr/lib/jvm/java-25-openjdk-amd64 (JDK 25 leg)
:solr:packaging:integrationTests --rerun-tasks --tests test_bats_assertions.bats --tests test_stream.bats
```

- **JDK 21:** BATS TAP `1..14`, all 14 passed; Gradle `BUILD SUCCESSFUL` in 2m 59s; exit 0.
- **JDK 25:** BATS TAP `1..14`, all 14 passed; Gradle `BUILD SUCCESSFUL` in 1m 42s; exit 0.

The resource monitor did not trigger; both runs stayed above the 15% root-inode and 1.5 GiB `/dev/shm` stop floors. Full logs, exact wrapper commands, and resource samples remain at `/workspace/gates/logs/phone-agent-2/bats-assertion-hardening-round-1/follow-up-2026-10-26/exact-two-file-commit/` (`jdk21-exact-commit.*` and `jdk25-exact-commit.*`).

## Limits and original gate status

These results cover only the helper-regression and stream files on the separate branch. They do not validate the original candidate as a whole or resolve TLS behavior. The candidate ref `solr-bats-assertion-hardening-submit` remains at the pinned baseline; its gate definition, claim, and original report are unchanged. The overall gate remains OPEN/INCOMPLETE: `test_rolling_upgrade.bats` is still Docker-blocked/unrun, and the eight discriminating control groups remain unrun. No receipt was added, and no PR or issue comments were posted.
