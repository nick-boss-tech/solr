# SOLR-15357 — Testing Handoff (external reviewer)

Ticket: https://issues.apache.org/jira/browse/SOLR-15357
("sub-fields of copyField targets should themselves be recorded as targets")
Branch: `solr-15357-submit` (based on origin/main @ 56ec140e363)
Fix commit: 31b07da547d — "SOLR-15357: record sub-fields of copyField targets as targets"

## What the patch does
`IndexSchema.incrementCopyFieldTargetCount` recorded only the top-level copyField
destination, so `RealTimeGetComponent.removeCopyFieldTargets` (which consults
`isCopyFieldTarget`) could not strip internal sub-fields of aggregate field types
from real-time-get documents.

- New `FieldType.getSubFields(baseField, schema)` hook (default: empty list), with
  overrides for `AbstractSubTypeFieldType` (suffix-based, e.g. PointType),
  `CurrencyFieldType` (`___amount` / `___currency`), and `BBoxField` (the five
  `__minX/__maxX/__minY/__maxY/__xdl` fields).
- `incrementCopyFieldTargetCount` records each present sub-field alongside the
  destination. A `postReadInform` pass covers sub-fields only registered during
  `inform()` (BBoxField), with a containsKey guard against double counting.
- `ManagedIndexSchema.decrementCopyFieldTargetCount` decrements sub-fields in sync;
  the field/field-type replace paths zero out the destination's sub-field counts via
  the new `removeCopyFieldTargetCount` helper before copy fields are rebuilt.

## What to verify
1. Compiles: `:solr:core:compileJava -Pvalidation.errorprone=true` (new public API
   on FieldType; no existing `getSubFields` signature collisions — checked).
2. Behavior: schema with `<copyField source="x" dest="price"/>` where `price` is a
   currency field; assert `schema.isCopyFieldTarget(schema.getField("price___currency"))`
   and `...("price___amount")` are true. Same for a BBoxField destination
   (`loc__minX`, etc.) and a PointType destination.
3. Managed-schema round trip: add then delete the copyField via the Schema API;
   assert sub-field target counts return to zero (no leak in copyFieldTargetCounts).
4. Existing suites: `TestIndexSchema` / schema-related tests, plus
   `RealTimeGetComponent` tests if quick.

## Known edges for the reviewer
- Deliberately runs no Gradle in the implement pass; compile + tests are on the reviewer.
- The `>1 && !multiValued` warning loop in `readSchema` runs before `postReadInform`,
  so BBox sub-field counts never feed it (avoids bogus warnings on single-valued
  internal fields).
- Pathological edge: an explicit copyField to an internal sub-field name
  (e.g. `x -> price___currency`) combined with replacing the base field type would
  zero that sub-field's count without rebuild re-adding it. Judged negligible; the
  replace paths mirror the pre-existing zero-out-then-rebuild pattern.

## AI disclosure
This change was drafted with AI assistance (Muse) and has not been compiled or
tested by the author. Human review, compilation, and test validation required
before any upstream PR.
