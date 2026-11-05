#!/usr/bin/env python3
"""Run one Solr Gradle step through sweep-run.sh with continuous resource guard."""
import datetime
import os
import pathlib
import signal
import subprocess
import sys
import time

if len(sys.argv) < 5:
    raise SystemExit("usage: run-guarded.py <worktree> <log-dir> <name> <gradle args...>")
worktree, log_dir, name, *gradle_args = sys.argv[1:]
worktree = pathlib.Path(worktree)
log_dir = pathlib.Path(log_dir)
log_dir.mkdir(parents=True, exist_ok=True)
monitor = log_dir / f"{name}.monitor.tsv"
driver_log = log_dir / f"{name}.driver.log"
runner = "/workspace/solr/pool/bin/sweep-run.sh"
gradle_home = "/workspace/gradle-home"
limit_pct = 18.0
limit_bytes = 3_000_000_000

def utc():
    return datetime.datetime.now(datetime.timezone.utc).isoformat()

def sample():
    rows = []
    for p in ("/", str(worktree), gradle_home):
        path = pathlib.Path(p)
        while not path.exists() and path != path.parent:
            path = path.parent
        st = os.statvfs(path)
        total, free = st.f_files, st.f_favail
        if not total:
            st = os.statvfs("/")
            total, free = st.f_files, st.f_favail
        free_bytes = st.f_bavail * st.f_frsize
        pct = 100.0 * free / total
        rows.append((p, free, total, pct, free_bytes))
    return rows

def log_sample(status):
    rows = sample()
    with monitor.open("a", encoding="utf-8") as f:
        for p, free, total, pct, free_bytes in rows:
            f.write(f"{utc()}\t{status}\t{p}\t{free}\t{total}\t{pct:.2f}\t{free_bytes}\n")
    min_pct = min(r[3] for r in rows)
    min_bytes = min(r[4] for r in rows)
    return rows, min_pct, min_bytes

_, start_pct, start_bytes = log_sample("pre")
if start_pct <= limit_pct or start_bytes <= limit_bytes:
    print(f"refused before launch: free_inode_pct={start_pct:.2f}, free_bytes={start_bytes}; thresholds are >{limit_pct}% and >{limit_bytes}", flush=True)
    raise SystemExit(4)
env = os.environ.copy()
env.pop("NODE_ENV", None)
env["JAVA_HOME"] = "/usr/lib/jvm/java-21-openjdk-amd64"
env["GRADLE_USER_HOME"] = gradle_home
env["PATH"] = env["JAVA_HOME"] + "/bin:" + env.get("PATH", "")
env["SWEEP_WAIT"] = "60"
cmd = ["bash", runner, str(worktree), str(log_dir), name, *gradle_args]
print(f"start {name} at {utc()} head={subprocess.check_output(['git','-C',str(worktree),'rev-parse','HEAD'],text=True).strip()} free_inode_pct={start_pct:.2f} free_bytes={start_bytes}", flush=True)
with driver_log.open("w", encoding="utf-8") as out:
    proc = subprocess.Popen(cmd, stdout=out, stderr=subprocess.STDOUT, env=env, start_new_session=True)
    last_progress = time.monotonic()
    stopped = False
    while proc.poll() is None:
        time.sleep(20)
        _, pct, free_bytes = log_sample("run")
        if pct <= limit_pct or free_bytes <= limit_bytes:
            stopped = True
            reason = f"resource stop: inode_pct={pct:.2f}, free_bytes={free_bytes}; thresholds are >{limit_pct}% and >{limit_bytes}"
            with monitor.open("a", encoding="utf-8") as f:
                f.write(f"{utc()}\tSTOP\t{reason}\n")
            print(reason, flush=True)
            try:
                os.killpg(proc.pid, signal.SIGTERM)
            except ProcessLookupError:
                pass
            try:
                proc.wait(timeout=20)
            except subprocess.TimeoutExpired:
                try:
                    os.killpg(proc.pid, signal.SIGKILL)
                except ProcessLookupError:
                    pass
                proc.wait()
            break
        if time.monotonic() - last_progress >= 120:
            print(f"still-running {name} at {utc()} free_inode_pct={pct:.2f} free_bytes={free_bytes}", flush=True)
            last_progress = time.monotonic()
    rc = proc.wait()
_, end_pct, end_bytes = log_sample("post")
print(f"finish {name} at {utc()} rc={rc} free_inode_pct={end_pct:.2f} free_bytes={end_bytes} resource_stopped={str(stopped).lower()}", flush=True)
raise SystemExit(4 if stopped else rc)
