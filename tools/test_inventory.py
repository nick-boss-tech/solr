import unittest
from unittest.mock import patch

import inventory


class InventoryTest(unittest.TestCase):
    def test_snapshot_preserves_tree_and_does_not_infer_claim_state(self):
        body = b"# A job\nStatus: OPEN\nSee claims/job.md\n"
        a, b = "a" * 40, "b" * 40
        listing = (f"100644 blob {a} {len(body):7}\tassignments/job.md\0"
                   f"100644 blob {b} {5:7}\treceipts/raw.xml\0").encode()
        payload = f"{a} blob {len(body)}\n".encode() + body + b"\n"

        def fake_git(repo, *args, input=None):
            if args[0] == "rev-parse":
                return ("c" * 40 + "\n").encode()
            if args[0] == "ls-tree":
                return listing
            self.assertEqual(input, (a + "\n").encode())
            return payload

        with patch.object(inventory, "git", fake_git):
            result = inventory.snapshot("unused", "ref")
        self.assertEqual(result["summary"]["files"], 2)
        self.assertEqual(result["entries"]["receipts/raw.xml"]["git_oid"], b)
        self.assertEqual(result["entries"]["assignments/job.md"]["reported_status"], "OPEN")
        self.assertEqual(result["entries"]["assignments/job.md"]["mentioned_paths"], ["claims/job.md"])
        self.assertNotIn("owner", result["entries"]["assignments/job.md"])

    def test_compare_reports_add_remove_and_mode_changes(self):
        old = {"snapshot": "old", "entries": {"a": {"git_oid": "1", "mode": "100644"}, "b": {"git_oid": "2", "mode": "100644"}}}
        new = {"snapshot": "new", "entries": {"a": {"git_oid": "1", "mode": "100755"}, "c": {"git_oid": "3", "mode": "100644"}}}
        result = inventory.compare(old, new)
        self.assertEqual((result["added"], result["removed"], result["modified"]), (["c"], ["b"], ["a"]))


if __name__ == "__main__":
    unittest.main()
