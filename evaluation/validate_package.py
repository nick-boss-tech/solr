#!/usr/bin/env python3
"""Validate this package with a supplied skill-creator quick_validate.py."""
import argparse
import hashlib
import json
from pathlib import Path
import re
import subprocess
import sys
import tempfile


def main():
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument('--skill-validator', type=Path, required=True)
    args = parser.parse_args()
    package = Path(__file__).resolve().parents[1]
    metadata = json.loads((package / 'package.json').read_text(encoding='utf-8'))
    results = []
    import yaml  # Same PyYAML requirement as skill-creator's validator.
    for name in metadata['skills']:
        folder = package / '.agents' / 'skills' / name
        check = subprocess.run([sys.executable, '-B', str(args.skill_validator), str(folder)],
                               capture_output=True, text=True)
        results.append({'skill': name, 'exit_code': check.returncode,
                        'result': (check.stdout + check.stderr).strip()})
        if check.returncode:
            raise ValueError(json.dumps(results, indent=2))
        ui = yaml.safe_load((folder / 'agents' / 'openai.yaml').read_text(encoding='utf-8'))
        if not 25 <= len(ui['interface']['short_description']) <= 64:
            raise ValueError('Invalid UI description length')
        if '$' + name not in ui['interface']['default_prompt']:
            raise ValueError('Default prompt does not invoke its skill')
    links = 0
    for path in package.rglob('*.md'):
        for target in re.findall(r'(?<!\\)\]\(([^)]+)\)', path.read_text(encoding='utf-8')):
            if '://' in target or target.startswith('#'):
                continue
            resolved = (path.parent / target.split('#', 1)[0]).resolve()
            if not resolved.is_relative_to(package) or not resolved.exists():
                raise ValueError(f'Broken/escaping package link in {path.relative_to(package)}: {target}')
            links += 1
    script = package / '.agents' / 'skills' / 'solr-pr-map' / 'scripts' / 'monthly_index.py'
    prefill = package / 'handbook' / 'months'
    with tempfile.TemporaryDirectory() as temp:
        reproduced = Path(temp) / 'reproduced'
        check = subprocess.run([sys.executable, '-B', str(script), '--snapshot', str(prefill / 'snapshot.json'),
                                '--output', str(reproduced)], capture_output=True, text=True)
        if check.returncode:
            raise ValueError(check.stderr)
        if {p.name: p.read_bytes() for p in prefill.iterdir()} != {p.name: p.read_bytes() for p in reproduced.iterdir()}:
            raise ValueError('CLI reproduction differs from the prefilled snapshot')
    unit = subprocess.run([sys.executable, '-B', '-m', 'unittest', 'discover', '-s', str(script.parent),
                           '-p', 'test_monthly_index.py', '-v'], capture_output=True, text=True)
    log = unit.stdout + unit.stderr
    (package / 'evaluation' / 'monthly-checks.txt').write_text(log, encoding='utf-8', newline='\n')
    count = re.search(r'Ran (\d+) tests?', log)
    if unit.returncode or not count:
        raise ValueError('Monthly unit checks failed; inspect evaluation/monthly-checks.txt')
    snapshot = json.loads((prefill / 'snapshot.json').read_text(encoding='utf-8'))
    summary = json.loads((prefill / 'summary.json').read_text(encoding='utf-8'))
    if sum(v['population'] for v in summary['months'].values()) != len(snapshot['records']):
        raise ValueError('Monthly population does not reconcile')
    hashes = {p.relative_to(package).as_posix(): hashlib.sha256(p.read_bytes()).hexdigest()
              for p in sorted(package.rglob('*')) if p.is_file() and '__pycache__' not in p.parts
              and p.name != 'structural-validation.json'}
    report = {'skills': results, 'relative_links_checked': links,
              'monthly_index_cli_reproduction': 'byte-identical', 'population': len(snapshot['records']),
              'monthly_unit_checks': {'executed': int(count.group(1)), 'exit_code': unit.returncode,
                                      'source': 'Full subprocess output in monthly-checks.txt'},
              'agent_behavior_evaluation': 'Pending independent review; scenarios are not executed passes',
              'file_sha256': hashes}
    (package / 'evaluation' / 'structural-validation.json').write_text(json.dumps(report, indent=2) + '\n', encoding='utf-8', newline='\n')
    print(json.dumps({k: v for k, v in report.items() if k != 'file_sha256'}, indent=2))


if __name__ == '__main__':
    main()
