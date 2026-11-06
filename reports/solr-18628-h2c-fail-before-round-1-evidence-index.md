# SOLR-18628 fail-before round 1: evidence index

**Verdict:** NON-REPRODUCED; the expected failure was not established. **Head:** `aebe1d8e9b0f226948e092f2610682c9c2596f27`. **Base:** `c0c219e374611e0edc475c0f793c9bd688b13292`. The exact test ran once with seed `18628C0DE`; JUnit reports 1 test, 0 failures, 0 errors, 0 skipped. The test worker log records the one-second keepalive option and the XML reports JVM 25.0.5+8-LTS.

## Primary artifacts

| Artifact | Board path | SHA-256 |
|---|---|---|
| Fresh JUnit XML | `reports/solr-18628-h2c-fail-before-round-1-evidence/raw/test/test.xml` | `72c88b6ce1ce183de3cc5370318edffbe53572e2bf69626f8cc8b09f10bfe6ef` |
| Complete focused-test Gradle log | `reports/solr-18628-h2c-fail-before-round-1-evidence/raw/test/test.log` | `bc8048b08d0cd02895694b1c5a5c41571a90050018fd1311034750d2b5bd14aa` |
| Focused-test status | `reports/solr-18628-h2c-fail-before-round-1-evidence/raw/test/test.status` | `9a271f2a916b0b6ee6cecb2426f0b3206ef074578be55d9bc94f6f3fe3ab86aa` |
| Compile log | `reports/solr-18628-h2c-fail-before-round-1-evidence/raw/compile/compile.log` | `ff976ece5d9ab9a5f9eb7743498636a9728e47c6f2fdc1efc182b6c50946b54f` |
| Tidy preflight log | `reports/solr-18628-h2c-fail-before-round-1-evidence/raw/preflight/preflight-tidy.log` | `5d85ad8eecd76cfe7154b0e65be05f51d1564c5db1a7d1ab692442192a10d57e` |
| Test-only B-to-H1 patch | `reports/solr-18628-h2c-fail-before-round-1-evidence/raw/source-O-to-H1.patch` | `1ba129c61e7d4a8d1fd9bee544f9a4c333c0bda38f091a3104f7692024c7b2ee` |
| JDK 25 install verification | `reports/solr-18628-h2c-fail-before-round-1-evidence/raw/toolchain/jdk25-install-verification.txt` | `46e357493158824c8b939205d165d35e0bc2c9aeaa1f4aad897102d506f59241` |

## Complete inventory and integrity

The evidence directory contains the complete preflight, compile, focused-test log/XML, runner status/context/headroom logs, toolchain verification, source patch, claim/addendum copies, source/history review, commands, and final process/lock/resource snapshot. `reports/solr-18628-h2c-fail-before-round-1-evidence/SHA256SUMS.txt` inventories and hashes all 23 staged raw/metadata files; verify from the evidence directory with `sha256sum -c SHA256SUMS.txt`. Matching local originals are preserved under `/workspace/gates/logs/phone-agent-2/solr-18628-h2c-fail-before-round-1/`.
