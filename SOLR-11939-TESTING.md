# SOLR-11939 - hypothetical reproduction (not run)

Nothing here was built. Docs-only change guessed from reading `upstream/main`.

## JIRA context
Reporter passed `property.name=...` to Collections API CREATE and the cores still came out as
`<collection>_shardN_replicaM`. Varun Thacker: one name cannot apply to four shards; the reporter conceded and asked that
the docs say so. Docs say only "Set core property name to value".

## What main shows
`collection-management.adoc` CREATE section, `property._name_=_value_`: generic text and an expert warning, nothing about `name`.
`AddReplicaCmd` also derives the core name; the ref guide documents no `name` parameter for ADDREPLICA, so the note does not
promise a way to pick one.

## Change
A NOTE inside the `property._name_=_value_` entry stating that core names are generated and `property.name` does not rename them.

## Guessed / verify first
- That `property.name` is still ignored in the current CREATE path (read `CreateCollectionCmd`/`CollectionHandlingUtils`, not run).
- Whether the same note belongs on the ADDREPLICA `property._name_` entry (unchecked).
- Example core name format `_replica_n1` taken from the current ref guide examples.
