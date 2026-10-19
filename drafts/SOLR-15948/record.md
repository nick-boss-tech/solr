# Skip record: SOLR-15948

- Ticket: SOLR-15948, "Server can't be stopped if there are startup errors". JIRA status Open, resolution none, fix versions none, created 2022-01-22, updated 2023-04-03. Checked 2026-10-19 UTC.
- Verdict: keep-open.
- Draft branch: solr-15948-draft-open.
- Pinned head: fork origin/main 498a09aea97aac86c2cc7ac114113b946d249942. No upstream sha cited.
- Checked by: vm2 (pool Linux host), 2026-10-19 UTC, for the remediation redo of this ticket (claim claims/skip-SOLR-15948-vm2.md).

## Ticket reading

Steps from the report: run bin/solr -c with a startup error, then bin/solr stop, which does not work and times out. Three comments (2022-10-30 to 2023-04-03): a maintainer asks for a reproduction; a later commenter says the Solr operator's integration tests hit it, and that it reproduces with the Docker image under low memory or CPU. No comment names a commit or a log.

## Code reading

- solr/bin/solr:513-570 (origin/main 498a09aea97), function stop_solr: it runs `start.jar --stop` against STOP.PORT (the listen port minus 1000, by default) with `|| true`, then waits in a background loop for the Solr PID to leave the process table (STAT not Z), up to SOLR_STOP_WAIT seconds (solr/bin/solr:154, default 180). If the process is still alive after that, it runs `kill -9 "$SOLR_PID"` (line 564).
- So a stop after a startup error that never opened the stop port (the STOP command has nowhere to go) will wait the full SOLR_STOP_WAIT, then be killed. That matches "times out". The wait is bounded; it is not a hang in the script.
- solr/bin/solr:1355: the start path writes the PID file for the background JVM. The PID file is removed by stop_solr (line 547 or 566).
- Not read in this pass: why the JVM stays alive after a startup error (for example a non-daemon thread started before the error), and whether the stop port is opened late in startup. Those are in the Solr server startup code, not in bin/solr.

Commands at the pinned head:
- git grep -n 'stop_solr' origin/main -- solr/bin/solr: definition at line 513 and calls.
- git log --oneline --grep 'SOLR-15948' origin/main: no output.

## History

- Commits on origin/main naming SOLR-15948: none.
- Commits touching stop_solr: not traced with git log -S in this pass.

## Reproduction attempts

Not attempted. Reproducing needs a built distribution (solr/packaging) and a startup error, for example a low memory limit as the commenter describes. That is a full build and a run of the packaged server, which was not done in this pass.

## Gap analysis

The script side is bounded: the stop waits SOLR_STOP_WAIT seconds and then kills the process. The reported timeout is therefore the wait plus the kill, and the open question is why the process does not exit on STOP after a startup error. That lies in the server startup code, which this pass did not read.

## Verdict justification

keep-open meets the rubric: the pinned head sha is cited, the Jira status and date are read in this pass, code paths are cited with file and line, and the attempt and the missing piece are listed. The missing pieces are (1) a reproduction on the packaged server with a startup error, with the timing of the stop, and (2) the server-side reason the process stays alive. A close verdict needs a fix or a reproduction showing the case cannot happen; a pr-needed verdict needs the server-side cause and a change.

## Limits

This record does not reproduce the timeout. It does not read the Solr startup code for the STOP port. It does not check whether the kill-after-wait is the intended behaviour.
