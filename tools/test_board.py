import hashlib
import json
import os
from pathlib import Path
import subprocess
import sys
import tempfile
import unittest

if os.name == "nt":
    git_cmd = Path("C:/Users/shaninna/AppData/Local/Programs/Git/cmd")
    if git_cmd.exists():
        os.environ["PATH"] = str(git_cmd) + os.pathsep + os.environ.get("PATH", "")

SCRIPT = Path(__file__).with_name("board.py")
HEAD = "a" * 40


class BoardTest(unittest.TestCase):
    def setUp(self):
        self.temp = tempfile.TemporaryDirectory()
        self.addCleanup(self.temp.cleanup)
        self.root = Path(self.temp.name)
        self.remote = self.root / "remote.git"
        self.repo = self.root / "work"
        self.run_git("init", "--bare", str(self.remote))
        self.run_git("init", "-b", "main", str(self.repo))
        self.run_git("-C", str(self.repo), "config", "user.name", "Test User")
        self.run_git("-C", str(self.repo), "config", "user.email", "test@example.invalid")
        self.run_git("-C", str(self.repo), "commit", "--allow-empty", "-m", "Initial")
        self.run_git("-C", str(self.repo), "remote", "add", "origin", str(self.remote))
        self.run_git("-C", str(self.repo), "push", "origin", "main")

    def run_git(self, *args):
        return subprocess.run(["git", *args], check=True, capture_output=True, text=True)

    def command(self, *args):
        return [sys.executable, str(SCRIPT), "--repo", str(self.repo), *args]

    def record_file(self, name, obj):
        path = self.root / name
        path.write_text(json.dumps(obj), encoding="utf-8")
        return str(path)

    def publish(self, obj, name="record.json"):
        return subprocess.run(self.command("publish", "--remote", "origin", "--branch", "main", "--file", self.record_file(name, obj)), capture_output=True, text=True)

    def test_competing_claims_and_pinned_index(self):
        self.assertEqual(self.publish({"type": "assignment", "id": "job-1", "job": "job-1"}).returncode, 0)
        files = [self.record_file(f"claim{i}.json", {"type": "claim", "id": "job-1", "job": "job-1", "owner": str(i)}) for i in range(2)]
        commands = [self.command("publish", "--remote", "origin", "--branch", "main", "--file", f) for f in files]
        workers = [subprocess.Popen(c, stdout=subprocess.PIPE, stderr=subprocess.PIPE, text=True) for c in commands]
        try:
            results = [p.communicate(timeout=180) for p in workers]
        finally:
            for worker in workers:
                if worker.poll() is None:
                    worker.kill()
                    worker.communicate()
        self.assertEqual(sorted(p.returncode for p in workers), [0, 1], results)
        head = self.run_git("-C", str(self.repo), "ls-remote", "origin", "refs/heads/main").stdout.split()[0]
        self.run_git("-C", str(self.repo), "fetch", "origin", "main")
        indexed = subprocess.run(self.command("index", "--snapshot", head), check=True, capture_output=True, text=True)
        data = json.loads(indexed.stdout)
        self.assertEqual(data["snapshot"], head)
        self.assertEqual(data["jobs"]["job-1"]["status"], "claim recorded: verify owner activity")
        self.assertEqual(len([r for r in data["jobs"]["job-1"]["records"] if r["type"] == "claim"]), 1)
        self.assertNotEqual(self.publish({"type": "claim", "id": "other", "job": "job-1"}).returncode, 0)

    def test_receipt_does_not_assert_green_and_evidence_hash(self):
        self.assertEqual(self.publish({"type": "assignment", "id": "j", "job": "j"}).returncode, 0)
        self.assertEqual(self.publish({"type": "gate", "id": "g", "job": "j", "head": HEAD}).returncode, 0)
        self.assertEqual(self.publish({"type": "receipt", "id": "r", "job": "j", "gate": "g", "head": "b" * 40, "verdict": "passed"}).returncode, 0)
        self.run_git("-C", str(self.repo), "fetch", "origin", "main")
        data = json.loads(subprocess.run(self.command("index", "--snapshot", "FETCH_HEAD"), check=True, capture_output=True, text=True).stdout)
        self.assertFalse(data["jobs"]["j"]["proof"][0]["gate_matches"])
        self.assertIn("inspect", data["jobs"]["j"]["status"])
        evidence = self.root / "log.txt"
        evidence.write_bytes(b"test evidence\n")
        result = subprocess.run(self.command("evidence", "--file", str(evidence), "--id", "log", "--job", "j", "--head", HEAD, "--location", "https://example.invalid/log"), check=True, capture_output=True, text=True)
        self.assertEqual(json.loads(result.stdout)["sha256"], hashlib.sha256(evidence.read_bytes()).hexdigest())


if __name__ == "__main__":
    unittest.main()
