# Assignment: SOLR-18586 Linux probe run of the prepared RC3 diagnostic (round 2)

Status: OPEN
Tags: premise-run, records
Intended host: any Linux pool host with the `premise-run` tag (per its `hosts/` file). Serialize under the host's build lock.

Origin: `reports/solr-18586-tlog-windows-investigation-round-1.md` (Windows, HANDED FOR LINUX PROBES). That round read the code, wrote a logging-only instrumentation generator, and checked it applies to RC3, but never ran it. This round runs it. It is a premise run, not a gate and not a fix.

## Facts (do not re-derive)

- Failing wait: `TestTlogReplica.java` line 484 ("Expected collection to be 1x2") in `testKillLeader`. Captured state: rejoining TLOG replica `core_node2` stays `recovering`; leader `core_node4` is active.
- Ruled out: `firstTime = true` at `RecoveryStrategy.java` line 572 (`reports/rc3-tlogreplica-peersync-probe-round-1.md`).
- Source revision: RC3 `749712b71940c9f96d824c533f32f5c56dcd3924`.
- Seeds: `3635CC89D23D0929` (RC3 failure), `9D6BB87C4F145405` (apache/main failure).

## Work

1. Extract the Python block under "Rung 2: prepared probe" in `reports/solr-18586-tlog-windows-investigation-round-1.md` into a file outside any worktree (for example `/workspace/probes/solr-18586/rc3-failure-probes.py`). Verify its SHA-256 is `385ba51970b26737bdf166c6fc1ffe4241e797a0969b77b4f895da99f73381bb` before running it. If the hash differs, stop and record that.
2. Create two clean RC3 scratch worktrees at the full SHA above: a control (clean) and a probe arm (`python3 <script> 18586 <scratch-path> --apply`). Save `git diff` for the probe arm before any run.
3. Run `testKillLeader` on both arms with `-Ptests.seed=3635CC89D23D0929`. Then run `testRecovery` and `testOnlyLeaderIndexes` at both seeds, matched across arms. Each run goes to its own absolute evidence directory.
4. Record for every run: `git rev-parse HEAD`, `java -version`, exit status, full Gradle log, and the test XML copied out immediately. Never reuse old XML as fresh evidence.
5. Read the probe log lines for the rejoining replica: which recovery attempt runs, where it stops, and what the leader publishes. Report the sequence, not a guess.
6. If a run does not reproduce the failure, say so and widen both arms to the whole class at the same seed before drawing any conclusion.

## Rules

- No production fix, no fix branch, no PR, no Jira or GitHub comment, no dev-list text.
- Do not run the gate. Do not run tidy, Error Prone, or the changelog check. No receipt.
- Do not change only one arm. Probe logging is diagnostic and does not replace either proof leg.
- Record each decision with its rung (`material/decision-ladder.md`).
- Commit as Nick Shanin with no trailers.

## Deliverables

- `reports/solr-18586-probe-run-round-2.md`: head SHAs, JDK, commands, exit status and the probe log sequence for each run, what reproduced, and the root cause or "not established".
- The run evidence directories, referenced by absolute path in the report. Logs and XML are archived on the host; the report summarizes them.
