# Skip verdict audit

## 2026-10-19

Scope: 20 auditable closeout records in file order in material/skip-audit/queue-current.json at coordination tip 4a0eaa1602059de1525030536cf16350254ed2f8, pinned head 498a09aea97aac86c2cc7ac114113b946d249942. Result: 13 pass, 7 suspect. Gate receipts for 20 DONE gate tickets were also checked this run; no gate receipt suspect was found.

For every suspect below, the draft branch named in queue-current.json exists on the fork and contains drafts/<KEY>/record.md and drafts/<KEY>/jira-comment.md, the record cites the pinned head that matches queue-current.json, the word count is over 40, the text is not identical to another ticket record, and the verdict and draft branch suffix match the rubric. The missing pieces are only the ones listed per ticket.

Claiming host: none of the suspects has a claiming host, because none has a skip closeout claim. Each record says it was checked by vm2 from an earlier reading dated 2026-10-18.

### SOLR-17820, verdict keep-open, draft branch solr-17820-draft-open

Missing:
- No skip closeout claim in claims/: no claims/skip-SOLR-17820-<host>.md and no closeout claim with a DONE line. The only file in claims/ naming this key is claims/pool-skip-review-committed-support-round-1.md, a 2026-10-18 review round, not a closeout claim.
- The re-check required by steps 11 and 12: record.md states JIRA was not re-read for this record; its JIRA status is taken from the earlier B3 reading in reports/2026-10-18-skip-repro/B3-committed-support-round-1.md.

### SOLR-17763, verdict close-fixed, draft branch solr-17763-draft-close

Missing:
- No claim in claims/ naming this key at all, so no closeout claim with a DONE line.
- The re-check required by steps 11 and 12: record.md states the JIRA status was not recorded in the B2 reading and JIRA was not re-read for this record.

### SOLR-17751, verdict keep-open, draft branch solr-17751-draft-open

Missing:
- No claim in claims/ naming this key at all, so no closeout claim with a DONE line.
- The re-check required by steps 11 and 12: record.md states JIRA was not re-read for this record; its JIRA status is taken from the earlier B3 reading in reports/2026-10-18-skip-repro/B3-committed-support-round-1.md.

### SOLR-17610, verdict keep-open, draft branch solr-17610-draft-open

Missing:
- No claim in claims/ naming this key at all, so no closeout claim with a DONE line.
- The re-check required by steps 11 and 12: record.md states the JIRA status was not recorded in the C reading and JIRA was not re-read for this record.

### SOLR-17509, verdict keep-open, draft branch solr-17509-draft-open

Missing:
- No claim in claims/ naming this key at all, so no closeout claim with a DONE line.
- The re-check required by steps 11 and 12: record.md states the JIRA status was not recorded in the C reading and JIRA was not re-read for this record.

### SOLR-17376, verdict keep-open, draft branch solr-17376-draft-open

Missing:
- No claim in claims/ naming this key at all, so no closeout claim with a DONE line.
- The re-check required by steps 11 and 12: record.md states the JIRA status was not recorded in the C reading and JIRA was not re-read for this record.
- A code path with file and line, or a reproduction attempt with a result: record.md names no file and line in its code reading, and its reproduction section states only Not attempted, with no attempt and no result.

### SOLR-17259, verdict keep-open, draft branch solr-17259-draft-open

Missing:
- No claim in claims/ naming this key at all, so no closeout claim with a DONE line.
- The re-check required by steps 11 and 12: record.md states the JIRA status was not recorded in the C reading and JIRA was not re-read for this record.
