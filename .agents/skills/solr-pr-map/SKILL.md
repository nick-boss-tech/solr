---
name: solr-pr-map
description: Organize an existing Apache Solr PR census and scan register into monthly merged activity and captured open or draft backlog. Use for monthly indexes and scan coverage without restarting source archaeology.
---

# Map captured PR activity

Use the scan project's existing artifacts as input. Read [the input and grouping contract](references/monthly-contract.md) before generating a report. Preserve the capture boundary and distinguish captured state from live state. Do not silently refresh a historical census or mark an unscanned PR reviewed.

Run `scripts/monthly_index.py` with explicit input/output paths. It can import the existing open/merged JSON plus scan-register CSV, or reproduce a report from a normalized snapshot. Run `python scripts/monthly_index.py --help` for the arguments. Use a fresh output directory so an older month's file cannot survive unnoticed.

The output includes a normalized snapshot, monthly Markdown indexes, a machine-readable monthly summary and input hashes. Link records to their actual PRs. Source reviews and scan paths are locators into the separate scan project; their contents are not bundled or newly verified here.

For a narrative month summary, use completed source-backed scans, cite their assessed heads and name missing coverage. PR titles alone support an activity listing, not claims about architecture, correctness or user impact. Keep speculative opportunities separate from verified changes. Do not perform new scans unless the user's scope includes them.

Report population totals, month semantics, capture time, complete-at-matching-head scans, stale scans and missing scans. Propose a later refresh or chapter only for a concrete gap.
