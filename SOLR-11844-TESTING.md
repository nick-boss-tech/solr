# SOLR-11844 - hypothetical reproduction (nothing was compiled or run)

JIRA: BlendedInfixLookupFactory with `blenderType=position_linear` over `DocumentDictionaryFactory` (field `title`, no
`weightField` in the posted config) returns "sky blue, blue whale, blue water, blue" for `blue` instead of position order.
Earlier audit note: "feature/support question". The config in the ticket has no `weightField`.

## Hypothesis
The ref guide formula is `weightFieldValue * coefficient(position)`. Lucene's `DocumentDictionary` gives weight 0 when no
weight field is configured, so each blended score is `0 * coefficient = 0` and the order falls back to the infix suggester's
internal tie order. The guide never said that.

## Change
Docs only: `suggester.adoc` BlendedInfixLookupFactory gets a paragraph saying the blend multiplies the weight and that a
dictionary without weights yields all-zero scores. No code, no test.

## Guesses to verify first
- `DocumentDictionary` weight is 0 (not 1) when `weightField` is absent, and `BlendedInfixSuggester` multiplies it
  (`(long) (weight * coefficient)`). Not re-read in Lucene source.
- Reproduce: the ticket config plus four docs; add `<str name="weightField">weight</str>` and see the order become positional.

## Fail-before
n/a (documentation).
