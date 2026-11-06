import json
import os
from pathlib import Path
import shutil
import subprocess
import sys
import unittest

from test_board import BoardTest, SCRIPT


@unittest.skipUnless(os.name == "nt" and shutil.which("pwsh"), "Windows PowerShell 7 required")
class WindowsWrapperTest(BoardTest):
    def test_local_publish_without_credentials(self):
        record = self.record_file("win-assignment.json", {"type": "assignment", "id": "windows-job", "job": "windows-job"})
        cmd = ["pwsh", "-NoProfile", "-File", str(SCRIPT.with_name("board-windows.ps1")),
               "-Repo", str(self.repo), "-Python", sys.executable,
               "-GitBinDirectory", str(Path(shutil.which("git")).parent),
               "-AuthorName", "Test User", "-AuthorEmail", "test@example.invalid",
               "publish", "--remote", "origin", "--branch", "main", "--file", record]
        result = subprocess.run(cmd, capture_output=True, text=True, timeout=180)
        self.assertEqual(result.returncode, 0, result.stderr)
        head = json.loads(result.stdout)["live_head"]
        text = self.run_git("-C", str(self.repo), "show", f"{head}:records/assignment/windows-job.json").stdout
        self.assertEqual(json.loads(text)["job"], "windows-job")


if __name__ == "__main__":
    unittest.main()
