"""Data fault control, not a production-code mutation or a GPU fixture generator."""
from pathlib import Path
import hashlib
import json
import os
import subprocess
import sys

ROOT = Path(__file__).resolve().parent
R2 = Path(os.environ.get("CUVS_BACKCOMPAT_R2", str(ROOT.parent / "round2")))
sys.path.insert(0, str(R2))
from compile_matrix import java_tool
from run_cpu_tests import classpath


def main():
    results = []
    methods = ["testReadIndices", "testUpgradeIndices", "testUpgradeIndicesWithDeletesAndSparseVectors"]
    for module in json.loads((R2 / "results/matrix.json").read_text()):
        minor = module["label"].removeprefix("module-")
        if minor not in ("10.2", "10.4"):
            continue
        label = "control-" + minor
        classes = ROOT / "classes" / label
        classes.mkdir(parents=True, exist_ok=True)
        cp = classpath(module)
        test = ROOT / "final/java/cuvs-lucene/src/test/java/com/nvidia/cuvs/lucene/TestBackCompatIndices.java"
        command = [java_tool("javac"), "--release", "21", "-proc:none", "-implicit:none",
                   "-cp", os.pathsep.join(map(str, cp)), "-d", str(classes),
                   str(test), str(ROOT / "BackCompatRunner.java"), str(ROOT / "FixtureMappingControl.java")]
        compiled = subprocess.run(command, text=True, capture_output=True)
        (ROOT / "results" / (label + "-compile.log")).write_text(compiled.stdout + compiled.stderr)
        assert compiled.returncode == 0, compiled.stderr
        codec = "Lucene101AcceleratedHNSWCodec" if minor == "10.2" else "Lucene104AcceleratedHNSWCodec"
        name = minor + "-" + codec + ".zip"
        fixture = ROOT / "resources/backcompat" / name
        resource = ROOT / "controls" / minor / "resources"
        output = resource / "backcompat" / name
        work = ROOT / "controls" / minor / "work"
        work.parent.mkdir(parents=True, exist_ok=True)
        generate = [java_tool("java"), "-ea", "-cp", os.pathsep.join(map(str, [classes, *cp])),
                    "FixtureMappingControl", str(fixture), str(output), str(work)]
        generated = subprocess.run(generate, text=True, capture_output=True)
        (ROOT / "results" / (label + "-generate.log")).write_text(generated.stdout + generated.stderr)
        assert generated.returncode == 0, generated.stdout + generated.stderr
        command = [java_tool("java"), "-ea", "-Dtests.seed=18556C0D", "-Djava.awt.headless=true",
                   "-cp", os.pathsep.join(map(str, [classes, resource, *cp])),
                   "BackCompatRunner", *methods]
        run = subprocess.run(command, text=True, capture_output=True)
        output_text = run.stdout + run.stderr
        (ROOT / "results" / (label + "-tests.log")).write_text(output_text, encoding="utf-8")
        for method in methods[:2]:
            assert f"CPU_RESULT {method} run=1 failures=0 ignored=0 assumptions=0" in output_text
        assert f"CPU_RESULT {methods[2]} run=1 failures=1 ignored=0 assumptions=0" in output_text
        assert "vector for id " in output_text and "arrays first differed" in output_text
        assert run.returncode != 0
        result = {"minor": minor, "exit_code": run.returncode, "expected_failure": methods[2],
                  "test_source_sha256": hashlib.sha256(test.read_bytes()).hexdigest(),
                  "fixture_sha256": hashlib.sha256(output.read_bytes()).hexdigest(), "command": command}
        results.append(result)
        print(label, "existing methods pass; new vector-identity assertion rejects reassociation", flush=True)
    (ROOT / "results/mapping-control.json").write_text(json.dumps(results, indent=2) + "\n")


if __name__ == "__main__":
    main()
