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
