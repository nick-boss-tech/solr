# SOLR-10390 - hypothetical reproduction (nothing was compiled or run)

JIRA: `bin/solr start` needs `lsof` to see whether Solr is listening; on kernels that restrict ptrace (CloudLinux
`kernel.user_ptrace`) lsof is broken, and without lsof the script printed "Please install lsof" and slept a fixed 10s.
Hoss: lsof is more common than perl; if removed, do a plain connect. Robert Muir suggested bash `/dev/tcp`. Earlier audit
note: "feature request (lsof replacement)". It is really a robustness gap in the start path, so it is treated as a bug here.

## Change
`bin/solr`: new `port_is_listening PORT`. If `lsof -v` works it keeps the old `lsof -t -PniTCP:PORT -sTCP:LISTEN` check;
otherwise it does `(exec 3<>/dev/tcp/HOST/PORT)` where HOST is `SOLR_HOST_BIND` (default 127.0.0.1; `0.0.0.0` and `::` map
to 127.0.0.1). `start_solr` now always runs the wait loop with the spinner; the "install lsof" branch and the fixed sleep
are gone. `solr.cmd` is untouched (it never used lsof).

## Test
`test_start_solr.bats`: a fake `lsof` that exits 1 is put first on `PATH`; `solr start` must print "Started Solr server on
port" and not "Please install lsof".

## Guesses to verify first
- `lsof -v 2>&1 | grep -q revision` is false for the fake (it prints nothing), so the `/dev/tcp` path runs.
- A bash built without `/dev/tcp` support (some distro builds) makes the connect fail forever and the wait ends with the
  "Still not seeing Solr listening" message after `SOLR_START_WAIT`; a stricter fallback could try `nc -z` as well.
- The `run solr start` output includes the line printed from the backgrounded subshell (bats captures it).
- Docker images that already ship lsof behave as before.
- `install_solr_service.sh` still recommends lsof (left as is, the recommendation is still valid for stop/status).

## Fail-before
Expected: on main the fake lsof sends the script into the "Please install lsof" branch and the output assertion fails.
