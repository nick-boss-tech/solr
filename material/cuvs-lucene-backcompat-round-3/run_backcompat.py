"""Run pinned upstream archived-index CPU tests without Maven, Gradle or native code."""
from pathlib import Path
import json
import argparse
import hashlib
import os
import subprocess
import sys

ROOT = Path(__file__).resolve().parent
R2 = Path(os.environ.get("CUVS_BACKCOMPAT_R2", str(ROOT.parent / "round2")))
sys.path.insert(0, str(R2))
from compile_matrix import java_tool
from run_cpu_tests import classpath


def main():
    parser = argparse.ArgumentParser()
    parser.add_argument("--patched", action="store_true")
    parser.add_argument("--final", action="store_true")
    args = parser.parse_args()
    (ROOT / "results").mkdir(exist_ok=True)
    results = []
    for module in json.loads((R2 / "results/matrix.json").read_text()):
        minor = module["label"].removeprefix("module-")
        cp = classpath(module)
        label = ("final-" if args.final else "patched-" if args.patched else "baseline-") + minor
        classes = ROOT / "classes" / label
        classes.mkdir(parents=True, exist_ok=True)
        test_root = ROOT / "final" if args.final else ROOT / "patched" if args.patched else R2 / "base"
        test = test_root / "java/cuvs-lucene/src/test/java/com/nvidia/cuvs/lucene/TestBackCompatIndices.java"
        sources = [test, ROOT / "BackCompatRunner.java"]
        command = [java_tool("javac"), "--release", "21", "-proc:none", "-implicit:none",
                   "-cp", os.pathsep.join(map(str, cp)), "-d", str(classes), *map(str, sources)]
        compiled = subprocess.run(command, text=True, capture_output=True)
        (ROOT / "results" / (label + "-compile.log")).write_text(compiled.stdout + compiled.stderr)
        assert compiled.returncode == 0, compiled.stderr
        command = [java_tool("java"), "-ea", "-Dtests.seed=18556C0D", "-Djava.awt.headless=true",
                   "-cp", os.pathsep.join(map(str, [classes, ROOT / "resources", *cp])),
                   "BackCompatRunner"]
        command += (["testUpgradeIndicesWithDeletesAndSparseVectors"] if args.patched or args.final else
                    ["testReadIndices", "testUpgradeIndices"])
        run = subprocess.run(command, text=True, capture_output=True)
        output = run.stdout + run.stderr
        (ROOT / "results" / (label + "-tests.log")).write_text(output, encoding="utf-8")
        results.append({"minor": minor, "exit_code": run.returncode, "command": command,
                        "test_source_sha256": hashlib.sha256(test.read_bytes()).hexdigest()})
        print(label, "exit", run.returncode,
              "\n".join(line for line in output.splitlines() if "CPU_RESULT" in line), flush=True)
        if run.returncode:
            print(output[-18000:], flush=True)
    result_name = "final.json" if args.final else "patched.json" if args.patched else "baseline.json"
    (ROOT / "results" / result_name).write_text(json.dumps(results, indent=2) + "\n")
    assert all(r["exit_code"] == 0 for r in results)


if __name__ == "__main__":
    main()
