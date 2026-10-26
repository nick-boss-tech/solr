# Gate: PR 5235 JDK raw-body capture follow-up, superseding round 2 (pinned `1517b48953dd`)

Status: OPEN
Tags: gate, premise-run. Intended host: vm2 (Linux, JDK 21.0.12.1+1), claim `claims/pr-5235-jdk-raw-capture-round-2-vm2.md`.
Supersedes: `gates/pr-5235-jdk-raw-capture-round-2.md` for the test head only, under its tidy-repair rule.

## Pinned inputs

- Fork: `nick-boss-tech/solr:solr-18351-submit`.
- Test head F: `1517b48953dd1a11037dadc28290bfb5ca05937a`. Parent `63e517018db57c59a9c4eab5140152f898bfee1d` (the repair under review). The new commit is formatting only: one wrapped constructor call in `TestLegacyContentStreamCompatibility.java` rejoined as google-java-format requires. Nick Shanin, no trailers.
- Production PR head: `73fa87f1f41b45862c165a9571176c89e2ef4def`. Unchanged production base: `f45b38d5f6c0c9f7f1204f6d518aaad18a4a88b7`.
- Only changed file relative to the parent: `solr/solrj-jetty/src/test/org/apache/solr/client/solrj/jetty/TestLegacyContentStreamCompatibility.java`.
- Pinned test blob at F: `6ae19ed015bcb601ab970595d679943a92c48db5`. Identical on both legs.
- Seed `5235C0FFEE18351` on both legs.

## Why this supersedes

The original round 2 test head `63e517018db` fails `./gradlew tidy` with a clean-tree requirement: tidy rejoined one wrapped constructor call in the same file. Committed as its own formatting-only change, so the body-capture assertions and hooks are unchanged from `63e517018db`.

## Legs and commands (as in the round 2 gate)

1. Preflight at F: `./gradlew --no-daemon tidy`, tree clean.
2. Base: `:solr:solrj-jetty:cleanTest :solr:solrj-jetty:test --tests org.apache.solr.client.solrj.jetty.TestLegacyContentStreamCompatibility -Ptests.seed=5235C0FFEE18351 --no-build-cache`
3. PR (F): the same command on `/workspace/worktrees/pr5235-pr`.
4. PR (F): `./gradlew --no-daemon :solr:solrj-jetty:check -x test`, a module check, not a repository-wide check.

Each leg: archived log, UTC start and end, exit code, fresh XML and class output. Evidence: `/workspace/gates/logs/vm2/pr5235-jdk-raw-round-2/`.

## Acceptance

As in `gates/pr-5235-jdk-raw-capture-round-2.md`: 12 methods must execute without skips on each leg; the 8 exact body and hook methods are not weakened; the four controls (two of them form-encoded) must pass on both legs. A JDK regression is confirmed only if its method passes on base and fails on PR for the intended body reason. Otherwise NOT REPRODUCED or BLOCKED BY HARNESS, with the raw observations.
