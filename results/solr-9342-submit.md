# Light verification: solr-9342-submit (script-only, no Gradle)

- Branch: origin/solr-9342-submit
- Head checked: 833e11192a7ff918f7e401bde920cf63c89df063 (from `git rev-parse HEAD` in the worktree)
- Base (merge-base with apache/solr main e34067ae64): 97d973814336101e12475558d7419321c743de79
- Files changed: solr/bin/solr (+5/-2), solr/packaging/test/test_start_solr.bats (+9), changelog/unreleased/SOLR-9342-gc-log-timezone.yml

## Checks

| Check | Result |
| --- | --- |
| Changelog YAML parse (step 0) | PASS (yaml.safe_load, type: fixed) |
| `bash -n solr/bin/solr` | PASS, rc 0 |
| shellcheck `-S warning` on branch vs base | rc 1 on both; the same 13 finding codes with the same counts on both (pre-existing, none added) |
| `env TZ="${TZ:-$SOLR_TIMEZONE}"` default | `SOLR_TIMEZONE` defaults to `UTC` (solr/bin/solr:1196), so the expansion never produces an empty `TZ` |
| Operator `TZ` precedence | `env TZ=America/New_York date +%Z` prints EDT; the operator value wins as the comment says |
| BATS test `SOLR-9342 GC log follows SOLR_TIMEZONE` | NOT RUN: it needs the packaging harness and a started Solr; no Gradle here |

## Verdict

LIGHT VERIFICATION PASS: script syntax is valid, shellcheck is unchanged from base, and the TZ default and precedence behave as the comment states. The BATS test has not been executed and should run in the packaging harness on a host that can start Solr.
