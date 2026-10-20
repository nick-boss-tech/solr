# Skip verdict remediation, batch 10

One line per item: ticket, claim file, verdict recorded, new draft branch head, and the evidence the record cites.

- SOLR-11688 | claims/skip-SOLR-11688-vm5.md | close-obsolete on solr-11688-draft-close, head b2c983c394 (verdict changed from close-fixed; same close draft updated) | Jira read 2026-10-20: Open, no resolution, 1 comment; TransientSolrCoreCacheDefault and log text 0 hits in whole solr tree; removed by cb6dd31eca (SOLR-17932, ancestor ok)
- SOLR-11675 | claims/skip-SOLR-11675-vm5.md | keep-open on solr-11675-draft-open, head b3dc563370 (unchanged verdict; bulk audit-derived record replaced) | Jira read 2026-10-20: Open, no resolution, 1 comment; V2HttpCall.java:131-133 size guard before get(1); :233-275 getApiInfo null for unsupported method; client response for DELETE/PATCH/PROPFIND not established; server run not done
- SOLR-11672 | claims/skip-SOLR-11672-vm5.md | keep-open on solr-11672-draft-open, head 9b0b97c0e6 (unchanged verdict; bulk audit-derived record replaced) | Jira read 2026-10-20: Open, no resolution, 0 comments; FacetComponent.java:734 unguarded qf.count; :844 removal in countFacets; two-shard run not done
