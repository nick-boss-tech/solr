# SOLR-12347 - hypothetical reproduction (not run)

Nothing here was run. The change and BATS test were guessed from reading `upstream/main`.

## JIRA context
Mark Miller: with many cores committing on shutdown, three minutes is not enough; a forced kill causes tlog replay on
restart. He suggested a generous default of 10-20 minutes.

## What main shows
`bin/solr` has `SOLR_STOP_WAIT:=180` and `SOLR_START_WAIT:=$SOLR_STOP_WAIT` (backwards compatibility), `bin/solr.cmd`
has 180 for stop and an independent 180 for start.

## Change
Stop default is 600 s (10 minutes, the low end of Mark's range) in `solr`, `solr.cmd` and the commented examples in
`solr.in.sh` / `solr.in.cmd`. In `bin/solr` the start default is resolved first (`${SOLR_STOP_WAIT:-180}`) so an unset
environment still waits 180 s to start while an explicit `SOLR_STOP_WAIT` still carries over, as before.

## Test
BATS in `test_start_solr.bats`: stop output says "waiting up to 600 seconds"; with `SOLR_STOP_WAIT=45` the start message says 45.

## Guessed / verify first
- 600 vs 900 or 1200 is a pick; the ticket text only says "10-20 minutes".
- `SOLR_STOP_WAIT=45 run solr start` relies on the BATS `solr` helper honouring a per-call environment prefix.
- Docker or systemd units that wrap the script may have their own shorter stop timeout (not checked).
- Ref guide has no page naming the 180 default (grep found only the 9.x upgrade note).
