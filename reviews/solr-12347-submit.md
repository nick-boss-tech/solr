# solr-12347-submit

- Branch: origin/solr-12347-submit
- Head: b77acba2ad60 (reviewed and unchanged; no patch)
- Base: upstream/main (merge-base b6b2b8f10e9, 18 commits behind)
- Scope: 7 files, +50/-6. `solr/bin/solr` (stop default 600; start default resolved from an explicit stop value, else 180), `solr/bin/solr.cmd` (stop default 600), `solr/bin/solr.in.cmd` and `solr/bin/solr.in.sh` (commented examples), `solr/packaging/test/test_start_solr.bats` (one new BATS test), `changelog/unreleased/SOLR-12347-stop-wait-default.yml` (type `changed`), `SOLR-12347-TESTING.md` (author's hypothetical note, left in place).
- Verdict: Nearly (at b77acba2ad60)
- Reviewer: review-agent A1, 2026-10-08

## Findings (ranked)

MEDIUM (direction call, posed, not decided): the 600 s default is the author's pick ("the low end of Mark's range" in the JIRA's 10-20 minute suggestion). The TESTING note asks whether 900 or 1200 is intended. The owner should confirm the number. It is a product default, not patched.

LOW (verified): the BATS test name says "start wait stays at three", but the test asserts only the 45-second override. The default 180 start is never asserted. Suggested: a second `solr start` with `SOLR_STOP_WAIT` unset, expecting `Waiting up to 180 seconds`. Not patched: a test-name and coverage nit, not a clear defect.

LOW (hypothesis): `SOLR_STOP_WAIT=45 run solr start` relies on an environment prefix passing through the BATS `run` helper to the child `solr` process. The author flagged this too. Not verified without running. The packaging gate is the check.

LOW (verified, OK): the bash line-154 comment ("start wait keeps following an explicit $SOLR_STOP_WAIT for backwards compatibility") explains existing behavior. It is not a comment about this change, so AGENTS.md's comment rule is not broken.

verified (checked against the code):
- `bin/solr` (around lines 154-155): `SOLR_START_WAIT` now resolves from `${SOLR_STOP_WAIT:-180}` before `SOLR_STOP_WAIT` defaults to 600. An unset environment gives start 180 and stop 600. An explicit `SOLR_STOP_WAIT` carries to start, as before.
- The BATS assertions match the script messages. Stop prints `waiting up to $SOLR_STOP_WAIT seconds` (around line 522). Start prints `Waiting up to $SOLR_START_WAIT seconds` (around line 1386).
- `solr.cmd`: the stop default is 600. Its start default is an independent `set SOLR_START_WAIT=180` (around lines 1188-1189), so Windows start does not inherit 600. No regression there.
- No other repo code reads `STOP_WAIT` (only `EnvToSyspropMappings.properties`, which maps the name with no default).
- The ref guide mentions the old default only in the 9.0 upgrade note (`major-changes-in-solr-9.adoc` around lines 738-743). That is historical; no current page documents the stop default.
- Changelog: `type: changed` is valid; `name` is the only required author key per `dev-docs/changelog.adoc`, so the missing `nick` is fine.

## Not checked
- Nothing built, formatted, or run. No Gradle, no BATS run.
- Docker and systemd wrappers that may set their own stop timeout (the TESTING note also flags this). Not searched beyond the repo's `solr/` tree.
- The 600 s value against the JIRA's 10-20 minute range beyond what the author stated.
