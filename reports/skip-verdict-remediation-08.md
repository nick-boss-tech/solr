# Skip verdict remediation, batch 8

One line per item: ticket, claim file, verdict recorded, new draft branch head, and the evidence the record cites.

- SOLR-12844 | claims/skip-SOLR-12844-vm5.md | keep-open on solr-12844-draft-open, head 99bbe209f9 (unchanged verdict; audit-derived record replaced) | Jira read 2026-10-20: Open, no resolution, updated 2019-06-08; test present at pin (TestDistributedStatsComponentCardinality.java:110), no skip marker; A1 run used seed A0D025C0835FBE2A (PASS), not the ticket's CCA4941EC30785C; ticket seed run not done
- SOLR-12819 | claims/skip-SOLR-12819-vm5.md | keep-open on solr-12819-draft-open, head 8fa60de2b2 (unchanged verdict; audit-derived record replaced) | Jira read 2026-10-20: Open, no resolution, updated 2019-06-08; TestPointFields.java:179 present, no skip marker; failing assertion not in ticket; JDK 8/11 run not done
- SOLR-12808 | claims/skip-SOLR-12808-vm5.md | keep-open on solr-12808-draft-open, head 69cecf3b14 (unchanged verdict; audit-derived record replaced) | Jira read 2026-10-20: Open, no resolution, updated 2019-06-08; offset mapping in Lucene PatternReplaceCharFilter (sources removed by 168df90d79, ancestor); no Solr-side fault shown; run not done
