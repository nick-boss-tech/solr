"""Offline checks for grouping, provenance boundaries and safe regeneration."""
import copy
import importlib.util
import json
from pathlib import Path
import tempfile
import unittest

SPEC = importlib.util.spec_from_file_location('monthly_index', Path(__file__).with_name('monthly_index.py'))
INDEX = importlib.util.module_from_spec(SPEC)
SPEC.loader.exec_module(INDEX)


def row(number, status, created, merged=None):
    return {'number': number, 'title': 'A | [title](x) <img>',
            'url': f'https://github.com/apache/solr/pull/{number}',
            'status': status, 'created_at': created, 'merged_at': merged,
            'head_sha': 'a' * 40, 'scan_status': 'queued', 'scan_head': '',
            'scan_report_locator': '', 'source_review_locator': ''}


def snapshot():
    return {'schema_version': 1, 'repository': 'apache/solr',
            'captured_at': '2026-11-05T03:17:59Z', 'window_start': '2026-08-01',
            'coverage_copied_at': '2026-11-06T05:00:00Z',
            'cohort_definition': INDEX.COHORT, 'sources': [], 'records': [
                row(1, 'Merged', '2024-01-01T00:00:00Z', '2026-09-01T00:01:00+02:00'),
                row(2, 'Open', '2026-09-02T00:00:00Z'),
                row(3, 'Draft', '2026-10-02T00:00:00Z')]}


class MonthlyIndexTest(unittest.TestCase):
    def test_merge_month_is_utc_and_uses_merge_not_creation(self):
        groups = INDEX.grouped(snapshot())
        self.assertEqual([1], [r['number'] for r in groups['2026-08']])
        self.assertEqual([2], [r['number'] for r in groups['2026-09']])
        self.assertEqual([3], [r['number'] for r in groups['2026-10']])

    def test_totals_and_stale_coverage_reconcile(self):
        data = snapshot()
        data['records'][0].update(scan_status='complete', scan_head='b' * 40)
        data['records'][1].update(scan_status='complete', scan_head='a' * 40)
        report = json.loads(INDEX.artifacts(data)['summary.json'])
        self.assertEqual(3, sum(v['population'] for v in report['months'].values()))
        self.assertEqual(1, report['months']['2026-08']['coverage']['complete at another head'])
        self.assertEqual(1, report['months']['2026-09']['coverage']['complete at captured head'])

    def test_duplicates_invalid_states_and_future_dates_are_rejected(self):
        data = snapshot()
        data['records'].append(copy.deepcopy(data['records'][0]))
        with self.assertRaises(ValueError):
            INDEX.validate(data)
        for change in ({'status': 'Closed'}, {'created_at': '2027-01-01T00:00:00Z'},
                       {'head_sha': 'no'}, {'created_at': '2026-09-01'},
                       {'merged_at': '2026-10-02T00:00:00Z'}):
            data = snapshot()
            data['records'][1].update(change)
            with self.subTest(change=change), self.assertRaises(ValueError):
                INDEX.validate(data)

    def test_reproduction_is_byte_deterministic_and_titles_are_text(self):
        first = INDEX.artifacts(snapshot())
        again = INDEX.artifacts(json.loads(first['snapshot.json']))
        self.assertEqual(first, again)
        self.assertIn('&lt;img&gt;', first['2026-08.md'])
        self.assertNotIn('[title](x)', first['2026-08.md'])

    def test_existing_nonempty_output_is_preserved(self):
        with tempfile.TemporaryDirectory() as temp:
            out = Path(temp) / 'output'
            INDEX.write_artifacts(snapshot(), out)
            (out / 'old.md').write_text('retain', encoding='utf-8')
            with self.assertRaises(ValueError):
                INDEX.write_artifacts(snapshot(), out)
            self.assertEqual('retain', (out / 'old.md').read_text())

    def test_import_rejects_duplicate_scan_and_invalid_raw_state(self):
        with tempfile.TemporaryDirectory() as temp:
            root = Path(temp)
            raw = [{'number': 2, 'title': 'x', 'url': 'https://github.com/apache/solr/pull/2',
                    'state': 'OPEN', 'isDraft': False, 'createdAt': '2026-09-02T00:00:00Z',
                    'mergedAt': None, 'headRefOid': 'a' * 40}]
            (root / 'open.json').write_text(json.dumps(raw))
            (root / 'merged.json').write_text('[]')
            (root / 'scan.csv').write_text('pr_number,scan_status,head_sha\n2,complete,' + 'a' * 40 + '\n')
            imported = INDEX.import_inputs(root / 'open.json', root / 'merged.json', root / 'scan.csv',
                                           '2026-11-05T03:17:59Z', '2026-08-01')
            self.assertEqual('complete at captured head', INDEX.coverage(imported['records'][0]))
            self.assertEqual(3, len(imported['sources']))
            (root / 'scan.csv').write_text('pr_number\n2\n2\n')
            with self.assertRaises(ValueError):
                INDEX.import_inputs(root / 'open.json', root / 'merged.json', root / 'scan.csv',
                                    '2026-11-05T03:17:59Z', '2026-08-01')
            (root / 'scan.csv').write_text('pr_number\n')
            raw[0]['state'] = 'CLOSED'
            (root / 'open.json').write_text(json.dumps(raw))
            with self.assertRaises(ValueError):
                INDEX.import_inputs(root / 'open.json', root / 'merged.json', root / 'scan.csv',
                                    '2026-11-05T03:17:59Z', '2026-08-01')


if __name__ == '__main__':
    unittest.main()
