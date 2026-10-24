"""CPU-only proof entrypoint; CUVS_CPU_JDK names an existing JDK 21+ installation."""
from pathlib import Path
import subprocess
import sys

ROOT = Path(__file__).resolve().parent
for script, args in [("prepare_sources.py", []), ("format_check.py", []), ("compile_matrix.py", []),
                     ("audit_matrix.py", []), ("run_cpu_tests.py", ["--mutations"])]:
    subprocess.run([sys.executable, str(ROOT / script), *args], check=True)
print("CPU compile, linkage, JUnit and mutation proofs completed. No GPU proof is claimed.")
