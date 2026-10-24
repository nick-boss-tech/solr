from pathlib import Path
import json
from run_cpu_tests import ROOT, junit_summary, verify_results

path = ROOT / "results/cpu-tests.json"
results = json.loads(path.read_text())
for result in results:
    log = (ROOT / "results" / (result["label"] + ".log")).read_text(encoding="utf-8")
    result.update(junit_summary(log))
verify_results(results)
path.write_text(json.dumps(results, indent=2) + "\n")
print(json.dumps([{k: v for k, v in r.items() if k != "command"} for r in results], indent=2))
