"""Check the saved method results and exact final-source identity."""
from pathlib import Path
import json
import re

ROOT = Path(__file__).resolve().parent


def main():
    patch = json.loads((ROOT / "patch-manifest.json").read_text())
    expected = {"10.2", "10.3", "10.4", "10.5"}
    old_methods = {"testReadIndices", "testUpgradeIndices"}
    new_method = "testUpgradeIndicesWithDeletesAndSparseVectors"
    for name, methods in (("baseline", old_methods), ("final", {new_method})):
        results = json.loads((ROOT / "results" / (name + ".json")).read_text())
        assert len(results) == 4 and {r["minor"] for r in results} == expected
        for result in results:
            assert result["exit_code"] == 0
            if name == "final":
                assert result["test_source_sha256"] == patch["testSourceSha256"]
            log = ROOT / "results" / (name + "-" + result["minor"] + "-tests.log")
            if not log.exists() and name == "baseline":
                log = ROOT / "results" / (result["minor"] + "-tests.log")
            rows = re.findall(r"CPU_RESULT (\w+) run=(\d+) failures=(\d+) ignored=(\d+) assumptions=(\d+)",
                              log.read_text(encoding="utf-8"))
            assert len(rows) == len(methods) and {r[0] for r in rows} == methods
            assert all(r[1:] == ("1", "0", "0", "0") for r in rows)
    controls = json.loads((ROOT / "results/mapping-control.json").read_text())
    assert len(controls) == 2 and {r["minor"] for r in controls} == {"10.2", "10.4"}
    for result in controls:
        assert result["test_source_sha256"] == patch["testSourceSha256"]
        assert result["exit_code"] != 0 and result["expected_failure"] == new_method
        output = (ROOT / "results" / ("control-" + result["minor"] + "-tests.log")).read_text(encoding="utf-8")
        rows = re.findall(r"CPU_RESULT (\w+) run=(\d+) failures=(\d+) ignored=(\d+) assumptions=(\d+)", output)
        assert len(rows) == 3 and {r[0] for r in rows} == old_methods | {new_method}
        assert all(r[1:] == ("1", "1" if r[0] == new_method else "0", "0", "0") for r in rows)
        assert "vector for id " in output and "arrays first differed" in output
    fixtures = json.loads((ROOT / "fixture-manifest.json").read_text())
    assert len(fixtures) == 16
    counts = [sum(tuple(map(int, Path(e["path"]).name.split("-")[0].split("."))) <= (10, minor)
                  for e in fixtures) for minor in (2, 3, 4, 5)]
    assert counts == [7, 10, 13, 16], counts
    print("Verified: 12 positive methods, 138 fixture cases, four passing control methods and two expected data-fault failures.")


if __name__ == "__main__":
    main()
