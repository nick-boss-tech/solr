# SOLR-17731 unrecorded code claims, round 1

Verdict: **both sentences are KEPT WITH LINKS.** The code shows both claims, so neither needed an owner call. The draft now links each claim to the lines that show it.

Scope: `assignments/pool-solr-17731-unrecorded-claims-round-1.md`. Claim: `claims/pool-solr-17731-unrecorded-claims-round-1.md`. One read-only subagent, as the assignment names. The base is `e2cdb2d7e8ae` and the head is `18165d35a7e3`, both in the local object store.

## Line 16: "The lookup logic is the same as before." — KEPT WITH LINKS

- Base `ListAliases.java` lines 85 to 100 read the aliases from ZooKeeper, take the collection list and the properties for the named alias, and return them.
- Head `GetAliasByName.java` lines 51 to 59 do the same reads, the same map lookups and the same null guard. The only difference is that the helper is inlined.
- Registration moves from `ListAliases.class` at base `CollectionsHandler.java` line 1205 to `GetAliasByName.class` at head line 1207.
- Ladder rung: information first. The code settled it, so the sentence stays and now carries both links.

## Line 39: "The alias response still uses the existing wire key `name`..." — KEPT WITH LINKS

- `GetAliasByNameResponse.java` is identical at base and head. Line 24 is `@JsonProperty("name")`, and line 25 is `public String alias;`.
- The file is not among the nine files the branch changes.
- `ListAliasesAPITest.java` line 111 checks the Java field, not the JSON key.
- Ladder rung: information first. The code settled it, so the sentence stays and now carries its link.

## Changes to the draft

- Line 16: the sentence now links the base `ListAliases.java` lines 85 to 100 and the head `GetAliasByName.java` lines 51 to 59.
- Line 39: the sentence now links `GetAliasByNameResponse.java` lines 24 to 25 at the head.

Nothing else in the draft changed.

## Notes

- The branch diff against the base changes nine files. That count is the branch's own, and the draft does not state it.

## Convergence line

Both sentences are verified and now linked. The draft's remaining receipt-supported claims are unchanged from the fresh check.
