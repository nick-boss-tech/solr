"""Reproduce Java-only evidence on Windows using Python and portable JDK 22."""
from pathlib import Path
import subprocess
import sys

ROOT = Path(__file__).resolve().parent


def run(script, *args):
    subprocess.run([sys.executable, str(ROOT / script), *map(str, args)], check=True)


run("prepare.py")
run("compile_proof.py")  # Records the expected full-module 10.4 failure without hiding it.
run("semantics_proof.py")
run("audit_selfcheck.py")
for jar, label, lucene in [
    ("jars/cuvs-lucene-26.10.0.jar", "original-10.2.0", "10.2.0"),
    ("jars/cuvs-lucene-26.10.0.jar", "original-10.4.0", "10.4.0"),
    ("cuvs-lucene-26.10.0-lucene104-shim.jar", "shim-10.4.0", "10.4.0"),
    ("jars/cuvs-lucene-25.12.0.jar", "original2512-10.4.0", "10.4.0"),
]:
    run("linkage_audit.py", ROOT / jar, ROOT / "results" / ("linkage-" + label + ".json"),
        "--lucene", lucene)
run("package_checks.py")
print("Evidence produced. Read failures in results; this is not a Lucene 10.4 compatibility pass.")
