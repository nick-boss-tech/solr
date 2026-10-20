# Review: SOLR-16155, branch solr-16155-submit (unit 12)

Head reviewed: 0881de1ed68b847c781abd7ccb56196cdbf15ecd (ls-remote 2026-10-20T15:37Z). Base: 14c7aac0d1. Commits: ddc04108ae (redact document values from update errors), 1c79186d10 (keep the document id, values at TRACE, changelog), 0881de1ed6 (pass the exception itself to the TRACE call). Diff: JavabinLoader +12/-2, DocumentBuilder (+39/-22 region), three test files, changelog. Receipt: gate green (hybrid); flagged: upstream PR #1151 covers the ticket; opening is the owner's call; not parked.

## Pass 1

Review (2026-10-20, vm5):

- Change. The update error messages for a failed document add no longer carry field values. `DocumentBuilder` now builds `ERROR: <id>Error adding field '<name>'` and logs `value=` at TRACE with the exception. The multiple-values messages drop the value. `JavabinLoader` replaces the document dump in `ERROR adding document` with the unique key, or nothing when the schema has no unique key. `MAX_VALUES_AS_STRING_LENGTH` is removed; no test references it (the test compile is green).
- Finding, minor (disclosed): the exception text (`msg=`) is also dropped from the response, not only the values. The ticket asks for field values to be left out; the exception text can carry a value (for example a number-parse message), so dropping it is wider than the ticket. The changelog says "only the document id and the field name", so the change is disclosed; the cause stays attached to the exception for server-side logs. Recorded for the owner; not repaired.
- Finding, minor (test expectations): `TolerantUpdateProcessorTest` and `DocumentBuilderTest` expected messages are updated to the new text in this branch. That is the commit's own intent, not a regression.
- Check: `name` in `getFieldErrorMessage(doc, schema, name)` is the same `field.getName()` captured at line 172, so the field name in the message matches the field.

Gate (vm5, on 0881de1ed6):
- `:solr:core:compileTestJava` rc=0; `JavabinLoaderTest` tests=3, failures=0, errors=0; `DocumentBuilderTest` tests=17, failures=0, errors=0; `TolerantUpdateProcessorTest` tests=11, failures=0, errors=0 (`/workspace/gates/logs/vm5/g16155-test.log`, XMLs under the gate worktree).

Outcome: green with a minor scope note; no blocker in pass 1.

Regate: gates/SOLR-16155-review-pass-1.md (DONE).

## Pass 2

Review (2026-10-20T15:52Z, vm5, second pass on head 0881de1ed68b847c781abd7ccb56196cdbf15ecd; ls-remote unchanged):

- Test expectations against the new message shape. `TolerantUpdateProcessorTest` (lines 240 and 270) expects `ERROR: [doc=1] Error adding field 'weight'`. The production message is `"ERROR: " + getID(doc, schema) + "Error adding field '" + name + "'"`, and `getID` returns `[doc=1] ` for this schema, so the expected string matches the new shape. The expected text carries no field value. The regex muter at line 81 still matches `Error adding field`. Consistent.
- Rerun. `JavabinLoaderTest` 3/3, `DocumentBuilderTest` 17/17, `TolerantUpdateProcessorTest` 11/11, all failures=0 and errors=0 (`/workspace/gates/logs/vm5/g16155-test-p2.log`).
- The scope note from pass 1 stands (exception text dropped from the response, disclosed in the changelog).

Repairs: none.

Regate: gates/SOLR-16155-review-pass-2.md (DONE).
