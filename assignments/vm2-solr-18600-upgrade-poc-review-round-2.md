# Assignment: decide next steps for SOLR-18600 with an upgrade demo (vm2, round 2)

Status: OPEN. Review and decide. Nothing has been posted to GitHub, JIRA, or the mailing list. No PR is open.

Follows `assignments/vm2-solr-18600-premise-review-round-1.md` (`a6af3056725`) and its addendum (`0643ae17ead`). It answers the review agent's Windows review (`reports/vm2-solr-18600-premise-review-round-1-windows.md`, board `a468ae747228`).

## What the demo shows

Source and run script: `reports/solr-18600-poc/`. Run `run.sh` (JDK 21; it fetches SolrJ 10.0.0 and 10.1.0 from Maven Central). `deps.txt` holds host-specific Gradle-cache paths for the other runtime jars.

| Stage | Result |
|---|---|
| App compiled against 10.0.0, run on 10.0.0 | `client built` |
| Same bytes, run on published 10.1.0, no recompile | `NoSuchMethodError` on `HttpJdkSolrClient$Builder.withConnectionTimeout` |
| App recompiled against 10.1.0, run on 10.1.0 | `client built` |

The same call on the branch's source, from the earlier probe: a consumer compiled against 10.1.0 passes on base `a336bf871b2c` and fails on the four-file core set and on the full ten-file set (`0643ae17ead`). The Cloud builder call tested passes on all three.

## What it does not show

- Cloud getters (`CloudHttp2SolrClient.getHttpClient`) and LB builders.
- Reverse direction (10.1-compiled bytes on 10.0.0).
- A real server connection; the app only builds the client.
- The full Linux test suite.

## Decisions for review

1. Is a user-facing break in a 10.1 minor release acceptable, given the project's stated norm against removals in minor releases (Solr 9 upgrade notes)? If not, who fixes it, and where?
2. Restore the 10.0 descriptors (branch as drafted) when the probe shows they break 10.1 callers? Or document a 10.1 recompile requirement in the 10.1 upgrade notes and leave the code alone?
3. If restoration is kept, what proof covers consumers compiled against both 10.0.0 and 10.1.0, including the Cloud and LB paths?
4. Is the 10.1.0 upgrade note the release manager's to add while 10.1.0 is still at rc3? Who raises it?
5. Does the demo belong in the review record only, or in the mailing-list question?

## Limits

No GitHub, JIRA, or mailing-list action taken. No PR opened. The demo and probe artifacts on vm2 are under `/workspace/poc/solr-10-binary-break` and `gates/logs/vm2/solr18600-probe/`.
