from pathlib import Path
import difflib
import hashlib
import json

ROOT = Path(__file__).resolve().parent
manifest = json.loads((ROOT / "source-manifest.json").read_text())
patch = ""
for entry in manifest["patchFiles"]:
    relative = entry["path"]
    before = (ROOT / "base" / relative).read_text(encoding="utf-8")
    after = (ROOT / "patched" / relative).read_text(encoding="utf-8")
    patch += f"diff --git a/{relative} b/{relative}\n"
    patch += "".join(difflib.unified_diff(before.splitlines(True), after.splitlines(True),
                                        fromfile="a/" + relative, tofile="b/" + relative))
target = ROOT / "cpu-regression.patch"
target.write_text(patch, encoding="utf-8", newline="\n")
print(json.dumps({"patch": str(target), "sha256": hashlib.sha256(target.read_bytes()).hexdigest()}))
