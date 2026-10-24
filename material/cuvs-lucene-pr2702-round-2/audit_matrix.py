from pathlib import Path
import json
import sys
import os
sys.path.insert(0, str(Path(__file__).resolve().parent))
from linkage_audit import audit

ROOT = Path(__file__).resolve().parent
matrix = json.loads((ROOT / "results/matrix.json").read_text())
summary = []
for module in matrix:
    minor, version = module["label"].removeprefix("module-"), module["lucene"]
    jar = ROOT / f"module-{minor}.jar"
    targets = [ROOT / "jars" / f"lucene-{n}-{version}.jar"
               for n in ("core", "backward-codecs", "codecs")]
    jdk = Path(os.environ.get("CUVS_CPU_JDK", str(ROOT.parent / "toolchain/jdk-22.0.2+9")))
    result = audit(jar, targets, jdk)
    (ROOT / "results" / f"linkage-{minor}.json").write_text(json.dumps(result, indent=2) + "\n")
    small = {k: v for k, v in result.items() if k != "records"}
    small["minor"] = minor
    summary.append(small)
    print(json.dumps(small, indent=2))
(ROOT / "results/linkage-summary.json").write_text(json.dumps(summary, indent=2) + "\n")
assert all(r["failure_records"] == 0 for r in summary), summary
