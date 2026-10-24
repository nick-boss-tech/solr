"""Execute direct javac proofs, capturing exact source lists and diagnostics."""
from pathlib import Path
import json
import os
import re
import subprocess
import sys
import zipfile

ROOT = Path(__file__).resolve().parent
JDK = ROOT / "toolchain/jdk-22.0.2+9"
SOURCE = ROOT / "release/java/cuvs-lucene/src/main/java"
PATCHED = ROOT / "patched/java/cuvs-lucene/src/main/java"
RESULTS = ROOT / "results"
RESULTS.mkdir(exist_ok=True)
JAVA = {p.stem: p for p in SOURCE.rglob("*.java") if p.name != "package-info.java"}
JAVA["AcceleratedHNSWUtils"] = PATCHED / "com/nvidia/cuvs/lucene/AcceleratedHNSWUtils.java"


def run(label, sources, version, extra=()):
    out = ROOT / "classes" / label
    out.mkdir(parents=True, exist_ok=True)
    cp = [ROOT / "jars" / f"lucene-{a}-{version}.jar" for a in ("core", "backward-codecs", "codecs")]
    cp += [ROOT / "jars/cuvs-java-26.10.0.jar", *extra]
    args = [str(JDK / "bin/javac.exe"), "--release", "22", "-proc:none", "-implicit:none",
            "-classpath", os.pathsep.join(map(str, cp)), "-d", str(out), *map(str, sources)]
    result = subprocess.run(args, text=True, capture_output=True)
    (RESULTS / (label + ".log")).write_text(result.stdout + result.stderr, encoding="utf-8")
    record = {"label": label, "exit_code": result.returncode, "lucene": version,
              "command": args, "sources": [str(p.relative_to(ROOT)) for p in sources],
              "class_files": len(list(out.rglob("*.class"))), "log": label + ".log"}
    print(json.dumps({k: record[k] for k in ("label", "exit_code", "class_files", "sources")}))
    return record


def main():
    closure = {"AcceleratedHNSWUtils"}
    while True:
        before = set(closure)
        for name in list(closure):
            text = JAVA[name].read_text(encoding="utf-8")
            text = re.sub(r"/\*.*?\*/|//[^\n]*", "", text, flags=re.S)
            closure.update(set(re.findall(r"\b[A-Za-z_$][\w$]*\b", text)) & JAVA.keys())
        if closure == before:
            break
    affected_sources = [JAVA[k] for k in sorted(closure)]
    all_sources = list(JAVA.values()) + [SOURCE / "com/nvidia/cuvs/lucene/package-info.java"]
    proof = []
    for version in ("10.2.0", "10.4.0"):
        proof.append(run("full-" + version, all_sources, version))
        proof.append(run("affected-" + version, affected_sources, version))
    (RESULTS / "compile-results.json").write_text(json.dumps(proof, indent=2) + "\n")
    assert [r["exit_code"] for r in proof] == [0, 0, 1, 0], proof
    write_shim()


def write_shim():
    # Only the outer class changed. Preserve the original nested enum byte-for-byte.
    original = ROOT / "jars/cuvs-lucene-26.10.0.jar"
    replacement_root = ROOT / "classes/affected-10.2.0"
    replacements = {p.relative_to(replacement_root).as_posix(): p.read_bytes()
                    for p in replacement_root.rglob("AcceleratedHNSWUtils.class")}
    assert replacements and any(p.endswith("AcceleratedHNSWUtils.class") for p in replacements)
    shim = ROOT / "cuvs-lucene-26.10.0-lucene104-shim.jar"
    with zipfile.ZipFile(original) as source, zipfile.ZipFile(shim, "w", zipfile.ZIP_DEFLATED) as target:
        for item in source.infolist():
            target.writestr(item, replacements.pop(item.filename, source.read(item)))
        for name, data in replacements.items():
            target.writestr(name, data)
    print("Shim: " + str(shim))


if __name__ == "__main__":
    write_shim() if "--shim-only" in sys.argv else main()
