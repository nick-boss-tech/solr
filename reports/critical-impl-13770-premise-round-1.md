# SOLR-13770 premise run, round 1 (vm2)

Verdict: NOT REPRODUCED ON MAIN by code reading (rung 1). No cluster probe run, no implementation, no branch, no gate request.

- Claim: `claims/critical-impl-13770-round-1-vm2.md`. Assignment: `assignments/critical-impl-13770-round-1.md` (step 1 only).
- Ticket: SOLR-13770, Open, versions 6.6.2, 7.7.1, 7.7.2. Reported: after restart, `tlog.<stamp>` then a nested `tlog.<newstamp>/tlog.<newdiffstamp>` is created, and the replica is marked down.
- Base for reading: apache/solr `upstream/main` `ee2f1bf2e01`.

## Why the premise fails on main

- `solr/core/src/java/org/apache/solr/update/UpdateLog.java:110`: `LOG_FILENAME_PATTERN = "%s.%019d"`. Log files are flat names.
- Every transaction log path is `tlogDir.resolve(name)` (for example lines 1629, 1656), with `tlogDir` set once from `ulogToTlogDir` (line 562) and created once with `Files.createDirectories(tlogDir)` (line 583). No code path on main creates a directory inside a log file's name.
- So the nested `tlog.<x>/tlog.<y>` layout the ticket reports cannot be produced by the current log-naming code.

## Limits

- The ticket is against 6.6 to 7.7. Those versions' code was not read, so the bug may have existed there and been changed since. This verdict says nothing about those versions.
- No cluster restart or tlog replay was run. The ticket's steps are load-and-restart, so a probe would need a cluster; the code reading already kills the premise on main.
- Missing to close this: a reproduction on a 7.7 build (the ticket's versions), with its tlog directory listing.

## Not done

- No step 2 implementation, no branch `solr-13770-submit`, no gate request.
- No PR, Jira, or GitHub write.
