# PR 5225 JDK 21 Picocli cross-host BATS diagnostic

Claim: `claims/pr-5225-bats-jdk21-picocli-cross-host-phone-agent-2.md`. This is a supplementary diagnostic leg requested by Nick. It does not replace vm2's live assignment or complete the full gate.

## Exact input and setup

- Fork branch: `nick-boss-tech/solr:solr-5225-bats-followup`
- Exact head: `096da6567e3db6d2bd141e3d2bf556ad1b3d2509`, parent `07fdebdda54a24085086281da1cde701e122564d`
- Host: Phone Agent 2 Sandbox, Linux x86_64
- Build/runtime JDK: OpenJDK 21.0.12.1, `/usr/lib/jvm/java-21-openjdk-amd64`
- Gradle: wrapper Gradle 9.7.0; BATS 1.10.0
- Parser: Picocli, with `SOLR_PICOCLI=true`
- Preflight: guarded tidy had passed on the exact head in the preceding JDK 21 Commons CLI run; this detached worktree remained at that same clean head.

## Run and outcome

The gate's initial temporary init script was applied to the included `build-infra` build as well as the Solr root, so the first Gradle invocation failed during settings evaluation with `Project with path ':solr:packaging' could not be found in project ':build-infra'`. Gradle failed in three seconds before executing any task or test. This was an environment/setup failure, not a BATS result.

I made one environment-only retry using a temporary init script that applies the same `testFiles = ['test/test_security_manager.bats']` override only when the root project name is `solr-root`, skipping included builds. The product checkout and candidate branch were not changed.

The fresh command was `-I <root-only init script> :solr:packaging:integrationTestsPicocli --rerun`. It ran from 2026-10-26T00:26:52Z to 00:27:48Z. Gradle exited 0. Fresh BATS XML records one test, zero failures, zero errors, and zero skips; output is `ok 1 allowPaths - backup` in 30.678 seconds. Teardown succeeded; no Solr process remained.

Evidence:
- First launcher attempt: `/workspace/gates/logs/phone-agent-2/pr5225-crosshost-jdk21/jdk21-picocli.log`
- Corrected Gradle output: `/workspace/gates/logs/phone-agent-2/pr5225-crosshost-jdk21/jdk21-picocli-root-only.log`
- Initial and corrected init scripts: `/workspace/gates/logs/phone-agent-2/pr5225-crosshost-jdk21/picocli-focus.init.gradle` and `picocli-focus-main-only.init.gradle`
- BATS output/XML and Solr logs: `/workspace/gates/logs/phone-agent-2/pr5225-crosshost-jdk21/jdk21-picocli-root-only.artifacts/`

## Limits

This is only the JDK 21 Picocli test-file leg. It does not cover either JDK 25 leg, mutation controls, packaging checks, or the full gate. No source files were changed. The full PR 5225 gate remains incomplete.