"""CPU-only proof of the PR 2970 test port: direct javac and JUnit, no Maven, Gradle, native or GPU.

Run from a fresh copy of this directory with CUVS_CPU_JDK set to an absolute JDK 21 root:
    python3 reproduce.py --preflight-only
    python3 reproduce.py
"""
from concurrent.futures import ThreadPoolExecutor
from pathlib import Path
import argparse
import hashlib
import json
import os
import re
import shutil
import subprocess
import sys
import urllib.request
import xml.etree.ElementTree as ET
import zipfile

ROOT = Path(__file__).resolve().parent
BASE = ROOT / "base"
PATCHED = ROOT / "patched"
RESULTS = ROOT / "results"
MODULE = "java/cuvs-lucene"
PKG = "src/test/java/com/nvidia/cuvs/lucene"
QUERY_TEST = f"{MODULE}/{PKG}/TestCompatKnnFloatVectorQuery.java"
BACKCOMPAT_TEST = f"{MODULE}/{PKG}/TestBackCompatIndices.java"
SEED = "18556C0D"
MINORS = ("10.2", "10.3")
NS = {"m": "http://maven.apache.org/POM/4.0.0"}
FORMATTER_SHA256 = "ed07239f3cb72e25bf2a0eae63e76831f9f11963bd19fc36a6f1d87016ac1763"
TEST_JARS = [("junit", "junit", "4.13.2"), ("org.hamcrest", "hamcrest", "2.2"),
             ("com.carrotsearch.randomizedtesting", "randomizedtesting-runner", "2.8.3")]
NEW_QUERY_TESTS = {"testFilterIsForwarded", "testDeletedDocumentsAreExcluded",
                   "testFilterAndDeletesAreIntersected"}
NEW_BACKCOMPAT_TEST = "testUpgradeIndicesWithDeletesAndSparseVectors"
CONTROL_INPUT = "releases/26.10.0/10.2/Lucene101AcceleratedHNSWCodec.zip"


def blob(data):
    return hashlib.sha1(b"blob " + str(len(data)).encode() + b"\0" + data).hexdigest()


def sha256(path):
    return hashlib.sha256(Path(path).read_bytes()).hexdigest()


def jdk_tool(name):
    return str(Path(os.environ["CUVS_CPU_JDK"]) / "bin" / (name + (".exe" if os.name == "nt" else "")))


def git():
    return os.environ.get("CUVS_CPU_GIT", "git")


def write_json(name, data):
    (RESULTS / name).write_text(json.dumps(data, indent=2) + "\n", encoding="utf-8")


def fetch(url):
    with urllib.request.urlopen(url) as response:
        return response.read()


def prepare_sources():
    manifest = json.loads((ROOT / "source-manifest.json").read_text())

    def source(entry):
        target = BASE / entry["path"]
        if not target.exists():
            data = fetch(f"https://raw.githubusercontent.com/{manifest['repo']}/{manifest['head']}/{entry['path']}")
            assert blob(data) == entry["sha"], entry["path"]
            target.parent.mkdir(parents=True, exist_ok=True)
            target.write_bytes(data)
        assert blob(target.read_bytes()) == entry["sha"], entry["path"]

    with ThreadPoolExecutor(max_workers=8) as pool:
        list(pool.map(source, manifest["files"]))
    patch = json.loads((ROOT / "patch-manifest.json").read_text())
    assert patch["head"] == manifest["head"]
    assert sha256(ROOT / "port.patch") == patch["patchSha256"], "port.patch digest"
    for entry in patch["files"]:
        assert blob((BASE / entry["path"]).read_bytes()) == entry["before"], entry["path"]
    if not PATCHED.exists():
        for entry in patch["files"]:
            target = PATCHED / entry["path"]
            target.parent.mkdir(parents=True, exist_ok=True)
            shutil.copyfile(BASE / entry["path"], target)
        command = [git(), "-c", "core.autocrlf=false", "-C", str(PATCHED), "apply"]
        subprocess.run([*command, "--check", "--whitespace=error-all", str(ROOT / "port.patch")], check=True)
        subprocess.run([*command, "--whitespace=error-all", str(ROOT / "port.patch")], check=True)
    for entry in patch["files"]:
        assert blob((PATCHED / entry["path"]).read_bytes()) == entry["after"], entry["path"]
    print(f"Verified {len(manifest['files'])} pinned source blobs and applied port.patch "
          f"({len(patch['files'])} files, final blobs match).", flush=True)
    return patch


def artifact(spec):
    group, name, version = spec
    dest = ROOT / "jars" / f"{name}-{version}.jar"
    url = "https://repo.maven.apache.org/maven2/" + group.replace(".", "/") + f"/{name}/{version}/{dest.name}"
    if not dest.exists():
        dest.parent.mkdir(exist_ok=True)
        dest.write_bytes(fetch(url))
    expected = fetch(url + ".sha1").decode().strip().split()[0]
    assert hashlib.sha1(dest.read_bytes()).hexdigest() == expected, url
    return {"path": str(dest.relative_to(ROOT)), "url": url, "sha256": sha256(dest)}


def format_check(patch):
    formatter = ROOT / "jars/google-java-format-1.27.0-all-deps.jar"
    if not formatter.exists():
        formatter.parent.mkdir(exist_ok=True)
        formatter.write_bytes(fetch("https://repo.maven.apache.org/maven2/com/google/googlejavaformat/"
                                    "google-java-format/1.27.0/google-java-format-1.27.0-all-deps.jar"))
    assert sha256(formatter) == FORMATTER_SHA256
    exports = [f"--add-exports=jdk.compiler/com.sun.tools.javac.{p}=ALL-UNNAMED"
               for p in ("api", "code", "file", "parser", "tree", "util")]
    # The upstream spotless configuration sets formatJavadoc=false.
    command = [jdk_tool("java"), *exports, "-jar", str(formatter), "--skip-javadoc-formatting",
               "--dry-run", "--set-exit-if-changed"]
    command += [str(PATCHED / e["path"]) for e in patch["files"]]
    p = subprocess.run(command, text=True, capture_output=True)
    (RESULTS / "format.log").write_text(p.stdout + p.stderr, encoding="utf-8")
    write_json("format.json", {"command": command, "exit_code": p.returncode, "formatter_sha256": FORMATTER_SHA256})
    assert p.returncode == 0, p.stdout + p.stderr
    print("google-java-format 1.27.0 (--skip-javadoc-formatting): no change.", flush=True)


def compile_sources(label, sources, cp=()):
    out = ROOT / "classes" / label
    assert not out.exists(), f"{out} exists; use a fresh proof directory"
    out.mkdir(parents=True)
    args = [jdk_tool("javac"), "--release", "21", "-proc:none", "-implicit:none", "-d", str(out)]
    if cp:
        args += ["-cp", os.pathsep.join(map(str, cp))]
    argfile = ROOT / "classes" / (label + ".args")
    argfile.write_text("\n".join('"' + str(s).replace("\\", "/") + '"' for s in sources))
    p = subprocess.run(args + ["@" + str(argfile)], text=True, capture_output=True)
    (RESULTS / (label + "-compile.log")).write_text(p.stdout + p.stderr, encoding="utf-8")
    result = {"label": label, "exit_code": p.returncode, "source_count": len(sources),
              "class_count": len(list(out.rglob("*.class"))),
              "sources": sorted(str(Path(s).relative_to(ROOT)) for s in sources)}
    print(json.dumps({k: v for k, v in result.items() if k != "sources"}), flush=True)
    assert p.returncode == 0, p.stdout + p.stderr
    return out, result


def jar_classes(label, resources=()):
    classes = ROOT / "classes" / label
    target = ROOT / (label + ".jar")
    with zipfile.ZipFile(target, "w", zipfile.ZIP_DEFLATED) as z:
        for p in sorted(classes.rglob("*.class")):
            z.write(p, p.relative_to(classes).as_posix())
        for directory in resources:
            if directory.exists():
                for p in sorted(directory.rglob("*")):
                    if p.is_file():
                        z.write(p, p.relative_to(directory).as_posix())
    return target


def modules():
    found = []
    for minor in MINORS:
        pom = BASE / MODULE / f"lucene-{minor}" / "pom.xml"
        props = {e.tag.split("}")[-1]: e.text for e in ET.parse(pom).getroot().find("m:properties", NS)}
        found.append({"minor": minor, "lucene": props["lucene.version"], "properties": props})
    return found


def compile_matrix():
    api_sources = sorted(p for p in (BASE / "java/cuvs-java/src/main/java").rglob("*.java")
                         if p.name != "module-info.java")
    _, api = compile_sources("cuvs-api", api_sources)
    api_jar = jar_classes("cuvs-api")
    matrix = [api]
    for m in modules():
        lucene = [ROOT / "jars" / f"lucene-{n}-{m['lucene']}.jar" for n in ("core", "backward-codecs", "codecs")]
        mod = BASE / MODULE / f"lucene-{m['minor']}"
        sources = list((BASE / MODULE / "src/main/java").rglob("*.java"))
        sources += list((mod / "src/main/java").rglob("*.java")) + list((mod / "src/since/java").rglob("*.java"))
        for prop in ("knnVectorsReader", "knnFloatVectorQuery"):
            variant = m["properties"]["cuvs.lucene.compat." + prop]
            sources += list((BASE / MODULE / "compat" / variant / "src/main/java").rglob("*.java"))
        label = "module-" + m["minor"]
        _, result = compile_sources(label, sorted(set(sources)), [*lucene, api_jar])
        result.update({"lucene": m["lucene"], "properties": m["properties"]})
        matrix.append(result)
        jar_classes(label, [BASE / MODULE / "src/main/resources", mod / "src/main/resources"])
    write_json("matrix.json", matrix)
    return matrix


def test_classpath(m):
    cp = [ROOT / f"module-{m['minor']}.jar", ROOT / "cuvs-api.jar"]
    cp += [ROOT / "jars" / f"lucene-{n}-{m['lucene']}.jar"
           for n in ("core", "backward-codecs", "codecs", "test-framework", "analysis-common")]
    cp += [ROOT / "jars" / f"{name}-{version}.jar" for _, name, version in TEST_JARS]
    return cp


def junit_summary(output):
    ok = re.search(r"OK \((\d+) tests?\)", output)
    failed = re.search(r"Tests run: (\d+),\s+Failures: (\d+)", output)
    return {"tests_run": int(ok[1]) if ok else int(failed[1]) if failed else None,
            "failures": 0 if ok else int(failed[2]) if failed else None,
            "failed_methods": sorted(re.findall(r"^\d+\) (test\w+)\(", output, re.M))}


def run_java(label, cp, main_args):
    command = [jdk_tool("java"), "-ea", "-Dtests.seed=" + SEED, "-Djava.awt.headless=true",
               "-cp", os.pathsep.join(map(str, cp)), *main_args]
    p = subprocess.run(command, text=True, capture_output=True)
    output = p.stdout + p.stderr
    (RESULTS / (label + ".log")).write_text(output, encoding="utf-8")
    return p.returncode, output, command


def query_tests():
    results = []
    for m in modules():
        cp = test_classpath(m)
        leaf = m["properties"]["cuvs.lucene.compat.leafReader"]
        helper = f"{MODULE}/compat/{leaf}/{PKG}/TestLuceneCompat.java"
        base_classes, _ = compile_sources(f"query-base-{m['minor']}", [BASE / QUERY_TEST], cp)
        port_classes, _ = compile_sources(f"query-port-{m['minor']}", [PATCHED / QUERY_TEST, PATCHED / helper], cp)
        variant = m["properties"]["cuvs.lucene.compat.knnFloatVectorQuery"]
        relative = f"{MODULE}/compat/{variant}/src/main/java/com/nvidia/cuvs/lucene/CompatKnnFloatVectorQuery.java"
        source = (BASE / relative).read_text(encoding="utf-8")
        call = "context.reader().searchNearestVectors(field, getTargetCopy(), results, acceptDocs);"
        assert source.count(call) == 1, relative
        mask = ("null" if variant.endswith("-bits") else
                "org.apache.lucene.search.AcceptDocs.fromLiveDocs(null, context.reader().maxDoc())")
        mutant_src = ROOT / "mutations" / m["minor"] / relative
        mutant_src.parent.mkdir(parents=True, exist_ok=True)
        mutant_src.write_text(source.replace(call, call.replace("acceptDocs", mask)), encoding="utf-8")
        mutant_classes, _ = compile_sources(f"mask-dropped-{m['minor']}", [mutant_src], cp)
        for kind, classes, mutant in (("base", base_classes, False), ("port", port_classes, False),
                                      ("base", base_classes, True), ("port", port_classes, True)):
            label = f"query-{kind}-{m['minor']}" + ("-mask-dropped" if mutant else "")
            run_cp = [classes, *([mutant_classes] if mutant else []), *cp]
            code, output, command = run_java(label, run_cp, ["org.junit.runner.JUnitCore",
                                                             "com.nvidia.cuvs.lucene.TestCompatKnnFloatVectorQuery"])
            result = {"label": label, "minor": m["minor"], "lucene": m["lucene"], "exit_code": code,
                      "command": command, **junit_summary(output)}
            print(label, "exit", code, result["tests_run"], "run", result["failures"], "failures",
                  result["failed_methods"], flush=True)
            results.append(result)
    write_json("query-tests.json", results)
    return results


def fixture_cases(minor):
    root = BASE / MODULE / "src/test/resources/backcompat/releases"
    return sorted(str(p.relative_to(root)) for p in root.rglob("*.zip")
                  if tuple(map(int, p.parent.name.split("."))) <= tuple(map(int, minor.split("."))))


def backcompat_tests():
    results = []
    resources = BASE / MODULE / "src/test/resources"
    for m in modules():
        cp = test_classpath(m)
        for kind, root, methods in (("base", BASE, ["testReadIndices", "testUpgradeIndices"]),
                                    ("port", PATCHED, ["testReadIndices", "testUpgradeIndices", NEW_BACKCOMPAT_TEST])):
            label = f"backcompat-{kind}-{m['minor']}"
            classes, _ = compile_sources(label, [root / BACKCOMPAT_TEST, ROOT / "BackCompatRunner.java"], cp)
            code, output, command = run_java(label, [classes, resources, *cp], ["BackCompatRunner", *methods])
            lines = [line for line in output.splitlines() if line.startswith("CPU_RESULT")]
            print(label, "exit", code, flush=True)
            print("\n".join("  " + line for line in lines), flush=True)
            results.append({"label": label, "minor": m["minor"], "lucene": m["lucene"], "exit_code": code,
                            "methods": methods, "cpu_results": lines, "fixtures": fixture_cases(m["minor"]),
                            "test_source_sha256": sha256(root / BACKCOMPAT_TEST), "command": command})
    write_json("backcompat-tests.json", results)
    return results


def mapping_controls():
    results = []
    methods = ["testReadIndices", "testUpgradeIndices", NEW_BACKCOMPAT_TEST]
    fixture = BASE / MODULE / "src/test/resources/backcompat" / CONTROL_INPUT
    for m in modules():
        cp = test_classpath(m)
        label = f"control-{m['minor']}"
        classes, _ = compile_sources(label, [PATCHED / BACKCOMPAT_TEST, ROOT / "BackCompatRunner.java",
                                             ROOT / "FixtureMappingControl.java"], cp)
        resource = ROOT / "controls" / m["minor"] / "resources"
        work = ROOT / "controls" / m["minor"] / "work"
        work.parent.mkdir(parents=True, exist_ok=True)
        code, output, _ = run_java(label + "-generate", [classes, *cp],
                                   ["FixtureMappingControl", str(fixture), str(resource), str(work)])
        assert code == 0, output
        zips = sorted(resource.rglob("*.zip"))
        assert len(zips) == 1, zips
        code, output, command = run_java(label, [classes, resource, *cp], ["BackCompatRunner", *methods])
        lines = [line for line in output.splitlines() if line.startswith("CPU_RESULT")]
        print(label, "exit", code, flush=True)
        print("\n".join("  " + line for line in lines), flush=True)
        results.append({"label": label, "minor": m["minor"], "lucene": m["lucene"], "exit_code": code,
                        "control_input": CONTROL_INPUT, "control_index": str(zips[0].relative_to(resource)),
                        "control_sha256": sha256(zips[0]), "cpu_results": lines,
                        "vector_mismatch": "vector for id " in output and "arrays first differed" in output,
                        "command": command})
    write_json("mapping-controls.json", results)
    return results


def verify(query, backcompat, controls):
    ok = "run=1 failures=0 ignored=0 assumptions=0"
    for r in query:
        if r["label"].startswith("query-base-"):
            assert r["exit_code"] == 0 and r["tests_run"] == 3 and r["failures"] == 0, r
        elif r["label"].endswith("-mask-dropped"):
            assert r["exit_code"] != 0 and r["tests_run"] == 6 and r["failures"] == 3, r
            assert set(r["failed_methods"]) == NEW_QUERY_TESTS, r
        else:
            assert r["exit_code"] == 0 and r["tests_run"] == 6 and r["failures"] == 0, r
    for r in backcompat:
        assert r["exit_code"] == 0, r
        assert r["cpu_results"] == [f"CPU_RESULT {name} {ok}" for name in r["methods"]], r
        assert len(r["fixtures"]) == 7, r
    for r in controls:
        assert r["exit_code"] != 0 and r["vector_mismatch"], r
        assert r["cpu_results"] == [f"CPU_RESULT testReadIndices {ok}", f"CPU_RESULT testUpgradeIndices {ok}",
                                    f"CPU_RESULT {NEW_BACKCOMPAT_TEST} run=1 failures=1 ignored=0 assumptions=0"], r
    summary = {
        "query_positive_executions": sum(r["tests_run"] for r in query if not r["label"].endswith("-mask-dropped")),
        "query_mask_dropped_expected_failures": sum(r["failures"] for r in query if r["label"].startswith("query-port-")
                                                    and r["label"].endswith("-mask-dropped")),
        "backcompat_positive_method_executions": sum(len(r["methods"]) for r in backcompat),
        "backcompat_fixture_cases": sum(len(r["methods"]) * len(r["fixtures"]) for r in backcompat),
        "data_fault_expected_failures": len(controls),
    }
    write_json("summary.json", summary)
    print("VERIFIED", json.dumps(summary), flush=True)


def main():
    parser = argparse.ArgumentParser()
    parser.add_argument("--preflight-only", action="store_true")
    args = parser.parse_args()
    assert "CUVS_CPU_JDK" in os.environ and Path(os.environ["CUVS_CPU_JDK"]).is_absolute(), \
        "Set CUVS_CPU_JDK to an absolute JDK 21 root."
    RESULTS.mkdir(exist_ok=True)
    runtime = subprocess.run([jdk_tool("java"), "-version"], capture_output=True, text=True, check=True)
    (RESULTS / "runtime.log").write_text(runtime.stdout + runtime.stderr, encoding="utf-8")
    patch = prepare_sources()
    specs = set(TEST_JARS)
    for m in modules():
        for name in ("core", "backward-codecs", "codecs", "test-framework", "analysis-common"):
            specs.add(("org.apache.lucene", "lucene-" + name, m["lucene"]))
    with ThreadPoolExecutor(max_workers=8) as pool:
        write_json("artifact-manifest.json", list(pool.map(artifact, sorted(specs))))
    format_check(patch)
    if args.preflight_only:
        print("PREFLIGHT OK: sources, patch, dependencies and formatting verified; nothing compiled or run.")
        return
    compile_matrix()
    verify(query_tests(), backcompat_tests(), mapping_controls())
    print("PASS: CPU compile, query mask tests with mutations, archived-index tests with data-fault controls. "
          "No GPU proof is claimed.")


if __name__ == "__main__":
    main()
