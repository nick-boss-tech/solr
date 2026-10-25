# SOLR-18586: Windows source investigation and Linux probe handoff, round 1

Status: HANDED FOR LINUX PROBES. Root cause not established. No production fix branch.

Claim: `claims/solr-18586-windows-investigation-round-1.md`. Host: windows. Owner direction in chat permits implementation here and reserves the runtime handoff to Nick. This is a partial implementation-assignment handoff, not completion of the original runtime assignment.

## Existing work checked

Board snapshot `dde5d9159fa184331fa213b1ec82c4b7a1c39543`: the implementation assignment was OPEN, no implementation claim/report/receipt or production branch existed. Prior reproduction and peersync probe reports were read. Both Jira tickets were Open with no comments when read through the Apache Jira connector. GitHub search found only references in PRs 5098 and 5106, not a competing implementation for this ticket.

Source revision: RC3 `749712b71940c9f96d824c533f32f5c56dcd3924`. Proposed eventual production branch remains `solr-18586-tlog-rejoin-recovering`; it was not created because no runtime cause has been established. There is no fix head SHA to gate. No Java code was changed or pushed for this ticket. No Gradle, JUnit, or local Windows queue was run.

## Rung 1: paths inspected, and why recovering alone does not identify a fix

All line numbers below refer to RC3:

| Source | Observation |
| --- | --- |
| `TestTlogReplica.java:438-489` | `testKillLeader` restarts the old leader at 484, then waits for two active replicas at 486. Failure therefore occurs after the new leader has already accepted and committed a document. |
| `ZkController.java:1503-1554` | A nonleader TLOG copies old updates instead of replaying the local log, then `checkRecovery` decides whether to schedule recovery. |
| `ZkController.java:1834-1856` | Ordinary nonleader startup schedules recovery; a lower shard term can also require recovery. |
| `DefaultSolrCoreState.java:293-372` | The update executor schedules a wrapper, which takes the recovery lock, submits the strategy to the recovery executor, and waits on its future. Executor/lock delays can occur before strategy work. |
| `RecoveryStrategy.java:646-696` | Recovery locates the leader, buffers updates, publishes RECOVERING, then sends PREPRECOVERY. Remaining work is not identified by the published RECOVERING state. |
| `RecoveryStrategy.java:238-276,764-807,885-902` | Full recovery commits on the leader, fetches its index, copies buffered TLOG updates, starts background replication, and publishes ACTIVE. Any failed/blocking stage can leave the same observed state. |
| `ZkController.java:1875-1983` | ACTIVE publication itself opens a registered searcher, updates shard terms and PRS/state.json. The probe logs entry/return on both leader and recovering node. A queued state update is not proof that the watcher has seen ACTIVE. |
| `RecoveryStrategy.java:988-990` | The PREPRECOVERY request runs inside a FutureTask without reading its result. An exception can be captured inside that task rather than reaching the caller's catch. This is an additional diagnostic observation, not an established cause or a proposed fix. |

History read: `295bd6c57efde8fc140d22092e0dbbc905c5ee02` adds tracing around the existing recovery work. It supplies observability rather than evidence identifying the stall. The existing `firstTime` peersync change remains ruled out by the previous matched control/probe at seed `9D6BB87C4F145405`; it is not repeated here.

## Rung 2: prepared probe, not executed

The Python block below generates logging-only changes on a clean RC3 scratch tree. It supports `18586` and, for the companion CoreAdmin handoff, `18587`. It refuses a different HEAD or a dirty tree. Without `--apply`, it prints a unified diff and writes nothing. Keep the script outside the scratch tree. Never merge its diagnostic Java changes into a production branch.

Local verification: both generator modes were dry-run on RC3 and both generated patches passed `git apply --check`. This verifies their anchors and patch applicability only. Java compilation and runtime results are pending. The printed TLOG probe adds before/after logging around leader discovery, buffering, PREPRECOVERY, leader commit, fetch, replay, background replication, registration, publication, recovery locking, and the recovery future. The PREPRECOVERY exception logger is inside the original FutureTask, and rethrows into that same task; no extra `get()`, timeout, retry, or peersync change is introduced.

Save the exact Python block as `rc3-failure-probes.py`. SHA-256 of the UTF-8/LF script including its final newline: `385ba51970b26737bdf166c6fc1ffe4241e797a0969b77b4f895da99f73381bb`.

```python
#!/usr/bin/env python3
"""Add diagnostic logging to a clean RC3 scratch tree; never run a build."""
import argparse
import difflib
from pathlib import Path
import subprocess

BASE = "749712b71940c9f96d824c533f32f5c56dcd3924"


def replace_once(text, before, after):
    if text.count(before) != 1:
        raise SystemExit(f"Expected exactly one anchor: {before!r}")
    return text.replace(before, after, 1)


def instrument(ticket, path, text):
    if ticket == "18587":
        return replace_once(
            text,
            "      if (loadFailure != null) {\n        // getting the index directory",
            "      if (loadFailure != null) {\n"
            "        SolrCore loadedCoreForProbe = solrCores.getCoreFromAnyList(name, false);\n"
            "        log.warn(\n"
            '            "SOLR18587_PROBE core={} loaded={} loadedDataDir={} fallbackDataDir={} branch=failed-init",\n'
            "            name,\n"
            "            loadedCoreForProbe != null,\n"
            '            loadedCoreForProbe == null ? "<none>" : loadedCoreForProbe.getDataDir(),\n'
            "            loadFailure.cd.getInstanceDir().resolve(loadFailure.cd.getDataDir()));\n"
            "        // getting the index directory",
        )

    if path.endswith("RecoveryStrategy.java"):
        text = replace_once(
            text,
            "  private void setOutcome(String outcome) {",
            "  private void traceRecoveryProbe(String phase, Object detail) {\n"
            "    log.warn(\n"
            '        "SOLR18586_PROBE node={} core={} replica={} type={} attempt={} phase={} detail={}",\n'
            "        zkController.getNodeName(), coreName, coreZkNodeName, replicaType, retries, phase, detail);\n"
            "  }\n\n"
            "  private void setOutcome(String outcome) {",
        )
        points = [
            ("        final Replica leader = pingLeader(ourUrl, this.coreDescriptor, true);", "pingLeader", "ourUrl"),
            ("        ulog.bufferUpdates();", "bufferUpdates", "ulog.getState()"),
            ("        sendPrepRecoveryCmd(leader.getBaseUrl(), leader.getCoreName(), slice);", "prepRecovery", "leader.getCoreUrl()"),
            ("      commitOnLeader(leaderBaseUrl, leaderCore);", "commitOnLeader", "leaderCore"),
            ("      boolean success = replicationHandler.doFetch(solrParams, false).getSuccessful();", "doFetch", "leaderCore"),
            ("              zkController.startReplicationFromLeader(coreName, true);", "startBackgroundReplication", "coreName"),
        ]
        for line, phase, detail in points:
            indent = line[: len(line) - len(line.lstrip())]
            text = replace_once(
                text, line,
                f'{indent}traceRecoveryProbe("{phase}.before", {detail});\n'
                f"{line}\n"
                f'{indent}traceRecoveryProbe("{phase}.after", {detail});',
            )
        line = "          replicate(zkController.getNodeName(), core, leader);"
        if text.count(line) != 2:
            raise SystemExit("Unexpected replication anchors")
        text = text.replace(
            line,
            '          traceRecoveryProbe("replicate.before", leader.getCoreUrl());\n'
            + line
            + '\n          traceRecoveryProbe("replicate.after", leader.getCoreUrl());',
        )
        # Replay occurs in both peersync and full-replication arms. Keep behavior unchanged.
        for indent in ("          ", "            "):
            line = "\n" + indent + "replayFuture = replay(core);"
            text = replace_once(
                text, line,
                f'\n{indent}traceRecoveryProbe("replay.before", ulog.getState());'
                + line
                + f'\n{indent}traceRecoveryProbe("replay.after", ulog.getState());',
            )
        # Log failures inside the existing FutureTask; do not add get(), a wait, or a new throw path.
        return replace_once(
            text,
            "      prevSendPreRecoveryHttpUriRequest = new FutureTask<>(() -> client.request(prepCmd));",
            "      prevSendPreRecoveryHttpUriRequest =\n"
            "          new FutureTask<>(\n"
            "              () -> {\n"
            "                try {\n"
            "                  return client.request(prepCmd);\n"
            "                } catch (Exception e) {\n"
            '                  log.warn("SOLR18586_PROBE prepRecovery request failed core=" + coreName, e);\n'
            "                  throw e;\n"
            "                }\n"
            "              });",
        )

    if path.endswith("ZkController.java"):
        text = replace_once(
            text,
            "            ulog.copyOverOldUpdates(Long.parseLong(commitVersion));",
            '            log.warn("SOLR18586_PROBE copyOld.before core={} commitVersion={}", coreName, commitVersion);\n'
            "            ulog.copyOverOldUpdates(Long.parseLong(commitVersion));\n"
            '            log.warn("SOLR18586_PROBE copyOld.after core={}", coreName);',
        )
        text = replace_once(
            text,
            "        boolean didRecovery =\n            checkRecovery(",
            '        log.warn("SOLR18586_PROBE checkRecovery.before core={} leader={} skip={}", coreName, isLeader, skipRecovery);\n'
            "        boolean didRecovery =\n            checkRecovery(",
        )
        text = replace_once(
            text,
            "        if (!didRecovery) {",
            '        log.warn("SOLR18586_PROBE checkRecovery.after core={} scheduled={}", coreName, didRecovery);\n'
            "        if (!didRecovery) {",
        )
        text = replace_once(
            text,
            '      log.debug("publishing state={}", state);',
            '      log.debug("publishing state={}", state);\n'
            '      log.warn("SOLR18586_PROBE publish.before node={} core={} state={}", getNodeName(), cd.getName(), state);',
        )
        text = replace_once(
            text,
            "          ensureRegisteredSearcher(core);",
            '          log.warn("SOLR18586_PROBE ensureSearcher.before core={}", cd.getName());\n'
            "          ensureRegisteredSearcher(core);\n"
            '          log.warn("SOLR18586_PROBE ensureSearcher.after core={}", cd.getName());',
        )
        text = replace_once(
            text,
            "          overseerJobQueue.offer(m);\n        }\n      }",
            "          overseerJobQueue.offer(m);\n        }\n      }\n"
            '      log.warn("SOLR18586_PROBE publish.after node={} core={} state={}", getNodeName(), cd.getName(), state);',
        )
        return text

    # Locate executor queuing/locking separately from recovery strategy execution.
    text = replace_once(
        text,
        "    Runnable recoveryTask =",
        '    log.warn("SOLR18586_PROBE recovery.scheduled core={}", cd.getName());\n'
        "    Runnable recoveryTask =",
    )
    text = replace_once(
        text,
        "                recoveryLock.lock();",
        '                log.warn("SOLR18586_PROBE recoveryLock.before core={}", cd.getName());\n'
        "                recoveryLock.lock();\n"
        '                log.warn("SOLR18586_PROBE recoveryLock.after core={}", cd.getName());',
    )
    return replace_once(
        text,
        "                    future.get();",
        '                    log.warn("SOLR18586_PROBE recoveryFuture.before core={}", cd.getName());\n'
        "                    future.get();\n"
        '                    log.warn("SOLR18586_PROBE recoveryFuture.after core={}", cd.getName());',
    )


def main():
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument("ticket", choices=["18586", "18587"])
    parser.add_argument("root", type=Path)
    parser.add_argument("--apply", action="store_true", help="write changes in the clean scratch tree")
    args = parser.parse_args()
    root = args.root.resolve()
    head = subprocess.check_output(["git", "-C", str(root), "rev-parse", "HEAD"], text=True).strip()
    if head != BASE:
        raise SystemExit(f"Scratch HEAD must be {BASE}, got {head}")
    if subprocess.check_output(["git", "-C", str(root), "status", "--porcelain"], text=True).strip():
        raise SystemExit("Scratch tree must be clean; store this script outside it")
    paths = (
        ["solr/core/src/java/org/apache/solr/core/CoreContainer.java"]
        if args.ticket == "18587" else [
            "solr/core/src/java/org/apache/solr/cloud/RecoveryStrategy.java",
            "solr/core/src/java/org/apache/solr/cloud/ZkController.java",
            "solr/core/src/java/org/apache/solr/update/DefaultSolrCoreState.java",
        ]
    )
    changes = []
    for path in paths:
        before = (root / path).read_text(encoding="utf-8")
        after = instrument(args.ticket, path, before)
        changes.append((path, after))
        print("".join(difflib.unified_diff(
            before.splitlines(True), after.splitlines(True), fromfile="a/" + path, tofile="b/" + path
        )), end="")
    if args.apply:
        for path, after in changes:
            (root / path).write_text(after, encoding="utf-8", newline="\n")


if __name__ == "__main__":
    main()
```

## Linux execution and evidence

Nick brings this handoff to a Linux host; no gate or Linux worker was dispatched by Windows. Claim the remaining probe slice and serialize all execution under that host's build lock. Record JDK version, architecture, resources, source head, commands, exit status, complete logs and XML. Use a clean scratch worktree at the full RC3 SHA above; the instrumented run is separate from the clean control.

1. Reproduce `org.apache.solr.cloud.TestTlogReplica.testKillLeader` at seed `3635CC89D23D0929` in a clean RC3 worktree. The earlier pre-leg ran the whole class; a new focused control establishes the exact method-filter shape for the forthcoming pair.
2. On a separate clean RC3 scratch worktree, apply `python3 /outside-tree/rc3-failure-probes.py 18586 /scratch/rc3-tlog --apply`. Save `git diff` with the run artifacts before running. Use the identical method filter and seed.
3. Interpret phase markers for the restarted replica's core/node, the leader's publications, existing exception stack traces, and a JVM thread dump taken while the wait is stalled. Capture `jcmd <test-worker-pid> Thread.print -l` before the failure tears the cluster down. Find the worker PID from `jcmd -l`; do not dump only the Gradle daemon. The last marker alone is a lead; match it to a waiting/blocked stack or exception.
4. If the focused method does not reproduce, record that and widen both control and scratch to the whole class at the same seed to match the original failing shape. Do not change just one arm or infer that logging fixed the bug. Preserve each run's evidence before starting another.
5. Only after probes establish a bounded cause, implement on `solr-18586-tlog-rejoin-recovering` from RC3, remove every diagnostic change, push to the fork, and report the head. Otherwise report "not established" plus the phase/stack evidence.

Focused invocation, on Linux only and under the host lock:

```bash
./gradlew :solr:core:test --tests org.apache.solr.cloud.TestTlogReplica.testKillLeader -Ptests.seed=3635CC89D23D0929 --no-daemon
```

Archive each run immediately, including failures. Example runner fragment (invoke from the selected scratch worktree, with a unique absolute run directory):

```bash
set -eu
run_dir=/absolute/evidence/solr-18586/rc3-control-seed-3635CC89D23D0929
mkdir -p "$run_dir"
git rev-parse HEAD > "$run_dir/head.txt"
git diff > "$run_dir/probe.diff"
java -version > "$run_dir/jdk.txt" 2>&1
set +e
./gradlew :solr:core:test --tests org.apache.solr.cloud.TestTlogReplica.testKillLeader -Ptests.seed=3635CC89D23D0929 --no-daemon > "$run_dir/gradle.log" 2>&1
status=$?
set -e
printf '%s\n' "$status" > "$run_dir/exit-status.txt"
if [ -d solr/core/build/test-results/test ]; then
  cp -a solr/core/build/test-results/test "$run_dir/test-results"
fi
exit "$status"
```

Run this detached using the host's documented runner and lock. Use a different worktree/run directory for the instrumented arm. Do not reuse old XML as fresh evidence. For a fix, the clean base and clean fix must run the same method/filter and seed; probe runs are diagnostic and do not replace either proof leg. Also cover `testRecovery` and `testOnlyLeaderIndexes` at seeds `3635CC89D23D0929` and `9D6BB87C4F145405` in matched shapes, then the focused class and formatting/check tasks named by the eventual gate. Archive each invocation before the next.

## Outcome and boundaries

No production fix, no branch head and no completed proof pair for SOLR-18586. The deliverable is a concrete diagnostic handoff, with runnable instrumentation rather than another proposed peersync flag change. No API, persistence, security or npm dependency changes. Public PR/Jira/dev-list actions remain owner-controlled. The next Linux report should carry runtime findings, any supported fix branch/head, matched-pair logs/XML and gate disposition. The original implementation assignment remains incomplete until that work is delivered.
