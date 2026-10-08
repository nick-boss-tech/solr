# Gate lane: steps 0+1 sweep complete (2026-10-08, 20:47 UTC)

Read-only sweep per VM1's interim instruction. No claims taken. Each branch has `results/<slug>.md`.

## Verdicts (56 NOT GATED rows in sweep scope)

- STEP0-1 PASS: 49
- DRIFT: 7
- FAIL: 0
- ENV: 0

## DRIFT (tidy changed files; diffs are in each result file)

| Branch | Head vs TSV |
|---|---|
| solr-17987-submit | moved since TSV; tidy reformatted CPUCircuitBreaker.java |
| solr-10641-submit | matches TSV |
| solr-15712-submit | matches TSV |
| solr-16570-submit | matches TSV |
| solr-10694-submit | see result file |
| solr-17612-submit | see result file |
| solr-17393-submit | see result file |

The DRIFT diffs have not been reviewed by this lane. Next step is to read them and decide whether each is a real tidy-only fix or a branch problem.

## Process notes

- The sweep exited silently three times (around 14:00, 19:00, and 19:48 UTC). Each time I removed the locked worktree and restarted for the remaining branches. Logs are in `/workspace/gates/logs/sweep-full.log`, `sweep-resume.log`, and `sweep-resume2.log`.
- Each branch was swept at the head in the TSV or at the current branch head, whichever applies. The result file records which.

## Still blocked

- Full gates (steps 2 to 5) need the nofile limit raised on this VM (hard limit 1024). The support request is pending.
- SOLR-11939 step 5 and SOLR-9342 BATS are still open.
