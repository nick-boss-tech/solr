import json
import subprocess
import unittest

from test_board import BoardTest


class ReleaseTest(BoardTest):
    def test_release_and_successor_require_prior_claim(self):
        assignment = {"type": "assignment", "id": "job-a", "job": "job-a"}
        successor = {"type": "assignment", "id": "job-b", "job": "job-b", "successor_of": "job-a"}
        release = {"type": "release", "id": "job-a", "job": "job-a", "actor": "worker-a",
                   "reason": "handoff", "evidence": "worker stopped; see report"}
        self.assertEqual(self.publish(assignment).returncode, 0)
        self.assertNotEqual(self.publish(release).returncode, 0)
        self.assertNotEqual(self.publish(successor).returncode, 0)
        self.assertEqual(self.publish({"type": "claim", "id": "job-a", "job": "job-a", "owner": "worker-a"}).returncode, 0)
        self.assertEqual(self.publish(release).returncode, 0)
        self.assertNotEqual(self.publish({"type": "report", "id": "late", "job": "job-a"}).returncode, 0)
        self.assertEqual(self.publish(successor).returncode, 0)
        self.run_git("-C", str(self.repo), "fetch", "origin", "main")
        result = subprocess.run(self.command("index", "--snapshot", "FETCH_HEAD"), check=True, capture_output=True, text=True)
        jobs = json.loads(result.stdout)["jobs"]
        self.assertEqual(jobs["job-a"]["status"], "release recorded: verify evidence and any successor")
        self.assertEqual(jobs["job-b"]["status"], "open: no canonical claim recorded")


if __name__ == "__main__":
    unittest.main()
