"""Non-build checks of source provenance, patch applicability and shim contents."""
from pathlib import Path
import hashlib
import json
import subprocess
import urllib.request
import zipfile
from linkage_audit import parse

ROOT = Path(__file__).resolve().parent
GIT = r"C:\Users\shaninna\AppData\Local\Programs\Git\cmd\git.exe"
JAVA = ROOT / "toolchain/jdk-22.0.2+9/bin"


def main():
    pinned = {
        "upstream/java/cuvs-lucene/src/main/java/com/nvidia/cuvs/lucene/AcceleratedHNSWUtils.java":
            "https://raw.githubusercontent.com/NVIDIA/cuvs/244f118479961a4448c2dfa294a4c2b93898edf6/"
            "java/cuvs-lucene/src/main/java/com/nvidia/cuvs/lucene/AcceleratedHNSWUtils.java",
        "legacy-writer.java":
            "https://raw.githubusercontent.com/NVIDIA/cuvs-lucene/5488ac8ef3f56c699f96d91e557be7aa7ce3a727/"
            "src/main/java/com/nvidia/cuvs/lucene/Lucene99AcceleratedHNSWVectorsWriter.java",
    }
    for relative, url in pinned.items():
        target = ROOT / relative
        if not target.exists():
            target.parent.mkdir(parents=True, exist_ok=True)
            with urllib.request.urlopen(url) as response:
                target.write_bytes(response.read())
    checked = []
    for name in ("release", "upstream"):
        cmd = [GIT, "-C", str(ROOT / name), "apply", "--check",
               str(ROOT / "cuvs-lucene-lucene104-fix.patch")]
        p = subprocess.run(cmd, capture_output=True, text=True)
        assert p.returncode == 0, p.stderr
        checked.append({"snapshot": name, "command": cmd, "exit_code": p.returncode})
    java_path = "java/cuvs-lucene/src/main/java/com/nvidia/cuvs/lucene/AcceleratedHNSWUtils.java"
    with zipfile.ZipFile(ROOT / "jars/cuvs-lucene-26.10.0-sources.jar") as z:
        published = z.read("com/nvidia/cuvs/lucene/AcceleratedHNSWUtils.java")
        assert published == (ROOT / "release" / java_path).read_bytes()
    with zipfile.ZipFile(ROOT / "jars/cuvs-lucene-25.12.0-sources.jar") as z:
        legacy = z.read("com/nvidia/cuvs/lucene/Lucene99AcceleratedHNSWVectorsWriter.java")
        assert legacy == (ROOT / "legacy-writer.java").read_bytes()
    with zipfile.ZipFile(ROOT / "jars/cuvs-lucene-26.10.0.jar") as old, zipfile.ZipFile(
            ROOT / "cuvs-lucene-26.10.0-lucene104-shim.jar") as new:
        assert set(old.namelist()) == set(new.namelist())
        changed = [n for n in old.namelist() if old.read(n) != new.read(n)]
        assert changed == ["com/nvidia/cuvs/lucene/AcceleratedHNSWUtils.class"], changed
        versioned = [n for n in new.namelist() if n.startswith("META-INF/versions/")]
        assert not versioned, versioned
        names = sorted(n for n in new.namelist() if n.endswith(".class"))
    records = []
    for version in ("10.2.0", "10.4.0"):
        for cls in ("org.apache.lucene.store.IOContext", "org.apache.lucene.index.LeafReader",
                    "org.apache.lucene.codecs.KnnVectorsReader"):
            cp = str(ROOT / "jars" / f"lucene-core-{version}.jar")
            cmd = [str(JAVA / "javap.exe"), "-p", "-s", "-classpath", cp, cls]
            p = subprocess.run(cmd, capture_output=True, text=True)
            assert p.returncode == 0, p.stderr
            log = f"javap-{cls.rsplit('.', 1)[-1]}-{version}.log"
            (ROOT / "results" / log).write_text(p.stdout + p.stderr, encoding="utf-8")
            records.append({"command": cmd, "exit_code": p.returncode, "log": log})
    result = {"status": "PASS", "patch_checks": checked,
              "release_source_matches_maven_sources": True,
              "legacy_source_matches_maven_sources": True,
              "changed_jar_entries": changed, "jar_classes": names,
              "jar_sha256": hashlib.sha256((ROOT / "cuvs-lucene-26.10.0-lucene104-shim.jar").read_bytes()).hexdigest(),
              "javap": records}
    (ROOT / "results/package-checks.json").write_text(json.dumps(result, indent=2) + "\n")
    print(json.dumps({k: v for k, v in result.items() if k not in ("jar_classes", "javap")}, indent=2))


if __name__ == "__main__":
    main()
