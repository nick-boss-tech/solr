The failures were test bugs, plus a product bug that was split out to SOLR-7829, which is closed.

- 1692552: `TestCloudPivotFacet` generated the same `facet.pivot` parameter twice.
- 1693160: epsilon comparison was wrong for negative expected values.
- 1e0e8d45e5 (2018): removed assertions on lossy `stats.field` values. This is the change that fixes the seed failure reported on 2018-10-31.

Caveat for the maintainer: the 2018 change removes checks on lossy stats values. Coverage was reduced on purpose.

If you still see this:

- Reopen with the Solr version and steps, and say which of the commits named here does not cover it.
