# Skip record: SOLR-7804

- Ticket: SOLR-7804, TestCloudPivotFacet failures: expected:<X> but was:<Y>. JIRA status at check: Open, checked 2026-10-18.
- Verdict: close-fixed.
- Draft branch: solr-7804-draft-close.
- Pinned head: fork origin/main 498a09aea9.
- Checked by: vm5, 2026-10-18T21:32:17Z.

## Ticket reading

The ticket is Open with no resolution at check. The summary is above; the report row is the evidence below.

## Code reading

The failures were test bugs, plus a product bug that was split out to SOLR-7829, which is closed.

- 1692552: `TestCloudPivotFacet` generated the same `facet.pivot` parameter twice.
- 1693160: epsilon comparison was wrong for negative expected values.
- 1e0e8d45e5 (2018): removed assertions on lossy `stats.field` values. This is the change that fixes the seed failure reported on 2018-10-31.

Caveat for the maintainer: the 2018 change removes checks on lossy stats values. Coverage was reduced on purpose.

Proposed resolution: Fixed

## History

- `1e0e8d45e5`: ancestor of origin/main.

## Reproduction attempts

Not attempted. This is a fix-commit verdict: the commit that fixes the reported problem is on main, and its changed code is still present at the pinned head. A reproduction is not required for close-fixed.

## Gap analysis

Fix commit named in the draft; the draft text is the evidence.

## Verdict justification

close-fixed: the fixing commit (or, for a removed feature, the removal commit and an empty search) is named above, each cited commit is an ancestor of 498a09aea9, and the changed code or the empty search is recorded at the pinned head. Rubric minimum for close-fixed is met.
