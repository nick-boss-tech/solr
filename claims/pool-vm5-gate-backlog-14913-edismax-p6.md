# Claim: pool-vm5-gate-backlog-14913-edismax-p6

- Claimant host: vm5, matching a file in `hosts/` (`hosts/vm5.md`).
- Status: ACTIVE; mark DONE with the deliverable paths when the work lands.
- Job or assignment: `gates/SOLR-14913-edismax-p6.md`.
- Slice: the whole job (full gate on the P6 head `137dbace0170d05f8655c212cac9b27562bcfb87`, proof leg, focused class, module check, receipt).
- Capability tags used: gate.
- Started: 2026-10-18T08:02:02Z (UTC).

Heartbeat: 2026-10-18T08:02:02Z. Base confirmed: `solr-14913-submit` at `137dbace0170d05f8655c212cac9b27562bcfb87` (git ls-remote, matches the job's Head line). Prior gated head `b80221f46d3c`. Build environment: `~/.pool-env` (JAVA_HOME `/workspace/jdk/jdk-21.0.12.1+1`, GRADLE_USER_HOME `/workspace/gradle-home`).

Gate worktree: `/workspace/worktrees/vm5-14913` (detached at `137dbace0170`). Diff scope against `b80221f46d3c` confirmed: `changelog/unreleased/SOLR-14913.yml` and `TestExtendedDismaxParser.java` (3 lines added at line 1031), nothing else. Changelog YAML parses (step 0).

Result: 2026-10-18T08:08:41Z: `tidy` (sweep `g14913-tidy`), head `137dbace0170`, rc=0, BUILD SUCCESSFUL, tree clean afterwards. Log `/workspace/gates/logs/vm5/g14913-tidy.log`. Conclusive: yes (step 1 clean).

Result: 2026-10-18T08:15:10Z: `:solr:core:compileTestJava -Pvalidation.errorprone=true` (sweep `g14913-errorprone`), head `137dbace0170`, rc=0, BUILD SUCCESSFUL. Log `/workspace/gates/logs/vm5/g14913-errorprone.log`. Conclusive: yes (step 2 green).

Result: 2026-10-18T08:20:42Z: proof leg (sweep `g14913-proof-leg`), head `137dbace0170` with `ExtendedDismaxQParser.java` reverted to `b5c71bc5573` and the head test file kept, `:solr:core:test --tests org.apache.solr.search.TestExtendedDismaxParser -Ptests.seed=14913C0FFEE14913`, rc=1, FAIL. Log `/workspace/gates/logs/vm5/g14913-proof-leg.log`. Failure shape: `testAliasingWithNonSchemaField` fails at `TestExtendedDismaxParser.java:960`, the xpath `parsedquery` contains `name:Zapp` on `q=myalias:(Zapp Brannigan)`, `numFound=0`. That is the multi-term expansion assertion, added by `589d9aea39` together with the parser fix. It is not the new compound-query assertion at line 1031, which is never reached on base. The job's decision row says a proof-leg failure of any other shape is stopped and recorded for the main side. Conclusive: the run is conclusive that the base parser fails earlier than the new assertion, so this proof leg does not discriminate the new assertion. Not conclusive about the new assertion itself. Parser restored to the head afterwards; tree clean.

Stopped at step 3 per the decision row. Steps 4 (focused head run) and 5 (module check) were not run, and no receipt was written. The main side needs to choose a proof design whose base state lets the new assertion be reached, for example a parser state between `78d8222742` and `589d9aea39`. The gate stays not green at `137dbace0170`.

DONE: 2026-10-18T08:21:36Z. Deliverables: none. Claim closed on stop; the job is set to HANDED for the main side to decide the proof design.
