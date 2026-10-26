#!/usr/bin/env python3
# Licensed to the Apache Software Foundation (ASF) under one or more
# contributor license agreements.  See the NOTICE file distributed with
# this work for additional information regarding copyright ownership.
# The ASF licenses this file to You under the Apache License, Version 2.0
# (the "License"); you may not use this file except in compliance with
# the License.  You may obtain a copy of the License at
#
#     http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.

"""Exercise the real license-header Gradle script in disposable Git repositories.

Run on Linux with JDK 21 and Git. No Solr compilation or third-party Python
packages are needed. --script can select an earlier script for fail-before proof.
The Spotless-named fixture tasks test ordering; the real Solr tidy is a separate
integration check.
"""

import argparse
import os
from pathlib import Path
import shutil
import subprocess
import tempfile
import unittest


ROOT = Path(__file__).resolve().parents[2]
OPTIONS = None


class LicenseHeadersTest(unittest.TestCase):
    def setUp(self):
        self.temporary = tempfile.TemporaryDirectory(prefix="solr-license-headers-")
        self.addCleanup(self.temporary.cleanup)
        self.root = Path(self.temporary.name) / "fixture"
        self.root.mkdir()
        self.invocations = 0
        self.header = (ROOT / "gradle/validation/spotless/asl-header.txt").read_bytes()
        self.header = self.header.replace(b"\r\n", b"\n")
        self.write("settings.gradle", "rootProject.name = 'license-header-test'\ninclude 'a', 'b'\n")
        self.write("gradle/validation/spotless/asl-header.txt", self.header)
        self.write("license-headers.gradle", OPTIONS.script.read_bytes())
        self.write(".gitignore", "out/\nbuild/\n.gradle/\n")
        self.write("Old.java", "class Old {}\n")
        for name in ("a", "b"):
            (self.root / name).mkdir()
        self.write("build.gradle", """
tasks.register('tidy')
subprojects { prj ->
  def formatting = tasks.register('spotlessJava') {
    doLast {
      def source = file('src/Probe.java')
      if (source.exists() && !source.getText('UTF-8').startsWith('/*\\n * Licensed to')) {
        throw new GradleException('formatting ran before header insertion: ' + prj.path)
      }
    }
  }
  def applyFormatting = tasks.register('spotlessJavaApply') { dependsOn formatting }
  def tidyTask = tasks.register('tidy') { dependsOn applyFormatting }
  rootProject.tasks.named('tidy').configure { dependsOn tidyTask }
}
apply from: 'license-headers.gradle'
""")
        self.git("init", "-b", "fixture")
        self.git("add", ".")
        self.git("commit", "-m", "Fixture baseline")
        self.git("update-ref", "refs/remotes/origin/main", "HEAD")

    def write(self, path, content):
        target = self.root / path
        target.parent.mkdir(parents=True, exist_ok=True)
        target.write_bytes(content.encode("utf-8") if isinstance(content, str) else content)
        return target

    def git(self, *args):
        result = subprocess.run(
            ["git", "-c", "commit.gpgsign=false", "-c", "user.name=License Header Test",
             "-c", "user.email=test@example.invalid", "-C", str(self.root), *args],
            stdout=subprocess.PIPE, stderr=subprocess.STDOUT, check=False,
        )
        self.assertEqual(0, result.returncode, result.stdout.decode("utf-8", errors="replace"))
        return result.stdout

    def gradle(self, *args, success=True):
        self.invocations += 1
        command = [str(OPTIONS.gradle), "-p", str(self.root), "--console=plain",
                   "--no-configuration-cache", *args]
        result = subprocess.run(
            command, stdout=subprocess.PIPE, stderr=subprocess.STDOUT, check=False,
            timeout=300,
        )
        output = result.stdout.decode("utf-8", errors="replace")
        if OPTIONS.log_dir:
            log = OPTIONS.log_dir / f"{self._testMethodName}-{self.invocations}.log"
            log.write_text(repr(command) + "\n" + output, encoding="utf-8")
        if success:
            self.assertEqual(0, result.returncode, output)
        else:
            self.assertNotEqual(0, result.returncode, output)
        return output

    def test_new_sources_preserve_notices_and_are_idempotent(self):
        committed = self.write("Committed.java", "class Committed {}\n")
        self.git("add", "Committed.java")
        self.git("commit", "-m", "New Java source")
        staged = self.write("Staged.java", "class Staged {}\n")
        self.git("add", "Staged.java")
        new = [committed, staged]
        for path in ("Untracked.java", "Unicode/Café.java", "space dir/Space.java",
                     "tab\tname/Tab.java", "line\nbreak/Line.java"):
            new.append(self.write(path, "class Example {}\n"))
        # An identifier/string is not a leading license notice.
        new.append(self.write("StringLiteral.java",
                              'class StringLiteral { String copyright = '
                              '"Licensed to the Apache Software Foundation"; }\n'))
        unchanged = {}
        notices = {
            "Asf.java": self.header + b"class Asf {}\n",
            "Mit.java": b"/* Copyright 2026 Example. MIT License. */\nclass Mit {}\n",
            "Spdx.java": b"// SPDX-License-Identifier: BSD-3-Clause\nclass Spdx {}\n",
            "Generated.java": b"/* Generated By: Example */\nclass Generated {}\n",
            "Annotated.java": b'@javax.annotation.processing.Generated("Example")\nclass Annotated {}\n',
            "LongNotice.java": b"/*\n" + b" * attribution\n" * 60 +
                               b" * Copyright 2026 Example\n */\nclass LongNotice {}\n",
            "out/Scratch.java": b"class Scratch {}\n",
            "build/GeneratedWithoutMarker.java": b"class BuildOutput {}\n",
            "build-tools/missing-doclet/Local.java": b"class BuildTool {}\n",
            "ui/shared/Local.java": b"class SeparateBuild {}\n",
            "solr/core/src/java/org/apache/solr/parser/Token.java": b"class Token {}\n",
            "Old.java": b"class Old { int changed; }\n",
        }
        for path, content in notices.items():
            unchanged[self.write(path, content)] = content
        self.git("add", "-f", "build/GeneratedWithoutMarker.java")
        outside = Path(self.temporary.name) / "outside.java"
        outside.write_bytes(b"class Outside {}\n")
        (self.root / "Symlink.java").symlink_to(outside)
        unchanged[outside] = outside.read_bytes()
        nested = self.write("nested/Local.java", "class Nested {}\n")
        subprocess.run(["git", "init", str(nested.parent)], check=True,
                       stdout=subprocess.PIPE, stderr=subprocess.STDOUT)
        unchanged[nested] = nested.read_bytes()
        bom_body = b"\xef\xbb\xbfclass Bom { String value = \"caf\xc3\xa9\"; }\r\n"
        bom = self.write("Bom.java", bom_body)

        self.gradle("addLicenseHeaders")
        for path in new:
            self.assertTrue(path.read_bytes().startswith(self.header), str(path))
        self.assertEqual(b"\xef\xbb\xbf" + self.header.replace(b"\n", b"\r\n") + bom_body[3:],
                         bom.read_bytes())
        for path, content in unchanged.items():
            self.assertEqual(content, path.read_bytes(), str(path))
        self.assertTrue((self.root / "Symlink.java").is_symlink())
        snapshots = {path: path.read_bytes() for path in [*new, bom]}
        self.gradle("addLicenseHeaders")
        for path, content in snapshots.items():
            self.assertEqual(content, path.read_bytes(), str(path))

    def test_remote_upstream_main_without_local_main(self):
        self.git("update-ref", "refs/remotes/upstream/main", "HEAD")
        self.git("update-ref", "-d", "refs/remotes/origin/main")
        source = self.write("Committed.java", "class Committed {}\n")
        self.git("add", "Committed.java")
        self.git("commit", "-m", "New Java source")
        self.gradle("addLicenseHeaders")
        self.assertTrue(source.read_bytes().startswith(self.header))

    def test_unicode_git_paths(self):
        self.git("update-ref", "refs/heads/main", "HEAD")
        source = self.write("Unicode/Café.java", "class Example {}\n")
        self.gradle("addLicenseHeaders")
        self.assertEqual(self.header + b"class Example {}\n", source.read_bytes())

    def test_preserve_existing_notices_and_generated_sources(self):
        originals = {
            "ThirdParty.java": b"/* Copyright 2026 Example. BSD license. */\nclass ThirdParty {}\n",
            "Spdx.java": b"// SPDX-License-Identifier: MIT\nclass Spdx {}\n",
            "Generated.java": b"/* Generated By: Example */\nclass Generated {}\n",
        }
        for path, content in originals.items():
            self.write(path, content)
        self.gradle("addLicenseHeaders")
        for path, content in originals.items():
            with self.subTest(path=path):
                self.assertEqual(content, (self.root / path).read_bytes())

    def test_explicit_base_overrides_defaults(self):
        source = self.write("Committed.java", "class Committed {}\n")
        self.git("add", "Committed.java")
        self.git("commit", "-m", "New Java source")
        pending = self.write("Pending.java", "class Pending {}\n")
        self.gradle("addLicenseHeaders", "-PlicenseHeadersBase=HEAD")
        self.assertEqual(b"class Committed {}\n", source.read_bytes())
        self.assertTrue(pending.read_bytes().startswith(self.header))

    def test_local_main_fallback(self):
        self.git("update-ref", "refs/heads/main", "HEAD")
        self.git("update-ref", "-d", "refs/remotes/origin/main")
        source = self.write("Committed.java", "class Committed {}\n")
        self.git("add", "Committed.java")
        self.git("commit", "-m", "New Java source")
        self.gradle("addLicenseHeaders")
        self.assertTrue(source.read_bytes().startswith(self.header))

    def test_missing_base_fails_before_writing(self):
        self.git("update-ref", "-d", "refs/remotes/origin/main")
        source = self.write("Pending.java", "class Pending {}\n")
        output = self.gradle("addLicenseHeaders", success=False)
        self.assertIn("licenseHeadersBase", output)
        self.assertEqual(b"class Pending {}\n", source.read_bytes())

    def test_invalid_override_fails_before_writing(self):
        source = self.write("Pending.java", "class Pending {}\n")
        self.gradle("addLicenseHeaders", "-PlicenseHeadersBase=missing-ref", success=False)
        self.assertEqual(b"class Pending {}\n", source.read_bytes())

    def test_failed_git_query_does_not_write(self):
        unrelated = self.git("commit-tree", "HEAD^{tree}", "-m", "Unrelated history").decode().strip()
        source = self.write("Pending.java", "class Pending {}\n")
        output = self.gradle("addLicenseHeaders", f"-PlicenseHeadersBase={unrelated}", success=False)
        self.assertIn("failed", output)
        self.assertEqual(b"class Pending {}\n", source.read_bytes())

    def test_source_archive_is_unchanged(self):
        archive = Path(self.temporary.name) / "archive"
        shutil.copytree(self.root, archive, ignore=shutil.ignore_patterns(".git"))
        self.root = archive
        source = self.write("Pending.java", "class Pending {}\n")
        output = self.gradle("addLicenseHeaders")
        self.assertIn("no Git metadata", output)
        self.assertEqual(b"class Pending {}\n", source.read_bytes())

    def test_linked_git_worktree(self):
        linked = Path(self.temporary.name) / "linked"
        self.git("worktree", "add", "-b", "linked", str(linked), "HEAD")
        self.root = linked
        self.assertTrue((linked / ".git").is_file())
        source = self.write("Pending.java", "class Pending {}\n")
        self.gradle("addLicenseHeaders")
        self.assertTrue(source.read_bytes().startswith(self.header))

    def test_parallel_tidy_orders_formatting_and_is_idempotent(self):
        sources = [self.write(f"{name}/src/Probe.java", "class Probe {}\n") for name in ("a", "b")]
        self.gradle("tidy", "--parallel", "--max-workers=2")
        snapshots = {path: path.read_bytes() for path in sources}
        for path, content in snapshots.items():
            self.assertTrue(content.startswith(self.header), str(path))
        self.gradle("tidy", "--parallel", "--max-workers=2")
        for path, content in snapshots.items():
            self.assertEqual(content, path.read_bytes(), str(path))


if __name__ == "__main__":
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument("--gradle", type=Path, default=ROOT / "gradlew")
    parser.add_argument("--script", type=Path, default=ROOT / "gradle/license-headers.gradle")
    parser.add_argument("--log-dir", type=Path)
    OPTIONS, remaining = parser.parse_known_args()
    OPTIONS.gradle = OPTIONS.gradle.resolve()
    OPTIONS.script = OPTIONS.script.resolve()
    if OPTIONS.log_dir:
        OPTIONS.log_dir = OPTIONS.log_dir.resolve()
        OPTIONS.log_dir.mkdir(parents=True, exist_ok=True)
    if os.name != "posix":
        parser.error("run the functional suite on a Linux gate host")
    unittest.main(argv=[__file__, *remaining], verbosity=2)
