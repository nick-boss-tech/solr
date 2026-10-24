"""Fetch and verify the exact PR source snapshot and apply the one-file test patch."""
from pathlib import Path
from concurrent.futures import ThreadPoolExecutor
import hashlib
import json
import os
import shutil
import subprocess
import urllib.request

ROOT = Path(__file__).resolve().parent


def blob(data):
    return hashlib.sha1(b"blob " + str(len(data)).encode() + b"\0" + data).hexdigest()


def main():
    manifest = json.loads((ROOT / "source-manifest.json").read_text())

    def source(entry):
        target = ROOT / "base" / entry["path"]
        if not target.exists():
            url = f"https://raw.githubusercontent.com/{manifest['repo']}/{manifest['head']}/{entry['path']}"
            with urllib.request.urlopen(url) as response:
                data = response.read()
            assert blob(data) == entry["sha"], entry["path"]
            target.parent.mkdir(parents=True, exist_ok=True)
            target.write_bytes(data)
        assert blob(target.read_bytes()) == entry["sha"], entry["path"]

    with ThreadPoolExecutor(max_workers=8) as pool:
        list(pool.map(source, manifest["files"]))
    patches = manifest["patchFiles"]
    if all((ROOT / "patched" / e["path"]).exists() and
           blob((ROOT / "patched" / e["path"]).read_bytes()) == e["sha"] for e in patches):
        print("Verified existing patched source and all pinned source blobs.")
        return
    for entry in patches:
        relative = entry["path"]
        patched = ROOT / "patched" / relative
        assert not patched.exists(), "Unexpected existing patch target; use a fresh proof directory."
        patched.parent.mkdir(parents=True, exist_ok=True)
        shutil.copyfile(ROOT / "base" / relative, patched)
    git = os.environ.get("CUVS_CPU_GIT")
    if not git:
        git = (r"C:\Users\shaninna\AppData\Local\Programs\Git\cmd\git.exe"
               if os.name == "nt" else "git")
    patch = ROOT / "cpu-regression.patch"
    subprocess.run([git, "-c", "core.autocrlf=false", "-C", str(ROOT / "patched"), "apply", "--check",
                    "--whitespace=error-all", str(patch)], check=True)
    subprocess.run([git, "-c", "core.autocrlf=false", "-C", str(ROOT / "patched"), "apply", str(patch)], check=True)
    for entry in patches:
        assert blob((ROOT / "patched" / entry["path"]).read_bytes()) == entry["sha"]
    print("Verified pinned source blobs and applied the exact test patch.")


if __name__ == "__main__":
    main()
