# Gate: PR 5235 compatibility premise, superseding round 1 (pinned `a7215a5bf1c7`)

Status: OPEN
Tags: gate, premise-run. Intended host: vm2 (Linux, JDK 21.0.12.1+1), claim `claims/pr-5235-compatibility-premise-round-1-vm2.md`.
Supersedes: `gates/pr-5235-compatibility-premise-round-1.md` for the test head only, under its fixture-repair rule.

## Pinned inputs

- Fork: `nick-boss-tech/solr:solr-18351-submit`.
- PR test head F: `a7215a5bf1c79b3b7dbcbf9289aac2a43e31fbcd`, one commit on `de1ffbbb55add66391eed68e4153322d6a8cc6c0`, Nick Shanin, no trailers. Change: `TestEmbeddedContentStreamCompatibility.java` ids `first`/`second` become `1`/`2` (two lines). No production change.
- Unchanged production base: `f45b38d5f6c0c9f7f1204f6d518aaad18a4a88b7`, with the same three test blobs overlaid from F.
- Test blob hashes, identical on both legs: `TestEmbeddedContentStreamCompatibility.java` `7136d6eb016`, `TestCommandOperationCompatibility.java` `e3a8821c3c1`, `TestLegacyContentStreamCompatibility.java` `d3d4cc29820`.
- Seed `5235C0FFEE18351` for every leg.

## Why this supersedes

The first run at `de1ffbbb55a` (logs under `/workspace/gates/logs/vm2/pr5235/`, not on the board) showed:

- Embedded tests fail at indexing on base and PR (`Error adding field 'id'='first'`). The standard test schema copies `id` into numeric fields, so non-numeric ids fail before the stream behavior runs. A fixture defect, repaired at F.
- JDK-client legacy and override tests receive an empty captured body on base (`actual.length=0`). This is not repaired here. It is recorded as an observation to explain, not a fixture to fix.

## Legs (all six, fresh XML per module via cleanTest, base first)

1. Preflight at F: `./gradlew tidy`, tree clean (done: rc 0 for the repair).
2. Base: `:solr:solrj:cleanTest :solr:solrj:test --tests org.apache.solr.common.util.TestCommandOperationCompatibility -Ptests.seed=5235C0FFEE18351 --no-build-cache`
3. Base: `:solr:core:cleanTest :solr:core:test --tests org.apache.solr.client.solrj.embedded.TestEmbeddedContentStreamCompatibility -Ptests.seed=5235C0FFEE18351 --no-build-cache`
4. Base: `:solr:solrj-jetty:cleanTest :solr:solrj-jetty:test --tests org.apache.solr.client.solrj.jetty.TestLegacyContentStreamCompatibility -Ptests.seed=5235C0FFEE18351 --no-build-cache`
5. PR (F): the same three commands on `/workspace/worktrees/pr5235-pr`.
6. PR (F): `./gradlew :solr:core:check -x test` and the module checks named in the gate, recording each exit.

Each leg: archived log, UTC start and end, exit code, and the three module XML files read directly from the module build directories after that leg. Logs: `/workspace/gates/logs/vm2/pr5235-superseding-a7215/`.

## Predicted outcomes (from the first run and the premise, to be tested)

- Command (solrj): base 2 pass; PR 2 fail with `NoSuchMethodException` (F2).
- Embedded (core): base 2 pass; PR fails with only `first`/`second` at index 1 (F1) and the named control passes on both.
- Jetty legacy and override (F3, F4): base Jetty variants pass; PR Jetty variants fail on the exact body assertion.
- JDK variants: not a valid comparison on base as observed (empty body). Recorded as is; the receipt must not count them as reproductions.
- Controls: JDK and Jetty content writer controls pass on both legs.

A finding is CONFIRMED only if the test passes at base and fails at PR for the predicted behavioral reason. A test that fails on both legs, fails setup, or is skipped is NOT REPRODUCED or BLOCKED BY HARNESS, with the evidence.
