# SOLR-9382 - hypothetical reproduction (nothing was compiled or run)

JIRA: a standalone leader/follower pair with a managed schema, synonyms and stopwords; the follower needs `confFiles`, but the
managed-resource files are generated per language/field, so the reporter asks for wildcard globs in `confFiles` ("a possible
improvement"). Earlier audit note: "feature (replicating managed resources glob)". Nothing else in the ticket (reload after config
replication) is touched here.

## Change
`ReplicationHandler.getConfFileInfoFromCache` runs its `confFiles` entries through `expandConfFileGlobs`: if the name part (after
the last `/`) contains `*` or `?`, the entry becomes the sorted, regular files of that directory matched by
`Files.newDirectoryStream(dir, glob)` (not recursive). Plain entries and aliases are unchanged; an alias on a pattern is dropped.
The directory must stay inside the config dir (`normalize().startsWith(configPath)`), else the entry is skipped with a WARN.
Expansion happens on the leader at `filelist` time, so the follower needs no change and sees the concrete names.

## Test
`TestReplicationConfFileGlob` (default `solrconfig.xml`/`schema.xml`, implicit `/replication` handler): plain name unchanged,
`stopwords*.txt` yields stopwords.txt and stopwordsWrongEncoding.txt only, unmatched pattern is empty, `../*` and `../../*.xml`
are empty.

## Guesses to verify first
- The implicit `/replication` handler exists in the `solrconfig.xml` test core and is a `ReplicationHandler` whose `core` is
  set (`inform`); if not, load a leader config such as `solrconfig-leader.xml`.
- `getConfigPath()` of the test core is `collection1/conf`, which holds both stopwords files.
- A follower's conf-file comparison (`IndexFetcher.downloadConfFiles`) copes with a changing set of leader names (new file appears).
- Managed resources live under `<conf>/managed-resources/` or the managed-schema area; a ZK-based core has no config dir (not relevant here).

## Fail-before
Expected: on main `stopwords*.txt` does not exist as a file, so the glob test gets an empty list and fails the size assertion.
