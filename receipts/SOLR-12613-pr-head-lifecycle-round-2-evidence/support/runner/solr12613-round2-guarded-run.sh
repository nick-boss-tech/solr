#!/usr/bin/env bash
set -euo pipefail
if [ "$#" -lt 6 ]; then
  echo "usage: $0 <checkout> <log-root> <run-name> <expected-head> <clean|appjs-overlay> <gradle args...>" >&2
  exit 64
fi
W=$1; LOGROOT=$2; NAME=$3; EXPECTED=$4; MODE=$5; shift 5
D=$LOGROOT/$NAME
mkdir -p "$D"
actual=$(git -C "$W" rev-parse HEAD)
[ "$actual" = "$EXPECTED" ] || { echo "checkout head mismatch: expected $EXPECTED got $actual" >&2; exit 12; }
status=$(git -C "$W" status --porcelain=v1)
case "$MODE" in
  clean)
    [ -z "$status" ] || { echo "checkout not clean before run: $status" >&2; exit 13; }
    ;;
  appjs-overlay)
    expected_status=' M solr/webapp/web/js/angular/app.js'
    [ "$status" = "$expected_status" ] || { echo "overlay status mismatch: $status" >&2; exit 14; }
    test_blob=$(git -C "$W" hash-object solr/webapp/src/test/org/apache/solr/webapp/AdminUiNodePickerTest.java)
    head_test_blob=$(git -C "$W" rev-parse HEAD:solr/webapp/src/test/org/apache/solr/webapp/AdminUiNodePickerTest.java)
    [ "$test_blob" = "$head_test_blob" ] || { echo "candidate test file differs from F2: $test_blob != $head_test_blob" >&2; exit 15; }
    ;;
  *) echo "unsupported mode $MODE" >&2; exit 64 ;;
esac
python3 - "$D/command.txt" "$W" "$D" "$NAME" "$@" <<'PY'
import shlex, sys
path, worktree, logdir, name, *args = sys.argv[1:]
command = ["env", "-u", "NODE_ENV", "bash", "/workspace/solr/pool/bin/sweep-run.sh", worktree, logdir, name, *args]
with open(path, "w", encoding="utf-8") as out:
    out.write("# Continuous guard: /workspace/tools/inode-watch-run.py; serialized runner: pool/bin/sweep-run.sh\n")
    out.write("# NODE_ENV is unset.\n")
    out.write(" ".join(shlex.quote(item) for item in command) + "\n")
PY
{
  printf 'started_utc=%s\nhead=%s\nmode=%s\ncheckout=%s\n' "$(date -u +%FT%TZ)" "$actual" "$MODE" "$W"
  printf 'status_before=%s\n' "${status:-<clean>}"
} > "$D/source-check.txt"
set +e
python3 /workspace/tools/inode-watch-run.py "$D/$NAME.monitor.tsv" env -u NODE_ENV bash /workspace/solr/pool/bin/sweep-run.sh "$W" "$D" "$NAME" "$@"
rc=$?
set -e
printf 'ended_utc=%s\nrunner_rc=%s\n' "$(date -u +%FT%TZ)" "$rc" > "$D/times.txt"
printf 'runner_rc=%s\n' "$rc" > "$D/runner-status.txt"
git -C "$W" status --porcelain=v1 > "$D/git-status-after.txt"
python3 - "$D" "$NAME" <<'PY'
import pathlib, sys, xml.etree.ElementTree as ET
logdir = pathlib.Path(sys.argv[1]); name = sys.argv[2]
xml_dir = logdir / f"{name}.xml"
files = sorted(xml_dir.glob("*.xml")) if xml_dir.exists() else []
total = failures = errors = skipped = cases = 0
rows = []
for path in files:
    root = ET.parse(path).getroot()
    suites = [root] if root.tag == "testsuite" else root.findall(".//testsuite")
    for suite in suites:
        total += int(suite.attrib.get("tests", 0))
        failures += int(suite.attrib.get("failures", 0))
        errors += int(suite.attrib.get("errors", 0))
        skipped += int(suite.attrib.get("skipped", 0))
    for case in root.findall(".//testcase"):
        cases += 1
        rows.append(f"{case.attrib.get('classname','')}#{case.attrib.get('name','')} failure={len(case.findall('./failure'))} error={len(case.findall('./error'))} skipped={len(case.findall('./skipped'))}")
summary = [f"xml_files={len(files)}", f"testcases={cases}", f"tests={total}", f"failures={failures}", f"errors={errors}", f"skipped={skipped}", *rows]
(logdir / "junit-summary.txt").write_text("\n".join(summary) + "\n", encoding="utf-8")
print("\n".join(summary))
PY
members=(command.txt source-check.txt times.txt runner-status.txt git-status-after.txt junit-summary.txt "$NAME.monitor.tsv")
for item in "$NAME.log" "$NAME.headroom.log" "$NAME.start" "$NAME.xml"; do
  [ ! -e "$D/$item" ] || members+=("$item")
done
tar -czf "$D/$NAME-evidence.tar.gz" -C "$D" "${members[@]}"
sha256sum "$D/$NAME-evidence.tar.gz" > "$D/$NAME-evidence.sha256"
printf 'evidence_archive=%s\n' "$D/$NAME-evidence.tar.gz"
exit "$rc"
