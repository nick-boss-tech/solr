#!/usr/bin/env python3
"""Build an offline monthly index from a pinned Solr PR census. No API calls."""
import argparse
import csv
from datetime import datetime, timezone
import hashlib
import html
import io
import json
from pathlib import Path
import re

SHA = re.compile(r'^[0-9a-f]{40}$')
COHORT = ('Merged on/after window_start regardless of creation date; '
          'open/draft at census and created on/after window_start; closed-unmerged excluded.')


def timestamp(value):
    if not isinstance(value, str) or 'T' not in value:
        raise ValueError('Expected a timezone-aware ISO timestamp')
    parsed = datetime.fromisoformat(value.replace('Z', '+00:00'))
    if parsed.tzinfo is None:
        raise ValueError('Timestamp has no timezone')
    return parsed.astimezone(timezone.utc)


def month(value):
    return timestamp(value).strftime('%Y-%m')


def validate(snapshot):
    if snapshot.get('schema_version') != 1 or snapshot.get('repository') != 'apache/solr':
        raise ValueError('Unsupported snapshot schema/repository')
    capture = timestamp(snapshot['captured_at'])
    boundary = datetime.strptime(snapshot['window_start'], '%Y-%m-%d').replace(tzinfo=timezone.utc)
    if boundary > capture:
        raise ValueError('Window starts after capture')
    timestamp(snapshot['coverage_copied_at'])
    seen = set()
    for row in snapshot['records']:
        number = row['number']
        if type(number) is not int or number <= 0 or number in seen:
            raise ValueError('Invalid or duplicate PR number')
        seen.add(number)
        if row['url'] != f'https://github.com/apache/solr/pull/{number}':
            raise ValueError('Unexpected PR URL')
        if not isinstance(row['title'], str) or not SHA.fullmatch(row['head_sha']):
            raise ValueError('Invalid title or head SHA')
        if row.get('scan_head') and not SHA.fullmatch(row['scan_head']):
            raise ValueError('Invalid scan head')
        created = timestamp(row['created_at'])
        if created > capture:
            raise ValueError('Creation after census capture')
        if row['status'] == 'Merged':
            merged = timestamp(row['merged_at'])
            if not boundary <= merged <= capture or merged < created:
                raise ValueError('Merge outside cohort/capture boundary')
        elif row['status'] in ('Open', 'Draft'):
            if row['merged_at'] is not None or created < boundary:
                raise ValueError('Open record outside cohort or already merged')
        else:
            raise ValueError('Unsupported captured PR status')
    return snapshot


def import_inputs(open_path, merged_path, scan_path, captured_at, window_start):
    paths = [Path(open_path), Path(merged_path), Path(scan_path)]
    original = [path.read_bytes() for path in paths]
    scans = {}
    for row in csv.DictReader(io.StringIO(original[2].decode('utf-8-sig'))):
        number = int(row['pr_number'])
        if number in scans:
            raise ValueError('Duplicate scan-register PR')
        scans[number] = row
    records = []
    for data, expected in zip(original[:2], ('OPEN', 'MERGED')):
        raw = json.loads(data.decode('utf-8-sig'))
        if not isinstance(raw, list):
            raise ValueError('Expected a JSON array of PRs')
        for row in raw:
            if row['state'] != expected or type(row['isDraft']) is not bool:
                raise ValueError('Input state/draft mismatch')
            if expected == 'OPEN' and row.get('mergedAt'):
                raise ValueError('Open input unexpectedly has mergedAt')
            number = row['number']
            scan = scans.get(number, {})
            records.append({
                'number': number, 'title': row['title'], 'url': row['url'],
                'status': 'Merged' if expected == 'MERGED' else ('Draft' if row['isDraft'] else 'Open'),
                'created_at': row['createdAt'], 'merged_at': row.get('mergedAt'),
                'head_sha': row['headRefOid'],
                'scan_status': scan.get('scan_status', 'missing'),
                'scan_head': scan.get('head_sha', ''),
                'scan_report_locator': scan.get('report_path', ''),
                'source_review_locator': scan.get('review_record', ''),
            })
    if any(path.read_bytes() != data for path, data in zip(paths, original)):
        raise ValueError('Inputs changed during import; capture a stable set and retry')
    seen = {row['number'] for row in records}
    snapshot = {
        'schema_version': 1, 'repository': 'apache/solr',
        'captured_at': captured_at, 'window_start': window_start,
        'coverage_copied_at': datetime.now(timezone.utc).isoformat(),
        'cohort_definition': COHORT,
        'sources': [{'name': path.name, 'sha256': hashlib.sha256(data).hexdigest()}
                    for path, data in zip(paths, original)],
        'unmatched_scan_numbers': sorted(set(scans) - seen),
        'records': sorted(records, key=lambda row: row['number']),
    }
    return validate(snapshot)


def coverage(row):
    if row['scan_status'] != 'complete':
        return 'not complete'
    return 'complete at captured head' if row.get('scan_head') == row['head_sha'] else 'complete at another head'


def grouped(snapshot):
    groups = {}
    for row in validate(snapshot)['records']:
        key = month(row['merged_at'] if row['status'] == 'Merged' else row['created_at'])
        groups.setdefault(key, []).append(row)
    return {key: sorted(rows, key=lambda row: (row['status'], row['number']))
            for key, rows in sorted(groups.items())}


def text_cell(value):
    value = html.escape(' '.join(str(value).split()), quote=False)
    for char in ('\\', '`', '[', ']', '*', '_', '|'):
        value = value.replace(char, '\\' + char)
    return value


def artifacts(snapshot):
    groups = grouped(snapshot)
    normalized = json.dumps(snapshot, ensure_ascii=True, indent=2) + '\n'
    digest = hashlib.sha256(normalized.encode('utf-8')).hexdigest()
    summary = {'snapshot_sha256': digest, 'months': {}}
    intro = [
        '# Captured Solr PR activity by month', '',
        f"PR census captured at {snapshot['captured_at']}; coverage overlay copied at {snapshot['coverage_copied_at']}.", '',
        f"Window starts {snapshot['window_start']} UTC. " + snapshot['cohort_definition'], '',
        'UTC months: merged PRs use merge time; Open/Draft backlog uses creation time. '
        f"This is not a complete opened/updated-event history. Capture month {month(snapshot['captured_at'])} is partial.", '',
        'Scan completion is source-review coverage, not runtime proof. '
        'Record locators refer to the ongoing scan project; its full reports are not bundled.', '',
        f'Normalized snapshot SHA-256: `{digest}`.', '',
        '| Month | Merged | Open | Draft | Complete at captured head | Complete at another head | Not complete |',
        '| --- | ---: | ---: | ---: | ---: | ---: | ---: |',
    ]
    result = {'snapshot.json': normalized}
    for key, rows in groups.items():
        counts = {status: sum(row['status'] == status for row in rows) for status in ('Merged', 'Open', 'Draft')}
        scans = {status: sum(coverage(row) == status for row in rows)
                 for status in ('complete at captured head', 'complete at another head', 'not complete')}
        summary['months'][key] = {'population': len(rows), 'status': counts, 'coverage': scans}
        intro.append(f"| [{key}]({key}.md) | {counts['Merged']} | {counts['Open']} | {counts['Draft']} | "
                     f"{scans['complete at captured head']} | {scans['complete at another head']} | {scans['not complete']} |")
        page = [f'# {key}: captured Solr PR activity', '',
                'See [the index](INDEX.md) for capture, population, UTC grouping and evidence limits.', '',
                '| PR | Captured status | Title | Captured head | Scan coverage |',
                '| --- | --- | --- | --- | --- |']
        for row in rows:
            page.append(f"| [{row['number']}]({row['url']}) | {row['status']} | {text_cell(row['title'])} | "
                        f"`{row['head_sha']}` | {coverage(row)} |")
        result[f'{key}.md'] = '\n'.join(page) + '\n'
    summary['population'] = len(snapshot['records'])
    summary['unmatched_scan_numbers'] = snapshot.get('unmatched_scan_numbers', [])
    result['summary.json'] = json.dumps(summary, indent=2, sort_keys=True) + '\n'
    result['INDEX.md'] = '\n'.join(intro) + '\n'
    return result


def write_artifacts(snapshot, output):
    output = Path(output)
    if output.exists() and (not output.is_dir() or any(output.iterdir())):
        raise ValueError('Output must be absent or an empty directory')
    rendered = artifacts(snapshot)
    output.mkdir(parents=True, exist_ok=True)
    for name, content in rendered.items():
        (output / name).write_text(content, encoding='utf-8', newline='\n')
    return json.loads(rendered['summary.json'])


def main():
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument('--snapshot', type=Path, help='Reproduce from normalized snapshot.json')
    parser.add_argument('--open-json', type=Path)
    parser.add_argument('--merged-json', type=Path)
    parser.add_argument('--scan-register', type=Path)
    parser.add_argument('--captured-at', help='UTC/offset ISO capture timestamp of the PR census')
    parser.add_argument('--window-start', help='Inclusive UTC date, YYYY-MM-DD')
    parser.add_argument('--output', type=Path, required=True)
    args = parser.parse_args()
    raw_args = (args.open_json, args.merged_json, args.scan_register, args.captured_at, args.window_start)
    if args.snapshot:
        if any(raw_args):
            parser.error('Use either snapshot reproduction or raw import, not both')
        snapshot = validate(json.loads(args.snapshot.read_text(encoding='utf-8-sig')))
    else:
        if not all(raw_args):
            parser.error('Raw import requires all three inputs, captured-at and window-start')
        snapshot = import_inputs(*raw_args)
    print(json.dumps(write_artifacts(snapshot, args.output), sort_keys=True))


if __name__ == '__main__':
    main()
