from pathlib import Path
import json
import os
import subprocess

ROOT = Path(__file__).resolve().parent
JDK = ROOT / "toolchain/jdk-22.0.2+9"
OUT = ROOT / "classes/harness"
OUT.mkdir(parents=True, exist_ok=True)
CP102 = os.pathsep.join(str(ROOT / "jars" / f"lucene-{a}-10.2.0.jar")
                       for a in ("core", "backward-codecs", "codecs"))
compile_cmd = [str(JDK / "bin/javac.exe"), "--release", "22", "-cp", CP102,
               "-d", str(OUT), str(ROOT / "SortedNodesSemantics.java")]
compiled = subprocess.run(compile_cmd, capture_output=True, text=True)
assert compiled.returncode == 0, compiled.stdout + compiled.stderr
results = []
for label, classes, lucene, mode in [
    ("semantics-compiled102-runtime102", "affected-10.2.0", "10.2.0", []),
    ("semantics-compiled104-runtime102", "affected-10.4.0", "10.2.0", []),
    ("semantics-compiled102-runtime104", "affected-10.2.0", "10.4.0", ["replacement-only"]),
]:
    cp = [OUT, ROOT / "classes" / classes,
          ROOT / "jars/cuvs-lucene-26.10.0.jar", ROOT / "jars/cuvs-java-26.10.0.jar"]
    cp += [ROOT / "jars" / f"lucene-{a}-{lucene}.jar"
           for a in ("core", "backward-codecs", "codecs")]
    cmd = [str(JDK / "bin/java.exe"), "-ea", "-cp", os.pathsep.join(map(str, cp)),
           "SortedNodesSemantics", *mode]
    ran = subprocess.run(cmd, capture_output=True, text=True)
    (ROOT / "results" / (label + ".log")).write_text(ran.stdout + ran.stderr, encoding="utf-8")
    results.append({"label": label, "exit_code": ran.returncode, "command": cmd})
    print(label, "exit", ran.returncode, ran.stdout + ran.stderr)
(ROOT / "results/semantics-results.json").write_text(json.dumps(results, indent=2) + "\n")
assert all(r["exit_code"] == 0 for r in results)
