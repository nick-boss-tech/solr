# SOLR-18521: connection-option declarations and resolvers in the picocli CLI, round 1

Claim: `claims/warm-ticket-evidence-units-round-1-unit-3-SOLR-18521-vm2.md`. Job: `assignments/warm-ticket-evidence-units-round-1.md`, unit 3.
Base: apache/solr main `13c693d805f6541f7cec684cd27d28eef7b3b614`, read with `git grep` at that commit. Ticket read through the Jira REST API on 2026-10-25 (Open, reporter and assignee Jan Høydahl, updated 2026-10-21, no comments). Paths are under `solr/core/src/java/org/apache/solr/cli/`.

## Result

The ticket's problem is real on main, and the scope is larger than the ticket states. Three counts differ from the ticket's wording, and one ticket claim is wrong: there is already a static `resolveSolrUrl` in two places.

## Counts

| Item | Count on main | Where |
| --- | --- | --- |
| Exclusive `ConnectionOptions` ArgGroup fields | 16 tool classes | `ConfigTool.java:119`, `CreateTool.java:130`, `DeleteTool.java:97`, `ExportTool.java:202`, `HealthcheckTool.java:100`, `PackageSubCommand.java:38`, `PackageTool.java:144`, `PostLogsTool.java:97`, `PostTool.java:294`, `SnapshotCreateTool.java:73`, `SnapshotDeleteTool.java:73`, `SnapshotDescribeTool.java:83`, `SnapshotExportTool.java:130`, `SnapshotListTool.java:57`, `StreamTool.java:99`, `VersionTool.java:38` |
| Non-exclusive `ZkConnectionOptions` Mixin fields | 10 tool classes | `AuthTool.java:200`, `ClusterTool.java:92`, `ConfigSetDownloadTool.java:65`, `ConfigSetUploadTool.java:68`, `UpdateACLTool.java:51`, `ZkCpTool.java:77`, `ZkLsTool.java:43`, `ZkMkrootTool.java:56`, `ZkMvTool.java:54`, `ZkRmTool.java:46` |
| Connection options re-declared in a private class | `StatusTool.TargetOptions` (`StatusTool.java:78`), options at lines 85, 93, 99, 118 | |
| Declaring classes of `-s`, `--solr-url`, `-z` | `ConnectionOptions.java:33`, `ZkConnectionOptions.java:31`, `StatusTool.java:78`, plus `ApiTool.java:65` (its own `--solr-url`, documented as a full endpoint URL) | |
| Per-tool `-c/--name` declarations | 8 tools: `ConfigTool.java:124`, `CreateTool.java:135`, `DeleteTool.java:102`, `ExportTool.java:207`, `HealthcheckTool.java:105`, `PostLogsTool.java:102`, `PostTool.java:299`, `StreamTool.java:133`. Snapshot tools use `CollectionNameOptions.java:27` | |
| `zkHost` environment fallback sites (excluding the default provider) | 8 sites in 7 files: `CLIUtils.java:268`, `ConfigTool.java:285`, `ConnectionOptions.java:103` and `:137`, `CreateTool.java:446`, `DeleteTool.java:170`, `HealthcheckTool.java:284`, `StreamTool.java:712` | |
| Static `resolveSolrUrl` helpers | 2: `CLIUtils.java:257` and `ConnectionOptions.java:80` | |

Other sites: `CliDefaultValueProvider.java:27` keys on `argSpec.paramLabel()` ("<zkHost>", "<solrUrl>", and so on). Those labels come from picocli's default, which is the field name, because the connection option classes set no explicit `paramLabel`. The ticket's paramLabel point is correct.

## Ticket claims checked

- "Three parallel declarations": there are three option classes with the same connection flags, plus `ApiTool`'s own.
- "Exclusive ArgGroup (create, delete, version)": understated. Sixteen tool classes use the exclusive group on main.
- "Zk-family tools re-declare `--credentials`": confirmed at `ZkConnectionOptions.java:56`.
- "About eight copies of URL resolution": confirmed as 8 `zkHost` fallback sites. Those 8 sites are in 7 files.
- "Four different fallback behaviours": not confirmed. Two resolvers differ (below). The per-tool inline sites were not classified, so no count of four is claimed here.
- "No shared resolve-a-base-URL counterpart": wrong. Two static `resolveSolrUrl` methods exist, at `CLIUtils.java:257` and `ConnectionOptions.java:80`. Their callers split: `PackageTool.java:248` uses the `ConnectionOptions` one, and the five Snapshot tools use the `CLIUtils` one (`SnapshotCreateTool.java:151`, `SnapshotDeleteTool.java:151`, `SnapshotDescribeTool.java:190`, `SnapshotExportTool.java:265`, `SnapshotListTool.java:128`).
- `ConnectionOptions.effectiveSolrUrl()` and `effectiveZkHost()` (`ConnectionOptions.java:49-78` region) already account for `--solr-connection`. The `CLIUtils` resolver reads the raw fields instead, so the two resolvers differ in precedence for a `--solr-connection` value.
- Behaviour difference, verified in code: for a `zkHost` that is not a ZooKeeper string, `CLIUtils.resolveSolrUrl` throws `IOException` (`CLIUtils.java` around line 275). `ConnectionOptions.resolveSolrUrl` passes it to `solrUrlFromConnection` without that check.
- `ApiTool`: its `--solr-url` is documented as a full endpoint URL (`ApiTool.java:65-68`) and carries `paramLabel = "URL"`, so the `SOLR_URL` default does not reach it. The ticket's requirement to opt out already holds through the label.
- "commons-cli rejects `--zk-host` with `--solr-url`": not checked in this unit.

## Limits

- Code reading and `git grep` at the stated commit. No build, no test, no CLI run.
- The duplicate-resolver difference is shown for the code paths. Its user-visible effect was not run.
- No Jira or GitHub write. The draft is not reviewed by Nick.
