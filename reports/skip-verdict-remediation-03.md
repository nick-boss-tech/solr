# Skip verdict remediation, batch 3

One line per item: ticket, claim file, verdict recorded, new draft branch head, and the evidence the record cites.

- SOLR-15571 | claims/skip-SOLR-15571-vm5.md | close-cannot-reproduce on solr-15571-draft-close, head a2ceb96a50 (supersedes keep-open on solr-15571-draft-open 7a19edfb57) | not reproduced on the pinned head in three variants (two runs each for the variants); ExportWriter.java:501-539 and MultiFieldWriter.java:84-105 read; reporter's index not covered, see the record
- SOLR-15544 | claims/skip-SOLR-15544-vm5.md | keep-open on solr-15544-draft-open, head 68264e3cda (unchanged verdict; earlier record replaced) | Jira read 2026-10-19: Open, no resolution; code path SolrCore.java:1100, :775-812 and CoreContainer.java:2014 read at 498a09aea97aac86c2cc7ac114113b946d249942; missing piece is whether plugin static state should survive reload (class loading), not reproduced
