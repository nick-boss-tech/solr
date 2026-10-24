"""Reconstruct pinned sources, compile with javac, and run CPU-only file-compatibility proof."""
from concurrent.futures import ThreadPoolExecutor
from pathlib import Path
import argparse
import hashlib
import json
import os
import shutil
import subprocess
import sys
import urllib.request

ROOT = Path(__file__).resolve().parent


def blob(data):
    return hashlib.sha1(b"blob " + str(len(data)).encode() + b"\0" + data).hexdigest()


def download(url, target, expected):
    if not target.exists():
        with urllib.request.urlopen(url) as response:
            data = response.read()
        assert blob(data) == expected, url
        target.parent.mkdir(parents=True, exist_ok=True)
        target.write_bytes(data)
    assert blob(target.read_bytes()) == expected, str(target)


def main():
    parser = argparse.ArgumentParser()
    parser.add_argument("--preflight-only", action="store_true")
    args = parser.parse_args()
    assert "CUVS_CPU_JDK" in os.environ, "Set CUVS_CPU_JDK to an absolute JDK 21 or 22 root."
    assert Path(os.environ["CUVS_CPU_JDK"]).is_absolute()
    inputs = json.loads((ROOT / "inputs-manifest.json").read_text())
    patch = json.loads((ROOT / "patch-manifest.json").read_text())
    r2 = ROOT / "inputs-round2"
    for entry in inputs["files"]:
        download("https://raw.githubusercontent.com/nick-boss-tech/solr/" + inputs["boardCommit"]
                 + "/" + entry["path"], r2 / Path(entry["path"]).name, entry["sha"])
    expected_artifacts = json.loads((r2 / "artifact-manifest.json").read_text())
    os.environ["CUVS_BACKCOMPAT_R2"] = str(r2)
    subprocess.run([sys.executable, str(r2 / "prepare_sources.py")], check=True)
    source = r2 / "base" / patch["path"]
    assert blob(source.read_bytes()) == patch["before"]
    target = ROOT / "final" / patch["path"]
    git = os.environ.get("CUVS_CPU_GIT", "git")
    if not target.exists():
        target.parent.mkdir(parents=True, exist_ok=True)
        shutil.copyfile(source, target)
        command = [git, "-c", "core.autocrlf=false", "-C", str(ROOT / "final"), "apply"]
        subprocess.run([*command, "--check", "--whitespace=error-all", str(ROOT / "backcompat.patch")], check=True)
        subprocess.run([*command, str(ROOT / "backcompat.patch")], check=True)
    assert blob(target.read_bytes()) == patch["after"]
    assert hashlib.sha256((ROOT / "backcompat.patch").read_bytes()).hexdigest() == patch["patchSha256"]
    fixtures = json.loads((ROOT / "fixture-manifest.json").read_text())

    def fixture(entry):
        download("https://raw.githubusercontent.com/" + patch["repo"] + "/" + patch["head"]
                 + "/" + entry["path"], ROOT / "resources/backcompat" / Path(entry["path"]).name,
                 entry["sha"])

    with ThreadPoolExecutor(max_workers=8) as pool:
        list(pool.map(fixture, fixtures))
    sys.path.insert(0, str(r2))
    from compile_matrix import java_tool
    formatter = r2 / "jars/google-java-format-1.27.0-all-deps.jar"
    if not formatter.exists():
        url = ("https://repo.maven.apache.org/maven2/com/google/googlejavaformat/"
               "google-java-format/1.27.0/google-java-format-1.27.0-all-deps.jar")
        with urllib.request.urlopen(url) as response:
            data = response.read()
        assert hashlib.sha256(data).hexdigest() == "ed07239f3cb72e25bf2a0eae63e76831f9f11963bd19fc36a6f1d87016ac1763"
        formatter.parent.mkdir(exist_ok=True)
        formatter.write_bytes(data)
    assert hashlib.sha256(formatter.read_bytes()).hexdigest() == "ed07239f3cb72e25bf2a0eae63e76831f9f11963bd19fc36a6f1d87016ac1763"
    subprocess.run([java_tool("java"), "-jar", str(formatter), "--dry-run", "--set-exit-if-changed",
                    str(target)], check=True)
    (ROOT / "results").mkdir(exist_ok=True)
    runtime = subprocess.run([java_tool("java"), "-version"], capture_output=True, text=True, check=True)
    (ROOT / "results/runtime.log").write_text(runtime.stdout + runtime.stderr)
    if args.preflight_only:
        print("Verified pinned source, fixtures, patch application and formatting; tests not run.")
        return
    subprocess.run([sys.executable, str(r2 / "compile_matrix.py")], check=True)
    for entry in expected_artifacts:
        jar = r2 / "jars" / entry["url"].rsplit("/", 1)[1]
        assert hashlib.sha256(jar.read_bytes()).hexdigest() == entry["sha256"]
    for command in ([sys.executable, str(ROOT / "run_backcompat.py")],
                    [sys.executable, str(ROOT / "run_backcompat.py"), "--final"],
                    [sys.executable, str(ROOT / "run_mapping_control.py")],
                    [sys.executable, str(ROOT / "verify_results.py")]):
        subprocess.run(command, check=True)
    print("PASS: original CPU read/upgrade matrix, final sparse/deletion matrix, and data-fault controls.")


if __name__ == "__main__":
    main()
