#!/usr/bin/env python3
"""Run with python3 -B pool/bin/test-inode-guard.py; never launches real Gradle."""
import contextlib
import importlib.util
import io
import os
from pathlib import Path
import subprocess
import tempfile
from types import SimpleNamespace
import unittest
from unittest.mock import patch

BIN = Path(__file__).resolve().parent
spec = importlib.util.spec_from_file_location("inode_guard", BIN / "inode-guard.py")
guard = importlib.util.module_from_spec(spec)
spec.loader.exec_module(guard)


class GuardTests(unittest.TestCase):
    def check(self, total, free):
        with patch.object(guard.os, "statvfs", return_value=SimpleNamespace(f_files=total, f_favail=free)), contextlib.redirect_stderr(io.StringIO()):
            return guard.check_paths(["/"])

    def test_threshold(self):
        for free in (0, 14, 15):
            with self.subTest(free=free):
                self.assertEqual(self.check(100, free), 4)
        for free in (16, 100):
            with self.subTest(free=free):
                self.assertEqual(self.check(100, free), 0)
        self.assertEqual(self.check(10000, 1501), 0)
        self.assertEqual(self.check(10000, 1500), 4)

    def test_invalid(self):
        for total, free in ((0, 0), (-1, 0), (100, -1), (100, 101)):
            self.assertEqual(self.check(total, free), 4)
        with patch.object(guard.os, "statvfs", side_effect=OSError("unavailable")), contextlib.redirect_stderr(io.StringIO()):
            self.assertEqual(guard.check_paths(["/"]), 4)

    def test_zero_counters_fall_back_to_root(self):
        zero = SimpleNamespace(f_files=0, f_favail=0)
        with patch.object(guard.os, "statvfs", side_effect=[zero, SimpleNamespace(f_files=100, f_favail=80)]), contextlib.redirect_stderr(io.StringIO()):
            self.assertEqual(guard.check_paths([str(BIN)]), 0)
        with patch.object(guard.os, "statvfs", side_effect=[zero, SimpleNamespace(f_files=100, f_favail=15)]), contextlib.redirect_stderr(io.StringIO()):
            self.assertEqual(guard.check_paths([str(BIN)]), 4)

    def test_all_filesystems(self):
        stats = [SimpleNamespace(f_files=100, f_favail=80), SimpleNamespace(f_files=100, f_favail=15)]
        with patch.object(guard.os, "statvfs", side_effect=stats), contextlib.redirect_stderr(io.StringIO()):
            self.assertEqual(guard.check_paths(["/", str(BIN)]), 4)

    def test_missing_cache_parent(self):
        with tempfile.TemporaryDirectory() as d, patch.object(guard.os, "statvfs", return_value=SimpleNamespace(f_files=100, f_favail=80)) as stat:
            self.assertEqual(guard.check_paths([str(Path(d) / "missing/cache")]), 0)
            stat.assert_called_once_with(Path(d))


class EntryPointTests(unittest.TestCase):
    def setUp(self):
        self.temp = tempfile.TemporaryDirectory()
        self.addCleanup(self.temp.cleanup)
        self.root = Path(self.temp.name)
        self.bin = self.root / "bin"
        self.bin.mkdir()
        self.work = self.root / "work"
        self.work.mkdir()
        self.logs = self.root / "logs"
        self.logs.mkdir()
        self.marker = self.root / "gradle-started"
        self.counter = self.root / "guard-count"
        self.agents = self.root / "AGENTS.md"
        self.agents.write_text(f"- Host id: `test`\n- JAVA_HOME: `{self.root}/jdk`\n- GRADLE_USER_HOME: `{self.root}/cache`\n")
        for name in ("sweep-run.sh", "gate-preflight.sh", "bg-steps.sh"):
            text = (BIN / name).read_text()
            text = text.replace("/workspace/AGENTS.md", str(self.agents)).replace("/workspace/tools", str(self.root)).replace("/workspace/tmp", str(self.root))
            # Never source .env in tests; preflight's existing auth path is out of scope.
            text = "\n".join("AUTH=test" if "AUTH=$(set -a;" in line else line for line in text.splitlines()) + "\n"
            (self.bin / name).write_text(text)
        (self.bin / "inode-guard.py").write_text(
            "import importlib.util, os\nfrom pathlib import Path\nfrom types import SimpleNamespace\n"
            f"s=importlib.util.spec_from_file_location('guard', {str(BIN / 'inode-guard.py')!r})\n"
            "g=importlib.util.module_from_spec(s); s.loader.exec_module(g)\n"
            "p=Path(os.environ['GUARD_COUNT']); n=int(p.read_text())+1 if p.exists() else 1; p.write_text(str(n))\n"
            "free=int(os.environ['TEST_FREE']) if n >= int(os.environ.get('LOW_ON_CALL', '1')) else 80\n"
            "g.os.statvfs=lambda p: SimpleNamespace(f_files=100, f_favail=free)\n"
            "raise SystemExit(g.check_paths(__import__('sys').argv[1:]))\n"
        )
        gradle = self.work / "gradlew"
        gradle.write_text('#!/bin/sh\nprintf started >> "$GRADLE_MARKER"\n')
        gradle.chmod(0o755)
        # Fake git materializes only our stub Gradle wrapper at checkout.
        git = self.bin / "git"
        git.write_text('#!/bin/sh\nif [ "$1" = checkout ]; then cp "$STUB_WORK/gradlew" ./gradlew; fi\nexit 0\n')
        git.chmod(0o755)
        self.env = dict(os.environ, PATH=str(self.bin) + ":" + os.environ["PATH"], TEST_FREE="15", GUARD_COUNT=str(self.counter), GRADLE_MARKER=str(self.marker), STUB_WORK=str(self.work), PYTHONDONTWRITEBYTECODE="1")

    def run_script(self, name, *args):
        return subprocess.run(["bash", str(self.bin / name), *map(str, args)], env=self.env, capture_output=True, text=True, timeout=10)

    def test_sweep_refuses_at_boundary(self):
        run = self.run_script("sweep-run.sh", self.work, self.logs, "blocked", "help")
        self.assertEqual(run.returncode, 4, run.stderr)
        self.assertFalse(self.marker.exists())
        self.assertFalse((self.logs / "blocked.start").exists())
        self.assertIn("rc=4", (self.logs / "blocked.log").read_text())

    def test_sweep_allows_above_boundary(self):
        self.env["TEST_FREE"] = "16"
        run = self.run_script("sweep-run.sh", self.work, self.logs, "allowed", "help")
        self.assertEqual(run.returncode, 0, run.stderr)
        self.assertTrue(self.marker.exists())

    def test_background_continue_cannot_bypass_guard(self):
        steps = self.root / "steps"
        steps.write_text("first|help|continue\nsecond|help\n")
        run = self.run_script("bg-steps.sh", "job", self.work, self.logs, steps)
        self.assertEqual(run.returncode, 4, run.stderr)
        self.assertEqual((self.logs / "job/state").read_text().strip(), "stopped at first rc=4")
        self.assertEqual(len((self.logs / "job/steps.tsv").read_text().splitlines()), 1)
        self.assertFalse(self.marker.exists())

    def test_preflight_initial_refusal(self):
        run = self.run_script("gate-preflight.sh", "unused", "unused", self.logs)
        self.assertEqual(run.returncode, 4, run.stderr)
        self.assertFalse(self.marker.exists())
        self.assertIn("<= 15%", (self.logs / "preflight.log").read_text())

    def test_preflight_rechecks_after_lock(self):
        self.env["LOW_ON_CALL"] = "2"
        run = self.run_script("gate-preflight.sh", "unused", "unused", self.logs)
        self.assertEqual(run.returncode, 4, run.stderr)
        self.assertEqual(self.counter.read_text(), "2")
        self.assertFalse(self.marker.exists())

    def test_preflight_allows_above_boundary(self):
        self.env["TEST_FREE"] = "16"
        run = self.run_script("gate-preflight.sh", "unused", "unused", self.logs)
        self.assertEqual(run.returncode, 0, run.stderr)
        self.assertTrue(self.marker.exists())
        self.assertEqual(self.counter.read_text(), "2")


if __name__ == "__main__":
    unittest.main(verbosity=2)
