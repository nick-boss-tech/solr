# SOLR-18551 branch validation, round 1

Assignment: assignments/pool-solr-18551-branch-review-round-1.md. Claims: claims/pool-solr-18551-branch-review-round-1-main.md (main row, vm4) and claims/pool-solr-18551-branch-review-round-1-10x.md (10x row, vm4). The 9.x row is with vm2 and has no section here yet.

Command for every leg (seed 18551C0FFEE18551): `./gradlew --no-daemon --console=plain -Ptests.workDir=/tmp/review-<line>-<leg> -Ptests.seed=18551C0FFEE18551 -Dtests.seed=18551C0FFEE18551 :solr:core:cleanTest :solr:core:test --tests org.apache.solr.handler.IndexFetcherTest`. The base leg copies the head test file onto the base commit and runs nothing else.

## main

- Head: solr-18551-harden-tests at 544d09af7663bc1c07bd15fe30d4c5c6e853f816. BUILD SUCCESSFUL. JUnit: tests=3, skipped=0, failures=0, errors=0.
- Base: 714d6ceff852e6dc743ef51ae094e4eaf8f3b3f9 with the head test file. BUILD FAILED at :solr:core:test. JUnit: tests=3, skipped=0, failures=3, errors=0. Each failure is `expected:<[fileLength:...]> but was:<[openInput:...]>` (present, empty and missing files).
- Logs on vm4: /workspace/gates/logs/vm4/review-main-head.log, /workspace/gates/logs/vm4/review-main-base.log, and the JUnit XML copies review-main-head-junit.xml and review-main-base-junit.xml.

## 10x

- Head: solr-18551-backport-10x at a786123b1031e98981d20ad7e5d89083a523bcec. BUILD SUCCESSFUL. JUnit: tests=3, skipped=0, failures=0, errors=0.
- Base: 0712c1f7831a0b0c22837fa6a07fc028040e9bbe with the head test file. BUILD FAILED at :solr:core:test. JUnit: tests=3, skipped=0, failures=3, errors=0. Each failure is `expected:<[fileLength:...]> but was:<[openInput:...]>`.
- Logs on vm4: /workspace/gates/logs/vm4/review-10x-head.log, /workspace/gates/logs/vm4/review-10x-base.log, and the JUnit XML copies review-10x-head-junit.xml and review-10x-base-junit.xml.

## 9x

- Head (JDK 11): solr-18551-backport-9x at d8f96dfeea. JDK 11.0.32.1, Gradle 8.10 from the branch wrapper. The first head run stalled at :buildSrc:jar and was stopped (recorded in claims/pool-solr-18551-branch-review-round-1-9x-10x.md). The retry: 3 of 3 pass, 0 failures, 0 errors, 0 skipped. Reported by vm2.
- Base (JDK 11): c135e6335c with the head test file copied in. 3 of 3 fail, each `expected:<[fileLength:...]> but was:<[openInput:...]>`. This is the expected fail-before result. Reported by vm2.
- Logs on vm2: logs/vm2/18551-9x-head.log, logs/vm2/18551-9x-base.log, logs/vm2/18551-branch-9x-result.txt.
- JDK 21 base: earlier vm4 run on c135e6335c with the same test file: 3 of 3 fail (logs/vm4/18551-9101-base2.log).
- JDK 21 head: not yet run. vm2 takes this leg (Nick's direction). Results will be added here.
