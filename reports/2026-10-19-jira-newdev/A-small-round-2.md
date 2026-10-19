# JIRA newdev small improvements, round 2 (vm3)

Assignment: `assignments/pool-jira-newdev-small-round-2.md`. One section per ticket claimed by vm3.

## SOLR-15462: dubious code in DistribPackageStore::refresh (vm3, 2026-10-19)

- Status: not applicable on apache/main at `c8ab71430f`. No branch. Claim: `claims/pool-vm3-jira-newdev-small-round-2-solr-15462.md`.
- `DistribPackageStore` is not in the source tree at base. A search of `solr/` for the class finds no source file, and the only references are changelog entries (`CHANGELOG.md` line 2372, `changelog/archive.md` line 1631), so the code the ticket describes has been removed.
- Gate: none, since there is nothing to change.

## SOLR-18182: Deprecate ByteBuffersDirectoryFactory (vm3, 2026-10-19)

- Status: fixed. Branch `solr-18182-deprecate-bytebuffers`, commit `0abcca2f4b` (tree `be235de6ba`). Claim: `claims/pool-vm3-jira-newdev-small-round-2-solr-18182.md`.
- Change: `ByteBuffersDirectoryFactory` is `@Deprecated(since = "11.0")` with a javadoc pointer to `RAMDirectoryFactory`. Code reading confirms the ticket's point: `RAMDirectoryFactory.create` builds the same `ByteBuffersDirectory`, and the lock handling matches. Changelog entry `changelog/unreleased/SOLR-18182-deprecate-bytebuffers-directory-factory.yml` (type `deprecated`).
- Test: `ByteBuffersDirectoryFactoryTest.testDeprecated` is a pin: it checks the annotation is present, and it does not show a behavior change. The behavior is unchanged, and the existing tests in the class cover it.
- Gate, on `0abcca2f4b`'s tree: changelog validation 0, tidy 0 (no changes), Error Prone compile 0, focused `ByteBuffersDirectoryFactoryTest` 5 of 5 with 0 failures (seed 18182E50), `:solr:core:check -x test` 0. Logs: `/workspace/gates/logs/vm3/solr-18182-gate/`.
- Not done: no reference-guide note. Maintainers may want one on the deprecation page.

## SOLR-16124: Solr example should prompt for ZK port (vm3, 2026-10-19)

- Status: stopped, no branch. The ticket asks for a choice the code does not settle. Claim: `claims/pool-vm3-jira-newdev-small-round-2-solr-16124.md`.
- Code on `c8ab71430f`: `solr/core/src/java/org/apache/solr/cli/RunExampleTool.java` prompts for node ports only (lines 858 to 886). The embedded ZooKeeper follows the first node's port plus 1000 (`bin/solr` line 517 and `StartCommand.java` line 63 document the `PORT-1000` and `PORT+10000` rule).
- Why stopped: a ZK prompt adds a question to the interactive flow, which changes the order of the `--script-inputs` answers that scripted and packaging tests depend on. The ticket does not say whether the prompt should be default-on or opt-in, or whether `-z` / the ZK port should be an option instead. That is a user-facing choice for the owner.
- Gate: none, since no branch was pushed.

## Unclaimed by vm3 at the end of this session (triage only, for the owner)

- SOLR-2534, add min() and max() on strings: a new function-query feature. The design (which function names, behavior with multi-valued and missing fields, and how it sorts) is not in the ticket, so it is outside a minimal fix.
- SOLR-8614, add simple SolrClient.setUseXmlFormat: a new public client API. The ticket does not say which clients and which request and response writers the toggle covers, so it needs an API decision.
- SOLR-12718, StreamContext constructor should always take a SolrClientCache: a constructor signature change, which is a breaking API change for existing callers. Owner decision on compatibility.
- SOLR-16865, Solr should warn about low disk space at startup: a new startup check. The ticket suggests 2% free but leaves the path, the threshold and the log level open.
- SOLR-17719 and SOLR-18173 are claimed by other hosts.
