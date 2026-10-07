# SOLR-8088 - hypothetical reproduction (NOT RUN)

Guessed, never compiled or executed. No Gradle was run.

JIRA: distributed grouping on a (tokenized, multi-valued docValues) field fails with
`IllegalStateException: unexpected docvalues type SORTED_SET ... (expected=SORTED)`, while a single-shard request works.
Yonik: fallout of LUCENE-5666 (no uninverting); no general fix. Smaller piece done here: a clear 400 up front.

Change: `SearchGroupsFieldCommand.checkGroupable` (also used by `TopGroupsFieldCommand.Builder`) throws BAD_REQUEST for a
non-numeric multiValued group field. Numeric fields use the ValueSource path and are unchanged.

Test: new `SearchGroupsFieldCommandTest` (builder level, no cluster).

Guesses to verify first:
- The failure is multiValued-related (SORTED_SET) rather than a missing-docValues case; a single-valued field without docValues is deliberately NOT rejected
  because existing distributed tests may rely on it.
- schema.xml `cat` is multiValued string, `id` single valued.
- Whether a Builder-level check fires before the shard-side failure in the real request path (it is built per shard request).
