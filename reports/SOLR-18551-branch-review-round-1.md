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

Pending. The 9.x row is with vm2 per the assignment.
