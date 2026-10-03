# SOLR-14638: open questions (branch note, remove before the PR)

Full review: `research/branch-reviews/round-5/SOLR-14638-review.md` in the local workspace (not on this remote).

Status: the parsed `ValueSource` is now wrapped in a `def()` (`DefFunction` with `ConstValueSource(1.0f)`) instead of
wrapping the boost string; changelog added. Written uncompiled, not run. This branch should not go upstream as is.

Questions for the owner:

1. Change the default for every edismax multiplicative boost at all? Missing values now become 1.0 instead of the
   computed value, which changes ranking for existing deployments. The one decision on record favours the current
   behaviour plus a user-side `def()`/`if(exists())`. A smaller step is a reference-guide note.
2. `def()` also overwrites explicit `query(..., default)` defaults and expression results over missing fields. Narrow
   the wrapping to plain field sources?
3. Should `{!boost}` follow for consistency?
4. If it goes ahead: upgrade note and reference-guide text.
