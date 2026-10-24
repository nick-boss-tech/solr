"""Run the CPU regression and fail-before mask mutations on pinned source."""
from pathlib import Path
import argparse
import json
import os
import re
import subprocess
from compile_matrix import ROOT, MODULE, java_tool, compile_sources

TEST_REL = Path("java/cuvs-lucene/src/test/java/com/nvidia/cuvs/lucene/TestCompatKnnFloatVectorQuery.java")
TEST_CLASS = "com.nvidia.cuvs.lucene.TestCompatKnnFloatVectorQuery"
SEED = "18556C0D"


def junit_summary(output):
    ok = re.search(r"OK \((\d+) tests?\)", output)
    failed = re.search(r"Tests run: (\d+),\s+Failures: (\d+)", output)
    return {"tests_run": int(ok[1]) if ok else int(failed[1]) if failed else None,
            "failures": 0 if ok else int(failed[2]) if failed else None,
            "failed_methods": re.findall(r"^\d+\) (test\w+)\(", output, re.M)}


def verify_results(results):
    expected_failed = {"testFilterIsForwarded", "testDeletedDocumentsAreExcluded",
                       "testFilterAndDeletesAreIntersected"}
    for r in results:
        if r["label"].startswith("test-original-"):
            assert r["exit_code"] == 0 and r["tests_run"] == 1 and r["failures"] == 0, r
        elif r["label"].endswith("-mutant"):
            assert r["exit_code"] != 0 and r["tests_run"] == 4 and r["failures"] == 3, r
            assert set(r["failed_methods"]) == expected_failed, r
        else:
            assert r["exit_code"] == 0 and r["tests_run"] == 4 and r["failures"] == 0, r


def classpath(module):
    version, minor = module["lucene"], module["label"].removeprefix("module-")
    cp = [ROOT / f"module-{minor}.jar", ROOT / "cuvs-api.jar"]
    cp += [ROOT / "jars" / f"lucene-{n}-{version}.jar"
           for n in ("core", "backward-codecs", "codecs", "test-framework", "analysis-common")]
    cp += [ROOT / "jars" / n for n in
           ("junit-4.13.2.jar", "hamcrest-2.2.jar", "randomizedtesting-runner-2.8.3.jar")]
    return cp


def execute(label, cp):
    command = [java_tool("java"), "-ea", "-Dtests.seed=" + SEED,
               "-Djava.awt.headless=true", "-cp", os.pathsep.join(map(str, cp)),
               "org.junit.runner.JUnitCore", TEST_CLASS]
    p = subprocess.run(command, text=True, capture_output=True)
    output = p.stdout + p.stderr
    (ROOT / "results" / (label + ".log")).write_text(output, encoding="utf-8")
    result = {"label": label, "exit_code": p.returncode, "command": command}
    result.update(junit_summary(output))
    print(label, "exit", p.returncode, output[-7000:], flush=True)
    return result


def main():
    parser = argparse.ArgumentParser()
    parser.add_argument("--mutations", action="store_true")
    args = parser.parse_args()
    modules = json.loads((ROOT / "results/matrix.json").read_text())
    results = []
    for module in modules:
        minor = module["label"].removeprefix("module-")
        cp = classpath(module)
        label = "test-patched-" + minor
        variant = module["properties"]["cuvs.lucene.compat.leafReader"]
        helper = (ROOT / "patched/java/cuvs-lucene/compat" / variant /
                  "src/test/java/com/nvidia/cuvs/lucene/TestLuceneCompat.java")
        compiled = compile_sources(label, [ROOT / "patched" / TEST_REL, helper], cp)
        assert compiled["exit_code"] == 0
        patched_cp = [ROOT / "classes" / label, *cp]
        normal = execute(label, patched_cp)
        results.append(normal)
        if normal["exit_code"] != 0:
            continue
        if args.mutations and minor in ("10.2", "10.4"):
            variant = module["properties"]["cuvs.lucene.compat.knnFloatVectorQuery"]
            relative = Path("compat") / variant / "src/main/java/com/nvidia/cuvs/lucene/CompatKnnFloatVectorQuery.java"
            source = (MODULE / relative).read_text(encoding="utf-8")
            call = "context.reader().searchNearestVectors(field, getTargetCopy(), results, acceptDocs);"
            assert source.count(call) == 1
            mask = ("null" if minor == "10.2" else
                    "org.apache.lucene.search.AcceptDocs.fromLiveDocs(null, context.reader().maxDoc())")
            mutated = source.replace(call, call.replace("acceptDocs", mask))
            mutant_src = ROOT / "mutations" / minor / relative
            mutant_src.parent.mkdir(parents=True, exist_ok=True)
            mutant_src.write_text(mutated, encoding="utf-8")
            mutant_label = "mask-dropped-" + minor
            compiled_mutant = compile_sources(mutant_label, [mutant_src], cp)
            assert compiled_mutant["exit_code"] == 0
            mutant_cp = [ROOT / "classes" / mutant_label, *cp]
            original_label = "test-original-" + minor
            compiled_original = compile_sources(original_label, [ROOT / "base" / TEST_REL], cp)
            assert compiled_original["exit_code"] == 0
            old_test = execute(original_label + "-mutant", [ROOT / "classes" / original_label, *mutant_cp])
            new_test = execute(label + "-mutant", [ROOT / "classes" / label, *mutant_cp])
            results += [old_test, new_test]
    (ROOT / "results/cpu-tests.json").write_text(json.dumps(results, indent=2) + "\n")
    verify_results(results)
    assert all(r["exit_code"] == 0 for r in results if not r["label"].endswith("-mutant"))
    if args.mutations:
        assert all(r["exit_code"] == 0 for r in results if r["label"].startswith("test-original-"))
        assert all(r["exit_code"] != 0 for r in results
                   if r["label"].startswith("test-patched-") and r["label"].endswith("-mutant"))


if __name__ == "__main__":
    main()
