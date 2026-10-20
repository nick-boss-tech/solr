# Pool assignment: cross-audit of the deferred-own-host items (aab re-audit round 1)

Status: OPEN

- Posted by: the phone agent, 2026-10-19, during the owner’s factory pause, from the 21 delivered reports in `reports/skip-verdict-audit-batch-*.md`.
- Background: the cross-audit round delivered all 21 batch reports (506 items: 5 pass, 334 suspect, 167 deferred-own-host). The 167 deferred items were never audited: the host that claimed their batch was also the checker named in the underlying reading, so it deferred them instead of passing its own work. They are listed below by source batch, 167 items in 13 slices. This assignment makes them claimable by the hosts that may audit them.
- Who may claim: a slice is claimable by any Linux host EXCEPT the host named in that slice’s exclusion line. First post wins, one claim file per slice: `claims/pool-aab-deferred-reaudit-batch-<NN>.md`.
- Method: the same floor as the original audit (`material/skip-audit/verdict-rubric.md` and tick step 12a): a closeout claim naming the ticket with a DONE line; the Jira status read in this pass; a code path with file and line or a reproduction result; the record’s pinned head matching the branch head.
- Deliverable per slice: `reports/skip-verdict-audit-batch-<NN>-reaudit.md` with a per-item pass or suspect verdict and the missing floor items named. An item the re-audit marks suspect is added to `material/skip-audit/remediation-current.json` as open in the same push (these items are not in that file yet; they were deferred, not suspected). A pass needs no other record.
- Limits: no Jira or GitHub posts, no PRs, no force push. Do not re-audit an item whose underlying reading names your own host as checker; defer it in the report and say so.

## Slices

### Slice batch 02 (3 items, from reports/skip-verdict-audit-batch-02.md)

Excluded host: vm3 (its readings and its audit). Claimable by vm2, vm4 or vm5.

Items: SOLR-11378, SOLR-11246, SOLR-11009.

### Slice batch 04 (14 items, from reports/skip-verdict-audit-batch-04.md)

Excluded host: vm2 (the checker named in the underlying readings). Claimable by vm3, vm4 or vm5.

Items: SOLR-10511, SOLR-10497, SOLR-10407, SOLR-10395, SOLR-10334, SOLR-10326, SOLR-10325, SOLR-10242, SOLR-10201, SOLR-10110, SOLR-10096, SOLR-10041, SOLR-10022, SOLR-10010.

### Slice batch 05 (20 items, from reports/skip-verdict-audit-batch-05.md)

Excluded host: vm2 (the checker named in the underlying readings). Claimable by vm3, vm4 or vm5.

Items: SOLR-9970, SOLR-9965, SOLR-9942, SOLR-9925, SOLR-9913, SOLR-9872, SOLR-9871, SOLR-9866, SOLR-9830, SOLR-9826, SOLR-9825, SOLR-9809, SOLR-9791, SOLR-9769, SOLR-9754, SOLR-9749, SOLR-9741, SOLR-9723, SOLR-9713, SOLR-9677.

### Slice batch 07 (15 items, from reports/skip-verdict-audit-batch-07.md)

Excluded host: vm2 (the checker named in the underlying readings). Claimable by vm3, vm4 or vm5.

Items: SOLR-9328, SOLR-9312, SOLR-9267, SOLR-9250, SOLR-9248, SOLR-9213, SOLR-9208, SOLR-9206, SOLR-9183, SOLR-9173, SOLR-9144, SOLR-9126, SOLR-9122, SOLR-9094, SOLR-9089.

### Slice batch 09 (8 items, from reports/skip-verdict-audit-batch-09.md)

Excluded host: vm2 (the checker named in the underlying readings). Claimable by vm3, vm4 or vm5.

Items: SOLR-8681, SOLR-8635, SOLR-8623, SOLR-8572, SOLR-8469, SOLR-8463, SOLR-8365, SOLR-8310.

### Slice batch 10 (19 items, from reports/skip-verdict-audit-batch-10.md)

Excluded host: vm2 (the checker named in the underlying readings). Claimable by vm3, vm4 or vm5.

Items: SOLR-8171, SOLR-8158, SOLR-8117, SOLR-8106, SOLR-8098, SOLR-8089, SOLR-8087, SOLR-8079, SOLR-8076, SOLR-8024, SOLR-7998, SOLR-7992, SOLR-7982, SOLR-7973, SOLR-7953, SOLR-7947, SOLR-7867, SOLR-7846, SOLR-7806.

### Slice batch 12 (11 items, from reports/skip-verdict-audit-batch-12.md)

Excluded host: vm2 (the checker named in the underlying readings). Claimable by vm3, vm4 or vm5.

Items: SOLR-7249, SOLR-7221, SOLR-7192, SOLR-7153, SOLR-7088, SOLR-7009, SOLR-6934, SOLR-6929, SOLR-6922, SOLR-6903, SOLR-6890.

### Slice batch 13 (13 items, from reports/skip-verdict-audit-batch-13.md)

Excluded host: vm2 (the checker named in the underlying readings). Claimable by vm3, vm4 or vm5.

Items: SOLR-6875, SOLR-6836, SOLR-6828, SOLR-6822, SOLR-6811, SOLR-6782, SOLR-6756, SOLR-6712, SOLR-6678, SOLR-6588, SOLR-6582, SOLR-6561, SOLR-6534.

### Slice batch 14 (16 items, from reports/skip-verdict-audit-batch-14.md)

Excluded host: vm2 (the checker named in the underlying readings). Claimable by vm3, vm4 or vm5.

Items: SOLR-6525, SOLR-6468, SOLR-6429, SOLR-6395, SOLR-6366, SOLR-6334, SOLR-6303, SOLR-6287, SOLR-6255, SOLR-6184, SOLR-6166, SOLR-6142, SOLR-5995, SOLR-5990, SOLR-5919, SOLR-5878.

### Slice batch 15 (16 items, from reports/skip-verdict-audit-batch-15.md)

Excluded host: vm2 (the checker named in the underlying readings). Claimable by vm3, vm4 or vm5.

Items: SOLR-5850, SOLR-5845, SOLR-5833, SOLR-5793, SOLR-5792, SOLR-5791, SOLR-5788, SOLR-5772, SOLR-5759, SOLR-5736, SOLR-5717, SOLR-5712, SOLR-5579, SOLR-5566, SOLR-5559, SOLR-5508.

### Slice batch 16 (8 items, from reports/skip-verdict-audit-batch-16.md)

Excluded host: vm2 (the checker named in the underlying readings). Claimable by vm3, vm4 or vm5.

Items: SOLR-5435, SOLR-5383, SOLR-5376, SOLR-5252, SOLR-5046, SOLR-4851, SOLR-4733, SOLR-4686.

### Slice batch 19 (12 items, from reports/skip-verdict-audit-batch-19.md)

Excluded host: vm2 (the checker named in the underlying readings). Claimable by vm3, vm4 or vm5.

Items: SOLR-3742, SOLR-3741, SOLR-3740, SOLR-3739, SOLR-3721, SOLR-3716, SOLR-3692, SOLR-3605, SOLR-3576, SOLR-3523, SOLR-3452, SOLR-3380.

### Slice batch 20 (12 items, from reports/skip-verdict-audit-batch-20.md)

Excluded host: vm2 (the checker named in the underlying readings). Claimable by vm3, vm4 or vm5.

Items: SOLR-3201, SOLR-3124, SOLR-3064, SOLR-3050, SOLR-3034, SOLR-3027, SOLR-3023, SOLR-2976, SOLR-2865, SOLR-2845, SOLR-2751, SOLR-2507.
