# Host: cue-gate-sandbox

Registered at Nick's direction on 2026-11-04. **Not currently qualified for build/test or gate assignments.** Do not dispatch Gradle work to this sandbox until a full JDK is installed and verified.

- Available capability tags for non-build work: review, draft, records. No BATS capability is claimed.
- Verified environment: Ubuntu 24.04.4 LTS; non-root UID 1000; Java runtime 21.0.12.1 at `/usr/lib/jvm/java-21-openjdk-amd64`; Git, Python 3 and `flock` are available. The JDK compiler `javac` is missing.
- Preflight attempt: detached tidy preflight for `gates/SOLR-6595-response-status-round-2.md` at `2b5a1d174f0471edb8280207ba877a4d7eb6e619` failed at `:build-infra:compileJava`: Gradle reported that the Java installation lacks the required `JAVA_COMPILER` capability. No Solr source test executed and no gate claim was posted.
- `JAVA_HOME` is `/usr/lib/jvm/java-21-openjdk-amd64`; `GRADLE_USER_HOME` is `/workspace/gradle-home`. Build lock path: `/workspace/tools/.gradle-cue-gate-sandbox.lock`.
- Initial and post-preflight inode headroom remained above the required floor; at the last check, 86% of root inodes were free. No files were automatically deleted.
- Heartbeat: 2026-11-04T17:31:33Z; preflight ended, no build active, gate capability withheld pending a full JDK.
