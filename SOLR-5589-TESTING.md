# SOLR-5589 - hypothetical reproduction (nothing was compiled or run)

JIRA (2013): replication "explicitly disabled in config is ignored". The ticket's own patch was never committed (Shalin: multiple
test failures; the thread ends with Uwe's bulk "move to 4.9"), so the audit note "fixed in 4.9/6.0" was a version move, not a fix.
On `upstream/main` `ReplicationHandler.inform` still does:
`if (!enableFollower && !enableLeader) { enableLeader = true; leader = new NamedList<>(); }`.
A handler whose only section is `<lst name="leader"><str name="enable">false</str>...</lst>` (the ticket's config) ends up as a leader
with replication enabled, committing replicable commit points and serving `indexversion`/`filelist`.

## Change
When neither section is enabled but at least one section is present (so `enable=false` was written by the user), set
`replicationEnabled` to false, equivalent to the `disablereplication` command, then keep the existing fallback so the handler still
works (details, backup, enable via `command=enablereplication`). A handler with no leader/follower config at all is unchanged
(the default leader on commit). This answers the failures Vitaliy described in 2014: "not configured" must keep replicating.
New `TestDisabledReplicationHandler` plus `solrconfig-disabled-replication.xml` (4 handlers: disabled leader, both disabled, nothing
configured, enabled leader), asserting `details/leader/replicationEnabled`.

## Guesses to verify first
- `command=details` puts `replicationEnabled` under `details/leader` for a handler with `isLeader` true; the XPath assumes that
  shape and that `details` is a `lst` in the XML response.
- Existing test configs that set both sections to enable=false through properties (`${enable.master:false}` and `${enable.follower:false}`)
  and still expect leader behaviour (TestReplicationHandler family, `solrconfig-leader*.xml`, `solrconfig-follower*.xml`): at least one
  of the two is true in each. Search for a config with both false before running the family.
- `SolrCloud` cores: replication sections absent, so unchanged.

## Fail-before
Expected: `testDisabledLeaderStaysDisabled` and `testDisabledLeaderAndFollowerStayDisabled` fail on `upstream/main` (`true` instead of
`false`); the other two pass both before and after.
