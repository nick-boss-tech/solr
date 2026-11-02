# SOLR-5928 tail verdict, batch B claim 3: CORRECTION, do not close yet

- Assignment: `assignments/overseer-tail-verdicts-b-round-1.md`, Claim 3.
- Claim: `claims/solr-5928-tail-verdict-b-phone-agent.md`.
- Ticket: SOLR-5928, "Expose Overseer Status via JMX", Open, last
  updated 2014-08-05, zero comments. Its whole description quotes Otis
  Gospodnetic from SOLR-5749: monitoring tools handle JMX fine even
  though the mbeans live only on the overseer node, but a non-standard
  API forces tools to implement a second mechanism besides JMX, "which
  makes things messy".
- Deliverable type: correction report, not a closure draft. The batch
  rules require this when JMX exposure was what the requester actually
  needed and no equivalent exists, and that is exactly this ticket.
  No Jira post, no close.

## Verdict

Do not close SOLR-5928 as superseded by SOLR-5749. The census premise
gets the ticket backwards: SOLR-5928 was opened because the 5749 API
is NOT a substitute for JMX, not in spite of it. The 5749 thread
records the sequence: Shalin implemented
`/admin/collections?action=overseerstatus` (queue sizes, leader node,
per-operation timing, success/error counts, recent failures); Otis
asked (2014-03-28) whether it could also be exposed via JMX "like
the rest of the stats"; Shalin replied the mbeans would live only on
the overseer node; Otis answered that good monitoring tools handle
that fine and the custom API is the messy part; Shalin agreed ("That
makes sense. I'll open an issue to add a jmx bean") and opened
SOLR-5928 the same day. The ticket's ask is therefore the access
mechanism (JMX), not the data, and the data already existing via the
API does not satisfy it.

## Current surface, verified in code

- The 5749 API still exists: `OverseerStatusCmd`
  (`CollApiCmds.java:185`), returning leader node, overseer and
  collection queue sizes, and per-operation stats with recent
  failures. It covers the data 5928's requester wanted monitored.
- No Overseer JMX bean exists. `OverseerTaskProcessor` exposes its
  stats through the Solr metrics context (`initializeMetrics`,
  category OVERSEER), i.e. the Metrics API, and no JMX reporter
  exists in `solr/core/.../metrics/`; nothing registers an Overseer
  MBean. A monitoring tool that speaks only JMX still cannot get
  Overseer status without a second mechanism, which is the exact
  complaint the ticket records.
- A further gap worth noting, not the verdict's ground: in
  distributed mode `OverseerStatusCmd` returns nothing ("If
  Collection API execution is distributed, we're not running on the
  Overseer node so can't return any Overseer stats"), with a TODO
  for a per-node status command. So even the API surface is
  incomplete in the mode the removal direction favors.

## What would close this ticket

- An Overseer JMX bean (or metrics-to-JMX exposure covering the
  Overseer category), which is what was promised in the 5749 thread
  and never built; or
- A maintainer ruling that the Metrics API plus OVERSEERSTATUS
  supersede JMX for this purpose, stated as a scope decision
  addressing Otis's monitoring-tools argument, not as "the API
  already covers it". The API covering the data is true and
  insufficient: the ticket is about the mechanism.

## Sources checked

- Jira SOLR-5928 (full description quoting the 5749 exchange) and
  SOLR-5749 (Closed 2016; full thread including the 2014-03-28
  JMX exchange and the commit lines), read live.
- `solr/core/src/java/org/apache/solr/cloud/api/collections/OverseerStatusCmd.java`
  (API still present; distributed-mode early return with TODO).
- `solr/core/src/java/org/apache/solr/cloud/OverseerTaskProcessor.java:156,436,725`
  (metrics context, category OVERSEER; no MBean registration).
- Grep for a JMX reporter or Overseer MBean in
  `solr/core/src/java/org/apache/solr/metrics/`: none.
- Limit: code read against the local main checkout at `e55d2e7bcb`
  (2026-09-29), not a fresh upstream fetch.
