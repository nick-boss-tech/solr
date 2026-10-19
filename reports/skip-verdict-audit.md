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

## 2026-10-19 (second run)

Scope: the next 20 auditable closeout records in file order in material/skip-audit/queue-current.json at coordination tip b700e341478c8f28f04921d3a397a557951c2861, pinned head 498a09aea97aac86c2cc7ac114113b946d249942. Result: 4 pass (SOLR-17365, SOLR-17250, SOLR-17219, SOLR-17169, all vm5 closeouts with a DONE claim, a draft branch containing both draft files, and a substantive record re-checked at the pinned head with file and line), 16 suspect. Gate receipts checked this run: SOLR-18555 is the only DONE gate ticket not already checked; receipts/SOLR-18555.md exists and names the gated head b99259cba3 recorded in its gate file, so no gate receipt suspect was found.

For every suspect below, the draft branch named in queue-current.json exists on the fork and contains drafts/<KEY>/record.md and drafts/<KEY>/jira-comment.md, the record cites the pinned head that matches queue-current.json, the word count is over 40, the record.md text is not identical to another ticket record in this batch, and the verdict and draft branch suffix match the rubric. The missing pieces are only the ones listed per ticket. Note on bulk records: the jira-comment.md files are byte-identical across the bulk groups (one shared text for SOLR-17086, SOLR-17003, SOLR-16930, SOLR-16792, SOLR-16772, SOLR-16758, SOLR-16657 and SOLR-16652; one for SOLR-17154, SOLR-16786, SOLR-16785 and SOLR-16548; one for SOLR-16609 and SOLR-16526).

### SOLR-17224, verdict close-fixed, draft branch solr-17224-draft-close

Claiming host: none. The record says it was checked by vm2 from an earlier B3 reading dated 2026-10-18.

Missing:
- No claim in claims/ naming this key at all, so no closeout claim with a DONE line.
- The re-check required by steps 11 and 12: record.md states JIRA was not re-read for this record; its JIRA status is taken from the earlier B3 reading.

### SOLR-17154, verdict keep-open, draft branch solr-17154-draft-open

Claiming host: none. The record says it was checked by vm2 from an earlier C reading dated 2026-10-18.

Missing:
- No claim in claims/ naming this key at all, so no closeout claim with a DONE line.
- The re-check required by steps 11 and 12: record.md states the JIRA status was not recorded in the C reading and JIRA was not re-read for this record.

### SOLR-17133, verdict keep-open, draft branch solr-17133-draft-open

Claiming host: none. The record source is the earlier A3 report dated 2026-10-18.

Missing:
- No claim in claims/ naming this key at all, so no closeout claim with a DONE line.
- The re-check required by steps 11 and 12: record.md states JIRA was not re-read for this record, its code reading was not read for this record, and the runs were not repeated. The record does state the A3 run command, seed and result; the missing piece is the re-check itself, not a reproduction statement.

### SOLR-17086, verdict keep-open, draft branch solr-17086-draft-open

Claiming host: vm5 (claims/skip-SOLR-17086-vm5.md, with a DONE line).

Missing:
- A re-check record on the named draft branch: the record on solr-17086-draft-open is a bulk audit-derived record, not the re-check record the vm5 claim describes. It states JIRA was not re-read, the audit was checked against upstream main c8ab71430f not the fork pin, and it was not re-checked at the pinned head. Its code reading is taken from the audit evidence, not re-read at the pinned head, names no code path with file and line, and its reproduction section states not attempted in this record.
- The draft head the claim states: the claim revised line states the new draft solr-17086-draft-open at fdf29b716508, which is not a valid object on the fork and does not match the fork tip of that branch, 564eb5312949c64c1fdd9abce02543e0abd96786.

### SOLR-17003, verdict keep-open, draft branch solr-17003-draft-open

Claiming host: vm5 (claims/skip-SOLR-17003-vm5.md, with a DONE line).

Missing:
- A re-check record on the named draft branch: the record on solr-17003-draft-open is a bulk audit-derived record, not the re-check record the vm5 claim describes. It states JIRA was not re-read, the audit was checked against upstream main c8ab71430f not the fork pin, and it was not re-checked at the pinned head. Its code reading is taken from the audit evidence, not re-read at the pinned head, names no code path with file and line, and its reproduction section states not attempted in this record.
- The draft head the claim states: the claim DONE line states the draft branch head as aa50491d480c, which is not a valid object on the fork and does not match the fork tip of that branch, be5edf5582e25efcb77bd67fd6633aacaa50d5e5.

### SOLR-16930, verdict keep-open, draft branch solr-16930-draft-open

Claiming host: vm5 (claims/skip-SOLR-16930-vm5.md, with a DONE line).

Missing:
- A re-check record on the named draft branch: the record on solr-16930-draft-open is a bulk audit-derived record, not the re-check record the vm5 claim describes. It states JIRA was not re-read, the audit was checked against upstream main c8ab71430f not the fork pin, and it was not re-checked at the pinned head. Its code reading is taken from the audit evidence, not re-read at the pinned head, names no code path with file and line, and its reproduction section states not attempted in this record.
- The draft head the claim states: the claim DONE line states the draft branch head as 523f44ad750a, which is not a valid object on the fork and does not match the fork tip of that branch, 0c1dd8c3c88a19a1118597f8abd3b4d112d44866.

### SOLR-16792, verdict keep-open, draft branch solr-16792-draft-open

Claiming host: none at the audit tip. A vm5 claim appeared after the audit tip; see the second missing item.

Missing:
- No claim in claims/ naming this key at all at the audit tip, so no closeout claim with a DONE line.
- The re-check required by steps 11 and 12: record.md is a bulk audit-derived record. It states JIRA was not re-read, the audit was checked against upstream main c8ab71430f not the fork pin, and it was not re-checked at the pinned head. Its code reading is taken from the audit evidence, not re-read at the pinned head, and its reproduction section states not attempted in this record.
- Post-tip note: the vm5 claim that appeared after the audit tip states the draft branch head as 8cb805ac4575, which is not a valid object on the fork and does not match the fork tip of that branch, 9269c45912e2d85948a308a97b49e7ae4f48d8c1, and the branch record is unchanged.

### SOLR-16786, verdict keep-open, draft branch solr-16786-draft-open

Claiming host: none. The record says it was checked by vm2 from an earlier C reading dated 2026-10-18.

Missing:
- No claim in claims/ naming this key at all, so no closeout claim with a DONE line.
- The re-check required by steps 11 and 12: record.md states the JIRA status was not recorded in the C reading and JIRA was not re-read for this record.

### SOLR-16785, verdict keep-open, draft branch solr-16785-draft-open

Claiming host: none. The record says it was checked by vm2 from an earlier C reading dated 2026-10-18.

Missing:
- No claim in claims/ naming this key at all, so no closeout claim with a DONE line.
- The re-check required by steps 11 and 12: record.md states the JIRA status was not recorded in the C reading and JIRA was not re-read for this record.
- A code path with file and line, or a reproduction attempt with a result: record.md names no file and line; its code reading states no code path is in the ticket and nothing on head can be tied to the failure, and its reproduction section states only Not attempted, with no attempt and no result.

### SOLR-16772, verdict keep-open, draft branch solr-16772-draft-open

Claiming host: none. The record source is the queue entry audit field, round audit-1 dated 2026-10-04.

Missing:
- No claim in claims/ naming this key at all, so no closeout claim with a DONE line.
- The re-check required by steps 11 and 12: record.md states JIRA was not re-read, the audit was checked against upstream main c8ab71430f not the fork pin, and it was not re-checked at the pinned head. Its code reading is taken from the audit evidence, not re-read at the pinned head, names no code path with file and line, and its reproduction section states not attempted in this record.

### SOLR-16758, verdict keep-open, draft branch solr-16758-draft-open

Claiming host: none. The record source is the queue entry audit field, round audit-1 dated 2026-10-04.

Missing:
- No claim in claims/ naming this key at all, so no closeout claim with a DONE line.
- The re-check required by steps 11 and 12: record.md states JIRA was not re-read, the audit was checked against upstream main c8ab71430f not the fork pin, and it was not re-checked at the pinned head. Its code reading is taken from the audit evidence, not re-read at the pinned head, names no code path with file and line, and its reproduction section states not attempted in this record.

### SOLR-16657, verdict keep-open, draft branch solr-16657-draft-open

Claiming host: none. The record source is the queue entry audit field, round audit-1 dated 2026-10-04.

Missing:
- No claim in claims/ naming this key at all, so no closeout claim with a DONE line.
- The re-check required by steps 11 and 12: record.md states JIRA was not re-read, the audit was checked against upstream main c8ab71430f not the fork pin, and it was not re-checked at the pinned head. Its code reading is taken from the audit evidence, not re-read at the pinned head, names no code path with file and line, and its reproduction section states not attempted in this record.

### SOLR-16652, verdict keep-open, draft branch solr-16652-draft-open

Claiming host: none. The record source is the queue entry audit field, round audit-1 dated 2026-10-04.

Missing:
- No claim in claims/ naming this key at all, so no closeout claim with a DONE line.
- The re-check required by steps 11 and 12: record.md states JIRA was not re-read, the audit was checked against upstream main c8ab71430f not the fork pin, and it was not re-checked at the pinned head. Its code reading is taken from the audit evidence, not re-read at the pinned head, names no code path with file and line, and its reproduction section states not attempted in this record.

### SOLR-16609, verdict keep-open, draft branch solr-16609-draft-open

Claiming host: none. The record says it was checked by vm2 from an earlier B3 reading dated 2026-10-18.

Missing:
- No claim in claims/ naming this key at all, so no closeout claim with a DONE line.
- The re-check required by steps 11 and 12: record.md states JIRA was not re-read for this record; its JIRA status is taken from the earlier B3 reading.

### SOLR-16548, verdict keep-open, draft branch solr-16548-draft-open

Claiming host: none. The record says it was checked by vm2 from an earlier C reading dated 2026-10-18.

Missing:
- No claim in claims/ naming this key at all, so no closeout claim with a DONE line.
- The re-check required by steps 11 and 12: record.md states the JIRA status was not recorded in the C reading and JIRA was not re-read for this record.

### SOLR-16526, verdict keep-open, draft branch solr-16526-draft-open

Claiming host: none. The record says it was checked by vm2 from an earlier C reading dated 2026-10-18.

Missing:
- No claim in claims/ naming this key at all, so no closeout claim with a DONE line.
- The re-check required by steps 11 and 12: record.md states the JIRA status was not recorded in the C reading and JIRA was not re-read for this record.

## 2026-10-19 (third run)

Scope: the next 20 auditable closeout records in file order in material/skip-audit/queue-current.json at coordination tip 12b57bcced1fa21b8868c2d0c37d3c8b34adfb55, pinned head 498a09aea97aac86c2cc7ac114113b946d249942 (the SOLR-15848 queue record cites the abbreviated form 498a09aea9, and its record cites the same form). Result: 1 pass (SOLR-16430, a vm5 closeout with a DONE claim line recording the verdict and the draft deliverables, a draft branch containing both draft files, and a substantive record re-checked at the pinned head with file and line and three reproduction attempts), 19 suspect. Gate receipts checked this run: no gates/*.md whose first Status line says DONE names a ticket outside the tickets already checked in earlier runs, so no new gate receipt check was owed and no gate receipt suspect was found.

For every suspect below, the draft branch named in queue-current.json exists on the fork and contains drafts/<KEY>/record.md and drafts/<KEY>/jira-comment.md, the record cites the pinned head that matches queue-current.json, the word count is over 40, and the record.md text is not identical to another ticket record in this batch. The verdict is a value the rubric allows and the draft branch suffix matches the verdict, except for SOLR-15848, whose verdict evidence mismatch is listed as a missing piece. The missing pieces are only the ones listed per ticket. Note on bulk records: the jira-comment.md files are byte-identical across three groups in this batch (one shared text for SOLR-16355, SOLR-16305, SOLR-16253, SOLR-16200, SOLR-16167, SOLR-16020, SOLR-15997, SOLR-15948 and SOLR-15858; one for SOLR-16084, SOLR-16062 and SOLR-15977; one for SOLR-16306 and SOLR-16077).

### SOLR-16355, verdict keep-open, draft branch solr-16355-draft-open

Claiming host: none. The record source is the queue entry audit field, round audit-1 dated 2026-10-04.

Missing:
- No claim in claims/ naming this key at all, so no closeout claim with a DONE line.
- The re-check required by steps 11 and 12: record.md states JIRA was not re-read, the audit was checked against upstream main c8ab71430f not the fork pin, and it was not re-checked at the pinned head. Its code reading is taken from the audit evidence, not re-read at the pinned head, names no code path with file and line, and its reproduction section states not attempted in this record.

### SOLR-16306, verdict keep-open, draft branch solr-16306-draft-open

Claiming host: none. The record says it was checked by vm2 from an earlier B3 reading dated 2026-10-18.

Missing:
- No claim in claims/ naming this key at all, so no closeout claim with a DONE line.
- The re-check required by steps 11 and 12: record.md states JIRA was not re-read for this record; its JIRA status is taken from the earlier B3 reading in reports/2026-10-18-skip-repro/B3-committed-support-round-1.md.

### SOLR-16305, verdict keep-open, draft branch solr-16305-draft-open

Claiming host: none. The record source is the queue entry audit field, round audit-1 dated 2026-10-04.

Missing:
- No claim in claims/ naming this key at all, so no closeout claim with a DONE line.
- The re-check required by steps 11 and 12: record.md states JIRA was not re-read, the audit was checked against upstream main c8ab71430f not the fork pin, and it was not re-checked at the pinned head. Its code reading is taken from the audit evidence, not re-read at the pinned head, names no code path with file and line, and its reproduction section states not attempted in this record.

### SOLR-16253, verdict keep-open, draft branch solr-16253-draft-open

Claiming host: none. The record source is the queue entry audit field, round audit-1 dated 2026-10-04.

Missing:
- No claim in claims/ naming this key at all, so no closeout claim with a DONE line.
- The re-check required by steps 11 and 12: record.md states JIRA was not re-read, the audit was checked against upstream main c8ab71430f not the fork pin, and it was not re-checked at the pinned head. Its code reading is taken from the audit evidence, not re-read at the pinned head, names no code path with file and line, and its reproduction section states not attempted in this record.

### SOLR-16200, verdict keep-open, draft branch solr-16200-draft-open

Claiming host: none. The record source is the queue entry audit field, round audit-1 dated 2026-10-04.

Missing:
- No claim in claims/ naming this key at all, so no closeout claim with a DONE line.
- The re-check required by steps 11 and 12: record.md states JIRA was not re-read, the audit was checked against upstream main c8ab71430f not the fork pin, and it was not re-checked at the pinned head. Its code reading is taken from the audit evidence, not re-read at the pinned head, names no code path with file and line, and its reproduction section states not attempted in this record.

### SOLR-16167, verdict keep-open, draft branch solr-16167-draft-open

Claiming host: none. The record source is the queue entry audit field, round audit-1 dated 2026-10-04.

Missing:
- No claim in claims/ naming this key at all, so no closeout claim with a DONE line.
- The re-check required by steps 11 and 12: record.md states JIRA was not re-read, the audit was checked against upstream main c8ab71430f not the fork pin, and it was not re-checked at the pinned head. Its code reading is taken from the audit evidence, not re-read at the pinned head, names no code path with file and line, and its reproduction section states not attempted in this record.

### SOLR-16122, verdict keep-open, draft branch solr-16122-draft-open

Claiming host: none. The record source is the earlier A3 report dated 2026-10-18.

Missing:
- No claim in claims/ naming this key at all, so no closeout claim with a DONE line.
- The re-check required by steps 11 and 12: record.md states JIRA was not re-read and records no JIRA status or date, its code reading was not read for this record, and the runs were not repeated. The record does state the A3 run command, seed and result; the missing piece is the re-check itself, not a reproduction statement.

### SOLR-16094, verdict keep-open, draft branch solr-16094-draft-open

Claiming host: none. The only file in claims/ naming this key is a vm2 repro round claim, and the record source is the earlier A1 report dated 2026-10-18.

Missing:
- No skip closeout claim in claims/: no claims/skip-SOLR-16094-<host>.md and no closeout claim with a DONE line. The only claims/ file naming this key is claims/pool-skip-repro-flake-round-1.md, a repro round claim whose DONE line delivers the A1 report, not a closeout claim.
- The re-check required by steps 11 and 12: record.md states JIRA was not re-read and records no JIRA status or date, its code reading was not read for this record, and the runs were not repeated. The record does state the A1 run command, seed and result; the missing piece is the re-check itself, not a reproduction statement.

### SOLR-16084, verdict keep-open, draft branch solr-16084-draft-open

Claiming host: none. The record says it was checked by vm2 from an earlier C reading dated 2026-10-18.

Missing:
- No claim in claims/ naming this key at all, so no closeout claim with a DONE line.
- The re-check required by steps 11 and 12: record.md states the JIRA status was not recorded in the C reading and JIRA was not re-read for this record.

### SOLR-16077, verdict keep-open, draft branch solr-16077-draft-open

Claiming host: none. The record says it was checked by vm2 from an earlier B3 reading dated 2026-10-18.

Missing:
- No claim in claims/ naming this key at all, so no closeout claim with a DONE line.
- The re-check required by steps 11 and 12: record.md states JIRA was not re-read for this record; its JIRA status is taken from the earlier B3 reading in reports/2026-10-18-skip-repro/B3-committed-support-round-1.md.

### SOLR-16062, verdict keep-open, draft branch solr-16062-draft-open

Claiming host: none. The record says it was checked by vm2 from an earlier C reading dated 2026-10-18.

Missing:
- No claim in claims/ naming this key at all, so no closeout claim with a DONE line.
- The re-check required by steps 11 and 12: record.md states the JIRA status was not recorded in the C reading and JIRA was not re-read for this record.
- A code path with file and line, or a reproduction attempt with a result: record.md states no code path is named (LTRRescorer.java is named without a line), and its reproduction section states only Not attempted, with no attempt and no result.

### SOLR-16020, verdict keep-open, draft branch solr-16020-draft-open

Claiming host: none. The record source is the queue entry audit field, round audit-1 dated 2026-10-04.

Missing:
- No claim in claims/ naming this key at all, so no closeout claim with a DONE line.
- The re-check required by steps 11 and 12: record.md states JIRA was not re-read, the audit was checked against upstream main c8ab71430f not the fork pin, and it was not re-checked at the pinned head. Its code reading is taken from the audit evidence, not re-read at the pinned head, names no code path with file and line, and its reproduction section states not attempted in this record.

### SOLR-15997, verdict keep-open, draft branch solr-15997-draft-open

Claiming host: none. The record source is the queue entry audit field, round audit-1 dated 2026-10-04.

Missing:
- No claim in claims/ naming this key at all, so no closeout claim with a DONE line.
- The re-check required by steps 11 and 12: record.md states JIRA was not re-read, the audit was checked against upstream main c8ab71430f not the fork pin, and it was not re-checked at the pinned head. Its code reading is taken from the audit evidence, not re-read at the pinned head, names no code path with file and line, and its reproduction section states not attempted in this record.

### SOLR-15977, verdict keep-open, draft branch solr-15977-draft-open

Claiming host: none. The record says it was checked by vm2 from an earlier C reading dated 2026-10-18.

Missing:
- No claim in claims/ naming this key at all, so no closeout claim with a DONE line.
- The re-check required by steps 11 and 12: record.md states the JIRA status was not recorded in the C reading and JIRA was not re-read for this record.
- A code path with file and line, or a reproduction attempt with a result: record.md names no file and line in its code reading, and its reproduction section states only Not attempted, with no attempt and no result.

### SOLR-15970, verdict close-fixed, draft branch solr-15970-draft-close

Claiming host: none. The record says it was checked by vm2 from an earlier B3 reading dated 2026-10-18.

Missing:
- No claim in claims/ naming this key at all, so no closeout claim with a DONE line.
- The re-check required by steps 11 and 12: record.md states JIRA was not re-read for this record; its JIRA status is taken from the earlier B3 reading in reports/2026-10-18-skip-repro/B3-committed-support-round-1.md.

### SOLR-15948, verdict keep-open, draft branch solr-15948-draft-open

Claiming host: none. The record source is the queue entry audit field, round audit-1 dated 2026-10-04.

Missing:
- No claim in claims/ naming this key at all, so no closeout claim with a DONE line.
- The re-check required by steps 11 and 12: record.md states JIRA was not re-read, the audit was checked against upstream main c8ab71430f not the fork pin, and it was not re-checked at the pinned head. Its code reading is taken from the audit evidence, not re-read at the pinned head, names no code path with file and line, and its reproduction section states not attempted in this record.

### SOLR-15879, verdict close-obsolete, draft branch solr-15879-draft-close

Claiming host: none. The record says it was checked by vm2 from an earlier B2 reading dated 2026-10-18.

Missing:
- No claim in claims/ naming this key at all, so no closeout claim with a DONE line.
- The re-check required by steps 11 and 12: record.md states the JIRA status was not recorded in the B2 reading and JIRA was not re-read for this record.

### SOLR-15858, verdict keep-open, draft branch solr-15858-draft-open

Claiming host: none. The record source is the queue entry audit field, round audit-3 dated 2026-10-06.

Missing:
- No claim in claims/ naming this key at all, so no closeout claim with a DONE line.
- The re-check required by steps 11 and 12: record.md states JIRA was not re-read, the audit was checked against upstream main c8ab71430f not the fork pin, and it was not re-checked at the pinned head. Its code reading is taken from the audit evidence, not re-read at the pinned head, names no code path with file and line, and its reproduction section states not attempted in this record.

### SOLR-15848, verdict close-fixed, draft branch solr-15848-draft-close

Claiming host: none. The record says it was checked by vm5 on 2026-10-18.

Missing:
- No claim in claims/ naming this key at all, so no closeout claim with a DONE line.
- Backing for the close-fixed verdict: close-fixed needs a fixing commit whose changed code is still present at the pinned head, but record.md cites removal commit cc0c111949 (SOLR-14656) and an empty grep for ScheduledTriggerIntegrationTest, and its gap analysis states the rubric's close-obsolete fits this case. No fixing commit is named.

## 2026-10-19 (fourth run)

Scope: the next 20 auditable closeout records in file order in material/skip-audit/queue-current.json at coordination tip e8802de49c8a30ae74faf58f8f0e1560ba70a6ca, pinned head 498a09aea97aac86c2cc7ac114113b946d249942. Result: 1 pass (SOLR-15823, a vm5 closeout with a DONE claim line recording the verdict and the draft deliverables, a draft branch containing both draft files, and a substantive record re-checked at the pinned head with file and line and a code path argument for pr-needed), 19 suspect. Gate receipts checked this run: no gates/*.md whose first Status line says DONE names a ticket outside the tickets already checked in earlier runs, so no new gate receipt check was owed and no gate receipt suspect was found.

For every suspect below, the draft branch named in queue-current.json exists on the fork and contains drafts/<KEY>/record.md and drafts/<KEY>/jira-comment.md, the record cites the pinned head that matches queue-current.json, the word count is over 40, and the record.md text is not identical to another ticket record in this batch. The verdict is a value the rubric allows and the draft branch suffix matches the verdict. The missing pieces are only the ones listed per ticket. Note on bulk records: the jira-comment.md files are byte-identical across four groups in this batch (one shared text for SOLR-15388, SOLR-15521, SOLR-15522, SOLR-15540, SOLR-15544, SOLR-15585, SOLR-15586 and SOLR-15672; one for SOLR-15371, SOLR-15408, SOLR-15679 and SOLR-15727; one for SOLR-15420, SOLR-15443, SOLR-15481 and SOLR-15571; one for SOLR-15690 and SOLR-15704).

### SOLR-15801, verdict close-answered, draft branch solr-15801-draft-close

Claiming host: none. The record says it was checked by vm2 from an earlier B3 reading dated 2026-10-18.

Missing:
- No claim in claims/ naming this key at all, so no closeout claim with a DONE line.
- The re-check required by steps 11 and 12: record.md states JIRA was not re-read for this record; its JIRA status is taken from the earlier B3 reading.

### SOLR-15727, verdict keep-open, draft branch solr-15727-draft-open

Claiming host: none. The record says it was checked by vm2 from an earlier C reading dated 2026-10-18.

Missing:
- No claim in claims/ naming this key at all, so no closeout claim with a DONE line.
- The re-check required by steps 11 and 12: record.md states the JIRA status was not recorded in the C reading and JIRA was not re-read for this record.

### SOLR-15704, verdict close-obsolete, draft branch solr-15704-draft-close

Claiming host: none. The record says it was checked by vm2 from an earlier B2 reading dated 2026-10-18.

Missing:
- No claim in claims/ naming this key at all, so no closeout claim with a DONE line.
- The re-check required by steps 11 and 12: record.md states the JIRA status was not recorded in the B2 reading and JIRA was not re-read for this record.
- A code path with file and line, or a reproduction attempt with a result: record.md names no file and line, its code reading states only Same as SOLR-15690, and its reproduction section states only Not attempted, with no attempt and no result.

### SOLR-15690, verdict close-obsolete, draft branch solr-15690-draft-close

Claiming host: none. The record says it was checked by vm2 from an earlier B2 reading dated 2026-10-18.

Missing:
- No claim in claims/ naming this key at all, so no closeout claim with a DONE line.
- The re-check required by steps 11 and 12: record.md states the JIRA status was not recorded in the B2 reading and JIRA was not re-read for this record.

### SOLR-15679, verdict keep-open, draft branch solr-15679-draft-open

Claiming host: none. The record says it was checked by vm2 from an earlier C reading dated 2026-10-18.

Missing:
- No claim in claims/ naming this key at all, so no closeout claim with a DONE line.
- The re-check required by steps 11 and 12: record.md states the JIRA status was not recorded in the C reading and JIRA was not re-read for this record.

### SOLR-15672, verdict keep-open, draft branch solr-15672-draft-open

Claiming host: none. The record source is the queue entry audit field, round audit-1 dated 2026-10-04.

Missing:
- No claim in claims/ naming this key at all, so no closeout claim with a DONE line.
- The re-check required by steps 11 and 12: record.md states JIRA was not re-read, the audit was checked against upstream main c8ab71430f not the fork pin, and it was not re-checked at the pinned head. Its code reading is taken from the audit evidence, not re-read at the pinned head, names no code path with file and line, and its reproduction section states not attempted in this record.

### SOLR-15586, verdict keep-open, draft branch solr-15586-draft-open

Claiming host: none. The record source is the queue entry audit field, round audit-1 dated 2026-10-04.

Missing:
- No claim in claims/ naming this key at all, so no closeout claim with a DONE line.
- The re-check required by steps 11 and 12: record.md states JIRA was not re-read, the audit was checked against upstream main c8ab71430f not the fork pin, and it was not re-checked at the pinned head. Its code reading is taken from the audit evidence, not re-read at the pinned head, names no code path with file and line, and its reproduction section states not attempted in this record.

### SOLR-15585, verdict keep-open, draft branch solr-15585-draft-open

Claiming host: none. The record source is the queue entry audit field, round audit-1 dated 2026-10-04.

Missing:
- No claim in claims/ naming this key at all, so no closeout claim with a DONE line.
- The re-check required by steps 11 and 12: record.md states JIRA was not re-read, the audit was checked against upstream main c8ab71430f not the fork pin, and it was not re-checked at the pinned head. Its code reading is taken from the audit evidence, not re-read at the pinned head, names no code path with file and line, and its reproduction section states not attempted in this record.

### SOLR-15571, verdict keep-open, draft branch solr-15571-draft-open

Claiming host: none. The record says it was checked by vm2 from an earlier C reading dated 2026-10-18.

Missing:
- No claim in claims/ naming this key at all, so no closeout claim with a DONE line.
- The re-check required by steps 11 and 12: record.md states the JIRA status was not recorded in the C reading and JIRA was not re-read for this record.

### SOLR-15544, verdict keep-open, draft branch solr-15544-draft-open

Claiming host: none. The record source is the queue entry audit field, round audit-1 dated 2026-10-04.

Missing:
- No claim in claims/ naming this key at all, so no closeout claim with a DONE line.
- The re-check required by steps 11 and 12: record.md states JIRA was not re-read, the audit was checked against upstream main c8ab71430f not the fork pin, and it was not re-checked at the pinned head. Its code reading is taken from the audit evidence, not re-read at the pinned head, names no code path with file and line, and its reproduction section states not attempted in this record.

### SOLR-15540, verdict keep-open, draft branch solr-15540-draft-open

Claiming host: none. The record source is the queue entry audit field, round audit-1 dated 2026-10-04.

Missing:
- No claim in claims/ naming this key at all, so no closeout claim with a DONE line.
- The re-check required by steps 11 and 12: record.md states JIRA was not re-read, the audit was checked against upstream main c8ab71430f not the fork pin, and it was not re-checked at the pinned head. Its code reading is taken from the audit evidence, not re-read at the pinned head, names no code path with file and line, and its reproduction section states not attempted in this record.

### SOLR-15522, verdict keep-open, draft branch solr-15522-draft-open

Claiming host: none. The record source is the queue entry audit field, round audit-1 dated 2026-10-04.

Missing:
- No claim in claims/ naming this key at all, so no closeout claim with a DONE line.
- The re-check required by steps 11 and 12: record.md states JIRA was not re-read, the audit was checked against upstream main c8ab71430f not the fork pin, and it was not re-checked at the pinned head. Its code reading is taken from the audit evidence, not re-read at the pinned head, names no code path with file and line, and its reproduction section states not attempted in this record.

### SOLR-15521, verdict keep-open, draft branch solr-15521-draft-open

Claiming host: none. The record source is the queue entry audit field, round audit-1 dated 2026-10-04.

Missing:
- No claim in claims/ naming this key at all, so no closeout claim with a DONE line.
- The re-check required by steps 11 and 12: record.md states JIRA was not re-read, the audit was checked against upstream main c8ab71430f not the fork pin, and it was not re-checked at the pinned head. Its code reading is taken from the audit evidence, not re-read at the pinned head, names no code path with file and line, and its reproduction section states not attempted in this record.

### SOLR-15481, verdict keep-open, draft branch solr-15481-draft-open

Claiming host: none. The record says it was checked by vm2 from an earlier C reading dated 2026-10-18.

Missing:
- No claim in claims/ naming this key at all, so no closeout claim with a DONE line.
- The re-check required by steps 11 and 12: record.md states the JIRA status was not recorded in the C reading and JIRA was not re-read for this record.

### SOLR-15443, verdict keep-open, draft branch solr-15443-draft-open

Claiming host: none. The record says it was checked by vm2 from an earlier C reading dated 2026-10-18.

Missing:
- No claim in claims/ naming this key at all, so no closeout claim with a DONE line.
- The re-check required by steps 11 and 12: record.md states the JIRA status was not recorded in the C reading and JIRA was not re-read for this record.

### SOLR-15420, verdict keep-open, draft branch solr-15420-draft-open

Claiming host: none. The record says it was checked by vm2 from an earlier C reading dated 2026-10-18.

Missing:
- No claim in claims/ naming this key at all, so no closeout claim with a DONE line.
- The re-check required by steps 11 and 12: record.md states the JIRA status was not recorded in the C reading and JIRA was not re-read for this record.
- A code path with file and line, or a reproduction attempt with a result: record.md names no file and line in its code reading, which states the failing code is in Lucene and cannot be checked in this repo, and its reproduction section states only Not attempted, with no attempt and no result.

### SOLR-15408, verdict keep-open, draft branch solr-15408-draft-open

Claiming host: none. The record says it was checked by vm2 from an earlier B3 reading dated 2026-10-18.

Missing:
- No claim in claims/ naming this key at all, so no closeout claim with a DONE line.
- The re-check required by steps 11 and 12: record.md states JIRA was not re-read for this record; its JIRA status is taken from the earlier B3 reading.

### SOLR-15388, verdict keep-open, draft branch solr-15388-draft-open

Claiming host: none. The record source is the queue entry audit field, round audit-1 dated 2026-10-04.

Missing:
- No claim in claims/ naming this key at all, so no closeout claim with a DONE line.
- The re-check required by steps 11 and 12: record.md states JIRA was not re-read, the audit was checked against upstream main c8ab71430f not the fork pin, and it was not re-checked at the pinned head. Its code reading is taken from the audit evidence, not re-read at the pinned head, names no code path with file and line, and its reproduction section states not attempted in this record.

### SOLR-15371, verdict keep-open, draft branch solr-15371-draft-open

Claiming host: none. The record says it was checked by vm2 from an earlier C reading dated 2026-10-18.

Missing:
- No claim in claims/ naming this key at all, so no closeout claim with a DONE line.
- The re-check required by steps 11 and 12: record.md states the JIRA status was not recorded in the C reading and JIRA was not re-read for this record.

## 2026-10-19 (fifth run)

Scope: the next 20 auditable closeout records in file order in material/skip-audit/queue-current.json at coordination tip e43e7c5033a5d67e86348b7c0ea0807c60cb8c81, pinned head 498a09aea97aac86c2cc7ac114113b946d249942. Result: 0 pass, 20 suspect. Gate receipts checked this run: one new DONE gate ticket, SOLR-18458; receipts/SOLR-18458.md exists and names gated head 344485485fb8e1706a1e04c02e373d8a97bcd1b8, the same head the gate file Status line records (344485485f), so it passes and no gate receipt suspect was found.

For every suspect below, the draft branch named in queue-current.json exists on the fork and contains drafts/<KEY>/record.md and drafts/<KEY>/jira-comment.md, the record cites the pinned head that matches queue-current.json, the word count is over 40, and the record.md text is not identical to another ticket record in this batch. The verdict is a value the rubric allows and the draft branch suffix matches the verdict. The missing pieces are only the ones listed per ticket. Note on bulk records: the jira-comment.md files are byte-identical across five groups in this batch (one shared text for SOLR-15350, SOLR-15310 and SOLR-15297; one for SOLR-15330, SOLR-15305, SOLR-15304, SOLR-15151, SOLR-15028, SOLR-15026, SOLR-15023 and SOLR-14861; one for SOLR-15279, SOLR-15237 and SOLR-15234; one for SOLR-15226, SOLR-15037 and SOLR-14756; one for SOLR-14881 and SOLR-14784).

### SOLR-15350, verdict close-obsolete, draft branch solr-15350-draft-close

Claiming host: none. The record says it was checked by vm2 from an earlier B2 reading dated 2026-10-18.

Missing:
- No claim in claims/ naming this key at all, so no closeout claim with a DONE line.
- The re-check required by steps 11 and 12: record.md states the JIRA status was not recorded in the B2 reading and JIRA was not re-read for this record.
- A code path with file and line, or a reproduction attempt with a result: record.md names no file and line in its code reading, and its reproduction section states only Not attempted, with no attempt and no result. Its verdict justification states the grep result is taken from the B2 reading and was not re-run in this record.

### SOLR-15330, verdict keep-open, draft branch solr-15330-draft-open

Claiming host: none. The record source is the queue entry audit field, round audit-1 dated 2026-10-04.

Missing:
- No claim in claims/ naming this key at all, so no closeout claim with a DONE line.
- The re-check required by steps 11 and 12: record.md states JIRA was not re-read, the audit was checked against upstream main c8ab71430f not the fork pin, and it was not re-checked at the pinned head. Its code reading is taken from the audit evidence, not re-read at the pinned head, names no code path with file and line, and its reproduction section states not attempted in this record.

### SOLR-15310, verdict close-obsolete, draft branch solr-15310-draft-close

Claiming host: none. The record says it was checked by vm2 from an earlier B2 reading dated 2026-10-18.

Missing:
- No claim in claims/ naming this key at all, so no closeout claim with a DONE line.
- The re-check required by steps 11 and 12: record.md states the JIRA status was not recorded in the B2 reading and JIRA was not re-read for this record.

### SOLR-15305, verdict keep-open, draft branch solr-15305-draft-open

Claiming host: none. The record source is the queue entry audit field, round audit-2 dated 2026-10-04.

Missing:
- No claim in claims/ naming this key at all, so no closeout claim with a DONE line.
- The re-check required by steps 11 and 12: record.md states JIRA was not re-read, the audit was checked against upstream main c8ab71430f not the fork pin, and it was not re-checked at the pinned head. Its code reading is taken from the audit evidence, not re-read at the pinned head, names no code path with file and line, and its reproduction section states not attempted in this record.

### SOLR-15304, verdict keep-open, draft branch solr-15304-draft-open

Claiming host: none. The record source is the queue entry audit field, round audit-1 dated 2026-10-04.

Missing:
- No claim in claims/ naming this key at all, so no closeout claim with a DONE line.
- The re-check required by steps 11 and 12: record.md states JIRA was not re-read, the audit was checked against upstream main c8ab71430f not the fork pin, and it was not re-checked at the pinned head. Its code reading is taken from the audit evidence, not re-read at the pinned head, names no code path with file and line, and its reproduction section states not attempted in this record.

### SOLR-15297, verdict close-obsolete, draft branch solr-15297-draft-close

Claiming host: none. The record says it was checked by vm2 from an earlier B2 reading dated 2026-10-18.

Missing:
- No claim in claims/ naming this key at all, so no closeout claim with a DONE line.
- The re-check required by steps 11 and 12: record.md states the JIRA status was not recorded in the B2 reading and JIRA was not re-read for this record.
- A code path with file and line, or a reproduction attempt with a result: record.md names no file and line in its code reading, and its reproduction section states only Not attempted, with no attempt and no result. Its verdict justification states the grep result is taken from the B2 reading and was not re-run in this record.

### SOLR-15279, verdict keep-open, draft branch solr-15279-draft-open

Claiming host: none. The record says it was checked by vm2 from an earlier C reading dated 2026-10-18.

Missing:
- No claim in claims/ naming this key at all, so no closeout claim with a DONE line.
- The re-check required by steps 11 and 12: record.md states the JIRA status was not recorded in the C reading and JIRA was not re-read for this record.

### SOLR-15237, verdict keep-open, draft branch solr-15237-draft-open

Claiming host: none. The record says it was checked by vm2 from an earlier C reading dated 2026-10-18.

Missing:
- No claim in claims/ naming this key at all, so no closeout claim with a DONE line.
- The re-check required by steps 11 and 12: record.md states the JIRA status was not recorded in the C reading and JIRA was not re-read for this record.

### SOLR-15234, verdict keep-open, draft branch solr-15234-draft-open

Claiming host: none. The record says it was checked by vm2 from an earlier C reading dated 2026-10-18.

Missing:
- No claim in claims/ naming this key at all, so no closeout claim with a DONE line.
- The re-check required by steps 11 and 12: record.md states the JIRA status was not recorded in the C reading and JIRA was not re-read for this record.

### SOLR-15226, verdict keep-open, draft branch solr-15226-draft-open

Claiming host: none. The record says it was checked by vm2 from an earlier B3 reading dated 2026-10-18.

Missing:
- No claim in claims/ naming this key at all, so no closeout claim with a DONE line.
- The re-check required by steps 11 and 12: record.md states JIRA was not re-read for this record; its JIRA status is taken from the earlier B3 reading.

### SOLR-15151, verdict keep-open, draft branch solr-15151-draft-open

Claiming host: none. The record source is the queue entry audit field, round audit-1 dated 2026-10-04.

Missing:
- No claim in claims/ naming this key at all, so no closeout claim with a DONE line.
- The re-check required by steps 11 and 12: record.md states JIRA was not re-read, the audit was checked against upstream main c8ab71430f not the fork pin, and it was not re-checked at the pinned head. Its code reading is taken from the audit evidence, not re-read at the pinned head, and its reproduction section states not attempted in this record.

### SOLR-15037, verdict keep-open, draft branch solr-15037-draft-open

Claiming host: none. The record says it was checked by vm2 from an earlier C reading dated 2026-10-18.

Missing:
- No claim in claims/ naming this key at all, so no closeout claim with a DONE line.
- The re-check required by steps 11 and 12: record.md states the JIRA status was not recorded in the C reading and JIRA was not re-read for this record.

### SOLR-15028, verdict keep-open, draft branch solr-15028-draft-open

Claiming host: none. The record source is the queue entry audit field, round audit-1 dated 2026-10-04.

Missing:
- No claim in claims/ naming this key at all, so no closeout claim with a DONE line.
- The re-check required by steps 11 and 12: record.md states JIRA was not re-read, the audit was checked against upstream main c8ab71430f not the fork pin, and it was not re-checked at the pinned head. Its code reading is taken from the audit evidence, not re-read at the pinned head, names no code path with file and line, and its reproduction section states not attempted in this record.

### SOLR-15026, verdict keep-open, draft branch solr-15026-draft-open

Claiming host: none. The record source is the queue entry audit field, round audit-1 dated 2026-10-04.

Missing:
- No claim in claims/ naming this key at all, so no closeout claim with a DONE line.
- The re-check required by steps 11 and 12: record.md states JIRA was not re-read, the audit was checked against upstream main c8ab71430f not the fork pin, and it was not re-checked at the pinned head. Its code reading is taken from the audit evidence, not re-read at the pinned head, names no code path with file and line, and its reproduction section states not attempted in this record.

### SOLR-15023, verdict keep-open, draft branch solr-15023-draft-open

Claiming host: none. The record source is the queue entry audit field, round audit-1 dated 2026-10-04.

Missing:
- No claim in claims/ naming this key at all, so no closeout claim with a DONE line.
- The re-check required by steps 11 and 12: record.md states JIRA was not re-read, the audit was checked against upstream main c8ab71430f not the fork pin, and it was not re-checked at the pinned head. Its code reading is taken from the audit evidence, not re-read at the pinned head, names no code path with file and line, and its reproduction section states not attempted in this record.

### SOLR-14966, verdict close-fixed, draft branch solr-14966-draft-close

Claiming host: none. The record says it was checked by vm2 from an earlier C reading dated 2026-10-18.

Missing:
- No claim in claims/ naming this key at all, so no closeout claim with a DONE line.
- The re-check required by steps 11 and 12: record.md states the JIRA status was not recorded in the C reading and JIRA was not re-read for this record.

### SOLR-14881, verdict keep-open, draft branch solr-14881-draft-open

Claiming host: none for the closeout. The record source is the A1 flake round 1 report dated 2026-10-18; the runs were not repeated.

Missing:
- No skip closeout claim in claims/: no claims/skip-SOLR-14881-<host>.md and no closeout claim with a DONE line. The only claims/ file naming this key is claims/pool-skip-repro-flake-round-1.md (vm2), a repro round claim whose DONE line delivers the A1 report, not a closeout.
- The re-check required by steps 11 and 12: record.md states JIRA was not re-read for this record and records no JIRA status or date. Its code reading was not read for this record and the A1 runs were not repeated. The record does state the A1 run command, seed and result, so the missing piece is the re-check itself, not a reproduction statement.

### SOLR-14861, verdict keep-open, draft branch solr-14861-draft-open

Claiming host: none. The record source is the queue entry audit field, round audit-1 dated 2026-10-04.

Missing:
- No claim in claims/ naming this key at all, so no closeout claim with a DONE line.
- The re-check required by steps 11 and 12: record.md states JIRA was not re-read, the audit was checked against upstream main c8ab71430f not the fork pin, and it was not re-checked at the pinned head. Its code reading is taken from the audit evidence, not re-read at the pinned head, names no code path with file and line, and its reproduction section states not attempted in this record.

### SOLR-14784, verdict keep-open, draft branch solr-14784-draft-open

Claiming host: none. The record source is the A3 other round 1 report dated 2026-10-18; the runs were not repeated.

Missing:
- No claim in claims/ naming this key at all, so no closeout claim with a DONE line.
- The re-check required by steps 11 and 12: record.md states JIRA was not re-read for this record, its code reading was not read for this record, and its source is the earlier A3 report with the runs not repeated; no re-check at the pinned head is recorded. The record does state the A3 run command, seed and result.

### SOLR-14756, verdict keep-open, draft branch solr-14756-draft-open

Claiming host: none. The record says it was checked by vm2 from an earlier C reading dated 2026-10-18.

Missing:
- No claim in claims/ naming this key at all, so no closeout claim with a DONE line.
- The re-check required by steps 11 and 12: record.md states the JIRA status was not recorded in the C reading and JIRA was not re-read for this record.

## 2026-10-19 (sixth run)

Scope: the next 20 auditable closeout records in file order in material/skip-audit/queue-current.json at coordination tip f8e7b814dc4940e6f314a75aaed57affde855b0c, pinned head 498a09aea97aac86c2cc7ac114113b946d249942. Result: 1 pass, 19 suspect. The pass is SOLR-14159 (claim claims/skip-SOLR-14159-vm5.md with a DONE line, JIRA status and date recorded, code reading and history at the pinned head, missing piece named). Gate receipts: no new DONE gate tickets this run; every gates file whose Status line says DONE names a ticket already checked in an earlier run, so no gate receipt check was due and no gate receipt suspect was found.

For every suspect below, the draft branch named in queue-current.json exists on the fork and contains drafts/<KEY>/record.md and drafts/<KEY>/jira-comment.md, the record cites the pinned head that matches queue-current.json, the word count is over 40, and the record.md text is not identical to another ticket record in this batch. The verdict is a value the rubric allows and the draft branch suffix matches the verdict. The missing pieces are only the ones listed per ticket. Note on shared text: the jira-comment.md files are byte-identical across 17 tickets in this batch (SOLR-14668, SOLR-14648, SOLR-14627, SOLR-14612, SOLR-14601, SOLR-14580, SOLR-14530, SOLR-14493, SOLR-14487, SOLR-14434, SOLR-14427, SOLR-14282, SOLR-14278, SOLR-14226, SOLR-14201, SOLR-14183 and SOLR-14180 share one generic keep-open text).

### SOLR-14668, verdict keep-open, draft branch solr-14668-draft-open

Claiming host: none. The record source is the queue entry audit field, round audit-1 dated 2026-10-04.

Missing:
- No claim in claims/ naming this key at all, so no closeout claim with a DONE line.
- The re-check required by steps 11 and 12: record.md states JIRA was not re-read, the audit was checked against upstream main c8ab71430f not the fork pin, and it was not re-checked at the pinned head. Its code reading is taken from the audit evidence, not re-read at the pinned head, names no code path with file and line, and its reproduction section states not attempted in this record.

### SOLR-14648, verdict keep-open, draft branch solr-14648-draft-open

Claiming host: none. The record source is the queue entry audit field, round audit-1 dated 2026-10-04.

Missing:
- No claim in claims/ naming this key at all, so no closeout claim with a DONE line.
- The re-check required by steps 11 and 12: record.md states JIRA was not re-read, the audit was checked against upstream main c8ab71430f not the fork pin, and it was not re-checked at the pinned head. Its code reading is taken from the audit evidence, not re-read at the pinned head, names no code path with file and line, and its reproduction section states not attempted in this record.

### SOLR-14627, verdict keep-open, draft branch solr-14627-draft-open

Claiming host: none. The record source is the queue entry audit field, round audit-1 dated 2026-10-04.

Missing:
- No claim in claims/ naming this key at all, so no closeout claim with a DONE line.
- The re-check required by steps 11 and 12: record.md states JIRA was not re-read, the audit was checked against upstream main c8ab71430f not the fork pin, and it was not re-checked at the pinned head. Its code reading is taken from the audit evidence, not re-read at the pinned head, names no code path with file and line, and its reproduction section states not attempted in this record.

### SOLR-14612, verdict keep-open, draft branch solr-14612-draft-open

Claiming host: none. The record source is the queue entry audit field, round audit-3 dated 2026-10-06.

Missing:
- No claim in claims/ naming this key at all, so no closeout claim with a DONE line.
- The re-check required by steps 11 and 12: record.md states JIRA was not re-read, the audit was checked against upstream main c8ab71430f not the fork pin, and it was not re-checked at the pinned head. Its code reading is taken from the audit evidence, not re-read at the pinned head, names no code path with file and line, and its reproduction section states not attempted in this record.

### SOLR-14601, verdict keep-open, draft branch solr-14601-draft-open

Claiming host: none. The record source is the queue entry audit field, round audit-1 dated 2026-10-04.

Missing:
- No claim in claims/ naming this key at all, so no closeout claim with a DONE line.
- The re-check required by steps 11 and 12: record.md states JIRA was not re-read, the audit was checked against upstream main c8ab71430f not the fork pin, and it was not re-checked at the pinned head. Its code reading is taken from the audit evidence, not re-read at the pinned head, names no code path with file and line, and its reproduction section states not attempted in this record.

### SOLR-14580, verdict keep-open, draft branch solr-14580-draft-open

Claiming host: none. The record source is the queue entry audit field, round audit-3 dated 2026-10-06.

Missing:
- No claim in claims/ naming this key at all, so no closeout claim with a DONE line.
- The re-check required by steps 11 and 12: record.md states JIRA was not re-read, the audit was checked against upstream main c8ab71430f not the fork pin, and it was not re-checked at the pinned head. Its code reading is taken from the audit evidence, not re-read at the pinned head, names no code path with file and line, and its reproduction section states not attempted in this record.

### SOLR-14530, verdict keep-open, draft branch solr-14530-draft-open

Claiming host: none. The record source is the queue entry audit field, round audit-1 dated 2026-10-04.

Missing:
- No claim in claims/ naming this key at all, so no closeout claim with a DONE line.
- The re-check required by steps 11 and 12: record.md states JIRA was not re-read, the audit was checked against upstream main c8ab71430f not the fork pin, and it was not re-checked at the pinned head. Its code reading is taken from the audit evidence, not re-read at the pinned head, names no code path with file and line, and its reproduction section states not attempted in this record.

### SOLR-14493, verdict keep-open, draft branch solr-14493-draft-open

Claiming host: none. The record source is the queue entry audit field, round audit-3 dated 2026-10-06.

Missing:
- No claim in claims/ naming this key at all, so no closeout claim with a DONE line.
- The re-check required by steps 11 and 12: record.md states JIRA was not re-read, the audit was checked against upstream main c8ab71430f not the fork pin, and it was not re-checked at the pinned head. Its code reading is taken from the audit evidence, not re-read at the pinned head, names no code path with file and line, and its reproduction section states not attempted in this record.

### SOLR-14487, verdict keep-open, draft branch solr-14487-draft-open

Claiming host: none. The record source is the queue entry audit field, round audit-3 dated 2026-10-06.

Missing:
- No claim in claims/ naming this key at all, so no closeout claim with a DONE line.
- The re-check required by steps 11 and 12: record.md states JIRA was not re-read, the audit was checked against upstream main c8ab71430f not the fork pin, and it was not re-checked at the pinned head. Its code reading is taken from the audit evidence, not re-read at the pinned head, names no code path with file and line, and its reproduction section states not attempted in this record.

### SOLR-14434, verdict keep-open, draft branch solr-14434-draft-open

Claiming host: none. The record source is the queue entry audit field, round audit-1 dated 2026-10-04.

Missing:
- No claim in claims/ naming this key at all, so no closeout claim with a DONE line.
- The re-check required by steps 11 and 12: record.md states JIRA was not re-read, the audit was checked against upstream main c8ab71430f not the fork pin, and it was not re-checked at the pinned head. Its code reading is taken from the audit evidence, not re-read at the pinned head, names no code path with file and line, and its reproduction section states not attempted in this record.

### SOLR-14427, verdict keep-open, draft branch solr-14427-draft-open

Claiming host: none. The record source is the queue entry audit field, round audit-3 dated 2026-10-05.

Missing:
- No claim in claims/ naming this key at all, so no closeout claim with a DONE line.
- The re-check required by steps 11 and 12: record.md states JIRA was not re-read, the audit was checked against upstream main c8ab71430f not the fork pin, and it was not re-checked at the pinned head. Its code reading is taken from the audit evidence, not re-read at the pinned head, and its reproduction section states not attempted in this record. The audit evidence itself cites line numbers (versionAdd and doVersionAdd), but no re-read at the pinned head is recorded.

### SOLR-14288, verdict keep-open, draft branch solr-14288-draft-open

Claiming host: none. The record says it was checked by vm2 from the C reading dated 2026-10-19 UTC (reports/2026-10-18-skip-repro/C4, slice 4).

Missing:
- No claim in claims/ naming this key at all, so no closeout claim with a DONE line.
- The re-check required by steps 11 and 12: record.md states the JIRA status was not recorded in the C reading and JIRA was not re-read for this record.
- A code path with file and line, or a reproduction attempt with a result: record.md names no code path (the ticket gives no query, schema or version, which the record notes) and its reproduction section states not attempted, so no re-check at the pinned head is recorded.

### SOLR-14282, verdict keep-open, draft branch solr-14282-draft-open

Claiming host: none. The record source is the queue entry audit field, round audit-1 dated 2026-10-04.

Missing:
- No claim in claims/ naming this key at all, so no closeout claim with a DONE line.
- The re-check required by steps 11 and 12: record.md states JIRA was not re-read, the audit was checked against upstream main c8ab71430f not the fork pin, and it was not re-checked at the pinned head. Its code reading is taken from the audit evidence, not re-read at the pinned head, names no code path with file and line, and its reproduction section states not attempted in this record.

### SOLR-14278, verdict keep-open, draft branch solr-14278-draft-open

Claiming host: none. The record source is the queue entry audit field, round audit-1 dated 2026-10-04.

Missing:
- No claim in claims/ naming this key at all, so no closeout claim with a DONE line.
- The re-check required by steps 11 and 12: record.md states JIRA was not re-read, the audit was checked against upstream main c8ab71430f not the fork pin, and it was not re-checked at the pinned head. Its code reading is taken from the audit evidence, not re-read at the pinned head, names no code path with file and line, and its reproduction section states not attempted in this record.

### SOLR-14226, verdict keep-open, draft branch solr-14226-draft-open

Claiming host: none. The record source is the queue entry audit field, round audit-1 dated 2026-10-04.

Missing:
- No claim in claims/ naming this key at all, so no closeout claim with a DONE line.
- The re-check required by steps 11 and 12: record.md states JIRA was not re-read, the audit was checked against upstream main c8ab71430f not the fork pin, and it was not re-checked at the pinned head. Its code reading is taken from the audit evidence, not re-read at the pinned head, names no code path with file and line, and its reproduction section states not attempted in this record.

### SOLR-14201, verdict keep-open, draft branch solr-14201-draft-open

Claiming host: none. The record source is the queue entry audit field, round audit-1 dated 2026-10-04 (audit verdict deferred).

Missing:
- No claim in claims/ naming this key at all, so no closeout claim with a DONE line.
- The re-check required by steps 11 and 12: record.md states JIRA was not re-read, the audit was checked against upstream main c8ab71430f not the fork pin, and it was not re-checked at the pinned head. Its code reading is taken from the audit evidence, not re-read at the pinned head, names no code path with file and line, and its reproduction section states not attempted in this record.

### SOLR-14183, verdict keep-open, draft branch solr-14183-draft-open

Claiming host: none. The record source is the queue entry audit field, round audit-1 dated 2026-10-04.

Missing:
- No claim in claims/ naming this key at all, so no closeout claim with a DONE line.
- The re-check required by steps 11 and 12: record.md states JIRA was not re-read, the audit was checked against upstream main c8ab71430f not the fork pin, and it was not re-checked at the pinned head. Its code reading is taken from the audit evidence, not re-read at the pinned head, and its reproduction section states not attempted in this record. The audit evidence itself cites line numbers (the SyncStrategy executor range), but no re-read at the pinned head is recorded.

### SOLR-14180, verdict keep-open, draft branch solr-14180-draft-open

Claiming host: none. The record source is the queue entry audit field, round audit-1 dated 2026-10-04.

Missing:
- No claim in claims/ naming this key at all, so no closeout claim with a DONE line.
- The re-check required by steps 11 and 12: record.md states JIRA was not re-read, the audit was checked against upstream main c8ab71430f not the fork pin, and it was not re-checked at the pinned head. Its code reading is taken from the audit evidence, not re-read at the pinned head, names no code path with file and line, and its reproduction section states not attempted in this record.

### SOLR-14178, verdict keep-open, draft branch solr-14178-draft-open

Claiming host: none. The record says it was checked by vm2 from the C reading dated 2026-10-19 UTC (reports/2026-10-18-skip-repro/C4, slice 4).

Missing:
- No claim in claims/ naming this key at all, so no closeout claim with a DONE line.
- The re-check required by steps 11 and 12: record.md states the JIRA status was not recorded in the C reading and JIRA was not re-read for this record.
## 2026-10-19 (seventh run)

Scope: the next 20 auditable closeout records in file order in material/skip-audit/queue-current.json at coordination tip 9d0a79696add0ec4275f18db57ef4619f6bf627d, pinned head 498a09aea97aac86c2cc7ac114113b946d249942. Result: 0 pass, 20 suspect. Gate receipts: no new DONE gate tickets this run; every gates file whose Status line says DONE names a ticket already checked in an earlier run, so no gate receipt check was due and no gate receipt suspect was found.

For every suspect below, the draft branch named in queue-current.json exists on the fork and contains drafts/<KEY>/record.md and drafts/<KEY>/jira-comment.md, the record cites the pinned head that matches queue-current.json, the word count is over 40, and the record.md text is not identical to another ticket record in this batch. The verdict is a value the rubric allows and the draft branch suffix matches the verdict. The missing pieces are only the ones listed per ticket. Note on shared text: the jira-comment.md files are byte-identical across four groups in this batch: SOLR-14150, SOLR-14140, SOLR-14133, SOLR-14100, SOLR-14063, SOLR-14027, SOLR-14004, SOLR-13853 and SOLR-13837 share one generic keep-open text; SOLR-14083, SOLR-13933, SOLR-13861 and SOLR-13825 share a second; SOLR-14074, SOLR-13976 and SOLR-13832 share a third; SOLR-14117 and SOLR-13846 share a close text.

### SOLR-14150, verdict keep-open, draft branch solr-14150-draft-open

Claiming host: none. The record source is the queue entry audit field, round audit-3 dated 2026-10-05.

Missing:
- No claim in claims/ naming this key at all, so no closeout claim with a DONE line.
- The re-check required by steps 11 and 12: record.md states JIRA was not re-read, the audit was checked against upstream main c8ab71430f not the fork pin, and it was not re-checked at the pinned head. Its code reading is taken from the audit evidence, not re-read at the pinned head, and its reproduction section states not attempted in this record. The audit evidence itself cites line numbers (LukeRequestHandler getFieldFlags, lines 654 to 711), but no re-read at the pinned head is recorded.

### SOLR-14140, verdict keep-open, draft branch solr-14140-draft-open

Claiming host: none. The record source is the queue entry audit field, round audit-5 dated 2026-10-17.

Missing:
- No claim in claims/ naming this key at all, so no closeout claim with a DONE line.
- The re-check required by steps 11 and 12: record.md states JIRA was not re-read, the audit was checked against upstream main c8ab71430f not the fork pin, and it was not re-checked at the pinned head. Its code reading is taken from the audit evidence, not re-read at the pinned head, names no code path with file and line, and its reproduction section states not attempted in this record.

### SOLR-14133, verdict keep-open, draft branch solr-14133-draft-open

Claiming host: none. The record source is the queue entry audit field, round audit-3 dated 2026-10-06.

Missing:
- No claim in claims/ naming this key at all, so no closeout claim with a DONE line.
- The re-check required by steps 11 and 12: record.md states JIRA was not re-read, the audit was checked against upstream main c8ab71430f not the fork pin, and it was not re-checked at the pinned head. Its code reading is taken from the audit evidence, not re-read at the pinned head, names no code path with file and line, and its reproduction section states not attempted in this record.

### SOLR-14117, verdict close-obsolete, draft branch solr-14117-draft-close

Claiming host: none. The record says it was checked by vm2 from an earlier B2 reading dated 2026-10-19 UTC.

Missing:
- No claim in claims/ naming this key at all, so no closeout claim with a DONE line.
- The re-check required by steps 11 and 12: record.md states the JIRA status was not recorded in the B2 reading and JIRA was not re-read for this record.
- A code path with file and line, or a reproduction attempt with a result: record.md names no file and line in its code reading, and its reproduction section states only Not attempted, with no attempt and no result. Its verdict justification states the grep result is taken from the B2 reading and was not re-run in this record.

### SOLR-14100, verdict keep-open, draft branch solr-14100-draft-open

Claiming host: none. The record source is the queue entry audit field, round audit-1 dated 2026-10-04.

Missing:
- No claim in claims/ naming this key at all, so no closeout claim with a DONE line.
- The re-check required by steps 11 and 12: record.md states JIRA was not re-read, the audit was checked against upstream main c8ab71430f not the fork pin, and it was not re-checked at the pinned head. Its code reading is taken from the audit evidence, not re-read at the pinned head, names no code path with file and line, and its reproduction section states not attempted in this record.

### SOLR-14083, verdict keep-open, draft branch solr-14083-draft-open

Claiming host: none. The record says it was checked by vm2 from an earlier B3 reading dated 2026-10-19 UTC.

Missing:
- No claim in claims/ naming this key at all, so no closeout claim with a DONE line.
- The re-check required by steps 11 and 12: record.md states JIRA was not re-read for this record; its JIRA status is taken from the earlier B3 reading.

### SOLR-14074, verdict keep-open, draft branch solr-14074-draft-open

Claiming host: none. The record says it was checked by vm2 from an earlier C reading dated 2026-10-19 UTC.

Missing:
- No claim in claims/ naming this key at all, so no closeout claim with a DONE line.
- The re-check required by steps 11 and 12: record.md states the JIRA status was not recorded in the C reading and JIRA was not re-read for this record.

### SOLR-14063, verdict keep-open, draft branch solr-14063-draft-open

Claiming host: none. The record source is the queue entry audit field, round audit-1 dated 2026-10-04.

Missing:
- No claim in claims/ naming this key at all, so no closeout claim with a DONE line.
- The re-check required by steps 11 and 12: record.md states JIRA was not re-read, the audit was checked against upstream main c8ab71430f not the fork pin, and it was not re-checked at the pinned head. Its code reading is taken from the audit evidence, not re-read at the pinned head, names no code path with file and line, and its reproduction section states not attempted in this record.

### SOLR-14061, verdict keep-open, draft branch solr-14061-draft-open

Claiming host: none. The record source is the A3 other round 1 report dated 2026-10-18; the runs were not repeated.

Missing:
- No claim in claims/ naming this key at all, so no closeout claim with a DONE line.
- The re-check required by steps 11 and 12: record.md states JIRA was not re-read for this record, its code reading was not read for this record, and its source is the earlier A3 report with the runs not repeated; no re-check at the pinned head is recorded. The record does state the A3 run command, seed and result, so the missing piece is the re-check itself, not a reproduction statement.

### SOLR-14027, verdict keep-open, draft branch solr-14027-draft-open

Claiming host: none. The record source is the queue entry audit field, round audit-1 dated 2026-10-04.

Missing:
- No claim in claims/ naming this key at all, so no closeout claim with a DONE line.
- The re-check required by steps 11 and 12: record.md states JIRA was not re-read, the audit was checked against upstream main c8ab71430f not the fork pin, and it was not re-checked at the pinned head. Its code reading is taken from the audit evidence, not re-read at the pinned head, names no code path with file and line, and its reproduction section states not attempted in this record.

### SOLR-14023, verdict keep-open, draft branch solr-14023-draft-open

Claiming host: none. The record says it was checked by vm2 from an earlier B2 reading dated 2026-10-19 UTC.

Missing:
- No claim in claims/ naming this key at all, so no closeout claim with a DONE line.
- The re-check required by steps 11 and 12: record.md states the JIRA status was not recorded in the B2 reading and JIRA was not re-read for this record.

### SOLR-14004, verdict keep-open, draft branch solr-14004-draft-open

Claiming host: none. The record source is the queue entry audit field, round audit-3 dated 2026-10-06.

Missing:
- No claim in claims/ naming this key at all, so no closeout claim with a DONE line.
- The re-check required by steps 11 and 12: record.md states JIRA was not re-read, the audit was checked against upstream main c8ab71430f not the fork pin, and it was not re-checked at the pinned head. Its code reading is taken from the audit evidence, not re-read at the pinned head, names no code path with file and line, and its reproduction section states not attempted in this record.

### SOLR-13976, verdict keep-open, draft branch solr-13976-draft-open

Claiming host: none. The record says it was checked by vm2 from an earlier C reading dated 2026-10-19 UTC.

Missing:
- No claim in claims/ naming this key at all, so no closeout claim with a DONE line.
- The re-check required by steps 11 and 12: record.md states the JIRA status was not recorded in the C reading and JIRA was not re-read for this record.

### SOLR-13933, verdict keep-open, draft branch solr-13933-draft-open

Claiming host: none. The record says it was checked by vm2 from an earlier C reading dated 2026-10-19 UTC.

Missing:
- No claim in claims/ naming this key at all, so no closeout claim with a DONE line.
- The re-check required by steps 11 and 12: record.md states the JIRA status was not recorded in the C reading and JIRA was not re-read for this record.
- A code path with file and line, or a reproduction attempt with a result: record.md names no code path with file and line (it states the ticket is a feature request with no named code path), and its reproduction section states only Not attempted, with no attempt and no result.

### SOLR-13861, verdict keep-open, draft branch solr-13861-draft-open

Claiming host: none. The record says it was checked by vm2 from an earlier C reading dated 2026-10-19 UTC.

Missing:
- No claim in claims/ naming this key at all, so no closeout claim with a DONE line.
- The re-check required by steps 11 and 12: record.md states the JIRA status was not recorded in the C reading and JIRA was not re-read for this record.

### SOLR-13853, verdict keep-open, draft branch solr-13853-draft-open

Claiming host: none. The record source is the queue entry audit field, round audit-1 dated 2026-10-04.

Missing:
- No claim in claims/ naming this key at all, so no closeout claim with a DONE line.
- The re-check required by steps 11 and 12: record.md states JIRA was not re-read, the audit was checked against upstream main c8ab71430f not the fork pin, and it was not re-checked at the pinned head. Its code reading is taken from the audit evidence, not re-read at the pinned head, names no code path with file and line, and its reproduction section states not attempted in this record.

### SOLR-13846, verdict close-obsolete, draft branch solr-13846-draft-close

Claiming host: none. The record says it was checked by vm2 from an earlier B2 reading dated 2026-10-19 UTC.

Missing:
- No claim in claims/ naming this key at all, so no closeout claim with a DONE line.
- The re-check required by steps 11 and 12: record.md states the JIRA status was not recorded in the B2 reading and JIRA was not re-read for this record.
- A code path with file and line, or a reproduction attempt with a result: record.md names no file and line in its code reading, and its reproduction section states only Not attempted, with no attempt and no result. Its verdict justification states the grep result is taken from the B2 reading and was not re-run in this record.

### SOLR-13837, verdict keep-open, draft branch solr-13837-draft-open

Claiming host: none. The record source is the queue entry audit field, round audit-1 dated 2026-10-04.

Missing:
- No claim in claims/ naming this key at all, so no closeout claim with a DONE line.
- The re-check required by steps 11 and 12: record.md states JIRA was not re-read, the audit was checked against upstream main c8ab71430f not the fork pin, and it was not re-checked at the pinned head. Its code reading is taken from the audit evidence, not re-read at the pinned head, and its reproduction section states not attempted in this record. The audit evidence itself cites line numbers (AuditEvent, lines 546 and 552 onward), but no re-read at the pinned head is recorded.

### SOLR-13832, verdict keep-open, draft branch solr-13832-draft-open

Claiming host: none. The record says it was checked by vm2 from an earlier C reading dated 2026-10-19 UTC.

Missing:
- No claim in claims/ naming this key at all, so no closeout claim with a DONE line.
- The re-check required by steps 11 and 12: record.md states the JIRA status was not recorded in the C reading and JIRA was not re-read for this record.

### SOLR-13825, verdict keep-open, draft branch solr-13825-draft-open

Claiming host: none. The record says it was checked by vm2 from an earlier B3 reading dated 2026-10-19 UTC.

Missing:
- No claim in claims/ naming this key at all, so no closeout claim with a DONE line.
- The re-check required by steps 11 and 12: record.md states JIRA was not re-read for this record; its JIRA status is taken from the earlier B3 reading.

## 2026-10-19 (eighth run)

Scope: the next 20 auditable closeout records in file order in material/skip-audit/queue-current.json at coordination tip dc19c3465b1df1e9986176415a905568a7996d82, pinned head 498a09aea97aac86c2cc7ac114113b946d249942 (the SOLR-13678 and SOLR-13570 queue records cite the abbreviated form 498a09aea9, and their records cite the same form). Result: 0 pass, 20 suspect. Gate receipts: no new DONE gate tickets this run; every gates file whose Status line says DONE names a ticket already checked in an earlier run, so no gate receipt check was due and no gate receipt suspect was found.

For every suspect below, the draft branch named in queue-current.json exists on the fork and contains drafts/<KEY>/record.md and drafts/<KEY>/jira-comment.md, the record cites the pinned head that matches queue-current.json, the word count is over 40, and the record.md text is not identical to another ticket record in this batch. The verdict is a value the rubric allows and the draft branch suffix matches the verdict. The missing pieces are only the ones listed per ticket. Note on shared text: the jira-comment.md files are byte-identical across three groups in this batch: SOLR-13785, SOLR-13772, SOLR-13709, SOLR-13708, SOLR-13687, SOLR-13654, SOLR-13618 and SOLR-13471 share one generic keep-open text; SOLR-13698, SOLR-13627 and SOLR-13616 share a second; SOLR-13781, SOLR-13591, SOLR-13505, SOLR-13501 and SOLR-13481 share a close-obsolete text.

### SOLR-13785, verdict keep-open, draft branch solr-13785-draft-open

Claiming host: none. The record source is the queue entry audit field, round audit-1 dated 2026-10-04.

Missing:
- No claim in claims/ naming this key at all, so no closeout claim with a DONE line.
- The re-check required by steps 11 and 12: record.md states JIRA was not re-read, the audit was checked against upstream main c8ab71430f not the fork pin, and it was not re-checked at the pinned head. Its code reading is taken from the audit evidence, not re-read at the pinned head, names no code path with file and line, and its reproduction section states not attempted in this record.

### SOLR-13781, verdict close-obsolete, draft branch solr-13781-draft-close

Claiming host: none. The record says it was checked by vm2 from an earlier C reading dated 2026-10-19 UTC.

Missing:
- No claim in claims/ naming this key at all, so no closeout claim with a DONE line.
- The re-check required by steps 11 and 12: record.md states the JIRA status was not recorded in the C reading and JIRA was not re-read for this record.
- A code path with file and line, or a reproduction attempt with a result: record.md names no file and line in its code reading, and its reproduction section states only Not attempted, with no attempt and no result. Its verdict justification states the grep result is taken from the C reading and was not re-run in this record.

### SOLR-13772, verdict keep-open, draft branch solr-13772-draft-open

Claiming host: none. The record source is the queue entry audit field, round audit-1 dated 2026-10-04.

Missing:
- No claim in claims/ naming this key at all, so no closeout claim with a DONE line.
- The re-check required by steps 11 and 12: record.md states JIRA was not re-read, the audit was checked against upstream main c8ab71430f not the fork pin, and it was not re-checked at the pinned head. Its code reading is taken from the audit evidence, not re-read at the pinned head, names no code path with file and line, and its reproduction section states not attempted in this record.

### SOLR-13709, verdict keep-open, draft branch solr-13709-draft-open

Claiming host: none. The record source is the queue entry audit field, round audit-1 dated 2026-10-04.

Missing:
- No claim in claims/ naming this key at all, so no closeout claim with a DONE line.
- The re-check required by steps 11 and 12: record.md states JIRA was not re-read, the audit was checked against upstream main c8ab71430f not the fork pin, and it was not re-checked at the pinned head. Its code reading is taken from the audit evidence, not re-read at the pinned head, names no code path with file and line, and its reproduction section states not attempted in this record.

### SOLR-13708, verdict keep-open, draft branch solr-13708-draft-open

Claiming host: none. The record source is the queue entry audit field, round audit-3 dated 2026-10-06.

Missing:
- No claim in claims/ naming this key at all, so no closeout claim with a DONE line.
- The re-check required by steps 11 and 12: record.md states JIRA was not re-read, the audit was checked against upstream main c8ab71430f not the fork pin, and it was not re-checked at the pinned head. Its code reading is taken from the audit evidence, not re-read at the pinned head, names no code path with file and line, and its reproduction section states not attempted in this record.

### SOLR-13698, verdict keep-open, draft branch solr-13698-draft-open

Claiming host: none. The record says it was checked by vm2 from an earlier C reading dated 2026-10-19 UTC.

Missing:
- No claim in claims/ naming this key at all, so no closeout claim with a DONE line.
- The re-check required by steps 11 and 12: record.md states the JIRA status was not recorded in the C reading and JIRA was not re-read for this record.

### SOLR-13687, verdict keep-open, draft branch solr-13687-draft-open

Claiming host: none. The record source is the queue entry audit field, round audit-1 dated 2026-10-04.

Missing:
- No claim in claims/ naming this key at all, so no closeout claim with a DONE line.
- The re-check required by steps 11 and 12: record.md states JIRA was not re-read, the audit was checked against upstream main c8ab71430f not the fork pin, and it was not re-checked at the pinned head. Its code reading is taken from the audit evidence, not re-read at the pinned head, names no code path with file and line, and its reproduction section states not attempted in this record.

### SOLR-13678, verdict close-fixed, draft branch solr-13678-draft-close

Claiming host: none. The record says it was checked by vm5 on 2026-10-18.

Missing:
- No claim in claims/ naming this key at all, so no closeout claim with a DONE line.
- The still-present check the rubric requires for close-fixed: record.md describes fixing commit 67a642fe02 and states it is an ancestor, but names no file and line read at the pinned head and records no check at the head showing the changed code still present; that presence claim appears only in the verdict justification.

### SOLR-13654, verdict keep-open, draft branch solr-13654-draft-open

Claiming host: none. The record source is the queue entry audit field, round audit-1 dated 2026-10-04.

Missing:
- No claim in claims/ naming this key at all, so no closeout claim with a DONE line.
- The re-check required by steps 11 and 12: record.md states JIRA was not re-read, the audit was checked against upstream main c8ab71430f not the fork pin, and it was not re-checked at the pinned head. Its code reading is taken from the audit evidence, not re-read at the pinned head, names no code path with file and line, and its reproduction section states not attempted in this record.

### SOLR-13627, verdict keep-open, draft branch solr-13627-draft-open

Claiming host: none. The record says it was checked by vm2 from an earlier C reading dated 2026-10-19 UTC.

Missing:
- No claim in claims/ naming this key at all, so no closeout claim with a DONE line.
- The re-check required by steps 11 and 12: record.md states the JIRA status was not recorded in the C reading and JIRA was not re-read for this record.
- A code path with file and line, or a reproduction attempt with a result: record.md names no file and line in its code reading (it names ZkController and RecoveryStrategy without a file or line), and its reproduction section states only Not attempted, with no attempt and no result.

### SOLR-13618, verdict keep-open, draft branch solr-13618-draft-open

Claiming host: none. The record source is the queue entry audit field, round audit-1 dated 2026-10-04.

Missing:
- No claim in claims/ naming this key at all, so no closeout claim with a DONE line.
- The re-check required by steps 11 and 12: record.md states JIRA was not re-read, the audit was checked against upstream main c8ab71430f not the fork pin, and it was not re-checked at the pinned head. Its code reading is taken from the audit evidence, not re-read at the pinned head, and its reproduction section states not attempted in this record. The audit evidence itself cites CoreContainer.create at line 1513, but no re-read at the pinned head is recorded.

### SOLR-13616, verdict keep-open, draft branch solr-13616-draft-open

Claiming host: none. The record says it was checked by vm2 from an earlier B3 reading dated 2026-10-19 UTC.

Missing:
- No claim in claims/ naming this key at all, so no closeout claim with a DONE line.
- The re-check required by steps 11 and 12: record.md states JIRA was not re-read for this record; its JIRA status is taken from the earlier B3 reading.

### SOLR-13591, verdict close-obsolete, draft branch solr-13591-draft-close

Claiming host: none. The record says it was checked by vm2 from an earlier C reading dated 2026-10-19 UTC.

Missing:
- No claim in claims/ naming this key at all, so no closeout claim with a DONE line.
- The re-check required by steps 11 and 12: record.md states the JIRA status was not recorded in the C reading and JIRA was not re-read for this record.
- The grep result the close-obsolete verdict needs is taken from the C reading and was not re-run in this record, as record.md itself states under Missing for this verdict.

### SOLR-13572, verdict keep-open, draft branch solr-13572-draft-open

Claiming host: none. The record says it was checked by vm2 from an earlier B3 reading dated 2026-10-19 UTC.

Missing:
- No claim in claims/ naming this key at all, so no closeout claim with a DONE line.
- The re-check required by steps 11 and 12: record.md states JIRA was not re-read for this record; its JIRA status is taken from the earlier B3 reading.

### SOLR-13570, verdict close-fixed, draft branch solr-13570-draft-close

Claiming host: none. The record says it was checked by vm5 on 2026-10-18.

Missing:
- No claim in claims/ naming this key at all, so no closeout claim with a DONE line.
- The verdict close-fixed is not backed by its own evidence: close-fixed needs a fixing commit whose changed code is still present at the pinned head, but record.md cites two removal commits (7eb46b3f03 and a80eb84d56) and empty greps, and its gap analysis states the rubric's close-obsolete fits this case.

### SOLR-13505, verdict close-obsolete, draft branch solr-13505-draft-close

Claiming host: none. The record says it was checked by vm2 from an earlier C reading dated 2026-10-19 UTC.

Missing:
- No claim in claims/ naming this key at all, so no closeout claim with a DONE line.
- The re-check required by steps 11 and 12: record.md states the JIRA status was not recorded in the C reading and JIRA was not re-read for this record.
- A code path with file and line, or a reproduction attempt with a result: record.md names no file and line in its code reading, and its reproduction section states only Not attempted, with no attempt and no result. Its verdict justification states the grep result is taken from the C reading and was not re-run in this record.

### SOLR-13501, verdict close-obsolete, draft branch solr-13501-draft-close

Claiming host: none. The record says it was checked by vm2 from an earlier B2 reading dated 2026-10-19 UTC.

Missing:
- No claim in claims/ naming this key at all, so no closeout claim with a DONE line.
- The re-check required by steps 11 and 12: record.md states the JIRA status was not recorded in the B2 reading and JIRA was not re-read for this record.
- A code path with file and line, or a reproduction attempt with a result: record.md names no file and line in its code reading, and its reproduction section states only Not attempted, with no attempt and no result. Its verdict justification states the grep result is taken from the B2 reading and was not re-run in this record.

### SOLR-13486, verdict keep-open, draft branch solr-13486-draft-open

Claiming host: none. The record source is the A3 other round 1 report dated 2026-10-18; the runs were not repeated.

Missing:
- No skip closeout claim in claims/ for this key: no claims/skip-SOLR-13486-<host>.md and no closeout claim with a DONE line. The only file in claims/ naming this key is claims/pool-skip-repro-14159-round-1.md, a claim for the SOLR-14159 repro assignment that mentions a SOLR-13486 read, not a closeout claim for this ticket.
- The re-check required by steps 11 and 12: record.md states JIRA was not re-read for this record, its code reading was not read for this record, and its source is the earlier A3 report with the runs not repeated; no re-check at the pinned head is recorded. The record does state the A3 run command, seed and result, so the missing piece is the re-check itself, not a reproduction statement.

### SOLR-13481, verdict close-obsolete, draft branch solr-13481-draft-close

Claiming host: none. The record says it was checked by vm2 from an earlier B2 reading dated 2026-10-19 UTC.

Missing:
- No claim in claims/ naming this key at all, so no closeout claim with a DONE line.
- The re-check required by steps 11 and 12: record.md states the JIRA status was not recorded in the B2 reading and JIRA was not re-read for this record.
- The grep result the close-obsolete verdict needs is taken from the B2 reading and was not re-run in this record, as record.md itself states under Missing for this verdict. Its reproduction section also states only Not attempted.

### SOLR-13471, verdict keep-open, draft branch solr-13471-draft-open

Claiming host: none. The record source is the queue entry audit field, round audit-1 dated 2026-10-04.

Missing:
- No claim in claims/ naming this key at all, so no closeout claim with a DONE line.
- The re-check required by steps 11 and 12: record.md states JIRA was not re-read, the audit was checked against upstream main c8ab71430f not the fork pin, and it was not re-checked at the pinned head. Its code reading is taken from the audit evidence, not re-read at the pinned head, names no code path with file and line, and its reproduction section states not attempted in this record.

## 2026-10-19 (ninth run)

Scope: the next 20 auditable closeout records in file order in material/skip-audit/queue-current.json at coordination tip 7207ee632ece015e8a25858c34a514a7050ca489, pinned head 498a09aea97aac86c2cc7ac114113b946d249942. Result: 0 pass, 20 suspect. Gate receipts: no new DONE gate tickets this run; every gates file whose Status line says DONE names a ticket already checked in an earlier run, so no gate receipt check was due and no gate receipt suspect was found.

For every suspect below, the draft branch named in queue-current.json exists on the fork and contains drafts/<KEY>/record.md and drafts/<KEY>/jira-comment.md, the record cites the pinned head that matches queue-current.json, the word count is over 40, and the record.md text is not identical to another ticket record in this batch. The verdict is a value the rubric allows and the draft branch suffix matches the verdict. The missing pieces are only the ones listed per ticket. Note on shared text: the jira-comment.md files are byte-identical across two groups in this batch: SOLR-13470, SOLR-13455, SOLR-13390, SOLR-13346, SOLR-13310, SOLR-13305, SOLR-13291, SOLR-13283, SOLR-13270, SOLR-13263, SOLR-13247, SOLR-13237, SOLR-13233, SOLR-13195 and SOLR-13192 share one generic keep-open text; SOLR-13416, SOLR-13242 and SOLR-13216 share a second.

### SOLR-13470, verdict keep-open, draft branch solr-13470-draft-open

Claiming host: none. The record source is the queue entry audit field, round audit-1 dated 2026-10-04.

Missing:
- No claim in claims/ naming this key at all, so no closeout claim with a DONE line.
- The re-check required by steps 11 and 12: record.md states JIRA was not re-read, the audit was checked against upstream main c8ab71430f not the fork pin, and it was not re-checked at the pinned head. Its code reading is taken from the audit evidence, not re-read at the pinned head, names no code path with file and line, and its reproduction section states not attempted in this record.

### SOLR-13455, verdict keep-open, draft branch solr-13455-draft-open

Claiming host: none. The record source is the queue entry audit field, round audit-3 dated 2026-10-06.

Missing:
- No claim in claims/ naming this key at all, so no closeout claim with a DONE line.
- The re-check required by steps 11 and 12: record.md states JIRA was not re-read, the audit was checked against upstream main c8ab71430f not the fork pin, and it was not re-checked at the pinned head. Its code reading is taken from the audit evidence, not re-read at the pinned head, names no code path with file and line, and its reproduction section states not attempted in this record.

### SOLR-13416, verdict keep-open, draft branch solr-13416-draft-open

Claiming host: none. The record says it was checked by vm2 from an earlier C reading dated 2026-10-19 UTC.

Missing:
- No claim in claims/ naming this key at all, so no closeout claim with a DONE line.
- The re-check required by steps 11 and 12: record.md states the JIRA status was not recorded in the C reading and JIRA was not re-read for this record.
- A code path with file and line, or a reproduction attempt with a result: record.md names no file and line in its code reading (it names LockTree and LockTree$Node without a file or line), and its reproduction section states only Not attempted, with no attempt and no result.

### SOLR-13390, verdict keep-open, draft branch solr-13390-draft-open

Claiming host: none. The record source is the queue entry audit field, round audit-1 dated 2026-10-04.

Missing:
- No claim in claims/ naming this key at all, so no closeout claim with a DONE line.
- The re-check required by steps 11 and 12: record.md states JIRA was not re-read, the audit was checked against upstream main c8ab71430f not the fork pin, and it was not re-checked at the pinned head. Its code reading is taken from the audit evidence, not re-read at the pinned head, names no code path with file and line, and its reproduction section states not attempted in this record.

### SOLR-13346, verdict keep-open, draft branch solr-13346-draft-open

Claiming host: none. The record source is the queue entry audit field, round audit-1 dated 2026-10-04.

Missing:
- No claim in claims/ naming this key at all, so no closeout claim with a DONE line.
- The re-check required by steps 11 and 12: record.md states JIRA was not re-read, the audit was checked against upstream main c8ab71430f not the fork pin, and it was not re-checked at the pinned head. Its code reading is taken from the audit evidence, not re-read at the pinned head, names no code path with file and line, and its reproduction section states not attempted in this record.

### SOLR-13310, verdict keep-open, draft branch solr-13310-draft-open

Claiming host: none. The record source is the queue entry audit field, round audit-1 dated 2026-10-04.

Missing:
- No claim in claims/ naming this key at all, so no closeout claim with a DONE line.
- The re-check required by steps 11 and 12: record.md states JIRA was not re-read, the audit was checked against upstream main c8ab71430f not the fork pin, and it was not re-checked at the pinned head. Its code reading is taken from the audit evidence, not re-read at the pinned head, names no code path with file and line, and its reproduction section states not attempted in this record.

### SOLR-13305, verdict keep-open, draft branch solr-13305-draft-open

Claiming host: none. The record source is the queue entry audit field, round audit-3 dated 2026-10-06.

Missing:
- No claim in claims/ naming this key at all, so no closeout claim with a DONE line.
- The re-check required by steps 11 and 12: record.md states JIRA was not re-read, the audit was checked against upstream main c8ab71430f not the fork pin, and it was not re-checked at the pinned head. Its code reading is taken from the audit evidence, not re-read at the pinned head, names no code path with file and line, and its reproduction section states not attempted in this record.

### SOLR-13304, verdict close-obsolete, draft branch solr-13304-draft-close

Claiming host: none. The record says it was checked by vm2 from an earlier B2 reading dated 2026-10-19 UTC.

Missing:
- No claim in claims/ naming this key at all, so no closeout claim with a DONE line.
- The re-check required by steps 11 and 12: record.md states the JIRA status was not recorded in the B2 reading and JIRA was not re-read for this record.
- A code path with file and line, or a reproduction attempt with a result: record.md names no file and line in its code reading, and its reproduction section states only Not attempted, with no attempt and no result. Its verdict justification states the grep result is taken from the B2 reading and was not re-run in this record.

### SOLR-13291, verdict keep-open, draft branch solr-13291-draft-open

Claiming host: none. The record source is the queue entry audit field, round audit-1 dated 2026-10-04.

Missing:
- No claim in claims/ naming this key at all, so no closeout claim with a DONE line.
- The re-check required by steps 11 and 12: record.md states JIRA was not re-read, the audit was checked against upstream main c8ab71430f not the fork pin, and it was not re-checked at the pinned head. Its code reading is taken from the audit evidence, not re-read at the pinned head, names no code path with file and line, and its reproduction section states not attempted in this record.

### SOLR-13283, verdict keep-open, draft branch solr-13283-draft-open

Claiming host: none. The record source is the queue entry audit field, round audit-1 dated 2026-10-04.

Missing:
- No claim in claims/ naming this key at all, so no closeout claim with a DONE line.
- The re-check required by steps 11 and 12: record.md states JIRA was not re-read, the audit was checked against upstream main c8ab71430f not the fork pin, and it was not re-checked at the pinned head. Its code reading is taken from the audit evidence, not re-read at the pinned head, names no code path with file and line, and its reproduction section states not attempted in this record.

### SOLR-13278, verdict close-answered, draft branch solr-13278-draft-close

Claiming host: none. The record says it was checked by vm2 from an earlier B3 reading dated 2026-10-19 UTC.

Missing:
- No claim in claims/ naming this key at all, so no closeout claim with a DONE line.
- The re-check required by steps 11 and 12: record.md states JIRA was not re-read for this record; its JIRA status is taken from the earlier B3 reading.

### SOLR-13270, verdict keep-open, draft branch solr-13270-draft-open

Claiming host: none. The record source is the queue entry audit field, round audit-1 dated 2026-10-04.

Missing:
- No claim in claims/ naming this key at all, so no closeout claim with a DONE line.
- The re-check required by steps 11 and 12: record.md states JIRA was not re-read, the audit was checked against upstream main c8ab71430f not the fork pin, and it was not re-checked at the pinned head. Its code reading is taken from the audit evidence, not re-read at the pinned head, names no code path with file and line, and its reproduction section states not attempted in this record.

### SOLR-13263, verdict keep-open, draft branch solr-13263-draft-open

Claiming host: none. The record source is the queue entry audit field, round audit-1 dated 2026-10-04.

Missing:
- No claim in claims/ naming this key at all, so no closeout claim with a DONE line.
- The re-check required by steps 11 and 12: record.md states JIRA was not re-read, the audit was checked against upstream main c8ab71430f not the fork pin, and it was not re-checked at the pinned head. Its code reading is taken from the audit evidence, not re-read at the pinned head, names no code path with file and line, and its reproduction section states not attempted in this record.

### SOLR-13247, verdict keep-open, draft branch solr-13247-draft-open

Claiming host: none. The record source is the queue entry audit field, round audit-1 dated 2026-10-04.

Missing:
- No claim in claims/ naming this key at all, so no closeout claim with a DONE line.
- The re-check required by steps 11 and 12: record.md states JIRA was not re-read, the audit was checked against upstream main c8ab71430f not the fork pin, and it was not re-checked at the pinned head. Its code reading is taken from the audit evidence, not re-read at the pinned head, names no code path with file and line, and its reproduction section states not attempted in this record.

### SOLR-13242, verdict keep-open, draft branch solr-13242-draft-open

Claiming host: none. The record says it was checked by vm2 from an earlier C reading dated 2026-10-19 UTC.

Missing:
- No claim in claims/ naming this key at all, so no closeout claim with a DONE line.
- The re-check required by steps 11 and 12: record.md states the JIRA status was not recorded in the C reading and JIRA was not re-read for this record.

### SOLR-13237, verdict keep-open, draft branch solr-13237-draft-open

Claiming host: none. The record source is the queue entry audit field, round audit-1 dated 2026-10-04.

Missing:
- No claim in claims/ naming this key at all, so no closeout claim with a DONE line.
- The re-check required by steps 11 and 12: record.md states JIRA was not re-read, the audit was checked against upstream main c8ab71430f not the fork pin, and it was not re-checked at the pinned head. Its code reading is taken from the audit evidence, not re-read at the pinned head, names no code path with file and line, and its reproduction section states not attempted in this record.

### SOLR-13233, verdict keep-open, draft branch solr-13233-draft-open

Claiming host: none. The record source is the queue entry audit field, round audit-1 dated 2026-10-04.

Missing:
- No claim in claims/ naming this key at all, so no closeout claim with a DONE line.
- The re-check required by steps 11 and 12: record.md states JIRA was not re-read, the audit was checked against upstream main c8ab71430f not the fork pin, and it was not re-checked at the pinned head. Its code reading is taken from the audit evidence, not re-read at the pinned head, names no code path with file and line, and its reproduction section states not attempted in this record.

### SOLR-13216, verdict keep-open, draft branch solr-13216-draft-open

Claiming host: none. The record says it was checked by vm2 from an earlier C reading dated 2026-10-19 UTC.

Missing:
- No claim in claims/ naming this key at all, so no closeout claim with a DONE line.
- The re-check required by steps 11 and 12: record.md states the JIRA status was not recorded in the C reading and JIRA was not re-read for this record.

### SOLR-13195, verdict keep-open, draft branch solr-13195-draft-open

Claiming host: none. The record source is the queue entry audit field, round audit-1 dated 2026-10-04.

Missing:
- No claim in claims/ naming this key at all, so no closeout claim with a DONE line.
- The re-check required by steps 11 and 12: record.md states JIRA was not re-read, the audit was checked against upstream main c8ab71430f not the fork pin, and it was not re-checked at the pinned head. Its code reading is taken from the audit evidence, not re-read at the pinned head, names no code path with file and line, and its reproduction section states not attempted in this record.

### SOLR-13192, verdict keep-open, draft branch solr-13192-draft-open

Claiming host: none. The record source is the queue entry audit field, round audit-1 dated 2026-10-04.

Missing:
- No claim in claims/ naming this key at all, so no closeout claim with a DONE line.
- The re-check required by steps 11 and 12: record.md states JIRA was not re-read, the audit was checked against upstream main c8ab71430f not the fork pin, and it was not re-checked at the pinned head. Its code reading is taken from the audit evidence, not re-read at the pinned head, names no code path with file and line, and its reproduction section states not attempted in this record.

## 2026-10-19 (tenth run)

Scope: the next 20 auditable closeout records in file order in material/skip-audit/queue-current.json at coordination tip ebb0cfce6980afd7ac38c918174177ba69f4c123, pinned head 498a09aea97aac86c2cc7ac114113b946d249942. Result: 0 pass, 20 suspect. Gate receipts: no new DONE gate tickets this run; every gates file whose Status line says DONE names a ticket already checked in an earlier run, so no gate receipt check was due and no gate receipt suspect was found.

For every suspect below, the draft branch named in queue-current.json exists on the fork and contains drafts/<KEY>/record.md and drafts/<KEY>/jira-comment.md, the record cites the pinned head that matches queue-current.json, the word count is over 40, and the record.md text is not identical to another ticket record in this batch. The verdict is a value the rubric allows and the draft branch suffix matches the verdict. The missing pieces are only the ones listed per ticket. SOLR-13066 is the exception on the pinned head wording: queue-current.json records its evidence head in short form, 498a09aea9, and its record.md cites that same short form. Note on shared text: the jira-comment.md files are byte-identical across three groups in this batch: SOLR-13175, SOLR-13166, SOLR-13061, SOLR-13056, SOLR-13038, SOLR-13009, SOLR-12944, SOLR-12884 and SOLR-12850 share one generic keep-open text; SOLR-13094, SOLR-13021, SOLR-12900 and SOLR-12808 share a second; SOLR-12918, SOLR-12844 and SOLR-12819 share a third.

### SOLR-13175, verdict keep-open, draft branch solr-13175-draft-open

Claiming host: none. The record source is the queue entry audit field, round audit-1 dated 2026-10-04.

Missing:
- No claim in claims/ naming this key at all, so no closeout claim with a DONE line.
- The re-check required by steps 11 and 12: record.md states JIRA was not re-read, the audit was checked against upstream main c8ab71430f not the fork pin, and it was not re-checked at the pinned head. Its code reading is taken from the audit evidence, not re-read at the pinned head, names no code path with file and line, and its reproduction section states not attempted in this record.

### SOLR-13167, verdict close-answered, draft branch solr-13167-draft-close

Claiming host: none. The record says it was checked by vm2 from an earlier B3 reading dated 2026-10-19 UTC.

Missing:
- No claim in claims/ naming this key at all, so no closeout claim with a DONE line.
- The re-check required by steps 11 and 12: record.md states JIRA was not re-read for this record; its JIRA status is taken from the earlier B3 reading.

### SOLR-13166, verdict keep-open, draft branch solr-13166-draft-open

Claiming host: none. The record source is the queue entry audit field, round audit-1 dated 2026-10-04.

Missing:
- No claim in claims/ naming this key at all, so no closeout claim with a DONE line.
- The re-check required by steps 11 and 12: record.md states JIRA was not re-read, the audit was checked against upstream main c8ab71430f not the fork pin, and it was not re-checked at the pinned head. Its code reading is taken from the audit evidence, not re-read at the pinned head, names no code path with file and line, and its reproduction section states not attempted in this record.

### SOLR-13094, verdict keep-open, draft branch solr-13094-draft-open

Claiming host: none. The record says it was checked by vm2 from an earlier C reading dated 2026-10-19 UTC.

Missing:
- No claim in claims/ naming this key at all, so no closeout claim with a DONE line.
- The re-check required by steps 11 and 12: record.md states the JIRA status was not recorded in the C reading and JIRA was not re-read for this record.

### SOLR-13066, verdict close-fixed, draft branch solr-13066-draft-close

Claiming host: none. The record says it was checked by a pool Linux host in a coordinator session dated 2026-10-18 UTC.

Missing:
- No claim in claims/ naming this key at all, so no closeout claim with a DONE line. The record itself is a re-check at the pinned head: it records the JIRA status and date checked, the fixing commit 7de72c9bc7 checked as an ancestor of the pinned head, and the changed code still present at CoreContainer.java lines 2014 and 2054. The claim is the only missing piece for this ticket.

### SOLR-13061, verdict keep-open, draft branch solr-13061-draft-open

Claiming host: none. The record source is the queue entry audit field, round audit-1 dated 2026-10-04.

Missing:
- No claim in claims/ naming this key at all, so no closeout claim with a DONE line.
- The re-check required by steps 11 and 12: record.md states JIRA was not re-read, the audit was checked against upstream main c8ab71430f not the fork pin, and it was not re-checked at the pinned head. Its code reading is taken from the audit evidence, not re-read at the pinned head, names no code path with file and line, and its reproduction section states not attempted in this record.

### SOLR-13056, verdict keep-open, draft branch solr-13056-draft-open

Claiming host: none. The record source is the queue entry audit field, round audit-1 dated 2026-10-04.

Missing:
- No claim in claims/ naming this key at all, so no closeout claim with a DONE line.
- The re-check required by steps 11 and 12: record.md states JIRA was not re-read, the audit was checked against upstream main c8ab71430f not the fork pin, and it was not re-checked at the pinned head. Its code reading is taken from the audit evidence, not re-read at the pinned head, names no code path with file and line, and its reproduction section states not attempted in this record.

### SOLR-13038, verdict keep-open, draft branch solr-13038-draft-open

Claiming host: none. The record source is the queue entry audit field, round audit-1 dated 2026-10-04.

Missing:
- No claim in claims/ naming this key at all, so no closeout claim with a DONE line.
- The re-check required by steps 11 and 12: record.md states JIRA was not re-read, the audit was checked against upstream main c8ab71430f not the fork pin, and it was not re-checked at the pinned head. Its code reading is taken from the audit evidence, not re-read at the pinned head, names no code path with file and line, and its reproduction section states not attempted in this record.

### SOLR-13021, verdict keep-open, draft branch solr-13021-draft-open

Claiming host: none. The record says it was checked by vm2 from an earlier C reading dated 2026-10-19 UTC.

Missing:
- No claim in claims/ naming this key at all, so no closeout claim with a DONE line.
- The re-check required by steps 11 and 12: record.md states the JIRA status was not recorded in the C reading and JIRA was not re-read for this record.
- A code path with file and line, or a reproduction attempt with a result: record.md names no file and line in its code reading (it states the description is empty and no code path is named, and that the test the comments cite does not exist at the pinned head), and its reproduction section states only Not attempted, with no attempt and no result.

### SOLR-13009, verdict keep-open, draft branch solr-13009-draft-open

Claiming host: none. The record source is the queue entry audit field, round audit-1 dated 2026-10-04.

Missing:
- No claim in claims/ naming this key at all, so no closeout claim with a DONE line.
- The re-check required by steps 11 and 12: record.md states JIRA was not re-read, the audit was checked against upstream main c8ab71430f not the fork pin, and it was not re-checked at the pinned head. Its code reading is taken from the audit evidence, not re-read at the pinned head, names no code path with file and line, and its reproduction section states not attempted in this record.

### SOLR-12944, verdict keep-open, draft branch solr-12944-draft-open

Claiming host: none. The record source is the queue entry audit field, round audit-1 dated 2026-10-04.

Missing:
- No claim in claims/ naming this key at all, so no closeout claim with a DONE line.
- The re-check required by steps 11 and 12: record.md states JIRA was not re-read, the audit was checked against upstream main c8ab71430f not the fork pin, and it was not re-checked at the pinned head. Its code reading is taken from the audit evidence, not re-read at the pinned head, names no code path with file and line, and its reproduction section states not attempted in this record.

### SOLR-12918, verdict keep-open, draft branch solr-12918-draft-open

Claiming host: none. The record source is the earlier A3 report dated 2026-10-18.

Missing:
- No claim in claims/ naming this key at all, so no closeout claim with a DONE line.
- The re-check required by steps 11 and 12: record.md states JIRA was not re-read for this record and its code reading was not read for this record; its source is the earlier A3 report and the runs were not repeated. The record does state the A3 run command, seed and result, so the missing piece is the re-check itself, not a reproduction statement.

### SOLR-12900, verdict keep-open, draft branch solr-12900-draft-open

Claiming host: none. The record says it was checked by vm2 from an earlier C reading dated 2026-10-19 UTC.

Missing:
- No claim in claims/ naming this key at all, so no closeout claim with a DONE line.
- The re-check required by steps 11 and 12: record.md states the JIRA status was not recorded in the C reading and JIRA was not re-read for this record.
- A Solr code path with file and line, or a reproduction attempt with a result: record.md states the failing code is in Lucene's FieldValueHitQueue, which is not in this tree, and the only Solr frame it names is a caller at line 1827 with no file; its reproduction section states only Not attempted, with no attempt and no result.

### SOLR-12884, verdict keep-open, draft branch solr-12884-draft-open

Claiming host: none. The record source is the queue entry audit field, round audit-1 dated 2026-10-04.

Missing:
- No claim in claims/ naming this key at all, so no closeout claim with a DONE line.
- The re-check required by steps 11 and 12: record.md states JIRA was not re-read, the audit was checked against upstream main c8ab71430f not the fork pin, and it was not re-checked at the pinned head. Its code reading is taken from the audit evidence, not re-read at the pinned head, names no code path with file and line, and its reproduction section states not attempted in this record.

### SOLR-12850, verdict keep-open, draft branch solr-12850-draft-open

Claiming host: none. The record source is the queue entry audit field, round audit-1 dated 2026-10-04.

Missing:
- No claim in claims/ naming this key at all, so no closeout claim with a DONE line.
- The re-check required by steps 11 and 12: record.md states JIRA was not re-read, the audit was checked against upstream main c8ab71430f not the fork pin, and it was not re-checked at the pinned head. Its code reading is taken from the audit evidence, not re-read at the pinned head, names no code path with file and line, and its reproduction section states not attempted in this record.

### SOLR-12844, verdict keep-open, draft branch solr-12844-draft-open

Claiming host: none. The record source is the earlier A1 report dated 2026-10-18; the only claims file naming this key is a vm2 repro round claim, not a closeout claim.

Missing:
- No skip closeout claim in claims/: no claims/skip-SOLR-12844-<host>.md and no closeout claim with a DONE line. The only file in claims/ naming this key is claims/pool-skip-repro-flake-round-1.md (vm2), a repro round claim whose DONE line delivers the A1 report, not a closeout.
- The re-check required by steps 11 and 12: record.md states JIRA was not re-read for this record and its code reading was not read for this record; its source is the earlier A1 report and the runs were not repeated. The record does state the A1 run command, seed and result, so the missing piece is the re-check itself, not a reproduction statement.

### SOLR-12819, verdict keep-open, draft branch solr-12819-draft-open

Claiming host: none. The record source is the earlier A3 report dated 2026-10-18.

Missing:
- No claim in claims/ naming this key at all, so no closeout claim with a DONE line.
- The re-check required by steps 11 and 12: record.md states JIRA was not re-read for this record and its code reading was not read for this record; its source is the earlier A3 report and the runs were not repeated. The record does state the A3 run command, seed and result, so the missing piece is the re-check itself, not a reproduction statement.

### SOLR-12808, verdict keep-open, draft branch solr-12808-draft-open

Claiming host: none. The record says it was checked by vm2 from an earlier C reading dated 2026-10-19 UTC.

Missing:
- No claim in claims/ naming this key at all, so no closeout claim with a DONE line.
- The re-check required by steps 11 and 12: record.md states the JIRA status was not recorded in the C reading and JIRA was not re-read for this record.
- A code path with file and line, or a reproduction attempt with a result: record.md names no file and line in its code reading (it states the offset mapping lives in Lucene's PatternReplaceCharFilter, which is not in this tree), and its reproduction section states only Not attempted, with no attempt and no result.

### SOLR-12781, verdict keep-open, draft branch solr-12781-draft-open

Claiming host: none. The record says it was checked by vm2 from an earlier C reading dated 2026-10-19 UTC.

Missing:
- No claim in claims/ naming this key at all, so no closeout claim with a DONE line.
- The re-check required by steps 11 and 12: record.md states the JIRA status was not recorded in the C reading and JIRA was not re-read for this record.

### SOLR-12748, verdict keep-open, draft branch solr-12748-draft-open

Claiming host: none. The record source is the earlier A2 report dated 2026-10-18.

Missing:
- No claim in claims/ naming this key at all, so no closeout claim with a DONE line.
- The re-check required by steps 11 and 12: record.md states JIRA was not re-read for this record and its code reading was not read for this record; it was checked from the earlier A2 report and the runs were not repeated. Its reproduction section states not attempted: no test is named in the description and nothing was run.

## 2026-10-19 (eleventh run)

Scope: the next 20 auditable closeout records in file order in material/skip-audit/queue-current.json at coordination tip a9a6ae3eed773510a996edeb6c649a65557cf33b, pinned head 498a09aea97aac86c2cc7ac114113b946d249942. Result: 0 pass, 20 suspect. Gate receipts: no new DONE gate tickets this run; every gates file whose Status line says DONE names a ticket already checked in an earlier run, so no gate receipt check was due and no gate receipt suspect was found. The pattern is unchanged from earlier passes: bulk closeouts without a claim or a per-ticket re-check.

For every suspect below, the draft branch named in queue-current.json exists on the fork and contains drafts/<KEY>/record.md and drafts/<KEY>/jira-comment.md, the record cites the pinned head that matches queue-current.json, the word count is over 40, and the record.md text is not identical to another ticket record in this batch. The verdict is a value the rubric allows and the draft branch suffix matches the verdict. The missing pieces are only the ones listed per ticket. Note on shared text: the jira-comment.md files are byte-identical across four groups in this batch: SOLR-12688, SOLR-12659, SOLR-12542, SOLR-12487, SOLR-12504, SOLR-12363, SOLR-12340, SOLR-12268 and SOLR-12262 share one generic keep-open text; SOLR-12263 and SOLR-12460 share a second; SOLR-12300 and SOLR-12315 share a third; SOLR-12645 and SOLR-12646 share a close-obsolete text.

### SOLR-12688, verdict keep-open, draft branch solr-12688-draft-open

Claiming host: none. The record source is the queue entry audit field, round audit-3 dated 2026-10-06.

Missing:
- No claim in claims/ naming this key at all, so no closeout claim with a DONE line.
- The re-check required by steps 11 and 12: record.md states JIRA was not re-read, the audit was checked against upstream main c8ab71430f not the fork pin, and it was not re-checked at the pinned head. Its code reading is taken from the audit evidence, not re-read at the pinned head, names no code path with file and line, and its reproduction section states not attempted in this record.

### SOLR-12659, verdict keep-open, draft branch solr-12659-draft-open

Claiming host: none. The record source is the queue entry audit field, round audit-1 dated 2026-10-04.

Missing:
- No claim in claims/ naming this key at all, so no closeout claim with a DONE line.
- The re-check required by steps 11 and 12: record.md states JIRA was not re-read, the audit was checked against upstream main c8ab71430f not the fork pin, and it was not re-checked at the pinned head. Its code reading is taken from the audit evidence, not re-read at the pinned head, names no code path with file and line, and its reproduction section states not attempted in this record.

### SOLR-12646, verdict close-obsolete, draft branch solr-12646-draft-close

Claiming host: none. The record says it was checked by vm2 from an earlier B2 reading dated 2026-10-19 UTC.

Missing:
- No claim in claims/ naming this key at all, so no closeout claim with a DONE line.
- The re-check required by steps 11 and 12: record.md states JIRA status was not recorded in the B2 reading and JIRA was not re-read for this record.
- The grep the close-obsolete verdict requires at the pinned head: record.md states the grep result is taken from the B2 reading and was not re-run in this record.

### SOLR-12645, verdict close-obsolete, draft branch solr-12645-draft-close

Claiming host: none. The record says it was checked by vm2 from an earlier B2 reading dated 2026-10-19 UTC.

Missing:
- No claim in claims/ naming this key at all, so no closeout claim with a DONE line.
- The re-check required by steps 11 and 12: record.md states JIRA status was not recorded in the B2 reading and JIRA was not re-read for this record.
- The grep the close-obsolete verdict requires at the pinned head: record.md states the grep result is taken from the B2 reading and was not re-run in this record.

### SOLR-12585, verdict keep-open, draft branch solr-12585-draft-open

Claiming host: none. The record says it was checked by vm2 from an earlier B2 reading dated 2026-10-19 UTC.

Missing:
- No claim in claims/ naming this key at all, so no closeout claim with a DONE line.
- The re-check required by steps 11 and 12: record.md states JIRA status was not recorded in the B2 reading and JIRA was not re-read for this record.
- A code path with file and line, or a reproduction attempt with a result: record.md names no file and line; its code reading states the ZooKeeper behavior is in the dependency, not this repo, and was not checked, and its reproduction section states only Not attempted.

### SOLR-12542, verdict keep-open, draft branch solr-12542-draft-open

Claiming host: none. The record source is the queue entry audit field, round audit-1 dated 2026-10-04.

Missing:
- No claim in claims/ naming this key at all, so no closeout claim with a DONE line.
- The re-check required by steps 11 and 12: record.md states JIRA was not re-read, the audit was checked against upstream main c8ab71430f not the fork pin, and it was not re-checked at the pinned head. Its code reading is taken from the audit evidence, not re-read at the pinned head, names no code path with file and line, and its reproduction section states not attempted in this record.

### SOLR-12504, verdict keep-open, draft branch solr-12504-draft-open

Claiming host: none. The record source is the queue entry audit field, round audit-1 dated 2026-10-04.

Missing:
- No claim in claims/ naming this key at all, so no closeout claim with a DONE line.
- The re-check required by steps 11 and 12: record.md states JIRA was not re-read, the audit was checked against upstream main c8ab71430f not the fork pin, and it was not re-checked at the pinned head. Its code reading is taken from the audit evidence, not re-read at the pinned head, names no code path with file and line, and its reproduction section states not attempted in this record.

### SOLR-12487, verdict keep-open, draft branch solr-12487-draft-open

Claiming host: none. The record source is the queue entry audit field, round audit-1 dated 2026-10-04.

Missing:
- No claim in claims/ naming this key at all, so no closeout claim with a DONE line.
- The re-check required by steps 11 and 12: record.md states JIRA was not re-read, the audit was checked against upstream main c8ab71430f not the fork pin, and it was not re-checked at the pinned head. Its code reading is taken from the audit evidence, not re-read at the pinned head, names no code path with file and line, and its reproduction section states not attempted in this record.

### SOLR-12460, verdict keep-open, draft branch solr-12460-draft-open

Claiming host: none. The record says it was checked by vm2 from an earlier C reading dated 2026-10-19 UTC.

Missing:
- No claim in claims/ naming this key at all, so no closeout claim with a DONE line.
- The re-check required by steps 11 and 12: record.md states JIRA status was not recorded in the C reading and JIRA was not re-read for this record.

### SOLR-12446, verdict keep-open, draft branch solr-12446-draft-open

Claiming host: none. The record says it was checked by vm2 from an earlier B2 reading dated 2026-10-19 UTC.

Missing:
- No claim in claims/ naming this key at all, so no closeout claim with a DONE line.
- The re-check required by steps 11 and 12: record.md states JIRA status was not recorded in the B2 reading and JIRA was not re-read for this record.

### SOLR-12369, verdict keep-open, draft branch solr-12369-draft-open

Claiming host: none. The record source is the earlier A2 report dated 2026-10-18.

Missing:
- No claim in claims/ naming this key at all, so no closeout claim with a DONE line.
- The re-check required by steps 11 and 12: record.md states JIRA was not re-read for this record and its code reading was not read for this record; its source is the earlier A2 report and the runs were not repeated. Its reproduction section states not attempted: no test is named in the description and nothing was run.

### SOLR-12363, verdict keep-open, draft branch solr-12363-draft-open

Claiming host: none. The record source is the queue entry audit field, round audit-1 dated 2026-10-04.

Missing:
- No claim in claims/ naming this key at all, so no closeout claim with a DONE line.
- The re-check required by steps 11 and 12: record.md states JIRA was not re-read, the audit was checked against upstream main c8ab71430f not the fork pin, and it was not re-checked at the pinned head. Its code reading is taken from the audit evidence, not re-read at the pinned head, names no code path with file and line, and its reproduction section states not attempted in this record.

### SOLR-12340, verdict keep-open, draft branch solr-12340-draft-open

Claiming host: none. The record source is the queue entry audit field, round audit-1 dated 2026-10-04.

Missing:
- No claim in claims/ naming this key at all, so no closeout claim with a DONE line.
- The re-check required by steps 11 and 12: record.md states JIRA was not re-read, the audit was checked against upstream main c8ab71430f not the fork pin, and it was not re-checked at the pinned head. Its code reading is taken from the audit evidence, not re-read at the pinned head, names no code path with file and line, and its reproduction section states not attempted in this record.

### SOLR-12315, verdict keep-open, draft branch solr-12315-draft-open

Claiming host: none. The record says it was checked by vm2 from an earlier C reading dated 2026-10-19 UTC.

Missing:
- No claim in claims/ naming this key at all, so no closeout claim with a DONE line.
- The re-check required by steps 11 and 12: record.md states JIRA status was not recorded in the C reading and JIRA was not re-read for this record.
- A code path with file and line, or a reproduction attempt with a result: record.md names no Solr code path, and its reproduction section states only Not attempted, with no attempt and no result.

### SOLR-12302, verdict keep-open, draft branch solr-12302-draft-open

Claiming host: none. The record source is the earlier A1 report dated 2026-10-18; the only claims file naming this key is a vm2 repro round claim, not a closeout claim.

Missing:
- No skip closeout claim in claims/: no claims/skip-SOLR-12302-<host>.md and no closeout claim with a DONE line. The only file in claims/ naming this key is claims/pool-skip-repro-flake-round-1.md (vm2), a repro round claim whose DONE line delivers the A1 report, not a closeout.
- The re-check required by steps 11 and 12: record.md states JIRA was not re-read for this record and its code reading was not read for this record; its source is the earlier A1 report and the runs were not repeated. The record does state the A1 run command, seed and result, so the missing piece is the re-check itself, not a reproduction statement.

### SOLR-12300, verdict keep-open, draft branch solr-12300-draft-open

Claiming host: none. The record says it was checked by vm2 from an earlier C reading dated 2026-10-19 UTC.

Missing:
- No claim in claims/ naming this key at all, so no closeout claim with a DONE line.
- The re-check required by steps 11 and 12: record.md states JIRA status was not recorded in the C reading and JIRA was not re-read for this record.

### SOLR-12296, verdict keep-open, draft branch solr-12296-draft-open

Claiming host: none. The record source is the earlier A3 report dated 2026-10-18.

Missing:
- No claim in claims/ naming this key at all, so no closeout claim with a DONE line.
- The re-check required by steps 11 and 12: record.md states JIRA was not re-read for this record and its code reading was not read for this record; its source is the earlier A3 report and the runs were not repeated. The record does state the A3 run command, seed and result, so the missing piece is the re-check itself, not a reproduction statement.

### SOLR-12268, verdict keep-open, draft branch solr-12268-draft-open

Claiming host: none. The record source is the queue entry audit field, round audit-1 dated 2026-10-04.

Missing:
- No claim in claims/ naming this key at all, so no closeout claim with a DONE line.
- The re-check required by steps 11 and 12: record.md states JIRA was not re-read, the audit was checked against upstream main c8ab71430f not the fork pin, and it was not re-checked at the pinned head. Its code reading is taken from the audit evidence, not re-read at the pinned head, names no code path with file and line, and its reproduction section states not attempted in this record.

### SOLR-12263, verdict keep-open, draft branch solr-12263-draft-open

Claiming host: none. The record says it was checked by vm2 from an earlier C reading dated 2026-10-19 UTC.

Missing:
- No claim in claims/ naming this key at all, so no closeout claim with a DONE line.
- The re-check required by steps 11 and 12: record.md states JIRA status was not recorded in the C reading and JIRA was not re-read for this record.

### SOLR-12262, verdict keep-open, draft branch solr-12262-draft-open

Claiming host: none. The record source is the queue entry audit field, round audit-1 dated 2026-10-04.

Missing:
- No claim in claims/ naming this key at all, so no closeout claim with a DONE line.
- The re-check required by steps 11 and 12: record.md states JIRA was not re-read, the audit was checked against upstream main c8ab71430f not the fork pin, and it was not re-checked at the pinned head. Its code reading is taken from the audit evidence, not re-read at the pinned head, names no code path with file and line, and its reproduction section states not attempted in this record.

## 2026-10-19 (twelfth run)

Scope: the next 20 auditable closeout records in file order in material/skip-audit/queue-current.json at coordination tip e48fa11863f33bd53c2f00779d2f35693f7bf015, pinned head 498a09aea97aac86c2cc7ac114113b946d249942. Priorities this run: no remediation re-audit was due (no claims/skip-<KEY>-<host>.md carries a DONE line naming a pool-aaa batch, and queue-current.json is unchanged between the previous audit tip a9a6ae3eed773510a996edeb6c649a65557cf33b and this tip, so no verdict, draft branch or evidence head changed), and no new or changed verdict was due for the same reason. Result: 0 pass, 20 suspect. Gate receipts: no new DONE gate tickets this run; every gates file whose Status line says DONE names a ticket already checked in an earlier run, so no gate receipt check was due and no gate receipt suspect was found. The pattern is unchanged from earlier passes: bulk closeouts without a claim or a per-ticket re-check.

For every suspect below, the draft branch named in queue-current.json exists on the fork and contains drafts/<KEY>/record.md and drafts/<KEY>/jira-comment.md, the record cites the pinned head that matches queue-current.json, the word count is over 40, and the record.md text is not identical to another ticket record in this batch. The verdict is a value the rubric allows and the draft branch suffix matches the verdict. The missing pieces are only the ones listed per ticket. Note on shared text: the jira-comment.md files are byte-identical across groups in this batch: SOLR-12236 and SOLR-11864 share one text; SOLR-12234, SOLR-12112, SOLR-12029, SOLR-11983, SOLR-11954, SOLR-11932, SOLR-11927 and SOLR-11815 share a second; SOLR-12166 and SOLR-11787 share a third; SOLR-12093 and SOLR-11826 share a fourth; SOLR-11858 and SOLR-11816 share a fifth; SOLR-11827 and SOLR-11814 share a sixth.

### SOLR-12236, verdict close-fixed, draft branch solr-12236-draft-close

Claiming host: none. The record says it was checked by vm2 from an earlier B3 reading dated 2026-10-19 UTC.

Missing:
- No claim in claims/ naming this key at all, so no closeout claim with a DONE line.
- The re-check required by steps 11 and 12: record.md states JIRA was not re-read for this record; status taken from the earlier B3 reading

### SOLR-12234, verdict keep-open, draft branch solr-12234-draft-open

Claiming host: none. The record source is the queue entry audit field, round audit-5 dated 2026-10-17.

Missing:
- No claim in claims/ naming this key at all, so no closeout claim with a DONE line.
- The re-check required by steps 11 and 12: record.md is a bulk audit-derived record: it states JIRA was not re-read for this record; the audit below was checked against upstream main c8ab71430f, not the fork pin, and was not re-checked at the pinned head for this record; its code reading is taken from the audit evidence, not re-read at the pinned head, names no code path with file and line, and its reproduction section states not attempted in this record

### SOLR-12220, verdict keep-open, draft branch solr-12220-draft-open

Claiming host: none. The record source is the earlier A2 report dated 2026-10-18; the only claims file naming this key is a vm5 flake locate round claim, not a closeout claim.

Missing:
- No skip closeout claim in claims/: no claims/skip-SOLR-12220-<host>.md and no closeout claim with a DONE line; the only claims/ file naming the key is claims/pool-skip-repro-flake-locate-round-1.md (vm5), a flake locate round claim whose DONE line delivers the A2 report, not a closeout.
- The re-check required by steps 11 and 12: record.md states JIRA was not re-read for this record; its code reading was not read for this record and the A2 runs were not repeated; the record does state the A2 run command, seed and result, so the missing piece is the re-check itself, not a reproduction statement

### SOLR-12166, verdict keep-open, draft branch solr-12166-draft-open

Claiming host: none. The record says it was checked by vm2 from an earlier B2 reading dated 2026-10-19 UTC.

Missing:
- No claim in claims/ naming this key at all, so no closeout claim with a DONE line.
- The re-check required by steps 11 and 12: record.md states JIRA status was not recorded in the B2 reading and JIRA was not re-read for this record

### SOLR-12112, verdict keep-open, draft branch solr-12112-draft-open

Claiming host: none. The record source is the queue entry audit field, round audit-1 dated 2026-10-04.

Missing:
- No claim in claims/ naming this key at all, so no closeout claim with a DONE line.
- The re-check required by steps 11 and 12: record.md is a bulk audit-derived record: it states JIRA was not re-read for this record; the audit below was checked against upstream main c8ab71430f, not the fork pin, and was not re-checked at the pinned head for this record; its code reading is taken from the audit evidence, not re-read at the pinned head, names no code path with file and line, and its reproduction section states not attempted in this record

### SOLR-12093, verdict keep-open, draft branch solr-12093-draft-open

Claiming host: none. The record source is the earlier A1 report dated 2026-10-18; the only claims file naming this key is a vm2 repro round claim, not a closeout claim.

Missing:
- No skip closeout claim in claims/: no claims/skip-SOLR-12093-<host>.md and no closeout claim with a DONE line; the only claims/ file naming the key is claims/pool-skip-repro-flake-round-1.md (vm2), a repro round claim whose DONE line delivers the A1 report, not a closeout.
- The re-check required by steps 11 and 12: record.md states JIRA was not re-read for this record and records no JIRA status or date; its code reading was not read for this record and the A1 runs were not repeated; the record does state the A1 run command, seed and result, so the missing piece is the re-check itself, not a reproduction statement

### SOLR-12029, verdict keep-open, draft branch solr-12029-draft-open

Claiming host: none. The record source is the queue entry audit field, round audit-1 dated 2026-10-04.

Missing:
- No claim in claims/ naming this key at all, so no closeout claim with a DONE line.
- The re-check required by steps 11 and 12: record.md is a bulk audit-derived record: it states JIRA was not re-read for this record; the audit below was checked against upstream main c8ab71430f, not the fork pin, and was not re-checked at the pinned head for this record; its code reading is taken from the audit evidence, not re-read at the pinned head, names no code path with file and line, and its reproduction section states not attempted in this record

### SOLR-11983, verdict keep-open, draft branch solr-11983-draft-open

Claiming host: none. The record source is the queue entry audit field, round audit-1 dated 2026-10-04.

Missing:
- No claim in claims/ naming this key at all, so no closeout claim with a DONE line.
- The re-check required by steps 11 and 12: record.md is a bulk audit-derived record: it states JIRA was not re-read for this record; the audit below was checked against upstream main c8ab71430f, not the fork pin, and was not re-checked at the pinned head for this record; its code reading is taken from the audit evidence, not re-read at the pinned head, names no code path with file and line, and its reproduction section states not attempted in this record

### SOLR-11955, verdict close-obsolete, draft branch solr-11955-draft-close

Claiming host: none. The record says it was checked by vm2 from an earlier B2 reading dated 2026-10-19 UTC.

Missing:
- No claim in claims/ naming this key at all, so no closeout claim with a DONE line.
- The re-check required by steps 11 and 12: record.md states JIRA status was not recorded in the B2 reading and JIRA was not re-read for this record
- The grep the close-obsolete verdict requires at the pinned head: record.md states the grep result is taken from the B2 reading and was not re-run in this record

### SOLR-11954, verdict keep-open, draft branch solr-11954-draft-open

Claiming host: none. The record source is the queue entry audit field, round audit-1 dated 2026-10-04.

Missing:
- No claim in claims/ naming this key at all, so no closeout claim with a DONE line.
- The re-check required by steps 11 and 12: record.md is a bulk audit-derived record: it states JIRA was not re-read for this record; the audit below was checked against upstream main c8ab71430f, not the fork pin, and was not re-checked at the pinned head for this record; its code reading is taken from the audit evidence, not re-read at the pinned head, names no code path with file and line, and its reproduction section states not attempted in this record

### SOLR-11932, verdict keep-open, draft branch solr-11932-draft-open

Claiming host: none. The record source is the queue entry audit field, round audit-3 dated 2026-10-05.

Missing:
- No claim in claims/ naming this key at all, so no closeout claim with a DONE line.
- The re-check required by steps 11 and 12: record.md is a bulk audit-derived record: it states JIRA was not re-read for this record; the audit below was checked against upstream main c8ab71430f, not the fork pin, and was not re-checked at the pinned head for this record; its code reading is taken from the audit evidence, not re-read at the pinned head, names no code path with file and line, and its reproduction section states not attempted in this record

### SOLR-11927, verdict keep-open, draft branch solr-11927-draft-open

Claiming host: none. The record source is the queue entry audit field, round audit-3 dated 2026-10-05.

Missing:
- No claim in claims/ naming this key at all, so no closeout claim with a DONE line.
- The re-check required by steps 11 and 12: record.md is a bulk audit-derived record: it states JIRA was not re-read for this record; the audit below was checked against upstream main c8ab71430f, not the fork pin, and was not re-checked at the pinned head for this record; its code reading is taken from the audit evidence, not re-read at the pinned head, names no code path with file and line, and its reproduction section states not attempted in this record

### SOLR-11864, verdict close-fixed, draft branch solr-11864-draft-close

Claiming host: none. The record says it was checked by vm2 from an earlier B3 reading dated 2026-10-19 UTC.

Missing:
- No claim in claims/ naming this key at all, so no closeout claim with a DONE line.
- The re-check required by steps 11 and 12: record.md states JIRA was not re-read for this record; status taken from the earlier B3 reading

### SOLR-11858, verdict keep-open, draft branch solr-11858-draft-open

Claiming host: none. The record says it was checked by vm2 from an earlier C reading dated 2026-10-19 UTC.

Missing:
- No claim in claims/ naming this key at all, so no closeout claim with a DONE line.
- The re-check required by steps 11 and 12: record.md states JIRA status was not recorded in the C reading and JIRA was not re-read for this record

### SOLR-11827, verdict keep-open, draft branch solr-11827-draft-open

Claiming host: none. The record says it was checked by vm2 from an earlier C reading dated 2026-10-19 UTC.

Missing:
- No claim in claims/ naming this key at all, so no closeout claim with a DONE line.
- The re-check required by steps 11 and 12: record.md states JIRA status was not recorded in the C reading and JIRA was not re-read for this record

### SOLR-11826, verdict keep-open, draft branch solr-11826-draft-open

Claiming host: none. The record source is the earlier A1 report dated 2026-10-18; the only claims file naming this key is a vm2 repro round claim, not a closeout claim.

Missing:
- No skip closeout claim in claims/: no claims/skip-SOLR-11826-<host>.md and no closeout claim with a DONE line; the only claims/ file naming the key is claims/pool-skip-repro-flake-round-1.md (vm2), a repro round claim whose DONE line delivers the A1 report, not a closeout.
- The re-check required by steps 11 and 12: record.md states JIRA was not re-read for this record and records no JIRA status or date; its code reading was not read for this record and the A1 runs were not repeated; the record does state the A1 run command, seed and result, so the missing piece is the re-check itself, not a reproduction statement

### SOLR-11816, verdict keep-open, draft branch solr-11816-draft-open

Claiming host: none. The record says it was checked by vm2 from an earlier C reading dated 2026-10-19 UTC.

Missing:
- No claim in claims/ naming this key at all, so no closeout claim with a DONE line.
- The re-check required by steps 11 and 12: record.md states JIRA status was not recorded in the C reading and JIRA was not re-read for this record
- A code path with file and line, or a reproduction attempt with a result: record.md names no code path with file and line (it states no class, method or config is named and there is no code path to check at this head) and its reproduction section states only Not attempted, with no attempt or result

### SOLR-11815, verdict keep-open, draft branch solr-11815-draft-open

Claiming host: none. The record source is the queue entry audit field, round audit-3 dated 2026-10-05.

Missing:
- No claim in claims/ naming this key at all, so no closeout claim with a DONE line.
- The re-check required by steps 11 and 12: record.md is a bulk audit-derived record: it states JIRA was not re-read for this record; the audit below was checked against upstream main c8ab71430f, not the fork pin, and was not re-checked at the pinned head for this record; its code reading is taken from the audit evidence, not re-read at the pinned head, names no code path with file and line, and its reproduction section states not attempted in this record

### SOLR-11814, verdict keep-open, draft branch solr-11814-draft-open

Claiming host: none. The record says it was checked by vm2 from an earlier C reading dated 2026-10-19 UTC.

Missing:
- No claim in claims/ naming this key at all, so no closeout claim with a DONE line.
- The re-check required by steps 11 and 12: record.md states JIRA status was not recorded in the C reading and JIRA was not re-read for this record

### SOLR-11787, verdict keep-open, draft branch solr-11787-draft-open

Claiming host: none. The record says it was checked by vm2 from an earlier B2 reading dated 2026-10-19 UTC.

Missing:
- No claim in claims/ naming this key at all, so no closeout claim with a DONE line.
- The re-check required by steps 11 and 12: record.md states JIRA status was not recorded in the B2 reading and JIRA was not re-read for this record
- A code path with file and line, or a reproduction attempt with a result: record.md names no code path with file and line; its reproduction section states only Not attempted, with no attempt or result

## 2026-10-19 (thirteenth run)

Scope: coordination tip 5c0a2b54cd99f4bf8b6f8caeeda0a5c8da5d230d, pinned head 498a09aea97aac86c2cc7ac114113b946d249942. Priorities this run: remediation re-audits first. Batch 01 items 1 to 7 landed since their audits with claims/skip-<KEY>-vm4.md DONE lines naming the batch: SOLR-17820, SOLR-17763, SOLR-17751, SOLR-17610, SOLR-17509, SOLR-17376 and SOLR-17259. All 7 were re-verified in full and pass: each claim carries a DONE line naming batch 01, each named draft branch exists on the fork and contains drafts/<KEY>/record.md and drafts/<KEY>/jira-comment.md, each record cites the pinned head matching queue-current.json, names the Jira status read on 2026-10-19 in that pass, and gives code paths with file and line plus the missing piece where the verdict is keep-open; each verdict is rubric-legal and its branch suffix matches. They are cleared in material/skip-audit/remediation-current.json. SOLR-17820 changed verdict from keep-open to pr-needed on the new branch solr-17820-draft-pr as part of its remediation; that is also the only queue-current.json verdict, draft branch or evidence head change between the previous audit tip e48fa11863f33bd53c2f00779d2f35693f7bf015 and this tip, so no separate new or changed verdict audit was due. One note on SOLR-17376: its fork tip is 04dbe81865f, one check-window correction commit beyond the head its claim states (2dedf0a7a2, a valid object and an ancestor of the tip); the record was verified at the tip. The next 13 auditable closeout records in file order in material/skip-audit/queue-current.json were then audited from the backlog. Result for the run: 20 audited, 7 pass (all remediation re-audits), 13 suspect (all backlog). Gate receipts: no new DONE gate tickets this run; every gates file whose Status line says DONE names a ticket already checked in an earlier run, so no gate receipt check was due and no gate receipt suspect was found. The backlog pattern is unchanged from earlier passes: bulk closeouts without a claim or a per-ticket re-check.

For every suspect below, the draft branch named in queue-current.json exists on the fork and contains drafts/<KEY>/record.md and drafts/<KEY>/jira-comment.md, the record cites the pinned head that matches queue-current.json, the word count is over 40, and the record.md text is not identical to another ticket record in this batch. The verdict is a value the rubric allows and the draft branch suffix matches the verdict, except for the SOLR-11688 verdict fit listed for that ticket. The missing pieces are only the ones listed per ticket. Note on shared text: the jira-comment.md files are byte-identical across groups in this batch: SOLR-11776 and SOLR-11659 share one text; SOLR-11768 and SOLR-11672 share a second; SOLR-11759, SOLR-11712, SOLR-11675, SOLR-11655, SOLR-11583 and SOLR-11577 share a third; SOLR-11627 shares its text with SOLR-11563, which was not audited this run.

### SOLR-11776, verdict close-obsolete, draft branch solr-11776-draft-close

Claiming host: none. The record says it was checked by vm2 from an earlier B2 reading dated 2026-10-19 UTC.

Missing:
- No claim in claims/ naming this key at all, so no closeout claim with a DONE line.
- The re-check required by steps 11 and 12: record.md states JIRA status was not recorded in the B2 reading and JIRA was not re-read for this record
- The grep the close-obsolete verdict requires at the pinned head: record.md states the grep result is taken from the B2 reading and was not re-run in this record

### SOLR-11768, verdict keep-open, draft branch solr-11768-draft-open

Claiming host: none. The record says it was checked by vm2 from an earlier C reading dated 2026-10-19 UTC.

Missing:
- No claim in claims/ naming this key at all, so no closeout claim with a DONE line.
- The re-check required by steps 11 and 12: record.md states JIRA status was not recorded in the C reading and JIRA was not re-read for this record

### SOLR-11764, verdict close-answered, draft branch solr-11764-draft-close

Claiming host: none. The record says it was checked by vm2 from an earlier B3 reading dated 2026-10-19 UTC.

Missing:
- No claim in claims/ naming this key at all, so no closeout claim with a DONE line.
- The re-check required by steps 11 and 12: record.md states JIRA was not re-read for this record; status taken from the earlier B3 reading

### SOLR-11759, verdict keep-open, draft branch solr-11759-draft-open

Claiming host: none. The record source is the queue entry audit field, round audit-3 dated 2026-10-06.

Missing:
- No claim in claims/ naming this key at all, so no closeout claim with a DONE line.
- The re-check required by steps 11 and 12: record.md is a bulk audit-derived record: it states JIRA was not re-read, the audit was checked against upstream main c8ab71430f not the fork pin, and it was not re-checked at the pinned head; its code reading is taken from the audit evidence, not re-read at the pinned head, names no code path with file and line, and its reproduction section states not attempted in this record

### SOLR-11712, verdict keep-open, draft branch solr-11712-draft-open

Claiming host: none. The record source is the queue entry audit field, round audit-3 dated 2026-10-05.

Missing:
- No claim in claims/ naming this key at all, so no closeout claim with a DONE line.
- The re-check required by steps 11 and 12: record.md is a bulk audit-derived record: it states JIRA was not re-read, the audit was checked against upstream main c8ab71430f not the fork pin, and it was not re-checked at the pinned head; its code reading is taken from the audit evidence, not re-read at the pinned head, names no code path with file and line, and its reproduction section states not attempted in this record

### SOLR-11688, verdict close-fixed, draft branch solr-11688-draft-close

Claiming host: none. The record says it was checked by vm5 on 2026-10-18, with the Jira status checked the same day.

Missing:
- No claim in claims/ naming this key at all, so no closeout claim with a DONE line.
- The verdict fit: close-fixed needs a fixing commit whose changed code is still present at the pinned head, but record.md cites removal commit cb6dd31eca (SOLR-17932) and an empty grep for TransientSolrCoreCacheDefault, and its gap analysis states the rubric's close-obsolete fits this case

### SOLR-11675, verdict keep-open, draft branch solr-11675-draft-open

Claiming host: none. The record source is the queue entry audit field, round audit-3 dated 2026-10-06.

Missing:
- No claim in claims/ naming this key at all, so no closeout claim with a DONE line.
- The re-check required by steps 11 and 12: record.md is a bulk audit-derived record: it states JIRA was not re-read, the audit was checked against upstream main c8ab71430f not the fork pin, and it was not re-checked at the pinned head; its code reading is taken from the audit evidence, not re-read at the pinned head, names no code path with file and line, and its reproduction section states not attempted in this record

### SOLR-11672, verdict keep-open, draft branch solr-11672-draft-open

Claiming host: none. The record says it was checked by vm2 from an earlier C reading dated 2026-10-19 UTC.

Missing:
- No claim in claims/ naming this key at all, so no closeout claim with a DONE line.
- The re-check required by steps 11 and 12: record.md states JIRA status was not recorded in the C reading and JIRA was not re-read for this record

### SOLR-11659, verdict close-obsolete, draft branch solr-11659-draft-close

Claiming host: none. The record says it was checked by vm2 from an earlier B2 reading dated 2026-10-19 UTC.

Missing:
- No claim in claims/ naming this key at all, so no closeout claim with a DONE line.
- The re-check required by steps 11 and 12: record.md states JIRA status was not recorded in the B2 reading and JIRA was not re-read for this record
- The grep the close-obsolete verdict requires at the pinned head: record.md states the grep result is taken from the B2 reading and was not re-run in this record

### SOLR-11655, verdict keep-open, draft branch solr-11655-draft-open

Claiming host: none. The record source is the queue entry audit field, round audit-5 dated 2026-10-17.

Missing:
- No claim in claims/ naming this key at all, so no closeout claim with a DONE line.
- The re-check required by steps 11 and 12: record.md is a bulk audit-derived record: it states JIRA was not re-read, the audit was checked against upstream main c8ab71430f not the fork pin, and it was not re-checked at the pinned head; its code reading is taken from the audit evidence, not re-read at the pinned head, names no code path with file and line, and its reproduction section states not attempted in this record

### SOLR-11627, verdict keep-open, draft branch solr-11627-draft-open

Claiming host: none. The record source is the earlier A1 report dated 2026-10-18; the only claims file naming this key is a vm2 repro round claim, not a closeout claim.

Missing:
- No skip closeout claim in claims/: no claims/skip-SOLR-11627-<host>.md and no closeout claim with a DONE line; the only claims/ file naming the key is claims/pool-skip-repro-flake-round-1.md (vm2), a repro round claim whose DONE line delivers the A1 report, not a closeout.
- The re-check required by steps 11 and 12: record.md states JIRA was not re-read for this record and records no JIRA status or date; its code reading was not read for this record and the A1 runs were not repeated; the record does state the A1 run command, seed and result, so the missing piece is the re-check itself, not a reproduction statement

### SOLR-11583, verdict keep-open, draft branch solr-11583-draft-open

Claiming host: none. The record source is the queue entry audit field, round audit-3 dated 2026-10-05.

Missing:
- No claim in claims/ naming this key at all, so no closeout claim with a DONE line.
- The re-check required by steps 11 and 12: record.md is a bulk audit-derived record: it states JIRA was not re-read, the audit was checked against upstream main c8ab71430f not the fork pin, and it was not re-checked at the pinned head; its code reading is taken from the audit evidence, not re-read at the pinned head, names no code path with file and line, and its reproduction section states not attempted in this record

### SOLR-11577, verdict keep-open, draft branch solr-11577-draft-open

Claiming host: none. The record source is the queue entry audit field, round audit-3 dated 2026-10-05.

Missing:
- No claim in claims/ naming this key at all, so no closeout claim with a DONE line.
- The re-check required by steps 11 and 12: record.md is a bulk audit-derived record: it states JIRA was not re-read, the audit was checked against upstream main c8ab71430f not the fork pin, and it was not re-checked at the pinned head; its code reading is taken from the audit evidence, not re-read at the pinned head, names no code path with file and line, and its reproduction section states not attempted in this record

## 2026-10-19 remediation re-audit, batch 01 redos (vm3)

Re-audited by vm3 at each redo head, pinned head 498a09aea97aac86c2cc7ac114113b946d249942. Each item below failed the rubric floor and stays open. Claim: `claims/pool-vm3-remediation-reaudit-batch-01.md`.

### SOLR-16785, verdict keep-open, draft branch solr-16785-draft-open

Claiming host: vm4, `claims/skip-SOLR-16785-vm4.md` (DONE 2026-10-19T14:02:41Z). Redo head cb55a58539.

Missing:
- No file:line code citation at the pin and no reproduction attempt. The record says "Not done at the code level" and "Not attempted".
- Needed: at least one code path for the core load or recovery failure, cited with file and line at 498a09aea97a, or a reproduction attempt with its command and result.

### SOLR-16758, verdict keep-open, draft branch solr-16758-draft-open

Claiming host: vm4, `claims/skip-SOLR-16758-vm4.md` (DONE 2026-10-19T14:43:28Z). Redo head 0f30dd2dfb.

Missing:
- No reproduction or benchmark on the pin. The record calls the pass review-only, but the ticket is a measured latency claim.
- SOLR-14185 is "not traced in this pass", yet commit `bec4ed890b` (DocSet.iterator(LeafReaderContext)) is at the pin and is the change the reporter's revert undoes. The central claim is untested against that commit.
- Needed: trace `bec4ed890b` against the reporter's revert, and run at least one repeatable cache=false benchmark on the pin with command, data size and log path, or state why it cannot run.

### SOLR-16657, verdict keep-open, draft branch solr-16657-draft-open

Claiming host: vm4, `claims/skip-SOLR-16657-vm4.md` (DONE 2026-10-19T14:47:46Z). Redo head c4c69b7187.

Missing:
- The code reading confirms the staleness (`solr/webapp/web/js/angular/app.js:527-529, 557-559`, verified at the pin). The open question is an owner decision, and the record says the browser reproduction was "Not attempted". A decision is not evidence.
- Needed: an Admin UI reproduction with command, result and log path, or a pr-needed verdict that names the test to add.

### SOLR-16652, verdict keep-open, draft branch solr-16652-draft-open

Claiming host: vm4, `claims/skip-SOLR-16652-vm4.md` (DONE 2026-10-19T14:52:50Z). Redo head d567d818b9.

Missing:
- The ticket gives its own reproduction steps (the `_default` configset, the synonym rule, four documents, two queries). The record says "Not attempted" and calls the pass review-only.
- Needed: run the ticket's steps at the pin and record the command, result and log path.

### SOLR-16792, verdict keep-open, draft branch solr-16792-draft-open

Claiming host: vm4, `claims/skip-SOLR-16792-vm4.md` (DONE 2026-10-19T14:58:05Z). Redo head 94e304ab68.

Missing:
- The candidate-site analysis is wrong. The record says the only dereference a null could reach is `CoreStatus.java:146-150`. At the pin, `solr/core/src/java/org/apache/solr/handler/admin/api/CoreStatus.java:139` has the same unguarded `core.getInstancePath().toString()` on the same loaded branch, and that is the frame in the ticket's NPE. The cloud-descriptor chains at lines 147-148 and 155-159 are also candidates, since the core descriptor leaves the cloud descriptor null for non-cloud cores.
- No reproduction attempt.
- Needed: correct the candidate-site analysis to name line 139 and the null cloud-descriptor path, attempt the reproduction or state why not, and fix the public JIRA draft, which repeats the wrong analysis.

### SOLR-16305, verdict keep-open, draft branch solr-16305-draft-open

Claiming host: vm4, `claims/skip-SOLR-16305-vm4.md` (DONE 2026-10-19T15:11:49Z). Redo head d32e52a658.

Missing:
- The record cites `CollectionsHandler.java:809` as the modify path. That line is inside `ADDREPLICAPROP_OP` (lines 800-812), not the modify path.
- Needed: cite the real modify path (`MODIFYCOLLECTION_OP` at `CollectionsHandler.java:848`, and `CollectionMutator.java:156-164`, which writes the `property.*` keys to collection state), and the grep behind "the modify path does not rewrite core.properties".

## 2026-10-19 remediation re-audit, batch 01 open items (vm5, second look)

Independent check of the six batch 01 items still open after vm3's re-audit above. Pinned head `498a09aea97aac86c2cc7ac114113b946d249942`. This check cleared nothing.

- SOLR-16305: agree with vm3. Verified at the pin: `CollectionsHandler.java:800-812` is `ADDREPLICAPROP_OP`; the modify path is `MODIFYCOLLECTION_OP` at `:848`, and `CollectionMutator.java:156-166` writes the `property.*` keys to collection state. The record's citation is wrong.
- SOLR-16792: agree with vm3. Verified at the pin: `CoreStatus.java:139` (`core.getInstancePath().toString()`) is unguarded on the same loaded-core branch as the ticket's frame, and the record does not name it.
- SOLR-16785: agree with vm3. No file-and-line code path and no attempt.
- SOLR-16758, SOLR-16657, SOLR-16652: vm3 requires a run for keep-open. Each redo names a file-and-line code path (`GraphQuery.java:117-200`; `app.js:527-529, 557-559`; `edismax-query-parser.adoc:40-44`) and names the missing piece, but runs nothing. Under vm3's stricter reading they stay open, as recorded above. The owner should settle which reading the keep-open minimum means; the same question applies to batch 21 (see that report's notes).
