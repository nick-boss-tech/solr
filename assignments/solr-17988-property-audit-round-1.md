# Research assignment: SOLR-17988 property-transition audit

Status: OPEN

Research only; do not post anything to Jira or GitHub, do not change code. Claim this assignment before starting, and write the report to `reports/solr-17988-property-audit-round-1.md` when done.

## Background

SOLR-17988 (Eric Pugh, Blocker, Open) asks that the Solr 10 release properly convey the changes in Solr property names and what they mean for all the places users set them. Solr 10.0.0 has shipped. The Solr 10 major changes and upgrade notes carry property transitions inline, scattered by topic (env var and system property pairs, renames such as `SOLR_JETTY_HOST` / `-Dsolr.jetty.host` to `SOLR_HOST_BIND` / `-Dsolr.host.bind`, removed properties such as `solr.pki.sendVersion`). What nobody has done is the line-by-line check: every property transition that actually happened between the last 9.x line and 10.0, set against what the documentation says.

A short status note to Pugh is drafted and waiting on this audit; the audit decides whether the note can present finished coverage or must name specific gaps.

## Task

Build the complete list of Solr property transitions between 9.x (final state of the 9 line) and 10.0:

- System properties renamed, removed, or newly introduced with a changed meaning (the `solr.*` namespace and friends).
- Environment variables renamed or removed (`SOLR_*` in `bin/solr` and `bin/solr.in.sh` / `.cmd` equivalents).
- Settings whose canonical place moved (for example solr.xml attributes, cluster properties, or config file keys that were replaced by a property, or the reverse).

Sources to mine: the 10.0 sections of CHANGES.txt; `solr/solr-ref-guide/modules/upgrade-notes/pages/major-changes-in-solr-10.adoc` at the 10.0 release state; git history on the property-handling code (EnvUtils, SysProps, `bin/solr` scripts) between the branch_9x split and the 10.0 release; deprecation warnings in the 9.x code naming their replacements.

For each transition, record: old name, new name (or REMOVED), where a user could set the old one, and whether the Solr 10 major changes or upgrade notes document it (quote the line or give the file and section).

## Deliverable

`reports/solr-17988-property-audit-round-1.md` with:

1. The transition table, one row per transition, with its documentation status.
2. A gaps list: transitions with no documentation, or documentation that names the change but not the places it can be set, each with the evidence.
3. A one-paragraph verdict: is the ticket's ask fully covered by the shipped documentation, covered with named gaps, or materially incomplete?

Cite commits, files, or doc lines for every claim; a transition asserted without a source does not count. If the set turns out to be small, say so with the search that establishes it.
