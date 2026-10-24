from pathlib import Path
import difflib
import hashlib
import json
import os

ROOT = Path(__file__).resolve().parent
R2 = Path(os.environ.get("CUVS_BACKCOMPAT_R2", str(ROOT.parent / "round2")))
manifest = json.loads((ROOT / "patch-manifest.json").read_text())
relative = manifest["path"]
before = (R2 / "base" / relative).read_text(encoding="utf-8")
after = (ROOT / "final" / relative).read_text(encoding="utf-8")
patch = f"diff --git a/{relative} b/{relative}\n"
patch += "".join(difflib.unified_diff(before.splitlines(True), after.splitlines(True),
                                    fromfile="a/" + relative, tofile="b/" + relative))
target = ROOT / "backcompat.patch"
target.write_text(patch, encoding="utf-8", newline="\n")
manifest["patchSha256"] = hashlib.sha256(target.read_bytes()).hexdigest()
(ROOT / "patch-manifest.json").write_text(json.dumps(manifest, indent=2) + "\n", newline="\n")
print(manifest["patchSha256"])
