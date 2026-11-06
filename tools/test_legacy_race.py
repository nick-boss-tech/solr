import json
import subprocess
import sys
import unittest

from test_board import BoardTest, SCRIPT


class LegacyRaceTest(BoardTest):
    def test_same_claim_path_one_winner(self):
        commands = []
        for n in (1, 2):
            payload = self.root / f"claim{n}.md"
            payload.write_text(f"worker {n}\n", encoding="utf-8")
            manifest = self.root / f"batch{n}.json"
            manifest.write_text(json.dumps([{"path": "claims/job.md", "file": str(payload), "expected_blob": None}]), encoding="utf-8")
            commands.append([sys.executable, str(SCRIPT.with_name("legacy_publish.py")), "--repo", str(self.repo),
                             "--remote", "origin", "--branch", "main", "--manifest", str(manifest), "--message", "Claim job"])
        workers = [subprocess.Popen(c, stdout=subprocess.PIPE, stderr=subprocess.PIPE, text=True) for c in commands]
        try:
            outputs = [p.communicate(timeout=180) for p in workers]
        finally:
            for p in workers:
                if p.poll() is None:
                    p.kill()
                    p.communicate()
        self.assertEqual(sorted(p.returncode for p in workers), [0, 1], outputs)
        winner = next(i for i, p in enumerate(workers) if p.returncode == 0)
        head = json.loads(outputs[winner][0])["live_head"]
        self.assertEqual(self.run_git("-C", str(self.repo), "show", f"{head}:claims/job.md").stdout,
                         f"worker {winner + 1}\n")


if __name__ == "__main__":
    unittest.main()
