# Assignment: SOLR-18616 Jetty HTTP and thread pool metrics

Status: HANDED (vm4; code on branch SOLR-18616-jetty-metrics at a22f47b6dd, see claims/solr-18616-jetty-metrics-handoff-vm4.md)

## Scope

- Source: SOLR-18616 (Chris Hostetter, mailing-list thread "Solr 10(.1) jetty layer metrics?"). SOLR-17855 removed the Dropwizard Jetty instrumentation without a replacement.
- Tags: implementation, gate, records.
- Code: branch `SOLR-18616-jetty-metrics` on origin, commit `a22f47b6dd` (based on origin/main `498a09aea9`). It has not been pushed as a PR.
- Decisions the owner must make before a PR: see the decisions section of the handoff claim.

## Result so far

- Tidy passes. `JettyServerMetricsTest` 2/2 and `JvmMetricsTest` 4/4 at seed `F98EA7EDC1450EB6`.
- Root `check -x test -x :rat` fails in the Antora ref-guide build. Not yet shown to be pre-existing on main.
- Root `:rat` fails on 56 untouched files, not from this change.

## Next

- Check the Antora failure on clean origin/main.
- Owner decides scope and whether to split the change, then a PR is opened by the main agent.
