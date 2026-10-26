# Repro viability: 2 repro-needed tickets (vm5, round 1, 2026-10-26)

Pipeline: repro needed > repro is viable > implement repro > gate repro. This round covers the first two steps only. Nothing was implemented or run. Code checked on apache/main `b73f03976fa`.

## SOLR-3945: SolrQuery.setFacet(false) does not add facet=false

Verdict: viable.

- `solrj/src/java/org/apache/solr/client/solrj/request/SolrQuery.java`, lines 370 to 378: `setFacet(false)` removes `facet`, `facet.mincount`, `facet.field`, `facet.limit` and `facet.missing`. It never sends `facet=false`.
- Consequence: a handler whose defaults set `facet=true` will still compute facets after `setFacet(false)`.
- Repro shape: a SolrJ-level unit test that builds `new SolrQuery("*:*").setFacet(false)` and asserts `facet=false` is in the parameters. That fails on main. A second, optional check needs a handler with `facet=true` in its defaults.
- Open question for the maintainers: is removing the parameter the intended meaning, or a bug? The ticket says it is a bug. The test is cheap and deterministic, so the question can be settled by running it.

## SOLR-4130: eDismax phrase boost skips terms inside parentheses

Verdict: viable.

- `core/src/java/org/apache/solr/search/ExtendedDismaxQParser.java`, line 308: `addPhraseFieldQueries` drops clauses that have a field or are phrases. Lines 764 to 766 set `clause.field` only from a `field:` prefix at that position.
- The reported parse, `+sw(a b) +ti:(c d)` with `pf=freitext^6 exttext^3`, gives a phrase boost of `"b d"`, built from terms that sit in different fields. That is the malfunction the ticket describes.
- Repro shape: a parse-level test in `core/src/test/org/apache/solr/search/TestExtendedDismaxParser.java` with the query above and the `pf` settings from the ticket, asserting the parsed string. The current output includes `"b d"`. The expected output is what the maintainers decide.
- Caveat: I have not confirmed in code where the field from `sw(...)` is lost. A probe would show it, but the parsed string already reproduces the symptom.

## Not done

- No repro implemented, no Gradle run.
- Next step if the owner agrees: implement both repros as failing tests at seed `787C6229591C6F1E` on RC3-independent main, then gate them. Gate is a separate decision.

## SOLR-3676: lowercased uniqueKey allows duplicates on update

Verdict: viable, deterministic.

- The indexed id comes from the uniqueKey field's analyzed value (`AddUpdateCommand.java`, `getIndexedIdStr()` around lines 112 to 116). A `lowercase` type on `id` therefore changes the indexed id.
- Repro shape: a single-core test with the schema and `updateLog` from the ticket. Add `{"id":"TEST", ...}` twice and assert `numFound` is 1. The ticket reports 2. This is a plain update test and needs no cloud setup.
- Not confirmed: why the update lookup misses the existing document. The repro shows the symptom only.

## SOLR-4486: batch command order not kept in SolrCloud

Verdict: viable, but heavier and possibly intermittent.

- Ordering is handled in `DistributedUpdateProcessor` and `DistributedZkUpdateProcessor` (`processDelete` at line 346 of the zk one). A command that reaches a non-owning shard is forwarded, so order depends on routing.
- Repro shape: a `MiniSolrCloudCluster` test with two shards, one doc owned by the other shard, and a batch of add, add, delete for that id. Assert the doc is gone at the end.
- Cost: a cloud test takes minutes and may need several seeds to show the race. The gate for this one should use more than one seed.
