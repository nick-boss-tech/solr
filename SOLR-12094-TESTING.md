# SOLR-12094 - hypothetical reproduction and fix (not run)

Nothing here was compiled or executed. Verify the guesses below first.

## JIRA context
`JsonRecordReader`, with a split below the top level (e.g. `/exams`), silently ignores
top-level fields that come after the split node in the JSON text, so which root fields reach the
record depends on field order in the input. The ticket (14 comments) ends with Dawid Weiss and
Noble Paul agreeing that the streaming default must not drop data quietly: it should throw, with
an opt-in relaxed (buffering) mode for users who need it. The buffering patch by Andrzej
Wislowski was never turned into the two-mode version.

## Bug mechanism
Records are streamed: `Node.handleObjectStart` emits the `/exams` records when each array
element ends, and the parent frame's `values` map is only copied into records at that time. A
mapped leaf in the parent that arrives later is written into `values` after all records are gone
and is then discarded by the frame's `finally` cleanup.

## Fix (strict-streaming half only)
Each object frame remembers whether a record was emitted from a split below it
(`recordEmittedBelow`). A mapped, non-null leaf that arrives in such a frame afterwards now
raises a `RuntimeException` naming the field and telling the user to move it before the split.
Unmapped trailing fields and fields before the split behave as before. The opt-in relaxed
(buffering) mode from the ticket is NOT implemented.

## What the test pins
`TestJsonRecordReader.testMappedFieldAfterSplitIsRejected`: the ticket's input shape with a
mapped `/after` field throws; the same input with only `/first` and `/exams/subject` mapped still
returns two records carrying `first`.

## What was guessed / verify first
- That throwing is acceptable as default behavior; this is a behavior change for anyone whose
  input has trailing mapped root fields and who never noticed they were dropped. Callers of
  `/update/json/docs` get the exception as a 400/500 from `JsonLoader`; the exact status was not
  checked.
- Other tests in `solr/core` (`TestJsonLoader`, `JsonLoaderTest`-style `f=` mappings) and DIH-like
  users may map fields after a split; run `TestJsonRecordReader` and the JSON loader tests.
- Multi-level splits (`/|/a/b`, child records) take the `isChildRecord` branch and do not set the
  flag; not exercised by the new test.
- Spotless and the exact exception type (a dedicated `SolrException` BAD_REQUEST might fit better
  in core, but this class lives in SolrJ).
