# Assignment: evidence units for four warm tickets, each ending in a comment draft (round 1)

Status: OPEN
Tags: premise-run, draft, records

Four separately claimable units, one claim per unit. Each unit produces evidence first and a comment draft second: the draft goes in `jira/` on this branch, in Jira wiki markup, first person, with the footer disclosure, and says only what the unit's evidence supports. Nothing posts; every text needs the owner's explicit go. The judgement that a comment is warranted is part of each unit: if the evidence does not support one, the unit's report says so and no draft is written.

## Unit 1: SOLR-17708 trace (Jason Gerlowski's ticket, updated 2026-10-23)

"JAX-RS v2 APIs go through authorization twice." Trace the double authorization on current main: which layer authorizes first, which second, for one concrete core-level and one container-level v2 endpoint, with file and line citations. Scope limit: this unit is about the double-authorization problem the ticket states, nothing else; do not widen it. Deliverables: `reports/solr-17708-double-auth-trace-round-1.md` and, if the trace confirms the ticket, `jira/jira-comment-SOLR-17708-draft.md` carrying the trace in short form and asking Jason whether a fix should remove one of the two checks or is already planned.

## Unit 2: SOLR-18503 audit (Jan Høydahl's ticket, Eric Pugh commented)

"JSON Facet API: seven parser-accepted keys are missing." Enumerate them: parse what the JSON facet parser accepts, diff against the Ref Guide and the schema, and name the seven keys with the parser citation for each. Deliverables: `reports/solr-18503-facet-keys-audit-round-1.md` and, if the seven check out, `jira/jira-comment-SOLR-18503-draft.md` listing them and offering to add the missing documentation.

## Unit 3: SOLR-18521 scoping count (Jan Høydahl's ticket, 2026-10-21, no comments)

"Unify picocli shared connection options into one mixin." Count the duplication: which CLI classes declare the shared connection options today, which options repeat, and what the single mixin would replace, all cited. Deliverables: `reports/solr-18521-picocli-scoping-round-1.md` and a staged-ask draft at `jira/jira-comment-SOLR-18521-draft.md` carrying the count and offering the refactor as one PR for Jan's review.

## Unit 4: SOLR-18347 verification list (Jan Høydahl's ticket)

"Possible AdminUI bugs found by Selenium." Read the ticket's listed bugs; for each, state whether it still reproduces on current main from the code, with citations, and mark the ones that need a live Selenium run instead. Deliverables: `reports/solr-18347-adminui-bugs-check-round-1.md` and, for any bug confirmed in code, a short draft at `jira/jira-comment-SOLR-18347-draft.md` reporting the confirmation. Selenium runs themselves are a later unit, not this one.
