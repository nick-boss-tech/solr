# SOLR-18503: JSON Facet API keys accepted by the parser but missing from the Ref Guide, round 1

Claim: `claims/warm-ticket-evidence-units-round-1-unit-2-SOLR-18503-vm2.md`. Job: `assignments/warm-ticket-evidence-units-round-1.md`, unit 2.
Base: apache/solr main `13c693d805f6541f7cec684cd27d28eef7b3b614`, read with `git grep` at that commit. Ticket read live through the Jira REST API on 2026-10-25 (status Open, reporter Jan Høydahl, unassigned, last updated 2026-10-05).

## Result

All seven keys in the ticket check out. The parser reads each one, and the JSON Facet pages of the Ref Guide do not document it. Two details differ from the ticket: the `perSeg` quote carries a word the code does not, and the ticket's `f` hint needs no correction (see below).

## Parser evidence (paths relative to `solr/core/src/java/org/apache/solr/search/facet/`)

| Key | Accepted where | Effect in code | Guide |
| --- | --- | --- | --- |
| `processEmpty` | `FacetParser.java:73`, inside `parseSubs` (line 64) | sets `facet.processEmpty`, read by `FacetFieldProcessor.java` and `FacetProcessor.java` | no hits in the whole Ref Guide |
| `cacheDf` | `FacetParser.java:564`, in the terms parser `FacetFieldParser` (line 535) | `FacetFieldProcessorByEnumTermsStream.java:175`: `-1` never caches; `0` is the default | no hits in the whole Ref Guide |
| `perSeg` | `FacetParser.java:569` | `FacetFieldProcessorByArrayDV.java:130-131` overrides the perSeg heuristic | `FacetField.java:38` says "experimental ... currently for testing purposes". The only Ref Guide hit is an unrelated spatial cache name in `spatial-search.adoc:560` |
| `f` (alias of `field`) | `FacetParser.java:289`, in `getField` (line 286) | used by terms (`FacetParser.java:551`) and heatmap (`FacetHeatmap.java:122`). Range reads `field` only (`FacetRangeParser.java:42`) | the `"f"` hits in `json-faceting-domain-changes.adoc:92-96` are JSON query-DSL `field` queries in a `queries` block, not facet keys |
| `excludeTags` at facet level | `FacetParser.java:209-211`, in `parseCommonParams` (line 205), which terms and query facets call | written to the domain, same as `domain.excludeTags` | documented only as `domain.excludeTags` (`json-faceting-domain-changes.adoc:124-165`) |
| `count` stat function | `CountAgg.java:23` (`super("count", null)`); `ValueSourceParser.java:1144` registers `agg_count`; `FunctionQParser.java:562` adds the `agg_` prefix | `count()` resolves by code reading. Not executed in this unit | the stat function table (`json-facet-api.adoc:561-586`) lists `countvals` at line 574 and has no `count` row |
| `query` (alias of `q`) in `type: query` | `FacetParser.java:506` reads `q`, then `FacetParser.java:508` reads `query`, in `FacetQueryParser` (line 488) | same query for both keys | the guide shows only `q` in query-facet examples (`json-facet-api.adoc:245-246`, `278`) |

## Ticket claims checked

- Seven keys, all accepted: confirmed by code (table above).
- Undocumented: confirmed for `processEmpty`, `cacheDf`, `count`, facet-level `excludeTags`, `query`, and `f` on range facets. `f` on terms and heatmap is also not in the JSON Facet pages.
- `perSeg` quote: the ticket quotes "experimental … currently for testing purposes only". The code comment at `FacetField.java:38` reads "experimental - force perSeg collection when using dv method, currently for testing purposes". The word "only" is not in the source. The draft does not quote it.
- Runtime claim: the ticket says each key was verified against a running 10.0.0. This unit did not start Solr. Parse results here come from code reading only.
- Cited classes: the ticket names `FacetFieldParser` and `FacetParser.parseSubs`. Both exist as named (`FacetParser.java:535` and `:64`).

## Decision split

The ticket proposes three groups (document; decide; reject). This unit adds no decision. The draft asks the reporter one question about the documentation group.

## Limits

- Code reading and `git grep` at the stated commit. No Solr instance run, no build, no test.
- The Ref Guide check covers the query-guide pages named in the table, plus a whole-guide grep for `processEmpty`, `cacheDf`, `perSeg`, `excludeTags`, and `agg_count`.
- Nothing posted to Jira or GitHub. The draft is not reviewed by Nick.
