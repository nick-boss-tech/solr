from pathlib import Path
import json
import subprocess
import sys
import hashlib
import urllib.request
from compile_matrix import ROOT, java_tool

files = json.loads((ROOT / "format-inputs.json").read_text())
formatter = ROOT / "jars/google-java-format-1.27.0-all-deps.jar"
if not formatter.exists():
    formatter.parent.mkdir(exist_ok=True)
    url = ("https://repo.maven.apache.org/maven2/com/google/googlejavaformat/"
           "google-java-format/1.27.0/google-java-format-1.27.0-all-deps.jar")
    with urllib.request.urlopen(url) as response:
        formatter.write_bytes(response.read())
assert hashlib.sha256(formatter.read_bytes()).hexdigest() == (
    "ed07239f3cb72e25bf2a0eae63e76831f9f11963bd19fc36a6f1d87016ac1763")
command = [java_tool("java"), "-jar", str(formatter)]
command += ["--replace"] if "--apply" in sys.argv else ["--dry-run", "--set-exit-if-changed"]
command += [str(ROOT / "patched" / f) for f in files]
p = subprocess.run(command, text=True, capture_output=True)
(ROOT / "results/format.log").write_text(p.stdout + p.stderr, encoding="utf-8")
(ROOT / "results/format.json").write_text(json.dumps({
    "command": command, "exit_code": p.returncode, "files": files,
    "formatter_sha256": hashlib.sha256(formatter.read_bytes()).hexdigest()
}, indent=2) + "\n")
print(p.stdout + p.stderr)
assert p.returncode == 0
