# SOLR-10364 - hypothetical reproduction (nothing was compiled or run)

JIRA: a `@Field Set<String>` bean field cannot be filled (`IllegalArgumentException: Can not set java.util.Set field
... to java.util.ArrayList`). The audit note "fixed/obsolete (fix version 4.10.4)" is a fix-version tag; the reporter
said it is an enhancement. On `upstream/main` `DocumentObjectBinder.DocField.storeType` still knows only
Collection/List/ArrayList and arrays; there is no `isSet`, and `inject` hands a List to `field.set`.

## Change
`DocField` gets `isSet` for declared types `Set`, `HashSet`, `LinkedHashSet` (not for child documents: BindingException).
`inject` builds a `LinkedHashSet` from the value (collection or single value) and sets it. The write side already
works (`SolrInputField` accepts any Collection).

## Test (guessed)
`TestDocumentObjectBinder.testSetFields` with new `SetItem` bean: multi-valued with a duplicate, single value, round trip value count.

## Guesses to verify first
- A `LinkedHashSet` is assignable to `Set`, and to a declared `LinkedHashSet`/`HashSet` field (it is a subclass of HashSet).
- `Set` as a Map value type for dynamic fields (`Map<String, Set<String>>`) is NOT handled.
- `type = Object.class` for a Set (no element-type conversion, same as List).

## Fail-before
Revert the change: `getBeans` throws BindingException wrapping the IllegalArgumentException.
