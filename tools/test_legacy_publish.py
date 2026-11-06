import json
import subprocess
import sys
import unittest

from test_board import BoardTest, SCRIPT


class LegacyPublisherTest(BoardTest):
    def test_batch_and_stale_edit(self):
        # Isolated remote; no network or production board access.
        source = self.root / "source.md"
        source.write_text("first\n", encoding="utf-8")
        manifest = self.root / "batch.json"

        def run_batch(expected):
            manifest.write_text(json.dumps([{"path": "claims/job.md", "file": str(source), "expected_blob": expected},
                                            {"path": "reports/job.md", "file": str(source), "expected_blob": expected}]), encoding="utf-8")
            return subprocess.run([sys.executable, str(SCRIPT.with_name("legacy_publish.py")), "--repo", str(self.repo),
                                   "--remote", "origin", "--branch", "main", "--manifest", str(manifest),
                                   "--message", "Write record"], capture_output=True, text=True)

        created = run_batch(None)
        self.assertEqual(created.returncode, 0, created.stderr)
        head = json.loads(created.stdout)["live_head"]
        blob = self.run_git("-C", str(self.repo), "rev-parse", f"{head}:claims/job.md").stdout.strip()
        source.write_text("second\n", encoding="utf-8")
        rejected = run_batch(None)
        self.assertNotEqual(rejected.returncode, 0)
        unchanged = self.run_git("-C", str(self.repo), "ls-remote", "origin", "refs/heads/main").stdout.split()[0]
        self.assertEqual(unchanged, head)
        updated = run_batch(blob)
        self.assertEqual(updated.returncode, 0, updated.stderr)
        new_head = json.loads(updated.stdout)["live_head"]
        self.assertEqual(self.run_git("-C", str(self.repo), "show", f"{new_head}:claims/job.md").stdout, "second\n")
        self.assertEqual(self.run_git("-C", str(self.repo), "show", f"{new_head}:reports/job.md").stdout, "second\n")
        rejected = run_batch(blob)
        self.assertNotEqual(rejected.returncode, 0)


if __name__ == "__main__":
    unittest.main()
