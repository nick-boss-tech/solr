# Gate: SOLR-10928 / PR 5229 fielded-query regression and matched proof, round 2

Status: DONE
Tags: gate
Intended host: Nick's Linux host or any capable Linux gate host with JDK 21, Python 3, Git and flock

- Ticket branch: `nick-boss-tech/solr:solr-10928-submit`.
- PR: https://github.com/apache/solr/pull/5229 (draft).
- Exact head: `5275653c66edc2610ab90046927d1c30fbe6f0f4`.
- Fixed fail-before base: `b73f03976fa17b857bc0a81bc8af2c6b1c31e0bb`, the PR merge base, not a moving `main` reference.
- Previous proven head: `f2001e640a464e8e861ee3c202821b6dcc4bd497`. The vm6 receipt at `receipts/SOLR-10928.md` proves only that older head. Its vm2 duplicate was stopped. This is a new test input, not a retry of either old gate.
- Implementation claim: `claims/pr-5229-fielded-query-windows-round-1.md`, based on Nick's direct instruction in the PR review chat. The only new source change is 12 test lines; no production change.
- New method: `org.apache.solr.handler.component.QueryElevationComponentTest.testElevateQOverridesFieldedSearchQuery`.

## What must be demonstrated

The new method first verifies `q=text:MMMM` returns IDs 3 and 6 without elevation. It then supplies `elevate.q=YYYY` and expects IDs 1 and 2 elevated ahead of the same two search matches, including the `[elevated]` flags. The second assertion expects `numFound=4`.

At the head, run tidy with a clean-tree guard, Error Prone compilation, `:solr:core:check -x test`, and the three focused classes at seeds `10928ABC0001` and `10928ABC0002`. Expected per seed: `QueryElevationComponentTest` 24 tests, `QueryElevationComponentParamTest` 4, `DistributedQueryElevationComponentTest` 1; zero failures, errors or skips, and the new method present in XML. These are acceptance counts, not results already obtained.

For fail-before, create a separate worktree at the fixed base and transplant **only** the exact head's `QueryElevationComponentTest.java` with `git restore --source=<head> --worktree -- <file>`. Keep the base production files unchanged. Do not copy `QueryElevationComponentParamTest.java`: its new helper and parameter constant are absent from the base, and a compile failure would not prove the endpoint behavior. Run just the new fielded-query method at each matching seed. Acceptance is exactly one test and one assertion failure, zero errors/skips; it must reach the override's `numFound=4` assertion and show the base response still has `numFound="2"`. A compilation failure, missing XML, test-discovery failure, setup failure, or unexpected base pass is not proof.

## Running the prepared recipe

Save the Bash block below as `run-fielded-query-proof.sh`, then run `bash run-fielded-query-proof.sh /absolute/path/to/a/solr/clone` on Linux. It fetches and verifies the pinned fork head, creates fresh detached worktrees and an evidence directory under `/tmp`, uses absolute test work directories, and retains all logs/XML/worktrees. It does not reset the caller's checkout or publish results. Java must resolve to JDK 21. Set `PYTHON` if Python 3 is not named `python3`.

On a pool host, set `SOLR_GATE_LOCK` to the host's existing build-lock path before running, for example `SOLR_GATE_LOCK=/workspace/tools/.gradle-vm6.lock bash run-fielded-query-proof.sh /path/to/clone`. The default lock for a standalone host is `$HOME/.solr-gate.lock`. Do not launch another build alongside it. Run the clean detached tidy preflight under that same lock before claiming this gate, as WORKFLOW.md requires. The recipe repeats tidy as its first standard leg.

The recipe has been syntax-checked with `bash -n` on Windows; no Gradle or Java test ran here. It rejects a moved fork head. If tidy changes source, mark this gate invalid, push the repair as a plain fork commit, and post a superseding job and runner pinned to that new head before treating proof as complete.

## Deliverables and completion

- Claim this gate before Linux execution and record the host, head, fixed base, seeds, build lock, runner/log paths and start time. The Windows claim is implementation only.
- Add `receipts/SOLR-10928-pr5229-fielded-query-round-2.md` as a new phase receipt owned by the executing host. Preserve vm6's old receipt. Include head/base/tree/test-blob identities, standard-leg exits and tidy status, fresh XML counts and assertion signatures for both matched pairs, environment, log paths, and any failure diagnosis.
- Do not mark the PR validated until all named legs meet the criteria at the pinned head. Full `:solr:core:test` is outside this gate.
- Finish with a public-body proof draft tied to the actual results. Do not post a PR comment, change draft status, or claim that the historical receipt proves this head.

## Runnable Linux recipe

```bash
#!/usr/bin/env bash
# Run on Linux with JDK 21, Python 3, Git and flock. No source checkout is reset.
set -euo pipefail

[[ $(uname -s) == Linux ]] || { echo 'This proof must run on Linux.' >&2; exit 1; }
head=5275653c66edc2610ab90046927d1c30fbe6f0f4
base=b73f03976fa17b857bc0a81bc8af2c6b1c31e0bb
test_file=solr/core/src/test/org/apache/solr/handler/component/QueryElevationComponentTest.java
test_class=org.apache.solr.handler.component.QueryElevationComponentTest
method=testElevateQOverridesFieldedSearchQuery
repo=$(git -C "${1:-.}" rev-parse --show-toplevel)
python=$(command -v "${PYTHON:-python3}")
command -v flock >/dev/null
unset NODE_ENV

# Pool hosts must set SOLR_GATE_LOCK to their existing host-wide build lock.
exec 9>"${SOLR_GATE_LOCK:-$HOME/.solr-gate.lock}"
flock -n 9 || { echo 'Another build holds the gate lock.' >&2; exit 1; }
git -C "$repo" fetch https://github.com/nick-boss-tech/solr.git solr-10928-submit
[[ $(git -C "$repo" rev-parse FETCH_HEAD) == "$head" ]] || {
  echo 'Fork head moved; supersede the gate for the new head before running.' >&2; exit 1;
}
[[ $(git -C "$repo" merge-base "$head" "$base") == "$base" ]]

run_root=$(mktemp -d /tmp/solr-10928-fielded-proof.XXXXXX)
echo "Proof logs and retained worktrees: $run_root"
head_dir=$run_root/head
base_dir=$run_root/base
mkdir -p "$run_root/work-head" "$run_root/work-base"
git -C "$repo" worktree add --detach "$head_dir" "$head"
git -C "$repo" worktree add --detach "$base_dir" "$base"
git -C "$base_dir" restore --source="$head" --worktree -- "$test_file"
[[ $(git -C "$base_dir" diff --name-only) == "$test_file" ]]
git -C "$base_dir" diff --cached --quiet
test_blob=$(git -C "$repo" rev-parse "$head:$test_file")
[[ $(git -C "$base_dir" hash-object "$test_file") == "$test_blob" ]]
{
  printf 'head=%s\nbase=%s\ntest_blob=%s\n' "$head" "$base" "$test_blob"
  git -C "$repo" show -s --format='head_tree=%T' "$head"
  git -C "$repo" show -s --format='base_tree=%T' "$base"
  java -version 2>&1
} > "$run_root/manifest.txt"

run_step() {
  local name=$1 directory=$2
  shift 2
  echo "Running $name; log: $run_root/$name.log"
  if (cd "$directory"; "$@") > "$run_root/$name.log" 2>&1; then
    printf '%s\t0\n' "$name" >> "$run_root/steps.tsv"
  else
    local rc=$?
    printf '%s\t%s\n' "$name" "$rc" >> "$run_root/steps.tsv"
    echo "Step $name returned $rc; inspect its log." >&2
    return "$rc"
  fi
}

cat > "$run_root/check-xml.py" <<'PY'
import pathlib
import sys
import xml.etree.ElementTree as ET

mode, directory = sys.argv[1:]
method = "testElevateQOverridesFieldedSearchQuery"
prefix = "org.apache.solr.handler.component."
expected = {"QueryElevationComponentTest": 24,
            "QueryElevationComponentParamTest": 4,
            "DistributedQueryElevationComponentTest": 1}
if mode == "base":
    expected = {"QueryElevationComponentTest": 1}
for name, count in expected.items():
    root = ET.parse(pathlib.Path(directory) / f"TEST-{prefix}{name}.xml").getroot()
    failures = 1 if mode == "base" else 0
    actual = tuple(int(root.get(key, "0")) for key in ("tests", "failures", "errors", "skipped"))
    assert actual == (count, failures, 0, 0), (mode, name, actual)
    if name == "QueryElevationComponentTest":
        cases = [case for case in root.iter("testcase")
                 if case.get("name", "").split(" {")[0] == method]
        assert len(cases) == 1, (mode, "new test missing or duplicated")
        if mode == "base":
            failure = cases[0].find("failure")
            assert failure is not None, "Expected an assertion failure, not a harness error"
            detail = failure.get("message", "") + "".join(failure.itertext())
            assert "[@numFound='4']" in detail, "Failure did not reach the override assertion"
            assert 'numFound="2"' in detail, "Failure did not show the two original search matches"
print(f"{mode}: fresh XML meets the acceptance criteria")
PY

common=(--no-daemon --max-workers=1 "-Ppython3.exe=$python")
run_step tidy "$head_dir" ./gradlew tidy "${common[@]}"
[[ -z $(git -C "$head_dir" status --porcelain) ]] || {
  echo 'GATE INVALID: tidy changed the tree. Commit repair and supersede the head-specific gate.' >&2
  exit 1
}
run_step errorprone "$head_dir" ./gradlew :solr:core:compileJava :solr:core:compileTestJava \
  -Pvalidation.errorprone=true "${common[@]}"
run_step check "$head_dir" ./gradlew :solr:core:check -x test "${common[@]}"

for seed in 10928ABC0001 10928ABC0002; do
  run_step "head-$seed-clean" "$head_dir" ./gradlew :solr:core:cleanTest "${common[@]}"
  run_step "head-$seed" "$head_dir" ./gradlew :solr:core:test \
    --tests "$test_class" \
    --tests org.apache.solr.handler.component.QueryElevationComponentParamTest \
    --tests org.apache.solr.handler.component.DistributedQueryElevationComponentTest \
    "-Ptests.seed=$seed" "-Ptests.workDir=$run_root/work-head" "${common[@]}"
  cp -a "$head_dir/solr/core/build/test-results/test" "$run_root/head-$seed-xml"
  "$python" "$run_root/check-xml.py" head "$run_root/head-$seed-xml"

  run_step "base-$seed-clean" "$base_dir" ./gradlew :solr:core:cleanTest "${common[@]}"
  if run_step "base-$seed" "$base_dir" ./gradlew :solr:core:test \
      --tests "$test_class.$method" "-Ptests.seed=$seed" \
      "-Ptests.workDir=$run_root/work-base" "${common[@]}"; then
    echo 'INVALID PROOF: the base unexpectedly passed.' >&2
    exit 1
  fi
  cp -a "$base_dir/solr/core/build/test-results/test" "$run_root/base-$seed-xml"
  "$python" "$run_root/check-xml.py" base "$run_root/base-$seed-xml"
done
echo "Matched proof complete at $head; evidence: $run_root"
echo 'Worktrees are retained for inspection; this script posts no receipt or PR update.'
```
