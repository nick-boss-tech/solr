# SOLR-7119 - hypothetical reproduction (nothing was compiled or run)

JIRA (2015): the reporter put `{!ex=weight}` on a `facet.interval.set` value and got 0, while the guide says "Filter exclusion is
supported for all types of facets". The audit note said "obsolete: interval facets use parsed.docs after exclusion". That is true
for the form the guide shows, `facet.interval={!ex=tag}field` (`SimpleFacets.getFacetIntervalCounts` builds `IntervalFacets` over
`parsed.docs`, the doc set after exclusion), but the ticket's form, `ex` on the interval set, still does nothing:
`IntervalFacets`/`FacetInterval` only read `key` from a set's local params. The reporter's last request was a docs clarification.

## Change
Docs only: `faceting.adoc` interval section now says `ex` belongs on `facet.interval`, `facet.interval.set` supports only `key`,
and shows the multi-select example from the ticket in the working form. No code, no test.

## Guesses to verify first
- `facet.interval={!ex=weight}weight` with `fq={!tag=weight}...` returns the unfiltered interval counts (read from code, not run;
  `ParsedParams.docs` applies `ex` for every `parseParams` caller).
- No other local param (besides `key`) is honoured on a set (look at `FacetInterval` construction in `IntervalFacets`).
- A supporting test would be `SimpleFacetsTest` with a tagged fq and `{!ex=...}` on `facet.interval` (not added; docs only).

## Fail-before
n/a (documentation).
