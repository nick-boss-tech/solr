# Assignment: assertion review of all packaging BATS scripts, round 1

Status: OPEN
Posted at Nick Shanin's direction on 2026-10-26.
Tags: review, premise-run, records. Hosts: any Linux pool host; no BATS runtime is required for the review itself. Not directed to phone-agent-2; she holds the PR 5225 follow-up gate.

## Why

The PR 5225 follow-up found two confirmed assertion defects in one test of `solr/packaging/test/test_security_manager.bats`, both found by running and tracing rather than by reading:

1. Version-blind alternation: the `allowPaths - backup` assertion accepted `access denied|Could not backup all shards` on every Java version, so on Java 24+ the test passed on any backup failure, not only on the path denial it exists to check.
2. Pattern written from expectation: the Java 24+ replacement asserted the log pattern `must be relative to SOLR_HOME.*<backup-dir>`, but the real log line puts the denied path before that phrase (`Path <path> must be relative to SOLR_HOME ...`, produced by `SolrPaths.assertPathAllowed` via `CreateCoreBackup`). The assertion could never match the message it was written for.

A third shape in the same test is suspected and deliberately not this assignment's to settle: the assertion searches the whole `solr.log` with no checkpoint, so stale content could satisfy it. That question belongs to control 2 of `gates/pr-5225-bats-followup-round-1.md`.

Nick's question stands behind this assignment: were these shapes checked anywhere else? They were not. This review checks the rest of the suite.

## Scope

All 30 files in `solr/packaging/test/` at apache/solr main, in three claimable units. One claim covers one unit.

- Unit 1: test_adminconsole_urls.bats, test_assert.bats, test_auth.bats, test_basic_auth.bats, test_bats.bats, test_compression.bats, test_config.bats, test_create.bats, test_create_collection.bats, test_delete_collection.bats
- Unit 2: test_example.bats, test_export.bats, test_extraction.bats, test_healthcheck.bats, test_help.bats, test_modules.bats, test_opennlp.bats, test_placement_plugin.bats, test_post.bats, test_postlogs.bats
- Unit 3: test_rolling_upgrade.bats, test_security_manager.bats, test_snapshots.bats, test_ssl.bats, test_start_solr.bats, test_status.bats, test_stream.bats, test_version.bats, test_zk.bats, test_zz_cleanup.bats

For `test_security_manager.bats` (unit 3), record the known state from the PR 5225 packet and review its remaining assertions; do not re-derive the two fixed defects and do not run or duplicate the follow-up gate's controls.

## What to hunt, generalized from the example

The family is one question per assertion: can this assertion pass without the behavior the test claims to check?

- An alternation or generic message accepted where a specific outcome is the test's subject, including outcomes that differ by Java version, parser, or security-manager state while the assertion does not.
- A log or output pattern whose wording or field order cannot match what the producing code actually emits. Every log assertion requires tracing the producing code path and citing it, the way the 5225 packet cited `CreateCoreBackup` and `SolrPaths`; a pattern that merely looks plausible is a suspected defect, not a confirmed one.
- A whole-file search (`assert_file_contains`, grep over a shared log or output file) with no checkpoint, truncation, or run-unique marker, where content from an earlier test or run could satisfy it.
- An assertion on a wrapper or summary message where the specific reason is the test's subject and is available elsewhere (log, response detail) unchecked.

## Method and evidence standard

Read every test in the unit's files. For each file record every assertion that touches command output, a log file, or an API response, with file and line. Give each a verdict:

- `sound`: the assertion can only pass when the claimed behavior happened; one line saying why.
- `confirmed-defect`: the wrong-pass scenario is established by a cited code path or captured real output, quoted.
- `suspected-defect`: the wrong-pass scenario is plausible from the text; name the deciding run or control that would settle it. Runs are not part of this assignment; the named control is the deliverable.

Do not file style nits, naming preferences, or missing-coverage wishes as defects. A test that checks less than it could is recorded in one line as coverage, separate from defects.

## Deliverables

- Per unit: `reports/bats-scripts-assertion-review-round-1-unit-<N>.md` with the per-file records, defect counts by verdict, and the deciding controls for every suspected defect.
- Assembly, claimable once all three units land: `reports/bats-scripts-assertion-review-round-1.md` ranking every confirmed and suspected defect across the suite, with the example defects as the reference entries.

## Limits

Review only. No edits to any script, no branch, no PR, no Jira or GitHub text. Fixes, confirming runs, and gates are follow-ups on Nick's direction after the report.
