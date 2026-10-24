"""Direct CPU-only compilation of pinned PR 2702 source variants; no build tool."""
from pathlib import Path
from concurrent.futures import ThreadPoolExecutor
import hashlib
import json
import os
import subprocess
import urllib.request
import xml.etree.ElementTree as ET
import zipfile

ROOT = Path(__file__).resolve().parent
PREVIOUS = ROOT.parent
BASE = ROOT / "base"
MODULE = BASE / "java/cuvs-lucene"
JDK = Path(os.environ.get("CUVS_CPU_JDK", str(PREVIOUS / "toolchain/jdk-22.0.2+9")))
RESULTS = ROOT / "results"
RESULTS.mkdir(exist_ok=True)
NS = {"m": "http://maven.apache.org/POM/4.0.0"}


def java_tool(name):
    return str(JDK / "bin" / (name + (".exe" if os.name == "nt" else "")))


def artifact(spec):
    group, name, version = spec
    dest = ROOT / "jars" / f"{name}-{version}.jar"
    dest.parent.mkdir(exist_ok=True)
    url = "https://repo.maven.apache.org/maven2/" + group.replace(".", "/")
    url += f"/{name}/{version}/{dest.name}"
    if not dest.exists():
        with urllib.request.urlopen(url) as response:
            dest.write_bytes(response.read())
    with urllib.request.urlopen(url + ".sha1") as response:
        digest = response.read().decode().strip().split()[0]
    assert hashlib.sha1(dest.read_bytes()).hexdigest() == digest
    return {"path": str(dest), "url": url,
            "sha256": hashlib.sha256(dest.read_bytes()).hexdigest()}


def compile_sources(label, sources, cp=(), release="21"):
    out = ROOT / "classes" / label
    out.mkdir(parents=True, exist_ok=True)
    args = [java_tool("javac"), "--release", release, "-proc:none", "-implicit:none",
            "-d", str(out)]
    if cp:
        args += ["-cp", os.pathsep.join(map(str, cp))]
    argfile = ROOT / ("javac-" + label + ".args")
    # javac argfiles avoid Windows' process-command length limit.
    argfile.write_text("\n".join('"' + str(s).replace("\\", "/") + '"' for s in sources))
    command = args + ["@" + str(argfile)]
    p = subprocess.run(command, text=True, capture_output=True)
    (RESULTS / (label + ".log")).write_text(p.stdout + p.stderr, encoding="utf-8")
    result = {"label": label, "exit_code": p.returncode, "command": command,
              "sources": [str(p.relative_to(ROOT)) for p in sources],
              "source_count": len(sources), "class_count": len(list(out.rglob("*.class")))}
    (RESULTS / (label + ".json")).write_text(json.dumps(result, indent=2) + "\n")
    print(json.dumps({k: v for k, v in result.items() if k not in ("command", "sources")}))
    if p.returncode:
        print(p.stderr)
    return result


def jar_classes(label, resources=()):
    classes = ROOT / "classes" / label
    target = ROOT / (label + ".jar")
    with zipfile.ZipFile(target, "w", zipfile.ZIP_DEFLATED) as z:
        for p in classes.rglob("*.class"):
            z.write(p, p.relative_to(classes).as_posix())
        for directory in resources:
            if directory.exists():
                for p in directory.rglob("*"):
                    if p.is_file():
                        z.write(p, p.relative_to(directory).as_posix())
    return target


def main():
    modules = []
    specs = set()
    for minor in ("10.2", "10.3", "10.4", "10.5"):
        p = MODULE / ("lucene-" + minor) / "pom.xml"
        xml = ET.parse(p).getroot()
        properties = {e.tag.split("}")[-1]: e.text for e in xml.find("m:properties", NS)}
        version = properties["lucene.version"]
        for name in ("core", "backward-codecs", "codecs", "test-framework", "analysis-common"):
            specs.add(("org.apache.lucene", "lucene-" + name, version))
        sources = list((MODULE / "src/main/java").rglob("*.java"))
        sources += list((p.parent / "src/main/java").rglob("*.java"))
        for prop in ("knnVectorsReader", "knnVectorsWriter", "knnFloatVectorQuery"):
            variant = properties["cuvs.lucene.compat." + prop]
            sources += list((MODULE / "compat" / variant / "src/main/java").rglob("*.java"))
        # Honor the exact add-since-sources list in the module POM.
        for entry in xml.findall(".//m:execution[m:id='add-since-sources']//m:source", NS):
            sources += list((p.parent / entry.text).rglob("*.java"))
        modules.append({"minor": minor, "version": version,
                        "sources": sorted(set(sources)), "properties": properties})
    specs.update({("junit", "junit", "4.13.2"),
                  ("org.hamcrest", "hamcrest", "2.2"),
                  ("com.carrotsearch.randomizedtesting", "randomizedtesting-runner", "2.8.3")})
    with ThreadPoolExecutor(max_workers=8) as pool:
        manifest = list(pool.map(artifact, sorted(specs)))
    (ROOT / "artifact-manifest.json").write_text(json.dumps(manifest, indent=2) + "\n")
    api_sources = sorted(p for p in (BASE / "java/cuvs-java/src/main/java").rglob("*.java")
                         if p.name != "module-info.java")
    api = compile_sources("cuvs-api", api_sources)
    assert api["exit_code"] == 0
    api_jar = jar_classes("cuvs-api")
    results = []
    for module in modules:
        cp = [ROOT / "jars" / f"lucene-{n}-{module['version']}.jar"
              for n in ("core", "backward-codecs", "codecs")]
        cp.append(api_jar)
        label = "module-" + module["minor"]
        result = compile_sources(label, module["sources"], cp)
        result.update({"lucene": module["version"], "properties": module["properties"]})
        results.append(result)
        if result["exit_code"] == 0:
            jar_classes(label, [MODULE / "src/main/resources",
                                MODULE / ("lucene-" + module["minor"]) / "src/main/resources"])
    (RESULTS / "matrix.json").write_text(json.dumps(results, indent=2) + "\n")
    assert all(r["exit_code"] == 0 for r in results)


if __name__ == "__main__":
    main()
