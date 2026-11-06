import unittest
import json
from pathlib import Path

import work_catalog


class CatalogTest(unittest.TestCase):
    def test_checked_in_catalog_matches_inventory(self):
        root = Path(__file__).resolve().parent.parent / "migration"
        manifest = json.loads((root / "legacy-inventory-0d91325.json").read_text(encoding="utf-8"))
        stored = json.loads((root / "legacy-work-catalog.json").read_text(encoding="utf-8"))
        self.assertEqual(work_catalog.catalog(manifest), stored)
        self.assertEqual(work_catalog.markdown(stored, "nick-boss-tech/solr"),
                         (root / "legacy-work-catalog.md").read_text(encoding="utf-8"))

    def test_catalog_keeps_evidence_separate_from_status(self):
        manifest = {"snapshot": "a" * 40, "entries": {
            "assignments/a.md": {"git_oid": "1", "bytes": 5, "title": "A", "reported_status": "OPEN", "mentioned_paths": ["claims/missing.md"]},
            "claims/c.md": {"git_oid": "2", "bytes": 6, "title": "Claim", "reported_status": "ACTIVE", "mentioned_paths": ["assignments/a.md"]},
            "gates/g.md": {"git_oid": "3", "bytes": 7, "title": "Gate", "reported_status": None, "mentioned_paths": []},
            "reports/r.md": {"git_oid": "4", "bytes": 8, "mentioned_paths": ["claims/c.md"]},
        }}
        result = work_catalog.catalog(manifest)
        self.assertEqual(result["counts"], {"assignments": 1, "claims": 1, "gates": 1})
        rows = {r["path"]: r for r in result["records"]}
        self.assertEqual(rows["assignments/a.md"]["reported_status"], "OPEN")
        self.assertEqual(rows["assignments/a.md"]["mentioned_by"], ["claims/c.md"])
        self.assertEqual(rows["assignments/a.md"]["mentions_not_found"], ["claims/missing.md"])
        self.assertNotIn("derived_status", rows["assignments/a.md"])
        text = work_catalog.markdown(result, "example/project")
        self.assertIn("example/project/blob/" + "a" * 40 + "/claims/c.md", text)
        self.assertIn("not a new queue", text)


if __name__ == "__main__":
    unittest.main()
