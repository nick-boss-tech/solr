# Skip verdict remediation, batch 7

One line per item: ticket, claim file, verdict recorded, new draft branch head, and the evidence the record cites.

- SOLR-13278 | claims/skip-SOLR-13278-vm5.md | keep-open on solr-13278-draft-open, head 1d0232df17 (verdict changed from close-answered; solr-13278-draft-close left in place) | Jira read 2026-10-20: Open, no resolution, updated 2019-06-08; suggester.adoc:679 documents multiple suggest.dictionary parameters, which answers usage but not the repeated output the ticket reports; SuggestComponent.java:94, :124-132, :296-330 read; prepare-path trace and the two-dictionary run not done
