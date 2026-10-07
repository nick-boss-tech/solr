# SOLR-10131 hypothetical reproduction

Status: hypothetical, unrun. Nothing was compiled and Gradle was not used.

## Ticket
An update with a bad value for a `UUIDField` returned 500 (a `RuntimeException` wrapped by
`FieldType.createField`). The audit note called it obsolete because `UUIDField.toInternal` now
throws `BAD_REQUEST`.

## Finding on current `upstream/main`
The `BAD_REQUEST` is real, but the validation in `UUIDField.toInternal` only checks the length (36)
and the four dash positions. A value such as `zzzzzzzz-zzzz-zzzz-zzzz-zzzzzzzzzzzz` passes, is
indexed, and is returned later as a "UUID" that `java.util.UUID.fromString` rejects (clients
that bind the value get an exception). Only the shape of the ticket's input (`1249948`, wrong
length) is rejected.

## Change
- `UUIDField.toInternal` calls a new `looksLikeUuid`: 36 characters, dashes at 8/13/18/23 and an
  ASCII hexadecimal digit at every other position (`Character.digit` alone would accept full width
  digits, so characters above `f` are rejected).
- `UUIDFieldTest.testNonHexCharactersAreRejected`: four invalid values give `BAD_REQUEST`; an upper
  case valid UUID is lower-cased as before.

## Expected
Before the fix the first three values are accepted (no exception, so `expectThrows` fails). After
the fix all pass.

## Risky guesses
- Behaviour change: documents that carry a non-hex "UUID" in the right shape used to index and now
  fail with 400. That is the intent, but a user with such data would see the failure on update.
- Other `UUID` entry points (`toInternal(UUID)`, `createFields` through `StrField`) are unchanged.

## Verify later
`:solr:core:test --tests org.apache.solr.schema.UUIDFieldTest`.
