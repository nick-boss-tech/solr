# Review: SOLR-18214, branch solr-18214-empty-configset (unit 39)

Head reviewed: c00cacddf62d67001b13b8aa2c2287a012a5807e. Base: c8ab71430f. Ticket: SOLR-18214, "Base configset during configset creation is not optional".

## Pass 1

- Ticket fit. The ticket reports that CREATE of a configset from the v2 API fails when `baseConfigSet` is omitted (`Base ConfigSet does not exist: null`). The branch lets an omitted base build a configset from generated minimal files (`solrconfig.xml` and `schema.xml`) through a new `minimalConfigSet` flag. The proof leg reproduces the ticket's message on base. The change fits the ticket.
- Scope. Four production files (one of them in `solr/api`), a changelog, and one cloud test class.
- Finding, minor: the generated `solrconfig.xml` contains the placeholder `${tests.luceneMatchVersion:LATEST}`. That is a test-framework property name in production output. In production the default `LATEST` is used, so the behaviour is right, but the placeholder is a leftover that a user would see in the file. Recorded, not repaired (a template change is outside the repair scope).
- Finding, minor: the test covers that the created configset loads a core. It does not check the generated files' content or the `minimalConfigSet` flag on the old (non-v2) path. Recorded.
- Read in code: the `if (minimal)` upload and the `!minimal` base check are consistent; no dead branch (`ConfigSetCmds.java:169-189`).
- Repairs: none.

Regate: `gates/SOLR-18214-review-pass-1.md`, GATE GREEN at c00cacddf6 on vm4.
